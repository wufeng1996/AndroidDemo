package com.example.imitate_wechat_view;

import com.example.imitate_wechat.R;

import android.app.*;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
/**
 * 此类为录音对话框类，对话框分为三种状态：正常录制，欲取消，时间过短
 * 该类需传入一个Context对象
 * @author Administrator
 *
 */
public class DialogManager {
	
	private Dialog mDialog;
	
	private ImageView mIcon;
	private ImageView mVoice;
	
	private TextView mLabel;
	
	private Context mContext;

	public DialogManager(Context context){
		mContext=context;
	}
	
	public void initRecordingDialog(){
		mDialog=new Dialog(mContext,R.style.Theme_AudioDialog);
		LayoutInflater inflater=LayoutInflater.from(mContext);
		View view=inflater.inflate(R.layout.dialog_recorder, null);
		mDialog.setContentView(view);
		
		mIcon=(ImageView) mDialog.findViewById(R.id.id_recorder_dialog_icon);
		mVoice=(ImageView) mDialog.findViewById(R.id.id_recorder_dialog_voice);
		mLabel=(TextView) mDialog.findViewById(R.id.id_recorder_dialog_label);
		
	}
	//对话框三种状态：正常录制，欲取消，录制时间过短
	public void recording(){
		if(mDialog !=null ){
			mIcon.setImageResource(R.drawable.recorder);
			mVoice.setImageResource(R.drawable.v1);
			mLabel.setText("手指上滑，取消发送");
			
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.VISIBLE);
			mLabel.setVisibility(View.VISIBLE);
			if(!mDialog.isShowing())
				mDialog.show();
		}
	}
	
	public void wantToCancel(){
		if(mDialog !=null ){
			mIcon.setImageResource(R.drawable.cancel);
			mLabel.setText("松开手指，取消发送");
			
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);
			mLabel.setVisibility(View.VISIBLE);
			if(!mDialog.isShowing())
				mDialog.show();
		}
	}
	
	public void tooShort(){
		if(mDialog !=null ){
			mIcon.setImageResource(R.drawable.voice_to_short);
			mLabel.setText("录音时间过短");
			
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);
			mLabel.setVisibility(View.VISIBLE);
			if(!mDialog.isShowing())
				mDialog.show();
		}
	}
	
	public void dimissDialog(){
		if(mDialog !=null && mDialog.isShowing()){
			mDialog.dismiss();
			mDialog=null;
		}
	}
	/**
	 * 通过level去更新图片
	 * @param level
	 */
	public void updateVoiceLevel(int level){
		if(mDialog !=null && mDialog.isShowing()){
		
			int resId=mContext.getResources().getIdentifier("v"+level, "drawable", 
					mContext.getPackageName());
			mVoice.setImageResource(resId);
	
		}
	}

	public TextView getmLabel() {
		return mLabel;
	}
	
	
	
	
	
	
	
	
	
	
}
