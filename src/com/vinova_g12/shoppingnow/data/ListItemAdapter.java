package com.vinova_g12.shoppingnow.data;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.ActionMode;
import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.fragment.Fragment_ViewbyDate;
import com.vinova_g12.shoppingnow.ui.MyActionModeCallBack;

public class ListItemAdapter extends ArrayAdapter<ListItem> {
	public Context context;
	public int layoutResource;
	public List<ListItem> data = null;
	public Cursor cursor;
	public ListItemHolder holder;
	
	public ListItemAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.layoutResource = textViewResourceId;
        this.context = context;
        data = new ArrayList<ListItem>();
	}
	
	public ListItemAdapter(Context context, int textViewResourceId, List<ListItem> data) {
		super(context, textViewResourceId, data);
		this.context = context;
		this.layoutResource = textViewResourceId;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResource, parent, false);
            
            holder = new ListItemHolder();
            holder.btn_delete = (Button) row.findViewById(R.id.list_item_delete);
            holder.checkbox = (CheckBox) row.findViewById(R.id.list_item_checkbox);
            holder.name = (TextView) row.findViewById(R.id.list_item_name);
            holder.priority = (ImageView) row.findViewById(R.id.list_item_priority);
 
            
            row.setTag(holder);
		} else {
			holder = (ListItemHolder)row.getTag();
		}
		/*Setting view of row
		 * Display name
		 * Display  total : if don't have note have done then display total
		 * else display note have done / total
		 * Display priority if it not equals 0
		 * Display delete button when list is done
		 * */
		
		//Name
		holder.name.setText(data.get(position).name);
		//Status
		if (data.get(position).status == 0) {
			holder.name.setTextColor(Color.parseColor("#000000"));
			holder.name.setPaintFlags(holder.name.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
			holder.btn_delete.setVisibility(View.GONE);
			holder.priority.setVisibility(View.VISIBLE);
		}
		else {
			holder.name.setTextColor(Color.parseColor("#777777"));
			holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			holder.btn_delete.setVisibility(View.VISIBLE);
			holder.priority.setVisibility(View.GONE);
		}
		//priority
		switch (data.get(position).priority) {
		case 1:
			holder.priority.setImageResource(R.drawable.priority_low);
			break;
		case 2:
			holder.priority.setImageResource(R.drawable.priority_medium);
			break;
		case 3:
			holder.priority.setImageResource(R.drawable.priority_hard);
			break;
		//Default is GONE
		default:
			holder.priority.setVisibility(View.GONE);
			break;
		}
		//Setup behavior for widgets of list item
		holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show();
				}
				else
					Toast.makeText(context, "Unchecked", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		return row;
	}
	
	static class ListItemHolder {
		CheckBox checkbox;
		TextView name;
		ImageView priority;
		Button btn_delete;
	}
	

}
