package com.medplan.app;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medpan.util.Constant;
import com.medpan.util.GlobalMethods;
import com.medplan.db.databasehelper;

public class RegisterScreen extends Activity implements OnClickListener {
	Button cancelButton, registerButton;
	EditText etFnm, etLnm, etUnm, etPwd, etZip, etAdd, etState, etCity;
	String fnm, lnm, unm, pwd, zip, address, city, uType, country, state;
	int type = 1;
	databasehelper db;
	Spinner spType;
	TextView tvLogin;
	static String _path, path = "";
	ImageView ivPhy;
	String[] items1 = new String[] { " ", "User/Patient", "Doctor/Physician",
			"Pharma" };
	AutoCompleteTextView autoCountryList, autoStateList;
	boolean flag;
	static AlertDialog loginMessage;
	static AlertDialog.Builder loginDialog;
	public static SharedPreferences SP;
	private Bitmap bitmap=null;
	
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

				SharedPreferences.Editor editor = SplashScreen.SP.edit();
				editor.putString("OperationType", spType.getSelectedItem()
						.toString());
				editor.commit();

				// Constant._StrCheck = SP.getString("OperationType", "");

				Constant._StrCheck = spType.getSelectedItem().toString();
				// System.out.println("user tpye~~~~~~~~~~~~~"+
				// spType.getSelectedItem().toString());

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
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
	
	public void Shot_Camera() {
		try { 
//			   Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
//               startActivityForResult(cameraIntent, 1212); 
			
			
//			_path=  "/sdcard/Medpalnn";
//			 File sdImageMainDirectory = new File(_path);
//	                if (!sdImageMainDirectory.exists()) {
//	                    sdImageMainDirectory.mkdirs();
//	                }
//	                if (!sdImageMainDirectory.isDirectory()) {
//	                	_path = "/sdcard";
//	        			}
	                
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
		
			
			//_path = Environment.getExternalStorageDirectory() + File.separator+ "TakenFromCamera" + cal.getTimeInMillis() + ".png";
			System.out.println("thumbnail path~~~~~~"+_path);
			File file = new File(_path);
			Uri outputFileUri = Uri.fromFile(file);
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			startActivityForResult(intent, 1212);
		} catch (Exception e) {
			e.printStackTrace();
		}

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

			break;
		case 1:
			Intent gallery = new Intent();
			gallery.setType("image/*"); // sets the return type to IMAGE
			gallery.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(gallery, "Select Picture"), 1);

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
		if (resultCode == RESULT_OK && requestCode == 1) {

			Uri contentUri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String tmppath = cursor.getString(column_index);
			// Bitmap mBitmap = BitmapFactory.decodeFile(tmppath);
			//ivPhy.setImageBitmap(GlobalMethods.decodeSampledBitmapFromResource(tmppath, 80, 80));
			
			Bitmap bitmap 
			= GlobalMethods.decodeFile(tmppath);
			ivPhy.setImageBitmap(bitmap);
			// ivPhy.setImageBitmap(mBitmap);
			path = tmppath;
		}
		if (requestCode == 1212) {
			
//			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED));
////			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
////			   Bitmap photo = (Bitmap) data.getExtras().get("data"); 
////			   ivPhy.setImageBitmap(photo);
//			   
////			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
////			Bitmap bitmap;
//			//bitmap=GlobalMethods.decodeSampledBitmapFromResource(_path, 80, 80);
//			bitmap=GlobalMethods.decodeFile(_path);
////			bitmap=null;
//			
//			if (bitmap == null) { 
//				ivPhy.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
//                try { 
//					GMailSender sender = new GMailSender("android.testapps@gmail.com", "androidandroid");
//					sender.sendMail("Resgister image",   
//					        "Registration screen , image has not displayed ",   
//					        "android.testapps@gmail.com",   
//					        "dharmendra@openxcelltechnolabs.com");
//				} catch (Exception e) {
//					e.printStackTrace();
//				} 
//			} 
//			else {
////                try { 
////					GMailSender sender = new GMailSender("android.testapps@gmail.com", "androidandroid");
////					sender.sendMail("Resgister image",   
////					        "Registration screen , image has not displayed ",   
////					        "android.testapps@gmail.com",   
////					        "dharmendra@openxcelltechnolabs.com");
////				} catch (Exception e) {
////					e.printStackTrace();
////				}  
////				ivPhy.setImageBitmap(bitmap);
////				ivPhy.setScaleType(ScaleType.FIT_XY);
//			}
			
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
			Bitmap bitmap; 
			//bitmap=GlobalMethods.decodeSampledBitmapFromResource(_path, 80, 80);
			bitmap=GlobalMethods.decodeFile(_path);
			if (bitmap == null) {
				try { 
//					ivPhy.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
					GMailSender sender = new GMailSender("android.testapps@gmail.com", "androidandroid");
					sender.sendMail("Resgister image",   
					    "Registration screen , image has not displayed ",   
					    "android.testapps@gmail.com",   
					    "dharmendra@openxcelltechnolabs.com");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				ivPhy.setImageBitmap(bitmap);
				ivPhy.setScaleType(ScaleType.FIT_XY);
				path = _path;
//				desc.setFocusable(true);
			}

		
		}
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