package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.PreventCollisionCalibrationBean;
import com.ztrs.zgj.device.bean.WeightCalibrationBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.PreventCollisionCalibrationMessage;
import com.ztrs.zgj.device.eventbus.WeightCalibrationMessage;

public class PreventCollisionCalibrationProtocol extends BaseProtocol{
    private static final String TAG= PreventCollisionCalibrationProtocol.class.getSimpleName();

    public static final byte CMD_PREVENT_COLLISION_CALIBRATION = (byte) 0x61;//临塔防碰撞

    private DeviceOperateInterface deviceOperateInterface;

    public PreventCollisionCalibrationProtocol(DeviceOperateInterface deviceOperateInterface){
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
        if (data.length != 8) {
            LogUtils.LogE(TAG, "data length error");
            return false;
        }
        PreventCollisionCalibrationBean calibrationBean = deviceOperateInterface.getZtrsDevice().getPreventCollisionCalibrationBean();
        int i=0;
        int heightWarnValue=((data[i]&0xffffffff) << 8) |(data[i+1]&0x00ff);
        calibrationBean.setHeightWarnValue(heightWarnValue);
        i = i+2;
        int heightAlarmValue=((data[i]&0xffffffff) << 8) |(data[i+1]&0x00ff);
        calibrationBean.setHeightAlarmValue(heightAlarmValue);

        i = i+2;
        int distanceWarnValue=((data[i]&0xffffffff) << 8) |(data[i+1]&0x00ff);
        calibrationBean.setDistanceWarnValue(distanceWarnValue);
        i= i+2;
        int distanceAlarmValue=((data[i]&0xffffffff) << 8) |(data[i+1]&0x00ff);
        calibrationBean.setDistanceAlarmValue(distanceAlarmValue);
        return true;
    }

    public void preventCollisionCalibration(PreventCollisionCalibrationBean calibrationBean){
        byte[] data = new byte[8];
        int heightWarnValue = calibrationBean.getHeightWarnValue();
        data[0] = (byte)(heightWarnValue>>8);
        data[1] = (byte)(heightWarnValue);
        int heightAlarmValue = calibrationBean.getHeightAlarmValue();
        data[2] = (byte)(heightAlarmValue>>8);
        data[3] = (byte)(heightAlarmValue);
        int distanceWarnValue = calibrationBean.getDistanceWarnValue();
        data[4] = (byte)(distanceWarnValue>>8);
        data[5] = (byte)(distanceWarnValue);
        int distanceAlarmValue = calibrationBean.getDistanceAlarmValue();
        data[6] = (byte)(distanceAlarmValue>>8);
        data[7] = (byte)(distanceAlarmValue);

        byte[] cmd = CommunicationProtocol.packetCmd(CMD_PREVENT_COLLISION_CALIBRATION,data);
        deviceOperateInterface.sendDataToDevice(cmd);
        PreventCollisionCalibrationMessage msg = new PreventCollisionCalibrationMessage(CommunicationProtocol.seq++,cmd);
        msg.setCmdType(BaseMessage.TYPE_CMD);
        cmdSend(msg);
    }
    public void queryPreventCollisionCalibration(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_PREVENT_COLLISION_CALIBRATION,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        PreventCollisionCalibrationMessage msg = new PreventCollisionCalibrationMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
