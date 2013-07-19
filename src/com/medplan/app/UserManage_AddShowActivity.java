package com.medplan.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medpan.util.Constant;
import com.medpan.util.GlobalMethods;
import com.medpan.util.Picture_Model;
import com.medpan.util.User_Model;
import com.medplan.db.databasehelper;

public class UserManage_AddShowActivity extends Activity implements
		OnClickListener {
	
	boolean typinget1 = true;
	
	LinearLayout addButton, showButton;
	databasehelper db;
	EditText etFName, etLName, etAddress, etCity, etState, etCountry, etZip,
			etTel, etMob, etMail, etNote1, etNote2, etTole, etPhy;
	ArrayList<User_Model> userList, userMainList;
	static int counter = 0;
	String _path = "";
	Uri mCapturedImageURI = null;
	Button add, cancel, back, update, delete, next, prev;
	ImageView ivUser, headLogo;
	TextView headerTitle, tvPhy;
	String name="", surname="", address, country, state, city, zip, tel, mob, mail="",
			gender, note1, note2, tole, btype, phy, genderSelected = "Male",
			_check, path = "";
	int picid, phyid, uid, user_id, blood, userid;
	RelativeLayout titleHeadLayout;
	LinearLayout MainBgLayout;
	Spinner spBloodType;
	RadioGroup genderGroup;
	RadioButton genderRadio, mailRadio, femailRadio;
	boolean flag, addImage;
	String doWhat;
	AutoCompleteTextView autoCountryList, autoStateList;
	ArrayList<Picture_Model> picList;
	AlertDialog alertMsg;
	AlertDialog.Builder alertDialog,ad1,ad2;
	public File cacheDir;
	Bitmap bitmap = null;
	
	private static final int CAMERA_IMAGE_CAPTURE = 0;
	private  Uri imageCaptureUri  ,unknowndeviceUri; 

	private boolean flagCamera ;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		doWhat = this.getIntent().getStringExtra("Task");
		userid = this.getIntent().getIntExtra("idtoshow", -1);
		Log.i("uid ad sho", "--" + userid);
		setContentView(R.layout.usermanage_addshow);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.prevnext);

		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(UserManage_AddShowActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});
		
		
		db = new databasehelper(this);
		//
		user_id = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getInt("UserID", -1);
		Log.i("User ID Add", "Add " + user_id);

		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.user_mgt);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.user_mgt);

		titleHeadLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		MainBgLayout = (LinearLayout) findViewById(R.id.mainUserLayout);

		try {
			SplashScreen.Cmethod.CheckAddShowScreen(Constant._StrCheck,
					titleHeadLayout, MainBgLayout);
		} catch (Exception e) {
			e.printStackTrace();
		}

		addButton = (LinearLayout) findViewById(R.id.btnForAdd);
		addButton.setOnClickListener(this);
		showButton = (LinearLayout) findViewById(R.id.btnForShow);
		showButton.setOnClickListener(this);
		next = (Button) findViewById(R.id.btnNextUser);
		next.setOnClickListener(this);
		prev = (Button) findViewById(R.id.btnPrevUser);
		prev.setOnClickListener(this);

		add = (Button) findViewById(R.id.btnAddUser);
		add.setOnClickListener(this);
		cancel = (Button) findViewById(R.id.btnCanUser);
		cancel.setOnClickListener(this);
		back = (Button) findViewById(R.id.btnBkUser);
		back.setOnClickListener(this);
		update = (Button) findViewById(R.id.btnUpUser);
		update.setOnClickListener(this);
		delete = (Button) findViewById(R.id.btnDelUser);
		delete.setOnClickListener(this);

		ivUser = (ImageView) findViewById(R.id.imageUser);
		ivUser.setOnClickListener(this);
		etFName = (EditText) findViewById(R.id.etFName);
		etLName = (EditText) findViewById(R.id.etLName);

		
