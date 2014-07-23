package com.brightr.weathermate.adapters;

import java.util.ArrayList;

import com.brightr.weathermate.R;
import com.brightr.weathermate.adapters.WeatherAdapter.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LocationListAdapter extends BaseAdapter {

	Activity context;
	LayoutInflater inflater;
	ArrayList<String> cities;
	ArrayList<String> dates;
	int[] icons;
	ArrayList<String> tempsF;
	ArrayList<String> tempsC;
	ArrayList<String> wConditions;
	ArrayList<String> cloudCovers;
	ArrayList<String> windspeeds;
	ArrayList<String> visib;
	ArrayList<String> precip;
	ArrayList<String> days;
	ArrayList<String> observationTimes;
	ArrayList<String> humidities;
	ArrayList<String> windSpeedsKmph;

	ArrayList<String> windDirectionsDegree;
	ArrayList<String> tempsMaxF;
	ArrayList<String> tempsMinF;
	ArrayList<String> tempsMaxC;
	ArrayList<String> tempsMinC;

	private SharedPreferences sharedPrefs;

	char degree = '\u00B0';

	public LocationListAdapter(Activity context, ArrayList<String> cities,
			ArrayList<String> dates, ArrayList<String> days, int[] icons,
			ArrayList<String> tempsF, ArrayList<String> tempsC,
			ArrayList<String> wConditions, ArrayList<String> cloudCovers,
			ArrayList<String> windspeeds, ArrayList<String> precip,
			ArrayList<String> humidities, ArrayList<String> windSpeedsKpmh,
			ArrayList<String> windDirectionsDegrees,
			ArrayList<String> tempsMaxF, ArrayList<String> tempsMinF,
			ArrayList<String> tempsMaxC, ArrayList<String> tempsMinC) {

		super();
		this.cities = cities;
		this.days = days;
		this.context = context;
		this.dates = dates;
		this.icons = icons;
		this.tempsF = tempsF;
		this.tempsC = tempsC;
		this.wConditions = wConditions;
		this.cloudCovers = cloudCovers;
		this.windspeeds = windspeeds;
		// this.visib = visib;
		this.precip = precip;
		// this.observationTimes = observationTimes;
		this.humidities = humidities;
		this.windSpeedsKmph = windSpeedsKpmh;
		this.windDirectionsDegree = windDirectionsDegrees;
		this.tempsMaxF = tempsMaxF;
		this.tempsMinF = tempsMinF;
		this.tempsMaxC = tempsMaxC;
		this.tempsMinC = tempsMinC;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	@Override
	public int getCount() {

		return wConditions.size();
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
		TextView day;
		TextView date;
		ImageView icon;
		TextView temp_F;
		TextView condition;
		TextView coverage;
		TextView wSpeed;
		TextView temp_C;
		TextView visibility;
		TextView precipitation;

		TextView observation_time;
		TextView humidity;
		TextView windspeed_kmph;
		TextView winddirection_degrees;

		TextView tempMaxF;
		TextView tempMinF;
		TextView tempMaxC;
		TextView tempMinC;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {

		ViewHolder holder;

		// Get the view type that was returned
		int theType = getItemViewType(pos);

		if (convertView == null) {

			holder = new ViewHolder();
			if (theType == 0) {

				convertView = inflater.inflate(R.layout.city_list_row, null);

				holder.city = (TextView) convertView
						.findViewById(R.id.tvCityName);
				holder.date = (TextView) convertView
						.findViewById(R.id.tvNextDate);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.imgWeatherIcon);
				holder.temp_F = (TextView) convertView
						.findViewById(R.id.tvTempF);
				holder.condition = (TextView) convertView
						.findViewById(R.id.tvWeatherCond);
				holder.coverage = (TextView) convertView
						.findViewById(R.id.tvCloudCov);
				holder.wSpeed = (TextView) convertView
						.findViewById(R.id.tvWindspeed);
				holder.temp_C = (TextView) convertView
						.findViewById(R.id.tvTempC);

				holder.precipitation = (TextView) convertView
						.findViewById(R.id.tvPrecip);

				holder.humidity = (TextView) convertView
						.findViewById(R.id.tvHumidity);
				holder.windspeed_kmph = (TextView) convertView
						.findViewById(R.id.tvWindspeedKph);
				holder.winddirection_degrees = (TextView) convertView
						.findViewById(R.id.tvWinddirection);
				holder.tempMaxF = (TextView) convertView
						.findViewById(R.id.tvtempMaxF);
				holder.tempMinF = (TextView) convertView
						.findViewById(R.id.tvtempMinF);
				holder.tempMaxC = (TextView) convertView
						.findViewById(R.id.tvtempMaxC);
				holder.tempMinC = (TextView) convertView
						.findViewById(R.id.tvtempMinC);
				// convertView.setTag(holder);
			}

			else if (theType == 1) {
				// For all other rows, inflate this specific layout
				convertView = inflater
						.inflate(R.layout.forecast_list_row, null);
				holder.day = (TextView) convertView
						.findViewById(R.id.tvforecastDayofWeek);
				holder.date = (TextView) convertView
						.findViewById(R.id.tvforecastDay);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.ivforecastWeatherIcon);

				holder.tempMaxF = (TextView) convertView
						.findViewById(R.id.tvforecastTempMaxF);
				holder.tempMinF = (TextView) convertView
						.findViewById(R.id.tvforecastTempMinF);

				holder.tempMaxC = (TextView) convertView
						.findViewById(R.id.tvforecastTempMaxC);
				holder.tempMinC = (TextView) convertView
						.findViewById(R.id.tvforecastTempMinC);
				holder.condition = (TextView) convertView
						.findViewById(R.id.tvforecastDescription);
			}

			convertView.setTag(holder);

		}

		else {
			holder = (ViewHolder) convertView.getTag();
			if (theType == 0) {

				holder.city.setText(cities.get(pos));
				holder.date.setText("Today");
				holder.icon.setBackgroundResource(icons[pos]);

				String color = sharedPrefs.getString("colorPref", "Yellow");

				holder.temp_C.setText(tempsC.get(pos));

				changeTextColor(holder.temp_C, color);

				holder.temp_F.setText(tempsF.get(pos));

				changeTextColor(holder.temp_F, color);

				holder.condition.setText("Condition: " + wConditions.get(pos));
				holder.coverage.setText(cloudCovers.get(pos));
				holder.wSpeed.setText(windspeeds.get(pos));

				// holder.visibility.setText(visib.get(pos));
				holder.precipitation.setText(precip.get(pos));

				boolean showC = sharedPrefs.getBoolean("degreesC", true);

				if (showC) {

				}

				else {
					holder.temp_C.setVisibility(View.INVISIBLE);
					holder.tempMaxC.setVisibility(View.INVISIBLE);
					holder.tempMinC.setVisibility(View.INVISIBLE);

				}

				boolean showF = sharedPrefs.getBoolean("degreesF", true);
				if (showF) {

				}

				else {
					holder.temp_F.setVisibility(View.INVISIBLE);
					holder.tempMaxF.setVisibility(View.INVISIBLE);
					holder.tempMinF.setVisibility(View.INVISIBLE);

				}

				// holder.observation_time.setText(observationTimes.get(pos));
				holder.humidity.setText(humidities.get(pos));
				holder.windspeed_kmph.setText(windSpeedsKmph.get(pos));
				holder.winddirection_degrees.setText(windDirectionsDegree
						.get(pos));
				holder.tempMaxF.setText("H " + tempsMaxF.get(pos) + degree
						+ "F");
				holder.tempMinF.setText("L " + tempsMinF.get(pos) + degree
						+ "F");
				holder.tempMaxC.setText("H " + tempsMaxC.get(pos) + degree
						+ "C");
				holder.tempMinC.setText("L " + tempsMinC.get(pos) + degree
						+ "C");
			}

			else if (theType == 1) {
				// holder = (ViewHolder) convertView.getTag();
				holder.date.setText(dates.get(pos));
				holder.icon.setBackgroundResource(icons[pos]);
				holder.day.setText(days.get(pos));
				holder.tempMaxF.setText("H " + tempsMaxF.get(pos) + degree
						+ " F");
				holder.tempMinF.setText("L " + tempsMinF.get(pos) + degree
						+ " F");
				holder.tempMaxC.setText("H " + tempsMaxC.get(pos) + degree
						+ " C");
				holder.tempMinC.setText("L " + tempsMinC.get(pos) + degree
						+ " C");
				holder.condition.setText("Condition:  " + wConditions.get(pos));

			}
		}

		return convertView;
	}

	private void changeTextColor(TextView text, String color) {
		// Change the first 2 characters of this textview to the color yellow
		//Also sets the color according to user settings
		ForegroundColorSpan fcs = null;
		final SpannableStringBuilder sb = new SpannableStringBuilder(
				text.getText());

		if (color.equalsIgnoreCase("yellow")) {
			fcs = new ForegroundColorSpan(Color.YELLOW);
		}

		else if (color.equalsIgnoreCase("blue")) {
			fcs = new ForegroundColorSpan(Color.BLUE);
		}

		else if (color.equalsIgnoreCase("white")) {
			fcs = new ForegroundColorSpan(Color.WHITE);
		}

		else if (color.equalsIgnoreCase("red")) {
			fcs = new ForegroundColorSpan(Color.RED);
		}

		sb.setSpan(fcs, 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		text.setText(sb);

	}

	@Override
	public int getItemViewType(int position) {
		// If position is 0, return 0, else return 1 for all other positions
		if (position == this.getCount() - this.getCount()) {
			return 0;
		}

		else
			return 1;
	}

	@Override
	public int getViewTypeCount() {
		// Return either of 2 view times in order to have a fixed layout for
		// element 0 and another layout for
		// all other elements
		return 2;
	}

}
