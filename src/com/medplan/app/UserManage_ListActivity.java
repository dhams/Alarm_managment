package com.medplan.app;

import java.util.ArrayList;
import java.util.Comparator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.medpan.util.Constant;
import com.medpan.util.GlobalMethods;
import com.medpan.util.Picture_Model;
import com.medpan.util.User_Model;
import com.medplan.db.databasehelper;

public class UserManage_ListActivity extends Activity implements
		OnClickListener {
	ListView l1;
	EditText etSearch;
	databasehelper db;
	ArrayList<User_Model> userList, tempUserList;
	RelativeLayout searchView;
	Button find;
	static int length;
	int width,height,setwidth,setheight;
	ImageView headLogo;
	int key = -1, user_id;
	 String doWhat;

	TextView headerTitle;
	RelativeLayout titleHeadLayout;
	LinearLayout MainCommonLayout;

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
				setwidth=30;
				setheight=30;
			}
			else if(height>=481){
				System.out.println("~~~~~~~~~~~~~~~~~~~~HDP~~~~~~~");
				setwidth=60;
				setheight=60;
				
			}

		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(UserManage_ListActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});

		user_id = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getInt("UserID", -1);
		Log.i("User ID List", "List " + user_id);

		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.user_mgt);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.user_mgt);

		titleHeadLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		MainCommonLayout = (LinearLayout) findViewById(R.id.MainCommonLayout);

		try {
			SplashScreen.Cmethod.CheckAddShowScreen(Constant._StrCheck,
					titleHeadLayout, MainCommonLayout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		db = new databasehelper(this);

		userList = db.getPatients(user_id);
		
		
		if (userList.size() == 0) {
			Toast.makeText(getApplicationContext(), R.string.no_patient_user_mgt,
					Toast.LENGTH_LONG).show();
			if(doWhat.equalsIgnoreCase("pick"))
			{
				Intent i = new Intent();
				i.putExtra("idtoshow", 0);
				i.putExtra("name", "");
				setResult(RESULT_OK, i);
				finish();
			}
			else
				this.finish();
		}
		l1 = (ListView) findViewById(R.id.list);
		searchView = (RelativeLayout) findViewById(R.id.rlSearch);
		etSearch = (EditText) findViewById(R.id.editText1);
		if (doWhat.equalsIgnoreCase("list")||doWhat.equalsIgnoreCase("pick")) {
			length = 10;
			l1.setAdapter(new EfficientAdapter(getApplicationContext(),
					userList));
		} else {
			length = 3;
			if (getIntent().getIntExtra("Key", -1) != -1) {
				key = getIntent().getIntExtra("Key", 0);
				if(key==-1)
				{
					etSearch.setHint(R.string.enter_keyword);
				}
				else if(key == 0)
				{
					etSearch.setHint(R.string.enter_name);
				}
				else if(key == 1)
				{
					etSearch.setHint(R.string.enter_city);
				}
				else if(key == 2)
				{
					etSearch.setHint(R.string.enter_gender);
				}
			}

			searchView.setVisibility(View.VISIBLE);
			find = (Button) findViewById(R.id.btnFind);
			find.setOnClickListener(this);
			find.setVisibility(View.GONE);
		}

		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				if (!etSearch.getText().toString().equalsIgnoreCase("")) {

					tempUserList = new ArrayList<User_Model>();
					tempUserList.clear();
					String text = etSearch.getText().toString();
					for (int i = 0; i < userList.size(); i++) {
						if (key == -1) {
							Log.i("","in key -1");
							if (userList.get(i).all.toUpperCase().contains(
									text.toUpperCase())) {
								
//								Log.i("","in key -1 - contains "+text.toUpperCase() + " in ="+userList.get(i).all.toUpperCase());
//								Log.i("", "index = "+userList.get(i).all.toUpperCase().indexOf(
//										text.toUpperCase()));
//								Log.i("", "index first = "+userList.get(i).all.toUpperCase().indexOf(
//										text.toUpperCase()));
//								Log.i("", "index second = "+(userList.get(i).all.toUpperCase().indexOf(
//										text.toUpperCase())-1));
//								Log.i("", "character = "+userList.get(i).all.charAt(
//												(userList.get(i).all.toUpperCase().indexOf(
//														text.toUpperCase()))-1));
//								if(userList.get(i).all.toUpperCase().indexOf(
//										text.toUpperCase())==0 || userList.get(i).all.charAt(
//												(userList.get(i).all.toUpperCase().indexOf(
//														text.toUpperCase()))-1) == ' ')
								{
								tempUserList.add(userList.get(i));
								
								}
							}
						} else if (key == 0) {
							String temp = userList.get(i).surname + " "+ userList.get(i).name;
							if (temp.toUpperCase().contains(
									text.toUpperCase())) {
//								if(temp.toUpperCase().indexOf(
//										text.toUpperCase())==0 || temp.charAt(
//												(temp.toUpperCase().indexOf(
//														text.toUpperCase()))-1) == ' ')
								{
								tempUserList.add(userList.get(i));
								}
							}
						} else if (key == 1) {
							if (userList.get(i).city.toUpperCase().contains(
									text.toUpperCase())) {
//								if(userList.get(i).city.toUpperCase().indexOf(
//										text.toUpperCase())==0 || userList.get(i).city.charAt(
//												(userList.get(i).city.toUpperCase().indexOf(
//														text.toUpperCase()))-1) == ' ')
								{
								tempUserList.add(userList.get(i));
								}
							}
						} else if (key == 2) {
							if (userList.get(i).gender.toUpperCase().contains(
									text.toUpperCase())) {
//								if(userList.get(i).gender.toUpperCase().indexOf(
//										text.toUpperCase())==0 || userList.get(i).gender.charAt(
//												(userList.get(i).gender.toUpperCase().indexOf(
//														text.toUpperCase()))-1) == ' ')
								{
								tempUserList.add(userList.get(i));
								}
							}
						}
					}
						if(tempUserList.size()==0){
							Toast.makeText(UserManage_ListActivity.this,R.string.no_record_found, Toast.LENGTH_SHORT).show();
						}
						l1.setAdapter(new EfficientAdapter(getApplicationContext(),tempUserList));
					
					
				} else {
					tempUserList.clear();
					l1.setAdapter(new EfficientAdapter(getApplicationContext(),tempUserList));
				}

			}
		});

		// etSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// System.out.println("change listner"+hasFocus);
		// if(tempUserList!=null)
		// {
		// tempUserList.clear();
		// l1.setAdapter(new
		// EfficientAdapter(getApplicationContext(),tempUserList));
		// }
		//
		// }
		// });
		l1.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(doWhat.equalsIgnoreCase("pick"))
				{
					Intent i = new Intent();
					i.putExtra("idtoshow", userList.get(arg2).uid);
					i.putExtra("name", userList.get(arg2).name);
					setResult(RESULT_OK, i);
					finish();
				}
				else if (doWhat.equalsIgnoreCase("list"))
				{
					Intent i = new Intent(UserManage_ListActivity.this,
							UserManage_AddShowActivity.class);
					i.putExtra("idtoshow", userList.get(arg2).uid);
					Log.i("uid list", arg2 + "--" + userList.get(arg2).uid);
					i.putExtra("Task", "show");
					i.putExtra("counter", arg2);
					startActivity(i);
				}
				else
				{
				int temp = userList.indexOf(tempUserList.get(arg2));
				Intent i = new Intent(UserManage_ListActivity.this,
						UserManage_AddShowActivity.class);
				i.putExtra("idtoshow", userList.get(temp).uid);
				Log.i("uid list", arg2 + "--" + userList.get(arg2).uid);
				i.putExtra("Task", "show");
				i.putExtra("counter", temp);
				startActivity(i);
				}
				finish();
			}
		});
	}

	private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<User_Model> userList;

		public EfficientAdapter(Context context, ArrayList<User_Model> userList) {
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

			holder.uName.setText(userList.get(position).name + " "
					+ userList.get(position).surname);
			holder.uType.setText("Mobile    No: " + userList.get(position).mob+ "\nTel.          No: " + userList.get(position).tel);
			
			
			    Picture_Model temp = db.getPicture(userList.get(position).picid);
			
				System.out.println("pictue path~~~~~~~~~~~~~~"+temp.path);
			
				holder.uImage.setImageBitmap(GlobalMethods.decodeFile(temp.path));
				holder.uImage.setScaleType(ScaleType.FIT_XY);
				return convertView;

		}

		class ViewHolder {

			TextView uName, uType;
			ImageView uImage;

		}
	}

	public void find() {
		tempUserList = new ArrayList<User_Model>();
		String text = etSearch.getText().toString();
		for (int i = 0; i < userList.size(); i++) {
			if (key == -1) {
				if (userList.get(i).all.toUpperCase().contains(
						text.toUpperCase())) {
					if(userList.get(i).all.toUpperCase().indexOf(
							text.toUpperCase())==0 || userList.get(i).all.charAt(
									(userList.get(i).all.toUpperCase().indexOf(
											text.toUpperCase()))-1) == ' ')
					{
					tempUserList.add(userList.get(i));
					
					}
				}
			} else if (key == 0) {
				String temp = userList.get(i).surname + " "+ userList.get(i).name;
				if (temp.toUpperCase().contains(
						text.toUpperCase())) {
					if(temp.toUpperCase().indexOf(
							text.toUpperCase())==0 || temp.charAt(
									(temp.toUpperCase().indexOf(
											text.toUpperCase()))-1) == ' ')
					{
					tempUserList.add(userList.get(i));
					}
				}
			} else if (key == 1) {
				if (userList.get(i).city.toUpperCase().contains(
						text.toUpperCase())) {
					if(userList.get(i).city.toUpperCase().indexOf(
							text.toUpperCase())==0 || userList.get(i).city.charAt(
									(userList.get(i).city.toUpperCase().indexOf(
											text.toUpperCase()))-1) == ' ')
					{
					tempUserList.add(userList.get(i));
					}
				}
			} else if (key == 2) {
				if (userList.get(i).gender.toUpperCase().contains(
						text.toUpperCase())) {
					if(userList.get(i).gender.toUpperCase().indexOf(
							text.toUpperCase())==0 || userList.get(i).gender.charAt(
									(userList.get(i).gender.toUpperCase().indexOf(
											text.toUpperCase()))-1) == ' ')
					{
					tempUserList.add(userList.get(i));
					}
				}
			}
		}
	}

	public void onClick(View v) {
		if (v.getId() == R.id.btnFind) {

//			tempUserList = new ArrayList<User_Model>();
//			String text = etSearch.getText().toString();
//			for (int i = 0; i < userList.size(); i++) {
//				if (key == -1) {
//					if (userList.get(i).all.toUpperCase().contains(
//							text.toUpperCase())) {
//						if(userList.get(i).all.toUpperCase().indexOf(
//								text.toUpperCase())==0 || userList.get(i).all.charAt(
//										(userList.get(i).all.toUpperCase().indexOf(
//												text.toUpperCase()))-1) == ' ')
//						{
//						tempUserList.add(userList.get(i));
//						
//						}
//					}
//				} else if (key == 0) {
//					String temp = userList.get(i).surname + " "+ userList.get(i).name;
//					if (temp.toUpperCase().contains(
//							text.toUpperCase())) {
//						if(temp.toUpperCase().indexOf(
//								text.toUpperCase())==0 || temp.charAt(
//										(temp.toUpperCase().indexOf(
//												text.toUpperCase()))-1) == ' ')
//						{
//						tempUserList.add(userList.get(i));
//						}
//					}
//				} else if (key == 1) {
//					if (userList.get(i).city.toUpperCase().contains(
//							text.toUpperCase())) {
//						if(userList.get(i).city.toUpperCase().indexOf(
//								text.toUpperCase())==0 || userList.get(i).city.charAt(
//										(userList.get(i).city.toUpperCase().indexOf(
//												text.toUpperCase()))-1) == ' ')
//						{
//						tempUserList.add(userList.get(i));
//						}
//					}
//				} else if (key == 2) {
//					if (userList.get(i).gender.toUpperCase().contains(
//							text.toUpperCase())) {
//						if(userList.get(i).gender.toUpperCase().indexOf(
//								text.toUpperCase())==0 || userList.get(i).gender.charAt(
//										(userList.get(i).gender.toUpperCase().indexOf(
//												text.toUpperCase()))-1) == ' ')
//						{
//						tempUserList.add(userList.get(i));
//						}
//					}
//				}
//			}
//			if (tempUserList.size() > 0) {
//				_NoDataFound.setVisibility(View.GONE);
//				l1.setVisibility(View.VISIBLE);
//				l1.setAdapter(new EfficientAdapter(getApplicationContext(),tempUserList));
//				etSearch.setText("");
//			} else {
//				//l1.setAdapter(new EfficientAdapter(getApplicationContext(),tempUserList));
//				etSearch.setText("");
//				l1.setVisibility(View.GONE);
//				_NoDataFound.setVisibility(View.VISIBLE);
//				Toast.makeText(UserManage_ListActivity.this,"No Record Found.", Toast.LENGTH_SHORT).show();
//			}
		}
	}
	
	public void onBackPressed() {
		if(doWhat.equalsIgnoreCase("pick"))
		{
			Intent i = new Intent();
			i.putExtra("pid", 0);

			setResult(RESULT_OK, i);
			finish();
		}
		finish();
	}
	
	class IgnoreCaseComparator implements Comparator<String> {
		  public int compare(String strA, String strB) {
		    return strA.compareToIgnoreCase(strB);
		  }
		}
}