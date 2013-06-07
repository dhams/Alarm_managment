package com.medplan.app;

import java.util.ArrayList;

import org.apache.http.client.UserTokenHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medpan.util.Constant;
import com.medpan.util.Contact_Model;
import com.medpan.util.GlobalMethods;
import com.medpan.util.Picture_Model;
import com.medplan.db.databasehelper;

public class MobileManage_ListActivity extends Activity implements
		OnClickListener {
	ListView l1;
	EditText etSearch;
	Spinner spSearch;
	databasehelper db;
	ArrayList<Contact_Model> userList, tempUserList;
	RelativeLayout searchView;
	Button find;
	static int length, user_id, key;
	String doWhat = "", fromWhere = "";
	ImageView headLogo;
	TextView headerTitle;
	RelativeLayout titleHeadLayout;
	LinearLayout MainCommonLayout;
	int width,height,setwidth,setheight;

	String[] arr = new String[] { "", "Mother", "Father", "Brother", "Sister",
			"Son", "Daughter", "Physician", "Friend", "Care", "Worker",
			"Others" };

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
				Intent intent = new Intent(MobileManage_ListActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});

		user_id = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getInt("UserID", -1);
		Log.i("User ID List", "List " + user_id);

		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.mob_mgt);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.mob_mgt); 

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
		userList = db.getContacts(user_id);
		
		l1 = (ListView) findViewById(R.id.list);
		searchView = (RelativeLayout) findViewById(R.id.rlSearch);
		spSearch = (Spinner) findViewById(R.id.spSearch);
		etSearch = (EditText) findViewById(R.id.editText1);
	
		
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

					tempUserList = new ArrayList<Contact_Model>();
					String text = etSearch.getText().toString();
					for (int i = 0; i < userList.size(); i++) {
						if (key == -1) {
							if (userList.get(i).all.toUpperCase().contains(
									text.toUpperCase())) {
								// if
								// (userList.get(i).all.toUpperCase().indexOf(
								// text.toUpperCase()) == 0
								// || userList.get(i).all.charAt((userList
								// .get(i).all.toUpperCase()
								// .indexOf(text.toUpperCase())) - 1) == ' ')
								{
									tempUserList.add(userList.get(i));
								}
							}
						} else if (key == 0) {
							if (userList.get(i).fnm.toUpperCase().contains(
									text.toUpperCase()) ||userList.get(i).lnm.toUpperCase().contains(
											text.toUpperCase())) {
								// if
								// (userList.get(i).fnm.toUpperCase().indexOf(
								// text.toUpperCase()) == 0
								// || userList.get(i).fnm.charAt((userList
								// .get(i).fnm.toUpperCase()
								// .indexOf(text.toUpperCase())) - 1) == ' ')
								{
									tempUserList.add(userList.get(i));
								}
							}
						} else if (key == 1) {
							String temp = arr[(userList.get(i).relation)];
							if (temp.toUpperCase().contains(text.toUpperCase())) {
								// if (temp.toUpperCase()
								// .indexOf(text.toUpperCase()) == 0
								// || temp
								// .charAt((temp
								// .toUpperCase()
								// .indexOf(text
								// .toUpperCase())) - 1) == ' ')
								{
									tempUserList.add(userList.get(i));
								}
							}
						} else if (key == 2) {
							if (userList.get(i).mob.toUpperCase().contains(
									text.toUpperCase())) {
								// if
								// (userList.get(i).mob.toUpperCase().indexOf(
								// text.toUpperCase()) == 0
								// || userList.get(i).mob.charAt((userList
								// .get(i).mob.toUpperCase()
								// .indexOf(text.toUpperCase())) - 1) == ' ')
								{
									tempUserList.add(userList.get(i));
								}
							}
						}

					}
					if (tempUserList.size() == 0) {
						Toast.makeText(MobileManage_ListActivity.this,
								R.string.no_record_found, Toast.LENGTH_SHORT)
								.show();
					}
					l1.setAdapter(new EfficientAdapter(getApplicationContext(),
							tempUserList));
				} else {
					if(tempUserList!=null)
					{
					tempUserList.clear();
					l1.setAdapter(new EfficientAdapter(getApplicationContext(),
							tempUserList));
					}
				}

			}
		});
		
		 
		//Spinner for relationship
		spSearch.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				tempUserList = new ArrayList<Contact_Model>();
				if(position==0){
					tempUserList.clear();
					l1.setAdapter(new EfficientAdapter(getApplicationContext(),
							tempUserList));
					return;
				}
				if (!spSearch.getSelectedItem().toString().equalsIgnoreCase("")) {

				
					String text = spSearch.getSelectedItem().toString();
					for (int i = 0; i < userList.size(); i++) {
						if (spSearch.getSelectedItemPosition()==0) {
							
						}  else {
							String temp = arr[(userList.get(i).relation)];
							if (temp.toUpperCase().contains(text.toUpperCase())) {
								// if (temp.toUpperCase()
								// .indexOf(text.toUpperCase()) == 0
								// || temp
								// .charAt((temp
								// .toUpperCase()
								// .indexOf(text
								// .toUpperCase())) - 1) == ' ')
								{
									tempUserList.add(userList.get(i));
								}
							}
						} 

					}
					if (tempUserList.size() == 0) {
						Toast.makeText(MobileManage_ListActivity.this,
								R.string.no_record_found, Toast.LENGTH_SHORT)
								.show();
					}
					l1.setAdapter(new EfficientAdapter(getApplicationContext(),
							tempUserList));
				} else {
					tempUserList.clear();
					l1.setAdapter(new EfficientAdapter(getApplicationContext(),
							tempUserList));
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		if (this.getIntent().getStringExtra("Task") != null) {
			doWhat = this.getIntent().getStringExtra("Task");
		}
		if (this.getIntent().getStringExtra("From") != null) {
			doWhat = "list";
			fromWhere = this.getIntent().getStringExtra("From");
			if (userList.size() == 0) {
				Toast.makeText(getApplicationContext(),R.string.no_contact_mob_mgt, Toast.LENGTH_LONG).show();
				Intent i = new Intent();
				i.putExtra("conid", 0);
				i.putExtra("connm", "");
				setResult(RESULT_OK, i);
				finish();
			}
		}
		if (userList.size() > 0) {
			if (doWhat.equalsIgnoreCase("list")||doWhat.equalsIgnoreCase("pick")) {
				length = 10;
				l1.setAdapter(new EfficientAdapter(getApplicationContext(),userList));
			} else {
				length = 3;
				if (getIntent().getIntExtra("Key", -1) != -1) {
					key = getIntent().getIntExtra("Key", 0);
					if (key == -1) {
						etSearch.setHint(R.string.enter_keyword);
					} else if (key == 0)

					{
						spSearch.setVisibility(View.GONE);
						etSearch.setVisibility(View.VISIBLE);
						etSearch.setHint(R.string.enter_name);
					} else if (key == 1) {
						spSearch.setVisibility(View.VISIBLE);
						etSearch.setVisibility(View.GONE);
						spSearch.setPromptId(R.string.relation_prompt);
						spSearch.setBackgroundResource(R.drawable.spinner_bg);
						
						String[] relation = getResources().getStringArray(R.array.relation_arrays);
						
						ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
								android.R.layout.simple_spinner_item, relation);
						
						adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spSearch.setAdapter(adapter1);
						
						
//						ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MobileManage_ListActivity.this,
//										R.array.relation_arrays,
//										android.R.layout.simple_spinner_dropdown_item);
//						spSearch.setAdapter(adapter);
						
						etSearch.setHint(R.string.enter_relation);
					} else if (key == 2) {
						spSearch.setVisibility(View.GONE);
						etSearch.setVisibility(View.VISIBLE);
						etSearch.setInputType(InputType.TYPE_CLASS_PHONE);
						etSearch.setHint(R.string.enter_mobile_no);
					}
				}
				searchView.setVisibility(View.VISIBLE);
				find = (Button) findViewById(R.id.btnFind);
				find.setOnClickListener(this);
			}
			
			
			l1.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					if (fromWhere.equalsIgnoreCase("Other")&&(!doWhat.equalsIgnoreCase("pick"))) {
						Intent i = new Intent();
						i.putExtra("conid", userList.get(position).icon);
						i.putExtra("connm", userList.get(position).fnm + " "+ userList.get(position).lnm);
						setResult(RESULT_OK, i);
						finish();
					} else {
						if (doWhat.equalsIgnoreCase("list")) {
							System.out.println("contact list~~~~~~~~~~~~~~~~"+userList.size());
							Intent i = new Intent(MobileManage_ListActivity.this,MobileManage_AddShowActivity.class);
							i.putExtra("idtoshow", userList.get(position).id);
							Log.i("uid list", position + "--"+ userList.get(position).id);
							i.putExtra("Task", "show");
							i.putExtra("counter", position);
							startActivity(i);
						}
						
						
						
						else if(doWhat.equalsIgnoreCase("pick")){
							Intent i = new Intent();
							i.putExtra("conid", userList.get(position).icon);
							i.putExtra("name", userList.get(position).fnm + " "+ userList.get(position).lnm);
							i.putExtra("contact", userList.get(position).mob);
							setResult(RESULT_OK, i);
							finish();
						}
						else {
							int temp = userList.indexOf(tempUserList.get(position));
							Intent i = new Intent(MobileManage_ListActivity.this,MobileManage_AddShowActivity.class);
							i.putExtra("idtoshow", userList.get(temp).id);
							Log.i("uid list", position + "--"+ userList.get(position).id);
							i.putExtra("Task", "show");
							i.putExtra("counter", temp);
							startActivity(i);
						}
						finish();

					}
				}

			});
		} else {
			Toast.makeText(getApplicationContext(),
					R.string.no_contact_stored_mob_mgt, Toast.LENGTH_SHORT)
					.show();
			this.finish();
		}

	}

	private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<Contact_Model> userList;

		public EfficientAdapter(Context context,
				ArrayList<Contact_Model> userList) {
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

			holder.uName.setText(userList.get(position).fnm + " "
					+ userList.get(position).lnm);
				holder.uType.setText(userList.get(position).mob);
				Picture_Model temp = db.getPicture(userList.get(position).picid);
				holder.uImage.setImageBitmap(GlobalMethods.decodeFile(temp.path));
				return convertView;

		}

		class ViewHolder {

			TextView uName, uType;
			ImageView uImage;

		}
	}

	public void onClick(View v) {
		if (v.getId() == R.id.btnFind) {
			tempUserList = new ArrayList<Contact_Model>();
			String text = etSearch.getText().toString();
			for (int i = 0; i < userList.size(); i++) {
				if (key == -1) {
					if (userList.get(i).all.toUpperCase().contains(
							text.toUpperCase())) {
						if (userList.get(i).all.toUpperCase().indexOf(
								text.toUpperCase()) == 0
								|| userList.get(i).all
										.charAt((userList.get(i).all
												.toUpperCase().indexOf(text
												.toUpperCase())) - 1) == ' ') {
							tempUserList.add(userList.get(i));
						}
					}
				} else if (key == 0) {
					if (userList.get(i).fnm.toUpperCase().contains(
							text.toUpperCase())) {
						if ((userList.get(i).fnm+" "+userList.get(i).lnm).toUpperCase().indexOf(
								text.toUpperCase()) == 0
								|| userList.get(i).fnm
										.charAt(((userList.get(i).fnm+" "+userList.get(i).lnm)
												.toUpperCase().indexOf(text
												.toUpperCase())) - 1) == ' ') {
							tempUserList.add(userList.get(i));
						}
					}
				} else if (key == 1) {
					String temp = arr[(userList.get(i).relation)];
					if (temp.toUpperCase().contains(text.toUpperCase())) {
						if (temp.toUpperCase().indexOf(text.toUpperCase()) == 0
								|| temp.charAt((temp.toUpperCase().indexOf(text
										.toUpperCase())) - 1) == ' ') {
							tempUserList.add(userList.get(i));
						}
					}
				} else if (key == 2) {
					if (userList.get(i).mob.toUpperCase().contains(
							text.toUpperCase())) {
						if (userList.get(i).mob.toUpperCase().indexOf(
								text.toUpperCase()) == 0
								|| userList.get(i).mob
										.charAt((userList.get(i).mob
												.toUpperCase().indexOf(text
												.toUpperCase())) - 1) == ' ') {
							tempUserList.add(userList.get(i));
						}
					}
				}

			}
			if (tempUserList.size() > 0) {
				l1.setAdapter(new EfficientAdapter(getApplicationContext(),
						tempUserList));
			} else {
				l1.setAdapter(new EfficientAdapter(getApplicationContext(),
						tempUserList));
				Toast.makeText(MobileManage_ListActivity.this,
						R.string.no_record_found, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (fromWhere.equalsIgnoreCase("Other")) {
			Intent i = new Intent();
			i.putExtra("conid", 0);
			i.putExtra("connm", "");
			setResult(RESULT_OK, i);
			finish();
		}
		finish();
	}
}