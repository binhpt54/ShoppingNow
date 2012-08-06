package com.vinova_g12.shoppingnow_app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.data.Item;
import com.vinova_g12.shoppingnow.data.ItemsDataSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AddNewItem extends Activity implements OnClickListener, OnCheckedChangeListener, 
		OnDateSetListener, OnTimeSetListener, OnItemSelectedListener {

	private Button btadd;
	private Button btcancel;
//	private Button moreOptions;
	LinearLayout moreOption;
	Button btn_more_option;
	
	private EditText ettitle;
	private EditText etquantity;
	private EditText etprice;
	private EditText etplace;
	private EditText ettime;
	private EditText etdate;
	
	private Spinner spunit;
	private RadioGroup rgpriority;
	private RadioButton rb0;
	private RadioButton rb1;
	private RadioButton rb2;
	private RadioButton rb3;
	
	
	private String strtitle;
	private int ipriority;
	private float fquantity;
	private float fprice;
	private String strunit;
	private String strplace;
	
	private int iyear;
	private int imonth;
	private int idate;
	private long ldate;
	private long ltime;
	private String strdate;
	private String strtime;
	
	private ItemsDataSource dataSource;
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.layout_add_new_products);
        
	    btadd = (Button) findViewById(R.id.btn_create);
        btcancel = (Button) findViewById(R.id.btn_cancel);
        
        ettitle = (EditText) findViewById(R.id.edit_new_title);
        etquantity = (EditText) findViewById(R.id.edit_new_quantity);
        etprice = (EditText) findViewById(R.id.edit_new_price);
        etplace = (EditText) findViewById(R.id.edit_new_place);
        
           
        
        spunit = (Spinner) findViewById(R.id.edit_new_unit);
        /*ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(
        		this, R.array.unit_array, R.layout.unit_array_textview);
        unitAdapter.setDropDownViewResource(
        		android.R.layout.simple_spinner_dropdown_item);
        spunit.setAdapter(unitAdapter);*/
        
        rgpriority = (RadioGroup) findViewById(R.id.gradio_priority);
        rb0 = (RadioButton) findViewById(R.id.pri_normal);
        rb1 = (RadioButton) findViewById(R.id.pri_low);
        rb2 = (RadioButton) findViewById(R.id.pri_medium);
        rb3 = (RadioButton) findViewById(R.id.pri_hard);
        btadd.setOnClickListener(this);
        btcancel.setOnClickListener(this);
        
        ettime.setOnClickListener(this);
        etdate.setOnClickListener(this);
        
        spunit.setOnItemSelectedListener(this);
        rgpriority.setOnCheckedChangeListener(this);
        dataSource = new ItemsDataSource(this);
        dataSource.open();
        
        moreOption = (LinearLayout) findViewById(R.id.layout_more_option);
        btn_more_option = (Button) findViewById(R.id.btn_more_option);
        btn_more_option.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (moreOption.getVisibility() == View.GONE){
					moreOption.setVisibility(View.VISIBLE);
					btn_more_option.setText("An them thong tin");
				}
				else {
					moreOption.setVisibility(View.GONE);
					btn_more_option.setText("Them thong tin");
				}
				
			}
		});
        
        moreOption.setVisibility(View.VISIBLE);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_create:
			strtitle = ettitle.getText().toString().trim();
			Log.i("strtitle", "null");
			if(strtitle == null || strtitle.equals("") ) {
				// TODO In thong bao loi
				Log.i("strtitle", "null");
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Please Enter Item's Name!")
				       .setCancelable(false)
				       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.dismiss();
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
				
			} else {
				// insert vao csdl
				String strbuffer = etquantity.getText().toString().trim();
				if(!strbuffer.equals("") && strbuffer != null)
					fquantity = Float.parseFloat(strbuffer);
				
				strbuffer = etprice.getText().toString().trim();
				if(!strbuffer.equals("") && strbuffer != null)
					fprice = Float.parseFloat(strbuffer);
				
				strplace = etplace.getText().toString().trim();
				strdate = etdate.getText().toString().trim();
				strtime = ettime.getText().toString().trim();
				Item item = new Item(strtitle, ipriority,
						fquantity, fprice, strunit, strplace, strtime, strdate);
				dataSource.addItem(item);
			}
			break;
			
		// DONE cancel
		case R.id.btn_cancel:
			dataSource.close();
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
			break;
		
		
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// DONE Auto-generated method stub
		switch(arg1) {
		case R.id.pri_normal:
			rb0.setChecked(true);
			ipriority = 0;
			break;
		case R.id.pri_low:
			rb1.setChecked(true);
			ipriority = 1;
			break;
		case R.id.pri_medium:
			rb2.setChecked(true);
			ipriority = 2;
			break;
		case R.id.pri_hard:
			rb3.setChecked(true);
			ipriority = 3;
			break;
		}
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// DONE Auto-generated method stub
		GregorianCalendar date = new GregorianCalendar(
				iyear, imonth, idate, hourOfDay, minute);
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("HH:mm");
		ltime = date.getTimeInMillis();
		ettime.setText(sdfDateTime.format(new Date(ltime)));
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// DONE Auto-generated method stub
		iyear = year;
		imonth = monthOfYear;
		idate = dayOfMonth;
		
		GregorianCalendar date = new GregorianCalendar(iyear, imonth, idate);
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd");
		ldate = date.getTimeInMillis();
		etdate.setText(sdfDateTime.format(new Date(ldate)));
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// DONE Auto-generated method stub
		strunit = arg0.getItemAtPosition(arg2).toString();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// DONE Auto-generated method stub
		strunit = arg0.getItemAtPosition(0).toString();
	}
}
