package com.brightr.weathermate.activities;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.brightr.weathermate.R;
import com.brightr.weathermate.adapters.FlightListAdapter;
import com.brightr.weathermate.adapters.SavedFlightsAdapter;
import com.brightr.weathermate.databases.FlightStorage;
import com.brightr.weathermate.providers.FlightTrackingProvider;
import com.brightr.weathermate.utils.AirlineNamesCollection;

import eu.erikw.PullToRefreshListView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MyFlightsActivity extends SherlockActivity implements
		OnItemClickListener, OnCheckedChangeListener, OnClickListener {

	//
	private SharedPreferences sharedPrefs;

	int QUERY_TYPE;

	// Flight tracking provider
	FlightTrackingProvider mFlightTrackingProvider = new FlightTrackingProvider();

	// Flight details database
	FlightStorage storage = new FlightStorage(this);

	// Adapters
	SavedFlightsAdapter savedAdapter;

	// ArrayLists
	static ArrayList<String> mAirlineNames;
	static ArrayList<String> mFlightNumbers;
	static ArrayList<String> mAirports;
	static ArrayList<String> mDepartureTimes;
	static ArrayList<String> mArrivalTimes;
	static ArrayList<String> mDepartureCities;
	static ArrayList<String> mArrivalCities;
	static ArrayList<String> mFlightIds;

	// ActionBar
	ActionBar actionBar;

	static TabHost tabHost;
	PullToRefreshListView flightsList;
	PullToRefreshListView flightsSavedList;
	TextView emptyText;
	TextView detailText;

	TabWidget tabWidget;

	FlightListAdapter mFlightAdapter;

	// Progress dialog
	ProgressDialog pd;
	protected String search;

	String maxResults = "1";

	// Flight Details layout

	View detailsView;
	Button saveButton;
	View flightHeader1;
	TextView header1Text;
	View flightHeader2;
	TextView header2Text;
	View flightHeader3;
	TextView header3Text;
	TextView flightsempty;

	static TextView originAirport;
	static TextView dateTime;
	static TextView aircraftType;
	static TextView terminalDeparture;
	static TextView gateDeparture;

	static TextView destinationAirport;
	static TextView dateTimeArrival;
	static TextView terminalArrival;
	static TextView gateArrival;
	static TextView baggageClaim;

	static TextView planeSpeed;
	static TextView planeAltitude;

	private static int flight_position;
	public static ArrayList<String> mOriginCities;
	public static ArrayList<String> mArrivalAirports;
	public static ArrayList<String> mAircraftTypes;
	public static ArrayList<String> mPlaneSpeedsKnot;
	public static ArrayList<String> mPlaneSpeedsMach;
	public static ArrayList<String> mPlaneAltitudes;

	private ArrayList<String> dbFlightNumbers;

	private ArrayList<String> dbAircraftTypes;

	private ArrayList<String> dbDepartureAirports;

	private ArrayList<String> dbDepartureCities;

	private ArrayList<String> dbDepartureDateTimes;

	private ArrayList<String> dbDepartureTerminals;

	private ArrayList<String> dbDepartureGates;

	private ArrayList<String> dbArrivalCities;

	private ArrayList<String> dbArrivalAirports;

	private ArrayList<String> dbArrivalDateTimes;

	private ArrayList<String> dbArrivalTerminals;

	private ArrayList<String> dbArrivalGates;

	private ArrayList<String> dbBaggageClaims;

	private ArrayList<String> dbPlaneSpeeds;

	private ArrayList<String> dbPlaneAltitudes;

	private ArrayList<String> dbFlightIds;

	private int saved_list_position;

	private int menuItemIndex;

	private int context_saved_pos;

	private HashMap<String, String> mAirlinesMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flights_layout);
		actionBar = getSupportActionBar();
		actionBar.setTitle("My Flights");
		actionBar.setIcon(getResources().getDrawable(R.drawable.falcon));
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.weathermate_actionbar_4));

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		tabHost = (TabHost) findViewById(R.id.FlightsTabHost);

		tabHost.setup();

		// Extract the ListView from the TabHost
		flightsList = (eu.erikw.PullToRefreshListView) findViewById(R.id.FlightsList);

		flightsSavedList = (eu.erikw.PullToRefreshListView) findViewById(R.id.FlightsSavedList);

		// Extract the details layout
		// flightsempty = (TextView)findViewById(R.id.tvFlightListEmpty);
		// / flightsList.setEmptyView(flightsempty);
		detailsView = findViewById(R.id.detailsLayout);

		flightHeader1 = detailsView.findViewById(R.id.flightsHeader1);
		header1Text = (TextView) flightHeader1
				.findViewById(R.id.FlightNameHeader);
		header1Text.setText("Origin");

		flightHeader2 = detailsView.findViewById(R.id.flightHeader2);
		header2Text = (TextView) flightHeader2
				.findViewById(R.id.FlightNameHeader);
		header2Text.setText("Destination");

		flightHeader3 = detailsView.findViewById(R.id.flightHeader3);
		header3Text = (TextView) flightHeader3
				.findViewById(R.id.FlightNameHeader);
		header3Text.setText("More Info");

		// Details tab views
		originAirport = (TextView) detailsView
				.findViewById(R.id.tvFlightOriginAirport);

		dateTime = (TextView) detailsView
				.findViewById(R.id.tvFlightDepartureTime);

		aircraftType = (TextView) detailsView
				.findViewById(R.id.tvFlightAircraftType);

		terminalDeparture = (TextView) detailsView
				.findViewById(R.id.tvFlightTerminal);

		gateDeparture = (TextView) detailsView.findViewById(R.id.tvFlightGate);

		destinationAirport = (TextView) detailsView
				.findViewById(R.id.tvFlightDestinationAirport);

		dateTimeArrival = (TextView) detailsView
				.findViewById(R.id.tvFlightArrivalTime);

		terminalArrival = (TextView) detailsView
				.findViewById(R.id.tvFlightArrivalTerminal);

		gateArrival = (TextView) detailsView
				.findViewById(R.id.tvFlightArrivalGate);

		baggageClaim = (TextView) detailsView
				.findViewById(R.id.tvFlightBaggageClaim);

		planeSpeed = (TextView) detailsView.findViewById(R.id.tvFlightSpeed);

		planeAltitude = (TextView) detailsView
				.findViewById(R.id.tvFlightAltitude);

		saveButton = (Button) detailsView.findViewById(R.id.bFlightsSave);
		saveButton.setOnClickListener(this);

		initDataSet();

		// Set click listeners to both listviews

		flightsList.setOnItemClickListener(this);
		flightsSavedList.setOnItemClickListener(this);
		registerForContextMenu(flightsSavedList);
		// tabHost.getTabWidget().setDividerDrawable(getResources().getDrawable(R.drawable.layout_color));

		setupTab(flightsList, "Flights");
		setupTab(detailsView, "Details");
		setupTab(flightsSavedList, "Saved");
		// detailsView.setVisibility(View.INVISIBLE);

		tabHost.setCurrentTab(1);
		tabHost.setCurrentTab(2);
		tabHost.setCurrentTab(0);

		getSavedFlightsFromDB();

		pd = new ProgressDialog(MyFlightsActivity.this);

	}

	private void getSavedFlightsFromDB() {

		// Retrieves saved flights from database and populates the "saved"
		// listview with that data
		try {

			storage.open();
			storage.getFlightDetails();

			dbFlightNumbers = storage.getFlightNumbers();
			dbAircraftTypes = storage.getAircraftTypes();
			dbDepartureAirports = storage.getDepartureAirports();
			dbDepartureCities = storage.getDepartureCities();
			dbDepartureDateTimes = storage.getDepartureDateTimes();
			dbDepartureTerminals = storage.getDepartureTerminals();
			dbDepartureGates = storage.getDepartureGates();
			dbArrivalCities = storage.getArrivalCities();
			dbArrivalAirports = storage.getArrivalAirports();
			dbArrivalDateTimes = storage.getArrivalDateTimes();
			dbArrivalTerminals = storage.getArrivalTerminals();
			dbArrivalGates = storage.getArrivalGates();
			dbBaggageClaims = storage.getBaggageClaims();
			dbPlaneSpeeds = storage.getPlaneSpeeds();
			dbPlaneAltitudes = storage.getPlaneAltitudes();
			dbFlightIds = storage.getFlightIds();

			savedAdapter = new SavedFlightsAdapter(this, dbFlightNumbers,
					dbDepartureCities, dbArrivalCities, dbDepartureDateTimes,
					dbArrivalDateTimes);
			flightsSavedList.setAdapter(savedAdapter);

			if (!dbFlightNumbers.isEmpty()) {

				tabHost.setCurrentTab(2);
			}

			Log.w("MyFlightsActivity", "Flight Details retrieved from DATABASE");

			storage.close();

		} catch (Exception e) {
			e.printStackTrace();
			Log.w("MyFlightsActivity",
					"Error retrieving flight details from DB");
		}

	}

	private void initDataSet() {
		mAirlineNames = new ArrayList<String>();
		mAirports = new ArrayList<String>();
		mArrivalCities = new ArrayList<String>();
		mArrivalTimes = new ArrayList<String>();
		mDepartureCities = new ArrayList<String>();
		mDepartureTimes = new ArrayList<String>();
		mFlightNumbers = new ArrayList<String>();

	}

	private void setupTab(final View v, final String tag) {
		View tabView = createTabView(tabHost.getContext(), tag);
		TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabView)
				.setContent(new TabContentFactory() {

					@Override
					public View createTabContent(String tag) {

						return v;
					}

				});

		tabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.flightsTabsText);
		tv.setText(text);
		tv.setTextColor(Color.WHITE);
		return view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.flights_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.flights_search:

			showFlightSearchDialog();

			break;

		case android.R.id.home:

			super.onBackPressed();

			break;

		case R.id.flights_info:

			showInfoDialog();

			break;
		}
		return true;
	}

	private void showInfoDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Info");
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.flights_info_dialog, null);

		builder.setView(v);
		builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		builder.show();

	}

	private void showFlightSearchDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.flight_search_layout, null);

		// Inflate the edit texts
		final AutoCompleteTextView airlineBox = (AutoCompleteTextView) v
				.findViewById(R.id.etFlightSearchAirline);
		final EditText flightNumberBox = (EditText) v
				.findViewById(R.id.etFlightSearchFlightNumber);

		// Get the map of the airline data and populate the autocomplete
		// textview list with it

		AirlineNamesCollection.popMap();
		mAirlinesMap = AirlineNamesCollection.getAirlinesMap();
		final ArrayList<String> airlineList = new ArrayList<String>(
				mAirlinesMap.keySet());
		Log.w("MyFlightsActivity", "HASH MAP SIZE ---> " + mAirlinesMap.size());

		// Set the adapter to the textview so the dropdown menu appears
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				MyFlightsActivity.this,
				android.R.layout.simple_dropdown_item_1line, airlineList);
		airlineBox.setAdapter(adapter);

		final Spinner querySpinner = (Spinner) v
				.findViewById(R.id.flightsQueryTypeSpinner);
		final String[] types = { "Airline Name", "ICAO Code" };
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, types);
		querySpinner.setAdapter(spinnerAdapter);
		querySpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> av, View v,
							int position, long id) {

						switch (position) {

						case 0:

							QUERY_TYPE = 0;

							break;

						case 1:

							QUERY_TYPE = 1;

							break;
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		// Inflate the radio group
		RadioGroup maxNumberRG = (RadioGroup) v
				.findViewById(R.id.rgFlightSearchMaxResults);
		RadioButton[] radioButtons = new RadioButton[3];
		maxNumberRG.setOnCheckedChangeListener(this);

		radioButtons[0] = new RadioButton(this);
		radioButtons[1] = new RadioButton(this);
		radioButtons[2] = new RadioButton(this);

		radioButtons[0].setText("5");
		radioButtons[0].setId(0);
		radioButtons[1].setText("10");
		radioButtons[1].setId(1);
		radioButtons[2].setText("Show All");
		radioButtons[2].setId(2);

		maxNumberRG.addView(radioButtons[0]);
		maxNumberRG.addView(radioButtons[1]);
		maxNumberRG.addView(radioButtons[2]);

		builder.setView(v);
		builder.setTitle("Flight Lookup");
		builder.setPositiveButton("Search",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						// When the user selects "search" to search for a
						// flight, extract the data
						// they are looking for, but parse the airline name into
						// an ICAO code first
						String airline = airlineBox.getText().toString();

						String flightNumber = flightNumberBox.getText()
								.toString();

						// If the query type is the airline name string, first
						// grab the icao code
						if (QUERY_TYPE == 0) {

							String code = getAirlineICAOCode(airline);

							search = code + flightNumber;

						}

						// else if they already entered the ICAO code, just use
						// that//
						else if (QUERY_TYPE == 1) {

							search = airline + flightNumber;
						}

						// Add code to parse airline string into ICAO code and
						// add that with flight number

						// Toast.makeText(MyFlightsActivity.this, "QUERY IS " +
						// search, Toast.LENGTH_SHORT).show();
						new GetFlightData().execute(search, maxResults);
					}
				});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();

					}
				});

		final AlertDialog dialog = builder.show();
	}

	// This method will search through the hash map to find a matching ICAO code
	// for the airline name the user entered
	protected String getAirlineICAOCode(String airline) {

		String mICAO_CODE = "";

		for (int i = 0; i < mAirlinesMap.size(); i++) {

			mICAO_CODE = mAirlinesMap.get(airline);

		}

		return mICAO_CODE;

	}

	@Override
	public void onItemClick(AdapterView<?> av, View v, int position, long id) {

		int ident = av.getId();

		if (ident == R.id.FlightsList) {

			tabHost.setCurrentTab(1);

			flight_position = position;

			detailsView.setVisibility(View.VISIBLE);
			new GetFlightDetails().execute();

		}

		else if (ident == R.id.FlightsSavedList) {

			saved_list_position = position;
			new GetSavedFlightDetails().execute();
			// detailsView.setVisibility(View.VISIBLE);
			tabHost.setCurrentTab(1);
		}

	}

	// Static method to return the tabhost in order to set a listener and change
	// tabs via the adapter
	public static TabHost getTabHost() {

		return tabHost;
	}

	/*********************************************************************************************************************************/
	// AsyncTask that will get the Flight data for the search parameters entered
	public class GetFlightData extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... query) {

			try {

				search = query[0];
				maxResults = query[1];

				mFlightTrackingProvider.getFlightInfo(search, maxResults);
				mOriginCities = mFlightTrackingProvider.getOriginNames();
				mFlightNumbers = mFlightTrackingProvider.getFlightNumbers();
				mAirports = mFlightTrackingProvider.getOriginAirports();
				mDepartureTimes = mFlightTrackingProvider
						.getEstimatedDepartureTimes();
				mArrivalTimes = mFlightTrackingProvider
						.getEstimateArrivalTimes();
				mDepartureCities = mFlightTrackingProvider.getOriginCities();
				mArrivalCities = mFlightTrackingProvider.getDestinationCities();
				mFlightIds = mFlightTrackingProvider.getFlightIds();
				mArrivalAirports = mFlightTrackingProvider
						.getDestinationNames();
				mAircraftTypes = mFlightTrackingProvider.getAircraftTypes();
				mPlaneSpeedsKnot = mFlightTrackingProvider.getAirspeedsKnot();
				mPlaneSpeedsMach = mFlightTrackingProvider.getAirspeedsMach();
				mPlaneAltitudes = mFlightTrackingProvider.getFlightAltitudes();

			} catch (Exception e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						Toast.makeText(MyFlightsActivity.this,
								"Error getting results!", Toast.LENGTH_SHORT)
								.show();
					}
				});

			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			try {

				// Set up the adapter
				mFlightAdapter = new FlightListAdapter(MyFlightsActivity.this,
						mFlightNumbers, mAirports, mDepartureTimes,
						mArrivalTimes, mArrivalCities, mDepartureCities);

				flightsList.setAdapter(mFlightAdapter);
				tabHost.setCurrentTab(0);
				dismissLoadingDialog();
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("MyFlightsActivity",
						"Error getting flight data in doInBackground");
			}

		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			clearDataSet();
			showLoadingDialog();

		}

	}

	/*********************************************************************************************************************************************/
	// Another AsyncTask that will get the details about a specific flight from
	// the list in the background

	public class GetFlightDetails extends AsyncTask<String, String, String> {

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			try {

				populateDetails();

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {

			// clearDataSet();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			try {

				mFlightTrackingProvider = new FlightTrackingProvider();
				mFlightTrackingProvider.getFlightDetails(mFlightIds
						.get(flight_position));
				Log.w("MyFlightsActivity", "CURRENT FLIGHT ID IS ---> "
						+ mFlightIds.get(flight_position));
				Log.w("MyFlightsActivity", "CURRENT FLIGHT NUMBER IS ---> "
						+ mFlightNumbers.get(flight_position));

			}

			catch (Exception e) {

				e.printStackTrace();
				Log.w("MyFlightsActivity",
						"Error getting details for flight at position "
								+ flight_position);
			}
			return null;
		}

	}

	/*************************************************************************************************************************************/
	// AsyncTask that will load the details tab when a saved flight is selected

	private class GetSavedFlightDetails extends
			AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			try {
				// Keep here for later

				/*
				 * mFlightTrackingProvider.getFlightDetails(dbFlightIds
				 * .get(saved_list_position)); Log.w("MyFlightsActivity",
				 * "FLIGHT ID FROM DATABASE IS --> " +
				 * dbFlightIds.get(saved_list_position));
				 * Log.w("MyFlightsActivity",
				 * "FLIGHT ID FROM DATABASE LIST --> " +
				 * dbFlightIds.toString());
				 */

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			// When a saved flight is selected, populate the "Details" tab with
			// some data we got from the database

			try {
				// populateDetails();
				// Log.w("MyFlightsActivity", "Populate details");

				originAirport.setText("From: "
						+ dbDepartureCities.get(saved_list_position) + " at "
						+ dbDepartureAirports.get(saved_list_position));
				changeStringColor(5, originAirport);

				dateTime.setText("When: "
						+ dbDepartureDateTimes.get(saved_list_position));

				changeStringColor(5, dateTime);

				aircraftType.setText("Aircraft Type: "
						+ dbAircraftTypes.get(saved_list_position));

				changeStringColor(13, aircraftType);

				destinationAirport.setText("To: "
						+ dbArrivalCities.get(saved_list_position) + " at "
						+ dbArrivalAirports.get(saved_list_position));

				changeStringColor(3, destinationAirport);

				dateTimeArrival.setText("When: "
						+ dbArrivalDateTimes.get(saved_list_position));

				changeStringColor(5, dateTimeArrival);

				planeSpeed.setText(dbPlaneSpeeds.get(saved_list_position));

				changeStringColor(12, planeSpeed);

				planeAltitude
						.setText(dbPlaneAltitudes.get(saved_list_position));
				changeStringColor(15, planeAltitude);

				terminalDeparture.setText(dbDepartureTerminals
						.get(saved_list_position));

				changeStringColor(9, terminalDeparture);
				gateDeparture
						.setText(dbDepartureGates.get(saved_list_position));
				changeStringColor(5, gateDeparture);

				terminalArrival.setText(dbArrivalTerminals
						.get(saved_list_position));

				changeStringColor(9, terminalArrival);
				gateArrival.setText(dbArrivalGates.get(saved_list_position));
				changeStringColor(5, gateArrival);
				baggageClaim.setText(dbBaggageClaims.get(saved_list_position));
				changeStringColor(14, baggageClaim);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

	}

	private void clearDataSet() {
		mAirlineNames.clear();
		mAirports.clear();
		mArrivalCities.clear();
		mArrivalTimes.clear();
		mDepartureCities.clear();
		mFlightNumbers.clear();
		mDepartureTimes.clear();

	}

	public void populateDetails() {

		originAirport.setText("From: " + mDepartureCities.get(flight_position)
				+ " at " + mOriginCities.get(flight_position));
		changeStringColor(5, originAirport);

		dateTime.setText("When: " + mDepartureTimes.get(flight_position));
		changeStringColor(5, dateTime);

		terminalDeparture.setText("Terminal: "
				+ mFlightTrackingProvider.getOrigTerminal());
		changeStringColor(9, terminalDeparture);

		gateDeparture.setText("Gate: " + mFlightTrackingProvider.getOrigGate());
		changeStringColor(5, gateDeparture);

		destinationAirport.setText("To:  "
				+ mArrivalAirports.get(flight_position));
		changeStringColor(3, destinationAirport);

		dateTimeArrival.setText("When: " + mArrivalTimes.get(flight_position));
		changeStringColor(5, dateTimeArrival);

		terminalArrival.setText("Terminal: "
				+ mFlightTrackingProvider.getDestTerminal());
		changeStringColor(9, terminalArrival);

		gateArrival.setText("Gate: " + mFlightTrackingProvider.getDestGate());
		changeStringColor(5, gateArrival);

		baggageClaim.setText("Baggage Claim: "
				+ mFlightTrackingProvider.getBaggageClaim());
		changeStringColor(14, baggageClaim);

		aircraftType.setText("Aircraft Type: "
				+ mAircraftTypes.get(flight_position));
		changeStringColor(14, aircraftType);

		planeSpeed.setText("Plane Speed: "
				+ mPlaneSpeedsMach.get(flight_position) + " Machs/ "
				+ mPlaneSpeedsKnot.get(flight_position) + " Knots");

		changeStringColor(12, planeSpeed);
		planeAltitude.setText("Plane Altitude: "
				+ mPlaneAltitudes.get(flight_position) + "00k feet");
		changeStringColor(15, planeAltitude);

	}

	// Create a context menu for when a user long presses a list item in the
	// saved flights list
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		if (v.getId() == R.id.FlightsSavedList) {

			AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

			menu.setHeaderTitle("Options");
			String[] menuItems = { "Email Flight Itinerary",
					"Share Flight Itinerary", "Delete" };
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}

		}

		// super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {

		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		menuItemIndex = item.getItemId();
		context_saved_pos = info.position;
		String[] menuItems = { "Email Flight Itinerary",
				"Share Flight Itinerary", "Delete" };
		String menuItemName = menuItems[menuItemIndex];
		// String

		switch (menuItemIndex) {

		case 0:

			emailFlightDetails();

			break;

		case 1:

			shareFlightDetails();

			break;

		case 2:

			deleteSavedFlight();

			break;
		}

		return true;
	}

	private void deleteSavedFlight() {
		// Delete a saved flight off the list
		// Also removes this entry from database

		int remove_index = context_saved_pos - 1;
		String index = " " + remove_index;

		dbFlightNumbers.remove(remove_index);
		dbDepartureCities.remove(remove_index);
		dbArrivalCities.remove(remove_index);
		dbDepartureDateTimes.remove(remove_index);
		dbArrivalDateTimes.remove(remove_index);

		try {
			storage.open();
			storage.removeEntry(remove_index + 1);
			storage.close();
			// Toast.makeText(MyFlightsActivity.this,
			// "Entry " + index + " removed from DB", Toast.LENGTH_SHORT)
			// .show();
			savedAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("MyFlightsActivity",
					"Error removing entry from flights database!");
		}

		/*
		 * savedAdapter = new SavedFlightsAdapter(this, dbFlightNumbers,
		 * dbDepartureCities, dbArrivalCities, dbDepartureDateTimes,
		 * dbArrivalDateTimes);
		 */

	}

	private void shareFlightDetails() {

		// Method to share flight details via android's share intent

		String details = "Trip Info: "
				+ dbDepartureCities.get(context_saved_pos - 1) + " to "
				+ dbArrivalCities.get(context_saved_pos - 1) + "\n"
				+ "Airport: " + dbDepartureAirports.get(context_saved_pos - 1)
				+ "\n" + "Leaves: "
				+ dbDepartureDateTimes.get(context_saved_pos - 1) + "\n"
				+ "Arrives: " + dbArrivalDateTimes.get(context_saved_pos - 1)
				+ "\n" + "Flight Number: "
				+ dbFlightNumbers.get(context_saved_pos - 1) + "\n"
				+ "Departure Terminal: "
				+ dbDepartureTerminals.get(context_saved_pos - 1) + "\n"
				+ "Departure Gate: "
				+ dbDepartureGates.get(context_saved_pos - 1) + "\n"
				+ "Arrival Terminal: "
				+ dbArrivalTerminals.get(context_saved_pos - 1) + "\n"
				+ "Arrival Gate: " + dbArrivalGates.get(context_saved_pos - 1)
				+ "\n" + "Baggage Claim: "
				+ dbBaggageClaims.get(context_saved_pos - 1);

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, details);
		startActivity(Intent.createChooser(shareIntent,
				"Share Flight Itinerary"));

	}

	private void emailFlightDetails() {

		// Try to allow the user to send an email with the flight details they
		// just selected
		try {

			// String emailadd[] = { "jjbyfield@gmail.com" };
			Intent e = new Intent(android.content.Intent.ACTION_SEND);
			String details = "Trip Info: "
					+ dbDepartureCities.get(context_saved_pos - 1) + " to "
					+ dbArrivalCities.get(context_saved_pos - 1) + "\n"
					+ "Airport: "
					+ dbDepartureAirports.get(context_saved_pos - 1) + "\n"
					+ "Leaves: "
					+ dbDepartureDateTimes.get(context_saved_pos - 1) + "\n"
					+ "Arrives: "
					+ dbArrivalDateTimes.get(context_saved_pos - 1) + "\n"
					+ "Flight Number: "
					+ dbFlightNumbers.get(context_saved_pos - 1) + "\n"
					+ "Departure Terminal: "
					+ dbDepartureTerminals.get(context_saved_pos - 1) + "\n"
					+ "Departure Gate: "
					+ dbDepartureGates.get(context_saved_pos - 1) + "\n"
					+ "Arrival Terminal: "
					+ dbArrivalTerminals.get(context_saved_pos - 1) + "\n"
					+ "Arrival Gate: "
					+ dbArrivalGates.get(context_saved_pos - 1) + "\n"
					+ "Baggage Claim: "
					+ dbBaggageClaims.get(context_saved_pos - 1);
			e.putExtra(android.content.Intent.EXTRA_SUBJECT,
					dbFlightNumbers.get(context_saved_pos - 1)
							+ " Flight Details");
			// e.putExtra(android.content.Intent.EXTRA_EMAIL, details);
			e.putExtra(android.content.Intent.EXTRA_TEXT, details);
			e.setType("plain/text");

			startActivity(Intent.createChooser(e, "Email Flight Itinerary"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void changeStringColor(int i, TextView text) {
		// This method changes a portion of a string to white(starting from
		// character 0 and ending at 1 of text)
		final SpannableStringBuilder sb = new SpannableStringBuilder(
				text.getText());
		final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.WHITE);
		sb.setSpan(fcs, 0, i, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		text.setText(sb);

	}

	private void showLoadingDialog() {

		pd.setTitle("My Flights");
		pd.setMessage("Fetching flight data...");
		pd.show();

	}

	private void dismissLoadingDialog() {
		pd.dismiss();
	}

	@Override
	public void onCheckedChanged(RadioGroup rg, int id) {

		switch (id) {

		case 0:
			maxResults = "5";
			break;

		case 1:

			maxResults = "10";

			break;

		case 2:

			maxResults = "15";
			break;

		}

	}

	@Override
	public void onClick(View v) {

		try {

			// Save the current flight details in the database for easy offline
			// viewing later

			storage.open();

			String flightNumber = mFlightNumbers.get(flight_position);
			String departureCity = mDepartureCities.get(flight_position);
			String departureAirport = mAirports.get(flight_position);
			String departureDateTime = mDepartureTimes.get(flight_position);
			String aircraftType = mAircraftTypes.get(flight_position);
			String departureTerminal = terminalDeparture.getText().toString();
			String departureGate = gateDeparture.getText().toString();
			String arrivalCity = mArrivalCities.get(flight_position);
			String arrivalAirport = mArrivalAirports.get(flight_position);
			String arrivalDateTime = mArrivalTimes.get(flight_position);
			String arrivalTerminal = terminalArrival.getText().toString();
			String arrivalGate = gateArrival.getText().toString();
			String bagClaim = baggageClaim.getText().toString();
			String airplaneSpeed = planeSpeed.getText().toString();
			String airplaneAltitude = planeAltitude.getText().toString();
			String flightId = mFlightIds.get(flight_position);

			storage.insertData(flightNumber, departureCity, departureAirport,
					departureDateTime, aircraftType, departureTerminal,
					departureGate, arrivalCity, arrivalAirport,
					arrivalDateTime, arrivalTerminal, arrivalGate, bagClaim,
					airplaneSpeed, airplaneAltitude, flightId);

			storage.close();
			Toast.makeText(this, "Flight saved successfully",
					Toast.LENGTH_SHORT).show();

			clearSavedDataSet();
			tabHost.setCurrentTab(2);

		} catch (Exception e) {
			e.printStackTrace();
			Log.w("MyFlightsActivity",
					"Error inserting flight data into database");
		}

	}

	private void clearSavedDataSet() {
		// After the save button is pressed, clear the saved flights list and
		// re-populate it from the database
		// in order to reflect the new entry that was added

		dbFlightNumbers.clear();
		dbDepartureCities.clear();
		dbArrivalCities.clear();
		dbDepartureDateTimes.clear();
		dbArrivalDateTimes.clear();
		dbAircraftTypes.clear();
		dbArrivalAirports.clear();
		dbArrivalGates.clear();
		dbBaggageClaims.clear();
		dbDepartureAirports.clear();
		dbDepartureTerminals.clear();
		dbFlightIds.clear();
		dbPlaneAltitudes.clear();
		dbPlaneSpeeds.clear();
		dbArrivalTerminals.clear();
		dbDepartureAirports.clear();
		dbDepartureGates.clear();

		getSavedFlightsFromDB();
		/*
		 * savedAdapter = new SavedFlightsAdapter(this, dbFlightNumbers,
		 * dbDepartureCities, dbArrivalCities, dbDepartureDateTimes,
		 * dbArrivalDateTimes);
		 */

	}

	private void loadLastSavedFlight() {
		// This method will only load the latest record from the saved flights
		// DB, and populate the arraylist with that data

	}
}
