package com.ztrs.zgj.device.bean;

import com.ztrs.zgj.LogUtils;

public class SensorRealtimeDataBean {
    private static final String TAG = SensorRealtimeDataBean.class.getSimpleName();
    long heightSensor;
    long amplitudeSensor;
    long aroundSensor;
    long weightSensor;
    long slopeSensorX;
    long slopeSensorY;
    long windSpeedSensor;

    public long getHeightSensor() {
        return heightSensor;
    }

    public void setHeightSensor(long heightSensor) {
        this.heightSensor = heightSensor;
    }

    public long getAmplitudeSensor() {
        return amplitudeSensor;
    }

    public void setAmplitudeSensor(long amplitudeSensor) {
        this.amplitudeSensor = amplitudeSensor;
    }

    public long getAroundSensor() {
        return aroundSensor;
    }

    public void setAroundSensor(long aroundSensor) {
        this.aroundSensor = aroundSensor;
    }

    public long getWeightSensor() {
        return weightSensor;
    }

    public void setWeightSensor(long weightSensor) {
        this.weightSensor = weightSensor;
    }

    public long getSlopeSensorX() {
        return slopeSensorX;
    }

    public void setSlopeSensorX(long slopeSensorX) {
        this.slopeSensorX = slopeSensorX;
    }

    public long getSlopeSensorY() {
        return slopeSensorY;
    }

    public void setSlopeSensorY(long slopeSensorY) {
        this.slopeSensorY = slopeSensorY;
    }

    public long getWindSpeedSensor() {
        return windSpeedSensor;
    }

    public void setWindSpeedSensor(long windSpeedSensor) {
        this.windSpeedSensor = windSpeedSensor;
    }
}
