package com.ztrs.zgj.device.bean;

import com.ztrs.zgj.LogUtils;

public class DeviceVersionBean {
    private static final String TAG = DeviceVersionBean.class.getSimpleName();
    byte verInt;
    byte verFloat;

    public byte getVerInt() {
        return verInt;
    }

    public void setVerInt(byte verInt) {
        this.verInt = verInt;
    }

    public byte getVerFloat() {
        return verFloat;
    }

    public void setVerFloat(byte verFloat) {
        this.verFloat = verFloat;
    }
}
