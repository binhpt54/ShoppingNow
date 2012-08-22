package com.vinova_g12.shoppingnow.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	public static final String DONE_DATE = "done_date";
	private static String[] allColumns = { ID, NAME, PRIO, QUANT, UNIT, PRICE,
			MONEY, PLACE, DUE, ALARM, STATUS };
	private static Context mContext;
	private SQLiteDatabase shoppingDB;
	private static final String DATABASE_NAME = "ShoppingNow";
	private static final String TABLE_NAME = "shoppingItem";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table if not exists "
			+ TABLE_NAME + " (" + ID + " integer primary key autoincrement,"
			+ NAME + " text not null," + PRIO + " integer default 0," + QUANT
			+ " float not null default 0," + UNIT + " text ," + PRICE
			+ " float default 0," + MONEY + " text," + DUE + " text," + ALARM
			+ " text default \" \"," + STATUS + " integer default 0," + PLACE
			+ " text default \" \" " + ")";
	private static final String DROP_TABLE = "drop table if exists "
			+ TABLE_NAME;
	private DataHelper mHelper;
	private Calendar today, tomorrow, yesterday, thisweek, lastweek, nextweek,
			later;
	private SimpleDateFormat format;

	public ShoppingDatabase(Context context) {
		today = Calendar.getInstance();
		tomorrow = Calendar.getInstance();
		yesterday = Calendar.getInstance();
		thisweek = Calendar.getInstance();
		thisweek.setFirstDayOfWeek(Calendar.MONDAY);
		nextweek = Calendar.getInstance();
		nextweek.setFirstDayOfWeek(Calendar.MONDAY);
		lastweek = Calendar.getInstance();
		lastweek.setFirstDayOfWeek(Calendar.MONDAY);
		later = Calendar.getInstance();
		format = new SimpleDateFormat("dd-MM-yyyy");
		this.mContext = context;
	}

	// Open database to write
	public ShoppingDatabase openDB() {
		mHelper = new DataHelper(mContext);
		shoppingDB = mHelper.getWritableDatabase();
		return this;
	}

	// Close database
	public void closeDB() {
		mHelper.close();
	}

	// Insert a new item to database. If successfull, return the rowId of the
	// newly inserted row
	// If error, return -1
	public long insert(ContentValues values) {
		if (values != null)
			return shoppingDB.insert(TABLE_NAME, null, values);
		return -1;
	}

	// Update Item
	public long update(int id, ContentValues values) {
		if (values != null)
			return shoppingDB.update(TABLE_NAME, values, ID + "=" + id, null);
		return -1;
	}

	// Get all item in a date (Today, Tomorrow, Yesterday)
	public Cursor getAll_inDate(String date) {
		Cursor cur = null;
		String sql;
		if (date.equals("Today")) {
			date = format.format(today.getTime());
			Log.v("DataHelper", date);
			sql = "select * from " + TABLE_NAME + " where " + DUE + "=\""
					+ date + "\"";
			Log.v("DataHelper", sql);
			cur = shoppingDB.rawQuery(sql, null);
			return cur;
		} else if (date.equals("Tomorrow")) {
			tomorrow.add(Calendar.DAY_OF_MONTH, 1);
			date = format.format(tomorrow.getTime());
			Log.v("Tomorrow", date);
			sql = "select * from " + TABLE_NAME + " where " + DUE + "=\""
					+ date + "\"";
			cur = shoppingDB.rawQuery(sql, null);
			return cur;
		} else if (date.equals("Yesterday")) {
			yesterday.add(Calendar.DAY_OF_MONTH, -1);
			date = format.format(yesterday.getTime());
			Log.v("Yesterday", date);
			sql = "select * from " + TABLE_NAME + " where " + DUE + "=\""
					+ date + "\"";
			cur = shoppingDB.rawQuery(sql, null);
			return cur;
		}
		return null;
	}

	// Get all item in a week (This week, Next week, last week)
	public Cursor getAll_inWeek(String week) {
		Cursor cursor = null;
		String sql;
		List<String> date_in_this_week = new ArrayList<String>();
		List<String> date_in_next_week = new ArrayList<String>();
		List<String> date_in_last_week = new ArrayList<String>();
		if (week.equals("This week")) {
			thisweek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			date_in_this_week.add(format.format(thisweek.getTime()));
			while (thisweek.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				thisweek.add(Calendar.DAY_OF_WEEK, 1);
				date_in_this_week.add(format.format(thisweek.getTime()));
			}

			sql = "select * from shoppingItem where due_date IN (";
			for (int i = 0; i < date_in_this_week.size(); i++) {
				sql += "\"";
				sql += date_in_this_week.get(i);
				if (i != (date_in_this_week.size() - 1))
					sql += "\",";
				else
					sql += "\")";
			}
			Log.d("Select week", sql);
			cursor = shoppingDB.rawQuery(sql, null);
			return cursor;
		}
		if (week.equals("Next week")) {
			nextweek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			nextweek.add(Calendar.DAY_OF_WEEK, 1);
			date_in_next_week.add(format.format(nextweek.getTime()));
			while (nextweek.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
				nextweek.add(Calendar.DAY_OF_MONTH, 1);
				date_in_next_week.add(format.format(nextweek.getTime()));
			}
			sql = "select * from shoppingItem where due_date IN (";
			for (int i = 0; i < date_in_next_week.size(); i++) {
				sql += "\"";
				sql += date_in_next_week.get(i);
				if (i != (date_in_next_week.size() - 1))
					sql += "\",";
				else
					sql += "\")";
			}
			Log.d("Select week", sql);
			cursor = shoppingDB.rawQuery(sql, null);
			return cursor;
		}

		if (week.equals("Last week")) {
			lastweek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			lastweek.add(Calendar.DAY_OF_WEEK, -1);
			date_in_last_week.add(format.format(lastweek.getTime()));
			while (lastweek.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				lastweek.add(Calendar.DAY_OF_MONTH, -1);
				date_in_last_week.add(format.format(lastweek.getTime()));
			}
			sql = "select * from shoppingItem where due_date IN (";
			for (int i = 0; i < date_in_last_week.size(); i++) {
				sql += "\"";
				sql += date_in_last_week.get(i);
				if (i != (date_in_last_week.size() - 1))
					sql += "\",";
				else
					sql += "\")";
			}
			Log.d("Select week", sql);
			cursor = shoppingDB.rawQuery(sql, null);
			return cursor;
		}
		return null;
	}

	// Get all item later
	public Cursor getAll_Later() {
		return null;
	}

	public List<ListItem> getAllItems() {
		List<ListItem> items = new ArrayList<ListItem>();
		Cursor cursor = shoppingDB.query(TABLE_NAME, allColumns, null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ListItem item = cursorToItem(cursor);
			items.add(item);
			cursor.moveToNext();
		}

		cursor.close();
		return items;
	}

	public Cursor getAllItemInAdrress(String addr) {

		String sql = "select * from " + TABLE_NAME + " where " + PLACE
				+ " like \"%" + addr + "%\"";
		Log.w("sqlabc", sql);
		Cursor cursor = shoppingDB.rawQuery(sql, null);
		return cursor;
	}

	private ListItem cursorToItem(Cursor cursor) {
		ListItem item = new ListItem();
		item.id = cursor.getInt(0);
		item.priority = cursor.getInt(2);
		item.quantity = cursor.getInt(3);
		item.price = cursor.getInt(5);
		item.status = cursor.getInt(10);
		item.name = cursor.getString(1);
		item.unit = cursor.getString(4);
		item.money = cursor.getString(6);
		item.place = cursor.getString(7);
		item.due_date = cursor.getString(8);
		item.alarm = cursor.getString(9);

		return item;
	}

	public Cursor getItem(int id) {
		Log.d("ID edit item", id + "");
		return shoppingDB.rawQuery("select * from " + TABLE_NAME
				+ " where _id = " + id, null);
	}

	// Delete some item is checked
	// If user want delete all item in a date or week, then list is all item
	// checked
	// in a date or week (use function getAll_inDate or getAll_inWeek to
	// instead)
	public int deleteSome(List<Integer> checked) {
		int count = checked.size();
		for (int i = 0; i < count; i++)
			delete(checked.get(i));
		return 1;
	}

	public int delete(int id) {
		Log.d("id delte", id + "");
		return shoppingDB.delete(TABLE_NAME, ID + "=" + id, null);
	}

	public long updateStatus(String stt, int id) {
		ContentValues values = new ContentValues();
		if (stt.equals("Done")) {
			values.put(STATUS, 1);
		} else
			values.put(STATUS, 0);
		return shoppingDB.update(TABLE_NAME, values, ID + "=" + id, null);
	}

	public void updateSomeStatus(String stt, List<Integer> id) {
		for (int i = 0; i < id.size(); i++)
			updateStatus(stt, id.get(i));
	}

	public List<String> search(String str) {
		List<String> items = new ArrayList<String>();
		String sql = "select * from " + TABLE_NAME + " where name like \"%"
				+ str + "%\"";
		Log.w("Search", sql);
		Cursor cursor = shoppingDB.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String item = cursor.getString(1);
			items.add(item);
			cursor.moveToNext();
		}
		
		for (int i=0;i<items.size();i++)
			Log.w("Search",i + " " + items.get(i));
		cursor.close();
		return items;
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
