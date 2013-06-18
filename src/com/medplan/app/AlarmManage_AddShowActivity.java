package com.medplan.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.medpan.util.Constant;

public class AlarmManage_AddShowActivity extends Activity implements
		OnClickListener {
	LinearLayout addButton, showButton;
	ImageView headLogo;
	TextView headerTitle;
	TextView tvAText;
	RelativeLayout titleHeadLayout;
	LinearLayout MainBgLayout;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		String doWhat = this.getIntent().getStringExtra("Task");
		setContentView(R.layout.cellmanage_addshow);
		
		Button btnHome = (Button) findViewById(R.id.button_home);
		btnHome.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(AlarmManage_AddShowActivity.this,MainActivity.class);
				startActivity(intent);
			}
		}); 
		 
		headLogo=(ImageView)findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.alram_mgt);
		
		headerTitle=(TextView)findViewById(R.id.tv_header_title);
		headerTitle.setText("Alarm Management");
		
		titleHeadLayout=(RelativeLayout)findViewById(R.id.rlHeadTitlelayout);
		MainBgLayout=(LinearLayout)findViewById(R.id.mainCellLayout);
		
		try {
			SplashScreen.Cmethod.CheckAddShowScreen(Constant._StrCheck, titleHeadLayout, MainBgLayout);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		addButton = (LinearLayout) findViewById(R.id.btnForAdd);
		showButton = (LinearLayout) findViewById(R.id.btnForShow);
		Spinner spMed = (Spinner) findViewById(R.id.spMed);
		Spinner spBG = (Spinner) findViewById(R.id.spBgColor);
		Spinner spSound = (Spinner) findViewById(R.id.spSound);

		String[] items1 = { "Select Medicine", "Medicine ABC", "Medicine XYZ" };
		String[] items2 = { "Background Color", "Red", "Green" };
		String[] items3 = { "Sound", "Sound 1", "Sound 2" };

		ArrayAdapter aa = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, items1);
		aa
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spMed.setAdapter(aa);

		ArrayAdapter bb = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, items2);
		bb
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spBG.setAdapter(bb);
		ArrayAdapter cc = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, items3);
		
		cc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spSound.setAdapter(cc);

		if (doWhat.equalsIgnoreCase("show")) {
			spMed.setSelection(1);
			spBG.setSelection(1);
			spSound.setSelection(1);
			showButton.setVisibility(View.VISIBLE);

		} else {
			spMed.setSelection(0);
			spBG.setSelection(0);
			spSound.setSelection(0);
			addButton.setVisibility(View.VISIBLE);
		}

	}

	public void onClick(View v) {

	}
}
