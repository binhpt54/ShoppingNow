package com.vinova_g12.shoppingnow.data;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.fragment.Fragment_ViewbyDate;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;
import com.vinova_g12.shoppingnow_app.MainActivity;

public class ListItemAdapter extends ArrayAdapter<ListItem> {
	private Context context;
	private int layoutResource;
	private List<ListItem> data = null;
	private Cursor cursor;
	private ListItemHolder holder;
	private SherlockFragmentActivity activity;
	private Fragment_ViewbyDate listFrament;
	private boolean selectAll = false;
	private ActionMode actionMode;
	public ShoppingDatabase db;
	private int flag = 0;
	
	public ListItemAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.layoutResource = textViewResourceId;
        this.context = context;
        data = new ArrayList<ListItem>();
	}
	
	public ListItemAdapter(Fragment_ViewbyDate list, Context context, int textViewResourceId, List<ListItem> data) {
		super(context, textViewResourceId, data);
		this.context = context;
		db = new ShoppingDatabase(context);
		this.layoutResource = textViewResourceId;
		this.data = data;
		this.listFrament = list;
		this.activity = (SherlockFragmentActivity) context;
		this.flag = 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final Integer id_item = new Integer(data.get(position).id);
		final Integer pos_item = new Integer(position);
		if (row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResource, parent, false);
            
            holder = new ListItemHolder();
            holder.btn_delete = (Button) row.findViewById(R.id.list_item_delete);
            holder.checkbox = (CheckBox) row.findViewById(R.id.list_item_checkbox);
            holder.name = (TextView) row.findViewById(R.id.list_item_name);
            holder.quantity = (TextView) row.findViewById(R.id.list_item_quantity);
            holder.price = (TextView) row.findViewById(R.id.list_item_price);
            holder.sub = (TextView) row.findViewById(R.id.list_item_subtitle);
            holder.section = (TextView) row.findViewById(R.id.list_section_header);
            holder.priority = (TextView) row.findViewById(R.id.list_priority);
            
            holder.name.setTypeface(MyTypeFace_Roboto.Roboto_Medium(context));
            holder.quantity.setTypeface(MyTypeFace_Roboto.Roboto_Regular(context));
            holder.price.setTypeface(MyTypeFace_Roboto.Roboto_Regular(context));
            holder.sub.setTypeface(MyTypeFace_Roboto.Roboto_Regular(context));
 
            row.setTag(holder);
		} else {
			holder = (ListItemHolder) row.getTag();
		}
		/*Setting section header for listview in view by week*/
		if (MainActivity.stateView == 2)
			if (listFrament.category_date_in_week.contains(data.get(position).due_date)) {
					holder.section.setVisibility(View.GONE);
			} else {
				listFrament.category_date_in_week.add(data.get(position).due_date);
				holder.section.setVisibility(View.VISIBLE);
				holder.section.setText(data.get(position).due_date);
			}
		
		/*Setting view of row
		 * Display name
		 * Display  total : if don't have note have done then display total
		 * else display note have done / total
		 * Display priority if it not equals 0
		 * Display delete button when list is done
		 * */
		if (!selectAll) {
			holder.checkbox.setChecked(false);
			MainActivity.list_item_checked.clear();
			Fragment_ViewbyDate.list_item_checked.clear();
		} else {
			holder.checkbox.setChecked(true);
			MainActivity.list_item_checked.clear();
			Fragment_ViewbyDate.list_item_checked.clear();
			MainActivity.list_item_checked.add(data.get(position).id);
			Fragment_ViewbyDate.list_item_checked.add(new Integer(position));
		}
		//Name
		holder.name.setText(data.get(position).name);
		//Status
		if (data.get(position).status == 0) {
			Log.d("Status", "UNDONE");
			holder.name.setTextColor(Color.parseColor("#000000"));
			holder.name.setPaintFlags(holder.name.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
			holder.btn_delete.setVisibility(View.GONE);
		}
		else {
			Log.d("Status", "DONE");
			holder.name.setTextColor(Color.parseColor("#777777"));
			holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			holder.btn_delete.setVisibility(View.VISIBLE);
			holder.btn_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					db.openDB();
					db.delete(id_item);
					db.closeDB();
					listFrament.notifyDataChanged(pos_item.toString());
				}
			});
		}
		//Set priority
		if (data.get(position).priority == 0)
			holder.priority.setVisibility(View.INVISIBLE);
		else
			holder.priority.setVisibility(View.INVISIBLE);
		
		//Set quantity and price
		if (data.get(position).quantity != 0) {
			holder.quantity.setText(data.get(position).quantity + "");
			holder.quantity.setVisibility(View.VISIBLE);
		}
		
		if (data.get(position).price != 0) {
			holder.price.setText(data.get(position).price + "");
			holder.price.setVisibility(View.VISIBLE);
		}
		//set place
		if (!data.get(position).place.equals(" ")) {
			holder.sub.setText(data.get(position).place);
			holder.sub.setVisibility(View.VISIBLE);
			Log.d("Place", "IS NULL");
		} else holder.sub.setVisibility(View.GONE);
		//Setup behavior for widgets of list item
		holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					MainActivity.list_item_checked.add(id_item);
					Fragment_ViewbyDate.list_item_checked.add(pos_item);
					MainActivity.countChecked ++;
					Toast.makeText(context,""+ MainActivity.countChecked, Toast.LENGTH_SHORT).show();
					if (MainActivity.countChecked != 0) {
						if (actionMode == null) {
							actionMode = activity.startActionMode(new MyActionModeCallBack());
						}
						actionMode.setTitle(MainActivity.countChecked + " Đã Chọn");
					}
				}
				else {
					MainActivity.list_item_checked.remove(id_item);
					if (actionMode != null) {
							MainActivity.countChecked --;
							Fragment_ViewbyDate.list_item_checked.remove(pos_item);
							Toast.makeText(context,""+ MainActivity.countChecked, Toast.LENGTH_SHORT).show();
							actionMode.setTitle(MainActivity.countChecked + " Đã Chọn");
							if (MainActivity.countChecked == 0) {
								actionMode.finish();
							}
						}
				}
			}
		});
		
		return row;
	}
	
	static class ListItemHolder {
		CheckBox checkbox;
		TextView name;
		Button btn_delete;
		TextView quantity;
		TextView priority;
		TextView section;
		TextView price;
		TextView sub;
	}
	
	class MyActionModeCallBack implements ActionMode.Callback{

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			activity.getSupportMenuInflater().inflate(R.menu.menu_actionmode, menu);
			MainActivity.countChecked = 1;
			Fragment_ViewbyDate.actionMode_running = true;
			db.openDB();
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.acm_select_all:
				selectAll = true;
				listFrament.notifyDataChanged("");
				break;
			case R.id.acm_unselect_all:
				selectAll = false;
				listFrament.notifyDataChanged("");
				break;
			case R.id.acm_delete:
				selectAll = false;
				db.deleteSome(MainActivity.list_item_checked);
				listFrament.notifyDataChanged("DELETE");
				break;
			case R.id.acm_done:
				selectAll = false;
				db.updateSomeStatus("Done", MainActivity.list_item_checked);
				for(int i=0;i< MainActivity.list_item_checked.size();i++)
				{
					/*Log.d("Check Done","id "+MainActivity.list_item_checked.get(i));
					data.get(MainActivity.list_item_checked.get(i)).status = 1;*/
				}
				Log.d("DONE IS CLICKED", "CLICKED");
				listFrament.notifyDataChanged("");
				break;
			case R.id.acm_undone:
				selectAll = false;
				db.updateSomeStatus("Undone", MainActivity.list_item_checked);
				for(int i=0;i< MainActivity.list_item_checked.size();i++)
				{
					Log.d("Check UnDone","id "+MainActivity.list_item_checked.get(i));
					data.get(MainActivity.list_item_checked.get(i)).status = 0;
				}
				listFrament.notifyDataChanged("");
				break;
			case R.id.acm_share:
				selectAll = false;
				break;
				

			default:
				break;
			}
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			actionMode = null;
			selectAll = false;
			Fragment_ViewbyDate.actionMode_running = false;
			MainActivity.countChecked = 0;
			listFrament.notifyDataChanged("");
			db.closeDB();
		}

	}
}
