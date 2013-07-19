package com.medplan.app;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medpan.util.Constant;
import com.medpan.util.GlobalMethods;
import com.medplan.db.databasehelper;

public class RegisterScreen extends Activity implements OnClickListener {
	Button cancelButton, registerButton;
	EditText etFnm, etLnm, etUnm, etPwd, etZip, etAdd, etState, etCity;
	String fnm="", lnm="", unm="", pwd="", zip, address, city, uType, country, state;
	int type = 1;
	databasehelper db;
	Spinner spType;
	TextView tvLogin;
	String  path = "";
	ImageView ivPhy;
	String[] items1 = new String[] { " ", "User/Patient", "Doctor/Physician",
			"Pharma" };
	AutoCompleteTextView autoCountryList, autoStateList;
	boolean flag;
	static AlertDialog loginMessage;
	static AlertDialog.Builder loginDialog;
	public static SharedPreferences SP;
	private Bitmap bitmap=null;
	
	private  Uri imageCaptureUri  ,unknowndeviceUri; 
	private static final int CAMERA_IMAGE_CAPTURE = 0;

	private static final int SELECT_PICTURE = 1;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		getWindow().setWindowAnimations(0);
		setContentView(R.layout.register_screen);

		db = new databasehelper(this);
		cancelButton = (Button) findViewById(R.id.btnCan);
		registerButton = (Button) findViewById(R.id.btnReg);
		tvLogin = (TextView) findViewById(R.id.tv_reg_login);
		ivPhy = (ImageView) findViewById(R.id.regimagivew);
		ivPhy.setOnClickListener(this);

		etFnm = (EditText) findViewById(R.id.et_reg_fname);
		etLnm = (EditText) findViewById(R.id.et_reg_lname);
		etUnm = (EditText) findViewById(R.id.et_reg_uname);
		etPwd = (EditText) findViewById(R.id.et_reg_password);
		etZip = (EditText) findViewById(R.id.et_reg_zipcode);
		etAdd = (EditText) findViewById(R.id.et_reg_address);
		etState = (EditText) findViewById(R.id.et_reg_state);
		etCity = (EditText) findViewById(R.id.et_reg_city);

		cancelButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		tvLogin.setOnClickListener(this);
		spType = (Spinner) findViewById(R.id.spType);
		// spState = (Spinner) findViewById(R.id.spStateUS);
		// spCountry = (Spinner)findViewById(R.id.spCountry);

		autoStateList = (AutoCompleteTextView) findViewById(R.id.autocomplete_state);
		autoStateList.setVisibility(View.GONE);
		etState.setVisibility(View.VISIBLE);

		loginDialog = new AlertDialog.Builder(this);
		loginDialog.setTitle(R.string.auto_login);
		loginDialog.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}
				});
		loginDialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

//						SharedPreferences spe = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//						Editor editer = spe.edit();
//						editer.putString("isLogin", "yes");
//						editer.commit();
						
						db.insertUser(path, fnm, lnm, unm, pwd, zip, address,
								country, state, city, uType);
						boolean tmp = db.isValidUser(unm, pwd);
						Log.i("", "-------" + tmp);
						ConfigureScreen.userType = 1;

						SharedPreferences sp = PreferenceManager
								.getDefaultSharedPreferences(getApplicationContext());
						Editor edit = sp.edit();
						edit.putInt("UserID", db.getUserID());
						Log.i("User Id Register Screen ", ""+db.getUserID());
						edit.putString("username", unm);
						edit.putString("password", pwd);
						edit.putString("compare", "compare");
						edit.commit();

						Intent intent = new Intent(RegisterScreen.this,
								MainActivity.class);
						startActivity(intent);
						RegisterScreen.this.finish();

					}
				});

		loginDialog.setNegativeButton(R.string.no_thanks,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						db.insertUser(path, fnm, lnm, unm, pwd, zip, address,
								country, state, city, uType);
						ConfigureScreen.userType = 1;

						Intent intent = new Intent(RegisterScreen.this,
								LoginScreen.class);
						startActivity(intent);
						RegisterScreen.this.finish();

					}
				});

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
							RegisterScreen.this, R.layout.auto_country_list,
							R.id.item, Constant.STATES);
					autoStateList.setAdapter(adapter);
				} else {
					autoStateList.setVisibility(View.GONE);
					etState.setVisibility(View.VISIBLE);
				}

			}
		});

			// spCountry.setOnItemSelectedListener(new OnItemSelectedListener() {
			//
			// public void onItemSelected(AdapterView<?> arg0, View arg1,
			// int arg2, long arg3) {
			// if(spCountry.getSelectedItem().toString().equalsIgnoreCase("United States"))
			// {
			// flag=true;
			// spState.setVisibility(View.VISIBLE);
			// etState.setVisibility(View.GONE);
			// }
			// else
			// {
			// spState.setVisibility(View.GONE);
			// etState.setVisibility(View.VISIBLE);
			// }
			//
			// }
			//
			// public void onNothingSelected(AdapterView<?> arg0) {
			// // TODO Auto-generated method stub
			//
			// }
			// });
 
		ArrayAdapter aa = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, items1);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spType.setAdapter(aa);
		spType.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				try {
					SharedPreferences.Editor editor = SplashScreen.SP.edit();
					editor.putString("OperationType", spType.getSelectedItem()
							.toString());
					editor.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Constant._StrCheck = SP.getString("OperationType", "");

				Constant._StrCheck = spType.getSelectedItem().toString();
				// System.out.println("user tpye~~~~~~~~~~~~~"+
				// spType.getSelectedItem().toString());

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		
//		gallery() ;
	}

	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode==KeyEvent.KEYCODE_BACK){
		
			launchHomeActivity();
		}
		
		
		return super.onKeyDown(keyCode, event);
	}
	private void  launchHomeActivity(){
		
	    Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
}

