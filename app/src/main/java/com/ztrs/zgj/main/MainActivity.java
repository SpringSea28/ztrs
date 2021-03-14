package com.ztrs.zgj.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ztrs.zgj.device.bean.StaticParameterBean;
import com.ztrs.zgj.device.eventbus.AmplitudeCalibrationMessage;
import com.ztrs.zgj.device.eventbus.AnnouncementMessage;
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
import com.ztrs.zgj.main.adapter.VideoAdapter;
import com.ztrs.zgj.main.dialog.AnnouncementDialog;
import com.ztrs.zgj.main.dialog.OutputDialog;
import com.ztrs.zgj.main.dialog.SettingSecretDialog;
import com.ztrs.zgj.main.dialog.VideoInputDialog;
import com.ztrs.zgj.main.fragment.AroundConverterFragment;
import com.ztrs.zgj.main.fragment.LuffingConverterFragment;
import com.ztrs.zgj.main.fragment.TowerParameterFragment;
import com.ztrs.zgj.main.fragment.UploadConverterFragment;
import com.ztrs.zgj.main.msg.SerialPortOpenResultMsg;
import com.ztrs.zgj.setting.OutputActivity;
import com.ztrs.zgj.setting.SettingActivity;
import com.ztrs.zgj.setting.SoftwareUpdateActivity;
import com.ztrs.zgj.setting.WebActivity;
import com.ztrs.zgj.setting.adapter.AddressAdapter;
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
import java.util.Date;
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

    private static final int PLAYER_STATE_IDLE = 0;
    private static final int PLAYER_STATE_PREPARING = 1;
    private static final int PLAYER_STATE_PREPARED = 2;
    private static final int PLAYER_STATE_PLAYING = 3;
    private static final int PLAYER_STATE_PAUSE = 4;
    private static final int PLAYER_STATE_STOP = 5;
    private static final int PLAYER_STATE_ERROR = 6;

    private int playerState;
    private boolean isPauseByUser;

    private String url = "rtsp://admin:ztrs12345@192.168.0.8:554/h264/ch1/main/av_stream";

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
//                test.queryRelayConfiguration();
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
//                test.testOnReceiveRealtimedata();
//                test.testOnReceiveTorqueCurve();
//                test.testQueryStaticParameter();
//                test.testQueryOrthogonalRegionalRestriction();
//                test.testOnReceiveRegisterInfo();
//                test.testOnReceiveStaticParameter();
//                test.onReceiveInverterData();
                DeviceManager.getInstance().emergencyCall();
            }
        });
        EventBus.getDefault().register(this);
        initView();
        display();
        checkUpdate();
        checkNetWorkState();
        initVideoTab();
        initVlc();
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

    private void addTowerParameterFragment(){
        TowerParameterFragment towerParameterFragment = TowerParameterFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_tower_parameter,towerParameterFragment);
        fragmentTransaction.commit();
    }

    @OnClick({R.id.tv_upload_converter,R.id.tv_luffing_converter,R.id.tv_around_converter,
        R.id.rl_setting,R.id.rl_output,R.id.tv_tower_market,R.id.btn_play,R.id.tv_v1,
        R.id.tv_v2,R.id.tv_v3,R.id.rl_identity,R.id.rl_announcement})
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
                SettingSecretDialog settingSecretDialog = new SettingSecretDialog(this);
                settingSecretDialog.setOnSecretListener(
                        new SettingSecretDialog.OnSecretListener() {
                            @Override
                            public void onSecretRight() {
                                MainActivity.this.startActivity(new Intent(MainActivity.this,
                                        SettingActivity.class));
                            }
                        });
//                settingSecretDialog.show();
                MainActivity.this.startActivity(new Intent(MainActivity.this,
                        SettingActivity.class));
                break;
            case R.id.rl_output:
//                OutputDialog outputDialog = new OutputDialog(this);
//                outputDialog.show();
                startActivity(new Intent(MainActivity.this, OutputDialogActivity.class));
                break;
            case R.id.rl_identity:

                break;
            case R.id.rl_announcement:
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

    private void initVideoTab(){
        SharedPreferences sp = getSharedPreferences("Address",MODE_PRIVATE);
        String addressJson = sp.getString("address",null);
        Log.e("wch","addressJson:"+addressJson);
        List<AddressBean> videoAddressList = new ArrayList<>();
        if(TextUtils.isEmpty(addressJson)){

        }else {
            Gson gson = new Gson();
            List<AddressBean> addressBeans =gson.fromJson(addressJson,
                    new TypeToken<List<AddressBean>>(){}.getType());
            for(int i=0;i<addressBeans.size();i++){
                if(!TextUtils.isEmpty(addressBeans.get(i).getAddress())){
                    videoAddressList.add(addressBeans.get(i));
                }
            }
        }

        videoAdapter = new VideoAdapter();
        videoAdapter.setData(videoAddressList);
        videoAdapter.setOnVideoSelectListener(new VideoAdapter.OnVideoSelectListener() {
            @Override
            public void onVideoSelected(int position,String address) {
                videoAdapter.notifyDataSetChanged();
                MainActivity.this.url = address;
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


    //----------------------subscribe------------------------------//

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSystemSetting(SettingEventBus msg){
        LogUtils.LogI(TAG,"onSystemSetting: "+msg.getAction());
        if(msg.getAction() == SettingEventBus.ACTION_LIGHT_CHANGE){
            updateLight();
        }else if(msg.getAction() == SettingEventBus.ACTION_VOLUME_CHANGE){

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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRealTimeData(RealTimeDataMessage msg){
        LogUtils.LogI(TAG,"onRealTimeData: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_REPORT
                || msg.getResult() == BaseMessage.RESULT_OK) {
            initView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAnnouncement(AnnouncementMessage msg){
        LogUtils.LogI(TAG,"onAnnouncement: "+msg.getResult());
        if(msg.getResult() == BaseMessage.RESULT_REPORT) {
            AnnouncementDialog announcementDialog = new AnnouncementDialog(this);
            announcementDialog.show();
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