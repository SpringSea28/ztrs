package com.ztrs.zgj.device.protocol;

import android.util.Log;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.WireRopeDetectionReportBean;
import com.ztrs.zgj.device.eventbus.WireRopeDetectionReportMessage;

public class WireRopeDetectionReportProtocol extends BaseProtocol{
    private static final String TAG= WireRopeDetectionReportProtocol.class.getSimpleName();

    public static final byte CMD_WIREROPE_DETECTION_REPORT = (byte) 0x45;//钢丝绳检测警情上报


    private DeviceOperateInterface deviceOperateInterface;

    public WireRopeDetectionReportProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
        boolean success = parseData(data);
        if(!success){
            return;
        }
        byte[] ack = CommunicationProtocol.packetAck(CMD_WIREROPE_DETECTION_REPORT, new byte[]{0});
        deviceOperateInterface.sendDataToDevice(ack);
    }

    public void parseAck(byte[] data){

    }

    private boolean parseData(byte[] data){
        if(data.length != 5){
            LogUtils.LogE(TAG,"data length error");
            return false;
        }
        WireRopeDetectionReportBean wireRopeDetectionReportBean = deviceOperateInterface.getZtrsDevice().getWireRopeDetectionReportBean();
        int damageValue = ((data[0]&0x00ff) << 8) |(data[1]&0x00ff);
        wireRopeDetectionReportBean.setDamageValue(damageValue);
        int height=  ((data[2]&0xff) << 8) |(data[3]&0x00ff);
        wireRopeDetectionReportBean.setHeight(height);
        byte magnification = data[4];
        wireRopeDetectionReportBean.setMagnification(magnification);
        WireRopeDetectionReportMessage wireRopeDetectionReportMessage =
                new WireRopeDetectionReportMessage();
        notifyDeviceReport(wireRopeDetectionReportMessage);
        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

}
