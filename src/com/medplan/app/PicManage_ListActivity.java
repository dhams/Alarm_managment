package com.medplan.app;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.AdapterView.OnItemSelectedListener;
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
import com.medpan.util.GlobalMethods;
import com.medpan.util.Picture_Model;
import com.medplan.db.SectionedAdapter;
import com.medplan.db.databasehelper;

public class PicManage_ListActivity extends Activity implements OnClickListener {
	protected static final String TAG = null;
	ListView l1;
	EditText etSearch;
	Spinner spSearch;
	databasehelper db;
	ArrayList<Picture_Model> picList, tempPicList,usersPic,medPic;
	RelativeLayout searchView;
	Button find;
	static int length;
	String doWhat, fromWhere = "";
	ImageView headLogo;
	TextView headerTitle,result;
	int userid,key=-1;
	RelativeLayout titleHeadLayout;
	LinearLayout MainCommonLayout;
	 Bitmap bitmap;
	 BitmapFactory.Options bfOptions;

	ArrayList<String> items1;
	
	int userListLenght;
	int medicineListLenght;
	int userLenght = 1;
	int width,height,setwidth,setheight;
	ArrayList<Integer> sectionPositionArrayList;
	ArrayList<String> blankList;
//	SectionedAdapter adapter;
	
	
	
