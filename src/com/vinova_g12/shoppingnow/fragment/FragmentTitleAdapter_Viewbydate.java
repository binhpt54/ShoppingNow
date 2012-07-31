package com.vinova_g12.shoppingnow.fragment;

import android.support.v4.app.FragmentManager;

public class FragmentTitleAdapter_Viewbydate extends FragmentAdapter_Viewbydate{

	public FragmentTitleAdapter_Viewbydate(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
        return FragmentAdapter_Viewbydate.CONTENT[position % CONTENT.length];
    }

}
