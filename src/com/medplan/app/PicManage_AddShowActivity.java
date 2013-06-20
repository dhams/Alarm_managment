package com.medplan.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.R.id;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.medpan.util.Constant;
import com.medpan.util.GlobalMethods;
import com.medpan.util.Picture_Model;
import com.medplan.db.databasehelper;

public class PicManage_AddShowActivity extends Activity implements
		OnClickListener {
	LinearLayout addButton, showButton;
	EditText desc, etnote1, etnote2;
	ImageView iv;
	Button add, update, delete, cancel, back,next,prev;
	databasehelper db;
	String _path="";
	Spinner spCat;
	String  description = "", note1 = "", note2 = "",doWhat;
	static ArrayList<Picture_Model> pics = new ArrayList<Picture_Model>();
	static ArrayList<Picture_Model> picsMainList = new ArrayList<Picture_Model>();
	int gotId, category=0,counter = -1;
	private static final String TEMP_PHOTO_FILE = "tempPhoto.jpg";
	private static final String MY_CALLBACK_ID = null;
	Picture_Model pics_temp;
	ImageView headLogo;
	TextView headerTitle;
	int userid,width,height,setwidth,setheight,id;
	RelativeLayout titleHeadLayout;
	LinearLayout MainBgLayout;
	AlertDialog.Builder ad1,ad2;
	int tid;
	private static final int CAMERA_IMAGE_CAPTURE = 0;
	private  Uri imageCaptureUri  ,unknowndeviceUri; 
	

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		doWhat = this.getIntent().getStringExtra("Task");
		setContentView(R.layout.picmanage_addshow);
		
		Display display = getWindowManager().getDefaultDisplay(); 
		 width = display.getWidth();  // deprecated
		 height = display.getHeight();  // deprecated
		 
		
		
		RelativeLayout rl = (RelativeLayout)findViewById(R.id.prevnext);


		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.picture_mgt);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.pic_mgt);

		titleHeadLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		MainBgLayout = (LinearLayout) findViewById(R.id.mainPicLayout);

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
				Intent intent = new Intent(PicManage_AddShowActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});

		db = new databasehelper(this);
		userid = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getInt("UserID", 0);
		addButton = (LinearLayout) findViewById(R.id.btnForAdd);
		showButton = (LinearLayout) findViewById(R.id.btnForShow);
		
		next = (Button) findViewById(R.id.btnNextPic);
		next.setOnClickListener(this);
		prev = (Button) findViewById(R.id.btnPrevPic);
		prev.setOnClickListener(this);

		iv = (ImageView) findViewById(R.id.imageUser);
		iv.setOnClickListener(this);
		add = (Button) findViewById(R.id.btnAdd);
		add.setOnClickListener(this);
		update = (Button) findViewById(R.id.btnUpdate);
		update.setOnClickListener(this);
		delete = (Button) findViewById(R.id.btnDelete);
		delete.setOnClickListener(this);
		back = (Button) findViewById(R.id.btnBack);
		back.setOnClickListener(this);
		cancel = (Button) findViewById(R.id.btnCancel);
		cancel.setOnClickListener(this);
		desc = (EditText) findViewById(R.id.etDesc);

		etnote1 = (EditText) findViewById(R.id.etNote1);
		etnote2 = (EditText) findViewById(R.id.etNote2);
		
		if(height <= 480){
			System.out.println("~~~~~~~~~~~~~~~~~~~~MDP~~~~~~~");
			setwidth=100;
			setheight=100;
		} 
		else if(height>=481){
			System.out.println("~~~~~~~~~~~~~~~~~~~~HDP~~~~~~~");
			setwidth=250;
			setheight=250;
			
		}

		spCat = (Spinner) findViewById(R.id.spPicCat);

		if (doWhat.equalsIgnoreCase("show")) {
			rl.setVisibility(View.VISIBLE);
			gotId = this.getIntent().getIntExtra("idtoshow", -1);
			if (gotId == -1) {
				pics = db.getPictures(userid);
				picsMainList = pics;
				if (pics.size() > 0) {
					showButton.setVisibility(View.VISIBLE);
					fillIsTheForm(pics.get(0));
					counter = 0;
					
				} else {
					rl.setVisibility(View.INVISIBLE);
					addButton.setVisibility(View.VISIBLE);
					spCat.setSelection(0);
					Toast.makeText(PicManage_AddShowActivity.this,
							R.string.no_picture_pic_mgt,
							Toast.LENGTH_SHORT).show();
				}
			} else {
				pics_temp = db.getPicture(gotId);
				picsMainList = db.getPictures(userid);
				showButton.setVisibility(View.VISIBLE);
				fillIsTheForm(pics_temp);
				counter = this.getIntent().getIntExtra("counter", 0);
			

			}
		} else { 
			rl.setVisibility(View.INVISIBLE);
			addButton.setVisibility(View.VISIBLE);
			spCat.setSelection(0);
		}

	}

	public void fillIsTheForm(Picture_Model pic)
	{
	

		id = pic.id;
		spCat.setSelection(Integer.parseInt(pic.category));
		_path = pic.path;
		
			Bitmap bitmap = GlobalMethods.decodeFile(_path);
			if (bitmap == null) {
				iv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} else {
				iv.setImageBitmap(bitmap);
				iv.setScaleType(ScaleType.FIT_XY);
			}
		
		

		desc.setText(pic.desc);
		etnote1.setText(pic.note1);
		etnote2.setText(pic.note2);
	}
	
	public void onClick(View v) {
		
		db.openDataBase() ;
		
		description = desc.getText().toString();
		category = spCat.getSelectedItemPosition();
		note1 = etnote1.getText().toString();
		note2 = etnote2.getText().toString();
		switch (v.getId()) {
		case R.id.btnNextPic:
			if(counter==(picsMainList.size()-1))
			{
				Toast.makeText(PicManage_AddShowActivity.this,
						R.string.last_record, Toast.LENGTH_SHORT).show();

			}
			else
			{
				
				if (counter < (picsMainList.size() - 1))
					counter++;
				
				fillIsTheForm(picsMainList.get(counter));
			}
			break;
			
		case R.id.btnPrevPic:
			if (counter == 0) {
				Toast.makeText(PicManage_AddShowActivity.this,
						R.string.first_record, Toast.LENGTH_SHORT).show();
	
			} else {
				
				if (counter > 0)
					counter--;
				
				fillIsTheForm(picsMainList.get(counter));
			}
			break;
		case R.id.imageUser:
			registerForContextMenu(v);
			openContextMenu(v);
			break;

		case R.id.btnAdd:
			if (!(_path.equals("") || description.equals("")|| category==0)) {
				db.insertPicture(userid, _path, description, category, note1,
						note2);
				Toast.makeText(PicManage_AddShowActivity.this,
					R.string.picture_save_pic_mgt, Toast.LENGTH_LONG).show();
				this.finish();
			} else {
				Toast.makeText(PicManage_AddShowActivity.this,
						R.string.feild_require_pic_mgt,
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnUpdate:
			if (!(_path.equals("") || description.equals("")|| category==0)) {
				db.updatePicture(userid,id, _path, description, category, note1,note2);
				Toast.makeText(PicManage_AddShowActivity.this,
						R.string.picture_update_pic_mgt, Toast.LENGTH_LONG).show();
				this.finish();
			} else
				Toast.makeText(PicManage_AddShowActivity.this,
						R.string.feild_1_require_pic_mgt,
						Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnDelete:
			
			if (gotId == 0) {
				tid = pics.get(0).id;
				
			} else {
				tid = picsMainList.get(counter).id;
				
			}
			if(db.getUserCountPic(tid)>0||db.getPhyCountPic(tid)>0||db.getMediCountPic(tid)>0)
			{
			ad1 = new AlertDialog.Builder(this);
			ad1.setTitle("Warning");
			ad1.setMessage(R.string.delete_warningpic);
			ad1.setPositiveButton("Next",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							ad2 = new AlertDialog.Builder(PicManage_AddShowActivity.this);
							ad2.setTitle("Warning");
							ad2.setMessage(R.string.delete_messagepic);
							ad2.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface dialog, int which) {
											db.deletePicture(tid);
											Toast.makeText(PicManage_AddShowActivity.this,
													R.string.picture_delete_pic_mgt, Toast.LENGTH_LONG).show();
											PicManage_AddShowActivity.this.finish();
											

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
				db.deletePicture(tid);
				Toast.makeText(PicManage_AddShowActivity.this,
						R.string.picture_delete_pic_mgt, Toast.LENGTH_LONG).show();
				PicManage_AddShowActivity.this.finish();
			}
			break;
		case R.id.btnBack:
		case R.id.btnCancel:
			PicManage_AddShowActivity.this.finish();
			break;

		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK && requestCode == 1) {
 
			Uri contentUri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String tmppath = cursor.getString(column_index);
			//Bitmap mBitmap = BitmapFactory.decodeFile(tmppath);
			_path = tmppath;
			Bitmap bitmap;
			//bitmap=GlobalMethods.decodeSampledBitmapFromResource(path, setwidth, setheight);
			bitmap=GlobalMethods.decodeFile(_path);
			iv.setImageBitmap(bitmap);
			desc.setFocusable(true);

		}
		if (requestCode == 1212) {
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
			Bitmap bitmap;
			//bitmap=GlobalMethods.decodeSampledBitmapFromResource(_path, 80, 80);
			bitmap=GlobalMethods.decodeFile(_path);
			if (bitmap == null) {
				iv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} else {
				iv.setImageBitmap(bitmap);
				iv.setScaleType(ScaleType.FIT_XY);
				desc.setFocusable(true);
				_path = _path;
			}

		}
		
		
		if(requestCode==CAMERA_IMAGE_CAPTURE && resultCode==Activity.RESULT_OK){
			_path	= getThubnailFilePath() ;
			Bitmap bitmap = GlobalMethods.decodeFile(_path);
			
			if (bitmap == null) {
				iv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_photo));
			} else {
				iv.setImageBitmap(bitmap);
			}
		}
		
	}
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
		
//			_path = Environment.getExternalStorageDirectory() + File.separator+ "TakenFromCamera" + cal.getTimeInMillis() + ".png";
			 
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
			Intent gallery = new Intent();
			gallery.setType("image/*"); // sets the return type to IMAGE
			gallery.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(gallery, "Select Picture"), 1);

			break;
		}
		return false;
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
			new AlertDialog.Builder(PicManage_AddShowActivity.this)
					.setMessage(
							"External Storeage (SD Card) is required.\n\nCurrent state: "
									+ storageState).setCancelable(true)
					.create().show();
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
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.select);
		menu.add(0, 0, 0, R.string.take_new_pic_mgt);
		menu.add(0, 1, 0, R.string.pic_gallery_pic_mgt);

	}
}
