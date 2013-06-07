package com.medplannapp.android.widgettbn;

import android.content.Context;
import android.content.Intent;

public abstract class TButton {

	public static int interval, callCount, mi, mc;
	public static int counter = 0;
	public static String mail, wt;

	public TButton() {
	}

	public abstract int getButtonBmp(Context context);

	public abstract int getTime();

	public abstract int getCall();

	public abstract int getEmailTime();

	public abstract int getEmailCall();

	public abstract String  getEmail();

	public abstract String getEmailText();

	public abstract void toggleSetting(Context context);
	
	public abstract void sendMail(Context context);

	public abstract String getIntentAction();

	public abstract int canHandleIntent(Context con);
}
