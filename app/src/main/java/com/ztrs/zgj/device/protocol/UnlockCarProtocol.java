package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.eventbus.UnlockCarMessage;

public class UnlockCarProtocol extends BaseProtocol{
    private static final String TAG= UnlockCarProtocol.class.getSimpleName();

    public static final byte CMD_UNLOCK_CAR = (byte) 0x14;//解锁车
    private static final byte U = 'U';
    private static final byte L = 'L';

    private DeviceOperateInterface deviceOperateInterface;

    public UnlockCarProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
        if(data.length != 4){
            LogUtils.LogE(TAG,"data length error");
        }
        byte state = data[3];
        if(state == U){
            LogUtils.LogI(TAG,"state unlock");
            deviceOperateInterface.getZtrsDevice().getUnLockCarBean().setState(state);
        }else if(state == L){
            LogUtils.LogI(TAG,"state lock");
            deviceOperateInterface.getZtrsDevice().getUnLockCarBean().setState(state);
        }
        int delayTime = ((data[1]&0x00FF)<<8)+(data[2]&0x00FF);
        deviceOperateInterface.getZtrsDevice().getUnLockCarBean().setDelayTime(delayTime);
        UnlockCarMessage msg = new UnlockCarMessage();
        notifyDeviceReport(msg);
        byte[] ack = CommunicationProtocol.packetAck(CMD_UNLOCK_CAR, data);
        deviceOperateInterface.sendDataToDevice(ack);
    }

    public void parseAck(byte[] data){

    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }



}
