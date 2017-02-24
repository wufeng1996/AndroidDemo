package com.example.imitate_wechat_view;

import com.example.imitate_wechat.R;

import android.app.*;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
/**
 * ����Ϊ¼���Ի����࣬�Ի����Ϊ����״̬������¼�ƣ���ȡ����ʱ�����
 * �����贫��һ��Context����
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
	//�Ի�������״̬������¼�ƣ���ȡ����¼��ʱ�����
	public void recording(){
		if(mDialog !=null ){
			mIcon.setImageResource(R.drawable.recorder);
			mVoice.setImageResource(R.drawable.v1);
			mLabel.setText("��ָ�ϻ���ȡ������");
			
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
			mLabel.setText("�ɿ���ָ��ȡ������");
			
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
			mLabel.setText("¼��ʱ�����");
			
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
	 * ͨ��levelȥ����ͼƬ
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
