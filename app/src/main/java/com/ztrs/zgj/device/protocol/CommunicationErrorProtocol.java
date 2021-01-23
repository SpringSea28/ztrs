package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.eventbus.UnlockCarMessage;

public class CommunicationErrorProtocol extends BaseProtocol{
    private static final String TAG= CommunicationErrorProtocol.class.getSimpleName();

    public static final byte CMD_COMMUNICATION_ERROR = (byte) 0xFE;//通信错误

    private DeviceOperateInterface deviceOperateInterface;

    public CommunicationErrorProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){

    }

    public void parseAck(byte[] data){
        if(data.length != 1){
            LogUtils.LogE(TAG,"ack data length error");
        }
        byte state = data[0];
        if(state == 1){
            LogUtils.LogE(TAG,"ack communication number error");

        }else if(state == 2){
            LogUtils.LogE(TAG,"ack state cmd code error");
        }else if(state == 3){
            LogUtils.LogE(TAG,"ack state time code error");
        }else if(state == 4){
            LogUtils.LogE(TAG,"ack state data load error");
        }else if(state == 9){
            LogUtils.LogE(TAG,"ack state crc error");
        }else {
            LogUtils.LogE(TAG,"ack state unknown");
        }
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

}
