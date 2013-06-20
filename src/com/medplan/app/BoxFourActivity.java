package com.medplan.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medpan.util.CellInfo_Model;
import com.medpan.util.CommonMethod;
import com.medpan.util.Constant;
import com.medpan.util.GlobalMethods;
import com.medpan.util.Notification_Model;
import com.medpan.util.Picture_Model;
import com.medpan.util.User_Model;
import com.medplan.db.databasehelper;

public class BoxFourActivity extends Activity implements OnClickListener,
		OnInitListener {
	RelativeLayout titleHeadLayout, rlCellA, rlCellB, rlCellC, rlCellD, tmpRl;
	LinearLayout MainBgLayout;
	TextView tvCellA, tvCellB, tvCellC, tvCellD, tvTimeA, tvTimeB, tvTimeC,
			tvTimeD, tvtmpTime;
	Intent intent;
	ImageView imgCellA, imgCellB, imgCellC, imgCellD, tmpIV;
	Button btnConfirm;
	public int cell_pos, boxid, userid, loginid, picid, WaytoStop, alarmSound;
	databasehelper db;
	public String task = "add", MedName,Desc;
	ArrayList<CellInfo_Model> cellinfolist = new ArrayList<CellInfo_Model>();
	Picture_Model picmodel;
	int a = -1, b = -1, c = -1, d = -1;
	Animation anim;
	 boolean blink, miniImg, vibrat, alert, sound, isConfirm, isAlarm;
	Handler handler;
	private TextToSpeech mTts;
	SimpleDateFormat sdf;
	boolean done = false;
	int counter = 1;
	// This code can be any value you want, its just a checksum.
	private static final int MY_DATA_CHECK_CODE = 1234;
	ImageView headLogo;
	TextView headerTitle;
	CellInfo_Model cell;
	AlertDialog alertMsg;
	SharedPreferences SP;
	private ApplicationClass applicationClass;
	private String 	activity   ;
	
	public   boolean stop ;
	private String fromWhere ;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		db = new databasehelper(this);
		SP = PreferenceManager.getDefaultSharedPreferences(this);
		Intent intent = getIntent();
		
		String pendingInterntID = null ; 
		
		pendingInterntID  = intent.getStringExtra("unique") ;
		
		applicationClass = (ApplicationClass) this.getApplication();

		if (intent.hasExtra("BoxID")){
//			
//			boxid = intent.getIntExtra("BoxID", 0);
//			userid = intent.getIntExtra("UserID", 0);
//			loginid = intent.getIntExtra("LoginID", 0);
//			cell_pos = intent.getIntExtra("CellID", -1);
//			MedName = intent.getStringExtra("Med");
//			WaytoStop = intent.getIntExtra("WayToStop", 0);
//			alarmSound = intent.getIntExtra("Sound", 0);	
//			Desc = intent.getStringExtra("Description");
//			fromWhere = intent.getStringExtra("FromWhereActivity");
			
			activity = intent.getStringExtra("Activity") ;
			
			boxid = intent.getIntExtra("BoxID", 0);
			applicationClass.boxId = boxid;
			userid = intent.getIntExtra("UserID", 0);
			applicationClass.userId = userid;
			loginid = intent.getIntExtra("LoginID", 0);
			applicationClass.loginId = loginid;
			cell_pos = intent.getIntExtra("CellID", -1);
			applicationClass.cellPos = cell_pos;
			MedName = intent.getStringExtra("Med");
			applicationClass.medName = MedName;
			WaytoStop = intent.getIntExtra("WayToStop", 0);
			applicationClass.wayToStop = WaytoStop;
			Desc = intent.getStringExtra("Description");
			applicationClass.desc = Desc;
			alarmSound = intent.getIntExtra("Sound", 0);
			applicationClass.alarmSound = alarmSound;
			fromWhere = intent.getStringExtra("FromWhereActivity");
//			applicationClass.fromWhere = fromWhere;
		}
		else {
			boxid = applicationClass.boxId;
			userid = applicationClass.userId;
			loginid = applicationClass.loginId;
//			cell_pos = applicationClass.cellPos;
			MedName = applicationClass.medName;
			WaytoStop = applicationClass.wayToStop;
			Desc = applicationClass.desc;
			alarmSound = applicationClass.alarmSound;
//			fromWhere = applicationClass.fromWhere;
		}

		
		Window wind;
	    wind = this.getWindow();
	    wind.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
	    wind.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	    wind.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
//		if (SP.getInt("lock", -1) == 0 && cell_pos != -1) {
//			Log.i("", "finish");
//			stop =true ;
//			finish();
//		}
//		else if (SP.getString("isLogin", "")=="no"){
//			stop = true ;
//		finish();
//	}
		if (pendingInterntID!=null)
			Log.d("*********** Pending intent Id =", "Pending intent Id ="+pendingInterntID) ;
		
//		if(((SP.getInt("lock", -1) == 0))||(SP.getString("isLogin", "")=="no")||(db.isUserExsist(userid)==false)) {
//			Log.e("", "*************  finish  ****	**************");
//			stop = true ;
//			finish();
//		}
//	else{
//		
//		
//		ArrayList<CellInfo_Model> arrCell = db.getCellInfoForBox(loginid,
//				userid, boxid, cell_pos);
//		if (arrCell.size()==0 && fromWhere!=null){
//			Log.e("", "*************  finish  ****	**************");
//			stop = true ;
//			finish();
//		}
//		else
//			if (activity!=null)
//		Toast.makeText(BoxFourActivity.this,
//				getResources().getString(R.string.cell_edit),
//				Toast.LENGTH_LONG).show();
//	}
       
		
		if (((SP.getInt("lock", -1) == 0) && cell_pos != -1)
				|| (SP.getString("isLogin", "") == "no")
				|| (db.isUserExsist(userid) == false)) {
			Log.e("", "*************  finish  ****	**************");
			stop = true;
			finish();
		} else {
			ArrayList<CellInfo_Model> arrCell = db.getCellInfoForBox(loginid,
					userid, boxid, cell_pos);
			if (arrCell.size() == 0 & fromWhere != null) {
				Log.e("", "*************  finish  ****	**************");
				stop = true;
				finish();
			} else{
				if (activity!=null)
				Toast.makeText(BoxFourActivity.this,
						getResources().getString(R.string.cell_edit),
						Toast.LENGTH_LONG).show();
			}
				
		}
		
		setContentView(R.layout.cell_mgt_four_boxe);
		Constant.flag = false;

		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.cell_mgt);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.cell_mgt);

