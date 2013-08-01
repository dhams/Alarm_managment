package com.medplan.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import javax.mail.Flags.Flag;

import org.xmlpull.v1.XmlPullParser;

import android.R.xml;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.DownloadListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.medpan.util.CellInfo_Model;
import com.medpan.util.Constant;
import com.medpan.util.GlobalMethods;
import com.medpan.util.Medicine_Model;
import com.medpan.util.Notification_Model;
import com.medpan.util.Picture_Model;
import com.medpan.util.SoundManager;
import com.medpan.util.User_Model;
import com.medplan.db.BgColorAdapter;
import com.medplan.db.SoundAdapter;
import com.medplan.db.databasehelper;

public class CellManage_AddShowActivity extends Activity implements
		OnClickListener  {
	public SharedPreferences SP;
	databasehelper db;
	TextView tvAText, tvMed, tvtime1, tvtime2, tvtime3, tvtime4, tvtime5,
			tvtime6, tvtime7, tvtime8, tvtime9, tvIdate ,  
			tvTitleTime1,tvTitleTime2,tvTitleTime3,tvTitleTime4,tvTitleTime5,tvTitleTime6,tvTitleTime7,tvTitleTime8,tvTitleTime9 ;
	RelativeLayout titleHeadLayout;
	LinearLayout MainBgLayout, addButton, showButton, lltime1, lltime2,
			lltime3, lltime4, lltime5, lltime6, lltime7, lltime8, lltime9;
	ImageView imgUser;
	EditText etDesc;
	
	Button btnAdd, btnCancel, btnDelete, btnBack, btnUpdate; 
	CheckBox cbMini, cbBlink, cbAlert, cbVibrant;
	Spinner spMed, spSound, spBuzzRepeat, spAlarm, spWayToStop,
			spConfirmMed, spScheduleInterval, 
			spDosageMgt, spHow_many, spWeek, spMonth ,spBG;
	
	MySpinner spIntervalDay  ;
	public  int  mPos;
	private  int spinnerCouner ;

	int picid, medid, intMed, intBg, intSound, intBuzz, intAlarm, intWay,
			intConfirm, intMany_Time, intInt_Time, intDayOf_Int = 0,
			intSch_Int, intInt_Day, intDos_Mgt, intHow_many, intWeek, intMonth,
			getCell_id, getCell_pos, intblAlert, intblBlink, intblMini,
			intblVibrant;

	int cellid, boxid, userid, loginid;
	String strDesc, mname, task, MedName;

	boolean blAlert, blBlink, blMini, blVibrant, flgShowAlert = false,
			fromFillisTheForm = false  , isFromMediList , saveStatchange;
	private int  mHour, mMinute, mLength, mDate, mMonth, mYear;
//	private int tmy, tmm, tmd ;
	private boolean mFlag = false;
	static final int TIME_DIALOG_ID = 0, DATE_DIALOG_ID = 1;
//	public static PendingIntent pendingIntent;
	AlertDialog alertMsg;
	AlertDialog.Builder alertDialog;

	BgColorAdapter adapter;
	SoundAdapter soundAdapter;
	String[] color_type;
	String[] num_type;
	String[] sound_type;
	int[] sound_image;
	ImageView headLogo;
	TextView headerTitle;
	AlertDialog.Builder builder;
	AlertDialog alert;
	SoundManager mSoundManager;
	ArrayList<Medicine_Model> medcineList ;
	

	final CharSequence[] items = { "Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday" };
	final boolean[] flags = { false, false, false, false, false, false, false };

	private ApplicationClass applicationClass ;
	
	private void updateDisplay() {
		switch (mPos) {
		case 1:
			tvtime1.setText(new StringBuilder().append(pad(mHour)).append(":")
					.append(pad(mMinute)));
			break;
		case 2:
			tvtime2.setText(new StringBuilder().append(pad(mHour)).append(":")
					.append(pad(mMinute)));
			break;
		case 3:
			tvtime3.setText(new StringBuilder().append(pad(mHour)).append(":")
					.append(pad(mMinute)));
			break;
		case 4:
			tvtime4.setText(new StringBuilder().append(pad(mHour)).append(":")
					.append(pad(mMinute)));
			break;
		case 5:
			tvtime5.setText(new StringBuilder().append(pad(mHour)).append(":")
					.append(pad(mMinute)));
			break;
		case 6:
			tvtime6.setText(new StringBuilder().append(pad(mHour)).append(":")
					.append(pad(mMinute)));
			break;
		case 7:
			tvtime7.setText(new StringBuilder().append(pad(mHour)).append(":")
					.append(pad(mMinute)));
			break;
		case 8:
			tvtime8.setText(new StringBuilder().append(pad(mHour)).append(":")
					.append(pad(mMinute)));
			break;
		case 9:
			tvtime9.setText(new StringBuilder().append(pad(mHour)).append(":")
					.append(pad(mMinute)));
				break;
			}
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else 
			return "0" + String.valueOf(c);
	}

	// DATE CONFIGURE
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			mDate = dayOfMonth;
			mMonth = monthOfYear;
			mYear = year;
			Calendar cal = Calendar.getInstance();
			int tYear = cal.get(Calendar.YEAR);
			int tMonth = cal.get(Calendar.MONTH);
			int tDate = cal.get(Calendar.DAY_OF_MONTH);
			if (mYear < tYear || (mMonth < tMonth && mYear <= tYear)
					|| (mDate < tDate && mMonth <= tMonth && mYear <= tYear)) {
				Toast.makeText(CellManage_AddShowActivity.this,
						"Select any upcoming date.", Toast.LENGTH_LONG).show();
			} else {
				mMonth++;
				tvIdate.setText("" + mDate + "/" + mMonth + "/" + mYear);
			}
 
			tv.requestFocus();
		}
	};

	// TIME CONFIGURE
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			view.setIs24HourView(true);
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
			removeDialog(TIME_DIALOG_ID);
		}
	};

	private void updateLayout() {
		if (mFlag == false) {
			lltime1.setVisibility(View.GONE);
			lltime2.setVisibility(View.GONE);
			lltime3.setVisibility(View.GONE);
			lltime4.setVisibility(View.GONE);
			lltime5.setVisibility(View.GONE);
			lltime6.setVisibility(View.GONE);
			lltime7.setVisibility(View.GONE);
			lltime8.setVisibility(View.GONE);
			lltime9.setVisibility(View.GONE);
		} else {
			lltime1.setVisibility(View.GONE);
			lltime2.setVisibility(View.GONE);
			lltime3.setVisibility(View.GONE);
			lltime4.setVisibility(View.GONE);
			lltime5.setVisibility(View.GONE);
			lltime6.setVisibility(View.GONE);
			lltime7.setVisibility(View.GONE);
			lltime8.setVisibility(View.GONE);
			lltime9.setVisibility(View.GONE);
			// float f1,f2,fans;
			// f1 = spHow_many.getSelectedItemPosition();
			// f2 = spDosageMgt.getSelectedItemPosition();
			// fans = (f1/(f2/2));
			// if(fans>0 && fans <1)
			// {
			// mLength =1;
			// }
			// else
			// {
			// mLength = (int) fans;
			// if(fans > (float)mLength)
			// mLength++;
			// }
			mLength = spHow_many.getSelectedItemPosition();
			for (int i = 1; i <= mLength; i++) {
				switch (i) {
				case 1:
					lltime1.setVisibility(View.VISIBLE);
					break;
				case 2:
					lltime2.setVisibility(View.VISIBLE);
					break;
				case 3:
					lltime3.setVisibility(View.VISIBLE);
					break;
				case 4:
					lltime4.setVisibility(View.VISIBLE);
					break;
				case 5:
					lltime5.setVisibility(View.VISIBLE);
					break;
				case 6:
					lltime6.setVisibility(View.VISIBLE);
					break;
				case 7:
					lltime7.setVisibility(View.VISIBLE);
					break;
				case 8:
					lltime8.setVisibility(View.VISIBLE);
					break;
				case 9:
					lltime9.setVisibility(View.VISIBLE);
					break;
				}
			}
		}
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub

		switch (id) {
		case TIME_DIALOG_ID:
			String time = "";
			Log.i("POS", "....... " + mPos);
			switch (mPos) {
			case 1:
				time = tvtime1.getText().toString();
				break;
			case 2:
				time = tvtime2.getText().toString();
				break;

			case 3:
				time = tvtime3.getText().toString();
				break;

			case 4:
				time = tvtime4.getText().toString();
				break;

			case 5:
				time = tvtime5.getText().toString();
				break;

			case 6:
				time = tvtime6.getText().toString();
				break;

			case 7:
				time = tvtime7.getText().toString();
				break;

			case 8:
				time = tvtime8.getText().toString();
				break;

			case 9:
				time = tvtime9.getText().toString();
				break;
			}

			Log.i("time", "...... " + time);
			if (time.equalsIgnoreCase("")) {
				Calendar cal1 = Calendar.getInstance();
				mHour = cal1.get(Calendar.HOUR_OF_DAY);
				mMinute = cal1.get(Calendar.MINUTE);
				((TimePickerDialog) dialog).updateTime(mHour, mMinute);
			} else {
				String arr[] = time.split(":");
				mHour = Integer.parseInt(arr[0]);
				mMinute = Integer.parseInt(arr[1]);
				((TimePickerDialog) dialog).updateTime(mHour, mMinute);
			}
		}

	}

	@Override
	public void onBackPressed() {
		
		mSoundManager.stopSound();
		super.onBackPressed();
	}
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			String time = "";
			Log.i("POS", "....... " + mPos);
			switch (mPos) {	
			case 1:
				time = tvtime1.getText().toString();
				break;
			case 2:
				time = tvtime2.getText().toString();
				break;

			case 3:
				time = tvtime3.getText().toString();
				break;

			case 4:
				time = tvtime4.getText().toString();
				break;

			case 5:
				time = tvtime5.getText().toString();
				break;

			case 6:
				time = tvtime6.getText().toString();
				break;

			case 7:
				time = tvtime7.getText().toString();
				break;

			case 8:
				time = tvtime8.getText().toString();
				break;

			case 9:
				time = tvtime9.getText().toString();
				break;

			}
			Log.i("time", "...... " + time);
			if (time.equalsIgnoreCase("")) {
				Calendar cal = Calendar.getInstance();
				mHour = cal.get(Calendar.HOUR_OF_DAY);
				mMinute = cal.get(Calendar.MINUTE);
				return new TimePickerDialog(this, mTimeSetListener, mHour,
						mMinute, true);
			} else {
				String arr[] = time.split(":");
				mHour = Integer.parseInt(arr[0]);
				mMinute = Integer.parseInt(arr[1]);
				return new TimePickerDialog(this, mTimeSetListener, mHour,
						mMinute, true); 
			}
		case DATE_DIALOG_ID:
			Calendar cal = Calendar.getInstance();
			mYear = cal.get(Calendar.YEAR);
			mMonth = cal.get(Calendar.MONTH);
			mDate = cal.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog dp = new DatePickerDialog(this, mDateSetListener,
					mYear, mMonth, mDate);
			dp.setTitle(getResources().getString(R.string.set_starting_date));
			return dp;
		}

		return null;
	}


	TextView tv;
	
	
	public static int SpBGPosition =0, spsoundPostion =0, spBuzzRepeatPostion=0 ,spAlarmPosition=0 , spWayToStopPostion=0, spConfirmMedPosition=0 ,
	spIntervalDayPostion=0  ,spDosageMgtPostion=0 , spHow_manyPostion=0 ;
	
	
	// ONCREATE
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.cellmanage_addshow);
		
		applicationClass = (ApplicationClass)this.getApplication() ;
		
		titleHeadLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		MainBgLayout = (LinearLayout) findViewById(R.id.mainCellLayout);
		try {
			SplashScreen.Cmethod.CheckAddShowScreen(Constant._StrCheck,
					titleHeadLayout, MainBgLayout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
//		spMed = (Spinner) findViewById(R.id.spMed);
//		spBG = (Spinner) findViewById(R.id.spBgColor);
//		spSound = (Spinner) findViewById(R.id.spSound);
//		spBuzzRepeat = (Spinner) findViewById(R.id.spBuzz_Repeat);
//		spAlarm = (Spinner) findViewById(R.id.sptimeInterval);// delete this
//		spWayToStop = (Spinner) findViewById(R.id.spwaytostop);
//		spConfirmMed = (Spinner) findViewById(R.id.spconfirm_medicine);
//		spIntervalDay = (MySpinner) findViewById(R.id.sptimeInterval);
//		spDosageMgt = (Spinner) findViewById(R.id.spdosageMesurement);
//		spHow_many = (Spinner) findViewById(R.id.sphow_many_day);
//		
//		if (saveStatchange){
//			spBG.setSelection(icicle.getInt(""+spBG.getId(), 0 )) ;
//			spSound.setSelection(icicle.getInt(""+spSound.getId() , 0)) ;
//			spBuzzRepeat.setSelection(icicle.getInt(""+spBuzzRepeat.getId(), 0)) ;
//			spWayToStop.setSelection(icicle.getInt(""+spWayToStop.getId(), 0)) ;
//			spIntervalDay.setSelection(icicle.getInt(""+spIntervalDay.getId(), 0)) ;
//			spDosageMgt.setSelection(icicle.getInt(""+spDosageMgt.getId(), 0)) ;
//			spHow_many.setSelection(icicle.getInt(""+spHow_many.getId(), 0)) ;
//			
//			Log.d("SAVED STATE RESTORE .............", ""+spBG.getId()) ;	
//		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		 
		db = new databasehelper(this);

		medcineList = db.getMedicals(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("UserID", -1)) ;
		
		tv = (TextView) findViewById(R.id.textDM);
		mSoundManager = new SoundManager();
		mSoundManager.initSounds(this);
		// mSoundManager.addSound(1, R.raw.sound);
		mSoundManager.addSound(1, R.raw.a);
		mSoundManager.addSound(2, R.raw.b);
		mSoundManager.addSound(3, R.raw.c);
		mSoundManager.addSound(4, R.raw.d);
		mSoundManager.addSound(5, R.raw.f);
		mSoundManager.addSound(6, R.raw.g);
		mSoundManager.addSound(7, R.raw.h);
		mSoundManager.addSound(8, R.raw.i);
		mSoundManager.addSound(9, R.raw.j);
		mSoundManager.addSound(10, R.raw.k);
		mSoundManager.addSound(11, R.raw.e);
		mSoundManager.addSound(12, R.raw.l);

		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.cell_mgt);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);

		headerTitle.setText(R.string.cell_mgt);
		String doWhat = this.getIntent().getStringExtra("Task");
		color_type = getResources().getStringArray(R.array.color_type);
		num_type = getResources().getStringArray(R.array.call_count_type);

		sound_type = getResources().getStringArray(R.array.sound_type);

		sound_image = new int[] {0, R.drawable.buzz, R.drawable.buzz,
				R.drawable.buzz, R.drawable.buzz, R.drawable.buzz,
				R.drawable.buzz, R.drawable.buzz, R.drawable.buzz,
				R.drawable.buzz, R.drawable.buzz, R.drawable.buzz,
				R.drawable.buzz };//delicious 

		tvIdate = (TextView) findViewById(R.id.tvIDate);
		tvIdate.setOnClickListener(this);

		alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Shot from Camera ");
		alertDialog.setMessage(R.string.shot_from_Camera);
		alertDialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}
				});

		alertDialog.setNegativeButton(R.string.no_thanks,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}
				});
		alertMsg = alertDialog.create();

		builder = new AlertDialog.Builder(CellManage_AddShowActivity.this);
		builder.setTitle("Select Days");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				showDialog(DATE_DIALOG_ID);
				Toast.makeText(CellManage_AddShowActivity.this,
						getResources().getString(R.string.set_date),
						Toast.LENGTH_SHORT).show();

			}
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});

		builder.setMultiChoiceItems(items, flags,
				new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton, boolean isChecked) {

						if (isChecked) {
							flags[whichButton] = true;

						} else {
							flags[whichButton] = false;
						}

					}
				}); 
		builder.create();

		// cell id@ A=0,B=1,C=3,D=4,E=5,F=6,G=7,H=8,I=9.

		Intent intent = getIntent();
		cellid = intent.getIntExtra("CellID", 0);
		boxid = intent.getIntExtra("BoxID", 0);
		userid = intent.getIntExtra("UserID", 0);
		loginid = intent.getIntExtra("LoginID", 0);
		task = intent.getStringExtra("Task");
		Log.i("", "CellID " + cellid + " boxid " + boxid + " userid " + userid
				+ " loginid" + loginid + " task " + task);

		SP = PreferenceManager
				.getDefaultSharedPreferences(CellManage_AddShowActivity.this);
		getCell_id = SP.getInt("Cell_id", 0);

		

		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(CellManage_AddShowActivity.this,
						ConfigureScreen.class);
				startActivity(intent);
			}
		});
 
		tvMed = (TextView) findViewById(R.id.tvMedicine);
		tvMed.setOnClickListener(this);
		addButton = (LinearLayout) findViewById(R.id.btnForAdd);
		showButton = (LinearLayout) findViewById(R.id.btnForShow);

		TextView tv = (TextView) findViewById(R.id.tv_userbox_name);
		TextView tvBoxname = (TextView) findViewById(R.id.tv_userbox_boxname);
  
