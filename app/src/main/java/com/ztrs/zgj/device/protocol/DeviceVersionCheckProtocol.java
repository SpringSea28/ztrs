package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.DeviceVersionBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.DeviceUpdateMessage;
import com.ztrs.zgj.device.eventbus.DeviceVersionMessage;
import com.ztrs.zgj.device.eventbus.RealTimeDataMessage;

public class DeviceVersionCheckProtocol extends BaseProtocol{
    private static final String TAG= DeviceVersionCheckProtocol.class.getSimpleName();

    public static final byte CMD_VERSION_CHECK = (byte) 0x50;//设备软件版本查询

    private DeviceOperateInterface deviceOperateInterface;

    public DeviceVersionCheckProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
       if(data.length < 2){
           LogUtils.LogE(TAG,"data length error");
       }
        DeviceVersionBean deviceVersionBean = DeviceManager.getInstance().getZtrsDevice().getDeviceVersionBean();
       deviceVersionBean.setVerInt(data[0]);
       deviceVersionBean.setVerFloat(data[1]);
        DeviceVersionMessage msg =
                new DeviceVersionMessage();
        notifyDeviceReport(msg);
        byte[] ack = CommunicationProtocol.packetAck(CMD_VERSION_CHECK, new byte[]{0});
        deviceOperateInterface.sendDataToDevice(ack);
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

    public void deviceVersionCheck(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_VERSION_CHECK,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        DeviceVersionMessage msg = new DeviceVersionMessage(CommunicationProtocol.seq++,cmd);
        msg.setCmdType(BaseMessage.TYPE_QUERY);
        cmdSend(msg);
    }

}
