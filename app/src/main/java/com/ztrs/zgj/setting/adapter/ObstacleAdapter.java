package com.ztrs.zgj.setting.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.RegionalRestrictionsBean;
import com.ztrs.zgj.device.bean.RegionalRestrictionsBean.OrthogonalCoordinate;
import com.ztrs.zgj.device.bean.RegionalRestrictionsBean.OrthogonalCoordinate.Coordinate;
import com.ztrs.zgj.setting.bean.TorqueCurveBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ObstacleAdapter extends RecyclerView.Adapter<ObstacleAdapter.ObstacleHolder> {

    ConcurrentHashMap<Integer,OrthogonalCoordinate> orthogonalCoordinates;

    public void setData( ConcurrentHashMap<Integer,OrthogonalCoordinate> orthogonalCoordinates){
        this.orthogonalCoordinates = orthogonalCoordinates;
    }


    @NonNull
    @Override
    public ObstacleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_prevent_collision_obstacle_recycle_item, parent,false);
        ObstacleHolder holder = new ObstacleHolder(inflate,parent.getContext());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ObstacleHolder holder, int position) {
        OrthogonalCoordinate orthogonalCoordinate = null;
        if(orthogonalCoordinates !=null){
            orthogonalCoordinate = orthogonalCoordinates.get(position+1);
        }
        int obstacleHigh = 0;
        int coordinate1x = 0;
        int coordinate1y = 0;
        int coordinate2x = 0;
        int coordinate2y = 0;
        int coordinate3x = 0;
        int coordinate3y = 0;
        int coordinate4x = 0;
        int coordinate4y = 0;
        if(orthogonalCoordinate != null){
            obstacleHigh = orthogonalCoordinate.getObstacleHigh();
            List<Coordinate> coordinateList = orthogonalCoordinate.getCoordinateList();
            if(coordinateList !=null) {
                for (int i = 0; i < coordinateList.size(); i++) {
                    if (i == 0) {
                        Coordinate coordinate = coordinateList.get(i);
                        coordinate1x = coordinate.getX();
                        coordinate1y = coordinate.getY();
                    }
                    if (i == 1) {
                        Coordinate coordinate = coordinateList.get(i);
                        coordinate2x = coordinate.getX();
                        coordinate2y = coordinate.getY();
                    }
                    if (i == 2) {
                        Coordinate coordinate = coordinateList.get(i);
                        coordinate3x = coordinate.getX();
                        coordinate3y = coordinate.getY();
                    }
                    if (i == 3) {
                        Coordinate coordinate = coordinateList.get(i);
                        coordinate4x = coordinate.getX();
                        coordinate4y = coordinate.getY();
                    }
                }
            }
        }
        holder.tvObstacleName.setText("障碍物"+(position+1));
        holder.edtHeight.setText(String.format("%.1f",obstacleHigh/10.0));

        holder.edtCoordinate1x.setText(String.format("%.1f",coordinate1x/10.0));
        holder.edtCoordinate1y.setText(String.format("%.1f",coordinate1y/10.0));

        holder.edtCoordinate2x.setText(String.format("%.1f",coordinate2x/10.0));
        holder.edtCoordinate2y.setText(String.format("%.1f",coordinate2y/10.0));

        holder.edtCoordinate3x.setText(String.format("%.1f",coordinate3x/10.0));
        holder.edtCoordinate3y.setText(String.format("%.1f",coordinate3y/10.0));

        holder.edtCoordinate4x.setText(String.format("%.1f",coordinate4x/10.0));
        holder.edtCoordinate4y.setText(String.format("%.1f",coordinate4y/10.0));

        if(holder.isShow()){
            holder.llObstacleValue.setVisibility(View.VISIBLE);
            holder.imgObstacleDivideLine.setVisibility(View.VISIBLE);
            holder.imgObstacleShow.setImageDrawable(holder.context.getDrawable(R.mipmap.zhankai_black));
        }else {
            holder.llObstacleValue.setVisibility(View.GONE);
            holder.imgObstacleDivideLine.setVisibility(View.GONE);
            holder.imgObstacleShow.setImageDrawable(holder.context.getDrawable(R.mipmap.more));
        }
        OrthogonalCoordinate finalOrthogonalCoordinate = orthogonalCoordinate;
        holder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reInput = holder.context.getString(R.string.setting_re_input);

                String coord1XStr = holder.edtCoordinate1x.getText().toString();
                float coord1X = 0;
                try {
                    coord1X = Float.valueOf(coord1XStr);
                }catch (NumberFormatException e){
                    holder.edtCoordinate1x.setHint(reInput);
                    return;
                }
                String coord1YStr = holder.edtCoordinate1y.getText().toString();
                float coord1Y = 0;
                try {
                    coord1Y = Float.valueOf(coord1YStr);
                }catch (NumberFormatException e){
                    holder.edtCoordinate1y.setHint(reInput);
                    return;
                }

                String coord2XStr = holder.edtCoordinate2x.getText().toString();
                float coord2X = 0;
                try {
                    coord2X = Float.valueOf(coord2XStr);
                }catch (NumberFormatException e){
                    holder.edtCoordinate2x.setHint(reInput);
                    return;
                }
                String coord2YStr = holder.edtCoordinate2y.getText().toString();
                float coord2Y = 0;
                try {
                    coord2Y = Float.valueOf(coord2YStr);
                }catch (NumberFormatException e){
                    holder.edtCoordinate2y.setHint(reInput);
                    return;
                }

                String coord3XStr = holder.edtCoordinate3x.getText().toString();
                float coord3X = 0;
                try {
                    coord3X = Float.valueOf(coord3XStr);
                }catch (NumberFormatException e){
                    holder.edtCoordinate3x.setHint(reInput);
                    return;
                }
                String coord3YStr = holder.edtCoordinate3y.getText().toString();
                float coord3Y = 0;
                try {
                    coord3Y = Float.valueOf(coord3YStr);
                }catch (NumberFormatException e){
                    holder.edtCoordinate3y.setHint(reInput);
                    return;
                }

                String coord4XStr = holder.edtCoordinate4x.getText().toString();
                float coord4X = 0;
                try {
                    coord4X = Float.valueOf(coord4XStr);
                }catch (NumberFormatException e){
                    holder.edtCoordinate4x.setHint(reInput);
                    return;
                }
                String coord4YStr = holder.edtCoordinate4y.getText().toString();
                float coord4Y = 0;
                try {
                    coord4Y = Float.valueOf(coord4YStr);
                }catch (NumberFormatException e){
                    holder.edtCoordinate4y.setHint(reInput);
                    return;
                }

                String heightStr = holder.edtHeight.getText().toString();
                float height = 0;
                try {
                    height = Float.valueOf(heightStr);
                }catch (NumberFormatException e){
                    holder.edtHeight.setHint(reInput);
                    return;
                }

                OrthogonalCoordinate save;
                if(finalOrthogonalCoordinate !=null) {
                    save = finalOrthogonalCoordinate.copy();
                }else {
                    save = new OrthogonalCoordinate();
                    save.setNumber((byte)(position+1));
                }
                List<Coordinate> coordinateList = new ArrayList<>();
                Coordinate coordinate1 = new Coordinate();
                coordinate1.setX((int)(coord1X*10.0));
                coordinate1.setY((int)(coord1Y*10.0));
                coordinateList.add(coordinate1);

                Coordinate coordinate2 = new Coordinate();
                coordinate2.setX((int)(coord2X*10.0));
                coordinate2.setY((int)(coord2Y*10.0));
                coordinateList.add(coordinate2);


                Coordinate coordinate3 = new Coordinate();
                coordinate3.setX((int)(coord3X*10.0));
                coordinate3.setY((int)(coord3Y*10.0));
                coordinateList.add(coordinate3);

                Coordinate coordinate4 = new Coordinate();
                coordinate4.setX((int)(coord4X*10.0));
                coordinate4.setY((int)(coord4Y*10.0));
                coordinateList.add(coordinate4);

                save.setCoordinateList(coordinateList);
                save.setObstacleHigh((int)(height*10.0));
                if(onSaveListener !=null){
                    onSaveListener.onSaveClick(save,position);
                }
            }
        });
        holder.rlObstacle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean show = !holder.isShow();
                holder.setShow(show);{
                    if(show){
                        holder.llObstacleValue.setVisibility(View.VISIBLE);
                        holder.imgObstacleDivideLine.setVisibility(View.VISIBLE);
                        holder.imgObstacleShow.setImageDrawable(holder.context.getDrawable(R.mipmap.zhankai_black));
                    }else {
                        holder.llObstacleValue.setVisibility(View.GONE);
                        holder.imgObstacleDivideLine.setVisibility(View.GONE);
                        holder.imgObstacleShow.setImageDrawable(holder.context.getDrawable(R.mipmap.more));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class ObstacleHolder extends RecyclerView.ViewHolder{

        EditText edtCoordinate1x;
        EditText edtCoordinate1y;
        EditText edtCoordinate2x;
        EditText edtCoordinate2y;
        EditText edtCoordinate3x;
        EditText edtCoordinate3y;
        EditText edtCoordinate4x;
        EditText edtCoordinate4y;
        EditText edtHeight;
        Button btnSave;
        LinearLayout llObstacleValue;
        TextView tvObstacleName;
        RelativeLayout rlObstacle;
        ImageView imgObstacleShow;
        ImageView imgObstacleDivideLine;
        private boolean show = false;
        Context context;
        public ObstacleHolder(@NonNull View itemView, Context context) {
            super(itemView);
            edtCoordinate1x = itemView.findViewById(R.id.et_coordinate_1_x);
            edtCoordinate1y = itemView.findViewById(R.id.et_coordinate_1_y);
            edtCoordinate2x = itemView.findViewById(R.id.et_coordinate_2_x);
            edtCoordinate2y = itemView.findViewById(R.id.et_coordinate_2_y);
            edtCoordinate3x = itemView.findViewById(R.id.et_coordinate_3_x);
            edtCoordinate3y = itemView.findViewById(R.id.et_coordinate_3_y);
            edtCoordinate4x = itemView.findViewById(R.id.et_coordinate_4_x);
            edtCoordinate4y = itemView.findViewById(R.id.et_coordinate_4_y);
            edtHeight = itemView.findViewById(R.id.et_height);
            btnSave = itemView.findViewById(R.id.btn_save_obstacle);
            tvObstacleName = itemView.findViewById(R.id.tv_obstacle_name);
            llObstacleValue = itemView.findViewById(R.id.ll_obstacle_value);
            rlObstacle = itemView.findViewById(R.id.rl_obstacle);
            imgObstacleShow = itemView.findViewById(R.id.img_obstacle_show);
            imgObstacleDivideLine = itemView.findViewById(R.id.img_obstacle_divide_line);
            this.context = context;
        }

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }
    }

    private OnSaveListener onSaveListener;
    public void setOnSaveListener(OnSaveListener onSaveListener){
        this.onSaveListener = onSaveListener;
    }
    public interface OnSaveListener{
        public void onSaveClick(OrthogonalCoordinate orthogonalCoordinate,int position);
    }
}
