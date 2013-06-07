package com.medplan.app;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medpan.util.Constant;

public class MainActivity extends Activity implements OnClickListener {
	ImageView ivLock, ivSetting;
	TextView headerTitle, tvStartMedplann, dateTime;
	public static boolean flag;
	RelativeLayout mainHeader;
	Thread myThread = null;
	boolean mActive;
	Handler mHandler;
	SimpleDateFormat sdf;
	Button btnLogout;
	SharedPreferences sp;
	private static final int HELLO_ID = 1;
	NotificationManager mNotificationManager;
	Notification notification;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		String ns = NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.app_logo;
		CharSequence tickerText = "On";
		long when = System.currentTimeMillis();

		notification = new Notification(icon, tickerText, when);

		Context context = getApplicationContext();
		CharSequence contentTitle = "Medplann is On";
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
 
		notification.setLatestEventInfo(context, contentTitle, "",
				contentIntent);

		notification.flags = Notification.FLAG_ONGOING_EVENT;
		
		sdf = new SimpleDateFormat("HH:mm:ss");
		startClock();

		btnLogout = (Button) findViewById(R.id.button_logout);
		btnLogout.setOnClickListener(this);

		ivLock = (ImageView) findViewById(R.id.ivLock);
		ivSetting = (ImageView) findViewById(R.id.ivMainSetting);
		dateTime = (TextView) findViewById(R.id.tvDateTime);

		ivSetting.setOnClickListener(this);
		ivLock.setOnClickListener(this);

		tvStartMedplann = (TextView) findViewById(R.id.tvStartmedplann);
		mainHeader = (RelativeLayout) findViewById(R.id.rlMainHeadlayout);

		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if (sp.getInt("lock", 0) == 1) {
			flag = true;
			ivLock.setBackgroundResource(R.drawable.start_thumbnail_on);
			tvStartMedplann.setText(R.string.stop_med);
			sp.edit().putInt("lock", 1).commit();

			mNotificationManager.notify(HELLO_ID, notification);
		} else {
			flag = false;
			ivLock.setBackgroundResource(R.drawable.start_thumbnail_off);
			tvStartMedplann.setText(R.string.start_med);
			sp.edit().putInt("lock", 0).commit();
			mNotificationManager.cancel(HELLO_ID);
		}
		
		if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
			mainHeader.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.main_layer_org));
		} else if (Constant._StrCheck.equalsIgnoreCase("Doctor/Physician")) {
			mainHeader.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.main_layer_green));
		} else {
			mainHeader.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.main_layer_blue));
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode== KeyEvent.KEYCODE_BACK){
			finish() ;
		}
		return super.onKeyDown(keyCode, event);
	}

	private final Runnable mRunnable = new Runnable() {
		public void run() {
			if (mActive) {
				if (dateTime != null) {
					// dateTime.setText(getTime());
				}
				mHandler.postDelayed(mRunnable, 1000);
			}
		}
	};

	public MainActivity() {
		mHandler = new Handler();
	}

	private void startClock() {
		mActive = true;
		mHandler.post(mRunnable);
	}

	public void changeLock() {
		if (flag) {
			ivLock.setBackgroundResource(R.drawable.start_thumbnail_off);
			tvStartMedplann.setText(R.string.start_med);
			sp.edit().putInt("lock", 0).commit();
			flag = false;
			mNotificationManager.cancel(HELLO_ID);
		} else {
			ivLock.setBackgroundResource(R.drawable.start_thumbnail_on);
			tvStartMedplann.setText(R.string.stop_med);
			sp.edit().putInt("lock", 1).commit();
			flag = true;
			mNotificationManager.notify(HELLO_ID, notification);
		}
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ivLock:
			changeLock();
			// Toast.makeText(MainActivity.this, "clicked"+flag,
			// Toast.LENGTH_SHORT).show();

			break;

		case R.id.ivMainSetting:
			intent = new Intent(MainActivity.this, ConfigureScreen.class);
			startActivity(intent);
			break;

		case R.id.button_logout:
			
			SharedPreferences spe = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Editor editer = spe.edit();
			editer.putString("isLogin", "no");
			editer.commit();
			
			System.out.println("clicked");
			
			mNotificationManager.cancelAll();
			
			
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			Editor edit = sp.edit();
			edit.putString("username", "");
			edit.putString("password", "");
			edit.putString("compare", "");
			edit.commit();
			intent = new Intent(MainActivity.this, LoginScreen.class);
			startActivity(intent);
			finish();
			break;
		}
	}

}