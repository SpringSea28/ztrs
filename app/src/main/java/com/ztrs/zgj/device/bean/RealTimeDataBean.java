package com.ztrs.zgj.device.bean;

public class RealTimeDataBean {
    private static final String TAG = RealTimeDataBean.class.getSimpleName();
    byte carNumber;
    byte liftingNumber;
    byte magnification;
    byte workMode;
    boolean bypassState;
    boolean connectStateByDeviceWithRemoteCenter;
    boolean plcState;
    boolean network;
    byte upWeightSensorState;
    byte heightSensorState;
    byte turnAroundSensorState;
    byte amplitudeSensorState;
    byte walkingSensorState;
    byte windSpeedSensorState;
    byte slopeSensorState;
    byte upWeightSensorState2;
    byte heightSensorState2;
    byte amplitudeSensorState2;

    boolean electronicTorqueLimitState4 = true;
    boolean electronicTorqueLimitState3= true;
    boolean electronicTorqueLimitState2= true;
    boolean electronicTorqueLimitState1= true;

    boolean electronicWeightLimitState4= true;
    boolean electronicWeightLimitState3= true;
    boolean electronicWeightLimitState2= true;
    boolean electronicWeightLimitState1= true;

    boolean outputUpUpStopLimit= true;
    boolean outputUpuPSlowLimit= true;
    boolean outputDownUpStopLimit= true;
    boolean outputDownUpSlowLimit= true;

    boolean outputLeftAroundStopLimit= true;
    boolean outputLeftAroundSlowLimit= true;
    boolean outputRightAroundStopLimit= true;
    boolean outputRightAroundSlowLimit= true;

    boolean outputOutLuffingStopLimit= true;
    boolean outputOutLuffingSlowLimit= true;
    boolean outputInLuffingStopLimit= true;
    boolean outputInLuffingSlowLimit= true;

    boolean torqueWarnLimit = true;
    boolean torqueAlarmLimit = true;
    boolean weightWarnLimit = true;
    boolean weightAlarmLimit = true;

    boolean electronicWindAlarmLimit= true;
    boolean electronicWindWarningLimit= true;

    boolean strokeUpUpStopLimit;
    boolean strokeUpuPSlowLimit;
    boolean strokeDownUpStopLimit;
    boolean strokeDownUpSlowLimit;
    boolean strokeLeftAroundStopLimit;
    boolean strokeLeftAroundSlowLimit;
    boolean strokeRightAroundStopLimit;
    boolean strokeRightAroundSlowLimit;
    boolean strokeOutLuffingStopLimit;
    boolean strokeOutLuffingSlowLimit;
    boolean strokeInLuffingStopLimit;
    boolean strokeInLuffingSlowLimit;

    boolean areaUpUpStopLimit;
    boolean areaUpuPSlowLimit;
    boolean areaDownUpStopLimit;
    boolean areaDownUpSlowLimit;
    boolean areaLeftAroundStopLimit;
    boolean areaLeftAroundSlowLimit;
    boolean areaRightAroundStopLimit;
    boolean areaRightAroundSlowLimit;
    boolean areaOutLuffingStopLimit;
    boolean areaOutLuffingSlowLimit;
    boolean areaInLuffingStopLimit;
    boolean areaInLuffingSlowLimit;

    boolean preventCollisionUpUpStopLimit;
    boolean preventCollisionUpuPSlowLimit;
    boolean preventCollisionDownUpStopLimit;
    boolean preventCollisionDownUpSlowLimit;
    boolean preventCollisionLeftAroundStopLimit;
    boolean preventCollisionLeftAroundSlowLimit;
    boolean preventCollisionRightAroundStopLimit;
    boolean preventCollisionRightAroundSlowLimit;
    boolean preventCollisionOutLuffingStopLimit;
    boolean preventCollisionOutLuffingSlowLimit;
    boolean preventCollisionInLuffingStopLimit;
    boolean preventCollisionInLuffingSlowLimit;

    int upWeightTorquePercent;
    int upWeight;
    short height;
    int aroundAngle;
    int amplitude;
    int boomUpAngle;
    int windSpeed;
    int xWalking;
    int yWalking;
    int currentRatedLoad = 1000;
    byte upState;
    byte amplitudeState;
    byte aroundState;
    int xSlope;
    int ySlope;

