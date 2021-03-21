package com.ztrs.zgj.device.bean;

public class AnnounecmentBean {
    private static final String TAG = AnnounecmentBean.class.getSimpleName();
    private boolean read = true;
    byte[] announcement;

    public byte[] getAnnouncement() {
        read  = true;
        return announcement;
    }

    public void setAnnouncement(byte[] announcement) {
        this.announcement = announcement;
        read = false;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
