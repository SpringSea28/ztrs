package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.RelayConfigurationProtocol;
import com.ztrs.zgj.device.protocol.RelayOutputControlProtocol;

public class RelayOutputControlMessage extends BaseMessage{

    public RelayOutputControlMessage(long seq, byte[] cmdbytes){
        super(seq, RelayOutputControlProtocol.CMD_RELAY_OUTPUT_CONTROL, cmdbytes);
    }
}
