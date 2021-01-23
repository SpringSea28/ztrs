package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.RealTimeDataProtocol;
import com.ztrs.zgj.device.protocol.RegisterInfoProtocol;

public class RegisterInfoMessage extends BaseMessage{

    public RegisterInfoMessage(long seq, byte[] cmdbytes){
        super(seq, RegisterInfoProtocol.CMD_REGISTER_INFO, cmdbytes);
    }

    public RegisterInfoMessage(){
        super(RegisterInfoProtocol.CMD_REGISTER_INFO);
    }
}
