package com.example.imitate_wechat_view;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;
/**
 * 该类采用单例模式，是application级别
 * 该类需传入一个dir：音频文件的存储位置
 * @author Administrator
 *
 */
public class RecorderManager {
	private MediaRecorder mMediaRecorder; //音频录制对象
	private String mDir;
	private String mCurrentFilePath;
	
	private boolean isPrepared;
	
	private static RecorderManager mInstance;
	
	/**
	 * 回调机制
	 * @author Administrator
	 *
	 */
	public interface AudioStateListener{
		void wellPrepared();
	}
	public AudioStateListener mListener;
	
	public void setOnAudioStateListener(AudioStateListener listener){
		mListener=listener;
	}
	
	
	
	private RecorderManager(String dir){
		mDir=dir;
	}
	
	public static RecorderManager getInstance(String dir){
		if(mInstance == null){
			synchronized (RecorderManager.class) {
				if(mInstance == null){
					mInstance=new RecorderManager(dir);
				}
			}
		}
		return mInstance;
	}
	
	@SuppressWarnings("deprecation")
	public void prepareAudio(){
		
		File dir=new File(mDir);
		if(!dir.exists()){
			dir.mkdirs();
		}
		String fileName=generateFileName();
		File file=new File(dir,fileName);
		mCurrentFilePath = file.getAbsolutePath();
		
		mMediaRecorder = new MediaRecorder();
		
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		//设置音频的格式
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		//设置音频的编码为amr
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mMediaRecorder.setAudioSamplingRate(8000);
		mMediaRecorder.setOutputFile(mCurrentFilePath);
		try {
			mMediaRecorder.prepare();
			mMediaRecorder.start();
			isPrepared=true;
			//准备工作完成
			if(mListener != null){
				mListener.wellPrepared();
			}
		} catch (IllegalStateException e) {			
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}
	}
	
	private String generateFileName() {
		return UUID.randomUUID().toString()+".amr";
	}



	public int getVoiceLevel(int maxLevel){
		if(isPrepared){
			//getMaxAmplitude 返回最大振幅(0~32767)
			try {
				return (int) (mMediaRecorder.getMaxAmplitude()/32768f * maxLevel + 1 );
			} catch (Exception e) {

			}
		}
		return 1;
	}
	
	public void release(){
		if(mMediaRecorder != null){
			mMediaRecorder.stop();
			mMediaRecorder.release();
			mMediaRecorder = null;
		}
	}
	
	public void cancel(){
		isPrepared=false;  //位置待定
		release();
		if(mCurrentFilePath != null){		
			mCurrentFilePath = null;				
		}
	}

	public String getCurrentFilePath() {
		return mCurrentFilePath;
	}
	
	
	
	
}
