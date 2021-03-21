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
 * Use the {@link AroundConverterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AroundConverterFragment extends Fragment {

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

    @BindView(R.id.tv_left_end_limit_value)
    TextView tvLeftEndLimitValue;
    @BindView(R.id.img_left_end_limit_value)
    ImageView imgLeftEndLimitValue;

    @BindView(R.id.tv_right_end_limit_value)
    TextView tvRightEndLimitValue;
    @BindView(R.id.img_right_end_limit_value)
    ImageView imgRightEndLimitValue;

    @BindView(R.id.tv_brake_unit_error_value)
    TextView tvBrakeUnitErrorValue;
    @BindView(R.id.img_brake_unit_error_value)
    ImageView imgBrakeUnitErrorValue;

    @BindView(R.id.tv_slow_running_value)
    TextView tvSlowRunningValue;
    @BindView(R.id.img_slow_running_value)
    ImageView imgSlowRunningValue;


    public AroundConverterFragment() {
        // Required empty public constructor
    }

    public static AroundConverterFragment newInstance() {
        AroundConverterFragment fragment = new AroundConverterFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_around_converter, container, false);
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
                tvGearValue.setText("左1档");
            }else if((towerCraftCardDI16&0x04) == 0x04){
                tvGearValue.setText("左2档");
            }else if((towerCraftCardDI16&0x08) == 0x08){
                tvGearValue.setText("左3档");
            }else if((towerCraftCardDI16&0x10) == 0x10){
                tvGearValue.setText("左4档");
            }
        }else if((towerCraftCardDI16&0x02) == 2){
            if((towerCraftCardDI16&0x36) == 0){
                tvGearValue.setText("右1档");
            }else if((towerCraftCardDI16&0x04) == 0x04){
                tvGearValue.setText("右2档");
            }else if((towerCraftCardDI16&0x08) == 0x08){
                tvGearValue.setText("右3档");
            }else if((towerCraftCardDI16&0x10) == 0x10){
                tvGearValue.setText("右4档");
            }
        }
    }
    private void initView(){
        InverterDataReportBean.InverterData inverterData = DeviceManager.getInstance().getZtrsDevice()
                .getInverterDataReportBean().getAroundInverterData();
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

        if((towerCraftCardStateInstructionLSW & 0x10) != 0) {
            imgLeftEndLimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvLeftEndLimitValue.setText("异常");
        }else {
            imgLeftEndLimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvLeftEndLimitValue.setText("正常");
        }

        if((towerCraftCardStateInstructionLSW & 0x20) != 0) {
            imgRightEndLimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_error_bg,null));
            tvRightEndLimitValue.setText("异常");
        }else {
            imgRightEndLimitValue.setImageDrawable(
                    getResources().getDrawable(R.drawable.tower_converter_normal_bg,null));
            tvRightEndLimitValue.setText("正常");
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
        tvBrakeUnitErrorValue.setText("未知");
        tvSlowRunningValue.setText("未知");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInverterDataReport(InverterDataReportMessage msg){
        LogUtils.LogI("wch","onInverterDataReport: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_REPORT) {
            initView();
        }
    }
}