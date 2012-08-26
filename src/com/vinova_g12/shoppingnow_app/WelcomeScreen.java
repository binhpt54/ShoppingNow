package com.vinova_g12.shoppingnow_app;

import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;

import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class WelcomeScreen extends Activity {
	private TextView vinova;
	private TextView shopping;
	private TextView now;
	private TextView muasamhieuqua;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        vinova = (TextView) findViewById(R.id.welcome_title4);
        shopping = (TextView) findViewById(R.id.welcome_title1);
        now = (TextView) findViewById(R.id.welcome_title2);
        muasamhieuqua = (TextView) findViewById(R.id.welcome_title3);
        
        vinova.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
        shopping.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
        now.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getApplicationContext()));
        muasamhieuqua.setTypeface(MyTypeFace_Roboto.Roboto_Medium(getApplicationContext()));
    }
    
    public void onStart()
    {
    	super.onStart();
        Thread delay = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {e.printStackTrace();}
				Thread launch = new Thread (new Runnable() {
					public void run() {
						Intent main = new Intent(WelcomeScreen.this,MainActivity.class);
			    		startActivity(main);
					}
				}); // runnable
	    		launch.start();
	    		finish();
		//=================================================================================================//
			} // run
		}); // End of Runnable
        delay.start();
    }

    
}
