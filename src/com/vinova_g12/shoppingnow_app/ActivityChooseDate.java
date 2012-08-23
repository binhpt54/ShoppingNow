package com.vinova_g12.shoppingnow_app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.actionbarsherlock.app.SherlockListActivity;
import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.data.ItemChooseDate;
import com.vinova_g12.shoppingnow.data.ListItemAdapter;
import com.vinova_g12.shoppingnow.data.ListItemChooseDate;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ActivityChooseDate extends ListActivity{
	private Calendar today = Calendar.getInstance();
	private SimpleDateFormat formatReverse = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	private List<ItemChooseDate> data;
	public static String dateRecv;
	private ListItemChooseDate adapter;
	private Intent intentRecv;
	private Button btn_custom_due_date;
	private Button btn_save;
	private Button btn_cancel;
	private String dayBack, dateBack, dateBackReverse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_duedate);
		
		btn_save = (Button) findViewById(R.id.btn_save_duedate);
		btn_cancel = (Button) findViewById(R.id.btn_cancel_duedate);
		
		btn_save.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		btn_cancel.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		
		today.setFirstDayOfWeek(Calendar.MONDAY);
		
		dayBack = "Hôm Nay";
		dateBack = format.format(today.getTime());
		dateBackReverse = formatReverse.format(today.getTime());
		
		//Get bundle from intent
		intentRecv = this.getIntent();
		Bundle bundle = intentRecv.getExtras();
		dateRecv = bundle.getString("dateSender");
		//Set behavior for btn save and cancel
		OnClickListener saveListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				intentRecv.putExtra(AddNew.DATE_RECV, dateBack);
				intentRecv.putExtra(AddNew.DATE_RECV_REVERSE, dateBackReverse);
				intentRecv.putExtra(AddNew.DAY_RECV, dayBack);
				setResult(RESULT_OK, intentRecv);
				finish();
			}
		};
		btn_save.setOnClickListener(saveListener);
		
		OnClickListener cancelListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED, intentRecv);
				finish();
			}
		};
		btn_cancel.setOnClickListener(cancelListener);
		//Get 7 date after
		getDateFromToday();
		//Create adapter
		adapter = new ListItemChooseDate(this, R.layout.list_item_choose_duedate, data);
		setListAdapter(adapter);
		//Set footer view
		View footer = View.inflate(this, R.layout.list_choose_date_footer, null);
		getListView().addFooterView(footer);
		btn_custom_due_date = (Button) footer.findViewById(R.id.btn_custom_duedate);
		btn_custom_due_date.setTypeface(MyTypeFace_Roboto.Roboto_Black(getApplicationContext()));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		dateRecv = data.get(position).date;
		dayBack = data.get(position).title;
		dateBack = data.get(position).date;
		dateBackReverse = data.get(position).dateReverse;
		adapter.notifyDataSetChanged();
	}
	
	
	public void getDateFromToday() {
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		data = new ArrayList<ItemChooseDate>();
		for (int i=0; i<7; i++) {
			ItemChooseDate item = new ItemChooseDate();
			item.date = format.format(cal.getTime());
			item.dateReverse = formatReverse.format(cal.getTime());
			if (cal.get(Calendar.DAY_OF_WEEK) == today.get(Calendar.DAY_OF_WEEK))
				item.title = "Hôm Nay";
			else {
				today.add(Calendar.DAY_OF_WEEK, 1);
				if (cal.get(Calendar.DAY_OF_WEEK) == today.get(Calendar.DAY_OF_WEEK))
					item.title = "Ngày Mai";
				else {
					today.add(Calendar.DAY_OF_WEEK, 1);
					if (cal.get(Calendar.DAY_OF_WEEK) == today.get(Calendar.DAY_OF_WEEK))
						item.title = "Ngày Kia";
					else 
						item.title = ListItemAdapter.getDay(cal);
				}
			}
			today = Calendar.getInstance();
			data.add(item);
			cal.add(Calendar.DAY_OF_WEEK, 1);
		}
	}
}
