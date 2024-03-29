package com.ztrs.zgj.main.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.ZtrsDevice;
import com.ztrs.zgj.main.adapter.TowerParameterAdapter;
import com.ztrs.zgj.device.bean.RealTimeDataBean;
import com.ztrs.zgj.device.bean.StaticParameterBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.RealTimeDataMessage;
import com.ztrs.zgj.device.eventbus.StaticParameterMessage;
import com.ztrs.zgj.device.protocol.RealTimeDataProtocol;
import com.ztrs.zgj.main.adapter.TowerParameterAlarmAdapter;
import com.ztrs.zgj.setting.HistoryActivity;
import com.ztrs.zgj.utils.ScaleUtils;
import com.ztrs.zgj.main.viewbean.TowerParameterBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TowerParameterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TowerParameterFragment extends Fragment {
    private static final String TAG = TowerParameterFragment.class.getSimpleName();

    @BindView(R.id.tv_tower_serial_number)
    TextView tvTowerSerialNumber;
    @BindView(R.id.tv_tower_specification_model)
    TextView tvTowerSpecificationModel;
    @BindView(R.id.tv_tower_monitoring_number)
    TextView tvTowerSerialMonitoringNumber;
    @BindView(R.id.tv_tower_magnification)
    TextView tvTowerMagnification;
    @BindView(R.id.rv_parameter)
    RecyclerView rvParameter;

    @BindView(R.id.tv_direction_up)
    TextView tvDirectionUp;
    @BindView(R.id.tv_direction_down)
    TextView tvDirectionDown;
    @BindView(R.id.tv_direction_left)
    TextView tvDirectionLeft;
    @BindView(R.id.tv_direction_right)
    TextView tvDirectionRight;
    @BindView(R.id.tv_direction_before)
    TextView tvDirectionBefore;
    @BindView(R.id.tv_direction_after)
    TextView tvDirectionAfter;

    @BindView(R.id.tv_after_arm_value)
    TextView tvAfterArmValue;
    @BindView(R.id.tv_max_height_value)
    TextView tvMaxHeightValue;
    @BindView(R.id.tv_big_arm_value)
    TextView tvBigArmValue;

    @BindView(R.id.tv_wire_rope_damage_position_value)
    TextView tvWireRopeDamagePositionValue;
    @BindView(R.id.tv_wire_rope_cur_position_value)
    TextView tvWireCurrentPositionValue;
    @BindView(R.id.tv_wire_rope_state_value)
    TextView tvWireRopeStateValue;
    @BindView(R.id.tv_current_max_load_value)
    TextView tvCurrentMaxLoadValue;

    @BindView(R.id.img_rotation_angle)
    ImageView imgRotationAngle;
    @BindView(R.id.rl_rotation_angle)
    RelativeLayout rlRotationAngle;
    @BindView(R.id.img_car)
    ImageView imgCar;
    @BindView(R.id.ll_hookup)
    LinearLayout llHookup;
//    @BindView(R.id.v_hookup_line)
//    View vHookupLine;
    @BindView(R.id.img_tower_wirerope_car)
    ImageView imgWireRopeCar;

    @BindView(R.id.ll_parameter_alarm)
    LinearLayout llParameterAlarm;
    @BindView(R.id.img_parameter_alarm)
    ImageView imgAlarm;

    TowerParameterAdapter towerParameterAdapter ;
    TowerParameterAlarmAdapter towerParameterAlarmAdapter;

    private int translationX = 0;
    private int heightY = 30;

    List<String > alarms;
    private ZtrsDevice device;
    Disposable alarmTimer;
    int count;

    public TowerParameterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TowerParameterFragment newInstance() {
        TowerParameterFragment fragment = new TowerParameterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private Activity context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        device  = DeviceManager.getInstance().getZtrsDevice();
    }

    Unbinder bind;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tower_parameter, container, false);
        bind = ButterKnife.bind(this,view);
        EventBus.getDefault().register(this);
        initRecycleView();
        initView();
        initRotationAngle();
        initHookup();
        return view;
    }

    @Override
    public void onDestroyView() {
        stopAlarm();
        EventBus.getDefault().unregister(this);
        bind.unbind();
        super.onDestroyView();
    }

    @OnClick({R.id.tv_history})
    public void onClick(View view){
        if(view.getId() == R.id.tv_history){
            startActivity(new Intent(getContext(), HistoryActivity.class));
        }
    }

    private void initRecycleView(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,8);
        towerParameterAdapter = new TowerParameterAdapter();
        rvParameter.setLayoutManager(gridLayoutManager);
        rvParameter.setAdapter(towerParameterAdapter);

    }

    private void initView(){
        StaticParameterBean staticParameterBean = device.getStaticParameterBean();
        byte towerNumber = staticParameterBean.getTowerNumber();
        tvTowerSerialNumber.setText(String.format(getString(R.string.tower_serial_number),towerNumber));
        String craneType = staticParameterBean.getTowerCraneTypeStr();
        if(craneType != null) {
            tvTowerSpecificationModel.setText(String.format(getString(R.string.tower_specification_model), craneType));
        }
        byte magnification = staticParameterBean.getMagnification();
        tvTowerMagnification.setText(String.format(getString(R.string.tower_magnification),magnification));

        String hostId = device.getRegisterInfoBean().getHostId();
        tvTowerSerialMonitoringNumber.setText(String.format(getString(R.string.tower_monitoring_number),hostId));

        towerParameterAdapter.setTowerParameterBeanList(getParameters());
        towerParameterAdapter.notifyDataSetChanged();

        updateAlarm();

        RealTimeDataBean realTimeDataBean = device.getRealTimeDataBean();
        tvDirectionUp.setSelected(realTimeDataBean.getUpState() == RealTimeDataProtocol.DIRECTION_UP);
        tvDirectionDown.setSelected(realTimeDataBean.getUpState() == RealTimeDataProtocol.DIRECTION_DOWN);
        tvDirectionLeft.setSelected(realTimeDataBean.getAroundState() == RealTimeDataProtocol.DIRECTION_LEFT);
        tvDirectionRight.setSelected(realTimeDataBean.getAroundState() == RealTimeDataProtocol.DIRECTION_RIGHT);
        tvDirectionBefore.setSelected(realTimeDataBean.getAmplitudeState() == RealTimeDataProtocol.DIRECTION_BEFORE_OUT);
        tvDirectionAfter.setSelected(realTimeDataBean.getAmplitudeState() == RealTimeDataProtocol.DIRECTION_AFTER_IN);

        int balanceArmLen = staticParameterBean.getBalanceArmLen();
        tvAfterArmValue.setText(String.format("%.1f",1.0*balanceArmLen/10));
        int upWeightArmLen = staticParameterBean.getUpWeightArmLen();
        tvBigArmValue.setText(String.format("%.1f",1.0*upWeightArmLen/10));
        int towerHeight = staticParameterBean.getTowerHeight();
        tvMaxHeightValue.setText(String.format("%.1f",1.0*towerHeight/10));

        initWireRopeView();
        initRotationAngleCar();
    }

    private void initRotationAngle() {
        RealTimeDataBean realTimeDataBean = device.getRealTimeDataBean();
        int aroundValue = realTimeDataBean.getAroundAngle();
        rlRotationAngle.setRotation((float) (aroundValue * 1.0 / 10));
    }

    ObjectAnimator rotationAnimator;
    boolean isRotationRunning = false;
    private void updateRotationAngle(){
        if(isRotationRunning){
            LogUtils.LogE(TAG,"isRotationRunning return");
            return;
        }
        float startAngle = rlRotationAngle.getRotation();
        RealTimeDataBean realTimeDataBean = device.getRealTimeDataBean();
        float stopAngle = (float)(realTimeDataBean.getAroundAngle()/10.0);
        if(rotationAnimator != null ) {
            startAngle = (float) rotationAnimator.getAnimatedValue();
        }
        if(startAngle == stopAngle){
            return;
        }
        if(rotationAnimator != null ){
            rotationAnimator.cancel();
            rotationAnimator.setFloatValues(startAngle,stopAngle);
        }else {
            rotationAnimator = ObjectAnimator.ofFloat(rlRotationAngle,"rotation",startAngle,stopAngle);
            rotationAnimator.setInterpolator(new LinearInterpolator());
            rotationAnimator.setDuration(1000);
            rotationAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    LogUtils.LogE(TAG,"onAnimationStart: ");
                    isRotationRunning = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    LogUtils.LogE(TAG,"onAnimationEnd: ");
                    isRotationRunning = false;
                    updateRotationAngle();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    LogUtils.LogE(TAG,"onAnimationCancel: ");
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    LogUtils.LogE(TAG,"onAnimationRepeat: ");
                }
            });
        }
        LogUtils.LogE(TAG,"startAngle: "+startAngle);
        LogUtils.LogE(TAG,"stopAngle: "+stopAngle);
        rotationAnimator.start();
    }

    private void initRotationAngleCar() {
        RealTimeDataBean realTimeDataBean = device.getRealTimeDataBean();
        int amplitude = realTimeDataBean.getAmplitude();
        int upWeightArmLen = device.getStaticParameterBean().getUpWeightArmLen();
        float offset = 0;
        if(upWeightArmLen != 0){
            offset = (float) (1.0*amplitude/upWeightArmLen);
            if(offset <0){
                offset = 0;
            }
            if(offset>1){
                offset = 1;
            }
        }
        imgCar.setTranslationY(offset*ScaleUtils.dip2px(context,33));
    }

    private void initHookup(){
        LogUtils.LogE(TAG,"init hookup");
        RealTimeDataBean realTimeDataBean = device.getRealTimeDataBean();
        int height = realTimeDataBean.getHeight();
        int towerHeight = device.getStaticParameterBean().getTowerHeight();
        float offsetY = 0;
        float translationY = 0;
        if(towerHeight != 0){
            offsetY = (float) (1.0*(height)/towerHeight);
            if(offsetY < 0){
                offsetY = 0;
            }
            if(offsetY >1){
                offsetY = 1;
             }
//            Log.e("wch","offsetY:"+offsetY);
            translationY = -ScaleUtils.dip2px(context,(offsetY)*117);
            llHookup.setTranslationY(translationY);
        }


        int amplitude = realTimeDataBean.getAmplitude();
        int upWeightArmLen = device.getStaticParameterBean().getUpWeightArmLen();
        float offset = 0;
        if(upWeightArmLen != 0){
            offset = (float) (1.0*amplitude/upWeightArmLen);
            if(offset<0){
                offset = 0;
            }
            if(offset >1){
                offset = 1;
            }
            float tranX = offset*102;
            Log.e("wch","tranX: "+tranX);
            llHookup.setTranslationX(ScaleUtils.dip2px(context,tranX));
            imgWireRopeCar.setTranslationX(ScaleUtils.dip2px(context,tranX));
        }
    }

    private ObjectAnimator transXAnimator;
    private ObjectAnimator transYAnimator;
    private ObjectAnimator transXCarAnimator;
    boolean isTransYRunning = false;
    boolean isTransXRunning = false;
    private void hookupYAnimation(){
//        LogUtils.LogE("WCH","hookupAnimation");
        if(isTransYRunning){
            LogUtils.LogE(TAG,"isTransYRunning return");
            return;
        }
        RealTimeDataBean realTimeDataBean = device.getRealTimeDataBean();
        int height = realTimeDataBean.getHeight();
        int towerHeight = device.getStaticParameterBean().getTowerHeight();
        float offsetY = 0;
        float translationYBefore = llHookup.getTranslationY();
        float translationY = 0;
        if(towerHeight != 0){
            offsetY = (float) (1.0*(height)/towerHeight);
            if(offsetY <0 ){
                offsetY = 0;
            }
            if(offsetY >1){
                offsetY = 1;
            }
//            Log.e("wch","offsetY:"+offsetY);
            translationY = -ScaleUtils.dip2px(context,(offsetY)*117);
        }
        if(transYAnimator != null ) {
            translationYBefore = (float) transYAnimator.getAnimatedValue();
        }
        if(translationYBefore == translationY){
            return;
        }
            if (transYAnimator != null) {
                translationYBefore = (float) transYAnimator.getAnimatedValue();
                transYAnimator.cancel();
                transYAnimator.setFloatValues(translationYBefore, translationY);
            } else {
                transYAnimator = ObjectAnimator.ofFloat(llHookup, "translationY", translationYBefore, translationY);
                transYAnimator.setInterpolator(new LinearInterpolator());
                transYAnimator.setDuration(1000);
                transYAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        LogUtils.LogE(TAG, " transYAnimator onAnimationStart: ");
                        isTransYRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        LogUtils.LogE(TAG, "transYAnimator onAnimationEnd: ");
                        isTransYRunning = false;
                        hookupYAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        LogUtils.LogE(TAG, "transYAnimator onAnimationCancel: ");
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        LogUtils.LogE(TAG, "transYAnimator onAnimationRepeat: ");
                    }
                });
            }
            LogUtils.LogE(TAG, "translationYBefore:" + translationYBefore);
            LogUtils.LogE(TAG, "translationY:" + translationY);
            transYAnimator.start();
    }

    private void hookupXAnimation(){
//        LogUtils.LogE("WCH","hookupAnimation");

        RealTimeDataBean realTimeDataBean = device.getRealTimeDataBean();
        if(isTransXRunning){
            LogUtils.LogE(TAG,"isTransXRunning return");
            return;
        }
        int amplitude = realTimeDataBean.getAmplitude();
        int upWeightArmLen = device.getStaticParameterBean().getUpWeightArmLen();
        float offset = 0;
        float tranX = 0;
        if(upWeightArmLen != 0){
            offset = (float) (1.0*amplitude/upWeightArmLen);
            if(offset<0){
                offset = 0;
            }
            if(offset >1){
                offset = 1;
            }
            float tranXDp = offset*102;
            tranX = ScaleUtils.dip2px(context,tranXDp);
        }
        float translationXBefore = llHookup.getTranslationX();
        if(transXAnimator != null ) {
            translationXBefore = (float) transXAnimator.getAnimatedValue();
        }
        if(translationXBefore == tranX){
            return;
        }
        if(transXAnimator != null ){
            translationXBefore = (float)transXAnimator.getAnimatedValue();
            transXAnimator.cancel();
            transXAnimator.setFloatValues(translationXBefore,tranX);
        }else {
            transXAnimator = ObjectAnimator.ofFloat(llHookup,"translationX",translationXBefore,tranX);
            transXAnimator.setInterpolator(new LinearInterpolator());
            transXAnimator.setDuration(1000);
            transXAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    LogUtils.LogE(TAG," transXAnimator onAnimationStart: ");
                    isTransXRunning = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    LogUtils.LogE(TAG,"transXAnimator onAnimationEnd: ");
                    isTransXRunning = false;
                    hookupXAnimation();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    LogUtils.LogE(TAG,"transXAnimator onAnimationCancel: ");
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    LogUtils.LogE(TAG,"transXAnimator onAnimationRepeat: ");
                }
            });
        }
        LogUtils.LogE(TAG,"translationXBefore:"+translationXBefore);
        LogUtils.LogE(TAG,"tranX:"+tranX);
        transXAnimator.start();

        translationXBefore = imgWireRopeCar.getTranslationX();
        if(transXCarAnimator != null ){
            translationXBefore = (float)transXCarAnimator.getAnimatedValue();
            transXCarAnimator.cancel();
            transXCarAnimator.setFloatValues(translationXBefore,tranX);
        }else {
            transXCarAnimator = ObjectAnimator.ofFloat(imgWireRopeCar,"translationX",translationXBefore,tranX);
            transXCarAnimator.setInterpolator(new LinearInterpolator());
            transXCarAnimator.setDuration(1000);
        }
        transXCarAnimator.start();

    }

    private void initWireRopeView(){
        byte wireRopeDamageMagnification = device.getRealTimeDataBean().getWireRopeDamageMagnification();
        int damageHeight = device.getRealTimeDataBean().getWireRopeDamageheight();
        tvWireRopeDamagePositionValue.setText(String.format("%.1f",1.0*damageHeight*wireRopeDamageMagnification/10));

//        int wireropeCurPosition = device.getRealTimeDataBean().getHeight() * device.getRealTimeDataBean().getWireRopeDamageMagnification();
//        tvWireCurrentPositionValue.setText(String.format("%.1f",1.0*wireropeCurPosition/10));
        byte wireRopeState = device.getRealTimeDataBean().getWireRopeState();
        if(wireRopeState<=5 && wireRopeState>=0) {
            if(wireRopeState == 0){
                tvWireRopeStateValue.setTextColor(getResources().getColor(R.color.tower_parameter_normal_text_color,null));
            }else {
                tvWireRopeStateValue.setTextColor(getResources().getColor(R.color.tower_parameter_warn_text_color,null));
            }
            int wireRopeDamageValue = device.getRealTimeDataBean().getWireRopeDamageValue();
            String stateStr = RealTimeDataBean.wireRopeStateArray[wireRopeState] + " " +wireRopeDamageValue/10+"%";
            tvWireRopeStateValue.setText(stateStr);
        }else {
            tvWireRopeStateValue.setText("未知");
        }

        RealTimeDataBean realTimeDataBean = device.getRealTimeDataBean();
        int currentRatedLoad = realTimeDataBean.getCurrentRatedLoad();
        tvCurrentMaxLoadValue.setText(String.format("%.2f",1.0*currentRatedLoad/100));
    }

    private List<TowerParameterBean> getParameters(){
        List<TowerParameterBean> list = new ArrayList<>(8);

//        WireRopeDetectionReportBean wireRopeDetectionReportBean = device.getWireRopeDetectionReportBean();
//        WireRopeDetectionParametersSetBean wireRopeDetectionParametersSetBean = device.getWireRopeDetectionParametersSetBean();
//        int damageWarn = wireRopeDetectionParametersSetBean.getThreshold();
//        int damage = wireRopeDetectionReportBean.getDamageValue();
        int damage = device.getRealTimeDataBean().getWireRopeDamageValue();
        byte state = device.getRealTimeDataBean().getWireRopeState();
        TowerParameterBean wireRope = new TowerParameterBean();
        wireRope.setType(TowerParameterAdapter.ALARM_WIRE_ROPE);
        wireRope.setKey(getString(R.string.parameter_wireRope));
        wireRope.setValue(damage);
//        if(damage>damageWarn){
//            wireRope.setWarn(true);
//        }
        if(state > 0){
            wireRope.setWarn(true);
            wireRope.setAlarm(true);
        }
        list.add(wireRope);

        RealTimeDataBean realTimeDataBean = device.getRealTimeDataBean();
        int upWeightTorquePercent = realTimeDataBean.getUpWeightTorquePercent();
        TowerParameterBean maxLoad = new TowerParameterBean();
        maxLoad.setType(TowerParameterAdapter.ALARM_LOAD);
        maxLoad.setValue((int)upWeightTorquePercent);
        maxLoad.setKey(getString(R.string.parameter_max_load));
        list.add(maxLoad);

        TowerParameterBean height = new TowerParameterBean();
        height.setType(TowerParameterAdapter.ALARM_HEIGHT);
        height.setKey(getString(R.string.parameter_height));
        int heightValue = realTimeDataBean.getHeight();
        boolean warn = !realTimeDataBean.isOutputUpuPSlowLimit()
                | !realTimeDataBean.isOutputDownUpSlowLimit();
        boolean alarm = !realTimeDataBean.isOutputUpUpStopLimit()
                | !realTimeDataBean.isOutputDownUpStopLimit();
        height.setValue(heightValue);
        height.setWarn(warn);
        height.setAlarm(alarm);
        list.add(height);

        TowerParameterBean around = new TowerParameterBean();
        around.setType(TowerParameterAdapter.ALARM_AROUND);
        around.setKey(getString(R.string.parameter_around));
        int aroundValue = realTimeDataBean.getAroundAngle();
        boolean warnAround = !realTimeDataBean.isOutputLeftAroundSlowLimit()
                | !realTimeDataBean.isOutputRightAroundSlowLimit();
        boolean alarmAround =  !realTimeDataBean.isOutputLeftAroundStopLimit()
                | !realTimeDataBean.isOutputRightAroundStopLimit();
        around.setValue(aroundValue);
        around.setWarn(warnAround);
        around.setAlarm(alarmAround);
        list.add(around);

        TowerParameterBean torque = new TowerParameterBean();
        torque.setType(TowerParameterAdapter.ALARM_TORQUE);
        torque.setKey(getString(R.string.parameter_torque));
        int torqueValue = realTimeDataBean.getTorque();
        torque.setValue(torqueValue);
//        boolean alarmTorque = !realTimeDataBean.isElectronicTorqueLimitState2()
//                | !realTimeDataBean.isElectronicTorqueLimitState3()
//                | !realTimeDataBean.isElectronicTorqueLimitState4();,
        boolean alarmTorque = !realTimeDataBean.isTorqueAlarmLimit();
        torque.setAlarm(alarmTorque);
        boolean warnTorque = !realTimeDataBean.isTorqueWarnLimit();
        torque.setWarn(warnTorque);
        list.add(torque);

        TowerParameterBean load = new TowerParameterBean();
        load.setType(TowerParameterAdapter.ALARM_WEIGHT);
        load.setKey(getString(R.string.parameter_load));
        int upWeight = realTimeDataBean.getUpWeight();
        load.setValue(upWeight);
//        boolean alarmWeight = !realTimeDataBean.isElectronicWeightLimitState3()
//                | !realTimeDataBean.isElectronicWeightLimitState4();
        boolean alarmWeight = !realTimeDataBean.isWeightAlarmLimit();
        load.setAlarm(alarmWeight);
        boolean warnWeight = !realTimeDataBean.isWeightWarnLimit();
        load.setWarn(warnWeight);
        list.add(load);


        TowerParameterBean wind = new TowerParameterBean();
        wind.setType(TowerParameterAdapter.ALARM_WIND);
        wind.setKey(getString(R.string.parameter_wind));
        int windValue = realTimeDataBean.getWindLevel();
        boolean warnWind = !realTimeDataBean.isElectronicWindWarningLimit();
        boolean alarmWind = !realTimeDataBean.isElectronicWindAlarmLimit();
        wind.setValue(windValue);
        wind.setWarn(warnWind);
        wind.setAlarm(alarmWind);
        list.add(wind);

        TowerParameterBean amplitude = new TowerParameterBean();
        amplitude.setType(TowerParameterAdapter.ALARM_AMPLITUDE);
        amplitude.setKey(getString(R.string.parameter_amplitude));
        int amplitudeValue = realTimeDataBean.getAmplitude();
        boolean warnAmplitude = !realTimeDataBean.isOutputOutLuffingSlowLimit()
                | !realTimeDataBean.isOutputInLuffingSlowLimit();

        boolean alarmAmplitude = !realTimeDataBean.isOutputOutLuffingStopLimit()
                | !realTimeDataBean.isOutputInLuffingStopLimit();
        amplitude.setValue(amplitudeValue);
        amplitude.setWarn(warnAmplitude);
        amplitude.setAlarm(alarmAmplitude);
        list.add(amplitude);

        return list;
    }

    private void updateAlarm(){
        alarms = new ArrayList<>();
        List<TowerParameterBean> parameters = getParameters();
        String[] stringArray = getResources().getStringArray(R.array.alarms);
        for(int i=0;i<parameters.size();i++){
            if(parameters.get(i).isAlarm()){
                alarms.add(stringArray[i]);
            }
        }
        if(alarms.size() == 0){
            llParameterAlarm.removeAllViews();
            stopAlarm();
        }else {
            llParameterAlarm.removeAllViews();
            llParameterAlarm.setOrientation(LinearLayout.HORIZONTAL);
            llParameterAlarm.setGravity(Gravity.CENTER);
            for(int i=0;i<alarms.size();i++){
                TextView alarm = new TextView(context);
                alarm.setText(alarms.get(i));
                alarm.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
                alarm.setBackground(getResources().getDrawable(R.drawable.tower_parameter_alarm_item_bg,null));
                alarm.setTextColor(Color.parseColor("#FF3702"));
                alarm.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.weight = 1;
                layoutParams.leftMargin = ScaleUtils.dip2px(context,3);
                layoutParams.rightMargin = ScaleUtils.dip2px(context,3);
                alarm.setLayoutParams(layoutParams);
                llParameterAlarm.addView(alarm);
            }
            if(alarmTimer !=null && !alarmTimer.isDisposed()){
                return;
            }
            alarmTimer = Observable.interval(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            if((count++) %2 == 0) {
                                imgAlarm.setVisibility(View.INVISIBLE);
                            }else {
                                imgAlarm.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }

    private void stopAlarm(){
        if(alarmTimer !=null && !alarmTimer.isDisposed()){
            alarmTimer.dispose();
        }
        alarmTimer = null;
    }

    //-----------------------------------------------------------//
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaticParameter(StaticParameterMessage msg){
        LogUtils.LogI(TAG,"onStaticParameter: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onStaticParameter: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_REPORT){
            initView();
        }
        if(msg.getCmdType() == BaseMessage.TYPE_QUERY) {
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                initView();
//                testWireRope();
            }
        }else if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                initView();
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRealTimeData(RealTimeDataMessage msg){
        LogUtils.LogI(TAG,"onRealTimeData: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_REPORT
            || msg.getResult() == BaseMessage.RESULT_OK) {
            initView();
            updateRotationAngle();
            hookupYAnimation();
            hookupXAnimation();
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onWireRopeDetectionReport(WireRopeDetectionReportMessage msg){
//        LogUtils.LogI("wch","onWireRopeDetectionReport: "+msg.getResult());
//        if(msg.getResult() == BaseMessage.RESULT_REPORT) {
//            initView();
//            hookupAnimation();
//        }
//    }
    private void testWireRope(){
        device.getRealTimeDataBean().setAmplitude(0);
        device.getStaticParameterBean().setUpWeightArmLen(4000);
        device.getRealTimeDataBean().setHeight((short)1000);
        device.getRealTimeDataBean().setWireRopeDamageMagnification((byte)2);
        device.getStaticParameterBean().setTowerHeight(4000);
        final int[] i = {0};
        Observable.interval(1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if((i[0]++)%2 == 0){
//                            return;
                        }
                        int aroundAngle = device.getRealTimeDataBean().getAroundAngle();
                        device.getRealTimeDataBean().setAroundAngle(aroundAngle+50);
                        int amplitude = device.getRealTimeDataBean().getAmplitude()+50;
                        device.getRealTimeDataBean().setAmplitude(amplitude);
                        int height = device.getRealTimeDataBean().getHeight()+50;
                        device.getRealTimeDataBean().setHeight((short)height);
                        hookupYAnimation();
                        hookupXAnimation();
                        updateRotationAngle();
                        if(amplitude>4000){
                            device.getRealTimeDataBean().setAmplitude(0);
                            device.getRealTimeDataBean().setHeight((short)0);
                        }
                    }
                });
    }
}