//		
//		etFName.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				// TODO Auto-generated method stub
//				if(etFName.getText().toString().length()==4 && typinget1==true)     //size as per your requirement
//		        {
//					typinget1 = false;
//					etLName.requestFocus();
//		        }
//				if(etFName.getText().toString().length()<=4 && typinget1==false)
//				{
//					typinget1 = true;
//				}
//
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//				if(etFName.getText().toString().length()==4 )
//				{
//					typinget1 = true;
//					etLName.requestFocus();
//				}
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		etAddress = (EditText) findViewById(R.id.etAddress);
		etCity = (EditText) findViewById(R.id.etCity);
		etState = (EditText) findViewById(R.id.etState);
		etZip = (EditText) findViewById(R.id.etZip);

		etTel = (EditText) findViewById(R.id.etTel);
		etMob = (EditText) findViewById(R.id.etMobile);
		etMail = (EditText) findViewById(R.id.etEmail);
		etTole = (EditText) findViewById(R.id.etInt);
		// etBType = (EditText)findViewById(R.id.etBType);
		etPhy = (EditText) findViewById(R.id.etPhy);
		etPhy.setOnClickListener(this);

		tvPhy = (TextView) findViewById(R.id.tvPhy);
		tvPhy.setOnClickListener(this);
		etNote1 = (EditText) findViewById(R.id.etNote1);
		etNote2 = (EditText) findViewById(R.id.etNote2);

		genderGroup = (RadioGroup) findViewById(R.id.gender_radio_grp);
		mailRadio = (RadioButton) findViewById(R.id.rdMale);
		femailRadio = (RadioButton) findViewById(R.id.rdFemale);

		genderGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup rGroup,
							int checkedId) {
						// This will get the radiobutton that has changed in its
						// check state
						genderRadio = (RadioButton) genderGroup
								.findViewById(genderGroup
										.getCheckedRadioButtonId());
						// This puts the value (true/false) into the variable
						genderSelected = genderRadio.getText().toString();
						// boolean isChecked = genderRadio.isChecked();
						// If the radiobutton that has changed in check state is
						// now checked...
						// if (isChecked)
						// {
						// // Changes the textview's text to
						// "Checked: example radiobutton text"
						// }
					}
				});

		// spState = (Spinner) findViewById(R.id.spStateUSUser);
		// spCountry = (Spinner) findViewById(R.id.spCountryUser);
		autoStateList = (AutoCompleteTextView) findViewById(R.id.autocomplete_state);
		autoStateList.setVisibility(View.GONE);
		etState.setVisibility(View.VISIBLE);

		spBloodType = (Spinner) findViewById(R.id.spBType);
		autoStateList.setVisibility(View.GONE);
		etState.setVisibility(View.VISIBLE);

		// AUTO COMPLETE FOR COUNTRIES.
		autoCountryList = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.auto_country_list, R.id.item, Constant.COUNTRIES);
		autoCountryList.setAdapter(adapter);

		autoCountryList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (autoCountryList.getText().toString()
						.equalsIgnoreCase("United States")) {
					System.out.println("country selected"
							+ autoCountryList.getText().toString());
					flag = true;
					System.out.println("state list adapter set");
					autoStateList.setVisibility(View.VISIBLE);
					etState.setVisibility(View.GONE);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							UserManage_AddShowActivity.this,
							R.layout.auto_country_list, R.id.item,
							Constant.STATES);
					autoStateList.setAdapter(adapter);
				} else {
					flag = false;
					autoStateList.setVisibility(View.GONE);
					etState.setVisibility(View.VISIBLE);
				}

			}
		});

		
		alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Shot from Camera");
		alertDialog.setMessage(R.string.shot_from_Camera);
		alertDialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Shot_Camera();

					}
				});
 
		alertDialog.setNegativeButton(R.string.no_thanks,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}
				});
		alertMsg = alertDialog.create();

		if (doWhat.equalsIgnoreCase("show")) {
			rl.setVisibility(View.VISIBLE);
			if (userid == -1) {

				userList = db.getPatients(user_id);
				counter = 0;
				userMainList = userList;
			} else {
				userList = db.getSinglePatientForId(userid);

				userMainList = db.getPatients(user_id);
				counter = this.getIntent().getIntExtra("counter", 0);

			}
			Log.i("TAG Counter", "Counter Value Initialized ... " + counter);

			if (userList.size() > 0) {
				showButton.setVisibility(View.VISIBLE);
				fillIsTheForm(userList.get(0));

			} else {
				rl.setVisibility(View.GONE);
				addButton.setVisibility(View.VISIBLE);
				Toast.makeText(UserManage_AddShowActivity.this,
						R.string.no_user_user_mgt, Toast.LENGTH_SHORT).show();
			}
		} else {
			rl.setVisibility(View.GONE);
			addButton.setVisibility(View.VISIBLE);
		}

	}

	public void fillIsTheForm(User_Model um) {

		_check = mailRadio.getText().toString();

		System.out.println("radivalues" + _check);
		System.out.println("db gender values" + um.gender);

		if (um.gender.equalsIgnoreCase(_check)) {
			mailRadio.setChecked(true);
		} else {
			femailRadio.setChecked(true);
		}
 
		picid = um.picid;
		phyid = um.phyid;
		uid = um.uid;
		etFName.setText(um.name);
		etLName.setText(um.surname);
		etAddress.setText(um.address);
		etCity.setText(um.city);
		etState.setText(um.state);
		autoCountryList.setText(um.country);
		// spCountry.setSelection(Integer.parseInt(um.country));
		spBloodType.setSelection(Integer.parseInt(um.btype));
		etZip.setText(um.zip);
		etTel.setText(um.tel);
		etMob.setText(um.mob);
		etMail.setText(um.mail);
		etTole.setText(um.tole);
		tvPhy.setText(um.phynm);

		etNote1.setText(um.note1);
		etNote2.setText(um.note2);

		Picture_Model picmodel = db.getPicture(picid);

		_path = picmodel.path ;
			Bitmap bitmap = GlobalMethods.decodeFile(picmodel.path);
			if (bitmap == null) {
				ivUser.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.add_photo));
			}
			else 
			{	
				ivUser.setImageBitmap(bitmap);
				ivUser.setScaleType(ScaleType.FIT_XY);
			}
		
		

	}

	public void onClick(View v) {
		name = etFName.getText().toString();
		surname = etLName.getText().toString();
		address = etAddress.getText().toString();
		zip = etZip.getText().toString();
		city = etCity.getText().toString();
		if (flag == true) {
			state = autoStateList.getText().toString();
			System.out.println("state spiner~~~~~~~" + state);
		} else {
			state = etState.getText().toString();
			System.out.println("state editbox~~~~~~~" + state);
		}

		// country = spCountry.getSelectedItemPosition();
		country = autoCountryList.getText().toString();
		blood = spBloodType.getSelectedItemPosition();

		tel = etTel.getText().toString();
		mob = etMob.getText().toString();
		mail = etMail.getText().toString();	
		gender = genderSelected;
		tole = etTole.getText().toString();
		phy = tvPhy.getText().toString();
		note1 = etNote1.getText().toString();
		note2 = etNote2.getText().toString();

		switch (v.getId()) {
		case R.id.btnNextUser:
			if (counter == (userMainList.size() - 1)) {
				Toast.makeText(UserManage_AddShowActivity.this,
						R.string.last_record, Toast.LENGTH_SHORT).show();

			} else {

				if (counter < (userMainList.size() - 1))
					counter++;

				fillIsTheForm(userMainList.get(counter));
			}
			Log.i("", "---------" + counter);
			break;
		case R.id.btnPrevUser:
			if (counter == 0) {
				Toast.makeText(UserManage_AddShowActivity.this,
						R.string.first_record, Toast.LENGTH_SHORT).show();

			} else {

				if (counter > 0)
					counter--;

				fillIsTheForm(userMainList.get(counter));
			}
			Log.i("", "---------" + counter);
			break;
		case R.id.tvPhy:
			addImage = false;
			registerForContextMenu(v);
			openContextMenu(v);

			break;
		case R.id.etPhy:
			// Intent intent = new Intent(UserManage_AddShowActivity.this,
			// PhysicianManage_ListActivity.class);
			// intent.putExtra("From", "Other");
			// startActivityForResult(intent, 222);
			break;
		case R.id.imageUser:
			addImage = true;
			registerForContextMenu(v);
			openContextMenu(v);
			break;
			
//			ttsharma94@gmail.com
//
//			TDhSharma489
		case R.id.btnAddUser:
			if (!(name.equals("") || surname.equals(""))) {
				
//				if(GlobalMethods.isEmail(mail)){
					
					Log.i("User Id : User Add ", ""+user_id);
					if (flagCamera == true) {
						// insertation in picture management
					

						System.out.println("User Id~~~~~~~" + user_id);
						System.out.println("image path~~~~~~~" + _path);
						flagCamera = false;

						db.insertPicture(user_id, _path, name, 1, note1, note2);
						
						ArrayList<Picture_Model> picsTemp = db.getPictures(user_id);
						picid = picsTemp.get(picsTemp.size() - 1).id;
					}

						
					db.insertPatients(user_id, picid, phyid, name, surname,
							address, zip, country, state, city, gender, tel, mob,
							mail, tole, blood, phy, note1, note2);
					
					Toast.makeText(getApplicationContext(),
							R.string.pateint_added_user_mgt, Toast.LENGTH_SHORT)
							.show();
					this.finish();
					
//				}
				
//				else{
//					Toast.makeText(UserManage_AddShowActivity.this,
//							"Enter correct Mail-ID", Toast.LENGTH_SHORT)
//							.show();
//				}

				
			} else
				Toast.makeText(UserManage_AddShowActivity.this,
						R.string.feild_require_user_mgt, Toast.LENGTH_SHORT)
						.show();
			break;
		case R.id.btnUpUser:
			if (!(name.equals("") || surname.equals(""))) {
				Log.i("PID", "......... " + uid);
				Log.i("User Id : User Update ", ""+user_id);
//				if (flagCamera == true) {
////					db.insertPicture(user_id, _path, name, 1, bnote1, note2);
//					
//					System.out.println("User Id~~~~~~~" + user_id);
//					System.out.println("image path~~~~~~~" + _path);
//					flagCamera = false;
//					
////					ArrayList<Picture_Model> picsTemp = db.getPictures(user_id);
////					picid = picsTemp.get(picsTemp.size() - 1).id;
//					
//					db.updatePicture(0, picid, _path, null, 0, null, null , false) ;
//				} 
				
				if (flagCamera == true) {
					db.insertPicture(user_id, _path, name, 1, note1, note2);
					System.out.println("User Id~~~~~~~" + user_id);
					System.out.println("image path~~~~~~~" + _path);
					flagCamera = false;
					ArrayList<Picture_Model> picsTemp = db.getPictures(user_id);
					picid = picsTemp.get(picsTemp.size() - 1).id;
				}
					
				
//				if(GlobalMethods.isEmail(mail)){
					db.updatePatients(user_id, uid, picid, phyid, name, surname,
							address, zip, country, state, city, gender, tel, mob,
							mail, tole, blood, phy, note1, note2);
					Toast.makeText(getApplicationContext(),
							R.string.pateint_update_user_mgt, Toast.LENGTH_SHORT)
							.show();
					this.finish();
//				}
//				else{
//					Toast.makeText(UserManage_AddShowActivity.this,
//							"Enter correct Mail-ID", Toast.LENGTH_SHORT)
//							.show();
//				}
				
			} else
				Toast.makeText(UserManage_AddShowActivity.this,
						R.string.feild_require_user_mgt, Toast.LENGTH_SHORT)
						.show();
			break;
		case R.id.btnDelUser:
			Log.i("PID", "......... " + uid);
			if(db.getCellCountUser(uid)>0)
			{
			ad1 = new AlertDialog.Builder(this);
			ad1.setTitle("Warning");
			ad1.setMessage(R.string.delete_warninguser);
			ad1.setPositiveButton("Next",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							
						
							ad2 = new AlertDialog.Builder(UserManage_AddShowActivity.this);
							ad2.setTitle("Warning");
							ad2.setMessage(R.string.delete_messageuser);
							ad2.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface dialog, int which) {
											db.deletePatients(uid);
											db.deleteUserCell(uid);
											db.deleteUserForm(uid);
											db.deleteUserNotification(uid);
											Toast.makeText(getApplicationContext(),
													R.string.pateint_delete_user_mgt, Toast.LENGTH_SHORT)
													.show();
											UserManage_AddShowActivity.this.finish();

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
				db.deletePatients(uid);
				db.deleteUserCell(uid);
				db.deleteUserForm(uid);
				db.deleteUserNotification(uid);
				Toast.makeText(getApplicationContext(),
						R.string.pateint_delete_user_mgt, Toast.LENGTH_SHORT)
						.show();
				UserManage_AddShowActivity.this.finish();
			}
			
			break;
		case R.id.btnBkUser:
		case R.id.btnCanUser:
			UserManage_AddShowActivity.this.finish();
			break;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == 1212) {
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
			Bitmap bitmap;
			//bitmap=GlobalMethods.decodeSampledBitmapFromResource(_path, 80, 80);
			bitmap=GlobalMethods.decodeFile(_path);
			if (bitmap == null) {
				ivUser.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} 
			else {
				ivUser.setImageBitmap(bitmap);
				ivUser.setScaleType(ScaleType.FIT_XY);
			}
			
			
		}
		if (requestCode == 111) {
			System.out.println("from list~~~~~~~~~");
			picid = data.getIntExtra("pid", 0);
			Picture_Model picmodel = db.getPicture(picid);
//			Bitmap bitmap = BitmapFactory.decodeFile(picmodel.path);
			Bitmap bitmap =GlobalMethods.decodeFile(picmodel.path);
			
			if (bitmap == null) {
				ivUser.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} else {
				ivUser.setImageBitmap(bitmap);
				// ivUser.setImageBitmap(bitmap);
			}
			_path = picmodel.path;
		}
		if (requestCode == 222) {
			if (data != null) {
				phyid = data.getIntExtra("phyid", 0);

				phy = data.getStringExtra("phynm");
				tvPhy.setText(phy);

			}

			etNote1.requestFocus();
		}
		
		if(requestCode==CAMERA_IMAGE_CAPTURE && resultCode==Activity.RESULT_OK){
			_path	= getThubnailFilePath() ;
			Bitmap bitmap = GlobalMethods.decodeFile(_path);
			
			if (bitmap == null) {
				ivUser.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} else {
				ivUser.setImageBitmap(bitmap);
				flagCamera = true ;
			}
		}
	}
	
	/**
	 * Get the path of captured  image by camera 
	 * @return
	 */
	private String getThubnailFilePath() {
		try {
			String[] largeFileProjection = {
					MediaStore.Images.ImageColumns._ID,
					MediaStore.Images.ImageColumns.DATA };

			String largeFileSort = MediaStore.Images.ImageColumns._ID
					+ " DESC";
		Cursor	myCursor = this.managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					largeFileProjection, null, null, largeFileSort);
			
			
			String largeImagePath = "";

			try {
				myCursor.moveToFirst();

				// This will actually give yo uthe file path location of
				// the
				// image.
				largeImagePath = myCursor
						.getString(myCursor
								.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
				unknowndeviceUri = Uri.fromFile(new File(
						largeImagePath));
				imageCaptureUri = null;
			} finally {
			}
			
			
		} catch (Exception e) {
			unknowndeviceUri = null;
			e.printStackTrace();
		}
		
		if (unknowndeviceUri != null)
			return unknowndeviceUri.getPath();
		else
			return getPath(imageCaptureUri);
	}

	/**
	 * Get image path from {@link Uri}
	 * @param uri
	 * @return
	 */
	public String getPath(Uri uri) {

		String StringPath = null;
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();

			StringPath = cursor.getString(column_index);
			if (StringPath != null)
				return StringPath;
		} else {
			StringPath = null;
		}

		if (StringPath == null) {
			StringPath = uri.getPath();
			if (StringPath != null)
				return StringPath;
		} else {
			return null;
		}
		return StringPath;
	}
	
	public void Shot_Camera(){
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) 
		{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			String filename = System.currentTimeMillis() + ".jpg";
			ContentValues values = new ContentValues();
			values.put(MediaStore.Images.Media.TITLE, filename);
			imageCaptureUri = getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					imageCaptureUri);
			try {
				startActivityForResult(intent, CAMERA_IMAGE_CAPTURE);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
		}
		else {
			new AlertDialog.Builder(UserManage_AddShowActivity.this)
					.setMessage(
							"External Storeage (SD Card) is required.\n\nCurrent state: "
									+ storageState).setCancelable(true)
					.create().show();
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		// add image from camera
		if (item.getItemId() == 0) {
			Shot_Camera();

		}
		// select image from list
		else if (item.getItemId() == 1) {
			picList = new ArrayList<Picture_Model>();
			userid = PreferenceManager.getDefaultSharedPreferences(
					getApplicationContext()).getInt("UserID", 0);
			picList = db.getPictures(userid);
			System.out.println("array size~~~~~~" + picList.size());
			if (picList.size() == 0) {
				alertDialog.show();

			} else {
				Intent intent = new Intent(UserManage_AddShowActivity.this,
						PicManage_ListActivity.class);
				intent.putExtra("From", "Other");
				startActivityForResult(intent, 111);
			}

		}

		// add physician from existing list
		else if (item.getItemId() == 2) {
			Intent intent = new Intent(UserManage_AddShowActivity.this,
					PhysicianManage_ListActivity.class);
			intent.putExtra("From", "Other");
			startActivityForResult(intent, 222);
		}
		// add new physician.
		else if (item.getItemId() == 3) {
			Intent intent = new Intent(UserManage_AddShowActivity.this,
					PhysicianManage_AddShowActivity.class);
			intent.putExtra("Task", "add");
			intent.putExtra("return", 1);
			Log.i("FLAG", "-----------------------------------" + 1);
			startActivityForResult(intent, 222);

		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.select);
		if (addImage == true) {
			menu.add(0, 0, 0, R.string.take_new_pic_mgt);
			menu.add(0, 1, 0, R.string.pic_gallery_pic_mgt);
		}

		else {
			menu.add(0, 2, 0, R.string.exists_list_user_mgt);
			menu.add(0, 3, 0, R.string.add_new_user_mgt);
		}

	}

}