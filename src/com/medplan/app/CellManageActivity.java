package com.medplan.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medpan.util.CellInfo_Model;
import com.medpan.util.Constant;
import com.medpan.util.PendingAlarmUtil;
import com.medplan.db.databasehelper;

public class CellManageActivity extends Activity implements OnClickListener {
	Button prev, next, activate;
	TextView tvBox, tvAlarm, tvUName;
	ImageView ivBox;
	String boxName = "Box 3", userSel, task, cellStatus = "";
	RelativeLayout rlCell, rlAlarm;
	ImageView headLogo;
	TextView headerTitle;
	int count = 0, box_pos;;
	RelativeLayout titleHeadLayout;
	// public SharedPreferences SP;
	public static int user_id, login_id, box_id = 3, box_id_choosed = 3,
			getCell_id;
	public static String user_name = "";
	databasehelper db;
	AlertDialog.Builder cellDialog;
	AlertDialog cellMessage;
	boolean cellFlag, isActivated, flg = false, flagCount;
	Intent intent;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.cell_screen);
		

		db = new databasehelper(this);
 
		WebView vv = new WebView(this);
		String message1 = String.format(getString(R.string.box_selection_cell_mgt_line1));
		String message2 = String.format(getString(R.string.box_selection_cell_mgt_line2));
		String message3 = String.format(getString(R.string.box_selection_cell_mgt_line3));
		String message4 = String.format(getString(R.string.box_selection_cell_mgt_line4));
		vv.loadData("<font color='white'><ol><li>"+message1+"</li><li>"+message2+"</li><li>"+message3+"</li><li>"+message4+"</li></ol></font>", "text/html", "utf-8");
		vv.setBackgroundColor(Color.TRANSPARENT);
	
		login_id = PreferenceManager.getDefaultSharedPreferences(this).getInt(
				"UserID", 0);

		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(CellManageActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});

		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.cell_mgt);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.cell_mgt);

		titleHeadLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		
		try {
			SplashScreen.Cmethod.CellScreen(Constant._StrCheck, titleHeadLayout);
		} catch (Exception e) {
			e.printStackTrace();
		}

		prev = (Button) findViewById(R.id.btnPrev);
		next = (Button) findViewById(R.id.btnNext);
		activate = (Button) findViewById(R.id.btnActivate);
		tvBox = (TextView) findViewById(R.id.tvBox);
		tvUName = (TextView) findViewById(R.id.tvUName);
		// tvAlarm = (TextView) findViewById(R.id.tvAlarm);
		ivBox = (ImageView) findViewById(R.id.ivCell);
		 
