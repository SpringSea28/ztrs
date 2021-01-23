package com.ztrs.zgj;

import android.app.Application;

import com.umeng.commonsdk.UMConfigure;
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
/**
 * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
 * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
 * UMConfigure.init调用中appkey和channel参数请置为null）。
 */
            UMConfigure.init(this, "600c16fff1eb4f3f9b6d1506", "wch",0, null);
        }


}
