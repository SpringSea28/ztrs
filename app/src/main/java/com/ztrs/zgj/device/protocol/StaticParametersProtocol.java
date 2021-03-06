package com.ztrs.zgj.device.protocol;

import android.util.Log;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.StaticParameterBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.StaticParameterMessage;

public class StaticParametersProtocol extends BaseProtocol{
    private static final String TAG= StaticParametersProtocol.class.getSimpleName();

    public static final byte CMD_STATIC_PARAMETER = (byte) 0x22;//静态参数设置
    public static final byte TOWER_TYPE_CAP = 0X00;
    public static final byte TOWER_TYPE_ALIGN_HEAD= 0X01;
    public static final byte TOWER_TYPE_MOVE_ARM = 0X02;
    public static final byte TOWER_TYPE_WALKING_CAP = 0X10;
    public static final byte TOWER_TYPE_WALKING_ALIGN_HEAD= 0X11;
    public static final byte TOWER_TYPE_WALKING_MOVE_ARM = 0X12;

    private static final int TOWER_TYPE_OFFSET = 0;
    private static final int UP_WEIGHT_ARM_LEN_OFFSET = TOWER_TYPE_OFFSET + 1;
    private static final int BALANCE_ARM_LEN_OFFSET = UP_WEIGHT_ARM_LEN_OFFSET + 2;
    private static final int BALANCE_ARM_WIDTH_OFFSET = BALANCE_ARM_LEN_OFFSET + 2;
    private static final int BALANCE_WEIGHT_SAG_HEIGHT_OFFSET = BALANCE_ARM_WIDTH_OFFSET + 2;
    private static final int TOWER_CAP_HEIGHT_OFFSET = BALANCE_WEIGHT_SAG_HEIGHT_OFFSET + 2;
    private static final int TOWER_HEIGHT_OFFSET = TOWER_CAP_HEIGHT_OFFSET + 2;
    private static final int MOVE_ARM_DISTANCE_OFFSET = TOWER_HEIGHT_OFFSET + 2;
    private static final int CLIMBING_FRAME_SIZE_OFFSET = MOVE_ARM_DISTANCE_OFFSET + 2;
    private static final int INTRODUCE_PLATFORM_ANGLE_OFFSET = CLIMBING_FRAME_SIZE_OFFSET + 2;
    private static final int TOWER_RELATIVE_HEIGHT_OFFSET = INTRODUCE_PLATFORM_ANGLE_OFFSET + 2;
    private static final int TOWER_X_OFFSET = TOWER_RELATIVE_HEIGHT_OFFSET + 2;
    private static final int TOWER_Y_OFFSET = TOWER_X_OFFSET + 2;
    private static final int WALKING_TRACK_ANGLE_OFFSET = TOWER_Y_OFFSET + 2;
    private static final int CAR_NUMBER_OFFSET = WALKING_TRACK_ANGLE_OFFSET + 2;
    private static final int LIFTING_NUMBER_OFFSET = CAR_NUMBER_OFFSET;
    private static final int TOWER_CRANE_TYPE_OFFSET = LIFTING_NUMBER_OFFSET + 1;
    private static final int MAGNIFICATION_OFFSET = TOWER_CRANE_TYPE_OFFSET + 15;
    private static final int WIND_SPEED_ALARM_VALUE_OFFSET = MAGNIFICATION_OFFSET + 1;
    private static final int WIND_SPEED_WARNING_VALUE_OFFSET = WIND_SPEED_ALARM_VALUE_OFFSET + 2;
    private static final int SLOPE_X_ALARM_VALUE_OFFSET = WIND_SPEED_WARNING_VALUE_OFFSET + 2;
    private static final int SLOPE_X_WARNING_VALUE_OFFSET = SLOPE_X_ALARM_VALUE_OFFSET + 2;
    private static final int SLOPE_Y_ALARM_VALUE_OFFSET = SLOPE_X_WARNING_VALUE_OFFSET + 2;
    private static final int SLOPE_Y_WARNING_VALUE_OFFSET = SLOPE_Y_ALARM_VALUE_OFFSET + 2;
    private static final int TOWER_NUMBER_OFFSET = SLOPE_Y_WARNING_VALUE_OFFSET +2;
    private static final int STANDARD_SECTION_OFFSET = TOWER_NUMBER_OFFSET +1;


