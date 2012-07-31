package com.vinova_g12.shoppingnow.data;

import java.util.Date;

import android.annotation.SuppressLint;
import android.database.Cursor;

@SuppressLint("ParserError")
public class ListItem {
	public int id;
	public String name;
	public int priority;
	public int total;
	public int note_have_done;
	public Double due_date;
	public Double alarm;
	public int status;
	
	public ListItem() {
		super();
	}
	
	public ListItem(Cursor cursor) {
		super();
		name = cursor.getString(1);
		priority = cursor.getInt(2);
		total = cursor.getInt(3);
		note_have_done = cursor.getInt(4);
		due_date = cursor.getDouble(5);
		alarm = cursor.getDouble(6);
		status = cursor.getInt(7);
	}
}
