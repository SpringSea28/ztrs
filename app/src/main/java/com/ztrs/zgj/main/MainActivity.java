package com.ztrs.zgj.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kongqw.serialportlibrary.Device;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.bean.StaticParameterBean;
import com.ztrs.zgj.device.eventbus.AmplitudeCalibrationMessage;
import com.ztrs.zgj.device.eventbus.AroundCalibrationMessage;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.EmergencyCallMessage;
import com.ztrs.zgj.device.eventbus.HeightCalibrationMessage;
import com.ztrs.zgj.device.eventbus.RealTimeDataMessage;
import com.ztrs.zgj.device.eventbus.RegionalRestrictionMessage;
import com.ztrs.zgj.device.eventbus.RelayConfigurationMessage;
import com.ztrs.zgj.device.eventbus.RelayOutputControlMessage;
import com.ztrs.zgj.device.eventbus.SensorRealtimeDataMessage;
import com.ztrs.zgj.device.eventbus.StaticParameterMessage;
import com.ztrs.zgj.device.eventbus.SwitchMachineMessage;
import com.ztrs.zgj.device.eventbus.UnlockCarMessage;
import com.ztrs.zgj.device.eventbus.WeightCalibrationMessage;
import com.ztrs.zgj.device.eventbus.WireRopeDetectionParametersSetMessage;
import com.ztrs.zgj.device.eventbus.WorkCycleDataMessage;
import com.ztrs.zgj.main.fragment.AroundConverterFragment;
import com.ztrs.zgj.main.fragment.LuffingConverterFragment;
import com.ztrs.zgj.main.fragment.TowerParameterFragment;
import com.ztrs.zgj.main.fragment.UploadConverterFragment;
import com.ztrs.zgj.main.msg.SerialPortOpenResultMsg;
import com.ztrs.zgj.setting.OutputActivity;
import com.ztrs.zgj.setting.SettingActivity;
import com.ztrs.zgj.setting.SoftwareUpdateActivity;
import com.ztrs.zgj.setting.dialog.UpdateDialog;
import com.ztrs.zgj.setting.viewModel.AppUpdateViewModel;
import com.ztrs.zgj.setting.viewModel.VersionModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity  {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.fl_tower_parameter)
    FrameLayout flTowerParameter;

    @BindView(R.id.tv_upload_converter)
    TextView tvUploadConverter;
    @BindView(R.id.tv_luffing_converter)
    TextView tvLuffingConverter;
    @BindView(R.id.tv_around_converter)
    TextView tvAroundConverter;

    @BindView(R.id.tv_emergency_call)
    TextView tvCall;

    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;

    private static final int ON_SELECT_UPLOAD = 0;
    private static final int ON_SELECT_LUFFING = 1;
    private static final int ON_SELECT_AROUND = 2;
    private int onSelect;
    UploadConverterFragment uploadConverterFragment;
    LuffingConverterFragment luffingConverterFragment;
    AroundConverterFragment aroundConverterFragment;

    AppUpdateViewModel versionModel;
    UpdateDialog updateDialog;

    private Device device;

    Unbinder bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        addTowerParameterFragment();
        switchConverterTab(onSelect);
        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test test = new Test();
//                byte[] bytes =new byte[10];
//                int i= bytes[-4];
//                Log.e("wch","testUnLockCarCmd");
//                test.testUnLockCarCmd();
//                test.testSwitchMachineCmd();
//                test.testOnReceiveSwitchMachine();
//                test.testSectorRegionalRestriction();
//                test.testPolarRegionalRestriction();
//                test.testOrthogonalRegionalRestriction();
//                test.testQueryOrthogonalRegionalRestriction();
//                test.testQueryPolarRegionalRestriction();
//                test.testQuerySectorRegionalRestriction();

