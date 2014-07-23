package com.brightr.weathermate.providers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.brightr.weathermate.parsers.FlightTrackingParser;

public class FlightTrackingProvider {

	private static final String TAG_IDENT = "ident";
	private static final String TAG_FA_FLIGHTID = "faFlightID";
	private static final String TAG_FLIGHTINFORESULT = "FlightInfoExResult";
	private static final String TAG_ALTITUDE = "filed_altitude";
	private static final String TAG_FLIGHTS = "flights";
	private static final String TAG_AIRCRAFTTYPE = "aircrafttype";
	private static final String TAG_DEPARTURETIME = "filed_departuretime";
	private static final String TAG_AIRSPEED = "filed_airspeed_kts";
	private static final String TAG_AIRSPEED_MACH = "filed_airspeed_mach";
	private static final String TAG_ACTUAL_DEPARTURETIME = "actualdeparturetime";
	private static final String TAG_ACTUAL_ARRIVALTIME = "actualarrivaltime";
	private static final String TAG_ESTIMATED_ARRIVALTIME = "estimatedarrivaltime";

	private static final String TAG_ORIGIN = "origin";
	private static final String TAG_DESTINATIN = "destination";
	private static final String TAG_ORIGIN_NAME = "originName";
	private static final String TAG_ORIGIN_CITY = "originCity";
	private static final String TAG_DESTINATION_NAME = "destinationName";
	private static final String TAG_DESTINATION_CITY = "destinationCity";
	private static final String TAG = "FlightTrackingProvider";

	public  ArrayList<String> flightNumbers = new ArrayList<String>();
	public  ArrayList<String> flighAltitudes = new ArrayList<String>();
	public  ArrayList<String> flightAircrafts = new ArrayList<String>();
	public  ArrayList<String> flightEstimatedDepartureTimes = new ArrayList<String>();
	public  ArrayList<String> flightAirspeedsMach = new ArrayList<String>();
	public  ArrayList<String> flightAirspeedKnots = new ArrayList<String>();
	public  ArrayList<String> flightActualDepartureTimes = new ArrayList<String>();
	public  ArrayList<String> flightEstimatedArrivalTimes = new ArrayList<String>();
	public  ArrayList<String> flightActualArrivalTimes = new ArrayList<String>();

	public  ArrayList<String> flightDestinations = new ArrayList<String>();
	public  ArrayList<String> flightOrigins = new ArrayList<String>();
	public  ArrayList<String> flightOriginNames = new ArrayList<String>();
	public  ArrayList<String> flightOriginCities = new ArrayList<String>();

	public  ArrayList<String> flightDestinationNames = new ArrayList<String>();
	public  ArrayList<String> flightDestinationCities = new ArrayList<String>();
	public  ArrayList<String> flightAltitudes = new ArrayList<String>();
	public  ArrayList<String> flightIDs = new ArrayList<String>();

	// Flight details members
	public  String TAG_FLIGHTINFO = "AirlineFlightInfoResult";
	public  String TAG_ORIGGATE = "gate_orig";
	public  String TAG_DEST_TERMINAL = "terminal_dest";
	public  String TAG_ORIG_TERMINAL = "terminal_orig";
	public  String TAG_GATE_DEST = "gate_dest";
	public  String TAG_BAGGAGE_CLAIM = "bag_claim";
	public  String mOrigGateNumber;
	public  String mDestGateNumber;
	public  String mOrigTerminal;
	public  String mDestTerminal;
	public  String mBagClaim;
	
	
	public FlightTrackingProvider(){
		
	}

