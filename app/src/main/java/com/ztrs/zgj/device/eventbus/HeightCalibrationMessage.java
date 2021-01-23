package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.HeightCalibrationProtocol;
import com.ztrs.zgj.device.protocol.WorkCycleDataProtocol;

public class HeightCalibrationMessage extends BaseMessage{

    public HeightCalibrationMessage(long seq, byte[] cmdbytes){
        super(seq, HeightCalibrationProtocol.CMD_HEIGHT_CALIBRATION, cmdbytes);
    }
}
