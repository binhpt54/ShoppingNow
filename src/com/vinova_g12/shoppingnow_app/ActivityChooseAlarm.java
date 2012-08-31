package com.vinova_g12.shoppingnow_app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.vinova_g12.shoppingnow.data.ListItemAdapter;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;

import kankan.wheel.R;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ActivityChooseAlarm extends Activity {
	private WheelView wheelDay;
	private WheelView wheelHour;
	private WheelView wheelMin;
	private TextView btn_custom_alarm;
	private Button btn_done;
	private Button btn_cancel;
	private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		setContentView(R.layout.alarm_activity);

		wheelDay = (WheelView) findViewById(R.id.alarm_day);
		wheelHour = (WheelView) findViewById(R.id.alarm_hour);
		wheelMin = (WheelView) findViewById(R.id.alarm_mins);
		btn_custom_alarm = (TextView) findViewById(R.id.alarm_selected);
		btn_done = (Button) findViewById(R.id.alarm_done);
		btn_cancel = (Button) findViewById(R.id.alarm_cancel);
		
		intent = this.getIntent();

		NumericWheelAdapter hourAdapter = new NumericWheelAdapter(this, 0, 23,
				"%02d");
		hourAdapter.setItemResource(R.layout.wheel_text_item);
		hourAdapter.setItemTextResource(R.id.text);
		wheelHour.setViewAdapter(hourAdapter);

		NumericWheelAdapter minAdapter = new NumericWheelAdapter(this, 0, 59,
				"%02d");
		minAdapter.setItemResource(R.layout.wheel_text_item);
		minAdapter.setItemTextResource(R.id.text);
		wheelMin.setViewAdapter(minAdapter);
		wheelMin.setCyclic(true);

		// set current time
		Calendar calendar = Calendar.getInstance();
		wheelHour.setCurrentItem(calendar.get(Calendar.HOUR));
		wheelMin.setCurrentItem(calendar.get(Calendar.MINUTE));
		DayArrayAdapter adapter = new DayArrayAdapter(this, calendar);
		wheelDay.setViewAdapter(adapter);
		
		OnClickListener doneListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_YEAR, wheelDay.getCurrentItem());
				String hour = String.format("%02d", wheelHour.getCurrentItem())
						+ ":" + String.format("%02d", wheelMin.getCurrentItem()) + " ";
				String temp = hour + format.format(cal.getTime());
				intent.putExtra("alarm_date", temp);
				setResult(RESULT_OK, intent);
				finish();
			}
		};
		btn_done.setOnClickListener(doneListener);
		
		OnClickListener cancelListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED, intent);
				finish();
			}
		};
		btn_cancel.setOnClickListener(cancelListener);
	}

	private class DayArrayAdapter extends AbstractWheelTextAdapter {
		// Count of days to be shown
		private final int daysCount = 30;
		// Calendar
		Calendar calendar;

		/**
		 * Constructor
		 */

		protected DayArrayAdapter(Context context, Calendar calendar) {
			super(context, R.layout.time2_day, NO_RESOURCE);
			this.calendar = calendar;
			setItemTextResource(R.id.time2_monthday);
		}

		@Override
		protected void configureTextView(TextView view) {
			// TODO Auto-generated method stub
			super.configureTextView(view);
			view.setTypeface(MyTypeFace_Roboto
					.Roboto_Regular(getApplicationContext()));
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			int day = index;
			Calendar newCalendar = (Calendar) calendar.clone();
			newCalendar.roll(Calendar.DAY_OF_YEAR, day);

			View view = super.getItem(index, cachedView, parent);
			TextView weekday = (TextView) view.findViewById(R.id.time2_weekday);
			if (day == 0) {
				weekday.setText("");
				weekday.setTypeface(MyTypeFace_Roboto
						.Roboto_Regular(getApplicationContext()));
			} else {
				weekday.setText(getDay(newCalendar));
				weekday.setTypeface(MyTypeFace_Roboto
						.Roboto_Regular(getApplicationContext()));
			}

			TextView monthday = (TextView) view
					.findViewById(R.id.time2_monthday);
			if (day == 0) {
				monthday.setText("Hôm Nay");
				monthday.setTypeface(MyTypeFace_Roboto
						.Roboto_Regular(getApplicationContext()));
				monthday.setTextColor(0xFF0000F0);
			} else {
				monthday.setText(getMonth(newCalendar));
				monthday.setTypeface(MyTypeFace_Roboto
						.Roboto_Regular(getApplicationContext()));
				monthday.setTextColor(0xFF111111);
			}

			return view;
		}

		@Override
		public int getItemsCount() {
			return daysCount + 1;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}
	}

	public static String getMonth(Calendar cal) {
		DateFormat format = new SimpleDateFormat("dd");
		if (cal.get(Calendar.MONTH) == Calendar.JANUARY)
			return "Tháng 1 " + format.format(cal.getTime());
		else if (cal.get(Calendar.MONTH) == Calendar.FEBRUARY)
			return "Tháng 2" + format.format(cal.getTime());
		else if (cal.get(Calendar.MONTH) == Calendar.MARCH)
			return "Tháng 3 " + format.format(cal.getTime());
		else if (cal.get(Calendar.MONTH) == Calendar.APRIL)
			return "Tháng 4 " + format.format(cal.getTime());
		else if (cal.get(Calendar.MONTH) == Calendar.MAY)
			return "Tháng 5 " + format.format(cal.getTime());
		else if (cal.get(Calendar.MONTH) == Calendar.JUNE)
			return "Tháng 6 " + format.format(cal.getTime());
		else if (cal.get(Calendar.MONTH) == Calendar.OCTOBER)
			return "Tháng 10 " + format.format(cal.getTime());
		else if (cal.get(Calendar.MONTH) == Calendar.JULY)
			return "Tháng 7 " + format.format(cal.getTime());
		else if (cal.get(Calendar.MONTH) == Calendar.AUGUST)
			return "Tháng 8 " + format.format(cal.getTime());
		else if (cal.get(Calendar.MONTH) == Calendar.SEPTEMBER)
			return "Tháng 9 " + format.format(cal.getTime());
		else if (cal.get(Calendar.MONTH) == Calendar.NOVEMBER)
			return "Tháng 11 " + format.format(cal.getTime());
		return "Tháng 12 " + format.format(cal.getTime());
	}

	public String getDay(Calendar cal) {
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
			return "T.Hai";
		else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)
			return "T.Ba";
		else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)
			return "T.Tư";
		else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
			return "T.Năm";
		else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
			return "T.Sáu";
		else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			return "T.Bảy";
		return "C.Nhật";
	}
	

}
