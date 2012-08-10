package com.vinova_g12.shoppingnow.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.vinova_g12.shoppingnow_app.AddNew;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ShoppingDatabase {
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String PRIO = "priority";
	public static final String QUANT = "quantity";
	public static final String UNIT = "unit";
	public static final String PRICE = "price";
	public static final String MONEY = "money";
	public static final String DUE = "due_date";
	public static final String ALARM = "alarm_date";
	public static final String STATUS = "status";
	public static final String PLACE = "place";
	
	private static Context mContext;
	private SQLiteDatabase shoppingDB;
	private static final String DATABASE_NAME = "ShoppingNow";
	private static final String TABLE_NAME = "shoppingItem";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = 
			"create table if not exists " + TABLE_NAME + " ("
			+ ID + " integer primary key autoincrement,"
			+ NAME + " text not null,"
			+ PRIO + " integer default 0,"
			+ QUANT + " float not null default 0,"
			+ UNIT + " text ,"
			+ PRICE + " float default 0,"
			+ MONEY + " text,"
			+ DUE + " text,"
			+ ALARM + " text,"
			+ STATUS + " integer default 0," 
			+ PLACE + " text default \" \" " + ")";
	private static final String DROP_TABLE = 
			"drop table if exists " + TABLE_NAME;
	private DataHelper mHelper;
	private Calendar today,tomorrow,yesterday,thisweek,lastweek,nextweek,later;
	private SimpleDateFormat format;
	
	
	public ShoppingDatabase(Context context) {
		today = Calendar.getInstance();
		tomorrow = Calendar.getInstance();
		yesterday = Calendar.getInstance();
		thisweek = Calendar.getInstance();
		nextweek = Calendar.getInstance();
		lastweek = Calendar.getInstance();
		later = Calendar.getInstance();
		format = new SimpleDateFormat("dd-MM-yyyy");
		this.mContext = context;
	}
	//Open database to write
	public ShoppingDatabase openDB() {
		mHelper = new DataHelper(mContext);
		shoppingDB = mHelper.getWritableDatabase();
		return this;
	}
	//Close database
	public void closeDB() {
		mHelper.close();
	}
	//Insert a new item to database. If successfull, return the rowId of the newly inserted row
	//If error, return -1
	public long insert(ContentValues values) {
		if (values != null)
			return shoppingDB.insert(TABLE_NAME, null, values);
		return -1;
	}
	//Get all item in a date (Today, Tomorrow, Yesterday)
	public Cursor getAll_inDate(String date) {
		Cursor cur = null;
		String sql;
		if (date.equals("Today")) {
			date = format.format(today.getTime());
			Log.v("DataHelper", date);
			sql = "select * from " + TABLE_NAME + " where " + DUE + "=\"" + date + "\"";
			Log.v("DataHelper", sql);
			cur = shoppingDB.rawQuery(sql, null);
			return cur;
		}
		else if (date.equals("Tomorrow")) {
			tomorrow.add(Calendar.DAY_OF_MONTH, 1);
			date = format.format(tomorrow.getTime());
			Log.v("Tomorrow", date);
			sql = "select * from " + TABLE_NAME + " where " + DUE + "=\"" + date + "\"";
			cur = shoppingDB.rawQuery(sql, null);
			return cur;
		}
		else if (date.equals("Yesterday")) {
			yesterday.add(Calendar.DAY_OF_MONTH, -1);
			date = format.format(yesterday.getTime());
			Log.v("Yesterday", date);
			sql = "select * from " + TABLE_NAME + " where " + DUE + "=\"" + date + "\"";
			cur = shoppingDB.rawQuery(sql, null);
			return cur;
		}
		return null;
	}
	//Get all item in a week (This week, Next week, last week)
	public Cursor getAll_inWeek(String week) {
		if (week.equals("This week"))
			return null;
		if (week.equals("Next week"))
			return null;
		if (week.equals("Last week"))
			return null;
		return null;
	}
	//Get all item later
	public Cursor getAll_Later() {
		return null;
	}
	//Delete some item is checked
	//If user want delete all item in a date or week, then list is all item checked 
	//in a date or week (use function getAll_inDate or getAll_inWeek to instead)
	public int deleteSome(List<Integer> checked) {
		int count = checked.size();
		for (int i=0; i<count; i++) 
			delte(checked.get(i));
		return 1;
	}
	
	public int delte(int id) {
		Log.d("id delte", id + "");
		return shoppingDB.delete(TABLE_NAME, ID + "=" + id, null);
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
			}catch(SQLException ex) {ex.printStackTrace();}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i("DataHelper", "Updating database...");
            db.execSQL(DROP_TABLE);
            onCreate(db);
		}
	}

}
