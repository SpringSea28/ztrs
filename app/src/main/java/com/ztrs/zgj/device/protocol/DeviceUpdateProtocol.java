package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.DeviceUpdateMessage;
import com.ztrs.zgj.device.eventbus.UnlockCarMessage;

public class DeviceUpdateProtocol extends BaseProtocol{
    private static final String TAG= DeviceUpdateProtocol.class.getSimpleName();

    public static final byte CMD_UPDATE = (byte) 0x25;//设备升级

    private DeviceOperateInterface deviceOperateInterface;

    public DeviceUpdateProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
    }

    public void parseAck(byte[] data){
        if(data.length == 1){
            ackReceive(data);
        }else {
            LogUtils.LogE(TAG,"data length error");
            ackReceiveError();
        }
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void deviceUpdate(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_UPDATE,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        DeviceUpdateMessage msg = new DeviceUpdateMessage(CommunicationProtocol.seq++,cmd);
        msg.setCmdType(BaseMessage.TYPE_CMD);
        cmdSend(msg);
    }

}
