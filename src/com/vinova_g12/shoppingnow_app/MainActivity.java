package com.vinova_g12.shoppingnow_app;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.example.shoppingnow.R;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.vinova_g12.shoppingnow.data.RepoData;
import com.vinova_g12.shoppingnow.data.ShoppingDatabase;
import com.vinova_g12.shoppingnow.fragment.FragmentAdapter_Viewbydate;
import com.vinova_g12.shoppingnow.fragment.FragmentTitleAdapter_Viewbydate;
import com.vinova_g12.shoppingnow.fragment.Fragment_ViewbyDate;
import com.vinova_g12.shoppingnow.quickaction.QuickAction;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;

@SuppressLint({ "ParserError", "NewApi" })
public class MainActivity extends SherlockFragmentActivity implements ActionBar.OnNavigationListener{

	private FragmentAdapter_Viewbydate mAdapter;
    public ViewPager mPager;
    public ViewPager mPagerWeek;
    public ViewPager mPagerAlarm;
    public ViewPager mPagerSearch;
    private String orderBy;
    public TitlePageIndicator mIndicator;
    private ViewPager.OnPageChangeListener pageListener;
    private ActionBar ab;
    private ShoppingDatabase db;
    public static EditText search_bar;
    //Count of checked
    public static int countChecked;
  	public static List<Integer> list_item_checked;
  	public static String REQUEST_CREATE = "create";
  	public static String REQUEST_EDIT = "edit";
  	
  	String[] categoryWeek = new String[] {"Tuần Trước", "Tuần Này", "Tuần Sau"};
  	String[] categoryDate = new String[] {"Hôm Qua", "Hôm Nay", "Ngày Mai"};
  	String[] categoryAlarm = new String[] {"Nhắc Nhở"};
  	String[] categorySearch = new String[] {"Tìm Kiếm"};
  	/*State of view
  	 * is 1 if view by date
  	 * is 2 if view by week
  	 * is 3 if view by alram*/
  	public static int stateView = 0;
  	public static int stateSearch = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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

        mPager = (ViewPager)findViewById(R.id.pager);
        mPagerWeek = (ViewPager) findViewById(R.id.pager1);
        mPagerAlarm = (ViewPager) findViewById(R.id.pager2);
        mPagerSearch = (ViewPager) findViewById(R.id.pager3);
        mPager.setAdapter(mAdapter);
        //Setting and bind viewpager with indicator
        mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
        mIndicator.setTextSize(27);
        mIndicator.setViewPager(mPager);
        mIndicator.setCurrentItem(1);
        mIndicator.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getApplicationContext()));
        mIndicator.setTitlePadding(100);
        pageListener = new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				Fragment_ViewbyDate.category_date_in_week.clear();
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		};
        mIndicator.setOnPageChangeListener(pageListener);
    }
    

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		com.actionbarsherlock.view.MenuItem item = (com.actionbarsherlock.view.MenuItem) menu.findItem(R.id.menu_search);
		search_bar = (EditText)item.getActionView();
		item.setOnActionExpandListener(new OnActionExpandListener() {
			
			@Override
			public boolean onMenuItemActionExpand(
					com.actionbarsherlock.view.MenuItem item) {
				search_bar.post(new Runnable() { 
                    public void run() { 
                        search_bar.requestFocusFromTouch(); 
                        InputMethodManager imm = 
                        		(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
                        imm.showSoftInput(search_bar, 0); 
                    } 
				}); 
				stateSearch = 1;
				return true; 
			}
			
			@Override
			public boolean onMenuItemActionCollapse(
					com.actionbarsherlock.view.MenuItem item) {
				InputMethodManager imm = 
						(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
						                                imm.hideSoftInputFromWindow(search_bar.getWindowToken(), 0); 
						                                search_bar.post(new Runnable() { 
						                                        public void run() { 
						                                        	search_bar.setText("");
						                                                search_bar.clearFocus(); 
						                                        } 
						                                }); 
						                                stateSearch = 0;
						                                setPagerView(stateView-1);
						                                return true; 
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_create:
			Intent intent = new Intent(getApplicationContext(), AddNew.class);
			Bundle update = new Bundle();
			update.putInt(REQUEST_CREATE, -1);
			intent.putExtras(update);
			startActivity(intent);
			break;
		case R.id.menu_search:
			mAdapter = new FragmentTitleAdapter_Viewbydate(getSupportFragmentManager(), categorySearch);
			mPagerSearch.setAdapter(mAdapter);
			mPager.setVisibility(View.GONE);
			mPagerAlarm.setVisibility(View.GONE);
			mPagerWeek.setVisibility(View.GONE);
			mPagerSearch.setVisibility(View.VISIBLE);
			mIndicator.setViewPager(mPagerSearch);
			mIndicator.setCurrentItem(0);
			stateSearch = 1;
			break;
		case R.id.menu_orderby_alphabet:
			orderBy = ShoppingDatabase.NAME;
			mAdapter.SetorderBy(orderBy);
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.menu_orderby_priority:
			orderBy = ShoppingDatabase.PRIO;
			mAdapter.SetorderBy(orderBy);
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.menu_orderby_done_date:
			orderBy = ShoppingDatabase.DONE_DATE;
			mAdapter.SetorderBy(orderBy);
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.menu_orderby_status:
			orderBy = ShoppingDatabase.STATUS;
			mAdapter.SetorderBy(orderBy);
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.menu_orderby_total:
			orderBy = ShoppingDatabase.PRICE + "*" + ShoppingDatabase.QUANT;
			mAdapter.SetorderBy(orderBy);
			mAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		setPagerView(itemPosition);
		return true;
	}
	
	public void setPagerView(int itemPosition) {
		if (itemPosition == 0) {
			mAdapter = new FragmentTitleAdapter_Viewbydate(getSupportFragmentManager(), categoryDate);
			stateView = 1;
			mPager.setAdapter(mAdapter);
			mPager.setVisibility(View.VISIBLE);
			mPagerAlarm.setVisibility(View.GONE);
			mPagerWeek.setVisibility(View.GONE);
			mPagerSearch.setVisibility(View.GONE);
			mIndicator.setViewPager(mPager);
		}
		else if (itemPosition == 1) {
			mAdapter = new FragmentTitleAdapter_Viewbydate(getSupportFragmentManager(), categoryWeek);
			stateView = 2;
			mPagerWeek.setAdapter(mAdapter);
			mPager.setVisibility(View.GONE);
			mPagerAlarm.setVisibility(View.GONE);
			mPagerWeek.setVisibility(View.VISIBLE);
			mPagerSearch.setVisibility(View.GONE);
			mIndicator.setViewPager(mPagerWeek);
		}
		else if (itemPosition == 2) {
			mAdapter = new FragmentTitleAdapter_Viewbydate(getSupportFragmentManager(), categoryAlarm);
			stateView = 3;
			mPagerAlarm.setAdapter(mAdapter);
			mPager.setVisibility(View.GONE);
			mPagerAlarm.setVisibility(View.VISIBLE);
			mPagerWeek.setVisibility(View.GONE);
			mPagerSearch.setVisibility(View.GONE);
			mIndicator.setViewPager(mPagerAlarm);
		}
        
        //Setting and bind viewpager with indicator
        if (itemPosition == 2)
        	mIndicator.setCurrentItem(0);
        else
        	mIndicator.setCurrentItem(1);
		
	}
}
