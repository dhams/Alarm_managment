package com.medplan.app;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.medpan.util.Constant;
import com.medpan.util.GlobalMethods;
import com.medpan.util.Phys_Model;
import com.medpan.util.Picture_Model;
import com.medpan.util.User_Model;
import com.medplan.db.databasehelper;

public class PhysicianManage_ListActivity extends Activity implements
		OnClickListener {
	ListView l1;
	EditText etSearch;
	databasehelper db;
	ArrayList<Phys_Model> userList, tempUserList;
	RelativeLayout searchView;
	Button find;
	static int length;
	String doWhat, fromWhere = "";
	ImageView headLogo;
	TextView headerTitle;
	int key = -1, user_id = -1;
	RelativeLayout titleHeadLayout;
	LinearLayout MainCommonLayout;
	int width, height, setwidth, setheight;
	final CharSequence[] items = { "Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday" };
	final boolean[] flags = { false, false, false, false, false, false, false };
	AlertDialog.Builder builder;

	@Override
	public void onBackPressed() {
		if (fromWhere.equalsIgnoreCase("Other")) {
			Intent i = new Intent();
			i.putExtra("phyid", 0);
			i.putExtra("phynm", "");

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

		builder = new AlertDialog.Builder(PhysicianManage_ListActivity.this);
		builder.setTitle("Select Days of Visiting");

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				for (int i = 0; i < 7; i++)
					Log.i("Value of Flags", "" + i + " " + flags[i]);
				int count = 0;
				etSearch.setText("");
				if (flags[1] == true) {
					count++;
					etSearch.setText(etSearch.getText() + "Monday ");
				} else {
					if (etSearch.getText().toString().contains("Monday"))
						etSearch.setText(etSearch.getText().toString()
								.replace("Monday", ""));

				}
				if (flags[2] == true) {
					count++;
					etSearch.setText(etSearch.getText() + "Tuesday ");

				} else {
					if (etSearch.getText().toString().contains("Tuesday"))
						etSearch.setText(etSearch.getText().toString()
								.replace("Tuesday", ""));
				}
				if (flags[3] == true) {
					count++;
					etSearch.setText(etSearch.getText() + "Wednesday ");

				} else {
					if (etSearch.getText().toString().contains("Wednesday"))
						etSearch.setText(etSearch.getText().toString()
								.replace("Wednesday", ""));
				}
				if (flags[4] == true) {
					count++;
					etSearch.setText(etSearch.getText() + "Thursday ");
				} else {
					if (etSearch.getText().toString().contains("Thursday"))
						etSearch.setText(etSearch.getText().toString()
								.replace("Thursday", ""));
				}
				if (flags[5] == true) {
					count++;
					etSearch.setText(etSearch.getText() + "Friday ");
				} else {
					if (etSearch.getText().toString().contains("Friday"))
						etSearch.setText(etSearch.getText().toString()
								.replace("Friday", ""));
				}
				if (flags[6] == true) {
					count++;
					etSearch.setText(etSearch.getText() + "Saturday ");
				} else {
					if (etSearch.getText().toString().contains("Saturday"))
						etSearch.setText(etSearch.getText().toString()
								.replace("Saturday", ""));
				}
				if (flags[0] == true) {
					count++;
					etSearch.setText(etSearch.getText() + "Sunday ");
				} else {
					if (etSearch.getText().toString().contains("Sunday"))
						etSearch.setText(etSearch.getText().toString()
								.replace("Sunday", ""));
				}

				if (count == 0) {
					etSearch.setText("");
				} else {
					count = 0;
				}

			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Toast.makeText(PhysicianManage_ListActivity.this, "Fail",
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

		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth(); // deprecated
		height = display.getHeight(); // deprecated

		if (height <= 480) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~MDP~~~~~~~");
			setwidth = 40;
			setheight = 40;
		} else if (height >= 481) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~HDP~~~~~~~");
			setwidth = 70;
			setheight = 70;

		}

		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(PhysicianManage_ListActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});

		user_id = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getInt("UserID", -1);
		Log.i("User ID List", "List " + user_id);

		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.phy_mgt);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.phy_mgt);

		titleHeadLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		MainCommonLayout = (LinearLayout) findViewById(R.id.MainCommonLayout);

		try {
			SplashScreen.Cmethod.CheckAddShowScreen(Constant._StrCheck,
					titleHeadLayout, MainCommonLayout);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		db = new databasehelper(this);
		userList = db.getPhys(user_id);
		l1 = (ListView) findViewById(R.id.list);
		searchView = (RelativeLayout) findViewById(R.id.rlSearch);
		etSearch = (EditText) findViewById(R.id.editText1);
		etSearch.setOnClickListener(this);
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

					tempUserList = new ArrayList<Phys_Model>();
					tempUserList.clear();
					String text = etSearch.getText().toString().trim();
					for (int i = 0; i < userList.size(); i++) {
						try {
							if (key == -1) {
								if (userList.get(i).all.toUpperCase().contains(
										text.toUpperCase())) {
									// if
									// (userList.get(i).all.toUpperCase().indexOf(
									// text.toUpperCase()) == 0
									// || userList.get(i).all.charAt((userList
									// .get(i).all.toUpperCase()
									// .indexOf(text.toUpperCase())) - 1) ==
									// ' ')
									{
										tempUserList.add(userList.get(i));
									}
								}
							} else if (key == 0) {
								String temp = userList.get(i).name + " "
										+ userList.get(i).surname;
								if (temp.toUpperCase().contains(
										text.toUpperCase())) {
									// if (temp.toUpperCase().indexOf(
									// text.toUpperCase()) == 0
									// || temp.charAt((temp.toUpperCase()
									// .indexOf(text.toUpperCase())) - 1) ==
									// ' ')
									{
										tempUserList.add(userList.get(i));
									}
								}
							} else if (key == 1) {
								if (userList.get(i).city.toUpperCase()
										.contains(text.toUpperCase())) {
									// if
									// (userList.get(i).city.toUpperCase().indexOf(
									// text.toUpperCase()) == 0
									// || userList.get(i).city.charAt((userList
									// .get(i).city.toUpperCase()
									// .indexOf(text.toUpperCase())) - 1) ==
									// ' ')
									{
										tempUserList.add(userList.get(i));
									}
								}
							} else if (key == 2) {
								String tArr[] = text.split(" ");
								for (int d = 0; d < tArr.length; d++) {

									if (userList.get(i).visiting.toUpperCase()
											.contains(tArr[d].toUpperCase())) {
										// if
										// (userList.get(i).visiting.toUpperCase().indexOf(
										// text.toUpperCase()) == 0
										// ||
										// userList.get(i).visiting.charAt((userList
										// .get(i).visiting.toUpperCase()
										// .indexOf(text.toUpperCase())) - 1) ==
										// ' ')
										{
											tempUserList.add(userList.get(i));
											break;
										}
									}
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
						}

					}
					if (tempUserList.size() == 0) {
						Toast.makeText(PhysicianManage_ListActivity.this,
								R.string.no_record_found, Toast.LENGTH_SHORT)
								.show();
					}
					l1.setAdapter(new EfficientAdapter(getApplicationContext(),
							tempUserList));
				} else {
					if (tempUserList != null) {
						tempUserList.clear();
						l1.setAdapter(new EfficientAdapter(
								getApplicationContext(), tempUserList));
					}
				}

			}
		});

		if (this.getIntent().getStringExtra("Task") != null) {
			doWhat = this.getIntent().getStringExtra("Task");
		}
		if (this.getIntent().getStringExtra("From") != null) {
			doWhat = "list";
			fromWhere = this.getIntent().getStringExtra("From");
			if (userList.size() == 0) {
				Toast.makeText(getApplicationContext(),
						R.string.no_phy_added_phy_mgt, Toast.LENGTH_LONG)
						.show();
				Intent i = new Intent();
				i.putExtra("phyid", 0);
				i.putExtra("phynm", "");
				setResult(RESULT_OK, i);
				finish();
			}
		}
		if (userList.size() > 0) {
			if (doWhat.equalsIgnoreCase("list")) {
				length = 10;
				l1.setAdapter(new EfficientAdapter(getApplicationContext(),
						userList));
			} else {
				length = 3;
				if (getIntent().getIntExtra("Key", -1) != -1) {
					key = getIntent().getIntExtra("Key", 0);
					if (key == -1) {
						etSearch.setHint(R.string.enter_keyword);
					} else if (key == 0) {
						etSearch.setHint(R.string.enter_name);
					} else if (key == 1) {
						etSearch.setHint(R.string.enter_city);
					} else if (key == 2) {
						etSearch.setHint(R.string.enter_weekday);
					}
				}
				searchView.setVisibility(View.VISIBLE);
				find = (Button) findViewById(R.id.btnFind);
				find.setOnClickListener(this);
			}

			l1.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					if (fromWhere.equalsIgnoreCase("Other")) {
						Intent i = new Intent();
						i.putExtra("phyid", userList.get(position).pid);
						i.putExtra("phynm", userList.get(position).name + " "
								+ userList.get(position).surname);

						setResult(RESULT_OK, i);
						finish();
					} else {
						if (doWhat.equalsIgnoreCase("list")) {
							Intent i = new Intent(
									PhysicianManage_ListActivity.this,
									PhysicianManage_AddShowActivity.class);
							i.putExtra("idtoshow", userList.get(position).pid);
							Log.i("uid list",
									position + "--"
											+ userList.get(position).pid);
							i.putExtra("Task", "show");
							i.putExtra("counter", position);
							startActivity(i);
						} else {
							Intent i = new Intent(
									PhysicianManage_ListActivity.this,
									PhysicianManage_AddShowActivity.class);

							int temp = userList.indexOf(tempUserList
									.get(position));
							i.putExtra("idtoshow", userList.get(temp).pid);
							Log.i("uid list",
									position + "--"
											+ userList.get(position).pid);
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
					R.string.no_phy_stored_phy_mgt, Toast.LENGTH_SHORT).show();
			this.finish();
		}

	}

	private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<Phys_Model> userList;

		public EfficientAdapter(Context context, ArrayList<Phys_Model> userList) {
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
			holder.uType.setText("Mobile No: " + userList.get(position).mobile
					+ "\nTel.       No: " + userList.get(position).tel);
			Picture_Model temp = db.getPicture(userList.get(position).iid);
			
				holder.uImage.setImageBitmap(GlobalMethods.decodeFile(temp.path));
			
			return convertView;

		}

		class ViewHolder {

			TextView uName, uType;
			ImageView uImage;

		}
	}

	public void onClick(View v) {
		if (v.getId() == R.id.editText1) {
			if (key == 2) {
				builder.show();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
			}
		}
		if (v.getId() == R.id.btnFind) {
			tempUserList = new ArrayList<Phys_Model>();
			String text = etSearch.getText().toString();
			for (int i = 0; i < userList.size(); i++) {
				try {
					if (key == -1) {
						if (userList.get(i).all.toUpperCase().contains(
								text.toUpperCase())) {
							if (userList.get(i).all.toUpperCase().indexOf(
									text.toUpperCase()) == 0
									|| userList.get(i).all.charAt((userList
											.get(i).all.toUpperCase()
											.indexOf(text.toUpperCase())) - 1) == ' ') {
								tempUserList.add(userList.get(i));
							}
						}
					} else if (key == 0) {
						String temp = userList.get(i).name + " "
								+ userList.get(i).surname;
						if (temp.toUpperCase().contains(text.toUpperCase())) {
							if (temp.toUpperCase().indexOf(text.toUpperCase()) == 0
									|| temp.charAt((temp.toUpperCase()
											.indexOf(text.toUpperCase())) - 1) == ' ') {
								tempUserList.add(userList.get(i));
							}
						}
					} else if (key == 1) {
						if (userList.get(i).city.toUpperCase().contains(
								text.toUpperCase())) {
							if (userList.get(i).city.toUpperCase().indexOf(
									text.toUpperCase()) == 0
									|| userList.get(i).city.charAt((userList
											.get(i).city.toUpperCase()
											.indexOf(text.toUpperCase())) - 1) == ' ') {
								tempUserList.add(userList.get(i));
							}
						}
					} else if (key == 2) {
						if (userList.get(i).visiting.toUpperCase().contains(
								text.toUpperCase())) {
							if (userList.get(i).visiting.toUpperCase().indexOf(
									text.toUpperCase()) == 0
									|| userList.get(i).visiting
											.charAt((userList.get(i).visiting
													.toUpperCase().indexOf(text
													.toUpperCase())) - 1) == ' ') {
								tempUserList.add(userList.get(i));
							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			if (tempUserList.size() > 0) {
				l1.setAdapter(new EfficientAdapter(getApplicationContext(),
						tempUserList));
			} else {
				l1.setAdapter(new EfficientAdapter(getApplicationContext(),
						tempUserList));
				Toast.makeText(PhysicianManage_ListActivity.this,
						R.string.no_record_found, Toast.LENGTH_SHORT).show();
			}
		}
	}
}