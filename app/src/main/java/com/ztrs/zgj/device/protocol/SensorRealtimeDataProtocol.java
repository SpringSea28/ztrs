package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.SensorRealtimeDataBean;
import com.ztrs.zgj.device.eventbus.SensorRealtimeDataMessage;

public class SensorRealtimeDataProtocol extends BaseProtocol{
    private static final String TAG= SensorRealtimeDataProtocol.class.getSimpleName();

    public static final byte CMD_SENSOR_REAL_TIME = (byte) 0x34;//传感器实时数据采集

    private DeviceOperateInterface deviceOperateInterface;

    public SensorRealtimeDataProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
        boolean success = parseData(data);
        if(!success){
            return;
        }
        byte[] ack = CommunicationProtocol.packetAck(CMD_SENSOR_REAL_TIME, new byte[]{0});
        deviceOperateInterface.sendDataToDevice(ack);
    }

    public void parseAck(byte[] data){
        boolean success = parseData(data);
        if(!success){
            ackReceiveError();
            return;
        }
        ackReceive(data);
    }

    private boolean parseData(byte[] data){
        if(data.length != 28){
            LogUtils.LogE(TAG,"data length error");
            return false;
        }
        SensorRealtimeDataBean sensorRealtimeDataBean = deviceOperateInterface.getZtrsDevice().getSensorRealtimeDataBean();
        long heightSensor = (((data[0]&0x00ff) << 24)
                | ((data[1]&0x00ff) << 16)
                | ((data[2]&0x00ff) << 8)
                | (data[3]&0x00ff))&0x00FFFFFFFFl;
        sensorRealtimeDataBean.setHeightSensor(heightSensor);
        long amplitudeSensor = (((data[4]&0x00ff) << 24)
                | ((data[5]&0x00ff) << 16)
                | ((data[6]&0x00ff) << 8)
                | (data[7]&0x00ff))&0x00FFFFFFFFl;
        sensorRealtimeDataBean.setAmplitudeSensor(amplitudeSensor);
        long aroundSensor = (((data[8]&0x00ff) << 24)
                | ((data[9]&0x00ff) << 16)
                | ((data[10]&0x00ff) << 8)
                | (data[11]&0x00ff))&0x00FFFFFFFFl;
        sensorRealtimeDataBean.setAroundSensor(aroundSensor);
        long weightSensor = (((data[12]&0x00ff) << 24)
                | ((data[13]&0x00ff) << 16)
                | ((data[14]&0x00ff) << 8)
                | (data[15]&0x00ff))&0x00FFFFFFFFl;
        sensorRealtimeDataBean.setWeightSensor(weightSensor);
        long slopeSensorX = (((data[16]&0x00ff) << 24)
                | ((data[17]&0x00ff) << 16)
                | ((data[18]&0x00ff) << 8)
                | (data[19]&0x00ff))&0x00FFFFFFFFl;
        sensorRealtimeDataBean.setSlopeSensorX(slopeSensorX);
        long slopeSensorY = (((data[20]&0x00ff) << 24)
                | ((data[21]&0x00ff) << 16)
                | ((data[22]&0x00ff) << 8)
                | (data[23]&0x00ff))&0x00FFFFFFFFl;
        sensorRealtimeDataBean.setSlopeSensorY(slopeSensorY);
        long windSpeedSensor = (((data[24]&0x00ff) << 24)
                | ((data[25]&0x00ff) << 16)
                | ((data[26]&0x00ff) << 8)
                | (data[27]&0x00ff))&0x00FFFFFFFFl;
        sensorRealtimeDataBean.setWindSpeedSensor(windSpeedSensor);
        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void querySensorRealtimeData(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_SENSOR_REAL_TIME,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        SensorRealtimeDataMessage msg = new SensorRealtimeDataMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

}