//		db = new databasehelper(this);
		
		Log.i("", userid + "-----------" + boxid);
		User_Model arr = db.getPatients(String.valueOf(userid));
		switch (cellid) {
		case 0: 
			tv.setText("User : " + arr.name + " "
					+ arr.surname);
			tvBoxname.setText(" Box: " + boxid + " Cell: A/1");
			break;
		case 1:
			tv.setText("User : " + arr.name + " "
					+ arr.surname);
			tvBoxname.setText(" Box: " + boxid + " Cell: B/2");
			break;
		case 2:
			tv.setText("User : " + arr.name + " "
					+ arr.surname);
			tvBoxname.setText(" Box: " + boxid + " Cell: C/3");
			break;
		case 3:
			tv.setText("User : " + arr.name + " "
					+ arr.surname);
			tvBoxname.setText(" Box: " + boxid + " Cell: D/4");
			break;
		case 4:
			tv.setText("User : " + arr.name + " "
					+ arr.surname);
			tvBoxname.setText(" Box: " + boxid + " Cell: E/5");
			break;
		case 5:
			tv.setText("User : " + arr.name + " "
					+ arr.surname);
			tvBoxname.setText(" Box: " + boxid + " Cell: F/6");
			break;
		case 6:
			tv.setText("User : " + arr.name + " "
					+ arr.surname);
			tvBoxname.setText(" Box: " + boxid + " Cell: G/7");
			break;
		case 7:
			tv.setText("User : " + arr.name + " "
					+ arr.surname);
			tvBoxname.setText(" Box: " + boxid + " Cell: H/8");
			break;
		case 8:
			tv.setText("User : " + arr.name + " "
					+ arr.surname);
			tvBoxname.setText(" Box: " + boxid + " Cell: I/9");
			break;
		case 9:
			tv.setText("User : " + arr.name + " "
					+ arr.surname);
			tvBoxname.setText(" Box: " + boxid + " Cell: J/10");
			break;
		}

		
		// Editbox
		etDesc = (EditText) findViewById(R.id.etDc);
		etDesc.setOnClickListener(this);

		// User ImageView
		imgUser = (ImageView) findViewById(R.id.imageUser);
		imgUser.setOnClickListener(this);

		// CheckBoxs
		cbAlert = (CheckBox) findViewById(R.id.cbAlert);
		cbBlink = (CheckBox) findViewById(R.id.cbBlink);
		cbMini = (CheckBox) findViewById(R.id.cbMiniImage);
		cbVibrant = (CheckBox) findViewById(R.id.cbVibrant);
		// cbVibrant.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// // TODO Auto-generated method stub
		// if(isChecked)
		// Toast.makeText(CellManage_AddShowActivity.this,
		// "Enableing Vibrate, will stop alarm and text to speech sounds.",
		// Toast.LENGTH_LONG).show();
		// }
		// });

		// buttons
		btnAdd = (Button) findViewById(R.id.btnAddForm);
		btnAdd.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btnCanForm);
		btnCancel.setOnClickListener(this);
		btnBack = (Button) findViewById(R.id.btnBkForm);
		btnBack.setOnClickListener(this);
		btnDelete = (Button) findViewById(R.id.btnDelForm);
		btnDelete.setOnClickListener(this);
		btnUpdate = (Button) findViewById(R.id.btnUpForm);
		btnUpdate.setOnClickListener(this);

		// Spinners
		spMed = (Spinner) findViewById(R.id.spMed);
		spBG = (MySpinner) findViewById(R.id.spBgColor);
		spSound = (Spinner) findViewById(R.id.spSound);
		// spSound.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Toast.makeText(CellManage_AddShowActivity.this,
		// "Click on icons to play the sound and then click on row to choose it",
		// Toast.LENGTH_LONG).show();
		// }
		// });

		spBuzzRepeat = (Spinner) findViewById(R.id.spBuzz_Repeat);
		
//		XmlPullParser xmlPullParser = getResources().getXml(R.id.sptimeInterval) ;
//		AttributeSet attributeSet =  Xml.asAttributeSet(xmlPullParser) ;
//		spAlarm = new Spinner(getApplicationContext()	, attributeSet) ;
		
		spAlarm = (Spinner) findViewById(R.id.sptimeInterval);// delete this
		spWayToStop = (Spinner) findViewById(R.id.spwaytostop);
		spConfirmMed = (Spinner) findViewById(R.id.spconfirm_medicine);
