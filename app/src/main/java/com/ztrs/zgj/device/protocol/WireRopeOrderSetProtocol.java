package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.WireRopeDetectionReportBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.WireRopeDetectionParametersSetMessage;
import com.ztrs.zgj.device.eventbus.WireRopeDetectionReportMessage;
import com.ztrs.zgj.device.eventbus.WireRopeOrderSetMessage;

public class WireRopeOrderSetProtocol extends BaseProtocol{
    private static final String TAG= WireRopeOrderSetProtocol.class.getSimpleName();

    public static final byte CMD_WIREROPE_ORDER_SET = (byte) 0x47;//钢丝绳下发指令
    private static final byte TYPE_START_DETECT = 0x04;
    private static final byte TYPE_STOP_DETECT = 0x06;
    private static final byte TYPE_QUERY = 0x0C;
    private static final byte TYPE_RESET = 0x00;

    private DeviceOperateInterface deviceOperateInterface;

    public WireRopeOrderSetProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
//        boolean success = parseData(data);
//        if(!success){
//            return;
//        }
//        byte[] ack = CommunicationProtocol.packetAck(CMD_WIREROPE_ORDER_SET, new byte[]{0});
//        deviceOperateInterface.sendDataToDevice(ack);
    }

    public void parseAck(byte[] data){

    }

    private boolean parseData(byte[] data){
//        if(data.length != 4){
//            LogUtils.LogE(TAG,"data length error");
//            return false;
//        }

        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void startDetect(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_WIREROPE_ORDER_SET,new byte[]{TYPE_START_DETECT});
        deviceOperateInterface.sendDataToDevice(cmd);
//        WireRopeOrderSetMessage msg = new WireRopeOrderSetMessage(CommunicationProtocol.seq++,cmd);
//        msg.setCmdType(BaseMessage.TYPE_CMD);
//        cmdSend(msg);
    }

    public void stopDetect(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_WIREROPE_ORDER_SET,new byte[]{TYPE_STOP_DETECT});
        deviceOperateInterface.sendDataToDevice(cmd);
    }

    public void queryState(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_WIREROPE_ORDER_SET,new byte[]{TYPE_QUERY});
        deviceOperateInterface.sendDataToDevice(cmd);
    }


    public void reset(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_WIREROPE_ORDER_SET,new byte[]{TYPE_RESET});
        deviceOperateInterface.sendDataToDevice(cmd);
    }

    public void clear(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_WIREROPE_ORDER_SET,new byte[]{0x1a});
        deviceOperateInterface.sendDataToDevice(cmd);
    }

}
