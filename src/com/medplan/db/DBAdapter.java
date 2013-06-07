package com.medplan.db;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBAdapter {

	private  final String DATABASE_NAME = "MedPlan.sqlite";

	private  final int DATABASE_VERSION = 1;

	private  Context context;

	private DatabaseHelper DBHelper;
	private static SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	public  class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		
		public void onCreate(SQLiteDatabase db) {
			// db.execSQL(DATABASE_CREATE);

		}

	
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			//onCreate(db);
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	public void update_delete_insertquery(String s) {
		db.execSQL(s);
	}

	public Cursor selectquery(String s) {
		return db.rawQuery(s, null);
	}
}
