package com.ztrs.zgj.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.RelayConfigurationMessage;
import com.ztrs.zgj.device.eventbus.VolumeMessage;
import com.ztrs.zgj.main.BaseActivity;
import com.ztrs.zgj.setting.bean.TorqueCurveBean;
import com.ztrs.zgj.setting.eventbus.SettingEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SystemSetActivity extends BaseActivity {
    private static final String TAG = SystemSetActivity.class.getSimpleName();

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.sb_volume)
    AppCompatSeekBar seekBar;
    @BindView(R.id.tv_vol_val)
    TextView tvVolVal;

    @BindView(R.id.sb_light)
    AppCompatSeekBar seekLight;
    @BindView(R.id.tv_light_val)
    TextView tvLightVal;



    Unbinder bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_set);
        bind = ButterKnife.bind(this);
        tvTitle.setText("系统设置");
        SharedPreferences sp = getSharedPreferences("SystemSetting",MODE_PRIVATE);
        int volume = sp.getInt("volume",50);
        seekBar.setProgress(volume);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                tvVolVal.setText(""+progress);
                SharedPreferences sp = getSharedPreferences("SystemSetting",MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putInt("volume",progress);
                edit.commit();
                SettingEventBus settingEventBus = new SettingEventBus(SettingEventBus.ACTION_VOLUME_CHANGE);
                settingEventBus.setValue(progress);
                EventBus.getDefault().post(settingEventBus);
            }
        });
        tvVolVal.setText(""+volume);

        int light = sp.getInt("light",50);
        seekLight.setProgress(light);
        seekLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                tvLightVal.setText(""+progress);
                SharedPreferences sp = getSharedPreferences("SystemSetting",MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putInt("light",progress);
                edit.commit();
                SettingEventBus settingEventBus = new SettingEventBus(SettingEventBus.ACTION_LIGHT_CHANGE);
                settingEventBus.setValue(progress);
                EventBus.getDefault().post(settingEventBus);
                updateLight();
            }
        });
        tvLightVal.setText(""+light);
    }

    @Override
    protected void onDestroy() {

        bind.unbind();
        super.onDestroy();
    }

    @OnClick({R.id.tv_back,R.id.rl_firmware_update,R.id.rl_soft_update,R.id.rl_reset})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_firmware_update:
                startActivity(new Intent(SystemSetActivity.this, FirmwareUpdateActivity.class));
                break;
            case R.id.rl_soft_update:
                startActivity(new Intent(SystemSetActivity.this, SoftwareUpdateActivity.class));
                break;
            case R.id.rl_reset:
                break;
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onVolume(VolumeMessage msg){
//        LogUtils.LogI(TAG,"onVolume: "+msg.getCmdType());
//        LogUtils.LogI(TAG,"onVolume: "+msg.getResult());
//        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
//            if(msg.getResult() == BaseMessage.RESULT_OK) {
//                Toast.makeText(this,"音量设置成功",Toast.LENGTH_LONG).show();
//            }else {
//                Toast.makeText(this,"音量设置失败",Toast.LENGTH_LONG).show();
//            }
//        }else if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
//            if(msg.getResult() == BaseMessage.RESULT_OK) {
//                byte volume = DeviceManager.getInstance().getZtrsDevice().getVolumeBean().getVolume();
//                seekBar.setProgress(volume);
//            }else {
//                Toast.makeText(this,"音量查询失败",Toast.LENGTH_LONG).show();
//            }
//        }
//    }
}