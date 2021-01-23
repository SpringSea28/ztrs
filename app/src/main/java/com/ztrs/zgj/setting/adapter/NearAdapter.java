package com.ztrs.zgj.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ztrs.zgj.R;
import com.ztrs.zgj.device.bean.PreventCollisionNearBean.NearCoordinate;
import com.ztrs.zgj.device.bean.RegionalRestrictionsBean.OrthogonalCoordinate;
import com.ztrs.zgj.device.bean.RegionalRestrictionsBean.OrthogonalCoordinate.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class NearAdapter extends RecyclerView.Adapter<NearAdapter.NearHolder> {

    List< NearCoordinate> nearCoordinates;

    public void setData( List< NearCoordinate> nearCoordinates){
        this.nearCoordinates = nearCoordinates;
    }


    @NonNull
    @Override
    public NearHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_prevent_collision_near, parent,false);
        NearHolder holder = new NearHolder(inflate,parent.getContext());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NearHolder holder, int position) {
        NearCoordinate nearCoordinate = nearCoordinates.get(position);
        holder.tvNumber.setText(""+(position+1));
        holder.tvTowerNumber.setText(""+nearCoordinate.getNumber());

        holder.tvX.setText(String.format("%.1f",nearCoordinate.getX()/10.0));
        holder.tvY.setText(String.format("%.1f",nearCoordinate.getY()/10.0));

    }

    @Override
    public int getItemCount() {
        return nearCoordinates.size();
    }

    public static class NearHolder extends RecyclerView.ViewHolder{
        TextView tvNumber;
        TextView tvTowerNumber;
        TextView tvX;
        TextView tvY;
        Context context;
        public NearHolder(@NonNull View itemView, Context context) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvTowerNumber = itemView.findViewById(R.id.tv_tower_number);
            tvX = itemView.findViewById(R.id.tv_x);
            tvY = itemView.findViewById(R.id.tv_y);
            this.context = context;
        }
    }

}
