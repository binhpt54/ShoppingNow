package com.vinova_g12.shoppingnow_app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TooManyListenersException;

import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsSpinner;
import com.example.shoppingnow.R;
import com.example.shoppingnow.R.layout;
import com.example.shoppingnow.R.menu;
import com.vinova_g12.shoppingnow.data.ListItem;
import com.vinova_g12.shoppingnow.data.ListItemAdapter;
import com.vinova_g12.shoppingnow.data.PlaceDatabase;
import com.vinova_g12.shoppingnow.data.RepoData;
import com.vinova_g12.shoppingnow.data.ShoppingDatabase;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
	
	private int state_action;
	public static final int REQUEST_CHOOSE_DATE = 261290;
	public static final String DATE_RECV = "dateGoBack";
	public static final String DATE_RECV_REVERSE = "dateGoBackReverse";
	public static final String DAY_RECV = "dayGoBack";
	public static final String DATE_SENDER = "dateSender";
	private ListItem item;
	//Database
	private ShoppingDatabase shoppingDB;
	private RepoData nameDB;
	private PlaceDatabase placeDB;
	//Button
	private Button btn_create;
	private Button btn_save;
	private Button btn_done;
	private Button btn_cancel;
	private Button btn_due_date;
	private Button btn_delete_name;
	private Button btn_delete_place;
	private ToggleButton btn_alarm;
	//Autocomplate name and place
	private AutoCompleteTextView name;
	private AutoCompleteTextView place;
	//Toogle Button Priority
	private ToggleButton toggle_priority;
	//Edittext Quantity and Price
	private EditText edit_quantity;
	private EditText edit_price;
	//Spinner unit
	//private Spinner edit_unit;
	//TextView
	private TextView total;
	private TextView money;
	private ImageView banner;
	private IcsSpinner edit_unit;
	
	private String dateSender;
	ContentValues valueAutoName, valueAutoPlace;
	
	List<String> autoPlace = new ArrayList<String>();
	List<String> autoName = new ArrayList<String>();
	
	private ArrayAdapter<String> adapterForName;
	private ArrayAdapter<String> adapterForPlace;
	
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat formatReverse = new SimpleDateFormat("yyyy-MM-dd");
	Calendar today = Calendar.getInstance();
	
	String[] itemSpinner = new String[] {"Cân", "Gam", "Lạng","Củ", "Quả","Chiếc", "Bó", "Mớ", "Túi", "Gói",  "Bình", "Chai", "Lọ", "Thùng", "Hộp" };
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
		btn_alarm = (ToggleButton) findViewById(R.id.edit_new_alarm);
		
		name = (AutoCompleteTextView) findViewById(R.id.edit_new_title);
		place = (AutoCompleteTextView) findViewById(R.id.edit_new_place);
		
		edit_quantity = (EditText) findViewById(R.id.edit_new_quantity);
		edit_price = (EditText) findViewById(R.id.edit_new_price);
		
		total = (TextView) findViewById(R.id.total);
		money = (TextView) findViewById(R.id.money);
		banner = (ImageView) findViewById(R.id.banner);
		
		total.setText("");
		toggle_priority = (ToggleButton) findViewById(R.id.toggle_priority);
		edit_unit = (IcsSpinner) findViewById(R.id.edit_new_unit);
		//Set font for widgets
		btn_create.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		btn_save.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		btn_done.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		btn_cancel.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		edit_quantity.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		edit_price.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		btn_delete_name = (Button) findViewById(R.id.delete_name);
		btn_delete_place = (Button) findViewById(R.id.delete_place);
		
		btn_due_date.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
		btn_alarm.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
		toggle_priority.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
		total.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
		money.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
		name.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
		place.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		
		//Create database
		shoppingDB = new ShoppingDatabase(this);
		nameDB = new RepoData(this);
		placeDB = new PlaceDatabase(this);
		
		setAdapterForAutoComplete();
		
		//Create a new item and open database
		item = new ListItem();
		item.due_date = formatReverse.format(today.getTime());
		
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
		//Set behavior for toogle button alarm
		View.OnClickListener toggleAlarm = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (item.alarm.equals(" ")) {
					item.alarm = format.format(today.getTime());
					//Show dialog date slider
				} else {
					item.alarm = " ";
				}
				
			}
		};
		btn_alarm.setOnClickListener(toggleAlarm);
		//Set adapter for spinner
		final ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, 
											itemSpinner);
		adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_unit.setAdapter(adapterSpinner);
		money.setText("VND/" + edit_unit.getSelectedItem().toString());
		com.actionbarsherlock.internal.widget.IcsAdapterView.OnItemSelectedListener lis = new IcsAdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(IcsAdapterView<?> parent, View view,
					int position, long id) {
				item.unit = parent.getItemAtPosition(position).toString();
				money.setText("VND/" + item.unit);
				
			}

			@Override
			public void onNothingSelected(IcsAdapterView<?> parent) {
				item.unit = parent.getSelectedItem().toString();
				
			}
		};
		OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				item.unit = parent.getItemAtPosition(pos).toString();
				money.setText("VND/" + item.unit);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				item.unit = parent.getSelectedItem().toString();
			}
		};
		edit_unit.setOnItemSelectedListener(lis);
		
		//Set action for autotext
		OnItemClickListener autoNameListener = new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
							long id) {
							String name = adapterForName.getItem(pos);
							nameDB.openDB();
							Cursor cur = null;
							cur = nameDB.getItemFromName(name);
							
							Log.d("Cur size", cur.getColumnCount() + "");
							if (cur != null) {
								if (cur.moveToFirst()) {
									String unit = cur.getString(2);
									if (cur.getFloat(3) != 0) {
										String priceOptimize = cur.getFloat(3) + "";
										edit_price.setText(ListItemAdapter.optimizePrice(priceOptimize, ""));
									}
									edit_unit.setSelection(adapterSpinner.getPosition(unit));
								}
							}
							nameDB.closeDB();
					}
				};
		name.setOnItemClickListener(autoNameListener);
		//Text changed listener for name
		TextWatcher nameListener = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (name.getText().length() != 0) {
					btn_delete_name.setVisibility(View.VISIBLE);
					
				}
				else
					btn_delete_name.setVisibility(View.GONE);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		};
		name.addTextChangedListener(nameListener);
		//Text changed listener for place
		TextWatcher placeListener = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (place.getText().length() != 0)
					btn_delete_place.setVisibility(View.VISIBLE);
				else
					btn_delete_place.setVisibility(View.GONE);
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		};
		place.addTextChangedListener(placeListener);
		//Button delete place and name listener. When clicked, make empty place and name edittext
		OnClickListener emptyNameListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					Log.d("Btn DELETE", "Clicked");
					name.setText("");
			}
		};
		
		OnClickListener emptyPlaceListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					place.setText("");
			}
		};
		name.setOnClickListener(emptyNameListener);
		place.setOnClickListener(emptyPlaceListener);
		//Set listener textwatcher. When user enter full quantit and price, 
		//calculate total price and display to screen
		TextWatcher textChanged = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (edit_price.getText().length() != 0 && edit_quantity.getText().length() != 0) {
					Float quant = Float.valueOf(edit_quantity.getText().toString());
					Float price = Float.valueOf(edit_price.getText().toString());
					total.setText("Tất Cả = " + ListItemAdapter.optimizePrice((quant * price)+"", "full") + " VND");
				} else
					total.setText("");
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		};
		edit_quantity.addTextChangedListener(textChanged);
		edit_price.addTextChangedListener(textChanged);
		//Set behavior for button due date
		OnClickListener btnDueDateListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ActivityChooseDate.class);
				Bundle bundle = new Bundle();
				try {
					dateSender = format.format(formatReverse.parse(item.due_date));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bundle.putString("dateSender", dateSender);
				intent.putExtras(bundle);
				startActivityForResult(intent, REQUEST_CHOOSE_DATE);
				
			}
		};
		btn_due_date.setOnClickListener(btnDueDateListener);
		
		//Set behavior for button create
		OnClickListener btnCreateListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				SaveorCreate();
				
			}
		};
		btn_create.setOnClickListener(btnCreateListener);
		
		//Set behavior for btn save
		OnClickListener btnSaveListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				SaveorCreate();
			}
		};
		btn_save.setOnClickListener(btnSaveListener);
		
		//Set behavior for btn done
		OnClickListener btnDoneListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		};
		btn_done.setOnClickListener(btnDoneListener);
		//Set behavior for btn cancel
		OnClickListener btnCancelListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		};
		btn_cancel.setOnClickListener(btnCancelListener);
		
		//Get intent with request edit and edit UI - SAVE OR CREATE
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		state_action = bundle.getInt(MainActivity.REQUEST_CREATE);
		if (state_action == -1) {
			btn_cancel.setVisibility(View.GONE);
			btn_save.setVisibility(View.GONE);
			banner.setImageResource(R.drawable.new_banner);
		} else {
			btn_save.setVisibility(View.VISIBLE);
			btn_create.setVisibility(View.GONE);
			btn_done.setVisibility(View.GONE);
			banner.setImageResource(R.drawable.edit_banner);
			
			Cursor cur = null;
			shoppingDB.openDB();
			cur = shoppingDB.getItem(state_action);
			if (cur != null) {
				if (cur.moveToFirst()) {
					ListItem editItem = new ListItem(cur);
					name.setText(editItem.name);
					//name.setFocusable(false);
					
					//Fixed unit and money
					edit_unit.setSelection(adapterSpinner.getPosition(editItem.unit));
					money.setText("VND/" + edit_unit.getSelectedItem().toString());
					//Fixed priority
					if (editItem.priority == 0) {
						toggle_priority.setChecked(false);
						toggle_priority.setCompoundDrawablesWithIntrinsicBounds(R.drawable.light_no_important, 0, 0, 0);
						toggle_priority.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
					}
					else {
						toggle_priority.setCompoundDrawablesWithIntrinsicBounds(R.drawable.light_important, 0, 0, 0);
						toggle_priority.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
						toggle_priority.setChecked(true);
					}
					
					//Fixed place
					if (!editItem.place.equals("") && !editItem.place.equals(" "))
						place.setText(editItem.place);
					
					//Fixed quantity and price
					if (editItem.quantity != 0)
						edit_quantity.setText(ListItemAdapter.optimizeQuantity(editItem.quantity+""));
					if (editItem.price != 0)
						edit_price.setText(ListItemAdapter.optimizePrice(editItem.price + "",""));
					
					Calendar cal = Calendar.getInstance();
					String s = "";
					try {
						s = format.format(formatReverse.parse(editItem.due_date));
						cal.setTime(formatReverse.parse(editItem.due_date));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					btn_due_date.setText(ListItemAdapter.getDay(cal) + ", " + s);
					
				}
			}
			shoppingDB.closeDB();
		}
		
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
			//Get money
			item.money = "VND";
			//Get unit
			item.unit = edit_unit.getSelectedItem().toString();
			return 1;
		}
		return 0;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CHOOSE_DATE) {
			if (resultCode == RESULT_OK) {
				item.due_date = data.getExtras().getString(DATE_RECV_REVERSE);
				btn_due_date.setText(data.getExtras().getString(DAY_RECV)
						+ ", " +data.getExtras().getString(DATE_RECV));
			} else {
				Calendar cal = Calendar.getInstance();
				String s = "";
				try {
					s = format.format(formatReverse.parse(item.due_date));
					cal.setTime(formatReverse.parse(item.due_date));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				btn_due_date.setText(ListItemAdapter.getDay(cal) + ", " + s);
			}
		}
	}
	
	public boolean CheckExistsAutoName() {
		if (!item.name.equals(""))
			for (int i=0; i<autoName.size(); i++)
				if (autoName.get(i).compareToIgnoreCase(item.name) == 0)
					return true;
		return false;
	}
	
	public boolean CheckExistsAutoPlace() {
		if (!item.place.equals(""))
			for (int i=0; i<autoPlace.size(); i++)
				if (autoPlace.get(i).compareToIgnoreCase(item.place) == 0)
					return true;
		return false;
	}
	
	public void setAdapterForAutoComplete() {
		//Set adapter for auto name and place
				nameDB.openDB();
				autoName = nameDB.getAllNames();
				adapterForName = new ArrayAdapter<String>(this, R.layout.list_item, autoName);
				name.setAdapter(adapterForName);
				name.setTextColor(Color.BLACK);
				nameDB.closeDB();
				
				placeDB.openDB();
				autoPlace = placeDB.getAllPlaces();
				adapterForPlace = new ArrayAdapter<String>(this, R.layout.list_item, autoPlace); 
				place.setAdapter(adapterForPlace);
				name.setTextColor(Color.BLACK);
				placeDB.closeDB();
	}
	
	//Only call after function getData
	public void SaveAutoTextData() {
		valueAutoName = new ContentValues();
		valueAutoName.put(ShoppingDatabase.NAME, item.name);
		valueAutoName.put(ShoppingDatabase.PRICE, item.price);
		valueAutoName.put(ShoppingDatabase.UNIT, item.unit);
		
		if (!CheckExistsAutoName()) {
			nameDB.openDB();
			nameDB.insert(valueAutoName);
			nameDB.closeDB();
		} else if (state_action != -1) {
			nameDB.openDB();
			Cursor cur = nameDB.getItemFromName(item.name);
			cur.moveToFirst();
			nameDB.update(cur.getInt(0), valueAutoName);
		}
		
		if (!CheckExistsAutoPlace()) {
			valueAutoPlace = new ContentValues();
			valueAutoPlace.put(ShoppingDatabase.PLACE, place
					.getText().toString());
			
			placeDB.openDB();
			placeDB.insert(valueAutoPlace);
			placeDB.closeDB();
		}
	}
	
	public void SaveorCreate() {
		if (getData() == 1) {
			ContentValues valuesProduct = new ContentValues();
			valuesProduct.put(ShoppingDatabase.NAME, item.name);
			valuesProduct.put(ShoppingDatabase.PRIO, item.priority);
			valuesProduct.put(ShoppingDatabase.QUANT, item.quantity);
			valuesProduct.put(ShoppingDatabase.UNIT, item.unit);
			valuesProduct.put(ShoppingDatabase.PRICE, item.price);
			valuesProduct.put(ShoppingDatabase.STATUS, item.status);
			valuesProduct.put(ShoppingDatabase.DUE, item.due_date);
			valuesProduct.put(ShoppingDatabase.MONEY, item.money);
			valuesProduct.put(ShoppingDatabase.PLACE, item.place);
			valuesProduct.put(ShoppingDatabase.ALARM, item.alarm);
			
			if (state_action == -1) {
				shoppingDB.openDB();
				shoppingDB.insert(valuesProduct);
				shoppingDB.closeDB();
			} else {
				shoppingDB.openDB();
				shoppingDB.update(state_action, valuesProduct);
				shoppingDB.closeDB();
				
				finish();
			}
			
			SaveAutoTextData();
			setAdapterForAutoComplete();
			
			//Set empty UI
			name.setText("");
			edit_price.setText("");
			edit_quantity.setText("");
			total.setText("");
			toggle_priority.setChecked(false);
			toggle_priority.setCompoundDrawablesWithIntrinsicBounds(R.drawable.light_no_important, 0, 0, 0);
			toggle_priority.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
			Toast.makeText(getApplicationContext(),
					"Sản phẩm đã được lưu!",
					Toast.LENGTH_SHORT).show();
			
		} else 
			Toast.makeText(getApplicationContext(),
					"Bạn chưa nhập tên sản phẩm. Vui lòng thử lại!",
					Toast.LENGTH_SHORT).show();
	}
	
	
}