//		Toast.makeText(BoxFourActivity.this,
//				getResources().getString(R.string.cell_edit),
//				Toast.LENGTH_LONG).show();

		

		anim = AnimationUtils.loadAnimation(this, R.anim.blink);
		handler = new Handler();

		tvCellA = (TextView) findViewById(R.id.tv_four_boxA);
		tvCellB = (TextView) findViewById(R.id.tv_four_boxB);
		tvCellC = (TextView) findViewById(R.id.tv_four_boxC);
		tvCellD = (TextView) findViewById(R.id.tv_four_boxD);

		tvTimeA = (TextView) findViewById(R.id.tv_four_timeA);
		tvTimeB = (TextView) findViewById(R.id.tv_four_timeB);
		tvTimeC = (TextView) findViewById(R.id.tv_four_timeC);
		tvTimeD = (TextView) findViewById(R.id.tv_four_timeD);

		rlCellA = (RelativeLayout) findViewById(R.id.rl_four_boxA);
		rlCellB = (RelativeLayout) findViewById(R.id.rl_four_boxB);
		rlCellC = (RelativeLayout) findViewById(R.id.rl_four_boxC);
		rlCellD = (RelativeLayout) findViewById(R.id.rl_four_boxD);

		imgCellA = (ImageView) findViewById(R.id.img_four_boxA);
		imgCellB = (ImageView) findViewById(R.id.img_four_boxB);
		imgCellC = (ImageView) findViewById(R.id.img_four_boxC);
		imgCellD = (ImageView) findViewById(R.id.img_four_boxD);

		btnConfirm = (Button) findViewById(R.id.btn_boxfour_confirm);

