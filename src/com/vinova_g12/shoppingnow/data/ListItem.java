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
	/*
	public ListItem(Cursor cursor, int a) {
		super();
		id = cursor.getInt(0);
		name = cursor.getString(1);
		unit = cursor.getString(2);
		price = cursor.getFloat(3);
	}
	*/
	
	public int compareALphabet(ListItem item) {
		return this.name.compareTo(item.name);
	}
	
	public int comparePriority(ListItem item) {
		if (this.priority < item.priority)
			return -1;
		if (this.priority > item.priority)
			return 1;
		return 0;
	}
	
	public int comparePrice(ListItem item) {
		if (this.price < item.price)
			return -1;
		if (this.price > item.price)
			return 1;
		return 0;
	}
}