//		spManyTime = (Spinner) findViewById(R.id.sptimeInterval);// delete this
		spIntervalDay = (MySpinner) findViewById(R.id.sptimeInterval);

		// this
		spDosageMgt = (Spinner) findViewById(R.id.spdosageMesurement);
		spDosageMgt.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (spHow_many.getSelectedItemPosition() != 0
						&& spDosageMgt.getSelectedItemPosition() != 0) {
					mFlag = true;
					updateLayout();
				} else {
					mFlag = false;
					updateLayout();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		spHow_many = (Spinner) findViewById(R.id.sphow_many_day);
		spHow_many.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (spHow_many.getSelectedItemPosition() != 0
						&& spDosageMgt.getSelectedItemPosition() != 0) {
					mFlag = true;
					updateLayout();
				} else {
					mFlag = false;
					updateLayout();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		 
        Drawable[] arr_phone_icon = { null,getResources().getDrawable(R.drawable.cell1),
				 getResources().getDrawable(R.drawable.cell2),getResources().getDrawable (R.drawable.cell4),
				 getResources().getDrawable(R.drawable.cell3),getResources().getDrawable( R.drawable.cell5)};
		 
		adapter = new BgColorAdapter(CellManage_AddShowActivity.this,
				R.layout.spinner_row, num_type, color_type,
				arr_phone_icon);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spBG.setAdapter(adapter);

//		spBG.setOnItemSelectedEvenIfUnchangedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//
//				if (spBG.getSelectedItemPosition() == 0) {
//
//				} else {
//					TextView text = (TextView) findViewById(R.id.text_number);
//					ImageView image = (ImageView) findViewById(R.id.image);
//					image.setVisibility(View.GONE);
//					text.setVisibility(View.GONE);
//				}
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				
//			}
//		}) ;
		
		spBG.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (spBG.getSelectedItemPosition() == 0) {

				} else {
					TextView text = (TextView) findViewById(R.id.text_number);
					ImageView image = (ImageView) findViewById(R.id.image);
					image.setVisibility(View.GONE);
					text.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		soundAdapter = new SoundAdapter(CellManage_AddShowActivity.this,
				R.layout.sound_row, sound_type, sound_image, mSoundManager);
		soundAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spSound.setAdapter(soundAdapter);

		
		spSound.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				if (spSound.getSelectedItemPosition() == 0) {

				} else {
					ImageView image = (ImageView) findViewById(R.id.image_sound);
					image.setVisibility(View.GONE);
				}
				mSoundManager.stopSound();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				mSoundManager.stopSound();
			}
			
		});

//		
//		spIntervalDay.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if (spIntervalDay.getSelectedItemPosition()==4)
//					spIntervalDay.setSelection(4);
//			}
//		});
		
		 
		spIntervalDay.setOnItemSelectedEvenIfUnchangedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				

					Log.d("SpinnerSelection", "postion="+arg2+" fromFilterfrom="+fromFillisTheForm) ;
					
					spinnerCouner++ ;
					
					if (fromFillisTheForm && spinnerCouner<=5) {
						if (spinnerCouner==6)
						fromFillisTheForm = false;
						return;
					}
					flgShowAlert = true;
					if (arg2 == 0) {
						
					} else if (arg2 == 4 && flgShowAlert == true) {
						builder.show();

					} else if ((arg2 == 1 || arg2 == 2 || arg2 == 3)
							&& flgShowAlert == true) {
						showDialog(DATE_DIALOG_ID);
						Toast.makeText(CellManage_AddShowActivity.this,
								getResources().getString(R.string.set_date),
								Toast.LENGTH_SHORT).show();
					}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				if (spIntervalDay.getSelectedItemPosition() == 4
						&& flgShowAlert == true)
					builder.show();
			}

		}) ; 
		
//		spIntervalDay.setOnItemSelectedListener(new OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				if (fromFillisTheForm) {
//					fromFillisTheForm = false;
//					return;
//				}
//				flgShowAlert = true;
//				if (arg2 == 0) {
//					// do nothing
//				} else if (arg2 == 4 && flgShowAlert == true) {
//					builder.show();
//
//				} else if ((arg2 == 1 || arg2 == 2 || arg2 == 3)
//						&& flgShowAlert == true) {
//					showDialog(DATE_DIALOG_ID);
//					Toast.makeText(CellManage_AddShowActivity.this,
//							getResources().getString(R.string.set_date),
//							Toast.LENGTH_SHORT).show();
//				}
//
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//				if (spIntervalDay.getSelectedItemPosition() == 4
//						&& flgShowAlert == true)
//					builder.show();
//			}
//
//		});

		// this
		spScheduleInterval = (Spinner) findViewById(R.id.sptimeInterval);// delete
		// this
		spWeek = (Spinner) findViewById(R.id.sptimeInterval);// delete this
		spMonth = (Spinner) findViewById(R.id.sptimeInterval);// delete this
  
		// textviews
		tvtime1 = (TextView) findViewById(R.id.tvTime1);
		tvTitleTime1 = (TextView)findViewById(R.id.textviewTitle1)  ;
		tvtime1.setOnClickListener(this);
		tvtime2 = (TextView) findViewById(R.id.tvTime2);
		tvTitleTime2 = (TextView)findViewById(R.id.textviewTitle2)  ;
		tvtime2.setOnClickListener(this);
		tvtime3 = (TextView) findViewById(R.id.tvTime3);
		tvTitleTime3 = (TextView)findViewById(R.id.textviewTitle3)  ;
		tvtime3.setOnClickListener(this);
		tvtime4 = (TextView) findViewById(R.id.tvTime4);
		tvTitleTime4 = (TextView)findViewById(R.id.textviewTitle4)  ;
		tvtime4.setOnClickListener(this);
		tvtime5 = (TextView) findViewById(R.id.tvTime5);
		tvTitleTime5 = (TextView)findViewById(R.id.textviewTitle5)  ;
		tvtime5.setOnClickListener(this);
		tvtime6 = (TextView) findViewById(R.id.tvTime6);
		tvTitleTime6 = (TextView)findViewById(R.id.textviewTitle6)  ;
		tvtime6.setOnClickListener(this);
		tvtime7 = (TextView) findViewById(R.id.tvTime7);
		tvTitleTime7 = (TextView)findViewById(R.id.textviewTitle7)  ;
		tvtime7.setOnClickListener(this);
		tvtime8 = (TextView) findViewById(R.id.tvTime8);
		tvTitleTime8 = (TextView)findViewById(R.id.textviewTitle8)  ;
		tvtime8.setOnClickListener(this);
		tvtime9 = (TextView) findViewById(R.id.tvTime9);
		tvTitleTime9 = (TextView)findViewById(R.id.textviewTitle9)  ;
		tvtime9.setOnClickListener(this);

		// LinearLayout
		lltime1 = (LinearLayout) findViewById(R.id.LLTiming1);
		lltime1.setVisibility(View.GONE);
		lltime2 = (LinearLayout) findViewById(R.id.LLTiming2);
		lltime2.setVisibility(View.GONE);
		lltime3 = (LinearLayout) findViewById(R.id.LLTiming3);
		lltime3.setVisibility(View.GONE);
		lltime4 = (LinearLayout) findViewById(R.id.LLTiming4);
		lltime4.setVisibility(View.GONE);
		lltime5 = (LinearLayout) findViewById(R.id.LLTiming5);
		lltime5.setVisibility(View.GONE);
		lltime6 = (LinearLayout) findViewById(R.id.LLTiming6);
		lltime6.setVisibility(View.GONE);
		lltime7 = (LinearLayout) findViewById(R.id.LLTiming7);
		lltime7.setVisibility(View.GONE);
		lltime8 = (LinearLayout) findViewById(R.id.LLTiming8);
		lltime8.setVisibility(View.GONE);
		lltime9 = (LinearLayout) findViewById(R.id.LLTiming9);
		lltime9.setVisibility(View.GONE);

		if (task.equalsIgnoreCase("ADD")) {
			addButton.setVisibility(View.VISIBLE);
			showButton.setVisibility(View.GONE);
		} else {

			showButton.setVisibility(View.VISIBLE);
			addButton.setVisibility(View.GONE);
			fillIsTheForm();
		}
		
		
	}
	
