package com.ztrs.zgj.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

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
        binding.imgClose.setOnClickListener(this);
        binding.edtInputSecret.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 6){
                    if(s.toString().equals("123456")){
                        verifySecret();
                        Toast.makeText(SettingSecretDialog.this.getContext(),
                                "密码正确",Toast.LENGTH_SHORT).show();
                    }else {
                        s.clear();
                        Toast.makeText(SettingSecretDialog.this.getContext(),
                                "密码错误",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        if(R.id.img_close == v.getId()){
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
