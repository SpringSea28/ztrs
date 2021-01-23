package com.ztrs.zgj.device.bean;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PreventCollisionNearBean {
    private static final String TAG = PreventCollisionNearBean.class.getSimpleName();

    private ConcurrentHashMap<Integer, NearCoordinate> nearCoordinates
            = new ConcurrentHashMap<Integer, NearCoordinate>();

    public ConcurrentHashMap<Integer, NearCoordinate> getNearCoordinates() {
        return nearCoordinates;
    }

    public NearCoordinate getNearCoordinate(int number) {
        return nearCoordinates.get(number);
    }

    public void putNearCoordinate(NearCoordinate nearCoordinate) {
        this.nearCoordinates.put((int)nearCoordinate.getNumber(),nearCoordinate);
    }

    public static class NearCoordinate{
        byte number;
        int x;
        int y;

        public byte getNumber() {
            return number;
        }

        public void setNumber(byte number) {
            this.number = number;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

}
