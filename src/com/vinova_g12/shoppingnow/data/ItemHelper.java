package com.vinova_g12.shoppingnow.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "item.db";
	public static final String ITEM_TABLE_NAME = "item";

	public static final String ID = "ID";
	public static final String NAME = "NAME";
	public static final String PRIORITY = "PRIORITY";
	public static final String PRICE = "PRICE";
	public static final String STATUS = "STATUS";
	public static final String QUANTITY = "QUANTITY";
	public static final String UNIT = "UNIT";
	public static final String PLACE = "PLACE";
	public static final String TIME = "TIME";
	public static final String DATE = "DATE";

	private static final String ITEM_TABLE_CREATE = "CREATE TABLE "
			+ ITEM_TABLE_NAME + "(" + ID
			+ " integer primary key autoincrement, "
			+ NAME + " TEXT, "
			+ PLACE + " TEXT,"
			+ PRICE + " float,"
			+ PRIORITY + " integer,"
			+ QUANTITY + " float,"
			+ STATUS + " integer, "
			+ TIME + " TEXT," 
			+ DATE + " TEXT,"
			+ UNIT + " TEXT);";
	
	ItemHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL("drop table if exists " + ITEM_TABLE_NAME);
		db.execSQL(ITEM_TABLE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(ItemHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS");
		onCreate(db);
	}	
	

}
