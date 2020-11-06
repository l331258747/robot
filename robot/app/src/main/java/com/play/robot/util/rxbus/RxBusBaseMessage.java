package com.play.robot.util.rxbus;

/**
 * Created by LGQ
 * Time: 2018/7/30
 * Function:
 */

public class RxBusBaseMessage {
    private int code;
    private Object object;

    public RxBusBaseMessage(int code, Object object) {
        this.code = code;
        this.object = object;
    }

    public RxBusBaseMessage() {
    }

    public int getCode() {
        return code;
    }

    public Object getObject() {
        return object;
    }
}