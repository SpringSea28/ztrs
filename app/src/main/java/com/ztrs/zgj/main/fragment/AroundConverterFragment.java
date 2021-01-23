package com.ztrs.zgj.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @BindView(R.id.tv_right_end_limit_value)
    TextView tvRightEndLimitValue;

    @BindView(R.id.tv_brake_unit_error_value)
    TextView tvBrakeUnitErrorValue;

    @BindView(R.id.tv_slow_running_value)
    TextView tvSlowRunningValue;


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

    private void initView(){
        InverterDataReportBean.InverterData inverterData = DeviceManager.getInstance().getZtrsDevice()
                .getInverterDataReportBean().getAmplitudeInverterData();

        tvRunningStateValue.setText("未知");

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

        tvLeftEndLimitValue.setText("未知");
        tvRightEndLimitValue.setText("未知");
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