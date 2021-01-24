package com.ztrs.zgj.device;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.AmplitudeCalibrationBean;
import com.ztrs.zgj.device.bean.AroundCalibrationBean;
import com.ztrs.zgj.device.bean.HeightCalibrationBean;
import com.ztrs.zgj.device.bean.InverterDataReportBean;
import com.ztrs.zgj.device.bean.RelayConfigurationBean;
import com.ztrs.zgj.device.bean.RelayOutputControlBean;
import com.ztrs.zgj.device.bean.SensorRealtimeDataBean;
import com.ztrs.zgj.device.bean.StaticParameterBean;
import com.ztrs.zgj.device.bean.WeightCalibrationBean;
import com.ztrs.zgj.device.bean.WireRopeDetectionParametersSetBean;
import com.ztrs.zgj.device.bean.WireRopeDetectionReportBean;
import com.ztrs.zgj.device.bean.WorkCycleDataBean;
import com.ztrs.zgj.device.protocol.CommunicationProtocol;
import com.ztrs.zgj.device.protocol.SwitchMachineProtocol;
import com.ztrs.zgj.device.utils.Crc16;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.ztrs.zgj.device.bean.RegionalRestrictionsBean.*;

public class Test {
    private static final String TAG = Test.class.getSimpleName();

    boolean DEBUG = false;
    //协议3.2 解锁车 下位机请求
    public void testUnLockCarCmd() {
        DeviceManager.getInstance().lockCar();
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
//                        String str = "B3 0E 00 00 14 20 11 18 14 42 46 4C DF 82";
//                        byte[] byteFromStr = getByteFromStr(str);
//        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
//        byteFromStr[byteFromStr.length-2] = (byte)(crc>>8);
//        byteFromStr[byteFromStr.length-1] = (byte)(crc);
//                        DeviceManager.getInstance().onDataReceive(byteFromStr);
//                        if (DeviceManager.getInstance().getZtrsDevice().getUnLockCarBean().getState() != 0X4C) {
//                            LogUtils.LogE(TAG, " testUnlockCarCmd B3 error");
//                        }
                    }
                });
    }

    public void testOnReceiveUnLockCar() {
        String str = "B0 0E 00 00 14 20 11 18 14 42 46 55 11 47";
        byte[] byteFromStr = getByteFromStr(str);
        DeviceManager.getInstance().onDataReceive(byteFromStr);
        if (DeviceManager.getInstance().getZtrsDevice().getUnLockCarBean().getState() != 0X55) {
            LogUtils.LogE(TAG, " testOnReceiveUnLockCar B0 error");
        }
    }

    //协议3.3 开关机
    public void testSwitchMachineCmd() {
        DeviceManager.getInstance().querySwitchMachine();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 16 00 00 1F 20 11 18 14 42 46 01 00 00 00 00 00 00 00 00 b2 af";
                        byte[] byteFromStr = getByteFromStr(str);
//        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
//        byteFromStr[byteFromStr.length-2] = (byte)(crc>>8);
//        byteFromStr[byteFromStr.length-1] = (byte)(crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                        if (DeviceManager.getInstance().getZtrsDevice().getSwitchMachineBean()
                                .getSwitchState() != SwitchMachineProtocol.ON) {
                            LogUtils.LogE(TAG, " testSwitchMachineCmd B3 error");
                        }
                    }
                });
    }

    public void testOnReceiveSwitchMachine() {
        String str = "B0 16 00 00 1F 20 11 18 14 42 46 01 00 00 00 00 00 00 00 00 81 EB";
        byte[] byteFromStr = getByteFromStr(str);
        DeviceManager.getInstance().onDataReceive(byteFromStr);
        if (DeviceManager.getInstance().getZtrsDevice()
                .getSwitchMachineBean().getSwitchState() != SwitchMachineProtocol.ON) {
            LogUtils.LogE(TAG, " testOnReceiveSwitchMachine B0 error");
        }
    }

    //协议3.4
    public void testSectorRegionalRestriction() {
        SectorRestriction sectorRestriction = new SectorRestriction();
        sectorRestriction.setNumber((byte) 1);
        sectorRestriction.setObstacleHigh(257);
        sectorRestriction.setAmplitudeInnerLimit(258);
        sectorRestriction.setAmplitudeOuterLimit(259);
        sectorRestriction.setAngleLeftLimit(230);
        sectorRestriction.setAngleRightLimit(231);
        DeviceManager.getInstance().setSectorRegionalRestriction(sectorRestriction);
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 0E 00 00 21 20 11 18 14 42 49 01 31 84";
                        byte[] byteFromStr = getByteFromStr(str);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                    }
                });
    }

    public void testPolarRegionalRestriction() {
        PolarCoordinate polarCoordinate = new PolarCoordinate();
        polarCoordinate.setNumber((byte) 1);
        polarCoordinate.setObstacleHigh(257);
        PolarCoordinate.Coordinate coordinate = new PolarCoordinate.Coordinate();
        coordinate.setAmplitude(01);
        coordinate.setAngle(02);
        PolarCoordinate.Coordinate coordinate2 = new PolarCoordinate.Coordinate();
        coordinate2.setAmplitude(03);
        coordinate2.setAngle(04);
        List<PolarCoordinate.Coordinate> list = new ArrayList<>();
        list.add(coordinate);
        list.add(coordinate2);
        polarCoordinate.setCoordinateList(list);
        DeviceManager.getInstance().setPolarRegionalRestriction(polarCoordinate);
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 0E 00 00 21 20 11 18 14 42 49 01 31 84";
                        byte[] byteFromStr = getByteFromStr(str);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                    }
                });
    }

    public void testOrthogonalRegionalRestriction() {
        OrthogonalCoordinate orthogonalCoordinate = new OrthogonalCoordinate();
        orthogonalCoordinate.setNumber((byte) 1);
        orthogonalCoordinate.setObstacleHigh(257);
        OrthogonalCoordinate.Coordinate coordinate = new OrthogonalCoordinate.Coordinate();
        coordinate.setX(01);
        coordinate.setY(02);
        OrthogonalCoordinate.Coordinate coordinate2 = new OrthogonalCoordinate.Coordinate();
        coordinate2.setX(03);
        coordinate2.setY(04);
        List<OrthogonalCoordinate.Coordinate> list = new ArrayList<>();
        list.add(coordinate);
        list.add(coordinate2);
        orthogonalCoordinate.setCoordinateList(list);
        DeviceManager.getInstance().setOrthogonalRegionalRestriction(orthogonalCoordinate);
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 0E 00 00 21 20 11 18 14 42 49 01 31 84";
                        byte[] byteFromStr = getByteFromStr(str);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                    }
                });
    }

    public void testQuerySectorRegionalRestriction() {
        DeviceManager.getInstance().queryRegionalRestriction();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "b3 19 00 00 21 20 12 20 10 12 39 01 01 01 01 01 02 01 03 00 e6 00 e7 ab 9c";
                        byte[] byteFromStr = getByteFromStr(str);
//                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
//                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
//                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                        SectorRestriction sectorRestriction = DeviceManager.getInstance()
                                .getZtrsDevice().getRegionalRestrictionsBean().getSectorRestriction();
                        if (sectorRestriction.getAngleLeftLimit() != 230) {
                            LogUtils.LogE(TAG, " testQuerySectorRegionalRestriction B2 error");
                        }
                    }
                });
    }

    public void testQueryPolarRegionalRestriction() {
        DeviceManager.getInstance().queryRegionalRestriction();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "b3 19 00 00 21 20 12 20 14 25 07 01 02 01 01 00 01 00 02 00 03 00 04 5a a5";
                        byte[] byteFromStr = getByteFromStr(str);
                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                        PolarCoordinate polarCoordinate = DeviceManager.getInstance()
                                .getZtrsDevice().getRegionalRestrictionsBean().getPolarCoordinate();
                        if (polarCoordinate.getCoordinateList().get(1).getAngle() != 4) {
                            LogUtils.LogE(TAG, " testQueryPolarRegionalRestriction B2 error");
                        }
                    }
                });
    }

    public void testQueryOrthogonalRegionalRestriction() {
        DeviceManager.getInstance().queryRegionalRestriction();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "b3 21 00 00 21 21 01 18 23 17 57 04 03 00 00 00 0a 00 14 00 1e 00 28 00 32 00 3c 00 46 ff f6 73 4a";
                        byte[] byteFromStr = getByteFromStr(str);
                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);

                        str = "b3 21 00 00 21 21 01 18 23 17 57 03 03 00 10 00 0a 00 14 00 1e 00 28 00 32 00 3c 00 46 ff f6 73 4a";
                        byteFromStr = getByteFromStr(str);
                        crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);

                        str = "b3 21 00 00 21 21 01 18 23 17 57 02 03 00 01 00 0a 00 14 00 1e 00 28 00 32 00 3c 00 46 ff f6 73 4a";
                        byteFromStr = getByteFromStr(str);
                        crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);


                        str = "b3 21 00 00 21 21 01 18 23 17 57 01 03 00 05 00 0a 00 14 00 1e 00 28 00 32 00 3c 00 46 ff f6 73 4a";
                        byteFromStr = getByteFromStr(str);
                        crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);

                        OrthogonalCoordinate orthogonalCoordinate = DeviceManager.getInstance()
                                .getZtrsDevice().getRegionalRestrictionsBean()
                                .getOrthogonalCoordinate(1);