    short torque;
    byte windLevel;

    byte wireRopeState;
    byte wireRopeDamageMagnification;
    int wireRopeDamageheight;
    int wireRopeDamageValue;

    public static final String[] wireRopeStateArray = new String[]{"正常","轻度损伤","中度损伤",
            "重度损伤","严重损伤","报废"};

    public byte getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(byte carNumber) {
        this.carNumber = carNumber;
    }

    public byte getLiftingNumber() {
        return liftingNumber;
    }

    public void setLiftingNumber(byte liftingNumber) {
        this.liftingNumber = liftingNumber;
    }

    public byte getMagnification() {
        return magnification;
    }

    public void setMagnification(byte magnification) {
        this.magnification = magnification;
    }

    public byte getWorkMode() {
        return workMode;
    }

    public void setWorkMode(byte workMode) {
        this.workMode = workMode;
    }

    public boolean isBypassState() {
        return bypassState;
    }

    public void setBypassState(boolean bypassState) {
        this.bypassState = bypassState;
    }

    public boolean isConnectStateByDeviceWithRemoteCenter() {
        return connectStateByDeviceWithRemoteCenter;
    }

    public void setConnectStateByDeviceWithRemoteCenter(boolean connectStateByDeviceWithRemoteCenter) {
        this.connectStateByDeviceWithRemoteCenter = connectStateByDeviceWithRemoteCenter;
    }

    public boolean isPlcState() {
        return plcState;
    }

    public void setPlcState(boolean plcState) {
        this.plcState = plcState;
    }

    public boolean isNetwork() {
        return network;
    }

    public void setNetwork(boolean network) {
        this.network = network;
    }

    public byte getUpWeightSensorState() {
        return upWeightSensorState;
    }

    public void setUpWeightSensorState(byte upWeightSensorState) {
        this.upWeightSensorState = upWeightSensorState;
    }

    public byte getHeightSensorState() {
        return heightSensorState;
    }

    public void setHeightSensorState(byte heightSensorState) {
        this.heightSensorState = heightSensorState;
    }

    public byte getTurnAroundSensorState() {
        return turnAroundSensorState;
    }

    public void setTurnAroundSensorState(byte turnAroundSensorState) {
        this.turnAroundSensorState = turnAroundSensorState;
    }

    public byte getAmplitudeSensorState() {
        return amplitudeSensorState;
    }

    public void setAmplitudeSensorState(byte amplitudeSensorState) {
        this.amplitudeSensorState = amplitudeSensorState;
    }

    public byte getWalkingSensorState() {
        return walkingSensorState;
    }

    public void setWalkingSensorState(byte walkingSensorState) {
        this.walkingSensorState = walkingSensorState;
    }

    public byte getWindSpeedSensorState() {
        return windSpeedSensorState;
    }

    public void setWindSpeedSensorState(byte windSpeedSensorState) {
        this.windSpeedSensorState = windSpeedSensorState;
    }

    public byte getSlopeSensorState() {
        return slopeSensorState;
    }

    public void setSlopeSensorState(byte slopeSensorState) {
        this.slopeSensorState = slopeSensorState;
    }

    public byte getUpWeightSensorState2() {
        return upWeightSensorState2;
    }

    public void setUpWeightSensorState2(byte upWeightSensorState2) {
        this.upWeightSensorState2 = upWeightSensorState2;
    }

    public byte getHeightSensorState2() {
        return heightSensorState2;
    }

    public void setHeightSensorState2(byte heightSensorState2) {
        this.heightSensorState2 = heightSensorState2;
    }

    public byte getAmplitudeSensorState2() {
        return amplitudeSensorState2;
    }

    public void setAmplitudeSensorState2(byte amplitudeSensorState2) {
        this.amplitudeSensorState2 = amplitudeSensorState2;
    }

    public boolean isElectronicTorqueLimitState4() {
        return electronicTorqueLimitState4;
    }

    public void setElectronicTorqueLimitState4(boolean electronicTorqueLimitState4) {
        this.electronicTorqueLimitState4 = electronicTorqueLimitState4;
    }

    public boolean isElectronicTorqueLimitState3() {
        return electronicTorqueLimitState3;
    }

