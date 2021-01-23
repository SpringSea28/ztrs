package com.ztrs.zgj.device.bean;

import androidx.annotation.NonNull;

import com.ztrs.zgj.LogUtils;

public class WireRopeDetectionParametersSetBean implements Cloneable{
    private static final String TAG = WireRopeDetectionParametersSetBean.class.getSimpleName();
    int threshold;
    byte detectionState;
    int lightDamage;
    int middleDamage;
    int severeDamage;
    int damage;
    int scrapped;
    int delay;
    int detectCount;
    int mode;
    int alarmInterval;
    int alarmCount;

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public byte getDetectionState() {
        return detectionState;
    }

    public void setDetectionState(byte detectionState) {
        this.detectionState = detectionState;
        LogUtils.LogE(TAG,"set state: "+ detectionState);
    }

    public int getLightDamage() {
        return lightDamage;
    }

    public void setLightDamage(int lightDamage) {
        this.lightDamage = lightDamage;
    }

    public int getMiddleDamage() {
        return middleDamage;
    }

    public void setMiddleDamage(int middleDamage) {
        this.middleDamage = middleDamage;
    }

    public int getSevereDamage() {
        return severeDamage;
    }

    public void setSevereDamage(int severeDamage) {
        this.severeDamage = severeDamage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getScrapped() {
        return scrapped;
    }

    public void setScrapped(int scrapped) {
        this.scrapped = scrapped;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getDetectCount() {
        return detectCount;
    }

    public void setDetectCount(int detectCount) {
        this.detectCount = detectCount;
    }

    public int getAlarmInterval() {
        return alarmInterval;
    }

    public void setAlarmInterval(int alarmInterval) {
        this.alarmInterval = alarmInterval;
    }

    public int getAlarmCount() {
        return alarmCount;
    }

    public void setAlarmCount(int alarmCount) {
        this.alarmCount = alarmCount;
    }

    @NonNull
    @Override
    public WireRopeDetectionParametersSetBean clone() throws CloneNotSupportedException {
        return (WireRopeDetectionParametersSetBean)super.clone();
    }
}
