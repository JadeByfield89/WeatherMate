package com.brightr.weathermate.fragments;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.view.Menu;
import com.brightr.weathermate.R;
import com.brightr.weathermate.activities.MainActivity;
import com.brightr.weathermate.adapters.WeatherAdapter;
import com.brightr.weathermate.databases.LocationStorage;
import com.brightr.weathermate.fragments.MainWeatherFragment.onLocationDataReceivedListener;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class SidePanelFragment extends Fragment implements OnItemClickListener {

	// ArrayLists
	ArrayList<String> cities = new ArrayList<String>();
	ArrayList<ImageView> icons;

	WeatherAdapter adapter;
	ImageView first;
	ListView locationList;

	private ArrayList<String> mCities;
	private ArrayList<String> mLatitudes;
	private ArrayList<String> mLongitudes;
	private ArrayList<String> mCountries;

	onSavedLocationSelectedListener mListener;

	MainWeatherFragment main = new MainWeatherFragment();
	private int menuItemIndex;
	private int list_position;
	private LocationStorage mStorage;



	LayoutInflater inflater;
	ViewGroup container;
	Bundle savedInstanceState;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getSavedLocationsFromDB();

		View v = inflater.inflate(R.layout.side_panel, container, false);
		locationList = (ListView) v.findViewById(R.id.lvForecast);
		
		View empty = inflater.inflate(R.layout.side_panel_empty_layout, container, false);
		//locationList.setEmptyView(empty);

		// initDataSet();

		adapter = new WeatherAdapter(getActivity(), mCities, mCountries);
		locationList.setAdapter(adapter);

		// set on item click listener to the location list
		locationList.setOnItemClickListener(this);
		
		registerForContextMenu(locationList);

		return v;
	}

	private void getSavedLocationsFromDB() {
		// Open up the locations database and get all the places the user has
		// saved
		try {
			mStorage = new LocationStorage(getActivity());
			mStorage.open();
			mStorage.getLocations();
			mCities = mStorage.getLocationNames();
			mCountries = mStorage.getCountries();
			Log.w("SidePanelFragment",
					"Saved places are --> " + mCities.toString());
			mLatitudes = mStorage.getLatitudes();
			mLongitudes = mStorage.getLongitudes();

			mStorage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Interface that will notify the host activity when the user has requested
	// the weather
	// for a new location on the saved list
	public interface onSavedLocationSelectedListener {

		public void onSavedLocationSelected(String name, String latitude,
				String longitude);
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

		try {
			mListener = (onSavedLocationSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnSavedLocationSelectedListener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		// setRetainInstance(true);
	}

	public SidePanelFragment() {

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		// setRetainInstance(true);
	}



	public void refreshSavedList() {

		mCities.clear();
		mCountries.clear();
		mLatitudes.clear();
		mLongitudes.clear();

		getSavedLocationsFromDB();

		adapter = new WeatherAdapter(getActivity(), mCities, mCountries);
		locationList.setAdapter(adapter);

		// adapter.notifyDataSetChanged();
	}

	

	@Override
	public void onItemClick(AdapterView<?> av, View v, int pos, long id) {

		// Get the string location and pass it to the main fragment to display
		// the weather data
		// for that location
		// Close the location panel
		MainActivity.slideForcecastPanel();

		String name = mCities.get(pos);
		String lat = mLatitudes.get(pos);
		String lon = mLongitudes.get(pos);

		mListener.onSavedLocationSelected(name, lat, lon);
		// main.getWeatherFromSavedLocation(mCities.get(pos),
		// mLatitudes.get(pos),
		// mLongitudes.get(pos));

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		if (v.getId() == R.id.lvForecast) {

			AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

			menu.setHeaderTitle("Options");
			String[] menuItems = { "Remove" };
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}

		}
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {

		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		menuItemIndex = item.getItemId();
		list_position = info.position;
		String[] menuItems = { "Delete" };
		String menuItemName = menuItems[menuItemIndex];
		// String

		removeSavedLocation();

		return true;
	}

	// Remove this lcoation from the list and from the database
	private void removeSavedLocation() {

		try {
			mStorage.open();
			mStorage.removeEntry(list_position + 1);
			mCities.remove(list_position);
			mCountries.remove(list_position);

			adapter.notifyDataSetChanged();
			mStorage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