    public void setElectronicTorqueLimitState3(boolean electronicTorqueLimitState3) {
        this.electronicTorqueLimitState3 = electronicTorqueLimitState3;
    }

    public boolean isElectronicTorqueLimitState2() {
        return electronicTorqueLimitState2;
    }

    public void setElectronicTorqueLimitState2(boolean electronicTorqueLimitState2) {
        this.electronicTorqueLimitState2 = electronicTorqueLimitState2;
    }

    public boolean isElectronicTorqueLimitState1() {
        return electronicTorqueLimitState1;
    }

    public void setElectronicTorqueLimitState1(boolean electronicTorqueLimitState1) {
        this.electronicTorqueLimitState1 = electronicTorqueLimitState1;
    }

    public boolean isElectronicWeightLimitState4() {
        return electronicWeightLimitState4;
    }

    public void setElectronicWeightLimitState4(boolean electronicWeightLimitState4) {
        this.electronicWeightLimitState4 = electronicWeightLimitState4;
    }

    public boolean isElectronicWeightLimitState3() {
        return electronicWeightLimitState3;
    }

    public void setElectronicWeightLimitState3(boolean electronicWeightLimitState3) {
        this.electronicWeightLimitState3 = electronicWeightLimitState3;
    }

    public boolean isElectronicWeightLimitState2() {
        return electronicWeightLimitState2;
    }

    public void setElectronicWeightLimitState2(boolean electronicWeightLimitState2) {
        this.electronicWeightLimitState2 = electronicWeightLimitState2;
    }

    public boolean isElectronicWeightLimitState1() {
        return electronicWeightLimitState1;
    }

    public void setElectronicWeightLimitState1(boolean electronicWeightLimitState1) {
        this.electronicWeightLimitState1 = electronicWeightLimitState1;
    }

    public boolean isOutputUpUpStopLimit() {
        return outputUpUpStopLimit;
    }

    public void setOutputUpUpStopLimit(boolean outputUpUpStopLimit) {
        this.outputUpUpStopLimit = outputUpUpStopLimit;
    }

    public boolean isOutputUpuPSlowLimit() {
        return outputUpuPSlowLimit;
    }

    public void setOutputUpuPSlowLimit(boolean outputUpuPSlowLimit) {
        this.outputUpuPSlowLimit = outputUpuPSlowLimit;
    }

    public boolean isOutputDownUpStopLimit() {
        return outputDownUpStopLimit;
    }

    public void setOutputDownUpStopLimit(boolean outputDownUpStopLimit) {
        this.outputDownUpStopLimit = outputDownUpStopLimit;
    }

    public boolean isOutputDownUpSlowLimit() {
        return outputDownUpSlowLimit;
    }

    public void setOutputDownUpSlowLimit(boolean outputDownUpSlowLimit) {
        this.outputDownUpSlowLimit = outputDownUpSlowLimit;
    }

    public boolean isOutputLeftAroundStopLimit() {
        return outputLeftAroundStopLimit;
    }

    public void setOutputLeftAroundStopLimit(boolean outputLeftAroundStopLimit) {
        this.outputLeftAroundStopLimit = outputLeftAroundStopLimit;
    }

    public boolean isOutputLeftAroundSlowLimit() {
        return outputLeftAroundSlowLimit;
    }

    public void setOutputLeftAroundSlowLimit(boolean outputLeftAroundSlowLimit) {
        this.outputLeftAroundSlowLimit = outputLeftAroundSlowLimit;
    }

    public boolean isOutputRightAroundStopLimit() {
        return outputRightAroundStopLimit;
    }

    public void setOutputRightAroundStopLimit(boolean outputRightAroundStopLimit) {
        this.outputRightAroundStopLimit = outputRightAroundStopLimit;
    }

    public boolean isOutputRightAroundSlowLimit() {
        return outputRightAroundSlowLimit;
    }

    public void setOutputRightAroundSlowLimit(boolean outputRightAroundSlowLimit) {
        this.outputRightAroundSlowLimit = outputRightAroundSlowLimit;
    }

    public boolean isOutputOutLuffingStopLimit() {
        return outputOutLuffingStopLimit;
    }

