package com.medplan.app;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.medpan.util.Constant;

public class ConfigureScreen extends Activity implements OnClickListener {
	RelativeLayout headTitleLayout, userManage, pictureManage, medicalManage, physicianManage,
			mobileManage, mailManage, cellManage, reportManage, alarmManage,logout;
	static int userType = 1;

	// themes
	LinearLayout mainLayout;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.configure_screen);

		 
		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(ConfigureScreen.this,MainActivity.class);
				startActivity(intent);
			}
		});
		

		userManage = (RelativeLayout) findViewById(R.id.rlUserManage);
		userManage.setOnClickListener(this);
		pictureManage = (RelativeLayout) findViewById(R.id.rlPicManage);
		pictureManage.setOnClickListener(this);
		medicalManage = (RelativeLayout) findViewById(R.id.rlMedicalManage);
		medicalManage.setOnClickListener(this);
		physicianManage = (RelativeLayout) findViewById(R.id.rlPhyManage);
		physicianManage.setOnClickListener(this);
		mobileManage = (RelativeLayout) findViewById(R.id.rlMobileManage);
		mobileManage.setOnClickListener(this);
		mailManage = (RelativeLayout) findViewById(R.id.rlMailManage);
		mailManage.setOnClickListener(this);
		cellManage = (RelativeLayout) findViewById(R.id.rlCellManage);
		cellManage.setOnClickListener(this);
		reportManage = (RelativeLayout) findViewById(R.id.rlReportManage);
		reportManage.setOnClickListener(this);
		
		alarmManage = (RelativeLayout) findViewById(R.id.rlAlarmManage);
		alarmManage.setOnClickListener(this);
		logout= (RelativeLayout) findViewById(R.id.rllogout);
		logout.setOnClickListener(this);

		// themes
		mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
		headTitleLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
	//	userLayout = (RelativeLayout) findViewById(R.id.rlUserManage);
		//picLayout = (RelativeLayout) findViewById(R.id.rlPicManage);
	//	medLayout = (RelativeLayout) findViewById(R.id.rlMedicalManage);
	//	phyLayout = (RelativeLayout) findViewById(R.id.rlPhyManage);
	//	mobileLayout = (RelativeLayout) findViewById(R.id.rlMobileManage);
	//	mailLayout = (RelativeLayout) findViewById(R.id.rlMailManage);
	//	dateLayout = (RelativeLayout) findViewById(R.id.rlDTManage);
	//	cellLayout = (RelativeLayout) findViewById(R.id.rlCellManage);
	//	AlarmLayout = (RelativeLayout) findViewById(R.id.rlAlarmManage);

		System.out.println("username within main screen" + Constant.username);
		try{ 
		SplashScreen.Cmethod.CheckConfigureScreen(Constant._StrCheck,
				mainLayout, headTitleLayout, userManage, pictureManage, medicalManage,
				physicianManage, mobileManage, mailManage, reportManage, cellManage,
				alarmManage,logout);
		}catch (Exception e) {
			
		}
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.rlUserManage:
			intent = new Intent(ConfigureScreen.this, UserManageActivity.class);
			startActivity(intent);
			break;
		case R.id.rlPicManage:
			intent = new Intent(ConfigureScreen.this,
					PictureManageActivity.class);
			startActivity(intent);
			break;
		case R.id.rlMedicalManage:
			intent = new Intent(ConfigureScreen.this,
					MediManageActivity.class);
			startActivity(intent);
			break;
		case R.id.rlPhyManage:
			intent = new Intent(ConfigureScreen.this,
					PhysicianManageActivity.class);
			startActivity(intent);
			break;
		case R.id.rlMobileManage:
			intent = new Intent(ConfigureScreen.this,
					MobileManageActivity.class);
			startActivity(intent);
			break;
		case R.id.rlCellManage:
			intent = new Intent(ConfigureScreen.this, AssignUser_ListActivity.class);
			intent.putExtra("Task", "Cell");
			
			startActivity(intent);
			break;
		case R.id.rlAlarmManage:
			intent = new Intent(ConfigureScreen.this, AlarmManageActivity.class);
			intent.putExtra("Task", "Alarm");
			startActivity(intent);
			break;

		case R.id.rlMailManage:
			System.out.println("clicked");
			try
			{
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName("com.google.android.gm",
			"com.google.android.gm.ConversationListActivity");
			startActivity(intent);
			}
			catch (Exception e) {
			try{	
			intent = getPackageManager().getLaunchIntentForPackage("com.android.email");
			startActivity(intent);
			}catch (Exception ef) {
				Toast.makeText(ConfigureScreen.this, "Could not open the email client.", Toast.LENGTH_LONG).show();
			}
			}
			//intent = getPackageManager().getLaunchIntentForPackage("com.android.email");
			//startActivity(intent);
			break;
			
		case R.id.rlReportManage:
			intent = new Intent(ConfigureScreen.this, UserReportActivity.class);
			startActivity(intent);
			break;
			
		case R.id.rllogout:
//			System.out.println("clicked");
//			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//			Editor edit = sp.edit();
//			edit.putString("username", "");
//			edit.putString("password", "");
//			edit.putString("compare", "");
//			edit.commit();
//			intent = new Intent(ConfigureScreen.this, LoginScreen.class);
//			startActivity(intent);
			//intent = getPackageManager().getLaunchIntentForPackage("com.android.email");
			//startActivity(intent);
			break;
		}
	}
}