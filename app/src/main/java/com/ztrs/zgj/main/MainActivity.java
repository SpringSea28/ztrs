package com.ztrs.zgj.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongqw.serialportlibrary.Device;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.bean.RealTimeDataBean;
import com.ztrs.zgj.device.bean.UnLockCarBean;
import com.ztrs.zgj.device.eventbus.AnnouncementMessage;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.EmergencyCallMessage;
import com.ztrs.zgj.device.eventbus.RealTimeDataMessage;
import com.ztrs.zgj.device.eventbus.StaticParameterMessage;
import com.ztrs.zgj.device.eventbus.SwitchMachineMessage;
import com.ztrs.zgj.device.eventbus.UnlockCarMessage;
import com.ztrs.zgj.device.eventbus.WorkCycleDataMessage;
import com.ztrs.zgj.main.adapter.VideoAdapter;
import com.ztrs.zgj.main.dialog.AnnouncementDialog;
import com.ztrs.zgj.main.dialog.SettingSecretDialog;
import com.ztrs.zgj.main.dialog.UnlockCarDialog;
import com.ztrs.zgj.main.dialog.VideoInputDialog;
import com.ztrs.zgj.main.fragment.AroundConverterFragment;
import com.ztrs.zgj.main.fragment.LuffingConverterFragment;
import com.ztrs.zgj.main.fragment.TowerParameterFragment;
import com.ztrs.zgj.main.fragment.UploadConverterFragment;
import com.ztrs.zgj.main.msg.SerialPortOpenResultMsg;
import com.ztrs.zgj.setting.SettingActivity;
import com.ztrs.zgj.setting.WebActivity;
import com.ztrs.zgj.setting.bean.AddressBean;
import com.ztrs.zgj.setting.dialog.UpdateDialog;
import com.ztrs.zgj.setting.eventbus.SettingEventBus;
import com.ztrs.zgj.setting.viewModel.AppUpdateViewModel;
import com.ztrs.zgj.setting.viewModel.VersionModel;
import com.ztrs.zgj.utils.ScaleUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity  {

    private static final String TAG = MainActivity.class.getSimpleName();


    @BindView(R.id.tv_upload_converter)
    TextView tvUploadConverter;
    @BindView(R.id.tv_luffing_converter)
    TextView tvLuffingConverter;
    @BindView(R.id.tv_around_converter)
    TextView tvAroundConverter;
    @BindView(R.id.tv_real_state)
    TextView tvRealState;

    @BindView(R.id.tv_emergency_call)
    TextView tvCall;

    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;

    @BindView(R.id.img_signal_1)
    ImageView imgNetwork;

    @BindView(R.id.img_signal_3)
    ImageView imgDeviceNetwork;


    @BindView(R.id.tv_tower_market)
    TextView tvTowerMarket;

    @BindView(R.id.video_layout)
    VLCVideoLayout videoLayout;

    @BindView(R.id.rl_video)
    RelativeLayout rlVideo;
    @BindView(R.id.rl_video_bg)
    RelativeLayout rlVideoBg;

    @BindView(R.id.btn_play)
    Button btnPlay;
    @BindView(R.id.tv_v1)
    TextView tvV1;
    @BindView(R.id.tv_v2)
    TextView tvV2;
    @BindView(R.id.tv_v3)
    TextView tvV3;
    @BindView(R.id.rv_video)
    RecyclerView rvVideo;
    VideoAdapter videoAdapter;

    @BindView(R.id.img_volume)
    ImageView imgVol;

    @BindView(R.id.img_announcement_unread)
    ImageView imgAnnouncementUnRead;

    @BindView(R.id.tv_attendance_onwork)
    TextView tvOnwork;
    @BindView(R.id.tv_attendance_offwork)
    TextView tvOffwork;
    @BindView(R.id.tv_attendance_workhistory)
    TextView tvWorkhistory;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_start_work_time)
    TextView tvStartWorkTime;
    @BindView(R.id.img_header)
    ImageView imgHeader;

    private static final int ON_SELECT_UPLOAD = 0;
    private static final int ON_SELECT_LUFFING = 1;
    private static final int ON_SELECT_AROUND = 2;
    private static final int ON_SELECT_REAL_STATE = 3;
    private int onSelect = ON_SELECT_REAL_STATE;
    UploadConverterFragment uploadConverterFragment;
    LuffingConverterFragment luffingConverterFragment;
    AroundConverterFragment aroundConverterFragment;
    TowerParameterFragment towerParameterFragment;

    AppUpdateViewModel versionModel;
    UpdateDialog updateDialog;

    private Device device;

    public static final int PLAYER_STATE_IDLE = 0;
    public static final int PLAYER_STATE_PREPARING = 1;
    public static final int PLAYER_STATE_PREPARED = 2;
    public static final int PLAYER_STATE_PLAYING = 3;
    public static final int PLAYER_STATE_PAUSE = 4;
    public static final int PLAYER_STATE_STOP = 5;
    public static final int PLAYER_STATE_ERROR = 6;

    private int playerState;
    private boolean isPauseByUser;

    private String url = "";
