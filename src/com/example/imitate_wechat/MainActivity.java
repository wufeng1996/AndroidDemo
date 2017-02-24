package com.example.imitate_wechat;

import java.util.ArrayList;
import java.util.List;

import com.example.imitate_wechat_view.AudioRecorderButton;
import com.example.imitate_wechat_view.PlayerManager;
import com.example.imitate_wechat_view.AudioRecorderButton.AudioFinishRecorderListener;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ListView mListView;
	private ArrayAdapter<Recorder> mAdapter;
	private List<Recorder> mDatas = new ArrayList<Recorder>();
	
	private AudioRecorderButton mAudioRecorderButton;
	//������������Ǹ�view
	private View mAnimView; 
	private AnimationDrawable anim;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		mListView=(ListView) findViewById(R.id.id_listview);
		
		mAudioRecorderButton=(AudioRecorderButton) findViewById(R.id.id_recorder_button);
		mAudioRecorderButton.setOnAudioFinishRecorderListener(new AudioFinishRecorderListener() {		
			@Override
			public void onFinish(float seconds, String filePath) {
				Recorder recorder=new Recorder(seconds,filePath);
				mDatas.add(recorder);
				mAdapter.notifyDataSetChanged();
				//�Զ�����
				mListView.setSelection(mDatas.size());
			}
		});
		
		mAdapter = new RecorderAdapter(this, mDatas);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			/**
			 * ����΢�ţ������ǰ�������ڲ��ţ�������ǰ������ȡ�����ţ������������������Ĳ����µ�����
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				boolean isAnother=true;  
				if(mAnimView!=null){
					if(((Long)mAnimView.getTag()).longValue()==id){
						isAnother=false;
					}
					//�������
					mAnimView.setBackgroundResource(R.drawable.adj);
					anim.stop();
					mAnimView=null;
					anim =null;
					//�������
					PlayerManager.reset();
				}
				if(isAnother){
					//���Ŷ���		
					mAnimView=view.findViewById(R.id.id_recorder_anim);
					mAnimView.setBackgroundResource(R.drawable.play_anim);
					mAnimView.setTag(new Long(id));
					anim=(AnimationDrawable) mAnimView.getBackground();
					anim.start();
					//������Ƶ
					PlayerManager.playSound(mDatas.get(position).filePath, new MediaPlayer.OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							mAnimView.setBackgroundResource(R.drawable.adj);
							anim.stop();
							mAnimView=null;
							anim =null;
						}
					});
				}
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		PlayerManager.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		PlayerManager.resume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		PlayerManager.release();
	}
	/**
	 * ÿ��¼���Ľ������װ��һ��Recorder ����¼��ʱ�䣬��ƵԴ
	 * @author Administrator
	 *
	 */
	class Recorder{
    	private float time;
    	private String filePath;
    	
		public Recorder(float time, String filePath) {
			super();
			this.time = time;
			this.filePath = filePath;
		}

		public float getTime() {
			return time;
		}

		public void setTime(float time) {
			this.time = time;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}	
    }
	
	
}