    public void setOutputOutLuffingStopLimit(boolean outputOutLuffingStopLimit) {
        this.outputOutLuffingStopLimit = outputOutLuffingStopLimit;
    }

    public boolean isOutputOutLuffingSlowLimit() {
        return outputOutLuffingSlowLimit;
    }

    public void setOutputOutLuffingSlowLimit(boolean outputOutLuffingSlowLimit) {
        this.outputOutLuffingSlowLimit = outputOutLuffingSlowLimit;
    }

    public boolean isOutputInLuffingStopLimit() {
        return outputInLuffingStopLimit;
    }

    public void setOutputInLuffingStopLimit(boolean outputInLuffingStopLimit) {
        this.outputInLuffingStopLimit = outputInLuffingStopLimit;
    }

    public boolean isOutputInLuffingSlowLimit() {
        return outputInLuffingSlowLimit;
    }

    public void setOutputInLuffingSlowLimit(boolean outputInLuffingSlowLimit) {
        this.outputInLuffingSlowLimit = outputInLuffingSlowLimit;
    }

    public boolean isTorqueWarnLimit() {
        return torqueWarnLimit;
    }

    public void setTorqueWarnLimit(boolean torqueWarnLimit) {
        this.torqueWarnLimit = torqueWarnLimit;
    }

    public boolean isTorqueAlarmLimit() {
        return torqueAlarmLimit;
    }

    public void setTorqueAlarmLimit(boolean torqueAlarmLimit) {
        this.torqueAlarmLimit = torqueAlarmLimit;
    }

    public boolean isWeightWarnLimit() {
        return weightWarnLimit;
    }

    public void setWeightWarnLimit(boolean weightWarnLimit) {
        this.weightWarnLimit = weightWarnLimit;
    }

    public boolean isWeightAlarmLimit() {
        return weightAlarmLimit;
    }

    public void setWeightAlarmLimit(boolean weightAlarmLimit) {
        this.weightAlarmLimit = weightAlarmLimit;
    }

    public boolean isElectronicWindAlarmLimit() {
        return electronicWindAlarmLimit;
    }

    public void setElectronicWindAlarmLimit(boolean electronicWindAlarmLimit) {
        this.electronicWindAlarmLimit = electronicWindAlarmLimit;
    }

    public boolean isElectronicWindWarningLimit() {
        return electronicWindWarningLimit;
    }

    public void setElectronicWindWarningLimit(boolean electronicWindWarningLimit) {
        this.electronicWindWarningLimit = electronicWindWarningLimit;
    }

    public boolean isStrokeUpUpStopLimit() {
        return strokeUpUpStopLimit;
    }

    public void setStrokeUpUpStopLimit(boolean strokeUpUpStopLimit) {
        this.strokeUpUpStopLimit = strokeUpUpStopLimit;
    }

    public boolean isStrokeUpuPSlowLimit() {
        return strokeUpuPSlowLimit;
    }

    public void setStrokeUpuPSlowLimit(boolean strokeUpuPSlowLimit) {
        this.strokeUpuPSlowLimit = strokeUpuPSlowLimit;
    }

    public boolean isStrokeDownUpStopLimit() {
        return strokeDownUpStopLimit;
    }

    public void setStrokeDownUpStopLimit(boolean strokeDownUpStopLimit) {
        this.strokeDownUpStopLimit = strokeDownUpStopLimit;
    }

    public boolean isStrokeDownUpSlowLimit() {
        return strokeDownUpSlowLimit;
    }

    public void setStrokeDownUpSlowLimit(boolean strokeDownUpSlowLimit) {
        this.strokeDownUpSlowLimit = strokeDownUpSlowLimit;
    }

    public boolean isStrokeLeftAroundStopLimit() {
        return strokeLeftAroundStopLimit;
    }

    public void setStrokeLeftAroundStopLimit(boolean strokeLeftAroundStopLimit) {
        this.strokeLeftAroundStopLimit = strokeLeftAroundStopLimit;
    }

    public boolean isStrokeLeftAroundSlowLimit() {
        return strokeLeftAroundSlowLimit;
    }

    public void setStrokeLeftAroundSlowLimit(boolean strokeLeftAroundSlowLimit) {
        this.strokeLeftAroundSlowLimit = strokeLeftAroundSlowLimit;
    }

