package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.RegionalRestrictionProtocol;
import com.ztrs.zgj.device.protocol.SwitchMachineProtocol;

public class RegionalRestrictionMessage extends BaseMessage{

    public RegionalRestrictionMessage(long seq, byte[] cmdbytes){
        super(seq, RegionalRestrictionProtocol.CMD_REGIONAL_RESTRICTION, cmdbytes);
    }
}
