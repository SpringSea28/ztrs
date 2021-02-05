package com.ztrs.zgj.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ztrs.zgj.LogUtils;
import com.ztrs.zgj.R;
import com.ztrs.zgj.device.DeviceManager;
import com.ztrs.zgj.device.bean.TorqueCurveApplyBean;
import com.ztrs.zgj.device.eventbus.BaseMessage;
import com.ztrs.zgj.device.eventbus.TorqueCurveMessage;
import com.ztrs.zgj.main.BaseActivity;
import com.ztrs.zgj.main.BaseEditAutoHideActivity;
import com.ztrs.zgj.setting.adapter.TorqueCureAdapter;
import com.ztrs.zgj.setting.bean.TorqueCurveBean;
import com.ztrs.zgj.setting.bean.TorqueModelBean;
import com.ztrs.zgj.setting.dialog.UpdateDialog;
import com.ztrs.zgj.setting.viewModel.AppUpdateViewModel;
import com.ztrs.zgj.setting.viewModel.CurveUpdateModel;
import com.ztrs.zgj.setting.viewModel.VersionModel;
import com.ztrs.zgj.utils.ScaleUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TorqueCurveActivity extends BaseEditAutoHideActivity {

    private static final String TAG = TorqueCurveActivity.class.getSimpleName();

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_big_arm_length)
    TextView tvBigArmLength;
    @BindView(R.id.tv_magnification)
    TextView tvMagnification;
    @BindView(R.id.tv_weight)
    TextView tvWeight;

    @BindView(R.id.spinner_model)
    TextView spinnerModel;

    @BindView(R.id.rv_curve)
    RecyclerView rvCurve;

    @BindView(R.id.btn_update)
    Button btnUpdate;

    private PopupWindow popupWindow;

    private List<TorqueModelBean> torqueModelBeans = new ArrayList<>();
    private TorqueModelBean curSelectTorqueModeBean;
    private List<TorqueCurveBean> curEditTorqueCurveBeanList;
    private TorqueCurveApplyBean torqueCurveApplyBean;
    private TorqueCurveApplyBean saveTorqueCurveApplyBean;
    private boolean hasAddCustomModel;
    private String curveVersion;

    Unbinder binder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torque_curve);
        binder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initCurveData();
        initData();
        initView();
        DeviceManager.getInstance().queryTorqueCurve();
        checkUpdate();
