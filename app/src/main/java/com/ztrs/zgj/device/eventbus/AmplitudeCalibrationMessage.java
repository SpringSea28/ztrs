package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.AmplitudeCalibrationProtocol;
import com.ztrs.zgj.device.protocol.HeightCalibrationProtocol;

public class AmplitudeCalibrationMessage extends BaseMessage{

    public AmplitudeCalibrationMessage(long seq, byte[] cmdbytes){
        super(seq, AmplitudeCalibrationProtocol.CMD_AMPLITUDE_CALIBRATION, cmdbytes);
    }
}
