package com.brightr.weathermate.activities;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.brightr.weathermate.R;
import com.brightr.weathermate.adapters.SearchListAdapter;
import com.brightr.weathermate.providers.LocationSearchProvider;

import eu.erikw.PullToRefreshListView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchLocationsActivity extends SherlockActivity implements
		OnItemClickListener {

	private static final String TAG = "SearchLocationsActivity";

	// Alert Dialog
	AlertDialog ad;
	EditText searchBox;

	// Action Bar
	ActionBar actionBar;

	// ListView
	eu.erikw.PullToRefreshListView results;

	// Adapter
	SearchListAdapter adapter;

	ArrayList<String> locations;
	ArrayList<String> latitudes;
	ArrayList<String> longitudes;
	ArrayList<String> regions;
	ArrayList<String> countries;
	ArrayList<String> populations;

	EditText searchField;
	Button searchButton;

	String query;

	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);

		initDataSet();

		// Get the string containing the location coordinates that was passed in
		// from
		// the previous Activity

		String location = getIntent().getStringExtra("key");

		// Set up the Action Bar
		actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.weathermate_actionbar_4));
		actionBar.setTitle(location);

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(R.drawable.map_icon);

		results = (PullToRefreshListView) findViewById(R.id.locationList);
		results.setOnItemClickListener(this);
		results.setHeaderDividersEnabled(false);

		// Load the search results in the background and then publish the
		// results

		new GetCityData().execute(location);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.search_locations_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_locations_search:

			showSearchDialog();

			break;

		case android.R.id.home:

			super.onBackPressed();

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {

		super.onResume();

		Log.d(TAG, "onResume called");

	}

	private void showSearchDialog() {

		AlertDialog.Builder searchDialog = new AlertDialog.Builder(this);
		// ad = searchDialog.create();
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.search_dialog, null);

		// Inflate the search box
		EditText search = (EditText) v.findViewById(R.id.etEnterSearch);
		// search.setHint("City/Zip/IP Address/Lat,Lon");

		// Inflate the radio group and add it's buttons
		RadioGroup rg = (RadioGroup) v.findViewById(R.id.rgSearchOptions);
		RadioButton[] group = new RadioButton[4];

		group[0] = new RadioButton(this);
		group[1] = new RadioButton(this);
		group[2] = new RadioButton(this);
		group[3] = new RadioButton(this);

		group[0].setText("City Name");
		group[1].setText("Zip Code");
		group[2].setText("IP Address");
		group[3].setText("Latitude,Longitude(no spaces)");

		rg.addView(group[0]);
		rg.addView(group[1]);
		rg.addView(group[2]);
		rg.addView(group[3]);

		searchBox = (EditText) v.findViewById(R.id.etEnterSearch);

		searchDialog.setView(v);

		searchDialog.setTitle("Search");
		searchDialog.setPositiveButton("Search",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						String query = searchBox.getText().toString();

						if (query.contains(" ")) {

							Toast.makeText(
									getActivity(),
									"Search Query cannot contain spaces, please use zip code instead!",
									Toast.LENGTH_LONG).show();
						} else {

							actionBar.setTitle(query);
							new GetCityData().execute(query);
						}

					}
				});

		searchDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						ad.dismiss();

					}
				});
		ad = searchDialog.show();

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause called");
		clearPreviousSearch();

	}

	private Activity getActivity() {
		return this;
	}

	private void initDataSet() {
		locations = new ArrayList<String>();
		latitudes = new ArrayList<String>();
		longitudes = new ArrayList<String>();
		regions = new ArrayList<String>();
		countries = new ArrayList<String>();
		populations = new ArrayList<String>();

	}

	private void clearPreviousSearch() {

		countries.clear();
		populations.clear();
		latitudes.clear();
		longitudes.clear();
		regions.clear();
		locations.clear();

	}

	private class GetCityData extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			String send = params[0];

			try {

				LocationSearchProvider.getCityData(send);

				countries = LocationSearchProvider.getCountryNames();
				populations = LocationSearchProvider.getPopulations();
				longitudes = LocationSearchProvider.getLongitudes();
				latitudes = LocationSearchProvider.getLatitudes();
				regions = LocationSearchProvider.getRegions();
				locations = LocationSearchProvider.getAreaNames();

			} catch (Exception e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						Toast.makeText(getActivity(),
								"Error Getting Location Data!",
								Toast.LENGTH_LONG).show();
					}

				});
			}

			return send;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			adapter = new SearchListAdapter(getActivity(), countries,
					locations, populations, latitudes, longitudes, regions);
			adapter.notifyDataSetChanged();
			results.setAdapter(adapter);

			dismissLoadingDialog();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			clearPreviousSearch();
			showLoadingDialog();
		}

	}

	private void showLoadingDialog() {

		pd = new ProgressDialog(getActivity());
		pd.setTitle("Searching");
		pd.setMessage("Getting Results....");
		pd.show();

	}

	private void dismissLoadingDialog() {
		pd.dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> av, View v, int pos, long id) {

		String lat = LocationSearchProvider.getLatitudeAt(pos);
		String lon = LocationSearchProvider.getLongitudeAt(pos);
		String city = LocationSearchProvider.getAreaNameAt(pos);
		String region = LocationSearchProvider.getRegionAt(pos);
		String country = countries.get(pos);

		String fullName = region + ", " + country;

		double latitude;
		double longitude;

		latitude = Double.parseDouble(lat);
		longitude = Double.parseDouble(lon);

		String param = lat + "," + lon;

		Intent i = new Intent(this, MainActivity.class);
		i.putExtra("key", param);
		i.putExtra("city", city);
		i.putExtra("latitude", latitude);
		i.putExtra("longitude", longitude);
		i.putExtra("region", fullName);
		i.putExtra("country", country);

		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(i);
		this.finish();

	}

}
