package com.play.robot.util.udp;

import android.os.Bundle;
import android.os.Message;

import com.play.robot.util.LogUtil;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.ConnectIpEvent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wujn on 2019/2/15.
 * Version : v1.0
 * Function: udp client 64k限制
 */
public class UdpClient {
    /**
     * single instance UdpClient
     */

    public static final int TIME_CALL = 3 * 1000;//报警时间
    public static final int TIME_OUT = 12 * 1000;//超时时间

    public UdpClient() {
        setOnDataReceiveListener();
    }

    private DatagramSocket mSocket;
    private DatagramPacket sendPacket;    //发送
    private DatagramPacket receivePacket; //接受//  private OutputStream mOutputStream;//  private InputStream mInputStream;

    private SocketThread mSocketThread;
    private boolean isStop = false;//thread flag

    private class SocketThread extends Thread {
        private String ip;
        private int port;

        public SocketThread(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            super.run();
            try {
                if (mSocket != null) {
                    mSocket.close();
                    mSocket = null;
                }

                InetAddress ipAddress = InetAddress.getByName(ip);
                mSocket = new DatagramSocket();
                mSocket.connect(ipAddress, port); //连接

                //设置timeout
                mSocket.setSoTimeout(TIME_OUT);

                LogUtil.e("udp connect = " + isConnect());

                //此处这样做没什么意义不大，真正的socket未连接还是靠心跳发送，等待服务端回应比较好，一段时间内未回应，则socket未连接成功
                if (isConnect()) {
                    isStop = false;
//                    onConnectSuccess();
                } else {
                    onConnectFail();
                    return;
                }

            } catch (IOException e) {
                onConnectFail();
                LogUtil.e("SocketThread connect io exception = " + e.getMessage());
                e.printStackTrace();
                return;
            }

            //send //发送一次，否则不发送则收不到，不知道为啥。。。
            sendByteCmd(new byte[]{00}, -1);//send once

            //read ...
            while (isConnect() && !isStop && !isInterrupted()) {
                int size;
                try {
                    byte[] preBuffer = new byte[4 * 1024];//预存buffer
                    receivePacket = new DatagramPacket(preBuffer, preBuffer.length);
                    mSocket.receive(receivePacket);
                    if (receivePacket.getData() == null) return;
                    size = receivePacket.getLength();     //此为获取后的有效长度，一次最多读64k，预存小的话可能分包
                    LogUtil.e("pre data size = " + receivePacket.getData().length + ", value data size = " + size);
                    byte[] dataBuffer = Arrays.copyOf(preBuffer, size);
                    if (size > 0) {
                        Message msg = new Message();

                        Bundle bundle = new Bundle();
                        bundle.putByteArray("data", dataBuffer);
                        bundle.putInt("size", size);
                        bundle.putInt("requestCode", requestCode);
                        msg.setData(bundle);

                        onDataReceive(msg);

                        onConnectSuccess();
                    }
                    LogUtil.e("SocketThread read listening");                    //Thread.sleep(100);//log eof
                } catch (IOException e) {
                    onConnectFail();
                    LogUtil.e("SocketThread read io exception = " + e.getMessage());
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    //==============================socket connect============================

    private String ip;
    private int port;
    private Timer mTimer;

    /**
     * connect socket in thread
     * Exception : android.os.NetworkOnMainThreadException
     */
    public void connect(String ip, int port) {
        this.ip = ip;
        this.port = port;
        mSocketThread = new SocketThread(ip, port);
        mSocketThread.start();

        mTimer = new Timer();
        mTimer.schedule(new SendThread(),TIME_CALL,TIME_CALL);

    }

    /**
     * socket is connect
     */
    public boolean isConnect() {
        boolean flag = false;
        if (mSocket != null) {
            flag = mSocket.isConnected();
        }
        return flag;
    }

    /**
     * socket disconnect
     */
    public void disconnect() {
        isStop = true;
        if (mSocket != null) {
            mSocket.close();
            mSocket = null;
        }
        if (mSocketThread != null) {
            mSocketThread.interrupt();
        }

        if(mTimer != null){
            mTimer.cancel();
        }
    }

    /**
     * send byte[] cmd
     * Exception : android.os.NetworkOnMainThreadException
     */
    public void sendByteCmd(final byte[] mBuffer, int requestCode) {
        this.requestCode = requestCode;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress ipAddress = InetAddress.getByName(ip);
                    sendPacket = new DatagramPacket(mBuffer, mBuffer.length, ipAddress, port);
                    mSocket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class SendThread extends TimerTask {

        public SendThread() {
        }

        @Override
        public void run() {
            try {
                InetAddress ipAddress = InetAddress.getByName(ip);
                byte[] mBuffer = new byte[]{00};
                sendPacket = new DatagramPacket(mBuffer, mBuffer.length, ipAddress, port);
                mSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onConnectFail() {
        if (null != onDataReceiveListener) {
            onDataReceiveListener.onConnectFail();
            disconnect();
        }

    }

    boolean isSuccess;

    public void onConnectSuccess() {
        if(isSuccess) return;
        if (null != onDataReceiveListener) {
            onDataReceiveListener.onConnectSuccess();
            isSuccess = true;
        }

    }

    public void onDataReceive(Message msg) {
        Bundle bundle = msg.getData();
        byte[] buffer = bundle.getByteArray("data");
        int size = bundle.getInt("size");
        int mequestCode = bundle.getInt("requestCode");
        if (null != onDataReceiveListener) {
            onDataReceiveListener.onDataReceive(buffer, size, mequestCode);
        }
    }

    /**
     * socket response data listener
     */
    private OnDataReceiveListener onDataReceiveListener = null;
    private int requestCode = -1;

    public interface OnDataReceiveListener {
        void onConnectSuccess();

        void onConnectFail();

        void onDataReceive(byte[] buffer, int size, int requestCode);
    }

    public void setOnDataReceiveListener() {
        onDataReceiveListener = new UdpClient.OnDataReceiveListener() {
            @Override
            public void onConnectSuccess() {
                LogUtil.e("已连接");
                RxBus2.getInstance().post(new ConnectIpEvent(ip, port, 1));
            }

            @Override
            public void onConnectFail() {
                LogUtil.e("未连接");
                RxBus2.getInstance().post(new ConnectIpEvent(ip, port, -1));
            }

            @Override
            public void onDataReceive(byte[] buffer, int size, int requestCode) {            //获取有效长度的数据
                byte[] data = new byte[size];
                System.arraycopy(buffer, 0, data, 0, size);
                final String oxValue = HexUtil.byte2hex(data);
                LogUtil.e("onDataReceive requestCode = " + requestCode + ", content = " + oxValue);


            }
        };
    }

}