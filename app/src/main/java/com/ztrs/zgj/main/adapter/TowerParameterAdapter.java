package com.ztrs.zgj.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        boolean isAlarm = towerParameterBean.isAlarm();
        holder.tvKey.setText(towerParameterBean.getKey());
        if(towerParameterBean.getType()== ALARM_WIND){
            holder.tvValue.setText(String.format("%s级", towerParameterBean.getValue()));
        }else if(towerParameterBean.getType() == ALARM_WEIGHT){
            holder.tvValue.setText(String.format("%.1f", 1.0 * towerParameterBean.getValue() / 100));
        } else if(towerParameterBean.getType() == ALARM_LOAD){
            holder.tvValue.setText(String.format("%.1f", 1.0 * towerParameterBean.getValue() / 10));
        }else {
            holder.tvValue.setText(String.format("%.1f", 1.0 * towerParameterBean.getValue() / 10));
        }
        if(isWarn | isAlarm){
            holder.tvWarn.setVisibility(View.VISIBLE);
            holder.tvKey.setTextColor(holder.context.getColor(R.color.tower_parameter_warn_text_color));
            holder.tvValue.setTextColor(holder.context.getColor(R.color.tower_parameter_warn_text_color));
            holder.llItem.setBackground(holder.context.getDrawable(R.drawable.tower_parameter_alarm_item_bg));
        }else {
            holder.tvWarn.setVisibility(View.GONE);
            holder.tvKey.setTextColor(holder.context.getColor(R.color.tower_parameter_normal_text_color));
            holder.tvValue.setTextColor(holder.context.getColor(R.color.tower_parameter_normal_text_color));
            holder.llItem.setBackground(holder.context.getDrawable(R.drawable.tower_parameter_item_bg));
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
        @BindView(R.id.ll_tower_parameter_item)
        LinearLayout llItem;

        Context context;

        public Holder(View view){
            super(view);
            context = view.getContext();
            ButterKnife.bind(this,view);
        }

    }
}
