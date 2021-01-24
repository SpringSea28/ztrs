package com.ztrs.zgj.setting;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.ActivityBaseSensorCalibrationBinding;
import com.ztrs.zgj.databinding.ActivitySlopeCalibrationBinding;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.CalibrationBean;
import com.ztrs.zgj.device.bean.RealTimeDataBean;
import com.ztrs.zgj.device.bean.SensorRealtimeDataBean;
import com.ztrs.zgj.device.bean.StaticParameterBean;
import com.ztrs.zgj.device.bean.WeightCalibrationBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.StaticParameterMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


public class SlopeCalibrationActivity extends AppCompatActivity {

    static String TAG = SlopeCalibrationActivity.class.getSimpleName();


    StaticParameterBean calibrationBean;
    SensorRealtimeDataBean sensorRealtimeDataBean;
    RealTimeDataBean realTimeDataBean;

    StaticParameterBean saveCalibrationBean;
    ActivitySlopeCalibrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySlopeCalibrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EventBus.getDefault().register(this);
        initData();
        initUi();
        querySensor();
    }

    @Override
    protected void onDestroy() {
        stopQuery();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    void initData() {
        calibrationBean = DeviceManager.getInstance().getZtrsDevice().getStaticParameterBean();
        sensorRealtimeDataBean = DeviceManager.getInstance().getZtrsDevice().getSensorRealtimeDataBean();
        realTimeDataBean = DeviceManager.getInstance().getZtrsDevice().getRealTimeDataBean();
    }
    


    private void initUi() {
        binding.rlTitle.tvTitle.setText("倾角标定");
        updateCurSensorValue(getSensorValue());
        updateCurMeasureValue(getCurMeasureValue());
        updateCurSensorValueY(getSensorValueY());
        updateCurMeasureValueY(getCurMeasureValueY());
        updateCalibration();
        binding.btnSave.setOnClickListener(v -> save());
        binding.rlTitle.tvBack.setOnClickListener(v -> finish());
    }
    

    void updateCalibration() {
        updateHighWarnValue(getHighWarnValue());
        updateHighAlarmValue(getHighAlarmValue());
        updateHighWarnValueY(getHighWarnValueY());
        updateHighAlarmValueY(getHighAlarmValueY());
    }

    void updateCurSensorValue(long value) {
        binding.tvCurCodedValue.setText("" + value);
    }

    void updateCurMeasureValue(int value) {
        binding.tvCurMeasureValue.setText(String.format("%.1f", 1.0 * value / 10));
    }

    long getSensorValue() {
        return  sensorRealtimeDataBean.getSlopeSensorX();
    }

    int getCurMeasureValue() {
        int height = realTimeDataBean.getxSlope();
        return height;
    }


    void updateHighWarnValue(int value) {
        binding.tvHighWarnValue.setText(String.format("%.1f", 1.0 * value / 10));
    }

    void updateHighAlarmValue(int value) {
        binding.tvHighAlarmValue.setText(String.format("%.1f", 1.0 * value / 10));
    }


    int getHighWarnValue() {
        if(calibrationBean != null){
            return calibrationBean.getSlopeXWarningValue();
        }
        return 0;
    }

    int getHighAlarmValue() {
        if(calibrationBean != null){
            return calibrationBean.getSlopeXAlarmValue();
        }
        return 0;
    }


    void updateCurSensorValueY(long value) {
        binding.tvCurCodedValueY.setText("" + value);
    }

    void updateCurMeasureValueY(int value) {
        binding.tvCurMeasureValueY.setText(String.format("%.1f", 1.0 * value / 10));
    }

    long getSensorValueY() {
        return  sensorRealtimeDataBean.getSlopeSensorY();
    }

    int getCurMeasureValueY() {
        int height = realTimeDataBean.getySlope();
        return height;
    }


    void updateHighWarnValueY(int value) {
        binding.tvHighWarnValueY.setText(String.format("%.1f", 1.0 * value / 10));
    }

    void updateHighAlarmValueY(int value) {
        binding.tvHighAlarmValueY.setText(String.format("%.1f", 1.0 * value / 10));
    }


    int getHighWarnValueY() {
        if(calibrationBean != null){
            return calibrationBean.getSlopeYWarningValue();
        }
        return 0;
    }

    int getHighAlarmValueY() {
        if(calibrationBean != null){
            return calibrationBean.getSlopeYAlarmValue();
        }
        return 0;
    }

    private Disposable disposable;

    private void querySensor() {
        Observable.interval(Constants.SENSOR_QUERY_INTERVAL, TimeUnit.MILLISECONDS)
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
        DeviceManager.getInstance().querySensorRealtimeData();
    }

    private void stopQuery() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
    }


    private void save() {
        String reInput = getString(R.string.setting_re_input);

        String highStr = binding.tvHighWarnValue.getText().toString();
        float highWarn = 0;
        try {
            highWarn = Float.valueOf(highStr);
        } catch (NumberFormatException e) {
            binding.tvHighWarnValue.setHint(reInput);
            return;
        }

        String highAlarmStr = binding.tvHighAlarmValue.getText().toString();
        float highAlarm = 0;
        try {
            highAlarm = Float.valueOf(highAlarmStr);
        } catch (NumberFormatException e) {
            binding.tvHighAlarmValue.setHint(reInput);
            return;
        }

        String highStrY = binding.tvHighWarnValueY.getText().toString();
        float highWarnY = 0;
        try {
            highWarnY = Float.valueOf(highStrY);
        } catch (NumberFormatException e) {
            binding.tvHighWarnValueY.setHint(reInput);
            return;
        }

        String highAlarmStrY = binding.tvHighAlarmValueY.getText().toString();
        float highAlarmY = 0;
        try {
            highAlarmY = Float.valueOf(highAlarmStrY);
        } catch (NumberFormatException e) {
            binding.tvHighAlarmValueY.setHint(reInput);
            return;
        }

        StaticParameterBean staticParameterBean = DeviceManager.getInstance().getZtrsDevice().getStaticParameterBean();
        try {
            saveCalibrationBean = staticParameterBean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        saveCalibrationBean.setSlopeXWarningValue((int) (highWarn * 10));
        saveCalibrationBean.setSlopeXAlarmValue((int) (highAlarm * 10));
        saveCalibrationBean.setSlopeYWarningValue((int) (highWarnY * 10));
        saveCalibrationBean.setSlopeYAlarmValue((int) (highAlarmY * 10));
        DeviceManager.getInstance().setStaticParameter(saveCalibrationBean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSlopeCalibration(StaticParameterMessage msg){
        LogUtils.LogI("wch","onSlopeCalibration: "+msg.getCmdType());
        LogUtils.LogI("wch","onSlopeCalibration: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                DeviceManager.getInstance().getZtrsDevice().setStaticParameterBean(saveCalibrationBean);
                initData();
                updateCalibration();
                Toast.makeText(this,"倾角标定成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"倾角标定失败",Toast.LENGTH_LONG).show();
            }
        }else if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                updateCalibration();
            }else {
//                Toast.makeText(this,"倾角标定查询失败",Toast.LENGTH_LONG).show();
            }
        }
    }

}