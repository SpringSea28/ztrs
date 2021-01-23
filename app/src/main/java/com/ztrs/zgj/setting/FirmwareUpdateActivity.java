package com.ztrs.zgj.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ztrs.zgj.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FirmwareUpdateActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    Unbinder bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firmware_update);
        bind = ButterKnife.bind(this);
        tvTitle.setText("固件升级");
    }

    @Override
    protected void onDestroy() {
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
                break;
        }
    }
}