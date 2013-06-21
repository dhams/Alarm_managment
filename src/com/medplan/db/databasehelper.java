package com.medplan.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.medpan.util.CellInfo_Model;
import com.medpan.util.Constant;
import com.medpan.util.Contact_Model;
import com.medpan.util.Medicine_Model;
import com.medpan.util.Notification_Model;
import com.medpan.util.PendingAlarmUtil;
import com.medpan.util.Phys_Model;
import com.medpan.util.Picture_Model;
import com.medpan.util.Report_Model;
import com.medpan.util.User_Model;

public class databasehelper extends SQLiteOpenHelper {
	private static String DATABASE_NAME = "MedPlan.sqlite";
	private static SQLiteDatabase myDataBase;
	private Context myContext;
	private int uid = 0, cellid;
	private String TAG = this.getClass().getSimpleName();
	private String path = "/data/data/com.medplan.app/databases/";
	
	public databasehelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		this.myContext = context;
	}

	// ---Create the database---
	public void createDataBase() throws IOException {
		// ---Check whether database is already created or not---
		
		boolean dbExist = checkDataBase();

		if (!dbExist) {
			this.getReadableDatabase();
			try {
				// ---If not created then copy the database---
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}

	}

	// --- Check whether database already created or not---
	private boolean checkDataBase() {
		try {
			
			String myPath = path + DATABASE_NAME;
			File f = new File(myPath);
			if (f.exists())
				return true;
			else
				return false;
		} catch (SQLiteException e) {
			e.printStackTrace();
			return false;
		}
	}

	// --- Copy the database to the output stream---
	private void copyDataBase() throws IOException {
		try {
			InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

			String outFileName = path + DATABASE_NAME;

			OutputStream myOutput = new FileOutputStream(outFileName);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			myOutput.flush();
			myOutput.close();
			myInput.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void openDataBase() throws SQLException {

		// --- Open the database---
		String myPath = path + DATABASE_NAME;
	
//		if (myDataBase!=null)
//		if (!myDataBase.isOpen())
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) 
	{
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public boolean notAlreadyExistCell(int uid) {
		boolean flg = true;
		openDataBase();
		Cursor c = myDataBase.rawQuery(
				"select * from UserCell_Medplann where UserID=" + uid, null);
		if (c.getCount() > 0) {
			flg = false;
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return flg;

	}

	// //////////////////////// User ////////////////////////////////

	public int getUserID() {
		return uid;
	}

	public void insertUser(String path, String firstname, String lastname,
			String username, String password, String zip, String address,
			String country, String state, String city, String type) {

		openDataBase();
		ContentValues initialValues = new ContentValues();

		initialValues.put("user_image_path", path);
		initialValues.put("user_fname", firstname);
		initialValues.put("user_lname", lastname);
		initialValues.put("user_uname", username);
		initialValues.put("user_password", password);
		initialValues.put("user_zip", zip);
		initialValues.put("user_address", address);
		initialValues.put("user_country", country);
		initialValues.put("user_state", state);
		initialValues.put("user_city", city);
		initialValues.put("user_type", type);
		myDataBase.insert("User_Master", null, initialValues);
//		myDataBase.close();

	}

	public boolean notAlreadyExist(String username) {
		boolean flg = true;
		openDataBase();
		Cursor c = myDataBase
				.rawQuery("select * from User_Master where user_uname='"
						+ username + "'", null);
		if (c.getCount() > 0) {
			// cellid = c.getInt(c.getColumnIndex("CellId"));
			flg = false;
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return flg;

	}

	public boolean isValidUser(String username, String password) {
		boolean type = false;
		openDataBase();
		Cursor c;
		try {
			c = myDataBase.rawQuery(
					"select * from User_Master where user_uname='" + username
							+ "' and user_password='" + password + "'", null);
			if (c.getCount() > 0) {
				c.moveToFirst();
				type = true;
				uid = c.getInt(0);
				Constant._StrCheck = c.getString(c.getColumnIndex("user_type"));
				Log.i("type", "//////////" + type);
				Log.i("checktyppe", "//////////" + Constant._StrCheck);
				c.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return type;

	}

	// //////////////////////// User ////////////////////////////////

	// //////////////////////// Physician ////////////////////////////////
	
	public int getPhyCountPic(int picid)
	{
		openDataBase();
		int count =0;
		Cursor c = myDataBase.rawQuery("select * from physician_medplann where phy_img_id="+picid, null);
		if (c.getCount() > 0) {
			count = c.getCount();
		}
		
		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return count;
	}
	

	public void insertPhysician(int userid, int iid, String firstname,
			String lastname, String address, String city, String zipcode,
			String country, String state, int gen, String tel, String mobile,
			String mail, String visiting, String note1, String note2,
			String mon, int monfrom, int monto, String tue, int tuefrom,
			int tueto, String wed, int wedfrom, int wedto, String thur,
			int thurfrom, int thurto, String fri, int frifrom, int frito,
			String sat, int satfrom, int satto, String sun, int sunfrom,
			int sunto) {

		openDataBase();
		ContentValues initialValues = new ContentValues();

		initialValues.put("phy_img_id", iid);
		initialValues.put("phy_name", firstname);
		initialValues.put("phy_surname", lastname);
		initialValues.put("phy_address", address);
		initialValues.put("phy_city", city);
		initialValues.put("phy_zip_code", zipcode);
		initialValues.put("phy_country", country);
		initialValues.put("phy_state", state);
		initialValues.put("phy_gender", gen);
		initialValues.put("phy_telephone", tel);
		initialValues.put("phy_mobile", mobile);
		initialValues.put("phy_mail", mail);
		initialValues.put("phy_visit", visiting);
		initialValues.put("phy_note_one", note1);
		initialValues.put("phy_note_two", note2);
		initialValues.put("phy_user_id", userid);

		initialValues.put("mon", mon);
		initialValues.put("mon_from", monfrom);
		initialValues.put("mon_to", monto);

		initialValues.put("tues", tue);
		initialValues.put("tues_from", tuefrom);
		initialValues.put("tues_to", tueto);

		initialValues.put("wed", wed);
		initialValues.put("wed_from", wedfrom);
		initialValues.put("wed_to", wedto);

		initialValues.put("thurs", thur);
		initialValues.put("thurs_from", thurfrom);
		initialValues.put("thurs_to", thurto);

		initialValues.put("fri", fri);
		initialValues.put("fri_from", frifrom);
		initialValues.put("fri_to", frito);

		initialValues.put("sat", sat);
		initialValues.put("sat_from", satfrom);
		initialValues.put("sat_to", satto);

		initialValues.put("sun", sun);
		initialValues.put("sun_from", sunfrom);
		initialValues.put("sun_to", sunto);

		myDataBase.insert("physician_medplann", null, initialValues);
//		myDataBase.close();

	}

	public boolean notAlreadyExistPhysician(String iid) {
		boolean flg = true;
		openDataBase();
		Cursor c = myDataBase.rawQuery(
				"select * from physician_medplann where phy_img_id=" + iid,
				null);
		if (c.getCount() > 0) {
			flg = false;
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return flg;

	}

	public void deletePhysican(int id) {
		openDataBase();
		myDataBase.delete("physician_medplann", "phy_id=" + id + "", null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}

	public void updatePhysician(int userid, int id, int iid, String firstname,
			String lastname, String address, String city, String zipcode,
			String country, String state, int gen, String tel, String mobile,
			String mail, String visiting, String note1, String note2,
			String mon, int monfrom, int monto, String tue, int tuefrom,
			int tueto, String wed, int wedfrom, int wedto, String thur,
			int thurfrom, int thurto, String fri, int frifrom, int frito,
			String sat, int satfrom, int satto, String sun, int sunfrom,
			int sunto) {
		openDataBase();
		ContentValues initialValues = new ContentValues();

		initialValues.put("phy_img_id", iid);
		initialValues.put("phy_name", firstname);
		initialValues.put("phy_surname", lastname);
		initialValues.put("phy_address", address);
		initialValues.put("phy_city", city);
		initialValues.put("phy_zip_code", zipcode);
		initialValues.put("phy_country", country);
		initialValues.put("phy_state", state);
		initialValues.put("phy_gender", gen);
		initialValues.put("phy_telephone", tel);
		initialValues.put("phy_mobile", mobile);
		initialValues.put("phy_mail", mail);
		initialValues.put("phy_visit", visiting);
		initialValues.put("phy_note_one", note1);
		initialValues.put("phy_note_two", note2);
		initialValues.put("phy_user_id", userid);

		initialValues.put("mon", mon);
		initialValues.put("mon_from", monfrom);
		initialValues.put("mon_to", monto);

		initialValues.put("tues", tue);
		initialValues.put("tues_from", tuefrom);
		initialValues.put("tues_to", tueto);

		initialValues.put("wed", wed);
		initialValues.put("wed_from", wedfrom);
		initialValues.put("wed_to", wedto);

		initialValues.put("thurs", thur);
		initialValues.put("thurs_from", thurfrom);
		initialValues.put("thurs_to", thurto);

		initialValues.put("fri", fri);
		initialValues.put("fri_from", frifrom);
		initialValues.put("fri_to", frito);

		initialValues.put("sat", sat);
		initialValues.put("sat_from", satfrom);
		initialValues.put("sat_to", satto);

		initialValues.put("sun", sun);
		initialValues.put("sun_from", sunfrom);
		initialValues.put("sun_to", sunto);

		myDataBase.update("physician_medplann", initialValues, "phy_id=" + id,
				null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}

	public int getNewPhyID() {
		int id = 0;
		openDataBase();
		Cursor c = myDataBase
				.rawQuery("select * from physician_medplann", null);
		if (c.getCount() > 0) {
			c.moveToLast();
			id = c.getInt(0);
		}
		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return id;
	}

	public ArrayList<Phys_Model> getSinglePhys(int id) {
		// CREATE TABLE "notifications" ("id" INTEGER PRIMARY KEY AUTOINCREMENT
		// NOT NULL , "status" TEXT, "schedule" TEXT)

		ArrayList<Phys_Model> User_List = new ArrayList<Phys_Model>();
//
		openDataBase();
		Cursor c = myDataBase.rawQuery(
				"select * from physician_medplann where phy_id=" + id, null);
		if (c.getCount() > 0) {
			c.moveToFirst();

			Phys_Model User_Item;
			do {
				User_Item = new Phys_Model();
				User_Item.pid = c.getInt(0);
				User_Item.iid = c.getInt(1);
				User_Item.name = c.getString(2);
				User_Item.surname = c.getString(3);
				User_Item.addres = c.getString(4);
				User_Item.city = c.getString(5);
				User_Item.zip = c.getString(6);
				User_Item.country = c.getString(7);
				User_Item.state = c.getString(15);
				User_Item.gen = c.getInt(37);
				User_Item.tel = c.getString(8);
				User_Item.mobile = c.getString(9);
				User_Item.mail = c.getString(10);
				User_Item.visiting = c.getString(11);
				User_Item.note1 = c.getString(12);
				User_Item.note2 = c.getString(13);

				User_Item.mon = c.getString(16);
				if (User_Item.mon.equalsIgnoreCase("true")) {
					User_Item.visiting = "Monday ";
				}
				User_Item.monfrom = c.getInt(17);
				User_Item.monto = c.getInt(18);

				User_Item.tue = c.getString(19);
				if (User_Item.tue.equalsIgnoreCase("true")) { 
					User_Item.visiting = User_Item.visiting + "Tuesday ";
				}
				User_Item.tuefrom = c.getInt(20);
				User_Item.tuesto = c.getInt(21);

				User_Item.wed = c.getString(22);
				if (User_Item.wed.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Wednesday ";
				}
				User_Item.webfrom = c.getInt(23);
				User_Item.wedto = c.getInt(24);

				User_Item.thu = c.getString(25);
				if (User_Item.thu.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Thursday ";
				}
				User_Item.thurfrom = c.getInt(26);
				User_Item.thursto = c.getInt(27);

				User_Item.fri = c.getString(28);
				if (User_Item.fri.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Friday ";
				}
				User_Item.frifrom = c.getInt(29);
				User_Item.frito = c.getInt(30);

				User_Item.sat = c.getString(31);
				if (User_Item.sat.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Saturday ";
				}
				User_Item.satfrom = c.getInt(32);
				User_Item.satto = c.getInt(33);

				User_Item.sun = c.getString(34);
				if (User_Item.sun.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Sunday ";
				}
				User_Item.sunfrom = c.getInt(35);
				User_Item.sunto = c.getInt(36);

				User_List.add(User_Item);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return User_List;

	}

	public ArrayList<Phys_Model> getPhys(int userid) {
		// CREATE TABLE "notifications" ("id" INTEGER PRIMARY KEY AUTOINCREMENT
		// NOT NULL , "status" TEXT, "schedule" TEXT)

		ArrayList<Phys_Model> User_List = new ArrayList<Phys_Model>();
//
		openDataBase();
		Cursor c = myDataBase.rawQuery(
				"select * from physician_medplann where phy_user_id=" + userid,
				null);
		if (c.getCount() > 0) {
			c.moveToFirst();

			Phys_Model User_Item;
			do {
				User_Item = new Phys_Model();
				User_Item.pid = c.getInt(0);
				User_Item.iid = c.getInt(1);
				User_Item.name = c.getString(2);
				User_Item.surname = c.getString(3);
				User_Item.addres = c.getString(4);
				User_Item.city = c.getString(5);
				User_Item.zip = c.getString(6);
				User_Item.country = c.getString(7);
				User_Item.state = c.getString(15);
				User_Item.gen = c.getInt(37);
				User_Item.tel = c.getString(8);
				User_Item.mobile = c.getString(9);
				User_Item.mail = c.getString(10);
				User_Item.visiting = c.getString(11);
				User_Item.note1 = c.getString(12);
				User_Item.note2 = c.getString(13);

				User_Item.mon = c.getString(16);

				if (User_Item.mon.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Monday ";
				}
				
				User_Item.monfrom = c.getInt(17);
				User_Item.monto = c.getInt(18);

				User_Item.tue = c.getString(19);
				if (User_Item.tue.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Tuesday ";
				}
				
				User_Item.tuefrom = c.getInt(20);
				User_Item.tuesto = c.getInt(21);

				User_Item.wed = c.getString(22);
				if (User_Item.wed.equalsIgnoreCase("true")) { 
					User_Item.visiting = User_Item.visiting + "Wednesday ";
				}
				User_Item.webfrom = c.getInt(23);
				User_Item.wedto = c.getInt(24);

				User_Item.thu = c.getString(25);
				if (User_Item.thu.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Thursday ";
				}
				User_Item.thurfrom = c.getInt(26);
				User_Item.thursto = c.getInt(27);

				User_Item.fri = c.getString(28);
				if (User_Item.fri.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Friday ";
				}
				User_Item.frifrom = c.getInt(29);
				User_Item.frito = c.getInt(30);

				User_Item.sat = c.getString(31);
				if (User_Item.sat.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Saturday ";
				}
				User_Item.satfrom = c.getInt(32);
				User_Item.satto = c.getInt(33);

				User_Item.sun = c.getString(34);
				if (User_Item.sun.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + " Sunday";
				}
				User_Item.sunfrom = c.getInt(35);
				User_Item.sunto = c.getInt(36);

				User_Item.all = User_Item.name + " " + User_Item.surname + " "
						+ User_Item.addres + " " + User_Item.city + " "
						+ User_Item.zip + " " + User_Item.country + " "
						+ User_Item.tel + " " + User_Item.mobile + " "
						+ User_Item.mail + " "
						+ User_Item.note1 + " " + User_Item.note2 + " ";

				User_List.add(User_Item);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return User_List;

	}

	public ArrayList<Phys_Model> getPhys() {
		// CREATE TABLE "notifications" ("id" INTEGER PRIMARY KEY AUTOINCREMENT
		// NOT NULL , "status" TEXT, "schedule" TEXT)

		ArrayList<Phys_Model> User_List = new ArrayList<Phys_Model>();

		openDataBase();
		Cursor c = myDataBase.rawQuery("select * from physician_medplann ",
				null);
		if (c.getCount() > 0) {
			c.moveToFirst();

			Phys_Model User_Item;
			do {
				User_Item = new Phys_Model();
				User_Item.pid = c.getInt(0);
				User_Item.iid = c.getInt(1);
				User_Item.name = c.getString(2);
				User_Item.surname = c.getString(3);
				User_Item.addres = c.getString(4);
				User_Item.city = c.getString(5);
				User_Item.zip = c.getString(6);
				User_Item.country = c.getString(7);
				User_Item.state = c.getString(15);
				User_Item.gen = c.getInt(37);
				User_Item.tel = c.getString(8);
				User_Item.mobile = c.getString(9);
				User_Item.mail = c.getString(10);
				User_Item.visiting = c.getString(11);
				User_Item.note1 = c.getString(12);
				User_Item.note2 = c.getString(13);

				User_Item.mon = c.getString(16);
				if (User_Item.mon.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Monday ";
				}
				User_Item.monfrom = c.getInt(17);
				User_Item.monto = c.getInt(18);

				User_Item.tue = c.getString(19);
				if (User_Item.tue.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Tuesday ";
				}
				User_Item.tuefrom = c.getInt(20);
				User_Item.tuesto = c.getInt(21);

				User_Item.wed = c.getString(22);
				if (User_Item.wed.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Wednesday ";
				}
				User_Item.webfrom = c.getInt(23);
				User_Item.wedto = c.getInt(24);

				User_Item.thu = c.getString(25);
				if (User_Item.thu.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Thursday ";
				}
				User_Item.thurfrom = c.getInt(26);
				User_Item.thursto = c.getInt(27);

				User_Item.fri = c.getString(28);
				if (User_Item.fri.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Friday ";
				}
				User_Item.frifrom = c.getInt(29);
				User_Item.frito = c.getInt(30);

				User_Item.sat = c.getString(31);
				if (User_Item.sat.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Saturday ";
				}
				User_Item.satfrom = c.getInt(32);
				User_Item.satto = c.getInt(33);

				User_Item.sun = c.getString(34);
				if (User_Item.sun.equalsIgnoreCase("true")) {
					User_Item.visiting = User_Item.visiting + "Sunday ";
				}
				User_Item.sunfrom = c.getInt(35);
				User_Item.sunto = c.getInt(36);

				User_Item.all =User_Item.name + " " + User_Item.surname + " "
						+ User_Item.addres + " " + User_Item.city + " "
						+ User_Item.zip + " " + User_Item.country + " "
						+ User_Item.tel + " " + User_Item.mobile + " "
						+ User_Item.mail + " "
						+ User_Item.note1 + " " + User_Item.note2 + " ";

				User_List.add(User_Item);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return User_List;

	}

	// //////////////////////// Physician ////////////////////////////////

	// //////////////////////// Patients ////////////////////////////////

	public void deletePatients(int pid) {
		openDataBase();
		int i = myDataBase.delete("patient_medplan", "patient_id=" + pid, null);
		Log.i("TAG", "-------------" + i);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}
	
	public int getUserCountPic(int picid)
	{
		openDataBase();
		int count =0;
		Cursor c = myDataBase.rawQuery("select * from patient_medplan where patient_pic_id="+picid, null);
		if (c.getCount() > 0) {
			count = c.getCount();
		}
		
		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return count;
	}

	public void updatePatients(int userid, int pid, int picid, int phyid,
			String name, String surname, String address, String zipcode,
			String country, String state, String city, String gender,
			String tel, String mob, String mail, String intolerence, int btype,
			String phynm, String note1, String note2) {
		openDataBase();
		ContentValues initialValues = new ContentValues();

		initialValues.put("patient_user_id", userid);
		initialValues.put("patient_pic_id", picid);
		initialValues.put("patient_phy_id", phyid);
		initialValues.put("patient_name", name);
		initialValues.put("patient_surname", surname);
		initialValues.put("patient_address", address);
		initialValues.put("patient_zip_code", zipcode);
		initialValues.put("patient_country_code", country);
		initialValues.put("patient_city", city);
		initialValues.put("patient_mobile", mob);
		initialValues.put("patient_tel", tel);
		initialValues.put("patient_mail", mail);
		initialValues.put("patient_sex", gender);
		initialValues.put("patient_intolerance", intolerence);
		initialValues.put("patient_blood_type", btype);
		initialValues.put("patient_physician", phynm);
		initialValues.put("patient_note_one", note1);
		initialValues.put("patient_note_two", note2);
		initialValues.put("patient_state", state);

		myDataBase.update("patient_medplan", initialValues,
				"patient_id=" + pid, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}

	public void updatePhyPatients(int phyid) {
		openDataBase();
		ContentValues initialValues = new ContentValues();
		initialValues.put("patient_phy_id", phyid);
		initialValues.put("patient_physician", "");
		myDataBase.update("patient_medplan", initialValues,
				"patient_phy_id=" + phyid, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}
	
	public int getUserCountPhy(int phid)
	{
		openDataBase();
		int count =0;
		Cursor c = myDataBase.rawQuery("select * from patient_medplan where patient_phy_id="+phid, null);
		if (c.getCount() > 0) {
			count = c.getCount();
		}
		
		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return count;
	}
	
	public void insertPatients(int userid, int picid, int phyid, String name,
			String surname, String address, String zipcode, String country,
			String state, String city, String gender, String tel, String mob,
			String mail, String intolerence, int btype, String phynm,
			String note1, String note2) {
//
		openDataBase();
		ContentValues initialValues = new ContentValues();

		initialValues.put("patient_user_id", userid);
		initialValues.put("patient_pic_id", picid);
		initialValues.put("patient_phy_id", phyid);
		initialValues.put("patient_name", name);
		initialValues.put("patient_surname", surname);
		initialValues.put("patient_address", address);
		initialValues.put("patient_zip_code", zipcode);
		initialValues.put("patient_country_code", country);
		initialValues.put("patient_state", state);
		initialValues.put("patient_city", city);
		initialValues.put("patient_mobile", mob);
		initialValues.put("patient_tel", tel);
		initialValues.put("patient_mail", mail);
		initialValues.put("patient_sex", gender);
		initialValues.put("patient_intolerance", intolerence);
		initialValues.put("patient_blood_type", btype);
		initialValues.put("patient_physician", phynm);
		initialValues.put("patient_note_one", note1);
		initialValues.put("patient_note_two", note2);

		System.out.println("state values in db~~~~~~~" + state);

		long l = myDataBase.insert("patient_medplan", null, initialValues);
		Log.i("--", "long-------" + l);
//		myDataBase.close();

	}

	public boolean alreadyAdded(int userid) {
		boolean flg = false;
		openDataBase();
		Cursor c = myDataBase
				.rawQuery(
						"select * from patient_medplan where patient_user_id="
								+ userid, null);
		if (c.getCount() > 0) {
			flg = true;
		}
		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return flg;
	}
	
	public boolean isUserExsist(int userId){
		boolean flag = false ;
		
		openDataBase();
		Cursor c = myDataBase.rawQuery("select * from patient_medplan where patient_id="
				+ userId, null) ;
		if (c.getCount() > 0) {
			flag = true;
		}
		c.close();
		return flag; 
	}

	public ArrayList<User_Model> getPatients(int id) {
		// CREATE TABLE "notifications" ("id" INTEGER PRIMARY KEY AUTOINCREMENT
		// NOT NULL , "status" TEXT, "schedule" TEXT)
 
		ArrayList<User_Model> User_List = new ArrayList<User_Model>();
 
		openDataBase();
		Cursor c = myDataBase.rawQuery(
				"select * from patient_medplan where patient_user_id=" + id,
				null);
		Log.i("--", "------------" + c.getCount());
		if (c.getCount() > 0) {
			c.moveToFirst();

			User_Model User_Item;
			do {
				User_Item = new User_Model();
				User_Item.uid = c.getInt(0);
				User_Item.userid = c.getInt(1);
				User_Item.picid = c.getInt(2);
				User_Item.phyid = c.getInt(3);
				User_Item.name = c.getString(4);
				User_Item.surname = c.getString(5);
				User_Item.address = c.getString(6);
				User_Item.city = c.getString(7);
				User_Item.zip = c.getString(8);
				User_Item.country = c.getString(9);
				User_Item.mob = c.getString(10);
				User_Item.tel = c.getString(11);
				User_Item.mail = c.getString(12);
				User_Item.gender = c.getString(13);
				User_Item.tole = c.getString(14);
				User_Item.btype = c.getString(15);
				User_Item.phynm = c.getString(16);
				User_Item.note1 = c.getString(17);
				User_Item.note2 = c.getString(18);
				User_Item.state = c.getString(19);

				User_Item.all =c.getString(4) + " " + c.getString(5) + " "
						+ c.getString(6) + " " + c.getString(7) + " "
						+ c.getString(8) + " " + c.getString(9) + " "
						+ c.getString(10) + " " + c.getString(11) + " "
						+ c.getString(12)  + " "
						+ c.getString(16) + " " + c.getString(17) + " "
						+ c.getString(18) + " " + c.getString(19);

				User_List.add(User_Item);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return User_List;

	}

	public ArrayList<User_Model> getSinglePatientForId(int id) {
		// CREATE TABLE "notifications" ("id" INTEGER PRIMARY KEY AUTOINCREMENT
		// NOT NULL , "status" TEXT, "schedule" TEXT)

		ArrayList<User_Model> User_List = new ArrayList<User_Model>();

		openDataBase();
		Cursor c = myDataBase.rawQuery(
				"select * from patient_medplan where patient_id=" + id, null);
		Log.i("--", "------------" + c.getCount());
		if (c.getCount() > 0) {
			c.moveToFirst();

			User_Model User_Item;
			do {
				User_Item = new User_Model();
				User_Item.userid = c.getInt(1);
				User_Item.uid = c.getInt(0);
				User_Item.picid = c.getInt(2);
				User_Item.phyid = c.getInt(3);
				User_Item.name = c.getString(4);
				User_Item.surname = c.getString(5);
				User_Item.address = c.getString(6);
				User_Item.city = c.getString(7);
				User_Item.zip = c.getString(8);
				User_Item.country = c.getString(9);
				User_Item.mob = c.getString(10);
				User_Item.tel = c.getString(11);
				User_Item.mail = c.getString(12);
				User_Item.gender = c.getString(13);
				User_Item.tole = c.getString(14);
				User_Item.btype = c.getString(15);
				User_Item.phynm = c.getString(16);
				User_Item.note1 = c.getString(17);
				User_Item.note2 = c.getString(18);
				User_Item.state = c.getString(19);

				User_Item.all = c.getString(4) + " " + c.getString(5) + " "
						+ c.getString(6) + " " + c.getString(7) + " "
						+ c.getString(8) + " " + c.getString(9) + " "
						+ c.getString(10) + " " + c.getString(11) + " "
						+ c.getString(12) + " "
						+ c.getString(16) + " " + c.getString(17) + " "
						+ c.getString(18) + " " + c.getString(19);

				User_List.add(User_Item);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return User_List;

	}

	public User_Model getPatients(String userId) {
		// CREATE TABLE "notifications" ("id" INTEGER PRIMARY KEY AUTOINCREMENT
		// NOT NULL , "status" TEXT, "schedule" TEXT)

//		ArrayList<User_Model> User_List = new ArrayList<User_Model>();
		
			openDataBase();
			User_Model User_Item = null;
		Cursor c = myDataBase.rawQuery("select * from patient_medplan where patient_id='"+userId+"'", null);
		Log.i("--", "------------" + c.getCount());
		if (c.getCount() > 0) {
			c.moveToFirst();

		
			do {
				User_Item = new User_Model();
				User_Item.userid = c.getInt(1);
				User_Item.uid = c.getInt(0);
				User_Item.picid = c.getInt(2);
				User_Item.phyid = c.getInt(3);
				User_Item.name = c.getString(4);
				User_Item.surname = c.getString(5);
				User_Item.address = c.getString(6);
				User_Item.city = c.getString(7);
				User_Item.zip = c.getString(8);
				User_Item.country = c.getString(9);
				User_Item.mob = c.getString(10);
				User_Item.tel = c.getString(11);
				User_Item.mail = c.getString(12);
				User_Item.gender = c.getString(13);
				User_Item.tole = c.getString(14);
				User_Item.btype = c.getString(15);
				User_Item.phynm = c.getString(16);
				User_Item.note1 = c.getString(17);
				User_Item.note2 = c.getString(18);

				User_Item.all = c.getString(4) + " " + c.getString(5) + " "
						+ c.getString(6) + " " + c.getString(7) + " "
						+ c.getString(8) + " " + c.getString(9) + " "
						+ c.getString(10) + " " + c.getString(11) + " "
						+ c.getString(12) + " " + c.getString(13) + " "
						+ c.getString(16) + " " + c.getString(17) + " "
						+ c.getString(18);

//				User_List.add(User_Item);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return User_Item;

	}

	// //////////////////////// Patients ////////////////////////////////

	// //////////////////////// Picture ////////////////////////////////

	public void deletePicture(int id) {
		openDataBase();
		myDataBase.delete("picture_master_medplan", "pic_id=" + id, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}

	public void updatePicture(int userid,int id, String path, String desc,
			int category, String note1, String note2) {
		openDataBase();
		ContentValues initialValues = new ContentValues();
 
		initialValues.put("pic_user_id", userid);
		initialValues.put("pic_path", path);
		initialValues.put("pic_desc", desc);
		initialValues.put("pic_category", category);
		initialValues.put("pic_note_one", note1);
		initialValues.put("pic_note_two", note2);
		myDataBase.update("picture_master_medplan", initialValues, "pic_id="
				+ id , null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}
 
	public void insertPicture(int userid, String path, String desc,
			int category, String note1, String note2) {

		openDataBase();
		ContentValues initialValues = new ContentValues();
		initialValues.put("pic_user_id", userid);
		initialValues.put("pic_path", path);
		initialValues.put("pic_desc", desc);
		initialValues.put("pic_category", category);
		initialValues.put("pic_note_one", note1);
		initialValues.put("pic_note_two", note2);

		long l = myDataBase.insert("picture_master_medplan", null,
				initialValues);
		Log.i("", "insert ========== " + l);
//		myDataBase.close();
		
	}

	public Picture_Model getPicture(int id) {
		
			openDataBase();

		Cursor c = myDataBase
				.rawQuery("select * from picture_master_medplan where pic_id="
						+ id, null);
		Picture_Model Pic_Item = new Picture_Model();
		if (c.getCount() > 0) {
			c.moveToFirst();

			Pic_Item.id = c.getInt(0);
			Pic_Item.path = c.getString(1);
			Pic_Item.category = c.getString(2);
			Pic_Item.desc = c.getString(3);
			Pic_Item.note1 = c.getString(4);
			Pic_Item.note2 = c.getString(5);
		}
		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return Pic_Item;
	}
	
	public int getPictureID(String path)
	{
		int pid=0;
				openDataBase();
		Cursor c = myDataBase
				.rawQuery("select * from picture_master_medplan where pic_path='"
						+ path+"'", null);
		Picture_Model Pic_Item = new Picture_Model();
		if (c.getCount() > 0) {
			c.moveToFirst();

			pid = c.getInt(0);
			
		}
		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return pid;
	}

	public ArrayList<Picture_Model> getPictures(int userid) {
		// CREATE TABLE "notifications" ("id" INTEGER PRIMARY KEY AUTOINCREMENT
		// NOT NULL , "status" TEXT, "schedule" TEXT)
		// comment chhe
		ArrayList<Picture_Model> Pic_List = new ArrayList<Picture_Model>();

		openDataBase();
		Cursor c = myDataBase.rawQuery(
				"select * from picture_master_medplan where pic_user_id="
						+ userid, null);
		if (c.getCount() > 0) {
			c.moveToFirst();

			Picture_Model Pic_Item;
			do {
				Pic_Item = new Picture_Model();

				Pic_Item.id = c.getInt(0);
				Pic_Item.path = c.getString(1);
				Pic_Item.category = c.getString(2);
				Pic_Item.desc = c.getString(3);
				Pic_Item.note1 = c.getString(4);
				Pic_Item.note2 = c.getString(5);
				Pic_Item.all = Pic_Item.id + " " + Pic_Item.path + " " +

				Pic_Item.desc + " " + Pic_Item.note1 + " " + Pic_Item.note2;

				if (Pic_Item.category.equalsIgnoreCase("1")) {
					Pic_Item.all = Pic_Item.all + " People";
				} else {
					Pic_Item.all = Pic_Item.all + " Medicines";
				}
				Pic_List.add(Pic_Item);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();	
//		SQLiteDatabase.releaseMemory();
		return Pic_List;

	}

	// //////////////////////// Picture ////////////////////////////////

	// //////////////////////// Mobile ////////////////////////////////

	public void deleteContact(int id) {
//		openDataBase();
		myDataBase.delete("emergency_medplann", "emer_id=" + id, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}

	public void updateContact(int id, int uid, int picid, String fnm,
			String lnm, int relation, String tel, String mob, int icon,
			int mti, int mcount, String email, String wtext, String mailcount,
			String mailinterval) {
		openDataBase();
		ContentValues initialValues = new ContentValues();

		initialValues.put("emer_user_id", uid);
		initialValues.put("emer_pic_id", picid);
		initialValues.put("emer_name", fnm);
		initialValues.put("emer_surname", lnm);
		initialValues.put("emer_relation", relation);
		initialValues.put("emer_telephone", tel);
		initialValues.put("emer_mobile", mob);
		initialValues.put("emer_icon", icon);
		initialValues.put("emer_time_interval", mti);
		initialValues.put("emer_call_count", mcount);
		initialValues.put("emer_mail", email);
		initialValues.put("emer_warning_msg", wtext);
		initialValues.put("emer_mail_count", mailcount);
		initialValues.put("emer_mail_interval", mailinterval);

		myDataBase.update("emergency_medplann", initialValues, "emer_id=" + id,
				null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}

	public void insertContact(int uid, int picid, String fnm, String lnm,
			int relation, String tel, String mob, int icon, int mti,
			int mcount, String email, String wtext, String mailcount,
			String mailinterval) {

		openDataBase();
		ContentValues initialValues = new ContentValues();
		initialValues.put("emer_user_id", uid);
		initialValues.put("emer_pic_id", picid);
		initialValues.put("emer_name", fnm);
		initialValues.put("emer_surname", lnm);
		initialValues.put("emer_relation", relation);
		initialValues.put("emer_telephone", tel);
		initialValues.put("emer_mobile", mob);
		initialValues.put("emer_icon", icon);
		initialValues.put("emer_time_interval", mti);
		initialValues.put("emer_call_count", mcount);
		initialValues.put("emer_mail", email);
		initialValues.put("emer_warning_msg", wtext);
		initialValues.put("emer_mail_count", mailcount);
		initialValues.put("emer_mail_interval", mailinterval);

		myDataBase.insert("emergency_medplann", null, initialValues);
//		myDataBase.close();

	}

	public ArrayList<Contact_Model> getContact(int id) {
		ArrayList<Contact_Model> Con_List = new ArrayList<Contact_Model>();
		openDataBase();
		Cursor c = myDataBase.rawQuery(
				"select * from emergency_medplann where emer_id=" + id, null);
		Contact_Model Con_Item = new Contact_Model();
		if (c.getCount() > 0) {
			c.moveToFirst();
			Con_Item.id = c.getInt(0);
			Con_Item.uid = c.getInt(1);
			Con_Item.picid = c.getInt(2);
			Con_Item.fnm = c.getString(3);
			Con_Item.lnm = c.getString(4);
			Con_Item.relation = c.getInt(5);
			Con_Item.tel = c.getString(6);
			Con_Item.mob = c.getString(7);
			Con_Item.icon = c.getInt(8);
			Con_Item.mti = c.getInt(9);
			Con_Item.mcount = c.getInt(10);
			Con_Item.email = c.getString(11);
			Con_Item.wtext = c.getString(12);
			Con_Item.mailcount = c.getString(13);
			Con_Item.mailinterval = c.getString(14);
		}
		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		Con_List.add(Con_Item);
		return Con_List;
	}

	public ArrayList<Contact_Model> getContacts(int userid) {
		ArrayList<Contact_Model> Con_List = new ArrayList<Contact_Model>();

		openDataBase();
		Cursor c = myDataBase
				.rawQuery(
						"select * from emergency_medplann where emer_user_id="
								+ userid, null);
		if (c.getCount() > 0) {
			c.moveToFirst();

			Contact_Model Con_Item;
			do {
				Con_Item = new Contact_Model();

				Con_Item.id = c.getInt(0);
				Con_Item.uid = c.getInt(1);
				Con_Item.picid = c.getInt(2);
				Con_Item.fnm = c.getString(3);
				Con_Item.lnm = c.getString(4);
				Con_Item.relation = c.getInt(5);
				Con_Item.tel = c.getString(6);
				Con_Item.mob = c.getString(7);
				Con_Item.icon = c.getInt(8);
				Con_Item.mti = c.getInt(9);
				Con_Item.mcount = c.getInt(10);
				Con_Item.email = c.getString(11);
				Con_Item.wtext = c.getString(12);
				Con_Item.mailcount = c.getString(13);
				Con_Item.mailinterval = c.getString(14);

				Con_Item.all = Con_Item.fnm + " "
						+ Con_Item.lnm + " " + Con_Item.relation + " "
						+ Con_Item.tel + " " + Con_Item.mob + " "
						+ Con_Item.email + " "
						+ Con_Item.wtext;

				Con_List.add(Con_Item);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return Con_List;

	}

	// //////////////////////// Mobile ////////////////////////////////
	// //////////////////////// Medical ////////////////////////////////

	public void deleteMedical(int id) {
		openDataBase();
		myDataBase.delete("medicine_medplann", "medicine_id=" + id, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}

	public int getMediCountPic(int picid)
	{
		openDataBase();
		int count =0;
		Cursor c = myDataBase.rawQuery("select * from medicine_medplann where medicine_pic_id="+picid, null);
		if (c.getCount() > 0) {
			count = c.getCount();
		}
		
		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return count;
	}
	
	public void updateMedical(int userid, int mid, int mpicid, String nm,
			String sdesc, String ldesc, String cat, String ingridiant,
			int dosage, int dosagetype, int dosagetime, String therapeutic,
			String mintolerance, String caution, int toxicity, String note1,
			String note2, int route) {
		openDataBase();
		ContentValues initialValues = new ContentValues();

		initialValues.put("medicine_pic_id", mpicid);
		initialValues.put("medicine_name", nm);
		initialValues.put("medicine_sdesc", sdesc);
		initialValues.put("medicine_ldesc", ldesc);
		initialValues.put("medicine_cat", cat);
		initialValues.put("medicine_ingredient", ingridiant);
		initialValues.put("medicine_dosage", dosage);
		initialValues.put("medicine_dosage_time", dosagetime);
		initialValues.put("medicine_dosage_unit", dosagetype);
		initialValues.put("mediciine_therap", therapeutic);
		initialValues.put("medicine_Intolerance", mintolerance);
		initialValues.put("medicine_caution", caution);
		initialValues.put("medicine_toxicity", toxicity);
		initialValues.put("medicine_note_one", note1);
		initialValues.put("medicine_note_two", note2);
		initialValues.put("medicine_user_id", userid);
		initialValues.put("medicine_route", route);

		myDataBase.update("medicine_medplann", initialValues, "medicine_id="
				+ mid, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}

	public void insertMedical(int userid, int mpicid, String nm, String sdesc,
			String ldesc, String cat, String ingridiant, int dosage,
			int dosagetype, int dosagetime, String therapeutic,
			String mintolerance, String caution, int toxicity, String note1,
			String note2, int route) {

		openDataBase();
		ContentValues initialValues = new ContentValues();
		initialValues.put("medicine_pic_id", mpicid);
		initialValues.put("medicine_name", nm);
		initialValues.put("medicine_sdesc", sdesc);
		initialValues.put("medicine_ldesc", ldesc);
		initialValues.put("medicine_cat", cat);
		initialValues.put("medicine_ingredient", ingridiant);
		initialValues.put("medicine_dosage", dosage);
		initialValues.put("medicine_dosage_time", dosagetime);
		initialValues.put("medicine_dosage_unit", dosagetype);
		initialValues.put("mediciine_therap", therapeutic);
		initialValues.put("medicine_Intolerance", mintolerance);
		initialValues.put("medicine_caution", caution);
		initialValues.put("medicine_toxicity", toxicity);
		initialValues.put("medicine_note_one", note1);
		initialValues.put("medicine_note_two", note2);
		initialValues.put("medicine_user_id", userid);
		initialValues.put("medicine_route", route);

		long l = myDataBase.insert("medicine_medplann", null, initialValues);
		Log.i("--------", "----------" + l);
//		myDataBase.close();

	}

	public ArrayList<Medicine_Model> getMedical(int id) {
		ArrayList<Medicine_Model> Med_List = new ArrayList<Medicine_Model>();

			openDataBase();

		Cursor c = myDataBase
				.rawQuery("select * from medicine_medplann where medicine_id="
						+ id, null);
		Medicine_Model Med_Item = new Medicine_Model();
		if (c.getCount() > 0) {
			c.moveToFirst();
			Med_Item.mid = c.getInt(0);
			Med_Item.mpicid = c.getInt(1);
			Med_Item.nm = c.getString(2);
			Med_Item.sdesc = c.getString(3);
			Med_Item.ldesc = c.getString(4);
			Med_Item.cat = c.getString(5);
			Med_Item.ingridiant = c.getString(6);
			Med_Item.dosage = c.getString(7);
			Med_Item.dosagetime = c.getString(8);
			Med_Item.dosageunit = c.getString(9);
			Med_Item.mtherap = c.getString(10);
			Med_Item.mintolerance = c.getString(11);
			Med_Item.caution = c.getString(12);
			Med_Item.toxicity = c.getString(13);
			Med_Item.note1 = c.getString(14);
			Med_Item.note2 = c.getString(15);
			Med_Item.muid = c.getInt(16);
			Med_Item.route = c.getInt(17);
			Med_List.add(Med_Item);
		}
		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

		return Med_List;
	}

	public ArrayList<Medicine_Model> getMedicals() {
		ArrayList<Medicine_Model> Med_List = new ArrayList<Medicine_Model>();

		openDataBase();
		Cursor c = myDataBase
				.rawQuery("select * from medicine_medplann ", null);
		if (c.getCount() > 0) {
			c.moveToFirst();

			Medicine_Model Med_Item;
			do {
				Med_Item = new Medicine_Model();

				Med_Item.mid = c.getInt(0);
				Med_Item.mpicid = c.getInt(1);
				Med_Item.nm = c.getString(2);
				Med_Item.sdesc = c.getString(3);
				Med_Item.ldesc = c.getString(4);
				Med_Item.cat = c.getString(5);
				Med_Item.ingridiant = c.getString(6);
				Med_Item.dosage = c.getString(7);
				Med_Item.dosagetime = c.getString(8);
				Med_Item.dosageunit = c.getString(9);
				Med_Item.mtherap = c.getString(10);
				Med_Item.mintolerance = c.getString(11);
				Med_Item.caution = c.getString(12);
				Med_Item.toxicity = c.getString(13);
				Med_Item.note1 = c.getString(14);
				Med_Item.note2 = c.getString(15);
				Med_Item.muid = c.getInt(16);
				Med_Item.route = c.getInt(17);

				Med_Item.all =Med_Item.nm + " " + Med_Item.sdesc + " "
						+ Med_Item.ldesc + " " + Med_Item.cat + " "
						+ Med_Item.ingridiant + " "
						+ Med_Item.mtherap + " " + Med_Item.mintolerance + " "
						+ Med_Item.caution + " " + Med_Item.toxicity + " "
						+ Med_Item.note1 + " " + Med_Item.note2 + " "
						+ Med_Item.route;

				Med_List.add(Med_Item);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return Med_List;

	}

	public ArrayList<Medicine_Model> getMedicals(int userid) {
		ArrayList<Medicine_Model> Med_List = new ArrayList<Medicine_Model>();

		openDataBase();
		Cursor c = myDataBase.rawQuery(
				"select * from medicine_medplann where medicine_user_id="
						+ userid, null);
		if (c.getCount() > 0) {
			c.moveToFirst();

			Medicine_Model Med_Item;
			do {
				Med_Item = new Medicine_Model();

				Med_Item.mid = c.getInt(0);
				Med_Item.mpicid = c.getInt(1);
				Med_Item.nm = c.getString(2);
				Med_Item.sdesc = c.getString(3);
				Med_Item.ldesc = c.getString(4);
				Med_Item.cat = c.getString(5);
				Med_Item.ingridiant = c.getString(6);
				Med_Item.dosage = c.getString(7);
				Med_Item.dosagetime = c.getString(8);
				Med_Item.dosageunit = c.getString(9);
				Med_Item.mtherap = c.getString(10);
				Med_Item.mintolerance = c.getString(11);
				Med_Item.caution = c.getString(12);
				Med_Item.toxicity = c.getString(13);
				Med_Item.note1 = c.getString(14);
				Med_Item.note2 = c.getString(15);
				Med_Item.muid = c.getInt(16);
				Med_Item.route = c.getInt(17);

				Med_Item.all =  Med_Item.nm + " " + Med_Item.sdesc + " "
						+ Med_Item.ldesc + " " + Med_Item.cat + " "
						+ Med_Item.ingridiant  + " "
						+ Med_Item.mtherap + " " + Med_Item.mintolerance + " "
						+ Med_Item.caution + " " + Med_Item.toxicity + " "
						+ Med_Item.note1 + " " + Med_Item.note2 + " "
						+ Med_Item.route;

				Med_List.add(Med_Item);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return Med_List;

	}

	// //////////////////////// Cell Management ////////////////////////////////
	public int getCellID() {
		return cellid;
	}

	public void ActivateCell(int loginid, int user_id, int cell_id) {
		openDataBase();
		ContentValues initialValues = new ContentValues();
		Log.d("~~~~~~~~~~~~", "activate cell");
		initialValues.put("LoginId", loginid);
		initialValues.put("UserId", user_id);
		initialValues.put("BoxId", cell_id);
 
//		String deleteSql = "delete from UserCell_Medplann where loginid="+loginid +" and userid="+user_id;
//		myDataBase.rawQuery(deleteSql, null);
		
		myDataBase.delete("UserCell_Medplann", "loginid="+loginid +" and userid="+user_id, null) ;
		myDataBase.insert("UserCell_Medplann", null, initialValues);
//		myDataBase.close();
	}

	public void ActivateCellUpdate(int loginid, int user_id, int cell_id) {
		openDataBase();
		ContentValues initialValues = new ContentValues();
		Log.d("~~~~~~~~~~~~", "activate cell");
		initialValues.put("LoginId", loginid);
		initialValues.put("UserId", user_id);
		initialValues.put("BoxId", cell_id);

		myDataBase.update("UserCell_Medplann", initialValues, "LoginId="
				+ loginid + " and UserId=" + user_id, null);
//		myDataBase.close();
	}

	
	public int isActivated(int loginid, int userid) {
		openDataBase();
		int boxid = 0;
		Cursor c = myDataBase.rawQuery(
				"select * from UserCell_Medplann where LoginId=" + loginid
						+ " and UserId=" + userid, null);
		if (c.getCount() > 0) {
			c.moveToLast();
			boxid = c.getInt(2);
			// Constant._Cell_id=c.getInt(1);
			Log.d("", "~~~~~~~!!!!!!!!!!~~~~~~~" + boxid);
		}
		c.close();
//		myDataBase.close();
		return boxid;
//		SQLiteDatabase.releaseMemory();
	}

	 
	public boolean NullCell (int loginId, int userId , int boxId){
		Cursor c = myDataBase.rawQuery(
				"select * from UserCell_Medplann where LoginId=" + loginId
						+ " and UserId=" + userId+" and BoxId="+boxId, null);
		
//		if (c.getCount() > 0) {
//			c.getString(c.getColumnIndex("BoxId"));
//		} 
		 
		return c.getCount()==0?false:true ;
	}
	
	public int getCellCountUser(int userid)
	{
		openDataBase();
		int count =0;
		Cursor c = myDataBase.rawQuery("select * from UserCell_Medplann where UserId="+userid, null);
		if (c.getCount() > 0) {
			count = c.getCount();
		}
		
		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return count;
	}
	public void deleteUserCell(int userid) {
		openDataBase();
		myDataBase.delete("UserCell_Medplann", "UserId=" + userid, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}

	public int getCell_id(int loginid,int userid) {
		openDataBase();
		Cursor c = myDataBase.rawQuery("select * from UserCell_Medplann where LoginId="+loginid+" and UserId="+userid, null);
		if (c.getCount() > 0) {
			c.moveToLast();
			cellid = c.getInt(c.getColumnIndex("BoxId"));
			Log.d("~~~~~~~!!!!!ziya!!!!!~~~~~~~", "" + cellid);
		}
		else
		{ 
			cellid = -1;
		}
		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return cellid;
	}

	// //////////////////////////////Cell Addshow
	// management////////////////////////////////////////

	public int getCellCounts(int loginid, int userid,
			int boxid) {
		int count =0;
		openDataBase();
		Cursor c = null;
		c = myDataBase.rawQuery(
					"select * from form_medplann where form_user_id=" + userid
							+ " and form_box_id=" + boxid
							+ " and form_login_id=" + loginid, null);	

		if (c.getCount() > 0) {
			count = c.getCount();
		}
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return count;
	}
	public ArrayList<CellInfo_Model> getCellInfoForBox(int loginid, int userid,
			int boxid, int cellid) {
		ArrayList<CellInfo_Model> CellInfo_List = new ArrayList<CellInfo_Model>();

			openDataBase();

		Cursor c = null;
		if (cellid == -1) {
			c = myDataBase.rawQuery(
					"select * from form_medplann where form_user_id=" + userid
							+ " and form_box_id=" + boxid
							+ " and form_login_id=" + loginid, null);
		} else {
			c = myDataBase.rawQuery(
					"select * from form_medplann where form_user_id=" + userid
							+ " and form_box_id=" + boxid
							+ " and form_login_id=" + loginid
							+ " and form_cell_id=" + cellid, null);
		}

		if (c.getCount() > 0) {
			c.moveToFirst();

			CellInfo_Model Cellinfo;
			do {
				Cellinfo = new CellInfo_Model();

				Cellinfo.userid = c.getInt(1);
				Cellinfo.boxid = c.getInt(2);
				Cellinfo.cellid = c.getInt(3);
				Cellinfo.medid = c.getInt(4);
				Cellinfo.picid = c.getInt(5);
				Cellinfo.strDesc = c.getString(6);
				Cellinfo.intBg = c.getInt(7);
				Cellinfo.blMini = c.getInt(8);
				Cellinfo.blBlink = c.getInt(9);
				Cellinfo.intSound = c.getInt(10);
				Cellinfo.intBuzz = c.getInt(11);
				Cellinfo.blVibrant = c.getInt(12);

				Cellinfo.blAlert = c.getInt(13);
				Cellinfo.intAlarm = c.getInt(14);
				Cellinfo.intWay = c.getInt(15);
				Cellinfo.intConfirm = c.getInt(16);
				Cellinfo.intDayOf_Int = c.getInt(17);
				Cellinfo.intDos_Mgt = c.getInt(18);
				Cellinfo.intMany_Time = c.getInt(19);
				Cellinfo.intSch_Int = c.getInt(20);
				Cellinfo.intInt_Day = c.getInt(21);
				Cellinfo.intWeek = c.getInt(22);
				Cellinfo.intMonth = c.getInt(23);
				Cellinfo.loginid = c.getInt(26);
				Cellinfo.weekdaystring = c.getString(27);
				Cellinfo.startdate = c.getString(28);
				CellInfo_List.add(Cellinfo);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return CellInfo_List;
	}
	public void deleteUserForm(int userid) {
		openDataBase();
		myDataBase.delete("form_medplann", "form_user_id=" + userid, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}
	
	public void insertForm(int form_user_id, int box_layout, int Cell,
			int med_id, int imgpath, String desc, int Bcolor, int alert,
			int blink, int sound, int buzz, int mini, int vibrant,
			int stopAlarm, int wayAlarm, int medConfirm, int intDay,
			int Dosage, int manyTime, int schedule, int dayInt, int week,
			int month, int loginid,String weekdaystring,String startdate) {

	
			openDataBase();

		ContentValues initialValues = new ContentValues();
		initialValues.put("form_user_id", form_user_id);
		initialValues.put("form_box_id", box_layout);
		initialValues.put("form_cell_id", Cell);
		initialValues.put("form_med_id", med_id);
		initialValues.put("form_pic_id", imgpath);
		initialValues.put("form_med_desc", desc);
		initialValues.put("form_cell_bg", Bcolor);
		initialValues.put("form_mini_med_img", mini);
		initialValues.put("form_blinking", blink);
		initialValues.put("form_buzz_sound", sound);
		initialValues.put("form_buzz_repeat", buzz);
		initialValues.put("form_vibrant", vibrant);
		initialValues.put("form_med_desc_red", alert);
		initialValues.put("form_stop_alarm", stopAlarm);
		initialValues.put("form_way_stop_alarm", wayAlarm);
		initialValues.put("form_confirm_med", medConfirm);
		initialValues.put("form_interval_day", intDay);
		initialValues.put("form_dosage_mes", Dosage);
		initialValues.put("form_med_each_time", manyTime);
		initialValues.put("form_schedule_interval", schedule);
		initialValues.put("form_which_day_interval", dayInt);
		initialValues.put("form_which_week_interval", week);
		initialValues.put("form_which_month_interval", month);
		initialValues.put("form_login_id", loginid);
		initialValues.put("form_week_day", weekdaystring);
		initialValues.put("form_start_date", startdate);

		myDataBase.insert("form_medplann", null, initialValues);
//		myDataBase.close();

	}

	public void updateForm(int form_user_id, int box_layout, int Cell,
			int med_id, int imgpath, String desc, int Bcolor, int alert,
			int blink, int sound, int buzz, int mini, int vibrant,
			int stopAlarm, int wayAlarm, int medConfirm, int intDay,
			int Dosage, int manyTime, int schedule, int dayInt, int week,
			int month, int loginid,String weekdaystring,String startdate) {

		openDataBase();
		ContentValues initialValues = new ContentValues();
		initialValues.put("form_user_id", form_user_id);
		initialValues.put("form_box_id", box_layout);
		initialValues.put("form_cell_id", Cell);
		initialValues.put("form_med_id", med_id);
		initialValues.put("form_pic_id", imgpath);
		initialValues.put("form_med_desc", desc);
		initialValues.put("form_cell_bg", Bcolor);
		initialValues.put("form_mini_med_img", mini);
		initialValues.put("form_blinking", blink);
		initialValues.put("form_buzz_sound", sound);
		initialValues.put("form_buzz_repeat", buzz);
		initialValues.put("form_vibrant", vibrant);
		initialValues.put("form_med_desc_red", alert);
		initialValues.put("form_stop_alarm", stopAlarm);
		initialValues.put("form_way_stop_alarm", wayAlarm);
		initialValues.put("form_confirm_med", medConfirm);
		initialValues.put("form_interval_day", intDay);
		initialValues.put("form_dosage_mes", Dosage);
		initialValues.put("form_med_each_time", manyTime);
		initialValues.put("form_schedule_interval", schedule);
		initialValues.put("form_which_day_interval", dayInt);
		initialValues.put("form_which_week_interval", week);
		initialValues.put("form_which_month_interval", month);
		initialValues.put("form_login_id", loginid);
		initialValues.put("form_week_day", weekdaystring);
		initialValues.put("form_start_date", startdate);

		myDataBase.update("form_medplann", initialValues, "form_user_id="
				+ form_user_id + " and form_box_id=" + box_layout +" and form_cell_id="+Cell
				+ " and form_login_id=" + loginid, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}

	public void deleteForm(int userid, int boxid, int cellid, int loginid) {

			openDataBase();

		myDataBase.delete("form_medplann", "form_user_id=" + userid
				+ " and form_box_id=" + boxid + " and form_login_id=" + loginid
				+ " and form_cell_id=" + cellid, null);
		
		myDataBase.delete("notification_medplann", "userid=" + userid
				+ " and boxid=" + boxid + " and loginid=" + loginid
				+ " and cellid=" + cellid, null) ;
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}
	
	public int getFormCountMedi(int mid)
	{
		openDataBase();
		int count =0;
		Cursor c = myDataBase.rawQuery("select * from form_medplann where form_med_id="+mid, null);
		if (c.getCount() > 0) {
			count = c.getCount();
		}
		
		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return count;
	}
	
	public void deleteMediForm(int medid) {
		openDataBase();
		myDataBase.delete("form_medplann", "form_med_id=" +medid, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}
	
	public void deleteForm(int userid, int boxid,int loginid) {
		openDataBase();
		myDataBase.delete("form_medplann", "form_user_id=" + userid
				+ " and form_box_id=" + boxid + " and form_login_id=" + loginid
				, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}

	// /////////////////////////////////// notification

	public ArrayList<Integer> getDeadIds(int userid, int boxid, int cellid,
			int loginid)
	{
		openDataBase();
		ArrayList<Integer> arr = new ArrayList<Integer>();
		Cursor c = null;
		
		String query  = "select pid from notification_medplann where userid=" + userid
						+ " and boxid=" + boxid + " and loginid=" + loginid
						+ " and cellid=" + cellid 
//						+" and isAlive="+0  
						; 
		Log.d("Query", query) ;  
		c = myDataBase.rawQuery(query, null);

		if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				arr.add(c.getInt(0));
			} while (c.moveToNext());
		} 
		
		myDataBase.delete("notification_medplann", "userid=" + userid
				+ " and boxid=" + boxid + " and loginid=" + loginid
				+ " and cellid=" + cellid, null) ;
		
//		myDataBase.close();
		return arr;
	}
/*	public void updateNotifcationDead(int userid, int boxid, int cellid,
			int loginid) {

			openDataBase();

				ContentValues initialValues = new ContentValues();
				initialValues.put("userid", userid);
				initialValues.put("boxid", boxid);
				initialValues.put("cellid", cellid);
				initialValues.put("timeset", "");
				initialValues.put("loginid", loginid);
				initialValues.put("isAlive",0);
				myDataBase.update("notification_medplann", initialValues, "userid=" + userid
								+ " and boxid=" + boxid + " and loginid=" + loginid
								+ " and cellid=" + cellid, null);
//				myDataBase.close();
//				SQLiteDatabase.releaseMemory();
			}
*/	
	public void updateNotifcation(int userid, int boxid, int cellid,
	int loginid) {

		openDataBase();
		ContentValues initialValues = new ContentValues();
		initialValues.put("userid", userid);
		initialValues.put("boxid", boxid);
		initialValues.put("cellid", cellid);
		initialValues.put("timeset", "");
		initialValues.put("loginid", loginid);

		myDataBase.update("notification_medplann", initialValues, "userid=" + userid
						+ " and boxid=" + boxid + " and loginid=" + loginid
						+ " and cellid=" + cellid, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
	}

	public ArrayList<Notification_Model> getCellNotification(int loginid,
			int userid, int boxid, int cellid) {
		ArrayList<Notification_Model> CellInfo_List = new ArrayList<Notification_Model>();


			openDataBase();

		Cursor c = null;
		c = myDataBase.rawQuery(
				"select * from notification_medplann where userid=" + userid
						+ " and boxid=" + boxid + " and loginid=" + loginid
						+ " and cellid=" + cellid +" and isAlive="+1 , null);

		if (c.getCount() > 0) {
			c.moveToFirst();
			Notification_Model Cellinfo;
			do {
				Cellinfo = new Notification_Model();
				Cellinfo.nid = c.getInt(0);
				Cellinfo.userid = c.getInt(2);
				Cellinfo.boxid = c.getInt(3);
				Cellinfo.cellid = c.getInt(4);
				Cellinfo.loginid = c.getInt(1);
				Cellinfo.strTime = c.getString(5);
				if(!(c.isNull(c.getColumnIndex("nextday"))))
                Cellinfo.nextDay = c.getString(c.getColumnIndex("nextday")) ;
                 
				CellInfo_List.add(Cellinfo);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return CellInfo_List;
	}
	
	public void updateNotificationId (int userid, int boxid, int cellid,
			int loginid , int notificationId){
	
		openDataBase();
		ContentValues initialValues = new ContentValues();
		initialValues.put("notificationid", notificationId);
		
		myDataBase.update("notification_medplann", initialValues, "userid=" + userid
				+ " and boxid=" + boxid + " and loginid=" + loginid
				+ " and cellid=" + cellid, null);
		
	}
	
	public ArrayList<Notification_Model> getCellNotification() {
		ArrayList<Notification_Model> CellInfo_List = new ArrayList<Notification_Model>();

			openDataBase();	

		Cursor c = null;
		c = myDataBase.rawQuery(
				"select * from notification_medplann"
						, null);

		if (c.getCount() > 0) {
			c.moveToFirst();

			Notification_Model Cellinfo;
			do {
				Cellinfo = new Notification_Model();
				Cellinfo.nid = c.getInt(0);
				Cellinfo.userid = c.getInt(2);
				Cellinfo.boxid = c.getInt(3);
				Cellinfo.cellid = c.getInt(4);
				Cellinfo.loginid = c.getInt(1);
				Cellinfo.strTime = c.getString(5);

				CellInfo_List.add(Cellinfo);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return CellInfo_List;
	}
	public void deleteUserNotification(int userid) {
		openDataBase();
		myDataBase.delete("notification_medplann", "userid=" + userid, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}
	public void deleteNotification(int userid, int boxid, int cellid,
			
			int loginid) {
		//http://www.facebook.com/hotveryspicy
		openDataBase();
		myDataBase.delete("notification_medplann", "userid=" + userid
				+ " and boxid=" + boxid + " and loginid=" + loginid
				+ " and cellid=" + cellid, null);
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();

	}

	public void insertNotification(int userid, int boxid, int cellid,
			int loginid, String strTime,int pid , String interval ,String nextDay, String date ,int wayToStop, int sound , String description , String medicine  ) {
 
			openDataBase();

		ContentValues initialValues = new ContentValues();
		initialValues.put("userid", userid);
		initialValues.put("boxid", boxid);
		initialValues.put("cellid", cellid);
		initialValues.put("timeset", strTime);
		initialValues.put("loginid", loginid);
		initialValues.put("pid",pid);
		initialValues.put("isAlive",1);
		initialValues.put("interval",interval) ;
		initialValues.put("nextday", nextDay) ;
		initialValues.put("date", date);
		initialValues.put("waytostop",wayToStop ) ;
		initialValues.put("sound", sound); 
		initialValues.put("description", description);
		initialValues.put("med", medicine); 
		
 		myDataBase.insert("notification_medplann", null, initialValues);
 		
 		Log.d("**********  Unique value ************", ""+pid); 
 		
//		myDataBase.close();

	}
	
	
	/**
	 * This method will check the alarm for same time .
	 */
	private boolean  checkSameTimeReport (int loginId, int userID , String pateintName,String medName,
			String date, String time, String dosageTaken,int qty ){
		openDataBase() ;
		
		Cursor cursor = myDataBase.rawQuery("select * from notification_medplann where loginid="+loginId+" and userid="+userID+" and ", null) ;
		return false ; 
	}


	
	/////////////////////////////////Report Medplann////////////////////////////
	
	public void insertReport(int loginid, String pateintName,String medName,
			String date, String time, String dosageTaken,int qty) {

		openDataBase();
		ContentValues initialValues = new ContentValues();
		initialValues.put("report_login_id", loginid);
		initialValues.put("report_patient_name", pateintName);
		initialValues.put("report_med_name", medName);
		initialValues.put("report_date", date);
		initialValues.put("report_time", time);
		initialValues.put("report_dosage_taken", dosageTaken);
		initialValues.put("report_qty", qty);
		myDataBase.insert("report_medplann", null, initialValues);
//		myDataBase.close();

	}
	
	public int  getLastNotificationId (){
		int lastIndex =0  ;
		Cursor cursor ; 
		 cursor= myDataBase.rawQuery(
				"select * from notification_medplann", null);
		 
		 if (cursor.getCount()!=0){
			 cursor.moveToLast() ;
		 lastIndex = cursor.getInt(cursor.getColumnIndex("notificationid")) ;
		 }

		  
		return  lastIndex ;
	}
	public ArrayList<Report_Model> getReportInfo(int loginid) {
		ArrayList<Report_Model> ReportInfo_List = new ArrayList<Report_Model>();

		openDataBase();
		Cursor c = null;
		c = myDataBase.rawQuery(
				"select * from report_medplann where report_login_id=" + loginid, null);

		if (c.getCount() > 0) {
			c.moveToFirst();

			Report_Model Reportinfo;
			do {
				Reportinfo = new Report_Model();
				Reportinfo.report_loginid = c.getInt(0);
				Reportinfo.report_patient_name=c.getString(1);
				Reportinfo.report_med_name=c.getString(2);
				Reportinfo.report_date=c.getString(3);
				Reportinfo.report_time=c.getString(4);
				Reportinfo.report_dos_taken=c.getString(5);
				Reportinfo.report_qty=c.getInt(6);
				Reportinfo.report_id=c.getInt(7);
				ReportInfo_List.add(Reportinfo);
			} while (c.moveToNext());
		}

		c.close();
//		myDataBase.close();
//		SQLiteDatabase.releaseMemory();
		return ReportInfo_List;
	}						
	public void deleteReport(int rid) {
		//http://www.facebook.com/hotveryspicy
		openDataBase();
		myDataBase.delete("report_medplann", "report_id=" + rid, null);
//		myDataBase.close();
	}
	
	
	
	
	/**
	 * It will delete all other data from <b>notification_medplann </b> table excluding current selected box data  
	 * @param loginID
	 * @param userId
	 * @param newBoxId
	 * @return ArryList of {@link PendingAlarmUtil } 
	 * 
	 */
	public ArrayList<PendingAlarmUtil> getDeadAlarm(int loginID, int userId ,int newBoxId ,int oldBoxId){
	openDataBase(); 
//		String Sql = "select * from notification_medplann where loginid="
//				+ loginID
//				+ " and userid="
//				+ userId
//				+ " and boxid not in (select boxid from notification_medplann where boxid="
//				+ boxId + " and userid=" + userId + " and loginid=" + loginID
//				+ ")";
	
	String Sql = "select * from notification_medplann where loginid="
			+ loginID
			+ " and userid="
			+ userId ;

	
	
	//select boxid from notification_medplann where boxid=3 and userid=1 and loginid=1
	Cursor cursor = myDataBase.rawQuery(Sql, null) ;
	
	PendingAlarmUtil alarmUtil  ;
	
	ArrayList<PendingAlarmUtil> alarmUtilsList = new ArrayList<PendingAlarmUtil>();
	
	while(cursor.moveToNext()){
		    alarmUtil = new  PendingAlarmUtil() ;
		    alarmUtil.notificationId = cursor.getInt(cursor.getColumnIndex("notificationid")) ;
		    alarmUtil.loginId = cursor.getInt(cursor.getColumnIndex("loginid"));
		    alarmUtil.userId = cursor.getInt(cursor.getColumnIndex("userid"));
	      	alarmUtil.boxId = cursor.getInt(cursor.getColumnIndex("boxid")); 
	        alarmUtil.cellId= cursor.getInt(cursor.getColumnIndex("cellid"));
	        alarmUtil.time = cursor.getString(cursor.getColumnIndex("timeset"));
	        alarmUtil.interval = cursor.getString(cursor.getColumnIndex("interval")) ;
	        alarmUtil.nextday = cursor.getString(cursor.getColumnIndex("nextday")) ;
	        alarmUtil.date = cursor.getString(cursor.getColumnIndex("date")) ;
	        alarmUtil.waytostop = cursor.getInt(cursor.getColumnIndex("waytostop"));
	        alarmUtil.sound = cursor.getInt(cursor.getColumnIndex("sound"));
	        alarmUtil.description = cursor.getString(cursor.getColumnIndex("description")) ;
	        alarmUtil.medicine = cursor.getString(cursor.getColumnIndex("med")) ;
	        
	        alarmUtilsList.add(alarmUtil);
	   }
	
	
//	String SqlDelete  = "delete  from notification_medplann where loginid="
//			+ loginID
//			+ " and userid="
//			+ userId
//			+ " and boxid not in (select boxid from notification_medplann where boxid="
//			+ newBoxId + " and userid=" + userId + " and loginid=" + loginID
//	+ ")";
                               

	ContentValues contentValues = new  ContentValues();
//	contentValues.put("userid", userId);
//	contentValues.put("loginid", loginID);
	contentValues.put("boxid", newBoxId);
	
	 
	myDataBase.delete("notification_medplann","userid="+userId +" and loginid="+loginID, null) ;
	
//	myDataBase.update("notification_medplann", contentValues, "userid="+userId +" and loginid="+loginID, null);
//	myDataBase.execSQL(SqlDelete);
       
       
	return alarmUtilsList;
	}
	
	
	public  void updateBox (int userId, int cellId, int boxId, int loginId){
		
		ContentValues values = new ContentValues() ;
//		values.put("form_user_id", userId);
//		values.put("form_cell_id", cellId) ;
		values.put("form_box_id", boxId);
//		values.put("form_login_id", loginId);
		myDataBase.update("form_medplann", values,"form_user_id="+userId+" and form_cell_id="+cellId +" and form_login_id="+loginId , null);
	}
	 
	public void deleteCell(int userId, int cellId, int boxId, int loginId) {
		
		myDataBase.delete("form_medplann", "form_user_id=" + userId
				+ " and form_cell_id=" + cellId + " and form_login_id="
				+ loginId + " and form_box_id=" + boxId, null);

		  
		myDataBase.delete("notification_medplann", " loginid="+loginId+" and userid="+userId+" and boxid="+boxId +" and cellid="+cellId, null) ;		
		 
	}
	
}
