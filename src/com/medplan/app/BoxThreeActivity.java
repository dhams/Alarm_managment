package com.medplan.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
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

public class BoxThreeActivity extends Activity implements OnClickListener,
		OnInitListener {

	RelativeLayout titleHeadLayout, rlCellA, rlCellB, rlCellC, tmpRl;
	LinearLayout MainBgLayout;
	TextView tvCellA, tvCellB, tvCellC, tvTimeA, tvTimeB, tvTimeC, tvtmpTime;
	ImageView imgCellA, imgCellB, imgCellC, tmpIV;
	Button btnConfirm;
	Intent intent;
	int cell_pos, boxid, userid, loginid, picid, counter = 1, WaytoStop,
			alarmSound;
	int a = -1, b = -1, c = -1;
	databasehelper db;
	String task = "add", MedName, Desc = "" , activity; 

	ArrayList<CellInfo_Model> cellinfolist = new ArrayList<CellInfo_Model>();
	Picture_Model picmodel;
	Animation anim;
	Handler handler;
	private TextToSpeech mTts;
	boolean blink, miniImg, vibrat, alert, sound, isConfirm, isAlarm, stop;
	private static final int MY_DATA_CHECK_CODE = 1234;
	String textA, textB, textC;
	boolean done = false;

	ImageView headLogo;
	TextView headerTitle;
	CellInfo_Model cell;
	AlertDialog alertMsg;
	public static SharedPreferences SP;
	SimpleDateFormat sdf;
	private String fromWhere;

	private ApplicationClass applicationClass;
	private AlertDialog.Builder alertDialog ;
	String pendingInterntID  ;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		db = new databasehelper(this);
		SP = PreferenceManager
				.getDefaultSharedPreferences(BoxThreeActivity.this);
		intent = getIntent();

		setVolumeControlStream(AudioManager.STREAM_MUSIC);


		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS Z");

	    pendingInterntID = intent.getStringExtra("unique");

		applicationClass = (ApplicationClass) this.getApplication();

		if (intent.hasExtra("BoxID")) {

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
			
			activity = intent.getStringExtra("Activity") ;

		} else {
			boxid = applicationClass.boxId;
			userid = applicationClass.userId;
			loginid = applicationClass.loginId;
			cell_pos = applicationClass.cellPos;
			MedName = applicationClass.medName;
			WaytoStop = applicationClass.wayToStop;
			Desc = applicationClass.desc;
			alarmSound = applicationClass.alarmSound;
//			fromWhere = applicationClass.fromWhere;
		}

		if (((SP.getInt("lock", -1) == 0) && cell_pos != -1)
				|| (SP.getString("isLogin", "") == "no")
				|| (db.isUserExsist(userid) == false)) {
			Log.e("", "*************  finish  ****	**************");
			stop = true;
			finish();
			launchHomeActivity() ;
			return  ;
		} else {
			ArrayList<CellInfo_Model> arrCell = db.getCellInfoForBox(loginid,
					userid, boxid, cell_pos);
			if (arrCell.size() == 0 & fromWhere != null) {
				Log.e("", "*************  finish  ****	**************");
				stop = true;
				finish();
				launchHomeActivity() ;
				return  ;
			} else{
				if (activity!=null)
				Toast.makeText(BoxThreeActivity.this,
						getResources().getString(R.string.cell_edit),
						Toast.LENGTH_LONG).show();
			}
				
		}

		// els e if (SP.getString("isLogin", "")=="no"){
		// stop = true ;
		// finish();
		// }else if (db.isUserExsist(userid)==false){
		// stop = true ;
		// finish();
		// }

		if (pendingInterntID != null){
			Log.d("*********** Pending intent Id =", "Pending intent Id ="
					+ pendingInterntID);
			unlockScren() ;
		}
		Log.d("!!!!!!!!!!!!!! Pending intent Id =", "Pending intent Id ="+SP.getString("BoxTenPid", "")) ;
		if (SP.getString("BoxTenPid", "").equals(pendingInterntID)){
			finish() ;
			return   ;
		}
		
			
		setContentView(R.layout.cell_mgt_three_boxe);

		Constant.flag = false;

		// toast=Toast.makeText(BoxThreeActivity.this,
		// getResources().getString(R.string.cell_edit), Toast.LENGTH_LONG);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		// toast.show();
 
		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.cell_mgt);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.cell_mgt);

		System.out.println("way to stop alarm~~~~~oncreate~~~~~~" + boxid
				+ "~~~~~~~ " + userid + " ~~~~~" + loginid + "~~~~~ "
				+ cell_pos + "~~~~ " + MedName + "~~~~~ " + WaytoStop
				+ "~~~~~ " + Desc);
 
		alertDialog = new AlertDialog.Builder(this);
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

		anim = AnimationUtils.loadAnimation(this, R.anim.blink);
		handler = new Handler();

		rlCellA = (RelativeLayout) findViewById(R.id.rl_three_boxA);
		rlCellB = (RelativeLayout) findViewById(R.id.rl_three_boxB);
		rlCellC = (RelativeLayout) findViewById(R.id.rl_three_boxC);

		tvCellA = (TextView) findViewById(R.id.tv_three_boxA);
		tvCellB = (TextView) findViewById(R.id.tv_three_boxB);
		tvCellC = (TextView) findViewById(R.id.tv_three_boxC);

		tvTimeA = (TextView) findViewById(R.id.tv_three_timeA);
		tvTimeB = (TextView) findViewById(R.id.tv_three_timeB);
		tvTimeC = (TextView) findViewById(R.id.tv_three_timeC);

		imgCellA = (ImageView) findViewById(R.id.img_three_boxA);
		imgCellB = (ImageView) findViewById(R.id.img_three_boxB);
		imgCellC = (ImageView) findViewById(R.id.img_three_boxC);

		btnConfirm = (Button) findViewById(R.id.btn_boxthree_confirm);
		TextView tv = (TextView) findViewById(R.id.tv_userbox_name);
		TextView tvBoxNum = (TextView) findViewById(R.id.tv_userbox_boxname);

		Log.i("", userid + "-----------" + boxid);

		User_Model arr = db.getPatients(String.valueOf(userid));
		try {
			tv.setText("User : " + arr.name + " " + arr.surname);
		} catch (Exception e) {
		}
		tvBoxNum.setText(" Box: " + boxid);

		cellinfolist = db.getCellInfoForBox(loginid, userid, boxid, -1);

		if (cellinfolist.size() > 0) {
			a = -1;
			b = -1;
			c = -1;
			for (int i = 0; i < cellinfolist.size(); i++) {
				Log.i("Cellid", "--------------" + cellinfolist.get(i).cellid);
				switch (cellinfolist.get(i).cellid) {
				case 0:
					tvCellA.setText(""
							+ db.getMedical(cellinfolist.get(i).medid).get(0).nm);
					textA = db.getMedical(cellinfolist.get(i).medid).get(0).nm;
					try {
						ArrayList<Notification_Model> tmp = db
								.getCellNotification(loginid, userid, boxid, 0);
						if (tmp.size() > 0) {
							tvTimeA.setText(tmp.get(0).strTime);
							tvTimeA.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exceptiononCreate
					}

					picid = cellinfolist.get(i).picid;
					a = i;
					break;
				case 1:
					tvCellB.setText(""
							+ db.getMedical(cellinfolist.get(i).medid).get(0).nm);
					textB = db.getMedical(cellinfolist.get(i).medid).get(0).nm;
					try {
						ArrayList<Notification_Model> tmp = db
								.getCellNotification(loginid, userid, boxid, 1);
						if (tmp.size() > 0) {
							tvTimeB.setText(tmp.get(0).strTime);
							tvTimeB.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
					picid = cellinfolist.get(i).picid;
					b = i;
					break;
				case 2:
					tvCellC.setText(""
							+ db.getMedical(cellinfolist.get(i).medid).get(0).nm);
					textC = db.getMedical(cellinfolist.get(i).medid).get(0).nm;

					try {
						ArrayList<Notification_Model> tmp = db
								.getCellNotification(loginid, userid, boxid, 2);
						if (tmp.size() > 0) {
							tvTimeC.setText(tmp.get(0).strTime);
							tvTimeC.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
					picid = cellinfolist.get(i).picid;
					c = i;
					break;
				}
			}
		}

		titleHeadLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		MainBgLayout = (LinearLayout) findViewById(R.id.MainAlarmLayout);

		Log.d("$$$$$$$$$$$", "strcheck" + Constant._StrCheck);

		try {
			SplashScreen.Cmethod.CheckAddShowScreen(Constant._StrCheck,
					titleHeadLayout, MainBgLayout);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(BoxThreeActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});

		// rlCellA.setOnClickListener(this);
		// rlCellB.setOnClickListener(this);
		// rlCellC.setOnClickListener(this);
		// btnConfirm.setOnClickListener(this);

		// DO_DM Need to know when cell is blinking and when button is blinking 

		if (cell_pos != -1 && stop == false) {

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
					setColor(Color.rgb(198, 195, 181));
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
				
				Log.i("TAG===",
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
								+ " "
								+ color
								+ " "
								+ limit);

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
//					Toast.makeText(getApplicationContext(),
//							getString(R.string.tapCellToast), Toast.LENGTH_LONG)
//							.show();
					tmpRl.setAnimation(anim);
					System.out.println("Working~~~~~~1");
				}

				else {
//					Toast.makeText(getApplicationContext(),
//							getString(R.string.tapToast), Toast.LENGTH_LONG)
//							.show();
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

				if (alert == true) {
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
//												CommonMethod.player.stop();
												CommonMethod.releaseSoundPlayer();
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
//												CommonMethod.player.stop();
												CommonMethod.releaseSoundPlayer();
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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
		if (alert == false)
			return;

		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		if (cell_pos != -1) {
			// Don�t forget to shutdown!
			if (mTts != null) {
				mTts.stop();
				mTts.shutdown();
			}
		}
		
		CommonMethod.releaseSoundPlayer() ;
		super.onDestroy();
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
		}
	}

	public void doShowToast(final View v) {
//		if (cell_pos != -1
//				& !(blink == true || miniImg == true || vibrat == true || sound == true))
		// if((blink == false || miniImg == false || vibrat == false|| sound ==
		// true))
			
	if (fromWhere==null)
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

	private class ClickAysnc extends AsyncTask<Integer, Void, Void> {

		ProgressDialog dialog = new ProgressDialog(BoxThreeActivity.this);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
			if (cell_pos != -1) {
				if (CommonMethod.vibrator != null)
					CommonMethod.vibrator.cancel();
				if (WaytoStop == 2) {
					tmpIV.setVisibility(View.GONE);
					tvtmpTime.setVisibility(View.GONE);
					tmpRl.setBackgroundColor(Color.TRANSPARENT);
				}
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dialog.dismiss();
		}

		@Override
		protected Void doInBackground(Integer... params) {

			int viewID = params[0];

			switch (viewID) {
			case R.id.rl_three_boxA:
				if (cell_pos != -1) {
					alert = false;
					tvCellA.setText(textA);

					if (blink == true) {
						anim.cancel();
						tmpRl.clearAnimation();
						btnConfirm.setVisibility(View.GONE);

					}
					if (miniImg == true) {

					}
					if (vibrat == true) {
						CommonMethod.vibrator.cancel();

					}
					if (sound == true) {
//						CommonMethod.player.stop();
						CommonMethod.releaseSoundPlayer();
						btnConfirm.setVisibility(View.GONE);

					}
					if (mTts != null)
						if (mTts.isSpeaking()) {
							mTts.stop();
							mTts.shutdown();
						}
					if (blink == true || miniImg == true || vibrat == true
							|| sound == true || alert == true) {
						blink = false;
						miniImg = false;
						vibrat = false;
						sound = false;
						alert = false;
						Constant.flag = true;
						tmpIV.setVisibility(View.GONE);
						tvtmpTime.setVisibility(View.GONE);
						alertMsg.show();
					}
				} else {
					cell_pos = 0;
					if (a != -1)
						task = "show";
					Call_actvity();
				}

				break;
			case R.id.rl_three_boxB:
				if (cell_pos != -1) {
					alert = false;
					tvCellB.setText(textB);
					if (blink == true) {
						anim.cancel();
						tmpRl.clearAnimation();
						btnConfirm.setVisibility(View.GONE);

					}
					if (miniImg == true) {

					}
					if (vibrat == true) {
						CommonMethod.vibrator.cancel();
					}
					// if(alert){
					// mTts.stop();
					// mTts.shutdown();
					// alert=false;
					// }
					if (sound == true) {
//						CommonMethod.player.stop();
						CommonMethod.releaseSoundPlayer();
						btnConfirm.setVisibility(View.GONE);
					}
					if (mTts != null)
						if (mTts.isSpeaking()) {
							mTts.stop();
							mTts.shutdown();
						}
					if (blink == true || miniImg == true || vibrat == true
							|| sound == true) {
						blink = false;
						miniImg = false;
						vibrat = false;
						sound = false;
						Constant.flag = true;
						tmpIV.setVisibility(View.GONE);
						tvtmpTime.setVisibility(View.GONE);
						alertMsg.show();
					}
				} else {
					cell_pos = 1;
					if (b != -1)
						task = "show";
					Call_actvity();
				}
				break;
			case R.id.rl_three_boxC:
				if (cell_pos != -1) {
					alert = false;
					tvCellC.setText(textC);
					if (blink == true) {
						anim.cancel();
						tmpRl.clearAnimation();
						btnConfirm.setVisibility(View.GONE);

					}
					if (miniImg == true) {

					}
					if (vibrat == true) {
						CommonMethod.vibrator.cancel();
						vibrat = false;
					}
					if (sound == true) {
//						CommonMethod.player.stop();
						CommonMethod.releaseSoundPlayer();
						btnConfirm.setVisibility(View.GONE);
					}
					if (mTts != null)
						if (mTts.isSpeaking()) {
							mTts.stop();
							mTts.shutdown();
						}

					if (blink == true || miniImg == true || vibrat == true
							|| sound == true) {
						blink = false;
						miniImg = false;
						vibrat = false;
						sound = false;
						tmpIV.setVisibility(View.GONE);
						tvtmpTime.setVisibility(View.GONE);
						Constant.flag = true;
						alertMsg.show();
					}
				} else {
					cell_pos = 2;
					if (c != -1)
						task = "show";
					Call_actvity();
				}
				break;
			case R.id.btn_boxthree_confirm:
				alert = false;
				rlCellA.setClickable(true);
				rlCellB.setClickable(true);
				rlCellC.setClickable(true);

				if (blink == true) {
					anim.cancel();

					tmpRl.clearAnimation();
					tmpRl.setBackgroundColor(Color.TRANSPARENT);
				}
				if (miniImg == true) {

				}
				if (vibrat == true) {
					CommonMethod.vibrator.cancel();
				}
				if (sound == true) {
//					CommonMethod.player.stop();
					CommonMethod.releaseSoundPlayer();

				}
				if (mTts != null)
					if (mTts.isSpeaking()) {
						mTts.stop();
						mTts.shutdown();
					}
				if (blink == true || miniImg == true || vibrat == true
						|| sound == true) {
					btnConfirm.setVisibility(View.GONE);
					blink = false;
					miniImg = false;
					vibrat = false;
					sound = false;
					Constant.flag = true;
					alertMsg.show();
				} else {
					// do nothing.
				}
				tmpIV.setVisibility(View.GONE);
				tvtmpTime.setVisibility(View.GONE);
				break;
			}

			return null;
		}

	}

	void doClick(View v) {
		if (cell_pos != -1) {
			if (CommonMethod.vibrator != null)
				CommonMethod.vibrator.cancel();
			if (WaytoStop == 2) {
				try {
					tmpIV.setVisibility(View.GONE);
					tvtmpTime.setVisibility(View.GONE);
					tmpRl.setBackgroundColor(Color.TRANSPARENT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		switch (v.getId()) {
		case R.id.rl_three_boxA:
			if (cell_pos != -1) {
				alert = false;
				tvCellA.setText(textA);

				if (blink == true) {
					anim.cancel();
					tmpRl.clearAnimation();
					btnConfirm.setVisibility(View.GONE);

				}
				if (miniImg == true) {

				}
				if (vibrat == true) {
					CommonMethod.vibrator.cancel();

				}
				if (sound == true) {
//					CommonMethod.player.stop();
					btnConfirm.setVisibility(View.GONE);

				}
				if (mTts != null)
					if (mTts.isSpeaking()) {
						mTts.stop();
						mTts.shutdown();
					}
//				if (blink == true || miniImg == true || vibrat == true
//						|| sound == true || alert == true || fromWhere != null)
				
				
					if (fromWhere != null )
				{
					blink = false;
					miniImg = false;
					vibrat = false;
					sound = false;
					alert = false;
					Constant.flag = true;
					tmpIV.setVisibility(View.GONE);
					tvtmpTime.setVisibility(View.GONE);
					alertMsg.show();
				} else {
					cell_pos = 0;
					if (a != -1)
						task = "show";
					Call_actvity();
				}
			} else {
				cell_pos = 0;
				if (a != -1)
					task = "show";
				Call_actvity();
			}
			break;
		case R.id.rl_three_boxB:
			if (cell_pos != -1) {
				alert = false;
				tvCellB.setText(textB);
				if (blink == true) {
					anim.cancel();
					tmpRl.clearAnimation();
					btnConfirm.setVisibility(View.GONE);

				}
				if (miniImg == true) {

				}
				if (vibrat == true) {
					CommonMethod.vibrator.cancel();
				}
				// if(alert){
				// mTts.stop();
				// mTts.shutdown();
				// alert=false;
				// }
				if (sound == true) {
//					CommonMethod.player.stop();
					CommonMethod.releaseSoundPlayer();
					btnConfirm.setVisibility(View.GONE);
				}
				if (mTts != null)
					if (mTts.isSpeaking()) {
						mTts.stop();
						mTts.shutdown();
					}
//				if (blink == true || miniImg == true || vibrat == true
//						|| sound == true || fromWhere != null) {
				if (fromWhere != null) {
					blink = false;
					miniImg = false;
					vibrat = false;
					sound = false;
					Constant.flag = true;
					tmpIV.setVisibility(View.GONE);
					tvtmpTime.setVisibility(View.GONE);
					alertMsg.show();
				} else {
					cell_pos = 1;
					if (b != -1)
						task = "show";
					Call_actvity();
				}
			} else {
				cell_pos = 1;
				if (b != -1)
					task = "show";
				Call_actvity();
			}
			break;
		case R.id.rl_three_boxC:
			if (cell_pos != -1) {
				alert = false;
				tvCellC.setText(textC);
				if (blink == true) {
					anim.cancel();
					tmpRl.clearAnimation();
					btnConfirm.setVisibility(View.GONE);

				}
				if (miniImg == true) {

				}
				if (vibrat == true) {
					CommonMethod.vibrator.cancel();
					vibrat = false;
				}
				if (sound == true) {
//					CommonMethod.player.stop();
					CommonMethod.releaseSoundPlayer();
					btnConfirm.setVisibility(View.GONE);
				}
				if (mTts != null)
					if (mTts.isSpeaking()) {
						mTts.stop();
						mTts.shutdown();
					}

//				if (blink == true || miniImg == true || vibrat == true
//						|| sound == true || fromWhere != null) {
				if (fromWhere != null) {
					blink = false;
					miniImg = false;
					vibrat = false;
					sound = false;
					tmpIV.setVisibility(View.GONE);
					tvtmpTime.setVisibility(View.GONE);
					Constant.flag = true;
					alertMsg.show();
				} else {
					cell_pos = 2;
					if (c != -1)
						task = "show";
					Call_actvity();
				}
			} else {
				cell_pos = 2;
				if (c != -1)
					task = "show";
				Call_actvity();
			}
			break;
		case R.id.btn_boxthree_confirm:
			alert = false;
			rlCellA.setClickable(true);
			rlCellB.setClickable(true);
			rlCellC.setClickable(true);

			if (blink == true) {
				anim.cancel();

				tmpRl.clearAnimation();
				tmpRl.setBackgroundColor(Color.TRANSPARENT);
			}
			if (miniImg == true) {

			}
			if (vibrat == true) {
				CommonMethod.vibrator.cancel();
			}
			if (sound == true) {
//				CommonMethod.player.stop();
				CommonMethod.releaseSoundPlayer();

			}
			if (mTts != null)
				if (mTts.isSpeaking()) {
					mTts.stop();
					mTts.shutdown();
				}
			if ( fromWhere != null) {
				btnConfirm.setVisibility(View.GONE);
				blink = false;
				miniImg = false;
				vibrat = false;
				sound = false;
				Constant.flag = true;
				alertMsg.show();
			} else {
				// do nothing.
			}
			tmpIV.setVisibility(View.GONE);
			tvtmpTime.setVisibility(View.GONE);
			break;
		}

	}

	// @Override
	// public void onClick(View v) {
	//
	// if (cell_pos != -1) {
	// if (CommonMethod.vibrator != null)
	// CommonMethod.vibrator.cancel();
	// if (WaytoStop == 2) {
	// tmpIV.setVisibility(View.GONE);
	// tvtmpTime.setVisibility(View.GONE);
	// tmpRl.setBackgroundColor(Color.TRANSPARENT);
	// }
	// }
	//
	// switch (v.getId()) {
	// case R.id.rl_three_boxA:
	// if (cell_pos != -1) {
	// alert = false;
	// tvCellA.setText(textA);
	//
	// if (blink == true) {
	// anim.cancel();
	// tmpRl.clearAnimation();
	// btnConfirm.setVisibility(View.GONE);
	//
	// }
	// if (miniImg == true) {
	//
	// }
	// if (vibrat == true) {
	// CommonMethod.vibrator.cancel();
	//
	// }
	// if (sound == true) {
	// CommonMethod.player.stop();
	// btnConfirm.setVisibility(View.GONE);
	//
	// }
	// if (mTts != null)
	// if (mTts.isSpeaking()) {
	// mTts.stop();
	// mTts.shutdown();
	// }
	// if (blink == true || miniImg == true || vibrat == true
	// || sound == true || alert == true) {
	// blink = false;
	// miniImg = false;
	// vibrat = false;
	// sound = false;
	// alert = false;
	// Constant.flag = true;
	// tmpIV.setVisibility(View.GONE);
	// tvtmpTime.setVisibility(View.GONE);
	// alertMsg.show();
	// }
	// } else {
	// cell_pos = 0;
	// if (a != -1)
	// task = "show";
	//
	// }
	//
	// break;
	// case R.id.rl_three_boxB:
	// if (cell_pos!=-1) {
	// alert = false;
	// tvCellB.setText(textB);
	// if (blink == true) {
	// anim.cancel();
	// tmpRl.clearAnimation();
	// btnConfirm.setVisibility(View.GONE);
	//
	//
	// }
	// if (miniImg == true) {
	//
	// }
	// if (vibrat == true) {
	// CommonMethod.vibrator.cancel();
	//
	// }
	// // if(alert){
	// // mTts.stop();
	// // mTts.shutdown();
	// // alert=false;
	// // }
	// if (sound == true) {
	// CommonMethod.player.stop();
	// btnConfirm.setVisibility(View.GONE);
	// }
	// if(mTts!=null)
	// if(mTts.isSpeaking())
	// {
	// mTts.stop();
	// mTts.shutdown();
	// }
	// if (blink == true || miniImg == true || vibrat == true
	// || sound == true) {
	// blink = false;
	// miniImg = false;
	// vibrat = false;
	// sound = false;
	// Constant.flag = true;
	// tmpIV.setVisibility(View.GONE);
	// tvtmpTime.setVisibility(View.GONE);
	// alertMsg.show();
	// }
	// }
	// else {
	// cell_pos = 1;
	// if (b != -1)
	// task = "show";
	//
	// }
	// break;
	// case R.id.rl_three_boxC:
	// if (cell_pos != -1) {
	// alert = false;
	// tvCellC.setText(textC);
	// if (blink == true) {
	// anim.cancel();
	// tmpRl.clearAnimation();
	// btnConfirm.setVisibility(View.GONE);
	//
	// }
	// if (miniImg == true) {
	//
	// }
	// if (vibrat == true) {
	// CommonMethod.vibrator.cancel();
	// vibrat = false;
	// }
	// if (sound == true) {
	// CommonMethod.player.stop();
	// btnConfirm.setVisibility(View.GONE);
	//
	// }
	// if (mTts != null)
	// if (mTts.isSpeaking()) {
	// mTts.stop();
	// mTts.shutdown();
	// }
	// if (blink == true || miniImg == true || vibrat == true
	// || sound == true) {
	// blink = false;
	// miniImg = false;
	// vibrat = false;
	// sound = false;
	// tmpIV.setVisibility(View.GONE);
	// tvtmpTime.setVisibility(View.GONE);
	// Constant.flag = true;
	// alertMsg.show();
	// }
	// } else {
	// cell_pos = 2;
	// if (c != -1)
	// task = "show";
	//
	//
	// }
	// break;
	// case R.id.btn_boxthree_confirm:
	// alert = false;
	// rlCellA.setClickable(true);
	// rlCellB.setClickable(true);
	// rlCellC.setClickable(true);
	//
	// if (blink == true) {
	// anim.cancel();
	//
	// tmpRl.clearAnimation();
	// tmpRl.setBackgroundColor(Color.TRANSPARENT);
	// }
	// if (miniImg == true) {
	//
	// }
	// if (vibrat == true) {
	// CommonMethod.vibrator.cancel();
	// }
	// if (sound == true) {
	// CommonMethod.player.stop();
	//
	// }
	// if (mTts != null)
	// if (mTts.isSpeaking()) {
	// mTts.stop();
	// mTts.shutdown();
	// }
	// if (blink == true || miniImg == true || vibrat == true
	// || sound == true) {
	// btnConfirm.setVisibility(View.GONE);
	// blink = false;
	// miniImg = false;
	// vibrat = false;
	// sound = false;
	// Constant.flag = true;
	// alertMsg.show();
	// } else {
	// // do nothing.
	// }
	// tmpIV.setVisibility(View.GONE);
	// tvtmpTime.setVisibility(View.GONE);
	// break;
	// }
	// if(task=="show")
	// {
	// Call_actvity();
	// }
	// }

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
		
		Log.i("Login Id", "" + lid);
		Log.i("User name", username);
		Log.i("Med Name", med);
		Log.i("Qty", "" + qty);
		Log.i("Dosage Taken", dosage_taken);
		Log.i("Date Time", date + " " + time);

		db.insertReport(lid, username, med, date, time, dosage_taken, qty);
		BoxThreeActivity.this.finish();
		
//		Intent intent  = new Intent(BoxThreeActivity.this, MainActivity.class) ;
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK) ;
//		if (Build.VERSION.SDK_INT >= 11 )//11 for Honeycomb
//	        intent.addFlags(0x8000);
//		startActivity(intent) ;
		
		SP.edit().putString("BoxTenPid", pendingInterntID).commit();
	}
	
	public void Call_actvity() {
		intent = new Intent(BoxThreeActivity.this,
				CellManage_AddShowActivity.class);
		intent.putExtra("CellID", cell_pos);
		intent.putExtra("BoxID", boxid);
		intent.putExtra("UserID", userid);
		intent.putExtra("LoginID", loginid);
		intent.putExtra("Task", task);
		startActivity(intent);
		// this.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.e("BOXTHREE", " ************ Timer Actived ******** ");

		rlCellA.setOnClickListener(this);
		rlCellB.setOnClickListener(this);
		rlCellC.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);

		if (vibrat == true) {
			CommonMethod.vibration(this);
		} else {
			try {
				CommonMethod.vibrator.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		task = "Add";
		cellinfolist = db.getCellInfoForBox(loginid, userid, boxid, -1);
		a = -1;
		b = -1;
		c = -1;
		tvCellA.setText("A/1");
		tvCellB.setText("B/2");
		tvCellC.setText("C/3");
		
		tvTimeA.setText("");
		tvTimeB.setText("");
		tvTimeC.setText("");
		
		if (cellinfolist.size() > 0) {

			for (int i = 0; i < cellinfolist.size(); i++) {
				Log.i("Cellid", "--------------" + cellinfolist.get(i).cellid);
				switch (cellinfolist.get(i).cellid) {
				case 0:
					tvCellA.setText(""
							+ db.getMedical(cellinfolist.get(i).medid).get(0).nm);
					textA = db.getMedical(cellinfolist.get(i).medid).get(0).nm;
					try {
						ArrayList<Notification_Model> tmp = db
								.getCellNotification(loginid, userid, boxid, 0);
						if (tmp.size() > 0) {
							tvTimeA.setText(tmp.get(0).strTime);
							tvTimeA.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
					picid = cellinfolist.get(i).picid;
					a = i;
					break;
				case 1:
					tvCellB.setText(""
							+ db.getMedical(cellinfolist.get(i).medid).get(0).nm);
					textB = db.getMedical(cellinfolist.get(i).medid).get(0).nm;
					try {
						ArrayList<Notification_Model> tmp = db
								.getCellNotification(loginid, userid, boxid, 1);
						if (tmp.size() > 0) {
							tvTimeB.setText(tmp.get(0).strTime);
							tvTimeB.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
					picid = cellinfolist.get(i).picid;
					b = i;
					break;
				case 2:
					tvCellC.setText(""
							+ db.getMedical(cellinfolist.get(i).medid).get(0).nm);
					textC = db.getMedical(cellinfolist.get(i).medid).get(0).nm;

					try {
						ArrayList<Notification_Model> tmp = db
								.getCellNotification(loginid, userid, boxid, 2);
						if (tmp.size() > 0) {
							tvTimeC.setText(tmp.get(0).strTime);
							tvTimeC.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}

					picid = cellinfolist.get(i).picid;
					c = i;
					break;
				}
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (cell_pos != -1) {

			try {
				CommonMethod.vibrator.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

//		CommonMethod.releaseSoundPlayer();
	}
	
	 
	private void  launchHomeActivity(){
		
		    Intent startMain = new Intent(Intent.ACTION_MAIN);
	        startMain.addCategory(Intent.CATEGORY_HOME);
	        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(startMain);
	}

	private  void unlockScren(){
		Window wind;
		wind = this.getWindow();
		wind.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
		wind.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		wind.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isAlarm ) {
				alertMsg.show();
				Toast.makeText(BoxThreeActivity.this,
						getResources().getString(R.string.stop_alarm_msg),
						Toast.LENGTH_LONG).show();
				System.out.println("frist loop~~~~");

			}else{
					boolean finish = true;
					Intent intent = new Intent(BoxThreeActivity.this,
							MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("Finish", finish);
					startActivity(intent);
					finish();
					Constant.flag = false;
					System.out.println("second loop~~~~");
			}
		} 
		return super.onKeyDown(keyCode, event);

	}

	@Override
	public void onClick(View v) {
		// final ProgressDialog dialog = new
		// ProgressDialog(getApplicationContext());
		// dialog.show();

		doShowToast(v);
		int viewId = v.getId();
		// new ClickAysnc().execute(viewId);

		// Thread thread
		// = new Thread( new Runnable() {
		// public void run() {
		//
		// }
		// }) ;

		// thread.start() ;
	}
	
	class AlertDialgo extends AlertDialog.Builder{

		public AlertDialgo(Context arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}
		
	}
}
