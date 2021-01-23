package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.RelayConfigurationBean;
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
        if(data.length != 30){
            LogUtils.LogE(TAG,"data length error");
            return false;
        }
        RelayConfigurationBean relayConfigurationBean = deviceOperateInterface.getZtrsDevice().getRelayConfigurationBean();
        relayConfigurationBean.setLoad25(data[0]);
        relayConfigurationBean.setLoad25OutputState(data[1]);
        relayConfigurationBean.setLoad50(data[2]);
        relayConfigurationBean.setLoad50OutputState(data[3]);
        relayConfigurationBean.setLoad90(data[4]);
        relayConfigurationBean.setLoad90OutputState(data[5]);
        relayConfigurationBean.setLoad100(data[6]);
        relayConfigurationBean.setLoad100OutputState(data[7]);

        relayConfigurationBean.setTorque80(data[8]);
        relayConfigurationBean.setTorque80OutputState(data[9]);
        relayConfigurationBean.setTorque90(data[10]);
        relayConfigurationBean.setTorque90OutputState(data[11]);
        relayConfigurationBean.setTorque100(data[12]);
        relayConfigurationBean.setTorque100OutputState(data[13]);
        relayConfigurationBean.setTorque110(data[14]);
        relayConfigurationBean.setTorque110OutputState(data[15]);

        relayConfigurationBean.setHeightUpStop(data[16]);
        relayConfigurationBean.setHeightUpStopOutputState(data[17]);
        relayConfigurationBean.setHeightDownStop(data[18]);
        relayConfigurationBean.setHeightDownStopOutputState(data[19]);
        relayConfigurationBean.setAroundLeftStop(data[20]);
        relayConfigurationBean.setAroundLeftStopOutputState(data[21]);
        relayConfigurationBean.setAroundRightStop(data[22]);
        relayConfigurationBean.setAroundRightStopOutputState(data[23]);
        relayConfigurationBean.setAmplitudeOutStop(data[24]);
        relayConfigurationBean.setAmplitudeOutStopOutputState(data[25]);
        relayConfigurationBean.setAmplitudeInStop(data[26]);
        relayConfigurationBean.setAmplitudeInStopOutputState(data[27]);

        relayConfigurationBean.setLoadWeight80(data[28]);
        relayConfigurationBean.setLoadWeight80OutputState(data[29]);

        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void setRelayConfiguration(RelayConfigurationBean relayConfiguration){
        byte[] data = new byte[30];
        data[0] = relayConfiguration.getLoad25();
        data[1] = relayConfiguration.getLoad25OutputState();
        data[2] = relayConfiguration.getLoad50();
        data[3] = relayConfiguration.getLoad50OutputState();
        data[4] = relayConfiguration.getLoad90();
        data[5] = relayConfiguration.getLoad90OutputState();
        data[6] = relayConfiguration.getLoad100();
        data[7] = relayConfiguration.getLoad100OutputState();

        data[8] = relayConfiguration.getTorque80();
        data[9] = relayConfiguration.getTorque80OutputState();
        data[10] = relayConfiguration.getTorque90();
        data[11] = relayConfiguration.getTorque90OutputState();
        data[12] = relayConfiguration.getTorque100();
        data[13] = relayConfiguration.getTorque100OutputState();
        data[14] = relayConfiguration.getTorque110();
        data[15] = relayConfiguration.getTorque110OutputState();

        data[16] = relayConfiguration.getHeightUpStop();
        data[17] = relayConfiguration.getHeightUpStopOutputState();
        data[18] = relayConfiguration.getHeightDownStop();
        data[19] = relayConfiguration.getHeightDownStopOutputState();
        data[20] = relayConfiguration.getAroundLeftStop();
        data[21] = relayConfiguration.getAroundLeftStopOutputState();
        data[22] = relayConfiguration.getAroundRightStop();
        data[23] = relayConfiguration.getAroundRightStopOutputState();

        data[24] = relayConfiguration.getAmplitudeOutStop();
        data[25] = relayConfiguration.getAmplitudeOutStopOutputState();
        data[26] = relayConfiguration.getAmplitudeInStop();
        data[27] = relayConfiguration.getAmplitudeInStopOutputState();

        data[28] = relayConfiguration.getLoadWeight80();
        data[29] = relayConfiguration.getLoadWeight80OutputState();

        byte[] cmd = CommunicationProtocol.packetCmd(CMD_RELAY_CONFIGURATION,data);
        deviceOperateInterface.sendDataToDevice(cmd);
        RelayConfigurationMessage msg = new RelayConfigurationMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

    public void queryRelayConfiguration(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_RELAY_CONFIGURATION,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        RelayConfigurationMessage msg = new RelayConfigurationMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
