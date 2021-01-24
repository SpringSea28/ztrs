package com.ztrs.zgj.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.RegisterInfoBean;
import com.ztrs.zgj.device.bean.StaticParameterBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.StaticParameterMessage;
import com.ztrs.zgj.main.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class InstallationActivity extends BaseActivity {

    private static final String TAG = InstallationActivity.class.getSimpleName();

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_host_id)
    TextView etHostId;
    @BindView(R.id.et_tower_number)
    EditText etTowerNumber;
    @BindView(R.id.et_big_arm_length)
    TextView etBigArmLength;
    @BindView(R.id.et_back_arm_length)
    EditText etBackArmLength;
    @BindView(R.id.et_tower_cap_height)
    EditText etTowerCapHeight;
    @BindView(R.id.et_tower_body_height)
    EditText etTowerBodyHeight;
    @BindView(R.id.tv_magnification)
    TextView tvMagnification;
    @BindView(R.id.et_standard_section)
    EditText etStandardSection;

    @BindView(R.id.et_tower_model)
    EditText etTowerModel;
//    @BindView(R.id.spinner_model)
//    Spinner spinnerModel;

    private StaticParameterBean tempStaticParameterBean;

    Unbinder binder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation);
        binder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        upDateView();
//        new Test().testQueryStaticParameter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DeviceManager.getInstance().queryStaticParameter();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        binder.unbind();
        super.onDestroy();
    }

    @Override
    protected List<View> getExcludeTouchHideInputViews() {
        List<View> list = new ArrayList<>();
        list.add(etHostId);
        list.add(etTowerNumber);
        list.add(etBackArmLength);
        list.add(etTowerCapHeight);
        list.add(etTowerBodyHeight);
        list.add(etStandardSection);
        return list;
    }

    private void initData(){
        StaticParameterBean staticParameterBean = DeviceManager.getInstance().getZtrsDevice().getStaticParameterBean();
        try {
            tempStaticParameterBean = (StaticParameterBean)staticParameterBean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void initView(){
        tvTitle.setText("现场安装设置");
        RegisterInfoBean registerInfoBean = DeviceManager.getInstance().getZtrsDevice().getRegisterInfoBean();
        Log.e("wch","hostId: "+registerInfoBean.getHostId());
        etHostId.setText(registerInfoBean.getHostId());
        initTowerModel();
    }

    private void upDateView(){
        initData();
        if(tempStaticParameterBean == null){
            return;
        }
        etTowerNumber.setText(""+tempStaticParameterBean.getTowerNumber());
        etTowerModel.setText(tempStaticParameterBean.getTowerCraneTypeStr());
        int upWeightArmLen = tempStaticParameterBean.getUpWeightArmLen();
        etBigArmLength.setText(String.format("%.1f",1.0*upWeightArmLen/10));
        int balanceArmLen = tempStaticParameterBean.getBalanceArmLen();
        etBackArmLength.setText(String.format("%.1f",1.0*balanceArmLen/10));
        int towerCapHeight = tempStaticParameterBean.getTowerCapHeight();
        etTowerCapHeight.setText(String.format("%.1f",1.0*towerCapHeight/10));
        int towerHeight = tempStaticParameterBean.getTowerHeight();
        etTowerBodyHeight.setText(String.format("%.1f",1.0*towerHeight/10));

        byte magnification = tempStaticParameterBean.getMagnification();
        tvMagnification.setText((magnification&0x00ff)+"倍率");

        short standardSection = tempStaticParameterBean.getStandardSection();
        etStandardSection.setText(String.format("%.1f",1.0*standardSection/10));
    }

    private void initTowerModel(){
//        String[] stringArray = getResources().getStringArray(R.array.tower_model);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.settting_spinner_item, stringArray){
//            @Override
//            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                return super.getDropDownView(position, convertView, parent);
//            }
//        };//默认样式
//        adapter.setDropDownViewResource(R.layout.settting_spinner_dropdown_item);//下拉样式
//        spinnerModel.setAdapter(adapter);
//        spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(tempStaticParameterBean != null) {
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    @OnClick({R.id.btn_save,R.id.tv_back,R.id.rl_torque_cruve})
    public void onClick(View view){
        if(view.getId() == R.id.btn_save){
            save();
        } else if(view.getId() == R.id.tv_back){
            finish();
        } else if(view.getId() == R.id.rl_torque_cruve){
            startActivity(new Intent(InstallationActivity.this,TorqueCurveActivity.class));
        }
    }

    private void save(){
        String reInput = getString(R.string.setting_re_input);

        String numberStr = etTowerNumber.getText().toString();
        if(TextUtils.isEmpty(numberStr)){
            etTowerNumber.setHint(reInput);
            return;
        }
        int number = 0;
        try {
            number = Integer.valueOf(numberStr);
            if((number & 0x00ff)>255){
                etTowerNumber.setHint(reInput);
            }
        }catch (NumberFormatException e){
            etTowerNumber.setHint(reInput);
            return;
        }


        String towerModel = etTowerModel.getText().toString();
        if(TextUtils.isEmpty(towerModel)){
            etTowerModel.setHint(reInput);
            return;
        }


//        String bigStr = etBigArmLength.getText().toString();
//        if(TextUtils.isEmpty(bigStr)){
//            etBigArmLength.setHint(reInput);
//            return;
//        }
//        float big = 0;
//        try {
//            big = Float.valueOf(bigStr);
//        }catch (NumberFormatException e){
//            etBigArmLength.setHint(reInput);
//            return;
//        }

        String backStr = etBackArmLength.getText().toString();
        float back = 0;
        try {
            back = Float.valueOf(backStr);
        }catch (NumberFormatException e){
            etBackArmLength.setHint(reInput);
            return;
        }

        String capStr = etTowerCapHeight.getText().toString();
        float cap = 0;
        try {
            cap = Float.valueOf(capStr);
        }catch (NumberFormatException e){
            etTowerCapHeight.setHint(reInput);
            return;
        }

        String bodyStr = etTowerBodyHeight.getText().toString();
        float body = 0;
        try {
            body = Float.valueOf(bodyStr);
        }catch (NumberFormatException e){
            etTowerBodyHeight.setHint(reInput);
            return;
        }

//        String magStr = etMagnification.getText().toString();
//        byte mag = 0;
//        try {
//            mag = Byte.valueOf(magStr);
//            if((mag & 0xff)>255){
//                etMagnification.setHint(reInput);
//            }
//        }catch (NumberFormatException e){
//            etMagnification.setHint(reInput);
//            return;
//        }

        String standardStr = etStandardSection.getText().toString();
        float standard = 0;
        try {
            standard = Float.valueOf(standardStr);
        }catch (NumberFormatException e){
            etTowerBodyHeight.setHint(reInput);
            return;
        }

        tempStaticParameterBean.setTowerNumber((byte)number);
        tempStaticParameterBean.setTowerCraneType(towerModel.getBytes());
//        tempStaticParameterBean.setUpWeightArmLen((int)(big*10));
        tempStaticParameterBean.setBalanceArmLen((int)(back*10));
        tempStaticParameterBean.setTowerCapHeight((int)(cap*10));
        tempStaticParameterBean.setTowerHeight((int)(body*10));
//        tempStaticParameterBean.setMagnification((byte)mag);
        tempStaticParameterBean.setStandardSection((short) (standard*10));
        DeviceManager.getInstance().setStaticParameter(tempStaticParameterBean);
    }

    //-----------------------------------------------------------//
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaticParameter(StaticParameterMessage msg){
        LogUtils.LogI(TAG,"onStaticParameter: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onStaticParameter: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                DeviceManager.getInstance().getZtrsDevice().setStaticParameterBean(tempStaticParameterBean);
                Toast.makeText(this,"静态参数保存成功",Toast.LENGTH_LONG).show();
                initData();
                upDateView();
            }else {
                Toast.makeText(this,"静态参数保存失败",Toast.LENGTH_LONG).show();
            }
        }
    }

}