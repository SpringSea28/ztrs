package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.eventbus.SwitchMachineMessage;

public class SwitchMachineProtocol extends BaseProtocol{
    private static final String TAG= SwitchMachineProtocol.class.getSimpleName();

    public static final byte CMD_SWITCH_MACHINE = (byte) 0x1F;//开关机
    public static final byte ON = 1;
    public static final byte OFF = 0;

    private static final int SWITCH_OFFSET = 0;
    private static final int WORK_TIME_OFFSET = SWITCH_OFFSET + 1;
    private static final int WORK_CYCLE_TIME_OFFSET = WORK_TIME_OFFSET + 4;


    private DeviceOperateInterface deviceOperateInterface;

    public SwitchMachineProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
        boolean success = parseData(data);
        if(!success){
            return;
        }
        byte[] ack = CommunicationProtocol.packetAck(CMD_SWITCH_MACHINE, new byte[]{0});
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
        if(data.length != 9){
            LogUtils.LogE(TAG,"data length error");
            return false;
        }
        byte state = data[SWITCH_OFFSET];
        if(state != ON && state != OFF){
            LogUtils.LogE(TAG,"state error");
            return false;
        }

        deviceOperateInterface.getZtrsDevice().getSwitchMachineBean().setSwitchState(state);
        int workTime = 0;
        for(int i= 0;i<4;i++){
            int shift = 3-i;
            shift *= 8;
            int mask = 0x000000ff << shift;
            workTime |= data[i+WORK_TIME_OFFSET]<<shift & mask;
        }
        deviceOperateInterface.getZtrsDevice().getSwitchMachineBean().setWorkTimeLength(workTime);
        int workCycleTime = 0;
        for(int i= 0;i<4;i++){
            int shift = 4 -i -1;
            shift *= 8;
            int mask = 0x000000ff << shift;
            workCycleTime |= data[i+WORK_CYCLE_TIME_OFFSET]<<shift & mask;
        }
        deviceOperateInterface.getZtrsDevice().getSwitchMachineBean().setWorkCycleTimeLength(workCycleTime);
        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void querySwitchMachine(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_SWITCH_MACHINE,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        SwitchMachineMessage switchMachineMessage = new SwitchMachineMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(switchMachineMessage);
    }

}
