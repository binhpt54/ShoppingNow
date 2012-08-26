package com.vinova_g12.shoppingnow.fragment;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.data.ListItem;
import com.vinova_g12.shoppingnow.data.ListItemAdapter;
import com.vinova_g12.shoppingnow.data.ShoppingDatabase;
import com.vinova_g12.shoppingnow.quickaction.ActionItem;
import com.vinova_g12.shoppingnow.quickaction.QuickAction;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;
import com.vinova_g12.shoppingnow_app.AddNew;
import com.vinova_g12.shoppingnow_app.MainActivity;
@SuppressLint({ "ParserError", "ParserError" })
public class Fragment_ViewbyDate extends SherlockListFragment{
	//Attributes of class
	private TextView textView1;
	private TextView textView2;
	public ListItemAdapter adapter;
	List<ListItem> data;
	ShoppingDatabase db;
	private Cursor mCursor;
	private String mContent = "";
	private int countProduct = 0;
	public Fragment_ViewbyDate fragment;
	//ID of action item
	private static final int ID_DONE = 1;
	private static final int ID_DELETE = 2;
	private static final int ID_EDIT = 3;
	private static final int ID_ALERT = 4;
	private static final int ID_SHARE = 5;	
	private static final int ID_VIEW_PLACE = 6;
	private static final int ID_UNDONE = 7;
	private static final int ID_PRIORITY = 8;
	private QuickAction quickAction;
	private QuickAction quickAction2;
	private QuickAction.OnActionItemClickListener quickActionListener;
	public static boolean actionMode_running = false;
	public static List<Integer> list_item_checked;
	public static List<String> category_date_in_week;
	private Comparator<ListItem> comparator;
	//Id of item checked for quickaction
	private int checked = -1;

	private int sort = 0;
	private String orderBy = "";
	
	
	//Create a new object Fragment with mContent is content
	public static Fragment_ViewbyDate newInstance(String content) {
		Fragment_ViewbyDate fragment = new Fragment_ViewbyDate(content);
		return fragment;
	}
	
	//Create a new object Fragment with mContent is content
	public static Fragment_ViewbyDate newInstance(String content, String orderBy) {
			Fragment_ViewbyDate fragment = new Fragment_ViewbyDate(content,orderBy);
			return fragment;
	}
	
	public Fragment_ViewbyDate(String content) {
		super();
		mContent = content;
		category_date_in_week = new ArrayList<String>();
	}
	
	public Fragment_ViewbyDate(String content, String orderBy) {
		super();
		mContent = content;
		this.orderBy = orderBy;
		category_date_in_week = new ArrayList<String>();
	}
	
	public Fragment_ViewbyDate() {
		super();
	}
	
	//Notify to activity, event data of list changed. Activity have to invadilate listview
	public void notifyDataChanged(String orderBy) {
		category_date_in_week.clear();
		add_data_to_Adapter(orderBy);
		adapter.notifyDataSetChanged();
	}
	
	public void add_data_to_Adapter(String orderby) {
		db.openDB();
		if (mContent.equals("Hôm Nay")) {
			mCursor = db.getAll_inDate("Today",orderby);
			bindData();
		}
		else if (mContent.equals("Ngày Mai")) {
			mCursor = db.getAll_inDate("Tomorrow",orderby);
			bindData();
		} else if (mContent.equals("Hôm Qua")) {
			mCursor = db.getAll_inDate("Yesterday",orderby);
			bindData();
		} else if (mContent.equals("Tuần Trước")) {
			mCursor = db.getAll_inWeek("Last week",orderby);
			bindData();
		} else if (mContent.equals("Tuần Này")) {
			mCursor = db.getAll_inWeek("This week",orderby);
			bindData();
		} else if (mContent.equals("Tuần Sau")) {
			mCursor = db.getAll_inWeek("Next week",orderby);
			bindData();
		}
		db.closeDB();
	}
	
