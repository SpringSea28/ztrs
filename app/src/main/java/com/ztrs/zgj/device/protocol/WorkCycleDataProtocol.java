package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.WorkCycleDataBean;
import com.ztrs.zgj.device.eventbus.WorkCycleDataMessage;

public class WorkCycleDataProtocol extends BaseProtocol{
    private static final String TAG= WorkCycleDataProtocol.class.getSimpleName();

    public static final byte CMD_WORK_CYCLE_DATA = (byte) 0x24;//循环工作数据

    private DeviceOperateInterface deviceOperateInterface;

    public WorkCycleDataProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
        boolean success = parseData(data);
        if(!success){
            return;
        }
        byte[] ack = CommunicationProtocol.packetAck(CMD_WORK_CYCLE_DATA, new byte[]{0});
        deviceOperateInterface.sendDataToDevice(ack);
    }

    public void parseAck(byte[] data){
        boolean success = parseData(data);
        if(!success){
            ackReceiveError();
            return;
        }
        ackReceive(data);
    }

    private boolean parseData(byte[] data){
        if(data.length != 44+2){
            LogUtils.LogE(TAG,"data length error");
            return false;
        }
        WorkCycleDataBean workCycleDataBean = deviceOperateInterface.getZtrsDevice().getWorkCycleDataBean();
        workCycleDataBean.setYearStart(data[0]);
        workCycleDataBean.setMonthStart(data[1]);
        workCycleDataBean.setDayStart(data[2]);
        workCycleDataBean.setHourStart(data[3]);
        workCycleDataBean.setMinuteStart(data[4]);
        workCycleDataBean.setSecondStart(data[5]);
        workCycleDataBean.setYearStop(data[6]);
        workCycleDataBean.setMonthStop(data[7]);
        workCycleDataBean.setDayStop(data[8]);
        workCycleDataBean.setHourStop(data[9]);
        workCycleDataBean.setMinuteStop(data[10]);
        workCycleDataBean.setSecondStop(data[11]);
        int maxUpWeightTorquePercent =((data[12]&0x00ff) << 8) |(data[13]&0x00ff);
        workCycleDataBean.setMaxUpWeightTorquePercent(maxUpWeightTorquePercent);
        int maxUpWeight =((data[14]&0x00ff) << 8) |(data[15]&0x00ff);
        workCycleDataBean.setMaxUpWeight(maxUpWeight);
        int startHeight =((data[16]&0x00ff) << 8) |(data[17]&0x00ff);
        workCycleDataBean.setStartHeight(startHeight);
        int stopHeight =((data[18]&0x00ff) << 8) |(data[19]&0x00ff);
        workCycleDataBean.setStopHeight(stopHeight);
        int startAround =((data[20]&0x00ff) << 8) |(data[21]&0x00ff);
        workCycleDataBean.setStartAround(startAround);
        int stopAround =((data[22]&0x00ff) << 8) |(data[23]&0x00ff);
        workCycleDataBean.setStopAround(stopAround);
        int startAmplitude =((data[24]&0x00ff) << 8) |(data[25]&0x00ff);
        workCycleDataBean.setStartAmplitude(startAmplitude);
        int stopAmplitude =((data[26]&0x00ff) << 8) |(data[27]&0x00ff);
        workCycleDataBean.setStopAmplitude(stopAmplitude);
        int startMoveArm =((data[28]&0x00ff) << 8) |(data[29]&0x00ff);
        workCycleDataBean.setStartMoveArm(startMoveArm);
        int stopMoveArm =((data[30]&0x00ff) << 8) |(data[31]&0x00ff);
        workCycleDataBean.setStopMoveArm(stopMoveArm);
        int maxAmplitude =((data[32]&0x00ff) << 8) |(data[33]&0x00ff);
        workCycleDataBean.setMaxAmplitude(maxAmplitude);
        int maxWindSpeed =((data[34]&0x00ff) << 8) |(data[35]&0x00ff);
        workCycleDataBean.setMaxWindSpeed(maxWindSpeed);
        boolean isComplete  = (data[36] &0X01) == 0x01;
        workCycleDataBean.setComplete(isComplete);
        int startX =((data[38]&0x00ff) << 8) |(data[39]&0x00ff);
        workCycleDataBean.setStartX(startX);
        int startY =((data[40]&0x00ff) << 8) |(data[41]&0x00ff);
        workCycleDataBean.setStartY(startY);
        int stopX =((data[42]&0x00ff) << 8) |(data[43]&0x00ff);
        workCycleDataBean.setStopX(stopX);
        int stopY =((data[44]&0x00ff) << 8) |(data[45]&0x00ff);
        workCycleDataBean.setStopY(stopY);

        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void queryWorkCycleData(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_WORK_CYCLE_DATA,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        WorkCycleDataMessage msg = new WorkCycleDataMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
