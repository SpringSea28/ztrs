package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.HeightCalibrationBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.HeightCalibrationMessage;

public class HeightCalibrationProtocol extends BaseProtocol{
    private static final String TAG= HeightCalibrationProtocol.class.getSimpleName();

    public static final byte CMD_HEIGHT_CALIBRATION = (byte) 0x35;//高度标定

    private DeviceOperateInterface deviceOperateInterface;

    public HeightCalibrationProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
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

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    private boolean parseData(byte[] data) {
        if (data.length != 24) {
            LogUtils.LogE(TAG, "data length error");
            return false;
        }
        int i= 0;
        HeightCalibrationBean heightCalibrationBean = deviceOperateInterface.getZtrsDevice().getHeightCalibrationBean();
        long current1 = (((data[0]&0x00ff) << 24)
                | ((data[1]&0x00ff) << 16)
                | ((data[2]&0x00ff) << 8)
                | (data[3]&0x00ff))&0x00FFFFFFFFl;
        heightCalibrationBean.setCurrent1(current1);
        int calibration1 =((data[4]&0xffffffff) << 8) |(data[5]&0x00ff);
        heightCalibrationBean.setCalibration1(calibration1);
        long current2= (((data[6]&0x00ff) << 24)
                | ((data[7]&0x00ff) << 16)
                | ((data[8]&0x00ff) << 8)
                | (data[9]&0x00ff))&0x00FFFFFFFFl;
        heightCalibrationBean.setCurrent2(current2);
        int calibration2=((data[10]&0xffffffff) << 8) |(data[11]&0x00ff);
        heightCalibrationBean.setCalibration2(calibration2);
        int lowAlarmValue=((data[12]&0xffffffff) << 8) |(data[13]&0x00ff);
        heightCalibrationBean.setLowAlarmValue(lowAlarmValue);
        int lowWarnValue=((data[14]&0xffffffff) << 8) |(data[15]&0x00ff);
        heightCalibrationBean.setLowWarnValue(lowWarnValue);
        i = 16;
        byte lowAlarmRelayControl = data[i];
        heightCalibrationBean.setLowAlarmRelayControl(lowAlarmRelayControl);
        i = i+1;
        byte lowAlarmRelayOutput = data[i];
        heightCalibrationBean.setLowAlarmRelayOutput(lowAlarmRelayOutput);
        i= i+1;
        int highAlarmValue=((data[i]&0xffffffff) << 8) |(data[i+1]&0x00ff);
        heightCalibrationBean.setHighAlarmValue(highAlarmValue);
        i= i+2;
        int highWarnValue=((data[i]&0xffffffff) << 8) |(data[i+1]&0x00ff);
        heightCalibrationBean.setHighWarnValue(highWarnValue);
        i = i+2;
        byte highAlarmRelayControl = data[i];
        heightCalibrationBean.setHighAlarmRelayControl(highAlarmRelayControl);
        i = i+1;
        byte highAlarmRelayOutput = data[i];
        heightCalibrationBean.setHighAlarmRelayOutput(highAlarmRelayOutput);
        return true;
    }

    public void heightCalibration(HeightCalibrationBean heightCalibrationBean){
        int i=0;
        byte[] data = new byte[24];
        long current1 = heightCalibrationBean.getCurrent1();
        data[0] = (byte)(current1>>24);
        data[1] = (byte)(current1>>16);
        data[2] = (byte)(current1>>8);
        data[3] = (byte)(current1);
        int calibration1 = heightCalibrationBean.getCalibration1();
        data[4] = (byte)(calibration1>>8);
        data[5] = (byte)(calibration1);
        long current2 = heightCalibrationBean.getCurrent2();
        data[6] = (byte)(current2>>24);
        data[7] = (byte)(current2>>16);
        data[8] = (byte)(current2>>8);
        data[9] = (byte)(current2);
        int calibration2 = heightCalibrationBean.getCalibration2();
        data[10] = (byte)(calibration2>>8);
        data[11] = (byte)(calibration2);
        int lowAlarmValue = heightCalibrationBean.getLowAlarmValue();
        data[12] = (byte)(lowAlarmValue>>8);
        data[13] = (byte)(lowAlarmValue);
        int lowWarnValue = heightCalibrationBean.getLowWarnValue();
        data[14] = (byte)(lowWarnValue>>8);
        data[15] = (byte)(lowWarnValue);
        i = 16;
        data[i] = heightCalibrationBean.getLowAlarmRelayControl();
        data[i+1] = heightCalibrationBean.getLowAlarmRelayOutput();
        i = i+2;
        int highAlarmValue = heightCalibrationBean.getHighAlarmValue();
        data[i] = (byte)(highAlarmValue>>8);
        data[i+1] = (byte)(highAlarmValue);
        i = i+2;
        int highWarnValue = heightCalibrationBean.getHighWarnValue();
        data[i] = (byte)(highWarnValue>>8);
        data[i+1] = (byte)(highWarnValue);
        i= i+2;
        data[i] = heightCalibrationBean.getHighAlarmRelayControl();
        data[i+1] = heightCalibrationBean.getHighAlarmRelayOutput();

        byte[] cmd = CommunicationProtocol.packetCmd(CMD_HEIGHT_CALIBRATION,data);
        deviceOperateInterface.sendDataToDevice(cmd);
        HeightCalibrationMessage msg = new HeightCalibrationMessage(CommunicationProtocol.seq++,cmd);
        msg.setCmdType(BaseMessage.TYPE_CMD);
        cmdSend(msg);
    }
    public void queryHeightCalibration(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_HEIGHT_CALIBRATION,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        HeightCalibrationMessage msg = new HeightCalibrationMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