    public boolean isStrokeRightAroundStopLimit() {
        return strokeRightAroundStopLimit;
    }

    public void setStrokeRightAroundStopLimit(boolean strokeRightAroundStopLimit) {
        this.strokeRightAroundStopLimit = strokeRightAroundStopLimit;
    }

    public boolean isStrokeRightAroundSlowLimit() {
        return strokeRightAroundSlowLimit;
    }

    public void setStrokeRightAroundSlowLimit(boolean strokeRightAroundSlowLimit) {
        this.strokeRightAroundSlowLimit = strokeRightAroundSlowLimit;
    }

    public boolean isStrokeOutLuffingStopLimit() {
        return strokeOutLuffingStopLimit;
    }

    public void setStrokeOutLuffingStopLimit(boolean strokeOutLuffingStopLimit) {
        this.strokeOutLuffingStopLimit = strokeOutLuffingStopLimit;
    }

    public boolean isStrokeOutLuffingSlowLimit() {
        return strokeOutLuffingSlowLimit;
    }

    public void setStrokeOutLuffingSlowLimit(boolean strokeOutLuffingSlowLimit) {
        this.strokeOutLuffingSlowLimit = strokeOutLuffingSlowLimit;
    }

    public boolean isStrokeInLuffingStopLimit() {
        return strokeInLuffingStopLimit;
    }

    public void setStrokeInLuffingStopLimit(boolean strokeInLuffingStopLimit) {
        this.strokeInLuffingStopLimit = strokeInLuffingStopLimit;
    }

    public boolean isStrokeInLuffingSlowLimit() {
        return strokeInLuffingSlowLimit;
    }

    public void setStrokeInLuffingSlowLimit(boolean strokeInLuffingSlowLimit) {
        this.strokeInLuffingSlowLimit = strokeInLuffingSlowLimit;
    }

    public boolean isAreaUpUpStopLimit() {
        return areaUpUpStopLimit;
    }

    public void setAreaUpUpStopLimit(boolean areaUpUpStopLimit) {
        this.areaUpUpStopLimit = areaUpUpStopLimit;
    }

    public boolean isAreaUpuPSlowLimit() {
        return areaUpuPSlowLimit;
    }

    public void setAreaUpuPSlowLimit(boolean areaUpuPSlowLimit) {
        this.areaUpuPSlowLimit = areaUpuPSlowLimit;
    }

    public boolean isAreaDownUpStopLimit() {
        return areaDownUpStopLimit;
    }

    public void setAreaDownUpStopLimit(boolean areaDownUpStopLimit) {
        this.areaDownUpStopLimit = areaDownUpStopLimit;
    }

    public boolean isAreaDownUpSlowLimit() {
        return areaDownUpSlowLimit;
    }

    public void setAreaDownUpSlowLimit(boolean areaDownUpSlowLimit) {
        this.areaDownUpSlowLimit = areaDownUpSlowLimit;
    }

    public boolean isAreaLeftAroundStopLimit() {
        return areaLeftAroundStopLimit;
    }

    public void setAreaLeftAroundStopLimit(boolean areaLeftAroundStopLimit) {
        this.areaLeftAroundStopLimit = areaLeftAroundStopLimit;
    }

    public boolean isAreaLeftAroundSlowLimit() {
        return areaLeftAroundSlowLimit;
    }

    public void setAreaLeftAroundSlowLimit(boolean areaLeftAroundSlowLimit) {
        this.areaLeftAroundSlowLimit = areaLeftAroundSlowLimit;
    }

    public boolean isAreaRightAroundStopLimit() {
        return areaRightAroundStopLimit;
    }

    public void setAreaRightAroundStopLimit(boolean areaRightAroundStopLimit) {
        this.areaRightAroundStopLimit = areaRightAroundStopLimit;
    }

    public boolean isAreaRightAroundSlowLimit() {
        return areaRightAroundSlowLimit;
    }

    public void setAreaRightAroundSlowLimit(boolean areaRightAroundSlowLimit) {
        this.areaRightAroundSlowLimit = areaRightAroundSlowLimit;
    }

    public boolean isAreaOutLuffingStopLimit() {
        return areaOutLuffingStopLimit;
    }

