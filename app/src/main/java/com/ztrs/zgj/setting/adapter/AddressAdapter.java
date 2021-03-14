package com.ztrs.zgj.setting.adapter;

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
import com.ztrs.zgj.device.bean.PreventCollisionNearBean.NearCoordinate;
import com.ztrs.zgj.setting.bean.AddressBean;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder> {

    List<AddressBean> addressBeans;

    public void setData( List< AddressBean> addressBeans){
        this.addressBeans = addressBeans;
    }


    @NonNull
    @Override
    public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_video_address_item, parent,false);
        AddressHolder holder = new AddressHolder(inflate,parent.getContext());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressHolder holder, int position) {
        AddressBean addressBean = addressBeans.get(position);
        holder.tvAddress.setText(String.format("视频%d地址",position+1));
        holder.edtAddress.setText(addressBean.getAddress());
        holder.tvName.setText(String.format("视频%d名称",position+1));
        holder.edtName.setText(addressBean.getName());
        holder.edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addressBean.setName(s.toString());
            }
        });
        holder.edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addressBean.setAddress(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressBeans.size();
    }

    public static class AddressHolder extends RecyclerView.ViewHolder{
        EditText edtName;
        EditText edtAddress;
        TextView tvName;
        TextView tvAddress;
        Context context;
        public AddressHolder(@NonNull View itemView, Context context) {
            super(itemView);
            edtName = itemView.findViewById(R.id.edt_name);
            edtAddress = itemView.findViewById(R.id.edt_address);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            this.context = context;
        }
    }

}
