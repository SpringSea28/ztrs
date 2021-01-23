package com.ztrs.zgj.setting.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ztrs.zgj.R;
import com.ztrs.zgj.setting.bean.TorqueCurveBean;

import java.util.ArrayList;
import java.util.List;

public class TorqueCureAdapter extends RecyclerView.Adapter<TorqueCureAdapter.CurveHolder> {

    List<TorqueCurveBean> torqueCurveBeans;

    public void setData(List<TorqueCurveBean> torqueCurveBeans){
        this.torqueCurveBeans = torqueCurveBeans;
    }

    @NonNull
    @Override
    public CurveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.torque_curve_recycle_item, parent,false);
        CurveHolder holder = new CurveHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CurveHolder holder, int position) {
        TorqueCureSubAdapter torqueCureSubAdapter = new TorqueCureSubAdapter();
        List<TorqueCurveBean> torqueCurveSubBeans = new ArrayList<>();
        for(int i=0;i<6 && position*6+i<torqueCurveBeans.size();i++){
            torqueCurveSubBeans.add(torqueCurveBeans.get(position*6+i));
        }
        torqueCureSubAdapter.setData(torqueCurveSubBeans);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.rvSubItem.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.rvSubItem.setLayoutManager(linearLayoutManager);
        holder.rvSubItem.setAdapter(torqueCureSubAdapter);
    }

    @Override
    public int getItemCount() {
        if(torqueCurveBeans == null) {
            return 0;
        }
        int size = torqueCurveBeans.size();
        if(size%6== 0){
            return size / 6;
        }else {
            return size/6 +1;
        }
    }

    public static class CurveHolder extends RecyclerView.ViewHolder{

        RecyclerView rvSubItem;
        public CurveHolder(@NonNull View itemView) {
            super(itemView);
            rvSubItem = itemView.findViewById(R.id.rv_curve_sub);
        }
    }
}
