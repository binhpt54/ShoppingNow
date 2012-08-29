package com.vinova_g12.shoppingnow.data;

import java.util.List;

import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AdapterSortDialog extends ArrayAdapter<String>{
	private Context context;
	private int layoutResource;
	private List<String> data;
	private ItemSortDialog holder;
	private int positionSelected = -1;

	public AdapterSortDialog(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
		this.context = context;
		layoutResource = resource;
		data = objects;
	}
	
	public AdapterSortDialog(Context context, int resource, List<String> objects, int selected) {
		super(context, resource, objects);
		this.context = context;
		layoutResource = resource;
		data = objects;
		positionSelected = selected;
	}

	@SuppressLint("ParserError") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final int pos = position;
		if (row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResource, parent, false);
            
            holder = new ItemSortDialog();
            holder.title = (TextView) row.findViewById(R.id.title_sort);
            holder.cb = (RadioButton) row.findViewById(R.id.radio_sort);
            
            holder.title.setTypeface(MyTypeFace_Roboto.Roboto_Regular(context));
            holder.title.setText(data.get(position));
            row.setTag(holder);
		} else
			holder = (ItemSortDialog) row.getTag();
		
		if (position == positionSelected)
			holder.cb.setChecked(true);
		else
			holder.cb.setChecked(false);

		
		OnCheckedChangeListener listener = new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					positionSelected = pos;
				} 
			}
		};
		holder.cb.setOnCheckedChangeListener(listener);
		return row;
	}
	
	static class ItemSortDialog {
		TextView title;
		RadioButton cb;
	}
	

}
