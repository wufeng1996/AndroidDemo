package com.example.imitate_wechat_view;

import com.example.imitate_wechat.R;
import com.example.imitate_wechat_view.RecorderManager.AudioStateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

@SuppressLint("HandlerLeak")
public class AudioRecorderButton extends Button implements AudioStateListener{
	//������ť����״̬�������������£���Ҫȡ��
	private static final int STATE_NORMAL=1; 
	private static final int STATE_PRESSING=2;
	private static final int STATE_WANT_TO_CANCEL=3;
	private int mCurState=STATE_NORMAL;
	
	private static final int DISTANCE_Y_CANCEL=50;
	//¼���׶εı�־λ
	private boolean isRecording=false;
	//�Ƿ񴥷�longClick
	private boolean mReady;
	
	private DialogManager mDialogManager;	
	private RecorderManager mRecorderManager;
	//¼��ʱ��
	private float mTime;
	
	public AudioRecorderButton(Context context) {
		this(context, null);
	}
	//��xml�����ļ����ص��Զ���view ���˹��췽������ȱ��
	public AudioRecorderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDialogManager=new DialogManager(context);
		String dir=Environment.getExternalStorageDirectory()+"/imitate_wechat";
		mRecorderManager=RecorderManager.getInstance(dir);
		mRecorderManager.setOnAudioStateListener(this);
		
		setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				mReady=true;
				mRecorderManager.prepareAudio();
				return false;
			}
		});
	}
	/**
	 * ¼����ɺ�Ļص�
	 * @author Administrator
	 *
	 */
	public interface AudioFinishRecorderListener{
		void onFinish(float seconds,String filePath);
	}
	private AudioFinishRecorderListener mListener;
	
	public void setOnAudioFinishRecorderListener(AudioFinishRecorderListener listener){
		mListener = listener;
	}
	
	//ΪHandler ���������Message����
	private static final int MSG_AUDIO_PREPARED=0X110;
	private static final int MSG_VOICE_CHANGED=0X111;
	private static final int MSG_DIALOG_DIMISS=0X112;
	
	private Runnable mGetVoiceLevelRunnable=new Runnable(){
		@Override
		public void run() {
			while(isRecording){
				try {
					Thread.sleep(100);
					mTime+=0.1f;
					mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
		
	};
	
	private Handler mHandler=new Handler(){

		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case MSG_AUDIO_PREPARED:
				mDialogManager.recording();
				isRecording=true;
				new Thread(mGetVoiceLevelRunnable).start();
				break;
			case MSG_VOICE_CHANGED:
				mDialogManager.updateVoiceLevel(mRecorderManager.getVoiceLevel(7));
				break;
			case MSG_DIALOG_DIMISS:
				mDialogManager.dimissDialog();
				break;
				
			}
		}
	};
	
	@Override
	public void wellPrepared() {
		mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);	
	}
	
	 
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action=event.getAction();
		int x=(int) event.getX();
		int y=(int) event.getY();
		
		switch(action){
		case MotionEvent.ACTION_DOWN:
			mDialogManager.initRecordingDialog();
			changeState(STATE_PRESSING);		
			break;
		case MotionEvent.ACTION_MOVE:
			if(isRecording){
				if(wantToCancel(x,y)){
					changeState(STATE_WANT_TO_CANCEL);
				}else{
					changeState(STATE_PRESSING);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			/**
			 * ���ᵯ��¼��ʱ����̵�dialog�����ᱨ�� sendUserActionEvent() mView==null
			 * this is not a problem related to your code, but related to S4 android version
			 */
			if(!mReady || !isRecording ){
				mDialogManager.tooShort();
				mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1500);
				if(mReady && !isRecording)
					mRecorderManager.cancel();
				reset();
				return super.onTouchEvent(event);
			}
			if(mCurState == STATE_PRESSING){
				mDialogManager.dimissDialog();
				if(mListener!= null){
					mListener.onFinish(mTime, mRecorderManager.getCurrentFilePath());
				}
				mRecorderManager.cancel();
			}else if(mCurState == STATE_WANT_TO_CANCEL){
				mDialogManager.dimissDialog();
				mRecorderManager.cancel();
			}
			reset();
			break;
		}
		return super.onTouchEvent(event);
	}
	/***
	 * �ָ�״̬����־λ
	 */
	private void reset() {
		isRecording =false;
		mReady=false;
		mTime=0;
		changeState(STATE_NORMAL);
	}
	
	private boolean wantToCancel(int x, int y) {
		if(x < 0 || x > getWidth())
			return true;
		if(y<-DISTANCE_Y_CANCEL || y>getHeight()+DISTANCE_Y_CANCEL)
			return true;
		
		return false;
	}
	
	private void changeState(int state) {
		if(mCurState != state){
			mCurState = state;
			switch(mCurState){
			case STATE_NORMAL:
				setBackgroundResource(R.drawable.btn_recorder_normal);
				setText(R.string.str_recorder_normal);
				mDialogManager.dimissDialog();
				break;
			case STATE_PRESSING:
				setBackgroundResource(R.drawable.btn_recording);
				setText(R.string.str_recorder_recording);
				if(isRecording)
					mDialogManager.recording();
				break;
			case STATE_WANT_TO_CANCEL:
				setBackgroundResource(R.drawable.btn_recording);
				setText(R.string.str_recorder_want_cancel);
				mDialogManager.wantToCancel();
				break;
			}
		}
	}
	
}