//	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		
//		switch (keyCode) {
//		case KeyEvent.KEYCODE_BACK:
//			
//			return true ; 
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	private boolean isValidateDate(){
		boolean flage  =true ;
		
		Calendar calendar = Calendar.getInstance();
		String time = "";
		mLength = spHow_many.getSelectedItemPosition();
		
		if (mLength==0){
			Toast.makeText(CellManage_AddShowActivity.this,
					getResources().getString(R.string.set_alarm_time),
					Toast.LENGTH_SHORT).show();
			flage=false ;
			return flage;
		}
		
		for (int i = 1; i <= mLength; i++) {
			switch (i) {
			case 1:
				time = tvtime1.getText().toString();

				break;
			case 2:
				time = tvtime2.getText().toString();
				break;
			case 3:
				time = tvtime3.getText().toString();
				break;
			case 4:
				time = tvtime4.getText().toString();
				break;
			case 5:
				time = tvtime5.getText().toString();
				break;
			case 6:
				time = tvtime6.getText().toString();
				break;
			case 7:
				time = tvtime7.getText().toString();
				break;
			case 8:
				time = tvtime8.getText().toString();
				break;
			case 9:
				time = tvtime9.getText().toString();
				break;
			}

			if (time.equalsIgnoreCase("")) {
				Toast.makeText(CellManage_AddShowActivity.this,
						getResources().getString(R.string.set_alarm_time),
						Toast.LENGTH_SHORT).show();
				flage= false ;
				break  ;
			}

				 
			time = time+":"+calendar.get(Calendar.SECOND)  ;
        	String timeArry[] = time.split(":") ;
        	String dateArry [] =  tvIdate.getText().toString().split("/") ;
        	
        	int year  = Integer.parseInt(dateArry[2]) ;
        	int month  = Integer.parseInt(dateArry[1])-1 ;
            int date  = Integer.parseInt(dateArry[0]) ; 
            
            int hour   = Integer.parseInt(timeArry[0]) ;
            int minute  =Integer.parseInt(timeArry[1]) ;
	 
            	calendar.set(Calendar.YEAR, year) ;
            	calendar.set(Calendar.MONTH, month) ;
            	calendar.set(Calendar.DATE, date) ;
            	
            	calendar.set(Calendar.HOUR_OF_DAY, hour) ;
            	calendar.set(Calendar.MINUTE,minute) ;
            	calendar.set(Calendar.SECOND,0) ;
            	
            	  Calendar calendar2 = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		            if (!(calendar.getTimeInMillis()> calendar2.getTimeInMillis()))
		            	
		            {
		            	Toast.makeText(CellManage_AddShowActivity.this,
								"Select  upcoming time ", Toast.LENGTH_LONG).show();
		            	flage=false ;
		            	break  ;
		            }
		            if (flage==false)
		            	break  ;
		}
		return flage;
	}

	public void fillIsTheForm() {
		try{
		ArrayList<CellInfo_Model> arr = db.getCellInfoForBox(loginid, userid,
				boxid, cellid);
		CellInfo_Model cell = arr.get(0);
		
		ArrayList<Medicine_Model> arrMed = db.getMedical(cell.medid);
		
		if (isFromMediList==false){
			medid = cell.medid;
			tvMed.setText(arrMed.get(0).nm);
			picid = arrMed.get(0).mpicid;
			
			Picture_Model picmodel = db.getPicture(arrMed.get(0).mpicid);
			// Bitmap bitmap = BitmapFactory.decodeFile(picmodel.path);
			Bitmap bitmap = GlobalMethods.decodeFile(picmodel.path);
			if (bitmap == null) {
				imgUser.setBackgroundResource(R.drawable.add_photo);
			} else {
				imgUser.setImageBitmap(bitmap);
				// imgUser.setImageBitmap(bitmap);
			}
		}

		fromFillisTheForm = true;
		tvIdate.setText(cell.startdate);
		
//		String dateArry [] =  cell.startdate.split("/") ;
//		mDate  =Integer.parseInt(dateArry[0]);
//		mMonth  =Integer.parseInt(dateArry[1]);
//		mYear  =Integer.parseInt(dateArry[2]);
		
//		Date dt;
//		try {
//			dt = new Date(cell.startdate);
//		} catch (Exception e) {
//			dt = new Date();
//			e.printStackTrace();
//		}
//		tmm = dt.getMonth();
//		tmy = dt.getYear() + 1900;
//		tmd = dt.getDate();
		
		Log.i("FILL IS The form", mDate + " " + mMonth + " " + mYear);
		
		spBG.setSelection(cell.intBg);
		etDesc.setText(cell.strDesc);
		spSound.setSelection(cell.intSound);
		spBuzzRepeat.setSelection(cell.intBuzz);
		if (cell.blAlert == 1)
			cbAlert.setChecked(true);
		if (cell.blBlink == 1)
			cbBlink.setChecked(true);
		if (cell.blMini == 1)
			cbMini.setChecked(true);
		if (cell.blVibrant == 1)
			cbVibrant.setChecked(true);
		spAlarm.setSelection(cell.intAlarm);
		spWayToStop.setSelection(cell.intWay);
		spConfirmMed.setSelection(cell.intConfirm);
		// spIntervalTime.setSelection(cell.)
		if (cell.intInt_Day == 4) {
			flgShowAlert = false;
		}
		spIntervalDay.setSelection(cell.intInt_Day);

		spDosageMgt.setSelection(cell.intDos_Mgt);
		spHow_many.setSelection(cell.intMany_Time);
		// spManyTime.setSelection(cell.)

		spScheduleInterval.setSelection(cell.intSch_Int);
		spWeek.setSelection(cell.intWeek);
		spMonth.setSelection(cell.intMonth);

		if (spIntervalDay.getSelectedItemPosition() == 4) {
			String arrW[] = cell.weekdaystring.split("MJ");
			for (int k = 0; k < arrW.length; k++) {
				if (arrW[k].equalsIgnoreCase("1"))
					flags[k] = true;
				else
					flags[k] = false;
			}

		}

		strDesc = etDesc.getText().toString();
		MedName = tvMed.getText().toString();
		blAlert = cbAlert.isChecked();
		intblAlert = (blAlert == true) ? 1 : 0;

		blBlink = cbBlink.isChecked();
		intblBlink = (blBlink == true) ? 1 : 0;

		blMini = cbMini.isChecked();
		intblMini = (blMini == true) ? 1 : 0;

		blVibrant = cbVibrant.isChecked();
		intblVibrant = (blVibrant == true) ? 1 : 0;

		intMed = spMed.getSelectedItemPosition();
		intBg = spBG.getSelectedItemPosition();
		intSound = spSound.getSelectedItemPosition();

		intBuzz = spBuzzRepeat.getSelectedItemPosition();
		intAlarm = spAlarm.getSelectedItemPosition();
		intWay = spWayToStop.getSelectedItemPosition();
		intConfirm = spConfirmMed.getSelectedItemPosition();
		intMany_Time = spHow_many.getSelectedItemPosition();
		intInt_Time = spIntervalDay.getSelectedItemPosition();

		intSch_Int = spScheduleInterval.getSelectedItemPosition();
		intInt_Day = spIntervalDay.getSelectedItemPosition();
		intDos_Mgt = spDosageMgt.getSelectedItemPosition();
		intHow_many = spHow_many.getSelectedItemPosition();
		intWeek = spWeek.getSelectedItemPosition();
		intMonth = spMonth.getSelectedItemPosition();

		ArrayList<Notification_Model> arr2 = db.getCellNotification(loginid,
				userid, boxid, cellid);
		Log.i("Length", "-------- length = " + arr2.size());

		String time = getResources().getString(R.string.time);
		for (int i = 0; i < arr2.size(); i++) {
			Log.i("Hello", "----------- " + arr2.get(i).strTime);
			switch (i) {
			case 0:
				tvtime1.setText(arr2.get(i).strTime);
				tvTitleTime1.setText(time+" 1");
				break;
			case 1:	
				tvtime2.setText(arr2.get(i).strTime);
				tvTitleTime2.setText(time+" 2");
				break;
			case 2:
				tvtime3.setText(arr2.get(i).strTime);
				tvTitleTime4.setText(time+" 3");
				break;

			case 3:
				tvtime4.setText(arr2.get(i).strTime);
				tvTitleTime4.setText(time+" 4");
				break;
			case 4:
				tvtime5.setText(arr2.get(i).strTime);
				tvTitleTime5.setText(time+" 5");
				break;
			case 5:
				tvtime6.setText(arr2.get(i).strTime);
				tvTitleTime6.setText(time+" 6");
				break;
			case 6: 
				tvtime7.setText(arr2.get(i).strTime);
				tvTitleTime7.setText(time+" 7");
				break;
			case 7:
				tvtime8.setText(arr2.get(i).strTime);
				tvTitleTime8.setText(time+" 8");
				break;
			case 8:
				tvtime9.setText(arr2.get(i).strTime);
				tvTitleTime9.setText(time+" 9");
				break;
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

//I  am planning to go out of town on (Wednesday)17th July  2013  ,
//kindly  approve this my casual leave .

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		  saveStatchange = true ; 
		   
	     SP.edit().putInt(""+spBG.getId(), spBG.getSelectedItemPosition()).commit() ;
	     SP.edit().putInt(""+spSound.getId(), spSound.getSelectedItemPosition()).commit()  ;
	     SP.edit().putInt(""+spBuzzRepeat.getId(), spBuzzRepeat.getSelectedItemPosition()).commit()  ;
	     SP.edit().putInt(""+spWayToStop.getId(), spWayToStop.getSelectedItemPosition()).commit()  ;
	     SP.edit().putInt(""+spIntervalDay.getId(), spIntervalDay.getSelectedItemPosition()).commit()  ;
	     SP.edit().putInt(""+spDosageMgt.getId(), spDosageMgt.getSelectedItemPosition()).commit()  ;
	     SP.edit().putInt(""+spHow_many.getId(), spHow_many.getSelectedItemPosition()).commit()  ;
	     
//	     spsoundPostion =  spSound.getSelectedItemPosition();
//	     spBuzzRepeatPostion =  spBuzzRepeat.getSelectedItemPosition();
//	     spWayToStopPostion = spWayToStop.getSelectedItemPosition() ;
//	     spIntervalDayPostion =  spIntervalDay.getSelectedItemPosition();
//	     spDosageMgtPostion = spDosageMgt.getSelectedItemPosition() ;
//	     spHow_manyPostion =  spHow_many.getSelectedItemPosition() ;
	      
	      Log.d("SAVED STATE  .............", ""+spBG.getId()) ;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		spBG.setSelection(savedInstanceState.getInt(""+spBG.getId())) ;
		spSound.setSelection(savedInstanceState.getInt(""+spSound.getId())) ;
		spBuzzRepeat.setSelection(savedInstanceState.getInt(""+spBuzzRepeat.getId())) ;
		spWayToStop.setSelection(savedInstanceState.getInt(""+spWayToStop.getId())) ;
		spIntervalDay.setSelection(savedInstanceState.getInt(""+spIntervalDay.getId())) ;
		spDosageMgt.setSelection(savedInstanceState.getInt(""+spDosageMgt.getId())) ;
		spHow_many.setSelection(savedInstanceState.getInt(""+spHow_many.getId())) ;
		
		Log.d("SAVED STATE RESTORE .............", ""+spBG.getId()) ;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1111) {
			picid = data.getIntExtra("pid", 0);
			Picture_Model picmodel = db.getPicture(picid);
			// Bitmap bitmap = BitmapFactory.decodeFile(picmodel.path);
			Bitmap bitmap = GlobalMethods.decodeFile(picmodel.path);
			if (bitmap == null) {
				imgUser.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.add_photo));
			} else {
				imgUser.setImageBitmap(bitmap);
				// imgUser.setImageBitmap(bitmap);
			}
		} else {
			try{
//				Drawable[] arr_phone_icon = { null,getResources().getDrawable(R.drawable.cell1),
//						 getResources().getDrawable(R.drawable.cell2),getResources().getDrawable (R.drawable.cell4),
//						 getResources().getDrawable(R.drawable.cell3),getResources().getDrawable( R.drawable.cell5)};
//				 
//				adapter = new BgColorAdapter(CellManage_AddShowActivity.this,
//						R.layout.spinner_row, num_type, color_type,
//						arr_phone_icon);
//				adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//				spBG.setAdapter(adapter);
				spBG.setSelection(SP.getInt(""+spBG.getId(), 1)) ;
//				spSound.setSelection(SP.getInt(""+spSound.getId(), 0)) ;
//				spBuzzRepeat.setSelection(SP.getInt(""+spBuzzRepeat.getId(), 0)) ;
//				spWayToStop.setSelection(SP.getInt(""+spWayToStop.getId(), 0)) ;
//				spIntervalDay.setSelection(SP.getInt(""+spIntervalDay.getId(), 0)) ;
//				spDosageMgt.setSelection(SP.getInt(""+spDosageMgt.getId(), 0)) ;
//				spHow_many.setSelection(SP.getInt(""+spHow_many.getId(), 0)) ;
				
		    isFromMediList = true ;
			picid = data.getIntExtra("picid", 0);
			mname = data.getStringExtra("mname");
			medid = data.getIntExtra("mid", 0);
			tvMed.setText(mname);
			
			Picture_Model picmodel = db.getPicture(picid);
			// Bitmap bitmap = BitmapFactory.decodeFile(picmodel.path);
			Bitmap bitmap = GlobalMethods.decodeFile(picmodel.path);
			if (bitmap == null) {
				imgUser.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.add_photo));
			} else {
				imgUser.setImageBitmap(bitmap);
				// imgUser.setImageBitmap(bitmap);
			}}catch (Exception e) {
			}
		}
	}

	public void onClick(View v) {
		long repeat = 0	;
		String repeatString  ="" ;
		
		String startdate = tvIdate.getText().toString();
		String weekdaystring = "";
		if (spIntervalDay.getSelectedItemPosition() == 4)
			for (int k = 0; k < flags.length; k++) {
				if (flags[k] == true)
					weekdaystring = weekdaystring + 1;
				else
					weekdaystring = weekdaystring + 0;

				if (k != flags.length - 1)
					weekdaystring = weekdaystring + "MJ";
			}

		if (spIntervalDay.getSelectedItemPosition() == 1) {
			repeat = AlarmManager.INTERVAL_DAY;
			repeatString = "daily" ;
		} else if (spIntervalDay.getSelectedItemPosition() == 2) {
			repeat = AlarmManager.INTERVAL_DAY * 7;
			repeatString = "weekly" ;
		} else if (spIntervalDay.getSelectedItemPosition() == 3) {
			repeat = AlarmManager.INTERVAL_DAY * 30;
			repeatString = "monthly" ;
		} else if (spIntervalDay.getSelectedItemPosition() == 4) {
			repeat = AlarmManager.INTERVAL_DAY * 7;
			repeatString = "weekDays" ;
		} else {
			repeat = AlarmManager.INTERVAL_DAY;
			repeatString = "daily" ;
		}
		strDesc = etDesc.getText().toString();
		MedName = tvMed.getText().toString();
		blAlert = cbAlert.isChecked();
		intblAlert = (blAlert == true) ? 1 : 0;

		blBlink = cbBlink.isChecked();
		intblBlink = (blBlink == true) ? 1 : 0;

		blMini = cbMini.isChecked();
		intblMini = (blMini == true) ? 1 : 0;

		blVibrant = cbVibrant.isChecked();
		intblVibrant = (blVibrant == true) ? 1 : 0;

		intMed = spMed.getSelectedItemPosition();
		intBg = spBG.getSelectedItemPosition();
		intSound = spSound.getSelectedItemPosition();

		intBuzz = spBuzzRepeat.getSelectedItemPosition();
		intAlarm = spAlarm.getSelectedItemPosition();
		intWay = spWayToStop.getSelectedItemPosition();
		intConfirm = spConfirmMed.getSelectedItemPosition();
		intMany_Time = spHow_many.getSelectedItemPosition();
		intInt_Time = spIntervalDay.getSelectedItemPosition();

		intSch_Int = spScheduleInterval.getSelectedItemPosition();
		intInt_Day = spIntervalDay.getSelectedItemPosition();
		intDos_Mgt = spDosageMgt.getSelectedItemPosition();
		intHow_many = spHow_many.getSelectedItemPosition();
		intWeek = spWeek.getSelectedItemPosition();
		intMonth = spMonth.getSelectedItemPosition();

		mSoundManager.stopSound() ;
		switch (v.getId()) {
		case R.id.tvIDate:
			tv.requestFocus();
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.tvMedicine:
			
//			Intent intent2 = new Intent(CellManage_AddShowActivity.this,
//					MediManage_ListActivity.class);
//			intent2.putExtra("From", "Other");
//			intent2.putExtra("Task", "list");
//			intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivityForResult(intent2, 1212);	
			
			AlertDialog.Builder  builder  = new AlertDialog.Builder(CellManage_AddShowActivity.this);
			builder.setTitle(R.string.medicine) ;
			
			builder.setAdapter(new EfficientAdapter(CellManage_AddShowActivity.this, medcineList), new DialogInterface.OnClickListener() {
				
				@Override 
				public void onClick(DialogInterface dialog, int which) {
					
					
					MedName = medcineList.get(which).nm;
					medid = medcineList.get(which).mid;
					picid = medcineList.get(which).mpicid ;
					
					tvMed.setText(MedName);
					
					Picture_Model picmodel = db.getPicture(picid);
					// Bitmap bitmap = BitmapFactory.decodeFile(picmodel.path);
					Bitmap bitmap = GlobalMethods.decodeFile(picmodel.path);
					if (bitmap == null) {
						imgUser.setImageBitmap(BitmapFactory.decodeResource(
								getResources(), R.drawable.add_photo));
					} else {
						imgUser.setImageBitmap(bitmap);
					}
				}
			});
			
			builder.show() ;
			
			break;
		case R.id.imageUser:
			Intent intent = new Intent(CellManage_AddShowActivity.this,
					PicManage_ListActivity.class);
			intent.putExtra("From", "Other");
			startActivityForResult(intent, 1111);

			break;
		case R.id.btnAddForm:
			if (tvMed.getText().toString().equalsIgnoreCase("")|| tvIdate.getText().toString().equalsIgnoreCase("")) {         
				Toast.makeText(CellManage_AddShowActivity.this,
						getResources().getString(R.string.enter_med_mgt),
						Toast.LENGTH_SHORT).show();
				return;
			
//			{"username":"john@doe.com","password":"strong!password" ,"realm":"eagleeyenetworks","callback":"callbackfunc" }

			} 
			
			
			//TODO Need to uncomment it 
			
			if (spBG.getSelectedItemPosition()==0 || spSound.getSelectedItemPosition()==0||spDosageMgt.getSelectedItemPosition()==0){
				Toast.makeText(CellManage_AddShowActivity.this,
						"Please enter required fields ",
						Toast.LENGTH_SHORT).show(); 
				return ; 
			}
			
			
			Log.i("User Id : Cell Add ", "" + loginid);
			Intent myIntent = null;
			

			for (int i = 1; i <= mLength; i++) {
				String time = "";
				switch (i) {
				case 1:
					time = tvtime1.getText().toString();

					break;
				case 2:
					time = tvtime2.getText().toString();
					break;
				case 3:
					time = tvtime3.getText().toString();
					break;
				case 4:
					time = tvtime4.getText().toString();
					break;
				case 5:
					time = tvtime5.getText().toString();
					break;
				case 6:
					time = tvtime6.getText().toString();
					break;
				case 7:
					time = tvtime7.getText().toString();
					break;
				case 8:
					time = tvtime8.getText().toString();
					break;
				case 9:
					time = tvtime9.getText().toString();
					break; 
				} 
				if (time.equalsIgnoreCase("")) {
					Toast.makeText(CellManage_AddShowActivity.this,
							getResources().getString(R.string.set_alarm_time),
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			Constant.flag = true;



   
		
			Log.i("",
					"userid, boxid, cellid, medid, picid,loginid,WayToStop,Med,Description,Sound == "
							+ userid
							+ " "
							+ boxid
							+ " "
							+ cellid
							+ " "
							+ medid
							+ " "
							+ picid
							+ " "
							+ " "
							+ loginid
							+ intWay
							+ " "
							+ MedName + " " + strDesc + " " + intSound);

//			ArrayList<Notification_Model> arr = db.getCellNotification();
			 
			
 			
			
//			if (arr.size() > 0)
//				totalCounter = arr.size() - 1;
//			else	
//				totalCounter = 0;

//			Log.d("Pending intent counter", "----- " + applicationClass.totalCounter);
			// totalCounter=10;
			
			switch (boxid) {
			case 3: 
				myIntent = new Intent(CellManage_AddShowActivity.this,
						BoxThreeActivity.class);
				break;
			case 4:
				myIntent = new Intent(CellManage_AddShowActivity.this,
						BoxFourActivity.class);
				break;

			case 6:
				myIntent = new Intent(CellManage_AddShowActivity.this,
						BoxSixActivity.class);
				break;

			case 8:
				myIntent = new Intent(CellManage_AddShowActivity.this,
						BoxEightActivity.class);
				break;

			case 10:
				myIntent = new Intent(CellManage_AddShowActivity.this,
						BoxTenActivity.class);
				break;

			}
			
			myIntent.putExtra("BoxID", boxid);
			myIntent.putExtra("UserID", userid);
			myIntent.putExtra("LoginID", loginid);
			myIntent.putExtra("CellID", cellid);
			myIntent.putExtra("WayToStop", intWay);
			myIntent.putExtra("Med", MedName);
			myIntent.putExtra("Description", strDesc);
			myIntent.putExtra("Sound", intSound-1);
			myIntent.putExtra("FromWhereActivity","Heaven" ); 
			
			int counter   ;
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			for (int i = 1; i <= mLength; i++) {
				
				
				
//				myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				myIntent.setFlags(Intent.FLAG_FROM_BACKGROUND) ;
				
				String time = "";
				switch (i) {
				case 1:
					time = tvtime1.getText().toString();

					break;
				case 2:
					time = tvtime2.getText().toString();
					break;
				case 3:
					time = tvtime3.getText().toString();
					break;
				case 4:
					time = tvtime4.getText().toString();
					break;
				case 5:
					time = tvtime5.getText().toString();
					break;
				case 6:
					time = tvtime6.getText().toString();
					break;
				case 7:
					time = tvtime7.getText().toString();
					break;
				case 8:
					time = tvtime8.getText().toString();
					break;
				case 9:
					time = tvtime9.getText().toString();
					break;
				}


				
//				myIntent.setData(Uri.parse("custom://" + counter));
//			    myIntent.setAction(String.valueOf(counter));
				 
				Calendar calendar = Calendar.getInstance();
			    Calendar calendar2 = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
				 
			    time = time+":"+calendar2.get(Calendar.SECOND)  ;
            	String timeArry[] = time.split(":") ;
            	
            	calendar.set(Calendar.YEAR, mYear) ;
            	calendar.set(Calendar.MONTH,mMonth-1) ;
            	calendar.set(Calendar.DATE, mDate) ;
            	
            	calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArry[0])) ;
            	calendar.set(Calendar.MINUTE, Integer.parseInt(timeArry[1])) ;
            	calendar.set(Calendar.SECOND,Integer.parseInt(timeArry[2])) ;
				 
	            if (!(calendar.getTimeInMillis()> calendar2.getTimeInMillis()))
	            	
	            {
	            	Toast.makeText(CellManage_AddShowActivity.this,
							"Select  upcoming time ", Toast.LENGTH_LONG).show();
	            	
	            	return  ;
	            }
	            
				PendingIntent pendingIntent ;
				
				if (spIntervalDay.getSelectedItemPosition() != 4){
					
//                    counter++ ;
					
//					myIntent.putExtra("unique", counter+"") ;  //Set counter as unique value 
					
				    if (repeatString.equalsIgnoreCase("daily")){
		            	
		            	repeat = AlarmManager.INTERVAL_DAY;
		            	
			            if (!(calendar.getTimeInMillis()> calendar2.getTimeInMillis())){
			            	
				            calendar.clear() ;
			                calendar.set(Calendar.DATE, calendar2.get(Calendar.DATE)+1) ;
			                calendar.set(Calendar.MONTH, calendar2.get(Calendar.MONTH)) ;
			                calendar.set(Calendar.YEAR, calendar2.get(Calendar.YEAR)) ;
			             	calendar.set(Calendar.HOUR_OF_DAY,  Integer.parseInt(timeArry[0])) ;
			            	calendar.set(Calendar.MINUTE, Integer.parseInt(timeArry[1])) ;
			            	calendar.set(Calendar.SECOND,Integer.parseInt(timeArry[1])) ; 
		            }
		         }
		            else if (repeatString.equalsIgnoreCase("weekly")){
		            	repeat = AlarmManager.INTERVAL_DAY * 7;	
				            	
							if (!(calendar2.get(Calendar.DATE) == calendar                     //To manage old dates 
									.get(Calendar.DATE)                                        //increment Week until it comes to current or greater then current  .   
									&& (calendar2.get(Calendar.MONTH) == calendar
											.get(Calendar.MONTH)) && (calendar2
										.get(Calendar.YEAR) == calendar
									.get(Calendar.YEAR)))){
								
								while (calendar.getTimeInMillis()<calendar2.getTimeInMillis()){
				        	    	calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+7) ;
				        		    Log.d("Next event ","Date = "+ calendar.get(Calendar.DATE)  +"Month= "+calendar.get(Calendar.MONTH)) ;
				        	    }
							}
		            }
		            else if (repeatString.equalsIgnoreCase("monthly")){
		            	repeat = AlarmManager.INTERVAL_DAY * 30;
		            	
		            	
						if (!(calendar2.get(Calendar.DATE) == calendar                     //To manage old dates 
								.get(Calendar.DATE)                                        //increment month until it comes to current or greater then current  .     
								&& (calendar2.get(Calendar.MONTH) == calendar
										.get(Calendar.MONTH)) && (calendar2
									.get(Calendar.YEAR) == calendar
								.get(Calendar.YEAR)))){
							
							while (calendar.getTimeInMillis()<calendar2.getTimeInMillis()){
			        	    	calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1) ;
			        		    Log.d("Next event ","Date = "+ calendar.get(Calendar.DATE)  +"Month= "+calendar.get(Calendar.MONTH)) ;
			        	    }
						}
		            } 
				    
				    db.insertNotification	(userid, boxid, cellid, loginid, time,
				    		0, repeatString , null , startdate,intWay ,intSound, strDesc,MedName);
				    
					counter = db.getLastNotificationId() ;
    			    myIntent.setData(Uri.parse("custom://" + counter));
    				myIntent.setAction(String.valueOf(counter));
					myIntent.putExtra("unique", counter+"") ;
					
					Log.d("Pending intent counter in IF ", "----- " + counter);
					
					pendingIntent = PendingIntent.getActivity(
							CellManage_AddShowActivity.this, counter,
							myIntent,PendingIntent.FLAG_UPDATE_CURRENT);


	            	Log.d("time", "Milisecond ="+calendar.getTimeInMillis()  ) ;
		      
		            
		            if (calendar.getTimeInMillis()> calendar2.getTimeInMillis())
					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
							calendar.getTimeInMillis(), repeat, pendingIntent);
					
				}
				
//				if (spIntervalDay.getSelectedItemPosition() != 4) {
//					
//					counter++ ;
//					
//					myIntent.putExtra("unique", counter+"") ;  //Set counter as unique value 
//					
////					tostarttimer = diff(time);
////					calendar.setTimeInMillis(System.currentTimeMillis());
////					curTime = calendar.getTimeInMillis();
////					calendar.set(mYear, mMonth - 1, mDate);	
////					nextTime = calendar.getTimeInMillis();
// 
////					tostarttimer = tostarttimer + (nextTime - curTime);
////					Log.i("id", "TC ------- " + totalCounter);
//					
//					
//					// Toast.makeText(getApplicationContext(),
//					// time+"  "+tostarttimer, Toast.LENGTH_LONG).show();
//					
//					// alarmManager.cancel(pendingIntent);
//
////					calendar.setTimeInMillis(System.currentTimeMillis());
////					calendar.add(Calendar.MILLISECOND, (int) tostarttimer);
//
//					
//					Log.d("Pending intent counter in IF ", "----- " + counter);
//					
//					pendingIntent = PendingIntent.getActivity(
//							CellManage_AddShowActivity.this, counter,
//							myIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//	            	Log.d("time", "Milisecond ="+calendar.getTimeInMillis()  ) ;
//	            	
//		      
//		            
//		            if (calendar.getTimeInMillis()> calendar2.getTimeInMillis())
//					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//							calendar.getTimeInMillis(), repeat, pendingIntent);
//					
//					db.insertNotification	(userid, boxid, cellid, loginid, time,
//							counter, repeatString , null , startdate,intWay ,intSound, strDesc,MedName);
////					applicationClass.totalCounter++;
//					
//				} 
				
				else {
					for (int k = 0; k < flags.length; k++) {

						calendar = Calendar.getInstance() ;
						calendar.set(Calendar.YEAR, mYear) ;
		            	calendar.set(Calendar.MONTH,mMonth-1) ;
		            	calendar.set(Calendar.DATE, mDate) ;
		            	
		            	calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArry[0])) ;
		            	calendar.set(Calendar.MINUTE, Integer.parseInt(timeArry[1])) ;
		            	calendar.set(Calendar.SECOND,Integer.parseInt(timeArry[2])) ;
		            	
						if (flags[k] == true) {
//							counter++ ;
//							myIntent.putExtra("unique", counter+"") ; //Set counter as unique value 

							calendar.set(Calendar.DAY_OF_WEEK , k+1);
							
							boolean flage =false; 
							if (!(calendar2.get(Calendar.DATE) == calendar                   //To manage old dates 
									.get(Calendar.DATE)                                      //increment day of week until it comes to current or greater then current  .   
									&& (calendar2.get(Calendar.MONTH) == calendar
											.get(Calendar.MONTH)) && (calendar2
										.get(Calendar.YEAR) == calendar
									.get(Calendar.YEAR)))){
								
								while (calendar.getTimeInMillis()<calendar2.getTimeInMillis()){
				        	    	calendar.set(Calendar.DATE,  calendar.get(Calendar.DATE)+7) ;
				        		    Log.d("Next event ","Date = "+ calendar.get(Calendar.DATE)  +"Month= "+calendar.get(Calendar.MONTH)) ;
				        		    flage=  true ;
				        	    }
								if (flage)
								calendar.set(Calendar.DAY_OF_WEEK , k+1);
							}
							
							db.insertNotification(userid, boxid, cellid,
									loginid, time.trim(),0, repeatString ,items[k].toString() , startdate ,intWay ,intSound, strDesc,MedName);
							counter = db.getLastNotificationId() ;
		    			    myIntent.setData(Uri.parse("custom://" + counter));
		    				myIntent.setAction(String.valueOf(counter));
							myIntent.putExtra("unique", counter+"") ;
							
							pendingIntent = PendingIntent.getActivity(
									CellManage_AddShowActivity.this,
									counter, myIntent,
									PendingIntent.FLAG_UPDATE_CURRENT);

							if ((calendar.getTimeInMillis()> calendar2.getTimeInMillis()))
							alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
									calendar.getTimeInMillis(), repeat,
									pendingIntent);

//							curTime = calendar.getTimeInMillis();
//							calendar.set(mYear, mMonth - 1, mDate);
//							
//							nextTime = calendar.getTimeInMillis();
//
//							int dow = calendar.get(Calendar.DAY_OF_WEEK);
//
//							if (k < dow) {
//								addDays = (7 - dow) + k;
//							} else if (k > dow) {
//								addDays = k - dow;
//							} else {
//								addDays = 0;
//							}
//							//
//							calendar.add(Calendar.DAY_OF_YEAR, addDays);
//
//							nextTime = calendar.getTimeInMillis();
//							tostarttimer = diff(time);
//
//							tostarttimer = tostarttimer + (nextTime - curTime);
//
//							
////							Log.i("Noti Counter", "----- " + totalCounter);
//							Log.d("Pending intent counter in ELSE", "----- " + counter);
//							// alarmManager.cancel(pendingIntent);
//
//							calendar.setTimeInMillis(System.currentTimeMillis());
//							calendar.add(Calendar.MILLISECOND,
//									(int) tostarttimer);
//
//							Toast.makeText(
//									getApplicationContext(),
//									"" + addDays + " date"
//											+ calendar.get(Calendar.DATE),
//									Toast.LENGTH_LONG).show();

//							applicationClass.totalCounter++;
							
							Log.d("time", "Milisecond ="+calendar.getTimeInMillis()) ;
						}
