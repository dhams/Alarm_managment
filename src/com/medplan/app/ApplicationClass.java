package com.medplan.app;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formKey = "dElmZ1JrZkFEU1N5bWNWTU9UcFlpTWc6MQ") 
public class ApplicationClass extends Application{

//	int totalCounter ; 
	int boxId , userId, loginId, cellPos,wayToStop , alarmSound ; 
	String medName ,desc  , fromWhere ;
	
	boolean isActive  ;
	
	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this) ;
		
//		totalCounter = 0 ;
		isActive = false ;
 
	}
}
