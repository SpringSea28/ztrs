package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.eventbus.EmergencyCallMessage;
import com.ztrs.zgj.device.eventbus.UnlockCarMessage;

public class EmergencyCallProtocol extends BaseProtocol{
    private static final String TAG= EmergencyCallProtocol.class.getSimpleName();

    public static final byte CMD_EMERGENCY_CALL = (byte) 0x33;//紧急呼叫

    private DeviceOperateInterface deviceOperateInterface;

    public EmergencyCallProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
    }

    public void parseAck(byte[] data){
        if(data.length != 1){
            LogUtils.LogE(TAG,"ack data length error");
        }
        ackReceive(data);
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }


    public void emergencyCall(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_EMERGENCY_CALL,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        EmergencyCallMessage msg = new EmergencyCallMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
