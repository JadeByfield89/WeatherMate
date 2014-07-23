package com.brightr.weathermate.providers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.brightr.weathermate.parsers.JSONParser;
import com.brightr.weathermate.parsers.JSONTrafficParser;

public class TrafficIncidentProvider {

	public static final String API_KEY = "AnSyais4Js99K_-0hNQ-K3JBB612iRsNeFx5JJOdg2e5ovlU9HL0PpkoG1qtacTw";
	public static final String TAG_AUTHENTICATIONREQUEST = "authenticationResultCode";
	public static final String TAG_RESOURCESETS = "resourceSets";
	public static final String TAG_RESOURCES = "resources";
	public static final String TAG_DESCRIPTION = "description";
	public static final String TAG_LANE = "lane";
	public static final String TAG_POINT = "point";
	public static final String TAG_COORDINATES = "coordinates";
	public static final String TAG_ROADCLOSED = "roadClosed";
	public static final String TAG_SEVERITY = "severity";

	String resourceSets;
	String resources;
	String description;
	String lane;
	String point;
	String coordinates;

	static String theLatitude;
	static String theLongitude;

	static ArrayList<String> latitudes = new ArrayList<String>();
	static ArrayList<String> longitudes = new ArrayList<String>();
	static ArrayList<String> descriptions = new ArrayList<String>();
	static ArrayList<String> lanes = new ArrayList<String>();
	static ArrayList<String> severities = new ArrayList<String>();
	static ArrayList<String> roads = new ArrayList<String>();

	private static final double LATITUDE_LIMIT = 2.391;
	private static final double LONGITUDE_LIMIT = .218;
	
    //private static final double LATITUDE_LIMIT = 2.391;
	//private static final double LONGITUDE_LIMIT = .218;