//						calendar.clear(Calendar.DAY_OF_WEEK)  ;
					}
				}
			}

//			SP.edit().putInt("totalCounter", totalCounter).commit() ;
			
			db.insertForm(userid, boxid, cellid, medid, picid, strDesc, intBg,
					intblAlert, intblBlink, intSound, intBuzz, intblMini,
					intblVibrant, intAlarm, intWay, intConfirm, intDayOf_Int,
					intDos_Mgt, intMany_Time, intSch_Int, intInt_Day, intWeek,
					intMonth, loginid, weekdaystring, startdate);
			Toast.makeText(getApplicationContext(), R.string.cell_add_cell_mgt,
					Toast.LENGTH_SHORT).show();
			// startActivity(myIntent);
			if (SP.getInt("lock", 0) == 1) {
				Toast.makeText(getApplicationContext(),
						R.string.start_medplan_on, Toast.LENGTH_LONG);
			}
			this.finish();
			break;
			
		case R.id.btnUpForm:
			if (tvMed.getText().toString().equalsIgnoreCase("") ) {         
				Toast.makeText(CellManage_AddShowActivity.this,
						getResources().getString(R.string.enter_med_mgt),
						Toast.LENGTH_SHORT).show();
				return;	
			}
			
			if (spBG.getSelectedItemPosition()==0 || spSound.getSelectedItemPosition()==0||spDosageMgt.getSelectedItemPosition()==0){
				Toast.makeText(CellManage_AddShowActivity.this,
						"Please enter required fields ",
						Toast.LENGTH_SHORT).show(); 
				return ; 
			}
			
