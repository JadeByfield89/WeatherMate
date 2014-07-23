package com.brightr.weathermate.fragments;

import com.brightr.weathermate.R;
import com.brightr.weathermate.activities.MainActivity;
import com.brightr.weathermate.adapters.LocationListAdapter;
import com.brightr.weathermate.providers.WeatherProvider;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
//import com.brightr.weathermate.views.PullToRefreshListView;
//import com.brightr.weathermate.views.PullToRefreshListView.OnRefreshListener;

public class MainWeatherFragment extends Fragment implements
		OnItemClickListener, OnRefreshListener {

	// PullToRefreshListView mList;

	// Views for the main panel as weather as the other 4 panels
	View vToday;

	View panel1;

	View panel2;

	View panel3;

	View panel4;

	View panel5;

	// Child Views for each Panel
	// Panel 1
	TextView mLocationName;

	TextView mDate;

	TextView mTempMaxF;

	TextView mTempMinF;

	TextView mTempMaxC;

	TextView mTempMinC;

	TextView mTempF;

	TextView mTempC;

	TextView mCondition;

	TextView mCloudCov;

	TextView mPrecip;

	TextView mWindDir;

	TextView mHumidity;

	TextView mWindspeeds;

	ImageView mIcon;

	// Panel2
	TextView mDay2;

	TextView mDate2;

	ImageView mIcon2;

	TextView mHighLow2;

	// Panel 3

	TextView mDay3;

	TextView mDate3;

	ImageView mIcon3;

	TextView mHighLow3;

	// Panel 4
	TextView mDay4;

	TextView mDate4;

	ImageView mIcon4;

	TextView mHighLow4;

	// Panel 5
	TextView mDay5;

	TextView mDate5;

	ImageView mIcon5;

	TextView mHighLow5;

	boolean isFavorite = false;

	ImageView favStar;

	ProgressBar spinner;

	// Longs for time checking
	long launchTime;

	long currentTime;

	// Location objects
	public static final String TAG = "MainWeatherFragment";

	// Location objects
	Geocoder gcd;

	static double lat;

	static double lon = 0;

	public LocationManager lm;

	public LocationListener locationListener;

	// Arrays
	ArrayList<String> cityNames;

	ArrayList<String> dates;

	int[] icons;

	ArrayList<String> tempsF;

	ArrayList<String> tempsC;

	ArrayList<String> conditions;

	ArrayList<String> cloudCover;

	ArrayList<String> windSpeeds;

	ArrayList<String> visib;

	ArrayList<String> precip;

	ArrayList<String> observationTimes;

	ArrayList<String> humidities;

	ArrayList<String> windSpeedsKmph;

	ArrayList<String> windDirectionsDegrees;

	ArrayList<String> pressures;

	ArrayList<String> tempsMaxF;

	ArrayList<String> tempsMinF;

	ArrayList<String> tempsMaxC;

	ArrayList<String> tempsMinC;

	ProgressDialog pd;

	PullToRefreshListView locationList;

	WeatherProvider weatherProvider;

	// Strings
	public String zip;

	String result;

	String cloud_cover;

	String weather_desc;

	String windSpeedMiles;

	String observationTime;

	String pressure;

	String precipitation;

	String visibility;

	String humidity;

	String windSpeedKmph;

	String windDirDegree;

	String tempC;

	String date;

	public static String cityName;

	// Fragments
	MenuFragment menuFrag;

	// Chars
	char degree = '\u00B0';

	// Adapters
	LocationListAdapter la;

	// ImageViews
	ImageView first;

	Context THIS_CONTEXT;

	String coords;

	public Location location;

	public ArrayList<String> days;

	// Intent i;

	static ArrayList<String> locationNames = new ArrayList<String>();

	static View v;

	int count = 0;

	onLocationDataReceivedListener mListener;

	private String region;

	private String country;

	private String countryName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.weather_layout, container, false);
		// spinner = (ProgressBar) v.findViewById(R.id.progressBar1);

		vToday = v.findViewById(R.id.vToday);
		vToday.setVisibility(View.INVISIBLE);
		panel1 = v.findViewById(R.id.vPanel1);
		panel1.setVisibility(View.INVISIBLE);
		panel2 = v.findViewById(R.id.vPanel2);
		panel2.setVisibility(View.INVISIBLE);
		panel3 = v.findViewById(R.id.vPanel3);
		panel3.setVisibility(View.INVISIBLE);
		panel4 = v.findViewById(R.id.vPanel4);
		panel4.setVisibility(View.INVISIBLE);

		// All of the child views that are apart of today's weather panel
		mLocationName = (TextView) vToday.findViewById(R.id.tvCityName);
		mDate = (TextView) vToday.findViewById(R.id.tvNextDate);
		mTempMaxF = (TextView) vToday.findViewById(R.id.tvtempMaxF);
		mTempMinF = (TextView) vToday.findViewById(R.id.tvtempMinF);
		mTempMaxC = (TextView) vToday.findViewById(R.id.tvtempMaxC);
		mTempMinC = (TextView) vToday.findViewById(R.id.tvtempMinC);
		mTempC = (TextView) vToday.findViewById(R.id.tvTempC);
		mTempF = (TextView) vToday.findViewById(R.id.tvTempF);
		mCondition = (TextView) vToday.findViewById(R.id.tvWeatherCond);
		mCloudCov = (TextView) vToday.findViewById(R.id.tvCloudCov);
		mPrecip = (TextView) vToday.findViewById(R.id.tvPrecip);
		mWindDir = (TextView) vToday.findViewById(R.id.tvWinddirection);
		mHumidity = (TextView) vToday.findViewById(R.id.tvHumidity);
		mWindspeeds = (TextView) vToday.findViewById(R.id.tvWindspeed);
		mIcon = (ImageView) vToday.findViewById(R.id.imgWeatherIcon);

		// now inflate the child views that apart of the other 4 panel panels
		// panel 1
		mDay2 = (TextView) panel1.findViewById(R.id.tvDay);
		mDate2 = (TextView) panel1.findViewById(R.id.tvForecastDate);
		mIcon2 = (ImageView) panel1.findViewById(R.id.ivIcon);
		mHighLow2 = (TextView) panel1.findViewById(R.id.tvHighLow);

		// panel 2
		mDay3 = (TextView) panel2.findViewById(R.id.tvDay);
		mDate3 = (TextView) panel2.findViewById(R.id.tvForecastDate);
		mIcon3 = (ImageView) panel2.findViewById(R.id.ivIcon);
		mHighLow3 = (TextView) panel2.findViewById(R.id.tvHighLow);

		// panel 3
		mDay4 = (TextView) panel3.findViewById(R.id.tvDay);
		mDate4 = (TextView) panel3.findViewById(R.id.tvForecastDate);
		mIcon4 = (ImageView) panel3.findViewById(R.id.ivIcon);
		mHighLow4 = (TextView) panel3.findViewById(R.id.tvHighLow);

		// panel 4
		mDay5 = (TextView) panel4.findViewById(R.id.tvDay);
		mDate5 = (TextView) panel4.findViewById(R.id.tvForecastDate);
		mIcon5 = (ImageView) panel4.findViewById(R.id.ivIcon);
		mHighLow5 = (TextView) panel4.findViewById(R.id.tvHighLow);

		initDataSet();

		pd = new ProgressDialog(this.getActivity());

		// Inflate the location list
		// locationList = (PullToRefreshListView) v.findViewById(R.id.cityList);
		// locationList.setOnRefreshListener(this);

		Log.d(TAG, "onCreateView MainWeatherFragment ");

		return v;
	}

	// Interface that will notify the host activity when the user has requested
	// the weather
	// for a new location
	public interface onLocationDataReceivedListener {

		public void onLocationDataReceived(String name, String country,
				double latitude, double longitude);
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

		try {
			mListener = (onLocationDataReceivedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnLocationDataReceivedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate MainWeatherFragment " + count++);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		Log.d(TAG, "onActivityCreated called! ");

	}

	public void getWeatherFromCoordinates(String coordinates) {

		if (haveNetworkConnection()) {

			new GetWeatherData().execute(coordinates);
			launchTime = System.currentTimeMillis();

		}

		/*
		 * else Toast.makeText(getActivity(),
		 * "Sorry, no network connection found!", Toast.LENGTH_LONG) .show();
		 */

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		Log.d(TAG, "onPause MainWeatherFragment ");

	}

	// When the fragment is resumed, either load the current location or the
	// location that is passed in

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "MainWeatherFragment onResumed ");

		if (haveNetworkConnection()) {

			Log.d(TAG, "hasNetworkConnection TRUE");
			// If the fragment is resumed and the user is returning back from a
			// previous activity
			// and data was passed in, use that data to grab the weather
			try {

				final Intent i = getActivity().getIntent();

				if (i.hasExtra("key"))

				{

					// User is returning back to this Activity from the Search
					// activity
					// with coordinates and city name, get the weather data for
					// this location
					Log.d(TAG,
							"RESUMED --> Returning from SearchActivity --> Getting weather from INTENT data");
					// Latitude and Longitude values were passed in
					// Get those values to use to get the weather
					lat = i.getDoubleExtra("latitude", 0);
					lon = i.getDoubleExtra("longitude", 0);
					region = i.getStringExtra("region");
					country = i.getStringExtra("country");

					countryName = region;
					// Clear the previous data and get the results for the new
					// location requested
					clearDataSet();
					coords = i.getStringExtra("key");
					String city = i.getStringExtra("city");
					// region = i.getStringExtra("region");

					cityName = city;

					getWeatherFromCoordinates(coords);

					// Notify the listener that new location data has been
					// received
					// Which will in turn notify the activity
					mListener.onLocationDataReceived(cityName, countryName,
							lat, lon);

					i.removeExtra("key");
				}

				else {
					if (cityNames.isEmpty()) {

						Log.d(TAG, "RESUMED --> INTENT was NULL");
						Log.d(TAG, "RESUMED --> City Names contains "
								+ cityNames.toString());

						getUsersCurrentLocation();

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy MainWeatherFragment");

		super.onDestroy();
	}

	// Methods that the host activity will access to get data
	// that will be stored in another fragment
	public double getLat() {

		return lat;
	}

	public double getLon() {

		return lon;
	}

	public String getLocation() {

		return cityName;
	}

	public void getUsersCurrentLocation() {
		// get the current location ONLY IF there's a network connection
		if (haveNetworkConnection()) {

			// Get the location manager
			lm = (LocationManager) getActivity().getSystemService(
					Context.LOCATION_SERVICE);

			if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

				showGPSDialog();

				// location =
				// lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}

			else {

				location = lm
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			}

			if (location != null) {
				lat = location.getLatitude();
				lon = location.getLongitude();
			}

			else if (location == null) {
				location = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				lat = location.getLatitude();
				lon = location.getLongitude();
			}

			// Get the name of the city for CURRENT Location
			cityName = getCityName(lat, lon);

			// Notify the callback that new location data has been received for
			// the CURRENT location
			// this way the activity can know to store it in the data
			mListener.onLocationDataReceived(cityName, countryName, lat, lon);

			// Put the latitude and longitude together as one string so we can
			// make the call to
			// the weather API
			coords = lat + "," + lon;
			getWeatherFromCoordinates(coords);

			// Define a listener that responds to location updates
			locationListener = new LocationListener() {

				@Override
				public void onLocationChanged(Location location) {
					// Called when a new location is found by the network
					// provider
					// lat = location.getLatitude();
					// lon = location.getLongitude();

					cityName = getCityName(lat, lon);

					Log.d(TAG, "Location Changed!");

				}

				@Override
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onProviderEnabled(String provider) {

				}

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					// TODO Auto-generated method stub

				}

			};

			// Register the listener with the Location Manager to receive
			// location
			// updates
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10,
					locationListener);

		}

	}

	private void showGPSDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Enable GPS");
		builder.setMessage("GPS not detected. Please enable GPS in your Settings so that WeatherMate can accurately provide weather for your location.");
		builder.setIcon(R.drawable.action_bar_mylocation);
		builder.setPositiveButton("Go To Settings",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
						onPause();

					}
				});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		builder.show();

	}

	@Override
	public void onStart() {

		super.onStart();
		Log.d(TAG, "onStart() called");

	}

	// Method to check if there's active Network Connection
	public boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	// Gets the local name based on latitude and longitude values
	protected String getCityName(double lat2, double lon2) {

		String city = null;

		try {
			// instantiate the geocoder that will gives us the local name
			gcd = new Geocoder(getActivity(), Locale.getDefault());

			List<Address> addresses = gcd.getFromLocation(lat2, lon2, 5);
			if (addresses.size() > 0)
				city = addresses.get(0).getLocality();
		} catch (IOException e) {
			Log.e(TAG, "Error getting city name!");
			e.printStackTrace();
		}

		return city;

	}

	public void initDataSet() {
		// Initialize arrays of data

		icons = new int[5];

		cityNames = new ArrayList<String>();

		dates = new ArrayList<String>();

		days = new ArrayList<String>();

		tempsF = new ArrayList<String>();

		tempsC = new ArrayList<String>();

		conditions = new ArrayList<String>();

		cloudCover = new ArrayList<String>();

		windSpeeds = new ArrayList<String>();

		visib = new ArrayList<String>();

		precip = new ArrayList<String>();

		observationTimes = new ArrayList<String>();
		humidities = new ArrayList<String>();
		windSpeedsKmph = new ArrayList<String>();
		windDirectionsDegrees = new ArrayList<String>();
		pressures = new ArrayList<String>();
		tempsMaxF = new ArrayList<String>();
		tempsMinF = new ArrayList<String>();
		tempsMaxC = new ArrayList<String>();
		tempsMinC = new ArrayList<String>();

	}

	// Clears all objects from arraylists that will be passed in to the
	// adapter
	public void clearDataSet() {

		cityNames.clear();
		dates.clear();
		icons = null;
		tempsF.clear();
		tempsC.clear();
		conditions.clear();
		cloudCover.clear();
		windSpeeds.clear();
		visib.clear();
		precip.clear();
		observationTimes.clear();
		humidities.clear();
		windSpeedsKmph.clear();
		windDirectionsDegrees.clear();
		pressures.clear();
		tempsMaxF.clear();
		tempsMinF.clear();
		tempsMaxC.clear();
		tempsMinC.clear();

		Log.d(TAG, "DATA SET IS CLEAR");

	}

	public void getWeatherFromSavedLocation(String name, String latitude,
			String longitude) {

		final String coordinates = latitude + "," + longitude;

		// clearDataSet();

		// initDataSet();

		cityName = name;
		// isFavorite = true;

		// clearDataSet();
		getWeatherFromCoordinates(coordinates);

	}

	// Get the weather data in the background and
	// display the result in the listview after loading
	public class GetWeatherData extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			clearDataSet();
			showLoadingDialog();
			// showSpinner();

		}

		@Override
		protected String doInBackground(String... Params) {
			// Use the Weather Provider class to get the values for
			// the current and future weather conditions

			weatherProvider = new WeatherProvider(Params[0]);
			weatherProvider.getWeatherData();

			cityNames.add(cityName);
			// Log.w(TAG, "City Name is " + cityName);

			result = weatherProvider.getTempF();
			tempsF.add(result + degree + "F");

			cloud_cover = weatherProvider.getCloudCoverage();
			cloudCover.add("Cloud Coverage: " + cloud_cover);

			tempC = weatherProvider.getTempC();
			tempsC.add(tempC + degree + "C");

			windSpeedMiles = weatherProvider.getWindspeedMiles();
			windSpeeds.add("Windspeed: " + windSpeedMiles + " mph");

			visibility = weatherProvider.getVisibility();
			visib.add("Visibility: " + visibility);

			precipitation = weatherProvider.getPrecipMM();
			precip.add("Precipitation: " + precipitation + " mm");

			pressure = weatherProvider.getPressure();
			pressures.add("Pressure: " + pressure + "Pa.");

			observationTime = weatherProvider.getObservationTime();
			observationTimes.add("Observation Time: " + observationTime);

			humidity = weatherProvider.getHumidity();
			humidities.add("Humidity: " + humidity + " %");

			windSpeedKmph = weatherProvider.getWindspeedKmph();
			windSpeedsKmph.add("/ " + windSpeedKmph + " kmph");

			windDirDegree = weatherProvider.getWinddirDegree();
			windDirectionsDegrees.add("Wind Direction: " + windDirDegree);

			// get values for all days in forecast
			conditions = weatherProvider.getDescriptions();

			icons = weatherProvider.getWeatherIcon();

			dates = weatherProvider.getDates();

			days = weatherProvider.getDayOfWeek();

			tempsMaxF = weatherProvider.getTempsMaxF();

			tempsMinF = weatherProvider.getTempsMinF();

			tempsMaxC = weatherProvider.getTempsMaxC();

			tempsMinC = weatherProvider.getTempsMinC();

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// populate the textviews with the weather data

			populateViews();

			// Try to set the adapter

			/*
			 * la = new LocationListAdapter(getActivity(), cityNames, dates,
			 * days, icons, tempsF, tempsC, conditions, cloudCover, windSpeeds,
			 * precip, humidities, windSpeedsKmph, windDirectionsDegrees,
			 * tempsMaxF, tempsMinF, tempsMaxC, tempsMinC);
			 * locationList.setAdapter(la); la.notifyDataSetChanged();
			 * locationList.invalidateViews(); if (locationList.getVisibility()
			 * == 4) { locationList.setVisibility(View.VISIBLE); //
			 * la.notifyDataSetChanged(); locationList.onRefreshComplete(); }
			 */

			dismissLoadingDialog();
			// hideSpinner();
			// Log.w(TAG, "Dialog Dismissed in onPostExecute()");

		}

	}

	private void populateViews() {

		// First panel
		mLocationName.setText(cityName);
		mDate.setText("Today");
		mTempMaxF.setText("H " + tempsMaxF.get(0));
		mTempMinF.setText("L " + tempsMinF.get(0));
		mTempMaxC.setText("H " + tempsMaxC.get(0));
		mTempMinC.setText("L " + tempsMinC.get(0));

		mTempC.setText(tempsC.get(0));
		changeTextColor(mTempC, "yellow");
		mTempF.setText(tempsF.get(0));
		changeTextColor(mTempF, "yellow");
		mIcon.setBackgroundResource(icons[0]);
		mCondition.setText(conditions.get(0));
		mCloudCov.setText(cloudCover.get(0));
		mPrecip.setText(precip.get(0));
		mWindDir.setText(windDirectionsDegrees.get(0));
		mHumidity.setText(humidities.get(0));
		mWindspeeds.setText(windSpeeds.get(0));

		// Panel 1
		mDate2.setText(dates.get(1));
		mDay2.setText(days.get(1));
		mIcon2.setBackgroundResource(icons[1]);
		mHighLow2.setText(tempsMaxF.get(1) + "/" + tempsMinF.get(1));

		// Panel 2
		mDate3.setText(dates.get(2));
		mDay3.setText(days.get(2));
		mIcon3.setBackgroundResource(icons[2]);
		mHighLow3.setText(tempsMaxF.get(2) + "/" + tempsMinF.get(2));

		// Panel 3
		mDate4.setText(dates.get(3));
		mDay4.setText(days.get(3));
		mIcon4.setBackgroundResource(icons[3]);
		mHighLow4.setText(tempsMaxF.get(3) + "/" + tempsMinF.get(3));

		// Panel 4
		mDate5.setText(dates.get(4));
		mDay5.setText(days.get(4));
		mIcon5.setBackgroundResource(icons[4]);
		mHighLow5.setText(tempsMaxF.get(4) + "/" + tempsMinF.get(4));

		makeViewsVisible();

	}

	private void makeViewsVisible() {
		vToday.setVisibility(View.VISIBLE);
		panel1.setVisibility(View.VISIBLE);
		panel2.setVisibility(View.VISIBLE);
		panel3.setVisibility(View.VISIBLE);
		panel4.setVisibility(View.VISIBLE);

	}

	private void changeTextColor(TextView text, String color) {
		// Change the first 2 characters of this textview to the color yellow
		// Also sets the color according to user settings
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
	public void onItemClick(AdapterView<?> av, View v, int pos, long id) {

		MainActivity.slideForcecastPanel();

	}

	// The list view was pulled to be refreshed
	@Override
	public void onRefresh() {

		currentTime = System.currentTimeMillis();

		// clearDataSet();

		if (isRefreshIntervalMet()) {
			// ListView was pulled to be refreshed
			locationList.setVisibility(View.INVISIBLE);
			// showLoadingDialog();
			new GetWeatherData().execute(lat + "," + lon);
		}

		else {
			locationList.onRefreshComplete();
			Toast.makeText(getActivity(),
					"Please refresh only at 15 minute intervals",
					Toast.LENGTH_SHORT).show();
		}

	}

	public String getLocationName() {

		return cityName;

	}

	public double getLatitude() {

		return lat;
	}

	public double getLongitude() {

		return lon;
	}

	// Ensures that the listview can only be refreshed once every 15 min
	// Mainly for development purposes
	public boolean isRefreshIntervalMet() {

		if (currentTime - launchTime < 900000)
			return false;
		else
			return true;
	}

	public void showLoadingDialog() {

		pd.setTitle("Getting Weather");
		pd.setMessage("Gathering weather data...");

		pd.show();

	}

	public void dismissLoadingDialog() {
		Log.d(TAG, "Dialog dismissed!");
		pd.dismiss();

	}

}