//            "rtsp://admin:ztrs12345@192.168.0.8:554/h264/ch1/main/av_stream";
    private String videoTitle;

    private boolean onWork = true;

    Unbinder bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        switchConverterTab(onSelect);
        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test test = new Test();
                DeviceManager.getInstance().emergencyCall();
                test.testOnReceiveRealtimedata();
            }
        });
        EventBus.getDefault().register(this);
        initView();
        initAnnouncementUnread();
        display();
        checkUpdate();
        checkNetWorkState();
        initVideoTab();
        initVlc();
        initAttendance();
        SharedPreferences sp = getSharedPreferences("SystemSetting",MODE_PRIVATE);
        volume = sp.getInt("volume",50);
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
                    updateDialog.startAnimation();
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
                    updateDialog.clearAnimation();
                }
                break;
            case DOWNLOAD_FAIL:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("抱歉，新版本下载失败");
                    updateDialog.showConfirm();
                    updateDialog.setOnUserClick(() -> updateDialog.dismiss());
                    updateDialog.show();
                    updateDialog.clearAnimation();
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
        RealTimeDataBean realTimeDataBean = DeviceManager.getInstance().getZtrsDevice().getRealTimeDataBean();
        boolean connectRemote = realTimeDataBean.isConnectStateByDeviceWithRemoteCenter();
        boolean network = realTimeDataBean.isNetwork();
        if(!network && !connectRemote){
            imgDeviceNetwork.setImageDrawable(getDrawable(R.mipmap.xinhao_2));
        }else if(!network){
            imgDeviceNetwork.setImageDrawable(getDrawable(R.mipmap.network_unable2));
        }else {
            imgDeviceNetwork.setImageDrawable(getDrawable(R.mipmap.network_wrong));
        }
    }

    private void initAnnouncementUnread(){
        boolean read = DeviceManager.getInstance().getZtrsDevice().getAnnounecmentBean().isRead();
        if(read){
            imgAnnouncementUnRead.setVisibility(View.GONE);
        }else{
            imgAnnouncementUnRead.setVisibility(View.VISIBLE);
        }
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
    protected void onResume() {
        super.onResume();
        if(!isPauseByUser && playerState == PLAYER_STATE_STOP){
            startPlay();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(playerState != PLAYER_STATE_IDLE) {
            stopPlay();
            rlVideoBg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG,"onDestroy");
        stopPlay();
        releasePlay();
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.unregisterNetworkCallback(networkCallback);
        DeviceManager.getInstance().closeOpenSerial();
        EventBus.getDefault().unregister(this);
        LogCatHelper.getInstance(this,null).stop();
        bind.unbind();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==RESULT_OK){
            if(requestCode == 1){
                Toast.makeText(this,"身份采集成功",Toast.LENGTH_SHORT).show();
            }else if(requestCode == 2){
                int code = data.getIntExtra("requestCode",-1);
                String name = data.getStringExtra("name");
                String idCard = data.getStringExtra("idCard");
                String fileName = data.getStringExtra("fileName");
                tvName.setText(name);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd HH:mm:ss");
                String time = dateFormat.format(Calendar.getInstance().getTime());
                tvStartWorkTime.setText(time);
                onWork = false;
                initAttendance();
                Toast.makeText(this,"上班打卡成功",Toast.LENGTH_SHORT).show();
//                String ROOT_PATH = getFilesDir().getAbsolutePath();
//                ROOT_PATH = ROOT_PATH.replace("com.ztrs.zgj","com.arcsoft.arcfacedemo");
//                String SAVE_IMG_DIR = "register" + File.separator + "imgs";
//                String IMG_SUFFIX = ".jpg";
//                File imgFile = new File(ROOT_PATH + File.separator
//                        + SAVE_IMG_DIR + File.separator
//                        + fileName + IMG_SUFFIX);
//                Log.e("wch","imgFile:"+imgFile);
//                Glide.with(this)
//                        .load(imgFile)
//                        .error(R.mipmap.logo_2)
//                        .into(imgHeader);
            }else if(requestCode == 3){
                int code = data.getIntExtra("requestCode",-1);
                String name = data.getStringExtra("name");
                String idCard = data.getStringExtra("idCard");
                String fileName = data.getStringExtra("fileName");
                tvName.setText(name);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd HH:mm:ss");
                String time = dateFormat.format(Calendar.getInstance().getTime());
                tvStartWorkTime.setText(time);
                onWork = true;
                initAttendance();
                Toast.makeText(this,"下班打卡成功",Toast.LENGTH_SHORT).show();
            }
        }else {
            if(requestCode == 1){
                Toast.makeText(this,"身份采集失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick({R.id.tv_upload_converter,R.id.tv_luffing_converter,R.id.tv_around_converter,
        R.id.rl_setting,R.id.rl_output,R.id.tv_tower_market,R.id.btn_play,R.id.tv_v1,
        R.id.tv_v2,R.id.tv_v3,R.id.rl_identity,R.id.rl_announcement,R.id.img_volume,
            R.id.img_full_screen,R.id.tv_attendance_onwork,R.id.tv_attendance_offwork,
            R.id.tv_real_state})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_real_state:
                switchConverterTab(ON_SELECT_REAL_STATE);
                break;
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
                SettingSecretDialog settingSecretDialog = new SettingSecretDialog(this);
                settingSecretDialog.setOnSecretListener(
                        new SettingSecretDialog.OnSecretListener() {
                            @Override
                            public void onSecretRight() {
                                MainActivity.this.startActivity(new Intent(MainActivity.this,
                                        SettingActivity.class));
                            }
                        });
                settingSecretDialog.show();
//                MainActivity.this.startActivity(new Intent(MainActivity.this,
//                        SettingActivity.class));
                break;
            case R.id.rl_output:
//                OutputDialog outputDialog = new OutputDialog(this);
//                outputDialog.show();
                startActivity(new Intent(MainActivity.this, OutputDialogActivity.class));
                break;
            case R.id.rl_identity:
                identity();
                break;
            case R.id.tv_attendance_onwork:
                onWork();
                break;
            case R.id.tv_attendance_offwork:
                offWork();
                break;
            case R.id.rl_announcement:
                DeviceManager.getInstance().getZtrsDevice().getAnnounecmentBean().setRead(true);
                initAnnouncementUnread();
                AnnouncementDialog announcementDialog = new AnnouncementDialog(this);
                announcementDialog.show();
                break;
            case R.id.tv_tower_market:
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("title","建机商城");
                intent.putExtra("url","http://mall.gxntp.com/");
                startActivity(intent);
                break;
            case R.id.btn_play:
                if(playerState == PLAYER_STATE_IDLE
                || playerState == PLAYER_STATE_STOP){
                    startPlay();
                    return;
                }
                if(mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    isPauseByUser = true;
                }else {
                    isPauseByUser = false;
                    mMediaPlayer.play();
                }
                break;
            case R.id.tv_v1:
                showUrlDialog(0);
                break;
            case R.id.tv_v2:
                showUrlDialog(1);
                break;
            case R.id.tv_v3:
                showUrlDialog(2);
                break;
            case R.id.img_volume:
                if(mute){
                    mute = false;
                    updateVolume();
                    imgVol.setBackgroundColor(getColor(R.color.transparent));
                }else {
                    mute = true;
                    mute();
                    imgVol.setBackgroundColor(getColor(R.color.primary_background_color_blue));
                }
                break;
            case R.id.img_full_screen:
                Intent intentFullScreen = new Intent(this,FullScreenActivity.class);
                intentFullScreen.putExtra("title",videoTitle);
                intentFullScreen.putExtra("url",url);
                startActivity(intentFullScreen);
                break;
        }
    }

    private void identity(){
        Intent intentIdentity = new Intent();
        //第一种方式
        ComponentName cn = new ComponentName("com.arcsoft.arcfacedemo",
                "com.arcsoft.arcfacedemo.activity.InitActivity");
        try {
            intentIdentity.setComponent(cn);
            //第二种方式
            //intent.setClassName("com.example.fm", "com.example.fm.MainFragmentActivity");
            intentIdentity.putExtra("requestCode",1);
            startActivityForResult(intentIdentity,1);
        } catch (Exception e) {
            //TODO  可以在这里提示用户没有安装应用或找不到指定Activity，或者是做其他的操作
            Toast.makeText(this,"人脸注册启动失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void onWork(){
        Intent intent = new Intent();
        //第一种方式
        ComponentName cn = new ComponentName("com.arcsoft.arcfacedemo",
                "com.arcsoft.arcfacedemo.activity.InitActivity");
        try {
            intent.setComponent(cn);
            //第二种方式
            //intent.setClassName("com.example.fm", "com.example.fm.MainFragmentActivity");
            intent.putExtra("requestCode",2);
            startActivityForResult(intent,2);
        } catch (Exception e) {
            //TODO  可以在这里提示用户没有安装应用或找不到指定Activity，或者是做其他的操作
            Toast.makeText(this,"打卡启动失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void offWork(){
        Intent intent = new Intent();
        //第一种方式
        ComponentName cn = new ComponentName("com.arcsoft.arcfacedemo",
                "com.arcsoft.arcfacedemo.activity.InitActivity");
        try {
            intent.setComponent(cn);
            //第二种方式
            //intent.setClassName("com.example.fm", "com.example.fm.MainFragmentActivity");
            intent.putExtra("requestCode",3);
            startActivityForResult(intent,3);
        } catch (Exception e) {
            //TODO  可以在这里提示用户没有安装应用或找不到指定Activity，或者是做其他的操作
            Toast.makeText(this,"打卡启动失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void showUrlDialog(int position){
        VideoInputDialog viDialog = new VideoInputDialog(this);
        viDialog.setUrl(url);
        viDialog.setOnUserClick(new VideoInputDialog.OnUserClick() {
            @Override
            public void onConfirm(String url) {
                MainActivity.this.url = url;
                if(playerState == PLAYER_STATE_IDLE
                        || playerState == PLAYER_STATE_STOP){
                    startPlay();
                }else {
                    stopPlay();
                    Observable.timer(100, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    startPlay();
                                }
                            });
                }
                switchVideo(position);
            }
        });
        viDialog.show();
    }

    private void switchVideo(int position){
        tvV1.setBackgroundColor(getColor(R.color.main_color_url_input_bg_normal));
        tvV1.setTextColor(getColor(R.color.main_color_url_input_text_normal));
        tvV2.setBackgroundColor(getColor(R.color.main_color_url_input_bg_normal));
        tvV2.setTextColor(getColor(R.color.main_color_url_input_text_normal));
        tvV3.setBackgroundColor(getColor(R.color.main_color_url_input_bg_normal));
        tvV3.setTextColor(getColor(R.color.main_color_url_input_text_normal));
        if(position == 0){
            tvV1.setBackgroundColor(getColor(R.color.main_color_url_input_bg_selected));
            tvV1.setTextColor(getColor(R.color.main_color_url_input_text_selected));
        } else  if(position == 1){
            tvV2.setBackgroundColor(getColor(R.color.main_color_url_input_bg_selected));
            tvV2.setTextColor(getColor(R.color.main_color_url_input_text_selected));
        }else  if(position == 2){
            tvV3.setBackgroundColor(getColor(R.color.main_color_url_input_bg_selected));
            tvV3.setTextColor(getColor(R.color.main_color_url_input_text_selected));
        }
    }


    private void switchConverterTab(int type){
        tvUploadConverter.setSelected(false);
        tvLuffingConverter.setSelected(false);
        tvAroundConverter.setSelected(false);
        tvRealState.setSelected(false);

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
        }else if(type == ON_SELECT_REAL_STATE){
            if(towerParameterFragment == null){
                towerParameterFragment = TowerParameterFragment.newInstance();
            }
            fragmentTransaction.replace(R.id.fl_frequency_converter,towerParameterFragment);
            tvRealState.setSelected(true);
        }
        fragmentTransaction.commit();
    }

    private void checkNetWorkState(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            imgNetwork.setImageDrawable(getDrawable(R.mipmap.xinhao_1));
        }else {
            imgNetwork.setImageDrawable(getDrawable(R.mipmap.network_unable));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        }
    }

    private void initAttendance(){
        tvWorkhistory.setSelected(true);
        tvWorkhistory.setClickable(true);
        if(onWork){
            tvOnwork.setSelected(true);
            tvOnwork.setClickable(true);
            tvOffwork.setSelected(false);
            tvOffwork.setClickable(false);
        }else {
            tvOnwork.setSelected(false);
            tvOnwork.setClickable(false);
            tvOffwork.setSelected(true);
            tvOffwork.setClickable(true);
        }
    }

    private void initVideoTab(){
        SharedPreferences sp = getSharedPreferences("Address",MODE_PRIVATE);
        String addressJson = sp.getString("address",null);
        Log.e("wch","addressJson:"+addressJson);
        List<AddressBean> addressBeans = new ArrayList<>();
        if(TextUtils.isEmpty(addressJson)){
            addressBeans = new ArrayList<>(5);
            for(int i=0;i<5;i++){
                AddressBean addressBean = new AddressBean();
                addressBean.setNumber(i);
                addressBean.setName("");
                addressBean.setAddress("");
                addressBeans.add(addressBean);
            }
            addressBeans.get(0).setName("吊钩可视化");
            addressBeans.get(0)
                    .setAddress("rtsp://admin:ztrs12345@192.168.0.17:554/h264/ch1/main/av_stream");
            addressBeans.get(1).setName("驾驶室");
            addressBeans.get(1)
                    .setAddress("rtsp://admin:ztrs12345@192.168.10.2:554/h264/ch1/main/av_stream");
            addressBeans.get(2).setName("起升排绳");
            addressBeans.get(2)
                    .setAddress("rtsp://admin:ztrs12345@192.168.10.3:554/h264/ch1/main/av_stream");
            addressBeans.get(3).setName("视频4");
            addressBeans.get(4).setName("视频5");
        }else {
            Gson gson = new Gson();
            addressBeans =gson.fromJson(addressJson,
                    new TypeToken<List<AddressBean>>(){}.getType());
        }

        List<AddressBean> videoAddressList = new ArrayList<>();

        for(int i=0;i<addressBeans.size();i++){
            if(!TextUtils.isEmpty(addressBeans.get(i).getAddress())){
                videoAddressList.add(addressBeans.get(i));
            }
        }

        videoAdapter = new VideoAdapter();
        videoAdapter.setData(videoAddressList);
        videoAdapter.setOnVideoSelectListener(new VideoAdapter.OnVideoSelectListener() {
            @Override
            public void onVideoSelected(int position,AddressBean address) {
                videoAdapter.notifyDataSetChanged();
                MainActivity.this.url = address.getAddress();
                MainActivity.this.videoTitle = address.getName();
                if(playerState == PLAYER_STATE_IDLE
                        || playerState == PLAYER_STATE_STOP){
                    startPlay();
                }else {
                    stopPlay();
                    rlVideoBg.setVisibility(View.VISIBLE);
                    Observable.timer(100, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    startPlay();
                                }
                            });
                }
            }
        });
        if(videoAddressList.size() == 0){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rvVideo.setLayoutManager(linearLayoutManager);
        }else {
            GridLayoutManager gridLayoutManager
                    = new GridLayoutManager(this, videoAddressList.size());
            rvVideo.setLayoutManager(gridLayoutManager);
        }
        rvVideo.setAdapter(videoAdapter);
        int itemNum = videoAddressList.size();
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int position = parent.getResources().getDisplayMetrics().widthPixels;
                //int position = parent.getChildAdapterPosition(view);
                if (parent.getChildCount() > 0) {
                    if (position % itemNum == 0) {                  //最左边Item
                        outRect.right = ScaleUtils.dip2px(MainActivity.this,1);
                    } else if (position % itemNum == itemNum - 1) { //最右边Item
                        outRect.left = ScaleUtils.dip2px(MainActivity.this,1);
                    } else {                                        //中间Item
                        outRect.left = ScaleUtils.dip2px(MainActivity.this,1);
                        outRect.right = ScaleUtils.dip2px(MainActivity.this,1);
                    }
                }
            }
        };
        rvVideo.addItemDecoration(itemDecoration);
    }

    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;
    private final int UPDATE_SCREEN = 0;
    private final int UPDATE_FULL_SCREEN = 1;
    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_SCREEN:
                    //frame的屏幕大小
                    int screen_width = rlVideo.getWidth();
                    int screen_height = rlVideo.getHeight();
                    Log.d(TAG, "screen_width:" + screen_width + " screen_height:" + screen_height);
                    mMediaPlayer.getVLCVout().setWindowSize(screen_width, screen_height);
                    mMediaPlayer.setAspectRatio(screen_width + ":" + screen_height);//设置屏幕比例
                    mMediaPlayer.setScale(0);
                    break;

            }
            return false;
        }
    });

    private void initVlc(){

        final ArrayList<String> args = new ArrayList<>();//VLC参数
        args.add("--rtsp-tcp");//强制rtsp-tcp，加快加载视频速度
        args.add("--live-caching=0");
        args.add("--file-caching=0");
        args.add("--network-caching=0");//增加实时性，延时大概2-3秒
        mLibVLC = new LibVLC(this, args);
        mMediaPlayer = new MediaPlayer(mLibVLC);
        updateVolume();
        mMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                if (event.type == MediaPlayer.Event.Opening) {
                    Log.d(TAG, "VLC Opening");
                }
                else if (event.type == MediaPlayer.Event.Buffering){
                    Log.d(TAG, "VLC Buffering：" + event.getBuffering());

                }
                else if (event.type == MediaPlayer.Event.Playing){
                    Log.d(TAG, "VLC Playing");
                    playerState = PLAYER_STATE_PLAYING;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rlVideoBg.setVisibility(View.GONE);
                            btnPlay.setText("暂停");
                        }
                    });             }
                else if (event.type == MediaPlayer.Event.Paused){
                    Log.d(TAG, "VLC Pause");
                    playerState = PLAYER_STATE_PAUSE;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnPlay.setText("播放");
                        }
                    });
                }
                else if (event.type == MediaPlayer.Event.Stopped){
                    Log.d(TAG, "VLC Stopped");
                    playerState = PLAYER_STATE_STOP;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d(TAG, "VLC Stopped detachViews");
                                mMediaPlayer.detachViews();
                            btnPlay.setText("播放");
                        }
                    });
                }
                else if (event.type == MediaPlayer.Event.EncounteredError){
                    Log.d(TAG, "VLC EncounteredError");
                    Toast.makeText(MainActivity.this, "播放失败", Toast.LENGTH_LONG).show();
                }
                else if (event.type == MediaPlayer.Event.Vout){
                    Log.d(TAG, "VLC Vout"+ event.getVoutCount());
                    if(event.getVoutCount() >0) {
                        mHandler.sendEmptyMessageDelayed(UPDATE_SCREEN, 100);
                    }
                }
                else if (event.type == MediaPlayer.Event.RecordChanged){
                    Log.d(TAG, "VLC RecordChanged");
                }
                else if (event.type == MediaPlayer.Event.EndReached){
                    Log.d(TAG, "VLC EndReached");
                }
            }
        });
    }
    private void startPlay(){
        Log.e(TAG,"startPlay");
        playerState = PLAYER_STATE_PREPARING;
        mMediaPlayer.attachViews(videoLayout, null, true, false);
        mMediaPlayer.setVideoScale(MediaPlayer.ScaleType.SURFACE_BEST_FIT);
        Uri uri = Uri.parse(url);//rtsp流地址或其他流地址
        //final Media media = new Media(mLibVLC, getAssets().openFd(ASSET_FILENAME));
        final Media media = new Media(mLibVLC, uri);
        media.setHWDecoderEnabled(false, false);//设置后才可以录制和截屏
        mMediaPlayer.setMedia(media);
        media.release();
        mMediaPlayer.play();
        updateVolume();

    }

    private void pausePlay(){
        mMediaPlayer.pause();
    }

    private void stopPlay(){
        mMediaPlayer.stop();
        mMediaPlayer.detachViews();
    }

    private void releasePlay(){
        mMediaPlayer.release();
        mLibVLC.release();
    }

    private void updateVolume(){
        if(mMediaPlayer != null){
            SharedPreferences sp = getSharedPreferences("SystemSetting",MODE_PRIVATE);
            int volume = sp.getInt("volume",50);
            mMediaPlayer.setVolume(volume);
            Log.e("wch","updatevolume:"+volume);
        }
    }

    private boolean mute = false;
    private void mute(){
        if(mMediaPlayer != null){
            mMediaPlayer.setVolume(0);
        }
    }

    private void playVoice(){
        boolean playerPlaying = PlayerManager.getInstance().isPlayerPlaying();
        if(playerPlaying){
            return;
        }

        int resId = getVoiceId();
        if(resId == 0){
            return;
        }
        AssetFileDescriptor afd = getResources().openRawResourceFd(resId);
        PlayerManager.getInstance().playerPrepare(afd);
        PlayerManager.getInstance().setVolume(volume/100.0f);
    }
    private int getVoiceId(){
        RealTimeDataBean realTimeDataBean = DeviceManager.getInstance().getZtrsDevice().getRealTimeDataBean();
        boolean alarmWind = !realTimeDataBean.isElectronicWindAlarmLimit();
        if(alarmWind){
            return R.raw.wind_alarm_1;
        }
        boolean torque110 = !realTimeDataBean.isElectronicTorqueLimitState3();
        if(torque110){
            return R.raw.torque_alarm_2;
        }
        boolean alarmWeight = !realTimeDataBean.isWeightAlarmLimit();
        if(alarmWeight){
            return R.raw.weight_alarm_3;
        }

        boolean alarmAround =  !realTimeDataBean.isOutputLeftAroundStopLimit()
                | !realTimeDataBean.isOutputRightAroundStopLimit();
        if(alarmAround){
            return R.raw.around_alarm_4;
        }
        boolean heightAlarm = !realTimeDataBean.isOutputUpUpStopLimit()
                | !realTimeDataBean.isOutputDownUpStopLimit();
        if(heightAlarm){
            return R.raw.height_alarm_5;
        }
        boolean alarmAmplitude = !realTimeDataBean.isOutputOutLuffingStopLimit()
                | !realTimeDataBean.isOutputInLuffingStopLimit();
        if(alarmAmplitude){
            return R.raw.amplitude_alarm_6;
        }


        boolean torque95 = !realTimeDataBean.isElectronicTorqueLimitState2();
        if(torque95){
            return R.raw.torque_warn_7;
        }
        boolean torque80 = !realTimeDataBean.isElectronicTorqueLimitState1();
        if(torque80){
            return R.raw.torque_warn_8;
        }

        boolean warnWeight = !realTimeDataBean.isWeightWarnLimit();
        if(warnWeight){
            return R.raw.weight_warn_9;
        }
        boolean warnAround = !realTimeDataBean.isOutputLeftAroundSlowLimit()
                | !realTimeDataBean.isOutputRightAroundSlowLimit();
        if(warnAround){
            return R.raw.around_warn_10;
        }

        boolean heightWarn = !realTimeDataBean.isOutputUpuPSlowLimit()
                | !realTimeDataBean.isOutputDownUpSlowLimit();
        if(heightWarn){
            return R.raw.height_warn_11;
        }

        boolean warnAmplitude = !realTimeDataBean.isOutputOutLuffingSlowLimit()
                | !realTimeDataBean.isOutputInLuffingSlowLimit();
        if(warnAmplitude){
            return R.raw.amplitude_warn_12;
        }

        boolean warnWind = !realTimeDataBean.isElectronicWindWarningLimit();
        if(warnWind){
            return R.raw.wind_warn_13;
        }

        boolean alarmSlope = !realTimeDataBean.isSlopeXAlarmLimit()
                | !realTimeDataBean.isSlopeYAlarmLimit();
        if(alarmSlope){
            return R.raw.slope_alarm_14;
        }
        boolean warnSlope = !realTimeDataBean.isSlopeXWarnLimit()
                | !realTimeDataBean.isSlopeYWarnLimit();
        if(warnSlope){
            return R.raw.slope_warn_15;
        }
        byte state = realTimeDataBean.getWireRopeState();
        if(state == 5){
            return  R.raw.wirerope_scrapped_16;
        }
        if(state == 4){
            return  R.raw.wirerope_serious_17;
        }
        if(state == 3){
            return  R.raw.wirerope_severe_18;
        }
        if(state == 2){
            return  R.raw.wirerope_middle_19;
        }
        if(state == 1){
            return  R.raw.wirerope_lihgt_20;
        }


        return 0;
    }

    int volume;
    //----------------------subscribe------------------------------//

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSystemSetting(SettingEventBus msg){
        LogUtils.LogI(TAG,"onSystemSetting: "+msg.getAction());
        if(msg.getAction() == SettingEventBus.ACTION_LIGHT_CHANGE){
            updateLight();
        }else if(msg.getAction() == SettingEventBus.ACTION_VOLUME_CHANGE){
            updateVolume();
            SharedPreferences sp = getSharedPreferences("SystemSetting",MODE_PRIVATE);
            volume = sp.getInt("volume",50);
        }else if(msg.getAction() == SettingEventBus.ACTION_VIDEO_URL_INPUT_CHANGE){
            if(playerState != PLAYER_STATE_IDLE) {
                stopPlay();
                rlVideoBg.setVisibility(View.VISIBLE);
            }
            initVideoTab();
        }
    }

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

            } else if(msg.getResult() == BaseMessage.RESULT_FAIL) {
                Toast.makeText(this, "查询静态参数查询命令发送失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    private UnlockCarDialog unlockCarDialog;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnlock(UnlockCarMessage unlockCarMessage){
        LogUtils.LogI("wch","unlockmessage: "+unlockCarMessage.getResult());
        if(unlockCarMessage.getResult() == BaseMessage.RESULT_REPORT) {
            UnLockCarBean unLockCarBean = DeviceManager.getInstance().getZtrsDevice().getUnLockCarBean();
            if(unLockCarBean.getState() == 'L') {
                if(unlockCarDialog == null) {
                    unlockCarDialog = new UnlockCarDialog(this);
                    unlockCarDialog.show();
                }else if(unlockCarDialog.isShowing()){
                    unlockCarDialog.dismiss();
                    unlockCarDialog = new UnlockCarDialog(this);
                    unlockCarDialog.show();
                }else {
                    unlockCarDialog = new UnlockCarDialog(this);
                    unlockCarDialog.show();
                }

            }else if(unLockCarBean.getState() == 'U'){
                if(unlockCarDialog != null && unlockCarDialog.isShowing()){
                    unlockCarDialog.dismiss();
                }
            }
        }else {

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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRealTimeData(RealTimeDataMessage msg){
        LogUtils.LogI(TAG,"onRealTimeData: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_REPORT
                || msg.getResult() == BaseMessage.RESULT_OK) {
            initView();
            playVoice();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAnnouncement(AnnouncementMessage msg){
        LogUtils.LogI(TAG,"onAnnouncement: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_REPORT) {
//            AnnouncementDialog announcementDialog = new AnnouncementDialog(this);
//            announcementDialog.show();
            initAnnouncementUnread();
        }
    }

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

    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback(){
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            Log.e(TAG,"onAvailable");
            runOnUiThread(() -> imgNetwork.setImageDrawable(getDrawable(R.mipmap.xinhao_1)));

        }

        @Override
        public void onLosing(@NonNull Network network, int maxMsToLive) {
            super.onLosing(network, maxMsToLive);
            Log.e(TAG,"onLosing");
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            Log.e(TAG,"onLost");
            runOnUiThread(() -> imgNetwork.setImageDrawable(getDrawable(R.mipmap.network_unable)));
        }

        @Override
        public void onUnavailable() {
            super.onUnavailable();
            Log.e(TAG,"onUnavailable");
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            Log.e(TAG,"onCapabilitiesChanged");
        }

        @Override
        public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties);
            Log.e(TAG,"onLinkPropertiesChanged");
        }

        @Override
        public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
            super.onBlockedStatusChanged(network, blocked);
            Log.e(TAG,"onBlockedStatusChanged");
        }
    };


}