//			boolean isDateValid  = isValidateDate() ;
//			Log.e("Date Valid ?", ""+isDateValid) ;
//			if (isDateValid==false){
//				break ;
//			}
//			else{
			
//	 			counter = db.getLastNotificationId() ;
				// db.deleteNotification(userid, boxid, cellid, loginid);
				AlarmManager alarmanager = (AlarmManager) getSystemService(ALARM_SERVICE);
				Intent myntent = null; 
//				db.updateNotifcationDead(userid, boxid, cellid, loginid);
				ArrayList<Integer> arrIds = db.getDeadIds(userid, boxid, cellid,loginid);
				
//				myntent = new Intent(CellManage_AddShowActivity.this,
//						BoxThreeActivity.class);
	  
				
				Log.i("User Id : Cell Add ", "" + loginid);
				
	           //Deactivate all previous alarm
				
				PendingIntent pendingIntent ;
				for (int i = 0; i < arrIds.size(); i++) {

					switch (boxid) {
					case 3:
						myntent = new Intent(CellManage_AddShowActivity.this,
								BoxThreeActivity.class);
						break;
					case 4:
						myntent = new Intent(CellManage_AddShowActivity.this,
								BoxFourActivity.class);
						break;

					case 6:	
						myntent = new Intent(CellManage_AddShowActivity.this	,
								BoxSixActivity.class);
						break;

					case 8:	
						myntent = new Intent(CellManage_AddShowActivity.this,
								BoxEightActivity.class);
						break;

					case 10:
						myntent = new Intent(CellManage_AddShowActivity.this,
								BoxTenActivity.class);
						break;

					}
					
					Log.i("Alarm Removed", ".......Alarm Removed............ " + arrIds.get(i));
					myntent.setData(Uri.parse("custom://" + arrIds.get(i)));
					myntent.setAction(String.valueOf(arrIds.get(i)));
					
		            pendingIntent = PendingIntent.getActivity(
							CellManage_AddShowActivity.this, arrIds.get(i),
							myntent, PendingIntent.FLAG_UPDATE_CURRENT);

					alarmanager.cancel(pendingIntent);
					pendingIntent.cancel();

				}
				
			    System.out.println("while selection~~~~~~~~~~~" + strDesc);

				Log.i("",
						"userid, boxid, cellid, medid, picid,loginid,WayToStop,Med,Description,Sound == "
								+ userid
								+ " "
								+ boxid
								+ " "
								+ cellid
								+ " "
								+ medid
								+ " "
								+ picid
								+ " "
								+ loginid
								+ " "
								+ intWay
								+ " "
								+ MedName + " " + strDesc + " " + intSound);