//                        if (orthogonalCoordinate.getCoordinateList().get(1).getY() != 4) {
//                            LogUtils.LogE(TAG, " testQueryOrthogonalRegionalRestriction B2 error");
//                        }
                    }
                });
    }

    //协议3.5
    public void testStaticParameter() {
        StaticParameterBean staticParameterBean = new StaticParameterBean();
        staticParameterBean.setTowerType((byte) 1);
        staticParameterBean.setUpWeightArmLen(2);
        staticParameterBean.setBalanceArmLen(3);
        staticParameterBean.setBalanceArmWidth(4);
        staticParameterBean.setBalanceWeightSagHeight(5);
        staticParameterBean.setTowerCapHeight(6);
        staticParameterBean.setTowerHeight(7);
        int moveArmDistance = 8;
        staticParameterBean.setMoveArmDistance(moveArmDistance);
        int climbingFrameSize = 9;
        staticParameterBean.setClimbingFrameSize(climbingFrameSize);
        int introducePlatformAngle = 10;
        staticParameterBean.setIntroducePlatformAngle(introducePlatformAngle);
        int towerRelativeHeight = 11;
        staticParameterBean.setTowerRelativeHeight(towerRelativeHeight);
        int towerX = 12;
        staticParameterBean.setTowerX(towerX);
        int towerY = 13;
        staticParameterBean.setTowerY(towerY);
        int walkingTrackAngle = 14;
        staticParameterBean.setWalkingTrackAngle(walkingTrackAngle);
        byte carNumber = 10;
        staticParameterBean.setCarNumber(carNumber);
        byte liftingNumber = 12;
        staticParameterBean.setLiftingNumber(liftingNumber);
        byte[] towerCraneType = new byte[]{'a','b','c'};
        staticParameterBean.setTowerCraneType(towerCraneType);
        byte magnification = 2;
        staticParameterBean.setMagnification(magnification);
        int windSpeedAlarmValue = 1;
        staticParameterBean.setWindSpeedAlarmValue(windSpeedAlarmValue);
        int windSpeedWarningValue = 2;
        staticParameterBean.setWindSpeedWarningValue(windSpeedWarningValue);
        int slopeXAlarmValue = 3;
        staticParameterBean.setSlopeXAlarmValue(slopeXAlarmValue);
        int slopeXWarningValue = 4;
        staticParameterBean.setSlopeXWarningValue(slopeXWarningValue);
        int slopeYAlarmValue = 5;
        staticParameterBean.setSlopeYAlarmValue(slopeYAlarmValue);
        int slopeYWarningValue = 6;
        staticParameterBean.setSlopeYWarningValue(slopeYWarningValue);
        DeviceManager.getInstance().setStaticParameter(staticParameterBean);
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 0E 00 00 22 20 11 18 14 42 50 00 74 0E";
                        byte[] byteFromStr = getByteFromStr(str);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                    }
                });
    }


    public void testQueryStaticParameter() {
        DeviceManager.getInstance().queryStaticParameter();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //平常
//                        String str = "b3 48 00 00 22 21 01 09 17 12 42 00 00 3c 00 15 00 00 00 00 00 0a 00 c8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 61 31 32 33 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ff 9c db 72";
                        //风力
//                        String str = "b3 48 00 00 22 21 01 09 20 17 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 05 00 03 00 00 00 00 00 00 00 00 00 00 00 34 40";
                        //倾角
//                        String str = "b3 48 00 00 22 21 01 10 00 03 24 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 c8 fc 18 00 19 fe 0c 00 00 00 a6 f1";
                       //塔机坐标
//                        String str = "b3 48 00 00 22 21 01 17 22 15 03 00 00 00 00 00 00 00 00 00 00 00 03 20 00 00 00 00 00 00 00 00 00 01 ff f1 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 32 cc";
                        String str = "b3 48 00 00 22 21 01 21 22 34 25 00 00 00 00 64 00 00 00 00 01 f4 01 90 00 00 00 00 00 00 00 00";
                        byte[] byteFromStr = getByteFromStr(str);
//                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
//                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
//                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);

                        str = "01 2c 00 c8 00 00 00 54 43 37 30 34 32 00 00 00 00 00 00 00 00 00 00 00 08 00 05 00 96 00 64 00";
                        byteFromStr = getByteFromStr(str);
//                        crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
//                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
//                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);

                        str = "96 00 64 00 00 1e 67 3c";
                        byteFromStr = getByteFromStr(str);
//                        crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
//                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
//                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                        StaticParameterBean staticParameterBean = DeviceManager.getInstance()
                                .getZtrsDevice().getStaticParameterBean();
//                        if (staticParameterBean.getSlopeYWarningValue() != 6) {
//                            LogUtils.LogE(TAG, " testQueryStaticParameter B2 error");
//                        }
                    }
                });
    }


    //测试分包
    public void testPackage() {
        String str = "B0 0E 00 00 14 20 11 18 14 42 46 55 11 47 B0 0E";
        byte[] byteFromStr = getByteFromStr(str);
        DeviceManager.getInstance().onDataReceive(byteFromStr);
        str = "00 00 14 20 11 18 14 42 46 55";
        byteFromStr = getByteFromStr(str);
        DeviceManager.getInstance().onDataReceive(byteFromStr);
        str = "11 47 B0 0E 00 00 14 20 11 18 14 42 46 55 11 47";
        byteFromStr = getByteFromStr(str);
        DeviceManager.getInstance().onDataReceive(byteFromStr);
    }

    //协议3.6 实时数据
    public void testOnReceiveRealtimedata() {
//        String str = "b0 3b 00 00 23 20 11 18 14 45 03 00 00 08 00 c0 f3 ff f7 d0 fc ff f0 ff 70 ff f0 00 00 00 01 00 b0";
//        String str2 = "f9 02 00 08 00 00 00 00 00 00 00 00 00 00 00 00 00 00 29 ff d6 00 00 00 ec 8e";
//        String str = "b3 09 00 01 01 00 00 6e c9";
        String str = "B0 3B 00 00 23 20 11 18 14 44 02 00 00 08 00 C0 F3 FF FF F0 0C FF F0 FF F0 FF F0 00 00 00 B0 00 96 01 F4 00 A0 00 00 00 00 00 00 00 00 1F 40 00 00 00 FF 6A 00 96 01 13 00 C5 C8";
//        String str = "B0 3B 00 00 23 20 11 18 14 42 58 00 00 08 00 C0 F3 FF FF F0 0C FF F0 FF F0 FF F0 00 00 00 04 02 04 0B 8E 02 12 00 00 00 03 00 00 00 00 1F 40 00 00 01 00 2A FF D7 00 14 01 E8 1B";
        byte[] byteFromStr = getByteFromStr(str);
//        byte[] byteFromStr2 = getByteFromStr(str2);
        LogUtils.LogE(TAG, " bytefrom str: " + byteFromStr.length);
//        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
//        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
//        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
        DeviceManager.getInstance().onDataReceive(byteFromStr);
//        DeviceManager.getInstance().onDataReceive(byteFromStr2);
//        if (DeviceManager.getInstance().getZtrsDevice()
//                .getRealTimeDataBean().getySlope() != 256) {
//            LogUtils.LogE(TAG, " testOnReceiveRealtimedata B0 error");
//        }
    }

    //协议3.7
    public void testQueryWorkCycleData() {
        DeviceManager.getInstance().queryWorkCycleData();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 3B 00 00 24 20 11 18 14 42 46 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 83 D5";
                        byte[] byteFromStr = getByteFromStr(str);
                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                        WorkCycleDataBean data = DeviceManager.getInstance()
                                .getZtrsDevice().getWorkCycleDataBean();
                        if (data.getStopY() != 256) {
                            LogUtils.LogE(TAG, " testQueryWorkCycleData B2 error");
                        }
                    }
                });
    }

    //3.9
    public void setRelayConfiguration() {
        RelayConfigurationBean relayConfigurationBean = new RelayConfigurationBean();
        relayConfigurationBean.setRelay1State((byte) 2);
        relayConfigurationBean.setRelay1Use((byte) 1);
        DeviceManager.getInstance().setRelayConfiguration(relayConfigurationBean);
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 0E 00 00 31 20 11 18 14 42 52 00 0D 4E";
                        byte[] byteFromStr = getByteFromStr(str);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                    }
                });
    }

    public void queryRelayConfiguration() {
        DeviceManager.getInstance().queryRelayConfiguration();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "b3 25 00 00 31 21 01 23 14 07 57 01 01 0a 02 08 01 00 02 00 02 00 02 00 02 00 02 00 02 00 02 00 02 00 00 8d 67";

                        byte[] byteFromStr = getByteFromStr(str);
                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                        RelayConfigurationBean data = DeviceManager.getInstance()
                                .getZtrsDevice().getRelayConfigurationBean();

                    }
                });
    }

    //3.10
    public void setRelayOutputControl() {
        RelayOutputControlBean relayOutputControlBean = new RelayOutputControlBean();
        relayOutputControlBean.setRelayOutputControl12((byte) 1);
        relayOutputControlBean.setRelayOutputControl11((byte) 1);
        DeviceManager.getInstance().setRelayOutputControl(relayOutputControlBean);
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 0E 00 00 32 20 11 18 14 42 48 00 78 05";
                        byte[] byteFromStr = getByteFromStr(str);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                    }
                });
    }

    public void queryRelayOutputControl() {
        DeviceManager.getInstance().queryRelayOutputConfig();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "b3 19 00 00 32 20 12 23 23 49 07 00 00 00 00 00 00 00 00 00 00 01 01 eb 8c";

                        byte[] byteFromStr = getByteFromStr(str);
                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                        RelayOutputControlBean data = DeviceManager.getInstance()
                                .getZtrsDevice().getRelayOutputControlBean();
                        if (data.getRelayOutputControl12() != 1) {
                            LogUtils.LogE(TAG, " queryRelayOutputControl B2 error");
                        }
                    }
                });
    }

    //3.11
    public void emergencyCall() {
        DeviceManager.getInstance().emergencyCall();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 0E 00 00 33 20 11 18 14 42 49 00 24 C5";

                        byte[] byteFromStr = getByteFromStr(str);
//                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
//                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
//                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                    }
                });
    }


    //3.12
    public void querySensorRealtimeData() {
        DeviceManager.getInstance().querySensorRealtimeData();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
//                        String str = "B3 29 00 00 34 20 11 18 14 42 47 80 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 80 00 00 01 4D 7D";
                        String str = "B3 29 00 00 34 21 01 21 20 49 20 00 00 00 00 00 00 00 00 00 01 86 A0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 71 23";
                        byte[] byteFromStr = getByteFromStr(str);
                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                        SensorRealtimeDataBean sensorRealtimeDataBean = DeviceManager.getInstance().getZtrsDevice().getSensorRealtimeDataBean();
                        if (sensorRealtimeDataBean.getWindSpeedSensor() != 2147483649l) {
                            LogUtils.LogE(TAG, " querySensorRealtimeData B2 error");
                        }
                    }
                });
    }

    public void onReceiveSensorRealtimeData() {
        String str = "B0 29 00 00 34 20 11 18 14 42 46 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 F8 A6";
        byte[] byteFromStr = getByteFromStr(str);
//        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
//        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
//        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
        DeviceManager.getInstance().onDataReceive(byteFromStr);
    }


    //3.13
    public void heightCalibration() {
        HeightCalibrationBean heightCalibrationBean = new HeightCalibrationBean();
        heightCalibrationBean.setCurrent1(0x00000001);
        heightCalibrationBean.setHighAlarmValue(0x0100);
        DeviceManager.getInstance().heightCalibration(heightCalibrationBean);
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 0E 00 00 35 20 11 18 14 42 49 00 0E 45 ";
                        byte[] byteFromStr = getByteFromStr(str);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                    }
                });
    }

    public void queryHeightCalibration() {
        DeviceManager.getInstance().queryHeightCalibration();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "b3 25 00 00 35 21 01 09 20 03 15 00 00 00 7b 01 92 00 00 01 41 03 23 00 12 00 0f 09 01 00 30 00 23 02 00 c2 2b";

                        byte[] byteFromStr = getByteFromStr(str);
                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                        HeightCalibrationBean heightCalibrationBean = DeviceManager.getInstance()
                                .getZtrsDevice().getHeightCalibrationBean();
//                        if (heightCalibrationBean.getHighAlarmValue() != 256) {
//                            LogUtils.LogE(TAG, " queryHeightCalibration B2 error");
//                        }
                    }
                });
    }


    //3.14
    public void amplitudeCalibration() {
        AmplitudeCalibrationBean calibrationBean = new AmplitudeCalibrationBean();
        calibrationBean.setCurrent1(0x00000001);
        calibrationBean.setHighAlarmValue(0x0100);
        DeviceManager.getInstance().amplitudeCalibration(calibrationBean);
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 0E 00 00 36 20 11 18 14 42 47 00 7B 01";
                        byte[] byteFromStr = getByteFromStr(str);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                    }
                });
    }

    //3.14
    public void queryAmplitudeCalibration() {
        DeviceManager.getInstance().queryAmplitudeCalibration();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "b3 25 00 00 36 21 01 09 20 05 48 00 00 04 d2 00 15 00 00 00 e7 00 21 00 e7 00 69 01 01 01 31 00 c8 02 00 6b de";

                        byte[] byteFromStr = getByteFromStr(str);
                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                        AmplitudeCalibrationBean calibrationBean = DeviceManager.getInstance()
                                .getZtrsDevice().getAmplitudeCalibrationBean();
//                        if (calibrationBean.getHighAlarmValue() != 256) {
//                            LogUtils.LogE(TAG, " queryAmplitudeCalibration B2 error");
//                        }
                    }
                });
    }

    //3.15
    public void aroundCalibration() {
        AroundCalibrationBean calibrationBean = new AroundCalibrationBean();
        calibrationBean.setCurrent1(0x00000001);
        calibrationBean.setHighAlarmValue(0x0100);
        DeviceManager.getInstance().aroundCalibration(calibrationBean);
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 0E 00 00 37 20 11 18 14 42 50 00 47 CF";
                        byte[] byteFromStr = getByteFromStr(str);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                    }
                });
    }

    //3.15
    public void queryAroundCalibration() {
        DeviceManager.getInstance().queryAroundCalibration();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "b3 25 00 00 37 21 01 09 20 05 48 00 00 04 d2 ff f6 00 00 00 e7 00 81 00 e7 00 69 01 01 01 31 00 c8 02 00 6b de";

                        byte[] byteFromStr = getByteFromStr(str);
                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                        AroundCalibrationBean calibrationBean = DeviceManager.getInstance()
                                .getZtrsDevice().getAroundCalibrationBean();
                        if (calibrationBean.getHighAlarmValue() != 256) {
                            LogUtils.LogE(TAG, " queryAroundCalibration B2 error");
                        }
                    }
                });
    }

    //3.16
    public void weightCalibration() {
        WeightCalibrationBean calibrationBean = new WeightCalibrationBean();
        calibrationBean.setCurrent1(0x00000001);
        calibrationBean.setHighAlarmValue(0x0100);
        DeviceManager.getInstance().weightCalibration(calibrationBean);
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 0E 00 00 38 20 11 18 14 42 55 00 57 8C";
                        byte[] byteFromStr = getByteFromStr(str);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                    }
                });
    }


    public void queryWeightCalibration() {
        DeviceManager.getInstance().queryWeightCalibration();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "b3 25 00 00 38 21 01 09 19 32 50 00 01 e2 40 07 d0 00 09 fb f1 03 e8 03 84 00 00 00 02 04 1a 00 00 01 00 de 62";
                        byte[] byteFromStr = getByteFromStr(str);
                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                        AroundCalibrationBean calibrationBean = DeviceManager.getInstance()
                                .getZtrsDevice().getAroundCalibrationBean();
//                        if (calibrationBean.getHighAlarmValue() != 0) {
//                            LogUtils.LogE(TAG, " queryWeightCalibration B2 error");
//                        }
                    }
                });
    }

    //3.21
    public void setWireRopeDetectionParameters() {
        WireRopeDetectionParametersSetBean bean = new WireRopeDetectionParametersSetBean();
        bean.setThreshold(0x0101);
        bean.setDetectionState((byte)1);
        DeviceManager.getInstance().setWireRopeDetectionParameters(bean);
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "B3 0E 00 00 44 20 11 18 14 42 55 00 57 8C";
                        byte[] byteFromStr = getByteFromStr(str);
                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                    }
                });
    }


    public void queryWireRopeDetectionParameters() {
        DeviceManager.getInstance().queryWireRopeDetectionParameters();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "b3 24 00 00 44 21 01 18 21 19 16 01 2c 00 01 36 01 40 01 4a 01 54 01 5e 03 20 00 5a 00 64 00 23 00 6e 8f e0";
                        byte[] byteFromStr = getByteFromStr(str);
                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                        WireRopeDetectionParametersSetBean bean = DeviceManager.getInstance()
                                .getZtrsDevice().getWireRopeDetectionParametersSetBean();
//                        if (bean.getThreshold() != 257) {
//                            LogUtils.LogE(TAG, " queryWireRopeDetectionParameters B2 error");
//                        }
                    }
                });
    }

    //3.22
    public void onReceiveWireRopeDetectionReport() {
        byte[] data = new byte[4];
        data[0] = 1;
        data[1] =1;
        data[2] = 1;
        byte[] bytes = CommunicationProtocol.packetData((byte) 0xB0, (byte) 0x45, data);
//        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
//        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
//        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
        DeviceManager.getInstance().onDataReceive(bytes);
        WireRopeDetectionReportBean wireRopeDetectionReportBean = DeviceManager.getInstance().getZtrsDevice().getWireRopeDetectionReportBean();
        if(wireRopeDetectionReportBean.getDamageValue() != 257){
            LogUtils.LogE(TAG, " onReceiveWireRopeDetectionReport B0 error");
        }
    }



    //3.23
    public void onReceiveInverterData() {
        byte[] data = new byte[204];
        data[66] = 1;
        data[66+68] =1;
        byte[] bytes = CommunicationProtocol.packetData((byte) 0xB0, (byte) 0x46, data);
//        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
//        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
//        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
        DeviceManager.getInstance().onDataReceive(bytes);
        InverterDataReportBean.InverterData upInverterData = DeviceManager.getInstance().getZtrsDevice()
                .getInverterDataReportBean().getUpInverterData();
        int currentLuffingPositionCurrentUpWeight = upInverterData.getCurrentLuffingPositionCurrentUpWeight();
        if(currentLuffingPositionCurrentUpWeight != 256){
            LogUtils.LogE(TAG, " onReceiveInverterData B0 error");
        }
        LogUtils.LogE(TAG, " test onReceive getCurrentLuffingPositionCurrentUpWeight:"+DeviceManager.getInstance().getZtrsDevice()
                .getInverterDataReportBean().getAroundInverterData()
                .getCurrentLuffingPositionCurrentUpWeight());
    }

    //协议3.27 设备注册信息
    public void testOnReceiveRegisterInfo() {
        String str = "B0 33 00 00 01 20 11 18 14 43 00 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 54 43 32 30 32 31 30 31 30 30 30 31 01 00 01 00 01 04 01 00 83 23";
        byte[] byteFromStr = getByteFromStr(str);
        LogUtils.LogE(TAG, " bytefrom str: " + byteFromStr.length);
        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
        DeviceManager.getInstance().onDataReceive(byteFromStr);
    }

    //3.14
    public void queryTorqueCalibration() {
        DeviceManager.getInstance().queryTorqueCalibration();
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String str = "b3 15 00 00 43 21 01 09 20 15 53 00 50 02 02 00 64 03 01 bd e9";

                        byte[] byteFromStr = getByteFromStr(str);
                        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
                        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
                        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
                        DeviceManager.getInstance().onDataReceive(byteFromStr);
                    }
                });
    }

    public void testOnReceiveTorqueCurve() {
        String str = "b3 7f 00 00 53 21 01 12 21 39 36 54 43 54 37 30 34 30 00 00 00 00 00 00 00 00 00 46 04 09 ec 36 b0 0a 28 35 52 0a f0 31 06 0b b8 2d 50 0c 80 2a 12 0d 48 27 33 0e 10 24 a4 0e d8 22 60 0f a0 20 53 10 68 1e 78 11 30 1c ca 11 f8 1b 3a 12 c0 19 d2 13 88 18 88 14 50 17 52 15 18 16 3a 15 e0 15 31 16 a8 14 3c 17 70 13 56 18 38 12 7f 19 00 11 b2 19 c8 10 f9 1a 90 10 45 1b 58 0f a0 4a e7";
        byte[] byteFromStr = getByteFromStr(str);
//        byte[] byteFromStr2 = getByteFromStr(str2);

        LogUtils.LogE(TAG, " bytefrom str: " + byteFromStr.length);
        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
        DeviceManager.getInstance().onDataReceive(byteFromStr);
//        DeviceManager.getInstance().onDataReceive(byteFromStr2);
//        if (DeviceManager.getInstance().getZtrsDevice()
//                .getRealTimeDataBean().getySlope() != 256) {
//            LogUtils.LogE(TAG, " testOnReceiveRealtimedata B0 error");
//        }
    }

    //
    public void testOnReceivePreventCollisionAlarm() {
        String str = "b3 15 00 00 61 21 01 19 01 23 05 00 0f 00 14 00 1e ff d8 43 54";
        byte[] byteFromStr = getByteFromStr(str);
        LogUtils.LogE(TAG, " bytefrom str: " + byteFromStr.length);
        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
        DeviceManager.getInstance().onDataReceive(byteFromStr);
    }

    public void testNear() {
        String str = "b0 12 00 00 62 21 01 19 01 23 05 01 0f 00 14 00 43 54";
        byte[] byteFromStr = getByteFromStr(str);
        LogUtils.LogE(TAG, " bytefrom str: " + byteFromStr.length);
        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
        DeviceManager.getInstance().onDataReceive(byteFromStr);

        str = "b0 12 00 00 62 21 01 19 01 23 05 02 0f 00 ff d8 43 54";
        byteFromStr = getByteFromStr(str);
        LogUtils.LogE(TAG, " bytefrom str: " + byteFromStr.length);
        crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
        DeviceManager.getInstance().onDataReceive(byteFromStr);

        str = "b0 12 00 00 62 21 01 19 01 23 05 03 0f 01 ff d8 43 54";
        byteFromStr = getByteFromStr(str);
        LogUtils.LogE(TAG, " bytefrom str: " + byteFromStr.length);
        crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
        DeviceManager.getInstance().onDataReceive(byteFromStr);

        str = "b0 12 00 00 62 21 01 19 01 23 05 01 00 01 ff d8 43 54";
        byteFromStr = getByteFromStr(str);
        LogUtils.LogE(TAG, " bytefrom str: " + byteFromStr.length);
        crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
        DeviceManager.getInstance().onDataReceive(byteFromStr);
    }

    public void testVersion() {
        String str = "b0 0f 00 00 50 21 01 24 21 05 25 10 10 56 ae";
        byte[] byteFromStr = getByteFromStr(str);
        LogUtils.LogE(TAG, " bytefrom str: " + byteFromStr.length);
        int crc = Crc16.getCRC(byteFromStr, byteFromStr.length - 2);
        byteFromStr[byteFromStr.length - 2] = (byte) (crc >> 8);
        byteFromStr[byteFromStr.length - 1] = (byte) (crc);
        DeviceManager.getInstance().onDataReceive(byteFromStr);
    }


    private byte[] getByteFromStr(String str) {
        String[] s = str.split(" ");
        byte[] data = new byte[s.length];
        for (int i = 0; i < s.length; i++) {
            String hexStr = s[i];
            int hex = Integer.valueOf(hexStr, 16);
            data[i] = (byte) hex;
        }
        return data;
    }

}
