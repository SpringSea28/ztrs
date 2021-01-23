package com.ztrs.zgj.setting;

import android.os.Bundle;
import android.widget.Toast;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.bean.AmplitudeCalibrationBean;
import com.ztrs.zgj.device.bean.CalibrationBean;
import com.ztrs.zgj.device.bean.RealTimeDataBean;
import com.ztrs.zgj.device.bean.SensorRealtimeDataBean;
import com.ztrs.zgj.device.eventbus.AmplitudeCalibrationMessage;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.RealTimeDataMessage;
import com.ztrs.zgj.device.eventbus.SensorRealtimeDataMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AmplitudeSensorCalibrationActivity extends BaseSensorCalibrationActivity {

    AmplitudeCalibrationBean amplitudeCalibrationBean;
    SensorRealtimeDataBean sensorRealtimeDataBean;
    RealTimeDataBean realTimeDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TAG = AmplitudeSensorCalibrationActivity.class.getSimpleName();
        super.onCreate(savedInstanceState);
        DeviceManager.getInstance().queryAmplitudeCalibration();
    }

    @Override
    void initData() {
        initAmplitudeCalibrationBean();
        sensorRealtimeDataBean = DeviceManager.getInstance().getZtrsDevice().getSensorRealtimeDataBean();
        realTimeDataBean = DeviceManager.getInstance().getZtrsDevice().getRealTimeDataBean();
    }

    private void initAmplitudeCalibrationBean(){
        AmplitudeCalibrationBean amplitudeCalibrationBean
                = DeviceManager.getInstance().getZtrsDevice().getAmplitudeCalibrationBean();
        try {
            this.amplitudeCalibrationBean = (AmplitudeCalibrationBean) amplitudeCalibrationBean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }


    @Override
    String getTitleText() {
        return "幅度标定";
    }

    @Override
    String getUnit() {
        return "米";
    }

    @Override
    long getSensorValue() {
        return  sensorRealtimeDataBean.getAmplitudeSensor();
    }

    @Override
    int getCurMeasureValue() {
        int height = realTimeDataBean.getAmplitude();
        return height;
    }

    @Override
    long getCode1Value() {
        if(amplitudeCalibrationBean != null){
            return amplitudeCalibrationBean.getCurrent1();
        }
        return 0;
    }

    @Override
    int getCalibration1Value() {
        if(amplitudeCalibrationBean != null){
            return amplitudeCalibrationBean.getCalibration1();
        }
        return 0;
    }

    @Override
    long getCode2Value() {
        if(amplitudeCalibrationBean != null){
            return amplitudeCalibrationBean.getCurrent2();
        }
        return 0;
    }

    @Override
    int getCalibration2Value() {
        if(amplitudeCalibrationBean != null){
            return amplitudeCalibrationBean.getCalibration2();
        }
        return 0;
    }

    @Override
    int getLowWarnValue() {
        if(amplitudeCalibrationBean != null){
            return amplitudeCalibrationBean.getLowWarnValue();
        }
        return 0;
    }

    @Override
    int getLowAlarmValue() {
        if(amplitudeCalibrationBean != null){
            return amplitudeCalibrationBean.getLowAlarmValue();
        }
        return 0;
    }

    @Override
    int getHighWarnValue() {
        if(amplitudeCalibrationBean != null){
            return amplitudeCalibrationBean.getHighWarnValue();
        }
        return 0;
    }

    @Override
    int getHighAlarmValue() {
        if(amplitudeCalibrationBean != null){
            return amplitudeCalibrationBean.getHighAlarmValue();
        }
        return 0;
    }

    @Override
    int getLowControl() {
        if(amplitudeCalibrationBean != null){
            return amplitudeCalibrationBean.getLowAlarmRelayControl();
        }
        return 0;
    }

    @Override
    int getLowOutput() {
        if(amplitudeCalibrationBean != null){
            return amplitudeCalibrationBean.getLowAlarmRelayOutput();
        }
        return 0;
    }

    @Override
    int getHighControl() {
        if(amplitudeCalibrationBean != null){
            return amplitudeCalibrationBean.getHighAlarmRelayControl();
        }
        return 0;
    }

    @Override
    int getHighOutput() {
        if(amplitudeCalibrationBean != null){
            return amplitudeCalibrationBean.getHighAlarmRelayOutput();
        }
        return 0;
    }

    @Override
    void saveCalibration(CalibrationBean calibrationBean) {
        amplitudeCalibrationBean.setCurrent1(calibrationBean.getCurrent1());
        amplitudeCalibrationBean.setCalibration1(calibrationBean.getCalibration1());
        amplitudeCalibrationBean.setCurrent2(calibrationBean.getCurrent2());
        amplitudeCalibrationBean.setCalibration2(calibrationBean.getCalibration2());
        amplitudeCalibrationBean.setLowWarnValue(calibrationBean.getLowWarnValue());
        amplitudeCalibrationBean.setLowAlarmValue(calibrationBean.getLowAlarmValue());
        amplitudeCalibrationBean.setHighWarnValue(calibrationBean.getHighWarnValue());
        amplitudeCalibrationBean.setHighAlarmValue(calibrationBean.getHighAlarmValue());
        amplitudeCalibrationBean.setLowAlarmRelayControl(calibrationBean.getLowAlarmRelayControl());
        amplitudeCalibrationBean.setLowAlarmRelayOutput(calibrationBean.getLowAlarmRelayOutput());
        amplitudeCalibrationBean.setHighAlarmRelayControl(calibrationBean.getHighAlarmRelayControl());
        amplitudeCalibrationBean.setHighAlarmRelayOutput(calibrationBean.getHighAlarmRelayOutput());
        DeviceManager.getInstance().amplitudeCalibration(amplitudeCalibrationBean);
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
                Toast.makeText(this,"获取传感器实时数据失败",Toast.LENGTH_LONG).show();
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
    public void onAmplitudeCalibration(AmplitudeCalibrationMessage msg){
        LogUtils.LogI("wch","onAmplitudeCalibration: "+msg.getCmdType());
        LogUtils.LogI("wch","onAmplitudeCalibration: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                DeviceManager.getInstance().getZtrsDevice().setAmplitudeCalibrationBean(amplitudeCalibrationBean);
                initAmplitudeCalibrationBean();
                updateCalibration();
            }else {
                Toast.makeText(this,"幅度标定失败",Toast.LENGTH_LONG).show();
            }
        }else if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                initAmplitudeCalibrationBean();
                updateCalibration();
            }else {
//                Toast.makeText(this,"幅度标定查询失败",Toast.LENGTH_LONG).show();
            }
        }
    }
}
