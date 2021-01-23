package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.PreventCollisionNearBean;
import com.ztrs.zgj.device.bean.PreventCollisionNearBean.NearCoordinate;
import com.ztrs.zgj.device.bean.WireRopeDetectionReportBean;
import com.ztrs.zgj.device.eventbus.PreventCollisionNearReportMessage;
import com.ztrs.zgj.device.eventbus.WireRopeDetectionReportMessage;

public class PreventCollisionNearProtocol extends BaseProtocol{
    private static final String TAG= PreventCollisionNearProtocol.class.getSimpleName();

    public static final byte CMD_NEAR = (byte) 0x62;//临塔设备信息


    private DeviceOperateInterface deviceOperateInterface;

    public PreventCollisionNearProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
        boolean success = parseData(data);
        if(!success){
            return;
        }
        byte[] ack = CommunicationProtocol.packetAck(CMD_NEAR, new byte[]{0});
        deviceOperateInterface.sendDataToDevice(ack);
    }

    public void parseAck(byte[] data){

    }

    private boolean parseData(byte[] data){
        if(data.length != 5){
            LogUtils.LogE(TAG,"data length error");
            return false;
        }
        PreventCollisionNearBean bean
                = deviceOperateInterface.getZtrsDevice().getPreventCollisionNearBean();
        NearCoordinate coordinate = new NearCoordinate();
        byte number = data[0];
        coordinate.setNumber(number);
        int x = ((data[1]&0xffffffff) << 8) |(data[2]&0x00ff);
        coordinate.setX(x);
        int y=  ((data[3]&0xffffffff) << 8) |(data[4]&0x00ff);
        coordinate.setY(y);
        bean.putNearCoordinate(coordinate);

        PreventCollisionNearReportMessage msg =
                new PreventCollisionNearReportMessage();
        notifyDeviceReport(msg);
        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

}
