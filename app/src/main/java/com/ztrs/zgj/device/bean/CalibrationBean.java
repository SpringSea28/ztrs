package com.ztrs.zgj.device.bean;

import androidx.annotation.NonNull;

public class CalibrationBean implements Cloneable{
    private static final String TAG = CalibrationBean.class.getSimpleName();
    long current1;
    int calibration1;
    long current2;
    int calibration2;
    int lowAlarmValue;
    int lowWarnValue;
    byte lowAlarmRelayControl;
    byte lowAlarmRelayOutput = 2;
    int highAlarmValue;
    int highWarnValue;
    byte highAlarmRelayControl;
    byte highAlarmRelayOutput = 2;

    public long getCurrent1() {
        return current1;
    }

    public void setCurrent1(long current1) {
        this.current1 = current1;
    }

    public int getCalibration1() {
        return calibration1;
    }

    public void setCalibration1(int calibration1) {
        this.calibration1 = calibration1;
    }

    public long getCurrent2() {
        return current2;
    }

    public void setCurrent2(long current2) {
        this.current2 = current2;
    }

    public int getCalibration2() {
        return calibration2;
    }

    public void setCalibration2(int calibration2) {
        this.calibration2 = calibration2;
    }

    public int getLowAlarmValue() {
        return lowAlarmValue;
    }

    public void setLowAlarmValue(int lowAlarmValue) {
        this.lowAlarmValue = lowAlarmValue;
    }

    public int getLowWarnValue() {
        return lowWarnValue;
    }

    public void setLowWarnValue(int lowWarnValue) {
        this.lowWarnValue = lowWarnValue;
    }

    public byte getLowAlarmRelayControl() {
        return lowAlarmRelayControl;
    }

    public void setLowAlarmRelayControl(byte lowAlarmRelayControl) {
        this.lowAlarmRelayControl = lowAlarmRelayControl;
    }

    public byte getLowAlarmRelayOutput() {
        return lowAlarmRelayOutput;
    }

    public void setLowAlarmRelayOutput(byte lowAlarmRelayOutput) {
        this.lowAlarmRelayOutput = lowAlarmRelayOutput;
    }

    public int getHighAlarmValue() {
        return highAlarmValue;
    }

    public void setHighAlarmValue(int highAlarmValue) {
        this.highAlarmValue = highAlarmValue;
    }

    public int getHighWarnValue() {
        return highWarnValue;
    }

    public void setHighWarnValue(int highWarnValue) {
        this.highWarnValue = highWarnValue;
    }

    public byte getHighAlarmRelayControl() {
        return highAlarmRelayControl;
    }

    public void setHighAlarmRelayControl(byte highAlarmRelayControl) {
        this.highAlarmRelayControl = highAlarmRelayControl;
    }

    public byte getHighAlarmRelayOutput() {
        return highAlarmRelayOutput;
    }

    public void setHighAlarmRelayOutput(byte highAlarmRelayOutput) {
        this.highAlarmRelayOutput = highAlarmRelayOutput;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
