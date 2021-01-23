package com.ztrs.zgj.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.bean.WireRopeDetectionParametersSetBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.WireRopeDetectionParametersSetMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class WireRopeCalibrationActivity extends AppCompatActivity {

    private static final String TAG = WireRopeCalibrationActivity.class.getSimpleName();

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_threshold)
    TextView etThreshold;
    @BindView(R.id.et_light_damage)
    EditText etLightDamage;
    @BindView(R.id.et_middle_damage)
    EditText etMiddleDamage;
    @BindView(R.id.et_severe_damage)
    EditText etSevereDamage;
    @BindView(R.id.et_damage)
    EditText etDamage;
    @BindView(R.id.et_scrapped)
    EditText etScrapped;
    @BindView(R.id.et_delay)
    EditText etDelay;
    @BindView(R.id.et_detect_count)
    EditText etDetectCount;
    @BindView(R.id.et_mode)
    EditText etMode;
    @BindView(R.id.et_alarm_interval)
    EditText etAlarmInterval;
    @BindView(R.id.et_alarm_count)
    EditText etAlarmCount;

    @BindView(R.id.ll_wirerope_parameter_value)
    LinearLayout llWireRopeParameter;
    @BindView(R.id.img_wirerope_parameter_show)
    ImageView imgParameterShow;
    @BindView(R.id.ll_wirerope_detect_setting)
    LinearLayout llWireRopeDetect;
    @BindView(R.id.img_wirerope_detect_show)
    ImageView imgDetectShow;

    private WireRopeDetectionParametersSetBean wireRopeDetectionParametersSetBean;
    private WireRopeDetectionParametersSetBean saveCalibration;
    private boolean parameterShow = true;
    private boolean detectShow = false;

    Unbinder binder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wirerope_calibration);
        binder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initData();
        upDateView();
        DeviceManager.getInstance().queryWireRopeDetectionParameters();
