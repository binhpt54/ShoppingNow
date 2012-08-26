package com.vinova_g12.shoppingnow.data;

import java.util.ArrayList;
import java.util.List;

import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;
import com.vinova_g12.shoppingnow_app.ActivityChooseDate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResource, parent, false);
            
			holder = new ItemChooseDateHolder();
			holder.checked = (ImageView) row.findViewById(R.id.choose_date_checked);
			holder.title = (TextView) row.findViewById(R.id.choose_date_name);
			holder.sub = (TextView) row.findViewById(R.id.choose_date_sub);
			holder.divider1 = (TextView) row.findViewById(R.id.choose_date_divider1);
			holder.divider2 = (TextView) row.findViewById(R.id.choose_date_divider2);
			
			holder.checked.setVisibility(View.INVISIBLE);
			
			holder.title.setTypeface(MyTypeFace_Roboto.Roboto_Bold(context));
			holder.sub.setTypeface(MyTypeFace_Roboto.Roboto_Regular(context));
			
			if (position == 7) {
				holder.checked.setVisibility(View.GONE);
				holder.divider1.setVisibility(View.GONE);
				holder.divider2.setVisibility(View.GONE);
				holder.title.setGravity(Gravity.CENTER);
				holder.title.setTextColor(Color.parseColor("#b12525"));
				holder.title.setShadowLayer(1, -1, -1, Color.parseColor("#ffffff"));
			}
			
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
		TextView divider1;
		TextView divider2;
	}
}
