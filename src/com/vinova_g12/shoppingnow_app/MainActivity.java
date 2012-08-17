package com.vinova_g12.shoppingnow_app;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.example.shoppingnow.R;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.vinova_g12.shoppingnow.data.ShoppingDatabase;
import com.vinova_g12.shoppingnow.fragment.FragmentAdapter_Viewbydate;
import com.vinova_g12.shoppingnow.fragment.FragmentTitleAdapter_Viewbydate;
import com.vinova_g12.shoppingnow.quickaction.QuickAction;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;

@SuppressLint("ParserError")
public class MainActivity extends SherlockFragmentActivity implements ActionBar.OnNavigationListener{

	private FragmentAdapter_Viewbydate mAdapter;
    private ViewPager mPager;
    private ViewPager mPagerWeek;
    private ViewPager mPagerAlarm;
    private TitlePageIndicator mIndicator;
    private ActionBar ab;
    private ShoppingDatabase db;
    //Count of checked
    public static int countChecked;
  	public static List<Integer> list_item_checked;
  	
  	String[] categoryWeek = new String[] {"Tuần Trước", "Tuần Này", "Tuần Sau"};
  	String[] categoryDate = new String[] {"Hôm Qua", "Hôm Nay", "Ngày Mai"};
  	String[] categoryAlarm = new String[] {"Nhắc Nhở"};
  	/*State of view
  	 * is 1 if view by date
  	 * is 2 if view by week
  	 * is 3 if view by alram*/
  	
  	
  	
  	
  	public static int stateView = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        list_item_checked = new ArrayList<Integer>();
        countChecked = 0;
        
        //Setting actionbar
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(false);
        db = new ShoppingDatabase(getApplicationContext());
       
        //Setting list navigation
        Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, 
        								R.array.sections, R.layout.sherlock_spinner_item);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this);
        
        mAdapter = new FragmentTitleAdapter_Viewbydate(getSupportFragmentManager(), categoryDate);
        mAdapter = new FragmentTitleAdapter_Viewbydate(getSupportFragmentManager(), categoryWeek);
        mAdapter = new FragmentTitleAdapter_Viewbydate(getSupportFragmentManager(), categoryAlarm);

        mPager = (ViewPager)findViewById(R.id.pager);
        mPagerWeek = (ViewPager) findViewById(R.id.pager1);
        mPagerAlarm = (ViewPager) findViewById(R.id.pager2);
        
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
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_create:
			Intent intent = new Intent(getApplicationContext(), AddNew.class);
			Bundle update = new Bundle();
			update.putInt("id", 1000);
			intent.putExtras(update);
			startActivity(intent);
			break;
		case R.id.menu_search:
			 db.openDB();
			db.search("a");
			db.closeDB();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if (itemPosition == 0) {
			mAdapter = new FragmentTitleAdapter_Viewbydate(getSupportFragmentManager(), categoryDate);
			stateView = 1;
			mPager.setAdapter(mAdapter);
			mPager.setVisibility(View.VISIBLE);
			mPagerAlarm.setVisibility(View.GONE);
			mPagerWeek.setVisibility(View.GONE);
			mIndicator.setViewPager(mPager);
		}
		else if (itemPosition == 1) {
			mAdapter = new FragmentTitleAdapter_Viewbydate(getSupportFragmentManager(), categoryWeek);
			stateView = 2;
			mPagerWeek.setAdapter(mAdapter);
			mPager.setVisibility(View.GONE);
			mPagerAlarm.setVisibility(View.GONE);
			mPagerWeek.setVisibility(View.VISIBLE);
			mIndicator.setViewPager(mPagerWeek);
		}
		else if (itemPosition == 2) {
			mAdapter = new FragmentTitleAdapter_Viewbydate(getSupportFragmentManager(), categoryAlarm);
			stateView = 3;
			mPagerAlarm.setAdapter(mAdapter);
			mPager.setVisibility(View.GONE);
			mPagerAlarm.setVisibility(View.VISIBLE);
			mPagerWeek.setVisibility(View.GONE);
			mIndicator.setViewPager(mPagerAlarm);
		}
        
        //Setting and bind viewpager with indicator
        if (itemPosition == 2)
        	mIndicator.setCurrentItem(0);
        else
        	mIndicator.setCurrentItem(1);

		return true;
		
	}
	
	
	
}
