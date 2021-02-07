package com.ztrs.zgj.device.protocol;

import android.util.Log;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.HeightCalibrationBean;
import com.ztrs.zgj.device.eventbus.SensorRealtimeDataMessage;
import com.ztrs.zgj.device.utils.Crc16;

import java.util.Calendar;

import static com.ztrs.zgj.device.protocol.UnlockCarProtocol.CMD_UNLOCK_CAR;

public class CommunicationProtocol{
    private static final String TAG = CommunicationProtocol.class.getSimpleName();

    public static long seq = 0;

    private static final int FRAME_HEAD_OFFSET = 0;
    private static final int LEN_OFFSET = FRAME_HEAD_OFFSET + 1;
    private static final int COMMUNICATION_NUMBER_OFFSET = LEN_OFFSET + 2;
    private static final int CMD_OFFSET = COMMUNICATION_NUMBER_OFFSET + 1;
    private static final int TIMER_OFFSET = CMD_OFFSET + 1;
    private static final int DATA_OFFSET = TIMER_OFFSET + 6;


    private static final byte FRAME_HEAD_B0 = (byte) 0XB0;//下位机主动发送数据到上位机帧头
    private static final byte FRAME_HEAD_B1 = (byte) 0xB1;//上位机应答下位机主到发送数据帧头
    private static final byte FRAME_HEAD_B2 = (byte) 0XB2;//上位机主动发送数据到下位机帧头
    private static final byte FRAME_HEAD_B3 = (byte) 0xB3;//下位机应答应位机主到发送数据帧头

    public static final int ACK_TIME_OUT = 1*1000;//MS
    public static final int RETRY_NUMBER = 2;
    public static final byte CMD_IDENTIFY = (byte) 0x13;//身份数据识别

    private byte[] lastNoPrase = null;

    DeviceOperateInterface deviceOperateInterface;

    public UnlockCarProtocol unlockCarProtocol;
    public SwitchMachineProtocol switchMachineProtocol;
    public RegionalRestrictionProtocol regionalRestrictionProtocol;
    public StaticParametersProtocol staticParametersProtocol;
    public RealTimeDataProtocol realTimeDataProtocol;
    public WorkCycleDataProtocol workCycleDataProtocol;
    public CommunicationErrorProtocol communicationErrorProtocol;
    public RelayConfigurationProtocol relayConfigurationProtocol;
    public RelayOutputControlProtocol relayOutputControlProtocol;
    public EmergencyCallProtocol emergencyCallProtocol;
    public SensorRealtimeDataProtocol sensorRealtimeDataProtocol;
    public HeightCalibrationProtocol heightCalibrationProtocol;
    public AmplitudeCalibrationProtocol amplitudeCalibrationProtocol;
    public AroundCalibrationProtocol aroundCalibrationProtocol;
    public WeightCalibrationProtocol weightCalibrationProtocol;
    public InverterDataReportProtocol inverterDataReportProtocol;
    public WireRopeDetectionParametersSetProtocol wireRopeDetectionParametersSetProtocol;
    public WireRopeDetectionReportProtocol wireRopeDetectionReportProtocol;
    public RegisterInfoProtocol registerInfoProtocol;
    public TorqueCalibrationProtocol torqueCalibrationProtocol;
    public TorqueCurveProtocol torqueCurveProtocol;
    public WireRopeOrderSetProtocol wireRopeOrderSetProtocol;
    public PreventCollisionCalibrationProtocol preventCollisionCalibrationProtocol;
    public PreventCollisionNearProtocol preventCollisionNearProtocol;
    public DeviceUpdateProtocol deviceUpdateProtocol;
    public DeviceVersionCheckProtocol deviceVersionCheckProtocol;


