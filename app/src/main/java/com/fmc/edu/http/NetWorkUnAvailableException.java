package com.fmc.edu.http;

/**
 * Created by Candy on 2015/5/3.
 */
public class NetWorkUnAvailableException extends InterruptedException {
    public String msg;

    public NetWorkUnAvailableException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}