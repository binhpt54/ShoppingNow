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

public class RepoData {
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String UNIT = "unit";
	public static final String PRICE = "price";
	private static String[] allColumns = {ID, NAME, UNIT, PRICE};
	private static Context mContext;
	private SQLiteDatabase repositoryDB;
	private static final String DATABASE_NAME = "Repository";
	private static final String TABLE_NAME = "Data";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = 
			"create table if not exists "
			+ TABLE_NAME + " (" 
			+ ID + " integer primary key autoincrement,"
			+ NAME + " text not null," 
			+ UNIT + " text ," 
			+ PRICE+ " float default 0" + ")";
	private static final String DROP_TABLE = "drop table if exists "
			+ TABLE_NAME;
	private DataHelper mHelper;

	public RepoData(Context context) {
		this.mContext = context;
	}

	public RepoData openDB() {
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

	public List<String> getAllNames() {
		List<String> names = new ArrayList<String>();
		Cursor cursor = repositoryDB.query(TABLE_NAME, allColumns, null, null,
				null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String name = cursor.getString(1);
			names.add(name);
			cursor.moveToNext();
		}
		return names;
	}
	
	
	public Cursor getItemFromName(String name){
		String sql = "select * from " + TABLE_NAME + " where " + NAME + " = \"" + name + "\"";
		Log.w("sqlabc",sql);
		Cursor cursor= repositoryDB.rawQuery(sql,null);
		return cursor;
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
