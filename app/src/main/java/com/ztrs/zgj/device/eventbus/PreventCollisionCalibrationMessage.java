package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.PreventCollisionCalibrationProtocol;
import com.ztrs.zgj.device.protocol.WeightCalibrationProtocol;

public class PreventCollisionCalibrationMessage extends BaseMessage{

    public PreventCollisionCalibrationMessage(long seq, byte[] cmdbytes){
        super(seq, PreventCollisionCalibrationProtocol.CMD_PREVENT_COLLISION_CALIBRATION, cmdbytes);
    }
}
