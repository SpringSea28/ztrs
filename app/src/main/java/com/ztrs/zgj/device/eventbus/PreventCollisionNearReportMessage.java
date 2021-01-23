package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.PreventCollisionNearProtocol;
import com.ztrs.zgj.device.protocol.WireRopeDetectionReportProtocol;

public class PreventCollisionNearReportMessage extends BaseMessage{

    public PreventCollisionNearReportMessage(long seq, byte[] cmdbytes){
        super(seq, PreventCollisionNearProtocol.CMD_NEAR, cmdbytes);
    }

    public PreventCollisionNearReportMessage( ){
        super(PreventCollisionNearProtocol.CMD_NEAR);
    }
}
