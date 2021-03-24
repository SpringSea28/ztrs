package com.ztrs.zgj.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.DialogUnclockCarBinding;
import com.ztrs.zgj.databinding.DialogVideoInputBinding;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.UnLockCarBean;
import com.ztrs.zgj.device.protocol.UnlockCarProtocol;
import com.ztrs.zgj.setting.Constants;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


public class UnlockCarDialog extends Dialog {


    private int delay = 0;
    public UnlockCarDialog(Context context){
        super(context, R.style.dialog_lockcar);
    }


    DialogUnclockCarBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        binding = DialogUnclockCarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
        UnLockCarBean unLockCarBean = DeviceManager.getInstance().getZtrsDevice().getUnLockCarBean();
        if(unLockCarBean.getState()== 'L'){
            delay = unLockCarBean.getDelayTime();
            if(delay >0){
                binding.llLock.setVisibility(View.GONE);
                binding.llDelay.setVisibility(View.VISIBLE);
                updateTime();
                startTimer();
            }else {
                binding.llLock.setVisibility(View.VISIBLE);
                binding.llDelay.setVisibility(View.GONE);
            }
        }
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                stopTimer();
            }
        });
    }

    private void updateTime(){
        int minute = (delay%3600)/60;
        int second = (delay%60);
        binding.tvMinute1.setText(""+minute/10);
        binding.tvMinute2.setText(""+minute%10);
        binding.tvSecond1.setText(""+second/10);
        binding.tvSecond2.setText(""+second%10);
        if(delay == 0){
            stopTimer();
            binding.llLock.setVisibility(View.VISIBLE);
            binding.llDelay.setVisibility(View.GONE);
        }
    }



    private Disposable disposable;

    private void startTimer() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        delay--;
                        updateTime();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void stopTimer() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
    }

}
