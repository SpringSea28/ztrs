package com.ztrs.zgj.device.bean;

import com.ztrs.zgj.LogUtils;

public class InverterDataReportBean {
    private static final String TAG = InverterDataReportBean.class.getSimpleName();
    InverterData upInverterData = new InverterData();
    InverterData aroundInverterData = new InverterData();
    InverterData amplitudeInverterData = new InverterData();

    public InverterData getUpInverterData() {
        return upInverterData;
    }

    public void setUpInverterData(InverterData upInverterData) {
        this.upInverterData = upInverterData;
    }

    public InverterData getAroundInverterData() {
        return aroundInverterData;
    }

    public void setAroundInverterData(InverterData aroundInverterData) {
        this.aroundInverterData = aroundInverterData;
    }

    public InverterData getAmplitudeInverterData() {
        return amplitudeInverterData;
    }

    public void setAmplitudeInverterData(InverterData amplitudeInverterData) {
        this.amplitudeInverterData = amplitudeInverterData;
    }

    public static class InverterData{
        int run;
        int direction;
        int frequency;
        int encoderSpeed;
        int rotorSpeed;
        int busVoltage;
        int outputVoltage;
        int outputCurrent;
        int control;
        int maximumTemperature;
        int warn;
        int error;
        int encoder1LSW;
        int encoder1MSW;
        int encoder2LSW;
        int encoder2MSW;
        int towerCraftCardDI16;
        int towerCraftCardDI23;
        int towerCraftCardDO8;
        int towerCraftCardStateInstructionMSW;
        int towerCraftCardStateInstructionLSW;
        int towerCraftCardStateValue;
        int towerCraftCardSoftwareVersion;
        int liftingTorqueUnderBreak;
        int mBControlTime;
        int mBStartStopControl;
        int mBDirectionControl;
        int mBSpeedGive;
        int lockingControlSignal;
        int antiHookFunctionEnabled;
        int maxTowerLoadWeight;
        int currentLuffingPosition;
        int currentLuffingPositionMaxUpWeight;
        int currentLuffingPositionCurrentUpWeight;

        public int getRun() {
            return run;
        }