//				ArrayList<Notification_Model> arr2 = db.getCellNotification();
//				totalCounter = arr2.size();
//				totalCounter = SP.getInt("totalCounter", 0) ;
				
//			    myntent.setData(Uri.parse("custom://" + totalCounter));
//			    myntent.setAction(String.valueOf(totalCounter))
//			    ;
//				Log.d("Total couter", "----- " + totalCounter);
				// totalCounter =10;


				switch (boxid) {
				case 3:
					myntent = new Intent(CellManage_AddShowActivity.this,
							BoxThreeActivity.class);
					break;
				case 4:
					myntent = new Intent(CellManage_AddShowActivity.this,
							BoxFourActivity.class);
					break;

				case 6:	
					myntent = new Intent(CellManage_AddShowActivity.this,
							BoxSixActivity.class);
					break;

				case 8:
					myntent = new Intent(CellManage_AddShowActivity.this,
							BoxEightActivity.class);
					break;

				case 10:
					myntent = new Intent(CellManage_AddShowActivity.this,
							BoxTenActivity.class);
					break;

				}
				 
				//4513/3 , Dave nivas ,opps adeswer soc. , near canera bank , saijpur bogha , naroada road , ahmedabad , 382345 .
				
//				myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				myntent.putExtra("BoxID", boxid);
				myntent.putExtra("UserID", userid);
				myntent.putExtra("LoginID", loginid);
				myntent.putExtra("CellID", cellid);
				myntent.putExtra("WayToStop", intWay);
				myntent.putExtra("Med", MedName);

				myntent.putExtra("Description", strDesc);
				myntent.putExtra("Sound", intSound-1);
			    myntent.putExtra("FromWhereActivity","Heaven" ); 
//			    myntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			    myntent.setFlags(Intent.FLAG_FROM_BACKGROUND) ;
				


				for (int i = 1; i <= mLength; i++) {
					
//					counter++  ;
					
					Calendar calendar ;	calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
					Calendar calendar2 = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		 			
					String time = "";
					switch (i) {
					case 1:
						time = tvtime1.getText().toString();

						break;
					case 2:
						time = tvtime2.getText().toString();
						break;
					case 3:
						time = tvtime3.getText().toString();
						break;
					case 4:
						time = tvtime4.getText().toString();
						break;
					case 5:
						time = tvtime5.getText().toString();
						break;
					case 6:
						time = tvtime6.getText().toString();
						break;
					case 7:
						time = tvtime7.getText().toString();
						break;
					case 8:
						time = tvtime8.getText().toString();
						break;
					case 9:
						time = tvtime9.getText().toString();
						break;
					}

					if (time.equalsIgnoreCase("")) {
						Toast.makeText(CellManage_AddShowActivity.this,
								getResources().getString(R.string.set_alarm_time),
								Toast.LENGTH_SHORT).show();
						return;
					}

					Log.i("i time", i + " " + time); 

					long tostarttimer = diff(time);

//					calendar.setTimeInMillis(System.currentTimeMillis());
//					long curTime = calendar.getTimeInMillis();
//					calendar.set(tmy, tmm, tmd);
//					long nextTime = calendar.getTimeInMillis();
					
					
					 
					time = time+":"+calendar2.get(Calendar.SECOND)  ;
	            	String timeArry[] = time.split(":") ;
	            	String dateArry [] =  tvIdate.getText().toString().split("/") ;
	            	
	            	int year  = Integer.parseInt(dateArry[2]) ;
	            	int month  = Integer.parseInt(dateArry[1])-1 ;
		            int date  = Integer.parseInt(dateArry[0]) ; 
		            
		            int hour   = Integer.parseInt(timeArry[0]) ;
		            int minute  =Integer.parseInt(timeArry[1]) ;
			 
		            	calendar.set(Calendar.YEAR, year) ;
		            	calendar.set(Calendar.MONTH, month) ;
		            	calendar.set(Calendar.DATE, date) ;
		            	
		            	calendar.set(Calendar.HOUR_OF_DAY, hour) ;
		            	calendar.set(Calendar.MINUTE,minute) ;
		            	calendar.set(Calendar.SECOND,Integer.parseInt(timeArry[2])) ;
		            	
		            	 
//				            if (!(calendar.getTimeInMillis()> calendar2.getTimeInMillis()))
//				            	
//				            {
//				            	Toast.makeText(CellManage_AddShowActivity.this,
//										"Select  upcoming time ", Toast.LENGTH_LONG).show();
//				            	
//				            	return  ;
//				            }
		            	  
		            	  if (spIntervalDay.getSelectedItemPosition() != 4){
		            		
		            		  if (repeatString.equalsIgnoreCase("daily")){
					            	
					            	repeat = AlarmManager.INTERVAL_DAY;
					            	
						            if (!(calendar.getTimeInMillis()> calendar2.getTimeInMillis())){
						            	
						            	calendar= calendar2 ;
//							            calendar.clear() ;
//						                calendar.set(Calendar.DATE, calendar2.get(Calendar.DATE)+1) ;
//						                calendar.set(Calendar.MONTH, calendar2.get(Calendar.MONTH)) ;
//						                calendar.set(Calendar.YEAR, calendar2.get(Calendar.YEAR)) ;
						            	calendar.set(Calendar.HOUR_OF_DAY,  Integer.parseInt(timeArry[0])) ;
						            	calendar.set(Calendar.MINUTE, Integer.parseInt(timeArry[1])) ;
						            	calendar.set(Calendar.SECOND,Integer.parseInt(timeArry[2])) ;
						            	
						            	if (calendar.getTimeInMillis()<Calendar.getInstance().getTimeInMillis())
						            		calendar.set(Calendar.DATE, calendar2.get(Calendar.DATE)+1) ;
					            }
					         }
			            	  
					            else if (repeatString.equalsIgnoreCase("weekly")){
					            	repeat = AlarmManager.INTERVAL_DAY * 7;	
					            	
									if ((calendar2.get(Calendar.DATE) == calendar                     //To manage old dates 
											.get(Calendar.DATE)                                        //increment Week until it comes to current or greater then current  .   
											&& (calendar2.get(Calendar.MONTH) == calendar
													.get(Calendar.MONTH)) && (calendar2
												.get(Calendar.YEAR) == calendar
											.get(Calendar.YEAR)))   && (calendar.getTimeInMillis()<calendar2.getTimeInMillis()))
									{
										calendar = calendar2 ;
										calendar.set(Calendar.DATE, calendar2.get(Calendar.DATE)+7) ;
						            	calendar.set(Calendar.HOUR_OF_DAY,  Integer.parseInt(timeArry[0])) ;
						            	calendar.set(Calendar.MINUTE, Integer.parseInt(timeArry[1])) ;
						            	calendar.set(Calendar.SECOND,Integer.parseInt(timeArry[2])) ;
									}
					            	
									else	if (!(calendar2.get(Calendar.DATE) == calendar                     //To manage old dates 
												.get(Calendar.DATE)                                        //increment Week until it comes to current or greater then current  .   
												&& (calendar2.get(Calendar.MONTH) == calendar
														.get(Calendar.MONTH)) && (calendar2
													.get(Calendar.YEAR) == calendar
												.get(Calendar.YEAR)))){
											
											while (calendar.getTimeInMillis()<calendar2.getTimeInMillis()){
							        	    	calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+7) ;
							        		    Log.d("Next event ","Date = "+ calendar.get(Calendar.DATE)  +"Month= "+calendar.get(Calendar.MONTH)) ;
							        	    }
										}
					            }
			            	  
//			            	  
//					            else if (repeatString.equalsIgnoreCase("weekly")){
//					            	repeat = AlarmManager.INTERVAL_DAY * 7;	
//							            	
//										if (!(calendar2.get(Calendar.DATE) == calendar                     //To manage old dates 
//												.get(Calendar.DATE)                                        //increment Week until it comes to current or greater then current  .   
//												&& (calendar2.get(Calendar.MONTH) == calendar
//														.gnet(Calendar.MONTH)) && (calendar2
//													.get(Calendar.YEAR) == calendar
//												.get(Calendar.YEAR)))){
//											
//											while (calendar.getTimeInMillis()<calendar2.getTimeInMillis()){
//							        	    	calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+7) ;
//							        		    Log.d("Next event ","Date = "+ calendar.get(Calendar.DATE)  +"Month= "+calendar.get(Calendar.MONTH)) ;
//							        	    }
//										}
//							        	    
////						            }
//					            }
			            	  
					            else if (repeatString.equalsIgnoreCase("monthly")){
					            	repeat = AlarmManager.INTERVAL_DAY * 30;

									if ((calendar2.get(Calendar.DATE) == calendar                     //To manage old dates 
											.get(Calendar.DATE)                                        //increment Week until it comes to current or greater then current  .   
											&& (calendar2.get(Calendar.MONTH) == calendar
													.get(Calendar.MONTH)) && (calendar2
												.get(Calendar.YEAR) == calendar
											.get(Calendar.YEAR)))   && (calendar.getTimeInMillis()<calendar2.getTimeInMillis()))
									{
										calendar = calendar2 ;
										calendar.set(Calendar.MONTH, calendar2.get(Calendar.MONTH)+1) ;
						            	calendar.set(Calendar.HOUR_OF_DAY,  Integer.parseInt(timeArry[0])) ;
						            	calendar.set(Calendar.MINUTE, Integer.parseInt(timeArry[1])) ;
						            	calendar.set(Calendar.SECOND,Integer.parseInt(timeArry[2])) ;
									}
									
									else if (!(calendar2.get(Calendar.DATE) == calendar                     //To manage old dates 
											.get(Calendar.DATE)                                        //increment month until it comes to current or greater then current  .     
											&& (calendar2.get(Calendar.MONTH) == calendar
													.get(Calendar.MONTH)) && (calendar2
												.get(Calendar.YEAR) == calendar
											.get(Calendar.YEAR)))){
										
										while (calendar.getTimeInMillis()<calendar2.getTimeInMillis()){
						        	    	calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1) ;
						        		    Log.d("Next event ","Date = "+ calendar.get(Calendar.DATE)  +"Month= "+calendar.get(Calendar.MONTH)) ;
						        	    }
									}
					            } 
		            		   
		            		    db.insertNotification(userid, boxid, cellid, loginid, time,
		            				  0, repeatString , null , startdate ,intWay ,intSound, strDesc,MedName);
		            		  
		            		    counter = db.getLastNotificationId() ;
		    					myntent.setData(Uri.parse("custom://" + counter));
		    					myntent.setAction(String.valueOf(counter));

								myntent.putExtra("unique", counter+"") ;
								
								Log.d("Pending intent counter in IF", "----- " + counter);
								 
								
						    	PendingIntent	pd = PendingIntent.getActivity(
										CellManage_AddShowActivity.this, counter,
										myntent, PendingIntent.FLAG_UPDATE_CURRENT);			
						    	
//						    	if ((calendar.getTimeInMillis()> calendar2.getTimeInMillis()))
					         	alarmanager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), repeat, pd);
					         	
					            Log.d("time","Date ="+ date+" Month ="+month+" Year = "+year+ " Hour ="+hour+" Minute= "+minute+" Milisecond ="+calendar.getTimeInMillis()  ) ;
					            
		            	  }
		            	  
