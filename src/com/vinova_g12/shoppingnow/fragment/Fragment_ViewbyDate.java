package com.vinova_g12.shoppingnow.fragment;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.data.ListItem;
import com.vinova_g12.shoppingnow.data.ListItemAdapter;
import com.vinova_g12.shoppingnow.data.ShoppingDatabase;
import com.vinova_g12.shoppingnow.quickaction.ActionItem;
import com.vinova_g12.shoppingnow.quickaction.QuickAction;
import com.vinova_g12.shoppingnow_app.AddNew;
import com.vinova_g12.shoppingnow_app.MainActivity;
@SuppressLint({ "ParserError", "ParserError" })
public class Fragment_ViewbyDate extends SherlockListFragment{
	//Attributes of class
	ListItemAdapter adapter;
	List<ListItem> data;
	ShoppingDatabase db;
	private Cursor mCursor;
	private String mContent = "";
	private int countProduct = 0;
	//ID of action item
	private static final int ID_DONE = 1;
	private static final int ID_DELETE = 2;
	private static final int ID_EDIT = 3;
	private static final int ID_ALERT = 4;
	private static final int ID_SHARE = 5;	
	private static final int ID_VIEW_PLACE = 6;
	private QuickAction quickAction;
	public static boolean actionMode_running = false;
	public static List<Integer> list_item_checked;
	public List<String> category_date_in_week;
	//Id of item checked for quickaction
	private int checked = -1;
	
	
	
	//Create a new object Fragment with mContent is content
	public static Fragment newInstance(String content) {
		Log.d("Fragment", content);
		Fragment_ViewbyDate fragment = new Fragment_ViewbyDate(content);
		return fragment;
	}
	
	public Fragment_ViewbyDate(String content) {
		super();
		mContent = content;
		category_date_in_week = new ArrayList<String>();
		Log.d("content", mContent);
	}
	
	public Fragment_ViewbyDate() {
		super();
	}
	
	//Notify to activity, event data of list changed. Activity have to invadilate listview
	public void notifyDataChanged(String cmd) {
		Log.d("SIZE", list_item_checked.size() + "");
		category_date_in_week.clear();
		if (cmd.equals("DELETE")) {
			if (list_item_checked.size() != 0)
				for (int i=0; i<list_item_checked.size(); i++) {
					adapter.remove(data.get(list_item_checked.get(i)));
				}
		}
		adapter.notifyDataSetChanged();
	}
	
	public void add_data_to_Adapter() {
		if (mContent.equals("Hôm Nay")) {
			mCursor = db.getAll_inDate("Today");
			bindData();
		}
		else if (mContent.equals("Ngày Mai")) {
			mCursor = db.getAll_inDate("Tomorrow");
			bindData();
		} else if (mContent.equals("Hôm Qua")) {
			mCursor = db.getAll_inDate("Yesterday");
			bindData();
		} else if (mContent.equals("Tuần Trước")) {
			mCursor = db.getAll_inWeek("Last week");
			bindData();
		} else if (mContent.equals("Tuần Này")) {
			mCursor = db.getAll_inWeek("This week");
			bindData();
		} else if (mContent.equals("Tuần Sau")) {
			mCursor = db.getAll_inWeek("Next week");
			bindData();
		}
	}
	
	//Add data from cursor to list adapter of list fragment
	public void bindData() {
		if (mCursor.moveToFirst()) {
			do {
				Log.d("Do...While", countProduct + "");
				countProduct ++;
				data.add(new ListItem(mCursor));
			} while (mCursor.moveToNext());
		}
		Log.d("Count product", countProduct + "");
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		createQuickAction();
		//Setting divider and list selector
		this.getListView().setDivider(getResources().getDrawable(R.xml.divider_list_item));
		this.getListView().setDividerHeight(2);
		LayoutAnimationController controller 
		   = AnimationUtils.loadLayoutAnimation(getSherlockActivity(), R.anim.list_animation);
		this.getListView().setLayoutAnimation(controller);
		this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				//Show quickaction when long click in item
				if (!actionMode_running)
					quickAction.show(arg1);
				checked = pos;
				Toast.makeText(getActivity(), "Clicked2 " + pos, Toast.LENGTH_LONG).show();
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
				ActionItem viewPlace = new ActionItem(ID_VIEW_PLACE, "Cùng Địa Điểm",
						getResources().getDrawable(R.drawable.location_place));
				//Create quickaction window
				quickAction = new QuickAction(getSherlockActivity(), QuickAction.HORIZONTAL);
				//Add action items into quickaction
				quickAction.addActionItem(doneItem);
				quickAction.addActionItem(deleteItem);
				quickAction.addActionItem(editItem);
				quickAction.addActionItem(alertItem);
				quickAction.addActionItem(shareItem);
				quickAction.addActionItem(viewPlace);

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
								db.delete(data.get(checked).id);
								data.remove(checked);
								adapter.notifyDataSetChanged();
								break;
							case ID_DONE:
								break;
							case ID_EDIT:
								Intent intent = new Intent(getActivity(),AddNew.class);
								Bundle update = new Bundle();
								update.putInt ("id", data.get(checked).id);
								intent.putExtras(update);
								startActivity(intent);
								adapter.notifyDataSetChanged();
								break;
							case ID_SHARE:
								break;
							case ID_VIEW_PLACE:
								break;
			
							default:
								break;
						}
						
					}
				});
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(getActivity(), "Clicked "+ position+ " " + id, Toast.LENGTH_LONG).show();
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list_item_checked = new ArrayList<Integer>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_fragment_viewbydate	, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		countProduct = 0;
		data = new ArrayList<ListItem>();	
		db = new ShoppingDatabase(getSherlockActivity().getApplicationContext());
		db.openDB();
		add_data_to_Adapter();
		// Setup adapter for list view
				adapter = new ListItemAdapter(this,getSherlockActivity(), R.layout.list_item_row, data);
				setListAdapter(adapter);
				db.closeDB();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		db.closeDB();
	}
	
	
}
