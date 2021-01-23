package com.ztrs.zgj.setting.utils;

import com.ztrs.zgj.device.bean.RelayConfigurationBean;
import com.ztrs.zgj.setting.bean.RelayBean;

import java.util.ArrayList;
import java.util.List;

public class OutPutChangUtils {

    public static final int INDEX_HEIGHT_HIGH_ALARM = 3;
    public static final int INDEX_HEIGHT_LOW_ALARM = 4;

    public static List<RelayBean> getRelayBeanList(RelayConfigurationBean relayConfigurationBean){
        List<RelayBean> relayBeanList = new ArrayList<>();

        byte heightHighAlarm = relayConfigurationBean.getHeightUpStop();
        byte heightHighAlarmAction = relayConfigurationBean.getHeightUpStopOutputState();
        if(heightHighAlarm != 0) {
            RelayBean relayBean = new RelayBean();
            relayBean.setRelay(heightHighAlarm);
            relayBean.setUse(INDEX_HEIGHT_HIGH_ALARM);
            relayBean.setAction(heightHighAlarmAction);
            relayBeanList.add(relayBean);
        }

        byte heightLowAlarm = relayConfigurationBean.getHeightDownStop();
        byte heightLowAlarmAction = relayConfigurationBean.getHeightDownStopOutputState();
        if(heightHighAlarm != 0) {
            RelayBean relayBean = new RelayBean();
            relayBean.setRelay(heightLowAlarm);
            relayBean.setUse(INDEX_HEIGHT_LOW_ALARM);
            relayBean.setAction(heightLowAlarmAction);
            relayBeanList.add(relayBean);
        }

        return relayBeanList;
    }
}
