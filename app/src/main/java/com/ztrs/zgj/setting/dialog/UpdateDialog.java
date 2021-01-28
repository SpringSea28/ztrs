package com.ztrs.zgj.setting.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.ztrs.zgj.databinding.DialogUpdateBinding;


public class UpdateDialog extends Dialog implements View.OnClickListener {

    String msg ;
    boolean confirm;
    boolean cancel;

    public UpdateDialog(Context context){
        super(context);
    }

    DialogUpdateBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("wch","onCreate");
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        binding = DialogUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
        binding.btnConfirm.setOnClickListener(v -> {
//            dismiss();
            if(onUserClick != null){
                onUserClick.onConfirm();
            }
        });
        binding.btnCancel.setOnClickListener(v -> dismiss());

        binding.tvMsg.setText(msg);
        if(confirm){
            binding.rlConfirm.setVisibility(View.VISIBLE);
        }else {
            binding.rlConfirm.setVisibility(View.GONE);
        }

        if(cancel){
            binding.rlCancel.setVisibility(View.VISIBLE);
        }else {
            binding.rlCancel.setVisibility(View.GONE);
        }
    }

    public void initText(String text){
        msg = text;
    }

    public void initButton(boolean confirm ,boolean cancel){
        this.confirm = confirm;
        this.cancel = cancel;
    }

    public void setText(String text){
        binding.tvMsg.setText(text);
    }
    public void hideButton(){
        binding.rlConfirm.setVisibility(View.GONE);
        binding.rlCancel.setVisibility(View.GONE);
    }

    public void showButton(){
        binding.rlConfirm.setVisibility(View.VISIBLE);
        binding.rlCancel.setVisibility(View.VISIBLE);
    }

    public void showConfirm(){
        binding.rlConfirm.setVisibility(View.VISIBLE);
        binding.rlCancel.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

    }

    OnUserClick onUserClick;
    public void setOnUserClick(OnUserClick onUserClick){
        this.onUserClick = onUserClick;
    }
    public interface OnUserClick{
        void onConfirm();
    }
}
