package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.UnlockCarProtocol;

public class UnlockCarMessage extends BaseMessage{

    public UnlockCarMessage(long seq, byte[] cmdbytes){
        super(seq,UnlockCarProtocol.CMD_UNLOCK_CAR, cmdbytes);
    }
}