//	@Override
//	protected void onResume() {
//		
//		super.onResume();
//		if (bitmap!=null){
//			ivPhy.setImageBitmap(bitmap);
//			ivPhy.setScaleType(ScaleType.FIT_XY);
//		}
//
//	}
	
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
			new AlertDialog.Builder(RegisterScreen.this)
					.setMessage(
							"External Storeage (SD Card) is required.\n\nCurrent state: "
									+ storageState).setCancelable(true)
					.create().show();
		}
	}
	
	public void gallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				SELECT_PICTURE);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
//			Calendar cal = Calendar.getInstance();
//			_path = Environment.getExternalStorageDirectory() + File.separator
//					+ "TakenFromCamera" + cal.getTimeInMillis() + ".png";
//			File file = new File(_path);
//			Uri outputFileUri = Uri.fromFile(file);
//			Intent intent = new Intent(
//					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//			startActivityForResult(intent, 1212);
//			 
//			Shot_Camera();
			
			
			
//			String parentdir;
//			parentdir = Environment.getExternalStorageDirectory()+"/Medplann";
//			File parentDirFile = new File(parentdir);
//			parentDirFile.mkdirs();
//
//			// If we can't write to that special path, try just writing
//			// directly to the sdcard
//			if (!parentDirFile.isDirectory()) {
//			parentdir = Environment.getExternalStorageDirectory()+"";
//			}
//	                Calendar cal = Calendar.getInstance();
//	                String filename = "IMG"+cal.getTimeInMillis()+".jpg";
//	                String filepath = Environment.getExternalStorageDirectory()+"/Medplann/"+filename;
//	                _path=filepath;
//			
//			File file = new File(_path);
//			Uri outputFileUri = Uri.fromFile(file);
//			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//			startActivityForResult(intent, 1212);

			
			Shot_Camera() ;
			
			
			break;
		case 1:
//			Intent gallery = new Intent();
//			gallery.setType("image/*"); // sets the return type to IMAGE
//			gallery.setAction(Intent.ACTION_GET_CONTENT);
//			startActivityForResult(
//					Intent.createChooser(gallery, "Select Picture"), 1);

			
			gallery();
			break;
		}
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.select);
		menu.add(0, 0, 0, R.string.take_new_pic_mgt);
		menu.add(0, 1, 0, R.string.pic_gallery_pic_mgt);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		
		if (requestCode == SELECT_PICTURE&&resultCode == RESULT_OK) {
				Uri selectedImageUri = data.getData();
				// filemanagerstring = selectedImageUri.getPath();
				this.path = getPath(selectedImageUri);
				if (path != null) {
					Log.e("Image path", path);
//					Bitmap bitmap = GlobalMethods.decodeFile(path);
					Bitmap bitmap = GlobalMethods.decodeFileForReg(path, selectedImageUri, getApplicationContext());
					
//					Bitmap bitmap = BitmapFactory.decodeFile(path);
					ivPhy.setImageBitmap(bitmap);
				}
		}
		
