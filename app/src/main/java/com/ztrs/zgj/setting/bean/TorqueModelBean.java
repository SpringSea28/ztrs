package com.ztrs.zgj.setting.bean;

import java.util.List;

public class TorqueModelBean {
    String model;
    int bigArmLen;
    int magnification;
    List<TorqueCurveBean> torqueCurveBeanList;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getBigArmLen() {
        return bigArmLen;
    }

    public void setBigArmLen(int bigArmLen) {
        this.bigArmLen = bigArmLen;
    }

    public int getMagnification() {
        return magnification;
    }

    public void setMagnification(int magnification) {
        this.magnification = magnification;
    }

    public List<TorqueCurveBean> getTorqueCurveBeanList() {
        return torqueCurveBeanList;
    }

    public void setTorqueCurveBeanList(List<TorqueCurveBean> torqueCurveBeanList) {
        this.torqueCurveBeanList = torqueCurveBeanList;
    }
}
