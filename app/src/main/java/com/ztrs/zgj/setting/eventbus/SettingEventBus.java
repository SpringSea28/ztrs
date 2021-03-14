package com.ztrs.zgj.setting.eventbus;

public class SettingEventBus {

    public static final int ACTION_QUERY_STATIC_PARAMETER = 1 ;
    public static final int ACTION_VOLUME_CHANGE = 2 ;
    public static final int ACTION_LIGHT_CHANGE = 3 ;
    public static final int ACTION_VIDEO_URL_INPUT_CHANGE = 4 ;

    int action;
    int value;

    public SettingEventBus(int action){
        this.action = action;
    }


    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
