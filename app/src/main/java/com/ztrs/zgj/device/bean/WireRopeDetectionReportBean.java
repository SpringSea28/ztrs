package com.ztrs.zgj.device.bean;

public class WireRopeDetectionReportBean {
    private static final String TAG = WireRopeDetectionReportBean.class.getSimpleName();
    int damageValue;
    int height;
    byte magnification;

    public int getDamageValue() {
        return damageValue;
    }

    public void setDamageValue(int damageValue) {
        this.damageValue = damageValue;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public byte getMagnification() {
        return magnification;
    }

    public void setMagnification(byte magnification) {
        this.magnification = magnification;
    }
}
