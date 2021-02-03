package com.ztrs.zgj.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.e("wch","onReceive:"+intent.getAction());
        Log.e("wch","自启动了 ！！！！！");
        if (ACTION_BOOT.equals(intent.getAction())) {
            Intent newIntent = new Intent(context, SplashActivity.class);  // 要启动的Activity
            //1.如果自启动APP，参数为需要自动启动的应用包名
//            Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
            //这句话必须加上才能开机自动运行app的界面
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //2.如果自启动Activity
            Log.e("wch","start activity");
            context.startActivity(newIntent);
            //3.如果自启动服务
            //context.startService(newIntent);
        }
    }
}
