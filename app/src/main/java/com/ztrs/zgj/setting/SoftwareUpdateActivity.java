package com.ztrs.zgj.setting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.ActivitySoftUpdateBinding;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.setting.dialog.UpdateDialog;
import com.ztrs.zgj.setting.viewModel.AppUpdateViewModel;
import com.ztrs.zgj.setting.viewModel.VersionModel;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SoftwareUpdateActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = SoftwareUpdateActivity.class.getSimpleName();

    ActivitySoftUpdateBinding binding;
//    VersionModel versionModel;
    AppUpdateViewModel versionModel;
    UpdateDialog updateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_soft_update);
        binding.rlTitle.tvTitle.setText("软件升级");
        binding.rlTitle.tvBack.setOnClickListener(this);
        binding.rlCheck.setOnClickListener(this);

        versionModel = new ViewModelProvider(this).get(AppUpdateViewModel.class);
        LiveData<String> curVersion = versionModel.getCurVersion();
        curVersion.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvVersion.setText(s);
            }
        });
        LiveData<VersionModel.UpdateState> updateState = versionModel.getUpdateState();
        updateState.observe(this, new Observer<VersionModel.UpdateState>() {
            @Override
            public void onChanged(VersionModel.UpdateState updateState) {
                onUpdateStateChange(updateState);
            }
        });
        versionModel.initVersion(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_check:
                String hostId = DeviceManager.getInstance().getZtrsDevice().getRegisterInfoBean().getHostId();
                Log.e("wch","hostId:"+hostId);
                versionModel.checkVersion(hostId);
                break;
        }
    }

    private void onUpdateStateChange(VersionModel.UpdateState updateState){
        switch (updateState){
            case IDLE:

                break;
            case CHECKING:
                if(updateDialog == null || !updateDialog.isShowing()) {
                    updateDialog = new UpdateDialog(this);
                    updateDialog.initText("检测新版本中...");
                    updateDialog.initButton(false,false);
                    updateDialog.show();
                }
                break;
            case CHECK_SUCCESS_CAN_UPDATE:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("检测新版本: " + versionModel.getRemoteVersion());
                    updateDialog.showButton();
                    updateDialog.setOnUserClick(() -> {
                        versionModel.downLoadApk(this);
                    });
                    updateDialog.show();
                }
                break;
            case CHECK_SUCCESS_NO_UPDATE:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("已经是最新版本");
                    updateDialog.showConfirm();
                    updateDialog.setOnUserClick(() -> updateDialog.dismiss());
                    updateDialog.show();
                }
                break;
            case CHECK_FAIL:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("检测新版本失败");
                    updateDialog.showConfirm();
                    updateDialog.setOnUserClick(() -> updateDialog.dismiss());
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
                        installApk(SoftwareUpdateActivity.this,file);
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
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}