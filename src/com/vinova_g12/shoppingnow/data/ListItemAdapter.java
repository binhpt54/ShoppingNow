package com.vinova_g12.shoppingnow.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.actionbarsherlock.widget.ShareActionProvider;
import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow.fragment.Fragment_ViewbyDate;
import com.vinova_g12.shoppingnow.ui.MyTypeFace_Roboto;
import com.vinova_g12.shoppingnow_app.ActivityChooseAlarm;
import com.vinova_g12.shoppingnow_app.ActivityChooseShare;
import com.vinova_g12.shoppingnow_app.AddNew;
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
	private Animation in;
	private SimpleDateFormat format;
	private SimpleDateFormat formatReverse;
	private SimpleDateFormat formatAlarm = new SimpleDateFormat(
			"HH:mm dd-MM-yyyy");
	SimpleDateFormat formatAlarmReverse = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm ");
	private boolean clear = false;

	public ListItemAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.layoutResource = textViewResourceId;
		this.context = context;
		data = new ArrayList<ListItem>();
	}

	public ListItemAdapter(Fragment_ViewbyDate list, Context context,
			int textViewResourceId, List<ListItem> data) {
		super(context, textViewResourceId, data);
		this.context = context;
		db = new ShoppingDatabase(context);
		this.layoutResource = textViewResourceId;
		this.data = data;
		this.listFrament = list;
		this.activity = (SherlockFragmentActivity) context;
		this.flag = 0;
		in = AnimationUtils.loadAnimation(context, R.anim.slide_right_to_left);
		format = new SimpleDateFormat("dd-MM-yyyy");
		formatReverse = new SimpleDateFormat("yyyy-MM-dd");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final Integer id_item = new Integer(data.get(position).id);
		final Integer pos_item = new Integer(position);
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResource, parent, false);

			holder = new ListItemHolder();
			holder.btn_delete = (Button) row
					.findViewById(R.id.list_item_delete);
			holder.checkbox = (CheckBox) row
					.findViewById(R.id.list_item_checkbox);
			holder.name = (TextView) row.findViewById(R.id.list_item_name);
			holder.quantity = (TextView) row
					.findViewById(R.id.list_item_quantity);
			holder.price = (TextView) row.findViewById(R.id.list_item_price);
			holder.sub = (TextView) row.findViewById(R.id.list_item_subtitle);
			holder.section = (TextView) row
					.findViewById(R.id.list_section_header);
			holder.priority = (TextView) row.findViewById(R.id.list_priority);
			holder.done_date = (TextView) row.findViewById(R.id.done_date);

			holder.name.setTypeface(MyTypeFace_Roboto.Roboto_Medium(context));
			holder.quantity.setTypeface(MyTypeFace_Roboto
					.Roboto_Medium(context));
			holder.price.setTypeface(MyTypeFace_Roboto.Roboto_Medium(context));
			holder.sub.setTypeface(MyTypeFace_Roboto.Roboto_Regular(context));
			holder.done_date.setTypeface(MyTypeFace_Roboto
					.Roboto_Regular(context));

			row.setTag(holder);
		} else {
			holder = (ListItemHolder) row.getTag();
		}
		/* Setting section header for listview in view by week */
		if (MainActivity.alarmView == 1) {
			if (listFrament.category_date_in_week
					.contains(data.get(position).alarm)) {
				holder.section.setVisibility(View.GONE);
			} else {
				listFrament.category_date_in_week.add(data.get(position).alarm);
				holder.section.setVisibility(View.VISIBLE);
				Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(formatAlarmReverse.parse(data.get(position).alarm));
					holder.section.setText(formatHour.format(cal.getTime())
							+ ListItemAdapter.getDay(cal) + ", "
							+ format.format(cal.getTime()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (MainActivity.stateView != 1 || MainActivity.stateSearch == 1)
			if (listFrament.category_date_in_week
					.contains(data.get(position).due_date)) {
				holder.section.setVisibility(View.GONE);
			} else {
				listFrament.category_date_in_week
						.add(data.get(position).due_date);
				holder.section.setVisibility(View.VISIBLE);
				Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(formatReverse.parse(data.get(position).due_date));
					holder.section.setText(getDay(cal) + ", "
							+ format.format(cal.getTime()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		/*
		 * Setting view of row Display name Display total : if don't have note
		 * have done then display total else display note have done / total
		 * Display priority if it not equals 0 Display delete button when list
		 * is done
		 */
		if (!selectAll) {
			clear = false;
			holder.checkbox.setChecked(false);
			MainActivity.list_item_checked.clear();
			Fragment_ViewbyDate.list_item_checked.clear();
		} else {
			holder.checkbox.setChecked(true);
			if (clear == false) {
				MainActivity.list_item_checked.clear();
				Fragment_ViewbyDate.list_item_checked.clear();
				clear = true;
			}
			MainActivity.list_item_checked.add(data.get(position).id);
			Fragment_ViewbyDate.list_item_checked.add(new Integer(position));
		}
		// Name
		holder.name.setText(data.get(position).name);
		// Status
		if (data.get(position).status == 0) {
			holder.name.setTextColor(Color.parseColor("#000000"));
			holder.name.setPaintFlags(holder.name.getPaintFlags()
					& (~Paint.STRIKE_THRU_TEXT_FLAG));
			holder.btn_delete.setVisibility(View.GONE);
			holder.done_date.setVisibility(View.GONE);
		} else {
			holder.name.setTextColor(Color.parseColor("#777777"));
			holder.name.setPaintFlags(holder.name.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
			holder.btn_delete.setVisibility(View.VISIBLE);
			holder.done_date.setVisibility(View.VISIBLE);
			holder.done_date.setText(getDoneDate(data.get(position).doneDate));
			holder.btn_delete.setFocusable(false);
			holder.btn_delete.startAnimation(in);
			holder.btn_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					db.openDB();
					db.delete(id_item);
					db.closeDB();
					listFrament.notifyDataChanged("");
				}
			});
		}
		// Set priority
		if (data.get(position).priority == 0)
			holder.priority.setVisibility(View.GONE);
		else
			holder.priority.setVisibility(View.VISIBLE);

		// Set quantity and total
		if (data.get(position).quantity != 0 && data.get(position).status == 0) {
			holder.quantity
					.setText(optimizeQuantity(data.get(position).quantity + "")
							+ " " + data.get(position).unit);
			holder.quantity.setVisibility(View.VISIBLE);
		} else
			holder.quantity.setVisibility(View.GONE);

		if (data.get(position).price != 0 && data.get(position).status == 0
				&& data.get(position).quantity != 0) {
			String total = (data.get(position).quantity * data.get(position).price)
					+ "";
			holder.price.setText(optimizePrice(total, "full") + " VND");
			holder.price.setVisibility(View.VISIBLE);
		} else
			holder.price.setVisibility(View.GONE);
		// set place
		if (!data.get(position).place.equals(" ")) {
			holder.sub.setText(data.get(position).place);
			holder.sub.setVisibility(View.VISIBLE);
		} else
			holder.sub.setVisibility(View.GONE);
		// Setup behavior for widgets of list item
		holder.checkbox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							MainActivity.list_item_checked.add(id_item);
							Fragment_ViewbyDate.list_item_checked.add(pos_item);
							MainActivity.countChecked++;
							// Toast.makeText(context,""+
							// MainActivity.countChecked,
							// Toast.LENGTH_SHORT).show();
							if (MainActivity.countChecked != 0) {
								if (actionMode == null) {
									actionMode = activity
											.startActionMode(new MyActionModeCallBack());
								}
								actionMode.setTitle(MainActivity.countChecked
										+ " Đã Chọn");
							}
						} else {
							MainActivity.list_item_checked.remove(id_item);
							if (actionMode != null) {
								MainActivity.countChecked--;
								Fragment_ViewbyDate.list_item_checked
										.remove(pos_item);
								// Toast.makeText(context,""+
								// MainActivity.countChecked,
								// Toast.LENGTH_SHORT).show();
								actionMode.setTitle(MainActivity.countChecked
										+ " Đã Chọn");
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
		TextView done_date;
	}

	class MyActionModeCallBack implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			activity.getSupportMenuInflater().inflate(R.menu.menu_actionmode,
					menu);
			MainActivity.countChecked = 1;
			Fragment_ViewbyDate.actionMode_running = true;
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
			case R.id.acm_priority:
				db.openDB();
				db.updateSomePriority("Yes", MainActivity.list_item_checked);
				db.closeDB();
				selectAll = false;
				listFrament.notifyDataChanged("");
				break;
			case R.id.acm_not_priority:
				db.openDB();
				db.updateSomePriority("No", MainActivity.list_item_checked);
				selectAll = false;
				listFrament.notifyDataChanged("");
				db.closeDB();
				break;
			case R.id.acm_select_all:
				selectAll = true;
				clear = true;
				listFrament.notifyDataChanged("");
				Log.d("Size of list item",
						MainActivity.list_item_checked.size() + "");
				break;
			case R.id.acm_unselect_all:
				selectAll = false;
				listFrament.notifyDataChanged("");
				break;
			case R.id.acm_delete:
				selectAll = false;
				db.openDB();
				db.deleteSome(MainActivity.list_item_checked);
				db.closeDB();
				listFrament.notifyDataChanged("");
				break;
			case R.id.acm_done:
				selectAll = false;
				db.openDB();
				db.updateSomeStatus("Done", MainActivity.list_item_checked);
				db.closeDB();
				listFrament.notifyDataChanged("");
				break;
			case R.id.acm_undone:
				selectAll = false;
				db.openDB();
				db.updateSomeStatus("Undone", MainActivity.list_item_checked);
				db.closeDB();
				listFrament.notifyDataChanged("");
				break;
			case R.id.acm_share:
				Intent intentShare = new Intent(activity, ActivityChooseShare.class);
				activity.startActivityForResult(intentShare, MainActivity.REQUEST_SHARE_CONTENT);
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
		}

	}

	public static String getDay(Calendar cal) {
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
			return "Thứ Hai";
		else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)
			return "Thứ Ba";
		else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)
			return "Thứ Tư";
		else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
			return "Thứ Năm";
		else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
			return "Thứ Sáu";
		else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			return "Thứ Bảy";
		return "Chủ Nhật";
	}

	public static String optimizeQuantity(String quantity) {
		String newQuantity = new String();
		if (quantity.endsWith("0")) {
			Log.d("String optimizer", "End of ");
			newQuantity = quantity.substring(0, quantity.length() - 2);
		} else
			newQuantity = quantity;
		return newQuantity;
	}

	public static String optimizePrice(String price, String option) {
		String newPrice = new String();
		if (price.endsWith("0")) {
			Log.d("End of 0", "OK");
			newPrice = price.substring(0, price.length() - 2);
		} else
			newPrice = price;
		if (option.equals("full")) {
			if (newPrice.length() > 9) {
				newPrice = newPrice.substring(0, newPrice.length() - 9)
						+ ","
						+ newPrice.substring(newPrice.length() - 9,
								newPrice.length() - 6)
						+ ","
						+ newPrice.substring(newPrice.length() - 6,
								newPrice.length() - 3)
						+ ","
						+ newPrice.substring(newPrice.length() - 3,
								newPrice.length());
				return newPrice;
			}

			if (newPrice.length() > 6) {
				newPrice = newPrice.substring(0, newPrice.length() - 6)
						+ ","
						+ newPrice.substring(newPrice.length() - 6,
								newPrice.length() - 3)
						+ ","
						+ newPrice.substring(newPrice.length() - 3,
								newPrice.length());
				return newPrice;
			}

			if (newPrice.length() > 3) {
				newPrice = newPrice.substring(0, newPrice.length() - 3)
						+ ","
						+ newPrice.substring(newPrice.length() - 3,
								newPrice.length());
				return newPrice;
			}
		}
		return newPrice;
	}

	public String getDoneDate(long done_date) {
		String s = new String();
		s = "";
		long one_minutes = 60000;
		long one_hours = 60 * one_minutes;
		long one_day = 24 * one_hours;
		long one_month = 30 * one_day;
		long one_year = 12 * one_month;

		Calendar today = Calendar.getInstance();
		long temp = today.getTimeInMillis();
		if (done_date != 0) {
			temp = temp - done_date;
			if (temp < 1000)
				s = "Vừa Mới";
			else if (temp >= 1000 && temp < one_minutes) {
				s = (temp / 1000) + " giây trước";
			} else if (temp >= one_minutes && temp < one_hours)
				s = (temp / one_minutes) + " phút trước";
			else if (temp >= one_hours && temp < one_day)
				s = (temp / one_hours) + " giờ trước";
			else if (temp >= one_day && temp < one_month)
				s = (temp / one_day) + " ngày trước";
			else if (temp >= one_month && temp < one_year)
				s = (temp / one_month) + " năm trước";
		}
		Log.d("DONE DATE", s + "");
		return s;
	}
}