    public CommunicationProtocol(DeviceOperateInterface deviceOperateInterface){
        this.deviceOperateInterface = deviceOperateInterface;
        unlockCarProtocol = new UnlockCarProtocol(deviceOperateInterface);
        switchMachineProtocol = new SwitchMachineProtocol(deviceOperateInterface);
        regionalRestrictionProtocol = new RegionalRestrictionProtocol(deviceOperateInterface);
        staticParametersProtocol = new StaticParametersProtocol(deviceOperateInterface);
        realTimeDataProtocol = new RealTimeDataProtocol(deviceOperateInterface);
        workCycleDataProtocol = new WorkCycleDataProtocol(deviceOperateInterface);
        communicationErrorProtocol = new CommunicationErrorProtocol(deviceOperateInterface);
        relayConfigurationProtocol = new RelayConfigurationProtocol(deviceOperateInterface);
        relayOutputControlProtocol = new RelayOutputControlProtocol(deviceOperateInterface);
        emergencyCallProtocol = new EmergencyCallProtocol(deviceOperateInterface);
        sensorRealtimeDataProtocol = new SensorRealtimeDataProtocol(deviceOperateInterface);
        heightCalibrationProtocol = new HeightCalibrationProtocol(deviceOperateInterface);
        amplitudeCalibrationProtocol = new AmplitudeCalibrationProtocol(deviceOperateInterface);
        aroundCalibrationProtocol = new AroundCalibrationProtocol(deviceOperateInterface);
        weightCalibrationProtocol = new WeightCalibrationProtocol(deviceOperateInterface);
        inverterDataReportProtocol = new InverterDataReportProtocol(deviceOperateInterface);
        wireRopeDetectionParametersSetProtocol = new WireRopeDetectionParametersSetProtocol(deviceOperateInterface);
        wireRopeDetectionReportProtocol = new WireRopeDetectionReportProtocol(deviceOperateInterface);
        registerInfoProtocol = new RegisterInfoProtocol(deviceOperateInterface);
        torqueCalibrationProtocol = new TorqueCalibrationProtocol(deviceOperateInterface);
        torqueCurveProtocol = new TorqueCurveProtocol(deviceOperateInterface);
        wireRopeOrderSetProtocol = new WireRopeOrderSetProtocol(deviceOperateInterface);
        preventCollisionCalibrationProtocol = new PreventCollisionCalibrationProtocol(deviceOperateInterface);
        preventCollisionNearProtocol = new PreventCollisionNearProtocol(deviceOperateInterface);
        deviceUpdateProtocol = new DeviceUpdateProtocol(deviceOperateInterface);
        deviceVersionCheckProtocol = new DeviceVersionCheckProtocol(deviceOperateInterface);
    }

    public void dataParse(byte[] data) {
        int startOffset = 0;
        int i = 0;
        if (lastNoPrase != null) {
            byte[] wholeData = new byte[lastNoPrase.length + data.length];
            System.arraycopy(lastNoPrase, 0, wholeData, 0, lastNoPrase.length);
            System.arraycopy(data, 0, wholeData, lastNoPrase.length, data.length);
            lastNoPrase = null;
            data = wholeData;
        }
        for (i = 0; i < data.length; i++) {
            if (data[i] != FRAME_HEAD_B0 && data[i] != FRAME_HEAD_B3) {
                continue;
            }
            if(i+LEN_OFFSET >=data.length){
                continue;
            }

            if ((data[i + LEN_OFFSET] & 0x00ff) < 13) {
                continue;
            }

            if ((data[i + LEN_OFFSET] & 0x00ff) > data.length - i) {
                continue;
            }


            byte[] onePackageData = new byte[data[i + LEN_OFFSET]&0x00ff];
            System.arraycopy(data, i, onePackageData, 0, onePackageData.length);
            int crc16 = Crc16.getCRC(onePackageData, onePackageData.length - 2);
            byte crcH = (byte) (crc16 >> 8);
            byte crcL = (byte) crc16;
            int crc16Offset = DATA_OFFSET + calDataLen(onePackageData[LEN_OFFSET] &0x00ff);
            int receiveCrc = (data[crc16Offset] << 8) | (data[crc16Offset + 1] & 0x00ff);
            if (crcH != data[crc16Offset] || crcL!=data[crc16Offset+1]) {
                continue;
            }
            parseOnePackage(onePackageData);
            i += onePackageData.length;
            startOffset = i;
            i = i - 1;
        }
        if (startOffset != i) {
            if (i - startOffset > 256 * 3) {
                LogUtils.LogE(TAG, "too long to get correct data");
                lastNoPrase = null;
            } else {
                lastNoPrase = new byte[i - startOffset];
                System.arraycopy(data, startOffset, lastNoPrase, 0, lastNoPrase.length);
            }
        }

    }

