package com.ztrs.zgj.device.bean;

import com.ztrs.zgj.LogUtils;

public class RelayConfigurationBean {
    private static final String TAG = RelayConfigurationBean.class.getSimpleName();
    byte load25;
    byte load25OutputState;
    byte load50;
    byte load50OutputState;
    byte load90;
    byte load90OutputState;
    byte load100;
    byte load100OutputState;

    byte torque80;
    byte torque80OutputState;
    byte torque90;
    byte torque90OutputState;
    byte torque100;
    byte torque100OutputState;
    byte torque110;
    byte torque110OutputState;

    byte heightUpStop;
    byte heightUpStopOutputState;
    byte heightDownStop;
    byte heightDownStopOutputState;
    byte aroundLeftStop;
    byte aroundLeftStopOutputState;
    byte aroundRightStop;
    byte aroundRightStopOutputState;
    byte amplitudeOutStop;
    byte amplitudeOutStopOutputState;
    byte amplitudeInStop;
    byte amplitudeInStopOutputState;

    byte loadWeight80;
    byte loadWeight80OutputState;

    public byte getLoad25() {
        return load25;
    }

    public void setLoad25(byte load25) {
        this.load25 = load25;
    }

    public byte getLoad25OutputState() {
        return load25OutputState;
    }

    public void setLoad25OutputState(byte load25OutputState) {
        this.load25OutputState = load25OutputState;
    }

    public byte getLoad50() {
        return load50;
    }

    public void setLoad50(byte load50) {
        this.load50 = load50;
    }

    public byte getLoad50OutputState() {
        return load50OutputState;
    }

    public void setLoad50OutputState(byte load50OutputState) {
        this.load50OutputState = load50OutputState;
    }

    public byte getLoad90() {
        return load90;
    }

    public void setLoad90(byte load90) {
        this.load90 = load90;
    }

    public byte getLoad90OutputState() {
        return load90OutputState;
    }

    public void setLoad90OutputState(byte load90OutputState) {
        this.load90OutputState = load90OutputState;
    }

    public byte getLoad100() {
        return load100;
    }

    public void setLoad100(byte load100) {
        this.load100 = load100;
    }

    public byte getLoad100OutputState() {
        return load100OutputState;
    }

    public void setLoad100OutputState(byte load100OutputState) {
        this.load100OutputState = load100OutputState;
    }

    public byte getTorque80() {
        return torque80;
    }

    public void setTorque80(byte torque80) {
        this.torque80 = torque80;
    }

    public byte getTorque80OutputState() {
        return torque80OutputState;
    }

    public void setTorque80OutputState(byte torque80OutputState) {
        this.torque80OutputState = torque80OutputState;
    }

    public byte getTorque90() {
        return torque90;
    }

    public void setTorque90(byte torque90) {
        this.torque90 = torque90;
    }

    public byte getTorque90OutputState() {
        return torque90OutputState;
    }

    public void setTorque90OutputState(byte torque90OutputState) {
        this.torque90OutputState = torque90OutputState;
    }

    public byte getTorque100() {
        return torque100;
    }

    public void setTorque100(byte torque100) {
        this.torque100 = torque100;
    }

    public byte getTorque100OutputState() {
        return torque100OutputState;
    }

    public void setTorque100OutputState(byte torque100OutputState) {
        this.torque100OutputState = torque100OutputState;
    }

    public byte getTorque110() {
        return torque110;
    }

    public void setTorque110(byte torque110) {
        this.torque110 = torque110;
    }

    public byte getTorque110OutputState() {
        return torque110OutputState;
    }

    public void setTorque110OutputState(byte torque110OutputState) {
        this.torque110OutputState = torque110OutputState;
    }

    public byte getHeightUpStop() {
        return heightUpStop;
    }

    public void setHeightUpStop(byte heightUpStop) {
        this.heightUpStop = heightUpStop;
    }

    public byte getHeightUpStopOutputState() {
        return heightUpStopOutputState;
    }

    public void setHeightUpStopOutputState(byte heightUpStopOutputState) {
        this.heightUpStopOutputState = heightUpStopOutputState;
    }

    public byte getHeightDownStop() {
        return heightDownStop;
    }

    public void setHeightDownStop(byte heightDownStop) {
        this.heightDownStop = heightDownStop;
    }

    public byte getHeightDownStopOutputState() {
        return heightDownStopOutputState;
    }

    public void setHeightDownStopOutputState(byte heightDownStopOutputState) {
        this.heightDownStopOutputState = heightDownStopOutputState;
    }

    public byte getAroundLeftStop() {
        return aroundLeftStop;
    }

    public void setAroundLeftStop(byte aroundLeftStop) {
        this.aroundLeftStop = aroundLeftStop;
    }

    public byte getAroundLeftStopOutputState() {
        return aroundLeftStopOutputState;
    }

    public void setAroundLeftStopOutputState(byte aroundLeftStopOutputState) {
        this.aroundLeftStopOutputState = aroundLeftStopOutputState;
    }

    public byte getAroundRightStop() {
        return aroundRightStop;
    }

    public void setAroundRightStop(byte aroundRightStop) {
        this.aroundRightStop = aroundRightStop;
    }

    public byte getAroundRightStopOutputState() {
        return aroundRightStopOutputState;
    }

    public void setAroundRightStopOutputState(byte aroundRightStopOutputState) {
        this.aroundRightStopOutputState = aroundRightStopOutputState;
    }

    public byte getAmplitudeOutStop() {
        return amplitudeOutStop;
    }

    public void setAmplitudeOutStop(byte amplitudeOutStop) {
        this.amplitudeOutStop = amplitudeOutStop;
    }

    public byte getAmplitudeOutStopOutputState() {
        return amplitudeOutStopOutputState;
    }

    public void setAmplitudeOutStopOutputState(byte amplitudeOutStopOutputState) {
        this.amplitudeOutStopOutputState = amplitudeOutStopOutputState;
    }

    public byte getAmplitudeInStop() {
        return amplitudeInStop;
    }

    public void setAmplitudeInStop(byte amplitudeInStop) {
        this.amplitudeInStop = amplitudeInStop;
    }

    public byte getAmplitudeInStopOutputState() {
        return amplitudeInStopOutputState;
    }

    public void setAmplitudeInStopOutputState(byte amplitudeInStopOutputState) {
        this.amplitudeInStopOutputState = amplitudeInStopOutputState;
    }

    public byte getLoadWeight80() {
        return loadWeight80;
    }

    public void setLoadWeight80(byte loadWeight80) {
        this.loadWeight80 = loadWeight80;
    }

    public byte getLoadWeight80OutputState() {
        return loadWeight80OutputState;
    }

    public void setLoadWeight80OutputState(byte loadWeight80OutputState) {
        this.loadWeight80OutputState = loadWeight80OutputState;
    }
}
