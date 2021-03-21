package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.InverterDataReportBean.InverterData;
import com.ztrs.zgj.device.eventbus.InverterDataReportMessage;

public class InverterDataReportProtocol extends BaseProtocol{
    private static final String TAG= InverterDataReportProtocol.class.getSimpleName();

    public static final byte CMD_INVERTER_DATA_REPORT = (byte) 0x46;//变频器数据上报

    private DeviceOperateInterface deviceOperateInterface;

    public InverterDataReportProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
        if(data.length != 204){
            LogUtils.LogE(TAG,"data length error");
        }

        InverterData upInverterData = deviceOperateInterface.getZtrsDevice()
                .getInverterDataReportBean().getUpInverterData();
        parseInverterData(data,0,upInverterData);

        InverterData aroundInverterData = deviceOperateInterface.getZtrsDevice()
                .getInverterDataReportBean().getAroundInverterData();
        parseInverterData(data,0+68,aroundInverterData);

        InverterData amplitudeInverterData = deviceOperateInterface.getZtrsDevice()
                .getInverterDataReportBean().getAmplitudeInverterData();
        parseInverterData(data,0+68+68,amplitudeInverterData);

        InverterDataReportMessage inverterDataReportMessage = new InverterDataReportMessage();
        notifyDeviceReport(inverterDataReportMessage);
        byte[] ack = CommunicationProtocol.packetAck(CMD_INVERTER_DATA_REPORT, new byte[]{0});
        deviceOperateInterface.sendDataToDevice(ack);

    }

    private void parseInverterData(byte[] data,int offset,InverterData inverterData){
        int run = ((data[offset]&0x00ff) << 8) |(data[offset+1]&0x00ff);
        inverterData.setRun(run);
        int direction =((data[offset+2]&0x00ff) << 8) |(data[offset+3]&0x00ff);
        inverterData.setDirection(direction);
        int frequency =((data[offset+4]&0x00ff) << 8) |(data[offset+5]&0x00ff);
        inverterData.setFrequency(frequency);
        int encoderSpeed =((data[offset+6]&0x00ff) << 8) |(data[offset+7]&0x00ff);
        inverterData.setEncoderSpeed(encoderSpeed);
        int rotorSpeed  =((data[offset+8]&0x00ff) << 8) |(data[offset+9]&0x00ff);
        inverterData.setRotorSpeed(rotorSpeed);
        int busVoltage =((data[offset+10]&0x00ff) << 8) |(data[offset+11]&0x00ff);
        inverterData.setBusVoltage(busVoltage);
        int outputVoltage =((data[offset+12]&0x00ff) << 8) |(data[offset+13]&0x00ff);
        inverterData.setOutputVoltage(outputVoltage);
        int outputCurrent =((data[offset+14]&0x00ff) << 8) |(data[offset+15]&0x00ff);
        inverterData.setOutputCurrent(outputCurrent);
        int control =((data[offset+16]&0x00ff) << 8) |(data[offset+17]&0x00ff);
        inverterData.setControl(control);
        int maximumTemperature =((data[offset+18]&0x00ff) << 8) |(data[offset+19]&0x00ff);
        inverterData.setMaximumTemperature(maximumTemperature);
        int warn =((data[offset+20]&0x00ff) << 8) |(data[offset+21]&0x00ff);
        inverterData.setWarn(warn);
        int error=((data[offset+22]&0x00ff) << 8) |(data[offset+23]&0x00ff);
        inverterData.setError(error);
        int encoder1LSW=((data[offset+24]&0x00ff) << 8) |(data[offset+25]&0x00ff);
        inverterData.setEncoder1LSW(encoder1LSW);
        int encoder1MSW=((data[offset+26]&0x00ff) << 8) |(data[offset+27]&0x00ff);
        inverterData.setEncoder1MSW(encoder1MSW);
        int encoder2LSW=((data[offset+28]&0x00ff) << 8) |(data[offset+29]&0x00ff);
        inverterData.setEncoder2LSW(encoder2LSW);
        int encoder2MSW=((data[offset+30]&0x00ff) << 8) |(data[offset+31]&0x00ff);
        inverterData.setEncoder2MSW(encoder2MSW);
        int towerCraftCardDI16=((data[offset+32]&0xffffffff) << 8) |(data[offset+33]&0x00ff);
        inverterData.setTowerCraftCardDI16(towerCraftCardDI16);
        int towerCraftCardDI23=((data[offset+34]&0x00ff) << 8) |(data[offset+35]&0x00ff);
        inverterData.setTowerCraftCardDI23(towerCraftCardDI23);
        int towerCraftCardDO8=((data[offset+36]&0x00ff) << 8) |(data[offset+37]&0x00ff);
        inverterData.setTowerCraftCardDO8(towerCraftCardDO8);
        int towerCraftCardStateInstructionMSW=((data[offset+38]&0xffffffff) << 8) |(data[offset+39]&0x00ff);
        inverterData.setTowerCraftCardStateInstructionMSW(towerCraftCardStateInstructionMSW);
        int towerCraftCardStateInstructionLSW=((data[offset+40]&0xffffffff) << 8) |(data[offset+41]&0x00ff);
        inverterData.setTowerCraftCardStateInstructionLSW(towerCraftCardStateInstructionLSW);
        int towerCraftCardStateValue=((data[offset+42]&0x00ff) << 8) |(data[offset+43]&0x00ff);
        inverterData.setTowerCraftCardStateValue(towerCraftCardStateValue);
        int towerCraftCardSoftwareVersion=((data[offset+44]&0x00ff) << 8) |(data[offset+45]&0x00ff);
        inverterData.setTowerCraftCardSoftwareVersion(towerCraftCardSoftwareVersion);
        int liftingTorqueUnderBreak=((data[offset+46]&0x00ff) << 8) |(data[offset+47]&0x00ff);
        inverterData.setLiftingTorqueUnderBreak(liftingTorqueUnderBreak);
        int mBControlTime=((data[offset+48]&0x00ff) << 8) |(data[offset+49]&0x00ff);
        inverterData.setmBControlTime(mBControlTime);
        int mBStartStopControl=((data[offset+50]&0x00ff) << 8) |(data[offset+51]&0x00ff);
        inverterData.setmBStartStopControl(mBStartStopControl);
        int mBDirectionControl=((data[offset+52]&0x00ff) << 8) |(data[offset+53]&0x00ff);
        inverterData.setmBDirectionControl(mBDirectionControl);
        int mBSpeedGive=((data[offset+54]&0x00ff) << 8) |(data[offset+55]&0x00ff);
        inverterData.setmBSpeedGive(mBSpeedGive);
        int lockingControlSignal=((data[offset+56]&0x00ff) << 8) |(data[offset+57]&0x00ff);
        inverterData.setLockingControlSignal(lockingControlSignal);
        int antiHookFunctionEnabled=((data[offset+58]&0x00ff) << 8) |(data[offset+59]&0x00ff);
        inverterData.setAntiHookFunctionEnabled(antiHookFunctionEnabled);
        int maxTowerLoadWeight=((data[offset+60]&0x00ff) << 8) |(data[offset+61]&0x00ff);
        inverterData.setMaxTowerLoadWeight(maxTowerLoadWeight);
        int currentLuffingPosition=((data[offset+62]&0x00ff) << 8) |(data[offset+63]&0x00ff);
        inverterData.setCurrentLuffingPosition(currentLuffingPosition);
        int currentLuffingPositionMaxUpWeight=((data[offset+64]&0x00ff) << 8) |(data[offset+65]&0x00ff);
        inverterData.setCurrentLuffingPositionMaxUpWeight(currentLuffingPositionMaxUpWeight);
        int currentLuffingPositionCurrentUpWeight=((data[offset+66]&0x00ff) << 8) |(data[offset+67]&0x00ff);
        inverterData.setCurrentLuffingPositionCurrentUpWeight(currentLuffingPositionCurrentUpWeight);
    }

    public void parseAck(byte[] data){

    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

}
