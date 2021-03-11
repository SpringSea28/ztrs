package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.eventbus.AnnouncementMessage;
import com.ztrs.zgj.device.eventbus.RealTimeDataMessage;
import com.ztrs.zgj.device.eventbus.UnlockCarMessage;

public class AnnouncementProtocol extends BaseProtocol{
    private static final String TAG= AnnouncementProtocol.class.getSimpleName();

    public static final byte CMD_ANNOUNCEMENT = (byte) 0x42;//公告

    private DeviceOperateInterface deviceOperateInterface;

    public AnnouncementProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
        deviceOperateInterface.getZtrsDevice().getAnnounecmentBean().setAnnouncement(data);
        AnnouncementMessage msg =
                new AnnouncementMessage();
        notifyDeviceReport(msg);
        byte[] ack = CommunicationProtocol.packetAck(CMD_ANNOUNCEMENT, data);
        deviceOperateInterface.sendDataToDevice(ack);
    }

    public void parseAck(byte[] data){

    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

}