    private void parseOnePackage(byte[] data) {
        byte cmd = data[CMD_OFFSET];
        byte[] loadData = new byte[calDataLen(data.length)];
        System.arraycopy(data,DATA_OFFSET,loadData,0,loadData.length);
        boolean b = parseTime(data);
        if(!b){
            return;
        }
        if (data[0] == FRAME_HEAD_B0) {
            parseCmd(cmd,loadData);
        } else if (data[0] == FRAME_HEAD_B3) {
            parseAck(cmd,loadData);
        } else {
            LogUtils.LogI(TAG,"unknown frame head");
        }

    }

    private boolean parseTime(byte[] data){
        byte year = data[TIMER_OFFSET];
        byte month = data[TIMER_OFFSET+1];
        byte day = data[TIMER_OFFSET+2];
        byte hour = data[TIMER_OFFSET+3];
        byte min = data[TIMER_OFFSET+4];
        byte sec = data[TIMER_OFFSET+5];
        String time = LogUtils.toHexString(year)+LogUtils.toHexString(month)
                +LogUtils.toHexString(day)+":"+LogUtils.toHexString(hour)+
                LogUtils.toHexString(min)+LogUtils.toHexString(sec);
//        LogUtils.LogI(TAG,"receive time: "+time);
        Calendar instance = Calendar.getInstance();
        int yearPhone = instance.get(Calendar.YEAR);
        int yearMonth = instance.get(Calendar.MONTH);
//        if(year>=21 && month>2){
//            return false;
//        }
        return true;
    }

    private void parseCmd(byte cmd,byte[] data){
        LogUtils.LogI(TAG,"parseCMD:"+LogUtils.toHexString(cmd)+"  "+getCmdName(cmd));
        switch (cmd){
            case CMD_UNLOCK_CAR:
                unlockCarProtocol.parseCmd(data);
                break;
            case SwitchMachineProtocol.CMD_SWITCH_MACHINE:
                switchMachineProtocol.parseCmd(data);
                break;
            case RealTimeDataProtocol.CMD_REAL_TIME_DATA:
                realTimeDataProtocol.parseCmd(data);
                break;
            case WorkCycleDataProtocol.CMD_WORK_CYCLE_DATA:
                workCycleDataProtocol.parseCmd(data);
                break;
            case SensorRealtimeDataProtocol.CMD_SENSOR_REAL_TIME:
                sensorRealtimeDataProtocol.parseCmd(data);
                break;
            case InverterDataReportProtocol.CMD_INVERTER_DATA_REPORT:
                inverterDataReportProtocol.parseCmd(data);
                break;
            case WireRopeDetectionReportProtocol.CMD_WIREROPE_DETECTION_REPORT:
                wireRopeDetectionReportProtocol.parseCmd(data);
                break;
            case RegisterInfoProtocol.CMD_REGISTER_INFO:
                registerInfoProtocol.parseCmd(data);
                break;
            case PreventCollisionNearProtocol.CMD_NEAR:
                preventCollisionNearProtocol.parseCmd(data);
                break;
            case DeviceVersionCheckProtocol.CMD_VERSION_CHECK:
                deviceVersionCheckProtocol.parseCmd(data);
                break;
        }
    }

