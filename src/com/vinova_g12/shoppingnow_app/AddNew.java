package com.vinova_g12.shoppingnow_app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TooManyListenersException;

import com.example.shoppingnow.R;
import com.example.shoppingnow.R.layout;
import com.example.shoppingnow.R.menu;
import com.vinova_g12.shoppingnow.data.ListItem;
import com.vinova_g12.shoppingnow.data.PlaceDatabase;
import com.vinova_g12.shoppingnow.data.RepoData;
import com.vinova_g12.shoppingnow.data.ShoppingDatabase;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.support.v4.app.NavUtils;

public class AddNew extends Activity{
	private ListItem item;
	//Button
	private Button btn_create;
	private Button btn_save;
	private Button btn_done;
	private Button btn_cancel;
	private Button btn_due_date;
	private Button btn_alarm;
	//Autocomplate name and place
	private AutoCompleteTextView name;
	private AutoCompleteTextView place;
	//Toogle Button Priority
	private ToggleButton toggle_priority;
	//Edittext Quantity and Price
	private EditText edit_quantity;
	private EditText edit_price;
	//Spinner unit
	private Spinner edit_unit;
	//TextView
	private TextView total;
	private TextView money;
	private ImageView banner;
	
	List<String> address = new ArrayList<String>();
	List<String> autoName = new ArrayList<String>();
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	Calendar today = Calendar.getInstance();
	
	String[] itemSpinner = new String[] {"Kilogam", "Gam", "Lạng","Chiếc", "Bó", "Mớ", "Túi", "Gói",  "Bình", "Chai", "Lọ", "Thùng", "Hộp" };
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new);
		//Get id widget from file xml layout
		btn_create = (Button) findViewById(R.id.btn_create);
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_done = (Button) findViewById(R.id.btn_done);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_due_date = (Button) findViewById(R.id.edit_new_duedate);
		btn_alarm = (Button) findViewById(R.id.edit_new_alarm);
		
		name = (AutoCompleteTextView) findViewById(R.id.edit_new_title);
		place = (AutoCompleteTextView) findViewById(R.id.edit_new_place);
		
		edit_quantity = (EditText) findViewById(R.id.edit_new_quantity);
		edit_price = (EditText) findViewById(R.id.edit_new_price);
		
		total = (TextView) findViewById(R.id.total);
		money = (TextView) findViewById(R.id.money);
		banner = (ImageView) findViewById(R.id.banner);
		
		toggle_priority = (ToggleButton) findViewById(R.id.toggle_priority);
		edit_unit = (Spinner) findViewById(R.id.edit_new_unit);
		//Set font for widgets
		btn_create.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		btn_save.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		btn_done.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		btn_cancel.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		edit_quantity.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		edit_price.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		
		btn_due_date.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
		btn_alarm.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
		toggle_priority.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
		total.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
		money.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
		name.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
		place.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		
		//Create a new item and open database
		item = new ListItem();
		
		//Set behavior for toogle button
		View.OnClickListener toggleListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (item.priority == 0) {
					item.priority = 1;
					toggle_priority.setCompoundDrawablesWithIntrinsicBounds(R.drawable.light_important, 0, 0, 0);
					toggle_priority.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
				} else {
					item.priority = 0;
					toggle_priority.setCompoundDrawablesWithIntrinsicBounds(R.drawable.light_no_important, 0, 0, 0);
					toggle_priority.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
				}
				
			}
		};
		toggle_priority.setOnClickListener(toggleListener);
		//Set adapter for spinner
		ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemSpinner);
		adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_unit.setAdapter(adapterSpinner);
		OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				item.unit = parent.getItemAtPosition(pos).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				item.unit = parent.getSelectedItem().toString();
			}
		};
		edit_unit.setOnItemSelectedListener(spinnerListener);
	}
	
	public int getData() {
		if (name.getText().toString().length() != 0) {
			//Get name
			item.name = name.getText().toString();
			//Get quantity
			if (edit_quantity.getText().toString().length() != 0)
				item.quantity = Float.valueOf(edit_quantity.getText().toString()).floatValue();
			else
				item.quantity = 0;
			//Get price
			if (edit_price.getText().toString().length() != 0)
				item.price = Float.valueOf(edit_price.getText().toString()).floatValue();
			else
				item.price = 0;
			//Get place
			if (place.getText().toString().length() != 0)
				item.place = place.getText().toString();
			else
				item.place = " ";
			return 1;
		}
		return 0;
	}
}