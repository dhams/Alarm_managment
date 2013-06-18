package com.medplan.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
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
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medpan.util.Constant;
import com.medpan.util.GlobalMethods;
import com.medpan.util.Medicine_Model;
import com.medpan.util.Picture_Model;
import com.medplan.db.databasehelper;

public class MediManage_AddShowActivity extends Activity implements
		OnClickListener {
	LinearLayout addButton, showButton;
	EditText etName, etSDesc, etLDesc, etUsedAs, etAIng, etTherapeutic, etInto,
			etCaution, etNote1, etNote2;
	Context ctx;

	databasehelper db;

	ArrayList<Medicine_Model> mediList,mediMainList;
	Button add, cancel, back, update, delete,next,prev;
	ImageView imgMed;

	String name, sdesc, ldesc, usedAs, aing, therapeutic, intolerancem,
			caution, note1, note2, mtherap;
	// float dosage;
	int id, picid, medi_user_id, cat, dosage, dosageType, dosagetime, dunit,route_admin,
			toxicity,counter = 0;
	ImageView headLogo;
	TextView headerTitle;
	public static Spinner spDos, spDosTime, spDosType, spToxicity,spRoute_admin;
	RelativeLayout titleHeadLayout;
	LinearLayout MainBgLayout;
	String doWhat = "";
	public static ArrayAdapter DosageTypeAdapter;
	String[] MilliLiter = { "Spoon", "Tea spoon ", "Small measure" };
	String[] medType = {"NR"};
	static String _path;
	ArrayList<Picture_Model> picList;
	AlertDialog alertMsg;
	AlertDialog.Builder alertDialog,ad1,ad2;
	boolean flagCamera;
	int userid;
	private static final int CAMERA_IMAGE_CAPTURE = 0;
	private  Uri imageCaptureUri  ,unknowndeviceUri; 
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		doWhat = this.getIntent().getStringExtra("Task");
		setContentView(R.layout.medimanage_addshow);
		
		 userid = this.getIntent().getIntExtra("mid", -1);
		Log.i("uid ad sho", "--" + userid);

		RelativeLayout rl = (RelativeLayout)findViewById(R.id.prevnext);
		
		ctx = MediManage_AddShowActivity.this;

		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.med_mgt);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.med_mgt);
		
		next = (Button) findViewById(R.id.btnNextMedi);
		next.setOnClickListener(this);
		prev = (Button) findViewById(R.id.btnPrevMedi);
		prev.setOnClickListener(this);

		titleHeadLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		MainBgLayout = (LinearLayout) findViewById(R.id.mainMedLayout);

		try {
			SplashScreen.Cmethod.CheckAddShowScreen(Constant._StrCheck,
					titleHeadLayout, MainBgLayout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(MediManage_AddShowActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});

		db = new databasehelper(this);
		medi_user_id = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getInt("UserID", -1);

		addButton = (LinearLayout) findViewById(R.id.btnForAdd);
		showButton = (LinearLayout) findViewById(R.id.btnForShow);

		etName = (EditText) findViewById(R.id.etName);
		etSDesc = (EditText) findViewById(R.id.etSDesc);
		etLDesc = (EditText) findViewById(R.id.etLDesc);
		etUsedAs = (EditText) findViewById(R.id.etUsedAs);
		etAIng = (EditText) findViewById(R.id.etAIng);
		etInto = (EditText) findViewById(R.id.etIntolerance);
		etCaution = (EditText) findViewById(R.id.etCaution);
		etNote1 = (EditText) findViewById(R.id.et_Med_Note1);
		etNote2 = (EditText) findViewById(R.id.et_Med_Note2);
		etTherapeutic = (EditText) findViewById(R.id.etTherapeutic);
		

		add = (Button) findViewById(R.id.btnMediAdd);
		add.setOnClickListener(this);
		cancel = (Button) findViewById(R.id.btnMediCan);
		cancel.setOnClickListener(this);
		delete = (Button) findViewById(R.id.btnMediDel);
		delete.setOnClickListener(this);
		update = (Button) findViewById(R.id.btnMediUp);
		update.setOnClickListener(this);
		back = (Button) findViewById(R.id.btnMediBk);
		back.setOnClickListener(this);

		imgMed = (ImageView) findViewById(R.id.imageMed);
		imgMed.setOnClickListener(this);

		// spCat = (Spinner) findViewById(R.id.spCategory);
		spDos = (Spinner) findViewById(R.id.spDos);
		spDosType = (Spinner) findViewById(R.id.spDos_type);
		spDosTime = (Spinner) findViewById(R.id.spDosTime);
		spToxicity = (Spinner) findViewById(R.id.spToxicity);
		spRoute_admin= (Spinner) findViewById(R.id.spRouteAdmin);

		String[] items3 = {" ","Daily before meals", "Daily after meals",
				"Daily just waken up", "Daily before to sleep",
				"Daily every X hours", "Daily at certain time", "Weekly",
				"Monthly","See Physician recipe ","Others" };

		// ArrayAdapter aa = new
		// ArrayAdapter(this,android.R.layout.simple_spinner_item, items1);
		// aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// spCat.setAdapter(aa);

		ArrayAdapter cc = new ArrayAdapter(this,android.R.layout.simple_spinner_item, items3);
		cc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spDosTime.setAdapter(cc);

//		DosageTypeAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, pills);
//		DosageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spDos.setAdapter(DosageTypeAdapter);

		spDos.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(arg2==9){
					DosageTypeAdapter = new ArrayAdapter(ctx,
					android.R.layout.simple_spinner_item, MilliLiter);
			DosageTypeAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spDosType.setAdapter(DosageTypeAdapter);
			
				}
				else if(arg2==0){
					//do nothing
				}
				else{
					DosageTypeAdapter = new ArrayAdapter(ctx,
							android.R.layout.simple_spinner_item, medType);
					DosageTypeAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spDosType.setAdapter(DosageTypeAdapter);
					
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

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
				mediList = db.getMedicals(medi_user_id);
				mediMainList = mediList;
			} else {
				mediList = db.getMedical(userid);
				counter = this.getIntent().getIntExtra("counter",0);
				mediMainList = db.getMedicals(medi_user_id);
			}
			
			if (mediList.size() > 0) {
				showButton.setVisibility(View.VISIBLE);
				fillIsTheForm(mediList.get(0));
				
			} else {
				rl.setVisibility(View.GONE);
				addButton.setVisibility(View.VISIBLE);
				Toast.makeText(MediManage_AddShowActivity.this,
						R.string.no_medicine_save_yet_med_mgt,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			rl.setVisibility(View.GONE);
			addButton.setVisibility(View.VISIBLE);
			spDos.setSelection(0);
			spDosTime.setSelection(0);
		}

	}
	
	public void fillIsTheForm(Medicine_Model med)
	{
		
		Log.i("","dosage "+med.dosage+"   dosageType "+med.dosageunit+"   dosageTime "+med.dosagetime+"  toxicity "+med.toxicity+"  route "+med.route);

		int arg2 = Integer.parseInt(med.dosage);
		try{
		spDos.setSelection(arg2);
		spDosType.setSelection(Integer.parseInt(med.dosageunit));
		spDosTime.setSelection(Integer.parseInt(med.dosagetime));

		}
			catch (Exception e) {
			e.printStackTrace();
		}
		
		id = med.mid;
	    picid=med.mpicid;
		
		spToxicity.setSelection(Integer.parseInt(med.toxicity));
		spRoute_admin.setSelection(med.route);
		etTherapeutic.setText(med.mtherap);
		etUsedAs.setText(med.cat);
		etName.setText(med.nm);
		etSDesc.setText(med.sdesc);
		etLDesc.setText(med.ldesc);
		etAIng.setText(med.ingridiant);
		etInto.setText(med.mintolerance);
		etCaution.setText(med.caution);
		etNote1.setText(med.note1);
		etNote2.setText(med.note2);
	    
	    
	    Picture_Model picmodel = db.getPicture(picid);
		
	    	 Bitmap bitmap = GlobalMethods.decodeFile(picmodel.path);
	 		if (bitmap == null) {
	 			imgMed.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
	 		} else {
	 			imgMed.setImageBitmap(bitmap);
	 			imgMed.setScaleType(ScaleType.FIT_XY);
	 		}
	   
	}



	public void onClick(View v) {
		name = etName.getText().toString();
		sdesc = etSDesc.getText().toString();
		ldesc = etLDesc.getText().toString();
		aing = etAIng.getText().toString();
		usedAs = etUsedAs.getText().toString();
		therapeutic = etTherapeutic.getText().toString();
		intolerancem = etInto.getText().toString();
		caution = etCaution.getText().toString();
		note1 = etNote1.getText().toString();
		note2 = etNote2.getText().toString();
		dosage = spDos.getSelectedItemPosition();
		dosageType = spDosType.getSelectedItemPosition();
		dosagetime = spDosTime.getSelectedItemPosition();
		toxicity = spToxicity.getSelectedItemPosition();
		route_admin=spRoute_admin.getSelectedItemPosition();
		

		Log.i("","dosage "+dosage+"   dosageType "+dosageType+"   dosageTime "+dosagetime+"  toxicity "+toxicity+"  route "+route_admin);
		switch (v.getId()) {
		case R.id.btnNextMedi:
			if (counter == (mediMainList.size() - 1)) {
				Toast.makeText(MediManage_AddShowActivity.this,
						R.string.last_record, Toast.LENGTH_SHORT).show();

			} else {
				
				if (counter < (mediMainList.size() - 1))
					counter++;
				
				fillIsTheForm(mediMainList.get(counter));
			}
			break;
		case R.id.btnPrevMedi:
			if (counter == 0) {
				Toast.makeText(MediManage_AddShowActivity.this,
						R.string.first_record, Toast.LENGTH_SHORT).show();
	
			} else {
				
				if (counter > 0)
					counter--;
				
				fillIsTheForm(mediMainList.get(counter));
			}
			break;
		case R.id.imageMed:
			registerForContextMenu(v);
			openContextMenu(v);
			break;
		case R.id.btnMediAdd:
			if (!(name.equals("")  || sdesc.equals(""))) {
				
				
				if(flagCamera==true){
					//insertation in picture management
				     db.insertPicture(medi_user_id, _path, sdesc, 2, note1,note2);
				     flagCamera=false;
//				     ArrayList<Picture_Model> picsTemp = db.getPicture(id)
					//	picid = picsTemp.get(picsTemp.size() - 1).id;
				     picid = db.getPictureID(_path);
				}
				
				db.insertMedical(medi_user_id, picid, name, sdesc, ldesc,
						usedAs, aing, dosage,dosageType,dosagetime,  therapeutic,
						intolerancem, caution, toxicity, note1, note2,route_admin);
				Toast.makeText(MediManage_AddShowActivity.this,R.string.medcine_save_med_mgt, Toast.LENGTH_LONG).show();
				
				this.finish();
			} else
				Toast.makeText(
						MediManage_AddShowActivity.this,
						R.string.feild_require_med_mgt,
						Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnMediUp:
			if (!(name.equals("") || sdesc.equals("")))
			{
				
				if(flagCamera==true){
					//insertation in picture management
				     db.insertPicture(medi_user_id, _path, sdesc, 2, note1,note2);
				     flagCamera=false;
//				     ArrayList<Picture_Model> picsTemp = db.getPictures(medi_user_id);
//						picid = picsTemp.get(picsTemp.size() - 1).id;
				     picid = db.getPictureID(_path);
				}
				
				db.updateMedical(medi_user_id, id, picid, name, sdesc, ldesc,
						usedAs, aing,  dosage, dosageType, dosagetime,   therapeutic,
						intolerancem, caution, toxicity, note1, note2,route_admin);
				    Toast.makeText(MediManage_AddShowActivity.this,R.string.medicine_update_med_mgt, Toast.LENGTH_LONG).show();
				
				this.finish();
			}
			else
				Toast.makeText(
						MediManage_AddShowActivity.this,
						R.string.feild_require_med_mgt,
						Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnMediDel:
			if(db.getFormCountMedi(id)>0)
			{
			ad1 = new AlertDialog.Builder(this);
			ad1.setTitle("Warning");
			ad1.setMessage(R.string.delete_warningmed);
			ad1.setPositiveButton("Next",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							ad2 = new AlertDialog.Builder(MediManage_AddShowActivity.this);
							ad2.setTitle("Warning");
							ad2.setMessage(R.string.delete_messagemed);
							ad2.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface dialog, int which) {

											db.deleteMedical(id);
											db.deleteMediForm(id);
											Toast.makeText(MediManage_AddShowActivity.this,
													R.string.medicine_delete_med_mgt, Toast.LENGTH_LONG).show();
											MediManage_AddShowActivity.this.finish();

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
				db.deleteMedical(id);
				db.deleteMediForm(id);
				Toast.makeText(MediManage_AddShowActivity.this,
						R.string.medicine_delete_med_mgt, Toast.LENGTH_LONG).show();
				MediManage_AddShowActivity.this.finish();
			}
			break;
		case R.id.btnMediBk:
		case R.id.btnMediCan:
			MediManage_AddShowActivity.this.finish();
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
//			if (requestCode == 1212) {
//			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
//			Bitmap bitmap;
//			//bitmap=GlobalMethods.decodeSampledBitmapFromResource(_path, 80, 80);
//			bitmap=GlobalMethods.decodeFile(_path);
//			if (bitmap == null) {
//				imgMed.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
//			} 
//			else {
//				imgMed.setImageBitmap(bitmap);
//				flagCamera=true;
//			}
//		}
		if (requestCode == 111) { 
			picid = data.getIntExtra("pid", 0);
			Picture_Model picmodel = db.getPicture(picid);
			//Bitmap bitmap = BitmapFactory.decodeFile(picmodel.path);
			Bitmap bitmap = GlobalMethods.decodeFile(picmodel.path);
			if (bitmap == null) {
				imgMed.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} else {
				//imgMed.setImageBitmap(GlobalMethods.decodeSampledBitmapFromResource(picmodel.path, 80, 80));
				imgMed.setImageBitmap(bitmap);
			}
			 _path=picmodel.path;
		}
		
		if(requestCode==CAMERA_IMAGE_CAPTURE && resultCode==Activity.RESULT_OK){
			_path	= getThubnailFilePath() ;
			Bitmap bitmap = GlobalMethods.decodeFile(_path);
			
			if (bitmap == null) {
				imgMed.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} else {
				imgMed.setImageBitmap(bitmap);
			}
		}
	
		
	}
	
	/**
	 * Get the path of captured  image by camera 
	 * @return
	 */
	private String getThubnailFilePath() {

		String thumbnailPath = "";

		try {
			String[] projection = {
					MediaStore.Images.Thumbnails._ID, // The columns we want
					MediaStore.Images.Thumbnails.IMAGE_ID,
					MediaStore.Images.Thumbnails.KIND,
					MediaStore.Images.Thumbnails.DATA };
			String selection = MediaStore.Images.Thumbnails.KIND + "=" + // Select
																			// only
																			// mini's
					MediaStore.Images.Thumbnails.MINI_KIND;

			String sort = MediaStore.Images.Thumbnails._ID + " DESC";

			Cursor myCursor = this.managedQuery(
					MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
					projection, selection, null, sort);

			myCursor.moveToFirst();
			thumbnailPath = myCursor.getString(myCursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));

			unknowndeviceUri = Uri.fromFile(new File(thumbnailPath));
			imageCaptureUri = null;
		} catch (Exception e) {
			unknowndeviceUri = null;
			e.printStackTrace();
		}
		
		if (unknowndeviceUri != null)
			return unknowndeviceUri.getPath();
		else
			return imageCaptureUri.getPath();
	}
	
	
	public void Shot_Camera(){
//		try {
////			Calendar cal = Calendar.getInstance();
////			_path = Environment.getExternalStorageDirectory() + File.separator
////					+ "TakenFromCamera" + cal.getTimeInMillis() + ".png";
//			
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
//			File file = new File(_path);
//			Uri outputFileUri = Uri.fromFile(file);
//			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//			startActivityForResult(intent, 1212);	
//			flagCamera=true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
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
		} else {
			new AlertDialog.Builder(MediManage_AddShowActivity.this)
					.setMessage(
							"External Storeage (SD Card) is required.\n\nCurrent state: "
									+ storageState).setCancelable(true)
					.create().show();
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
			medi_user_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("UserID", 0);
			picList = db.getPictures(medi_user_id);
			System.out.println("array size~~~~~~"+picList.size());
			if (picList.size() == 0) {
				alertDialog.show();
				
				
			}else{
			Intent intent = new Intent(MediManage_AddShowActivity.this,PicManage_ListActivity.class);
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