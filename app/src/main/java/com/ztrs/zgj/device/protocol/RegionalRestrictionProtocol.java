package com.ztrs.zgj.device.protocol;

import android.util.Log;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.RegionalRestrictionMessage;

import java.util.ArrayList;
import java.util.List;

import static com.ztrs.zgj.device.bean.RegionalRestrictionsBean.*;

public class RegionalRestrictionProtocol extends BaseProtocol{
    private static final String TAG= RegionalRestrictionProtocol.class.getSimpleName();

    public static final byte CMD_REGIONAL_RESTRICTION = (byte) 0x21;//区域限制
    private static final byte TYPE_SECTOR= 1;
    private static final byte TYPE_POLAR = 2;
    private static final byte TYPE_ORTHOGONAL = 3;

    private static final int NUMBER_OFFSET = 0;
    private static final int TYPE_OFFSET = NUMBER_OFFSET + 1;
    private static final int OBSTACLE_HIGH_OFFSET = TYPE_OFFSET + 1;

    private static final int AMPLITUDE_INNER_LIMIT_OFFSET = OBSTACLE_HIGH_OFFSET + 2;
    private static final int AMPLITUDE_OUTER_LIMIT_OFFSET = AMPLITUDE_INNER_LIMIT_OFFSET + 2;
    private static final int ANGLE_LEFT_LIMIT_OFFSET = AMPLITUDE_OUTER_LIMIT_OFFSET + 2;
    private static final int ANGLE_RIGHT_LIMIT_OFFSET = ANGLE_LEFT_LIMIT_OFFSET + 2;

    private static final int POLAR_COORDINATE_OFFSET = OBSTACLE_HIGH_OFFSET +2;

    private static final int ORTHOGONAL_COORDINATE_OFFSET = OBSTACLE_HIGH_OFFSET +2;

    private DeviceOperateInterface deviceOperateInterface;

    public RegionalRestrictionProtocol(DeviceOperateInterface deviceOperateInterface){
        super(TAG);
        this.deviceOperateInterface = deviceOperateInterface;
    }

    public void parseCmd(byte[] data){
//        boolean success = parseData(data);
//        if(!success){
//            return;
//        }
//        byte number = data[NUMBER_OFFSET];
//        byte[] ack = CommunicationProtocol.packetAck(CMD_REGIONAL_RESTRICTION, new byte[]{number});
//        deviceOperateInterface.sendDataToDevice(ack);
    }

    public void parseAck(byte[] data){
        if(data.length == 1){
            ackReceive(data);
            return;
        }
        boolean success = parseData(data);
        if(!success){
            ackReceiveError();
            return;
        }
        ackReceive(data);
    }

    private boolean parseData(byte[] data){
        if(data.length < 5){
            LogUtils.LogE(TAG,"data length error");
            return false;
        }
        byte number = data[NUMBER_OFFSET];
        byte type = data[TYPE_OFFSET];
        int obstacleHigh = ((data[OBSTACLE_HIGH_OFFSET]&0x00ff) << 8)
                |(data[OBSTACLE_HIGH_OFFSET+1]&0x00ff);
        switch (type){
            case TYPE_SECTOR:
                parseSector(number,obstacleHigh,data);
                break;
            case TYPE_POLAR:
                parsePolar(number,obstacleHigh,data);
                break;
            case TYPE_ORTHOGONAL:
                parseOrthogonal(number,obstacleHigh,data);
                break;
        }
        return true;
    }

    private boolean parseSector(byte number,int obstacleHigh ,byte[] data){
        if(data.length != ANGLE_RIGHT_LIMIT_OFFSET+2){
            LogUtils.LogE(TAG,"sector data length error");
            return false;
        }
        int amplitudeInnerLimit = ((data[AMPLITUDE_INNER_LIMIT_OFFSET]&0x00ff) << 8)
                |(data[AMPLITUDE_INNER_LIMIT_OFFSET+1]&0x00ff);
        int amplitudeOuterLimit =((data[AMPLITUDE_OUTER_LIMIT_OFFSET]&0x00ff) << 8)
                |(data[AMPLITUDE_OUTER_LIMIT_OFFSET+1]&0x00ff);
        int angleLeftLimit =((data[ANGLE_LEFT_LIMIT_OFFSET]&0x00ff) << 8)
                |(data[ANGLE_LEFT_LIMIT_OFFSET+1]&0x00ff);
        int angleRightLimit =((data[ANGLE_RIGHT_LIMIT_OFFSET]&0x00ff) << 8)
                |(data[ANGLE_RIGHT_LIMIT_OFFSET+1]&0x00ff);

        SectorRestriction sectorRestriction = new SectorRestriction();
        sectorRestriction.setNumber(number);
        sectorRestriction.setObstacleHigh(obstacleHigh);
        sectorRestriction.setAmplitudeInnerLimit(amplitudeInnerLimit);
        sectorRestriction.setAmplitudeOuterLimit(amplitudeOuterLimit);
        sectorRestriction.setAngleLeftLimit(angleLeftLimit);
        sectorRestriction.setAngleRightLimit(angleRightLimit);
        deviceOperateInterface.getZtrsDevice().getRegionalRestrictionsBean().setSectorRestriction(sectorRestriction);
        return true;
    }

