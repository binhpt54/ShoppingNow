package com.vinova_g12.shoppingnow.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter_Viewbydate extends FragmentPagerAdapter{
	

	protected static String[] CONTENT = new String[] { "Ngày Mai", "Hôm Nay", "Hôm Qua"};
	private int mCount = CONTENT.length;
	
	public static void setContent(String[] cont) {
		CONTENT = cont;
	}
	
	public FragmentAdapter_Viewbydate(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		return Fragment_ViewbyDate.newInstance(CONTENT[position % CONTENT.length]);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCount;
	}

}
