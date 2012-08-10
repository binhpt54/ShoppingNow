package com.vinova_g12.shoppingnow.data;

import java.util.Date;

import android.annotation.SuppressLint;
import android.database.Cursor;

public class ListItem {
	public int id;
	public String name;
	public int priority;
	public float quantity;
	public float price;
	public String unit;
	public String money;
	public String due_date;
	public String alarm;
	public String place;
	public int status;
	
	public ListItem() {
		super();
		id = -1;
	}
	
	public ListItem(Cursor cursor) {
		super();
		id = cursor.getInt(0);
		name = cursor.getString(1);
		priority = cursor.getInt(2);
		quantity = cursor.getFloat(3);
		unit = cursor.getString(4);
		price = cursor.getFloat(5);
		money = cursor.getString(6);
		due_date = cursor.getString(7);
		alarm = cursor.getString(8);
		status = cursor.getInt(9);
		place = cursor.getString(10);
	}
}
