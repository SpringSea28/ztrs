package com.ztrs.zgj.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.DialogAnnouncementBinding;
import com.ztrs.zgj.databinding.DialogInputSecretBinding;
import com.ztrs.zgj.device.DeviceManager;

import java.io.UnsupportedEncodingException;


public class AnnouncementDialog extends Dialog implements View.OnClickListener {


    public AnnouncementDialog(Context context){
        super(context);
    }

    DialogAnnouncementBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        binding = DialogAnnouncementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
        binding.imgClose.setOnClickListener(this);
        byte[] announcement = DeviceManager.getInstance().getZtrsDevice().getAnnounecmentBean().getAnnouncement();
        if(announcement != null){
            try {
                String str = new String(announcement,"UTF-8");
                String[] split = str.split("\n");
                if(split != null && split.length>0){
                    if(!TextUtils.isEmpty(split[0])){
                        binding.tvText1.setText(split[0]);
                    }
                }

                if(split != null && split.length>1){
                    if(!TextUtils.isEmpty(split[1])){
                        binding.tvText2.setText(split[1]);
                    }
                }

                if(split != null && split.length>2){
                    if(!TextUtils.isEmpty(split[2])){
                        binding.tvText3.setText(split[2]);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onClick(View v) {
        if(R.id.img_close == v.getId()){
            dismiss();
        }
    }



}
