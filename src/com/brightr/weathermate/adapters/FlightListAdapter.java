package com.brightr.weathermate.adapters;

import java.util.ArrayList;

import com.brightr.weathermate.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FlightListAdapter extends BaseAdapter {

	Activity mContext;
	ArrayList<String> mAirlineNames;
	ArrayList<String> mFlightNumbers;
	ArrayList<String> mAirports;
	ArrayList<String> mDepartureTimes;
	ArrayList<String> mArrivalTimes;
	ArrayList<String> mDepartureCities;
	ArrayList<String> mArrivalCities;
	int[] mNextIcons = { R.drawable.flights_next };
	ArrayList<ImageView> mAirlineIcons;
	LayoutInflater inflater;

	ArrayList<String> mHeaderText = new ArrayList<String>();

	private ArrayList<Integer> mSeparatorSet = new ArrayList<Integer>();

	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
	private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

	public FlightListAdapter(Activity context,
			ArrayList<String> mFlightNumbers, ArrayList<String> mAirports,
			ArrayList<String> mDepartureTimes, ArrayList<String> mArrivalTimes,
			ArrayList<String> mArrivalCities, ArrayList<String> mDepartureCities) {

		this.mContext = context;

		this.mFlightNumbers = mFlightNumbers;
		this.mAirports = mAirports;
		this.mDepartureTimes = mDepartureTimes;
		this.mArrivalTimes = mArrivalTimes;
		this.mArrivalCities = mArrivalCities;
		this.mDepartureCities = mDepartureCities;
		inflater = context.getLayoutInflater();

	}

	@Override
	public int getCount() {

		return mFlightNumbers.size();
	}

	public void addSeparatorItem(final String item) {
		mHeaderText.add(item);
		// save separator position
		mSeparatorSet.add(mHeaderText.size() - 1);
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	public class ViewHolder {

		TextView airlineName;
		TextView flightNumber;
		TextView airport;
		TextView departureTime;
		TextView arrivalTime;
		TextView departureCity;
		TextView arrivalCity;
		View header;
		TextView headerText;
		ImageView airlineIcon;
		ImageView nextIcon;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		int theType = getItemViewType(position);

		System.out.println("GETVIEW  " + position + " " + convertView
				+ " TYPE = " + theType);

		if (convertView == null) {
			holder = new ViewHolder();

			switch (theType) {

			case TYPE_ITEM:
				convertView = inflater.inflate(R.layout.flightlist_row, null);

				holder.flightNumber = (TextView) convertView
						.findViewById(R.id.tvFlightNumber);
				holder.airport = (TextView) convertView
						.findViewById(R.id.tvFlightAirport);
				holder.departureTime = (TextView) convertView
						.findViewById(R.id.tvFlightDepartureTime);
				holder.arrivalTime = (TextView) convertView
						.findViewById(R.id.tvFlightArrivalTime);
				holder.departureCity = (TextView) convertView
						.findViewById(R.id.tvFlightOriginCity);
				holder.arrivalCity = (TextView) convertView
						.findViewById(R.id.tvFlightDestinationCity);
				holder.airlineIcon = (ImageView) convertView
						.findViewById(R.id.ivAirlineIcon);
				holder.nextIcon = (ImageView) convertView
						.findViewById(R.id.ivFlightsNext);
				holder.header = (View) convertView
						.findViewById(R.id.flightsHeaderLayout);
				holder.headerText = (TextView) holder.header
						.findViewById(R.id.FlightNameHeader);

				break;

			case TYPE_SEPARATOR:

				convertView = inflater.inflate(R.layout.flightlist_row_header,
						null);
				holder.headerText = (TextView) convertView
						.findViewById(R.id.FlightNameHeader);

				break;

			}

			convertView.setTag(holder);

		}

		else

			holder = (ViewHolder) convertView.getTag();
		// holder.airlineName.setText("Airline Name:  "
		// + mAirlineNames.get(position));
		holder.flightNumber.setText("Flight Number: "
				+ mFlightNumbers.get(position));
		holder.airport.setText("Airport Code: " + mAirports.get(position));
		holder.departureTime.setText("Departure Time: "
				+ mDepartureTimes.get(position));
		holder.arrivalTime.setText("Arrival Time: "
				+ mArrivalTimes.get(position));
		holder.departureCity.setText("From: " + mDepartureCities.get(position));
		holder.arrivalCity.setText("To: " + mArrivalCities.get(position));
		holder.departureTime
				.setText("Leaves: " + mDepartureTimes.get(position));
		holder.arrivalTime.setText("Arrives: " + mArrivalTimes.get(position));
		// holder.airlineIcon
		// .setBackgroundResource(R.drawable.weathermate_airlineicon_default);
		holder.nextIcon.setBackgroundResource(R.drawable.flights_next);
		holder.headerText.setText(mDepartureCities.get(position) + " to "
				+ mArrivalCities.get(position));

		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return mSeparatorSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

}
