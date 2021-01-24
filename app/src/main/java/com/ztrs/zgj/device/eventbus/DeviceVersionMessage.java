package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.DeviceVersionCheckProtocol;

public class DeviceVersionMessage extends BaseMessage{

    public DeviceVersionMessage(long seq, byte[] cmdbytes){
        super(seq, DeviceVersionCheckProtocol.CMD_VERSION_CHECK, cmdbytes);
    }

    public DeviceVersionMessage(){
        super(DeviceVersionCheckProtocol.CMD_VERSION_CHECK);
    }
}
