package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.RelayConfigurationBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.RelayConfigurationMessage;

public class RelayConfigurationProtocol extends BaseProtocol{
    private static final String TAG= RelayConfigurationProtocol.class.getSimpleName();

    public static final byte CMD_RELAY_CONFIGURATION = (byte) 0x31;//配置继电器

    private DeviceOperateInterface deviceOperateInterface;

    public RelayConfigurationProtocol(DeviceOperateInterface deviceOperateInterface){
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
        if(data.length != 24){
            LogUtils.LogE(TAG,"data length error");
            return false;
        }
        RelayConfigurationBean relayConfigurationBean = deviceOperateInterface.getZtrsDevice().getRelayConfigurationBean();
        relayConfigurationBean.setRelay1Use(data[0]);
        relayConfigurationBean.setRelay1State(data[1]);
        relayConfigurationBean.setRelay2Use(data[2]);
        relayConfigurationBean.setRelay2State(data[3]);
        relayConfigurationBean.setRelay3Use(data[4]);
        relayConfigurationBean.setRelay3State(data[5]);
        relayConfigurationBean.setRelay4Use(data[6]);
        relayConfigurationBean.setRelay4State(data[7]);

        relayConfigurationBean.setRelay5Use(data[8]);
        relayConfigurationBean.setRelay5State(data[9]);
        relayConfigurationBean.setRelay6Use(data[10]);
        relayConfigurationBean.setRelay6State(data[11]);
        relayConfigurationBean.setRelay7Use(data[12]);
        relayConfigurationBean.setRelay7State(data[13]);
        relayConfigurationBean.setRelay8Use(data[14]);
        relayConfigurationBean.setRelay8State(data[15]);

        relayConfigurationBean.setRelay9Use(data[16]);
        relayConfigurationBean.setRelay9State(data[17]);
        relayConfigurationBean.setRelay10Use(data[18]);
        relayConfigurationBean.setRelay10State(data[19]);
        relayConfigurationBean.setRelay11Use(data[20]);
        relayConfigurationBean.setRelay11State(data[21]);

        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void setRelayConfiguration(RelayConfigurationBean relayConfiguration){
        byte[] data = new byte[24];
        data[0] = relayConfiguration.getRelay1Use();
        data[1] = relayConfiguration.getRelay1State();
        data[2] = relayConfiguration.getRelay2Use();
        data[3] = relayConfiguration.getRelay2State();
        data[4] = relayConfiguration.getRelay3Use();
        data[5] = relayConfiguration.getRelay3State();
        data[6] = relayConfiguration.getRelay4Use();
        data[7] = relayConfiguration.getRelay4State();

        data[8] = relayConfiguration.getRelay5Use();
        data[9] = relayConfiguration.getRelay5State();
        data[10] = relayConfiguration.getRelay6Use();
        data[11] = relayConfiguration.getRelay6State();
        data[12] = relayConfiguration.getRelay7Use();
        data[13] = relayConfiguration.getRelay7State();
        data[14] = relayConfiguration.getRelay8Use();
        data[15] = relayConfiguration.getRelay8State();

        data[16] = relayConfiguration.getRelay9Use();
        data[17] = relayConfiguration.getRelay9State();
        data[18] = relayConfiguration.getRelay10Use();
        data[19] = relayConfiguration.getRelay10State();
        data[20] = relayConfiguration.getRelay11Use();
        data[21] = relayConfiguration.getRelay11State();

        byte[] cmd = CommunicationProtocol.packetCmd(CMD_RELAY_CONFIGURATION,data);
        deviceOperateInterface.sendDataToDevice(cmd);
        RelayConfigurationMessage msg = new RelayConfigurationMessage(CommunicationProtocol.seq++,cmd);
        msg.setCmdType(BaseMessage.TYPE_CMD);
        cmdSend(msg);
    }

    public void queryRelayConfiguration(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_RELAY_CONFIGURATION,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        RelayConfigurationMessage msg = new RelayConfigurationMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
