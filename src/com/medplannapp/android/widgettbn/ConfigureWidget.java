package com.medplannapp.android.widgettbn;

import java.util.ArrayList;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medpan.util.Constant;
import com.medplan.app.MainActivity;
import com.medplan.app.MobileManage_ListActivity;
import com.medplan.app.R;
import com.medplan.app.SplashScreen;

public class ConfigureWidget extends Activity implements OnClickListener{

	
	
	private int mAppWidgetId;
	TextView tv1,tv2,tv3,tv4,tv5;
	int count=0;
	SharedPreferences sp;
	Editor edit;
	public static ArrayList<String> arr = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID && mAppWidgetId!=301188) {
            finish();
        }
		Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainwidget);
		
		
		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(ConfigureWidget.this,MainActivity.class);
				startActivity(intent);
			}
		});
		
		TextView headerTitle = (TextView) findViewById(R.id.tv_header_title);

		Typeface face = Typeface.createFromAsset(getAssets(),"century_gothic.ttf");
		headerTitle.setText(R.string.reprot_mgt);
		//headerTitle.setText(Html.fromHtml("Users" + "</b>" + "<br/>"+ "Management"));
		headerTitle.setTypeface(face);

		RelativeLayout HeaderLayout = (RelativeLayout) findViewById(R.id.rlHeadTitlelayout);
		LinearLayout mainLayout= (LinearLayout) findViewById(R.id.Mainlinearlayout);
		
		try {
			SplashScreen.Cmethod.CheckAddShowScreen(Constant._StrCheck,
					HeaderLayout, mainLayout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		tv1 = (TextView)findViewById(R.id.textView1);
		tv2 = (TextView)findViewById(R.id.textView2);
		tv3 = (TextView)findViewById(R.id.textView3);
		tv4 = (TextView)findViewById(R.id.textView4);
		tv5 = (TextView)findViewById(R.id.textView5);
		sp = PreferenceManager.getDefaultSharedPreferences(ConfigureWidget.this);
		edit = sp.edit();
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		tv4.setOnClickListener(this);
		tv5.setOnClickListener(this);
		arr.clear();
		arr.add(sp.getString("First", ""));
		arr.add(sp.getString("Second", ""));
		arr.add(sp.getString("Third", ""));
		arr.add(sp.getString("Fourth", ""));
		arr.add(sp.getString("Fifth", ""));
		
		tv1.setText(sp.getString("FirstContact", ""));
		tv2.setText(sp.getString("SecondContact", ""));
		tv3.setText(sp.getString("ThirdContact", ""));
		tv4.setText(sp.getString("FourthContact", ""));
		tv5.setText(sp.getString("FifthContact", ""));
		
		Button btnSave = (Button)findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				for(int i=0;i<5;i++)
					Log.i(""+i,arr.get(i));
				edit.putString("First", arr.get(0));
				edit.putString("Second",arr.get(1));
				edit.putString("Third", arr.get(2));
				edit.putString("Fourth", arr.get(3));
				edit.putString("Fifth", arr.get(4));
				edit.commit();
				ToggleWidget toggleWidget = new ToggleWidget();
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ConfigureWidget.this);
				toggleWidget.onUpdate(ConfigureWidget.this, appWidgetManager, null);
				ConfigureWidget.this.finish();
			}
		});
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.textView1:
			count =1;
			break;
		case R.id.textView2:
			count =2;
			break;
		case R.id.textView3:
			count =3;
			break;
		case R.id.textView4:
			count =4;
			break;
		case R.id.textView5:
			count =5;
			break;
		}
		Intent intent = new Intent(ConfigureWidget.this,
				MobileManage_ListActivity.class);
		intent.putExtra("Task", "pick");
		startActivityForResult(intent, 999);	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		String temp = "  Name : "+data.getStringExtra("name")+"\n  Contact : "+data.getStringExtra("contact");
		
		switch (count) {
		case 1:
			arr.set(0, data.getStringExtra("contact"));
			edit.putString("FirstContact", temp);
			tv1.setText(temp);
			break;
		case 2:
			arr.set(1, data.getStringExtra("contact"));
			edit.putString("SecondContact", temp);
			tv2.setText(temp);
			break;
		case 3:
			arr.set(2, data.getStringExtra("contact"));
			edit.putString("ThirdContact", temp);
			tv3.setText(temp);
			break;
		case 4:
			arr.set(3, data.getStringExtra("contact"));
			edit.putString("FourthContact", temp);
			tv4.setText(temp);
			break;
		case 5:
			arr.set(4, data.getStringExtra("contact"));
			edit.putString("FifthContact", temp);
			tv5.setText(temp);
			break;
		}
		edit.commit();
	}
}
