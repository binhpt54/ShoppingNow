package com.vinova_g12.shoppingnow.fragment;

import com.vinova_g12.shoppingnow_app.MainActivity;

import android.support.v4.app.FragmentManager;

public class FragmentTitleAdapter_Viewbydate extends FragmentAdapter_Viewbydate{

	public FragmentTitleAdapter_Viewbydate(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	
	public FragmentTitleAdapter_Viewbydate(MainActivity act, FragmentManager fm, String[] cont) {
		super(act, fm,cont);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
        return FragmentAdapter_Viewbydate.CONTENT[position % CONTENT.length];
    }

}
