package com.medpan.util;


import java.io.IOException;

import com.medplan.app.BoxSixActivity;
import com.medplan.app.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CommonMethod {
	
	public static Vibrator vibrator;
	public static MediaPlayer player;
	
	

	
	 public static int arr_images[] = { 0,R.drawable.red,
            R.drawable.yellow, R.drawable.green,
            R.drawable.orange, R.drawable.black,R.drawable.white };
	 
//	 public static int arr_phone_icon[] = { 0,R.drawable.cell1,
//         R.drawable.cell2, R.drawable.cell3,
//         R.drawable.cell4, R.drawable.cell5};
	 
	public void CheckUserType(String _StrCheck, LinearLayout Rl, Button b1,
			Button b2, Button b3, Button b4, Button b5) {
		if (_StrCheck.equalsIgnoreCase("User/Patient")) {
			Constant.Patient = new int[] { R.drawable.main_org,
					R.drawable.add_org, R.drawable.show_org,
					R.drawable.list_org, R.drawable.search_org,
					R.drawable.filter_org};

		}
		if (_StrCheck.equalsIgnoreCase("Doctor/Physician")) {
			Constant.Patient = new int[] { R.drawable.main_green,
					R.drawable.add_green, R.drawable.show_green,
					R.drawable.list_green, R.drawable.search_green,
					R.drawable.filter_green };

		}
		if (_StrCheck.equalsIgnoreCase("Pharma")) {
			Constant.Patient = new int[] { R.drawable.main_blue,
					R.drawable.add_blue, R.drawable.show_blue,
					R.drawable.list_blue, R.drawable.search_blue,
					R.drawable.filter_blue};
		}

		Rl.setBackgroundResource(Constant.Patient[0]);
		b1.setBackgroundResource(Constant.Patient[1]);
		b2.setBackgroundResource(Constant.Patient[2]);
		b3.setBackgroundResource(Constant.Patient[3]);
		b4.setBackgroundResource(Constant.Patient[4]);
		b5.setBackgroundResource(Constant.Patient[5]);
		

	}

	public void CheckConfigureScreen(String _StrCheck,LinearLayout LL, RelativeLayout RLOne,
			RelativeLayout RLTwo, RelativeLayout RLThree,
			RelativeLayout RLFour, RelativeLayout RLFive, RelativeLayout RLSix,
			RelativeLayout RLSeven, RelativeLayout RLEight,
			RelativeLayout RLNine, RelativeLayout RLTen,RelativeLayout RLEleven) {
		
		if (_StrCheck.equalsIgnoreCase("User/Patient")) {
			Constant.ConfigureScreen = new int[] { Color.parseColor(Constant._Bgcolor[0]),
					R.drawable.title_bar_org,
					R.drawable.config_bg_org,R.drawable.config_bg_org,
					R.drawable.config_bg_org,R.drawable.config_bg_org,
					R.drawable.config_bg_org,R.drawable.config_bg_org,
					R.drawable.config_bg_org,R.drawable.config_bg_org,
					R.drawable.config_bg_org,R.drawable.config_bg_org};

		}
		if (_StrCheck.equalsIgnoreCase("Doctor/Physician")) {
			Constant.ConfigureScreen = new int[] {Color.parseColor(Constant._Bgcolor[1]),
					R.drawable.title_bar_green,
					R.drawable.config_bg_green,R.drawable.config_bg_green,
					R.drawable.config_bg_green,R.drawable.config_bg_green,
					R.drawable.config_bg_green,R.drawable.config_bg_green,
					R.drawable.config_bg_green,R.drawable.config_bg_green,
					R.drawable.config_bg_green,R.drawable.config_bg_green};

		}
		if (_StrCheck.equalsIgnoreCase("Pharma")) {
			Constant.ConfigureScreen = new int[] {Color.parseColor(Constant._Bgcolor[2]),
					R.drawable.title_bar_blue,
					R.drawable.config_bg_blue,R.drawable.config_bg_blue,
					R.drawable.config_bg_blue,R.drawable.config_bg_blue,
					R.drawable.config_bg_blue,R.drawable.config_bg_blue,
					R.drawable.config_bg_blue,R.drawable.config_bg_blue,
					R.drawable.config_bg_blue,R.drawable.config_bg_blue};
		}
		
		LL.setBackgroundColor(Constant.ConfigureScreen[0]);
		RLOne.setBackgroundResource(Constant.ConfigureScreen[1]);
		RLTwo.setBackgroundResource(Constant.ConfigureScreen[2]);
		RLThree.setBackgroundResource(Constant.ConfigureScreen[3]);
		RLFour.setBackgroundResource(Constant.ConfigureScreen[4]);
		RLFive.setBackgroundResource(Constant.ConfigureScreen[5]);
		RLSix.setBackgroundResource(Constant.ConfigureScreen[6]);
		RLSeven.setBackgroundResource(Constant.ConfigureScreen[7]);
		RLEight.setBackgroundResource(Constant.ConfigureScreen[8]);
		RLNine.setBackgroundResource(Constant.ConfigureScreen[9]);
		RLTen.setBackgroundResource(Constant.ConfigureScreen[10]);
		RLEleven.setBackgroundResource(Constant.ConfigureScreen[11]);

	}
	
	public void CheckAddShowScreen(String _StrCheck,RelativeLayout RLHead,
			LinearLayout RLBackground){
		
		if (_StrCheck.equalsIgnoreCase("User/Patient")) {
			Constant.AddShowScreen = new int[] { R.drawable.title_bar_org,
					Color.parseColor(Constant._Bgcolor[0])};

		}
		if (_StrCheck.equalsIgnoreCase("Doctor/Physician")) {
			Constant.AddShowScreen = new int[] { R.drawable.title_bar_green,
					Color.parseColor(Constant._Bgcolor[1])};

		}
		if (_StrCheck.equalsIgnoreCase("Pharma")) {
			Constant.AddShowScreen = new int[] { R.drawable.title_bar_blue,
					Color.parseColor(Constant._Bgcolor[2])};
		}
		
		RLHead.setBackgroundResource(Constant.AddShowScreen[0]);
		RLBackground.setBackgroundColor(Constant.AddShowScreen[1]);

		//the value of _StrCheck is default set "User/Patient" .  
		
	}
	
	public void CellScreen(String _StrCheck,RelativeLayout RLHead){
		
		if (_StrCheck.equalsIgnoreCase("User/Patient")) {
			Constant.CellScreen =  R.drawable.title_bar_org;
		}
		if (_StrCheck.equalsIgnoreCase("Doctor/Physician")) {
			Constant.CellScreen =  R.drawable.title_bar_green;
		}
		if (_StrCheck.equalsIgnoreCase("Pharma")) {
			Constant.CellScreen =  R.drawable.title_bar_blue;
			
		}
		
		RLHead.setBackgroundResource(Constant.CellScreen);
		
	}
	
public void MainScreen(String _StrCheck,RelativeLayout RLHead){
		
		if (_StrCheck.equalsIgnoreCase("User/Patient")) {
			Constant.MainScreen =  R.drawable.main_layer_org;
					

		}
		if (_StrCheck.equalsIgnoreCase("Doctor/Physician")) {
			Constant.MainScreen =  R.drawable.main_layer_green;
			

		}
		if (_StrCheck.equalsIgnoreCase("Pharma")) {
			Constant.MainScreen =  R.drawable.main_layer_blue;
		}
		
		RLHead.setBackgroundResource(Constant.MainScreen);
		
	}



//	public static int calculateInSampleSize(BitmapFactory.Options options,
//			int reqWidth, int reqHeight) {
//		// Raw height and width of image
//		final int height = options.outHeight;
//		final int width = options.outWidth;
//		int inSampleSize = 1;
//
//		if (height > reqHeight || width > reqWidth) {
//			if (width > height) {
//				inSampleSize = Math.round((float) height / (float) reqHeight);
//			} else {
//				inSampleSize = Math.round((float) width / (float) reqWidth);
//			}
//		}
//		return inSampleSize;
//	}
//
//	public static Bitmap decodeSampledBitmapFromResource(String resId,
//			int reqWidth, int reqHeight) {
//
//		// First decode with inJustDecodeBounds=true to check dimensions
//		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(resId, options);
//
//		// Calculate inSampleSize
//		options.inSampleSize = calculateInSampleSize(options, reqWidth,
//				reqHeight);
//
//		// Decode bitmap with inSampleSize set
//		options.inJustDecodeBounds = false;
//		return BitmapFactory.decodeFile(resId, options);
//	}
	
public static int calculateInSampleSize(BitmapFactory.Options options,
		int reqWidth, int reqHeight) {
	// Raw height and width of image
	final int height = options.outHeight;
	final int width = options.outWidth;
	int inSampleSize = 1;

	if (height > reqHeight || width > reqWidth) {
		if (width > height) {
			inSampleSize = Math.round((float) height / (float) reqHeight);
		} else {
			inSampleSize = Math.round((float) width / (float) reqWidth);
		}
	}
	return inSampleSize;
}

public static Bitmap decodeSampledBitmapFromResource(String resId,
		int reqWidth, int reqHeight) {

	// First decode with inJustDecodeBounds=true to check dimensions
	final BitmapFactory.Options options = new BitmapFactory.Options();
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeFile(resId, options);

	// Calculate inSampleSize
	options.inSampleSize = calculateInSampleSize(options, reqWidth,
			reqHeight);

	// Decode bitmap with inSampleSize set
	options.inJustDecodeBounds = false;
	return BitmapFactory.decodeFile(resId, options);
}



	public static void vibration(Context ctx){ 
		 vibrator = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
		 
		    //vibrator.vibrate(500);  
		    long[] pattern = {0L,100L,250L,1000L,250L,500L};  
		    vibrator.vibrate(pattern,2);  
	}
	
	public static void SoundPlayer(Context ctx,int raw_id){
		player =  new MediaPlayer().create(ctx, raw_id);
//		player.setLooping(false); // Set looping
		player.setVolume(100, 100);
		
		//player.release();
		 player.start();
		 Log.d("Sound " , "Is playing ...................");
	}
	public static void ShowToast(Activity context)
	{
		LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast,
                                       (ViewGroup) context.findViewById(R.id.relativeLayout1));
        Toast toast = new Toast(context.getApplicationContext());
        toast.setView(view);
        toast.show();
	}

}