	public  void getFlightInfo(String query, String maxResults) {

		try {

			JSONObject json = FlightTrackingParser.getJSONFromUrl(query,
					maxResults);
			JSONArray jArray = json.getJSONObject(TAG_FLIGHTINFORESULT)
					.getJSONArray(TAG_FLIGHTS);

			for (int i = 0; i < jArray.length(); i++) {

				Log.w("FlightTrackingProvider",
						"ArrayLength is " + jArray.length());

				JSONObject jobject = jArray.getJSONObject(i);
				String mIdent = jobject.getString(TAG_IDENT);

				flightNumbers.add(mIdent);
				Log.w(TAG, "IDENT --> " + flightNumbers.toString());

				String mAircraftType = jobject.getString(TAG_AIRCRAFTTYPE);

				flightAircrafts.add(mAircraftType);
				Log.w(TAG, "AIRCRAFT TYPE--> " + flightAircrafts.toString());

				String mDepartureTime = jobject.getString(TAG_DEPARTURETIME);
				long dTime = Long.parseLong(mDepartureTime);
				Date date = new Date(dTime * 1000);
				DateFormat df = new SimpleDateFormat(
						"EEEE, MM-dd-yyyy, KK:mm", Locale.US);
				TimeZone tz = TimeZone.getDefault();
				//df.setTimeZone(tz);
				String simpleTime = df.format(date);
				flightEstimatedDepartureTimes.add(simpleTime);
				Log.w(TAG, "DEPARTURE TIME --> "
						+ flightEstimatedDepartureTimes.toString());

				String mAirspeedKnots = jobject.getString(TAG_AIRSPEED);

				flightAirspeedKnots.add(mAirspeedKnots);
				Log.w(TAG, "AIRSPEED --> " + flightAirspeedKnots.toString());

				String mAirspeedMach = jobject.getString(TAG_AIRSPEED_MACH);

				flightAirspeedsMach.add(mAirspeedMach);
				Log.w(TAG,
						"AIRSPEED MACH --> " + flightAirspeedsMach.toString());

				String mArrivalTime = jobject
						.getString(TAG_ESTIMATED_ARRIVALTIME);
				long aTime = Long.parseLong(mArrivalTime);
				Date datey = new Date(aTime * 1000);
				DateFormat adf = new SimpleDateFormat(
						"EEEE, MM-dd-yyyy, KK:mm", Locale.US);
				//adf.setTimeZone(tz);
				String arrtime = adf.format(datey);
				flightEstimatedArrivalTimes.add(arrtime);
				Log.w(TAG, "ESTIMATED ARRIVAL TIME --> "
						+ flightEstimatedArrivalTimes.toString());

				String mActualDepartureTime = jobject
						.getString(TAG_ACTUAL_DEPARTURETIME);

				flightActualDepartureTimes.add(mActualDepartureTime);
				Log.w(TAG, "ACTUAL DEPARTURE TIME--> "
						+ flightActualDepartureTimes.toString());

				String mActualArrivalTime = jobject
						.getString(TAG_ACTUAL_ARRIVALTIME);

				flightActualArrivalTimes.add(mActualArrivalTime);
				Log.w(TAG,
						"ACTUAL ARRIVAL TIME--> "
								+ flightActualArrivalTimes.toString());

				String mOriginAirport = jobject.getString(TAG_ORIGIN);

				flightOrigins.add(mOriginAirport);
				Log.w(TAG, "ORIGIN AIRPORT --> " + flightOrigins.toString());

				String mOriginCity = jobject.getString(TAG_ORIGIN_CITY);

				flightOriginCities.add(mOriginCity);
				Log.w(TAG, "ORIGIN CITY --> " + flightOriginCities.toString());

				String mOriginName = jobject.getString(TAG_ORIGIN_NAME);

				flightOriginNames.add(mOriginName);
				Log.w(TAG, "ORIGIN NAME --> " + flightOriginNames.toString());

				String mDestination = jobject.getString(TAG_DESTINATIN);

				flightDestinations.add(mDestination);
				Log.w(TAG, "DESTINATION --> " + flightDestinations.toString());

				String mDestinationCity = jobject
						.getString(TAG_DESTINATION_CITY);

				flightDestinationCities.add(mDestinationCity);
				Log.w(TAG,
						"DESTINATION CITY --> "
								+ flightDestinationCities.toString());

				String mDestinationName = jobject
						.getString(TAG_DESTINATION_NAME);

				flightDestinationNames.add(mDestinationName);
				Log.w(TAG,
						"DESTINATION NAME --> "
								+ flightDestinationNames.toString());

				String mAltitude = jobject.getString(TAG_ALTITUDE);

				flightAltitudes.add(mAltitude);
				Log.w(TAG, "ALTITUDE  --> " + flightAltitudes.toString());

				String flightId = jobject.getString(TAG_FA_FLIGHTID);
				flightIDs.add(flightId);
				Log.w(TAG, "Flight ID ---> " + flightIDs.toString());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public  void getFlightDetails(String flightId) {

		try {

			JSONObject jobject = FlightTrackingParser
					.getFlightDetails(flightId);
			JSONObject object = jobject.getJSONObject(TAG_FLIGHTINFO);
			Log.w(TAG, "Flight Details ---> " + jobject);

			// for(int i = 0; i < jarray.length(); i++){
			// Log.w(TAG, "DETAILS ARARY ---> " + jarray.length());

			// JSONObject current = jarray.getJSONObject(i);
			mOrigGateNumber = object.getString(TAG_ORIGGATE);
			Log.w(TAG, "ORIGIN GATE ---> " + mOrigGateNumber);

			mDestGateNumber = object.getString(TAG_GATE_DEST);
			Log.w(TAG, "DESTINATION GATE ---> " + mDestGateNumber);

			mOrigTerminal = object.getString(TAG_ORIG_TERMINAL);
			Log.w(TAG, "ORIGIN TERMINAL ---> " + mOrigTerminal);

			mDestTerminal = object.getString(TAG_DEST_TERMINAL);
			Log.w(TAG, "DESTINATION TERMINAL ---> " + mDestTerminal);

			mBagClaim = object.getString(TAG_BAGGAGE_CLAIM);
			Log.w(TAG, "BAGGAGE CLAIM ---> " + mBagClaim);

			// }

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public String getOrigTerminal() {

		return mOrigTerminal;

	}

	public String getOrigGate() {

		return mOrigGateNumber;
	}

	public String getDestTerminal() {

		return mDestTerminal;
	}

	public String getDestGate() {

		return mDestGateNumber;
	}

	public String getBaggageClaim() {

		return mBagClaim;
	}

	public ArrayList<String> getFlightIds() {

		return flightIDs;
	}

	public ArrayList<String> getFlightNumbers() {

		return flightNumbers;

	}

	public ArrayList<String> getFlightAltitudes() {

		return flightAltitudes;

	}

	public ArrayList<String> getEstimatedDepartureTimes() {

		return flightEstimatedDepartureTimes;

	}

	public ArrayList<String> getActualDepartureTimes() {

		return flightActualDepartureTimes;

	}

	public ArrayList<String> getEstimateArrivalTimes() {

		return flightEstimatedArrivalTimes;

	}

	public ArrayList<String> getActualArrivalTimes() {

		return flightActualArrivalTimes;

	}

	public ArrayList<String> getOriginAirports() {

		return flightOrigins;

	}

	public ArrayList<String> getOriginCities() {

		return flightOriginCities;

	}

	public ArrayList<String> getOriginNames() {

		return flightOriginNames;

	}

	public ArrayList<String> getDestinationAirports() {

		return flightDestinations;

	}

	public ArrayList<String> getDestinationCities() {

		return flightDestinationCities;

	}

	public ArrayList<String> getDestinationNames() {

		return flightDestinationNames;

	}

	public ArrayList<String> getAirspeedsMach() {

		return flightAirspeedsMach;

	}

	public ArrayList<String> getAirspeedsKnot() {

		return flightAirspeedKnots;

	}

	public ArrayList<String> getAircraftTypes() {

		return flightAircrafts;
	}

}
