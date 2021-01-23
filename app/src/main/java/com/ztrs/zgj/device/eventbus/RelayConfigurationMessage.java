package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.RelayConfigurationProtocol;
import com.ztrs.zgj.device.protocol.WorkCycleDataProtocol;

public class RelayConfigurationMessage extends BaseMessage{

    public RelayConfigurationMessage(long seq, byte[] cmdbytes){
        super(seq, RelayConfigurationProtocol.CMD_RELAY_CONFIGURATION, cmdbytes);
    }
}
