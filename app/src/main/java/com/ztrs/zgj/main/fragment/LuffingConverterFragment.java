package com.ztrs.zgj.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.InverterDataReportBean;
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
 * Use the {@link LuffingConverterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LuffingConverterFragment extends Fragment {

    @BindView(R.id.tv_gear_value)
    TextView tvGearValue;

    @BindView(R.id.tv_running_state_value)
    TextView tvRunningStateValue;
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

    @BindView(R.id.tv_go_ahead_reduce_speed_limit_value)
    TextView tvGoAheadReduceSpeedLimitValue;
    @BindView(R.id.img_go_ahead_reduce_speed_limit_value)
    ImageView imgGoAheadReduceSpeedLimitValue;
    @BindView(R.id.tv_back_reduce_speed_limit_value)
    TextView tvBackReduceSpeedLimitValue;
    @BindView(R.id.img_back_reduce_speed_limit_value)
    ImageView imgBackReduceSpeedLimitValue;
    @BindView(R.id.tv_go_ahead_end_limit_value)
    TextView tvGoAheadEndLimitValue;
    @BindView(R.id.img_go_ahead_end_limit_value)
    ImageView imgGoAheadEndLimitValue;

    @BindView(R.id.tv_back_end_limit_value)
    TextView tvBackEndLimitValue;
    @BindView(R.id.img_back_end_limit_value)
    ImageView imgBackEndLimitValue;

    @BindView(R.id.tv_torque_100_limit_value)
    TextView tvTorque100LimitValue;
    @BindView(R.id.img_torque_100_limit_value)
    ImageView imgTorque100LimitValue;

    @BindView(R.id.tv_torque_90_limit_value)
    TextView tvTorque90LimitValue;
    @BindView(R.id.img_torque_90_limit_value)
    ImageView imgTorque90LimitValue;

    @BindView(R.id.tv_torque_80_limit_value)
    TextView tvTorque80LimitValue;
    @BindView(R.id.img_torque_80_limit_value)
    ImageView imgTorque80LimitValue;

    @BindView(R.id.tv_brake_unit_error_value)
    TextView tvBrakeUnitErrorValue;
    @BindView(R.id.img_brake_unit_error_value)
    ImageView imgBrakeUnitErrorValue;

    @BindView(R.id.tv_slow_running_value)
    TextView tvSlowRunningValue;
    @BindView(R.id.img_slow_running_value)
    ImageView imgSlowRunningValue;




    public LuffingConverterFragment() {
        // Required empty public constructor
    }

    public static LuffingConverterFragment newInstance() {
        LuffingConverterFragment fragment = new LuffingConverterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        View rootView = inflater.inflate(R.layout.fragment_luffing_converter, container, false);
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
    private void initGear(InverterDataReportBean.InverterData inverterData){
        int towerCraftCardDI16 = inverterData.getTowerCraftCardDI16();
        if((towerCraftCardDI16&0x01) == 1){
            if((towerCraftCardDI16&0x36) == 0){
                tvGearValue.setText("前1档");
            }else if((towerCraftCardDI16&0x04) == 0x04){
                tvGearValue.setText("前2档");
            }else if((towerCraftCardDI16&0x08) == 0x08){
                tvGearValue.setText("前3档");
            }else if((towerCraftCardDI16&0x10) == 0x10){
                tvGearValue.setText("前4档");
            }
        }else if((towerCraftCardDI16&0x02) == 2){
            if((towerCraftCardDI16&0x36) == 0){
                tvGearValue.setText("后1档");
            }else if((towerCraftCardDI16&0x04) == 0x04){
                tvGearValue.setText("后2档");
            }else if((towerCraftCardDI16&0x08) == 0x08){
                tvGearValue.setText("后3档");
            }else if((towerCraftCardDI16&0x10) == 0x10){
                tvGearValue.setText("后4档");
            }
        }
    }
    private void initView(){
        InverterData inverterData = DeviceManager.getInstance().getZtrsDevice()
                .getInverterDataReportBean().getAmplitudeInverterData();
        initGear(inverterData);
        int run = inverterData.getRun();
        if(run==0){
            tvRunningStateValue.setText("停止");
        }else if(run == 1){
            tvRunningStateValue.setText("运行");
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
            imgGoAheadReduceSpeedLimitValue.setImageDrawable(getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvGoAheadReduceSpeedLimitValue.setText("异常");
        }else {
            imgGoAheadReduceSpeedLimitValue.setImageDrawable(getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvGoAheadReduceSpeedLimitValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & 0x08) != 0) {
            imgBackReduceSpeedLimitValue.setImageDrawable(getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvBackReduceSpeedLimitValue.setText("异常");
        }else {
            imgBackReduceSpeedLimitValue.setImageDrawable(getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvBackReduceSpeedLimitValue.setText("正常");
        }


        if((towerCraftCardStateInstructionLSW & 0x10) != 0) {
            imgGoAheadEndLimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvGoAheadEndLimitValue.setText("异常");
        }else {
            imgGoAheadEndLimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvGoAheadEndLimitValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & 0x20) != 0) {
            imgBackEndLimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvBackEndLimitValue.setText("异常");
        }else {
            imgBackEndLimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvBackEndLimitValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & (0x01<<7)) != 0) {
            imgTorque100LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvTorque100LimitValue.setText("异常");
        }else {
            imgTorque100LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvTorque100LimitValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & (0x01<<8)) != 0) {
            imgTorque90LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvTorque90LimitValue.setText("异常");
        }else {
            imgTorque90LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvTorque90LimitValue.setText("正常");
        }

        if((towerCraftCardStateInstructionMSW & (0x01<<0)) != 0) {
            imgTorque80LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvTorque80LimitValue.setText("异常");
        }else {
            imgTorque80LimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvTorque80LimitValue.setText("正常");
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

        if((towerCraftCardStateInstructionLSW & (0x01<<15)) != 0) {
            imgSlowRunningValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvSlowRunningValue.setText("异常");
        }else {
            imgSlowRunningValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvSlowRunningValue.setText("正常");
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