package com.ztrs.zgj.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.ActivityWeightCalibrationBinding;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.RealTimeDataBean;
import com.ztrs.zgj.device.bean.SensorRealtimeDataBean;
import com.ztrs.zgj.device.bean.TorqueCurveApplyBean;
import com.ztrs.zgj.device.bean.WeightCalibrationBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.RealTimeDataMessage;
import com.ztrs.zgj.device.eventbus.SensorRealtimeDataMessage;
import com.ztrs.zgj.device.eventbus.TorqueCurveMessage;
import com.ztrs.zgj.device.eventbus.WeightCalibrationMessage;
import com.ztrs.zgj.main.BaseEditAutoHideActivity;
import com.ztrs.zgj.setting.bean.TorqueCurveBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


public class WeightCalibrationActivity extends BaseEditAutoHideActivity {

    static String TAG = WeightCalibrationActivity.class.getSimpleName();

    WeightCalibrationBean calibrationBean;
    SensorRealtimeDataBean sensorRealtimeDataBean;
    RealTimeDataBean realTimeDataBean;
    TorqueCurveApplyBean torqueCurveApplyBean;

    ActivityWeightCalibrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeightCalibrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EventBus.getDefault().register(this);
        initData();
        initUi();
        querySensor();
        DeviceManager.getInstance().queryWeightCalibration();
        DeviceManager.getInstance().queryTorqueCurve();
    }

    @Override
    protected void onDestroy() {
        stopQuery();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected List<View> getExcludeTouchHideInputViews() {
        List<View> list = new ArrayList<>();
        list.add(binding.tvCode1Value);
        list.add(binding.tvCalibration1Value);
        list.add(binding.tvCode2Value);
        list.add(binding.tvCalibration2Value);
        list.add(binding.tvLowAlarmValue);
        list.add(binding.tvHighAlarmValue);
        return list;
    }

    void initData() {
        initCalibrationBean();
        sensorRealtimeDataBean = DeviceManager.getInstance().getZtrsDevice().getSensorRealtimeDataBean();
        realTimeDataBean = DeviceManager.getInstance().getZtrsDevice().getRealTimeDataBean();
        torqueCurveApplyBean = DeviceManager.getInstance().getZtrsDevice().getTorqueCurveApplyBean();
    }

    private void initCalibrationBean(){
        WeightCalibrationBean calibrationBean = DeviceManager.getInstance().getZtrsDevice().getWeightCalibrationBean();
        try {
            this.calibrationBean = (WeightCalibrationBean) calibrationBean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }


    private void initUi() {
        binding.rlTitle.tvTitle.setText("载重标定");
        initHighControl();
        initHighOutput();
        updateCurSensorValue(getSensorValue());
        updateCurMeasureValue(getCurMeasureValue());
        updateCalibration();
        binding.btnSave.setOnClickListener(v -> save());
        binding.rlTitle.tvBack.setOnClickListener(v -> finish());
        binding.tvCod1Current.setOnClickListener(v -> updateCode1Value(sensorRealtimeDataBean.getWeightSensor()));
        binding.tvCod2Current.setOnClickListener(v -> updateCode2Value(sensorRealtimeDataBean.getWeightSensor()));
    }

    long getSensorValue() {
        return  sensorRealtimeDataBean.getWeightSensor();
    }

    int getCurMeasureValue() {
        int height = realTimeDataBean.getUpWeight();
        return height;
    }



    void initHighControl() {
        String[] stringArray = getResources().getStringArray(R.array.control);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.settting_spinner_item, stringArray) {};//默认样式
        adapter.setDropDownViewResource(R.layout.settting_spinner_dropdown_item);//下拉样式
        binding.spinnerHighControl.setAdapter(adapter);
    }

    void initHighOutput() {
        String[] stringArray = getResources().getStringArray(R.array.output);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.settting_spinner_item, stringArray) {};//默认样式
        adapter.setDropDownViewResource(R.layout.settting_spinner_dropdown_item);//下拉样式
        binding.spinnerHighOutput.setAdapter(adapter);
    }


    void updateCalibration() {
        updateCode1Value(getCode1Value());
        updateCalibration1Value(getCalibration1Value());
        updateCode2Value(getCode2Value());
        updateCalibration2Value(getCalibration2Value());
        updateLowAlarmValue(getHighWarnValue());
        updateHighAlarmValue(getHighAlarmValue());
        updateHighControl(getHighControl());
        updateHighOutput(getHighOutput());
    }
    
    long getCode1Value() {
        if(calibrationBean != null){
            return calibrationBean.getCurrent1();
        }
        return 0;
    }
    
    int getCalibration1Value() {
        if(calibrationBean != null){
            return calibrationBean.getCalibration1();
        }
        return 0;
    }
    
    long getCode2Value() {
        if(calibrationBean != null){
            return calibrationBean.getCurrent2();
        }
        return 0;
    }
    
    int getCalibration2Value() {
        if(calibrationBean != null){
            return calibrationBean.getCalibration2();
        }
        return 0;
    }

    
    int getHighWarnValue() {
        if(calibrationBean != null){
            return calibrationBean.getHighWarnValue();
        }
        return 0;
    }

    
    int getHighAlarmValue() {
        if(calibrationBean != null){
            return calibrationBean.getHighAlarmValue();
        }
        return 0;
    }
    
    int getHighControl() {
        if(calibrationBean != null){
            return calibrationBean.getHighAlarmRelayControl();
        }
        return 0;
    }
    
    int getHighOutput() {
        int output = 0;
        if(calibrationBean != null){
            output = calibrationBean.getHighAlarmRelayOutput();
            if(output >=2){
                output = 0;
            }
        }
        return output;
    }

    void updateCurSensorValue(long value) {
        binding.tvCurCodedValue.setText("" + value);
    }

    void updateCurMeasureValue(int value) {
        binding.tvCurMeasureValue.setText(String.format("%.1f", 1.0 * value / 100));
    }

    void updateCode1Value(long value) {
        binding.tvCode1Value.setText("" + value);
    }

    void updateCalibration1Value(int value) {
        binding.tvCalibration1Value.setText(String.format("%.1f", 1.0 * value / 100));
    }

    void updateCode2Value(long value) {
        binding.tvCode2Value.setText("" + value);
    }

    void updateCalibration2Value(int value) {
        binding.tvCalibration2Value.setText(String.format("%.1f", 1.0 * value / 100));
    }


    void updateLowAlarmValue(int value) {
        int currentRatedLoad = 0;
        List<TorqueCurveBean> torqueCurveBeanList = torqueCurveApplyBean.getTorqueCurveBeanList();
        if(torqueCurveBeanList == null || torqueCurveBeanList.size()<1){
            currentRatedLoad = 0;
        }else {
            currentRatedLoad = torqueCurveBeanList.get(0).getWeight();
        }
        int percent;
        if(currentRatedLoad == 0){
            percent = 0;
        }else {
            percent = (int) (1.0 * value / currentRatedLoad*100);
        }
        binding.tvLowAlarmValue.setText(""+percent);
    }


    void updateHighAlarmValue(int value) {
        int currentRatedLoad = 0;
        List<TorqueCurveBean> torqueCurveBeanList = torqueCurveApplyBean.getTorqueCurveBeanList();
        if(torqueCurveBeanList == null || torqueCurveBeanList.size()<1){
            currentRatedLoad = 0;
        }else {
            currentRatedLoad = torqueCurveBeanList.get(0).getWeight();
        }
        int percent;
        if(currentRatedLoad == 0){
            percent = 0;
        }else {
            percent = (int) (1.0 * value / currentRatedLoad*100);
        }
        binding.tvHighAlarmValue.setText(""+percent);
    }


    void updateHighControl(int value){
        binding.spinnerHighControl.setSelection(value);
    }

    void updateHighOutput(int value){
        binding.spinnerHighOutput.setSelection(value);
    }

    private Disposable disposable;

    private void querySensor() {
        Observable.interval(100,Constants.SENSOR_QUERY_INTERVAL, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        DeviceManager.getInstance().querySensorRealtimeData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void stopQuery() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
    }


    private void save() {
        String reInput = getString(R.string.setting_re_input);

        String code1Str = binding.tvCode1Value.getText().toString();
        long code1 = 0;
        try {
            code1 = Long.valueOf(code1Str);
        } catch (NumberFormatException e) {
            binding.tvCode1Value.setHint(reInput);
            return;
        }

        String calibration1Str = binding.tvCalibration1Value.getText().toString();
        float calibration1 = 0;
        try {
            calibration1 = Float.valueOf(calibration1Str);
        } catch (NumberFormatException e) {
            binding.tvCalibration1Value.setHint(reInput);
            return;
        }

        String code2Str = binding.tvCode2Value.getText().toString();
        long code2 = 0;
        try {
            code2 = Long.valueOf(code2Str);
        } catch (NumberFormatException e) {
            binding.tvCode2Value.setHint(reInput);
            return;
        }

        String calibration2Str = binding.tvCalibration2Value.getText().toString();
        float calibration2 = 0;
        try {
            calibration2 = Float.valueOf(calibration2Str);
        } catch (NumberFormatException e) {
            binding.tvCalibration2Value.setHint(reInput);
            return;
        }


        String lowAlarmStr = binding.tvLowAlarmValue.getText().toString();
        int lowAlarm = 0;
        try {
            lowAlarm = Integer.valueOf(lowAlarmStr);
        } catch (NumberFormatException e) {
            binding.tvLowAlarmValue.setHint(reInput);
            return;
        }

        String highAlarmStr = binding.tvHighAlarmValue.getText().toString();
        int highAlarm = 0;
        try {
            highAlarm = Integer.valueOf(highAlarmStr);
        } catch (NumberFormatException e) {
            binding.tvHighAlarmValue.setHint(reInput);
            return;
        }
//        if(calibrationBean == null){
//            Toast.makeText(this,"未初始化，高度标定失败",Toast.LENGTH_LONG).show();
//            return;
//        }
        List<TorqueCurveBean> torqueCurveBeanList = torqueCurveApplyBean.getTorqueCurveBeanList();
        if(torqueCurveBeanList == null || torqueCurveBeanList.size()<1){
            Toast.makeText(this,"力矩曲线未获取，高度标定失败",Toast.LENGTH_LONG).show();
            return;
        }

        byte highControl = (byte) binding.spinnerHighControl.getSelectedItemPosition();
        byte highOutput = (byte) binding.spinnerHighOutput.getSelectedItemPosition();
        if(highControl != 0 && highOutput == 2){
            Toast.makeText(this,"高报警动作不能为NC",Toast.LENGTH_LONG).show();
            return;
        }

        calibrationBean = new WeightCalibrationBean();
        calibrationBean.setCurrent1(code1);
        calibrationBean.setCalibration1((int) (calibration1 * 100));
        calibrationBean.setCurrent2(code2);
        calibrationBean.setCalibration2((int) (calibration2 * 100));
        int currentRatedLoad = 0;
        currentRatedLoad = torqueCurveBeanList.get(0).getWeight();
        int highWarnSend = (int)(1.0*currentRatedLoad*lowAlarm/100);
        calibrationBean.setHighWarnValue(highWarnSend);
        int highAlarmSend = (int)(1.0*currentRatedLoad*highAlarm/100);
        calibrationBean.setHighAlarmValue(highAlarmSend);
        calibrationBean.setHighAlarmRelayControl(highControl);
        calibrationBean.setHighAlarmRelayOutput(highOutput);
        DeviceManager.getInstance().weightCalibration(calibrationBean);
    }

    //-----------------------------------------------------------//
    int i= 0;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSensorRealTime(SensorRealtimeDataMessage msg){
        LogUtils.LogI(TAG,"onSensorRealTime: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onSensorRealTime: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                updateCurSensorValue(getSensorValue());
                i= 0;
            }else {
                if(i%4== 0) {
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
    public void onWeightCalibration(WeightCalibrationMessage msg){
        LogUtils.LogI("wch","onWeightCalibration: "+msg.getCmdType());
        LogUtils.LogI("wch","onWeightCalibration: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                DeviceManager.getInstance().getZtrsDevice().setWeightCalibrationBean(calibrationBean);
                initCalibrationBean();
                updateCalibration();
                Toast.makeText(this,"载重标定成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"载重标定失败",Toast.LENGTH_LONG).show();
            }
        }else if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                initCalibrationBean();
                updateCalibration();
            }else {
                Toast.makeText(this,"载重标定查询失败",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTorqueCurve(TorqueCurveMessage msg){
        LogUtils.LogI(TAG,"onTorqueCurve: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onTorqueCurve: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
//                Toast.makeText(this,"力矩曲线保存成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"力矩曲线获取失败",Toast.LENGTH_LONG).show();
            }
        }
    }
}