package com.ztrs.zgj.main.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ztrs.zgj.R;
import com.ztrs.zgj.setting.bean.AddressBean;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.AddressHolder> {

    List<AddressBean> addressBeans;
    private int selected = -1;

    public void setData( List< AddressBean> addressBeans){
        this.addressBeans = addressBeans;
    }


    @NonNull
    @Override
    public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_video_address_item, parent,false);
        AddressHolder holder = new AddressHolder(inflate,parent.getContext());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressHolder holder, int position) {
        if(position == selected){
            holder.tvName.setSelected(true);
            holder.tvName.setBackgroundColor(holder.context.getColor(R.color.main_color_url_input_bg_selected));
            holder.tvName.setTextColor(holder.context.getColor(R.color.main_color_url_input_text_selected));
        }else {
            holder.tvName.setSelected(false);
            holder.tvName.setBackgroundColor(holder.context.getColor(R.color.main_color_url_input_bg_normal));
            holder.tvName.setTextColor(holder.context.getColor(R.color.main_color_url_input_text_normal));
        }

        AddressBean addressBean = addressBeans.get(position);
        holder.tvName.setText(addressBean.getName());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onVideoSelectListener != null){
                    selected = position;
                    onVideoSelectListener.onVideoSelected(position,addressBean.getAddress());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressBeans.size();
    }

    public static class AddressHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        Context context;
        public AddressHolder(@NonNull View itemView, Context context) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_video);
            this.context = context;
        }
    }

    OnVideoSelectListener onVideoSelectListener;
    public void setOnVideoSelectListener(OnVideoSelectListener onVideoSelectListener){
        this.onVideoSelectListener = onVideoSelectListener;
    }
    public interface OnVideoSelectListener{
        void onVideoSelected(int position,String address);
    }

}
