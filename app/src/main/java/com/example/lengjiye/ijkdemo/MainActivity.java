package com.example.lengjiye.ijkdemo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.lengjiye.ijkdemo.widget.media.AndroidMediaController;
import com.example.lengjiye.ijkdemo.widget.media.IRenderView;
import com.example.lengjiye.ijkdemo.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private IjkVideoView videoView;
    private Button btn_start,btn_stop, btn_quan, btn_heng;
    private AndroidMediaController mediaController;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initView();
    }

    private void init(){
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
//        ActionBar actionBar = getSupportActionBar();
        mediaController = new AndroidMediaController(this, true);// 初始化视频控制器
//        mediaController.setSupportActionBar(actionBar);
    }

    private void initView(){
        videoView = (IjkVideoView) findViewById(R.id.video_view);
        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        btn_stop = (Button)findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(this);
        btn_quan = (Button)findViewById(R.id.btn_quan);
        btn_quan.setOnClickListener(this);
        btn_heng = (Button)findViewById(R.id.btn_heng);
        btn_heng.setOnClickListener(this);
        frameLayout = (FrameLayout) findViewById(R.id.flVideoPlayer);
        Log.e("lz", "initView");
//        videoView.setVideoURI(Uri.parse("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8"));
        videoView.setVideoURI(Uri.parse("http://ugcydzd.qq.com/flv/33/240/j0304gd2gir.p702.1.mp4?vkey=7896655B8D38BABBB611CD97A64B8E5DE2AD24F972509E07EE5CB0F3A50FF8A61FB77424B84E8AE6F9A06E99367366EDB4C3D9F17C05EAECA07931991478BF3044FCE91EBCBEFA548B28569F911DA49FDA8EE88D8BD11C408B9713CB0C091F8C"));
        videoView.setMediaController(mediaController); // 设置控制器
        // 视频准备完成监听
        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                Log.e("lz", "准备完成，开始播放");
                btn_start.setEnabled(true);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                videoView.start();
                break;
            case R.id.btn_stop:
                videoView.pause();
                break;
            case R.id.btn_quan:
                videoView.toggleAspectRatio();
                break;
            case R.id.btn_heng:
                directionSwitch();
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("lz", "onConfigurationChanged");
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.e("lz", "横屏");
//            initVideoViewLayout(2);
        }else{
            Log.e("lz", "竖屏");
//            initVideoViewLayout(1);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!videoView.isBackgroundPlayEnabled()) {
            videoView.stopPlayback(); // 停止播放并释放内存
            videoView.release(true);
            videoView.stopBackgroundPlay();
        } else {
            videoView.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }

    /**
     * 屏幕方向切换
     */
    private void directionSwitch() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏切换成竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            //竖屏切换成横屏SENSOR
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
    }

    private void initVideoViewLayout(int type) {
        DisplayMetrics dm = new DisplayMetrics();
        Display display = this.getWindowManager().getDefaultDisplay();
        display.getMetrics(dm);
        int screenWidth = dm.widthPixels;// 获取屏幕宽度
        int screenHeight = dm.heightPixels;// 获取屏幕高度
        if (type == 1) {
            if (null != frameLayout) {
                RelativeLayout.LayoutParams fl = (RelativeLayout.LayoutParams) frameLayout.getLayoutParams();
                fl.width = screenWidth;
                fl.height = screenWidth * 2 / 3 - 100;
                frameLayout.setLayoutParams(fl);
            }
        } else if (type == 2) {
            if (null != frameLayout) {
                RelativeLayout.LayoutParams fl = (RelativeLayout.LayoutParams) frameLayout.getLayoutParams();
                fl.width = screenWidth;
                fl.height = screenHeight;
                frameLayout.setLayoutParams(fl);
            }
        }
    }
}
