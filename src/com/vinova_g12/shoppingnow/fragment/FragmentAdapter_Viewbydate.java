package com.vinova_g12.shoppingnow.fragment;

import com.vinova_g12.shoppingnow_app.MainActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class FragmentAdapter_Viewbydate extends FragmentStatePagerAdapter{
	

	protected static String[] CONTENT = new String[] { "Hôm Qua", "Hôm Nay","Ngày Mai" };
	private int mCount = CONTENT.length;
	private String orderBy = "";
	private MainActivity activity;
	
	
	public void SetorderBy(String col) {
		orderBy = col;
	}
	public static void setContent(String[] cont) {
		CONTENT = cont;
	}
	
	public FragmentAdapter_Viewbydate(FragmentManager fm) {
		super(fm);
		mCount = CONTENT.length;
		// TODO Auto-generated constructor stub
	}
	
	public FragmentAdapter_Viewbydate(MainActivity act, FragmentManager fm, String[] cont) {
		super(fm);
		this.activity = act;
		CONTENT = cont;
		mCount = CONTENT.length;
	}

	@Override
	public Fragment_ViewbyDate getItem(int position) {
		Log.d("Fragment GET ITEM", CONTENT[position % CONTENT.length] + " with " + orderBy);
		if (orderBy.equals(""))
			return Fragment_ViewbyDate.newInstance(activity,CONTENT[position % CONTENT.length]);
		else
			return Fragment_ViewbyDate.newInstance(activity,CONTENT[position % CONTENT.length], orderBy);
	}
	

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCount;
	}
}
