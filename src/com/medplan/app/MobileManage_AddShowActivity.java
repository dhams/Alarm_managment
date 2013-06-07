package com.medplan.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.medpan.util.CommonMethod;
import com.medpan.util.Constant;
import com.medpan.util.Contact_Model;
import com.medpan.util.GlobalMethods;
import com.medpan.util.Picture_Model;
import com.medplan.db.BgColorAdapter;
import com.medplan.db.databasehelper;

public class MobileManage_AddShowActivity extends Activity implements
		OnClickListener {
	LinearLayout addButton, showButton;
	databasehelper db;
	EditText etFName, etLName, etTel, etMob, etMail, etWT, etMC, etMI;
	ArrayList<Contact_Model> userList,userMainList;
	Button add, cancel, back, update, delete,next,prev;

	String name, surname, tel, mob, mail, wt, mailcount, mailinterval;
	int id, uid,picid, icon, mti, relation, totalIcon, intTime, callCount, mcount,counter=0,mc,mi;
	ImageView headLogo,imgMobile;
	TextView headerTitle;
	String doWhat = "";
	int idtoshow, userid;
	RelativeLayout titleHeadLayout;
	LinearLayout MainBgLayout;
	Spinner spRelation, spTotalIcon, spIntTime, spCallCount;
	
	static String _path;
	AlertDialog alertMsg;
	AlertDialog.Builder alertDialog,ad1,ad2;
	ArrayList<Picture_Model> picList;
	BgColorAdapter adapter;
	String []widget_icon;
	String []num_type;
	String[] color_type ;
	boolean flagCamera;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		doWhat = this.getIntent().getStringExtra("Task");
		idtoshow = this.getIntent().getIntExtra("idtoshow", -1);
		Log.i("uid ad sho", "--" + idtoshow);
		setContentView(R.layout.mobilemanage_addshow);
		
		widget_icon=getResources().getStringArray(R.array.widget_icon);
		num_type=getResources().getStringArray(R.array.call_count_type);

		color_type = getResources().getStringArray(R.array.color_type);
		RelativeLayout rl = (RelativeLayout)findViewById(R.id.prevnext);
		
		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(MobileManage_AddShowActivity.this,
						MainActivity.class);
				startActivity(intent);
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
		
		next = (Button) findViewById(R.id.btnNextUserMob);
		next.setOnClickListener(this);
		prev = (Button) findViewById(R.id.btnPrevUserMob);
		prev.setOnClickListener(this);

		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		imgMobile= (ImageView) findViewById(R.id.imageMob);
		imgMobile.setOnClickListener(this);
		headLogo.setBackgroundResource(R.drawable.mob_mgt);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.mob_mgt);

		titleHeadLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		MainBgLayout = (LinearLayout) findViewById(R.id.mainMobileLayout);

		try {
			SplashScreen.Cmethod.CheckAddShowScreen(Constant._StrCheck,
					titleHeadLayout, MainBgLayout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		db = new databasehelper(this);

		userid = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getInt("UserID", -1);
		Log.i("User ID Add", "Add " + userid);

		addButton = (LinearLayout) findViewById(R.id.btnForAdd);
		addButton.setOnClickListener(this);
		showButton = (LinearLayout) findViewById(R.id.btnForShow);
		showButton.setOnClickListener(this);
		add = (Button) findViewById(R.id.btnMobAdd);
		add.setOnClickListener(this);
		cancel = (Button) findViewById(R.id.btnMobCan);
		cancel.setOnClickListener(this);
		back = (Button) findViewById(R.id.btnMobBk);
		back.setOnClickListener(this);
		update = (Button) findViewById(R.id.btnMobUp);
		update.setOnClickListener(this);
		delete = (Button) findViewById(R.id.btnMobDel);
		delete.setOnClickListener(this);

		etFName = (EditText) findViewById(R.id.etFName);
		etLName = (EditText) findViewById(R.id.etLName);
		etTel = (EditText) findViewById(R.id.etTel);
		etMob = (EditText) findViewById(R.id.etMob);
		etMail = (EditText) findViewById(R.id.etEmail);
		etMC = (EditText) findViewById(R.id.etMailCounter);
		etMI = (EditText) findViewById(R.id.etMailInterval);
		etWT = (EditText) findViewById(R.id.etText);

		spRelation = (Spinner) findViewById(R.id.spRelation);
		spTotalIcon = (Spinner) findViewById(R.id.spIcon);
		
	
		         
		         Drawable[] arr_phone_icon = { null,getResources().getDrawable(R.drawable.cell1),
						 getResources().getDrawable(R.drawable.cell2),getResources().getDrawable (R.drawable.cell4),
						 getResources().getDrawable(R.drawable.cell3),getResources().getDrawable( R.drawable.cell5)};
		         
//		        BgColorAdapter bgColorAdapter = new BgColorAdapter(MobileManage_AddShowActivity.this,R.layout.spinner_row,num_type,widget_icon,arr_phone_icon) ;
				adapter= new BgColorAdapter(MobileManage_AddShowActivity.this,R.layout.spinner_row,num_type,widget_icon,arr_phone_icon);
				
				 
				adapter = new BgColorAdapter(MobileManage_AddShowActivity.this,
						R.layout.spinner_row, num_type, color_type,
						arr_phone_icon);
				
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
				spTotalIcon.setAdapter(adapter);
				
				spTotalIcon.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if(spTotalIcon.getSelectedItemPosition()==0){
							
						}
						else{
//							TextView text =(TextView)findViewById(R.id.text_number);
//							ImageView image = (ImageView)findViewById(R.id.image);
//							image.setVisibility(View.GONE);
//							text.setVisibility(View.GONE);	
						}
						
						
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});
			
		
		spIntTime = (Spinner) findViewById(R.id.spInterval);
		spCallCount = (Spinner) findViewById(R.id.spCall);

		if (doWhat.equalsIgnoreCase("show")) {
			rl.setVisibility(View.VISIBLE);
			if (idtoshow == -1) {
				userList = db.getContacts(userid);
				counter = 0;
				userMainList = userList;
			} else {
				userList = db.getContact(idtoshow);
				counter = this.getIntent().getIntExtra("counter", 0);
				userMainList = db.getContacts(userid);
				Log.i("size", "size of the list " + userList.size());
			}
			if (userList.size() > 0) {
				showButton.setVisibility(View.VISIBLE);
				fillIsTheForm(userList.get(0));
				

			} else {
				rl.setVisibility(View.GONE);
				addButton.setVisibility(View.VISIBLE);
				Toast.makeText(MobileManage_AddShowActivity.this,
					R.string.no_contact_saved_mob_mgt,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			rl.setVisibility(View.GONE);
			addButton.setVisibility(View.VISIBLE);
		}

	}

	public void fillIsTheForm(Contact_Model cm) {
		id = cm.id;
		uid = cm.uid;
		picid = cm.picid;

		etFName.setText(cm.fnm);
		etLName.setText(cm.lnm);

		etTel.setText(cm.tel);
		etMob.setText(cm.mob);
		etMail.setText(cm.email);
		etWT.setText(cm.wtext);
		
		spRelation.setSelection(cm.relation);
		
		spTotalIcon.setSelection(cm.icon);
		spIntTime.setSelection(cm.mti);
		spCallCount.setSelection(cm.mcount);
		
		
		etMC.setText(cm.mailcount);
		etMI.setText(cm.mailinterval);
		
		Picture_Model picmodel = db.getPicture(picid);
		
			Bitmap bitmap = GlobalMethods.decodeFile(picmodel.path);
			if (bitmap == null) {
				imgMobile.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} else {
				imgMobile.setImageBitmap(bitmap);
				imgMobile.setScaleType(ScaleType.FIT_XY);
			}
	}


	public void onClick(View v) {
		name = etFName.getText().toString();
		surname = etLName.getText().toString();
		relation = spRelation.getSelectedItemPosition();
		tel = etTel.getText().toString();
		mob = etMob.getText().toString();
		totalIcon = spTotalIcon.getSelectedItemPosition();
		intTime = spIntTime.getSelectedItemPosition();
		callCount = spCallCount.getSelectedItemPosition();
		mail = etMail.getText().toString();
		wt = etWT.getText().toString();
		mailcount = etMC.getText().toString();
		mailinterval = etMI.getText().toString();
		try {
			 mc = Integer.parseInt(mailcount);
			 mi = Integer.parseInt(mailinterval);	
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		




		switch (v.getId()) {
		case R.id.imageMob:
			registerForContextMenu(v);
			openContextMenu(v);
			break;
		
		case R.id.btnNextUserMob:
			if (counter == (userMainList.size() - 1)) {
				Toast.makeText(MobileManage_AddShowActivity.this,
						R.string.last_record, Toast.LENGTH_SHORT).show();

			} else {
				
				if (counter < (userMainList.size() - 1))
					counter++;
				
				fillIsTheForm(userMainList.get(counter));
			}
			break;
		case R.id.btnPrevUserMob:
			if (counter == 0) {
				Toast.makeText(MobileManage_AddShowActivity.this,
						R.string.first_record, Toast.LENGTH_SHORT).show();
	
			} else {
			
				if (counter > 0)
					counter--;
				
				fillIsTheForm(userMainList.get(counter));
			}
			break;

		case R.id.btnMobAdd:
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Editor edit = sp.edit();
			Log.i("","Total "+totalIcon);
			switch (totalIcon) {
			case 1:
			edit.putString("First", mob);
			edit.putInt("FirstTime", intTime);
			edit.putInt("FirstCall", callCount);
			edit.putInt("FirstEmailTime", mi);
			edit.putInt("FirstEmailCall", mc);
			edit.putString("FirstEmail", mail);
			edit.putString("FirstText", wt);
			Log.i("", "mob f "+mob);
			break;
			case 2:
			edit.putString("Second",mob);
			edit.putInt("SecondTime", intTime);
			edit.putInt("SecondCall", callCount);
			edit.putInt("SecondEmailTime", mi);
			edit.putInt("SecondEmailCall", mc);
			edit.putString("SecondEmail", mail);
			edit.putString("SecondText", wt);
			Log.i("", "mob sec "+mob);
			break;
			case 3:
			edit.putString("Third", mob);
			edit.putInt("ThirdTime", intTime);
			edit.putInt("ThirdCall", callCount);
			edit.putInt("ThirdEmailTime", mi);
			edit.putInt("ThirdEmailCall", mc);
			edit.putString("ThirdEmail", mail);
			edit.putString("ThirdText", wt);
			Log.i("", "mob th "+mob);
			break;
			case 4:
			edit.putString("Fourth",mob);
			edit.putInt("FourthTime", intTime);
			edit.putInt("FourthCall", callCount);
			edit.putInt("FourthEmailTime", mi);
			edit.putInt("FourthEmailCall", mc);
			edit.putString("FourthEmail", mail);
			edit.putString("FourthText", wt);
			Log.i("", "mob forr "+mob);
			break;
			case 5:
			edit.putString("Fifth",mob);
			edit.putInt("FifthTime", intTime);
			edit.putInt("FifthCall", callCount);
			edit.putInt("FifthEmailTime", mi);
			edit.putInt("FifthEmailCall", mc);
			edit.putString("FifthEmail", mail);
			edit.putString("FifthText", wt);
			Log.i("", "mob five "+mob);
			break;

			}
			edit.commit();
			if (!(name.equals("") || surname.equals("") || tel.equals("") || mob
					.equals("")||relation==0 )) {
				
				
				
			    if(flagCamera==true){
			    	//insertation in picture management
			    	db.insertPicture(userid, _path, name, 1, mail,wt);
			    	flagCamera=false;
			    	ArrayList<Picture_Model> picsTemp = db.getPictures(userid);
					picid = picsTemp.get(picsTemp.size() - 1).id;
			    }
			    if(GlobalMethods.isEmail(mail)){
			    	  db.insertContact(userid,picid, name, surname, relation, tel, mob,
								totalIcon, intTime, callCount, mail, wt, mailcount,
								mailinterval);
					    
						Toast.makeText(MobileManage_AddShowActivity.this,
								R.string.contact_saved_mob_mgt, Toast.LENGTH_LONG).show();
						this.finish();
				}
				else{
					Toast.makeText(MobileManage_AddShowActivity.this,
							"Enter correct Mail-ID", Toast.LENGTH_SHORT)
							.show();
				}
			  
			} else
				Toast.makeText(
						MobileManage_AddShowActivity.this,
						R.string.feild_require_mob_mgt,
						Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnMobUp:
			sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			edit = sp.edit();
			switch (totalIcon) {
			case 1:
			edit.putString("First", mob);
			edit.putInt("FirstTime", intTime);
			edit.putInt("FirstCall", callCount);
			edit.putInt("FirstEmailTime", mi);
			edit.putInt("FirstEmailCall", mc);
			edit.putString("FirstEmail", mail);
			edit.putString("FirstText", wt);
			Log.i("", "mob f "+mob);
			break;
			case 2:
			edit.putString("Second",mob);
			edit.putInt("SecondTime", intTime);
			edit.putInt("SecondCall", callCount);
			edit.putInt("SecondEmailTime", mi);
			edit.putInt("SecondEmailCall", mc);
			edit.putString("SecondEmail", mail);
			edit.putString("SecondText", wt);
			Log.i("", "mob sec "+mob);
			break;
			case 3:
//			edit.putString("Third", mob);
//			edit.putInt("ThirdTime", intTime);
//			edit.putInt("ThirdCall", callCount);
//			edit.putInt("ThirdEmailTime", mi);
//			edit.putInt("ThirdEmailCall", mc);
//			edit.putString("ThirdEmail", mail);
//			edit.putString("ThirdText", wt);
//			Log.i("", "mob th "+mob);
				
				edit.putString("Fourth",mob);
				edit.putInt("FourthTime", intTime);
				edit.putInt("FourthCall", callCount);
				edit.putInt("FourthEmailTime", mi);
				edit.putInt("FourthEmailCall", mc);
				edit.putString("FourthEmail", mail);
				edit.putString("FourthText", wt);
				Log.i("", "mob forr "+mob);
				
			break;
			case 4:
				
				edit.putString("Third", mob);
				edit.putInt("ThirdTime", intTime);
				edit.putInt("ThirdCall", callCount);
				edit.putInt("ThirdEmailTime", mi);
				edit.putInt("ThirdEmailCall", mc);
				edit.putString("ThirdEmail", mail);
				edit.putString("ThirdText", wt);
				Log.i("", "mob th "+mob);
				
				
//			edit.putString("Fourth",mob);
//			edit.putInt("FourthTime", intTime);
//			edit.putInt("FourthCall", callCount);
//			edit.putInt("FourthEmailTime", mi);
//			edit.putInt("FourthEmailCall", mc);
//			edit.putString("FourthEmail", mail);
//			edit.putString("FourthText", wt);
//			Log.i("", "mob forr "+mob);
			break;
			case 5:
			edit.putString("Fifth",mob);
			edit.putInt("FifthTime", intTime);
			edit.putInt("FifthCall", callCount);
			edit.putInt("FifthEmailTime", mi);
			edit.putInt("FifthEmailCall", mc);
			edit.putString("FifthEmail", mail);
			edit.putString("FifthText", wt);
			Log.i("", "mob five "+mob);
			break;

			}
			edit.commit();
			if (!(name.equals("") || surname.equals("") || tel.equals("") || mob
					.equals("")||relation==0)) {
				
				 if(flagCamera==true){
				    	//insertation in picture management
				    	db.insertPicture(userid, _path, name, 1, mail,wt);
				    	flagCamera=false;
				    	ArrayList<Picture_Model> picsTemp = db.getPictures(userid);
						picid = picsTemp.get(picsTemp.size() - 1).id;
				    }
				 if(GlobalMethods.isEmail(mail)){
					 
					 
						db.updateContact(id, userid,picid, name, surname, relation, tel, mob,
								totalIcon, intTime, callCount, mail, wt, mailcount,
								mailinterval);
						Toast.makeText(MobileManage_AddShowActivity.this,
								R.string.contact_update_mob_mgt, Toast.LENGTH_LONG).show();

						this.finish();
					}
					else{
						Toast.makeText(MobileManage_AddShowActivity.this,
								"Enter correct Mail-ID", Toast.LENGTH_SHORT)
								.show();
					}
				
			} else
				Toast.makeText(
						MobileManage_AddShowActivity.this,
						R.string.feild_require_mob_mgt,
						Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnMobDel:
			ad1 = new AlertDialog.Builder(this);
			ad1.setTitle("Warning");
			ad1.setMessage(R.string.delete_warningmob);
			ad1.setPositiveButton("Next",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							ad2 = new AlertDialog.Builder(MobileManage_AddShowActivity.this);
							ad2.setTitle("Warning");
							ad2.setMessage(R.string.delete_messagemob);
							ad2.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface dialog, int which) {
											db.deleteContact(id);
											Toast.makeText(MobileManage_AddShowActivity.this,
													R.string.contact_delete_mob_mgt, Toast.LENGTH_LONG).show();

											MobileManage_AddShowActivity.this.finish();

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
			
			break;
		case R.id.btnMobBk:
		case R.id.btnMobCan:
			MobileManage_AddShowActivity.this.finish();
			break;
		}
	}
	
	public void Shot_Camera(){
		try {
//			Calendar cal = Calendar.getInstance();
//			_path = Environment.getExternalStorageDirectory() + File.separator
//					+ "TakenFromCamera" + cal.getTimeInMillis() + ".png";
			
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
	                
			File file = new File(_path);
			Uri outputFileUri = Uri.fromFile(file);
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			startActivityForResult(intent, 1212);	
			flagCamera=true;
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
				imgMobile.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} 
			else {
				imgMobile.setImageBitmap(bitmap);
				imgMobile.setScaleType(ScaleType.FIT_XY);
			}
		}
		
		if (requestCode == 111) {
			picid = data.getIntExtra("pid", 0);
			Picture_Model picmodel = db.getPicture(picid);
			_path=picmodel.path;

			//Bitmap bitmap = BitmapFactory.decodeFile(picmodel.path);
			//Bitmap bitmap=	GlobalMethods.decodeSampledBitmapFromResource(_path, 80, 80);
			Bitmap bitmap=	GlobalMethods.decodeFile(_path);
			if (bitmap == null) {
				imgMobile.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} else {
				imgMobile.setImageBitmap(bitmap);
				//ivPhy.setImageBitmap(bitmap);
			}
			 
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
			Intent intent = new Intent(MobileManage_AddShowActivity.this,PicManage_ListActivity.class);
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
