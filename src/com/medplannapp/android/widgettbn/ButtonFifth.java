package com.medplannapp.android.widgettbn;



import com.medplan.app.GMailSender;
import com.medplan.app.R;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class ButtonFifth extends TButton
{
	private final static String INTENT_ACTION = "com.medplannapp.android.widgettbn.ButtonFifth";
	private String number;
	public static int interval,callCount,mi,mc;
	public static int counter=0;
	public static String mail,wt;
	
	@Override
	public int getTime()
	{
		return interval;
	}
	
	
	@Override
	public int getCall()
	{
		return callCount;
	}
	
	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return mail;
	}
	@Override
	public String getEmailText() {
		// TODO Auto-generated method stub
		return wt;
	}
	
	@Override
	public int getEmailCall() {
		// TODO Auto-generated method stub
		return mc;
	}
	
	@Override
	public int getEmailTime() {
		// TODO Auto-generated method stub
		return mi;
	}
	@Override
	public int canHandleIntent(Context con)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(con);
		number = sp.getString("Fifth", "");
		if (!number.equalsIgnoreCase(""))
		{
			interval = sp.getInt("FifthTime", 1);
			callCount = sp.getInt("FifthCall", 1);
			mi = sp.getInt("FifthEmailTime", 1);
			mc = sp.getInt("FifthEmailCall",1);
			mail = sp.getString("FifthEmail", "");
			wt = sp.getString("FifthText", "");
			return ToggleWidget.HANDLING_TYPE_DONE;
		} else
		{
			return ToggleWidget.HANDLING_TYPE_UNABLE;
		}
	}
	public int getButtonBmp(Context context)
	{
		
				return R.drawable.cell5;
		}


	@Override
	public String getIntentAction()
	{
		return INTENT_ACTION;
	}

	@Override
	public void toggleSetting(Context context) {
		
		if(!number.equalsIgnoreCase(""))
		{
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setData(Uri.parse("tel:"+number));
        context.startActivity(callIntent);
		}
		else
		{
			Toast.makeText(context, "Please enter a valid mobile number.", Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	@Override
	public void sendMail(Context context) {
		
		if(!mail.equalsIgnoreCase(""))
		{
			try {   
                GMailSender sender = new GMailSender("android.testapps@gmail.com", "androidandroid");
                sender.sendMail("Emergency Mail from Medplann",  wt,    "android.testapps@gmail.com",   
                        mail);    
            } catch (Exception e) {   
                Log.e("SendMail", e.getMessage(), e);   
            }   
		}
		else
		{
			Toast.makeText(context, "Please enter a valid email address.", Toast.LENGTH_LONG).show();
		}
	}

}
