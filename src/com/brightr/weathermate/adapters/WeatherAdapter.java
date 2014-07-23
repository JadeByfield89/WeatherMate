package com.brightr.weathermate.adapters;

import java.util.ArrayList;

import com.brightr.weathermate.R;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherAdapter extends BaseAdapter {

	Activity context;
	LayoutInflater inflater;

	ArrayList<String> cities;
	ArrayList<String> countries;
	ImageView icon;

	public WeatherAdapter(Activity context, ArrayList<String> cities, ArrayList<String> countries) {

		super();
		this.context = context;
		this.cities = cities;
		this.countries = countries;

		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {

		return cities.size();
	}

	@Override
	public Object getItem(int index) {

		return null;
	}

	@Override
	public long getItemId(int index) {

		return 0;
	}

	public class ViewHolder {
		TextView city;
		TextView country;
		ImageView icon;

	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.sidepanel_list_row, null);
			holder.city = (TextView) convertView.findViewById(R.id.tvCityName);
			holder.country = (TextView)convertView.findViewById(R.id.tvRegion);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.imgWeatherIcon);

			convertView.setTag(holder);

		}

		else
			holder = (ViewHolder) convertView.getTag();
		holder.city.setText(cities.get(pos));
		holder.country.setText(countries.get(pos));
		//holder.icon.setBackgroundResource(R.drawable.weather_icon_partlycloudy);

		return convertView;
	}

}
