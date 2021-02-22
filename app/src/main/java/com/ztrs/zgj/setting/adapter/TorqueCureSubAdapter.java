package com.ztrs.zgj.setting.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ztrs.zgj.R;
import com.ztrs.zgj.setting.bean.TorqueCurveBean;

import java.util.List;

public class TorqueCureSubAdapter extends RecyclerView.Adapter<TorqueCureSubAdapter.CurveHolder> {

    List<TorqueCurveBean> torqueCurveBeans;

    public void setData(List<TorqueCurveBean> torqueCurveBeans){
        this.torqueCurveBeans = torqueCurveBeans;
    }


    @NonNull
    @Override
    public CurveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.torque_curve_sub_recycle_item, parent,false);
        CurveHolder holder = new CurveHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CurveHolder holder, int position) {
        TorqueCurveBean torqueCurveBean = torqueCurveBeans.get(position);
        holder.edtAmp.setText(String.format("%.2f",torqueCurveBean.getAmplitude()/100.0));
        holder.edtWei.setText(String.format("%.3f",torqueCurveBean.getWeight()/1000.0));
        holder.edtAmp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                float amp = Float.valueOf(s.toString());
                torqueCurveBean.setAmplitude((int)(amp*1000));
            }
        });

        holder.edtWei.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                float wei = Float.valueOf(s.toString());
                torqueCurveBean.setWeight((int)(wei*100));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(torqueCurveBeans == null) {
            return 0;
        }
        return torqueCurveBeans.size();
    }

    public static class CurveHolder extends RecyclerView.ViewHolder{

        EditText edtAmp;
        EditText edtWei;
        public CurveHolder(@NonNull View itemView) {
            super(itemView);
            edtAmp = itemView.findViewById(R.id.et_amp);
            edtWei = itemView.findViewById(R.id.et_wei);
        }
    }
}
