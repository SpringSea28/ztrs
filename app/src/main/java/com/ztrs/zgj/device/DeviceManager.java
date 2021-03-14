package com.ztrs.zgj.device;

import android.util.Log;

import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortFinder;
import com.kongqw.serialportlibrary.SerialPortManager;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;
import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.AmplitudeCalibrationBean;
import com.ztrs.zgj.device.bean.AroundCalibrationBean;
import com.ztrs.zgj.device.bean.PreventCollisionCalibrationBean;
import com.ztrs.zgj.device.bean.TorqueCalibrationBean;
import com.ztrs.zgj.device.bean.TorqueCurveApplyBean;
import com.ztrs.zgj.device.bean.ZtrsDevice;
import com.ztrs.zgj.device.bean.HeightCalibrationBean;
import com.ztrs.zgj.device.bean.RegionalRestrictionsBean;
import com.ztrs.zgj.device.bean.RegionalRestrictionsBean.SectorRestriction;
import com.ztrs.zgj.device.bean.RelayConfigurationBean;
import com.ztrs.zgj.device.bean.RelayOutputControlBean;
import com.ztrs.zgj.device.bean.StaticParameterBean;
import com.ztrs.zgj.device.bean.WeightCalibrationBean;
import com.ztrs.zgj.device.bean.WireRopeDetectionParametersSetBean;
import com.ztrs.zgj.device.protocol.CommunicationProtocol;
import com.ztrs.zgj.device.protocol.DeviceOperateInterface;
import com.ztrs.zgj.device.protocol.PreventCollisionCalibrationProtocol;
import com.ztrs.zgj.main.msg.SerialPortOpenResultMsg;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class DeviceManager implements DeviceOperateInterface {
    private static final String TAG = DeviceManager.class.getSimpleName();

    private static volatile DeviceManager deviceManager;
    private ZtrsDevice ztrsDevice;

    private CommunicationProtocol communicationProtocol;
    private SerialPortManager mSerialPortManager;
    private Device serialDevice;


    OnOpenSerialPortListener onOpenSerialPortListener = new OnOpenSerialPortListener() {
        @Override
        public void onSuccess(File device) {
            Log.e(TAG,"open serial success");
            EventBus.getDefault().post(new SerialPortOpenResultMsg(true));
        }

        @Override
        public void onFail(File device, Status status) {
            Log.e(TAG,"open serial fail");
            EventBus.getDefault().post(new SerialPortOpenResultMsg(false));
        }
    };

    private DeviceManager(){
        ztrsDevice = new ZtrsDevice();
        communicationProtocol = new CommunicationProtocol(this);
        mSerialPortManager = new SerialPortManager();
    }

    public static DeviceManager getInstance(){
        if(deviceManager == null){
            synchronized (DeviceManager.class){
                if(deviceManager == null){
                    deviceManager = new DeviceManager();
                }
            }
        }
        return deviceManager;
    }


    public void openSerial(){

        SerialPortFinder serialPortFinder = new SerialPortFinder();
        ArrayList<Device> devices = serialPortFinder.getDevices();

        for(Device device:devices){
            if(device.getName().equals("ttyS3")){
                this.serialDevice = device;
            }
        }

        if(serialDevice == null){
            Log.i(TAG, "not find ttyS3= ");
            return;
        }

        // 打开串口
        boolean openSerialPort = mSerialPortManager.setOnOpenSerialPortListener(onOpenSerialPortListener)
                .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                    @Override
                    public void onDataReceived(byte[] bytes) {
                        final byte[] finalBytes = bytes;
                        onDataReceive(finalBytes);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showToast(String.format("接收\n%s", new String(finalBytes)));
//                            }
//                        });
                    }

                    @Override
                    public void onDataSent(byte[] bytes) {
                        Log.i(TAG, "onDataSent [ byte[] ]: " + LogUtils.toHexString(bytes));
                        final byte[] finalBytes = bytes;
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showToast(String.format("发送\n%s", new String(finalBytes)));
//                            }
//                        });
                    }
                })
                .openSerialPort(serialDevice.getFile(), 115200);

        Log.i(TAG, "onCreate: openSerialPort = " + openSerialPort);
    }

    public void closeOpenSerial(){
        mSerialPortManager.closeSerialPort();
    }

