package com.example.imitate_wechat_view;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;
/**
 * ������õ���ģʽ����application����
 * �����贫��һ��dir����Ƶ�ļ��Ĵ洢λ��
 * @author Administrator
 *
 */
public class RecorderManager {
	private MediaRecorder mMediaRecorder; //��Ƶ¼�ƶ���
	private String mDir;
	private String mCurrentFilePath;
	
	private boolean isPrepared;
	
	private static RecorderManager mInstance;
	
	/**
	 * �ص�����
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
		//������Ƶ�ĸ�ʽ
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		//������Ƶ�ı���Ϊamr
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mMediaRecorder.setAudioSamplingRate(8000);
		mMediaRecorder.setOutputFile(mCurrentFilePath);
		try {
			mMediaRecorder.prepare();
			mMediaRecorder.start();
			isPrepared=true;
			//׼���������
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
			//getMaxAmplitude ����������(0~32767)
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
		isPrepared=false;  //λ�ô���
		release();
		if(mCurrentFilePath != null){		
			mCurrentFilePath = null;				
		}
	}

	public String getCurrentFilePath() {
		return mCurrentFilePath;
	}
	
	
	
	
}
