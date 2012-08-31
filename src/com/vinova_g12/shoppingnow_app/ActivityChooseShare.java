package com.vinova_g12.shoppingnow_app;

import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;

import kankan.wheel.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ParserError") public class ActivityChooseShare extends Activity {
	private TextView title;
	private CheckBox cb_name;
	private CheckBox cb_price;
	private CheckBox cb_quantity;
	private CheckBox cb_place;
	private CheckBox cb_priority;
	private Button btn_done;
	private Button btn_cancel;
	private String shareContent = "";
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_product);

		title = (TextView) findViewById(R.id.title_share);
		cb_name = (CheckBox) findViewById(R.id.share_name);
		cb_price = (CheckBox) findViewById(R.id.share_price);
		cb_quantity = (CheckBox) findViewById(R.id.share_quantity);
		cb_place = (CheckBox) findViewById(R.id.share_place);
		cb_priority = (CheckBox) findViewById(R.id.share_priority);
		btn_done = (Button) findViewById(R.id.share_done);
		btn_cancel = (Button) findViewById(R.id.share_cancel);

		cb_name.setTypeface(MyTypeFace_Roboto
				.Roboto_Regular(getApplicationContext()));
		cb_price.setTypeface(MyTypeFace_Roboto
				.Roboto_Regular(getApplicationContext()));
		cb_quantity.setTypeface(MyTypeFace_Roboto
				.Roboto_Regular(getApplicationContext()));
		cb_place.setTypeface(MyTypeFace_Roboto
				.Roboto_Regular(getApplicationContext()));
		cb_priority.setTypeface(MyTypeFace_Roboto
				.Roboto_Regular(getApplicationContext()));
		btn_cancel.setTypeface(MyTypeFace_Roboto
				.Roboto_Thin(getApplicationContext()));
		btn_done.setTypeface(MyTypeFace_Roboto
				.Roboto_Thin(getApplicationContext()));
		title.setTypeface(MyTypeFace_Roboto
				.Roboto_Bold(getApplicationContext()));

		intent = this.getIntent();

		OnClickListener doneListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDataShare();
				if (shareContent.equals(""))
					Toast.makeText(getApplicationContext(),
							"Bạn cần chọn ít nhất 1 nội dung để chia sẻ!", 0)
							.show();
				else {
					intent.putExtra("content", shareContent);
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

	public void getDataShare() {
		if (cb_name.isChecked())
			shareContent += "Tên ";
		if (cb_place.isChecked())
			shareContent += "ĐịaChỉ ";
		if (cb_price.isChecked())
			shareContent += "Giá ";
		if (cb_quantity.isChecked())
			shareContent += "SốLượng ";
		if (cb_priority.isChecked())
			shareContent += "ƯuTiên ";
	}

}