    public void setAreaOutLuffingStopLimit(boolean areaOutLuffingStopLimit) {
        this.areaOutLuffingStopLimit = areaOutLuffingStopLimit;
    }

    public boolean isAreaOutLuffingSlowLimit() {
        return areaOutLuffingSlowLimit;
    }

    public void setAreaOutLuffingSlowLimit(boolean areaOutLuffingSlowLimit) {
        this.areaOutLuffingSlowLimit = areaOutLuffingSlowLimit;
    }

    public boolean isAreaInLuffingStopLimit() {
        return areaInLuffingStopLimit;
    }

    public void setAreaInLuffingStopLimit(boolean areaInLuffingStopLimit) {
        this.areaInLuffingStopLimit = areaInLuffingStopLimit;
    }

    public boolean isAreaInLuffingSlowLimit() {
        return areaInLuffingSlowLimit;
    }

    public void setAreaInLuffingSlowLimit(boolean areaInLuffingSlowLimit) {
        this.areaInLuffingSlowLimit = areaInLuffingSlowLimit;
    }

    public boolean isPreventCollisionUpUpStopLimit() {
        return preventCollisionUpUpStopLimit;
    }

    public void setPreventCollisionUpUpStopLimit(boolean preventCollisionUpUpStopLimit) {
        this.preventCollisionUpUpStopLimit = preventCollisionUpUpStopLimit;
    }

    public boolean isPreventCollisionUpuPSlowLimit() {
        return preventCollisionUpuPSlowLimit;
    }

    public void setPreventCollisionUpuPSlowLimit(boolean preventCollisionUpuPSlowLimit) {
        this.preventCollisionUpuPSlowLimit = preventCollisionUpuPSlowLimit;
    }

    public boolean isPreventCollisionDownUpStopLimit() {
        return preventCollisionDownUpStopLimit;
    }

    public void setPreventCollisionDownUpStopLimit(boolean preventCollisionDownUpStopLimit) {
        this.preventCollisionDownUpStopLimit = preventCollisionDownUpStopLimit;
    }

    public boolean isPreventCollisionDownUpSlowLimit() {
        return preventCollisionDownUpSlowLimit;
    }

    public void setPreventCollisionDownUpSlowLimit(boolean preventCollisionDownUpSlowLimit) {
        this.preventCollisionDownUpSlowLimit = preventCollisionDownUpSlowLimit;
    }

    public boolean isPreventCollisionLeftAroundStopLimit() {
        return preventCollisionLeftAroundStopLimit;
    }

    public void setPreventCollisionLeftAroundStopLimit(boolean preventCollisionLeftAroundStopLimit) {
        this.preventCollisionLeftAroundStopLimit = preventCollisionLeftAroundStopLimit;
    }

    public boolean isPreventCollisionLeftAroundSlowLimit() {
        return preventCollisionLeftAroundSlowLimit;
    }

    public void setPreventCollisionLeftAroundSlowLimit(boolean preventCollisionLeftAroundSlowLimit) {
        this.preventCollisionLeftAroundSlowLimit = preventCollisionLeftAroundSlowLimit;
    }

    public boolean isPreventCollisionRightAroundStopLimit() {
        return preventCollisionRightAroundStopLimit;
    }

    public void setPreventCollisionRightAroundStopLimit(boolean preventCollisionRightAroundStopLimit) {
        this.preventCollisionRightAroundStopLimit = preventCollisionRightAroundStopLimit;
    }

    public boolean isPreventCollisionRightAroundSlowLimit() {
        return preventCollisionRightAroundSlowLimit;
    }

    public void setPreventCollisionRightAroundSlowLimit(boolean preventCollisionRightAroundSlowLimit) {
        this.preventCollisionRightAroundSlowLimit = preventCollisionRightAroundSlowLimit;
    }

    public boolean isPreventCollisionOutLuffingStopLimit() {
        return preventCollisionOutLuffingStopLimit;
    }

    public void setPreventCollisionOutLuffingStopLimit(boolean preventCollisionOutLuffingStopLimit) {
        this.preventCollisionOutLuffingStopLimit = preventCollisionOutLuffingStopLimit;
    }

