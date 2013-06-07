package com.medplannapp.android.widgettbn;

import java.util.Locale;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class WidgetApplication extends Application
{

	public Configuration cfg;
	
	@Override
    public void onCreate()
    {
		changeLocale(null);
	    super.onCreate();
    }
	
	public void changeLocale(String locale)
	{
		Resources resources = getResources();
		cfg = resources.getConfiguration();
		if(locale == null)
		{
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
			locale = sp.getString("locale", "en");
		}
		resources.updateConfiguration(cfg, resources.getDisplayMetrics());
		if(locale.equals("zh-rcn"))
			cfg.locale = Locale.CHINA;
		else
			cfg.locale = Locale.UK;
	}
}