	public static void getTrafficIncidents(Double lat, Double lon) {

		double latitude = lat + LATITUDE_LIMIT;
		double longitude = lon + LONGITUDE_LIMIT;
		
		String negLog = "-" + longitude;

		String limitLat = Double.toString(latitude);
		String limitLon = Double.toString(longitude);
		

		final String url = "http://dev.virtualearth.net/REST/v1/Traffic/Incidents/"
				+ lat
				+ ","
				+ limitLon
				+ ","
				+ limitLat
				+ ","
				+ lon
				+ "?key=AnSyais4Js99K_-0hNQ-K3JBB612iRsNeFx5JJOdg2e5ovlU9HL0PpkoG1qtacTw";

		// Create JSONParser instance
		// JSONTrafficParser jParser = new JSONTrafficParser();
		// JSONObject jObject = jParser.getJSONFromUrl(url);

		try {
			JSONObject jobject = JSONTrafficParser.requestWebService(url);
			Log.w("TrafficIncidentProvider", jobject.toString());

			// Get the json array named resourceSets
			JSONArray jarray = jobject.getJSONArray("resourceSets");
			System.out.println("dateNow jarray :" + jarray.length());

			// Begin to iterate through the array, returning each object at each
			// index
			for (int i = 0; i < jarray.length(); i++) {
				Log.w("TrafficIncidentProvider",
						"ArrayLength is " + jarray.length());

				// If the array isn't null at index i, then get the data at that
				// index
				if (!jarray.isNull(i)) {
					JSONObject jobjresources = jarray.getJSONObject(i);

					// Get the date now object
					System.out.println("dateNow jobjresources :"
							+ jobjresources.length());
					// estimatedTotal
					if (!jobjresources.isNull("estimatedTotal")) {
						String str_estimatedTotal = jobjresources
								.getString("estimatedTotal");
						System.out.println("resources str_estimatedTotal :"
								+ str_estimatedTotal);

					} else {
						System.out
								.println("resources str_estimatedTotal NULL for :"
										+ i + " ITEM");
					}
					if (!jobjresources.isNull("resources")) {
						// resources
						JSONArray jarrresources = jobjresources
								.getJSONArray("resources");
						for (int j = 0; j < jarrresources.length(); j++) {
							System.out.println("$$$$$$$$$$ ITEM " + j
									+ " START $$$$$$$$$$$$$$$$#");
							if (!jarrresources.isNull(j)) {

								JSONObject jobjjarrresources = jarrresources
										.getJSONObject(j);
								if (!jobjjarrresources.isNull("__type")) {
									// __type"
									String str_type = jobjjarrresources
											.getString("__type");
									System.out.println("resources str_type :"
											+ str_type);
								} else {
									System.out
											.println("resources __type NULL for :"
													+ j + " ITEM");
								}
								// description"
								if (!jobjjarrresources.isNull("description")) {
									String strdescription = jobjjarrresources
											.getString("description");
									System.out
											.println("resources description :"
													+ strdescription);
									// Add description to list of traffic
									// descriptions
									descriptions.add(strdescription);
								} else {
									System.out
											.println("resources description NULL for :"
													+ j + " ITEM");
								}
								// lane"
								if (!jobjjarrresources.isNull("lane")) {
									String strlane = jobjjarrresources
											.getString("lane");
									System.out.println("resources lane :"
											+ strlane);

									// Add lane information to list
									lanes.add(strlane);
								} else {
									System.out
											.println("resources lane NULL for :"
													+ j + " ITEM");
								}
								// lane"
								if (!jobjjarrresources.isNull("point")) {
									JSONObject jobjpoint = jobjjarrresources
											.getJSONObject("point");
									// point
									if (!jobjpoint.isNull("coordinates")) {
										JSONArray jarcoordinates = jobjpoint
												.getJSONArray("coordinates");
										for (int k = 0; k < jarcoordinates
												.length(); k++) {
											// JSONObject
											// jobjcoordinates=jarcoordinates.getString(k);
											if (!jarcoordinates.isNull(k)) {
												String str_zero = jarcoordinates
														.getString(k);
												System.out
														.println("coordinates :"
																+ k
																+ ": "
																+ str_zero);

												theLatitude = jarcoordinates
														.getString(0);
												theLongitude = jarcoordinates
														.getString(1);
												// add the lat and lon values to
												// their respective lists

											} else {
												System.out
														.println("coordinates :"
																+ k
																+ " is NULL:"
																+ j + " ITEM");
											}
										}

										// ADD COORDINATES TO LIST
										latitudes.add(theLatitude);
										longitudes.add(theLongitude);
									} else {
										System.out
												.println("resources coordinates NULL for :"
														+ j + " ITEM");
									}
								} else {
									System.out
											.println("resources point NULL for :"
													+ j + " ITEM");
								}

								// roadClosed"
								// lane"
								if (!jobjjarrresources.isNull("roadClosed")) {

									String strroadClosed = jobjjarrresources
											.getString("roadClosed");
									System.out.println("resources roadClosed :"
											+ strroadClosed);

									// Add roadClosed boolean to it's list
									roads.add(strroadClosed);
								} else {
									System.out
											.println("resources roadClosed NULL for :"
													+ j + " ITEM");
								}
								// severity"
								if (!jobjjarrresources.isNull("severity")) {
									String strroadseverity = jobjjarrresources
											.getString("severity");
									System.out.println("resources severity :"
											+ strroadseverity);
									// Add description to list of traffic
									// descriptions
									severities.add(strroadseverity);
								} else {
									System.out
											.println("resources severity NULL for :"
													+ j + " ITEM");
								}
							} else {
								System.out
										.println("jarrresources    NULL for :"
												+ j + " ITEM");
							}

							System.out.println("##################### ITEM "
									+ j + " END ##############");
						}
					} else {
						System.out.println("resources    NULL for :" + i
								+ " ITEM");
					}
				}

				else {
					System.out.println("resources     NULL for : ITEM");
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * try { JSONArray trafficData = jObject.getJSONArray(TAG_RESOURCESETS);
		 * for(int i=0;i<trafficData.length();i++){ JSONObject trafficjobj =
		 * trafficData.getJSONObject(i);
		 * 
		 * //estimatedTotal String
		 * str_estimatedTotal=trafficjobj.getString("trafficjobj");
		 * 
		 * //resources JSONArray JSONArray resourcesjarray =
		 * trafficjobj.getJSONArray("resources");
		 * 
		 * for(int j=0;j<resourcesjarray.length();j++){ JSONObject jobjresources
		 * = resourcesjarray.getJSONObject(j);
		 * 
		 * //__type String str_type=jobjresources.getString("__type");
		 * 
		 * //point JSONObject JSONObject jobjpoint =
		 * jobjresources.getJSONObject("point"); //type String
		 * strtype=jobjpoint.getString("type");
		 * 
		 * //coordinates JSONArray coordinatesjarray =
		 * jobjpoint.getJSONArray("coordinates"); for(int
		 * k=0;k<coordinatesjarray.length();k++){ JSONObject jobjcoordinate =
		 * coordinatesjarray.getJSONObject(k); //coordinates 0 String
		 * str_zero=jobjcoordinate.getString("0"); ////coordinates 1 String
		 * str_one=jobjcoordinate.getString("1"); }
		 * 
		 * } } } catch (JSONException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * // Get JSON from url /*final JSONObject jObject =
		 * jParser.getJSONFromUrl(url);
		 * 
		 * 
		 * try { JSONArray trafficData =
		 * jObject.getJSONObject(TAG_RESOURCESETS).getJSONArray(TAG_RESOURCES);
		 * Log.w("TrafficIncidentProvider", "Traffic Array consists of " +
		 * trafficData.toString()); } catch (JSONException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

	}

	public static ArrayList<String> getLatitudes() {

		return latitudes;
	}

	public static ArrayList<String> getLongitudes() {

		return longitudes;
	}

	public static ArrayList<String> getDescriptions() {

		return descriptions;
	}

	public static ArrayList<String> getLanes() {

		return lanes;
	}

	public static ArrayList<String> getSeverities() {

		return severities;
	}

	public static ArrayList<String> getRoads() {

		return roads;
	}

}
