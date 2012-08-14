package com.vinova_g12.shoppingnow_app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.shoppingnow.R;
import com.example.shoppingnow.R.layout;
import com.example.shoppingnow.R.menu;
import com.vinova_g12.shoppingnow.data.ListItem;
import com.vinova_g12.shoppingnow.data.RepoData;
import com.vinova_g12.shoppingnow.data.ShoppingDatabase;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class AddNew extends Activity {
	LinearLayout moreOption;
	Button btn_more_option;
	private ListItem newItem;
	private ShoppingDatabase database;
	private RepoData saveData;
	// Buttons
	private Button btn_update;
	private Button btn_cancel;
	private Button btn_set_duedate;
	private Button btn_set_alarm;
	private Button btn_cancel_duedate;
	private Button btn_cancel_alarm;
	// Due date
	private int mYear, mMonth, mDay;
	// Alarm date
	private int aYear, aMonth, aDay;
	// Edittexts and Radio Button
	private AutoCompleteTextView name;
	private RadioGroup priority;
	private EditText price;
	private EditText quantity;
	private EditText place;
	// Radio Button
	private RadioButton radioLow;
	private RadioButton radioNone;
	private RadioButton radioMedium;
	private RadioButton radioHard;

	private int id;
	// Calendar
	Calendar today = Calendar.getInstance();
	public SimpleDateFormat format;
	DatePickerDialog.OnDateSetListener datePickerListener;
	DatePickerDialog.OnDateSetListener alarmDatePickerListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_add_new_products);

		Intent id_receive = getIntent();

		Bundle myBundle = id_receive.getExtras();
		id = myBundle.getInt("id");

		newItem = new ListItem();
		database = new ShoppingDatabase(getApplicationContext());
		// Open database
		database.openDB();
		
		saveData = new RepoData(getApplicationContext());
		saveData.openDB();

		moreOption = (LinearLayout) findViewById(R.id.layout_more_option);
		btn_more_option = (Button) findViewById(R.id.btn_more_option);
		btn_set_duedate = (Button) findViewById(R.id.edit_new_duedate);
		btn_set_alarm = (Button) findViewById(R.id.edit_new_alarm);
		btn_update = (Button) findViewById(R.id.btn_create);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel_duedate = (Button) findViewById(R.id.btn_cancel_duedate);
		btn_cancel_alarm = (Button) findViewById(R.id.btn_cancel_alarm);

		name = (AutoCompleteTextView) findViewById(R.id.edit_new_title);
		
		List<String> auto = new ArrayList<String>();
		auto = saveData.getAllNames();
		for (int i = 0; i < auto.size(); i++)
			Log.w("name", i + " " + auto.get(i));
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, auto);
		name.setAdapter(adapter);
		name.setTextColor(Color.BLACK);

		
		price = (EditText) findViewById(R.id.edit_new_price);
		quantity = (EditText) findViewById(R.id.edit_new_quantity);
		place = (EditText) findViewById(R.id.edit_new_place);
		priority = (RadioGroup) findViewById(R.id.gradio_priority);

		radioNone = (RadioButton) findViewById(R.id.pri_normal);
		radioLow = (RadioButton) findViewById(R.id.pri_low);
		radioMedium = (RadioButton) findViewById(R.id.pri_medium);
		radioHard = (RadioButton) findViewById(R.id.pri_hard);

		/* Get current day and format date */
		format = new SimpleDateFormat("dd-MM-yyyy");

		mYear = today.get(Calendar.YEAR);
		mMonth = today.get(Calendar.MONTH);
		mDay = today.get(Calendar.DAY_OF_MONTH);

		aYear = today.get(Calendar.YEAR);
		aMonth = today.get(Calendar.MONTH);
		aDay = today.get(Calendar.DAY_OF_MONTH);

		newItem.due_date = format.format(today.getTime());
		//Set click for item in list autocomple
		name.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				String name_ = adapter.getItem(position);
				Log.w("name_",name_);
				Cursor getData = saveData.getItemFromName(name_);
				float price_ = 0;
				Log.w("Size of data return","");
				if (getData.moveToFirst()){
					Log.w("move to first","");
					price_ = getData.getFloat(3);
					price.setText(price_+"");
				}

				Toast.makeText(getApplicationContext(),"onClickAutocomplete" ,Toast.LENGTH_SHORT).show();
				Log.w("Click Autocomplete list","");

			}
			
		});
		/* Display moreoption when user press on button More Options */
		btn_more_option.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (moreOption.getVisibility() == View.GONE) {
					moreOption.setVisibility(View.VISIBLE);
					btn_more_option.setText("Ẩn Thông Tin");
				} else {
					moreOption.setVisibility(View.GONE);
					btn_more_option.setText("Thêm Thông Tin");
				}

			}
		});

		/* Display dialog to picker due date */
		datePickerListener = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				Toast.makeText(getApplicationContext(),
						"" + mDay + " " + mMonth + " " + mYear,
						Toast.LENGTH_SHORT).show();
				btn_set_duedate.setText(format.format(new Date(mYear - 1900,
						mMonth, mDay)));
				newItem.due_date = btn_set_duedate.getText().toString();
				btn_cancel_duedate.setVisibility(View.VISIBLE);
			}
		};

		btn_cancel_duedate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mYear = today.get(Calendar.YEAR);
				mMonth = today.get(Calendar.MONTH);
				mDay = today.get(Calendar.DAY_OF_MONTH);
				btn_set_duedate.setText("Đặt Ngày Giờ");
				btn_cancel_duedate.setVisibility(View.GONE);
			}
		});

		btn_set_duedate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new DatePickerDialog(AddNew.this, datePickerListener,
						mYear, mMonth, mDay).show();
			}
		});

		/* Display dialog to picker alarm date */
		alarmDatePickerListener = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				aYear = year;
				aMonth = monthOfYear;
				aDay = dayOfMonth;
				btn_set_alarm.setText(format.format(new Date(aYear - 1900,
						aMonth, aDay)));
				newItem.alarm = btn_set_alarm.getText().toString();
				btn_cancel_alarm.setVisibility(View.VISIBLE);
			}
		};

		btn_cancel_alarm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				aYear = aMonth = aDay = 0;
				btn_set_alarm.setText("Đặt Ngày Giờ");
				btn_cancel_alarm.setVisibility(View.GONE);
			}
		});

		btn_set_alarm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new DatePickerDialog(AddNew.this, alarmDatePickerListener,
						aYear, aMonth, aDay).show();
			}
		});
		/* cancel */
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AddNew.this.finish();
			}
		});
		/* create new product */
		btn_update.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!name.getText().toString().equals("")) {
					// Set name for product
					newItem.name = name.getText().toString();
					// Set priority for product
					int radioId = priority.getCheckedRadioButtonId();
					if (radioNone.getId() == radioId)
						newItem.priority = 0;
					else if (radioLow.getId() == radioId)
						newItem.priority = 1;
					else if (radioMedium.getId() == radioId)
						newItem.priority = 2;
					else if (radioHard.getId() == radioId)
						newItem.priority = 3;

					// Set quantity
					if (!quantity.getText().toString().equals(""))
						newItem.quantity = Float.valueOf(
								quantity.getText().toString()).floatValue();
					else
						newItem.quantity = 0;

					// Set price
					if (!price.getText().toString().equals(""))
						newItem.price = Float.valueOf(
								price.getText().toString()).floatValue();
					else
						newItem.price = 0;
					// Set status is undone
					newItem.status = 0;

					Log.d("New Item", newItem.name);
					Log.d("New Item", newItem.priority + "");
					Log.d("New Item", newItem.quantity + "");
					Log.d("New Item", newItem.price + "");
					Log.d("New Item", newItem.due_date);
					Log.d("New Item", newItem.alarm + "alarm");

					ContentValues values = new ContentValues();
					
					ContentValues values_ = new ContentValues();
					
					values.put(ShoppingDatabase.NAME, newItem.name);
					values.put(ShoppingDatabase.PRIO, newItem.priority);
					values.put(ShoppingDatabase.QUANT, newItem.quantity);
					values.put(ShoppingDatabase.PRICE, newItem.price);
					values.put(ShoppingDatabase.STATUS, newItem.status);
					values.put(ShoppingDatabase.DUE, newItem.due_date);
					values.put(ShoppingDatabase.MONEY, "VND");

					if (!btn_set_alarm.getText().equals("Đặt Ngày Giờ"))
						values.put(ShoppingDatabase.ALARM, newItem.alarm);
					if (!place.getText().toString().equals("")){
						values.put(ShoppingDatabase.PLACE, place.getText()
								.toString());
						values_.put(ShoppingDatabase.PLACE, place.getText()
								.toString());
					}	
					
					

					values_.put(ShoppingDatabase.NAME, newItem.name);
					values_.put(ShoppingDatabase.PRICE, newItem.price);
					
					if (id == 1000) {
						List<ListItem> names = new ArrayList<ListItem>();
						names = database.getAllItems();
						boolean add = true;
						for (int i = 0; i < names.size(); i++) {
							if (names.get(i).name.equals(newItem.name)) {
								add = false;
								Log.w("Ten san pham da ton tai", "sfnholush");
								break;
							}
						}
						if (add) {
							database.insert(values);
							saveData.insert(values_);
						} else
							Toast.makeText(getApplicationContext(),
									"Sản phẩm đã tồn tại trong danh sách!",
									Toast.LENGTH_SHORT).show();

					}
					else database.update(id, values);
					database.closeDB();
					today.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					Log.d("Calendar", today.getTime().toString());
					today.add(Calendar.DAY_OF_WEEK, -7);
					Log.d("Calendar", today.getTime().toString());

					AddNew.this.finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"Bạn chưa nhập tên sản phẩm. Vui lòng thử lại!",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		if (id != 1000) {
			btn_update.setText("Save");
			Cursor cur = null;
			cur = database.getItem(id);
			int c = cur.getCount();
			Log.w("count", c + " " + cur.getColumnCount());

			if (cur.moveToFirst()) {
				ListItem editItem = new ListItem(cur);
				Log.d("Name", editItem.name);
				name.setText(editItem.name);
				priority.check(editItem.priority);
				place.setText(editItem.place);
				quantity.setText(editItem.quantity + "");
				price.setText(editItem.price + "");
				btn_set_duedate.setText(editItem.due_date);
				btn_set_alarm.setText(editItem.alarm);			
				
			}
			
			
		}

	}

}
