package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.WireRopeDetectionReportProtocol;

public class WireRopeDetectionReportMessage extends BaseMessage{

    public WireRopeDetectionReportMessage(long seq, byte[] cmdbytes){
        super(seq, WireRopeDetectionReportProtocol.CMD_WIREROPE_DETECTION_REPORT, cmdbytes);
    }

    public WireRopeDetectionReportMessage( ){
        super(WireRopeDetectionReportProtocol.CMD_WIREROPE_DETECTION_REPORT);
    }
}
