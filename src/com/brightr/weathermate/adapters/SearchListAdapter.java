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

public class SearchListAdapter extends BaseAdapter {

	Activity context;
	LayoutInflater inflater;
	int[] icons;
	ArrayList<String> locations;
	ArrayList<String> latitudes;
	ArrayList<String> longitudes;
	ArrayList<String> regions;
	ArrayList<String> countries;
	ArrayList<String> populations;
	char degree = '\u00B0';

	public SearchListAdapter(Activity context, ArrayList<String> countries,
			ArrayList<String> locations, ArrayList<String> populations,
			ArrayList<String> latitudes, ArrayList<String> longitudes,
			ArrayList<String> regions) {

		this.context = context;
		this.countries = countries;
		this.populations = populations;
		this.locations = locations;
		this.latitudes = latitudes;
		this.longitudes = longitudes;
		this.regions = regions;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {

		return locations.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public class ViewHolder {

		ImageView icon;
		TextView location;
		TextView latitude;
		TextView longitude;
		TextView region;
		TextView country;
		TextView population;
		View header;
		TextView headerText;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {

			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.search_layout_row, null);
			holder.location = (TextView) convertView
					.findViewById(R.id.tvSearchLocationName);
			holder.latitude = (TextView) convertView
					.findViewById(R.id.tvSearchLocationLatitude);
			holder.longitude = (TextView) convertView
					.findViewById(R.id.tvSearchLocationLongitude);
			holder.region = (TextView) convertView
					.findViewById(R.id.tvSearchLocationRegion);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.ivSearchLocationIcon);
			holder.country = (TextView) convertView
					.findViewById(R.id.tvSearchLocationCountry);
			holder.population = (TextView) convertView
					.findViewById(R.id.tvSearchLocationPopulation);
			holder.header = (View) convertView
					.findViewById(R.id.SearchHeaderLayout);
			holder.headerText = (TextView) holder.header
					.findViewById(R.id.FlightNameHeader);
			convertView.setTag(holder);

		}

		else

			holder = (ViewHolder) convertView.getTag();
		holder.location.setText(locations.get(pos));
		holder.latitude.setText("Latitude: " + latitudes.get(pos) + degree);
		holder.longitude.setText("Longitude: " + longitudes.get(pos) + degree);
		holder.region.setText("Region: " + regions.get(pos));
		holder.icon.setBackgroundResource(R.drawable.map_icon);
		holder.country.setText("Country: " + countries.get(pos));

		if (populations.get(pos).equals("0")) {

			holder.population.setText("Population: Not available  ");
		}

		else
			holder.population.setText("Population:  " + populations.get(pos));

		// holder.population.setVisibility(View.INVISIBLE);
		holder.headerText.setText(regions.get(pos));

		return convertView;
	}

}
