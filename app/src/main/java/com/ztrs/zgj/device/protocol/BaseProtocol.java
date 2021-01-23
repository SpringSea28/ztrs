package com.ztrs.zgj.device.protocol;

import android.os.CountDownTimer;
import android.util.Log;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.device.eventbus.BaseMessage;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseProtocol {

    private String TAG;
    CountDownTimer countDownTimer;
    BaseMessage baseMessage;

    public abstract void parseCmd(byte[] data);
    public abstract void parseAck(byte[] data);
    public abstract void resendCmd(byte[] data);

    public BaseProtocol(String tag){
        TAG = tag;
    }

    public void cmdSend(BaseMessage baseMessage){
        this.baseMessage = baseMessage;
        waitingAck();
    }

    private void waitingAck(){
        resetCountDownTimer();
        countDownTimer = new CountDownTimer(CommunicationProtocol.ACK_TIME_OUT,
                CommunicationProtocol.ACK_TIME_OUT/2) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(CommunicationProtocol.ACK_TIME_OUT - millisUntilFinished
                        <CommunicationProtocol.ACK_TIME_OUT/2){
                    return;
                }
                LogUtils.LogI(TAG,"resend");
                resendCmd(baseMessage.getCmdBytes());
            }

            @Override
            public void onFinish() {
                BaseMessage msg = baseMessage;
                baseMessage = null;
                msg.setResult(BaseMessage.RESULT_FAIL);
                Log.e("wch","ack time out msg fail");
                EventBus.getDefault().post(msg);
            }
        };
        countDownTimer.start();
    }

    public void ackReceive(byte[] ack){
        if(baseMessage == null){
            return;
        }
        Log.e("wch","ackReceive");
        resetCountDownTimer();
        BaseMessage msg = baseMessage;
        baseMessage = null;
        msg.setResult(BaseMessage.RESULT_OK);
        EventBus.getDefault().post(msg);
    }

    public void ackReceiveError(){
        if(baseMessage == null){
            return;
        }
        resetCountDownTimer();
        Log.e("wch","ackReceiveError");
        BaseMessage msg = baseMessage;
        baseMessage = null;
        msg.setResult(BaseMessage.RESULT_FAIL);
        Log.e("wch","msg fail");
        EventBus.getDefault().post(msg);
    }


    private void resetCountDownTimer(){
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = null;
    }

    public void notifyDeviceReport(BaseMessage message){
        message.setResult(BaseMessage.RESULT_REPORT);
        EventBus.getDefault().post(message);
    }
}
