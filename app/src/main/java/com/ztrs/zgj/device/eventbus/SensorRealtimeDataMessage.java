package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.SensorRealtimeDataProtocol;
import com.ztrs.zgj.device.protocol.WorkCycleDataProtocol;

public class SensorRealtimeDataMessage extends BaseMessage{

    public SensorRealtimeDataMessage(long seq, byte[] cmdbytes){
        super(seq, SensorRealtimeDataProtocol.CMD_SENSOR_REAL_TIME, cmdbytes);
    }
}
