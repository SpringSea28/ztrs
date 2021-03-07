package com.ztrs.zgj.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.DialogInputSecretBinding;


public class SettingSecretDialog extends Dialog implements View.OnClickListener {


    public SettingSecretDialog(Context context){
        super(context);
    }

    DialogInputSecretBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        binding = DialogInputSecretBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
        binding.btnSave.setOnClickListener(this);
        binding.imgClose.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        if(R.id.btn_save == v.getId()){
            verifySecret();
        }else if(R.id.img_close == v.getId()){
            dismiss();
        }
    }


    private void verifySecret(){
//        String secret = binding.edtInputSecret.getText().toString();
        dismiss();
       if(onSecretListener != null){
           onSecretListener.onSecretRight();
       }
    }

    OnSecretListener onSecretListener;
    public void setOnSecretListener(OnSecretListener onSecretListener){
        this.onSecretListener = onSecretListener;
    }
    public interface OnSecretListener{
        void onSecretRight();
    }
}
