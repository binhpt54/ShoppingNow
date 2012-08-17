package com.vinova_g12.shoppingnow.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlaceDatabase {
	public static final String ID = "_id";
	public static final String PLACE = "place";
	private static Context mContext;
	private SQLiteDatabase repositoryDB;
	private static final String DATABASE_NAME = "Place";
	private static final String TABLE_NAME = "Data";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = 
			"create table if not exists " + TABLE_NAME + " ("
			+ ID + " integer primary key autoincrement,"
			+ PLACE + " text default \" \" " + ")";
	private static final String DROP_TABLE = "drop table if exists "
			+ TABLE_NAME;
	private DataHelper mHelper;

	public PlaceDatabase(Context context) {
		this.mContext = context;
	}

	public PlaceDatabase openDB() {
		mHelper = new DataHelper(mContext);
		repositoryDB = mHelper.getWritableDatabase();
		return this;
	}

	// Close database
	public void closeDB() {
		mHelper.close();
	}

	public long insert(ContentValues values) {
		if (values != null)
			return repositoryDB.insert(TABLE_NAME, null, values);
		return -1;
	}

	// Update Item
	public long update(int id, ContentValues values) {
		if (values != null)
			return repositoryDB.update(TABLE_NAME, values, ID + "=" + id, null);
		return -1;
	}

	public List<String> getAllPlaces() {
		List<String> places = new ArrayList<String>();
		String[] Place = {PLACE};
		Cursor cursor = repositoryDB.rawQuery("select place from "+ TABLE_NAME , null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String place = cursor.getString(0);
			places.add(place);
			cursor.moveToNext();
		}
		return places;
	}
	
	
	private static class DataHelper extends SQLiteOpenHelper {
		DataHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
				Log.v("DataHelper", "Database is created");
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i("DataHelper", "Updating database...");
			db.execSQL(DROP_TABLE);
			onCreate(db);
		}

	}

}