    private DeviceOperateInterface deviceOperateInterface;

    public StaticParametersProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
        boolean success = parseData(data);
        if(!success){
            return;
        }
        StaticParameterMessage msg = new StaticParameterMessage();
        notifyDeviceReport(msg);

        byte[] ack = CommunicationProtocol.packetAck(CMD_STATIC_PARAMETER, new byte[]{0});
        deviceOperateInterface.sendDataToDevice(ack);
    }

    public void parseAck(byte[] data){
        if(data.length == 1){
            ackReceive(data);
            return;
        }
        boolean success = parseData(data);
        if(!success){
            ackReceiveError();
            return;
        }
        ackReceive(data);
    }

    private boolean parseData(byte[] data){
        if(data.length != STANDARD_SECTION_OFFSET+2){
            LogUtils.LogE(TAG,"data length error");
            return false;
        }
        StaticParameterBean staticParameterBean = deviceOperateInterface.getZtrsDevice().getStaticParameterBean();
        byte towerType= data[TOWER_TYPE_OFFSET];
        staticParameterBean.setTowerType(towerType);
        int upWeightArmLen = ((data[UP_WEIGHT_ARM_LEN_OFFSET]&0xffffffff) << 8)
                |(data[UP_WEIGHT_ARM_LEN_OFFSET+1]&0x00ff);
        staticParameterBean.setUpWeightArmLen(upWeightArmLen);
        int balanceArmLen = ((data[BALANCE_ARM_LEN_OFFSET]&0xffffffff) << 8)
                |(data[BALANCE_ARM_LEN_OFFSET+1]&0x00ff);
        staticParameterBean.setBalanceArmLen(balanceArmLen);
        int balanceArmWidth = ((data[BALANCE_ARM_WIDTH_OFFSET]&0xffffffff) << 8)
                |(data[BALANCE_ARM_WIDTH_OFFSET+1]&0x00ff);
        staticParameterBean.setBalanceArmWidth(balanceArmWidth);
        int balanceWeightSagHeight = ((data[BALANCE_WEIGHT_SAG_HEIGHT_OFFSET]&0xffffffff) << 8)
                |(data[BALANCE_WEIGHT_SAG_HEIGHT_OFFSET+1]&0x00ff);
        staticParameterBean.setBalanceWeightSagHeight(balanceWeightSagHeight);
        int towerCapHeight = ((data[TOWER_CAP_HEIGHT_OFFSET]&0xffffffff) << 8)
                |(data[TOWER_CAP_HEIGHT_OFFSET+1]&0x00ff);
        staticParameterBean.setTowerCapHeight(towerCapHeight);
        int towerHeight = ((data[TOWER_HEIGHT_OFFSET]&0xffffffff) << 8)
                |(data[TOWER_HEIGHT_OFFSET+1]&0x00ff);
        staticParameterBean.setTowerHeight(towerHeight);
        int moveArmDistance = ((data[MOVE_ARM_DISTANCE_OFFSET]&0xffffffff) << 8)
                |(data[MOVE_ARM_DISTANCE_OFFSET+1]&0x00ff);
        staticParameterBean.setMoveArmDistance(moveArmDistance);
        int climbingFrameSize = ((data[CLIMBING_FRAME_SIZE_OFFSET]&0xffffffff) << 8)
                |(data[CLIMBING_FRAME_SIZE_OFFSET+1]&0x00ff);
        staticParameterBean.setClimbingFrameSize(climbingFrameSize);
        int introducePlatformAngle = ((data[INTRODUCE_PLATFORM_ANGLE_OFFSET]&0xffffffff) << 8)
                |(data[INTRODUCE_PLATFORM_ANGLE_OFFSET+1]&0x00ff);
        staticParameterBean.setIntroducePlatformAngle(introducePlatformAngle);
        int towerRelativeHeight = ((data[TOWER_RELATIVE_HEIGHT_OFFSET]&0xffffffff) << 8)
                |(data[TOWER_RELATIVE_HEIGHT_OFFSET+1]&0x00ff);
        staticParameterBean.setTowerRelativeHeight(towerRelativeHeight);
        int towerX = ((data[TOWER_X_OFFSET]&0xffffffff) << 8)
                |(data[TOWER_X_OFFSET+1]&0x00ff);
        staticParameterBean.setTowerX(towerX);
        int towerY = ((data[TOWER_Y_OFFSET]&0xffffffff) << 8)
                |(data[TOWER_Y_OFFSET+1]&0x00ff);
        staticParameterBean.setTowerY(towerY);
        int walkingTrackAngle = ((data[WALKING_TRACK_ANGLE_OFFSET]&0xffffffff) << 8)
                |(data[WALKING_TRACK_ANGLE_OFFSET+1]&0x00ff);
        staticParameterBean.setWalkingTrackAngle(walkingTrackAngle);
        byte carNumber= (byte)((data[CAR_NUMBER_OFFSET] &0X00F0)>>4);
        staticParameterBean.setCarNumber(carNumber);
        byte liftingNumber= (byte)(data[LIFTING_NUMBER_OFFSET] & 0x0F);
        staticParameterBean.setLiftingNumber(liftingNumber);
        byte[] towerCraneType = new byte[15];
        System.arraycopy(data,TOWER_CRANE_TYPE_OFFSET,towerCraneType,0,15);
        staticParameterBean.setTowerCraneType(towerCraneType);
        byte magnification= data[MAGNIFICATION_OFFSET];
        staticParameterBean.setMagnification(magnification);
        int windSpeedAlarmValue = ((data[WIND_SPEED_ALARM_VALUE_OFFSET]&0xffffffff) << 8)
                |(data[WIND_SPEED_ALARM_VALUE_OFFSET+1]&0x00ff);
        Log.e(TAG,"windSpeedAlarmValue: "+windSpeedAlarmValue);
        staticParameterBean.setWindSpeedAlarmValue(windSpeedAlarmValue);
        int windSpeedWarningValue = ((data[WIND_SPEED_WARNING_VALUE_OFFSET]&0xffffffff) << 8)
                |(data[WIND_SPEED_WARNING_VALUE_OFFSET+1]&0x00ff);
        Log.e(TAG,"windSpeedWarningValue: "+windSpeedWarningValue);
        staticParameterBean.setWindSpeedWarningValue(windSpeedWarningValue);
        int slopeXAlarmValue = ((data[SLOPE_X_ALARM_VALUE_OFFSET]&0xffffffff) << 8)
                |(data[SLOPE_X_ALARM_VALUE_OFFSET+1]&0x00ff);
        staticParameterBean.setSlopeXAlarmValue(slopeXAlarmValue);
        int slopeXWarningValue = ((data[SLOPE_X_WARNING_VALUE_OFFSET]&0xffffffff) << 8)
                |(data[SLOPE_X_WARNING_VALUE_OFFSET+1]&0x00ff);
        staticParameterBean.setSlopeXWarningValue(slopeXWarningValue);
        int slopeYAlarmValue = ((data[SLOPE_Y_ALARM_VALUE_OFFSET]&0xffffffff) << 8)
                |(data[SLOPE_Y_ALARM_VALUE_OFFSET+1]&0x00ff);
        staticParameterBean.setSlopeYAlarmValue(slopeYAlarmValue);
        int slopeYWarningValue = ((data[SLOPE_Y_WARNING_VALUE_OFFSET]&0xffffffff) << 8)
                |(data[SLOPE_Y_WARNING_VALUE_OFFSET+1]&0x00ff);
        staticParameterBean.setSlopeYWarningValue(slopeYWarningValue);
        byte towerNumber = data[TOWER_NUMBER_OFFSET];
        staticParameterBean.setTowerNumber(towerNumber);
        short standardSection = (short) (((data[STANDARD_SECTION_OFFSET]&0xffffffff) << 8)
                |(data[STANDARD_SECTION_OFFSET+1]&0x00ff));
        staticParameterBean.setStandardSection(standardSection);
        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void setStaticParameter(StaticParameterBean staticParameter){
        byte[] data = new byte[STANDARD_SECTION_OFFSET+2];

        byte towerType= staticParameter.getTowerType();
        data[TOWER_TYPE_OFFSET] = towerType;

        int upWeightArmLen = staticParameter.getUpWeightArmLen();
        data[UP_WEIGHT_ARM_LEN_OFFSET] = (byte)(upWeightArmLen>>8);
        data[UP_WEIGHT_ARM_LEN_OFFSET+1] = (byte)upWeightArmLen;

        int balanceArmLen = staticParameter.getBalanceArmLen();
        data[BALANCE_ARM_LEN_OFFSET] = (byte)(balanceArmLen>>8);
        data[BALANCE_ARM_LEN_OFFSET+1] = (byte)balanceArmLen;

        int balanceArmWidth = staticParameter.getBalanceArmWidth();
        data[BALANCE_ARM_WIDTH_OFFSET] = (byte)(balanceArmWidth>>8);
        data[BALANCE_ARM_WIDTH_OFFSET+1] = (byte)balanceArmWidth;

        int balanceWeightSagHeight = staticParameter.getBalanceWeightSagHeight();
        data[BALANCE_WEIGHT_SAG_HEIGHT_OFFSET] = (byte)(balanceWeightSagHeight>>8);
        data[BALANCE_WEIGHT_SAG_HEIGHT_OFFSET+1] = (byte)balanceWeightSagHeight;

        int towerCapHeight = staticParameter.getTowerCapHeight();
        data[TOWER_CAP_HEIGHT_OFFSET] = (byte)(towerCapHeight>>8);
        data[TOWER_CAP_HEIGHT_OFFSET+1] = (byte)towerCapHeight;

        int towerHeight = staticParameter.getTowerHeight();
        data[TOWER_HEIGHT_OFFSET] = (byte)(towerHeight>>8);
        data[TOWER_HEIGHT_OFFSET+1] = (byte)towerHeight;

//        Log.e("wch",""+data[TOWER_CAP_HEIGHT_OFFSET]);
//        Log.e("wch",""+data[TOWER_CAP_HEIGHT_OFFSET+1]);
//        Log.e("wch",""+data[TOWER_HEIGHT_OFFSET]);
//        Log.e("wch",""+data[TOWER_HEIGHT_OFFSET+1]);

        int moveArmDistance = staticParameter.getMoveArmDistance();
        data[MOVE_ARM_DISTANCE_OFFSET] = (byte)(moveArmDistance>>8);
        data[MOVE_ARM_DISTANCE_OFFSET+1] = (byte)moveArmDistance;

        int climbingFrameSize = staticParameter.getClimbingFrameSize();
        data[CLIMBING_FRAME_SIZE_OFFSET] = (byte)(climbingFrameSize>>8);
        data[CLIMBING_FRAME_SIZE_OFFSET+1] = (byte)climbingFrameSize;

        int introducePlatformAngle = staticParameter.getIntroducePlatformAngle();
        data[INTRODUCE_PLATFORM_ANGLE_OFFSET] = (byte)(introducePlatformAngle>>8);
        data[INTRODUCE_PLATFORM_ANGLE_OFFSET+1] = (byte)introducePlatformAngle;

        int towerRelativeHeight = staticParameter.getTowerRelativeHeight();
        data[TOWER_RELATIVE_HEIGHT_OFFSET] = (byte)(towerRelativeHeight>>8);
        data[TOWER_RELATIVE_HEIGHT_OFFSET+1] = (byte)towerRelativeHeight;

        int towerX = staticParameter.getTowerX();
        data[TOWER_X_OFFSET] = (byte)(towerX>>8);
        data[TOWER_X_OFFSET+1] = (byte)towerX;

        int towerY  = staticParameter.getTowerY();
        data[TOWER_Y_OFFSET] = (byte)(towerY>>8);
        data[TOWER_Y_OFFSET+1] = (byte)towerY;

        int walkingTrackAngle  = staticParameter.getWalkingTrackAngle();
        data[WALKING_TRACK_ANGLE_OFFSET] = (byte)(walkingTrackAngle>>8);
        data[WALKING_TRACK_ANGLE_OFFSET+1] = (byte)walkingTrackAngle;

        byte carNumber= staticParameter.getCarNumber();
        data[CAR_NUMBER_OFFSET] |= carNumber<<4;
        byte liftingNumber= staticParameter.getLiftingNumber();
        data[LIFTING_NUMBER_OFFSET] |= liftingNumber;
        byte[] towerCraneType= staticParameter.getTowerCraneType();
        if(towerCraneType !=null) {
            System.arraycopy(towerCraneType, 0, data, TOWER_CRANE_TYPE_OFFSET,
                    towerCraneType.length > 15 ? 15 : towerCraneType.length);
        }
        byte magnification= staticParameter.getMagnification();
        data[MAGNIFICATION_OFFSET] = magnification;

        int windSpeedAlarmValue  = staticParameter.getWindSpeedAlarmValue();
        data[WIND_SPEED_ALARM_VALUE_OFFSET] = (byte)(windSpeedAlarmValue>>8);
        data[WIND_SPEED_ALARM_VALUE_OFFSET+1] = (byte)windSpeedAlarmValue;

        int windSpeedWarningValue = staticParameter.getWindSpeedWarningValue();
        data[WIND_SPEED_WARNING_VALUE_OFFSET] = (byte)(windSpeedWarningValue>>8);
        data[WIND_SPEED_WARNING_VALUE_OFFSET+1] = (byte)windSpeedWarningValue;

        int slopeXAlarmValue   = staticParameter.getSlopeXAlarmValue();
        data[SLOPE_X_ALARM_VALUE_OFFSET] = (byte)(slopeXAlarmValue>>8);
        data[SLOPE_X_ALARM_VALUE_OFFSET+1] = (byte)slopeXAlarmValue;

        int slopeXWarningValue  = staticParameter.getSlopeXWarningValue();
        data[SLOPE_X_WARNING_VALUE_OFFSET] = (byte)(slopeXWarningValue>>8);
        data[SLOPE_X_WARNING_VALUE_OFFSET+1] = (byte)slopeXWarningValue;

        int slopeYAlarmValue  = staticParameter.getSlopeYAlarmValue();
        data[SLOPE_Y_ALARM_VALUE_OFFSET] = (byte)(slopeYAlarmValue>>8);
        data[SLOPE_Y_ALARM_VALUE_OFFSET+1] = (byte)slopeYAlarmValue;

        int slopeYWarningValue   = staticParameter.getSlopeYWarningValue();
        data[SLOPE_Y_WARNING_VALUE_OFFSET] = (byte)(slopeYWarningValue>>8);
        data[SLOPE_Y_WARNING_VALUE_OFFSET+1] = (byte)slopeYWarningValue;

        byte towerNumber = staticParameter.getTowerNumber();
        data[TOWER_NUMBER_OFFSET] = towerNumber;

        short standardSection = staticParameter.getStandardSection();
        data[STANDARD_SECTION_OFFSET] = (byte)(standardSection>>8);
        data[STANDARD_SECTION_OFFSET+1] = (byte)(standardSection);

        byte[] cmd = CommunicationProtocol.packetCmd(CMD_STATIC_PARAMETER,data);
        deviceOperateInterface.sendDataToDevice(cmd);
        StaticParameterMessage msg = new StaticParameterMessage(CommunicationProtocol.seq++,cmd);
        msg.setCmdType(BaseMessage.TYPE_CMD);
        cmdSend(msg);
    }

    public void queryStaticParameter(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_STATIC_PARAMETER,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        StaticParameterMessage msg = new StaticParameterMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
