package com.ztrs.zgj.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.DialogOutputBinding;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.Test;
import com.ztrs.zgj.device.bean.RelayConfigurationBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.RelayConfigurationMessage;
import com.ztrs.zgj.main.adapter.OutputMainAdapter;
import com.ztrs.zgj.main.viewbean.OutputBean;
import com.ztrs.zgj.setting.SystemSetActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class OutputDialogActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = OutputDialogActivity.class.getSimpleName();

    String[] outputItemValue;
    OutputMainAdapter outputMainAdapter;
    List<OutputBean> outputWeights;

    OutputMainAdapter outputTorqueAdapter;
    List<OutputBean> outputTorques;

    DialogOutputBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        binding = DialogOutputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        outputItemValue = getResources().getStringArray(R.array.output_all);
        String[] title = new String[]{"载重控制","25%","50%","90%","100%"};
        outputWeights = initWeight();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvWeight.setLayoutManager(layoutManager);
        outputMainAdapter = new OutputMainAdapter();
        outputMainAdapter.setSrc(title,outputItemValue);
        outputMainAdapter.setData(outputWeights);
        binding.rvWeight.setAdapter(outputMainAdapter);


        String[] titleTorque = new String[]{"力矩控制","80%","90%","100%","110%"};
        outputTorques = initTorque();
        LinearLayoutManager layoutManagerToruqe = new LinearLayoutManager(this);
        layoutManagerToruqe.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvTorque.setLayoutManager(layoutManagerToruqe);
        outputTorqueAdapter = new OutputMainAdapter();
        outputTorqueAdapter.setSrc(titleTorque,outputItemValue);
        outputTorqueAdapter.setData(outputTorques);
        binding.rvTorque.setAdapter(outputTorqueAdapter);

        binding.btnSave.setOnClickListener(this);
        binding.imgClose.setOnClickListener(this);

        EventBus.getDefault().register(this);
        DeviceManager.getInstance().queryRelayConfiguration();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void updateWeight(){
        outputWeights = initWeight();
        outputMainAdapter.setData(outputWeights);
        outputMainAdapter.notifyDataSetChanged();
    }

    private void updateTorque(){
        outputTorques = initTorque();
        outputTorqueAdapter.setData(outputTorques);
        outputTorqueAdapter.notifyDataSetChanged();
    }


    private List<OutputBean> initWeight(){
        List<OutputBean> datas = new ArrayList();
        OutputBean weight = new OutputBean();
        weight.setKeyTitle("载重控制");
        weight.setPositionValue(-1);
        datas.add(weight);

        OutputBean outputBean25 = new OutputBean();
        outputBean25.setKeyTitle("25%");
        outputBean25.setKeyValue(12);
        outputBean25.setPositionValue(getPosition(12));
        datas.add(outputBean25);

        OutputBean outputBean50 = new OutputBean();
        outputBean50.setKeyTitle("50%");
        outputBean50.setKeyValue(13);
        outputBean50.setPositionValue(getPosition(13));
        datas.add(outputBean50);

        OutputBean outputBean90 = new OutputBean();
        outputBean90.setKeyTitle("90%");
        outputBean90.setKeyValue(15);
        outputBean90.setPositionValue(getPosition(15));
        datas.add(outputBean90);

        OutputBean outputBean100 = new OutputBean();
        outputBean100.setKeyTitle("100%");
        outputBean100.setKeyValue(16);
        outputBean100.setPositionValue(getPosition(16));
        datas.add(outputBean100);
        return datas;
    }

    private List<OutputBean> initTorque(){
        List<OutputBean> datas = new ArrayList();
        OutputBean weight = new OutputBean();
        weight.setPositionValue(-1);
        datas.add(weight);

        OutputBean outputBean80 = new OutputBean();
        outputBean80.setKeyValue(17);
        outputBean80.setPositionValue(getPosition(17));
        datas.add(outputBean80);

        OutputBean outputBean90 = new OutputBean();
        outputBean90.setKeyValue(18);
        outputBean90.setPositionValue(getPosition(18));
        datas.add(outputBean90);

        OutputBean outputBean100 = new OutputBean();
        outputBean100.setKeyValue(19);
        outputBean100.setPositionValue(getPosition(19));
        datas.add(outputBean100);

        OutputBean outputBean110 = new OutputBean();
        outputBean110.setKeyValue(20);
        outputBean110.setPositionValue(getPosition(20));
        datas.add(outputBean110);
        return datas;
    }


    @Override
    public void onClick(View v) {
        if(R.id.btn_save == v.getId()){
            saveOutputWeight();
        }else if(R.id.img_close == v.getId()){
            finish();
        }
    }

    private int getPosition(int key){
        RelayConfigurationBean relayConfigurationBean = DeviceManager.getInstance().getZtrsDevice().getRelayConfigurationBean();
        byte[] data = relayConfigurationBean.getData(relayConfigurationBean);
        for(int i=0;i<data.length;i=i+2){
            if(data[i]== key){
                if(data[i+1] == 0){
                    return i/2;
                }else if(data[i+1] == 1){
                    return i/2 + outputItemValue.length/2;
                }
            }
        }
        return outputItemValue.length-1;
    }

    private void saveOutputWeight(){
        for(int i=0;i<outputWeights.size();i++) {
            Log.e("wch","i"+i +" "+outputWeights.get(i).getPositionValue());
        }
        for(int i=0;i<outputTorques.size();i++) {
            Log.e("wch","ti"+i +" "+outputTorques.get(i).getPositionValue());
        }
        for(int i=1;i<outputWeights.size();i++){
            for(int j=i+1;j<outputWeights.size();j++){
                if((outputWeights.get(i).getPositionValue() == outputWeights.get(j).getPositionValue())
                        && outputWeights.get(i).getPositionValue() != outputItemValue.length-1){
                    Toast.makeText(this,"继电器用途重叠",Toast.LENGTH_LONG).show();
                    updateWeight();
                    Log.e("wch","!!!"+1);
                    return;
                }
                if((outputWeights.get(i).getPositionValue() ==
                        outputWeights.get(j).getPositionValue()+outputItemValue.length/2
                        && outputWeights.get(j).getPositionValue()+outputItemValue.length/2
                        != outputItemValue.length-1)){
                    Toast.makeText(this,"继电器用途重叠",Toast.LENGTH_LONG).show();
                    updateWeight();
                    Log.e("wch","!!!"+2);
                    return;
                }
                if((outputWeights.get(i).getPositionValue()+outputItemValue.length/2 ==
                        outputWeights.get(j).getPositionValue()
                        && outputWeights.get(i).getPositionValue()+outputItemValue.length/2
                        != outputItemValue.length-1)){
                    Toast.makeText(this,"继电器用途重叠",Toast.LENGTH_LONG).show();
                    updateWeight();
                    Log.e("wch","!!!"+3);
                    return;
                }
            }
        }

        for(int i=1;i<outputTorques.size();i++){
            for(int j=i+1;j<outputTorques.size();j++){
                if((outputTorques.get(i).getPositionValue() == outputTorques.get(j).getPositionValue())
                        && outputTorques.get(i).getPositionValue() != outputItemValue.length-1){
                    Toast.makeText(this,"继电器用途重叠",Toast.LENGTH_LONG).show();
                    updateTorque();
                    Log.e("wch","!!!"+4);
                    return;
                }
                if(outputTorques.get(i).getPositionValue() ==
                        outputTorques.get(j).getPositionValue()+outputItemValue.length/2
                && outputTorques.get(j).getPositionValue()+outputItemValue.length/2
                        != outputItemValue.length-1){
                    Toast.makeText(this,"继电器用途重叠",Toast.LENGTH_LONG).show();
                    updateTorque();
                    Log.e("wch","!!!"+5);
                    return;
                }
                if((outputTorques.get(i).getPositionValue()+outputItemValue.length/2 ==
                        outputTorques.get(j).getPositionValue()
                        && outputTorques.get(i).getPositionValue()+outputItemValue.length/2
                        != outputItemValue.length-1)){
                    Toast.makeText(this,"继电器用途重叠",Toast.LENGTH_LONG).show();
                    updateTorque();
                    Log.e("wch","!!!"+6);
                    return;
                }
            }
        }

        for(int i=1;i<outputTorques.size();i++){
            for(int j=1;j<outputTorques.size();j++){
                if((outputTorques.get(i).getPositionValue() == outputWeights.get(j).getPositionValue())
                        && outputTorques.get(i).getPositionValue() != outputItemValue.length-1){
                    Toast.makeText(this,"继电器用途重叠",Toast.LENGTH_LONG).show();
                    updateWeight();
                    updateTorque();
                    Log.e("wch","!!!"+7);
                    return;
                }
                if((outputTorques.get(i).getPositionValue() ==
                        outputWeights.get(j).getPositionValue()+outputItemValue.length/2
                        && outputWeights.get(j).getPositionValue()+outputItemValue.length/2
                        != outputItemValue.length-1)){
                    Toast.makeText(this,"继电器用途重叠",Toast.LENGTH_LONG).show();
                    updateWeight();
                    updateTorque();
                    Log.e("wch","!!!"+8);
                    return;
                }
                if((outputTorques.get(i).getPositionValue()+outputItemValue.length/2 ==
                        outputWeights.get(j).getPositionValue()
                        && outputTorques.get(i).getPositionValue()+outputItemValue.length/2
                        != outputItemValue.length-1)){
                    Toast.makeText(this,"继电器用途重叠",Toast.LENGTH_LONG).show();
                    updateWeight();
                    updateTorque();
                    Log.e("wch","!!!"+9);
                    return;
                }
            }
        }

        RelayConfigurationBean relayConfigurationBean = DeviceManager.getInstance().getZtrsDevice().getRelayConfigurationBean();
        byte[] data = relayConfigurationBean.getData(relayConfigurationBean);
        byte[] des =new byte[data.length];
        System.arraycopy(data,0,des,0,data.length);
        for( int j=1;j<outputWeights.size();j++){
            OutputBean outputBean =outputWeights.get(j);
            int positionValue = outputBean.getPositionValue();
            int keyValue = outputBean.getKeyValue();
            for(int i=0;i<des.length;i=i+2){
                if(des[i]== keyValue){
                    des[i] = 0;
                    des[i+1] = 2;
                }
            }
            if(positionValue == outputItemValue.length -1){
                for(int i=0;i<des.length;i=i+2){
                    if(des[i] == keyValue){
                        des[i+1] = 2;
                    }
                }
            }else if(positionValue < outputItemValue.length/2){
                des[positionValue*2] = (byte)keyValue;
                des[positionValue*2+1] = 0;
            }else {
                des[(positionValue-outputItemValue.length/2)*2] = (byte)keyValue;
                des[(positionValue-outputItemValue.length/2)*2+1] = 1;
            }
        }

        for( int j=1;j<outputTorques.size();j++){
            OutputBean outputBean =outputTorques.get(j);
            int positionValue = outputBean.getPositionValue();
            int keyValue = outputBean.getKeyValue();
            for(int i=0;i<des.length;i=i+2){
                if(des[i]== keyValue){
                    des[i] = 0;
                    des[i+1] = 2;
                }
            }
            if(positionValue == outputItemValue.length -1){
                for(int i=0;i<des.length;i=i+2){
                    if(des[i] == keyValue){
                        des[i+1] = 2;
                    }
                }
            }else if(positionValue < outputItemValue.length/2){
                des[positionValue*2] = (byte)keyValue;
                des[positionValue*2+1] = 0;
            }else {
                des[(positionValue-outputItemValue.length/2)*2] = (byte)keyValue;
                des[(positionValue-outputItemValue.length/2)*2+1] = 1;
            }
        }
        saveBean = RelayConfigurationBean.getRelayConfiguration(des);
        DeviceManager.getInstance()
                .setRelayConfiguration(saveBean);
//        new Test().setRelayConfiguration(saveBean);
    }

    RelayConfigurationBean saveBean;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRelayConfig(RelayConfigurationMessage msg){
        LogUtils.LogI(TAG,"onRelayConfig: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onRelayConfig: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                DeviceManager.getInstance().getZtrsDevice().setRelayConfigurationBean(saveBean);
                updateWeight();
                updateTorque();
                Toast.makeText(this,"输出控制设置成功",Toast.LENGTH_LONG).show();
            }else {
                updateWeight();
                updateTorque();
                Toast.makeText(this,"输出控制设置失败",Toast.LENGTH_LONG).show();
            }
        }else if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                updateWeight();
                updateTorque();
            }else {
                Toast.makeText(this,"输出控制查询失败",Toast.LENGTH_LONG).show();
            }
        }
    }

}
