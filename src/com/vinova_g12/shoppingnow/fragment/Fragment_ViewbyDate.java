package com.vinova_g12.shoppingnow.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.data.ListItem;
import com.vinova_g12.shoppingnow.data.ListItemAdapter;
import com.vinova_g12.shoppingnow.quickaction.ActionItem;
import com.vinova_g12.shoppingnow.quickaction.QuickAction;

@SuppressLint({ "ParserError", "ParserError" })
public class Fragment_ViewbyDate extends SherlockListFragment{
	//Attributes of class
	ListItemAdapter adapter;
	List<ListItem> data;
	private String mContent = "";
	//ID of action item
	private static final int ID_DONE = 1;
	private static final int ID_DELETE = 2;
	private static final int ID_EDIT = 3;
	private static final int ID_ALERT = 4;
	private static final int ID_SHARE = 5;	
	private QuickAction quickAction;
	public static boolean actionMode_running = false;
	
	
	
	//Methos of class
	public static Fragment newInstance(String content) {
		Fragment_ViewbyDate fragment = new Fragment_ViewbyDate();
		return fragment;
	}
	
	public Fragment_ViewbyDate() {
		super();
	}
	
	public void notifyDateChanged() {
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		createQuickAction();
		
		//Add item to list view
		ListItem item1, item2, item3, item4;
		item1 = new ListItem();
		item2 = new ListItem();
		item3 = new ListItem();
		item4 = new ListItem();
		data = new ArrayList<ListItem>();
		
		item1.name = "Đi chợ cho bữa tối";
		
		item1.id = 1;
		item2.id = 2;
		item3.id = 3;
		item1.priority = 1;
		item2.priority = 2;
		item3.priority = 3;
		item4.priority = 0;
		
		item2.name = "Đi chợ cho bữa sáng";
		item3.name = "Đi chợ cho ngày mai";
		item4.name = "Dầu Hòa Aji-Ngon";
		
		data.add(item1);
		data.add(item2);
		data.add(item4);
		data.add(item3);
		
		
		// Setup adapter for list view
		adapter = new ListItemAdapter(this,getSherlockActivity(), R.layout.list_item_row, data);
		setListAdapter(adapter);
		//Setting divider and list selector
		this.getListView().setDivider(getResources().getDrawable(R.xml.divider_list_item));
		this.getListView().setDividerHeight(2);
		
		this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				//Show quickaction when long click in item
				if (!actionMode_running)
					quickAction.show(arg1);
				Toast.makeText(getActivity(), "Clicked2 " + pos, Toast.LENGTH_LONG).show();
				return false;
			}
		});
	}
	
	public void createQuickAction() {
		//Create action items
				ActionItem doneItem = new ActionItem(ID_DONE, "Xong", 
						getResources().getDrawable(R.drawable.icon_content_done));
				ActionItem deleteItem = new ActionItem(ID_DELETE, "Xóa", 
						getResources().getDrawable(R.drawable.icon_action_discard));
				ActionItem editItem = new ActionItem(ID_EDIT, "Sửa", 
						getResources().getDrawable(R.drawable.icon_content_edit));
				ActionItem alertItem = new ActionItem(ID_ALERT, "Nhắc Nhở", 
						getResources().getDrawable(R.drawable.icon_devicce_alram));
				ActionItem shareItem = new ActionItem(ID_SHARE, "Chia Sẻ", 
						getResources().getDrawable(R.drawable.icon_share));
				//Create quickaction window
				quickAction = new QuickAction(getSherlockActivity(), QuickAction.HORIZONTAL);
				//Add action items into quickaction
				quickAction.addActionItem(doneItem);
				quickAction.addActionItem(deleteItem);
				quickAction.addActionItem(editItem);
				quickAction.addActionItem(alertItem);
				quickAction.addActionItem(shareItem);
				quickAction.addActionItem(shareItem);
				quickAction.addActionItem(shareItem);
				quickAction.addActionItem(shareItem);
				//Behavior for quickaction
				quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					
					@Override
					public void onItemClick(QuickAction source, int pos, int actionId) {
						ActionItem actionItem = quickAction.getActionItem(pos);
						//Filter action item have clicked
						switch (actionId) {
							case ID_ALERT:
								
								break;
							case ID_DELETE:
								break;
							case ID_DONE:
								break;
							case ID_EDIT:
								break;
							case ID_SHARE:
								break;
			
							default:
								break;
						}
						
					}
				});
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
}
