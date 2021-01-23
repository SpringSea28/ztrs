package com.ztrs.zgj.device.bean;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.ztrs.zgj.BR;

public class TorqueCalibrationBean extends BaseObservable implements Cloneable {
    private static final String TAG = TorqueCalibrationBean.class.getSimpleName();

    @Bindable
    int ratedLiftingWarnValue;
    byte torqueWarnRelayControl;
    byte torqueWarnRelayOutput = 2;

    @Bindable
    int ratedLiftingAlarmValue;
    byte torqueAlarmRelayControl;
    byte torqueAlarmRelayOutput = 2;

    public int getRatedLiftingWarnValue() {
        return ratedLiftingWarnValue;
    }

    public void setRatedLiftingWarnValue(int ratedLiftingWarnValue) {
        this.ratedLiftingWarnValue = ratedLiftingWarnValue;
        notifyPropertyChanged(BR.ratedLiftingWarnValue);
    }

    public byte getTorqueWarnRelayControl() {
        return torqueWarnRelayControl;
    }

    public void setTorqueWarnRelayControl(byte torqueWarnRelayControl) {
        this.torqueWarnRelayControl = torqueWarnRelayControl;
    }

    public byte getTorqueWarnRelayOutput() {
        return torqueWarnRelayOutput;
    }

    public void setTorqueWarnRelayOutput(byte torqueWarnRelayOutput) {
        this.torqueWarnRelayOutput = torqueWarnRelayOutput;
    }

    public int getRatedLiftingAlarmValue() {
        return ratedLiftingAlarmValue;
    }

    public void setRatedLiftingAlarmValue(int ratedLiftingAlarmValue) {
        this.ratedLiftingAlarmValue = ratedLiftingAlarmValue;
        notifyPropertyChanged(BR.ratedLiftingAlarmValue);
    }

    public byte getTorqueAlarmRelayControl() {
        return torqueAlarmRelayControl;
    }

    public void setTorqueAlarmRelayControl(byte torqueAlarmRelayControl) {
        this.torqueAlarmRelayControl = torqueAlarmRelayControl;
    }

    public byte getTorqueAlarmRelayOutput() {
        return torqueAlarmRelayOutput;
    }

    public void setTorqueAlarmRelayOutput(byte torqueAlarmRelayOutput) {
        this.torqueAlarmRelayOutput = torqueAlarmRelayOutput;
    }

    @NonNull
    @Override
    public TorqueCalibrationBean clone() throws CloneNotSupportedException {
        return (TorqueCalibrationBean) super.clone();
    }
}
