package com.brightr.weathermate.adapters;

import java.util.ArrayList;

import com.brightr.weathermate.R;
import com.brightr.weathermate.activities.MyFlightsActivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SavedFlightsAdapter extends BaseAdapter {

	Activity mContext;
	ArrayList<String> mFlightNumbers;
	ArrayList<String> mFromTo;
	ArrayList<String> mTimeLeaves;
	ArrayList<String> mTimeArrives;
	ArrayList<String> mArrivals;
	LayoutInflater inflater;

	public SavedFlightsAdapter(Activity mContext,
			ArrayList<String> mFlightNumbers, ArrayList<String> mFromTo, ArrayList<String> mArrivals,
			ArrayList<String> mTimeLeaves, ArrayList<String> mTimeArrives) {

		this.mContext = mContext;
		this.mFlightNumbers = mFlightNumbers;
		this.mFromTo = mFromTo;
		this.mTimeLeaves = mTimeLeaves;
		this.mTimeArrives = mTimeArrives;
        this.mArrivals = mArrivals;
		inflater = mContext.getLayoutInflater();
	}

	@Override
	public int getCount() {

		return mFlightNumbers.size();
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

		TextView fromTo;
		TextView flightNumber;
		TextView timeLeaves;
		TextView timeArrives;
		View savedHeader;
		TextView headerText;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {

			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.saved_flights_row, null);
			holder.fromTo = (TextView) convertView
					.findViewById(R.id.tvSavedFromTo);
			holder.flightNumber = (TextView) convertView
					.findViewById(R.id.tvSavedFlightNumber);
			holder.timeLeaves = (TextView) convertView
					.findViewById(R.id.tvSavedLeaves);
			holder.timeArrives = (TextView) convertView
					.findViewById(R.id.tvSavedArrives);
			holder.savedHeader = (View)convertView.findViewById(R.id.savedFlightsHeader);
			holder.headerText = (TextView) holder.savedHeader.findViewById(R.id.FlightNameHeader);
			

			convertView.setTag(holder);

		}

		else

			holder = (ViewHolder) convertView.getTag();
		holder.fromTo.setText("Departing from " + mFromTo.get(position));
		holder.flightNumber.setText("Flight Number: "
				+ mFlightNumbers.get(position));
		holder.timeLeaves.setText("Leaves: " + mTimeLeaves.get(position));
		holder.timeArrives.setText("Arrives: " + mTimeArrives.get(position));
		holder.headerText.setText(mFromTo.get(position) + " to " + mArrivals.get(position));
		
		
		


		
		

		return convertView;
	}

}