	public void onBackPressed() {
		if(fromWhere.equalsIgnoreCase("Other"))
		{
			Intent i = new Intent();
			i.putExtra("pid", 0);

			setResult(RESULT_OK, i);
			finish();
		}
		finish();
	}
	@SuppressWarnings("unused")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.listuser);
		
		System.out.println("PICTURE MANGEMENT");
		
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
				Intent intent = new Intent(PicManage_ListActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});

		headLogo=(ImageView)findViewById(R.id.img_header_logo);
		headLogo.setBackgroundResource(R.drawable.picture_mgt);

		headerTitle=(TextView)findViewById(R.id.tv_header_title);
		headerTitle.setText(R.string.pic_mgt);


		titleHeadLayout=(RelativeLayout)findViewById(R.id.rlHeadTitlelayout);
		MainCommonLayout=(LinearLayout)findViewById(R.id.MainCommonLayout);

		try {
			SplashScreen.Cmethod.CheckAddShowScreen(Constant._StrCheck, titleHeadLayout, MainCommonLayout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		items1 = new ArrayList<String>();
		items1.add(" ");
		items1.add("People");
		items1.add("Medicines");

		db = new databasehelper(this);
		userid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("UserID", 0);
		picList = db.getPictures(userid);

		if (this.getIntent().getStringExtra("Task") != null) {
			doWhat = this.getIntent().getStringExtra("Task");
		}
		if (this.getIntent().getStringExtra("From") != null) {
			doWhat = "list";
			fromWhere = this.getIntent().getStringExtra("From");
			
			if (picList.size() == 0) {
//				Toast.makeText(getApplicationContext(),
//						R.string.add_picture_pic_mgt,
//						Toast.LENGTH_LONG).show();
//				Intent i = new Intent();
//				i.putExtra("pid", 0);
//				setResult(RESULT_OK, i);
//				finish();
			}
		}
		System.out.println("array size~~~~~~"+picList.size());
		
		db = new databasehelper(this);

		if (picList.size() > 0) {
			l1 = (ListView) findViewById(R.id.list);
			searchView = (RelativeLayout) findViewById(R.id.rlSearch);
			etSearch = (EditText) findViewById(R.id.editText1);
			spSearch=(Spinner)findViewById(R.id.spSearch);
			
			
			final SectionedAdapter adapter =new SectionedAdapter()
				{
					
					  protected View getHeaderView(String caption, int index, View convertView,ViewGroup parent) 
					  {
						  
					    result=(TextView)convertView;
					    
					    if (convertView==null) 
					    {
					      result=(TextView)getLayoutInflater().inflate(R.layout.section_header,null);

					    }
					    
					    result.setText(caption);
					   // temp=caption;
					   // ind=index;
					    
					    return(result);
					  }
					};
			
			
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
							
								tempPicList = new ArrayList<Picture_Model>();
								tempPicList.clear();
								String text = etSearch.getText().toString().trim();
								for (int i = 0; i < picList.size(); i++) {
									if(key==-1)
									{
										if(picList.get(i).all.toUpperCase().contains(text.toUpperCase()))
										{
//											if(picList.get(i).all.toUpperCase().indexOf(
//													text.toUpperCase())==0 || picList.get(i).all.charAt(
//															(picList.get(i).all.toUpperCase().indexOf(
//																	text.toUpperCase()))-1) == ' ')
											{
											tempPicList.add(picList.get(i));
											}
										}
									}
									else if(key==0)
									{
										String temp = items1.get(Integer.parseInt(picList.get(i).category));
										if(temp.toUpperCase().contains(text.toUpperCase()))
										{
											
//											if(temp.toUpperCase().indexOf(
//													text.toUpperCase())==0 ||temp.charAt(
//															(temp.toUpperCase().indexOf(
//																	text.toUpperCase()))-1) == ' ')
											{
											tempPicList.add(picList.get(i));
											}
										}
									}
									else if(key==1)
									{
										if(picList.get(i).desc.toUpperCase().contains(text.toUpperCase()))
										{
//											if(picList.get(i).desc.toUpperCase().indexOf(
//													text.toUpperCase())==0 || picList.get(i).desc.charAt(
//															(picList.get(i).desc.toUpperCase().indexOf(
//																	text.toUpperCase()))-1) == ' ')
											{
											tempPicList.add(picList.get(i));
											}
										}
									}
								}
								if(tempPicList.size()==0){
									Toast.makeText(PicManage_ListActivity.this,R.string.no_record_found, Toast.LENGTH_SHORT).show();
								}
//								//user
//								adapter.addSection(getResources().getString(R.string.user_mgt), new EfficientAdapter(getApplicationContext(),tempPicList));
//								
//								//medicine
//								adapter.addSection(getResources().getString(R.string.med_mgt), new EfficientAdapter(getApplicationContext(),tempPicList));
//								l1.setAdapter(adapter);
								
							 l1.setAdapter(new EfficientAdapter(getApplicationContext(),tempPicList));
							}
							else
							{
								tempPicList.clear();
//								//user
//								adapter.addSection(getResources().getString(R.string.user_mgt), new EfficientAdapter(getApplicationContext(),tempPicList));
//								
//								//medicine
//								adapter.addSection(getResources().getString(R.string.med_mgt), new EfficientAdapter(getApplicationContext(),tempPicList));
//								l1.setAdapter(adapter);
								
								l1.setAdapter(new EfficientAdapter(getApplicationContext(),tempPicList));
							}
				
					
					
				}
			});
			
			
			
			////////////spinner for category
			spSearch.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					 tempPicList = new ArrayList<Picture_Model>();
					if(position==0){
						tempPicList.clear();
						l1.setAdapter(new EfficientAdapter(getApplicationContext(),tempPicList));
					
					}
					
					 if(!spSearch.getSelectedItem().toString().equalsIgnoreCase(""))
						{
							
						
						tempPicList.clear();
						String getString=spSearch.getSelectedItem().toString().trim();
						System.out.println("category values!!!!!!!!!!!!!!!"+getString);
						for (int i = 0; i < picList.size(); i++) {
							
							String temp = items1.get(Integer.parseInt(picList.get(i).category));
							if(temp.toUpperCase().contains(getString.toUpperCase()))
							{
								{
								tempPicList.add(picList.get(i));
								}
							}
							
						}
						
						
							if(tempPicList.size()==0){
							Toast.makeText(PicManage_ListActivity.this,R.string.no_record_found, Toast.LENGTH_SHORT).show();
						}
						  l1.setAdapter(new EfficientAdapter(getApplicationContext(),tempPicList));
						}
						else
						{
							tempPicList.clear();
							l1.setAdapter(new EfficientAdapter(getApplicationContext(),tempPicList));
						}
					
				
					
				}
				

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
					 usersPic = new ArrayList<Picture_Model>();
					 medPic =  new ArrayList<Picture_Model>();
					for(int c=0;c<picList.size();c++)
					{
						if(picList.get(c).category.equalsIgnoreCase("1"))
						{
							usersPic.add(picList.get(c));
						}
						else
						{
							medPic.add(picList.get(c));
						}
					}
			
			
			 if (doWhat.equalsIgnoreCase("list")) {
				//user
				 blankList= new ArrayList<String>();
				 blankList.add(" ");
				 
				 if(usersPic.size()!=0){
					 adapter.addSection(getResources().getString(R.string.people), new EfficientAdapter(getApplicationContext(),usersPic)); 
				 }
				
					if(medPic.size()!=0){
						adapter.addSection(getResources().getString(R.string.medicine), new EfficientAdapter(getApplicationContext(),medPic));
					}
						l1.setAdapter(adapter);
						
				//l1.setAdapter(new EfficientAdapter(getApplicationContext(),picList));
			} else {
				searchView.setVisibility(View.VISIBLE);
				if(getIntent().getIntExtra("Key", -1)!=-1)
				{
					key = getIntent().getIntExtra("Key", 0);
					if(key == -1)
					{
						etSearch.setHint(R.string.enter_keyword);
					}
					else if(key == 0)
					{
						spSearch.setVisibility(View.VISIBLE);
						etSearch.setVisibility(View.GONE);
						etSearch.setHint(R.string.enter_category);
					}
					else if(key == 1)
					{
						spSearch.setVisibility(View.GONE);
						etSearch.setVisibility(View.VISIBLE);
						etSearch.setHint(R.string.enter_desc);
					}
					
				}
				find = (Button) findViewById(R.id.btnFind);
				find.setOnClickListener(this);
			}
			    System.out.println("id~~~~~~~"+key);