//		rlCellA.setOnClickListener(this);
//		rlCellB.setOnClickListener(this);
//		rlCellC.setOnClickListener(this);
//		rlCellD.setOnClickListener(this);
//		btnConfirm.setOnClickListener(this);

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setCancelable(false);
		alertDialog.setTitle("Confirm");	
		alertDialog.setMessage("Did you take your medicine?");
		alertDialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						isConfirm = true;
						done = true;
						ReportDetails();
					}
				});

		alertDialog.setNegativeButton(R.string.no_thanks,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						isConfirm = false;
						done = true;
						ReportDetails();
					}
				});

		alertMsg = alertDialog.create();

		TextView tv = (TextView) findViewById(R.id.tv_userbox_name);
		TextView tvBoxname = (TextView) findViewById(R.id.tv_userbox_boxname);

		
		Log.i("", userid + "-----------" + boxid);
		User_Model arr = db.getPatients(String.valueOf(userid));
		try{
		tv.setText("User : " + arr.name + " " + arr.surname);
		}catch (Exception e) {
		}
		tvBoxname.setText(" Box: " + boxid);

		cellinfolist = db.getCellInfoForBox(loginid, userid, boxid, -1);

		if (cellinfolist.size() > 0) {
			a = -1;
			b = -1;
			c = -1;
			d = -1;
			for (int i = 0; i < cellinfolist.size(); i++) {
				Log.i("Cellid", "--------------" + cellinfolist.get(i).cellid);
				switch (cellinfolist.get(i).cellid) {
				case 0:
					tvCellA
							.setText(""
									+ db.getMedical(cellinfolist.get(i).medid)
											.get(0).nm);
					try{
						ArrayList<Notification_Model> tmp = db.getCellNotification(loginid, userid, boxid, 0);
						if(tmp.size()>0)
						{
							tvTimeA.setText(tmp.get(0).strTime);
							tvTimeA.setVisibility(View.VISIBLE);
						}
						}catch (Exception e) {
							// TODO: handle exception
						}
					picid = cellinfolist.get(i).picid;
					a = i;
					break;
				case 1:
					tvCellB
							.setText(""
									+ db.getMedical(cellinfolist.get(i).medid)
											.get(0).nm);
					try{
						ArrayList<Notification_Model> tmp = db.getCellNotification(loginid, userid, boxid, 1);
						if(tmp.size()>0)
						{
							tvTimeB.setText(tmp.get(0).strTime);
							tvTimeB.setVisibility(View.VISIBLE);
						}
						}catch (Exception e) {
							// TODO: handle exception
						}
					picid = cellinfolist.get(i).picid;
					b = i;
					break;
				case 2:
					tvCellC
							.setText(""
									+ db.getMedical(cellinfolist.get(i).medid)
											.get(0).nm);
					try{
						ArrayList<Notification_Model> tmp = db.getCellNotification(loginid, userid, boxid, 2);
						if(tmp.size()>0)
						{
							tvTimeC.setText(tmp.get(0).strTime);
							tvTimeC.setVisibility(View.VISIBLE);
						}
						}catch (Exception e) {
							// TODO: handle exception
						}
					picid = cellinfolist.get(i).picid;
					c = i;
					break;
				case 3:
					tvCellD
							.setText(""
									+ db.getMedical(cellinfolist.get(i).medid)
											.get(0).nm);
					try{
						ArrayList<Notification_Model> tmp = db.getCellNotification(loginid, userid, boxid, 3);
						if(tmp.size()>0)
						{
							tvTimeD.setText(tmp.get(0).strTime);
							tvTimeD.setVisibility(View.VISIBLE);
						}
						}catch (Exception e) {
							// TODO: handle exception
						}
					picid = cellinfolist.get(i).picid;
					d = i;
					break;

				}
			}
		}

		titleHeadLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		MainBgLayout = (LinearLayout) findViewById(R.id.MainAlarmLayout);
		try {
			SplashScreen.Cmethod.CheckAddShowScreen(Constant._StrCheck,
					titleHeadLayout, MainBgLayout);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(BoxFourActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});

		if (cell_pos != -1 && stop==false) {
			Toast.makeText(getApplicationContext(), getString(R.string.tapToast), Toast.LENGTH_LONG).show();
			isAlarm = true;
			ArrayList<CellInfo_Model> arrCell = db.getCellInfoForBox(loginid,
					userid, boxid, cell_pos);
			try {
				cell = arrCell.get(0);
				blink = cell.blBlink == 1 ? true : false;
				miniImg = cell.blMini == 1 ? true : false;
				vibrat = cell.blVibrant == 1 ? true : false;
				alert = cell.blAlert == 1 ? true : false;
				int color = cell.intBg;
				final int limit = cell.intBuzz;

				switch (color) {
				case 1:
					setColor(Color.RED);
					break;
				case 2:
					setColor(Color.YELLOW);
					break;
				case 3:
					setColor(Color.GREEN);
					break;
				case 4:
					setColor(Color.rgb(255, 200, 0));
					break;			
				case 5:
					setColor(Color.rgb(198,195,181));
					break;
				case 6:
					setColor(Color.WHITE);
					break;
				}

				// show alarm time
				tvtmpTime.setVisibility(View.VISIBLE);
				sdf = new SimpleDateFormat("HH:mm");
				String currentDateandTime = sdf.format(new Date(System
						.currentTimeMillis()));
				System.out.println("current time" + currentDateandTime);
				tvtmpTime.setText(currentDateandTime);

				Log
						.i(
								"TAG===",
								"loginid, userid, boxid, cell_pos,blink,minImg,vibrat,alert,color,limit ===== \n"
										+ loginid
										+ " "
										+ userid
										+ " "
										+ boxid
										+ " "
										+ cell_pos
										+ " "
										+ blink
										+ " "
										+ miniImg
										+ " "
										+ vibrat
										+ " "
										+ alert
										+ " " + color + " " + limit);

				sound = true;

				// check whether blink,miniImg,alert,vibrate are true or
				// false...

				// check which way is selected to stop alarm.
				// 1 indicate the way: touch the button and stop the alarm.
				if (WaytoStop == 1 || WaytoStop == 0) {
					btnConfirm.setVisibility(View.VISIBLE);
					rlCellA.setClickable(false);
					rlCellB.setClickable(false);
					rlCellC.setClickable(false);
					rlCellD.setClickable(false);
//					Toast.makeText(getApplicationContext(), getString(R.string.tapToast), Toast.LENGTH_LONG).show();
				}
				
				if (WaytoStop==1)
					Toast.makeText(getApplicationContext(),
							getString(R.string.tapToast), Toast.LENGTH_LONG)
							.show();
				else
					Toast.makeText(getApplicationContext(),
							getString(R.string.tapCellToast), Toast.LENGTH_LONG)
							.show();
				
				// blinking cell
				if (blink == true) {
//					Toast.makeText(getApplicationContext(), getString(R.string.tapCellToast), Toast.LENGTH_LONG).show();
					tmpRl.setAnimation(anim);
					
					System.out.println("Working~~~~~~1");
				}
				// to show to not mini Image.
				if (miniImg == true) {
					System.out.println("Working~~~~~~2");
					tmpIV.setVisibility(View.VISIBLE);
					picmodel = db.getPicture(picid);
					Bitmap bitmap = GlobalMethods.decodeFile(picmodel.path);
					System.out.println("wint three box cell~~~~~~~~~~~~"
							+ picmodel.path);
					if (bitmap == null) {
						tmpIV.setVisibility(View.GONE);
						// tmpIV.setBackgroundResource(R.drawable.add_photo);
					} else {
						tmpIV.setImageBitmap(bitmap);
					}
				}
				// To vibrate or not.
				if (vibrat == true) {
					System.out.println("Working~~~~~~3");
					CommonMethod.vibration(this);
				}
 
				if (sound == true && alert == true) {
					System.out.println("Both~~~~~~~");
					CommonMethod.SoundPlayer(this, Constant.Sound[alarmSound]);

			
					CommonMethod.player
							.setOnCompletionListener(new OnCompletionListener() {

								@Override
								public void onCompletion(MediaPlayer mp) {
									// /will use count and loop as per number of
									// repetition chosen.

									Intent checkIntent = new Intent();
									checkIntent
											.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
									startActivityForResult(checkIntent,
											MY_DATA_CHECK_CODE);

									handler.postDelayed(new Runnable() {

										public void run() {
											if (counter >= limit) {
												CommonMethod.player.stop();
											} else {
												CommonMethod.player.start();
											}
											counter++;
										}
									}, 3000);
								}
							});
				}
				
				if (sound == true && alert == false) {
					System.out.println("sound true alert false~~~~~~~");
					CommonMethod.SoundPlayer(this, Constant.Sound[alarmSound]);
					CommonMethod.player
							.setOnCompletionListener(new OnCompletionListener() {

								@Override
								public void onCompletion(MediaPlayer mp) {
									// /will use count and loop as per number of
									// repetition chosen.
									handler.postDelayed(new Runnable() {

										public void run() {
											if (counter >= limit) {
												CommonMethod.player.stop();
											} else {
												CommonMethod.player.start();
											}
											counter++;
										}
									}, 3000);

								}
							});
				}
				if (sound == false && alert == true) {
					System.out.println("sound false alert true~~~~~~~");
					Intent checkIntent = new Intent();
					checkIntent
							.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
					startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

				} else {
					System.out.println("~~~~~~~~~~~~do nothing~~~~~~~");
				}
			}

			catch (Exception e) {

			}
		}
	}
	

	public void doShowToast(final View v)
	{
//		if(cell_pos!=-1&!(blink == true || miniImg == true || vibrat == true|| sound == true))
		if(fromWhere==null)
		{
		LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast,
                                       (ViewGroup) findViewById(R.id.llayout));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.FILL, 0, 0);
        toast.setView(view);
        toast.show();
		}
		        new Handler().post(new Runnable() {
					@Override
					public void run() {
						doClick(v);		
					}
				});
				
	}
	void doClick(View v)
	{

		if(CommonMethod.vibrator!=null)
			CommonMethod.vibrator.cancel();
		if(WaytoStop==2)
		{ 
			try {
				tmpIV.setVisibility(View.GONE);
				tvtmpTime.setVisibility(View.GONE);
				tmpRl.setBackgroundColor(Color.TRANSPARENT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		switch (v.getId()) {
		case R.id.rl_four_boxA:
			alert = false;
			if (blink == true) {
				anim.cancel();
				tmpRl.clearAnimation();
				tmpRl.setBackgroundColor(Color.TRANSPARENT);
				btnConfirm.setVisibility(View.GONE);
			}
			if (miniImg == true) {
			}
			if (vibrat == true) {
				CommonMethod.vibrator.cancel();

			}

			if (sound == true) {
				CommonMethod.player.stop();
				mTts.stop();
				mTts.shutdown();
				btnConfirm.setVisibility(View.GONE);

			}
			if(mTts!=null)
			{
				mTts.stop();
				mTts.shutdown();
			}
//			if (blink == true || miniImg == true || vibrat == true
//					|| sound == true || alert == true||fromWhere!=null)
			if (fromWhere != null)
			{
				blink = false;
				miniImg = false;
				vibrat = false;
				sound = false;
				alert = false;
				tmpIV.setVisibility(View.GONE);
				tvtmpTime.setVisibility(View.GONE);
				Constant.flag = true;
				alertMsg.show();
			}

			else {
				cell_pos = 0;
				if (a != -1)
					task = "show";
				Call_actvity();
			}
			break;
		case R.id.rl_four_boxB:
			alert = false;
			if (blink == true) {
				anim.cancel();
				tmpRl.clearAnimation();
				tmpRl.setBackgroundColor(Color.TRANSPARENT);
				btnConfirm.setVisibility(View.GONE);

			}
			if (miniImg == true) {

			}
			if (vibrat == true) {
				CommonMethod.vibrator.cancel();
			}

			if (sound == true) {
				CommonMethod.player.stop();
				btnConfirm.setVisibility(View.GONE);
			}
			if(mTts!=null)if(mTts.isSpeaking())
			{
				mTts.stop();
				mTts.shutdown();
			}
//			if (blink == true || miniImg == true || vibrat == true
//					|| sound == true||fromWhere!=null)
			if (fromWhere != null)
			{
				blink = false;
				miniImg = false;
				vibrat = false;
				sound = false;
				tmpIV.setVisibility(View.GONE);
				tvtmpTime.setVisibility(View.GONE);
				Constant.flag = true;
				alertMsg.show();
			}

			else {
				cell_pos = 1;
				if (b != -1)
					task = "show";
				Call_actvity();
			}
			break;
		case R.id.rl_four_boxC:
			alert = false;
			if (blink == true) {
				anim.cancel();
				tmpRl.clearAnimation();
				tmpRl.setBackgroundColor(Color.TRANSPARENT);
				btnConfirm.setVisibility(View.GONE);
			}
			if (miniImg == true) {

			}
			if (vibrat == true) {
				CommonMethod.vibrator.cancel();
			}

			if (sound == true) {
				CommonMethod.player.stop();
				btnConfirm.setVisibility(View.GONE);
			}
			if(mTts!=null)if(mTts.isSpeaking())
			{
				mTts.stop();
				mTts.shutdown();
			}
//			if (blink == true || miniImg == true || vibrat == true
//					|| sound == true||fromWhere!=null)
			if (fromWhere != null)
			{
				blink = false;
				miniImg = false;
				vibrat = false;
				sound = false;
				Constant.flag = true;
				tmpIV.setVisibility(View.GONE);
				tvtmpTime.setVisibility(View.GONE);
				alertMsg.show();
			}

			else {
				cell_pos = 2;
				if (c != -1)
					task = "show";
				Call_actvity();
			}
			break;
		case R.id.rl_four_boxD:
			alert = false;
			if (blink == true) {
				anim.cancel();
				tmpRl.clearAnimation();
				tmpRl.setBackgroundColor(Color.TRANSPARENT);
				btnConfirm.setVisibility(View.GONE);
			}
			if (miniImg == true) {

			}
			if (vibrat == true) {
				CommonMethod.vibrator.cancel();
			}

			if (sound == true) {
				CommonMethod.player.stop();
				btnConfirm.setVisibility(View.GONE);
			}
			if(mTts!=null)if(mTts.isSpeaking())
			{
				mTts.stop();
				mTts.shutdown();
			}
//			if (blink == true || miniImg == true || vibrat == true
//					|| sound == true||fromWhere!=null) 
			if (fromWhere != null)
			{
				blink = false;
				miniImg = false;
				vibrat = false;
				sound = false;
				Constant.flag = true;
				tmpIV.setVisibility(View.GONE);
				tvtmpTime.setVisibility(View.GONE);
				alertMsg.show();
			}

			else {
				cell_pos = 3;
				if (d != -1)
					task = "show";
				Call_actvity();
			}
			break;

		case R.id.btn_boxfour_confirm:
			alert = false;
			
			if (blink == true) {
				anim.cancel();
				tmpRl.clearAnimation();
				tmpRl.setBackgroundColor(Color.TRANSPARENT);
				btnConfirm.setVisibility(View.GONE);
			}
			if (miniImg == true) {

			}
			if (vibrat == true) {
	
				CommonMethod.vibrator.cancel();
				btnConfirm.setVisibility(View.GONE);
			}

			if (sound == true) {
				CommonMethod.player.stop();
			}
			if(mTts!=null)if(mTts.isSpeaking())
			{
				mTts.stop();
				mTts.shutdown();
			}
//			if (blink == true || miniImg == true || vibrat == true
//					|| sound == true||fromWhere!=null) 
			if (fromWhere != null)
			{
				btnConfirm.setVisibility(View.GONE);
				blink = false;
				miniImg = false;
				vibrat = false;
				sound = false;
				Constant.flag = true;
				tmpIV.setVisibility(View.GONE);
				tvtmpTime.setVisibility(View.GONE);
				alertMsg.show();
			} else {
				// do nothing.
			}
			
			rlCellA.setClickable(true);
			rlCellB.setClickable(true);
			rlCellC.setClickable(true);
			rlCellD.setClickable(true);
			
			break;
		}

	}

	public void Call_actvity() {
		intent = new Intent(BoxFourActivity.this,
				CellManage_AddShowActivity.class);
		intent.putExtra("CellID", cell_pos);
		intent.putExtra("BoxID", boxid);
		intent.putExtra("UserID", userid);
		intent.putExtra("LoginID", loginid);
		intent.putExtra("Task", task);
		intent.putExtra("FromWhereActivity", "noWhere"); 
		startActivity(intent);
//		finish();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				mTts = new TextToSpeech(this, this);
			} else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	@Override
	public void onInit(int pos) {
		if(alert==false)
			return;
		
		System.out.println("medname~~~~~~~~~~~~" + MedName + ""
				+ "~~~~~~~~" + Desc);
		AudioManager am = (AudioManager) getSystemService(this.AUDIO_SERVICE);
		int amStreamMusicMaxVol = am.getStreamMaxVolume(am.STREAM_MUSIC);
		am.setStreamVolume(am.STREAM_MUSIC, amStreamMusicMaxVol, 15);
		mTts.speak(MedName + ".   " + Desc, TextToSpeech.QUEUE_FLUSH, // Drop
				// all
				// pending
				// entries
				// in
				// the
				// playback queue.
				null);
	}

	@Override
	public void onDestroy() {
		// Don�t forget to shutdown!
		if (mTts != null) {
			mTts.stop();
			mTts.shutdown();
		}
		super.onDestroy();
	}

	public void ReportDetails() {
		
		DateFormat df = new android.text.format.DateFormat();
		String date = df.format("dd/MM/yyyy", new Date()).toString();
		String time = df.format("kk:mm:ss", new Date()).toString();
		String dosage_taken = isConfirm == true ? "Y" : "N";
		int qty = cell.intDos_Mgt;
		int lid = loginid;
		User_Model user = db.getSinglePatientForId(userid).get(0);
		String username = user.name + " " + user.surname;
		String med = MedName;

		// asfas

		Log.i("Login Id", "" + lid);
		Log.i("User name", username);
		Log.i("Med Name", med);
		Log.i("Qty", "" + qty);
		Log.i("Dosage Taken", dosage_taken);
		Log.i("Date Time", date + " " + time);
		
		db.insertReport(lid, username, med, date, time, dosage_taken, qty);
		stop = true ; 
		
		finish();
		
		Intent intent  = new Intent(BoxFourActivity.this, MainActivity.class) ;
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK) ;
		if (Build.VERSION.SDK_INT >= 11 )//11 for Honeycomb
	        intent.addFlags(0x8000);
		startActivity(intent) ;
		
		
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(vibrat==true)
		{
			CommonMethod.vibration(this);
		}
		else
		{
			try{
				CommonMethod.vibrator.cancel();
				}catch (Exception e) {
					// TODO: handle exception
				}
		}
		
		task = "Add";
		cellinfolist = db.getCellInfoForBox(loginid, userid, boxid, -1);
		a = -1;
		b = -1;
		c = -1;
		d = -1;

		tvCellA.setText("A/1");
		tvCellB.setText("B/2");
		tvCellC.setText("C/3");
		tvCellD.setText("D/4");

		tvTimeA.setText("");
		tvTimeB.setText("");
		tvTimeC.setText("");
		tvTimeD.setText("");
		
//		if (cellinfolist.size() > 0) {
//
//			for (int i = 0; i < cellinfolist.size(); i++) {
//				Log.i("Cellid", "--------------" + cellinfolist.get(i).cellid);
//				switch (cellinfolist.get(i).cellid) {
//				case 0:
//					tvCellA
//							.setText(""
//									+ db.getMedical(cellinfolist.get(i).medid)
//											.get(0).nm);
//					a = i;
//					break;
//				case 1:
//					tvCellB
//							.setText(""
//									+ db.getMedical(cellinfolist.get(i).medid)
//											.get(0).nm);
//					b = i;
//					break;
//				case 2:
//					tvCellC
//							.setText(""
//									+ db.getMedical(cellinfolist.get(i).medid)
//											.get(0).nm);
//					c = i;
//					break;
//				case 3:
//					tvCellD
//							.setText(""
//									+ db.getMedical(cellinfolist.get(i).medid)
//											.get(0).nm);
//					d = i;
//					break;
//
//				}
//			}
//		}
		
		
		
//		User_Model arr = db.getPatients(String.valueOf(userid));
//		try{
//		tv.setText("User : " + arr.name + " " + arr.surname);
//		}catch (Exception e) {
//		}
//		tvBoxname.setText(" Box: " + boxid);

		cellinfolist = db.getCellInfoForBox(loginid, userid, boxid, -1);

		if (cellinfolist.size() > 0) {
			a = -1;
			b = -1;
			c = -1;
			d = -1;
			for (int i = 0; i < cellinfolist.size(); i++) {
				Log.i("Cellid", "--------------" + cellinfolist.get(i).cellid);
				switch (cellinfolist.get(i).cellid) {
				case 0:
					tvCellA
							.setText(""
									+ db.getMedical(cellinfolist.get(i).medid)
											.get(0).nm);
					try{
						ArrayList<Notification_Model> tmp = db.getCellNotification(loginid, userid, boxid, 0);
						if(tmp.size()>0)
						{
							tvTimeA.setText(tmp.get(0).strTime);
							tvTimeA.setVisibility(View.VISIBLE);
						}
						}catch (Exception e) {
							// TODO: handle exception
						}
					picid = cellinfolist.get(i).picid;
					a = i;
					break;
				case 1:
					tvCellB
							.setText(""
									+ db.getMedical(cellinfolist.get(i).medid)
											.get(0).nm);
					try{
						ArrayList<Notification_Model> tmp = db.getCellNotification(loginid, userid, boxid, 1);
						if(tmp.size()>0)
						{
							tvTimeB.setText(tmp.get(0).strTime);
							tvTimeB.setVisibility(View.VISIBLE);
						}
						}catch (Exception e) {
							// TODO: handle exception
						}
					picid = cellinfolist.get(i).picid;
					b = i;
					break;
				case 2:
					tvCellC
							.setText(""
									+ db.getMedical(cellinfolist.get(i).medid)
											.get(0).nm);
					try{
						ArrayList<Notification_Model> tmp = db.getCellNotification(loginid, userid, boxid, 2);
						if(tmp.size()>0)
						{
							tvTimeC.setText(tmp.get(0).strTime);
							tvTimeC.setVisibility(View.VISIBLE);
						}
						}catch (Exception e) {
							// TODO: handle exception
						}
					picid = cellinfolist.get(i).picid;
					c = i;
					break;
				case 3:
					tvCellD
							.setText(""
									+ db.getMedical(cellinfolist.get(i).medid)
											.get(0).nm);
					try{
						ArrayList<Notification_Model> tmp = db.getCellNotification(loginid, userid, boxid, 3);
						if(tmp.size()>0)
						{
							tvTimeD.setText(tmp.get(0).strTime);
							tvTimeD.setVisibility(View.VISIBLE);
						}
						}catch (Exception e) {
							// TODO: handle exception
						}
					picid = cellinfolist.get(i).picid;
					d = i;
					break;

				}
			}
		}
		
	}

	public void setColor(int color) {
		switch (cell_pos) {
		case 0:
			rlCellA.setBackgroundColor(color);
			tmpRl = rlCellA;
			tmpIV = imgCellA;
			tvtmpTime = tvTimeA;
			break;
		case 1:
			rlCellB.setBackgroundColor(color);
			tmpRl = rlCellB;
			tmpIV = imgCellB;
			tvtmpTime = tvTimeB;
			break;
		case 2:
			rlCellC.setBackgroundColor(color);
			tmpRl = rlCellC;
			tmpIV = imgCellC;
			tvtmpTime = tvTimeC;
			break;
		case 3:
			rlCellD.setBackgroundColor(color);
			tmpRl = rlCellD;
			tmpIV = imgCellD;
			tvtmpTime = tvTimeD;
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isAlarm && done == false) {
				System.out.println("frist loop~~~~");

				if (Constant.flag == true) {
					boolean finish = true;
					Intent intent = new Intent(BoxFourActivity.this,
							MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					intent.putExtra("Finish", finish);
					startActivity(intent);
					finish();
					Constant.flag = false;
					System.out.println("second loop~~~~");
				} else {
					alertMsg.show();
					Toast.makeText(BoxFourActivity.this,
							getResources().getString(R.string.stop_alarm_msg),
							Toast.LENGTH_LONG).show();
				}
				return false;
			}
		} else {
			System.out.println("outside loop~~~~");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try{
			CommonMethod.vibrator.cancel();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
