package com.brightr.weathermate.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.brightr.weathermate.R;
import com.brightr.weathermate.providers.TrafficIncidentProvider;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class LocationMapview extends SherlockMapActivity implements
		OnClickListener, OnTouchListener {

	private static final String TAG = "LocationMapview";
	String location;
	MapView map;
	Drawable pin;
	Drawable incidentIcon;
	int zoomLevel;
	LayoutInflater inflater;
	View incidentView;
	
	SoundPool dialogSound;
	int dialogAlert;
	
	int incidentPosition;

	// IncidentView children
	TextView incidentLatitude;
	TextView incidentLongitude;
	TextView incidentDescription;
	TextView incidentLaneInfo;
	TextView incidentSeverity;
	TextView incidentRoadsClosed;
	ImageView incidentCancel;
	View shareView;
	// ArrayLists that will hold our traffic incident data

	ArrayList<String> latitudes = new ArrayList<String>();
	ArrayList<String> longitudes = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> roads = new ArrayList<String>();
	ArrayList<String> severities = new ArrayList<String>();
	ArrayList<String> lanes = new ArrayList<String>();

	// Animations
	Animation animUp;
	Animation animDown;

	// Menu
	View mapMenu;

	// Longs
	long start, stop;

	// ActionBar
	ActionBar actionBar;

	MyLocationOverlay compass;
	MapController controller;
	private double lat;
	private double lon;
	private boolean isMenuVisible;
	private boolean isTrafficSet;
	private boolean isSatelliteSet;

	MapItem mi;

	// Google traffic overlay
	Object trafficInfo;
	private LinearLayout zoomView;

	// Overlay list
	List<Overlay> overlayList;
	
	private boolean setTraffic;

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		//Intent i = new Intent(this, MainActivity.class);
		//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//startActivity(i);
	}

	ArrayList<GeoPoint> trafficPoints = new ArrayList<GeoPoint>();
	ArrayList<OverlayItem> trafficItems = new ArrayList<OverlayItem>();

	// microdegree
	private static final double MICRO_DEGREE = 1E6;

	private boolean trafficIncidentsShowing = false;

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);
		setContentView(R.layout.mapview_location);

		mapMenu = (View) findViewById(R.id.MapMenuRoot);
		mapMenu.setVisibility(View.INVISIBLE);
		mapMenu.setOnClickListener(this);

		// Inflate the incident view layout
		// set it to INVISIBLE by default
		inflater = getLayoutInflater();
		incidentView = (View) findViewById(R.id.trafficIncidentPanelRoot);
		incidentView.setVisibility(View.INVISIBLE);

		incidentLatitude = (TextView) incidentView
				.findViewById(R.id.tvTrafficIncidentLatitude);
		incidentLongitude = (TextView) incidentView
				.findViewById(R.id.tvTrafficIncidentLongitude);
		incidentDescription = (TextView) incidentView
				.findViewById(R.id.tvTrafficIncidentDescription);
		incidentLaneInfo = (TextView) incidentView
				.findViewById(R.id.tvTrafficIncidentLaneInfo);
		incidentRoadsClosed = (TextView) incidentView
				.findViewById(R.id.tvTrafficIncidentRoadInfo);
		incidentSeverity = (TextView) incidentView
				.findViewById(R.id.tvTrafficIncidentSeverityInfo);
		shareView = (View)incidentView.findViewById(R.id.trafficdialogShare);
		shareView.setOnClickListener(this);
		
        incidentCancel = (ImageView)incidentView.findViewById(R.id.TrafficDialogCancel);
        incidentCancel.setOnClickListener(this);
        
		// get the map drawables
		pin = getResources().getDrawable(R.drawable.gps_pin);
		incidentIcon = getResources().getDrawable(R.drawable.traffic_icon);
		pin.setBounds(0, 0, pin.getIntrinsicWidth(), pin.getIntrinsicHeight());

		

		mi = new MapItem(incidentIcon, LocationMapview.this);

		// Get the name of the location
		location = getIntent().getStringExtra("name");
		lat = getIntent().getDoubleExtra("latitude", 0);
		lon = getIntent().getDoubleExtra("longitude", 0);
		
		
		Log.d(TAG, "Latitude is " + lat);
		Log.d(TAG, "Longitude is " + lon);

		// Set up the action bar
		actionBar = getSupportActionBar();
		actionBar.setTitle(location);
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.weathermate_actionbar_4));
		
		actionBar.setIcon(R.drawable.daemons);
		
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		

		// Set up our map
		map = (MapView) findViewById(R.id.LocationMapView);
		map.setBuiltInZoomControls(true);
		map.setSatellite(false);
		map.setTraffic(true);

		map.displayZoomControls(true);
		map.setOnTouchListener(this);

		// set up compass
		compass = new MyLocationOverlay(this, map);

		// Define the main overlay
		TouchOverlay tl = new TouchOverlay();
		overlayList = map.getOverlays();
		overlayList.add(tl);
		overlayList.add(compass);

		controller = map.getController();

		GeoPoint point = new GeoPoint((int) (lat * MICRO_DEGREE),
				(int) (lon * MICRO_DEGREE));
		controller.animateTo(point);
		controller.setCenter(point);
		controller.setZoom(15);

		// map.setTraffic(true);

		// load the animations
		animUp = AnimationUtils.loadAnimation(this, R.anim.anim_up);
		animDown = AnimationUtils.loadAnimation(this, R.anim.anim_down);

		OverlayItem overlayitem = new OverlayItem(point, "point", "point");
		MapItem item = new MapItem(pin, this);

		item.addPinPoint(overlayitem);
		overlayList.add(item);
		
		dialogSound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		dialogAlert = dialogSound.load(this, R.raw.dialog_alert, 1);

	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockMapActivity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		compass.disableCompass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.MapActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		compass.enableCompass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {

			if (isMenuVisible == false) {
				// User touched the menu button, show the map menu

				slideMenuUp();

			}

			else
				slideMenuDown();
			// map.setTraffic(true);

		}

		else
			return super.onKeyDown(keyCode, event);

		return true;
	}

	class TouchOverlay extends Overlay {

		private AlertDialog alertDialog;

		public boolean onTouchEvent(MotionEvent me, MapView m) {

			if (me.getAction() == MotionEvent.ACTION_DOWN) {
				start = me.getEventTime();
			}

			if (me.getAction() == MotionEvent.ACTION_UP) {
				stop = me.getEventTime();
			}

			
			return false;

		}
	}

	@Override
	public void onClick(View v) {
		// Toast.makeText(this, "Menu Touched!", Toast.LENGTH_SHORT).show();

		switch (v.getId()) {

		case R.id.MapMenuRoot:
			if (isTrafficSet) {

				map.setTraffic(false);
				isTrafficSet = false;
				slideMenuDown();
			}

			else {
				isTrafficSet = true;
				map.setTraffic(true);
				slideMenuDown();
			}

			break;

		case R.id.TrafficDialogCancel:

			incidentView.setVisibility(View.INVISIBLE);

			break;
			
		case R.id.trafficdialogShare:
			
			try{
			
			String incidentData = "Traffic incident in  " + location + ", \n" + "Condition: " + descriptions.get(incidentPosition) + "\n" + "Road Closed: " + roads.get(incidentPosition);
			
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, incidentData);
			startActivity(Intent.createChooser(shareIntent, "Share Traffic Info"));
			}catch(Exception e){
				e.printStackTrace();
			}
			
			break;

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockMapActivity#onCreateOptionsMenu(com
	 * .actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.map_menu, menu);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockMapActivity#onOptionsItemSelected(com
	 * .actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_showTraffic:
			if (isTrafficSet) {
				map.setTraffic(false);
				isTrafficSet = false;

			}

			else {
				// readTxt();
				// Load the traffic conditions in the background
				if (!trafficIncidentsShowing) {
					new showTrafficConditions().execute();
				}

				// Change the mapview to traffic
				map.setTraffic(true);
				isTrafficSet = true;

				// Notify the user that the traffic view is currently set
				Toast.makeText(this, "Showing Traffic incidents near " + location, Toast.LENGTH_SHORT)
						.show();
			}
			return true;

		case R.id.menu_showSatellite:
			if (isSatelliteSet == false) {
				map.setSatellite(true);
				isSatelliteSet = true;
				Toast.makeText(this, "Satellite view ON", Toast.LENGTH_SHORT)
						.show();
			}

			else {
				map.setSatellite(false);
				Toast.makeText(this, "Satellite view OFF", Toast.LENGTH_SHORT)
						.show();
				isSatelliteSet = false;

			}

			return true;
			
			
		case android.R.id.home:
			
			super.onBackPressed();
			
			break;

		}
		return super.onOptionsItemSelected(item);

	}

	

	
