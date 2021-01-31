package com.ztrs.zgj.setting.http;

import android.util.Log;

import com.ztrs.zgj.setting.viewModel.AppUpdateViewModel;
import com.ztrs.zgj.setting.viewModel.CurveUpdateModel;
import com.ztrs.zgj.setting.viewModel.VersionModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    private static final String TAG = HttpRequest.class.getSimpleName();

    public void checkAppVersion(String deviceId, String ver, AppUpdateViewModel viewModel){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://t3.wujjc.com:2042/t3/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ZjgService zjgService = retrofit.create(ZjgService.class);
        Observable<AppCheckBean> appCheckBeanObservable
                = zjgService.checkAppVersion(deviceId, ver, System.currentTimeMillis());
        appCheckBeanObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AppCheckBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e(TAG,"APP onSubscribe");

                    }

                    @Override
                    public void onNext(@NonNull AppCheckBean appCheckBean) {
                        Log.e(TAG,"APP onNext: "+appCheckBean);
                        viewModel.onGetRemoteVersion(true,appCheckBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG,"APP onError");
                        e.printStackTrace();
                        viewModel.onGetRemoteVersion(false,null);
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG,"APP onComplete");
                    }
                });
    }

    public void downloadAppApk(String url,final AppUpdateViewModel versionModel,File saveFile){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://120.78.84.188/upgrade/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ZjgService apiService = retrofit.create(ZjgService.class);
        Observable<ResponseBody> versionBeanObservable = apiService.downloadApk(url);
        versionBeanObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody responseBody) throws Exception {
                        return HttpRequest.this.saveFile(responseBody, saveFile);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG,"onSubscribe");
                    }

                    @Override
                    public void onNext(Boolean result) {
                        Log.e(TAG,"onNext");
                        versionModel.onDownApk(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"onError");
                        e.printStackTrace();
                        versionModel.onDownApk(false);
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG,"onComplete");
                    }
                });
    }

    public void checkVersion(final VersionModel versionModel){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://120.78.84.188/upgrade/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ZjgService apiService = retrofit.create(ZjgService.class);
        Observable<VersionBean> versionBeanObservable = apiService.checkVersion();
        versionBeanObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VersionBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG,"onSubscribe");
                    }

                    @Override
                    public void onNext(VersionBean versionBean) {
                        Log.e(TAG,"onNext");
                        List<VersionBean.UpdatesBean> updates = versionBean.getUpdates();
                        if(updates != null){
                            for(VersionBean.UpdatesBean updatesBean:updates){
                                if("CGenie".equals(updatesBean.getProject())){
                                    versionModel.onGetRemoteVersion(true,updatesBean);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"onError");
                        e.printStackTrace();
                        versionModel.onGetRemoteVersion(false,null);
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG,"onComplete");
                    }
                });
    }

    public void downloadApk(String url,final VersionModel versionModel,File saveFile){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://120.78.84.188/upgrade/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ZjgService apiService = retrofit.create(ZjgService.class);
        Observable<ResponseBody> versionBeanObservable = apiService.downloadApk(url);
        versionBeanObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody responseBody) throws Exception {
                        return HttpRequest.this.saveFile(responseBody, saveFile);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG,"onSubscribe");
                    }

                    @Override
                    public void onNext(Boolean result) {
                        Log.e(TAG,"onNext");
                        versionModel.onDownApk(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"onError");
                        e.printStackTrace();
                        versionModel.onDownApk(false);
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG,"onComplete");
                    }
                });
    }

    private boolean saveFile(ResponseBody responseBody, File saveFile){
        byte[] buffer = new byte[1024];
        int len;
        InputStream inputStream = responseBody.byteStream();
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(saveFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        try {
            while ((len = inputStream.read(buffer)) > 0) {
                Log.e(TAG,"save file");
                fileOutputStream.write(buffer, 0, len);
            }
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public void checkCurveVersion(String deviceId, String ver, CurveUpdateModel viewModel){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://t3.wujjc.com:2042/t3/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ZjgService zjgService = retrofit.create(ZjgService.class);
        Observable<TorqueCurveCheckBean> appCheckBeanObservable
                = zjgService.checkDeviceVersion(deviceId, ver, System.currentTimeMillis());
        appCheckBeanObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TorqueCurveCheckBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e(TAG,"curve onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull TorqueCurveCheckBean torqueCurveCheckBean) {
                        Log.e(TAG,"curve onNext: "+torqueCurveCheckBean);
                        viewModel.onGetRemoteVersion(true,torqueCurveCheckBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG,"curve onError");
                        e.printStackTrace();
                        viewModel.onGetRemoteVersion(false,null);
                    }

                    @Override
                    public void onComplete() {
                            Log.e(TAG,"curve onComplete");
                    }
                });
    }

    public void downloadCurve(String url,final CurveUpdateModel versionModel,File saveFile){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://120.78.84.188/upgrade/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ZjgService apiService = retrofit.create(ZjgService.class);
        Observable<ResponseBody> versionBeanObservable = apiService.downloadApk(url);
        versionBeanObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody responseBody) throws Exception {
                        return HttpRequest.this.saveFile(responseBody, saveFile);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG,"onSubscribe");
                    }

                    @Override
                    public void onNext(Boolean result) {
                        Log.e(TAG,"onNext");
                        versionModel.onDownApk(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"onError");
                        e.printStackTrace();
                        versionModel.onDownApk(false);
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG,"onComplete");
                    }
                });
    }

}
