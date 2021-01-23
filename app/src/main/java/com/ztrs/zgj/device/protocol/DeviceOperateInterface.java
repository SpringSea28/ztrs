package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.device.bean.ZtrsDevice;

public interface DeviceOperateInterface {

    void sendDataToDevice(byte[] data);
    ZtrsDevice getZtrsDevice();
}
