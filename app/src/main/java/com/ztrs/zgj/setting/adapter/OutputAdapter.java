package com.ztrs.zgj.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.SettingOutputRecycleItemBinding;
import com.ztrs.zgj.device.bean.PreventCollisionNearBean.NearCoordinate;
import com.ztrs.zgj.setting.bean.RelayBean;

import java.util.List;

public class OutputAdapter extends RecyclerView.Adapter<OutputAdapter.OutputHolder> {

    List<RelayBean> relayBeans;
    String[] relayName;
    String[] optUses;
    String[] optActions;
    public void setData( List<RelayBean> relayBeans){
        this.relayBeans = relayBeans;
    }

    public void setSrc(String[] relayName,String[] optUses,String[] optActions){
        this.relayName = relayName;
        this.optUses = optUses;
        this.optActions = optActions;
    }


    @NonNull
    @Override
    public OutputHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SettingOutputRecycleItemBinding inflate =
                SettingOutputRecycleItemBinding.inflate(LayoutInflater.from(parent.getContext()));
//        View inflate = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.setting_output_recycle_item, parent,false);
        OutputHolder holder = new OutputHolder(inflate,parent.getContext());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OutputHolder holder, int position) {
        RelayBean relayBean = relayBeans.get(position);
        holder.binding.tvRelay.setText(relayName[position]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.context, R.layout.settting_spinner_item, optUses);//默认样式
        adapter.setDropDownViewResource(R.layout.settting_spinner_dropdown_item);//下拉样式
        holder.binding.spinnerUse.setAdapter(adapter);
        holder.binding.spinnerUse.setSelection(relayBean.getUse());
        holder.binding.spinnerUse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relayBean.setUse(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> actionAdapter = new ArrayAdapter<String>(holder.context, R.layout.settting_spinner_item, optActions);//默认样式
        actionAdapter.setDropDownViewResource(R.layout.settting_spinner_dropdown_item);//下拉样式
        holder.binding.spinnerAction.setAdapter(actionAdapter);
        holder.binding.spinnerAction.setSelection(relayBean.getAction());
        holder.binding.spinnerAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relayBean.setAction(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 11;
    }

    public static class OutputHolder extends RecyclerView.ViewHolder{
        SettingOutputRecycleItemBinding binding;
        Context context;
        public OutputHolder(SettingOutputRecycleItemBinding binding,Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }
    }

}
