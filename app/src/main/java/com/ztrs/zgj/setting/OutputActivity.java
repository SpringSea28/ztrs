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
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.bean.RelayConfigurationBean;
import com.ztrs.zgj.device.bean.RelayOutputControlBean;
import com.ztrs.zgj.device.bean.StaticParameterBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.RelayConfigurationMessage;
import com.ztrs.zgj.device.eventbus.StaticParameterMessage;
import com.ztrs.zgj.main.BaseActivity;
import com.ztrs.zgj.setting.adapter.OutputAdapter;
import com.ztrs.zgj.setting.bean.RelayBean;
import com.ztrs.zgj.setting.utils.OutPutChangUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class OutputActivity extends BaseActivity {

    RelayConfigurationBean calibrationBean;
    ActivityOutputBinding binding;
    List<RelayBean> relayBeans;


    OutputAdapter outputAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EventBus.getDefault().register(this);
        initData();
        initUi();
        DeviceManager.getInstance().queryRelayConfiguration();
//        new Test().queryRelayConfiguration();
    }

    private void initData(){
        RelayConfigurationBean calibrationBean = DeviceManager.getInstance().getZtrsDevice().getRelayConfigurationBean();
        this.calibrationBean = calibrationBean;
        binding.setCalibration(this.calibrationBean);
        relayBeans = OutPutChangUtils.getRelayBeanList(calibrationBean);
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
        outputAdapter.setData(relayBeans);
        binding.rvRelay.setAdapter(outputAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvRelay.setLayoutManager(linearLayoutManager);
    }



    void updateCalibration() {
        relayBeans = OutPutChangUtils.getRelayBeanList(calibrationBean);
        outputAdapter.setData(relayBeans);
        outputAdapter.notifyDataSetChanged();
    }

    private void save() {
        for(int i=0;i<relayBeans.size();i++){
            for(int j=i+1;j<relayBeans.size();j++){
                if((relayBeans.get(i).getUse() == relayBeans.get(j).getUse())
                && relayBeans.get(i).getUse() != 10){
                    Toast.makeText(this,"继电器用途重叠",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        DeviceManager.getInstance().setRelayConfiguration(OutPutChangUtils.getRelayConfig(relayBeans));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRelayConfig(RelayConfigurationMessage msg){
        LogUtils.LogI("wch","onRelayConfig: "+msg.getCmdType());
        LogUtils.LogI("wch","onRelayConfig: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                updateCalibration();
                Toast.makeText(this,"输出控制设置成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"输出控制设置失败",Toast.LENGTH_LONG).show();
            }
        }else if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                updateCalibration();
            }else {
                Toast.makeText(this,"输出控制查询失败",Toast.LENGTH_LONG).show();
            }
        }
    }

}