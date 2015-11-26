package com.example.androidtest;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class VideoPlayerDemo extends Activity implements Callback {
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_player);
		mSurfaceView = (SurfaceView) findViewById(R.id.surface_video);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		// mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.setKeepScreenOn(true);
		mMediaPlayer = new MediaPlayer();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		try {
			play();
		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | IOException e) {
			Log.i("sunkun:", e.getMessage());
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void play() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
		mMediaPlayer.reset();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 设置需要播放的视频
		mMediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.spdiy));
        // 把视频画面输出到SurfaceView
		mMediaPlayer.setDisplay(mSurfaceHolder);
		mMediaPlayer.prepare();
        // 获取窗口管理器
//        WindowManager wManager = getWindowManager();
//        DisplayMetrics metrics = new DisplayMetrics();
//        // 获取屏幕大小
//        wManager.getDefaultDisplay().getMetrics(metrics);
//        // 设置视频保持纵横比缩放到占满整个屏幕
//        surfaceView.setLayoutParams(new LayoutParams(metrics.widthPixels
//            , mPlayer.getVideoHeight() * metrics.widthPixels
//            / mPlayer.getVideoWidth()));
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mMediaPlayer.isPlaying())
			mMediaPlayer.stop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMediaPlayer.isPlaying())
			mMediaPlayer.stop();
		mMediaPlayer.release();
	}
}