	//Add data from cursor to list adapter of list fragment
	public void bindData() {
		data.clear();
		if (mCursor.moveToFirst()) {
			do {
				countProduct ++;
				data.add(new ListItem(mCursor));
			} while (mCursor.moveToNext());
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		createQuickAction();
		fragment = this;
		//Setting divider and list selector
		this.getListView().setDivider(getResources().getDrawable(R.xml.divider_list_item));
		this.getListView().setDividerHeight(2);
		LayoutAnimationController controller 
		   = AnimationUtils.loadLayoutAnimation(getSherlockActivity(), R.anim.list_animation);
		LayoutAnimationController controller2
		   = AnimationUtils.loadLayoutAnimation(getSherlockActivity(), R.anim.list_animation_right_left);
		if (mContent.equals("Ngày Mai") || mContent.equals("Tuần Sau"))
			this.getListView().setLayoutAnimation(controller2);
		else
			this.getListView().setLayoutAnimation(controller);
		textView1 = (TextView) getActivity().findViewById(R.id.textView1);
		textView2 = (TextView) getActivity().findViewById(R.id.textView2);
		
		textView1.setTypeface(MyTypeFace_Roboto.Roboto_Bold(getActivity()));
		textView2.setTypeface(MyTypeFace_Roboto.Roboto_Regular(getActivity()));
		
		this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				//Show quickaction when long click in item
				if (!actionMode_running)
					if (data.get(pos).status == 0)
						quickAction.show(arg1);
					else quickAction2.show(arg1);
				checked = pos;
				//Toast.makeText(getActivity(), "Clicked2 " + pos, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	public void createQuickAction() {
		//Create action items
				ActionItem priority = new ActionItem(ID_PRIORITY, "Cần Mua", 
						getResources().getDrawable(R.drawable.light_icon_priority));
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
				ActionItem undoneItem = new ActionItem(ID_UNDONE, "Chưa Xong", 
						getResources().getDrawable(R.drawable.icon_content_undone));
				//Create quickaction window
				quickAction = new QuickAction(getSherlockActivity(), QuickAction.HORIZONTAL);
				//Add action items into quickaction
				quickAction.addActionItem(priority);
				quickAction.addActionItem(doneItem);
				quickAction.addActionItem(deleteItem);
				quickAction.addActionItem(editItem);
				quickAction.addActionItem(alertItem);
				quickAction.addActionItem(shareItem);
				quickAction.addActionItem(viewPlace);
				
				//Create quickaction window
				quickAction2 = new QuickAction(getSherlockActivity(), QuickAction.HORIZONTAL);
				//Add action items into quickaction
				quickAction2.addActionItem(priority);
				quickAction2.addActionItem(undoneItem);
				quickAction2.addActionItem(deleteItem);
				quickAction2.addActionItem(alertItem);
				quickAction2.addActionItem(shareItem);
				quickAction2.addActionItem(viewPlace);
				
				quickActionListener = new QuickAction.OnActionItemClickListener() {
					
					@Override
					public void onItemClick(QuickAction source, int pos, int actionId) {
						ActionItem actionItem = quickAction.getActionItem(pos);
						//Filter action item have clicked
						switch (actionId) {
							case ID_PRIORITY:
								if (data.get(checked).priority == 0) {
									db.openDB();
									db.updatePriority("Yes", data.get(checked).id);
									db.closeDB();
								} else {
									db.openDB();
									db.updatePriority("No", data.get(checked).id);
									db.closeDB();
								}
								notifyDataChanged("");
								break;
							case ID_ALERT:
								break;
							case ID_DELETE:
								db.openDB();
								db.delete(data.get(checked).id);
								db.closeDB();
								data.remove(checked);
								notifyDataChanged("");
								break;
							case ID_DONE:
								db.openDB();
								db.updateStatus("Done", data.get(checked).id);
								data.get(checked).status = 1;
								db.closeDB();
								notifyDataChanged("");
								break;
							case ID_EDIT:
								Intent intent = new Intent(getActivity(),AddNew.class);
								Bundle update = new Bundle();
								update.putInt (MainActivity.REQUEST_CREATE, data.get(checked).id);
								intent.putExtras(update);
								startActivity(intent);
								notifyDataChanged("");
								break;
							case ID_SHARE:
								notifyDataChanged("");
								break;
							case ID_VIEW_PLACE:
								notifyDataChanged("");
								break;
							case ID_UNDONE:
								db.openDB();
								db.updateStatus("Undone", data.get(checked).id);
								data.get(checked).status = 0;
								db.closeDB();
								notifyDataChanged("");
								break;
			
							default:
								break;
						}
					}
				};

				//Behavior for quickaction
				quickAction.setOnActionItemClickListener(quickActionListener);
				quickAction2.setOnActionItemClickListener(quickActionListener);
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
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
		Log.d("Fragment View By Date", mContent + " " + "Is running with sort by" + orderBy);
		super.onResume();
		countProduct = 0;
		category_date_in_week = new ArrayList<String>();
		data = new ArrayList<ListItem>();	
		db = new ShoppingDatabase(getSherlockActivity().getApplicationContext());
		add_data_to_Adapter(orderBy);
		// Setup adapter for list view
		adapter = new ListItemAdapter(this,getSherlockActivity(), R.layout.list_item_row, data);
		setListAdapter(adapter);
	}
	@Override
	public void onDetach() {
		super.onDetach();
	}
}
