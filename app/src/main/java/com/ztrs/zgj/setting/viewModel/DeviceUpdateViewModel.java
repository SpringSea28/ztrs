package com.ztrs.zgj.setting.viewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.DeviceVersionBean;
import com.ztrs.zgj.device.bean.RegisterInfoBean;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class DeviceUpdateViewModel extends ViewModel {
    MutableLiveData<String> curVersion;

    MutableLiveData<VersionModel.UpdateState> updateState;

    String remoteVersion;


    public LiveData<String> getCurVersion(){
        if(curVersion == null){
            curVersion = new MutableLiveData<>();
        }
        return curVersion;
    }

    public LiveData<VersionModel.UpdateState> getUpdateState(){
        if(updateState == null){
            updateState = new MutableLiveData<>();
        }
        return updateState;
    }

    public String getRemoteVersion(){

        return remoteVersion;
    }

    public void updateCurVersion(){
        RegisterInfoBean registerInfoBean = DeviceManager.getInstance().getZtrsDevice().getRegisterInfoBean();
        curVersion.setValue(registerInfoBean.getDeviceVersion());
        if(updateState.getValue() == VersionModel.UpdateState.DOWNLOADING){
            onUpdate(true);
        }
    }

    public void initVersion(Context context) {
        String deviceVersion = DeviceManager.getInstance().getZtrsDevice().getRegisterInfoBean().getDeviceVersion();
        curVersion.setValue(deviceVersion);
        if("unKnown".equals(deviceVersion)){
            DeviceManager.getInstance().queryRegisterInfo();
        }
    }

    private Disposable checkVersionDispose;
    public void checkVersion(){
        updateState.setValue(VersionModel.UpdateState.CHECKING);
        DeviceManager.getInstance().deviceVersionCheck();
        Observable.timer(20,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        checkVersionDispose =d;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        onGetRemoteVersion(false);
                    }
                });
    }

    public void onGetRemoteVersion(boolean success){
        stopCheckVersion();
        if(success){
            DeviceVersionBean deviceVersionBean = DeviceManager.getInstance().getZtrsDevice()
                    .getDeviceVersionBean();
            remoteVersion = Integer.toHexString(deviceVersionBean.getVerInt()&0xff)
                    +"."+Integer.toHexString(deviceVersionBean.getVerFloat()&0xff);
            if(!remoteVersion.equals(curVersion.getValue())){
                updateState.setValue(VersionModel.UpdateState.CHECK_SUCCESS_CAN_UPDATE);
            }else {
                updateState.setValue(VersionModel.UpdateState.CHECK_SUCCESS_NO_UPDATE);
            }
        }else {
            updateState.setValue(VersionModel.UpdateState.CHECK_FAIL);
        }
    }

    private Disposable updateDispose;
    public void update(Context context){
        updateState.setValue(VersionModel.UpdateState.DOWNLOADING);
        DeviceManager.getInstance().deviceUpdate();
        Observable.timer(60, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        updateDispose = d;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        onUpdate(false);
                    }
                });
    }

    public void onUpdate(boolean success){
        stopCheckVersion();
        if(success){
            updateState.setValue(VersionModel.UpdateState.DOWNLOAD_SUCCESS);
        }else
            updateState.setValue(VersionModel.UpdateState.DOWNLOAD_FAIL);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopCheckVersion();
        stopUpdate();
    }

    private void stopCheckVersion(){
        if(checkVersionDispose != null && !checkVersionDispose.isDisposed()){
            Log.e("wch","checkVersionDispose");
            checkVersionDispose.dispose();
            checkVersionDispose = null;
        }
    }

    private void stopUpdate(){
        if(updateDispose != null && !updateDispose.isDisposed()){
            Log.e("wch","updateDispose");
            updateDispose.dispose();
            updateDispose = null;
        }
    }

}
