package com.ztrs.zgj.setting.utils;

import android.util.Log;

import com.ztrs.zgj.device.bean.RelayConfigurationBean;
import com.ztrs.zgj.setting.bean.RelayBean;

import java.util.ArrayList;
import java.util.List;

public class OutPutChangUtils {


    public static List<RelayBean> getRelayBeanList(RelayConfigurationBean relayConfigurationBean){
        List<RelayBean> relayBeanList = new ArrayList<>();

        byte use1 = relayConfigurationBean.getRelay1Use();
        byte state1 = relayConfigurationBean.getRelay1State();
        RelayBean relay1 = new RelayBean();
        relay1.setRelay(1);
        relay1.setUse(changeUseFromProtocol(use1));
        relay1.setAction(state1);
        relayBeanList.add(relay1);

        byte use2 = relayConfigurationBean.getRelay2Use();
        byte state2 = relayConfigurationBean.getRelay2State();
        RelayBean relay2 = new RelayBean();
        relay2.setRelay(2);
        relay2.setUse(changeUseFromProtocol(use2));
        relay2.setAction(state2);
        relayBeanList.add(relay2);


        byte use3 = relayConfigurationBean.getRelay3Use();
        byte state3 = relayConfigurationBean.getRelay3State();
        RelayBean relay3 = new RelayBean();
        relay3.setRelay(3);
        relay3.setUse(changeUseFromProtocol(use3));
        relay3.setAction(state3);
        relayBeanList.add(relay3);


        byte use4 = relayConfigurationBean.getRelay4Use();
        byte state4 = relayConfigurationBean.getRelay4State();
        RelayBean relay4 = new RelayBean();
        relay4.setRelay(4);
        relay4.setUse(changeUseFromProtocol(use4));
        relay4.setAction(state4);
        relayBeanList.add(relay4);

        byte use5 = relayConfigurationBean.getRelay5Use();
        byte state5 = relayConfigurationBean.getRelay5State();
        RelayBean relay5 = new RelayBean();
        relay5.setRelay(5);
        relay5.setUse(changeUseFromProtocol(use5));
        relay5.setAction(state5);
        relayBeanList.add(relay5);


        byte use6 = relayConfigurationBean.getRelay6Use();
        byte state6 = relayConfigurationBean.getRelay6State();
        RelayBean relay6 = new RelayBean();
        relay6.setRelay(6);
        relay6.setUse(changeUseFromProtocol(use6));
        relay6.setAction(state6);
        relayBeanList.add(relay6);


        byte use7 = relayConfigurationBean.getRelay7Use();
        byte state7 = relayConfigurationBean.getRelay7State();
        RelayBean relay7 = new RelayBean();
        relay7.setRelay(7);
        relay7.setUse(changeUseFromProtocol(use7));
        relay7.setAction(state7);
        relayBeanList.add(relay7);


        byte use8 = relayConfigurationBean.getRelay8Use();
        byte state8 = relayConfigurationBean.getRelay8State();
        RelayBean relay8 = new RelayBean();
        relay8.setRelay(8);
        relay8.setUse(changeUseFromProtocol(use8));
        relay8.setAction(state8);
        relayBeanList.add(relay8);


        byte use9 = relayConfigurationBean.getRelay9Use();
        byte state9 = relayConfigurationBean.getRelay9State();
        RelayBean relay9 = new RelayBean();
        relay9.setRelay(9);
        relay9.setUse(changeUseFromProtocol(use9));
        relay9.setAction(state9);
        relayBeanList.add(relay9);

        byte use10 = relayConfigurationBean.getRelay10Use();
        byte state10 = relayConfigurationBean.getRelay10State();
        RelayBean relay10 = new RelayBean();
        relay10.setRelay(10);
        relay10.setUse(changeUseFromProtocol(use10));
        relay10.setAction(state10);
        relayBeanList.add(relay10);

        byte use11 = relayConfigurationBean.getRelay11Use();
        byte state11 = relayConfigurationBean.getRelay11State();
        RelayBean relay11 = new RelayBean();
        relay11.setRelay(11);
        relay11.setUse(changeUseFromProtocol(use11));
        relay11.setAction(state11);
        relayBeanList.add(relay11);

        return relayBeanList;
    }

