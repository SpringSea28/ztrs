package com.ztrs.zgj.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kongqw.serialportlibrary.Device;
import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.RegisterInfoMessage;
import com.ztrs.zgj.device.eventbus.StaticParameterMessage;
import com.ztrs.zgj.device.eventbus.TorqueCurveMessage;
import com.ztrs.zgj.main.msg.SerialPortOpenResultMsg;
import com.ztrs.zgj.setting.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @BindView(R.id.tv_init)
    TextView tvInit;
    @BindView(R.id.btn_retry)
    Button btnRetry;

    Unbinder bind;

    private boolean isOpen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        queryInit();
        DeviceManager.getInstance().openSerial();
        requestPermission();
    }

    @Override
    protected void onDestroy() {
        stopQuery();
        EventBus.getDefault().unregister(this);
        bind.unbind();
        super.onDestroy();
    }

    private Disposable disposable;

    private void queryInit() {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Long aLong) {
                        if(!isOpen){
                            tvInit.setText("串口打开失败，请重试");
                            btnRetry.setVisibility(View.VISIBLE);
                            return;
                        }
                        if(!isInitQuerySuccess()){
                            btnRetry.setVisibility(View.VISIBLE);
                            tvInit.setText("初始化失败，请重试");
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void stopQuery() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
    }

    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //没有权限则申请权限
            ActivityCompat.requestPermissions(SplashActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            LogCatHelper.getInstance(this,null).start();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 ){
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.e("wch","get permission");
                LogCatHelper.getInstance(this,null).start();
                //用户允许该权限

            } else {
                Log.e("wch","not get permission");
                //用户拒绝该权限

            }
            return;
        }
    }

    private long lastClick;
    @OnClick({R.id.btn_retry,R.id.btn_test})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_retry:
                long cur = System.currentTimeMillis();
                if(cur - lastClick > 2000) {
                    lastClick = cur;
                    tvInit.setText("初始化中，请稍后");
                    queryInit();
                    if(!isOpen){
                        DeviceManager.getInstance().openSerial();
                    }else if(!queryStaticParameterResult){
                        DeviceManager.getInstance().queryStaticParameter();
                    }else if(!queryTorqueResult){
                        DeviceManager.getInstance().queryTorqueCurve();
                    }else if(!queryRegisterInfo){
                        DeviceManager.getInstance().queryRegisterInfo();
                    }
                }else {
                    Toast.makeText(SplashActivity.this,"初始化中，请稍后",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_test:
                Log.e("wch","onclick");
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSerialOpen(SerialPortOpenResultMsg msg){
        LogUtils.LogI(TAG,"onSerialOpen: "+msg.isSuccess());
        if(msg.isSuccess()) {
            isOpen = true;
            DeviceManager.getInstance().queryStaticParameter();
            Observable.timer(100, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            DeviceManager.getInstance().queryTorqueCurve();
                        }
                    });
            Observable.timer(200, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            DeviceManager.getInstance().queryRegisterInfo();
                        }
                    });

        }else {
            isOpen = false;
            Toast.makeText(this, "串口打开失败", Toast.LENGTH_LONG).show();
        }
    }


    private int QUERY_IDLE = 0;
    private int QUERY_START = 1;
    private int QUERY_FINISH_SUCCESS = 2;
    private int QUERY_FINISH_FAIL = 3;
    private boolean queryStaticParameterResult;
    private boolean queryTorqueResult;
    private boolean queryRegisterInfo;
    private boolean isInitQuerySuccess(){
        if(queryStaticParameterResult==false){
            return false;
        }

        if(queryTorqueResult == false){
            return false;
        }

        if(queryRegisterInfo == false){
            return false;
        }
        stopQuery();
        Log.e(TAG,"start MainActivity");
        startActivity(new Intent(this,MainActivity.class));
        finish();
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaticParameter(StaticParameterMessage msg){
        LogUtils.LogI(TAG,"onStaticParameter: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onStaticParameter: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_QUERY) {
            if (msg.getResult() == BaseMessage.RESULT_OK) {
                queryStaticParameterResult = true;
            } else {
                queryStaticParameterResult = false;
                Toast.makeText(this, "查询静态参数查询失败", Toast.LENGTH_LONG).show();
            }
            isInitQuerySuccess();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTorqueCurve(TorqueCurveMessage msg){
        LogUtils.LogI(TAG,"onTorqueCurve: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onTorqueCurve: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                queryTorqueResult = true;
            }else {
                queryTorqueResult = false;
                Toast.makeText(this,"力矩曲线获取失败",Toast.LENGTH_LONG).show();
            }
            isInitQuerySuccess();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterInfo(RegisterInfoMessage msg){
        LogUtils.LogI(TAG,"onRegisterInfo: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onRegisterInfo: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                queryRegisterInfo = true;
            }else {
                queryRegisterInfo = false;
                Toast.makeText(this,"设备注册信息查询发送失败",Toast.LENGTH_LONG).show();
            }
        }
        isInitQuerySuccess();
    }
}