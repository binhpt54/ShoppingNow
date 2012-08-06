package com.vinova_g12.shoppingnow_app;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.shoppingnow.R;
import com.example.shoppingnow.R.layout;
import com.example.shoppingnow.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.support.v4.app.NavUtils;

public class AddNew extends Activity {
	LinearLayout moreOption;
	Button btn_more_option;
	private Button btn_create;
	private Button btn_cancel;
	private Button btn_set_duedate;
	private Button btn_set_alarm;
	//Due date
	private int mYear, mMonth, mDay;
	//Alarm date
	private int aYear, aMonth, aDay;
	private Button btn_cancel_duedate;
	private Button btn_cancel_alarm;
	Calendar today = Calendar.getInstance();
	SimpleDateFormat format;
	DatePickerDialog.OnDateSetListener datePickerListener;
	DatePickerDialog.OnDateSetListener alarmDatePickerListener;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_new_products);
        moreOption = (LinearLayout) findViewById(R.id.layout_more_option);
        btn_more_option = (Button) findViewById(R.id.btn_more_option);
        btn_set_duedate = (Button) findViewById(R.id.edit_new_duedate);
        btn_set_alarm = (Button) findViewById(R.id.edit_new_alarm);
        btn_create = (Button) findViewById(R.id.btn_create);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel_duedate = (Button) findViewById(R.id.btn_cancel_duedate);
        btn_cancel_alarm = (Button) findViewById(R.id.btn_cancel_alarm);
        
        /*Get current day and format date*/
        format = new SimpleDateFormat("dd-MM-yyyy");
        mYear = today.get(Calendar.YEAR);
        mMonth = today.get(Calendar.MONTH);
        mDay = today.get(Calendar.DAY_OF_MONTH);
        aYear = aMonth = aDay = 0;
        
        /*Display moreoption when user press on button More Options*/
        btn_more_option.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (moreOption.getVisibility() == View.GONE){
					moreOption.setVisibility(View.VISIBLE);
					btn_more_option.setText("Ẩn Thông Tin");
				}
				else {
					moreOption.setVisibility(View.GONE);
					btn_more_option.setText("Thêm Thông Tin");
				}
				
			}
		});
        
        /*Display dialog to picker due date */
        datePickerListener = new DatePickerDialog.OnDateSetListener() {
    		
    		@Override
    		public void onDateSet(DatePicker view, int year, int monthOfYear,
    				int dayOfMonth) {
    			mYear = year;
    			mMonth = monthOfYear;
    			mDay = dayOfMonth;
    			btn_set_duedate.setText(""+mDay+"-"+mMonth+"-"+mYear);
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
				new DatePickerDialog(AddNew.this, datePickerListener,mYear,mMonth,mDay).show();		
			}
		});
        
        /*Display dialog to picker alarm date */
        alarmDatePickerListener = new DatePickerDialog.OnDateSetListener() {
    		
    		@Override
    		public void onDateSet(DatePicker view, int year, int monthOfYear,
    				int dayOfMonth) {
    			aYear = year;
    			aMonth = monthOfYear;
    			aDay = dayOfMonth;
    			btn_set_alarm.setText(""+aDay+"-"+aMonth+"-"+aYear);
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
				new DatePickerDialog(AddNew.this, alarmDatePickerListener,aYear,aMonth,aDay).show();		
			}
		});
    }


}
