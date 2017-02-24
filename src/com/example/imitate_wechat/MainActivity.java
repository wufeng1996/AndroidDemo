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
	//拟产生动画的那个view
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
				//自动播放
				mListView.setSelection(mDatas.size());
			}
		});
		
		mAdapter = new RecorderAdapter(this, mDatas);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			/**
			 * 仿造微信，如果当前有语音在播放，单击当前语音会取消播放，单击另外的语音框，则改播放新的语音
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				boolean isAnother=true;  
				if(mAnimView!=null){
					if(((Long)mAnimView.getTag()).longValue()==id){
						isAnother=false;
					}
					//动画清除
					mAnimView.setBackgroundResource(R.drawable.adj);
					anim.stop();
					mAnimView=null;
					anim =null;
					//声音清除
					PlayerManager.reset();
				}
				if(isAnother){
					//播放动画		
					mAnimView=view.findViewById(R.id.id_recorder_anim);
					mAnimView.setBackgroundResource(R.drawable.play_anim);
					mAnimView.setTag(new Long(id));
					anim=(AnimationDrawable) mAnimView.getBackground();
					anim.start();
					//播放音频
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
	 * 每次录音的结果，封装成一个Recorder 包含录音时间，音频源
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
