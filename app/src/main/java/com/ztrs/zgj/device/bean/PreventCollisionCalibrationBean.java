package com.ztrs.zgj.device.bean;

import androidx.annotation.NonNull;

public class PreventCollisionCalibrationBean implements Cloneable{
    private static final String TAG = PreventCollisionCalibrationBean.class.getSimpleName();
    int heightAlarmValue;
    int heightWarnValue;

    int distanceAlarmValue;
    int distanceWarnValue;


    public int getHeightAlarmValue() {
        return heightAlarmValue;
    }

    public void setHeightAlarmValue(int heightAlarmValue) {
        this.heightAlarmValue = heightAlarmValue;
    }

    public int getHeightWarnValue() {
        return heightWarnValue;
    }

    public void setHeightWarnValue(int heightWarnValue) {
        this.heightWarnValue = heightWarnValue;
    }


    public int getDistanceAlarmValue() {
        return distanceAlarmValue;
    }

    public void setDistanceAlarmValue(int distanceAlarmValue) {
        this.distanceAlarmValue = distanceAlarmValue;
    }

    public int getDistanceWarnValue() {
        return distanceWarnValue;
    }

    public void setDistanceWarnValue(int distanceWarnValue) {
        this.distanceWarnValue = distanceWarnValue;
    }


    @NonNull
    @Override
    public PreventCollisionCalibrationBean clone() throws CloneNotSupportedException {
        return (PreventCollisionCalibrationBean)super.clone();
    }
}
