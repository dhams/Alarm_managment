package com.medpan.util;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.medplan.app.GMailSender;
import com.medplan.app.R;

public class GlobalMethods {

	public static AlertDialog alertMsg;
	public static AlertDialog.Builder alertDialog;
	public static SoundManager mSoundManager;

	public static void Common_alert(Context ctx) {
		alertDialog = new AlertDialog.Builder(ctx);
		alertDialog.setTitle("Confirm"); 
		alertDialog.setMessage("Did you taken your medicine?");
		alertDialog.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					}
				});

		alertDialog.setNegativeButton(R.string.no_thanks,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}
				});
		alertMsg = alertDialog.create();
		alertDialog.show();
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 2;

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

		// First decode with inJustDecodeBounds=true to check dimensionsve 
		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(resId, options);

		// Calculate inSampleSize
//		options.inSampleSize = calculateInSampleSize(options, reqWidth,
//				reqHeight);

		options.inSampleSize = 4; 
		
		// Decode bitmap with inSampleSize set
//		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(resId, options);
	}

	public static Bitmap decodeFile(String path)  {
		
		int orientation = 0;
		try {
			if (path == null) {
				return null;
			}
			
			Bitmap bm = decodeSampledBitmapFromResource(path, 70, 70);
			
			
//			// decode image size 
//			BitmapFactory.Options o = new BitmapFactory.Options();
////			o.inJustDecodeBounds = true;
//			// Find the correct scale value. It should be the power of 2.
//			final int REQUIRED_SIZE = 70;
//			int width_tmp = o.outWidth, height_tmp = o.outHeight;
//			final int scale = 4;
////			while (true) {
////				if (width_tmp / 2 < REQUIRED_SIZE
////						|| height_tmp / 2 < REQUIRED_SIZE)
////					break;
////				width_tmp /= 2;
////				height_tmp /= 2;
////				scale++;
////			}
//			
//			Log.e("ExifInteface .........", "Scale ="+scale);
//			// decode with inSampleSize
//			BitmapFactory.Options o2 = new BitmapFactory.Options();
//			o2.inSampleSize = scale;
//			Bitmap bm = BitmapFactory.decodeFile(path, o2);
////			Bitmap bitmap = bm;

			
			ExifInterface exif = new ExifInterface(path);
			
			orientation = exif
					.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			
         	Log.e("ExifInteface .........", "rotation ="+orientation);
			
//			exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);
			
			Log.e("orientation", "" + orientation);
			Matrix m = new Matrix();

			if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
				m.postRotate(180);
//				m.postScale((float) bm.getWidth(), (float) bm.getHeight());
				// if(m.preRotate(90)){
				Log.e("in orientation", "" + orientation);
				bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bm;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				m.postRotate(90); 
				Log.e("in orientation", "" + orientation);
				bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bm;
			}
			else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				m.postRotate(270);
				Log.e("in orientation", "" + orientation);
				bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bm;
			} 
			return bm;
		} catch (Exception e) {
			doMail(orientation , e.toString()) ;
			return null;
		}
	}

	
	
	public static Bitmap decodeFileForReg(String path , Uri uri ,Context context)  {
		
		int orientation = 0;
		try {
			if (path == null) {
				return null;
			}
			
			Bitmap bm = decodeSampledBitmapFromResource(path, 70, 70);
			
			if (bm==null){
				bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
				if (bm==null)
					doMail(0 , "Image null") ;		
			}
			
			ExifInterface exif = new ExifInterface(path);
			
			orientation = exif
					.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			
         	Log.e("ExifInteface .........", "rotation ="+orientation);
			
//			exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);
			
			Log.e("orientation", "" + orientation);
			Matrix m = new Matrix();

			if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
				m.postRotate(180);
//				m.postScale((float) bm.getWidth(), (float) bm.getHeight());
				// if(m.preRotate(90)){
				Log.e("in orientation", "" + orientation);
				bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bm;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				m.postRotate(90); 
				Log.e("in orientation", "" + orientation);
				bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bm;
			}
			else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				m.postRotate(270);
				Log.e("in orientation", "" + orientation);
				bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bm;
			} 
			return bm;
		} catch (Exception e) {
			doMail(orientation , e.toString()) ;
			return null;
		}
	}
	
	public static void doMail(int orientation , String exception){
		GMailSender sender = new GMailSender("android.testapps@gmail.com", "androidandroid");
		try {
			sender.sendMail("Medplann image",   
			    " image has not displayed and orientation is ="+orientation+" and exception is "+exception,   
			    "android.testapps@gmail.com",   
			    "dharmendra@openxcelltechnolabs.com");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public static boolean isEmail(String email) {
		boolean matchFound1;
		boolean returnResult = true;
		email = email.trim();
		if (email.equalsIgnoreCase(""))
			// returnResult = false; // when field is mandatory.
			returnResult = true; // when field is not mandatory.
		else if (!Character.isLetter(email.charAt(0)))
			returnResult = false;
		else {
			Pattern p1 = Pattern.compile("^\\.|^\\@ |^_");
			Matcher m1 = p1.matcher(email.toString());
			matchFound1 = m1.matches();

			Pattern p = Pattern
					.compile("^[a-zA-z0-9._-]+[@]{1}+[a-zA-Z0-9]+[.]{1}+[a-zA-Z]{2,4}$");
			// Match the given string with the pattern
			Matcher m = p.matcher(email.toString());

			// check whether match is found
			boolean matchFound = m.matches();

			StringTokenizer st = new StringTokenizer(email, ".");
			String lastToken = null;
			while (st.hasMoreTokens()) {
				lastToken = st.nextToken();
			}//oh come on honey ..  i am your friend , at least  you share that kind of information with me 
			if (matchFound && lastToken.length() >= 2
					&& email.length() - 1 != lastToken.length()
					&& matchFound1 == false) {

				returnResult = true;
			} else
				returnResult = false;
		}
		return returnResult;
	}

}