    private void parseAck(byte cmd,byte[] data){
        LogUtils.LogI(TAG,"parseACK:"+LogUtils.toHexString(cmd)+"  "+getCmdName(cmd));
        switch (cmd){
            case CMD_UNLOCK_CAR:
                unlockCarProtocol.parseAck(data);
                break;
            case SwitchMachineProtocol.CMD_SWITCH_MACHINE:
                switchMachineProtocol.parseAck(data);
                break;
            case RegionalRestrictionProtocol.CMD_REGIONAL_RESTRICTION:
                regionalRestrictionProtocol.parseAck(data);
                break;
            case StaticParametersProtocol.CMD_STATIC_PARAMETER:
                staticParametersProtocol.parseAck(data);
                break;
            case RealTimeDataProtocol.CMD_REAL_TIME_DATA:
                realTimeDataProtocol.parseAck(data);
                break;
            case WorkCycleDataProtocol.CMD_WORK_CYCLE_DATA:
                workCycleDataProtocol.parseAck(data);
                break;
            case CommunicationErrorProtocol.CMD_COMMUNICATION_ERROR:
                communicationErrorProtocol.parseAck(data);
                break;
            case RelayConfigurationProtocol.CMD_RELAY_CONFIGURATION:
                relayConfigurationProtocol.parseAck(data);
                break;
            case RelayOutputControlProtocol.CMD_RELAY_OUTPUT_CONTROL:
                relayOutputControlProtocol.parseAck(data);
                break;
            case EmergencyCallProtocol.CMD_EMERGENCY_CALL:
                emergencyCallProtocol.parseAck(data);
                break;
            case SensorRealtimeDataProtocol.CMD_SENSOR_REAL_TIME:
                sensorRealtimeDataProtocol.parseAck(data);
                break;
            case HeightCalibrationProtocol.CMD_HEIGHT_CALIBRATION:
                heightCalibrationProtocol.parseAck(data);
                break;
            case AmplitudeCalibrationProtocol.CMD_AMPLITUDE_CALIBRATION:
                amplitudeCalibrationProtocol.parseAck(data);
                break;
            case AroundCalibrationProtocol.CMD_AROUND_CALIBRATION:
                aroundCalibrationProtocol.parseAck(data);
                break;
            case WeightCalibrationProtocol.CMD_WEIGHT_CALIBRATION:
                weightCalibrationProtocol.parseAck(data);
                break;
            case WireRopeDetectionParametersSetProtocol.CMD_WIREROPE_DETECTION_PARAMETERS_SET:
                wireRopeDetectionParametersSetProtocol.parseAck(data);
                break;
            case TorqueCalibrationProtocol.CMD_TORQUE_CALIBRATION:
                torqueCalibrationProtocol.parseAck(data);
                break;
            case TorqueCurveProtocol.CMD_TORQUE_CURVE:
                torqueCurveProtocol.parseAck(data);
                break;
            case WireRopeOrderSetProtocol.CMD_WIREROPE_ORDER_SET:
                wireRopeOrderSetProtocol.parseAck(data);
                break;
            case PreventCollisionCalibrationProtocol.CMD_PREVENT_COLLISION_CALIBRATION:
                preventCollisionCalibrationProtocol.parseAck(data);
                break;
            case DeviceUpdateProtocol.CMD_UPDATE:
                deviceUpdateProtocol.parseAck(data);
                break;
            case DeviceVersionCheckProtocol.CMD_VERSION_CHECK:
                deviceVersionCheckProtocol.parseAck(data);
                break;
            case RegisterInfoProtocol.CMD_REGISTER_INFO:
                registerInfoProtocol.parseAck(data);
                break;
        }
    }

    private int calDataLen(int framLen) {
        return framLen - (DATA_OFFSET) - 2;
    }

    public static byte[] packetAck(byte cmd,byte[] cmdData){
        return packetData(FRAME_HEAD_B1,cmd,cmdData);
    }

    public static byte[] packetCmd(byte cmd,byte[] cmdData){
        return packetData(FRAME_HEAD_B2,cmd,cmdData);
    }

