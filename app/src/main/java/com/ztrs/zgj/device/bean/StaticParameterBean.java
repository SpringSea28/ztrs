package com.ztrs.zgj.device.bean;

import androidx.annotation.NonNull;

import com.ztrs.zgj.LogUtils;

public class StaticParameterBean implements Cloneable{
    private static final String TAG = StaticParameterBean.class.getSimpleName();
    byte towerType;//塔机类型
    int upWeightArmLen;
    int balanceArmLen;
    int balanceArmWidth;
    int balanceWeightSagHeight;
    int towerCapHeight;
    int towerHeight;
    int moveArmDistance;
    int climbingFrameSize;
    int introducePlatformAngle;
    int towerRelativeHeight;
    int towerX;
    int towerY;
    int walkingTrackAngle;
    byte carNumber;
    byte liftingNumber;
    byte[] towerCraneType;//塔吊型号
    byte magnification;//倍率
    int windSpeedAlarmValue;
    int windSpeedWarningValue;
    int slopeXAlarmValue;
    int slopeXWarningValue;
    int slopeYAlarmValue;
    int slopeYWarningValue;
    byte towerNumber;
    short standardSection;

    public byte getTowerType() {
        return towerType;
    }

    public void setTowerType(byte towerType) {
        this.towerType = towerType;
    }

    public int getUpWeightArmLen() {
        return upWeightArmLen;
    }

    public void setUpWeightArmLen(int upWeightArmLen) {
        this.upWeightArmLen = upWeightArmLen;
    }

    public int getBalanceArmLen() {
        return balanceArmLen;
    }

    public void setBalanceArmLen(int balanceArmLen) {
        this.balanceArmLen = balanceArmLen;
    }

    public int getBalanceArmWidth() {
        return balanceArmWidth;
    }

    public void setBalanceArmWidth(int balanceArmWidth) {
        this.balanceArmWidth = balanceArmWidth;
    }

    public int getBalanceWeightSagHeight() {
        return balanceWeightSagHeight;
    }

    public void setBalanceWeightSagHeight(int balanceWeightSagHeight) {
        this.balanceWeightSagHeight = balanceWeightSagHeight;
    }

    public int getTowerCapHeight() {
        return towerCapHeight;
    }

    public void setTowerCapHeight(int towerCapHeight) {
        this.towerCapHeight = towerCapHeight;
    }

    public int getTowerHeight() {
        return towerHeight;
    }

    public void setTowerHeight(int towerHeight) {
        this.towerHeight = towerHeight;
    }

    public int getMoveArmDistance() {
        return moveArmDistance;
    }

    public void setMoveArmDistance(int moveArmDistance) {
        this.moveArmDistance = moveArmDistance;
    }

    public int getClimbingFrameSize() {
        return climbingFrameSize;
    }

    public void setClimbingFrameSize(int climbingFrameSize) {
        this.climbingFrameSize = climbingFrameSize;
    }

    public int getIntroducePlatformAngle() {
        return introducePlatformAngle;
    }

    public void setIntroducePlatformAngle(int introducePlatformAngle) {
        this.introducePlatformAngle = introducePlatformAngle;
    }

    public int getTowerRelativeHeight() {
        return towerRelativeHeight;
    }

    public void setTowerRelativeHeight(int towerRelativeHeight) {
        this.towerRelativeHeight = towerRelativeHeight;
    }

    public int getTowerX() {
        return towerX;
    }

    public void setTowerX(int towerX) {
        this.towerX = towerX;
    }

    public int getTowerY() {
        return towerY;
    }

    public void setTowerY(int towerY) {
        this.towerY = towerY;
    }

    public int getWalkingTrackAngle() {
        return walkingTrackAngle;
    }

    public void setWalkingTrackAngle(int walkingTrackAngle) {
        this.walkingTrackAngle = walkingTrackAngle;
    }

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

    public byte[] getTowerCraneType() {
        return towerCraneType;
    }

    public String getTowerCraneTypeStr(){
        if(towerCraneType == null){
            return "";
        }
        int zeroOffset = 0;
        for(int i=0;i<towerCraneType.length;i++){
            if(towerCraneType[i]== 0){
                zeroOffset = i;
                break;
            }
        }
        String s = new String(towerCraneType);
        return s.substring(0,zeroOffset);
    }

    public void setTowerCraneType(byte[] towerCraneType) {
        this.towerCraneType = towerCraneType;
    }

    public byte getMagnification() {
        return magnification;
    }

    public void setMagnification(byte magnification) {
        this.magnification = magnification;
    }

    public int getWindSpeedAlarmValue() {
        return windSpeedAlarmValue;
    }

    public void setWindSpeedAlarmValue(int windSpeedAlarmValue) {
        this.windSpeedAlarmValue = windSpeedAlarmValue;
    }

    public int getWindSpeedWarningValue() {
        return windSpeedWarningValue;
    }

    public void setWindSpeedWarningValue(int windSpeedWarningValue) {
        this.windSpeedWarningValue = windSpeedWarningValue;
    }

    public int getSlopeXAlarmValue() {
        return slopeXAlarmValue;
    }

    public void setSlopeXAlarmValue(int slopeXAlarmValue) {
        this.slopeXAlarmValue = slopeXAlarmValue;
    }

    public int getSlopeXWarningValue() {
        return slopeXWarningValue;
    }

    public void setSlopeXWarningValue(int slopeXWarningValue) {
        this.slopeXWarningValue = slopeXWarningValue;
    }

    public int getSlopeYAlarmValue() {
        return slopeYAlarmValue;
    }

    public void setSlopeYAlarmValue(int slopeYAlarmValue) {
        this.slopeYAlarmValue = slopeYAlarmValue;
    }

    public int getSlopeYWarningValue() {
        return slopeYWarningValue;
    }

    public void setSlopeYWarningValue(int slopeYWarningValue) {
        this.slopeYWarningValue = slopeYWarningValue;
    }

    public byte getTowerNumber() {
        return towerNumber;
    }

    public void setTowerNumber(byte towerNumber) {
        this.towerNumber = towerNumber;
    }

    public short getStandardSection() {
        return standardSection;
    }

    public void setStandardSection(short standardSection) {
        this.standardSection = standardSection;
    }

    @NonNull
    @Override
    public StaticParameterBean clone() throws CloneNotSupportedException {
        return (StaticParameterBean)super.clone();
    }
}