//        new Test().testQueryStaticParameter();
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
        list.add(rvCurve);
        return list;
    }
    private void initCurveData(){
        TorqueModelBean modeFromSp = getModeFromSp();
        if(modeFromSp != null){
            torqueModelBeans.add(modeFromSp);
            hasAddCustomModel = true;
        }
        String versionSave = getVersion4Sp();
        InputStream open = null;
        if(!TextUtils.isEmpty(versionSave)){
            File file = CurveUpdateModel.getFile(this,versionSave);
            if(file.exists()){
                try {
                    open = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if(open == null) {
                open = getResources().getAssets().open("MomentLib_1.txt");
            }
            InputStreamReader inputStreamReader = new InputStreamReader(open);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String head;
            String version = reader.readLine();
            if(version !=null) {
                String[] split = version.split(":");
                curveVersion = split[1];
            }
            Log.e(TAG, "curveVersion:" + curveVersion);
            while ((head = reader.readLine()) != null){
                if(head.contains("[ML]:")){
                    String ampStr = reader.readLine();
                    String weiStr = reader.readLine();
                    parseCurveStr(head,ampStr,weiStr);
                }
            }
            reader.close();
            inputStreamReader.close();
            open.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseCurveStr(String head,String ampStr,String weiStr){
        TorqueModelBean torqueModelBean = new TorqueModelBean();
        int headDivider = head.indexOf(':');
        String modelStr = head.substring(headDivider+1);
        String[] splitModel = modelStr.split(",");
        String model = splitModel[0];
        int bigArmLen = Integer.valueOf(splitModel[1]);
        int magnification = Integer.valueOf(splitModel[2]);
//        Log.e("wch","model:"+model);
//        Log.e("wch","bigArmLen:"+bigArmLen);
//        Log.e("wch","magnification:"+magnification);
        torqueModelBean.setModel(model);
        torqueModelBean.setBigArmLen(bigArmLen);
        torqueModelBean.setMagnification(magnification);
        String[] splitAmp = ampStr.split(",");
        String[] splitWei = weiStr.split(",");
        List<TorqueCurveBean> torqueCurveBeans = new ArrayList<>();
        for(int i=0;i<splitAmp.length && i<splitWei.length;i++){
            String amp =splitAmp[i].replace(" ","");
            String wei =splitWei[i].replace(" ","");
            TorqueCurveBean torqueCurveBean = new TorqueCurveBean();
            torqueCurveBean.setAmplitude(Integer.valueOf(amp));
            torqueCurveBean.setWeight(Integer.valueOf(wei));
            torqueCurveBeans.add(torqueCurveBean);
        }
        torqueModelBean.setTorqueCurveBeanList(torqueCurveBeans);
        torqueModelBeans.add(torqueModelBean);
    }

    private void initData(){
        torqueCurveApplyBean = DeviceManager.getInstance().getZtrsDevice().getTorqueCurveApplyBean();
        initSelectCurve();
    }

    private void initView(){
        tvTitle.setText("力矩曲线");
        if(curSelectTorqueModeBean != null) {
            curEditTorqueCurveBeanList = curveClone(curSelectTorqueModeBean.getTorqueCurveBeanList());
            initCurve(curEditTorqueCurveBeanList);
            TorqueModelBean modelBean = curSelectTorqueModeBean;
            String modelStr = modelBean.getModel()+"_"+modelBean.getBigArmLen()+"_"+modelBean.getMagnification();
            spinnerModel.setText(modelStr);
            upDateView(curSelectTorqueModeBean);
        }
    }

    private void initSelectCurve(){
        if(torqueCurveApplyBean.getModel() == null){
            return;
        }
        if(torqueCurveApplyBean.getModel().contains("custom")){
            curSelectTorqueModeBean = getModeFromSp();
        }
        for(int i= 0;i<torqueModelBeans.size();i++){
            TorqueModelBean modelBean = torqueModelBeans.get(i);
            String model = modelBean.getModel()+"_"+modelBean.getBigArmLen()+"_"+modelBean.getMagnification();
            TorqueModelBean applyModelBean = torqueCurveApplyBean;
            String applyModel =applyModelBean.getModel()+"_"+applyModelBean.getBigArmLen()+"_"+applyModelBean.getMagnification();
            if(model.equals(applyModel)){
                curSelectTorqueModeBean = modelBean;
                break;
            }
        }
    }

    private void upDateView(TorqueModelBean torqueModelBean){

        int upWeightArmLen = torqueModelBean.getBigArmLen();
        tvBigArmLength.setText(String.format("%d",upWeightArmLen));

        byte magnification = (byte)torqueModelBean.getMagnification();
        tvMagnification.setText((magnification&0x00ff)+"倍率");

        int weight = torqueModelBean.getTorqueCurveBeanList().get(0).getWeight();
        tvWeight.setText(String.format("%.2f",weight/100.0));
    }

    private void initCurve(List<TorqueCurveBean> torqueCurveBeans){
        if(torqueCurveBeans ==null){
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        TorqueCureAdapter torqueCureAdapter = new TorqueCureAdapter();
        torqueCureAdapter.setData(torqueCurveBeans);
        rvCurve.setLayoutManager(linearLayoutManager);
        rvCurve.setAdapter(torqueCureAdapter);
    }


    private void showPopWindow(){
        List<String> stringList = new ArrayList<>();
        for(int i= 0;i<torqueModelBeans.size();i++){
            TorqueModelBean modelBean = torqueModelBeans.get(i);
            String model = modelBean.getModel()+"_"+modelBean.getBigArmLen()+"_"+modelBean.getMagnification();
            stringList.add(model);
        }
        ListView listView = new ListView(this);
        ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this,
                R.layout.settting_spinner_item, stringList);
        listView.setAdapter(adapter);
        listView.setDivider(null);
        listView.setBackground(getDrawable(R.drawable.setting_torque_model_bg));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                spinnerModel.setText((String) item);
                popupWindow.dismiss();
                curSelectTorqueModeBean = torqueModelBeans.get(position);
                curEditTorqueCurveBeanList = curveClone(curSelectTorqueModeBean.getTorqueCurveBeanList());
                initCurve(curEditTorqueCurveBeanList);
                upDateView(torqueModelBeans.get(position));
            }
        });
        popupWindow = new PopupWindow(listView, ScaleUtils.dip2px(this,140),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
//        popupWindow.setBackgroundDrawable(getDrawable(R.drawable.setting_torque_model_bg));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(spinnerModel,0, ScaleUtils.dip2px(this,3)
        , Gravity.BOTTOM);
    }

    private List<TorqueCurveBean> curveClone(List<TorqueCurveBean> torqueCurveBeanList){
        List<TorqueCurveBean> copyList = new ArrayList<>();
        for(int i=0;i<torqueCurveBeanList.size();i++){
            TorqueCurveBean copyItem = new TorqueCurveBean();
            copyItem.setAmplitude(torqueCurveBeanList.get(i).getAmplitude());
            copyItem.setWeight(torqueCurveBeanList.get(i).getWeight());
            copyList.add(copyItem);
        }
        return copyList;
    }

    @OnClick({R.id.btn_save,R.id.tv_back,R.id.spinner_model,R.id.btn_update})
    public void onClick(View view){
        if(view.getId() == R.id.btn_save){
            save();
        } else if(view.getId() == R.id.tv_back){
            finish();
        }else if(view.getId() == R.id.spinner_model){
            if(popupWindow != null && popupWindow.isShowing()){
                popupWindow.dismiss();
            }else {
                showPopWindow();
            }
        }else if(view.getId() == R.id.btn_update){
            String hostId = DeviceManager.getInstance().getZtrsDevice().getRegisterInfoBean().getHostId();
            Log.e(TAG,"hostId:"+hostId);
            versionModel.checkVersion(hostId);
        }
    }

    private void save(){
        String reInput = getString(R.string.setting_re_input);
        Log.e("wch","save");
        if(curSelectTorqueModeBean == null){
            return;
        }
        List<TorqueCurveBean> original = curSelectTorqueModeBean.getTorqueCurveBeanList();
        boolean modify = false;
        for(int i=0;i<original.size();i++){
            if(original.get(i).getAmplitude()!=curEditTorqueCurveBeanList.get(i).getAmplitude()){
                modify = true;
                break;
            }
            if(original.get(i).getWeight()!=curEditTorqueCurveBeanList.get(i).getWeight()){
                modify = true;
                break;
            }
        }
        Log.e("wch","modify:"+modify);
        saveTorqueCurveApplyBean = new TorqueCurveApplyBean();
        if(modify){
            saveTorqueCurveApplyBean.setModel("custom");
        }else {
            saveTorqueCurveApplyBean.setModel(curSelectTorqueModeBean.getModel());
        }
        saveTorqueCurveApplyBean.setBigArmLen(curSelectTorqueModeBean.getBigArmLen());
        saveTorqueCurveApplyBean.setMagnification(curSelectTorqueModeBean.getMagnification());
        saveTorqueCurveApplyBean.setTorqueCurveBeanList(curEditTorqueCurveBeanList);
        DeviceManager.getInstance().setTorqueCurve(saveTorqueCurveApplyBean);
    }


    private void saveModelToSp(TorqueModelBean torqueModelBean){
        SharedPreferences sp = getSharedPreferences("torqueCurve",MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("model",torqueModelBean.getModel());
        edit.putInt("bigAlarmLen",torqueModelBean.getBigArmLen());
        edit.putInt("magnification",torqueModelBean.getMagnification());
        String ampStr ="";
        String weiStr ="";

        List<TorqueCurveBean> torqueCurveBeanList = torqueModelBean.getTorqueCurveBeanList();
        for(int i = 0; i< torqueCurveBeanList.size()-1; i++){
            ampStr = ampStr+ torqueCurveBeanList.get(i).getAmplitude();
            ampStr = ampStr+",";

            weiStr = weiStr+ torqueCurveBeanList.get(i).getWeight();
            weiStr = weiStr+",";
        }
        ampStr = ampStr + torqueCurveBeanList.get(torqueCurveBeanList.size()-1).getAmplitude();
        weiStr = weiStr + torqueCurveBeanList.get(torqueCurveBeanList.size()-1).getWeight();
        edit.putString("ampStr",ampStr);
        edit.putString("weiStr",weiStr);
        edit.commit();
    }

    private TorqueModelBean getModeFromSp(){
        TorqueModelBean torqueModelBean = new TorqueModelBean();
        SharedPreferences sp = getSharedPreferences("torqueCurve",MODE_PRIVATE);
        String model = sp.getString("model",null);
        if(TextUtils.isEmpty(model)){
            return null;
        }
        int bigAlarmLen = sp.getInt("bigAlarmLen",0);
        int magnification = sp.getInt("magnification",0);
        torqueModelBean.setModel(model);
        torqueModelBean.setBigArmLen(bigAlarmLen);
        torqueModelBean.setMagnification(magnification);
        String ampStr =sp.getString("ampStr",null);
        String weiStr =sp.getString("weiStr",null);
        if(ampStr == null || weiStr == null){
            return null;
        }

        String[] splitAmp = ampStr.split(",");
        String[] splitWei = weiStr.split(",");
        List<TorqueCurveBean> torqueCurveBeans = new ArrayList<>();
        for(int i=0;i<splitAmp.length;i++){
            String amp =splitAmp[i].replace(" ","");
            String wei =splitWei[i].replace(" ","");
            TorqueCurveBean torqueCurveBean = new TorqueCurveBean();
            torqueCurveBean.setAmplitude(Integer.valueOf(amp));
            torqueCurveBean.setWeight(Integer.valueOf(wei));
            torqueCurveBeans.add(torqueCurveBean);
        }
        torqueModelBean.setTorqueCurveBeanList(torqueCurveBeans);
        return torqueModelBean;
    }

    private void saveVersion2Sp(String version){
        SharedPreferences sp = getSharedPreferences("torqueCurve",MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("version",version);
        edit.commit();
    }

    private String getVersion4Sp(){
        SharedPreferences sp = getSharedPreferences("torqueCurve",MODE_PRIVATE);
        String version = sp.getString("version",null);
        return version;
    }


    CurveUpdateModel versionModel;
    UpdateDialog updateDialog;
    private void checkUpdate(){
        versionModel = new ViewModelProvider(this).get(CurveUpdateModel.class);
        LiveData<String> curVersion = versionModel.getCurVersion();
        curVersion.observe(this, s -> { });
        versionModel.initVersion(this,curveVersion);
        LiveData<VersionModel.UpdateState> updateState = versionModel.getUpdateState();
        updateState.observe(this, updateState1 -> onUpdateStateChange(updateState1));
//        String hostId = DeviceManager.getInstance().getZtrsDevice().getRegisterInfoBean().getHostId();
//        Log.e(TAG,"hostId:"+hostId);
//        versionModel.checkVersion(hostId);
    }

    private void onUpdateStateChange(VersionModel.UpdateState updateState){
        Log.e(TAG,""+updateState);
        switch (updateState){
            case CHECKING:
                if(updateDialog == null || !updateDialog.isShowing()) {
                    updateDialog = new UpdateDialog(this);
                    updateDialog.initText("检测新版本中...");
                    updateDialog.initButton(false,false);
                    updateDialog.show();
                }
                break;
            case CHECK_SUCCESS_CAN_UPDATE:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("检测新版本: " + versionModel.getRemoteVersion()+"\n"+"确认升级？");
                    updateDialog.showButton();
                    updateDialog.setOnUserClick(() -> {
                        versionModel.downloadCurve(this);
                    });
                    updateDialog.show();
                }
                break;
            case CHECK_SUCCESS_NO_UPDATE:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("已经是最新版本");
                    updateDialog.showConfirm();
                    updateDialog.setOnUserClick(() -> updateDialog.dismiss());
                    updateDialog.show();
                }
                break;
            case CHECK_FAIL:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("检测新版本失败");
                    updateDialog.showConfirm();
                    updateDialog.setOnUserClick(() -> updateDialog.dismiss());
                    updateDialog.show();
                }
                break;

            case DOWNLOADING:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("新版本下载中...");
                    updateDialog.hideButton();
                    updateDialog.show();
                }
                break;
            case DOWNLOAD_SUCCESS:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("下载成功");
                    updateDialog.showButton();
                    updateDialog.setOnUserClick(() -> {
                        updateDialog.dismiss();
                        saveVersion2Sp(versionModel.getRemoteVersion());
                        torqueModelBeans.clear();
                        initCurveData();
                        initData();
                        initView();
                        versionModel.initVersion(this,curveVersion);
                    });
                    updateDialog.show();
                }
                break;
            case DOWNLOAD_FAIL:
                if(updateDialog.isShowing()) {
                    updateDialog.setText("抱歉，下载失败");
                    updateDialog.showConfirm();
                    updateDialog.setOnUserClick(() -> updateDialog.dismiss());
                    updateDialog.show();
                }
                break;
        }
    }


    //-----------------------------------------------------------//
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTorqueCurve(TorqueCurveMessage msg){
        LogUtils.LogI(TAG,"onTorqueCurve: "+msg.getCmdType());
        LogUtils.LogI(TAG,"onTorqueCurve: "+msg.getResult());
        if(msg.getCmdType() == BaseMessage.TYPE_CMD){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                Toast.makeText(this,"力矩曲线保存成功",Toast.LENGTH_LONG).show();
                if(saveTorqueCurveApplyBean.getModel().contains("custom")) {
                    saveModelToSp(saveTorqueCurveApplyBean);
                    if(!hasAddCustomModel){
                        torqueModelBeans.clear();
                        initCurveData();
                    }
                }
                DeviceManager.getInstance().getZtrsDevice().setTorqueCurveApplyBean(saveTorqueCurveApplyBean);
                initData();
                initView();
            }else {

                Toast.makeText(this,"力矩曲线保存失败",Toast.LENGTH_LONG).show();
            }
        }else if(msg.getCmdType() == BaseMessage.TYPE_QUERY){
            if(msg.getResult() == BaseMessage.RESULT_OK) {
                initData();
                initView();
            }else {

                Toast.makeText(this,"力矩曲线查询失败",Toast.LENGTH_LONG).show();
            }
        }
    }

}