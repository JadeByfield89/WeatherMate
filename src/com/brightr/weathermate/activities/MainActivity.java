package com.brightr.weathermate.activities;

/**
 * 
 * @author Jade
 */

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.brightr.weathermate.R;
import com.brightr.weathermate.databases.LocationStorage;
import com.brightr.weathermate.fragments.MainWeatherFragment;
import com.brightr.weathermate.fragments.MainWeatherFragment.onLocationDataReceivedListener;
import com.brightr.weathermate.fragments.MenuFragment;
import com.brightr.weathermate.fragments.MenuFragment.onMenuItemSelectedListener;
import com.brightr.weathermate.fragments.SidePanelFragment;
import com.brightr.weathermate.fragments.SidePanelFragment.onSavedLocationSelectedListener;
import com.deaux.fan.FanView;

import eu.erikw.PullToRefreshListView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//import eu.erikw.PullToRefreshListView;

/*MainActivity that will host two fragments(the menu and the sliding panel)
 * This Activity will be notified whenever anything is selected in either fragment
 */

public class MainActivity extends SherlockFragmentActivity implements
		onMenuItemSelectedListener, onLocationDataReceivedListener,
		onSavedLocationSelectedListener {

	RelativeLayout parent;

	// ActionBar
	ActionBar actionBar;

	// Fan view for sliding fragments
	static FanView fan;

	// Fragments
	MenuFragment menuFrag;
	SidePanelFragment sideFrag;
	MainWeatherFragment main;
	FragmentManager fm;
	FragmentTransaction ft;

	// Booleans
	private static boolean isMenuVisible = false;
	public static boolean isSidePanelVisible = false;

	static View lv;
	PullToRefreshListView listView;

	// Animations
	private Animation animUp;

	private ProgressDialog pd;

	private static Animation animDown;

	// Alert searchDialog;
	AlertDialog ad;

	private EditText searchBox;

	private String mLocation;

	private double mLatitude;

	private double mLongitude;

	private double lat;

	private double lon;

	private boolean isFromSavedList = false;

	private String mCountry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get the action bar and set the background
		actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.weathermate_actionbar_4));
		actionBar.setTitle("");
		actionBar.setIcon(R.drawable.weather_icon_partlycloudy);

		// Fan view that will set the sliding side panel
		fan = (FanView) findViewById(R.id.fan_view);

		// Get instance of menu fragment object
		main = new MainWeatherFragment();
		menuFrag = new MenuFragment();
		sideFrag = new SidePanelFragment();

		fan.setFragments(main, sideFrag);

		// set up the menu fragment
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();

		ft.add(R.id.weathermateFragContainer, menuFrag, "Frags");

		ft.commit();

		// load the fragment animations
		animUp = AnimationUtils.loadAnimation(this, R.anim.anim_up);
		animDown = AnimationUtils.loadAnimation(this, R.anim.anim_down);

		// listView = MainWeatherFragment.getListView();

		parent = (RelativeLayout) findViewById(R.id.weathermateFragContainer);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_refresh:

			fan.showMenu();
			isSidePanelVisible = true;
			return true;

		case R.id.menu_map:

			showTrafficInMap();

			return true;

		case R.id.menu_search:

			showSearchDialog();

			return true;

		case R.id.menu_more:

			return true;

		case R.id.menu_flights:

			Intent intent = new Intent(this, MyFlightsActivity.class);
			startActivity(intent);

			return true;

		case R.id.menu_social:

			Intent social = new Intent(this, SocialMediaActivity.class);
			startActivity(social);

			return true;

		case R.id.menu_settings:

			Intent settings = new Intent(this, SettingsActivity.class);
			startActivity(settings);

			return true;

		case R.id.menu_news:

			Intent newsIntent = new Intent(this, NewsViewerActivity.class);
			startActivity(newsIntent);

			return true;

		case R.id.menu_traffic:

			showTrafficInMap();

			return true;

		case R.id.menu_addLocation:

			addLocationToFavorites();

			break;

		case R.id.menu_about:

			showAboutDialog();

			break;

		}
		return super.onOptionsItemSelected(item);
	}

	public static void showMenu() {

		fan.showMenu();
	}

	public static boolean isPanelVisible() {

		if (isSidePanelVisible) {

			return true;

		}

		else
			return false;
	}

	private void addLocationToFavorites() {

		// Change the coordinates we just received to string values to store in
		// the database

		String latitude = "" + mLatitude;
		String longitude = "" + mLongitude;

		// open the database
		try {
			LocationStorage mStorage = new LocationStorage(this);
			mStorage.open();

			mStorage.insertData(mLocation, mCountry, latitude, longitude);

			mStorage.close();

			sideFrag.refreshSavedList();

			Toast.makeText(this, mLocation + " saved to places",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("MainActivity", "Error storing location data in database");
		}

	}

	private void showAboutDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.about_dialog, null);
		final TextView aboutText = (TextView) v.findViewById(R.id.tvaboutText);
		v.findViewById(R.id.tvDisclaimerText);

		builder.setTitle("About");
		builder.setView(v);

		aboutText.setText(Html
				.fromHtml("&copy Copyright 2013 BRIGHTR Apps \n \n"
						+ readRawTextFile(R.raw.about)));
		aboutText.setLinkTextColor(Color.WHITE);
		Linkify.addLinks(aboutText, Linkify.ALL);
		builder.setPositiveButton("Rate App",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						startActivity(new Intent(
								Intent.ACTION_VIEW,
								Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=com.brightr.weathermate")));

					}
				});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();

					}
				});

		builder.show();

	}

	private void showSearchDialog() {

		AlertDialog.Builder searchDialog = new AlertDialog.Builder(this);
		// ad = searchDialog.create();
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.search_dialog, null);

		v.findViewById(R.id.etEnterSearch);

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

						// Get search query and pass to location search activity
						String query = searchBox.getText().toString();
						Intent i = new Intent(getApplicationContext(),
								SearchLocationsActivity.class);
						if (query.contains(" ")) {

							Toast.makeText(
									getApplicationContext(),
									"Search Query cannot contain spaces, please use zip code instead!",
									Toast.LENGTH_LONG).show();
						} else {
							i.putExtra("key", query);
							startActivity(i);
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
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);

	}

	// Required for implementation of custom menu
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {

			// get the menu fragment View
			lv = menuFrag.getGridView();

			// if the menu is already visuble, slide it down
			// if not, slide it up

			if (!isMenuVisible) {
				slideMenuUp();
			}

			else
				slideMenuDown();

			return true;

		}

		else
			return super.onKeyDown(keyCode, event);

	}

	public static void slideForcecastPanel() {

		if (isMenuVisible) {

			slideMenuDown();
			isMenuVisible = false;
			fan.showMenu();
		}

		fan.showMenu();

	}

	// Handy methods to slide the menu up and down
	private static void slideMenuDown() {

		lv.setVisibility(View.INVISIBLE);
		lv.startAnimation(animDown);
		isMenuVisible = false;

	}

	private void slideMenuUp() {

		lv.setVisibility(View.VISIBLE);
		lv.startAnimation(animUp);
		isMenuVisible = true;

	}

	// An item in the menu fragment was selected
	// Depending on it's position, start the required activity

	@Override
	public void onMenuItemSelected(int position) {

		slideMenuDown();
		switch (position) {

		case 0:

			Intent i = new Intent(this, MyFlightsActivity.class);
			startActivity(i);

			break;

		case 1:

			showTrafficInMap();

			break;

		case 2:

			Intent intent = new Intent(this, NewsViewerActivity.class);
			startActivity(intent);

			break;

		case 3:

			Intent social = new Intent(this, SocialMediaActivity.class);
			startActivity(social);

			break;

		case 4:

			showAboutDialog();

			break;

		case 5:

			Intent settings = new Intent(this, SettingsActivity.class);
			startActivity(settings);

			break;

		}

	}

	public String readRawTextFile(int id) {
		InputStream inputStream = getResources().openRawResource(id);

		InputStreamReader in = new InputStreamReader(inputStream);
		BufferedReader buf = new BufferedReader(in);
		String line;
		StringBuilder text = new StringBuilder();
		try {

			while ((line = buf.readLine()) != null)
				text.append(line);
		} catch (IOException e) {
			return null;
		}
		return text.toString();
	}

	// Get the current location and coordinates
	// and also start the map activity that will show the location + traffic,
	// etc
	private void showTrafficInMap() {
		String location = main.getLocation();

		// First check to make sure if the location about to be shown in the map
		// is from the user's
		// favorites list or not
		if (!isFromSavedList) {
			lat = main.getLat();
			lon = main.getLon();
		}
		boolean setTraffic = true;

		// Toast.makeText(this, "lat is " + lat + " lon is " +
		// lon,Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, LocationMapview.class);
		intent.putExtra("name", location);
		intent.putExtra("latitude", lat);
		intent.putExtra("longitude", lon);
		intent.putExtra("traffic", setTraffic);

		startActivity(intent);

	}

	// Callback from weather fragment to notify this activity that new data is
	// available
	@Override
	public void onLocationDataReceived(String name, String country,
			double latitude, double longitude) {
		// Add code here to store location name and coordinates in a database

		mLocation = name;
		mLatitude = latitude;
		mLongitude = longitude;
		mCountry = country;

	}

	@Override
	public void onSavedLocationSelected(String name, String latitude,
			String longitude) {

		// A location from the saved panel has been selected and passed into the
		// activity
		// convert the coordinates to doubles so that we can pass them into the
		// mapView activity
		double dLat = Double.parseDouble(latitude);
		double dLon = Double.parseDouble(longitude);

		isFromSavedList = true;

		lat = dLat;
		lon = dLon;
		main.getWeatherFromSavedLocation(name, latitude, longitude);
	}

}
