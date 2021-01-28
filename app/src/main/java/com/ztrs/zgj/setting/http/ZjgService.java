package com.ztrs.zgj.setting.http;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ZjgService {

    @GET("update/momentlib")
    public Observable<TorqueCurveCheckBean> checkDeviceVersion(@Query("device") String device,
                                            @Query("ver") String ver,
                                            @Query("t") long time);

    @GET("update/app")
    public Observable<AppCheckBean> checkAppVersion(@Query("device") String device,
                            @Query("ver") String ver,
                            @Query("t") long time);


    @GET("update.json")
    Observable<VersionBean> checkVersion();

    @Streaming
    @GET
    Observable<ResponseBody> downloadApk(@Url String url);

}
