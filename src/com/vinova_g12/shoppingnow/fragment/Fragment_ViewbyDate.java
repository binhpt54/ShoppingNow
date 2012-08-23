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
	public Fragment_ViewbyDate fragment;
	//ID of action item
	private static final int ID_DONE = 1;
	private static final int ID_DELETE = 2;
	private static final int ID_EDIT = 3;
	private static final int ID_ALERT = 4;
	private static final int ID_SHARE = 5;	
	private static final int ID_VIEW_PLACE = 6;
	private static final int ID_UNDONE = 7;
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
	
	
	//Create a new object Fragment with mContent is content
	public static Fragment_ViewbyDate newInstance(String content) {
		Fragment_ViewbyDate fragment = new Fragment_ViewbyDate(content);
		return fragment;
	}
	
	public Fragment_ViewbyDate(String content) {
		super();
		mContent = content;
		category_date_in_week = new ArrayList<String>();
	}
	
	public Fragment_ViewbyDate() {
		super();
	}
	
	//Notify to activity, event data of list changed. Activity have to invadilate listview
	public void notifyDataChanged(String cmd) {
		category_date_in_week.clear();
		Log.d("Category in week", category_date_in_week.size() + "");
		if (cmd.equals("DELETE")) {
			if (list_item_checked.size() != 0)
				for (int i=0; i<list_item_checked.size(); i++) {
					adapter.remove(data.get(list_item_checked.get(i)));
				}
		} else if (cmd.equals("Undone")) {
			for (int i=0; i<list_item_checked.size(); i++)
				data.get(i).status = 0;
			} else if (cmd.equals("Done")) {
				for (int i=0; i<list_item_checked.size(); i++)
					data.get(i).status = 1;
			} else if (!cmd.equals("")) {
				int pos = Integer.parseInt(cmd);
				Log.d("Delete button clicked", "" + pos);
				if (pos >= 0 && pos < 10000000) {
					adapter.remove(data.get(pos));
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
		this.getListView().setLayoutAnimation(controller);
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
				ActionItem undoneItem = new ActionItem(ID_UNDONE, "Chưa Xong", 
						getResources().getDrawable(R.drawable.icon_content_undone));
				//Create quickaction window
				quickAction = new QuickAction(getSherlockActivity(), QuickAction.HORIZONTAL);
				//Add action items into quickaction
				quickAction.addActionItem(doneItem);
				quickAction.addActionItem(deleteItem);
				quickAction.addActionItem(editItem);
				quickAction.addActionItem(alertItem);
				quickAction.addActionItem(shareItem);
				quickAction.addActionItem(viewPlace);
				
				//Create quickaction window
				quickAction2 = new QuickAction(getSherlockActivity(), QuickAction.HORIZONTAL);
				//Add action items into quickaction
				quickAction2.addActionItem(undoneItem);
				quickAction2.addActionItem(deleteItem);
				quickAction2.addActionItem(alertItem);
				quickAction2.addActionItem(shareItem);
				quickAction2.addActionItem(viewPlace);
				
				quickActionListener = new QuickAction.OnActionItemClickListener() {
					
					@Override
					public void onItemClick(QuickAction source, int pos, int actionId) {
						db.openDB();
						ActionItem actionItem = quickAction.getActionItem(pos);
						//Filter action item have clicked
						switch (actionId) {
							case ID_ALERT:
								break;
							case ID_DELETE:
								db.delete(data.get(checked).id);
								data.remove(checked);
								notifyDataChanged("");
								break;
							case ID_DONE:
								db.updateStatus("Done", data.get(checked).id);
								data.get(checked).status = 1;
								notifyDataChanged("");
								break;
							case ID_EDIT:
								Intent intent = new Intent(getActivity(),AddNew.class);
								Bundle update = new Bundle();
								update.putInt ("id", data.get(checked).id);
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
								db.updateStatus("Undone", data.get(checked).id);
								data.get(checked).status = 0;
								notifyDataChanged("");
								break;
			
							default:
								break;
						}
						db.closeDB();
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
		super.onResume();
		countProduct = 0;
		category_date_in_week = new ArrayList<String>();
		data = new ArrayList<ListItem>();	
		db = new ShoppingDatabase(getSherlockActivity().getApplicationContext());
		db.openDB();
		add_data_to_Adapter();
		comparator = new Comparator<ListItem>() {
			
			@Override
			public int compare(ListItem lhs, ListItem rhs) {
				if (sort == 0) {
					Log.d("Sort by", "Alphabet");
					return lhs.compareALphabet(rhs);
				}
				if (sort == 1)
					return lhs.comparePriority(rhs);
				return lhs.comparePrice(rhs);
			}
		};
		// Setup adapter for list view
				adapter = new ListItemAdapter(this,getSherlockActivity(), R.layout.list_item_row, data);
				
				//sort
				if (adapter.getCount() > 1) {
					adapter.sort(comparator);
					adapter.notifyDataSetChanged();
				}
				
				setListAdapter(adapter);
				db.closeDB();
	}
	
	private class SearchData extends AsyncTask<String, Long, Void> {
		List<ListItem> dataSearch;
		protected void onPreExecute() {
			super.onPreExecute();
			getSherlockActivity().setSupportProgressBarVisibility(true);
			data = new ArrayList<ListItem>();
			dataSearch = new ArrayList<ListItem>();
		}

		@Override
		protected Void doInBackground(String... params) {
			dataSearch = db.getAllItems();
			int length;
			length = MainActivity.search_bar.length();
			for (int i=0; i<dataSearch.size(); i++) {
				if (length <= dataSearch.get(i).name.length())
					if (MainActivity.search_bar.getText().toString().
							equalsIgnoreCase((String) data.get(i).name.subSequence(0, length))) {
						data.add(dataSearch.get(i));
					}	
			}
			publishProgress();
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Long... values) {
			super.onProgressUpdate(values);
			adapter.notifyDataSetChanged();

		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			getSherlockActivity().setSupportProgressBarVisibility(false);
		}
		
	}

	@Override
	public void onDetach() {
		super.onDetach();
		db.closeDB();
	}
}
