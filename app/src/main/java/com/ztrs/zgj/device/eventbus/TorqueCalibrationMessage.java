package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.AroundCalibrationProtocol;
import com.ztrs.zgj.device.protocol.TorqueCalibrationProtocol;

public class TorqueCalibrationMessage extends BaseMessage{

    public TorqueCalibrationMessage(long seq, byte[] cmdbytes){
        super(seq, TorqueCalibrationProtocol.CMD_TORQUE_CALIBRATION, cmdbytes);
    }
}
