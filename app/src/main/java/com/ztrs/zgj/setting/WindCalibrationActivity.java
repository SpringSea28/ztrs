package com.ztrs.zgj.setting;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.ActivityTorqueCalibrationBinding;
import com.ztrs.zgj.databinding.ActivityWindCalibrationBinding;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.bean.StaticParameterBean;
import com.ztrs.zgj.device.bean.TorqueCalibrationBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.StaticParameterMessage;
import com.ztrs.zgj.device.eventbus.TorqueCalibrationMessage;
import com.ztrs.zgj.main.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WindCalibrationActivity extends BaseActivity {

    StaticParameterBean calibrationBean;
    ActivityWindCalibrationBinding binding;
    StaticParameterBean saveCalibration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWindCalibrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EventBus.getDefault().register(this);
        initData();
        initUi();
        DeviceManager.getInstance().queryStaticParameter();
    }

    private void initData(){
        StaticParameterBean calibrationBean = DeviceManager.getInstance().getZtrsDevice().getStaticParameterBean();
        this.calibrationBean = calibrationBean;
        binding.setCalibration(this.calibrationBean);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initUi() {
        binding.tvTitle.setText("风力设置");
        initLowControl();
        initHighControl();
        updateCalibration();
        binding.btnSave.setOnClickListener(v -> save());
        binding.tvBack.setOnClickListener(v -> finish());
    }

    void initLowControl() {
        String[] stringArray = getResources().getStringArray(R.array.wind);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.settting_spinner_item, stringArray);//默认样式
        adapter.setDropDownViewResource(R.layout.settting_spinner_dropdown_item);//下拉样式
        binding.spinnerLowControl.setAdapter(adapter);
    }


    void initHighControl() {
        String[] stringArray = getResources().getStringArray(R.array.wind);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.settting_spinner_item, stringArray) {};//默认样式
        adapter.setDropDownViewResource(R.layout.settting_spinner_dropdown_item);//下拉样式
        binding.spinnerHighControl.setAdapter(adapter);
    }

    void updateCalibration() {
        int warnLevel = calibrationBean.getWindSpeedWarningValue();
        binding.spinnerLowControl.setSelection(warnLevel > 12?12:warnLevel);
        int alarmLevel = calibrationBean.getWindSpeedAlarmValue();
        binding.spinnerHighControl.setSelection(alarmLevel> 12?12:alarmLevel);
    }

    private void save() {
        StaticParameterBean staticParameterBean = DeviceManager.getInstance().getZtrsDevice().getStaticParameterBean();
        try {
            saveCalibration = staticParameterBean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if(staticParameterBean == null){
            Toast.makeText(this,"风力标定失败",Toast.LENGTH_LONG).show();
            return;
        }
        saveCalibration.setWindSpeedWarningValue((byte) binding.spinnerLowControl.getSelectedItemPosition());
        saveCalibration.setWindSpeedAlarmValue((byte) binding.spinnerHighControl.getSelectedItemPosition());
        DeviceManager.getInstance().setStaticParameter(saveCalibration);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWindCalibration(StaticParameterMessage msg){
        LogUtils.LogI("wch","onWindCalibration: "+msg.getCmdType());
        LogUtils.LogI("wch","onWindCalibration: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                DeviceManager.getInstance().getZtrsDevice().setStaticParameterBean(saveCalibration);
                initData();
                updateCalibration();
                Toast.makeText(this,"风力标定成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"风力标定失败",Toast.LENGTH_LONG).show();
            }
        }else if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                updateCalibration();
            }else {
                Toast.makeText(this,"风力标定查询失败",Toast.LENGTH_LONG).show();
            }
        }
    }

}