package com.vinova_g12.shoppingnow_app;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.example.shoppingnow.R;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.vinova_g12.shoppingnow.fragment.FragmentAdapter_Viewbydate;
import com.vinova_g12.shoppingnow.fragment.FragmentTitleAdapter_Viewbydate;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;

@SuppressLint("ParserError")
public class MainActivity extends SherlockFragmentActivity implements ActionBar.OnNavigationListener{

	FragmentAdapter_Viewbydate mAdapter;
    ViewPager mPager;
    TitlePageIndicator mIndicator;
    ActionBar ab;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Setting actionbar
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(false);
        //Setting list navigation
        Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, 
        								R.array.sections, R.layout.sherlock_spinner_item);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this);
        
        mAdapter = new FragmentTitleAdapter_Viewbydate(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        
        //Setting and bind viewpager with indicator
        mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
        mIndicator.setTextSize(20);
        mIndicator.setViewPager(mPager);
        mIndicator.setCurrentItem(1);
        mIndicator.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
        mIndicator.setTitlePadding(50);
    }

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		// Set file with share history to the provider and set the share intent.
        //MenuItem actionItem = (MenuItem) menu.findItem(R.id.menu_share);
        //ShareActionProvider actionProvider = (ShareActionProvider) actionItem.getActionProvider();
        //actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        // Note that you can set/change the intent any time,
        // say when the user has selected an image.
        //actionProvider.setShareIntent(createShareIntent());		
		return super.onCreateOptionsMenu(menu);
	}
	
	

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_create:
			Intent intent = new Intent(getApplicationContext(), AddNewItem.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}
    
    
    
}
