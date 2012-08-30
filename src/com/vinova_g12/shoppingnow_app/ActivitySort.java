package com.vinova_g12.shoppingnow_app;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockListActivity;
import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.data.AdapterSortDialog;
import com.vinova_g12.shoppingnow.data.ShoppingDatabase;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

public class ActivitySort extends Activity{
	private Button btn_done;
	private Button btn_cancel;
	private ToggleButton btn_toggle;
	private RadioGroup radioGroup;
	private RadioButton sort_by_name;
	private RadioButton sort_by_pri;
	private RadioButton sort_by_total;
	private RadioButton sort_by_done;
	private RadioButton sort_by_status;
	private String sort = " asc";
	private String orderBy = "";
	private Intent intent;
	private int posSelected = -1;
	public static final String ASC = " desc";
	public static final String DESC = " asc";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sort);
		
		this.setTitleColor(Color.parseColor("#EFc76508"));
		
		btn_done = (Button) findViewById(R.id.sort_done);
		btn_cancel = (Button) findViewById(R.id.sort_cancel);
		btn_toggle = (ToggleButton) findViewById(R.id.btn_up_or_down);
		radioGroup = (RadioGroup) findViewById(R.id.group_sort);
		sort_by_name = (RadioButton) findViewById(R.id.sort_by_name);
		sort_by_pri = (RadioButton) findViewById(R.id.sort_by_priority);
		sort_by_total = (RadioButton) findViewById(R.id.sort_by_total);
		sort_by_done = (RadioButton) findViewById(R.id.sort_by_donedate);
		sort_by_status = (RadioButton) findViewById(R.id.sort_by_status);
		btn_toggle.setSelected(false);
		
		sort_by_done.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		sort_by_name.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		sort_by_pri.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		sort_by_total.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		sort_by_status.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
		
		btn_done.setTypeface(MyTypeFace_Roboto.Roboto_Thin(getApplicationContext()));
		btn_cancel.setTypeface(MyTypeFace_Roboto.Roboto_Thin(getApplicationContext()));
		intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		posSelected = bundle.getInt(MainActivity.POSITION_SORTED);
		Log.d("POSITION SELECTED", posSelected + "");
		switch (posSelected) {
		case 0:
			sort_by_name.setChecked(true);
			break;
		case 1:
			sort_by_pri.setChecked(true);
			break;
		case 2:
			sort_by_total.setChecked(true);
			break;
		case 3:
			sort_by_done.setChecked(true);
		case 4:
			sort_by_status.setChecked(true);
			break;

		default:
			sort_by_name.setChecked(true);
			break;
		}
		
		OnClickListener cancelListener = new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED, intent);
				finish();
			}
		};
		btn_cancel.setOnClickListener(cancelListener);
		
		OnClickListener toggleListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (sort.equals(ASC)) {
					sort = DESC;
				} else
					sort = ASC;
				
			}
		};
		btn_toggle.setOnClickListener(toggleListener);
		
		OnClickListener doneListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int radioId = radioGroup.getCheckedRadioButtonId();
				if (radioId == sort_by_name.getId()) {
					orderBy = ShoppingDatabase.NAME + sort;
					posSelected = 0;
				}
				else if (radioId == sort_by_pri.getId()) {
					orderBy = ShoppingDatabase.PRIO + sort;
					posSelected = 1;
				}
				else if (radioId == sort_by_total.getId()) {
					orderBy = ShoppingDatabase.PRICE + "*" + ShoppingDatabase.QUANT + sort;
					posSelected = 2;
				}
				else if (radioId == sort_by_done.getId()) {
					orderBy = ShoppingDatabase.DONE_DATE + sort;
					posSelected = 3;
				}
				else if (radioId == sort_by_status.getId()) {
					orderBy = ShoppingDatabase.STATUS + sort;
					posSelected = 4;
				}	
				intent.putExtra(MainActivity.REQUEST_ORDEYBY, orderBy);
				intent.putExtra(MainActivity.POSITION_SORTED, posSelected);
				setResult(RESULT_OK, intent);
				finish();
			}
		};
		btn_done.setOnClickListener(doneListener);
	}
	
}
