package com.ztrs.zgj.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.ActivityFirmwareUpdateBinding;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.bean.DeviceVersionBean;
import com.ztrs.zgj.device.bean.RegisterInfoBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.DeviceUpdateMessage;
import com.ztrs.zgj.device.eventbus.DeviceVersionMessage;
import com.ztrs.zgj.device.eventbus.RegisterInfoMessage;
import com.ztrs.zgj.device.eventbus.StaticParameterMessage;
import com.ztrs.zgj.main.BaseActivity;
import com.ztrs.zgj.setting.dialog.UpdateDialog;
import com.ztrs.zgj.setting.viewModel.AppUpdateViewModel;
import com.ztrs.zgj.setting.viewModel.DeviceUpdateViewModel;
import com.ztrs.zgj.setting.viewModel.VersionModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class FirmwareUpdateActivity extends BaseActivity implements View.OnClickListener {

    ActivityFirmwareUpdateBinding binding;
    DeviceUpdateViewModel deviceUpdateViewModel;
    UpdateDialog updateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_firmware_update);
        binding.rlTitle.tvTitle.setText("固件升级");
        binding.rlTitle.tvBack.setOnClickListener(this);
        binding.rlCheck.setOnClickListener(this);
        deviceUpdateViewModel = new ViewModelProvider(this).get(DeviceUpdateViewModel.class);
        LiveData<String> curVersion = deviceUpdateViewModel.getCurVersion();
        curVersion.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvVersion.setText(s);
            }
        });
        LiveData<VersionModel.UpdateState> updateState = deviceUpdateViewModel.getUpdateState();
        updateState.observe(this, new Observer<VersionModel.UpdateState>() {
            @Override
            public void onChanged(VersionModel.UpdateState updateState) {
                onUpdateStateChange(updateState);
            }
        });
        EventBus.getDefault().register(this);
        deviceUpdateViewModel.initVersion(this);
//        new Test().testOnReceiveRegisterInfo();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_check:
                deviceUpdateViewModel.checkVersion();
                break;
        }
    }

    private void onUpdateStateChange(VersionModel.UpdateState updateState){
        switch (updateState){
            case IDLE:

                break;
            case CHECKING:
                if(updateDialog == null || !updateDialog.isShowing()) {
                    updateDialog = new UpdateDialog(this);
                    updateDialog.initText("检测新版本中...");
                    updateDialog.initButton(false,false);
                    updateDialog.show();
                }
                break;
            case CHECK_SUCCESS_CAN_UPDATE:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("检测新版本: " + deviceUpdateViewModel.getRemoteVersion()+"\n"+"确认升级？");
                    updateDialog.showButton();
                    updateDialog.setOnUserClick(() -> {
                        deviceUpdateViewModel.update(this);
                    });
                    updateDialog.show();
                }
                break;
            case CHECK_SUCCESS_NO_UPDATE:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("已经是最新版本");
                    updateDialog.showConfirm();
                    updateDialog.setOnUserClick(() -> updateDialog.dismiss());
                    updateDialog.show();
                }
                break;
            case CHECK_FAIL:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("检测新版本失败");
                    updateDialog.showConfirm();
                    updateDialog.setOnUserClick(() -> updateDialog.dismiss());
                    updateDialog.show();
                }
                break;
            case DOWNLOADING:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("升级中...");
                    updateDialog.hideButton();
                    updateDialog.show();
                }
                break;
            case DOWNLOAD_SUCCESS:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("升级成功");
                    updateDialog.showConfirm();
                    updateDialog.setOnUserClick(() -> {
                        updateDialog.dismiss();
                    });
                    updateDialog.show();
                }
                break;
            case DOWNLOAD_FAIL:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("抱歉，升级失败");
                    updateDialog.showConfirm();
                    updateDialog.setOnUserClick(() -> updateDialog.dismiss());
                    updateDialog.show();
                }
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceUpdate(DeviceUpdateMessage msg){
        LogUtils.LogI("wch","onDeviceUpdate: "+msg.getCmdType());
        LogUtils.LogI("wch","onDeviceUpdate: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                Toast.makeText(this,"升级命令发送成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"升级命令发送失败",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceCheck(DeviceVersionMessage msg){
        LogUtils.LogI("wch","onDeviceCheck: "+msg.getCmdType());
        LogUtils.LogI("wch","onDeviceCheck: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_REPORT){
            deviceUpdateViewModel.onGetRemoteVersion(true);
            return;
        }
        if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                Toast.makeText(this,"远端版本号查询发送成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"远端版本号查询发送失败",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterInfo(RegisterInfoMessage msg){
        LogUtils.LogI("wch","onRegisterInfo: "+msg.getCmdType());
        LogUtils.LogI("wch","onRegisterInfo: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_REPORT){
            deviceUpdateViewModel.updateCurVersion();
            return;
        }
        if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                Toast.makeText(this,"版本号查询发送成功",Toast.LENGTH_LONG).show();
                deviceUpdateViewModel.updateCurVersion();
            }else {
                Toast.makeText(this,"版本号查询发送失败",Toast.LENGTH_LONG).show();
            }
        }
    }
}