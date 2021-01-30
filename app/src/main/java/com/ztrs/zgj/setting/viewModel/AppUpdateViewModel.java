package com.ztrs.zgj.setting.viewModel;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ztrs.zgj.setting.http.AppCheckBean;
import com.ztrs.zgj.setting.http.HttpRequest;
import com.ztrs.zgj.setting.http.VersionBean;

import java.io.File;

public class AppUpdateViewModel extends ViewModel {
    MutableLiveData<String> curVersion;

    MutableLiveData<VersionModel.UpdateState> updateState;

    AppCheckBean versionBean;


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
        if(versionBean !=null && versionBean.getCode()==0
          &&versionBean.getData() != null){
            return versionBean.getData().getVer();
        }
        return null;
    }

    public void initVersion(Context context) {

        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            curVersion.setValue(version);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void checkVersion(String deviceId){
        updateState.setValue(VersionModel.UpdateState.CHECKING);
        new HttpRequest().checkAppVersion(deviceId,curVersion.getValue(),this);
    }

    public void onGetRemoteVersion(boolean success,AppCheckBean versionBean){
        this.versionBean = versionBean;
        if(success){
            if(versionBean.getCode() == 0 && versionBean.getData() != null
                    && versionBean.getData().getCode() ==1){
                updateState.setValue(VersionModel.UpdateState.CHECK_SUCCESS_CAN_UPDATE);
            }else {
                updateState.setValue(VersionModel.UpdateState.CHECK_SUCCESS_NO_UPDATE);
            }
        }else {
            updateState.setValue(VersionModel.UpdateState.CHECK_FAIL);
        }
    }

    public void downLoadApk(Context context){
        File file = getApkFile(context);
        updateState.setValue(VersionModel.UpdateState.DOWNLOADING);
        new HttpRequest().downloadAppApk(versionBean.getData().getUrl(),this,file);
    }

    public void onDownApk(boolean success){
        if(success){
            updateState.setValue(VersionModel.UpdateState.DOWNLOAD_SUCCESS);
        }else
            updateState.setValue(VersionModel.UpdateState.DOWNLOAD_FAIL);
    }

    private File getApkDir(Context context){
        String dirPath = context.getExternalFilesDir(null).getAbsolutePath().concat("/Apk");
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
        String apkPath = getApkDir(context).getAbsolutePath()+"/"+"1.0.17"+".apk";
        File file = new File(apkPath);
        return file;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }

}
