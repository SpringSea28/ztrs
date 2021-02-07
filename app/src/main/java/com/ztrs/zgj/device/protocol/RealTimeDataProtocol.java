package com.ztrs.zgj.device.protocol;

import android.util.Log;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.RealTimeDataBean;
import com.ztrs.zgj.device.eventbus.RealTimeDataMessage;

public class RealTimeDataProtocol extends BaseProtocol{
    private static final String TAG= RealTimeDataProtocol.class.getSimpleName();

    public static final byte CMD_REAL_TIME_DATA = (byte) 0x23;//实时数据
    private static final int DATA_LENGTH = 46+6;

    public static final int DIRECTION_STOP = 0;
    public static final int DIRECTION_UP = 1;
    public static final int DIRECTION_DOWN = 2;
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = 2;
    public static final int DIRECTION_BEFORE_OUT = 2;
    public static final int DIRECTION_AFTER_IN = 1;

    private DeviceOperateInterface deviceOperateInterface;

    public RealTimeDataProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
        boolean success = parseData(data);
        if(!success){
            return;
        }
        RealTimeDataMessage realTimeDataMessage =
                new RealTimeDataMessage();
        notifyDeviceReport(realTimeDataMessage);
        byte[] ack = CommunicationProtocol.packetAck(CMD_REAL_TIME_DATA, new byte[]{0});
        deviceOperateInterface.sendDataToDevice(ack);
    }

    public void parseAck(byte[] data){
        boolean success = parseData(data);
        if(!success){
            ackReceiveError();
            return;
        }
        ackReceive(data);
    }

    private boolean parseData(byte[] data){
        if(data.length != DATA_LENGTH){
            LogUtils.LogE(TAG,"data length error");
            return false;
        }
        RealTimeDataBean realTimeDataBean = deviceOperateInterface.getZtrsDevice().getRealTimeDataBean();
        byte carNumber= (byte)((data[0] &0X00F0)>>4);
        realTimeDataBean.setCarNumber(carNumber);
        byte liftingNumber= (byte)(data[0] & 0x0F);
        realTimeDataBean.setLiftingNumber(liftingNumber);
        byte magnification = data[1];
        realTimeDataBean.setMagnification(magnification);
        byte workMode = (byte)((data[2] &0X00C0)>>6);
        realTimeDataBean.setWorkMode(workMode);
        boolean bypassState = (data[2] &0X20) == 0x20;
        realTimeDataBean.setBypassState(bypassState);
        boolean connectStateByDeviceWithRemoteCenter= (data[2] &0X10) == 0x10;
        realTimeDataBean.setConnectStateByDeviceWithRemoteCenter(connectStateByDeviceWithRemoteCenter);
        boolean plcState = (data[2] &0X08) == 0x08;
        realTimeDataBean.setPlcState(plcState);
        boolean network = (data[2] &0X04) == 0x04;
        realTimeDataBean.setNetwork(network);

        byte upWeightSensorState = (byte)((data[3] &0X00C0)>>6);
        realTimeDataBean.setUpWeightSensorState(upWeightSensorState);
        byte heightSensorState = (byte)((data[3] &0X0030)>>4);
        realTimeDataBean.setHeightSensorState(heightSensorState);
        byte turnAroundSensorState = (byte)((data[3] &0X0C)>>2);
        realTimeDataBean.setTurnAroundSensorState(turnAroundSensorState);
        byte amplitudeSensorState = (byte)(data[3] &0X03);
        realTimeDataBean.setAmplitudeSensorState(amplitudeSensorState);
        byte walkingSensorState = (byte)((data[4] &0X00C0)>>6);
        realTimeDataBean.setWalkingSensorState(walkingSensorState);
        byte windSpeedSensorState = (byte)((data[4] &0X0C)>>2);
        realTimeDataBean.setWindSpeedSensorState(windSpeedSensorState);
        byte slopeSensorState = (byte)(data[4] &0X03);
        realTimeDataBean.setSlopeSensorState(slopeSensorState);
        byte upWeightSensorState2= (byte)((data[5] &0X00C0)>>6);
        realTimeDataBean.setUpWeightSensorState(upWeightSensorState);
        byte heightSensorState2 = (byte)((data[5] &0X30)>>4);
        realTimeDataBean.setHeightSensorState2(heightSensorState2);
        byte amplitudeSensorState2= (byte)(data[5] &0X03);
        realTimeDataBean.setAmplitudeSensorState2(amplitudeSensorState2);
        boolean electronicTorqueLimitState4  = (data[6] &0X80) == 0x80;
        realTimeDataBean.setElectronicTorqueLimitState4(electronicTorqueLimitState4);
        boolean electronicTorqueLimitState3  = (data[6] &0X40) == 0x40;
        realTimeDataBean.setElectronicTorqueLimitState3(electronicTorqueLimitState3);
        boolean electronicTorqueLimitState2  = (data[6] &0X20) == 0x20;
        realTimeDataBean.setElectronicTorqueLimitState2(electronicTorqueLimitState2);
        boolean electronicTorqueLimitState1  = (data[6] &0X10) == 0x10;
        realTimeDataBean.setElectronicTorqueLimitState1(electronicTorqueLimitState1);
        boolean electronicWeightLimitState4  = (data[6] &0X08) == 0x08;
        realTimeDataBean.setElectronicWeightLimitState4(electronicWeightLimitState4);
        boolean electronicWeightLimitState3  = (data[6] &0X04) == 0x04;
        realTimeDataBean.setElectronicWeightLimitState3(electronicWeightLimitState3);
        boolean electronicWeightLimitState2  = (data[6] &0X02) == 0x02;
        realTimeDataBean.setElectronicWeightLimitState2(electronicWeightLimitState2);
        boolean electronicWeightLimitState1  = (data[6] &0X01) == 0x01;
        realTimeDataBean.setElectronicWeightLimitState1(electronicWeightLimitState1);

        boolean outputUpUpStopLimit= (data[7] &0X80) == 0x80;
        realTimeDataBean.setOutputUpUpStopLimit(outputUpUpStopLimit);
        boolean outputUpuPSlowLimit = (data[7] &0X40) == 0x40;
        realTimeDataBean.setOutputUpuPSlowLimit(outputUpuPSlowLimit);
        boolean outputDownUpStopLimit  = (data[7] &0X20) == 0x20;
        realTimeDataBean.setOutputDownUpStopLimit(outputDownUpStopLimit);
        boolean outputDownUpSlowLimit= (data[7] &0X10) == 0x10;
        realTimeDataBean.setOutputDownUpSlowLimit(outputDownUpSlowLimit);
        boolean outputLeftAroundStopLimit = (data[7] &0X08) == 0x08;
        realTimeDataBean.setOutputLeftAroundStopLimit(outputLeftAroundStopLimit);
        boolean outputLeftAroundSlowLimit = (data[7] &0X04) == 0x04;
        realTimeDataBean.setOutputLeftAroundSlowLimit(outputLeftAroundSlowLimit);
        boolean outputRightAroundStopLimit = (data[7] &0X02) == 0x02;
        realTimeDataBean.setOutputRightAroundStopLimit(outputRightAroundStopLimit);
        boolean outputRightAroundSlowLimit = (data[7] &0X01) == 0x01;
        realTimeDataBean.setOutputRightAroundSlowLimit(outputRightAroundSlowLimit);
        boolean outputOutLuffingStopLimit= (data[8] &0X80) == 0x80;
        realTimeDataBean.setOutputOutLuffingStopLimit(outputOutLuffingStopLimit);
        boolean outputOutLuffingSlowLimit = (data[8] &0X40) == 0x40;
        realTimeDataBean.setOutputOutLuffingSlowLimit(outputOutLuffingSlowLimit);
        boolean outputInLuffingStopLimit  = (data[8] &0X20) == 0x20;
        realTimeDataBean.setOutputInLuffingStopLimit(outputInLuffingStopLimit);
        boolean outputInLuffingSlowLimit= (data[8] &0X10) == 0x10;
        realTimeDataBean.setOutputInLuffingSlowLimit(outputInLuffingSlowLimit);
        Log.e(TAG,"outputOutLuffingStopLimit:"+outputOutLuffingStopLimit);
        Log.e(TAG,"outputOutLuffingSlowLimit:"+outputOutLuffingSlowLimit);
        Log.e(TAG,"outputInLuffingStopLimit:"+outputInLuffingStopLimit);
        Log.e(TAG,"outputInLuffingSlowLimit:"+outputInLuffingSlowLimit);
        Log.e(TAG,"data8:"+LogUtils.toHexString(data[8]));


        //1.27协议
        boolean torqueWarnLimit = (data[8] &0X08) == 0x08;
        realTimeDataBean.setTorqueWarnLimit(torqueWarnLimit);
        boolean torqueAlarmLimit = (data[8] &0X04) == 0x04;
        realTimeDataBean.setTorqueAlarmLimit(torqueAlarmLimit);
        boolean weightWarnLimit = (data[8] &0X02) == 0x02;
        realTimeDataBean.setWeightWarnLimit(weightWarnLimit);
        boolean weightAlarmLimit = (data[8] &0X01) == 0x01;
        realTimeDataBean.setWeightAlarmLimit(weightAlarmLimit);
//        Log.e(TAG,"data[8]: "+LogUtils.toHexString(data[8]));

        boolean electronicWindAlarmLimit = (data[9] &0X08) == 0x08;
        realTimeDataBean.setElectronicWindAlarmLimit(electronicWindAlarmLimit);
        boolean electronicWindWarningLimit= (data[9] &0X04) == 0x04;
        realTimeDataBean.setElectronicWindWarningLimit(electronicWindWarningLimit);

        boolean strokeUpUpStopLimit= (data[10] &0X80) == 0x80;
        realTimeDataBean.setStrokeUpUpStopLimit(strokeUpUpStopLimit);
        boolean strokeUpuPSlowLimit = (data[10] &0X40) == 0x40;
        realTimeDataBean.setStrokeUpuPSlowLimit(strokeUpuPSlowLimit);
        boolean strokeDownUpStopLimit  = (data[10] &0X20) == 0x20;
        realTimeDataBean.setStrokeDownUpStopLimit(strokeDownUpStopLimit);
        boolean strokeDownUpSlowLimit= (data[10] &0X10) == 0x10;
        realTimeDataBean.setStrokeDownUpSlowLimit(strokeDownUpSlowLimit);
        boolean strokeLeftAroundStopLimit = (data[10] &0X08) == 0x08;
        realTimeDataBean.setStrokeLeftAroundStopLimit(strokeLeftAroundStopLimit);
        boolean strokeLeftAroundSlowLimit = (data[10] &0X04) == 0x04;
        realTimeDataBean.setStrokeLeftAroundSlowLimit(strokeLeftAroundSlowLimit);
        boolean strokeRightAroundStopLimit = (data[10] &0X02) == 0x02;
        realTimeDataBean.setStrokeRightAroundStopLimit(strokeRightAroundStopLimit);
        boolean strokeRightAroundSlowLimit = (data[10] &0X01) == 0x01;
        realTimeDataBean.setStrokeRightAroundSlowLimit(strokeRightAroundSlowLimit);
        boolean strokeOutLuffingStopLimit= (data[11] &0X80) == 0x80;
        realTimeDataBean.setStrokeInLuffingStopLimit(strokeOutLuffingStopLimit);
        boolean strokeOutLuffingSlowLimit = (data[11] &0X04) == 0x04;
        realTimeDataBean.setStrokeOutLuffingSlowLimit(strokeOutLuffingSlowLimit);
        boolean strokeInLuffingStopLimit  = (data[11] &0X02) == 0x02;
        realTimeDataBean.setStrokeInLuffingStopLimit(strokeInLuffingStopLimit);
        boolean strokeInLuffingSlowLimit= (data[11] &0X10) == 0x10;
        realTimeDataBean.setStrokeInLuffingSlowLimit(strokeInLuffingSlowLimit);

        boolean areaUpUpStopLimit= (data[12] &0X80) == 0x80;
        realTimeDataBean.setAreaUpUpStopLimit(areaUpUpStopLimit);
        boolean areaUpuPSlowLimit = (data[12] &0X40) == 0x40;
        realTimeDataBean.setAreaUpuPSlowLimit(areaUpuPSlowLimit);
        boolean areaDownUpStopLimit  = (data[12] &0X20) == 0x20;
        realTimeDataBean.setAreaDownUpStopLimit(areaDownUpStopLimit);
        boolean areaDownUpSlowLimit= (data[12] &0X10) == 0x10;
        realTimeDataBean.setAreaDownUpSlowLimit(areaDownUpSlowLimit);
        boolean areaLeftAroundStopLimit = (data[12] &0X08) == 0x08;
        realTimeDataBean.setAreaLeftAroundStopLimit(areaLeftAroundStopLimit);
        boolean areaLeftAroundSlowLimit = (data[12] &0X04) == 0x04;
        realTimeDataBean.setAreaLeftAroundSlowLimit(areaLeftAroundSlowLimit);
        boolean areaRightAroundStopLimit = (data[12] &0X02) == 0x02;
        realTimeDataBean.setAreaRightAroundStopLimit(areaRightAroundStopLimit);
        boolean areaRightAroundSlowLimit = (data[12] &0X01) == 0x01;
        realTimeDataBean.setAreaRightAroundSlowLimit(areaRightAroundSlowLimit);
        boolean areaOutLuffingStopLimit= (data[13] &0X80) == 0x80;
        realTimeDataBean.setAreaInLuffingStopLimit(areaOutLuffingStopLimit);
        boolean areaOutLuffingSlowLimit = (data[13] &0X04) == 0x04;
        realTimeDataBean.setAreaOutLuffingSlowLimit(areaOutLuffingSlowLimit);
        boolean areaInLuffingStopLimit  = (data[13] &0X02) == 0x02;
        realTimeDataBean.setAreaInLuffingStopLimit(areaInLuffingStopLimit);
        boolean areaInLuffingSlowLimit= (data[13] &0X10) == 0x10;
        realTimeDataBean.setAreaInLuffingSlowLimit(areaInLuffingSlowLimit);

        boolean preventCollisionUpUpStopLimit= (data[14] &0X80) == 0x80;
        realTimeDataBean.setPreventCollisionUpUpStopLimit(preventCollisionUpUpStopLimit);
        boolean preventCollisionUpuPSlowLimit = (data[14] &0X40) == 0x40;
        realTimeDataBean.setPreventCollisionUpuPSlowLimit(preventCollisionUpuPSlowLimit);
        boolean preventCollisionDownUpStopLimit  = (data[14] &0X20) == 0x20;
        realTimeDataBean.setPreventCollisionDownUpStopLimit(preventCollisionDownUpStopLimit);
        boolean preventCollisionDownUpSlowLimit= (data[14] &0X10) == 0x10;
        realTimeDataBean.setPreventCollisionDownUpSlowLimit(preventCollisionDownUpSlowLimit);
        boolean preventCollisionLeftAroundStopLimit = (data[14] &0X08) == 0x08;
        realTimeDataBean.setPreventCollisionLeftAroundStopLimit(preventCollisionLeftAroundStopLimit);
        boolean preventCollisionLeftAroundSlowLimit = (data[14] &0X04) == 0x04;
        realTimeDataBean.setPreventCollisionLeftAroundSlowLimit(preventCollisionLeftAroundSlowLimit);
        boolean preventCollisionRightAroundStopLimit = (data[14] &0X02) == 0x02;
        realTimeDataBean.setPreventCollisionRightAroundStopLimit(preventCollisionRightAroundStopLimit);
        boolean preventCollisionRightAroundSlowLimit = (data[14] &0X01) == 0x01;
        realTimeDataBean.setPreventCollisionRightAroundSlowLimit(preventCollisionRightAroundSlowLimit);
        boolean preventCollisionOutLuffingStopLimit= (data[15] &0X80) == 0x80;
        realTimeDataBean.setPreventCollisionInLuffingStopLimit(preventCollisionOutLuffingStopLimit);
        boolean preventCollisionOutLuffingSlowLimit = (data[15] &0X04) == 0x04;
        realTimeDataBean.setPreventCollisionOutLuffingSlowLimit(preventCollisionOutLuffingSlowLimit);
        boolean preventCollisionInLuffingStopLimit  = (data[15] &0X02) == 0x02;
        realTimeDataBean.setPreventCollisionInLuffingStopLimit(preventCollisionInLuffingStopLimit);
        boolean preventCollisionInLuffingSlowLimit= (data[15] &0X10) == 0x10;
        realTimeDataBean.setPreventCollisionInLuffingSlowLimit(preventCollisionInLuffingSlowLimit);

        int upWeightTorquePercent = ((data[16]&0xffffffff) << 8) |(data[17]&0x00ff);
        realTimeDataBean.setUpWeightTorquePercent(upWeightTorquePercent);
//                Log.e(TAG,"upWeightTorquePercent:"+upWeightTorquePercent);
//        Log.e(TAG,"data16:"+LogUtils.toHexString(data[16]));
//        Log.e(TAG,"data[17]:"+LogUtils.toHexString(data[17]));
        int upWeight= ((data[18]&0xffffffff) << 8) |(data[19]&0x00ff);
        realTimeDataBean.setUpWeight(upWeight);
        short height= (short) (((data[20]&0xffffffff) << 8) |(data[21]&0x00ff));
        realTimeDataBean.setHeight(height);
        int aroundAngle= ((data[22]&0xffffffff) << 8) |(data[23]&0x00ff);
        realTimeDataBean.setAroundAngle(aroundAngle);
        int amplitude = ((data[24]&0xffffffff) << 8) |(data[25]&0x00ff);
//        Log.e(TAG,"amplitude:"+amplitude);
//        Log.e(TAG,"data24:"+LogUtils.toHexString(data[24]));
//        Log.e(TAG,"data[25]:"+LogUtils.toHexString(data[25]));
        realTimeDataBean.setAmplitude(amplitude);
        int boomUpAngle= ((data[26]&0xffffffff) << 8) |(data[27]&0x00ff);
        realTimeDataBean.setBoomUpAngle(boomUpAngle);
        int windSpeed= ((data[28]&0xffffffff) << 8) |(data[29]&0x00ff);
        realTimeDataBean.setWindSpeed(windSpeed);
        int xWalking= ((data[30]&0xffffffff) << 8) |(data[31]&0x00ff);
        realTimeDataBean.setxWalking(xWalking);
        int yWalking= ((data[32]&0xffffffff) << 8) |(data[33]&0x00ff);
        realTimeDataBean.setyWalking(yWalking);
        int currentRatedLoad= ((data[34]&0xffffffff) << 8) |(data[35]&0x00ff);
        realTimeDataBean.setCurrentRatedLoad(currentRatedLoad);
        byte upState = data[36];
        realTimeDataBean.setUpState(upState);
        byte amplitudeState= data[37];
        realTimeDataBean.setAmplitudeState(amplitudeState);
        byte aroundState= data[38];
        realTimeDataBean.setAroundState(aroundState);
        int xSlope= ((data[39]&0xffffffff) << 8) |(data[40]&0x00ff);
        realTimeDataBean.setxSlope(xSlope);
        int ySlope= ((data[41]&0xffffffff) << 8) |(data[42]&0x00ff);
        realTimeDataBean.setySlope(ySlope);
        short torque = (short)(((data[43]&0xffffffff) << 8) |(data[44]&0x00ff));
        realTimeDataBean.setTorque(torque);
        Log.e(TAG,"torque:"+torque);
        Log.e(TAG,"data43:"+LogUtils.toHexString(data[43]));
        Log.e(TAG,"data[44]:"+LogUtils.toHexString(data[44]));
        byte windLevel = data[45];
        realTimeDataBean.setWindLevel(windLevel);
        byte wireRopeState = data[46];
        realTimeDataBean.setWireRopeState(wireRopeState);
        byte wireRopeDamageMagnification = data[47];
        realTimeDataBean.setWireRopeDamageMagnification(wireRopeDamageMagnification);
        int wireRopeDamageheight =  ((data[48]&0xff) << 8) |(data[49]&0x00ff);
        realTimeDataBean.setWireRopeDamageheight(wireRopeDamageheight);
        int wireRopeDamageValue = ((data[50]&0x00ff) << 8) |(data[51]&0x00ff);
        realTimeDataBean.setWireRopeDamageValue(wireRopeDamageValue);
        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void queryRealTimeData(byte frequency,byte number){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_REAL_TIME_DATA,new byte[]{frequency,number});
        deviceOperateInterface.sendDataToDevice(cmd);
        RealTimeDataMessage msg = new RealTimeDataMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
