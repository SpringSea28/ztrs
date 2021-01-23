package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.AroundCalibrationBean;
import com.ztrs.zgj.device.bean.TorqueCalibrationBean;
import com.ztrs.zgj.device.eventbus.AroundCalibrationMessage;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.TorqueCalibrationMessage;

public class TorqueCalibrationProtocol extends BaseProtocol{
    private static final String TAG= TorqueCalibrationProtocol.class.getSimpleName();

    public static final byte CMD_TORQUE_CALIBRATION = (byte) 0x43;//力矩参数设置

    private DeviceOperateInterface deviceOperateInterface;

    public TorqueCalibrationProtocol(DeviceOperateInterface deviceOperateInterface){
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
        int i = 0;
        TorqueCalibrationBean calibrationBean
                = deviceOperateInterface.getZtrsDevice().getTorqueCalibrationBean();

        int ratedLiftingWarnValue =((data[i]&0xffffffff) << 8) |(data[i+1]&0x00ff);
        calibrationBean.setRatedLiftingWarnValue(ratedLiftingWarnValue);
        i = i+2;

        byte torqueWarnRelayControl = data[i];
        calibrationBean.setTorqueWarnRelayControl(torqueWarnRelayControl);
        i = i+1;
        byte torqueWarnRelayOutput = data[i];
        calibrationBean.setTorqueWarnRelayOutput(torqueWarnRelayOutput);
        i= i+1;

        int ratedLiftingAlarmValue=((data[i]&0xffffffff) << 8) |(data[i+1]&0x00ff);
        calibrationBean.setRatedLiftingAlarmValue(ratedLiftingAlarmValue);
        i= i+2;

        byte torqueAlarmRelayControl = data[i];
        calibrationBean.setTorqueAlarmRelayControl(torqueAlarmRelayControl);
        i = i+1;
        byte torqueAlarmRelayOutput = data[i];
        calibrationBean.setTorqueAlarmRelayOutput(torqueAlarmRelayOutput);
        return true;
    }

    public void torqueCalibration(TorqueCalibrationBean calibrationBean){
        byte[] data = new byte[8];
        int i= 0;
        int ratedLiftingWarnValue = calibrationBean.getRatedLiftingWarnValue();
        data[i] = (byte)(ratedLiftingWarnValue>>8);
        data[i+1] = (byte)(ratedLiftingWarnValue);

        i=i+2;
        data[i] = calibrationBean.getTorqueWarnRelayControl();
        data[i+1] = calibrationBean.getTorqueWarnRelayOutput();

        i = i+2;
        int ratedLiftingAlarmValue = calibrationBean.getRatedLiftingAlarmValue();
        data[i] = (byte)(ratedLiftingAlarmValue>>8);
        data[i+1] = (byte)(ratedLiftingAlarmValue);

        i = i+2;
        data[i] = calibrationBean.getTorqueAlarmRelayControl();
        data[i+1] = calibrationBean.getTorqueAlarmRelayOutput();

        byte[] cmd = CommunicationProtocol.packetCmd(CMD_TORQUE_CALIBRATION,data);
        deviceOperateInterface.sendDataToDevice(cmd);
        TorqueCalibrationMessage msg = new TorqueCalibrationMessage(CommunicationProtocol.seq++,cmd);
        msg.setCmdType(BaseMessage.TYPE_CMD);
        cmdSend(msg);
    }
    public void queryTorqueCalibration(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_TORQUE_CALIBRATION,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        TorqueCalibrationMessage msg = new TorqueCalibrationMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
