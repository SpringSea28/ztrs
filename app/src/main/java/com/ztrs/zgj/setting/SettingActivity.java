package com.ztrs.zgj.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ztrs.zgj.R;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.main.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;

    Unbinder bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        bind = ButterKnife.bind(this);
        tvTitle.setText("设置");
    }

    @Override
    protected void onDestroy() {
        bind.unbind();
        super.onDestroy();
    }

    @OnClick({R.id.tv_back,R.id.rl_installation,R.id.rl_sensor,R.id.rl_collision,R.id.rl_output,
    R.id.rl_system})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_installation:
                startActivity(new Intent(SettingActivity.this, InstallationActivity.class));
                break;
            case R.id.rl_sensor:
                startActivity(new Intent(SettingActivity.this, SensorActivity.class));
                break;
            case R.id.rl_collision:
                startActivity(new Intent(SettingActivity.this, PreventCollisionActivity.class));
                break;
            case R.id.rl_output:
                startActivity(new Intent(SettingActivity.this, OutputActivity.class));
                break;
            case R.id.rl_system:
                startActivity(new Intent(SettingActivity.this, SystemSetActivity.class));
                break;
        }
    }
}