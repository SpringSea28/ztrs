package com.ztrs.zgj.setting;

import android.os.Bundle;
import android.widget.Toast;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.bean.AroundCalibrationBean;
import com.ztrs.zgj.device.bean.CalibrationBean;
import com.ztrs.zgj.device.bean.HeightCalibrationBean;
import com.ztrs.zgj.device.bean.RealTimeDataBean;
import com.ztrs.zgj.device.bean.SensorRealtimeDataBean;
import com.ztrs.zgj.device.eventbus.AroundCalibrationMessage;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.HeightCalibrationMessage;
import com.ztrs.zgj.device.eventbus.RealTimeDataMessage;
import com.ztrs.zgj.device.eventbus.SensorRealtimeDataMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AroundSensorCalibrationActivity extends BaseSensorCalibrationActivity {

    AroundCalibrationBean aroundCalibrationBean;
    SensorRealtimeDataBean sensorRealtimeDataBean;
    RealTimeDataBean realTimeDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TAG = AroundSensorCalibrationActivity.class.getSimpleName();
        super.onCreate(savedInstanceState);
        DeviceManager.getInstance().queryAroundCalibration();
    }

    @Override
    void initData() {
        initAroundCalibrationBean();
        sensorRealtimeDataBean = DeviceManager.getInstance().getZtrsDevice().getSensorRealtimeDataBean();
        realTimeDataBean = DeviceManager.getInstance().getZtrsDevice().getRealTimeDataBean();
    }

    private void initAroundCalibrationBean(){
        AroundCalibrationBean aroundCalibrationBean
                = DeviceManager.getInstance().getZtrsDevice().getAroundCalibrationBean();
        try {
            this.aroundCalibrationBean = (AroundCalibrationBean) aroundCalibrationBean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }


    @Override
    String getTitleText() {
        return "回转标定";
    }

    @Override
    String getUnit() {
        return "度";
    }

    @Override
    long getSensorValue() {
        return  sensorRealtimeDataBean.getAroundSensor();
    }

    @Override
    int getCurMeasureValue() {
        int height = realTimeDataBean.getAroundAngle();
        return height;
    }

    @Override
    long getCode1Value() {
        if(aroundCalibrationBean != null){
            return aroundCalibrationBean.getCurrent1();
        }
        return 0;
    }

    @Override
    int getCalibration1Value() {
        if(aroundCalibrationBean != null){
            return aroundCalibrationBean.getCalibration1();
        }
        return 0;
    }

    @Override
    long getCode2Value() {
        if(aroundCalibrationBean != null){
            return aroundCalibrationBean.getCurrent2();
        }
        return 0;
    }

    @Override
    int getCalibration2Value() {
        if(aroundCalibrationBean != null){
            return aroundCalibrationBean.getCalibration2();
        }
        return 0;
    }

    @Override
    int getLowWarnValue() {
        if(aroundCalibrationBean != null){
            return aroundCalibrationBean.getLowWarnValue();
        }
        return 0;
    }

    @Override
    int getLowAlarmValue() {
        if(aroundCalibrationBean != null){
            return aroundCalibrationBean.getLowAlarmValue();
        }
        return 0;
    }

    @Override
    int getHighWarnValue() {
        if(aroundCalibrationBean != null){
            return aroundCalibrationBean.getHighWarnValue();
        }
        return 0;
    }

    @Override
    int getHighAlarmValue() {
        if(aroundCalibrationBean != null){
            return aroundCalibrationBean.getHighAlarmValue();
        }
        return 0;
    }

    @Override
    int getLowControl() {
        if(aroundCalibrationBean != null){
            return aroundCalibrationBean.getLowAlarmRelayControl();
        }
        return 0;
    }

    @Override
    int getLowOutput() {
        if(aroundCalibrationBean != null){
            return aroundCalibrationBean.getLowAlarmRelayOutput();
        }
        return 0;
    }

    @Override
    int getHighControl() {
        if(aroundCalibrationBean != null){
            return aroundCalibrationBean.getHighAlarmRelayControl();
        }
        return 0;
    }

    @Override
    int getHighOutput() {
        if(aroundCalibrationBean != null){
            return aroundCalibrationBean.getHighAlarmRelayOutput();
        }
        return 0;
    }

    @Override
    void saveCalibration(CalibrationBean calibrationBean) {
        aroundCalibrationBean.setCurrent1(calibrationBean.getCurrent1());
        aroundCalibrationBean.setCalibration1(calibrationBean.getCalibration1());
        aroundCalibrationBean.setCurrent2(calibrationBean.getCurrent2());
        aroundCalibrationBean.setCalibration2(calibrationBean.getCalibration2());
        aroundCalibrationBean.setLowWarnValue(calibrationBean.getLowWarnValue());
        aroundCalibrationBean.setLowAlarmValue(calibrationBean.getLowAlarmValue());
        aroundCalibrationBean.setHighWarnValue(calibrationBean.getHighWarnValue());
        aroundCalibrationBean.setHighAlarmValue(calibrationBean.getHighAlarmValue());
        aroundCalibrationBean.setLowAlarmRelayControl(calibrationBean.getLowAlarmRelayControl());
        aroundCalibrationBean.setLowAlarmRelayOutput(calibrationBean.getLowAlarmRelayOutput());
        aroundCalibrationBean.setHighAlarmRelayControl(calibrationBean.getHighAlarmRelayControl());
        aroundCalibrationBean.setHighAlarmRelayOutput(calibrationBean.getHighAlarmRelayOutput());
        DeviceManager.getInstance().aroundCalibration(aroundCalibrationBean);
    }

    //-----------------------------------------------------------//
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSensorRealTime(SensorRealtimeDataMessage msg){
        LogUtils.LogI(TAG,"onSensorRealTime: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onSensorRealTime: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                updateCurSensorValue(getSensorValue());
            }else {
                Toast.makeText(this,"获取传感器实时数据失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRealTimeData(RealTimeDataMessage msg){
        LogUtils.LogI("wch","onRealTimeData: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_REPORT
                || msg.getResult() == BaseMessage.RESULT_OK) {
            updateCurMeasureValue(getCurMeasureValue());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAroundCalibration(AroundCalibrationMessage msg){
        LogUtils.LogI("wch","onAroundCalibration: "+msg.getCmdType());
        LogUtils.LogI("wch","onAroundCalibration: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                DeviceManager.getInstance().getZtrsDevice().setAroundCalibrationBean(aroundCalibrationBean);
                initAroundCalibrationBean();
                updateCalibration();
                Toast.makeText(this,"回转标定成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"回转标定失败",Toast.LENGTH_LONG).show();
            }
        }else if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                initAroundCalibrationBean();
                updateCalibration();
            }else {
                Toast.makeText(this,"回转标定查询失败",Toast.LENGTH_LONG).show();
            }
        }
    }
}