    private boolean parsePolar(byte number,int obstacleHigh ,byte[] data){
        List<PolarCoordinate.Coordinate> coordinates = new
                ArrayList<>();
        for(int i= 0;i<(data.length-POLAR_COORDINATE_OFFSET)/4;i++){
            int amplitude = ((data[POLAR_COORDINATE_OFFSET+4*i]&0x00ff) << 8)
                    |(data[POLAR_COORDINATE_OFFSET+4*i+1]&0x00ff);
            int angle = ((data[POLAR_COORDINATE_OFFSET+4*i+2]&0x00ff) << 8)
                    |(data[POLAR_COORDINATE_OFFSET+4*i+3]&0x00ff);
            PolarCoordinate.Coordinate coordinate = new PolarCoordinate.Coordinate();
            coordinate.setAmplitude(amplitude);
            coordinate.setAngle(angle);
            coordinates.add(coordinate);
        }

        PolarCoordinate polarCoordinate = new PolarCoordinate();
        polarCoordinate.setNumber(number);
        polarCoordinate.setObstacleHigh(obstacleHigh);
        polarCoordinate.setCoordinateList(coordinates);
        deviceOperateInterface.getZtrsDevice().getRegionalRestrictionsBean().setPolarCoordinate(polarCoordinate);
        return true;
    }

