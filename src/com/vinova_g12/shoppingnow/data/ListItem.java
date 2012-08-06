package com.vinova_g12.shoppingnow.data;

import java.util.Date;

import android.annotation.SuppressLint;
import android.database.Cursor;

public class ListItem {
	public int id;
	public String name;
	public int priority;
	public int quantity;
	public int price;
	public String unit;
	public String money;
	public Double due_date;
	public Double alarm;
	public int status;
	
	public ListItem() {
		super();
		id = -1;
	}
	
	public ListItem(Cursor cursor) {
		super();
		name = cursor.getString(1);
		priority = cursor.getInt(2);
		quantity = cursor.getInt(3);
		unit = cursor.getString(4);
		price = cursor.getInt(5);
		money = cursor.getString(6);
		due_date = cursor.getDouble(7);
		alarm = cursor.getDouble(8);
		status = cursor.getInt(9);
	}
}
