package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.InverterDataReportProtocol;
import com.ztrs.zgj.device.protocol.UnlockCarProtocol;

public class InverterDataReportMessage extends BaseMessage{

    public InverterDataReportMessage(long seq, byte[] cmdbytes){
        super(seq, InverterDataReportProtocol.CMD_INVERTER_DATA_REPORT, cmdbytes);
    }

    public InverterDataReportMessage(){
        super(InverterDataReportProtocol.CMD_INVERTER_DATA_REPORT);
    }
}
