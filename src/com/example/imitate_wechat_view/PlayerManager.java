package com.example.imitate_wechat_view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
/**
 * 从SD卡读取文件，播放声音
 * @author Administrator
 *
 */
public class PlayerManager {
	
	private static MediaPlayer mMediaPlayer; 
	private static boolean isPause;
	
	public static void playSound(String filePath,
			OnCompletionListener onCompletionListener){
		if(mMediaPlayer == null){
			mMediaPlayer=new MediaPlayer();
			mMediaPlayer.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					mMediaPlayer.reset();
					return false;
				}
			});
		}else{
			mMediaPlayer.reset();
		}
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setOnCompletionListener(onCompletionListener);
		try {					
			mMediaPlayer.setDataSource(filePath);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public static void reset(){
		if(mMediaPlayer!=null ){
			mMediaPlayer.reset();
		}
	}
	
	public static void pause(){
		if(mMediaPlayer!=null && mMediaPlayer.isPlaying()){
			mMediaPlayer.pause();
			isPause=true;
		}		
	}
		
	public static void resume(){
		if(mMediaPlayer!=null && isPause){
			mMediaPlayer.start();
			isPause=false;
		}
	}
		
	public static void release(){
		if(mMediaPlayer!=null){
			mMediaPlayer.release();
			mMediaPlayer=null;
		}
	}
		
	
}
