package com.ztrs.zgj.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.ztrs.zgj.databinding.DialogUpdateBinding;
import com.ztrs.zgj.databinding.DialogVideoInputBinding;


public class VideoInputDialog extends Dialog implements View.OnClickListener {

    String msg ;
    boolean confirm;
    boolean cancel;

    public VideoInputDialog(Context context){
        super(context);
    }

    DialogVideoInputBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        binding = DialogVideoInputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
        binding.btnConfirm.setOnClickListener(v -> {
            dismiss();
            if(onUserClick != null){
                onUserClick.onConfirm(binding.edtUrl.getText().toString());
            }
        });
        binding.btnCancel.setOnClickListener(v -> dismiss());

        binding.edtUrl.setText(msg);
    }

    public void setUrl(String url){
        msg = url;
    }


    @Override
    public void onClick(View v) {

    }

    OnUserClick onUserClick;
    public void setOnUserClick(OnUserClick onUserClick){
        this.onUserClick = onUserClick;
    }
    public interface OnUserClick{
        void onConfirm(String url);
    }
}