//		if (resultCode == RESULT_OK && requestCode == 1) {
//
//			Uri contentUri = data.getData();
//			String[] proj = { MediaStore.Images.Media.DATA };
//			Cursor cursor = managedQuery(contentUri, proj, null, null, null);
//			int column_index = cursor
//					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//			cursor.moveToFirst();
//			String tmppath = cursor.getString(column_index);
//			// Bitmap mBitmap = BitmapFactory.decodeFile(tmppath);
//			//ivPhy.setImageBitmap(GlobalMethods.decodeSampledBitmapFromResource(tmppath, 80, 80));
//			
//			Bitmap bitmap 
//			= GlobalMethods.decodeFile(tmppath);
//			ivPhy.setImageBitmap(bitmap);
//			// ivPhy.setImageBitmap(mBitmap);
//			path = tmppath;
//		}
//		if (requestCode == 1212) {
//			
////			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED));
//////			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
//////			   Bitmap photo = (Bitmap) data.getExtras().get("data"); 
//////			   ivPhy.setImageBitmap(photo);
////			   
//////			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
//////			Bitmap bitmap;
////			//bitmap=GlobalMethods.decodeSampledBitmapFromResource(_path, 80, 80);
////			bitmap=GlobalMethods.decodeFile(_path);
//////			bitmap=null;
////			
////			if (bitmap == null) { 
////				ivPhy.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
////                try { 
////					GMailSender sender = new GMailSender("android.testapps@gmail.com", "androidandroid");
////					sender.sendMail("Resgister image",   
////					        "Registration screen , image has not displayed ",   
////					        "android.testapps@gmail.com",   
////					        "dharmendra@openxcelltechnolabs.com");
////				} catch (Exception e) {
////					e.printStackTrace();
////				} 
////			} 
////			else {
//////                try { 
//////					GMailSender sender = new GMailSender("android.testapps@gmail.com", "androidandroid");
//////					sender.sendMail("Resgister image",   
//////					        "Registration screen , image has not displayed ",   
//////					        "android.testapps@gmail.com",   
//////					        "dharmendra@openxcelltechnolabs.com");
//////				} catch (Exception e) {
//////					e.printStackTrace();
//////				}  
//////				ivPhy.setImageBitmap(bitmap);
//////				ivPhy.setScaleType(ScaleType.FIT_XY);
////			}
//			
//			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
//			Bitmap bitmap; 
//			//bitmap=GlobalMethods.decodeSampledBitmapFromResource(_path, 80, 80);
//			bitmap=GlobalMethods.decodeFile(_path);
//			if (bitmap == null) {
//				try { 
////					ivPhy.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
//					GMailSender sender = new GMailSender("android.testapps@gmail.com", "androidandroid");
//					sender.sendMail("Resgister image",   
//					    "Registration screen , image has not displayed ",   
//					    "android.testapps@gmail.com",   
//					    "dharmendra@openxcelltechnolabs.com");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			} else {
//				ivPhy.setImageBitmap(bitmap);
//				ivPhy.setScaleType(ScaleType.FIT_XY);
////				desc.setFocusable(true);
//			}
//
//		}
		
		if(requestCode==CAMERA_IMAGE_CAPTURE && resultCode==Activity.RESULT_OK){
			path	= getThubnailFilePath() ;
			Options options = new Options() ;
			Bitmap bitmap = GlobalMethods.decodeFile(path);
			
			if (bitmap == null) {
				ivPhy.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} else {
				ivPhy.setImageBitmap(bitmap);
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
	 * Get String path from {@link Uri}
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
		} 
		return StringPath;
	}
	
	public void onClick(View v) {
		fnm = etFnm.getText().toString();
		lnm = etLnm.getText().toString();
		unm = etUnm.getText().toString();
		pwd = etPwd.getText().toString();
		zip = etZip.getText().toString();
		address = etAdd.getText().toString();
		country = autoCountryList.getText().toString();
		// country=spCountry.getSelectedItemPosition();

		if (flag == true) {
			state = autoStateList.getText().toString();
			System.out.println("state spiner~~~~~~~" + state);
		} else {
			state = etState.getText().toString();
			System.out.println("state editbox~~~~~~~" + state);
		}
		city = etCity.getText().toString();
		uType = spType.getSelectedItem().toString();
		int pos = spType.getSelectedItemPosition();
		Intent intent;
		switch (v.getId()) {
		case R.id.regimagivew:
			registerForContextMenu(v);
			openContextMenu(v);
			break;
		case R.id.btnCan:
			RegisterScreen.this.finish();
			break;

		case R.id.btnReg:
			if (fnm.equalsIgnoreCase("") || lnm.equalsIgnoreCase("")
					|| unm.equalsIgnoreCase("") || pwd.equalsIgnoreCase("")
					|| pos == 0) {
				Toast.makeText(
						RegisterScreen.this,
						getResources().getString(
								R.string.feild_require_register),
						Toast.LENGTH_LONG).show();
			} else {
				if (db.notAlreadyExist(unm)) {
					
					if (spType.getSelectedItem().toString()
							.equalsIgnoreCase("User/Patient")) {	

						loginDialog.setMessage(getResources().getString(
								R.string.store_user_login));
					loginMessage = loginDialog.create();
						loginMessage.show();
					} else {
						db.insertUser(path, fnm, lnm, unm, pwd, zip, address,
								country, state, city, uType);
						ConfigureScreen.userType = 1;
						intent = new Intent(RegisterScreen.this,
								LoginScreen.class);
						startActivity(intent); 
						RegisterScreen.this.finish();
					}

					SharedPreferences spe = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					Editor editer = spe.edit();
					editer.putString("isLogin", "yes");
					editer.commit();

				} else {
					Toast.makeText(
							RegisterScreen.this,
							getResources().getString(
									R.string.alreday_exist_register),
							Toast.LENGTH_LONG).show();
				}
			}
			break;

		case R.id.tv_reg_login:
			intent = new Intent(RegisterScreen.this, LoginScreen.class);
			startActivity(intent);
			RegisterScreen.this.finish();
			break;
		}
	}

}