package com.ztrs.zgj.device.bean;

import com.ztrs.zgj.LogUtils;

public class RelayConfigurationBean {
    private static final String TAG = RelayConfigurationBean.class.getSimpleName();

    public static final int NC = 0;
    public static final int HEIGHT_ALARM_HIGH = 1;
    public static final int HEIGHT_ALARM_LOW = 2;
    public static final int AROUND_ALARM_HIGH = 3;
    public static final int AROUND_ALARM_LOW = 4;
    public static final int AMPLITUDE_ALARM_HIGH = 5;
    public static final int AMPLITUDE_ALARM_LOW = 6;
    public static final int TORQUE_WARN = 7;
    public static final int TORQUE_ALARM = 8;
    public static final int WEIGHT_ALARM = 10;
    public static final int LOCK_CONTROL = 11;

    byte relay1Use;
    byte relay1State =2;
    byte relay2Use ;
    byte relay2State=2;
    byte relay3Use;
    byte relay3State=2;
    byte relay4Use;
    byte relay4State=2;
    byte relay5Use;
    byte relay5State=2;
    byte relay6Use;
    byte relay6State=2;
    byte relay7Use;
    byte relay7State=2;
    byte relay8Use;
    byte relay8State=2;
    byte relay9Use;
    byte relay9State=2;
    byte relay10Use;
    byte relay10State=2;
    byte relay11Use;
    byte relay11State=2;
    byte relay12Use;
    byte relay12State=2;
//    byte load25;
//    byte load25OutputState;
//    byte load50;
//    byte load50OutputState;
//    byte load90;
//    byte load90OutputState;
//    byte load100;
//    byte load100OutputState;
//
//    byte torque80;
//    byte torque80OutputState;
//    byte torque90;
//    byte torque90OutputState;
//    byte torque100;
//    byte torque100OutputState;
//    byte torque110;
//    byte torque110OutputState;
//
//    byte heightUpStop;
//    byte heightUpStopOutputState;
//    byte heightDownStop;
//    byte heightDownStopOutputState;
//    byte aroundLeftStop;
//    byte aroundLeftStopOutputState;
//    byte aroundRightStop;
//    byte aroundRightStopOutputState;
//    byte amplitudeOutStop;
//    byte amplitudeOutStopOutputState;
//    byte amplitudeInStop;
//    byte amplitudeInStopOutputState;
//
//    byte loadWeight80;
//    byte loadWeight80OutputState;


    public byte getRelay1Use() {
        return relay1Use;
    }

    public void setRelay1Use(byte relay1Use) {
        this.relay1Use = relay1Use;
    }

    public byte getRelay1State() {
        return relay1State;
    }

    public void setRelay1State(byte relay1State) {
        this.relay1State = relay1State;
    }

    public byte getRelay2Use() {
        return relay2Use;
    }

    public void setRelay2Use(byte relay2Use) {
        this.relay2Use = relay2Use;
    }

    public byte getRelay2State() {
        return relay2State;
    }

    public void setRelay2State(byte relay2State) {
        this.relay2State = relay2State;
    }

    public byte getRelay3Use() {
        return relay3Use;
    }

    public void setRelay3Use(byte relay3Use) {
        this.relay3Use = relay3Use;
    }

    public byte getRelay3State() {
        return relay3State;
    }

    public void setRelay3State(byte relay3State) {
        this.relay3State = relay3State;
    }

    public byte getRelay4Use() {
        return relay4Use;
    }

    public void setRelay4Use(byte relay4Use) {
        this.relay4Use = relay4Use;
    }

    public byte getRelay4State() {
        return relay4State;
    }

    public void setRelay4State(byte relay4State) {
        this.relay4State = relay4State;
    }

    public byte getRelay5Use() {
        return relay5Use;
    }

    public void setRelay5Use(byte relay5Use) {
        this.relay5Use = relay5Use;
    }

    public byte getRelay5State() {
        return relay5State;
    }

    public void setRelay5State(byte relay5State) {
        this.relay5State = relay5State;
    }

    public byte getRelay6Use() {
        return relay6Use;
    }

    public void setRelay6Use(byte relay6Use) {
        this.relay6Use = relay6Use;
    }

    public byte getRelay6State() {
        return relay6State;
    }

    public void setRelay6State(byte relay6State) {
        this.relay6State = relay6State;
    }

    public byte getRelay7Use() {
        return relay7Use;
    }

    public void setRelay7Use(byte relay7Use) {
        this.relay7Use = relay7Use;
    }

    public byte getRelay7State() {
        return relay7State;
    }

    public void setRelay7State(byte relay7State) {
        this.relay7State = relay7State;
    }

    public byte getRelay8Use() {
        return relay8Use;
    }

    public void setRelay8Use(byte relay8Use) {
        this.relay8Use = relay8Use;
    }

    public byte getRelay8State() {
        return relay8State;
    }

    public void setRelay8State(byte relay8State) {
        this.relay8State = relay8State;
    }

    public byte getRelay9Use() {
        return relay9Use;
    }

    public void setRelay9Use(byte relay9Use) {
        this.relay9Use = relay9Use;
    }

    public byte getRelay9State() {
        return relay9State;
    }

    public void setRelay9State(byte relay9State) {
        this.relay9State = relay9State;
    }

    public byte getRelay10Use() {
        return relay10Use;
    }

    public void setRelay10Use(byte relay10Use) {
        this.relay10Use = relay10Use;
    }

    public byte getRelay10State() {
        return relay10State;
    }

    public void setRelay10State(byte relay10State) {
        this.relay10State = relay10State;
    }

    public byte getRelay11Use() {
        return relay11Use;
    }

    public void setRelay11Use(byte relay11Use) {
        this.relay11Use = relay11Use;
    }

    public byte getRelay11State() {
        return relay11State;
    }

    public void setRelay11State(byte relay11State) {
        this.relay11State = relay11State;
    }

    public byte getRelay12Use() {
        return relay12Use;
    }

    public void setRelay12Use(byte relay12Use) {
        this.relay12Use = relay12Use;
    }

    public byte getRelay12State() {
        return relay12State;
    }

    public void setRelay12State(byte relay12State) {
        this.relay12State = relay12State;
    }
}
