package com.brightr.weathermate.adapters;

import java.util.ArrayList;

import com.brightr.weathermate.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {

	Activity context;
	int[] icons = { R.drawable.falcon,
			R.drawable.daemons, R.drawable.news,
			R.drawable.gossip_birds, R.drawable.icon_about,
			R.drawable.icon_settings};
	String[] names = {"My Flights", "Traffic", "News", "Social", "About", "Settings"};

	LayoutInflater inflater;

	public MenuAdapter(Activity context) {

		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {

		return icons.length;
	}

	@Override
	public Object getItem(int arg0) {

		return null;
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	public class ViewHolder {

		ImageView option_icon;
		TextView optionName;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.menu_fragment_row, null);
			holder.option_icon = (ImageView) convertView
					.findViewById(R.id.menuItem);
			holder.optionName = (TextView) convertView
					.findViewById(R.id.tvMenuItemName);

			convertView.setTag(holder);
		}

		else
			holder = (ViewHolder) convertView.getTag();
		holder.option_icon.setBackgroundResource(icons[pos]);
		holder.optionName.setText(names[pos]);

		return convertView;
	}
}
