package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.StaticParametersProtocol;
import com.ztrs.zgj.device.protocol.TorqueCurveProtocol;

public class TorqueCurveMessage extends BaseMessage{

    public TorqueCurveMessage(long seq, byte[] cmdbytes){
        super(seq, TorqueCurveProtocol.CMD_TORQUE_CURVE, cmdbytes);
    }
}
