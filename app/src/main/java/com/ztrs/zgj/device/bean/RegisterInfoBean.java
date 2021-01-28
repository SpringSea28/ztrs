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
            return "unKnown";
        }
        byte[] hostIdBytes = new byte[30];
        System.arraycopy(info,0,hostIdBytes,0,hostIdBytes.length);
        int zeroOffset = 0;
        for(int i=0;i<hostIdBytes.length;i++){
            if(hostIdBytes[i]== 0){
                zeroOffset = i;
                break;
            }
        }
        String s = new String(hostIdBytes);
        return s.substring(0,zeroOffset);
    }

    public String getDeviceVersion(){
        if(info == null || info.length<34){
            return "unKnown";
        }
        String version = Integer.toHexString(info[32]&0xff)
                +"."+Integer.toHexString(info[33]&0xff);
        return version;
    }
}