//		rlCell = (RelativeLayout) findViewById(R.id.rlCell);
		// rlAlarm = (RelativeLayout) findViewById(R.id.rlAlarm);
		//
		// SP = PreferenceManager
		// .getDefaultSharedPreferences(getApplicationContext());
		// Editor edit = SP.edit();
		// edit.putInt("Cell_id", db.getCell_id());
		// edit.commit();

		if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
			cell(R.drawable.box_three_org, "Box 3");
		} else if (Constant._StrCheck.equalsIgnoreCase("Doctor/Physician")) {
			cell(R.drawable.box_three_green, "Box 3");
		} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
			cell(R.drawable.box_three_blue, "Box 3");
		}

		ivBox.setOnClickListener(this);
		prev.setOnClickListener(this);
		next.setOnClickListener(this);
		activate.setOnClickListener(this);
		task = this.getIntent().getStringExtra("Task");

		// if (task.endsWith("Cell")) {
		// rlCell.setVisibility(View.VISIBLE);

		AlertDialog.Builder alert = new AlertDialog.Builder(
				CellManageActivity.this);
		alert.setTitle(R.string.help);
		//alert.setMessage(Html.fromHtml("<ol><li> Select User/Patient to link  to the Box.</li> <li> Select the BOX type you want to use. </li> <li> Link the right BOX using  activate button.</li><li> Assign Meds/Treats and alarms to the Box cells.</li></ol>"));
		
		alert.setView(vv);
		alert.setPositiveButton(R.string.ok_thanks,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						Intent intent = new Intent(CellManageActivity.this,
								CellUser_ListActivity.class);
						intent.putExtra("Task", "pick");
						startActivityForResult(intent, 999);
					}
				});
		alert.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						CellManageActivity.this.finish();
					}
				});

		cellDialog = new AlertDialog.Builder(CellManageActivity.this);
		cellDialog.setTitle(R.string.help);
		cellDialog.setPositiveButton(R.string.yeah_sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						_CellSelection();

					}
				});

		cellDialog.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		cellMessage = cellDialog.create();
		
		user_id = this.getIntent().getIntExtra("idtoshow", 0);
		if (user_id != 0) {
			user_name = this.getIntent().getStringExtra("name");
			setIdName();
		} else {
			alert.show();
		}
		
		box_pos= this.getIntent().getIntExtra("position", 0);
		flagCount= this.getIntent().getBooleanExtra("SelectedBox", false);
		
		System.out.println("box count~~~~~~~~~"+box_pos+"count"+count+"selectedBox"+flagCount);
		
		if(flagCount){
			
			box_id_choosed = db.isActivated(login_id, user_id);
			box_id=box_pos;
			
			if (box_id == box_id_choosed) {
				activate.setText(getResources().getString(R.string.edit_cell));
				activate.setBackgroundResource(R.drawable.button_edit);
			} else {
				activate.setText(getResources().getString(R.string.activate));
				activate.setBackgroundResource(R.drawable.button_activate);
			}
			
			
			//
			if(box_pos==3){
				count=0;
			}
			if(box_pos==4){
				count=1;
			}
			
			if(box_pos==6){
				count=2;
			}
			if(box_pos==8){
				count=3;
			}
			if(box_pos==10){
				count=4;
			}
			
				if (count == 0) {
				box_id = 3;
				if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
					cell(R.drawable.box_three_org, "Box 3");
				} else if (Constant._StrCheck.equalsIgnoreCase("Doctor/Physician")) {
					cell(R.drawable.box_three_green, "Box 3");
				} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
					cell(R.drawable.box_three_blue, "Box 3");
				}

			} else if (count == 1) {
				box_id = 4;
				if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
					cell(R.drawable.box_four_org, "Box 4");
				} else if (Constant._StrCheck
						.equalsIgnoreCase("Doctor/Physician")) {
					cell(R.drawable.box_four_green, "Box 4");
				} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
					cell(R.drawable.box_four_blue, "Box 4");
				}

			} else if (count == 2) {
				box_id = 6;
				if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
					cell(R.drawable.box_six_org, "Box 6");
				} else if (Constant._StrCheck
						.equalsIgnoreCase("Doctor/Physician")) {
					cell(R.drawable.box_six_green, "Box 6");
				} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
					cell(R.drawable.box_six_blue, "Box 6");
				}
			} else if (count == 3) {
				box_id = 8;
				if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
					cell(R.drawable.box_eight_org, "Box 8");
				} else if (Constant._StrCheck
						.equalsIgnoreCase("Doctor/Physician")) {
					cell(R.drawable.box_eight_green, "Box 8");
				} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
					cell(R.drawable.box_eight_blue, "Box 8");
				}
			}
			else if (count == 4) {
				box_id = 10;
				if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
					cell(R.drawable.box_ten_org, "Box 10");
				} else if (Constant._StrCheck
						.equalsIgnoreCase("Doctor/Physician")) {
					cell(R.drawable.box_ten_green, "Box 10");
				} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
					cell(R.drawable.box_ten_blue, "Box 10");
				}
			}
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		user_id = data.getIntExtra("idtoshow", 0);
		user_name = data.getStringExtra("name");
		setIdName();

	}

	void setIdName() {
		if (user_id == 0) {
			// Toast.makeText(getApplicationContext(),
			// "Please First add User in User Management",
			// Toast.LENGTH_LONG).show();
			finish();
		}

		tvUName.setText(user_name);
		box_id_choosed = db.isActivated(login_id, user_id);
		if (box_id_choosed == 0) {
			box_id = 3;
			isActivated = false;
		} else {

			isActivated = true;
			Toast.makeText(
					CellManageActivity.this,
					getResources().getString(R.string.user_cell_mgt)
							+ " "
							+ " "
							+ user_name
							+ " "
							+ getResources().getString(
									R.string.already_exist_cell_mgt) + " "
							+ box_id, Toast.LENGTH_LONG).show();
			box_id = 3;
		}
		if (box_id == box_id_choosed) {
			activate.setText(getResources().getString(R.string.edit_cell));
			activate.setBackgroundResource(R.drawable.button_edit);
		} else {
			activate.setText(getResources().getString(R.string.activate));
			activate.setBackgroundResource(R.drawable.button_activate);
		}
		Log.i("log", user_id + "------------" + user_name);
	}

	public void cell(int id, String name) {
		tvBox.setText(name);
		ivBox.setBackgroundResource(id);
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {

		case R.id.btnPrev:
			if (count <= 0) {
				Toast.makeText(CellManageActivity.this,
						getResources().getString(R.string.first_box_cell_mgt),
						Toast.LENGTH_SHORT).show();
				break;
			} else if (count == 1) {
				box_id = 3;
				if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
					cell(R.drawable.box_three_org, "Box 3");
				} else if (Constant._StrCheck
						.equalsIgnoreCase("Doctor/Physician")) {
					cell(R.drawable.box_three_green, "Box 3");
				} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
					cell(R.drawable.box_three_blue, "Box 3");
				}

			} else if (count == 2) {
				box_id = 4;
				if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
					cell(R.drawable.box_four_org, "Box 4");
				} else if (Constant._StrCheck
						.equalsIgnoreCase("Doctor/Physician")) {
					cell(R.drawable.box_four_green, "Box 4");
				} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
					cell(R.drawable.box_four_blue, "Box 4");
				}

			} else if (count == 3) {
				box_id = 6;
				if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
					cell(R.drawable.box_six_org, "Box 6");
				} else if (Constant._StrCheck
						.equalsIgnoreCase("Doctor/Physician")) {
					cell(R.drawable.box_six_green, "Box 6");
				} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
					cell(R.drawable.box_six_blue, "Box 6");
				}
			} else if (count == 4) {
				box_id = 8;
				if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
					cell(R.drawable.box_eight_org, "Box 8");
				} else if (Constant._StrCheck
						.equalsIgnoreCase("Doctor/Physician")) {
					cell(R.drawable.box_eight_green, "Box 8");
				} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
					cell(R.drawable.box_eight_blue, "Box 8");
				}
			}
			if (db.NullCell(login_id, user_id, box_id)){ 
//			if (box_id == box_id_choosed) {
				activate.setText(getResources().getString(R.string.edit_cell));
				activate.setBackgroundResource(R.drawable.button_edit);
			} else {
				activate.setText(getResources().getString(R.string.activate));
				activate.setBackgroundResource(R.drawable.button_activate);
			}
			count--;
			break;

		case R.id.btnNext:
			if (count == 0) {
				box_id = 4;
				if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
					cell(R.drawable.box_four_org, "Box 4");
				} else if (Constant._StrCheck
						.equalsIgnoreCase("Doctor/Physician")) {
					cell(R.drawable.box_four_green, "Box 4");
				} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
					cell(R.drawable.box_four_blue, "Box 4");
				}
				
				
			} else if (count == 1) {
				box_id = 6;

				if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
					cell(R.drawable.box_six_org, "Box 6");
				} else if (Constant._StrCheck
						.equalsIgnoreCase("Doctor/Physician")) {
					cell(R.drawable.box_six_green, "Box 6");
				} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
					cell(R.drawable.box_six_blue, "Box 6");
				}
			}

			else if (count == 2) {
				box_id = 8;
				if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
					cell(R.drawable.box_eight_org, "Box 8");
				} else if (Constant._StrCheck
						.equalsIgnoreCase("Doctor/Physician")) {
					cell(R.drawable.box_eight_green, "Box 8");
				} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
					cell(R.drawable.box_eight_blue, "Box 8");
				}
			} else if (count == 3) {
				box_id = 10;
				if (Constant._StrCheck.equalsIgnoreCase("User/Patient")) {
					cell(R.drawable.box_ten_org, "Box 10");
				} else if (Constant._StrCheck
						.equalsIgnoreCase("Doctor/Physician")) {
					cell(R.drawable.box_ten_green, "Box 10");
				} else if (Constant._StrCheck.equalsIgnoreCase("Pharma")) {
					cell(R.drawable.box_ten_blue, "Box 10");
				}
			} else if (count > 3) {
				Toast.makeText(CellManageActivity.this,
						getResources().getString(R.string.last_box_cell_mgt),
						Toast.LENGTH_SHORT).show();
				break;
			}
			 
			if (db.NullCell(login_id, user_id, box_id)==true){ 
//			if (box_id == box_id_choosed) {
				activate.setText(getResources().getString(R.string.edit_cell));
				activate.setBackgroundResource(R.drawable.button_edit);
			} 
			else
			{
				
				activate.setText(getResources().getString(R.string.activate));
				activate.setBackgroundResource(R.drawable.button_activate);
			}
			count++;

			break;
		case R.id.ivCell:

			break;
		case R.id.btnActivate:
 
			if (activate.getText().toString().contains("Activate")) {
				
				if (box_id_choosed == 0) {
					cellDialog.setMessage(getResources().getString(
							R.string.connect_box_cell_mgt)
							+ " " 
							+ tvBox.getText()
							+ " "
							+ getResources() 
									.getString(R.string.to_you_cell_mgt)
							+ " "
							+ user_name + "?");
					cellDialog.show();	

				} else if (box_id_choosed == box_id) {
					cellDialog.setMessage(getResources().getString(
							R.string.the_box_cell_mgt)
							+ " "
							+ tvBox.getText()
							+ " " 
							+ getResources().getString(
									R.string.same_choosed_cell_mgt)
							+ " "
							+ user_name);
					cellDialog.show(); 
				}

				else {
					if (box_id_choosed > box_id) {
						cellStatus = "same";
						cellFlag = true;
						cellDialog.setMessage(getResources().getString(
								R.string.pre_choosen_cell_mgt)
								+ " "
								+ box_id_choosed
								+ " "
								+ getResources().getString(
										R.string.pre_choosen_overite_cell_mgt));
						cellDialog.show();
						flg = false;
					} else {
						isActivated = true;
						cellStatus = "same";
						cellFlag = true;
						cellDialog
								.setMessage("This will copy the cells from Box Type "
										+ box_id_choosed
										+ " to Box Type "
										+ box_id);
						cellDialog.show();
						flg = true;
					}
				}
			} else {
				_CellSelection();
			}
			break;
		}
	}

	public void _CellSelection() {
		
		
//		if (box_id!=box_id_choosed){ 
			
//		if (cellFlag==true)
		db.ActivateCell(login_id, user_id, box_id);	
			
//			if (box_id_choosed!=box_id)
			new AlarmRemover().execute(box_id_choosed , box_id);

//		}
		
		// SP =
		// PreferenceManager.getDefaultSharedPreferences(CellManageActivity.this);
		// user_id = SP.getInt("UserID", 0);
//		if (cellFlag == true) {
//			if (isActivated == false) {
//			db.ActivateCell(login_id, user_id, box_id);
//			} 
//			else {
//				if (flg == true) { 
//					ArrayList<CellInfo_Model> arrCell = db.getCellInfoForBox(
//							login_id, user_id, box_id_choosed, -1);
//					
//					Log.i("", "arrCell -- " + arrCell.size());
//					for (int i = 0; i < arrCell.size(); i++) {
//
//						CellInfo_Model cell = arrCell.get(i);
//						ArrayList<Notification_Model> arr = db
//							.getCellNotification(login_id, user_id,
//									box_id, cell.cellid);
//							db.updateNotifcationDead(cell.userid, cell.boxid, cell.cellid, cell.loginid);
//							AlarmManager alarmanager = (AlarmManager) getSystemService(ALARM_SERVICE);
//							Intent myntent = null;
//							
//							ArrayList<Integer> arrIds = db.getDeadIds(cell.userid, cell.boxid, cell.cellid, cell.loginid);
//							for(int p = 0 ; p < arrIds.size() ; p++)
//							{
//								myntent = new Intent(CellManageActivity.this,
//										BoxThreeActivity.class);
//								Log.i("Alarm Removed", "....... "+arrIds.get(p));
//								PendingIntent pendingIntent = PendingIntent.getActivity(
//										CellManageActivity.this, arrIds.get(p), myntent,
//										PendingIntent.FLAG_UPDATE_CURRENT);
//								pendingIntent.cancel();
//								alarmanager.cancel(pendingIntent);
//							}
//						Log.i("", box_id + " from " + cell.boxid);
//						db.insertForm(cell.userid, box_id, cell.cellid,
//								cell.medid, cell.picid, cell.strDesc,
//								cell.intBg, cell.blAlert, cell.blBlink,
//								cell.intSound, cell.intBuzz, cell.blMini,
//								cell.blVibrant, cell.intAlarm, cell.intWay,
//								cell.intConfirm, cell.intDayOf_Int,
//								cell.intDos_Mgt, cell.intMany_Time,
//								cell.intSch_Int, cell.intInt_Day, cell.intWeek,
//								cell.intMonth, cell.loginid,
//								cell.weekdaystring, cell.startdate);
//
//						long repeat = 0;
//						if (cell.intSch_Int == 1) {
//							repeat = AlarmManager.INTERVAL_DAY;
//						} else if (cell.intSch_Int == 2) {
//							repeat = AlarmManager.INTERVAL_DAY * 7;
//						} else if (cell.intSch_Int == 3) {
//							repeat = AlarmManager.INTERVAL_DAY * 30;
//						} else {
//							repeat = AlarmManager.INTERVAL_DAY;
//						}
//						Log.i("Repeat", "" + repeat);
//						ArrayList<Notification_Model> arrN = db
//								.getCellNotification(login_id, user_id,
//										box_id_choosed, cell.cellid);
//						Intent myIntent = null;
//						switch (box_id_choosed) {
//						case 3:
//							myIntent = new Intent(CellManageActivity.this,
//									BoxThreeActivity.class);
//							break;
//						case 4:
//							myIntent = new Intent(CellManageActivity.this,
//									BoxFourActivity.class);
//							break;
//
//						case 6:
//							myIntent = new Intent(CellManageActivity.this,
//									BoxSixActivity.class);
//							break;
//
//						case 8:
//							myIntent = new Intent(CellManageActivity.this,
//									BoxEightActivity.class);
//							break;
//
//						case 10:
//							myIntent = new Intent(CellManageActivity.this,
//									BoxTenActivity.class);
//							break;
//
//						}
//
//						myIntent.putExtra("BoxID", box_id_choosed);
//						myIntent.putExtra("UserID", cell.userid);
//						myIntent.putExtra("LoginID", cell.loginid);
//						myIntent.putExtra("CellID", cell.cellid);
// 
//						myIntent.putExtra("WayToStop", cell.intWay);
//						myIntent.putExtra("Med", db.getMedical(cell.medid).get(0).nm);
//						myIntent.putExtra("Description", db.getMedical(cell.medid).get(0).sdesc);
//						myIntent.putExtra("Sound", cell.intSound);
//
//						Log.i("putExtra", box_id + " " + user_id + " "
//								+ login_id + " " + cell.cellid + " "
//								+ db.getMedical(cell.medid).get(0).nm);
//						
//
//						AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//						Log.i("arr Size ", "" + arr.size());
//						int totalCounter = db.getCellNotification().size();
//						for (int k = 0; k < arr.size(); k++) {
//							String time = arr.get(k).strTime;
//
//							Log.i("time and counter", time + " "
//											+ totalCounter);
//							long tostarttimer = diff(time);
//							// Toast.makeText(getApplicationContext(),
//							// time+"  "+tostarttimer,
//							// Toast.LENGTH_LONG).show();
//							PendingIntent pendingIntent = PendingIntent
//									.getActivity(CellManageActivity.this,
//											totalCounter, myIntent,
//											totalCounter);
//
//							alarmManager.cancel(pendingIntent);
// 
//							Calendar calendar = Calendar.getInstance();
//							calendar
//									.setTimeInMillis(System.currentTimeMillis());
//							calendar.add(Calendar.MILLISECOND,
//									(int) tostarttimer);
//
//							
//							alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//									calendar.getTimeInMillis(), repeat,
//									pendingIntent);
//							db.insertNotification(user_id, box_id_choosed, cell.cellid,
//									login_id, time,totalCounter);
//							totalCounter++;
//						}
//
//					}
//				} else {
//					db.deleteForm(user_id, box_id, login_id);
//				}
//
//				db.ActivateCellUpdate(login_id, user_id, box_id);
//			}
//		} else {
//			db.ActivateCell(login_id, user_id, box_id);
//		}
		
		
//		if (box_id == 3) {
//			intent = new Intent(CellManageActivity.this, BoxThreeActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//		} else if (box_id == 4) {
//			intent = new Intent(CellManageActivity.this, BoxFourActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//		} else if (box_id == 6) {
//			intent = new Intent(CellManageActivity.this, BoxSixActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//		} else if (box_id == 8) {
//			intent = new Intent(CellManageActivity.this, BoxEightActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//		} else if (box_id == 10) {
//			intent = new Intent(CellManageActivity.this, BoxTenActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//		}
//		intent.putExtra("BoxID", box_id);
//		intent.putExtra("UserID", user_id);
//		intent.putExtra("LoginID", login_id);
//		intent.putExtra("Activity", "Hell") ;
////		intent.putExtra(name, cell)
//		startActivity(intent);
//		finish();

	}

	long diff(String time2) {
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
				difference = 86400000 - difference;
			}
		} catch (Exception e) {
		}
		return difference;
	}
	
	class AlarmRemover extends AsyncTask<Integer, Void, Void>{

		private ArrayList<PendingAlarmUtil>  alarmUtilsList ;
		private ArrayList<CellInfo_Model> cellInfoList ;
		private AlarmManager alarmManager ;
    	final String[] items = { "Sunday", "Monday", "Tuesday", "Wednesday",
    			"Thursday", "Friday", "Saturday" };
		
    	
    	ProgressDialog  dialog ;
    	
    	
    	@Override
    	protected void onPreExecute() {
    		
    	
    		dialog = new ProgressDialog(CellManageActivity.this);
    	    dialog.show() ;
    	    
    		super.onPreExecute();
    	}
    	
		@Override
		protected Void doInBackground(Integer... params) {
			
			AlarmManager alarmanager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
			int counter  ;
			int oldBoxId  = params[0];
			int newBoxId  =  params[1];

			String day =null;
			int dayCount = 1  ;
			int n ; 
			
			if (newBoxId==0)
				return null ;
			if (newBoxId==oldBoxId)
				return null ; 
			
			alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			
			n= newBoxId>oldBoxId?oldBoxId:newBoxId; 
					 
			cellInfoList = db.getCellInfoForBox(login_id, user_id, oldBoxId, -1);
			
			counter = db.getLastNotificationId() ;	
			 
			if (newBoxId>oldBoxId){
				
				for (int i = 0; i < cellInfoList.size(); i++) {
					db.updateBox(user_id, cellInfoList.get(i).cellid, newBoxId, login_id) ;
				}
			}
			else{
				for (int i = 0; i < cellInfoList.size(); i++) {
					if ((cellInfoList.get(i).cellid+1)>n)
					{
					   db.deleteCell(user_id,cellInfoList.get(i).cellid , oldBoxId, login_id) ;
					}
					else 
					db.updateBox(user_id, cellInfoList.get(i).cellid, newBoxId, login_id) ;
				}
			}
			
//			alarmUtilsList = new ArrayList<PendingAlarmUtil>();
			 
			alarmUtilsList = new ArrayList<PendingAlarmUtil>();
			alarmUtilsList =	db.getDeadAlarm(login_id, user_id, newBoxId , oldBoxId) ;
			
			
			if (alarmUtilsList.size()!=0)
			
			{
				Intent oldIntent = null ;
				Intent newIntent  = null  ;
				PendingIntent oldPendingIntent ;
				PendingIntent newPendingIntent ;
				
				switch (oldBoxId) {
				case 3:
					oldIntent = new Intent(CellManageActivity.this,
							BoxThreeActivity.class);
					break;
				case 4:
					oldIntent = new Intent(CellManageActivity.this,
							BoxFourActivity.class);
					break;

				case 6:	
					oldIntent = new Intent(CellManageActivity.this	,
							BoxSixActivity.class);
					break;

				case 8:	
					oldIntent = new Intent(CellManageActivity.this,
							BoxEightActivity.class);
					break;

				case 10:
					oldIntent = new Intent(CellManageActivity.this,
							BoxTenActivity.class);
					break;
					
				}
				
				switch (newBoxId) {
				case 3:
					newIntent = new Intent(CellManageActivity.this,
							BoxThreeActivity.class);
					break;
				case 4:
					newIntent = new Intent(CellManageActivity.this,
							BoxFourActivity.class);
					break;

				case 6:	
					newIntent = new Intent(CellManageActivity.this	,
							BoxSixActivity.class);
					break;

				case 8:	
					newIntent = new Intent(CellManageActivity.this,
							BoxEightActivity.class);
					break;

				case 10:
					newIntent = new Intent(CellManageActivity.this,
							BoxTenActivity.class);
					break;
					
//					alarmUtilsList.get(i).time ;
					
				}
				
			
				
				long repeat = 0 ;
				String interval  ;
				 
				Calendar calendar = null ;
				calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
				Calendar calendar2 = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
				
//				int counter =alarmUtilsList.get(alarmUtilsList.size()-1).notificationId;
	            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy") ;
	            Date date = null;
	            	
				for(int i=0 ; i <alarmUtilsList.size(); i++){
					counter++ ;
					
					Log.d("removed alarm", "box id ="+oldBoxId+"and unique id ="+alarmUtilsList.get(i).notificationId) ;
					
		            oldPendingIntent = PendingIntent.getActivity(
							CellManageActivity.this, alarmUtilsList.get(i).notificationId,
							oldIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					alarmanager.cancel(oldPendingIntent);
					oldPendingIntent.cancel();
					  
					newIntent.putExtra("unique", counter) ;
					newIntent.putExtra("BoxID", box_id);
					newIntent.putExtra("UserID", user_id);
					newIntent.putExtra("LoginID", login_id);
					newIntent.putExtra("CellID", alarmUtilsList.get(i).cellId);
					newIntent.putExtra("WayToStop", alarmUtilsList.get(i).waytostop);
					newIntent.putExtra("Med", alarmUtilsList.get(i).medicine);
					newIntent.putExtra("Description", alarmUtilsList.get(i).description);
					newIntent.putExtra("Sound", alarmUtilsList.get(i).sound);
					newIntent.putExtra("FromWhereActivity","Heaven" ); 
					
		            newPendingIntent = PendingIntent.getActivity(
							CellManageActivity.this, counter,
							newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		             

		            interval = alarmUtilsList.get(i).interval ;
		           
		            try {
						date = dateFormat.parse(alarmUtilsList.get(i).date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
		            
		            
		            String dateArry [] =  alarmUtilsList.get(i).date.split("/") ;
	            	String timeArry[] = alarmUtilsList.get(i).time.split(":") ;
	            	
	            	calendar.set(Calendar.YEAR, Integer.parseInt(dateArry[2])) ;
	            	calendar.set(Calendar.MONTH, Integer.parseInt(dateArry[1])-1) ;
	            	calendar.set(Calendar.DATE, Integer.parseInt(dateArry[0])) ;
	            	
	            	calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArry[0])) ;
	            	calendar.set(Calendar.MINUTE, Integer.parseInt(timeArry[1])) ;
	            	calendar.set(Calendar.SECOND,0) ;
	            	
//		            if (!(interval.equalsIgnoreCase("weekDays")))
//		            {

			            if (interval.equalsIgnoreCase("daily")){
			            	
			            	repeat = AlarmManager.INTERVAL_DAY;
			            	
				            if (!(calendar.getTimeInMillis()> calendar2.getTimeInMillis())){
				            	
					            calendar.clear() ;
				                calendar.set(Calendar.DATE, calendar2.get(Calendar.DATE)+1) ;
				                calendar.set(Calendar.MONTH, calendar2.get(Calendar.MONTH)) ;
				                calendar.set(Calendar.YEAR, calendar2.get(Calendar.YEAR)) ;
				             	calendar.set(Calendar.HOUR_OF_DAY,  Integer.parseInt(timeArry[0])) ;
				            	calendar.set(Calendar.MINUTE, Integer.parseInt(timeArry[1])) ;
				            	calendar.set(Calendar.SECOND,0) ;
			            }
			         }
			            else if (interval.equalsIgnoreCase("weekly")){
			            	repeat = AlarmManager.INTERVAL_DAY * 7;	
			            	
//			            	String  timeSet = alarmUtilsList.get(i).time;
//			            	
//			            	String[] value =  timeSet.split(":") ;
//			            	
//			            	date.setHours(Integer.parseInt(value[0]));
//			            	date.setMinutes(Integer.parseInt(value[1])) ;
			            	
//			            	calendar.clear()  ;
			            	
//			            	calendar.set(Calendar.YEAR, Integer.parseInt(dateArry[2])) ;
//			            	calendar.set(Calendar.MONTH, Integer.parseInt(dateArry[1])-1) ;
//			            	calendar.set(Calendar.DATE, Integer.parseInt(dateArry[0])) ;
//			            	
//			            	calendar.set(Calendar.HOUR, Integer.parseInt(timeArry[0])) ;
//			            	calendar.set(Calendar.MINUTE, Integer.parseInt(timeArry[1])) ;
//			            	calendar.set(Calendar.SECOND,0) ;
			            	
//			                if (!(calendar.getTimeInMillis()> calendar2.getTimeInMillis())){
					            	
//						            calendar.clear() ;
						            
//					                calendar.set(Calendar.DATE, calendar2.get(Calendar.DATE)) ;
//					                calendar.set(Calendar.MONTH, calendar2.get(Calendar.MONTH)) ;
//					                calendar.set(Calendar.YEAR, calendar2.get(Calendar.YEAR)) ;
//					             	calendar.set(Calendar.HOUR,  Integer.parseInt(timeArry[0])) ;
//					            	calendar.set(Calendar.MINUTE, Integer.parseInt(timeArry[1])) ;
//					            	calendar.set(Calendar.SECOND,0) ;
					            	
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
					        	    
//				            }
			            }
			            else if (interval.equalsIgnoreCase("monthly")){
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
			            else if (interval.equalsIgnoreCase("weekDays")){
			            	repeat = AlarmManager.INTERVAL_DAY * 7;	

			            	if(alarmUtilsList.get(i).nextday.equalsIgnoreCase(items[0])){
			            		dayCount=1;
			            		day = items[0] ;
			            	}
			            	else if (alarmUtilsList.get(i).nextday.equalsIgnoreCase(items[1])){
			            		dayCount=2;
			            		day = items[1] ;
			            	}
			            	else if (alarmUtilsList.get(i).nextday.equalsIgnoreCase(items[2])){
			            		dayCount=3;
			            		day = items[2] ;
			            	}
			            	else if (alarmUtilsList.get(i).nextday.equalsIgnoreCase(items[3])){
			            		dayCount=4;
			            		day = items[3] ;
			            	}
			            	else if (alarmUtilsList.get(i).nextday.equalsIgnoreCase(items[4])){
			            		dayCount=5;
			            		day = items[4] ;
			            	}
			            	else if (alarmUtilsList.get(i).nextday.equalsIgnoreCase(items[5])){
			            		dayCount=6;
			            		day = items[5] ;
			            	}
			            	else if (alarmUtilsList.get(i).nextday.equalsIgnoreCase(items[6])){
			            		dayCount=7;
			            		day = items[6] ;
			            	}
			            	
			            	
			            	calendar.set(Calendar.DAY_OF_WEEK, dayCount) ;
			            	
							if (!(calendar2.get(Calendar.DATE) == calendar                   //To manage old dates 
									.get(Calendar.DATE)                                      //increment day of week until it comes to current or greater then current  .   
									&& (calendar2.get(Calendar.MONTH) == calendar
											.get(Calendar.MONTH)) && (calendar2
										.get(Calendar.YEAR) == calendar
									.get(Calendar.YEAR)))){
								
								while (calendar.getTimeInMillis()<calendar2.getTimeInMillis()){
				        	    	calendar.set(Calendar.DATE,  calendar.get(Calendar.DATE)+7) ;
				        		    Log.d("Next event ","Date = "+ calendar.get(Calendar.DATE)  +"Month= "+calendar.get(Calendar.MONTH)) ;
				        	    }
							}
							
							calendar.set(Calendar.DAY_OF_WEEK, dayCount) ;
			            }
	 
			            Log.d("time", "Milisecond ="+calendar.getTimeInMillis()) ;
			            
			            if ((calendar.getTimeInMillis()> calendar2.getTimeInMillis()))
						alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
								calendar.getTimeInMillis(), repeat, newPendingIntent);
						   
						db.insertNotification(user_id, box_id,
								alarmUtilsList.get(i).cellId, login_id,
								alarmUtilsList.get(i).time, counter, interval,
								day, alarmUtilsList.get(i).date,
								alarmUtilsList.get(i).waytostop,
								alarmUtilsList.get(i).sound,
								alarmUtilsList.get(i).description,
								alarmUtilsList.get(i).medicine);
						
//						db.updateNotificationId(user_id, newBoxId,
//								alarmUtilsList.get(i).cellId, login_id, counter);
					 
//		            }
		            
//		            alarmanager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, interval, operation)
				}
				
			}


			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {	
			super.onPostExecute(result);
		
			
			ApplicationClass  applicationClass = (ApplicationClass)getApplication() ;
			applicationClass.isActive = false ; 
			
			if (box_id == 3) {
				intent = new Intent(CellManageActivity.this, BoxThreeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			} else if (box_id == 4) {
				intent = new Intent(CellManageActivity.this, BoxFourActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			} else if (box_id == 6) {
				intent = new Intent(CellManageActivity.this, BoxSixActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			} else if (box_id == 8) {
				intent = new Intent(CellManageActivity.this, BoxEightActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			} else if (box_id == 10) {
				intent = new Intent(CellManageActivity.this, BoxTenActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			}
			intent.putExtra("BoxID", box_id);
			intent.putExtra("UserID", user_id);
			intent.putExtra("LoginID", login_id);
			intent.putExtra("Activity", "Hell") ;
//			intent.putExtra(name, cell)
		
			startActivity(intent);
			
			dialog.cancel() ;
			
			finish();
			
			
		}
		
	}
}