package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.eventbus.UnlockCarMessage;

public class UnlockCarProtocol extends BaseProtocol{
    private static final String TAG= UnlockCarProtocol.class.getSimpleName();

    public static final byte CMD_UNLOCK_CAR = (byte) 0x14;//解锁车
    private static final byte U = 'U';
    private static final byte L = 'L';
    private static final byte C = 'C';
    private static final byte N = 'N';

    private DeviceOperateInterface deviceOperateInterface;

    public UnlockCarProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
        if(data.length != 1){
            LogUtils.LogE(TAG,"data length error");
        }
        byte state = data[0];
        if(state == U){
            LogUtils.LogI(TAG,"state unlock");
            deviceOperateInterface.getZtrsDevice().getUnLockCarBean().setState(state);
        }else if(state == L){
            LogUtils.LogI(TAG,"state lock");
            deviceOperateInterface.getZtrsDevice().getUnLockCarBean().setState(state);
        }else {
            LogUtils.LogI(TAG,"state unknown");
        }
        byte[] ack = CommunicationProtocol.packetAck(CMD_UNLOCK_CAR, data);
        deviceOperateInterface.sendDataToDevice(ack);
    }

    public void parseAck(byte[] data){
        if(data.length != 1){
            LogUtils.LogE(TAG,"ack data length error");
        }
        byte state = data[0];
        if(state == U){
            LogUtils.LogI(TAG,"ack state unlock");
            deviceOperateInterface.getZtrsDevice().getUnLockCarBean().setState(state);
            ackReceive(data);
            return;
        }else if(state == L){
            LogUtils.LogI(TAG,"ack state lock");
            deviceOperateInterface.getZtrsDevice().getUnLockCarBean().setState(state);
            ackReceive(data);
            return;
        }else if(state == N){
            LogUtils.LogI(TAG,"ack state NULL");
            deviceOperateInterface.getZtrsDevice().getUnLockCarBean().setState(state);
            ackReceive(data);
            return;
        }else {
            LogUtils.LogI(TAG,"ack state unknown");
        }
        ackReceiveError();
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void unLockCar(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_UNLOCK_CAR,new byte[]{U});
        deviceOperateInterface.sendDataToDevice(cmd);
        UnlockCarMessage unlockCarMessage = new UnlockCarMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(unlockCarMessage);
    }

    public void lockCar(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_UNLOCK_CAR,new byte[]{L});
        deviceOperateInterface.sendDataToDevice(cmd);
        UnlockCarMessage unlockCarMessage = new UnlockCarMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(unlockCarMessage);
    }

    public void queryUnLockCar(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_UNLOCK_CAR,new byte[]{C});
        deviceOperateInterface.sendDataToDevice(cmd);
        UnlockCarMessage unlockCarMessage = new UnlockCarMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(unlockCarMessage);
    }

}
