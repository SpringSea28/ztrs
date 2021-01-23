package com.ztrs.zgj.device.bean;

import com.ztrs.zgj.LogUtils;

public class RegisterInfoBean {
    private static final String TAG = RegisterInfoBean.class.getSimpleName();
    byte[] info;

    public byte[] getInfo() {
        return info;
    }

    public void setInfo(byte[] info) {
        this.info = info;
    }

    public String getHostId(){
        if(info == null || info.length<30){
            return "unKnow";
        }
        byte[] hostIdBytes = new byte[30];
        System.arraycopy(info,0,hostIdBytes,0,hostIdBytes.length);
        return new String(hostIdBytes);
    }
}
