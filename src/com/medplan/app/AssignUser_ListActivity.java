package com.medplan.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medpan.util.Constant;
import com.medpan.util.User_Model;
import com.medplan.db.databasehelper;

public class AssignUser_ListActivity extends Activity implements
		OnClickListener {
	ListView l1;
	EditText etSearch;
	databasehelper db;
	ArrayList<User_Model> userList, tempUserList;
	RelativeLayout searchView;
	Button find;
	static int length;
	ImageView headLogo;
	int key = -1, user_id;
	String doWhat;

	TextView headerTitle;
	RelativeLayout titleHeadLayout;
	LinearLayout MainCommonLayout;
	ArrayList<DataUser> arrShow = new ArrayList<DataUser>();

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.box_assign_users);

		Button newBtn = (Button)findViewById(R.id.btn_addNew);
		newBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AssignUser_ListActivity.this, CellManageActivity.class);
				startActivity(intent);
				AssignUser_ListActivity.this.finish();
			}
		});
		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(AssignUser_ListActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});

		user_id = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getInt("UserID", -1);
		Log.i("User ID List", "List " + user_id);

		headLogo = (ImageView) findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.cell_mgt);

		headerTitle = (TextView) findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.cell_mgt);

		titleHeadLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		MainCommonLayout = (LinearLayout) findViewById(R.id.mainll);

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
			Toast.makeText(getApplicationContext(),
					R.string.no_patient_user_mgt, Toast.LENGTH_LONG).show();
			
				Intent i = new Intent();
				i.putExtra("idtoshow", 0);
				i.putExtra("name", "");
				setResult(RESULT_OK, i);
				finish();
		}
		 
		for(int i = 0; i < userList.size();i++)
		{
			int cellid= db.getCell_id(user_id, userList.get(i).uid);
			if(cellid!=-1)
			{
				DataUser data = new DataUser();
				data.name = userList.get(i).name+" "+userList.get(i).surname;
				data.box = cellid;
				data.filled = db.getCellCounts(user_id, userList.get(i).uid, cellid);
				data.remain = data.box - data.filled;
				data.id = userList.get(i).uid;
				
				arrShow.add(data);
			}
		}
		if(arrShow.size()>0)
		{
			l1 = (ListView) findViewById(R.id.list_assign_users);

			l1.setAdapter(new EfficientAdapter(getApplicationContext(), arrShow));

			l1.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {

					Intent i = new Intent(AssignUser_ListActivity.this,CellManageActivity.class);
					i.putExtra("idtoshow", arrShow.get(arg2).id);
					i.putExtra("name", arrShow.get(arg2).name);
					i.putExtra("position", arrShow.get(arg2).box);
					
					i.putExtra("SelectedBox", true);
					System.out.println("box postion~~~~~~~~~"+arrShow.get(arg2).box+"id too"+arrShow.get(arg2).id);
					startActivity(i);
					finish();

				}
			});
		}
		else
		{
			Toast.makeText(AssignUser_ListActivity.this, "No User have choosen Box, Click on New", Toast.LENGTH_SHORT).show();
		}
	}

	class DataUser
	{
		String name;
		int box,filled,id,remain;
	}
	private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<DataUser> userList;

		public EfficientAdapter(Context context, ArrayList<DataUser> userList) {
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
				convertView = mInflater.inflate(R.layout.box_assign_users_row, null);
				holder = new ViewHolder();
				holder.uName = (TextView) convertView
						.findViewById(R.id.tv_assignUsers);
				holder.box = (TextView) convertView
						.findViewById(R.id.tv_assignBox);
				holder.fill = (TextView) convertView
				.findViewById(R.id.tv_assignCell);
				holder.remain = (TextView) convertView
				.findViewById(R.id.tv_unassignCell);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.uName.setText(userList.get(position).name);
			holder.box.setText(userList.get(position).box+"");
			holder.fill.setText(userList.get(position).filled+"");
			holder.remain.setText(userList.get(position).remain+"");
			
		
			// holder.uImage.setImageBitmap(BitmapFactory.decodeFile(temp.path));
			return convertView;

		}

		class ViewHolder {

			TextView uName, box,fill,remain;


		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}