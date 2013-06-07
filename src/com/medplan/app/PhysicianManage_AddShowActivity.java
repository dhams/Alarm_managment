package com.medplan.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView.ScaleType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medpan.util.CommonMethod;
import com.medpan.util.Constant;
import com.medpan.util.GlobalMethods;
import com.medpan.util.Phys_Model;
import com.medpan.util.Picture_Model;
import com.medplan.db.databasehelper;

public class PhysicianManage_AddShowActivity extends Activity implements
		OnClickListener {
	

	Spinner spMonFrom, spMonTo, spTueFrom, spTueTo, spWedFrom, spWedTo,
			spThursFrom, spThursTo, spFriFrom, spFriTo, spSatFrom, spSatTo,
			spSunFrom, spSunTo;
	
	LinearLayout addButton, showButton, llmon, llmontime, lltues, lltuestime,
			llwed, llwedtime, llthurs, llthurstime, llfri, llfritime, llsat,
			llsattime, llsun, llsuntime,MainBgLayout;
	
	String name, surname, address, city, state, zip, country, tel, mob, mail,
			note1, note2, visiting, doWhat;
	
	int picid, phyid, phy_user_id, userid, counter = 0, returnflag, _Str,
			monfrom, monto, tuefrom, tuesto, webfrom, wedto, thurfrom, thursto,
			frifrom, frito, satfrom, satto, sunfrom, sunto, gen;
	
	EditText etFName, etLName, etAddress, etState, etCity, etCountry, etZip,
			etTel, etMob, etMail, etNote1, etNote2;
	
	TextView headerTitle, tvMonday,tvTuesday,tvWednesday,tvThursday,tvFriday,tvSaturday,tvSunday;
	
	final CharSequence[] items = new CharSequence[7];
	final boolean[] flags = { false, false, false, false, false, false, false };
	boolean fillistha = false,increment=false,falgCamera;
	
	ArrayList<Phys_Model> userList, userMainList;
	Button add, cancel, back, update, delete, next, prev,btnHome;
	RadioButton rdMale,rdFemale;
	ImageView headLogo,ivPhy;
	RelativeLayout titleHeadLayout,rl;
	AlertDialog.Builder builder;
	AlertDialog alert;
	AutoCompleteTextView autoCountryList,autoStateList;
	static String _path;
	AlertDialog alertMsg;
	AlertDialog.Builder alertDialog,ad1,ad2;
	ArrayList<Picture_Model> picList;
	databasehelper db;
	

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		items[0]= getResources().getString(R.string.mon);
		items[1]= getResources().getString(R.string.tues);
		items[2]= getResources().getString(R.string.wed);
		items[3]= getResources().getString(R.string.thurs);
		items[4]= getResources().getString(R.string.fri);
		items[5]= getResources().getString(R.string.sat);
		items[6]= getResources().getString(R.string.sun);
		
		
		doWhat = this.getIntent().getStringExtra("Task");
		userid = this.getIntent().getIntExtra("idtoshow", -1);
		counter = this.getIntent().getIntExtra("counter", 0);
		returnflag = this.getIntent().getIntExtra("return", 0);
		Log.i("FLAG", "-----------------------------------" + returnflag);
		Log.i("uid ad sho", "--" + userid);
		setContentView(R.layout.physicianmanage_addshow);
		

		db = new databasehelper(this);
		phy_user_id = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getInt("UserID", -1);
		
		rl = (RelativeLayout) findViewById(R.id.prevnext);
		rdMale = (RadioButton)findViewById(R.id.rdMale);
		rdFemale = (RadioButton)findViewById(R.id.rdFemale);
		btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(
						PhysicianManage_AddShowActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});
		
		

		next = (Button) findViewById(R.id.btnNextPhy);
		next.setOnClickListener(this);
		prev = (Button) findViewById(R.id.btnPrevPhy);
		prev.setOnClickListener(this);

		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.phy_mgt);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.phy_mgt);

		titleHeadLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		MainBgLayout = (LinearLayout) findViewById(R.id.mainPhyLayout);

		llmon = (LinearLayout) findViewById(R.id.llMonday);
		llmontime = (LinearLayout) findViewById(R.id.llMondayTime);
		
		tvMonday = (TextView) findViewById(R.id.tvMonday);
		tvMonday.setOnClickListener(this);
		
		tvTuesday = (TextView) findViewById(R.id.tvTuesday);
		tvTuesday.setOnClickListener(this);
		
		tvWednesday = (TextView) findViewById(R.id.tvWednesday);
		tvWednesday.setOnClickListener(this);
		
		tvThursday = (TextView) findViewById(R.id.tvThursday);
		tvThursday.setOnClickListener(this);
		
		tvFriday = (TextView) findViewById(R.id.tvFriday);
		tvFriday.setOnClickListener(this);
		
		tvSaturday = (TextView) findViewById(R.id.tvSaturday);
		tvSaturday.setOnClickListener(this);
		
		tvSunday = (TextView) findViewById(R.id.tvSunday);
		tvSunday.setOnClickListener(this);
		
		
		
		
		lltues = (LinearLayout) findViewById(R.id.llTuesday);
		lltuestime = (LinearLayout) findViewById(R.id.llTuesdayTime);
		lltues = (LinearLayout) findViewById(R.id.llTuesday);
		lltuestime = (LinearLayout) findViewById(R.id.llTuesdayTime);
		llwed = (LinearLayout) findViewById(R.id.llWed);
		llwedtime = (LinearLayout) findViewById(R.id.llWedTime);
		llthurs = (LinearLayout) findViewById(R.id.llThur);
		llthurstime = (LinearLayout) findViewById(R.id.llThursTime);
		llfri = (LinearLayout) findViewById(R.id.llFri);
		llfritime = (LinearLayout) findViewById(R.id.llFriTime);
		llsat = (LinearLayout) findViewById(R.id.llSat);
		llsattime = (LinearLayout) findViewById(R.id.llSatTime);
		llsun = (LinearLayout) findViewById(R.id.llSun);
		llsuntime = (LinearLayout) findViewById(R.id.llSunTime);

		try {
			new CommonMethod().CheckAddShowScreen(Constant._StrCheck,
					titleHeadLayout, MainBgLayout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addButton = (LinearLayout) findViewById(R.id.btnForAdd);
		addButton.setOnClickListener(this);
		showButton = (LinearLayout) findViewById(R.id.btnForShow);
		showButton.setOnClickListener(this);
		add = (Button) findViewById(R.id.btnAddPhy);
		add.setOnClickListener(this);
		cancel = (Button) findViewById(R.id.btnCanPhy);
		cancel.setOnClickListener(this);
		back = (Button) findViewById(R.id.btnBkPhy);
		back.setOnClickListener(this);
		update = (Button) findViewById(R.id.btnUpPhy);
		update.setOnClickListener(this);
		delete = (Button) findViewById(R.id.btnDelPhy);
		delete.setOnClickListener(this);

		ivPhy = (ImageView) findViewById(R.id.imagePhy);
		ivPhy.setOnClickListener(this);
		etFName = (EditText) findViewById(R.id.etFName);
		etLName = (EditText) findViewById(R.id.etLName);
		etAddress = (EditText) findViewById(R.id.etAddress);
		etCity = (EditText) findViewById(R.id.etCity);
		etZip = (EditText) findViewById(R.id.etZip);
		etCountry = (EditText) findViewById(R.id.etCountry);
		etTel = (EditText) findViewById(R.id.etTel);
		etMob = (EditText) findViewById(R.id.etMobile);
		etMail = (EditText) findViewById(R.id.etEmail);

		etNote1 = (EditText) findViewById(R.id.etNote1);
		etNote2 = (EditText) findViewById(R.id.etNote2);

		FromListener fromListener = new FromListener();
		ToListener toListener = new ToListener();
		spMonFrom = (Spinner) findViewById(R.id.spTimeFromMonday);
		spMonFrom.setOnItemSelectedListener(fromListener);
		spMonTo = (Spinner) findViewById(R.id.spTimeToMonday);
		spMonTo.setOnItemSelectedListener(toListener);
		spTueFrom = (Spinner) findViewById(R.id.spTimeFromTuesday);
		spTueFrom.setOnItemSelectedListener(fromListener);
		spTueTo = (Spinner) findViewById(R.id.spTimeToTuesday);
		spTueTo.setOnItemSelectedListener(toListener);
		spWedFrom = (Spinner) findViewById(R.id.spTimeFromWed);
		spWedFrom.setOnItemSelectedListener(fromListener);
		spWedTo = (Spinner) findViewById(R.id.spTimeToWed);
		spWedTo.setOnItemSelectedListener(toListener);
		spThursFrom = (Spinner) findViewById(R.id.spTimeFromThurs);
		spThursFrom.setOnItemSelectedListener(fromListener);
		spThursTo = (Spinner) findViewById(R.id.spTimeToThurs);
		spThursTo.setOnItemSelectedListener(toListener);
		spFriFrom = (Spinner) findViewById(R.id.spTimeFromFri);
		spFriFrom.setOnItemSelectedListener(fromListener);
		spFriTo = (Spinner) findViewById(R.id.spTimeToFri);
		spFriTo.setOnItemSelectedListener(toListener);
		spSatFrom = (Spinner) findViewById(R.id.spTimeFromSat);
		spSatFrom.setOnItemSelectedListener(fromListener);
		spSatTo = (Spinner) findViewById(R.id.spTimeToSat);
		spSatTo.setOnItemSelectedListener(toListener);
		spSunFrom = (Spinner) findViewById(R.id.spTimeFromSun);
		spSunFrom.setOnItemSelectedListener(fromListener);
		spSunTo = (Spinner) findViewById(R.id.spTimeToSun);
		spSunTo.setOnItemSelectedListener(toListener);

		etState = (EditText) findViewById(R.id.etState);

//		GridView gv = null;
//		gv.setSelector(null);
		
		autoStateList=(AutoCompleteTextView)findViewById(R.id.autocomplete_state);
		autoStateList.setVisibility(View.GONE);
		etState.setVisibility(View.VISIBLE);
		
		autoStateList.setVisibility(View.GONE);
		etState.setVisibility(View.VISIBLE);
		
		//AUTO COMPLETE FOR COUNTRIES.
				autoCountryList = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						R.layout.auto_country_list, R.id.item, Constant.COUNTRIES);
				autoCountryList.setAdapter(adapter);
				
				
				autoCountryList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						 if(autoCountryList.getText().toString().equalsIgnoreCase("United States")) {
							 System.out.println("country selected"+autoCountryList.getText().toString());
						 autoStateList.setVisibility(View.VISIBLE);
						 etState.setVisibility(View.GONE);
						 ArrayAdapter<String> adapter = new ArrayAdapter<String>(PhysicianManage_AddShowActivity.this,
									R.layout.auto_country_list, R.id.item, Constant.STATES);
						 autoStateList.setAdapter(adapter);
						 }
						 else
						 {
							 autoStateList.setVisibility(View.GONE);
						 etState.setVisibility(View.VISIBLE);
						 }
						
					}
				});
				
				alertDialog = new AlertDialog.Builder(this);
				alertDialog.setTitle("Shot from Camera");
				alertDialog.setMessage(	R.string.shot_from_Camera);
				alertDialog.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
									Shot_Camera();
									
								}
							});
				
				alertDialog.setNegativeButton(R.string.no_thanks, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
					
						
					}
				});
				alertMsg = alertDialog.create();
				
				

		if (doWhat.equalsIgnoreCase("show")) {
			rl.setVisibility(View.VISIBLE);
			if (userid == -1) {
				userList = db.getPhys(phy_user_id);
				userMainList = userList;
				counter = 0;
			} else {
				userList = db.getSinglePhys(userid);
				userMainList = db.getPhys(phy_user_id);
			}

			if (userList.size() > 0) {
				showButton.setVisibility(View.VISIBLE);
				fillIsTheForm(userList.get(0));

			} else {
				rl.setVisibility(View.GONE);
				addButton.setVisibility(View.VISIBLE);
				Toast.makeText(PhysicianManage_AddShowActivity.this,
						R.string.no_user_phy_mgt,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			rl.setVisibility(View.GONE);
			addButton.setVisibility(View.VISIBLE);
		}

		builder = new AlertDialog.Builder(PhysicianManage_AddShowActivity.this);
		builder.setTitle("Select Days of Visiting");

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				for (int i = 0; i < 7; i++)
					Log.i("Value of Flags", "" + i + " " + flags[i]);
				int count = 0;
				if (flags[0] == true) {
					count ++;
					tvMonday.setText(R.string.mon);
					llmon.setVisibility(View.VISIBLE);
					llmontime.setVisibility(View.VISIBLE);
				} else {
					llmon.setVisibility(View.GONE);
					llmontime.setVisibility(View.GONE);
				}
				if (flags[1] == true) {
					count ++;
					lltues.setVisibility(View.VISIBLE);
					lltuestime.setVisibility(View.VISIBLE);
				} else {
					lltues.setVisibility(View.GONE);
					lltuestime.setVisibility(View.GONE);
				}
				if (flags[2] == true) {
					count ++;
					llwed.setVisibility(View.VISIBLE);
					llwedtime.setVisibility(View.VISIBLE);
				} else {
					llwed.setVisibility(View.GONE);
					llwedtime.setVisibility(View.GONE);
				}
				if (flags[3] == true) {
					count ++;
					llthurs.setVisibility(View.VISIBLE);
					llthurstime.setVisibility(View.VISIBLE);
				} else {
					llthurs.setVisibility(View.GONE);
					llthurstime.setVisibility(View.GONE);
				}
				if (flags[4] == true) {
					count ++;
					llfri.setVisibility(View.VISIBLE);
					llfritime.setVisibility(View.VISIBLE);
				} else {
					llfri.setVisibility(View.GONE);
					llfritime.setVisibility(View.GONE);
				}
				if (flags[5] == true) {
					count ++;
					llsat.setVisibility(View.VISIBLE);
					llsattime.setVisibility(View.VISIBLE);
				} else {
					llsat.setVisibility(View.GONE);
					llsattime.setVisibility(View.GONE);
				}
				if (flags[6] == true) {
					count ++;
					llsun.setVisibility(View.VISIBLE);
					llsuntime.setVisibility(View.VISIBLE);
				} else {
					llsun.setVisibility(View.GONE);
					llsuntime.setVisibility(View.GONE);
				}
				
				if(count ==0)
				{
					tvMonday.setText("");
					llmon.setVisibility(View.VISIBLE);
				}
				else
				{
					count=0;
				}

			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Toast.makeText(PhysicianManage_AddShowActivity.this, "Fail",
						Toast.LENGTH_SHORT).show();
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

//		builder.setItems(items,
//				new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						
//					}
//				});

	}
	public class FromListener implements OnItemSelectedListener
	{ 
		

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if(fillistha==true)
				return;
			
			if(arg2==0)
				arg2=0;
			else if(arg2==24)
				arg2=1;
			else
				arg2++;
			
			switch(arg0.getId())
			{
			case R.id.spTimeFromMonday:
			spMonTo.setSelection(arg2);
			 break;	
				
			case R.id.spTimeFromTuesday:
				spTueTo.setSelection(arg2);
				break;
			case R.id.spTimeFromWed:
				spWedTo.setSelection(arg2);
				break;
			case R.id.spTimeFromThurs:
				spThursTo.setSelection(arg2);
				break;
			case R.id.spTimeFromFri:
				spFriTo.setSelection(arg2);
				break;
			case R.id.spTimeFromSat:
				spSatTo.setSelection(arg2);
				break;
			case R.id.spTimeFromSun:
				spSunTo.setSelection(arg2);
				break;
			}
			
			
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	public class ToListener implements OnItemSelectedListener
	{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			
			Log.i("", arg0.getId()+"");
			Log.i("", R.id.spTimeFromMonday+"");
			if(spMonFrom.getSelectedItemPosition()==24&&spMonTo.getSelectedItemPosition()==1)
				return;
			
			switch(arg0.getId())
			{
			case R.id.spTimeToMonday:
				if(spMonTo.getSelectedItemPosition()<=spMonFrom.getSelectedItemPosition()&&spMonTo.getSelectedItemPosition()!=0)
				Toast.makeText(PhysicianManage_AddShowActivity.this, R.string.warning_msg_phy_mgt, Toast.LENGTH_LONG).show();	
					break;
			case R.id.spTimeToTuesday:
				if(spTueTo.getSelectedItemPosition()<=spTueFrom.getSelectedItemPosition()&&spTueTo.getSelectedItemPosition()!=0)
				Toast.makeText(PhysicianManage_AddShowActivity.this, R.string.warning_msg_phy_mgt, Toast.LENGTH_LONG).show();	
					break;
			case R.id.spTimeToWed:
				if(spWedTo.getSelectedItemPosition()<=spWedFrom.getSelectedItemPosition()&&spWedTo.getSelectedItemPosition()!=0)
				Toast.makeText(PhysicianManage_AddShowActivity.this, R.string.warning_msg_phy_mgt, Toast.LENGTH_LONG).show();	
					break;
			case R.id.spTimeToThurs:
				if(spThursTo.getSelectedItemPosition()<=spThursFrom.getSelectedItemPosition()&&spThursTo.getSelectedItemPosition()!=0)
				Toast.makeText(PhysicianManage_AddShowActivity.this, R.string.warning_msg_phy_mgt, Toast.LENGTH_LONG).show();	
					break;
			case R.id.spTimeToFri:
				if(spFriTo.getSelectedItemPosition()<=spFriFrom.getSelectedItemPosition()&&spFriTo.getSelectedItemPosition()!=0)
				Toast.makeText(PhysicianManage_AddShowActivity.this, R.string.warning_msg_phy_mgt, Toast.LENGTH_LONG).show();	
					break;
			case R.id.spTimeToSat:
				if(spSatTo.getSelectedItemPosition()<=spSatFrom.getSelectedItemPosition()&&spSatTo.getSelectedItemPosition()!=0)
				Toast.makeText(PhysicianManage_AddShowActivity.this, R.string.warning_msg_phy_mgt, Toast.LENGTH_LONG).show();	
					break;
			case R.id.spTimeToSun:
				if(spSatTo.getSelectedItemPosition()<=spSunFrom.getSelectedItemPosition()&&spSunTo.getSelectedItemPosition()!=0)
				Toast.makeText(PhysicianManage_AddShowActivity.this, R.string.warning_msg_phy_mgt, Toast.LENGTH_LONG).show();	
					break;
				
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	public void fillIsTheForm(Phys_Model phy) {
		picid = phy.iid;
		phyid = phy.pid;
		etFName.setText(phy.name);
		etLName.setText(phy.surname);
		etAddress.setText(phy.addres);

		etCity.setText(phy.city);
		etZip.setText(phy.zip);
		etCountry.setText(phy.country);
		autoCountryList.setText(phy.country);
		//spCountry.setSelection(Integer.parseInt(phy.country));

		if (autoCountryList.getText().toString().equalsIgnoreCase(
				"United States")) {
			autoStateList.setVisibility(View.VISIBLE);
			etState.setVisibility(View.GONE);
			autoStateList.setText(phy.state);
			
		} else {
			autoStateList.setVisibility(View.GONE);
			etState.setVisibility(View.VISIBLE);
			etState.setText(phy.state);
		}
		
		if(phy.gen==0)
		{
			rdMale.setChecked(true);
		}
		else
		{
			rdFemale.setChecked(true);
		}
		etTel.setText(phy.tel);
		etMob.setText(phy.mobile);
		etMail.setText(phy.mail);
		etNote1.setText(phy.note1);
		etNote2.setText(phy.note2);
		Picture_Model picmodel = db.getPicture(picid);
		
			Bitmap bitmap = GlobalMethods.decodeFile(picmodel.path);
			if (bitmap == null) {
				ivPhy.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} else {
				ivPhy.setImageBitmap(bitmap);
				ivPhy.setScaleType(ScaleType.FIT_XY);
			}
		

		boolean flg = false;
		fillistha = true;
		if (phy.mon.equalsIgnoreCase("true") && phy.monfrom!=0) {
			flg = true;
			flags[0]=true;
			llmon.setVisibility(View.VISIBLE);
			llmontime.setVisibility(View.VISIBLE);
			spMonFrom.setSelection(phy.monfrom);
			spMonTo.setSelection(phy.monto);
			tvMonday.setText(R.string.mon);
		} else {
			llmon.setVisibility(View.GONE);
			llmontime.setVisibility(View.GONE);
		}
		if (phy.tue.equalsIgnoreCase("true" )&& phy.tuefrom!=0) {
			flg = true;
			flags[1]=true;
			lltues.setVisibility(View.VISIBLE);
			lltuestime.setVisibility(View.VISIBLE);
			spTueFrom.setSelection(phy.tuefrom);
			spTueTo.setSelection(phy.tuesto);
		} else {
			lltues.setVisibility(View.GONE);
			lltuestime.setVisibility(View.GONE);
		}
		if (phy.wed.equalsIgnoreCase("true")&& phy.webfrom!=0) {
			flg = true;
			flags[2]=true;
			llwed.setVisibility(View.VISIBLE);
			llwedtime.setVisibility(View.VISIBLE);
			spWedFrom.setSelection(phy.webfrom);
			spWedTo.setSelection(phy.wedto);
		} else {
			llwed.setVisibility(View.GONE);
			llwedtime.setVisibility(View.GONE);
		}
		if (phy.thu.equalsIgnoreCase("true")&& phy.thurfrom!=0) {
			flg = true;
			flags[3]=true;
			llthurs.setVisibility(View.VISIBLE);
			llthurstime.setVisibility(View.VISIBLE);
			spThursFrom.setSelection(phy.thurfrom);
			spThursTo.setSelection(phy.thursto);
		} else {
			llthurs.setVisibility(View.GONE);
			llthurstime.setVisibility(View.GONE);
		}
		if (phy.fri.equalsIgnoreCase("true")&& phy.frifrom!=0) {
			flg = true;
			flags[4]=true;
			llfri.setVisibility(View.VISIBLE);
			llfritime.setVisibility(View.VISIBLE);
			spFriFrom.setSelection(phy.frifrom);
			spFriTo.setSelection(phy.frito);
		} else {
			llfri.setVisibility(View.GONE);
			llfritime.setVisibility(View.GONE);
		}
		if (phy.sat.equalsIgnoreCase("true")&& phy.satfrom!=0) {
			flg = true;
			flags[5]=true;
			llsat.setVisibility(View.VISIBLE);
			llsattime.setVisibility(View.VISIBLE);
			spSatFrom.setSelection(phy.satfrom);
			spSatTo.setSelection(phy.satto);
		} else {
			llsat.setVisibility(View.GONE);
			llsattime.setVisibility(View.GONE);
		}
		if (phy.sun.equalsIgnoreCase("true") && phy.sunfrom!=0) {
			flg = true;
			flags[6]=true;
			llsun.setVisibility(View.VISIBLE);
			llsuntime.setVisibility(View.VISIBLE);
			spSunFrom.setSelection(phy.sunfrom);
			spSunTo.setSelection(phy.sunto);
		} else {
			llsun.setVisibility(View.GONE);
			llsuntime.setVisibility(View.GONE);
		}
		if(flg==false)
		{
			llmon.setVisibility(View.VISIBLE);
		}
		fillistha = true;
	}

	

	public void onClick(View v) {
		name = etFName.getText().toString();
		surname = etLName.getText().toString();
		address = etAddress.getText().toString();
		city = etCity.getText().toString();
		zip = etZip.getText().toString();
		country = autoCountryList.getText().toString();
		if (autoCountryList.getText().toString().equalsIgnoreCase(
				"United States")) {
			state = autoStateList.getText().toString();
		} else {
			state = etState.getText().toString();
		}
		tel = etTel.getText().toString();
		mob = etMob.getText().toString();
		mail = etMail.getText().toString();
		note1 = etNote1.getText().toString();
		note2 = etNote2.getText().toString();
		if(rdFemale.isChecked()==true)
		{
			gen = 1;
		}
		else
		{
			gen = 0;
		}

		switch (v.getId()) {
		case R.id.tvMonday:
			 alert = builder.create();
			alert.show();
			break;
		case R.id.tvTuesday:
			 alert = builder.create();
			alert.show();
			break;
		case R.id.tvWednesday:
			 alert = builder.create();
			alert.show();
			break;
		case R.id.tvThursday:
			 alert = builder.create();
			alert.show();
			break;
		case R.id.tvFriday:
			 alert = builder.create();
			alert.show();
			break;
		case R.id.tvSaturday:
			 alert = builder.create();
			alert.show();
			break;
		case R.id.tvSunday:
			 alert = builder.create();
			alert.show();
			break;
		case R.id.btnNextPhy:
			if (counter == (userMainList.size() - 1)) {
				Toast.makeText(PhysicianManage_AddShowActivity.this,
						R.string.last_record, Toast.LENGTH_SHORT).show();

			} else {
				
				if (counter < (userMainList.size() - 1))
					counter++;
				
				fillIsTheForm(userMainList.get(counter));
			}
			break;
		case R.id.btnPrevPhy:
			if (counter == 0) {
				Toast.makeText(PhysicianManage_AddShowActivity.this,
						R.string.first_record, Toast.LENGTH_SHORT).show();

			} else {
				
				if (counter > 0)
					counter--;
				
				fillIsTheForm(userMainList.get(counter));
			}
			break;
		case R.id.imagePhy:
			registerForContextMenu(v);
			openContextMenu(v);
			break;
		case R.id.btnAddPhy:
			if (flags[0] == true && spMonFrom.getSelectedItemPosition()!=0) {
				monfrom = spMonFrom.getSelectedItemPosition();
				monto = spMonTo.getSelectedItemPosition();
			}
			if (flags[1] == true && spTueFrom.getSelectedItemPosition()!=0) {
				tuefrom = spTueFrom.getSelectedItemPosition();
				tuesto = spTueTo.getSelectedItemPosition();
			}
			if (flags[2] == true && spWedFrom.getSelectedItemPosition()!=0) {
				webfrom = spWedFrom.getSelectedItemPosition();
				wedto = spWedTo.getSelectedItemPosition();
			}
			if (flags[3] == true && spThursFrom.getSelectedItemPosition()!=0) {
				thurfrom = spThursFrom.getSelectedItemPosition();
				thursto = spThursTo.getSelectedItemPosition();
			}
			if (flags[4] == true && spFriFrom.getSelectedItemPosition()!=0) {
				frifrom = spFriFrom.getSelectedItemPosition();
				frito = spFriTo.getSelectedItemPosition();
			}
			if (flags[5] == true && spSatFrom.getSelectedItemPosition()!=0) {
				satfrom = spSatFrom.getSelectedItemPosition();
				satto = spSatTo.getSelectedItemPosition();
			}
			if (flags[6] == true && spSunFrom.getSelectedItemPosition()!=0) {
				sunfrom = spSunFrom.getSelectedItemPosition();
				sunto = spSunTo.getSelectedItemPosition();
			}
			if (!(name.equals("") || surname.equals("")|| tel.equals("")|| mob.equals(""))) {
				
				if(falgCamera==true){
					//insertation in picture management
				     db.insertPicture(phy_user_id, _path, name, 1, note1,note2);
				     falgCamera=false;
				     ArrayList<Picture_Model> picsTemp = db.getPictures(phy_user_id);
						picid = picsTemp.get(picsTemp.size() - 1).id;
				}
				
				if(GlobalMethods.isEmail(mail)){
					db.insertPhysician(phy_user_id, picid, name, surname, address,
							city, zip, country, state,gen, tel, mob, mail, visiting,
							note1, note2, "" + flags[0], monfrom, monto, ""
									+ flags[1], tuefrom, tuesto, "" + flags[2],
							webfrom, wedto, "" + flags[3], thurfrom, thursto, ""
									+ flags[4], frifrom, frito, "" + flags[5],
							satfrom, satto, "" + flags[6], sunfrom, sunto);
			     
				Toast.makeText(PhysicianManage_AddShowActivity.this,
						R.string.physician_saved_phy_mgt, Toast.LENGTH_LONG).show();
				Log.i("FLAG", "-----------------------------------"+ returnflag);
				
				if (returnflag == 1) {
					Intent i = new Intent();
					i.putExtra("phyid", db.getNewPhyID());
					i.putExtra("phynm", name + " " + surname);

					setResult(RESULT_OK, i);
					this.finish();
				} else
					this.finish();
				
				}
				else{
					Toast.makeText(PhysicianManage_AddShowActivity.this,
							"Enter correct Mail-ID", Toast.LENGTH_SHORT)
							.show();
				}
				
					
					
			} else
				Toast
						.makeText(
								PhysicianManage_AddShowActivity.this,
								R.string.feild_require_phy_mgt,
								Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnUpPhy:
			if (flags[0] == true) {
				monfrom = spMonFrom.getSelectedItemPosition();
				monto = spMonTo.getSelectedItemPosition();
			}
			if (flags[1] == true) {
				tuefrom = spTueFrom.getSelectedItemPosition();
				tuesto = spTueTo.getSelectedItemPosition();
			}
			if (flags[2] == true) {
				webfrom = spWedFrom.getSelectedItemPosition();
				wedto = spWedTo.getSelectedItemPosition();
			}
			if (flags[3] == true) {
				thurfrom = spThursFrom.getSelectedItemPosition();
				thursto = spThursTo.getSelectedItemPosition();
			}
			if (flags[4] == true) {
				frifrom = spFriFrom.getSelectedItemPosition();
				frito = spFriTo.getSelectedItemPosition();
			}
			if (flags[5] == true) {
				satfrom = spSatFrom.getSelectedItemPosition();
				satto = spSatTo.getSelectedItemPosition();
			}
			if (flags[6] == true) {
				sunfrom = spSunFrom.getSelectedItemPosition();
				sunto = spSunTo.getSelectedItemPosition();
			}
			if (!(name.equals("") || surname.equals("") )) {
				
				if (falgCamera == true) {
					db.insertPicture(phy_user_id, _path, name, 1, note1, note2);

					falgCamera = false;
					ArrayList<Picture_Model> picsTemp = db.getPictures(phy_user_id);
					picid = picsTemp.get(picsTemp.size() - 1).id;
				}
				if(GlobalMethods.isEmail(mail)){
					
					db.updatePhysician(phy_user_id, phyid, picid, name, surname,
							address, city, zip, country, state,gen, tel, mob, mail,
							visiting, note1, note2, "" + flags[0], monfrom, monto,
							"" + flags[1], tuefrom, tuesto, "" + flags[2], webfrom,
							wedto, "" + flags[3], thurfrom, thursto, "" + flags[4],
							frifrom, frito, "" + flags[5], satfrom, satto, ""
									+ flags[6], sunfrom, sunto);
					Log.i("","" + flags[0]+" "+ monfrom+ " " + monto+ " " +
							"" + flags[1]+ " " + tuefrom+ " " + tuesto + " " +"" + flags[2]+ " " + webfrom+ " " +
							wedto+ " " + "" + flags[3]+ " " + thurfrom+ " " + thursto+ " " + "" + flags[4]+ " " +
							frifrom+ " " + frito+ " " + "" + flags[5]+ " " + satfrom+ " " + satto+ " " + ""
									+ flags[6]+ " " + sunfrom+ " " + sunto);
					Toast.makeText(PhysicianManage_AddShowActivity.this,
							R.string.physician_update_phy_mgt, Toast.LENGTH_LONG).show();

					this.finish();
					
				}
				else{
					Toast.makeText(PhysicianManage_AddShowActivity.this,
							"Enter correct Mail-ID", Toast.LENGTH_SHORT)
							.show();
				}
				
			} else
				Toast
						.makeText(
								PhysicianManage_AddShowActivity.this,
								R.string.feild_require_phy_mgt,
								Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnDelPhy:
			if(db.getUserCountPhy(phyid)>0)
			{
			ad1 = new AlertDialog.Builder(this);
			ad1.setTitle("Warning");
			ad1.setMessage(R.string.delete_warningphy);
			ad1.setPositiveButton("Next",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							ad2 = new AlertDialog.Builder(PhysicianManage_AddShowActivity.this);
							ad2.setTitle("Warning");
							ad2.setMessage(R.string.delete_messagephy);
							ad2.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface dialog, int which) {
											db.deletePhysican(phyid);
											db.updatePhyPatients(phyid);
											
											Toast.makeText(PhysicianManage_AddShowActivity.this,R.string.physician_delete_phy_mgt, Toast.LENGTH_SHORT).show();
											PhysicianManage_AddShowActivity.this.finish();

										}
									});

							ad2.setNegativeButton("No",
									new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface dialog, int which) {

										}
									});
							ad2.create().show();
							
						}
					});

			ad1.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

						}
					});
			ad1.create().show();
			}
			else
			{
				db.deletePhysican(phyid);
				db.updatePhyPatients(phyid);
				
				Toast.makeText(PhysicianManage_AddShowActivity.this,R.string.physician_delete_phy_mgt, Toast.LENGTH_SHORT).show();
				PhysicianManage_AddShowActivity.this.finish();
			}
			break;
		case R.id.btnBkPhy:
		case R.id.btnCanPhy:
			PhysicianManage_AddShowActivity.this.finish();
			break;
		}
	}
	public void Shot_Camera(){
		try {
			//_path = Environment.getExternalStorageDirectory() + File.separator+ "TakenFromCamera" + cal.getTimeInMillis() + ".png";
			
			String parentdir;
			parentdir = Environment.getExternalStorageDirectory()+"/Medplann";
			File parentDirFile = new File(parentdir);
			parentDirFile.mkdirs();

			// If we can't write to that special path, try just writing
			// directly to the sdcard
			if (!parentDirFile.isDirectory()) {
			parentdir = Environment.getExternalStorageDirectory()+"";
			}
	                Calendar cal = Calendar.getInstance();
	                String filename = "IMG"+cal.getTimeInMillis()+".jpg";
	                String filepath = Environment.getExternalStorageDirectory()+"/Medplann/"+filename;
	                _path=filepath;
	                
			System.out.println("thumbnail path~~~~~~"+_path);
			File file = new File(_path);
			Uri outputFileUri = Uri.fromFile(file);
			
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			startActivityForResult(intent, 1212);	
			falgCamera=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == 1212) {
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
			Bitmap bitmap;
			//bitmap=GlobalMethods.decodeSampledBitmapFromResource(_path, 80, 80);
			bitmap=GlobalMethods.decodeFile(_path);
			if (bitmap == null) {
				ivPhy.setImageBitmap(bitmap);
			} 
			else {
				ivPhy.setImageBitmap(bitmap);
				ivPhy.setScaleType(ScaleType.FIT_XY);
			}
		}
		
		if (requestCode == 111) {
			picid = data.getIntExtra("pid", 0);
			Picture_Model picmodel = db.getPicture(picid);

			//Bitmap bitmap = BitmapFactory.decodeFile(picmodel.path);
			Bitmap bitmap = GlobalMethods.decodeFile(picmodel.path);
			if (bitmap == null) {
				ivPhy.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} else {
				ivPhy.setImageBitmap(bitmap);
				//ivPhy.setImageBitmap(bitmap);
			}
			 _path=picmodel.path;
		}
		
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		//add image from camera
		if(item.getItemId()==0)
		{
			Shot_Camera();
			
		}
		//select image from list
		else if(item.getItemId()==1)
		{
			picList= new ArrayList<Picture_Model>();
			userid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("UserID", 0);
			picList = db.getPictures(userid);
			System.out.println("array size~~~~~~"+picList.size());
			if (picList.size() == 0) {
				alertDialog.show();
			}else{
			Intent intent = new Intent(PhysicianManage_AddShowActivity.this,PicManage_ListActivity.class);
			intent.putExtra("From", "Other");
			startActivityForResult(intent, 111);
			}
		}
		return super.onContextItemSelected(item);
		

	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.select);
		
			menu.add(0, 0, 0, R.string.take_new_pic_mgt);
			menu.add(0, 1, 0, R.string.pic_gallery_pic_mgt);
		}
}
