package com.ztrs.zgj.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kongqw.serialportlibrary.Device;
import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.bean.PreventCollisionCalibrationBean;
import com.ztrs.zgj.device.bean.PreventCollisionNearBean;
import com.ztrs.zgj.device.bean.PreventCollisionNearBean.NearCoordinate;
import com.ztrs.zgj.device.bean.RegionalRestrictionsBean;
import com.ztrs.zgj.device.bean.RegionalRestrictionsBean.OrthogonalCoordinate;
import com.ztrs.zgj.device.bean.StaticParameterBean;
import com.ztrs.zgj.device.bean.WireRopeDetectionParametersSetBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.PreventCollisionCalibrationMessage;
import com.ztrs.zgj.device.eventbus.PreventCollisionNearReportMessage;
import com.ztrs.zgj.device.eventbus.RegionalRestrictionMessage;
import com.ztrs.zgj.device.eventbus.StaticParameterMessage;
import com.ztrs.zgj.device.eventbus.WireRopeDetectionParametersSetMessage;
import com.ztrs.zgj.device.eventbus.WireRopeDetectionReportMessage;
import com.ztrs.zgj.setting.adapter.NearAdapter;
import com.ztrs.zgj.setting.adapter.ObstacleAdapter;
import com.ztrs.zgj.setting.adapter.TorqueCureAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PreventCollisionActivity extends AppCompatActivity {

    private static final String TAG = PreventCollisionActivity.class.getSimpleName();

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_self_x)
    EditText etSelfX;
    @BindView(R.id.et_self_y)
    EditText etSelfY;
    @BindView(R.id.et_self_height)
    EditText etSelfHeight;

    @BindView(R.id.et_height_warn)
    EditText etHeightWarn;
    @BindView(R.id.et_height_alarm)
    EditText etHeightAlarm;
    @BindView(R.id.et_distance_warn)
    EditText etDistanceWarn;
    @BindView(R.id.et_distance_alarm)
    EditText etDistanceAlarm;

    @BindView(R.id.ll_self_value)
    LinearLayout llSelf;
    @BindView(R.id.img_self_show)
    ImageView imgSelfShow;
    @BindView(R.id.ll_regional_restriction_value)
    LinearLayout llRegionalRestrictionValue;
    @BindView(R.id.img_regional_restriction_show)
    ImageView imgRegionalRestrictionShow;
    @BindView(R.id.ll_alarm_setting_value)
    LinearLayout llAlarm;
    @BindView(R.id.img_alarm_setting_show)
    ImageView imgAlarmShow;
    @BindView(R.id.ll_near_value)
    LinearLayout llNear;
    @BindView(R.id.img_near_show)
    ImageView imgNearShow;

    @BindView(R.id.rv_obstacle)
    RecyclerView rvObstacle;
    private ObstacleAdapter obstacleAdapter;

    @BindView(R.id.rv_near)
    RecyclerView rvNear;
    private NearAdapter nearAdapter;

    private StaticParameterBean staticParameterBean;
    private StaticParameterBean saveStaticParameterBean;
    private ConcurrentHashMap<Integer, OrthogonalCoordinate> orthogonalCoordinates;
    private OrthogonalCoordinate saveOrthogonalCoordinate;
    private int savePosition;
    private PreventCollisionCalibrationBean preventCollisionCalibrationBean;
    private PreventCollisionCalibrationBean savePreventCollisionCalibrationBean;

    private PreventCollisionNearBean preventCollisionNearBean;

    private boolean selfShow = false;
    private boolean regionalShow = false;
    private boolean alarmShow = false;
    private boolean nearShow = false;

    Unbinder binder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevent_collision);
        binder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initData();
        upDateView();
        DeviceManager.getInstance().queryRegionalRestriction();
        DeviceManager.getInstance().queryPreventCollisionCalibration();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        binder.unbind();
        super.onDestroy();
    }


    private void initData(){
        staticParameterBean = DeviceManager.getInstance().getZtrsDevice().getStaticParameterBean();
        orthogonalCoordinates = DeviceManager.getInstance().getZtrsDevice().getRegionalRestrictionsBean().getOrthogonalCoordinates();
        preventCollisionCalibrationBean = DeviceManager.getInstance().getZtrsDevice().getPreventCollisionCalibrationBean();
        preventCollisionNearBean = DeviceManager.getInstance().getZtrsDevice().getPreventCollisionNearBean();
    }

    private void initView(){
        tvTitle.setText("防碰撞设置");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        obstacleAdapter = new ObstacleAdapter();
        rvObstacle.setLayoutManager(linearLayoutManager);
        rvObstacle.setAdapter(obstacleAdapter);
        obstacleAdapter.setOnSaveListener(new ObstacleAdapter.OnSaveListener() {
            @Override
            public void onSaveClick(OrthogonalCoordinate orthogonalCoordinate, int position) {
                PreventCollisionActivity.this.saveOrthogonalCoordinate = orthogonalCoordinate;
                PreventCollisionActivity.this.savePosition = position;
                DeviceManager.getInstance().setOrthogonalRegionalRestriction(orthogonalCoordinate);
            }
        });

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        nearAdapter = new NearAdapter();
        rvNear.setLayoutManager(linearLayoutManager1);
        rvNear.setAdapter(nearAdapter);
    }

    private void upDateView(){
        if(staticParameterBean == null){
            return;
        }
        updateSelf();
        updateRegional();
        updateAlarm();
        updateNear();
    }

    private void updateAlarm(){
        if(alarmShow){
            llAlarm.setVisibility(View.VISIBLE);
            imgAlarmShow.setImageDrawable(getDrawable(R.mipmap.zhankai));
        }else {
            llAlarm.setVisibility(View.GONE);
            imgAlarmShow.setImageDrawable(getDrawable(R.mipmap.arrow_more));
        }
        int heightWarnValue = preventCollisionCalibrationBean.getHeightWarnValue();
        etHeightWarn.setText(String.format("%.1f",1.0*heightWarnValue/10));
        int heightAlarmValue = preventCollisionCalibrationBean.getHeightAlarmValue();
        etHeightAlarm.setText(String.format("%.1f",1.0*heightAlarmValue/10));
        int distanceWarnValue = preventCollisionCalibrationBean.getDistanceWarnValue();
        etDistanceWarn.setText(String.format("%.1f",1.0*distanceWarnValue/10));
        int distanceAlarmValue = preventCollisionCalibrationBean.getDistanceAlarmValue();
        etDistanceAlarm.setText(String.format("%.1f",1.0*distanceAlarmValue/10));
    }

    private void updateSelf() {
        if(selfShow){
            llSelf.setVisibility(View.VISIBLE);
            imgSelfShow.setImageDrawable(getDrawable(R.mipmap.zhankai));
        }else {
            llSelf.setVisibility(View.GONE);
            imgSelfShow.setImageDrawable(getDrawable(R.mipmap.arrow_more));
        }
        int towerX = staticParameterBean.getTowerX();
        etSelfX.setText(String.format("%.1f",1.0*towerX/10));

        int towerY = staticParameterBean.getTowerY();
        etSelfY.setText(String.format("%.1f",1.0*towerY/10));

        int towerHeight = staticParameterBean.getTowerHeight();
        etSelfHeight.setText(String.format("%.1f",1.0*towerHeight/10));
    }

    private void updateRegional() {
        if(regionalShow){
            llRegionalRestrictionValue.setVisibility(View.VISIBLE);
            imgRegionalRestrictionShow.setImageDrawable(getDrawable(R.mipmap.zhankai));
        }else {
            llRegionalRestrictionValue.setVisibility(View.GONE);
            imgRegionalRestrictionShow.setImageDrawable(getDrawable(R.mipmap.arrow_more));
        }
        obstacleAdapter.setData(orthogonalCoordinates);
        obstacleAdapter.notifyDataSetChanged();
    }

    private void updateNear() {
        if(nearShow){
            llNear.setVisibility(View.VISIBLE);
            imgNearShow.setImageDrawable(getDrawable(R.mipmap.zhankai));
        }else {
            llNear.setVisibility(View.GONE);
            imgNearShow.setImageDrawable(getDrawable(R.mipmap.arrow_more));
        }
        Collection<NearCoordinate> values = preventCollisionNearBean.getNearCoordinates().values();
        ArrayList<NearCoordinate> nearCoordinates = new ArrayList<>(values);
        nearAdapter.setData(nearCoordinates);
        nearAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btn_save,R.id.tv_back,R.id.rl_self,R.id.rl_regional_restriction,R.id.rl_alarm_setting,
    R.id.btn_save_alarm,R.id.rl_near_prevent_collision})
    public void onClick(View view){
        if(view.getId() == R.id.btn_save){
            save();
        } else if(view.getId() == R.id.tv_back){
            finish();
        } else if(view.getId() == R.id.rl_self){
            selfShow =!selfShow;
            updateSelf();
        }else if(view.getId() == R.id.rl_regional_restriction){
            regionalShow =!regionalShow;
            updateRegional();
        } else if(view.getId() == R.id.rl_alarm_setting){
            alarmShow =!alarmShow;
            updateAlarm();
        } else if(view.getId() == R.id.btn_save_alarm){
            saveAlarm();
        } else if(view.getId() == R.id.rl_near_prevent_collision){
            nearShow =!nearShow;
            updateNear();
        }
    }

    private void saveAlarm(){
        String reInput = getString(R.string.setting_re_input);

        String heightWarnStr = etHeightWarn.getText().toString();
        float heightWarn = 0;
        try {
            heightWarn = Float.valueOf(heightWarnStr);
        }catch (NumberFormatException e){
            etHeightWarn.setHint(reInput);
            return;
        }

        String heightAlarmStr = etHeightAlarm.getText().toString();
        float heightAlarm = 0;
        try {
            heightAlarm = Float.valueOf(heightAlarmStr);
        }catch (NumberFormatException e){
            etHeightAlarm.setHint(reInput);
            return;
        }

        String distanceWarnStr = etDistanceWarn.getText().toString();
        float distanceWarn = 0;
        try {
            distanceWarn = Float.valueOf(distanceWarnStr);
        }catch (NumberFormatException e){
            etDistanceWarn.setHint(reInput);
            return;
        }

        String distanceAlarmStr = etDistanceAlarm.getText().toString();
        float distanceAlarm = 0;
        try {
            distanceAlarm = Float.valueOf(distanceAlarmStr);
        }catch (NumberFormatException e){
            etDistanceAlarm.setHint(reInput);
            return;
        }

        PreventCollisionCalibrationBean preventCollisionCalibrationBean = DeviceManager.getInstance().getZtrsDevice()
                .getPreventCollisionCalibrationBean();
        try {
            savePreventCollisionCalibrationBean = preventCollisionCalibrationBean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        if(savePreventCollisionCalibrationBean == null){
            Toast.makeText(this,"防碰撞报警参数设置失败",Toast.LENGTH_LONG).show();
            return;
        }
        savePreventCollisionCalibrationBean.setHeightWarnValue((int)(heightWarn*10));
        savePreventCollisionCalibrationBean.setHeightAlarmValue((int)(heightAlarm*10));
        savePreventCollisionCalibrationBean.setDistanceWarnValue((int)(distanceWarn*10));
        savePreventCollisionCalibrationBean.setDistanceAlarmValue((int)(distanceAlarm*10));
        DeviceManager.getInstance().preventCollisionCalibration(savePreventCollisionCalibrationBean);
    }

    private void save(){
        String reInput = getString(R.string.setting_re_input);

        String selfXStr = etSelfX.getText().toString();
        float selfX = 0;
        try {
            selfX = Float.valueOf(selfXStr);
        }catch (NumberFormatException e){
            etSelfX.setHint(reInput);
            return;
        }

        String selfYStr = etSelfY.getText().toString();
        float selfY = 0;
        try {
            selfY = Float.valueOf(selfYStr);
        }catch (NumberFormatException e){
            etSelfY.setHint(reInput);
            return;
        }

        String selfHeightStr = etSelfHeight.getText().toString();
        float selfHeight = 0;
        try {
            selfHeight = Float.valueOf(selfHeightStr);
        }catch (NumberFormatException e){
            etSelfHeight.setHint(reInput);
            return;
        }

        StaticParameterBean staticParameterBean = DeviceManager.getInstance().getZtrsDevice()
                .getStaticParameterBean();
        try {
            saveStaticParameterBean = staticParameterBean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        if(saveStaticParameterBean == null){
            Toast.makeText(this,"本塔机参数设置失败",Toast.LENGTH_LONG).show();
            return;
        }
        saveStaticParameterBean.setTowerX((int)(selfX*10));
        saveStaticParameterBean.setTowerY((int)(selfY*10));
        saveStaticParameterBean.setTowerHeight((int)(selfHeight*10));
        DeviceManager.getInstance().setStaticParameter(saveStaticParameterBean);
    }

    //-----------------------------------------------------------//
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaticParameter(StaticParameterMessage msg){
        LogUtils.LogI(TAG,"onStaticParameter: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onStaticParameter: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                DeviceManager.getInstance().getZtrsDevice()
                        .setStaticParameterBean(saveStaticParameterBean);
                Toast.makeText(this,"本塔机参数设置保存成功",Toast.LENGTH_LONG).show();
                initData();
                updateSelf();
            }else {
                Toast.makeText(this,"本塔机参数设置保存失败",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegionalRestriction(RegionalRestrictionMessage msg){
        LogUtils.LogI(TAG,"onRegionalRestriction: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onRegionalRestriction: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                DeviceManager.getInstance().getZtrsDevice()
                        .getRegionalRestrictionsBean().putOrthogonalCoordinate(saveOrthogonalCoordinate);
                Toast.makeText(this,"区域限制参数设置保存成功",Toast.LENGTH_LONG).show();
                obstacleAdapter.notifyItemChanged(savePosition);
            }else {
                Toast.makeText(this,"区域限制参数设置保存失败",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPreventCollision(PreventCollisionCalibrationMessage msg){
        LogUtils.LogI(TAG,"onPreventCollision: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onPreventCollision: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                DeviceManager.getInstance().getZtrsDevice()
                        .setPreventCollisionCalibrationBean(savePreventCollisionCalibrationBean);
                Toast.makeText(this,"防碰撞报警参数设置保存成功",Toast.LENGTH_LONG).show();
                initData();
                updateAlarm();
            }else {
                Toast.makeText(this,"防碰撞报警参数设置保存失败",Toast.LENGTH_LONG).show();
            }
        }else if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                initData();
                updateAlarm();
            }else {
//                Toast.makeText(this,"防碰撞报警参数查询失败",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNear(PreventCollisionNearReportMessage msg){
        LogUtils.LogI("wch","onNear: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_REPORT) {
            initData();
            updateNear();
        }
    }

}