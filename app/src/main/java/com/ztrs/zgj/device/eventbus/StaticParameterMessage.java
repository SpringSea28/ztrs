package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.RegionalRestrictionProtocol;
import com.ztrs.zgj.device.protocol.StaticParametersProtocol;

public class StaticParameterMessage extends BaseMessage{

    public StaticParameterMessage(long seq, byte[] cmdbytes){
        super(seq, StaticParametersProtocol.CMD_STATIC_PARAMETER, cmdbytes);
    }
}
