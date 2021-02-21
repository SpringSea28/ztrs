package com.ztrs.zgj.main.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.DialogOutputBinding;
import com.ztrs.zgj.databinding.OutputRecycleItemBinding;
import com.ztrs.zgj.databinding.SettingOutputRecycleItemBinding;
import com.ztrs.zgj.main.viewbean.OutputBean;
import com.ztrs.zgj.setting.bean.RelayBean;
import com.ztrs.zgj.utils.ScaleUtils;

import java.lang.reflect.Field;
import java.util.List;

public class OutputMainAdapter extends RecyclerView.Adapter<OutputMainAdapter.OutputHolder> {

    List<OutputBean> outputBeans;
    String[] relayName;
    String[] optUses;
    public void setData( List<OutputBean> outputBeans){
        this.outputBeans = outputBeans;
    }

    public void setSrc(String[] relayName,String[] optUses){
        this.relayName = relayName;
        this.optUses = optUses;
    }


    @NonNull
    @Override
    public OutputHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OutputRecycleItemBinding inflate =
                OutputRecycleItemBinding.inflate(LayoutInflater.from(parent.getContext()));
//        View inflate = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.setting_output_recycle_item, parent,false);
        OutputHolder holder = new OutputHolder(inflate,parent.getContext());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OutputHolder holder, int position) {
        OutputBean outputBean = outputBeans.get(position);
        holder.binding.tvKey.setText(relayName[position]);

        if(outputBean.getKeyValue() == 0){
            holder.binding.rlOutput.setVisibility(View.GONE);
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.context, R.layout.settting_spinner_item, optUses);//默认样式
        adapter.setDropDownViewResource(R.layout.settting_spinner_dropdown_item);//下拉样式
        holder.binding.spinnerOutput.setAdapter(adapter);
        holder.binding.spinnerOutput.setSelection(outputBean.getPositionValue());
        holder.binding.spinnerOutput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                outputBean.setPositionValue(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return outputBeans.size();
    }

    public static class OutputHolder extends RecyclerView.ViewHolder{
        OutputRecycleItemBinding binding;
        Context context;
        public OutputHolder(OutputRecycleItemBinding binding,Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }
    }

}
