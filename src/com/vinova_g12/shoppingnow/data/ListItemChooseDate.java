package com.vinova_g12.shoppingnow.data;

import java.util.ArrayList;
import java.util.List;

import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;
import com.vinova_g12.shoppingnow_app.ActivityChooseDate;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListItemChooseDate extends ArrayAdapter<ItemChooseDate>{
	private List<ItemChooseDate> data;
	private Context context;
	private int layoutResource;
	public ItemChooseDateHolder holder;
	
	public ListItemChooseDate(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
		this.layoutResource = textViewResourceId;
		data = new ArrayList<ItemChooseDate>();
	}
	
	public ListItemChooseDate(Context context, int layout, List<ItemChooseDate> data) {
		super(context, layout, data);
		this.context = context;
		this.layoutResource = layout;
		this.data = data;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResource, parent, false);
            
			holder = new ItemChooseDateHolder();
			holder.checked = (ImageView) row.findViewById(R.id.choose_date_checked);
			holder.title = (TextView) row.findViewById(R.id.choose_date_name);
			holder.sub = (TextView) row.findViewById(R.id.choose_date_sub);
			
			holder.checked.setVisibility(View.INVISIBLE);
			
			holder.title.setTypeface(MyTypeFace_Roboto.Roboto_Bold(context));
			holder.sub.setTypeface(MyTypeFace_Roboto.Roboto_Regular(context));
			
			holder.title.setText(data.get(position).title);
			holder.sub.setText(data.get(position).date);
			
			if (data.get(position).date.equals(ActivityChooseDate.dateRecv))
				holder.checked.setVisibility(View.VISIBLE);
			row.setTag(holder);
		} else
			holder = (ItemChooseDateHolder) row.getTag();
		return row;
	}

	static class ItemChooseDateHolder {
		ImageView checked;
		TextView title;
		TextView sub;
	}
}
