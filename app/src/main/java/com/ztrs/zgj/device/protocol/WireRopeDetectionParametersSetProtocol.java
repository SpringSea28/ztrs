package com.ztrs.zgj.device.protocol;

import android.util.Log;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.WireRopeDetectionParametersSetBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.WireRopeDetectionParametersSetMessage;

public class WireRopeDetectionParametersSetProtocol extends BaseProtocol{
    private static final String TAG= WireRopeDetectionParametersSetProtocol.class.getSimpleName();

    public static final byte CMD_WIREROPE_DETECTION_PARAMETERS_SET = (byte) 0x44;//钢丝绳检测参数设置


    private DeviceOperateInterface deviceOperateInterface;

    public WireRopeDetectionParametersSetProtocol(DeviceOperateInterface deviceOperateInterface){
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

    private boolean parseData(byte[] data){
        if(data.length != 23){
            LogUtils.LogE(TAG,"data length error");
            return false;
        }
        WireRopeDetectionParametersSetBean wireRopeDetectionParametersSetBean = deviceOperateInterface.getZtrsDevice().getWireRopeDetectionParametersSetBean();
        int threshold = ((data[0]&0x00ff) << 8) |(data[1]&0x00ff);
        wireRopeDetectionParametersSetBean.setThreshold(threshold);
        byte detectionState= data[2];
        wireRopeDetectionParametersSetBean.setDetectionState(detectionState);

        int i = 3;
        int lightDamage = ((data[i]&0x00ff) << 8) |(data[i+1]&0x00ff);
        wireRopeDetectionParametersSetBean.setLightDamage(lightDamage);

        i = i+2;
        int middleDamage = ((data[i]&0x00ff) << 8) |(data[i+1]&0x00ff);
        wireRopeDetectionParametersSetBean.setMiddleDamage(middleDamage);

        i = i+2;
        int severeDamage = ((data[i]&0x00ff) << 8) |(data[i+1]&0x00ff);
        wireRopeDetectionParametersSetBean.setSevereDamage(severeDamage);

        i = i+2;
        int damage = ((data[i]&0x00ff) << 8) |(data[i+1]&0x00ff);
        wireRopeDetectionParametersSetBean.setDamage(damage);

        i = i+2;
        int scrapped = ((data[i]&0x00ff) << 8) |(data[i+1]&0x00ff);
        wireRopeDetectionParametersSetBean.setScrapped(scrapped);

        i = i+2;
        int delay = ((data[i]&0x00ff) << 8) |(data[i+1]&0x00ff);
        wireRopeDetectionParametersSetBean.setDelay(delay);

        i = i+2;
        int detectCount = ((data[i]&0x00ff) << 8) |(data[i+1]&0x00ff);
        wireRopeDetectionParametersSetBean.setDetectCount(detectCount);

        i = i+2;
        int mode = ((data[i]&0x00ff) << 8) |(data[i+1]&0x00ff);
        wireRopeDetectionParametersSetBean.setMode(mode);

        i = i+2;
        int alarmInterval = ((data[i]&0x00ff) << 8) |(data[i+1]&0x00ff);
        wireRopeDetectionParametersSetBean.setAlarmInterval(alarmInterval);

        i = i+2;
        int alarmCount = ((data[i]&0x00ff) << 8) |(data[i+1]&0x00ff);
        wireRopeDetectionParametersSetBean.setAlarmCount(alarmCount);
        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void setWireRopeDetectionParameters(WireRopeDetectionParametersSetBean wireRopeDetectionParametersSetBean){
        byte[] data = new byte[23];

        int threshold = wireRopeDetectionParametersSetBean.getThreshold();
        data[0] = (byte)(threshold>>8);
        data[1] = (byte)threshold;
        byte detectionState= wireRopeDetectionParametersSetBean.getDetectionState();
        data[2] = detectionState;

        int i = 3;
        int lightDamage = wireRopeDetectionParametersSetBean.getLightDamage();
        data[i] = (byte)(lightDamage>>8);
        data[i+1] = (byte)lightDamage;

        i = i+2;
        int middleDamage = wireRopeDetectionParametersSetBean.getMiddleDamage();
        data[i] = (byte)(middleDamage>>8);
        data[i+1] = (byte)middleDamage;

        i = i+2;
        int severeDamage = wireRopeDetectionParametersSetBean.getSevereDamage();
        data[i] = (byte)(severeDamage>>8);
        data[i+1] = (byte)severeDamage;

        i = i+2;
        int damage = wireRopeDetectionParametersSetBean.getDamage();
        data[i] = (byte)(damage>>8);
        data[i+1] = (byte)damage;

        i= i+2;
        int scrapped = wireRopeDetectionParametersSetBean.getScrapped();
        data[i] = (byte)(scrapped>>8);
        data[i+1] = (byte)scrapped;

        i= i+2;
        int delay = wireRopeDetectionParametersSetBean.getDelay();
        data[i] = (byte)(delay>>8);
        data[i+1] = (byte)delay;

        i = i+2;
        int detectCount = wireRopeDetectionParametersSetBean.getDetectCount();
        data[i] = (byte)(detectCount>>8);
        data[i+1] = (byte)detectCount;

        i= i+2;
        int mode = wireRopeDetectionParametersSetBean.getMode();
        data[i] = (byte)(mode>>8);
        data[i+1] = (byte)mode;

        i= i+2;
        int alarmInterval = wireRopeDetectionParametersSetBean.getAlarmInterval();
        data[i] = (byte)(alarmInterval>>8);
        data[i+1] = (byte)alarmInterval;

        i= i+2;
        int alarmCount = wireRopeDetectionParametersSetBean.getAlarmCount();
        data[i] = (byte)(alarmCount>>8);
        data[i+1] = (byte)alarmCount;

        i= i+2;
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_WIREROPE_DETECTION_PARAMETERS_SET,data);
        deviceOperateInterface.sendDataToDevice(cmd);
        WireRopeDetectionParametersSetMessage msg = new WireRopeDetectionParametersSetMessage(CommunicationProtocol.seq++,cmd);
        msg.setCmdType(BaseMessage.TYPE_CMD);
        cmdSend(msg);
    }

    public void queryWireRopeDetectionParameters(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_WIREROPE_DETECTION_PARAMETERS_SET,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        WireRopeDetectionParametersSetMessage msg = new WireRopeDetectionParametersSetMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
