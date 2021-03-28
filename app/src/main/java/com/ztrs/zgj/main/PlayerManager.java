package com.ztrs.zgj.main;


import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

public class PlayerManager {
    private static final String TAG = PlayerManager.class.getSimpleName();

    private static volatile PlayerManager playerManager;
    public static final int MUSIC_INITIAL = 100;
    public static final int MUSIC_PREPAREING = 101;
    public static final int MUSIC_PREPAREED = 102;
    public static final int MUSIC_BUFFERING = 103;
    public static final int MUSIC_START = 104;
    public static final int MUSIC_PAUSE = 105;
    public static final int MUSIC_ERROR = 106;
    public static final int MUSIC_STOP = 107;
    public static final int MUSIC_FINISHED = 108;

    private int curState = MUSIC_INITIAL;
    private float volume = 1.0f;

    public static PlayerManager getInstance(){
        if(playerManager == null){
            synchronized (PlayerManager.class){
                if(playerManager == null){
                    playerManager = new PlayerManager();
                }
            }
        }
        return playerManager;
    }

    private MediaPlayer mediaPlayer;
    
    public boolean isPlayerPlaying() {
        if(!hasAPlayer()){
            return false;
        }
        try {
            return mediaPlayer.isPlaying();
        } catch (Exception e) {
            Log.e("TAG","isPlayerPlaying Exception ");
            return false;
        }
    }

    public void playerPrepare(AssetFileDescriptor afd) {
        createPlayer(afd);
        prepare();
        playerStart();
    }


    public void playerStart() {
        mediaPlayer.start();
    }

    public void playerPause() {
        mediaPlayer.pause();
    }

    public void playerRelease() {
        if(!hasAPlayer()){
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void playerSeekTo(int postion) {
        mediaPlayer.seekTo(postion);
    }


    long getPlayerDuration() {
        return mediaPlayer.getDuration();
    }


    public long getPlayerCurPosition() {
        return mediaPlayer.getCurrentPosition();
    }


    public boolean hasAPlayer() {
        return mediaPlayer != null;
    }


    public void setVolume(float volume) {
        if(!hasAPlayer()){
            return;
        }
        mediaPlayer.setVolume(volume,volume);
    }

    private void createPlayer(AssetFileDescriptor afd) {
        playerInit();
        try {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException ioException) {
            Log.e("TAG","setDataSource ioException");
            releaseMediaPlayer();
        } catch (IllegalArgumentException e) {
            Log.e("TAG","setDataSource IllegalArgumentException");
            releaseMediaPlayer();
        } catch (SecurityException e) {
            Log.e("TAG","setDataSource SecurityException");
            releaseMediaPlayer();
        } catch (IllegalStateException e) {
            Log.e("TAG","setDataSource IllegalStateException");
            releaseMediaPlayer();
        }
    }

    private void prepare() {
        if (!hasAPlayer()) {
            return;
        }
        try {
            mediaPlayer.prepare();
            curState = MUSIC_PREPAREED;
        } catch (IllegalStateException e) {
            Log.e("TAG","prepareAsync IllegalStateException");
            releaseMediaPlayer();
        } catch (IOException e) {
            Log.e("TAG","prepareAsync IOException");
            releaseMediaPlayer();
        }
    }


    private void releaseMediaPlayer() {
        if (hasAPlayer()) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void playerInit() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build();
                mediaPlayer.setAudioAttributes(attributes);
            } else {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            mediaPlayer.setOnPreparedListener(mp -> {
                curState = MUSIC_PREPAREED;
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                curState = MUSIC_FINISHED;
            });
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                }
            });
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                curState = MUSIC_ERROR;
                return true;
            });
        } else {
            Log.e("TAG","player is already exist, reset");
            mediaPlayer.reset();
        }
    }
}
