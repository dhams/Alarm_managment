package com.medplan.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.medpan.util.Constant;
import com.medplan.db.databasehelper;

public class LoginScreen extends Activity implements OnClickListener {
	Button loginButton;
	TextView tvSignUp;
	databasehelper db;
	EditText uName, uPwd;
	static AlertDialog loginMessage;
	static AlertDialog.Builder loginDialog;
	String unm,pwd;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		getWindow().setWindowAnimations(0);
		setContentView(R.layout.login_screen);
 
		db = new databasehelper(this);

		loginButton = (Button) findViewById(R.id.button1);
		tvSignUp = (TextView) findViewById(R.id.tv_log_signup);
		uName = (EditText) findViewById(R.id.etUname);
		uPwd = (EditText) findViewById(R.id.etPwd);
		loginButton.setOnClickListener(this);
		tvSignUp.setOnClickListener(this);
		
		loginDialog = new AlertDialog.Builder(this);
		loginDialog.setTitle(R.string.auto_login);
		loginDialog.setMessage(R.string.store_user_login);
		loginDialog.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							

						}
					});
		loginDialog.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Constant.username = uName.getText().toString();
							System.out.println("username within login screen"+ Constant.username);
							SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
							Editor edit = sp.edit();
							edit.putInt("UserID", db.getUserID());
							Log.i("User Id Login ", ""+db.getUserID());
							edit.putString("username", unm);
							edit.putString("password", pwd);
							edit.putString("compare", "compare");
							edit.commit();
							Intent intent = new Intent(LoginScreen.this, MainActivity.class);
							startActivity(intent);
							LoginScreen.this.finish();
								
						}
					});
		
		loginDialog.setNegativeButton(R.string.no_thanks, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
			Intent	intent = new Intent(LoginScreen.this, MainActivity.class);
				startActivity(intent);
				LoginScreen.this.finish();
			}
		});
		 loginMessage = loginDialog.create();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode==KeyEvent.KEYCODE_BACK){
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
		} 
		
		return super.onKeyDown(keyCode, event);
	}
	
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button1:

			if (uName.getText().toString().equalsIgnoreCase("")
					|| uPwd.getText().toString().equalsIgnoreCase("")) {
				Toast.makeText(LoginScreen.this,
						getResources().getString(R.string.incorrect_login), Toast.LENGTH_LONG)
						.show();
			} else {
				unm= uName.getText().toString();
				pwd= uPwd.getText().toString();
				boolean tmp = db.isValidUser(unm, pwd);
				Log.i("", "-------" + tmp);
				if (tmp) {
					SharedPreferences spe = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					Editor editer = spe.edit();
					editer.putString("isLogin", "yes");
					editer.commit();
					
					
					if(Constant._StrCheck.equalsIgnoreCase("User/Patient")){
						loginMessage.show();
					}
					else{
						Constant.username = uName.getText().toString();
						System.out.println("username within login screen"+ Constant.username);
						SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						Editor edit = sp.edit();
						edit.putInt("UserID", db.getUserID());
						edit.commit();
						intent = new Intent(LoginScreen.this, MainActivity.class);
						startActivity(intent);
						LoginScreen.this.finish();	
					}
					
				} else {
					Toast.makeText(LoginScreen.this,
							R.string.incorrect_login,
							Toast.LENGTH_LONG).show();
				}
			}

			break;

		case R.id.tv_log_signup:
			intent = new Intent(LoginScreen.this, RegisterScreen.class);
			startActivity(intent);
			LoginScreen.this.finish();

			break;
		}
	}
}