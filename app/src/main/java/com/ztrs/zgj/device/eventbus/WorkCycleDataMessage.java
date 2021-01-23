package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.StaticParametersProtocol;
import com.ztrs.zgj.device.protocol.WorkCycleDataProtocol;

public class WorkCycleDataMessage extends BaseMessage{

    public WorkCycleDataMessage(long seq, byte[] cmdbytes){
        super(seq, WorkCycleDataProtocol.CMD_WORK_CYCLE_DATA, cmdbytes);
    }
}
