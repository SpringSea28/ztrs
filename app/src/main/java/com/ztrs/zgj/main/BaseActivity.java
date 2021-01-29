package com.ztrs.zgj.main;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime;

    boolean isFastClick(MotionEvent ev){
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            long interval = System.currentTimeMillis() -lastClickTime;
            if(interval < MIN_CLICK_DELAY_TIME){
                return true;
            }else {
                lastClickTime = System.currentTimeMillis();
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isFastClick(ev)){
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

}