//    // //协议3.2
//    public void queryUnlockCar(){
//        communicationProtocol.unlockCarProtocol.queryUnLockCar();
//    }
//
//    public void unlockCar(){
//        communicationProtocol.unlockCarProtocol.unLockCar();
//    }
//
//    public void lockCar(){
//        communicationProtocol.unlockCarProtocol.lockCar();
//    }

    //协议3.3
    public void querySwitchMachine(){
        communicationProtocol.switchMachineProtocol.querySwitchMachine();
    }

    //协议3.4
    public void queryRegionalRestriction(){
        communicationProtocol.regionalRestrictionProtocol.queryRegionalRestriction();
    }

    public void setSectorRegionalRestriction(SectorRestriction sectorRestriction){
        communicationProtocol.regionalRestrictionProtocol.setSectorRegionalRestriction(sectorRestriction);
    }

    public void setPolarRegionalRestriction(RegionalRestrictionsBean.PolarCoordinate polarCoordinate){
        communicationProtocol.regionalRestrictionProtocol.setPolarRegionalRestriction(polarCoordinate);
    }

    public void setOrthogonalRegionalRestriction(RegionalRestrictionsBean.OrthogonalCoordinate orthogonalCoordinate){
        communicationProtocol.regionalRestrictionProtocol.setOrthogonalRegionalRestriction(orthogonalCoordinate);
    }

    //协议3.5
    public void queryStaticParameter(){
        communicationProtocol.staticParametersProtocol.queryStaticParameter();
    }

    public void setStaticParameter(StaticParameterBean bean){
        communicationProtocol.staticParametersProtocol.setStaticParameter(bean);
    }

    // 协议3.6
    public void queryRealTimeData(byte frequency,byte number){
        communicationProtocol.realTimeDataProtocol.queryRealTimeData(frequency,number);
    }

    //协议3.7
    public void queryWorkCycleData(){
        communicationProtocol.workCycleDataProtocol.queryWorkCycleData();
    }

    //协议3.9
    public void queryRelayConfiguration(){
        communicationProtocol.relayConfigurationProtocol.queryRelayConfiguration();
    }

    public void setRelayConfiguration(RelayConfigurationBean relayConfiguration){
        communicationProtocol.relayConfigurationProtocol.setRelayConfiguration(relayConfiguration);
    }

    //协议3.10
    public void queryRelayOutputConfig(){
        communicationProtocol.relayOutputControlProtocol.queryRelayOutputControl();
    }

    public void setRelayOutputControl(RelayOutputControlBean outputControlBean){
        communicationProtocol.relayOutputControlProtocol.setRelayOutputControl(outputControlBean);
    }

    //协议3.11
    public void emergencyCall(){
        communicationProtocol.emergencyCallProtocol.emergencyCall();
    }

    //3.12
    public void querySensorRealtimeData(){
        communicationProtocol.sensorRealtimeDataProtocol.querySensorRealtimeData();
    }

    //3.13
    public void queryHeightCalibration(){
        communicationProtocol.heightCalibrationProtocol.queryHeightCalibration();
    }

    public void heightCalibration(HeightCalibrationBean calibrationBean){
        communicationProtocol.heightCalibrationProtocol.heightCalibration(calibrationBean);
    }

    //3.14
    public void queryAmplitudeCalibration(){
        communicationProtocol.amplitudeCalibrationProtocol.queryAmplitudeCalibration();
    }

    public void amplitudeCalibration(AmplitudeCalibrationBean calibrationBean){
        communicationProtocol.amplitudeCalibrationProtocol.amplitudeCalibration(calibrationBean);
    }

    //3.15
    public void queryAroundCalibration(){
        communicationProtocol.aroundCalibrationProtocol.queryAroundCalibration();
    }

    public void aroundCalibration(AroundCalibrationBean calibrationBean){
        communicationProtocol.aroundCalibrationProtocol.aroundCalibration(calibrationBean);
    }

    //3.16
    public void queryWeightCalibration(){
        communicationProtocol.weightCalibrationProtocol.queryWeightCalibration();
    }

    public void weightCalibration(WeightCalibrationBean calibrationBean){
        communicationProtocol.weightCalibrationProtocol.weightCalibration(calibrationBean);
    }

    //3.21
    public void queryWireRopeDetectionParameters(){
        communicationProtocol.wireRopeDetectionParametersSetProtocol.queryWireRopeDetectionParameters();
    }

    public void setWireRopeDetectionParameters(WireRopeDetectionParametersSetBean bean){
        communicationProtocol.wireRopeDetectionParametersSetProtocol.setWireRopeDetectionParameters(bean);
    }

    //3.19
    public void queryTorqueCalibration(){
        communicationProtocol.torqueCalibrationProtocol.queryTorqueCalibration();
    }

    public void torqueCalibration(TorqueCalibrationBean bean){
        communicationProtocol.torqueCalibrationProtocol.torqueCalibration(bean);
    }

    //3.29
    public void queryTorqueCurve(){
        communicationProtocol.torqueCurveProtocol.queryTorqueCurve();
    }

    public void setTorqueCurve(TorqueCurveApplyBean bean){
        communicationProtocol.torqueCurveProtocol.setTorqueCurve(bean);
    }

    //3.30
    public void wireRopeStartDetect(){
        communicationProtocol.wireRopeOrderSetProtocol.startDetect();
    }

    public void wireRopeStopDetect(){
        communicationProtocol.wireRopeOrderSetProtocol.stopDetect();
    }

    public void wireRopeQueryState(){
        communicationProtocol.wireRopeOrderSetProtocol.queryState();
    }

    public void wireRopeReset(){
        communicationProtocol.wireRopeOrderSetProtocol.reset();
    }

    public void wireRopeClear(){
        communicationProtocol.wireRopeOrderSetProtocol.clear();
    }


    //3.31
    public void queryPreventCollisionCalibration(){
        communicationProtocol.preventCollisionCalibrationProtocol.queryPreventCollisionCalibration();
    }

    public void preventCollisionCalibration(PreventCollisionCalibrationBean bean){
        communicationProtocol.preventCollisionCalibrationProtocol.preventCollisionCalibration(bean);
    }

    //3.24 设备升级
    public void deviceUpdate(){
        communicationProtocol.deviceUpdateProtocol.deviceUpdate();
    }

    //3.25 设备版本号
    public void deviceVersionCheck(){
        communicationProtocol.deviceVersionCheckProtocol.deviceVersionCheck();
    }

    public void queryRegisterInfo(){
        communicationProtocol.registerInfoProtocol.queryRegisterInfo();
    }

    //3.33
    public void queryVolume(){
        communicationProtocol.volumeProtocol.queryVolume();
    }

    public void setVolume(byte volume){
        communicationProtocol.volumeProtocol.setVolume(volume);
    }

    @Override
    public void sendDataToDevice(byte[] data){
        LogUtils.LogE(TAG,"send: "+LogUtils.toHexString(data));
        boolean b = mSerialPortManager.sendBytes(data);
        LogUtils.LogE(TAG,"send result: "+b);
    }

    @Override
    public ZtrsDevice getZtrsDevice() {
        return ztrsDevice;
    }

    public void onDataReceive(byte[] data){
//        LogUtils.LogE(TAG,"receive: "+LogUtils.toHexString(data));
        communicationProtocol.dataParse(data);
    }
}
