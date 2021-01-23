package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.EmergencyCallProtocol;
import com.ztrs.zgj.device.protocol.WorkCycleDataProtocol;

public class EmergencyCallMessage extends BaseMessage{

    public EmergencyCallMessage(long seq, byte[] cmdbytes){
        super(seq, EmergencyCallProtocol.CMD_EMERGENCY_CALL, cmdbytes);
    }
}
