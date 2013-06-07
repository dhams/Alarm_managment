package com.medplan.app;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medpan.util.Constant;
import com.medpan.util.GlobalMethods;
import com.medpan.util.Medicine_Model;
import com.medpan.util.Picture_Model;
import com.medpan.util.User_Model;

import com.medplan.db.databasehelper;

public class MediManage_ListActivity extends Activity implements
		OnClickListener {
	ListView l1;
	EditText etSearch;
	databasehelper db;
	ArrayList<Medicine_Model> userList,tempUserList;
	RelativeLayout searchView;
	Button find;
	static int length;
	String doWhat="",fromWhere="";
	ImageView headLogo;
	TextView headerTitle;
	int user_id,key=-1;
	RelativeLayout titleHeadLayout;
	LinearLayout MainCommonLayout;
	int width,height,setwidth,setheight;
	
	@Override
	public void onBackPressed() {
		if(fromWhere.equalsIgnoreCase("Other"))
		{
			Intent i = new Intent();
			i.putExtra("mid", -1);

			setResult(RESULT_OK, i);
			finish();
		}
		finish();
	}
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		doWhat = this.getIntent().getStringExtra("Task");
		setContentView(R.layout.listuser);
		
		Display display = getWindowManager().getDefaultDisplay(); 
		 width = display.getWidth();  // deprecated
		 height = display.getHeight();  // deprecated
		 
		 if(height <= 480){
				System.out.println("~~~~~~~~~~~~~~~~~~~~MDP~~~~~~~");
				setwidth=40;
				setheight=40;
			}
			else if(height>=481){
				System.out.println("~~~~~~~~~~~~~~~~~~~~HDP~~~~~~~");
				setwidth=70;
				setheight=70;
			}

		
		
		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MediManage_ListActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
		
		user_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("UserID", -1);
		Log.i("User ID List", "List "+user_id);	  
		
		headLogo=(ImageView)findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.med_mgt);
		
		headerTitle=(TextView)findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.med_mgt);

		

		titleHeadLayout=(RelativeLayout)findViewById(R.id.rlHeadTitlelayout);
		MainCommonLayout=(LinearLayout)findViewById(R.id.MainCommonLayout);
		
		try {
			SplashScreen.Cmethod.CheckAddShowScreen(Constant._StrCheck, titleHeadLayout, MainCommonLayout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			  
		db = new databasehelper(this);
		userList = db.getMedicals(user_id);
		l1 = (ListView) findViewById(R.id.list);
		searchView = (RelativeLayout) findViewById(R.id.rlSearch);
		etSearch = (EditText) findViewById(R.id.editText1);
        etSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
		
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {

						if(!etSearch.getText().toString().equalsIgnoreCase(""))
						{
						
							tempUserList = new ArrayList<Medicine_Model>();
							String text = etSearch.getText().toString();
							for(int i=0;i<userList.size();i++)
							{
								if(key==-1)
								{
								if(userList.get(i).all.toUpperCase().contains(text.toUpperCase()))
								{
//									if (userList.get(i).all.toUpperCase().indexOf(
//											text.toUpperCase()) == 0
//											|| userList.get(i).all.charAt((userList
//													.get(i).all.toUpperCase()
//													.indexOf(text.toUpperCase())) - 1) == ' ')
									{
									tempUserList.add(userList.get(i));
									}
								}
								}
								else if(key==0)
								{
									if(userList.get(i).nm.toUpperCase().contains(text.toUpperCase()))
									{
//										if (userList.get(i).nm.toUpperCase().indexOf(
//												text.toUpperCase()) == 0
//												|| userList.get(i).nm.charAt((userList
//														.get(i).nm.toUpperCase()
//														.indexOf(text.toUpperCase())) - 1) == ' ')
										{
										tempUserList.add(userList.get(i));
										}
									}
									
								}
								else if(key==1)
								{
									if(userList.get(i).sdesc.toUpperCase().contains(text.toUpperCase()))
									{
//										if (userList.get(i).sdesc.toUpperCase().indexOf(
//												text.toUpperCase()) == 0
//												|| userList.get(i).sdesc.charAt((userList
//														.get(i).sdesc.toUpperCase()
//														.indexOf(text.toUpperCase())) - 1) == ' ')
										{
										tempUserList.add(userList.get(i));
										}
									}
								}
								else if(key==2)
								{
									if(userList.get(i).ingridiant.toUpperCase().contains(text.toUpperCase()))
									{
//										if (userList.get(i).ingridiant.toUpperCase().indexOf(
//												text.toUpperCase()) == 0
//												|| userList.get(i).ingridiant.charAt((userList
//														.get(i).ingridiant.toUpperCase()
//														.indexOf(text.toUpperCase())) - 1) == ' ')
										{
										tempUserList.add(userList.get(i));
										}
									}
								}
								
							}
							
							if(tempUserList.size()==0){
								Toast.makeText(MediManage_ListActivity.this,R.string.no_record_found, Toast.LENGTH_SHORT).show();
							}
						
						l1.setAdapter(new EfficientAdapter(getApplicationContext(),tempUserList));
						}
						else
						{
							tempUserList.clear();
							l1.setAdapter(new EfficientAdapter(getApplicationContext(),tempUserList));
						}
			}
		});
		if(this.getIntent().getStringExtra("Task")!=null)
		{
			doWhat = this.getIntent().getStringExtra("Task");
		}
		if(this.getIntent().getStringExtra("From")!=null)
		{
			doWhat="list";
			fromWhere = this.getIntent().getStringExtra("From");
			if(userList.size()==0)
			{
				Toast.makeText(getApplicationContext(), R.string.no_medicine_save_yet_med_mgt, Toast.LENGTH_LONG).show();
				Intent i = new Intent();
				i.putExtra("mid", -1);

				setResult(RESULT_OK, i);
				finish();
			}
		}
		if(userList.size()>0)
		{
		if (doWhat.equalsIgnoreCase("list")) {
			length = 10;
			l1.setAdapter(new EfficientAdapter(getApplicationContext(),userList));
		} else {
			length = 3;
			if(getIntent().getIntExtra("Key", -1)!=-1)
			{
				key = getIntent().getIntExtra("Key", 0);
				if(key == -1)
				{
					etSearch.setHint(R.string.enter_keyword);
				}
				else if(key == 0)
				{
					etSearch.setHint(R.string.enter_name);
				}
				else if(key == 1)
				{
					etSearch.setHint(R.string.enter_short);
				}
				else if(key == 2)
				{
					etSearch.setHint(R.string.enter_active);
				}
			}
			searchView.setVisibility(View.VISIBLE);
			find = (Button) findViewById(R.id.btnFind);
			find.setOnClickListener(this);
		}
		
		l1.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,  int arg2,
					long arg3) {
				if(fromWhere.equalsIgnoreCase("Other"))
				{
//					LayoutInflater inflater = getLayoutInflater();
//			        View view = inflater.inflate(R.layout.custom_toast,
//			                                       (ViewGroup) findViewById(R.id.llayout));
//			        Toast toast = new Toast(getApplicationContext());
//			        toast.setGravity(Gra	vity.FILL, 0, 0);
//			        toast.setView(view);
//			        toast.show();

				String mediname  =userList.get(arg2).nm  ; 	
					
				Log.d(" ***** MEdiManag *********", mediname);
				
					Intent i = new Intent();
					i.putExtra("mid", userList.get(arg2).mid);
					i.putExtra("mname", userList.get(arg2).nm);
					i.putExtra("picid", userList.get(arg2).mpicid);
					
					setResult(RESULT_OK, i);
					finish();		
					
//			        new Handler().post(new Runnable() {
//						
//						@Override
//						public void run() {
//							Intent i = new Intent();
//							i.putExtra("mid", userList.get(arg2).mid);
//							i.putExtra("mname", userList.get(arg2).nm);
//							i.putExtra("picid", userList.get(arg2).mpicid);
//							
//							setResult(RESULT_OK, i);
//							finish();		
//						}
//					});
					
				}
				else
				{
					if (doWhat.equalsIgnoreCase("list")) {
						Intent i = new Intent(MediManage_ListActivity.this,
								MediManage_AddShowActivity.class);
						i.putExtra("mid", userList.get(arg2).mid);
						i.putExtra("Task", "show");
						i.putExtra("counter", arg2);
						startActivity(i);
					}
					else
					{
					int temp = userList.indexOf(tempUserList.get(arg2));
					Intent i = new Intent(MediManage_ListActivity.this,
							MediManage_AddShowActivity.class);
					i.putExtra("mid", userList.get(temp).mid);
					i.putExtra("Task", "show");
					i.putExtra("counter", temp);
					startActivity(i);
					}
					MediManage_ListActivity.this.finish();
				}
			}
			
		});
		}
		else
		{
			Toast.makeText(getApplicationContext(), R.string.no_medicine_stored_med_mgt, Toast.LENGTH_SHORT).show();
			this.finish();
		}

		
	}

	private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<Medicine_Model> userList;
		public EfficientAdapter(Context context,ArrayList<Medicine_Model> userList) {
			mInflater = LayoutInflater.from(context);
			this.userList = userList;
		}
		
		public int getCount() {
			return userList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.listuserrow, null);
				holder = new ViewHolder();
				holder.uName = (TextView) convertView
						.findViewById(R.id.userName);
				holder.uType = (TextView) convertView
						.findViewById(R.id.userType);
				holder.uImage = (ImageView) convertView
						.findViewById(R.id.userImage);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		
			
			holder.uName.setText(userList.get(position).nm);
			holder.uType.setText(userList.get(position).sdesc);
			Picture_Model temp = db.getPicture(userList.get(position).mpicid);
			
				holder.uImage.setImageBitmap(GlobalMethods.decodeFile(temp.path));
				holder.uImage.setScaleType(ScaleType.FIT_XY);
				return convertView;

		}

	   class ViewHolder {

			TextView uName, uType;
			ImageView uImage;

		}
	}

	public void onClick(View v) {
		if (v.getId() == R.id.btnFind) {
			tempUserList = new ArrayList<Medicine_Model>();
			String text = etSearch.getText().toString();
			for(int i=0;i<userList.size();i++)
			{
				if(key==-1)
				{
				if(userList.get(i).all.toUpperCase().contains(text.toUpperCase()))
				{
					if (userList.get(i).all.toUpperCase().indexOf(
							text.toUpperCase()) == 0
							|| userList.get(i).all.charAt((userList
									.get(i).all.toUpperCase()
									.indexOf(text.toUpperCase())) - 1) == ' '){
					tempUserList.add(userList.get(i));
					}
				}
				}
				else if(key==0)
				{
					if(userList.get(i).nm.toUpperCase().contains(text.toUpperCase()))
					{
						if (userList.get(i).nm.toUpperCase().indexOf(
								text.toUpperCase()) == 0
								|| userList.get(i).nm.charAt((userList
										.get(i).nm.toUpperCase()
										.indexOf(text.toUpperCase())) - 1) == ' '){
						tempUserList.add(userList.get(i));
						}
					}
				}
				else if(key==1)
				{
					if(userList.get(i).sdesc.toUpperCase().contains(text.toUpperCase()))
					{
						if (userList.get(i).sdesc.toUpperCase().indexOf(
								text.toUpperCase()) == 0
								|| userList.get(i).sdesc.charAt((userList
										.get(i).sdesc.toUpperCase()
										.indexOf(text.toUpperCase())) - 1) == ' '){
						tempUserList.add(userList.get(i));
						}
					}
				}
				else if(key==2)
				{
					if(userList.get(i).ingridiant.toUpperCase().contains(text.toUpperCase()))
					{
						if (userList.get(i).ingridiant.toUpperCase().indexOf(
								text.toUpperCase()) == 0
								|| userList.get(i).ingridiant.charAt((userList
										.get(i).ingridiant.toUpperCase()
										.indexOf(text.toUpperCase())) - 1) == ' '){
						tempUserList.add(userList.get(i));
						}
					}
				}
				
			}
			if(tempUserList.size()>0)
			{
			l1.setAdapter(new EfficientAdapter(getApplicationContext(),tempUserList));
			}
			else
			{
				l1.setAdapter(new EfficientAdapter(getApplicationContext(),tempUserList));
				Toast.makeText(MediManage_ListActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
			}
		}
	}
}