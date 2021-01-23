package com.ztrs.zgj.setting;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.ActivityOutputBinding;
import com.ztrs.zgj.databinding.ActivityWindCalibrationBinding;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.RelayOutputControlBean;
import com.ztrs.zgj.device.bean.StaticParameterBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.StaticParameterMessage;
import com.ztrs.zgj.setting.adapter.OutputAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class OutputActivity extends AppCompatActivity {

    RelayOutputControlBean calibrationBean;
    ActivityOutputBinding binding;
    StaticParameterBean saveCalibration;

    OutputAdapter outputAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EventBus.getDefault().register(this);
        initData();
        initUi();
        DeviceManager.getInstance().queryRelayOutputConfig();
    }

    private void initData(){
        RelayOutputControlBean calibrationBean = DeviceManager.getInstance().getZtrsDevice().getRelayOutputControlBean();
        this.calibrationBean = calibrationBean;
        binding.setCalibration(this.calibrationBean);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initUi() {
        binding.tvTitle.setText("输出控制设置");
        initRecycleView();
        updateCalibration();
        binding.btnSave.setOnClickListener(v -> save());
        binding.tvBack.setOnClickListener(v -> finish());
    }

    private void initRecycleView(){
        outputAdapter = new OutputAdapter();
        String[] relay = getResources().getStringArray(R.array.relay);
        String[] use = getResources().getStringArray(R.array.use);
        String[] action = getResources().getStringArray(R.array.output);
        outputAdapter.setSrc(relay,use,action);
        binding.rvRelay.setAdapter(outputAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvRelay.setLayoutManager(linearLayoutManager);
    }



    void updateCalibration() {
//        binding.spinnerLowControl.setSelection(calibrationBean.getWindSpeedWarningValue());
//        binding.spinnerHighControl.setSelection(calibrationBean.getWindSpeedAlarmValue());
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