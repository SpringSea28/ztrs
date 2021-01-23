package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.WireRopeDetectionParametersSetProtocol;

public class WireRopeDetectionParametersSetMessage extends BaseMessage{

    public WireRopeDetectionParametersSetMessage(long seq, byte[] cmdbytes){
        super(seq, WireRopeDetectionParametersSetProtocol.CMD_WIREROPE_DETECTION_PARAMETERS_SET, cmdbytes);
    }
}
