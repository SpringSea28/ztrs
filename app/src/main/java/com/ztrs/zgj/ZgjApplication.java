package com.ztrs.zgj;

import android.app.Application;

import com.ztrs.zgj.main.CrashCatchHandler;

public class ZgjApplication extends Application {
    /**
     * Created by waka on 2016/1/13.
     */

        @Override
        public void onCreate() {
            super.onCreate();
//            CrashCatchHandler crashCatchHandler = CrashCatchHandler.getInstance();//获得单例
//            crashCatchHandler.init(getApplicationContext());//初始化,传入context
        }
}
