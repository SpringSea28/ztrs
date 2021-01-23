package com.ztrs.zgj.setting.eventbus;

public class SettingEventBus {

    public static final int ACTION_QUERY_STATIC_PARAMETER = 1 ;

    int action;

    public SettingEventBus(int action){
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
