package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.RelayOutputControlBean;
import com.ztrs.zgj.device.eventbus.RelayOutputControlMessage;

public class RelayOutputControlProtocol extends BaseProtocol{
    private static final String TAG= RelayOutputControlProtocol.class.getSimpleName();

    public static final byte CMD_RELAY_OUTPUT_CONTROL = (byte) 0x32;//继电器输出控制

    private DeviceOperateInterface deviceOperateInterface;

    public RelayOutputControlProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){

    }

    public void parseAck(byte[] data){
        if(data.length == 1){
            ackReceive(data);
            return;
        }
        boolean success = parseData(data);
        if(!success){
            ackReceiveError();
            return;
        }
        ackReceive(data);
    }

    private boolean parseData(byte[] data){
        if(data.length != 12){
            LogUtils.LogE(TAG,"data length error");
            return false;
        }
        RelayOutputControlBean relayOutputControlBean = deviceOperateInterface.getZtrsDevice().getRelayOutputControlBean();
        relayOutputControlBean.setRelayOutputControl1(data[0]);
        relayOutputControlBean.setRelayOutputControl2(data[1]);
        relayOutputControlBean.setRelayOutputControl3(data[2]);
        relayOutputControlBean.setRelayOutputControl4(data[3]);
        relayOutputControlBean.setRelayOutputControl5(data[4]);
        relayOutputControlBean.setRelayOutputControl6(data[5]);
        relayOutputControlBean.setRelayOutputControl7(data[6]);
        relayOutputControlBean.setRelayOutputControl8(data[7]);

        relayOutputControlBean.setRelayOutputControl9(data[8]);
        relayOutputControlBean.setRelayOutputControl10(data[9]);
        relayOutputControlBean.setRelayOutputControl11(data[10]);
        relayOutputControlBean.setRelayOutputControl12(data[11]);

        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void setRelayOutputControl(RelayOutputControlBean relayOutputControlBean){
        byte[] data = new byte[12];
        data[0] = relayOutputControlBean.getRelayOutputControl1();
        data[1] = relayOutputControlBean.getRelayOutputControl2();
        data[2] = relayOutputControlBean.getRelayOutputControl3();
        data[3] = relayOutputControlBean.getRelayOutputControl4();
        data[4] = relayOutputControlBean.getRelayOutputControl5();
        data[5] = relayOutputControlBean.getRelayOutputControl6();
        data[6] = relayOutputControlBean.getRelayOutputControl7();
        data[7] = relayOutputControlBean.getRelayOutputControl8();

        data[8] = relayOutputControlBean.getRelayOutputControl9();
        data[9] = relayOutputControlBean.getRelayOutputControl10();
        data[10] = relayOutputControlBean.getRelayOutputControl11();
        data[11] = relayOutputControlBean.getRelayOutputControl12();

        byte[] cmd = CommunicationProtocol.packetCmd(CMD_RELAY_OUTPUT_CONTROL,data);
        deviceOperateInterface.sendDataToDevice(cmd);
        RelayOutputControlMessage msg = new RelayOutputControlMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

    public void queryRelayOutputControl(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_RELAY_OUTPUT_CONTROL,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        RelayOutputControlMessage msg = new RelayOutputControlMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
