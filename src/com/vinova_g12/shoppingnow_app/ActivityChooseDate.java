package com.vinova_g12.shoppingnow_app;

import java.text.ParseException;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
	private TextView selectDifferent;
	private String dayBack, dateBack, dateBackReverse;
	public static final int REQUESR_CHOOSE_DIFFERENT = 90;
	public static final String DAY_DIFFERENT = "day_different";
	String temp = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_duedate);
		
		btn_save = (Button) findViewById(R.id.btn_save_duedate);
		btn_cancel = (Button) findViewById(R.id.btn_cancel_duedate);
		selectDifferent = (TextView) findViewById(R.id.choose_different_day);
		btn_custom_due_date = (Button) findViewById(R.id.btn_choose_diff);
		
		btn_custom_due_date.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		btn_save.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		btn_cancel.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		selectDifferent.setTypeface(MyTypeFace_Roboto.Roboto_Medium(getApplicationContext()));
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
		
		OnClickListener customListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(getApplicationContext(), ChooseDifferentDate.class);
				startActivityForResult(intent2, REQUESR_CHOOSE_DIFFERENT);
			}
		};
		btn_custom_due_date.setOnClickListener(customListener);
		
		//Get 7 date after
		getDateFromToday();
		//Create adapter
		adapter = new ListItemChooseDate(this, R.layout.list_item_choose_duedate, data);
		setListAdapter(adapter);
		LayoutAnimationController controller2 = AnimationUtils
				.loadLayoutAnimation(this,
						R.anim.list_animation_right_left);
		this.getListView().setLayoutAnimation(controller2);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		dateRecv = data.get(position).date;
		dayBack = data.get(position).title;
		dateBack = data.get(position).date;
		dateBackReverse = data.get(position).dateReverse;
		if (position != 7) {
			intentRecv.putExtra(AddNew.DATE_RECV, dateBack);
			intentRecv.putExtra(AddNew.DATE_RECV_REVERSE, dateBackReverse);
			intentRecv.putExtra(AddNew.DAY_RECV, dayBack);
			setResult(RESULT_OK, intentRecv);
			finish();
		}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == REQUESR_CHOOSE_DIFFERENT) {
			if (resultCode == RESULT_OK) {
				temp = intent.getExtras().getString(DAY_DIFFERENT);
				Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(format.parse(temp));
					dayBack = ListItemAdapter.getDay(cal);
					dateBack = temp;
					dateBackReverse = formatReverse.format(cal.getTime());

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				btn_custom_due_date.setText("Đã chọn: " + temp);
			}
		}
	}
	
	
}