    private static byte changeUseFromProtocol(byte use){
        if(use == RelayConfigurationBean.WEIGHT_ALARM){
            return 0;
        }else if(use == RelayConfigurationBean.TORQUE_WARN){
            return 1;
        }else if(use == RelayConfigurationBean.TORQUE_ALARM){
            return 2;
        }
        else if(use == RelayConfigurationBean.TORQUE_ALARM){
            return 2;
        }
        else if(use == RelayConfigurationBean.HEIGHT_ALARM_HIGH){
            return 3;
        }
        else if(use == RelayConfigurationBean.HEIGHT_ALARM_LOW){
            return 4;
        }
        else if(use == RelayConfigurationBean.AMPLITUDE_ALARM_HIGH){
            return 5;
        }
        else if(use == RelayConfigurationBean.AMPLITUDE_ALARM_LOW){
            return 6;
        }
        else if(use == RelayConfigurationBean.AROUND_ALARM_HIGH){
            return 7;
        }
        else if(use == RelayConfigurationBean.AROUND_ALARM_LOW){
            return 8;
        }
        else if(use == RelayConfigurationBean.LOCK_CONTROL){
            return 9;
        }else if(use == RelayConfigurationBean.NC){
            return 10;
        }
        return 10;
    }

   private static byte changeUseFormUi(int use){
        for(byte i=0; i<12;i++){
            if(changeUseFromProtocol(i) == use) {
                return i;
            }
        }
        return 0;
   }

    public static RelayConfigurationBean getRelayConfig(List<RelayBean> relayBeans){
        RelayConfigurationBean relayConfigurationBean = new RelayConfigurationBean();

        Log.e("wch","use0: "+relayBeans.get(0).getUse());
        Log.e("wch","change:"+changeUseFormUi(relayBeans.get(0).getUse()));
        relayConfigurationBean.setRelay1Use(changeUseFormUi(relayBeans.get(0).getUse()));
        relayConfigurationBean.setRelay1State((byte)relayBeans.get(0).getAction());

        relayConfigurationBean.setRelay2Use(changeUseFormUi(relayBeans.get(1).getUse()));
        relayConfigurationBean.setRelay2State((byte)relayBeans.get(1).getAction());

        relayConfigurationBean.setRelay3Use(changeUseFormUi(relayBeans.get(2).getUse()));
        relayConfigurationBean.setRelay3State((byte)relayBeans.get(2).getAction());

        relayConfigurationBean.setRelay4Use(changeUseFormUi(relayBeans.get(3).getUse()));
        relayConfigurationBean.setRelay4State((byte)relayBeans.get(3).getAction());

        relayConfigurationBean.setRelay5Use(changeUseFormUi(relayBeans.get(4).getUse()));
        relayConfigurationBean.setRelay5State((byte)relayBeans.get(4).getAction());

        relayConfigurationBean.setRelay6Use(changeUseFormUi(relayBeans.get(5).getUse()));
        relayConfigurationBean.setRelay6State((byte)relayBeans.get(5).getAction());

        relayConfigurationBean.setRelay7Use(changeUseFormUi(relayBeans.get(6).getUse()));
        relayConfigurationBean.setRelay7State((byte)relayBeans.get(6).getAction());

        relayConfigurationBean.setRelay8Use(changeUseFormUi(relayBeans.get(7).getUse()));
        relayConfigurationBean.setRelay8State((byte)relayBeans.get(7).getAction());

        relayConfigurationBean.setRelay9Use(changeUseFormUi(relayBeans.get(8).getUse()));
        relayConfigurationBean.setRelay9State((byte)relayBeans.get(8).getAction());


        relayConfigurationBean.setRelay10Use(changeUseFormUi(relayBeans.get(9).getUse()));
        relayConfigurationBean.setRelay10State((byte)relayBeans.get(9).getAction());


        relayConfigurationBean.setRelay11Use(changeUseFormUi(relayBeans.get(10).getUse()));
        relayConfigurationBean.setRelay11State((byte)relayBeans.get(10).getAction());


        return relayConfigurationBean;
    }
}
