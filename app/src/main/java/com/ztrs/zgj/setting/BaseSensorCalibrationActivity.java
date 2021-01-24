package com.ztrs.zgj.setting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.ActivityBaseSensorCalibrationBinding;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.CalibrationBean;
import com.ztrs.zgj.main.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


public abstract class BaseSensorCalibrationActivity extends BaseActivity {

    static String TAG;

    abstract void initData();

    abstract String getTitleText();

    abstract String getUnit();

    abstract long getSensorValue();

    abstract int getCurMeasureValue();

    abstract long getCode1Value();

    abstract int getCalibration1Value();

    abstract long getCode2Value();

    abstract int getCalibration2Value();

    abstract int getLowWarnValue();

    abstract int getLowAlarmValue();

    abstract int getHighWarnValue();

    abstract int getHighAlarmValue();

    abstract int getLowControl();
    abstract int getLowOutput();
    abstract int getHighControl();
    abstract int getHighOutput();

    abstract void saveCalibration(CalibrationBean calibrationBean);

    ActivityBaseSensorCalibrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBaseSensorCalibrationBinding.inflate(getLayoutInflater());
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


    private void initUi() {
        binding.rlTitle.tvTitle.setText(getTitleText());
        initUnit();
        initLowControl();
        initLowOutput();
        initHighControl();
        initHighOutput();
        updateCurSensorValue(getSensorValue());
        updateCurMeasureValue(getCurMeasureValue());
        updateCalibration();
        binding.btnSave.setOnClickListener(v -> save());
        binding.rlTitle.tvBack.setOnClickListener(v -> finish());
        binding.tvCode1Current.setOnClickListener(v -> updateCode1Value(getSensorValue()));
        binding.tvCode2Current.setOnClickListener(v -> updateCode2Value(getSensorValue()));
    }

    void initLowControl() {
        String[] stringArray = getResources().getStringArray(R.array.control);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.settting_spinner_item, stringArray) {
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }
        };//默认样式
        adapter.setDropDownViewResource(R.layout.settting_spinner_dropdown_item);//下拉样式
        binding.spinnerLowControl.setAdapter(adapter);
        binding.spinnerLowControl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void initLowOutput() {
        String[] stringArray = getResources().getStringArray(R.array.output);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.settting_spinner_item, stringArray) {};//默认样式
        adapter.setDropDownViewResource(R.layout.settting_spinner_dropdown_item);//下拉样式
        binding.spinnerLowOutput.setAdapter(adapter);
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

    void initUnit() {
        binding.tvCurMeasureValueUnit.setText(getUnit());
        binding.tvCalibration1ValueUnit.setText(getUnit());
        binding.tvCalibration2ValueUnit.setText(getUnit());
        binding.tvLowWarnValueUnit.setText(getUnit());
        binding.tvLowAlarmValueUnit.setText(getUnit());
        binding.tvHighWarnValueUnit.setText(getUnit());
        binding.tvHighAlarmValueUnit.setText(getUnit());
    }

    void updateCalibration() {
        updateCode1Value(getCode1Value());
        updateCalibration1Value(getCalibration1Value());
        updateCode2Value(getCode2Value());
        updateCalibration2Value(getCalibration2Value());
        updateLowWarnValue(getLowWarnValue());
        updateLowAlarmValue(getLowAlarmValue());
        updateHighWarnValue(getHighWarnValue());
        updateHighAlarmValue(getHighAlarmValue());
        updateLowControl(getLowControl());
        updateLowOutput(getLowOutput());
        updateHighControl(getHighControl());
        updateHighOutput(getHighOutput());
    }

    void updateCurSensorValue(long value) {
        binding.tvCurCodedValue.setText("" + value);
    }

    void updateCurMeasureValue(int value) {
        binding.tvCurMeasureValue.setText(String.format("%.1f", 1.0 * value / 10));
    }

    void updateCode1Value(long value) {
        binding.tvCode1Value.setText("" + value);
    }

    void updateCalibration1Value(int value) {
        binding.tvCalibration1Value.setText(String.format("%.1f", 1.0 * value / 10));
    }

    void updateCode2Value(long value) {
        binding.tvCode2Value.setText("" + value);
    }

    void updateCalibration2Value(int value) {
        binding.tvCalibration2Value.setText(String.format("%.1f", 1.0 * value / 10));
    }

    void updateLowWarnValue(int value) {
        binding.tvLowWarnValue.setText(String.format("%.1f", 1.0 * value / 10));
    }

    void updateLowAlarmValue(int value) {
        binding.tvLowAlarmValue.setText(String.format("%.1f", 1.0 * value / 10));
    }

    void updateHighWarnValue(int value) {
        binding.tvHighWarnValue.setText(String.format("%.1f", 1.0 * value / 10));
    }

    void updateHighAlarmValue(int value) {
        binding.tvHighAlarmValue.setText(String.format("%.1f", 1.0 * value / 10));
    }

    void updateLowControl(int value){
        binding.spinnerLowControl.setSelection(value);
    }

    void updateLowOutput(int value){
        binding.spinnerLowOutput.setSelection(value);
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
//        DeviceManager.getInstance().querySensorRealtimeData();
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

        String lowWarnStr = binding.tvLowWarnValue.getText().toString();
        float lowWarn = 0;
        try {
            lowWarn = Float.valueOf(lowWarnStr);
        } catch (NumberFormatException e) {
            binding.tvLowWarnValue.setHint(reInput);
            return;
        }

        String lowAlarmStr = binding.tvLowAlarmValue.getText().toString();
        float lowAlarm = 0;
        try {
            lowAlarm = Float.valueOf(lowAlarmStr);
        } catch (NumberFormatException e) {
            binding.tvLowAlarmValue.setHint(reInput);
            return;
        }

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

        byte lowControl = (byte) binding.spinnerLowControl.getSelectedItemPosition();
        byte lowOutput = (byte) binding.spinnerLowOutput.getSelectedItemPosition();
        if(lowControl != 0 && lowOutput == 2){
            Toast.makeText(this,"低报警动作不能为NC",Toast.LENGTH_LONG).show();
            return;
        }

        byte highControl = (byte) binding.spinnerHighControl.getSelectedItemPosition();
        byte highOutput = (byte) binding.spinnerHighOutput.getSelectedItemPosition();
        if(highControl != 0 && highOutput == 2){
            Toast.makeText(this,"高报警动作不能为NC",Toast.LENGTH_LONG).show();
            return;
        }

        CalibrationBean calibrationBean = new CalibrationBean();
        calibrationBean.setCurrent1(code1);
        calibrationBean.setCalibration1((int) (calibration1 * 10));
        calibrationBean.setCurrent2(code2);
        calibrationBean.setCalibration2((int) (calibration2 * 10));
        calibrationBean.setLowWarnValue((int) (lowWarn * 10));
        calibrationBean.setLowAlarmValue((int) (lowAlarm * 10));
        calibrationBean.setHighWarnValue((int) (highWarn * 10));
        calibrationBean.setHighAlarmValue((int) (highAlarm * 10));
        calibrationBean.setLowAlarmRelayControl((byte) binding.spinnerLowControl.getSelectedItemPosition());
        calibrationBean.setLowAlarmRelayOutput((byte) binding.spinnerLowOutput.getSelectedItemPosition());
        calibrationBean.setHighAlarmRelayControl((byte) binding.spinnerHighControl.getSelectedItemPosition());
        calibrationBean.setHighAlarmRelayOutput((byte) binding.spinnerHighOutput.getSelectedItemPosition());
        saveCalibration(calibrationBean);
    }

}