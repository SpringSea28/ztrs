package com.ztrs.zgj.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.main.BaseActivity;
import com.ztrs.zgj.main.BaseEditAutoHideActivity;
import com.ztrs.zgj.setting.adapter.AddressAdapter;
import com.ztrs.zgj.setting.bean.AddressBean;
import com.ztrs.zgj.setting.eventbus.SettingEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class VideoInputActivity extends BaseEditAutoHideActivity {
    private static final String TAG = VideoInputActivity.class.getSimpleName();
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.rv_address)
    RecyclerView rvAddress;

    AddressAdapter addressAdapter;
    List<AddressBean> addressBeans;

    Unbinder bind;

    @Override
    protected List<View> getExcludeTouchHideInputViews() {
        List<View> list = new ArrayList<>();
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoinput);
        bind = ButterKnife.bind(this);
        tvTitle.setText("视频参数设置");
        initAddressList();
        addressAdapter = new AddressAdapter();
        addressAdapter.setData(addressBeans);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAddress.setLayoutManager(linearLayoutManager);
        rvAddress.setAdapter(addressAdapter);
    }

    @Override
    protected void onDestroy() {
        bind.unbind();
        super.onDestroy();
    }

    @OnClick({R.id.tv_back,R.id.btn_save})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_save:
                Gson gson = new Gson();
                String addressStr = gson.toJson(addressBeans);
                SharedPreferences sp = getSharedPreferences("Address",MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("address",addressStr);
                edit.commit();
                SettingEventBus settingEventBus =
                        new SettingEventBus(SettingEventBus.ACTION_VIDEO_URL_INPUT_CHANGE);
                EventBus.getDefault().post(settingEventBus);
                break;

        }
    }

    private void initAddressList(){
        SharedPreferences sp = getSharedPreferences("Address",MODE_PRIVATE);
        String addressJson = sp.getString("address",null);
        Log.e("wch","addressJson:"+addressJson);
        if(TextUtils.isEmpty(addressJson)){
            addressBeans = new ArrayList<>(5);
            for(int i=0;i<5;i++){
                AddressBean addressBean = new AddressBean();
                addressBean.setNumber(i);
                addressBean.setName("");
                addressBean.setAddress("");
                addressBeans.add(addressBean);
            }
        }else {
            Gson gson = new Gson();
            addressBeans =gson.fromJson(addressJson,
                    new TypeToken<List<AddressBean>>(){}.getType());
        }
    }
}