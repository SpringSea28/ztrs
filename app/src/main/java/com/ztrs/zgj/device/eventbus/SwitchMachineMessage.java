package com.ztrs.zgj.device.eventbus;

import com.ztrs.zgj.device.protocol.SwitchMachineProtocol;

public class SwitchMachineMessage extends BaseMessage{

    public SwitchMachineMessage(long seq, byte[] cmdbytes){
        super(seq, SwitchMachineProtocol.CMD_SWITCH_MACHINE, cmdbytes);
    }
}
