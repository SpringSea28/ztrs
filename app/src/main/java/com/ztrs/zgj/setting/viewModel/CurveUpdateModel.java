package com.ztrs.zgj.setting.viewModel;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ztrs.zgj.setting.http.AppCheckBean;
import com.ztrs.zgj.setting.http.HttpRequest;
import com.ztrs.zgj.setting.http.TorqueCurveCheckBean;

import java.io.File;

public class CurveUpdateModel extends ViewModel {
    MutableLiveData<String> curVersion;

    MutableLiveData<VersionModel.UpdateState> updateState;

    TorqueCurveCheckBean versionBean;


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

    public void initVersion(Context context,String version) {
            curVersion.setValue(version);
    }

    public void checkVersion(String deviceId){
        updateState.setValue(VersionModel.UpdateState.CHECKING);
        new HttpRequest().checkCurveVersion(deviceId,curVersion.getValue(),this);
    }

    public void onGetRemoteVersion(boolean success,TorqueCurveCheckBean versionBean){
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

    public void downloadCurve(Context context){
        File file = getFile(context);
        updateState.setValue(VersionModel.UpdateState.DOWNLOADING);
        new HttpRequest().downloadCurve(versionBean.getData().getUrl(),this,file);
    }

    public void onDownApk(boolean success){
        if(success){
            updateState.setValue(VersionModel.UpdateState.DOWNLOAD_SUCCESS);
        }else
            updateState.setValue(VersionModel.UpdateState.DOWNLOAD_FAIL);
    }

    private File getDir(Context context){
        String dirPath = context.getExternalFilesDir(null).getAbsolutePath().concat("/momentLib");
        File textDir = new File(dirPath);
        if(!textDir.exists()){
            textDir.mkdir();
        }
        return textDir;
    }

    public File getFile(Context context){
        String textPath = getDir(context).getAbsolutePath()+"/"+getRemoteVersion()+".txt";
        File file = new File(textPath);
        return file;
    }

    public static File getFile(Context context,String version){
        String dirPath = context.getExternalFilesDir(null).getAbsolutePath().concat("/momentLib");
        File textDir = new File(dirPath);
        if(!textDir.exists()){
            textDir.mkdir();
        }
        String textPath = textDir.getAbsolutePath()+"/"+version+".txt";
        File file = new File(textPath);
        return file;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }

}
