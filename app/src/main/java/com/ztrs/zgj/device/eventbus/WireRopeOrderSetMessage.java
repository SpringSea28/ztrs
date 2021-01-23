package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.WireRopeOrderSetProtocol;

public class WireRopeOrderSetMessage extends BaseMessage{

    public WireRopeOrderSetMessage(long seq, byte[] cmdbytes){
        super(seq, WireRopeOrderSetProtocol.CMD_WIREROPE_ORDER_SET, cmdbytes);
    }
}
