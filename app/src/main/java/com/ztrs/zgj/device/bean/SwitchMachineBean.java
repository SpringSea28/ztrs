package com.ztrs.zgj.device.bean;

import com.ztrs.zgj.LogUtils;

public class SwitchMachineBean {
    private static final String TAG = SwitchMachineBean.class.getSimpleName();
    byte switchState;
    int workTimeLength;
    int workCycleTimeLength;

    public SwitchMachineBean(){}

    private SwitchMachineBean(byte switchState,int workTimeLength,int workCycleTimeLength){
        this.switchState = switchState;
        this.workTimeLength = workTimeLength;
        this.workCycleTimeLength = workCycleTimeLength;
    }

    public byte getSwitchState() {
        return switchState;
    }

    public void setSwitchState(byte switchState) {
        this.switchState = switchState;
        LogUtils.LogE(TAG,"set switchState: "+ switchState);
    }

    public int getWorkTimeLength() {
        return workTimeLength;
    }

    public void setWorkTimeLength(int workTimeLength) {
        this.workTimeLength = workTimeLength;
    }

    public int getWorkCycleTimeLength() {
        return workCycleTimeLength;
    }

    public void setWorkCycleTimeLength(int workCycleTimeLength) {
        this.workCycleTimeLength = workCycleTimeLength;
    }
}