        public void setRun(int run) {
            this.run = run;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public int getEncoderSpeed() {
            return encoderSpeed;
        }

        public void setEncoderSpeed(int encoderSpeed) {
            this.encoderSpeed = encoderSpeed;
        }

        public int getRotorSpeed() {
            return rotorSpeed;
        }

        public void setRotorSpeed(int rotorSpeed) {
            this.rotorSpeed = rotorSpeed;
        }

        public int getBusVoltage() {
            return busVoltage;
        }

        public void setBusVoltage(int busVoltage) {
            this.busVoltage = busVoltage;
        }

        public int getOutputVoltage() {
            return outputVoltage;
        }

        public void setOutputVoltage(int outputVoltage) {
            this.outputVoltage = outputVoltage;
        }

        public int getOutputCurrent() {
            return outputCurrent;
        }

        public void setOutputCurrent(int outputCurrent) {
            this.outputCurrent = outputCurrent;
        }

        public int getControl() {
            return control;
        }

        public void setControl(int control) {
            this.control = control;
        }

        public int getMaximumTemperature() {
            return maximumTemperature;
        }

        public void setMaximumTemperature(int maximumTemperature) {
            this.maximumTemperature = maximumTemperature;
        }

        public int getWarn() {
            return warn;
        }

        public void setWarn(int warn) {
            this.warn = warn;
        }

        public int getError() {
            return error;
        }

        public void setError(int error) {
            this.error = error;
        }

        public int getEncoder1LSW() {
            return encoder1LSW;
        }

        public void setEncoder1LSW(int encoder1LSW) {
            this.encoder1LSW = encoder1LSW;
        }

        public int getEncoder1MSW() {
            return encoder1MSW;
        }

        public void setEncoder1MSW(int encoder1MSW) {
            this.encoder1MSW = encoder1MSW;
        }

        public int getEncoder2LSW() {
            return encoder2LSW;
        }

        public void setEncoder2LSW(int encoder2LSW) {
            this.encoder2LSW = encoder2LSW;
        }

        public int getEncoder2MSW() {
            return encoder2MSW;
        }

        public void setEncoder2MSW(int encoder2MSW) {
            this.encoder2MSW = encoder2MSW;
        }

        public int getTowerCraftCardDI16() {
            return towerCraftCardDI16;
        }

        public void setTowerCraftCardDI16(int towerCraftCardDI16) {
            this.towerCraftCardDI16 = towerCraftCardDI16;
        }

        public int getTowerCraftCardDI23() {
            return towerCraftCardDI23;
        }

        public void setTowerCraftCardDI23(int towerCraftCardDI23) {
            this.towerCraftCardDI23 = towerCraftCardDI23;
        }

        public int getTowerCraftCardDO8() {
            return towerCraftCardDO8;
        }

        public void setTowerCraftCardDO8(int towerCraftCardDO8) {
            this.towerCraftCardDO8 = towerCraftCardDO8;
        }

        public int getTowerCraftCardStateInstructionMSW() {
            return towerCraftCardStateInstructionMSW;
        }

        public void setTowerCraftCardStateInstructionMSW(int towerCraftCardStateInstructionMSW) {
            this.towerCraftCardStateInstructionMSW = towerCraftCardStateInstructionMSW;
        }

        public int getTowerCraftCardStateInstructionLSW() {
            return towerCraftCardStateInstructionLSW;
        }

        public void setTowerCraftCardStateInstructionLSW(int towerCraftCardStateInstructionLSW) {
            this.towerCraftCardStateInstructionLSW = towerCraftCardStateInstructionLSW;
        }

        public int getTowerCraftCardStateValue() {
            return towerCraftCardStateValue;
        }

        public void setTowerCraftCardStateValue(int towerCraftCardStateValue) {
            this.towerCraftCardStateValue = towerCraftCardStateValue;
        }

        public int getTowerCraftCardSoftwareVersion() {
            return towerCraftCardSoftwareVersion;
        }

        public void setTowerCraftCardSoftwareVersion(int towerCraftCardSoftwareVersion) {
            this.towerCraftCardSoftwareVersion = towerCraftCardSoftwareVersion;
        }

        public int getLiftingTorqueUnderBreak() {
            return liftingTorqueUnderBreak;
        }

        public void setLiftingTorqueUnderBreak(int liftingTorqueUnderBreak) {
            this.liftingTorqueUnderBreak = liftingTorqueUnderBreak;
        }

        public int getmBControlTime() {
            return mBControlTime;
        }

        public void setmBControlTime(int mBControlTime) {
            this.mBControlTime = mBControlTime;
        }

        public int getmBStartStopControl() {
            return mBStartStopControl;
        }

        public void setmBStartStopControl(int mBStartStopControl) {
            this.mBStartStopControl = mBStartStopControl;
        }

        public int getmBDirectionControl() {
            return mBDirectionControl;
        }

        public void setmBDirectionControl(int mBDirectionControl) {
            this.mBDirectionControl = mBDirectionControl;
        }

        public int getmBSpeedGive() {
            return mBSpeedGive;
        }

        public void setmBSpeedGive(int mBSpeedGive) {
            this.mBSpeedGive = mBSpeedGive;
        }

        public int getLockingControlSignal() {
            return lockingControlSignal;
        }

        public void setLockingControlSignal(int lockingControlSignal) {
            this.lockingControlSignal = lockingControlSignal;
        }

        public int getAntiHookFunctionEnabled() {
            return antiHookFunctionEnabled;
        }

        public void setAntiHookFunctionEnabled(int antiHookFunctionEnabled) {
            this.antiHookFunctionEnabled = antiHookFunctionEnabled;
        }

        public int getMaxTowerLoadWeight() {
            return maxTowerLoadWeight;
        }

        public void setMaxTowerLoadWeight(int maxTowerLoadWeight) {
            this.maxTowerLoadWeight = maxTowerLoadWeight;
        }

        public int getCurrentLuffingPosition() {
            return currentLuffingPosition;
        }

        public void setCurrentLuffingPosition(int currentLuffingPosition) {
            this.currentLuffingPosition = currentLuffingPosition;
        }

        public int getCurrentLuffingPositionMaxUpWeight() {
            return currentLuffingPositionMaxUpWeight;
        }

        public void setCurrentLuffingPositionMaxUpWeight(int currentLuffingPositionMaxUpWeight) {
            this.currentLuffingPositionMaxUpWeight = currentLuffingPositionMaxUpWeight;
        }

        public int getCurrentLuffingPositionCurrentUpWeight() {
            return currentLuffingPositionCurrentUpWeight;
        }

        public void setCurrentLuffingPositionCurrentUpWeight(int currentLuffingPositionCurrentUpWeight) {
            this.currentLuffingPositionCurrentUpWeight = currentLuffingPositionCurrentUpWeight;
        }
    }
}
