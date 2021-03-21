package com.ztrs.zgj.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.ActivityFullScreenBinding;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.io.File;
import java.util.ArrayList;

import static com.ztrs.zgj.main.MainActivity.PLAYER_STATE_IDLE;
import static com.ztrs.zgj.main.MainActivity.PLAYER_STATE_PAUSE;
import static com.ztrs.zgj.main.MainActivity.PLAYER_STATE_PLAYING;
import static com.ztrs.zgj.main.MainActivity.PLAYER_STATE_PREPARING;
import static com.ztrs.zgj.main.MainActivity.PLAYER_STATE_STOP;

public class FullScreenActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = FullScreenActivity.class.getSimpleName();

    ActivityFullScreenBinding binding;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_full_screen);
        String title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(title)) {
            binding.rlTitle.tvTitle.setText(title);
        }else {
            binding.rlTitle.tvTitle.setText("监控视频");
        }

        binding.rlTitle.tvBack.setOnClickListener(this);
        binding.btnPlay.setOnClickListener(this);
        initVlc();
        startPlay();
    }

    @Override
    protected void onDestroy() {
        stopPlay();
        releasePlay();
        super.onDestroy();
    }



    private int playerState;

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_play:
                if(playerState == PLAYER_STATE_IDLE
                        || playerState == PLAYER_STATE_STOP){
                    startPlay();
                    return;
                }
                if(mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }else {
                    mMediaPlayer.play();
                }
                break;
            case R.id.img_volume:
                if(mute){
                    mute = false;
                    updateVolume();
                    binding.imgVolume.setBackgroundColor(getColor(R.color.transparent));
                }else {
                    mute = true;
                    mute();
                    binding.imgVolume.setBackgroundColor(getColor(R.color.primary_background_color_blue));
                }
                break;
        }
    }

    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;
    private final int UPDATE_SCREEN = 0;
    private final int UPDATE_FULL_SCREEN = 1;
    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_SCREEN:
                    //frame的屏幕大小
                    int screen_width = binding.rlVideo.getWidth();
                    int screen_height = binding.rlVideo.getHeight();
                    Log.d(TAG, "screen_width:" + screen_width + " screen_height:" + screen_height);
                    mMediaPlayer.getVLCVout().setWindowSize(screen_width, screen_height);
                    mMediaPlayer.setAspectRatio(screen_width + ":" + screen_height);//设置屏幕比例
                    mMediaPlayer.setScale(0);
                    break;

            }
            return false;
        }
    });

    private void initVlc(){
        final ArrayList<String> args = new ArrayList<>();//VLC参数
        args.add("--rtsp-tcp");//强制rtsp-tcp，加快加载视频速度
        args.add("--live-caching=0");
        args.add("--file-caching=0");
        args.add("--network-caching=0");//增加实时性，延时大概2-3秒
        mLibVLC = new LibVLC(this, args);
        mMediaPlayer = new MediaPlayer(mLibVLC);
        mMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                if (event.type == MediaPlayer.Event.Opening) {
                    Log.d(TAG, "VLC Opening");
                }
                else if (event.type == MediaPlayer.Event.Buffering){
                    Log.d(TAG, "VLC Buffering：" + event.getBuffering());

                }
                else if (event.type == MediaPlayer.Event.Playing){
                    Log.d(TAG, "VLC Playing");
                    playerState = PLAYER_STATE_PLAYING;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.rlVideoBg.setVisibility(View.GONE);
                            binding.btnPlay.setText("暂停");
                        }
                    });             }
                else if (event.type == MediaPlayer.Event.Paused){
                    Log.d(TAG, "VLC Pause");
                    playerState = PLAYER_STATE_PAUSE;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.btnPlay.setText("播放");
                        }
                    });
                }
                else if (event.type == MediaPlayer.Event.Stopped){
                    Log.d(TAG, "VLC Stopped");
                    playerState = PLAYER_STATE_STOP;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d(TAG, "VLC Stopped detachViews");
                            mMediaPlayer.detachViews();
                            binding.btnPlay.setText("播放");
                        }
                    });
                }
                else if (event.type == MediaPlayer.Event.EncounteredError){
                    Log.d(TAG, "VLC EncounteredError");
                    Toast.makeText(FullScreenActivity.this, "播放失败", Toast.LENGTH_LONG).show();
                }
                else if (event.type == MediaPlayer.Event.Vout){
                    Log.d(TAG, "VLC Vout"+ event.getVoutCount());
                    if(event.getVoutCount() >0) {
                        mHandler.sendEmptyMessageDelayed(UPDATE_SCREEN, 100);
                    }
                }
                else if (event.type == MediaPlayer.Event.RecordChanged){
                    Log.d(TAG, "VLC RecordChanged");
                }
                else if (event.type == MediaPlayer.Event.EndReached){
                    Log.d(TAG, "VLC EndReached");
                }
            }
        });
    }
    private void startPlay(){
        Log.e(TAG,"startPlay");
        playerState = PLAYER_STATE_PREPARING;
        mMediaPlayer.attachViews(binding.videoLayout, null, true, false);
        mMediaPlayer.setVideoScale(MediaPlayer.ScaleType.SURFACE_BEST_FIT);
        Uri uri = Uri.parse(url);//rtsp流地址或其他流地址
        //final Media media = new Media(mLibVLC, getAssets().openFd(ASSET_FILENAME));
        final Media media = new Media(mLibVLC, uri);
        media.setHWDecoderEnabled(false, false);//设置后才可以录制和截屏
        mMediaPlayer.setMedia(media);
        media.release();
        mMediaPlayer.play();

    }

    private void pausePlay(){
        mMediaPlayer.pause();
    }

    private void stopPlay(){
        mMediaPlayer.stop();
        mMediaPlayer.detachViews();
    }

    private void releasePlay(){
        mMediaPlayer.release();
        mLibVLC.release();
    }

    private void updateVolume(){
        if(mMediaPlayer != null){
            SharedPreferences sp = getSharedPreferences("SystemSetting",MODE_PRIVATE);
            int volume = sp.getInt("volume",50);
            mMediaPlayer.setVolume(volume);
            Log.e("wch","updatevolume:"+volume);
        }
    }

    private boolean mute = false;
    private void mute(){
        if(mMediaPlayer != null){
            mMediaPlayer.setVolume(0);
        }
    }
}