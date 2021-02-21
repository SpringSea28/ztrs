package com.ztrs.zgj.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.DialogOutputBinding;
import com.ztrs.zgj.databinding.DialogOutputBindingImpl;
import com.ztrs.zgj.databinding.DialogUpdateBinding;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.RelayConfigurationBean;
import com.ztrs.zgj.main.adapter.OutputMainAdapter;
import com.ztrs.zgj.main.viewbean.OutputBean;
import com.ztrs.zgj.setting.utils.OutPutChangUtils;

import java.util.ArrayList;
import java.util.List;


public class OutputDialog extends Dialog implements View.OnClickListener {

    String[] outputItemValue;
    OutputMainAdapter outputMainAdapter;
    List<OutputBean> outputWeights;
    public OutputDialog(Context context){
        super(context);
    }

    DialogOutputBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        binding = DialogOutputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
        outputItemValue = getContext().getResources().getStringArray(R.array.output_all);
        String[] title = new String[]{"载重控制","25%","50%","90%","100%"};
        outputWeights = initWeight();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvWeight.setLayoutManager(layoutManager);
        outputMainAdapter = new OutputMainAdapter();
        outputMainAdapter.setSrc(title,outputItemValue);
        outputMainAdapter.setData(outputWeights);
        binding.rvWeight.setAdapter(outputMainAdapter);
        binding.btnSave.setOnClickListener(this);
    }

    private List<OutputBean> initWeight(){
        List<OutputBean> datas = new ArrayList();
        OutputBean weight = new OutputBean();
        weight.setKeyTitle("载重控制");
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


    @Override
    public void onClick(View v) {
        if(R.id.btn_save == v.getId()){
            saveOutputWeight();
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
        for(int i=0;i<outputWeights.size();i++){
            for(int j=i+1;j<outputWeights.size();j++){
                if((outputWeights.get(i).getPositionValue() == outputWeights.get(j).getPositionValue())
                        && outputWeights.get(i).getPositionValue() != outputItemValue.length-1){
                    Toast.makeText(getContext(),"继电器用途重叠",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        RelayConfigurationBean relayConfigurationBean = DeviceManager.getInstance().getZtrsDevice().getRelayConfigurationBean();
        byte[] data = relayConfigurationBean.getData(relayConfigurationBean);
        byte[] des =new byte[data.length];
        System.arraycopy(data,0,des,0,data.length);
        for(OutputBean outputBean :outputWeights){
            int positionValue = outputBean.getPositionValue();
            int keyValue = outputBean.getKeyValue();
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

        DeviceManager.getInstance()
                .setRelayConfiguration(RelayConfigurationBean.getRelayConfiguration(des));
    }

    OnUserClick onUserClick;
    public void setOnUserClick(OnUserClick onUserClick){
        this.onUserClick = onUserClick;
    }
    public interface OnUserClick{
        void onConfirm();
    }
}
