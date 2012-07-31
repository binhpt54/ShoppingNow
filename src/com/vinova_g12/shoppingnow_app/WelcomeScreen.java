package com.vinova_g12.shoppingnow_app;

import com.example.shoppingnow.R;

import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.support.v4.app.NavUtils;

public class WelcomeScreen extends Activity {
	private ProgressBar progress_begin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        progress_begin = (ProgressBar) findViewById(R.id.progress_welcome);
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
