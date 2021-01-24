package com.ztrs.zgj.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.bean.DeviceVersionBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.DeviceUpdateMessage;
import com.ztrs.zgj.device.eventbus.DeviceVersionMessage;
import com.ztrs.zgj.device.eventbus.StaticParameterMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FirmwareUpdateActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_version)
    TextView tvVersion;

    Unbinder bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firmware_update);
        bind = ButterKnife.bind(this);
        tvTitle.setText("固件升级");
        updateVersion();
        EventBus.getDefault().register(this);
        DeviceManager.getInstance().deviceVersionCheck();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        bind.unbind();
        super.onDestroy();
    }

    @OnClick({R.id.tv_back,R.id.rl_check})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_check:
                DeviceManager.getInstance().deviceUpdate();
                break;
        }
    }

    private void updateVersion(){
        DeviceVersionBean deviceVersionBean = DeviceManager.getInstance().getZtrsDevice().getDeviceVersionBean();
        String version = Integer.toHexString(deviceVersionBean.getVerInt()&0xff)
                +"."+Integer.toHexString(deviceVersionBean.getVerFloat()&0xff);
        tvVersion.setText(version);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceUpdate(DeviceUpdateMessage msg){
        LogUtils.LogI("wch","onDeviceUpdate: "+msg.getCmdType());
        LogUtils.LogI("wch","onDeviceUpdate: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                Toast.makeText(this,"检查更新发送成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"检查更新发送失败",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceCheck(DeviceVersionMessage msg){
        LogUtils.LogI("wch","onDeviceCheck: "+msg.getCmdType());
        LogUtils.LogI("wch","onDeviceCheck: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_REPORT){
            updateVersion();
            return;
        }
        if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                Toast.makeText(this,"版本号查询发送成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"版本号查询发送失败",Toast.LENGTH_LONG).show();
            }
        }

    }
}