//        new Test().queryWireRopeDetectionParameters();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        binder.unbind();
        super.onDestroy();
    }


    private void initData(){
        wireRopeDetectionParametersSetBean = DeviceManager.getInstance().getZtrsDevice().getWireRopeDetectionParametersSetBean();
    }

    private void initView(){
        tvTitle.setText("钢丝绳设置");
    }

    private void upDateView(){
        if(wireRopeDetectionParametersSetBean == null){
            return;
        }
        updateSettings();
        updateParameter();
    }

    private void updateSettings() {
        if(parameterShow ){
            llWireRopeParameter.setVisibility(View.VISIBLE);
            imgParameterShow.setImageDrawable(getDrawable(R.mipmap.zhankai));
        }else {
            llWireRopeParameter.setVisibility(View.GONE);
            imgParameterShow.setImageDrawable(getDrawable(R.mipmap.arrow_more));
        }
        if(detectShow){
            llWireRopeDetect.setVisibility(View.VISIBLE);
            imgDetectShow.setImageDrawable(getDrawable(R.mipmap.zhankai));
        }else {
            llWireRopeDetect.setVisibility(View.GONE);
            imgDetectShow.setImageDrawable(getDrawable(R.mipmap.arrow_more));
        }
    }

    private void updateParameter(){
        int threshold = wireRopeDetectionParametersSetBean.getThreshold();
        etThreshold.setText(String.format("%.1f",1.0*threshold/10));

        int lightDamage = wireRopeDetectionParametersSetBean.getLightDamage();
        etLightDamage.setText(String.format("%.1f",1.0*lightDamage/10));

        int middleDamage = wireRopeDetectionParametersSetBean.getMiddleDamage();
        etMiddleDamage.setText(String.format("%.1f",1.0*middleDamage/10));

        int severeDamage = wireRopeDetectionParametersSetBean.getSevereDamage();
        etSevereDamage.setText(String.format("%.1f",1.0*severeDamage/10));

        int damage = wireRopeDetectionParametersSetBean.getDamage();
        etDamage.setText(String.format("%.1f",1.0*damage/10));

        int scrapped = wireRopeDetectionParametersSetBean.getScrapped();
        etScrapped.setText(String.format("%.1f",1.0*scrapped/10));

        int delay = wireRopeDetectionParametersSetBean.getDelay();
        etDelay.setText(String.format("%.1f",1.0*delay/10));

        int detectCount = wireRopeDetectionParametersSetBean.getDetectCount();
        etDetectCount.setText(""+detectCount);

        int mode = wireRopeDetectionParametersSetBean.getMode();
        etMode.setText(""+mode);

        int interval = wireRopeDetectionParametersSetBean.getAlarmInterval();
        etAlarmInterval.setText(String.format("%.1f",1.0*interval/10));

        int alarmCount = wireRopeDetectionParametersSetBean.getAlarmCount();
        etAlarmCount.setText(""+alarmCount);
    }

    @OnClick({R.id.btn_save,R.id.tv_back,R.id.rl_wirerope_parameter,
    R.id.rl_wirerope_detect,R.id.rl_start_detect,R.id.rl_stop_detect,R.id.rl_query_state,
    R.id.rl_reset,R.id.tv_clear})
    public void onClick(View view){
        if(view.getId() == R.id.btn_save){
            save();
        } else if(view.getId() == R.id.tv_back){
            finish();
        } else if(view.getId() == R.id.rl_wirerope_parameter){
            parameterShow =!parameterShow;
            updateSettings();
        } else if(view.getId() == R.id.rl_wirerope_detect){
            detectShow =!detectShow;
            updateSettings();
        } else if(view.getId() == R.id.rl_start_detect){
            DeviceManager.getInstance().wireRopeStartDetect();
        }else if(view.getId() == R.id.rl_stop_detect){
            DeviceManager.getInstance().wireRopeStopDetect();
        }else if(view.getId() == R.id.rl_query_state){
            DeviceManager.getInstance().wireRopeQueryState();
        }else if(view.getId() == R.id.rl_reset){
            DeviceManager.getInstance().wireRopeReset();
        }else if(view.getId() == R.id.tv_clear){
            DeviceManager.getInstance().wireRopeClear();
        }
    }

    private void save(){
        String reInput = getString(R.string.setting_re_input);

        String thresholdStr = etThreshold.getText().toString();
        float threshold = 0;
        try {
            threshold = Float.valueOf(thresholdStr);
        }catch (NumberFormatException e){
            etThreshold.setHint(reInput);
            return;
        }


        String lightDamageStr = etLightDamage.getText().toString();
        float lightDamage = 0;
        try {
            lightDamage = Float.valueOf(lightDamageStr);
        }catch (NumberFormatException e){
            etLightDamage.setHint(reInput);
            return;
        }

        String middleDamageStr = etMiddleDamage.getText().toString();
        float middleDamage = 0;
        try {
            middleDamage = Float.valueOf(middleDamageStr);
        }catch (NumberFormatException e){
            etMiddleDamage.setHint(reInput);
            return;
        }

        String  severeDamageStr = etSevereDamage.getText().toString();
        float severeDamage = 0;
        try {
            severeDamage = Float.valueOf(severeDamageStr);
        }catch (NumberFormatException e){
            etSevereDamage.setHint(reInput);
            return;
        }

        String damageStr = etDamage.getText().toString();
        float damage = 0;
        try {
            damage = Float.valueOf(damageStr);
        }catch (NumberFormatException e){
            etDamage.setHint(reInput);
            return;
        }

        String scrappedStr = etScrapped.getText().toString();
        float scrapped = 0;
        try {
            scrapped = Float.valueOf(scrappedStr);
        }catch (NumberFormatException e){
            etScrapped.setHint(reInput);
            return;
        }

        String delayStr = etDelay.getText().toString();
        float delay = 0;
        try {
            delay = Float.valueOf(delayStr);
        }catch (NumberFormatException e){
            etDelay.setHint(reInput);
            return;
        }



        String detectCountStr = etDetectCount.getText().toString();
        int detectCount = 0;
        try {
            detectCount = Integer.valueOf(detectCountStr);
        }catch (NumberFormatException e){
            etDetectCount.setHint(reInput);
            return;
        }

        String modeStr = etMode.getText().toString();
        int mode = 0;
        try {
            mode = Integer.valueOf(modeStr);
        }catch (NumberFormatException e){
            etMode.setHint(reInput);
            return;
        }

        String alarmIntervalStr = etAlarmInterval.getText().toString();
        float interval = 0;
        try {
            interval = Float.valueOf(alarmIntervalStr);
        }catch (NumberFormatException e){
            etAlarmInterval.setHint(reInput);
            return;
        }

        String alarmCountStr = etAlarmCount.getText().toString();
        int alarmCount = 0;
        try {
            alarmCount = Integer.valueOf(alarmCountStr);
        }catch (NumberFormatException e){
            etAlarmCount.setHint(reInput);
            return;
        }


        WireRopeDetectionParametersSetBean wireRopeDetectionParametersSetBean = DeviceManager.getInstance().getZtrsDevice()
                .getWireRopeDetectionParametersSetBean();
        try {
            saveCalibration = wireRopeDetectionParametersSetBean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if(saveCalibration == null){
            Toast.makeText(this,"钢丝绳参数设置失败",Toast.LENGTH_LONG).show();
            return;
        }
        saveCalibration.setThreshold((int)(threshold*10));
        saveCalibration.setLightDamage((int)(lightDamage*10));
        saveCalibration.setMiddleDamage((int)(middleDamage*10));
        saveCalibration.setSevereDamage((int)(severeDamage*10));
        saveCalibration.setDamage((int)(damage*10));
        saveCalibration.setScrapped((int)(scrapped*10));
        saveCalibration.setDelay((int)(delay*10));
        saveCalibration.setDetectCount(detectCount);
        saveCalibration.setMode(mode);
        saveCalibration.setAlarmInterval((int)(interval*10));
        saveCalibration.setAlarmCount(alarmCount);
        DeviceManager.getInstance().setWireRopeDetectionParameters(saveCalibration);
    }

    //-----------------------------------------------------------//
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWireRopeDetectionParameters(WireRopeDetectionParametersSetMessage msg){
        LogUtils.LogI(TAG,"onWireRopeDetectionParametersSet: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onWireRopeDetectionParametersSet: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                DeviceManager.getInstance().getZtrsDevice()
                        .setWireRopeDetectionParametersSetBean(saveCalibration);
                Toast.makeText(this,"钢丝绳参数设置保存成功",Toast.LENGTH_LONG).show();
                initData();
                upDateView();
            }else {
                Toast.makeText(this,"钢丝绳参数设置保存失败",Toast.LENGTH_LONG).show();
            }
        }else if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                initData();
                upDateView();
            }else {
                Toast.makeText(this,"钢丝绳参数查询失败",Toast.LENGTH_LONG).show();
            }
        }
    }

}