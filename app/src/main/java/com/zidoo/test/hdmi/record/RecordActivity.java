package com.zidoo.test.hdmi.record;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.zuishare_test.R;
import com.zidoo.test.hdmi.ZidooHdmiDisPlay;

public class RecordActivity extends Activity {
	private ZidooHdmiDisPlay	mRealtekeHdmi	= null;
	private Button				mRecordView		= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_ac);
		mRecordView = (Button) findViewById(R.id.home_ac_start_record);
		initService();
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
			//没有权限则申请权限
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
		}
	}

	private void initService() {
		ViewGroup hdmiGroud = (ViewGroup) findViewById(R.id.home_ac_hdmi);
		mRealtekeHdmi = new ZidooHdmiDisPlay(RecordActivity.this, hdmiGroud, new ZidooHdmiDisPlay.HdmiInFristDisplayListener() {
			@Override
			public void fristDisplay() {
				mRealtekeHdmi.setAudio(true);
			}
		}, ZidooHdmiDisPlay.TYPE_SURFACEVIEW);
		mRealtekeHdmi.startDisPlay();
	}

	@Override
	protected void onDestroy() {
		if (mRealtekeHdmi != null) {
			mRealtekeHdmi.exit();
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		try {
			View thisView = getWindow().getDecorView();
			thisView.setSystemUiVisibility((View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY));
		} catch (Exception e) {
			e.printStackTrace();
		}
		mRealtekeHdmi.startDisPlay();
		super.onResume();
	}

	public void onMyClick(View view) {
		switch (view.getId()) {
		case R.id.home_ac_start_record:
			record();
			break;
		default:
			break;
		}
	}

	private void record() {
		if (mRealtekeHdmi.isRecording()) {
			mRealtekeHdmi.stopRecorder();
			mRecordView.setText("Start recorde");
		} else {
			if (mRealtekeHdmi.startRecorder(ZidooHdmiDisPlay.RECORD_FORMAT_TS)) {
				mRecordView.setText("Stop recorde");
			}
		}
	}

}
