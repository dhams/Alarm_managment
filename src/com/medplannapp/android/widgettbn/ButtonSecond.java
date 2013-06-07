package com.medplannapp.android.widgettbn;


import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.medplan.app.GMailSender;
import com.medplan.app.R;
public class ButtonSecond extends TButton
{

	private final static String INTENT_ACTION = "com.medplannapp.android.widgettbn.ButtonSecond";
	private String number;
	public static int interval,callCount,mi,mc;
	public static int counter=0;
	public static String mail,wt;
	
	@Override
	public int getTime()
	{
		return 0;
	}
	
	
	@Override
	public int getCall()
	{
		return 0;
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
	public int getButtonBmp(Context context)
	{
		
				return R.drawable.cell2;
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
		
		if(!mail.equalsIgnoreCase(""))
		{
			
		}
		else
		{
			Toast.makeText(context, "Please enter a valid email address.", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public String getIntentAction()
	{
		return INTENT_ACTION;
	}

	@Override
	public int canHandleIntent(Context con)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(con);
		number = sp.getString("Second", "");
		if (!number.equalsIgnoreCase(""))
		{
			interval = sp.getInt("SecondTime", 1);
			callCount = sp.getInt("SecondCall", 1);
			mi = sp.getInt("SecondEmailTime", 1);
			mc = sp.getInt("SecondEmailCall",1);
			mail = sp.getString("SecondEmail", "");
			wt = sp.getString("SecondText", "");
			return ToggleWidget.HANDLING_TYPE_DONE;
		} else
		{
			return ToggleWidget.HANDLING_TYPE_UNABLE;
		}
	}

	@Override
	public void sendMail(Context context) {
		if(!mail.equalsIgnoreCase(""))
		{
			try {   
                GMailSender sender = new GMailSender("android.testapps@gmail.com", "androidandroid");
                sender.sendMail("Emergency Mail from Medplann",   
                        wt,   
                        "android.testapps@gmail.com",   
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
