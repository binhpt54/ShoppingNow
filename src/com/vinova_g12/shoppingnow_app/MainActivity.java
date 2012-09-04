package com.vinova_g12.shoppingnow_app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
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
import com.vinova_g12.shoppingnow.data.ListItem;
import com.vinova_g12.shoppingnow.data.ListItemAdapter;
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
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
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

@SuppressLint({ "ParserError", "NewApi", "ParserError" })
public class MainActivity extends SherlockFragmentActivity implements
		ActionBar.OnNavigationListener {

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
	// Count of checked
	public static int countChecked;
	private int posSorted = -1;
	public static List<Integer> list_item_checked;
	public static final String REQUEST_CREATE = "create";
	public static final String REQUEST_EDIT = "edit";
	public static final int REQUEST_SORT = 90;
	public static final String REQUEST_ORDEYBY = "orderby";
	public static final String POSITION_SORTED = "position";
	public static final int REQUEST_SHARE_CONTENT = 9;

	SimpleDateFormat formatAlarm = new SimpleDateFormat("HH:mm dd-MM-yyyy");
	SimpleDateFormat formatAlarmReverse = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	String[] categoryWeek = new String[] { "Tuần Trước", "Tuần Này", "Tuần Sau" };
	String[] categoryDate = new String[] { "Hôm Qua", "Hôm Nay", "Ngày Mai" };
	String[] categoryAlarm = new String[] { "Hẹn Giờ" };
	String[] categorySearch = new String[] { "Tìm Kiếm" };
	String[] categoryViewPlace = new String[] { "Cùng Địa Điểm" };
	/*
	 * State of view is 1 if view by date is 2 if view by week is 3 if view by
	 * alram
	 */
	public static int stateView = 0;
	public static int stateSearch = 0;
	public static int alarmView = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);

		list_item_checked = new ArrayList<Integer>();
		countChecked = 0;

		// Setting actionbar
		ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowTitleEnabled(false);

		db = new ShoppingDatabase(getApplicationContext());

		// Setting list navigation
		Context context = getSupportActionBar().getThemedContext();
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(
				context, R.array.sections, R.layout.sherlock_spinner_item);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(list, this);

		mAdapter = new FragmentTitleAdapter_Viewbydate(this,
				getSupportFragmentManager(), categoryDate);
		alarmView = 0;
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerWeek = (ViewPager) findViewById(R.id.pager1);
		mPagerAlarm = (ViewPager) findViewById(R.id.pager2);
		mPagerSearch = (ViewPager) findViewById(R.id.pager3);
		mPager.setAdapter(mAdapter);
		// Setting and bind viewpager with indicator
		mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		mIndicator.setTextSize(21);
		mIndicator.setViewPager(mPager);
		mIndicator.setCurrentItem(1);
		mIndicator.setTypeface(MyTypeFace_Roboto
				.Roboto_Regular(getApplicationContext()));
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
		com.actionbarsherlock.view.MenuItem item = (com.actionbarsherlock.view.MenuItem) menu
				.findItem(R.id.menu_search);
		search_bar = (EditText) item.getActionView();
		// auto Show and hide keyboard
		item.setOnActionExpandListener(new OnActionExpandListener() {

			@Override
			public boolean onMenuItemActionExpand(
					com.actionbarsherlock.view.MenuItem item) {
				search_bar.post(new Runnable() {
					public void run() {
						search_bar.requestFocusFromTouch();
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(search_bar, 0);
					}
				});
				stateSearch = 1;
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(
					com.actionbarsherlock.view.MenuItem item) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(search_bar.getWindowToken(), 0);
				search_bar.post(new Runnable() {
					public void run() {
						search_bar.setText("");
						search_bar.clearFocus();
					}
				});
				stateSearch = 0;
				setPagerView(stateView - 1);
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
			mAdapter = new FragmentTitleAdapter_Viewbydate(this,
					getSupportFragmentManager(), categorySearch);
			mPagerSearch.setAdapter(mAdapter);
			mPager.setVisibility(View.GONE);
			mPagerAlarm.setVisibility(View.GONE);
			mPagerWeek.setVisibility(View.GONE);
			mPagerSearch.setVisibility(View.VISIBLE);
			mIndicator.setViewPager(mPagerSearch);
			mIndicator.setCurrentItem(0);

			TextWatcher searchChangedListener = new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					mAdapter.notifyDataSetChanged();

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			};
			search_bar.addTextChangedListener(searchChangedListener);
			stateSearch = 1;
			break;
		case R.id.menu_sort:
			Intent intentSort = new Intent(getApplicationContext(),
					ActivitySort.class);
			Bundle bundle = new Bundle();
			bundle.putInt(POSITION_SORTED, posSorted);
			intentSort.putExtras(bundle);
			startActivityForResult(intentSort, REQUEST_SORT);

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
			mAdapter = new FragmentTitleAdapter_Viewbydate(this,
					getSupportFragmentManager(), categoryDate);
			stateView = 1;
			alarmView = 0;
			mPager.setAdapter(mAdapter);
			mPager.setVisibility(View.VISIBLE);
			mPagerAlarm.setVisibility(View.GONE);
			mPagerWeek.setVisibility(View.GONE);
			mPagerSearch.setVisibility(View.GONE);
			mIndicator.setViewPager(mPager);
		} else if (itemPosition == 1) {
			mAdapter = new FragmentTitleAdapter_Viewbydate(this,
					getSupportFragmentManager(), categoryWeek);
			stateView = 2;
			alarmView = 0;
			mPagerWeek.setAdapter(mAdapter);
			mPager.setVisibility(View.GONE);
			mPagerAlarm.setVisibility(View.GONE);
			mPagerWeek.setVisibility(View.VISIBLE);
			mPagerSearch.setVisibility(View.GONE);
			mIndicator.setViewPager(mPagerWeek);
		} else if (itemPosition == 2) {
			mAdapter = new FragmentTitleAdapter_Viewbydate(this,
					getSupportFragmentManager(), categoryAlarm);
			alarmView = 1;
			mPagerAlarm.setAdapter(mAdapter);
			mPager.setVisibility(View.GONE);
			mPagerAlarm.setVisibility(View.VISIBLE);
			mPagerWeek.setVisibility(View.GONE);
			mPagerSearch.setVisibility(View.GONE);
			mIndicator.setViewPager(mPagerAlarm);
		}

		// Setting and bind viewpager with indicator
		if (itemPosition == 2)
			mIndicator.setCurrentItem(0);
		else
			mIndicator.setCurrentItem(1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_SORT) {
			if (resultCode == RESULT_OK) {
				orderBy = data.getExtras().getString(
						MainActivity.REQUEST_ORDEYBY);
				Log.d("ORDERBY", orderBy);
				posSorted = data.getExtras().getInt(
						MainActivity.POSITION_SORTED);
				Log.d("ORDERBY", posSorted + "");
				mAdapter.SetorderBy(orderBy);
				mAdapter.notifyDataSetChanged();
			}
		} else if (requestCode == AddNew.ALARM_REQUEST) {
			if (resultCode == RESULT_OK) {
				String alarm_date = data.getExtras().getString("alarm_date");
				Log.w("ALARM DATE", alarm_date);
				Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(formatAlarm.parse(alarm_date));
					// data.get(checked).alarm =
					// formatAlarmReverse.format(cal.getTime());
					db.openDB();
					for (int i = 0; i < list_item_checked.size(); i++) {
						ContentValues value = new ContentValues();
						value.put(ShoppingDatabase.ALARM,
								formatAlarmReverse.format(cal.getTime()));
						db.update(list_item_checked.get(i), value);
					}
					db.closeDB();
					Toast.makeText(
							this,
							list_item_checked.size()
									+ " sản phẩm đã được hẹn giờ vào lúc : "
									+ alarm_date, 1).show();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (requestCode == REQUEST_SHARE_CONTENT) {
			if (resultCode == RESULT_OK) {
				db.openDB();
				String contentShare = data.getExtras().getString("content");
				String share = "";
				int id;
				List<Integer> temp = new ArrayList<Integer>();
				if (contentShare.contains("ƯuTiên")) {
					share += "Đồ Cần Mua :";
					for (int i = 0; i < list_item_checked.size(); i++) {
						share += "\n * ";
						id = list_item_checked.get(i).intValue();
						Log.w("id getItem", id + "");
						Cursor cur = db.getItem(id);
						cur.moveToFirst();
						ListItem item = new ListItem(cur);
						if (contentShare.contains("Tên"))
							share += item.name;
						if (contentShare.contains("Giá"))
							share = share
									+ "-"
									+ ListItemAdapter.optimizePrice(item.price
											+ "", "") + "VND/" + item.unit;
						if (contentShare.contains("SốLượng"))
							share = share
									+ "-"
									+ ListItemAdapter
											.optimizeQuantity(item.quantity
													+ "") + " " + item.unit;
						if (contentShare.contains("ĐịaChỉ"))
							share = share + "- Tại :" + item.place;
						temp.add(Integer.valueOf(id));
					}

					for (int i = 0; i < list_item_checked.size(); i++) {
						id = list_item_checked.get(i).intValue();
						Log.w("id getItem", id + "");
						if (!temp.contains(Integer.valueOf(id))) {
							share += "\n + ";
							Cursor cur = db.getItem(id);
							cur.moveToFirst();
							ListItem item = new ListItem(cur);
							if (contentShare.contains("Tên"))
								share += item.name;
							if (contentShare.contains("Giá"))
								share = share
										+ "-"
										+ ListItemAdapter.optimizePrice(
												item.price + "", "") + "VND/"
										+ item.unit;
							if (contentShare.contains("SốLượng"))
								share = share
										+ "-"
										+ ListItemAdapter
												.optimizeQuantity(item.quantity
														+ "") + " " + item.unit;
							if (contentShare.contains("ĐịaChỉ"))
								share = share + "- Tại :" + item.place;
						}

					}
				} else {
					for (int i = 0; i < list_item_checked.size(); i++) {
						id = list_item_checked.get(i).intValue();
						Log.w("id getItem", id + "");
						Cursor cur = db.getItem(id);
						cur.moveToFirst();
						ListItem item = new ListItem(cur);
						if (contentShare.contains("Tên"))
							share += item.name;
						if (contentShare.contains("Giá"))
							share = share
									+ "-"
									+ ListItemAdapter.optimizePrice(item.price
											+ "", "") + "VND/" + item.unit;
						if (contentShare.contains("SốLượng"))
							share = share
									+ "-"
									+ ListItemAdapter
											.optimizeQuantity(item.quantity
													+ "") + " " + item.unit;
						if (contentShare.contains("ĐịaChỉ"))
							share = share + "- Tại :" + item.place;
					}
				}
				share = share + "\n\n" + "Gửi bởi ShoppingNow trên Android";
				Intent intentChosseShare = new Intent(Intent.ACTION_SEND);
				intentChosseShare.setType("text/plain");
				intentChosseShare.putExtra(Intent.EXTRA_TEXT, share);
				startActivity(Intent.createChooser(intentChosseShare,
						"Chia sẻ bằng : "));

				db.closeDB();
			}
		}
	}

	public void setViewPlaceFragment(String place) {
		mAdapter = new FragmentTitleAdapter_Viewbydate(place, this,
				getSupportFragmentManager(), categoryViewPlace);
		mPagerSearch.setAdapter(mAdapter);
		mPager.setVisibility(View.GONE);
		mPagerAlarm.setVisibility(View.GONE);
		mPagerWeek.setVisibility(View.GONE);
		mPagerSearch.setVisibility(View.VISIBLE);
		mIndicator.setViewPager(mPagerSearch);
		mIndicator.setCurrentItem(0);
	}
}
