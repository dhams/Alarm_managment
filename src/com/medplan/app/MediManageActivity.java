package com.medplan.app;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medpan.util.Constant;

public class MediManageActivity extends Activity implements OnClickListener {
	Button add, show, list, search, filter;
	TextView headerTitle;
	LinearLayout HeaderLayout;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.medical_manage_screen);
		
		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MediManageActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
		
		headerTitle=(TextView)findViewById(R.id.tv_header_title);
		Typeface face=Typeface.createFromAsset(getAssets(), "century_gothic.ttf");
		 headerTitle.setText(R.string.med_mgt);
		// headerTitle.setText(Html.fromHtml("Medicines"+"</b>"+"<br/>"+"Management"));
		 headerTitle.setTypeface(face);
		 
		 
		 HeaderLayout = (LinearLayout) findViewById(R.id.rlHeadlayout);
		add = (Button) findViewById(R.id.btnAdd);
		show = (Button) findViewById(R.id.btnShow);
		list = (Button) findViewById(R.id.btnList);
		search = (Button) findViewById(R.id.btnSearch);
		filter = (Button) findViewById(R.id.btnFilter);
			
		add.setOnClickListener(this);
		show.setOnClickListener(this);
		list.setOnClickListener(this);
		search.setOnClickListener(this);
		filter.setOnClickListener(this);
		
		try {
			SplashScreen.Cmethod.CheckUserType(Constant._StrCheck, HeaderLayout,
					add, show, list, search, filter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btnAdd:
			intent = new Intent(MediManageActivity.this,
					MediManage_AddShowActivity.class);
			intent.putExtra("Task", "add");
			startActivity(intent);
			break;

		case R.id.btnShow:
			intent = new Intent(MediManageActivity.this,
					MediManage_AddShowActivity.class);
			intent.putExtra("Task", "show");
			startActivity(intent);
			break;
		case R.id.btnList:
			intent = new Intent(MediManageActivity.this,
					MediManage_ListActivity.class);
			intent.putExtra("Task", "list");
			startActivity(intent);
			break;
		case R.id.btnSearch:
			intent = new Intent(MediManageActivity.this,
					MediManage_ListActivity.class);
			intent.putExtra("Task", "search");
			startActivity(intent);
			break;
		case R.id.btnFilter:
			Toast.makeText(MediManageActivity.this, R.string.filter_search_med_mgt, Toast.LENGTH_LONG).show();
			registerForContextMenu(v);
			openContextMenu(v);
			break;
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Intent intent = new Intent(MediManageActivity.this,
				MediManage_ListActivity.class);
		if(item.getItemId()==0)
			intent.putExtra("Key", 0);
		else if(item.getItemId()==1)
			intent.putExtra("Key", 1);
		else if(item.getItemId()==2)
			intent.putExtra("Key", 2);
		intent.putExtra("Task", "search");
		startActivity(intent);
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.select);
		menu.add(0, 0, 0, R.string.by_name_user_mgt);
		menu.add(0, 1, 0, R.string.by_short_med_mgt);
		menu.add(0, 2, 0, R.string.by_active_med_mgt);
	}
}