package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.UnlockCarProtocol;
import com.ztrs.zgj.device.protocol.VolumeProtocol;

public class VolumeMessage extends BaseMessage{

    public VolumeMessage(long seq, byte[] cmdbytes){
        super(seq, VolumeProtocol.CMD_VOLUME, cmdbytes);
    }
}
