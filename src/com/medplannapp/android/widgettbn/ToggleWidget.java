package com.medplannapp.android.widgettbn;

import java.util.ArrayList;
import com.medplan.app.R;



import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ToggleWidget extends AppWidgetProvider
{

	private Context _context;
	private RemoteViews _remoteViews;
	private SharedPreferences sp;
	private ArrayList<String> _btnClassArray;
	int counter = 0,ecounter=0;

	
	
	public static final int HANDLING_TYPE_DONE = 3;
	public static final int HANDLING_TYPE_IMMEDIATE = 2; // Means ToggleWidget
															// should update the
															// btnImgs after
															// TButton.canHandlerIntent()
															// And only works with intent starts with "armNclover.intent"
	public static final int HANDLING_TYPE_UNABLE = 1;

	private final int[] _layoutIDList = new int[] { R.layout.widgetlayout};
	private final int[] _btnIdList = new int[] { R.id.Button1, R.id.Button2, R.id.Button3, R.id.Button4, R.id.Button5};
	private int _btnAmount = 5;
	
	private static TButton btnWifi,btnAirplane,btnBrightness,btnScreenAlwaysOn,btnSync;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		initWithPreference(context);
		updateButtonImg(appWidgetManager, appWidgetIds);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		_context = null;
	}

	@Override
	public void onReceive(final Context context, Intent intent)
		{
			initWithPreference(context);
	
			TButton btn;
			if (intent.getAction().startsWith("com.medplannapp.android.widgettbn"))
			{
				if (sp.getBoolean("press_vibrate", false))
				{
					Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
					vibrator.vibrate(50);
				}
	
				btn = getButtonByName(intent.getAction().substring(34));
				if (btn.canHandleIntent(context) == HANDLING_TYPE_DONE)
				{
					final TButton temp = btn;
	
					final int calls = temp.getCall();
					final int interval = temp.getTime();
					final Handler handler = new Handler();
					Runnable runnable = new Runnable() {
						
						@Override
						public void run() {
							temp.toggleSetting(context);
							counter++;
					        if(counter<calls)
					        	handler.postDelayed(this, interval*60000);
					        
					        Log.i("Values", counter+" -- "+calls+" >> "+interval);
						}
					};
					handler.post(runnable);
					
					final int ecalls = temp.getEmailCall();
					final int einterval = temp.getEmailTime();
					final Handler ehandler = new Handler();
					Runnable erunnable = new Runnable() {
						@Override
						public void run() {
							temp.sendMail(context);
							ecounter++;
					        if(ecounter<ecalls)
					        	handler.postDelayed(this, einterval*60000);
					        
					        Log.i("Values", ecounter+" -- "+ecalls+" >> "+einterval);
							
						}
					};
					ehandler.post(erunnable);
				
				}
				else
				{
					Toast.makeText(context, "Please set Contact for this button ", Toast.LENGTH_LONG).show();
				}
	
			} 
			else
			{
				for (int i = 0; i < _btnAmount; ++i)
				{
					btn = getButtonByName(_btnClassArray.get(i));
					if (btn.canHandleIntent(context) == HANDLING_TYPE_DONE)
					{
						updateButtonImg(null, null);
						break;
					}
				}
			}
	
			super.onReceive(context, intent);
			_context = null;
		}

	private void initWithPreference(Context context)
	{
		_context = context;

		if (sp == null)
			sp = PreferenceManager.getDefaultSharedPreferences(_context);

		int _btnAmount_new = Integer.parseInt(sp.getString("btn_amount", "5"));
		boolean layoutChanged = (_btnAmount_new != _btnAmount);
		_btnAmount = _btnAmount_new;
		if (_remoteViews == null || layoutChanged)
			_remoteViews = new RemoteViews(_context.getPackageName(), _layoutIDList[_btnAmount - 5]);

		if (_btnClassArray == null)
		{
			_btnClassArray = new ArrayList<String>();
			_btnClassArray.add(sp.getString("Button1", "ButtonFirst"));
			_btnClassArray.add(sp.getString("Button2", "ButtonSecond"));
			_btnClassArray.add(sp.getString("Button3", "ButtonThird"));
			_btnClassArray.add(sp.getString("Button4", "ButtonFourth"));
			_btnClassArray.add(sp.getString("Button5", "ButtonFifth"));
		}
	}

	// Replace java relectioin 
	private TButton getButtonByName(String name)
	{
		if (name.equals("ButtonFirst")) 
			return (btnWifi != null)?btnWifi:(btnWifi = new ButtonFirst());
		else if (name.equals("ButtonSecond"))
			return (btnAirplane!=null)?btnAirplane:(btnAirplane = new ButtonSecond());
		else if (name.equals("ButtonThird"))
			return (btnScreenAlwaysOn != null)?btnScreenAlwaysOn:(btnScreenAlwaysOn = new ButtonThird());
		else if (name.equals("ButtonFourth"))
			return (btnBrightness != null)?btnBrightness:(btnBrightness = new ButtonFourth());
		else
			return (btnSync != null)?btnSync:(btnSync = new ButtonFifth());
	}

	private void updateButtonImg(AppWidgetManager manager, int[] appWidgetIds)
	{
		for (int i = 0; i < _btnAmount; ++i)
		{
			TButton btn = getButtonByName(_btnClassArray.get(i));
			Intent myIntent = new Intent(btn.getIntentAction());
			PendingIntent pintent = PendingIntent.getBroadcast(_context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			int buttonImgResource = btn.getButtonBmp(_context);

			_remoteViews.setOnClickPendingIntent(_btnIdList[i], pintent);
			_remoteViews.setImageViewResource(_btnIdList[i], buttonImgResource);
		}

		if (manager == null)
			manager = AppWidgetManager.getInstance(_context);
		if (appWidgetIds != null)
			manager.updateAppWidget(appWidgetIds, _remoteViews);
		else
			manager.updateAppWidget(new ComponentName(_context, ToggleWidget.class), _remoteViews);
	}
}
