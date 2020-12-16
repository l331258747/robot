package com.play.robot.util.udp;

import android.text.TextUtils;

import com.play.robot.util.LogUtil;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.ConnectIpEvent;
import com.play.robot.util.rxbus.rxbusEvent.ReceiveCarEvent;
import com.play.robot.util.rxbus.rxbusEvent.ReceiveStatusEvent;

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

//    public static final int TIME_CALL = 3 * 1000;//报警时间
    public static final int TIME_CALL = 20 * 1000;//报警时间
    public static final int TIME_OUT = 12 * 1000;//超时时间
    public static final String wSend = "hello";

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
                mSocket.setSoTimeout(TIME_CALL);

                LogUtil.e("udp connect = " + isConnect());

                //此处这样做没什么意义不大，真正的socket未连接还是靠心跳发送，等待服务端回应比较好，一段时间内未回应，则socket未连接成功
                if (isConnect()) {
                    isStop = false;
//                    onConnectSuccess();
                } else {
                    onConnectFail();
                    return;
                }

            } catch (Exception e) {
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
                    //此为获取后的有效长度，一次最多读64k，预存小的话可能分包
                    size = receivePacket.getLength();
                    LogUtil.e("pre data size = " + receivePacket.getData().length + ", value data size = " + size);
                    byte[] dataBuffer = Arrays.copyOf(preBuffer, size);
                    if (size > 0) {
                        String RecMsg = new String(receivePacket.getData(),0,receivePacket.getLength(),"gb2312");
                        onDataReceive(RecMsg, requestCode);
                        onConnectSuccess();
                    }
                    LogUtil.e("SocketThread read listening");
                } catch (Exception e) {
                    onConnectFail();
                    LogUtil.e("Exception = " + e.getMessage());
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
                } catch (Exception e) {
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
                byte[] mBuffer = wSend.getBytes();
                sendPacket = new DatagramPacket(mBuffer, mBuffer.length, ipAddress, port);
                mSocket.send(sendPacket);
            } catch (Exception e) {
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

    private void onDataReceive(String dataBuffer, int requestCode) {
        if (null != onDataReceiveListener) {
            onDataReceiveListener.onDataReceive(dataBuffer, requestCode);
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

        void onDataReceive(String buffer, int requestCode);
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
            public void onDataReceive(String buffer, int requestCode) {            //获取有效长度的数据
                LogUtil.e("buffer" + buffer + ", requestCode = " + requestCode);

                if(TextUtils.isEmpty(buffer)) return;
                if(!buffer.startsWith("#")) return;

                String[] s = buffer.split(",");
                if(s.length < 3) return;


                if(TextUtils.equals(s[1],"1") && TextUtils.equals(s[2],"1")){
                    if(s.length < 14) return;
                    ReceiveCarEvent event = new ReceiveCarEvent();
                    event.setIp(ip);
                    event.setPort(port);
                    event.setMode(s[1]);
                    event.setTask(s[2]);
                    event.setN1(s[3]);
                    event.setN2(s[4]);
                    event.setN3(s[5]);
                    event.setN4(s[6]);
                    event.setN5(s[7]);
                    event.setN6(s[8]);
                    event.setN7(s[9]);
                    event.setN8(s[10]);
                    event.setN9(s[11]);
                    event.setN10(s[12]);
                    event.setN11(s[13]);
                    RxBus2.getInstance().post(event);
                }else if(TextUtils.equals(s[1],"3") && TextUtils.equals(s[2],"1")){
                    if(s.length < 5) return;
                    ReceiveStatusEvent event = new ReceiveStatusEvent();
                    event.setIp(ip);
                    event.setPort(port);
                    event.setMode(s[1]);
                    event.setTask(s[2]);
                    event.setN1(s[3]);
                    event.setN2(s[4]);
                    RxBus2.getInstance().post(event);
                }
            }
        };
    }

}