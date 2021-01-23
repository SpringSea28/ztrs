package com.ztrs.zgj.device.protocol;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.bean.TorqueCurveApplyBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.RegionalRestrictionMessage;
import com.ztrs.zgj.device.eventbus.TorqueCurveMessage;
import com.ztrs.zgj.setting.bean.TorqueCurveBean;

import java.util.ArrayList;
import java.util.List;

import static com.ztrs.zgj.device.bean.RegionalRestrictionsBean.OrthogonalCoordinate;
import static com.ztrs.zgj.device.bean.RegionalRestrictionsBean.PolarCoordinate;
import static com.ztrs.zgj.device.bean.RegionalRestrictionsBean.SectorRestriction;

public class TorqueCurveProtocol extends BaseProtocol{
    private static final String TAG= TorqueCurveProtocol.class.getSimpleName();

    public static final byte CMD_TORQUE_CURVE = (byte) 0x53;//力矩设置


    private static final int CURVE_LIST_OFFSET = 18;

    private DeviceOperateInterface deviceOperateInterface;

    public TorqueCurveProtocol(DeviceOperateInterface deviceOperateInterface){
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
        if(data.length < CURVE_LIST_OFFSET){
            LogUtils.LogE(TAG,"data length error");
            return false;
        }
        TorqueCurveApplyBean torqueCurveApplyBean = deviceOperateInterface.getZtrsDevice().getTorqueCurveApplyBean();
        byte[] modelBytes = new byte[15];
        System.arraycopy(data,0,modelBytes,0,15);
        int zeroOffset = 0;
        for(int i=0;i<modelBytes.length;i++){
            if(modelBytes[i]== 0){
                zeroOffset = i;
                break;
            }
        }
        String s = new String(modelBytes);
        torqueCurveApplyBean.setModel(s.substring(0,zeroOffset));
        int bigArmLen = ((data[15]&0xffffffff) << 8)
                |(data[15+1]&0x00ff);
        torqueCurveApplyBean.setBigArmLen(bigArmLen);
        byte magnification= data[17];
        torqueCurveApplyBean.setMagnification(magnification);

        List<TorqueCurveBean> beans = new
                ArrayList<>();
        for(int i= 0;i<(data.length-CURVE_LIST_OFFSET)/4;i++){
            int amplitude = ((data[CURVE_LIST_OFFSET+4*i]&0xff) << 8)
                    |(data[CURVE_LIST_OFFSET+4*i+1]&0x00ff);
            int weight = ((data[CURVE_LIST_OFFSET+4*i+2]&0xff) << 8)
                    |(data[CURVE_LIST_OFFSET+4*i+3]&0x00ff);
            TorqueCurveBean torqueCurveBean = new TorqueCurveBean();
            torqueCurveBean.setAmplitude(amplitude);
            torqueCurveBean.setWeight(weight);
            beans.add(torqueCurveBean);
        }
        torqueCurveApplyBean.setTorqueCurveBeanList(beans);
        return true;

    }




    @Override
    public void resendCmd(byte[] data) {
        deviceOperateInterface.sendDataToDevice(data);
    }

    public void queryTorqueCurve(){
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_TORQUE_CURVE,new byte[]{0});
        deviceOperateInterface.sendDataToDevice(cmd);
        TorqueCurveMessage msg = new TorqueCurveMessage(CommunicationProtocol.seq++,cmd);
        cmdSend(msg);
    }

    public void setTorqueCurve(TorqueCurveApplyBean torqueCurveApplyBean){
        List<TorqueCurveBean> beans = torqueCurveApplyBean.getTorqueCurveBeanList();
        byte[] data = new byte[CURVE_LIST_OFFSET+beans.size()*4];
        String model = torqueCurveApplyBean.getModel();
        if(model !=null) {
            System.arraycopy(model.getBytes(), 0, data, 0,
                    model.length() > 15 ? 15 : model.length());
        }
        int bigArmLen  = torqueCurveApplyBean.getBigArmLen();
        data[15] = (byte)(bigArmLen>>8);
        data[15+1] = (byte)bigArmLen;

        data[17] = (byte)torqueCurveApplyBean.getMagnification();

        for(int i=0;i<beans.size();i++){
            int amplitude = beans.get(i).getAmplitude();
            data[CURVE_LIST_OFFSET+4*i] = (byte)(amplitude>>8);
            data[CURVE_LIST_OFFSET+4*i+1] = (byte)(amplitude);
            int weight = beans.get(i).getWeight();
            data[CURVE_LIST_OFFSET+4*i+2] = (byte)(weight>>8);
            data[CURVE_LIST_OFFSET+4*i+3] = (byte)(weight);
        }
        byte[] cmd = CommunicationProtocol.packetCmd(CMD_TORQUE_CURVE,data);
        deviceOperateInterface.sendDataToDevice(cmd);
        TorqueCurveMessage msg = new TorqueCurveMessage(CommunicationProtocol.seq++,cmd);
        msg.setCmdType(BaseMessage.TYPE_CMD);
        cmdSend(msg);
    }

}
