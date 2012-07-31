package com.vinova_g12.shoppingnow.ui;

import java.util.ArrayList;
import java.util.List;



import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.data.ListItem;
import com.vinova_g12.shoppingnow.data.ListItemAdapter;
import com.vinova_g12.shoppingnow.quickaction.ActionItem;
import com.vinova_g12.shoppingnow.quickaction.QuickAction;

public class MyActionModeCallBack implements ActionMode.Callback{

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		menu.add("Xong")
			.setIcon(R.drawable.icon_content_done)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add("Xóa")
			.setIcon(R.drawable.icon_content_done)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add("Chia Sẻ")
			.setIcon(R.drawable.icon_content_done)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return false;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		// TODO Auto-generated method stub
		
	}

}