//			l1.setOnItemClickListener(new OnItemClickListener() {
//
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int position, long arg3) {
//					if (fromWhere.equalsIgnoreCase("Other")) {
//						Intent i = new Intent();
//						i.putExtra("pid", picList.get(position).id);
//						setResult(RESULT_OK, i);
//						finish();
//
//					} else {
//						if (doWhat.equalsIgnoreCase("list")) {
//							Intent i = new Intent(PicManage_ListActivity.this,PicManage_AddShowActivity.class);
//							i.putExtra("idtoshow", picList.get(position).id);
//							Log.i("uid list", position + "--"+ picList.get(position).id);
//							i.putExtra("Task", "show");
//							i.putExtra("counter", position);
//							startActivity(i);
//						}
//						else
//						{
//						int temp = picList.indexOf(tempPicList.get(position));
//						Intent i = new Intent(PicManage_ListActivity.this,PicManage_AddShowActivity.class);
//						i.putExtra("idtoshow", picList.get(temp).id);
//						i.putExtra("Task", "show");
//						i.putExtra("counter", temp);
//						startActivity(i);
//						}
//						finish();
//					}
//				}
//
//			});
		
			    sectionPositionArrayList=new ArrayList<Integer>();
			    
		l1.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				userListLenght = usersPic.size();
				int firstSectionPosition,secondSectionPosition;
				if(usersPic.size()>0 && medPic.size()>0)
				{
					firstSectionPosition = 0;
					secondSectionPosition = (firstSectionPosition + userListLenght) + 1;	
				}
				else if(usersPic.size()==0)
				{
					firstSectionPosition = -1;
					secondSectionPosition = (firstSectionPosition + userListLenght) + 1;	
				}
				else
				{
					firstSectionPosition = 0;
					secondSectionPosition = (firstSectionPosition + userListLenght) + 1;	
				}

				sectionPositionArrayList.add(firstSectionPosition);
				sectionPositionArrayList.add(secondSectionPosition);

				Log.i(TAG, "arraylist size===>"
						+ sectionPositionArrayList.size());
				int j = 0;
				for (int i = 0; i < sectionPositionArrayList.size(); i++) {
					if (sectionPositionArrayList.get(i) < position) {
						j = sectionPositionArrayList.get(i);
					}
				}
				Log.i(TAG, "j===>" + j);
				
				
				if (fromWhere.equalsIgnoreCase("Other")) {
					if (j == firstSectionPosition)// user/pateint
					{
						int item = position - j-1;
						Log.i(TAG, "item===>" + item);
						Log.i("","id "+usersPic.get(item).id+" counter "+item);
						for(int k=0;k<picList.size();k++)
						{
							if(picList.get(k).id==usersPic.get(item).id)
							{
								Intent i = new Intent(PicManage_ListActivity.this,PicManage_AddShowActivity.class);
								i.putExtra("pid", usersPic.get(item).id);
								Log.i("uid list", position + "--"+ usersPic.get(item).id);
								setResult(RESULT_OK, i);
								finish();
						}
						}

					} else if (j == secondSectionPosition) // Medicine
					{
						int item = position - j-1;
						Log.i(TAG, "item===>" + item);
						Log.i("","id "+medPic.get(item).id+" counter "+item);
						for(int k=0;k<picList.size();k++)
						{
							
						if(picList.get(k).id==medPic.get(item).id){
						Intent i = new Intent(PicManage_ListActivity.this,PicManage_AddShowActivity.class);
						i.putExtra("pid", medPic.get(item).id);
						Log.i("uid list", position + "--"+ medPic.get(item).id);
						setResult(RESULT_OK, i);
						finish();
						}
						}

					}

				}
				else {
					if (doWhat.equalsIgnoreCase("list")) {
					Log.i(TAG, "position====>" + position);

					/*
					 * if(mFeaturedArraylist != null) { imageLenght = 1 ; } else {
					 * imageLenght = 0 ; }
					 */

					

					if (j == firstSectionPosition)// user/pateint
					{
						int item = position - j-1;
						Log.i(TAG, "item===>" + item);
						Log.i("","id "+usersPic.get(item).id+" counter "+item);
						for(int k=0;k<picList.size();k++)
						{
							if(picList.get(k).id==usersPic.get(item).id)
							{
								Intent i = new Intent(PicManage_ListActivity.this,PicManage_AddShowActivity.class);
								i.putExtra("idtoshow", usersPic.get(item).id);
								Log.i("uid list", position + "--"+ usersPic.get(item).id);
								i.putExtra("Task", "show");
								i.putExtra("counter", k);
								startActivity(i);
						}
						}

					} else if (j == secondSectionPosition) // Medicine
					{
						int item = position - j-1;
						Log.i(TAG, "item===>" + item);
						Log.i("","id "+medPic.get(item).id+" counter "+item);
						for(int k=0;k<picList.size();k++)
						{
							
						if(picList.get(k).id==medPic.get(item).id)
						{
							
						
						Intent i = new Intent(PicManage_ListActivity.this,PicManage_AddShowActivity.class);
						i.putExtra("idtoshow", medPic.get(item).id);
						Log.i("uid list", position + "--"+ medPic.get(item).id);
						i.putExtra("Task", "show");
						i.putExtra("counter", k);
						startActivity(i);
						}
						}

					}

					
				}
				else
				{
				int temp = picList.indexOf(tempPicList.get(position));
				Intent i = new Intent(PicManage_ListActivity.this,PicManage_AddShowActivity.class);
				i.putExtra("idtoshow", picList.get(temp).id);
				i.putExtra("Task", "show");
				i.putExtra("counter", temp);
				startActivity(i);
				}
				finish();
				}
		
			}

		});
		
		} else {
			Toast.makeText(getApplicationContext(),R.string.no_picture_stored_pic_mgt,
					Toast.LENGTH_SHORT).show();
			this.finish();
		}
	}

	private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<Picture_Model> picList;

		public EfficientAdapter(Context context,ArrayList<Picture_Model> picList) {
			mInflater = LayoutInflater.from(context);
			this.picList = picList;
		}

		public int getCount() {
			return picList.size();
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
			
				bitmap=GlobalMethods.decodeFile( picList.get(position).path);
		        holder.uImage.setImageBitmap(bitmap);
			
	       
	        
	        holder.uName.setText(picList.get(position).desc);
			if(picList.get(position).category.equalsIgnoreCase("1"))
				holder.uType.setText(R.string.people);
			else
				holder.uType.setText(R.string.medicine);
			

			return convertView;

		}

		class ViewHolder {

			TextView uName, uType;
			ImageView uImage;

		}
	}
	

	   public void onClick(View v) {
		if (v.getId() == R.id.btnFind) {
			tempPicList = new ArrayList<Picture_Model>();
			String text = etSearch.getText().toString();
			for (int i = 0; i < picList.size(); i++) {
				if(key==-1)
				{
					if(picList.get(i).all.toUpperCase().contains(text.toUpperCase()))
					{
						if(picList.get(i).all.toUpperCase().indexOf(
								text.toUpperCase())==0 || picList.get(i).all.charAt(
										(picList.get(i).all.toUpperCase().indexOf(
												text.toUpperCase()))-1) == ' ')
						{
						tempPicList.add(picList.get(i));
						}
					}
				}
				else if(key==0)
				{
					String temp = items1.get(Integer.parseInt(picList.get(i).category));
					if(temp.toUpperCase().contains(text.toUpperCase()))
					{
						
						if(temp.toUpperCase().indexOf(
								text.toUpperCase())==0 ||temp.charAt(
										(temp.toUpperCase().indexOf(
												text.toUpperCase()))-1) == ' ')
						{
						tempPicList.add(picList.get(i));
						}
					}
				}
				else if(key==1)
				{
					if(picList.get(i).desc.toUpperCase().contains(text.toUpperCase()))
					{
						if(picList.get(i).desc.toUpperCase().indexOf(
								text.toUpperCase())==0 || picList.get(i).desc.charAt(
										(picList.get(i).desc.toUpperCase().indexOf(
												text.toUpperCase()))-1) == ' ')
						{
						tempPicList.add(picList.get(i));
						}
					}
				}
			}
			if (tempPicList.size() > 0) {
				l1.setAdapter(new EfficientAdapter(getApplicationContext(),
						tempPicList));
				etSearch.setText("");
			} else {
				l1.setAdapter(new EfficientAdapter(getApplicationContext(),
						tempPicList));
				etSearch.setText("");
				Toast.makeText(PicManage_ListActivity.this, R.string.no_record_found,
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}