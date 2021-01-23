package com.ztrs.zgj.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ztrs.zgj.R;
import com.ztrs.zgj.main.viewbean.TowerParameterBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TowerParameterAdapter extends RecyclerView.Adapter<TowerParameterAdapter.Holder> {

    public static final int ALARM_WIRE_ROPE =0;
    public static final int ALARM_LOAD =1;
    public static final int ALARM_HEIGHT =2;
    public static final int ALARM_AROUND =3;
    public static final int ALARM_TORQUE =4;
    public static final int ALARM_WEIGHT =5;
    public static final int ALARM_WIND =6;
    public static final int ALARM_AMPLITUDE =7;

    List<TowerParameterBean> towerParameterBeanList;

    public void setTowerParameterBeanList(List<TowerParameterBean> towerParameterBeanList){
        this.towerParameterBeanList = towerParameterBeanList;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tower_parameter_recycle_item, parent,false);
        Holder holder = new Holder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        TowerParameterBean towerParameterBean = towerParameterBeanList.get(position);
        boolean isWarn = towerParameterBean.isWarn();
        holder.tvKey.setText(towerParameterBean.getKey());
        if(towerParameterBean.getType()== ALARM_WIND){
            holder.tvValue.setText(String.format("%s级", towerParameterBean.getValue()));

        }else if(towerParameterBean.getType() == ALARM_WEIGHT){
            holder.tvValue.setText(String.format("%.1f", 1.0 * towerParameterBean.getValue() / 100));
        } else {
            holder.tvValue.setText(String.format("%.1f", 1.0 * towerParameterBean.getValue() / 10));
        }
        if(isWarn){
            holder.tvWarn.setVisibility(View.VISIBLE);
        }else {
            holder.tvWarn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(towerParameterBeanList == null) {
            return 0;
        }
        return towerParameterBeanList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_key)
        TextView tvKey;
        @BindView(R.id.tv_value)
        TextView tvValue;
        @BindView(R.id.tv_warn)
        TextView tvWarn;

        public Holder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }

    }
}
