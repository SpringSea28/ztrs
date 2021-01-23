package com.ztrs.zgj.device.bean;

import com.ztrs.zgj.LogUtils;

public class UnLockCarBean {
    private static final String TAG = UnLockCarBean.class.getSimpleName();
    byte state;

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
        LogUtils.LogE(TAG,"set state: "+state);
    }
}
