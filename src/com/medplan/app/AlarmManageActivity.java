package com.medplan.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.medplan.db.databasehelper;

public class AlarmManageActivity extends Activity{
	Button prev, next, activate;
	TextView tvBox,tvAlarm;
	ImageView ivBox;
	String boxName = "Box 3", userSel,task;
	RelativeLayout rlCell,rlAlarm;
	TextView boxFirst,boxSecond;
	
	ImageView headLogo;
	TextView headerTitle;
	RelativeLayout titleHeadLayout;
	LinearLayout MainBgLayout;
	databasehelper db;
	public  SharedPreferences SP;
	String _username;
	int getCell_id,userid,loginid,boxid;
	Intent intent;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		db = new databasehelper(this);
		loginid= PreferenceManager.getDefaultSharedPreferences(this).getInt("UserID", 0);
		AlertDialog.Builder alert = new AlertDialog.Builder(
				AlarmManageActivity.this);
		alert.setTitle(R.string.confirm);
		alert.setMessage(R.string.connect_box_alarm_mgt);

		alert.setPositiveButton(R.string.ok_thanks,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					
						Intent intent = new Intent(AlarmManageActivity.this,
								UserManage_ListActivity.class);
						intent.putExtra("Task", "pick");
						startActivityForResult(intent,999);
					}
				});
		alert.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						
						AlarmManageActivity.this.finish();
					}
				});
		alert.show();
	
	}
		
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK)
		{
		userid=data.getIntExtra("idtoshow", 0);
		
		if(userid==0)
		{
			finish();
		}
		boxid=db.isActivated(loginid, userid);
		if(boxid==0)
		{
			Toast.makeText(getApplicationContext(),R.string.select_box_alarm_mgt, Toast.LENGTH_LONG).show();
			finish();
			
		}
		else
		{
			if(boxid==3){
				intent = new Intent(AlarmManageActivity.this,BoxThreeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				
			}else if(boxid==4){
				intent = new Intent(AlarmManageActivity.this,BoxFourActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			
				
			}else if(boxid==6){
				intent = new Intent(AlarmManageActivity.this,BoxSixActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				
				
			}else if(boxid==8){
				intent = new Intent(AlarmManageActivity.this,BoxEightActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

				
			}else if(boxid==10){
				intent = new Intent(AlarmManageActivity.this,BoxTenActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				
			}
			intent.putExtra("BoxID", boxid);
			intent.putExtra("UserID", userid);
			intent.putExtra("LoginID", loginid);
			startActivity(intent);
			finish();
		}
		Log.i("log",userid+"------------");
		}
	}
	
	}
	
