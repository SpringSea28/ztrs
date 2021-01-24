package com.ztrs.zgj.setting;

import android.os.Bundle;
import android.widget.Toast;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.bean.CalibrationBean;
import com.ztrs.zgj.device.bean.HeightCalibrationBean;
import com.ztrs.zgj.device.bean.RealTimeDataBean;
import com.ztrs.zgj.device.bean.SensorRealtimeDataBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.HeightCalibrationMessage;
import com.ztrs.zgj.device.eventbus.RealTimeDataMessage;
import com.ztrs.zgj.device.eventbus.SensorRealtimeDataMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HeightSensorCalibrationActivity extends BaseSensorCalibrationActivity {

    HeightCalibrationBean heightCalibrationBean;
    SensorRealtimeDataBean sensorRealtimeDataBean;
    RealTimeDataBean realTimeDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TAG = HeightSensorCalibrationActivity.class.getSimpleName();
        super.onCreate(savedInstanceState);
        DeviceManager.getInstance().queryHeightCalibration();
    }

    @Override
    void initData() {
        initHeightCalibrationBean();
        sensorRealtimeDataBean = DeviceManager.getInstance().getZtrsDevice().getSensorRealtimeDataBean();
        realTimeDataBean = DeviceManager.getInstance().getZtrsDevice().getRealTimeDataBean();
    }

    private void initHeightCalibrationBean(){
        HeightCalibrationBean heightCalibrationBean = DeviceManager.getInstance().getZtrsDevice().getHeightCalibrationBean();
        try {
            this.heightCalibrationBean = (HeightCalibrationBean) heightCalibrationBean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }


    @Override
    String getTitleText() {
        return "高度标定";
    }

    @Override
    String getUnit() {
        return "米";
    }

    @Override
    long getSensorValue() {
        return  sensorRealtimeDataBean.getHeightSensor();
    }

    @Override
    int getCurMeasureValue() {
        int height = realTimeDataBean.getHeight();
        return height;
    }

    @Override
    long getCode1Value() {
        if(heightCalibrationBean != null){
            return heightCalibrationBean.getCurrent1();
        }
        return 0;
    }

    @Override
    int getCalibration1Value() {
        if(heightCalibrationBean != null){
            return heightCalibrationBean.getCalibration1();
        }
        return 0;
    }

    @Override
    long getCode2Value() {
        if(heightCalibrationBean != null){
            return heightCalibrationBean.getCurrent2();
        }
        return 0;
    }

    @Override
    int getCalibration2Value() {
        if(heightCalibrationBean != null){
            return heightCalibrationBean.getCalibration2();
        }
        return 0;
    }

    @Override
    int getLowWarnValue() {
        if(heightCalibrationBean != null){
            return heightCalibrationBean.getLowWarnValue();
        }
        return 0;
    }

    @Override
    int getLowAlarmValue() {
        if(heightCalibrationBean != null){
            return heightCalibrationBean.getLowAlarmValue();
        }
        return 0;
    }

    @Override
    int getHighWarnValue() {
        if(heightCalibrationBean != null){
            return heightCalibrationBean.getHighWarnValue();
        }
        return 0;
    }

    @Override
    int getHighAlarmValue() {
        if(heightCalibrationBean != null){
            return heightCalibrationBean.getHighAlarmValue();
        }
        return 0;
    }

    @Override
    int getLowControl() {
        if(heightCalibrationBean != null){
            return heightCalibrationBean.getLowAlarmRelayControl();
        }
        return 0;
    }

    @Override
    int getLowOutput() {
        if(heightCalibrationBean != null){
            return heightCalibrationBean.getLowAlarmRelayOutput();
        }
        return 0;
    }

    @Override
    int getHighControl() {
        if(heightCalibrationBean != null){
            return heightCalibrationBean.getHighAlarmRelayControl();
        }
        return 0;
    }

    @Override
    int getHighOutput() {
        if(heightCalibrationBean != null){
            return heightCalibrationBean.getHighAlarmRelayOutput();
        }
        return 0;
    }

    @Override
    void saveCalibration(CalibrationBean calibrationBean) {
        heightCalibrationBean.setCurrent1(calibrationBean.getCurrent1());
        heightCalibrationBean.setCalibration1(calibrationBean.getCalibration1());
        heightCalibrationBean.setCurrent2(calibrationBean.getCurrent2());
        heightCalibrationBean.setCalibration2(calibrationBean.getCalibration2());
        heightCalibrationBean.setLowWarnValue(calibrationBean.getLowWarnValue());
        heightCalibrationBean.setLowAlarmValue(calibrationBean.getLowAlarmValue());
        heightCalibrationBean.setHighWarnValue(calibrationBean.getHighWarnValue());
        heightCalibrationBean.setHighAlarmValue(calibrationBean.getHighAlarmValue());
        heightCalibrationBean.setLowAlarmRelayControl(calibrationBean.getLowAlarmRelayControl());
        heightCalibrationBean.setLowAlarmRelayOutput(calibrationBean.getLowAlarmRelayOutput());
        heightCalibrationBean.setHighAlarmRelayControl(calibrationBean.getHighAlarmRelayControl());
        heightCalibrationBean.setHighAlarmRelayOutput(calibrationBean.getHighAlarmRelayOutput());
        DeviceManager.getInstance().heightCalibration(heightCalibrationBean);
    }

    //-----------------------------------------------------------//
    int i=0;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSensorRealTime(SensorRealtimeDataMessage msg){
        LogUtils.LogI(TAG,"onSensorRealTime: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onSensorRealTime: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                updateCurSensorValue(getSensorValue());
                i= 0;
            }else {
                if (i % 4 == 0) {
                    Toast.makeText(this, "获取传感器实时数据失败", Toast.LENGTH_SHORT).show();
                }
                i++;
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
    public void onHeightCalibration(HeightCalibrationMessage msg){
        LogUtils.LogI("wch","onHeightCalibration: "+msg.getCmdType());
        LogUtils.LogI("wch","onHeightCalibration: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                DeviceManager.getInstance().getZtrsDevice().setHeightCalibrationBean(heightCalibrationBean);
                initHeightCalibrationBean();
                updateCalibration();
                Toast.makeText(this,"高度标定成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"高度标定失败",Toast.LENGTH_LONG).show();
            }
        }else if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                initHeightCalibrationBean();
                updateCalibration();
            }else {
                Toast.makeText(this,"高度标定查询失败",Toast.LENGTH_LONG).show();
            }
        }
    }
}
