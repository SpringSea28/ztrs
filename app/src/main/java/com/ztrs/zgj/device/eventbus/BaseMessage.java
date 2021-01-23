package com.ztrs.zgj.device.eventbus;

public abstract class BaseMessage {

    public static final int RESULT_OK = 0;
    public static final int RESULT_FAIL = -1;
    public static final int RESULT_REPORT = 1;

    public static final int TYPE_QUERY = 0;
    public static final int TYPE_CMD = 1;

    byte[] cmdBytes;
    byte cmd;
    long seq;
    int result;
    int cmdType;

    BaseMessage(long seq,byte cmd,byte[]cmdBytes){
        this.seq = seq;
        this.cmd = cmd;
        this.cmdBytes = cmdBytes;
    }

    BaseMessage(byte cmd){
        this.cmd = cmd;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public byte[] getCmdBytes() {
        return cmdBytes;
    }

    public int getCmdType() {
        return cmdType;
    }

    public void setCmdType(int cmdType) {
        this.cmdType = cmdType;
    }
}
