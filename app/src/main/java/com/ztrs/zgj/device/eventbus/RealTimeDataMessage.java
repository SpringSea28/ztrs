package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.RealTimeDataProtocol;
import com.ztrs.zgj.device.protocol.SwitchMachineProtocol;

public class RealTimeDataMessage extends BaseMessage{

    public RealTimeDataMessage(long seq, byte[] cmdbytes){
        super(seq, RealTimeDataProtocol.CMD_REAL_TIME_DATA, cmdbytes);
    }

    public RealTimeDataMessage(){
        super(RealTimeDataProtocol.CMD_REAL_TIME_DATA);
    }
}