//Asynctask that is responsible for getting and displaying the relevant traffic incidents
	
	public class showTrafficConditions extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... arg0) {
			try {
				TrafficIncidentProvider.getTrafficIncidents(lat, lon);

				latitudes = TrafficIncidentProvider.getLatitudes();
				longitudes = TrafficIncidentProvider.getLongitudes();
				descriptions = TrafficIncidentProvider.getDescriptions();
				lanes = TrafficIncidentProvider.getLanes();
				roads = TrafficIncidentProvider.getRoads();
				severities = TrafficIncidentProvider.getSeverities();

				Log.w("LocationMapView",
						"Latitudes list " + latitudes.toString());
				Log.w("LocationMapView",
						"Longitudes list " + longitudes.toString());
				Log.w("LocationMapView",
						"Descriptions list " + descriptions.toString());
				Log.w("LocationMapView", "Lanes lis " + lanes.toString());
				Log.w("LocationMapView", "Roads lis " + roads.toString());
				Log.w("LocationMapView",
						"Severities lis " + severities.toString());

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);

			try {

				displayTrafficIncidents();
				trafficIncidentsShowing = true;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private void displayTrafficIncidents() {
			
			

			for (int i = 0; i < latitudes.size(); i++) {

				double trafficLat = Double.parseDouble(latitudes.get(i));
				double trafficLon = Double.parseDouble(longitudes.get(i));

				GeoPoint trafficPoint = new GeoPoint(
						(int) (trafficLat * MICRO_DEGREE),
						(int) (trafficLon * MICRO_DEGREE));

				trafficPoints.add(trafficPoint);
				OverlayItem item = new OverlayItem(trafficPoint, "point",
						"point");
				trafficItems.add(item);

				mi.addPinPoint(item);
				overlayList.add(mi);
				
				

			}
			
			Log.w("LocationMapView",
					"overlayList size is " + overlayList.size());
			
			Log.w("LocationMapView",
					"Latitude List size is " + latitudes.size());
			
			Log.w("LocationMapView",
					"ItemizedOverlay List size is " + mi.size());

		}

	}

	private void slideMenuDown() {
		if (isMenuVisible == true) {
			mapMenu.setVisibility(View.INVISIBLE);
			mapMenu.startAnimation(animDown);
			isMenuVisible = false;
		}
	}

	private void slideMenuUp() {
		if (isMenuVisible == false) {
			mapMenu.setVisibility(View.VISIBLE);
			mapMenu.startAnimation(animUp);
			isMenuVisible = true;
		}
	}



	public class MapItem extends ItemizedOverlay<OverlayItem> {

		private ArrayList<OverlayItem> pinpoints = new ArrayList<OverlayItem>();
		private Context context;

		int height;
		int width;

		public MapItem(Drawable d) {
			super(boundCenterBottom(d));

		}

		public MapItem(Drawable m, Context context) {

			this(m);
			this.context = context;
            
			m.setDither(true);
			m.setAlpha(250);
			
			
			height = m.getIntrinsicHeight();
			width = m.getIntrinsicWidth();

		}

		@Override
		protected OverlayItem createItem(int index) {
			// TODO Auto-generated method stub
			return pinpoints.get(index);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return pinpoints.size();
		}

		public void addPinPoint(OverlayItem item) {

			pinpoints.add(item);
			this.populate();

		}

	
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, false);
			if (!shadow) {

			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.android.maps.ItemizedOverlay#onTap(int)
		 */
		@Override
		protected boolean onTap(int index) {
			super.onTap(index);

			dialogSound.play(dialogAlert, 1f, 1f, 0, 0, 0);
			
			try{
			
		
           
			if(index <= pinpoints.size()) {
			incidentPosition = index;
			incidentView.setVisibility(View.VISIBLE);
			incidentLatitude.setText("Latitude: " + latitudes.get(index));
			incidentLongitude.setText("Longitude: " + longitudes.get(index));
			incidentDescription.setText("Description: "
					+ descriptions.get(index));
			incidentLaneInfo.setText("Lane info: " + lanes.get(index));
			incidentRoadsClosed.setText("Roads Closed: " + roads.get(index));
			incidentSeverity.setText("Severity(1-4): " + severities.get(index));
			}
			
			Log.w("LocationMapView",
					"Pinpoints list size is " + pinpoints.size());
			Log.w("LocationMapView", "INDEX IS " + index);
			Log.w(TAG, "Map Item size is " + mi.size());

			
			}catch(Exception e){
				e.printStackTrace();
				Log.w(TAG, "Exception in onTap()");
				
			}

			return true;
		}
			
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		

		return false;
	}

}