//					if (spIntervalDay.getSelectedItemPosition() != 4) {
//						
//						myntent.putExtra("unique", counter+"") ;
//						
//						Log.d("Pending intent counter in IF", "----- " + counter);
//						
//				    	PendingIntent	pd = PendingIntent.getActivity(
//								CellManage_AddShowActivity.this, counter,
//								myntent, PendingIntent.FLAG_UPDATE_CURRENT);					
//			            
//			         	alarmanager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), repeat, pd);
//			         	
//			            Log.d("time","Date ="+ date+" Month ="+month+" Year = "+year+ " Hour ="+hour+" Minute= "+minute+" Milisecond ="+calendar.getTimeInMillis()  ) ;
//			            
//						db.insertNotification(userid, boxid, cellid, loginid, time,
//								counter, repeatString , null , startdate ,intWay ,intSound, strDesc,MedName);
////						applicationClass.totalCounter++;
//					}
					else {
						for (int k = 0; k < flags.length; k++) {

//			            	calendar.clear() ; 
					        calendar = Calendar.getInstance() ;
			            	calendar.set(Calendar.YEAR, year) ;
			            	calendar.set(Calendar.MONTH, month) ;
			            	calendar.set(Calendar.DATE, date) ;
			            	
			            	calendar.set(Calendar.HOUR_OF_DAY, hour) ;
			            	calendar.set(Calendar.MINUTE,minute) ;
			            	calendar.set(Calendar.SECOND,Integer.parseInt(timeArry[2])) ;	
			            	
							if (flags[k] == true)  {
								 
//								counter++ ;
						 
								boolean flage =false;
//								myntent.putExtra("unique", counter+"") ;

//								Log.d("time before set ", "Milisecond ="+calendar.getTimeInMillis()) ;

								calendar.set(Calendar.DAY_OF_WEEK , k+1);
								
								if ((calendar2.get(Calendar.DATE) == calendar                     //To manage old dates 
										.get(Calendar.DATE)                                        //increment Week until it comes to current or greater then current  .   
										&& (calendar2.get(Calendar.MONTH) == calendar
												.get(Calendar.MONTH)) && (calendar2
											.get(Calendar.YEAR) == calendar
										.get(Calendar.YEAR)))   && (calendar.getTimeInMillis()<calendar2.getTimeInMillis()))
								{
									calendar = calendar2 ;
									calendar.set(Calendar.DATE, calendar2.get(Calendar.DATE)+7) ;
					            	calendar.set(Calendar.HOUR_OF_DAY,  Integer.parseInt(timeArry[0])) ;
					            	calendar.set(Calendar.MINUTE, Integer.parseInt(timeArry[1])) ;
					            	calendar.set(Calendar.SECOND,Integer.parseInt(timeArry[2])) ;
								}
								
								else	if (!(calendar2.get(Calendar.DATE) == calendar                   //To manage old dates 
										.get(Calendar.DATE)                                      //increment day of week until it comes to current or greater then current  .   
										&& (calendar2.get(Calendar.MONTH) == calendar
												.get(Calendar.MONTH)) && (calendar2
											.get(Calendar.YEAR) == calendar
										.get(Calendar.YEAR)))){
									
									while (calendar.getTimeInMillis()<calendar2.getTimeInMillis()){
										
					        	    	calendar.set(Calendar.DATE,  calendar.get(Calendar.DATE)+7) ;
					        		    Log.d("Next event ","Date = "+ calendar.get(Calendar.DATE)  +"Month= "+calendar.get(Calendar.MONTH)) ;
					        		    flage=true;
					        	    }
									if (flage){
										calendar.set(Calendar.DAY_OF_WEEK , k+1);
									}
								}

//								calendar = Calendar.getInstance();
////								curTime = calendar.getTimeInMillis();
//								calendar.set(mYear, mMonth - 1, mDate);
	//
////								nextTime = calendar.getTimeInMillis();
	//
//								int dow = calendar.get(Calendar.DAY_OF_WEEK);
	//
//								if (k < dow) {
//									addDays = (7 - dow) + k;
//								} else if (k > dow) {	
//									addDays = k - dow;
//								} else {
//									addDays = 0;
//								}
//								//
//								calendar.add(Calendar.DAY_OF_YEAR, addDays);
////								nextTime = calendar.getTimeInMillis();	
	// 
//								tostarttimer = diff(time);
	//
////								tostarttimer = tostarttimer + (nextTime - curTime);
	//
								
								db.insertNotification(userid, boxid, cellid,
										loginid, time.trim(), 0,repeatString, items[k].toString() , startdate ,intWay ,intSound, strDesc,MedName);

								counter = db.getLastNotificationId() ;
			    			    myntent.setData(Uri.parse("custom://" + counter));
			    				myntent.setAction(String.valueOf(counter));
								myntent.putExtra("unique", counter+"") ;
								
						    	PendingIntent	pdI = PendingIntent.getActivity(
										CellManage_AddShowActivity.this,
										counter, myntent,PendingIntent.FLAG_UPDATE_CURRENT);
						    	
//								Log.i("Total couter", "----- " + totalCounter);
//								// alarmManager.cancel(pendingIntent);
	//
//								calendar.setTimeInMillis(System.currentTimeMillis());
//								calendar.add(Calendar.MILLISECOND,
//										(int) tostarttimer);
	//
//								Toast.makeText(
//										getApplicationContext(),
//										"" + addDays + " date"
//												+ calendar.get(Calendar.DATE),
//										Toast.LENGTH_LONG).show();

//						    	if ((calendar.getTimeInMillis()> calendar2.getTimeInMillis()))
								alarmanager.setRepeating(AlarmManager.RTC_WAKEUP,
										calendar.getTimeInMillis(), repeat,
										pdI);
															
						        Log.d("time", "Milisecond ="+calendar.getTimeInMillis()) ;
							}
						}
						
					}
//					applicationClass.totalCounter++;
				}

//				SP.edit().putInt("totalCounter", totalCounter).commit() ;
				
				Log.i("", "userid, boxid, cellid, medid, picid,loginid == "
						+ userid + " " + boxid + " " + cellid + " " + medid + " "
						+ picid + " " + loginid);
				db.updateForm(userid, boxid, cellid, medid, picid, strDesc, intBg,
						intblAlert, intblBlink, intSound, intBuzz, intblMini,
						intblVibrant, intAlarm, intWay, intConfirm, intDayOf_Int,
						intDos_Mgt, intMany_Time, intSch_Int, intInt_Day, intWeek,
						intMonth, loginid, weekdaystring, startdate);
				Toast.makeText(getApplicationContext(),
						R.string.cell_update_cell_mgt, Toast.LENGTH_SHORT).show();
				// startActivity(myntent);	
				this.finish();

//			}
			break;

		case R.id.btnDelForm:
		
			AlarmManager alarmanager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
//			db.updateNotifcationDead(userid, boxid, cellid, loginid);
			Intent myntent1 = null; 
			ArrayList<Integer> arrIds1 = db.getDeadIds(userid, boxid, cellid,
					loginid); 
			
			switch (boxid) {
			case 3:
				myntent1 = new Intent(CellManage_AddShowActivity.this,
						BoxThreeActivity.class);
				break;
			case 4:
				myntent1 = new Intent(CellManage_AddShowActivity.this,
						BoxFourActivity.class);
				break;

			case 6:
				myntent1 = new Intent(CellManage_AddShowActivity.this,
						BoxSixActivity.class);
				break;

			case 8:
				myntent1 = new Intent(CellManage_AddShowActivity.this,
						BoxEightActivity.class);
				break;

			case 10:
				myntent1 = new Intent(CellManage_AddShowActivity.this,
						BoxTenActivity.class);
				break;

			}
			PendingIntent pdIntent ;
			for (int i = 0; i < arrIds1.size(); i++) {
				
				Log.d("Removed pending intent's id is= ",arrIds1.get(i)+"") ;
				
				Log.i("Alarm Removed", "....... " + arrIds1.get(i));
				
				myntent1.setData(Uri.parse("custom://" + arrIds1.get(i)));
				myntent1.setAction(String.valueOf(arrIds1.get(i)));
			    
				pdIntent =  PendingIntent.getActivity(
						CellManage_AddShowActivity.this, arrIds1.get(i),
						myntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				
				pdIntent.cancel();
				alarmanager1.cancel(pdIntent);
			}
			
			db.deleteForm(userid, boxid, cellid, loginid);
			Toast.makeText(getApplicationContext(),
					R.string.cell_delete_cell_mgt, Toast.LENGTH_SHORT).show();
			this.finish();
			break;
		case R.id.tvTime1:
			mPos = 1;
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.tvTime2:
			mPos = 2;
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.tvTime3:
			mPos = 3;
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.tvTime4:
			mPos = 4;
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.tvTime5:
			mPos = 5;
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.tvTime6:
			mPos = 6;
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.tvTime7:
			mPos = 7;
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.tvTime8:
			mPos = 8;
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.tvTime9:
			mPos = 9;
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.btnBkForm:
		case R.id.btnCanForm:
			this.finish();
			break;
		}
	}
 
	public static long diff(String time2) {
		long difference = 0;
		try {

			// set current time
			Calendar c = Calendar.getInstance();
			int curHours = c.get(Calendar.HOUR_OF_DAY);
			int curMinutes = c.get(Calendar.MINUTE);

			String time1 = String.valueOf(curHours) + ":"
					+ String.valueOf(curMinutes);
			// String time2 = "17:00:00";

			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			Date date1 = format.parse(time1);
			Date date2 = format.parse(time2);
			difference = date2.getTime() - date1.getTime();
			if (difference < 0) {
				difference = difference * -1;
				//(24*60*60*100)=86400000 convert one day in the millisecond 
				difference = 86400000 - difference;
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return difference;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	class CustomSpiner extends Spinner{

		public CustomSpiner(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
		
		@Override
		public void setSelection(int position) {
			boolean sameSelected = position == getSelectedItemPosition();
			super.setSelection(position);
			if (sameSelected) {
			      // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
			      getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
			    }
		}

	}
	
	private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<Medicine_Model> userList;
		public EfficientAdapter(Context context,ArrayList<Medicine_Model> userList) {
			mInflater = LayoutInflater.from(context);
			this.userList = userList;
		}
		
		public int getCount() {
			return userList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.listuserrow, null);
				holder = new ViewHolder();
				holder.uName = (TextView) convertView
						.findViewById(R.id.userName);
				holder.uType = (TextView) convertView
						.findViewById(R.id.userType);
				holder.uImage = (ImageView) convertView
						.findViewById(R.id.userImage);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		
//			Log.d("Medicine name ", userList.get(position).nm ) ;
//			Log.d("Medicine type  ", userList.get(position).sdesc ) ;
			
			holder.uName.setText(userList.get(position).nm);
			holder.uType.setText(userList.get(position).sdesc);
			Picture_Model temp = db.getPicture(userList.get(position).mpicid);
			
				holder.uImage.setImageBitmap(GlobalMethods.decodeFile(temp.path));
				holder.uImage.setScaleType(ScaleType.FIT_XY);
				return convertView;

		}

	   class ViewHolder {

			TextView uName, uType;
			ImageView uImage;

		}
	}
}