    public boolean isPreventCollisionOutLuffingSlowLimit() {
        return preventCollisionOutLuffingSlowLimit;
    }

    public void setPreventCollisionOutLuffingSlowLimit(boolean preventCollisionOutLuffingSlowLimit) {
        this.preventCollisionOutLuffingSlowLimit = preventCollisionOutLuffingSlowLimit;
    }

    public boolean isPreventCollisionInLuffingStopLimit() {
        return preventCollisionInLuffingStopLimit;
    }

    public void setPreventCollisionInLuffingStopLimit(boolean preventCollisionInLuffingStopLimit) {
        this.preventCollisionInLuffingStopLimit = preventCollisionInLuffingStopLimit;
    }

    public boolean isPreventCollisionInLuffingSlowLimit() {
        return preventCollisionInLuffingSlowLimit;
    }

    public void setPreventCollisionInLuffingSlowLimit(boolean preventCollisionInLuffingSlowLimit) {
        this.preventCollisionInLuffingSlowLimit = preventCollisionInLuffingSlowLimit;
    }

    public int getUpWeightTorquePercent() {
        return upWeightTorquePercent;
    }

    public void setUpWeightTorquePercent(int upWeightTorquePercent) {
        this.upWeightTorquePercent = upWeightTorquePercent;
    }

    public int getUpWeight() {
        return upWeight;
    }

    public void setUpWeight(int upWeight) {
        this.upWeight = upWeight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public int getAroundAngle() {
        return aroundAngle;
    }

    public void setAroundAngle(int aroundAngle) {
        this.aroundAngle = aroundAngle;
    }

    public int getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(int amplitude) {
        this.amplitude = amplitude;
    }

    public int getBoomUpAngle() {
        return boomUpAngle;
    }

    public void setBoomUpAngle(int boomUpAngle) {
        this.boomUpAngle = boomUpAngle;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getxWalking() {
        return xWalking;
    }

    public void setxWalking(int xWalking) {
        this.xWalking = xWalking;
    }

    public int getyWalking() {
        return yWalking;
    }

    public void setyWalking(int yWalking) {
        this.yWalking = yWalking;
    }

    public int getCurrentRatedLoad() {
        return currentRatedLoad;
    }

    public void setCurrentRatedLoad(int currentRatedLoad) {
        this.currentRatedLoad = currentRatedLoad;
    }

    public byte getUpState() {
        return upState;
    }

    public void setUpState(byte upState) {
        this.upState = upState;
    }

    public byte getAmplitudeState() {
        return amplitudeState;
    }

    public void setAmplitudeState(byte amplitudeState) {
        this.amplitudeState = amplitudeState;
    }

    public byte getAroundState() {
        return aroundState;
    }

    public void setAroundState(byte aroundState) {
        this.aroundState = aroundState;
    }

    public int getxSlope() {
        return xSlope;
    }

    public void setxSlope(int xSlope) {
        this.xSlope = xSlope;
    }

    public int getySlope() {
        return ySlope;
    }

    public void setySlope(int ySlope) {
        this.ySlope = ySlope;
    }

    public short getTorque() {
        return torque;
    }

    public void setTorque(short torque) {
        this.torque = torque;
    }

    public byte getWindLevel() {
        return windLevel;
    }

    public void setWindLevel(byte windLevel) {
        this.windLevel = windLevel;
    }

    public byte getWireRopeState() {
        return wireRopeState;
    }

    public void setWireRopeState(byte wireRopeState) {
        this.wireRopeState = wireRopeState;
    }

    public byte getWireRopeDamageMagnification() {
        return wireRopeDamageMagnification;
    }

    public void setWireRopeDamageMagnification(byte wireRopeDamageMagnification) {
        this.wireRopeDamageMagnification = wireRopeDamageMagnification;
    }

    public int getWireRopeDamageheight() {
        return wireRopeDamageheight;
    }

    public void setWireRopeDamageheight(int wireRopeDamageheight) {
        this.wireRopeDamageheight = wireRopeDamageheight;
    }

    public int getWireRopeDamageValue() {
        return wireRopeDamageValue;
    }

    public void setWireRopeDamageValue(int wireRopeDamageValue) {
        this.wireRopeDamageValue = wireRopeDamageValue;
    }
}
