package com.ztrs.zgj.main.msg;

public class SerialPortOpenResultMsg {

    private boolean success;
    public SerialPortOpenResultMsg(boolean success){
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
