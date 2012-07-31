package com.vinova_g12.shoppingnow_app;

import com.example.shoppingnow.R;
import com.example.shoppingnow.R.layout;
import com.example.shoppingnow.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.support.v4.app.NavUtils;

public class AddNew extends Activity {
	LinearLayout moreOption;
	Button btn_more_option;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_new_products);
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


}
