package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.AroundCalibrationProtocol;
import com.ztrs.zgj.device.protocol.WorkCycleDataProtocol;

public class AroundCalibrationMessage extends BaseMessage{

    public AroundCalibrationMessage(long seq, byte[] cmdbytes){
        super(seq, AroundCalibrationProtocol.CMD_AROUND_CALIBRATION, cmdbytes);
    }
}
