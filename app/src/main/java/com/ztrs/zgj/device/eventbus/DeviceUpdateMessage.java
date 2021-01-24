package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.DeviceUpdateProtocol;
import com.ztrs.zgj.device.protocol.UnlockCarProtocol;

public class DeviceUpdateMessage extends BaseMessage{

    public DeviceUpdateMessage(long seq, byte[] cmdbytes){
        super(seq, DeviceUpdateProtocol.CMD_UPDATE, cmdbytes);
    }
}
