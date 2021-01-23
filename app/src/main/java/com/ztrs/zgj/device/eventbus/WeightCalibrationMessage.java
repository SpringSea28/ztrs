package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.WeightCalibrationProtocol;

public class WeightCalibrationMessage extends BaseMessage{

    public WeightCalibrationMessage(long seq, byte[] cmdbytes){
        super(seq, WeightCalibrationProtocol.CMD_WEIGHT_CALIBRATION, cmdbytes);
    }
}