//                test.testStaticParameter();
//                test.testQueryStaticParameter();
//                test.testOnReceiveRealtimedata();
//                test.testQueryWorkCycleData();
//                test.setRelayConfiguration();
//                test.setRelayOutputControl();
//                test.queryRelayOutputControl();
//                test.emergencyCall();
//                test.querySensorRealtimeData();
//                test.onReceiveSensorRealtimeData();
//                test.heightCalibration();
//                test.queryHeightCalibration();
//                test.amplitudeCalibration();
//                test.queryAmplitudeCalibration();
//                test.aroundCalibration();
//                test.queryAroundCalibration();
//                test.weightCalibration();
//                test.queryWeightCalibration();
//                test.onReceiveInverterData();
//                test.queryWireRopeDetectionParameters();
//                test.onReceiveWireRopeDetectionReport();
//                test.testPackage();
//                test.testOnReceiveRegisterInfo();
                test.testOnReceiveRealtimedata();
//                test.testOnReceiveTorqueCurve();
//                test.testQueryStaticParameter();
//                test.testQueryOrthogonalRegionalRestriction();
//                test.testOnReceiveRegisterInfo();
            }
        });
        EventBus.getDefault().register(this);
        initView();
        display();
        checkUpdate();
    }

    private void checkUpdate(){
        versionModel = new ViewModelProvider(this).get(AppUpdateViewModel.class);
        LiveData<String> curVersion = versionModel.getCurVersion();
        curVersion.observe(this, s -> { });
        versionModel.initVersion(this);
        LiveData<VersionModel.UpdateState> updateState = versionModel.getUpdateState();
        updateState.observe(this, updateState1 -> onUpdateStateChange(updateState1));
        String hostId = DeviceManager.getInstance().getZtrsDevice().getRegisterInfoBean().getHostId();
        Log.e(TAG,"hostId:"+hostId);
        versionModel.checkVersion(hostId);
    }

    private void onUpdateStateChange(VersionModel.UpdateState updateState){
        switch (updateState){
            case CHECK_SUCCESS_CAN_UPDATE:
                if(updateDialog == null || !updateDialog.isShowing()) {
                    updateDialog = new UpdateDialog(this);
                    updateDialog.initText("检测新版本: " + versionModel.getRemoteVersion()+"\n"+"确认升级？");
                    updateDialog.initButton(true,true);
                    updateDialog.setOnUserClick(() -> {
                        versionModel.downLoadApk(this);
                    });
                    updateDialog.show();
                }
                break;
            case DOWNLOADING:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("新版本下载中...");
                    updateDialog.hideButton();
                    updateDialog.show();
                }
                break;
            case DOWNLOAD_SUCCESS:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("新版本下载成功，确认安装？");
                    updateDialog.showButton();
                    updateDialog.setOnUserClick(() -> {
                        updateDialog.dismiss();
                        requestInstallPermission(versionModel.getApkFile(this));
                    });
                    updateDialog.show();
                }
                break;
            case DOWNLOAD_FAIL:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("抱歉，新版本下载失败");
                    updateDialog.showConfirm();
                    updateDialog.setOnUserClick(() -> updateDialog.dismiss());
                    updateDialog.show();
                }
                break;
        }
    }

    private void requestInstallPermission(File file){
        AndPermission.with(this)
                .install()
                .onGranted(new Action<File>() {
                    @Override
                    public void onAction(File data) {
                        installApk(MainActivity.this,file);
                    }
                })
                .onDenied(new Action<File>() {
                    @Override
                    public void onAction(File data) {

                    }
                })
                .start();
    }

    private  void installApk(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri apkUri = FileProvider.getUriForFile(context, "com.ztrs.zgj", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());
    }


    private void initView(){

    }

    private void display(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Log.e("wch","density: "+displayMetrics.density);
        Log.e("wch","densityDpi: "+displayMetrics.densityDpi);
        Log.e("wch","scaledDensity: "+displayMetrics.scaledDensity);
        Log.e("wch","widthPixels: "+displayMetrics.widthPixels);
        Log.e("wch","heightPixels: "+displayMetrics.heightPixels);
        Log.e("wch","xdpi: "+displayMetrics.xdpi);
        Log.e("wch","ydpi: "+displayMetrics.ydpi);
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG,"onDestroy");
        DeviceManager.getInstance().closeOpenSerial();
        EventBus.getDefault().unregister(this);
        LogCatHelper.getInstance(this,null).stop();
        bind.unbind();
        super.onDestroy();
    }

    private void addTowerParameterFragment(){
        TowerParameterFragment towerParameterFragment = TowerParameterFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_tower_parameter,towerParameterFragment);
        fragmentTransaction.commit();
    }

    @OnClick({R.id.tv_upload_converter,R.id.tv_luffing_converter,R.id.tv_around_converter,
        R.id.rl_setting,R.id.rl_output})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_upload_converter:
                switchConverterTab(ON_SELECT_UPLOAD);
                break;
            case R.id.tv_luffing_converter:
                switchConverterTab(ON_SELECT_LUFFING);
                break;
            case R.id.tv_around_converter:
                switchConverterTab(ON_SELECT_AROUND);
                break;
            case R.id.rl_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.rl_output:
                startActivity(new Intent(MainActivity.this, OutputActivity.class));
                break;
        }
    }

    private void switchConverterTab(int type){
        tvUploadConverter.setSelected(false);
        tvLuffingConverter.setSelected(false);
        tvAroundConverter.setSelected(false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(type == ON_SELECT_UPLOAD){
            if(uploadConverterFragment == null){
                uploadConverterFragment = UploadConverterFragment.newInstance();
            }
            fragmentTransaction.replace(R.id.fl_frequency_converter,uploadConverterFragment);
            tvUploadConverter.setSelected(true);

        }else if(type == ON_SELECT_LUFFING){
            if(luffingConverterFragment == null){
                luffingConverterFragment = LuffingConverterFragment.newInstance();
            }
            fragmentTransaction.replace(R.id.fl_frequency_converter,luffingConverterFragment);
            tvLuffingConverter.setSelected(true);
        }else if(type == ON_SELECT_AROUND){
            if(aroundConverterFragment == null){
                aroundConverterFragment = AroundConverterFragment.newInstance();
            }
            fragmentTransaction.replace(R.id.fl_frequency_converter,aroundConverterFragment);
            tvAroundConverter.setSelected(true);
        }
        fragmentTransaction.commit();
    }



    //----------------------subscribe------------------------------//
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSerialOpen(SerialPortOpenResultMsg msg){
        LogUtils.LogI(TAG,"onSerialOpen: "+msg.isSuccess());
        if(msg.isSuccess()) {
           DeviceManager.getInstance().queryStaticParameter();
        }else {
            Toast.makeText(this, "串口打开失败", Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaticParameter(StaticParameterMessage msg){
        LogUtils.LogI(TAG,"onStaticParameter: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onStaticParameter: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_QUERY) {
            if (msg.getResult() == BaseMessage.RESULT_OK) {

            } else {
                Toast.makeText(this, "查询静态参数查询命令发送失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnlock(UnlockCarMessage unlockCarMessage){
        LogUtils.LogI("wch","unlockmessage: "+unlockCarMessage.getResult());
        if(unlockCarMessage.getResult() == BaseMessage.RESULT_OK) {
            Toast.makeText(this, "开关锁命令发送成功", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "开关锁命令发送失败", Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchMachine(SwitchMachineMessage switchMachineMessage){
        LogUtils.LogI("wch","switchMachineMessage: "+switchMachineMessage.getResult());
        if(switchMachineMessage.getResult() == BaseMessage.RESULT_OK) {
            Toast.makeText(this, "开关机命令发送成功", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "开关机命令发送失败", Toast.LENGTH_LONG).show();
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRegionalRestriction(RegionalRestrictionMessage msg){
//        LogUtils.LogI("wch","onRegionalRestriction: "+msg.getResult());
//        if(msg.getResult() == BaseMessage.RESULT_OK) {
//            Toast.makeText(this, "区域限制命令发送成功", Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(this, "区域限制命令发送失败", Toast.LENGTH_LONG).show();
//        }
//    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRealTimeData(RealTimeDataMessage msg){
//        LogUtils.LogI("wch","onRealTimeData: "+msg.getResult());
//        if(msg.getResult() == BaseMessage.RESULT_OK) {
//            Toast.makeText(this, "实时数据命令发送成功", Toast.LENGTH_LONG).show();
//        }else if(msg.getResult() == BaseMessage.RESULT_FAIL){
//            Toast.makeText(this, "实时数据命令发送失败", Toast.LENGTH_LONG).show();
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWorkCycleData(WorkCycleDataMessage msg){
        LogUtils.LogI("wch","onWorkCycleData: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_OK) {
            Toast.makeText(this, "获取循环数据命令发送成功", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "获取循环数据命令发送失败", Toast.LENGTH_LONG).show();
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRelayConfiguration(RelayConfigurationMessage msg){
//        LogUtils.LogI("wch","onRelayConfiguration: "+msg.getResult());
//        if(msg.getResult() == BaseMessage.RESULT_OK) {
//            Toast.makeText(this, "配置继电器命令发送成功", Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(this, "配置继电器命令发送失败", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRelayOutputControl(RelayOutputControlMessage msg){
//        LogUtils.LogI("wch","onRelayOutputControl: "+msg.getResult());
//        if(msg.getResult() == BaseMessage.RESULT_OK) {
//            Toast.makeText(this, "继电器输出控制命令发送成功", Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(this, "继电器输出控制命令发送失败", Toast.LENGTH_LONG).show();
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEmergencyCall(EmergencyCallMessage msg){
        LogUtils.LogI("wch","onEmergencyCall: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_OK) {
            Toast.makeText(this, "紧急呼叫命令发送成功", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "紧急呼叫命令发送失败", Toast.LENGTH_LONG).show();
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onSensorRealtimeData(SensorRealtimeDataMessage msg){
//        LogUtils.LogI("wch","onSensorRealtimeData: "+msg.getResult());
//        if(msg.getResult() == BaseMessage.RESULT_OK) {
//            Toast.makeText(this, "传感器实时数据采集命令发送成功", Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(this, "传感器实时数据采集命令发送失败", Toast.LENGTH_LONG).show();
//        }
//    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onHeightCalibration(HeightCalibrationMessage msg){
//        LogUtils.LogI("wch","onHeightCalibration: "+msg.getResult());
//        if(msg.getResult() == BaseMessage.RESULT_OK) {
//            Toast.makeText(this, "高度标定采集命令发送成功", Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(this, "高度标定采集命令发送失败", Toast.LENGTH_LONG).show();
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onAmplitudeCalibration(AmplitudeCalibrationMessage msg){
//        LogUtils.LogI("wch","onAmplitudeCalibration: "+msg.getResult());
//        if(msg.getResult() == BaseMessage.RESULT_OK) {
//            Toast.makeText(this, "幅度标定采集命令发送成功", Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(this, "幅度标定采集命令发送失败", Toast.LENGTH_LONG).show();
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onAroundCalibration(AroundCalibrationMessage msg){
//        LogUtils.LogI("wch","onAroundCalibration: "+msg.getResult());
//        if(msg.getResult() == BaseMessage.RESULT_OK) {
//            Toast.makeText(this, "回转标定采集命令发送成功", Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(this, "回转标定采集命令发送失败", Toast.LENGTH_LONG).show();
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onWeightCalibration(WeightCalibrationMessage msg){
//        LogUtils.LogI("wch","onWeightCalibration: "+msg.getResult());
//        if(msg.getResult() == BaseMessage.RESULT_OK) {
//            Toast.makeText(this, "载重标定采集命令发送成功", Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(this, "载重标定采集命令发送失败", Toast.LENGTH_LONG).show();
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onWireRopeDetectionParametersSet(WireRopeDetectionParametersSetMessage msg){
//        LogUtils.LogI("wch","onWireRopeDetectionParametersSet: "+msg.getResult());
//        if(msg.getResult() == BaseMessage.RESULT_OK) {
//            Toast.makeText(this, "钢丝绳检测参数设置命令发送成功", Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(this, "钢丝绳检测参数设置命令发送失败", Toast.LENGTH_LONG).show();
//        }
//    }
}