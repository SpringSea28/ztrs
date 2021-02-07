package com.ztrs.zgj.main.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.InverterDataReportBean.InverterData;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.InverterDataReportMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadConverterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadConverterFragment extends Fragment {

    @BindView(R.id.tv_running_state_value)
    TextView tvRunningStateValue;
    @BindView(R.id.tv_brake_detect)
    TextView tvBreakDetect;
    @BindView(R.id.tv_brake_detect_value)
    TextView tvBreakDetectValue;
    @BindView(R.id.tv_bus_voltage_value)
    TextView tvBusVoltageValue;
    @BindView(R.id.tv_output_voltage_value)
    TextView tvOutputVoltageValue;
    @BindView(R.id.tv_output_current_value)
    TextView tvOutputCurrentValue;
    @BindView(R.id.tv_running_frequency_value)
    TextView tvRunningFrequencyValue;
    @BindView(R.id.tv_motor_speed_value)
    TextView tvMotorSpeedValue;
    @BindView(R.id.tv_temperature_value)
    TextView tvTemperatureValue;

    @BindView(R.id.tv_up_reduce_speed_limit_value)
    TextView tvUpReduceSpeedLimitValue;
    @BindView(R.id.img_up_reduce_speed_limit_value)
    ImageView imgUpReduceSpeedLimitValue;

    @BindView(R.id.tv_down_reduce_speed_limit_value)
    TextView tvDownReduceSpeedLimitValue;
    @BindView(R.id.img_down_reduce_speed_limit_value)
    ImageView imgDownReduceSpeedLimitValue;

    @BindView(R.id.tv_up_end_limit_value)
    TextView tvUpEndLimitValue;
    @BindView(R.id.img_up_end_limit_value)
    ImageView imgUpEndLimitValue;

    @BindView(R.id.tv_down_end_limit_value)
    TextView tvDownEndLimitValue;
    @BindView(R.id.img_down_end_limit_value)
    ImageView imgDownEndLimitValue;

    @BindView(R.id.tv_weight_100_limit_value)
    TextView tvWeight100LimitValue;
    @BindView(R.id.img_weight_100_limit_value)
    ImageView imgWeight100LimitValue;

    @BindView(R.id.tv_weight_90_limit_value)
    TextView tvWeight90LimitValue;
    @BindView(R.id.img_weight_90_limit_value)
    ImageView imgWeight90LimitValue;

    @BindView(R.id.tv_weight_50_limit_value)
    TextView tvWeight50LimitValue;
    @BindView(R.id.img_weight_50_limit_value)
    ImageView imgWeight50LimitValue;

    @BindView(R.id.tv_weight_25_limit_value)
    TextView tvWeight25LimitValue;
    @BindView(R.id.img_weight_25_limit_value)
    ImageView imgWeight25LimitValue;

    @BindView(R.id.tv_brake_unit_error_value)
    TextView tvBrakeUnitErrorValue;
    @BindView(R.id.img_brake_unit_error_value)
    ImageView imgBrakeUnitErrorValue;

    @BindView(R.id.tv_brake_failure_value)
    TextView tvBrakeFailureValue;
    @BindView(R.id.img_brake_failure_value)
    ImageView imgBrakeFailureValue;

    @BindView(R.id.tv_slow_running_value)
    TextView tvSlowRunningValue;
    @BindView(R.id.img_slow_running_value)
    ImageView imgSlowRunningValue;

    @BindView(R.id.tv_brake_close_value)
    TextView tvBreakCloseValue;
    @BindView(R.id.img_brake_close_value)
    ImageView imgBreakCloseValue;

    @BindView(R.id.tv_limit_switch_shield_value)
    TextView tvLimitSwitchShieldValue;
    @BindView(R.id.img_limit_switch_shield_value)
    ImageView imgLimitSwitchShieldValue;



    public UploadConverterFragment() {
        // Required empty public constructor
    }

    public static UploadConverterFragment newInstance() {
        UploadConverterFragment fragment = new UploadConverterFragment();
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
    }

    Unbinder bind;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_upload_converter, container, false);
        bind = ButterKnife.bind(this,rootView);
        EventBus.getDefault().register(this);
        initView();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        bind.unbind();
        super.onDestroyView();
    }

    private void initView(){
        InverterData inverterData = DeviceManager.getInstance().getZtrsDevice().getInverterDataReportBean().getUpInverterData();
        int run = inverterData.getRun();
        if(run==0){
            tvRunningStateValue.setText("停止");
        }else if(run == 1){
            tvRunningStateValue.setText("运行");
        }
        int liftingTorqueUnderBreak = inverterData.getLiftingTorqueUnderBreak();
        if(liftingTorqueUnderBreak == 0){
            tvBreakDetectValue.setSelected(false);
            tvBreakDetectValue.setText("正常");
            tvBreakDetectValue.setTextColor(
                    getResources().getColor(R.color.tower_primary_normal_text_color_blue,null));
            tvBreakDetect.setTextColor(
                    getResources().getColor(R.color.tower_primary_text_color,null));
        }else {
            
            tvBreakDetectValue.setTextColor(
                    getResources().getColor(R.color.tower_parameter_converter_error_text_color,null));
            tvBreakDetectValue.setSelected(true);
            tvBreakDetectValue.setText("力矩异常");
            tvBreakDetect.setTextColor(
                    getResources().getColor(R.color.tower_parameter_converter_error_text_color,null));
        }

        int frequency = inverterData.getFrequency();
        tvRunningFrequencyValue.setText(String.format("%.1fHZ",1.0*frequency/10));
        int busVoltage = inverterData.getBusVoltage();
        tvBusVoltageValue.setText(String.format("%.1fV",1.0*busVoltage/10));
        int outputVoltage = inverterData.getOutputVoltage();
        tvOutputVoltageValue.setText(String.format("%.1fV",1.0*outputVoltage/10));
        int outputCurrent = inverterData.getOutputCurrent();
        tvOutputCurrentValue.setText(String.format("%.1fA",1.0*outputCurrent/10));
        int rotorSpeed = inverterData.getRotorSpeed();
        tvMotorSpeedValue.setText(rotorSpeed+"RPM");
        int maximumTemperature = inverterData.getMaximumTemperature();
        tvTemperatureValue.setText(String.format("%.1f℃",1.0*maximumTemperature/10));

        int towerCraftCardStateInstructionMSW = inverterData.getTowerCraftCardStateInstructionMSW();
        int towerCraftCardStateInstructionLSW = inverterData.getTowerCraftCardStateInstructionLSW();
        if((towerCraftCardStateInstructionLSW & 0x04) != 0) {
            imgUpReduceSpeedLimitValue.setImageDrawable(getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvUpReduceSpeedLimitValue.setText("异常");
        }else {
            imgUpReduceSpeedLimitValue.setImageDrawable(getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvUpReduceSpeedLimitValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & 0x08) != 0) {
            imgDownReduceSpeedLimitValue.setImageDrawable(getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvDownReduceSpeedLimitValue.setText("异常");
        }else {
            imgDownReduceSpeedLimitValue.setImageDrawable(getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvDownReduceSpeedLimitValue.setText("正常");
        }


        if((towerCraftCardStateInstructionLSW & 0x10) != 0) {
            imgUpEndLimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvUpEndLimitValue.setText("异常");
        }else {
            imgUpEndLimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvUpEndLimitValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & 0x20) != 0) {
            imgDownEndLimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvDownEndLimitValue.setText("异常");
        }else {
            imgDownEndLimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvDownEndLimitValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & (0x01<<9)) != 0) {
            imgWeight100LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvWeight100LimitValue.setText("异常");
        }else {
            imgWeight100LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvWeight100LimitValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & (0x01<<10)) != 0) {
            imgWeight90LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvWeight90LimitValue.setText("异常");
        }else {
            imgWeight90LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvWeight90LimitValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & (0x01<<11)) != 0) {
            imgWeight50LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvWeight50LimitValue.setText("异常");
        }else {
            imgWeight50LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvWeight50LimitValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & (0x01<<12)) != 0) {
            imgWeight25LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvWeight25LimitValue.setText("异常");
        }else {
            imgWeight25LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvWeight25LimitValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & (0x01<<13)) != 0) {
            imgBrakeUnitErrorValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvBrakeUnitErrorValue.setText("异常");
        }else {
            imgBrakeUnitErrorValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvBrakeUnitErrorValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & (0x01<<14)) != 0) {
            imgBrakeFailureValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvBrakeFailureValue.setText("异常");
        }else {
            imgBrakeFailureValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvBrakeFailureValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & (0x01<<15)) != 0) {
            imgSlowRunningValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvSlowRunningValue.setText("异常");
        }else {
            imgSlowRunningValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvSlowRunningValue.setText("正常");
        }

        if((towerCraftCardStateInstructionMSW & (0x01<<(17-16))) != 0) {
            imgBreakCloseValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvBreakCloseValue.setText("异常");
        }else {
            imgBreakCloseValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvBreakCloseValue.setText("正常");
        }

        if((towerCraftCardStateInstructionMSW & (0x01<<(18-16))) != 0) {
            imgLimitSwitchShieldValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvLimitSwitchShieldValue.setText("异常");
        }else {
            imgLimitSwitchShieldValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvLimitSwitchShieldValue.setText("正常");
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInverterDataReport(InverterDataReportMessage msg){
        LogUtils.LogI("wch","onInverterDataReport: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_REPORT) {
            initView();
        }
    }
}