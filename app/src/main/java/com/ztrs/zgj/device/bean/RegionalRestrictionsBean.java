package com.ztrs.zgj.device.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RegionalRestrictionsBean {
    private static final String TAG = RegionalRestrictionsBean.class.getSimpleName();
    private SectorRestriction sectorRestriction = new SectorRestriction();
    private PolarCoordinate polarCoordinate = new PolarCoordinate();
    private ConcurrentHashMap<Integer,OrthogonalCoordinate> orthogonalCoordinates = new ConcurrentHashMap<Integer,OrthogonalCoordinate>();

    public SectorRestriction getSectorRestriction() {
        return sectorRestriction;
    }

    public void setSectorRestriction(SectorRestriction sectorRestriction) {
        this.sectorRestriction = sectorRestriction;
    }

    public PolarCoordinate getPolarCoordinate() {
        return polarCoordinate;
    }

    public void setPolarCoordinate(PolarCoordinate polarCoordinate) {
        this.polarCoordinate = polarCoordinate;
    }

    public ConcurrentHashMap<Integer,OrthogonalCoordinate> getOrthogonalCoordinates() {
        return orthogonalCoordinates;
    }

    public OrthogonalCoordinate getOrthogonalCoordinate(int number) {
        return orthogonalCoordinates.get(number);
    }

    public void putOrthogonalCoordinate(OrthogonalCoordinate orthogonalCoordinate) {
        this.orthogonalCoordinates.put((int)orthogonalCoordinate.getNumber(),orthogonalCoordinate);
    }

    public static class OrthogonalCoordinate {
        byte number;
        int obstacleHigh; //单位0.1m
        List<Coordinate> coordinateList;

        public byte getNumber() {
            return number;
        }

        public void setNumber(byte number) {
            this.number = number;
        }

        public int getObstacleHigh() {
            return obstacleHigh;
        }

        public void setObstacleHigh(int obstacleHigh) {
            this.obstacleHigh = obstacleHigh;
        }

        public List<Coordinate> getCoordinateList() {
            return coordinateList;
        }

        public void setCoordinateList(List<Coordinate> coordinateList) {
            this.coordinateList = coordinateList;
        }

        public static class Coordinate{
            int x;
            int y;

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

        public OrthogonalCoordinate copy(){
            OrthogonalCoordinate orthogonalCoordinate = new OrthogonalCoordinate();
            orthogonalCoordinate.number = this.number;
            orthogonalCoordinate.obstacleHigh = this.obstacleHigh;
            List<Coordinate> coordinateList = new ArrayList<>();
            if(this.coordinateList !=null){
                for(int i=0;i<coordinateList.size();i++){
                    Coordinate coordinate = new Coordinate();
                    coordinate.setX(coordinateList.get(i).getX());
                    coordinate.setY(coordinateList.get(i).getY());
                    coordinateList.add(coordinate);
                }
            }
            orthogonalCoordinate.setCoordinateList(coordinateList);
            return orthogonalCoordinate;
        }
    }

    public static class PolarCoordinate{
        byte number;
        int obstacleHigh; //单位0.1m
        List<Coordinate> coordinateList;

        public byte getNumber() {
            return number;
        }

        public void setNumber(byte number) {
            this.number = number;
        }

        public int getObstacleHigh() {
            return obstacleHigh;
        }

        public void setObstacleHigh(int obstacleHigh) {
            this.obstacleHigh = obstacleHigh;
        }

        public List<Coordinate> getCoordinateList() {
            return coordinateList;
        }

        public void setCoordinateList(List<Coordinate> coordinateList) {
            this.coordinateList = coordinateList;
        }

        public static class Coordinate{
            int amplitude;
            int angle;

            public int getAmplitude() {
                return amplitude;
            }

            public void setAmplitude(int amplitude) {
                this.amplitude = amplitude;
            }

            public int getAngle() {
                return angle;
            }

            public void setAngle(int angle) {
                this.angle = angle;
            }
        }
    }

    public static class SectorRestriction{
        byte number;
        int obstacleHigh; //单位0.1m
        int amplitudeInnerLimit;
        int amplitudeOuterLimit;
        int angleLeftLimit;
        int angleRightLimit;

        public byte getNumber() {
            return number;
        }

        public void setNumber(byte number) {
            this.number = number;
        }

        public int getObstacleHigh() {
            return obstacleHigh;
        }

        public void setObstacleHigh(int obstacleHigh) {
            this.obstacleHigh = obstacleHigh;
        }

        public int getAmplitudeInnerLimit() {
            return amplitudeInnerLimit;
        }

        public void setAmplitudeInnerLimit(int amplitudeInnerLimit) {
            this.amplitudeInnerLimit = amplitudeInnerLimit;
        }

        public int getAmplitudeOuterLimit() {
            return amplitudeOuterLimit;
        }

        public void setAmplitudeOuterLimit(int amplitudeOuterLimit) {
            this.amplitudeOuterLimit = amplitudeOuterLimit;
        }

        public int getAngleLeftLimit() {
            return angleLeftLimit;
        }

        public void setAngleLeftLimit(int angleLeftLimit) {
            this.angleLeftLimit = angleLeftLimit;
        }

        public int getAngleRightLimit() {
            return angleRightLimit;
        }

        public void setAngleRightLimit(int angleRightLimit) {
            this.angleRightLimit = angleRightLimit;
        }
    }
}
