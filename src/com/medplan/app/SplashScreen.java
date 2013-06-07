package com.medplan.app;

import java.io.IOException;

import com.medpan.util.CommonMethod;
import com.medpan.util.Constant;
import com.medplan.db.databasehelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class SplashScreen extends Activity {
	private final int SPLASH_DISPLAY_LENGTH = 3000;
	public static SharedPreferences SP;
	String autoUser,autoPassword,compare;
	databasehelper db;
	Intent intent;

	public static CommonMethod Cmethod;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.splash_screen);
		databasehelper helpDatabasehelper = new databasehelper(
				getApplicationContext());
			
			Cmethod=new CommonMethod();
			
			SP = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
			if(SP!=null){
				autoUser = SP.getString("username", "");
				autoPassword = SP.getString("password", "");
				compare= SP.getString("compare", "");
				Constant._StrCheck=SP.getString("OperationType", "");
				System.out.println("auto user ~~~~~~~~~"+autoUser);
				System.out.println("auto password~~~~~~~~~"+autoPassword);
				System.out.println("compare~~~~~~~~~"+compare);
				}
			
			
			 
			
		try {
			helpDatabasehelper.createDataBase();
			db = new databasehelper(this);
			db.openDataBase();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		/*
		 * New Handler to start the Main-Activity and close this Splash-Screen
		 * after some seconds.
		 */
		new Handler().postDelayed(new Runnable() {

			public void run() {
				if(autoUser!="" || autoPassword!= ""){
					boolean tmp = db.isValidUser(autoUser, autoPassword);
					Log.i("", "-------" + tmp);
					if (tmp) {
						Constant.username = autoUser;
						System.out.println("username within login screen"+ Constant.username);
						SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						Editor edit = sp.edit();
						Log.i("User Id Splashscreen ", ""+db.getUserID());
						edit.putInt("UserID", db.getUserID());
						edit.commit();
						intent = new Intent(SplashScreen.this, MainActivity.class);
						startActivity(intent);
						SplashScreen.this.finish();
					} else {
						Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
						startActivity(intent); 
						SplashScreen.this.finish();
					} 
					
				}
				else{
					Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
					startActivity(intent);
					SplashScreen.this.finish();
				}
				
			}
		}, SPLASH_DISPLAY_LENGTH);
	}
}