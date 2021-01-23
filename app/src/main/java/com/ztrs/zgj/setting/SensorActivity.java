package com.ztrs.zgj.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ztrs.zgj.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SensorActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    Unbinder bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        bind = ButterKnife.bind(this);
        tvTitle.setText("传感器标定");
    }

    @Override
    protected void onDestroy() {
        bind.unbind();
        super.onDestroy();
    }

    @OnClick({R.id.tv_back,R.id.rl_weight,R.id.rl_height,R.id.rl_amplitude,R.id.rl_around,R.id.rl_torque,
    R.id.rl_wind,R.id.rl_dip,R.id.rl_wire})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_weight:
                startActivity(new Intent(SensorActivity.this, WeightCalibrationActivity.class));
                break;
            case R.id.rl_height:
                startActivity(new Intent(SensorActivity.this, HeightSensorCalibrationActivity.class));
                break;
            case R.id.rl_amplitude:
                startActivity(new Intent(SensorActivity.this, AmplitudeSensorCalibrationActivity.class));
                break;
            case R.id.rl_around:
                startActivity(new Intent(SensorActivity.this, AroundSensorCalibrationActivity.class));
                break;
            case R.id.rl_torque:
                startActivity(new Intent(SensorActivity.this, TorqueCalibrationActivity.class));
                break;
            case R.id.rl_wind:
                startActivity(new Intent(SensorActivity.this, WindCalibrationActivity.class));
                break;
            case R.id.rl_dip:
                startActivity(new Intent(SensorActivity.this, SlopeCalibrationActivity.class));
                break;
            case R.id.rl_wire:
                startActivity(new Intent(SensorActivity.this, WireRopeCalibrationActivity.class));
                break;
        }
    }
}