package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.UnlockCarMessage;
import com.ztrs.zgj.device.eventbus.VolumeMessage;

public class VolumeProtocol extends BaseProtocol{
    private static final String TAG= VolumeProtocol.class.getSimpleName();

    public static final byte CMD_VOLUME = (byte) 0x63;//音量

    private DeviceOperateInterface deviceOperateInterface;

    public VolumeProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){

    }

    public void parseAck(byte[] data){
        if(data.length != 1){
            LogUtils.LogE(TAG,"ack data length error");
            ackReceiveError();
        }
        byte volume = data[0];
        deviceOperateInterface.getZtrsDevice().getVolumeBean().setVolume(volume);
        ackReceive(data);
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void setVolume(byte volume){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_VOLUME,new byte[]{volume});
        deviceOperateInterface.sendDataToDevice(cmd);
        VolumeMessage msg = new VolumeMessage(CommunicationProtocol.seq++,cmd);
        msg.setCmdType(BaseMessage.TYPE_CMD);
        cmdSend(msg);
    }

    public void queryVolume(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_VOLUME,new byte[]{(byte)0xFF});
        deviceOperateInterface.sendDataToDevice(cmd);
        VolumeMessage msg = new VolumeMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
