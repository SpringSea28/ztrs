package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.AnnouncementProtocol;
import com.ztrs.zgj.device.protocol.RealTimeDataProtocol;

public class AnnouncementMessage extends BaseMessage{

    public AnnouncementMessage(long seq, byte[] cmdbytes){
        super(seq, AnnouncementProtocol.CMD_ANNOUNCEMENT, cmdbytes);
    }

    public AnnouncementMessage(){
        super(AnnouncementProtocol.CMD_ANNOUNCEMENT);
    }
}
