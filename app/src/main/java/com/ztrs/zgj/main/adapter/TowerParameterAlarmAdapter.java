package com.ztrs.zgj.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ztrs.zgj.R;
import com.ztrs.zgj.main.viewbean.TowerParameterAlarmBean;
import com.ztrs.zgj.main.viewbean.TowerParameterBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TowerParameterAlarmAdapter extends RecyclerView.Adapter<TowerParameterAlarmAdapter.Holder> {

    List<TowerParameterAlarmBean> towerParameterAlarmBeans;

    public void setTowerParameterAlarmBeanList(List<TowerParameterAlarmBean> alarms){
        this.towerParameterAlarmBeans = alarms;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tower_parameter_alarm_recycle_item, parent,false);
        Holder holder = new Holder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        TowerParameterAlarmBean towerParameterAlarmBean = towerParameterAlarmBeans.get(position);
        String alarm = towerParameterAlarmBean.getAlarm();
        holder.tvAlarm.setText(alarm);
    }

    @Override
    public int getItemCount() {
        if(towerParameterAlarmBeans == null) {
            return 0;
        }
        return towerParameterAlarmBeans.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_alarm_value)
        TextView tvAlarm;

        public Holder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }

    }
}
