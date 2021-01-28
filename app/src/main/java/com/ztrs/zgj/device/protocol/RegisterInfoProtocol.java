package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.RealTimeDataBean;
import com.ztrs.zgj.device.bean.RegisterInfoBean;
import com.ztrs.zgj.device.eventbus.RealTimeDataMessage;
import com.ztrs.zgj.device.eventbus.RegisterInfoMessage;
import com.ztrs.zgj.device.eventbus.UnlockCarMessage;

public class RegisterInfoProtocol extends BaseProtocol{
    private static final String TAG= RegisterInfoProtocol.class.getSimpleName();

    public static final byte CMD_REGISTER_INFO = (byte) 0x01;//设备注册信息
    private static final int DATA_LENGTH = 38;

    private DeviceOperateInterface deviceOperateInterface;

    public RegisterInfoProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
        boolean success = parseData(data);
        if(!success){
            return;
        }
        RegisterInfoMessage msg =
                new RegisterInfoMessage();
        notifyDeviceReport(msg);
        byte[] ack = CommunicationProtocol.packetAck(CMD_REGISTER_INFO, new byte[]{0});
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
        RegisterInfoBean registerInfoBean = deviceOperateInterface.getZtrsDevice().getRegisterInfoBean();
        registerInfoBean.setInfo(data);
        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void queryRegisterInfo(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_REGISTER_INFO,new byte[]{});
        deviceOperateInterface.sendDataToDevice(cmd);
        RegisterInfoMessage msg = new RegisterInfoMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
