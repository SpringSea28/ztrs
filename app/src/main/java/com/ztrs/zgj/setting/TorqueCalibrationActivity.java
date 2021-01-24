package com.ztrs.zgj.setting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.ActivityTorqueCalibrationBinding;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.bean.CalibrationBean;
import com.ztrs.zgj.device.bean.HeightCalibrationBean;
import com.ztrs.zgj.device.bean.TorqueCalibrationBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.TorqueCalibrationMessage;
import com.ztrs.zgj.main.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class TorqueCalibrationActivity extends BaseActivity {

    TorqueCalibrationBean calibrationBean;
    ActivityTorqueCalibrationBinding binding;
    TorqueCalibrationBean saveCalibration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTorqueCalibrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EventBus.getDefault().register(this);
        initData();
        initUi();
        DeviceManager.getInstance().queryTorqueCalibration();
    }

    @Override
    protected List<View> getExcludeTouchHideInputViews() {
        List<View> list = new ArrayList<>();
        list.add(binding.tvLowAlarmValue);
        list.add(binding.tvHighAlarmValue);
        return list;
    }

    private void initData(){
        TorqueCalibrationBean calibrationBean = DeviceManager.getInstance().getZtrsDevice().getTorqueCalibrationBean();
        this.calibrationBean = calibrationBean;
        binding.setCalibration(this.calibrationBean);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initUi() {
        binding.tvTitle.setText("力矩设置");
        initLowControl();
        initLowOutput();
        initHighControl();
        initHighOutput();
        updateCalibration();
        binding.btnSave.setOnClickListener(v -> save());
        binding.tvBack.setOnClickListener(v -> finish());
    }

    void initLowControl() {
        String[] stringArray = getResources().getStringArray(R.array.control);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.settting_spinner_item, stringArray);//默认样式
        adapter.setDropDownViewResource(R.layout.settting_spinner_dropdown_item);//下拉样式
        binding.spinnerLowControl.setAdapter(adapter);
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

    void updateCalibration() {
        binding.spinnerLowControl.setSelection(calibrationBean.getTorqueWarnRelayControl());
        binding.spinnerLowOutput.setSelection(calibrationBean.getTorqueWarnRelayOutput());
        binding.spinnerHighControl.setSelection(calibrationBean.getTorqueAlarmRelayControl());
        binding.spinnerHighOutput.setSelection(calibrationBean.getTorqueAlarmRelayOutput());
    }

    private void save() {
        String reInput = getString(R.string.setting_re_input);


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

        saveCalibration = new TorqueCalibrationBean();
        saveCalibration.setRatedLiftingWarnValue(lowAlarm);
        saveCalibration.setRatedLiftingAlarmValue(highAlarm );
        saveCalibration.setTorqueWarnRelayControl((byte) binding.spinnerLowControl.getSelectedItemPosition());
        saveCalibration.setTorqueWarnRelayOutput((byte) binding.spinnerLowOutput.getSelectedItemPosition());
        saveCalibration.setTorqueAlarmRelayControl((byte) binding.spinnerHighControl.getSelectedItemPosition());
        saveCalibration.setTorqueAlarmRelayOutput((byte) binding.spinnerHighOutput.getSelectedItemPosition());
        DeviceManager.getInstance().torqueCalibration(saveCalibration);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTorqueCalibration(TorqueCalibrationMessage msg){
        LogUtils.LogI("wch","onTorqueCalibration: "+msg.getCmdType());
        LogUtils.LogI("wch","onTorqueCalibration: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                DeviceManager.getInstance().getZtrsDevice().setTorqueCalibrationBean(saveCalibration);
                initData();
                updateCalibration();
                Toast.makeText(this,"力矩标定成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"力矩标定失败",Toast.LENGTH_LONG).show();
            }
        }else if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                updateCalibration();
            }else {
                Toast.makeText(this,"力矩标定查询失败",Toast.LENGTH_LONG).show();
            }
        }
    }

}