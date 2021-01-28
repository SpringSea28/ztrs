package com.ztrs.zgj.setting.viewModel;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ztrs.zgj.setting.http.HttpRequest;
import com.ztrs.zgj.setting.http.VersionBean;

public class VersionModel extends ViewModel {

    MutableLiveData<String> curVersion;

    MutableLiveData<UpdateState> updateState;

    VersionBean.UpdatesBean versionBean;


    public LiveData<String> getCurVersion(){
        if(curVersion == null){
            curVersion = new MutableLiveData<>();
        }
        return curVersion;
    }

    public LiveData<UpdateState> getUpdateState(){
        if(updateState == null){
            updateState = new MutableLiveData<>();
        }
        return updateState;
    }

    public String getRemoteVersion(){
        if(versionBean !=null){
            return versionBean.getVersionName();
        }
        return null;
    }

    public void initVersion(Context context) {

        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            curVersion.postValue(version);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void checkVersion(){
        updateState.setValue(UpdateState.CHECKING);
        new HttpRequest().checkVersion(this);
    }

    public void onGetRemoteVersion(boolean success,VersionBean.UpdatesBean versionBean){
        this.versionBean = versionBean;
        if(success){
            if(versionBean.getVersionName().equals(curVersion.getValue())){
                updateState.setValue(UpdateState.CHECK_SUCCESS_NO_UPDATE);
            }else {
                updateState.setValue(UpdateState.CHECK_SUCCESS_CAN_UPDATE);
            }
        }else {
            updateState.setValue(UpdateState.CHECK_FAIL);
        }
    }

    public void downLoadApk(Context context){
        File file = getApkFile(context);
        updateState.setValue(UpdateState.DOWNLOADING);
        new HttpRequest().downloadApk("http://120.78.84.188/upgrade"+versionBean.getFile(),this,file);
    }

    public void onDownApk(boolean success){
        if(success){
            updateState.setValue(UpdateState.DOWNLOAD_SUCCESS);
        }else
            updateState.setValue(UpdateState.DOWNLOAD_FAIL);
    }

    private File getApkDir(Context context){
        String dirPath = context.getExternalFilesDir(null).getAbsolutePath().concat("/cchipApk");
        File apkDir = new File(dirPath);
        if(!apkDir.exists()){
            apkDir.mkdir();
        }
        return apkDir;
    }

    public File getApkFile(Context context){
        String apkPath = getApkDir(context).getAbsolutePath()+"/"+getRemoteVersion()+".apk";
        File file = new File(apkPath);
        return file;
    }

    public File getApkFileTest(Context context){
        String apkPath = getApkDir(context).getAbsolutePath()+"/"+"1.4.5"+".apk";
        File file = new File(apkPath);
        return file;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public static enum UpdateState{
        IDLE,CHECKING,CHECK_SUCCESS_CAN_UPDATE,CHECK_SUCCESS_NO_UPDATE,CHECK_FAIL,DOWNLOADING,DOWNLOAD_SUCCESS,DOWNLOAD_FAIL
    }
}
