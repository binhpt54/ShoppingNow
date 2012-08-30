package com.vinova_g12.shoppingnow_app;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;

import kankan.wheel.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "ParserError", "ParserError" }) public class ChooseDifferentDate extends Activity{
	private TextView selected;
	private Button btn_done;
	private Button btn_cancel;
	private boolean wheelScrool = false;
	private boolean wheelChange = false;
	private WheelView month;
	private WheelView year;
	private WheelView day;
	private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	private Calendar calTemp = Calendar.getInstance();
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wheel_choose_date);
		
		month = (WheelView) findViewById(R.id.month);
	    year = (WheelView) findViewById(R.id.year);
	    day = (WheelView) findViewById(R.id.day);
	    
	    btn_cancel = (Button) findViewById(R.id.choose_cancel);
	    btn_done = (Button) findViewById(R.id.choose_done);
	    selected = (TextView) findViewById(R.id.date_selected);
	    
	    btn_cancel.setTypeface(MyTypeFace_Roboto.Roboto_Thin(getApplicationContext()));
	    btn_done.setTypeface(MyTypeFace_Roboto.Roboto_Thin(getApplicationContext()));
	    selected.setTypeface(MyTypeFace_Roboto.Roboto_Medium(getApplicationContext()));
	    
	    Calendar calendar = Calendar.getInstance();
		intent = this.getIntent();
	    
		OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            		Calendar calendar = Calendar.getInstance();
            		int curyear = calendar.get(Calendar.YEAR);
            		Log.d("NAM", year.getCurrentItem() + curyear + "");
            		calendar.set(year.getCurrentItem() + curyear,
            				month.getCurrentItem(), day.getCurrentItem());
	                selected.setText(format.format(calendar.getTime()));
            }
        };

        // month
        int curMonth = calendar.get(Calendar.MONTH);
        String months[] = new String[] {"Tháng Một", "Tháng Hai", "Tháng Ba", "Tháng Tư", "Tháng Năm",
                "Tháng Sáu", "Tháng Bảy", "Tháng Tám", "Tháng Chín", "Tháng Mười", "Tháng Mười Một", "Tháng Mười Hai"};
        month.setViewAdapter(new DateArrayAdapter(this, months, curMonth));
        month.setCurrentItem(curMonth);
        month.addChangingListener(listener);
 
        // year
        int curYear = calendar.get(Calendar.YEAR);
        year.setViewAdapter(new DateNumericAdapter(this, curYear, curYear + 10, 0));
        year.setCurrentItem(curYear);
        year.addChangingListener(listener);
        
        //day
        updateDays(year.getCurrentItem(), month.getCurrentItem());
        day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        
        OnWheelChangedListener dateChangedListener = new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				Calendar calendar = Calendar.getInstance();
        		int curyear = calendar.get(Calendar.YEAR);
        		calendar.set(year.getCurrentItem() + curyear,
        				month.getCurrentItem(), day.getCurrentItem() + 1);
                selected.setText(format.format(calendar.getTime()));
			}
		};
		day.addChangingListener(dateChangedListener);
		
		OnWheelClickedListener wheelClicked = new OnWheelClickedListener() {
			
			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
				wheel.setCurrentItem(itemIndex, true);
			}
		};
		day.addClickingListener(wheelClicked);
		month.addClickingListener(wheelClicked);
		year.addClickingListener(wheelClicked);
		
		OnWheelScrollListener wheelScrooler = new OnWheelScrollListener() {
			
			@Override
			public void onScrollingStarted(WheelView wheel) {
				wheelScrool = true;	
			}
			
			@Override
			public void onScrollingFinished(WheelView wheel) {
				updateDays(year.getCurrentItem(), month.getCurrentItem());
				wheelScrool = false;
			}
		};
		
		month.addScrollingListener(wheelScrooler);
		year.addScrollingListener(wheelScrooler);
		
		OnClickListener doneListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selected.getText().toString().equals("Chọn Ngày"))
					Toast.makeText(getApplicationContext(), "Bạn chưa chọn ngày khác!" +
							"Hãy thử chọn lại!", 0).show();
				else {
					intent.putExtra(ActivityChooseDate.DAY_DIFFERENT, selected.getText().toString());
					setResult(RESULT_OK, intent);
					finish();
				}
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
    
    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(int Curyear, int Curmonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR) + year.getCurrentItem(), month.getCurrentItem(),1);
        int maxDays;
        maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }
    
    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
    // Index of current item
    int currentItem;
    // Index of item to be highlighted
    int currentValue;
    
    /**
     * Constructor
     */
    public DateArrayAdapter(Context context, String[] items, int current) {
        super(context, items);
        this.currentValue = current;
        setTextSize(16);
    }
    
    @Override
    protected void configureTextView(TextView view) {
        super.configureTextView(view);
        if (currentItem == currentValue) {
            view.setTextColor(0xFF0000F0);
        }
        view.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
    }
    
    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        currentItem = index;
        return super.getItem(index, cachedView, parent);
    }
	}
	

}