    public static byte[] packetData(byte frameHead, byte cmd, byte[] cmdData){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        byte yearBcd =(byte)( (((year%100)/10)<<4) + (year%10));
        int month = calendar.get(Calendar.MONTH)+1;
        byte monthBcd =(byte)( ((month/10)<<4) + (month%10));
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        byte dayBcd =(byte)( ((day/10)<<4) + (day%10));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        byte hourBcd =(byte)( ((hour/10)<<4) + (hour%10));
        int min = calendar.get(Calendar.MINUTE);
        byte minBcd =(byte)( ((min/10)<<4) + (min%10));
        int sec = calendar.get(Calendar.SECOND)+1;
        byte secBcd =(byte)( ((sec/10)<<4) + (sec%10));

        byte[] data = new byte[DATA_OFFSET+2+cmdData.length];
        data[0] = frameHead;
        data[1] = (byte)data.length;
        data[2] = 0;
        data[3] = 0;
        data[4] = cmd;
        data[5] = yearBcd;
        data[6] = monthBcd;
        data[7] = dayBcd;
        data[8] = hourBcd;
        data[9] = minBcd;
        data[10] = secBcd;
        System.arraycopy(cmdData,0,data,DATA_OFFSET,cmdData.length);
        int crc = Crc16.getCRC(data, data.length - 2);
        data[data.length -2] =(byte)(crc>>8);
        data[data.length -1] =(byte)crc;
        return data;
    }

    private String getCmdName(byte cmd) {
        String cmdName = "unknown";
        switch (cmd) {
            case CMD_UNLOCK_CAR:
                cmdName = "解锁车";
                break;
            case SwitchMachineProtocol.CMD_SWITCH_MACHINE:
                cmdName = "开关机";
                break;
            case RegionalRestrictionProtocol.CMD_REGIONAL_RESTRICTION:
                cmdName = "区域限制";
                break;
            case StaticParametersProtocol.CMD_STATIC_PARAMETER:
                cmdName = "静态参数";
                break;
            case RealTimeDataProtocol.CMD_REAL_TIME_DATA:
                cmdName = "实时数据";
                break;
            case WorkCycleDataProtocol.CMD_WORK_CYCLE_DATA:
                cmdName = "工作循环数据";
                break;
            case CommunicationErrorProtocol.CMD_COMMUNICATION_ERROR:
                cmdName = "通讯异常";
                break;
            case RelayConfigurationProtocol.CMD_RELAY_CONFIGURATION:
                cmdName = "继电器配置";
                break;
            case RelayOutputControlProtocol.CMD_RELAY_OUTPUT_CONTROL:
                cmdName = "继电器输出控制";
                break;
            case EmergencyCallProtocol.CMD_EMERGENCY_CALL:
                cmdName = "紧急呼叫";
                break;
            case SensorRealtimeDataProtocol.CMD_SENSOR_REAL_TIME:
                cmdName = "传感器实时数据";
                break;
            case HeightCalibrationProtocol.CMD_HEIGHT_CALIBRATION:
                cmdName = "高度标定";
                break;
            case AmplitudeCalibrationProtocol.CMD_AMPLITUDE_CALIBRATION:
                cmdName = "幅度标定";
                break;
            case AroundCalibrationProtocol.CMD_AROUND_CALIBRATION:
                cmdName = "回转标定";
                break;
            case WeightCalibrationProtocol.CMD_WEIGHT_CALIBRATION:
                cmdName = "载重标定";
                break;
            case WireRopeDetectionParametersSetProtocol.CMD_WIREROPE_DETECTION_PARAMETERS_SET:
                cmdName = "钢丝绳检测参数设置";
                break;
            case TorqueCalibrationProtocol.CMD_TORQUE_CALIBRATION:
                cmdName = "力矩标定";
                break;
            case TorqueCurveProtocol.CMD_TORQUE_CURVE:
                cmdName = "力矩曲线";
                break;
            case WireRopeOrderSetProtocol.CMD_WIREROPE_ORDER_SET:
                cmdName = "钢丝绳命令设置";
                break;
            case PreventCollisionCalibrationProtocol.CMD_PREVENT_COLLISION_CALIBRATION:
                cmdName = "防碰撞设置";
                break;
            case DeviceUpdateProtocol.CMD_UPDATE:
                cmdName = "设备升级";
                break;
            case DeviceVersionCheckProtocol.CMD_VERSION_CHECK:
                cmdName = "设备版本号查询";
                break;
            case RegisterInfoProtocol.CMD_REGISTER_INFO:
                cmdName = "设备注册信息";
                break;
        }
        return cmdName;
    }
}