    private boolean parseOrthogonal(byte number,int obstacleHigh ,byte[] data){
        List<OrthogonalCoordinate.Coordinate> coordinates = new
                ArrayList<>();
        for(int i= 0;i<(data.length-ORTHOGONAL_COORDINATE_OFFSET)/4;i++){
            int x = ((data[ORTHOGONAL_COORDINATE_OFFSET+4*i]&0xffffffff) << 8)
                    |(data[ORTHOGONAL_COORDINATE_OFFSET+4*i+1]&0x00ff);
            int y = ((data[ORTHOGONAL_COORDINATE_OFFSET+4*i+2]&0xffffffff) << 8)
                    |(data[ORTHOGONAL_COORDINATE_OFFSET+4*i+3]&0x00ff);
            OrthogonalCoordinate.Coordinate coordinate = new OrthogonalCoordinate.Coordinate();
            coordinate.setX(x);
            coordinate.setY(y);
            coordinates.add(coordinate);
        }

        OrthogonalCoordinate orthogonalCoordinate = new OrthogonalCoordinate();
        orthogonalCoordinate.setNumber(number);
        orthogonalCoordinate.setObstacleHigh(obstacleHigh);
        orthogonalCoordinate.setCoordinateList(coordinates);
        deviceOperateInterface.getZtrsDevice().getRegionalRestrictionsBean()
                .putOrthogonalCoordinate(orthogonalCoordinate);
        return true;
    }

    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void queryRegionalRestriction(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_REGIONAL_RESTRICTION,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        RegionalRestrictionMessage msg = new RegionalRestrictionMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

    public void setSectorRegionalRestriction(SectorRestriction sectorRegionalRestriction){
        byte[] data = new byte[ANGLE_RIGHT_LIMIT_OFFSET+2];
        data[NUMBER_OFFSET] = sectorRegionalRestriction.getNumber();
        data[TYPE_OFFSET] = TYPE_SECTOR;

        int obstacleHigh = sectorRegionalRestriction.getObstacleHigh();
        data[OBSTACLE_HIGH_OFFSET] = (byte)(obstacleHigh>>8);
        data[OBSTACLE_HIGH_OFFSET+1] = (byte)obstacleHigh;

        int amplitudeInnerLimit = sectorRegionalRestriction.getAmplitudeInnerLimit();
        data[AMPLITUDE_INNER_LIMIT_OFFSET] = (byte)(amplitudeInnerLimit>>8);
        data[AMPLITUDE_INNER_LIMIT_OFFSET+1] = (byte)amplitudeInnerLimit;

        int amplitudeOuterLimit = sectorRegionalRestriction.getAmplitudeOuterLimit();
        data[AMPLITUDE_OUTER_LIMIT_OFFSET] = (byte)(amplitudeOuterLimit>>8);
        data[AMPLITUDE_OUTER_LIMIT_OFFSET+1] = (byte)amplitudeOuterLimit;

        int angleLeftLimit = sectorRegionalRestriction.getAngleLeftLimit();
        data[ANGLE_LEFT_LIMIT_OFFSET] = (byte)(angleLeftLimit>>8);
        data[ANGLE_LEFT_LIMIT_OFFSET+1] = (byte)angleLeftLimit;

        int angleRightLimit = sectorRegionalRestriction.getAngleRightLimit();
        data[ANGLE_RIGHT_LIMIT_OFFSET] = (byte)(angleRightLimit>>8);
        data[ANGLE_RIGHT_LIMIT_OFFSET+1] = (byte)angleRightLimit;

        byte[] cmd = CommunicationProtocol.packetCmd(CMD_REGIONAL_RESTRICTION,data);
        deviceOperateInterface.sendDataToDevice(cmd);
        RegionalRestrictionMessage msg = new RegionalRestrictionMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

    public void setPolarRegionalRestriction(PolarCoordinate polarCoordinate){
        List<PolarCoordinate.Coordinate> coordinateList = polarCoordinate.getCoordinateList();
        byte[] data = new byte[OBSTACLE_HIGH_OFFSET+2+coordinateList.size()*4];
        data[NUMBER_OFFSET] = polarCoordinate.getNumber();
        data[TYPE_OFFSET] = TYPE_POLAR;

        int obstacleHigh = polarCoordinate.getObstacleHigh();
        data[OBSTACLE_HIGH_OFFSET] = (byte)(obstacleHigh>>8);
        data[OBSTACLE_HIGH_OFFSET+1] = (byte)obstacleHigh;
        for(int i=0;i<coordinateList.size();i++){
            int amplitude = coordinateList.get(i).getAmplitude();
            data[POLAR_COORDINATE_OFFSET+4*i] = (byte)(amplitude>>8);
            data[POLAR_COORDINATE_OFFSET+4*i+1] = (byte)(amplitude);
            int angle = coordinateList.get(i).getAngle();
            data[POLAR_COORDINATE_OFFSET+4*i+2] = (byte)(angle>>8);
            data[POLAR_COORDINATE_OFFSET+4*i+3] = (byte)(angle);
        }
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_REGIONAL_RESTRICTION,data);
        deviceOperateInterface.sendDataToDevice(cmd);
        RegionalRestrictionMessage msg = new RegionalRestrictionMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

    public void setOrthogonalRegionalRestriction(OrthogonalCoordinate orthogonalCoordinate){
        List<OrthogonalCoordinate.Coordinate> coordinateList = orthogonalCoordinate.getCoordinateList();
        byte[] data = new byte[OBSTACLE_HIGH_OFFSET+2+coordinateList.size()*4];
        data[NUMBER_OFFSET] = orthogonalCoordinate.getNumber();
        data[TYPE_OFFSET] = TYPE_ORTHOGONAL;

        int obstacleHigh = orthogonalCoordinate.getObstacleHigh();
        data[OBSTACLE_HIGH_OFFSET] = (byte)(obstacleHigh>>8);
        data[OBSTACLE_HIGH_OFFSET+1] = (byte)obstacleHigh;
        for(int i=0;i<coordinateList.size();i++){
            int x = coordinateList.get(i).getX();
            data[POLAR_COORDINATE_OFFSET+4*i] = (byte)(x>>8);
            data[POLAR_COORDINATE_OFFSET+4*i+1] = (byte)(x);
            int y = coordinateList.get(i).getY();
            data[POLAR_COORDINATE_OFFSET+4*i+2] = (byte)(y>>8);
            data[POLAR_COORDINATE_OFFSET+4*i+3] = (byte)(y);
        }
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_REGIONAL_RESTRICTION,data);
        deviceOperateInterface.sendDataToDevice(cmd);
        RegionalRestrictionMessage msg = new RegionalRestrictionMessage(CommunicationProtocol.seq++,cmd);
        msg.setCmdType(BaseMessage.TYPE_CMD);
        cmdSend(msg);
    }


}
