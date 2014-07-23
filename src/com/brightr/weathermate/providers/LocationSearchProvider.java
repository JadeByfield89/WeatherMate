
package com.brightr.weathermate.providers;

import com.brightr.weathermate.parsers.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import java.util.ArrayList;

public class LocationSearchProvider {

    private static final String API_KEY = "xuqt8ny6jnquqwhasu6kn4ab";

    private static final String TAG_AREANAME = "areaName";

    private static final String TAG_COUNTRY = "country";

    private static final String TAG_LATITUDE = "latitude";

    private static final String TAG_LONGITUDE = "longitude";

    private static final String TAG_POPULATION = "population";

    private static final String TAG_REGION = "region";

    private static final String TAG = "LocationSearchProvider";

    private static final String TAG_VALUE = "value";

    static JSONArray cityData = null;

    static JSONArray area = null;

    static JSONArray country = null;

    static JSONArray region = null;

    static ArrayList<String> areaNames = new ArrayList<String>();

    static ArrayList<String> countryNames = new ArrayList<String>();

    static ArrayList<String> latitudes = new ArrayList<String>();

    static ArrayList<String> longitudes = new ArrayList<String>();

    static ArrayList<String> populations = new ArrayList<String>();

    static ArrayList<String> regions = new ArrayList<String>();

    // private static String query;

    public static void getCityData(String query) {

        String address = "http://api.worldweatheronline.com/free/v1/search.ashx?key=" + API_KEY
                + "&q=" + query + "&num_of_results=3&format=json";

        // Create a JSON Parser instance
        JSONParser jParser = new JSONParser();

        // getting JSON String from URL
        final JSONObject json = jParser.getJSONFromUrl(address);

        try {
            // Try to get the city data for the query
            cityData = json.getJSONObject("search_api").getJSONArray("result");
            Log.w(TAG, "cityData consits of " + cityData.toString());

            if (cityData == null) {

                showErrorDialog();
            }

            for (int i = 0; i < cityData.length(); i++) {

                // Iterate through all JSON objects
                JSONObject object = cityData.getJSONObject(i);

                // Get the array named "areaName"
                area = object.getJSONArray(TAG_AREANAME);

                // Get the first value in that array and add it to the a list
                JSONObject name = area.getJSONObject(0);
                String value = name.getString(TAG_VALUE);
                areaNames.add(value);

                // repeat for country names
                country = object.getJSONArray(TAG_COUNTRY);
                JSONObject o = country.getJSONObject(0);
                String country = o.getString(TAG_VALUE);
                countryNames.add(country);

                // repeat for regions

                region = object.getJSONArray(TAG_REGION);
                JSONObject r = region.getJSONObject(0);
                String val = r.getString(TAG_VALUE);
                regions.add(val);

                // repeat for latitude values
                String lat = object.getString(TAG_LATITUDE);
                latitudes.add(lat);

                // repeat for longitude values
                String lon = object.getString(TAG_LONGITUDE);
                longitudes.add(lon);

                // repeat for population values
                String pop = object.getString(TAG_POPULATION);
                populations.add(pop);

            }

            Log.w(TAG, "Area Names consists of " + areaNames.toString());
            Log.w(TAG, "City Data length is " + cityData.length());
            Log.w(TAG, "Country list consists of " + countryNames.toString());
            Log.w(TAG, "Regions list consists of " + regions.toString());
            Log.w(TAG, "Latitude list consists of " + latitudes.toString());
            Log.w(TAG, "Population list consists of " + populations.toString());

        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Error getting city data!");

        }

    }

    private static void showErrorDialog() {

    }

    public static ArrayList<String> getAreaNames() {

        return areaNames;
    }

    public static ArrayList<String> getCountryNames() {

        return countryNames;
    }

    public static ArrayList<String> getLatitudes() {

        return latitudes;
    }

    public static ArrayList<String> getLongitudes() {

        return longitudes;
    }

    public static String getLongitudeAt(int index) {

        return longitudes.get(index);
    }

    public static String getLatitudeAt(int index) {

        return latitudes.get(index);
    }

    public static String getAreaNameAt(int index) {

        return areaNames.get(index);
    }

    public static ArrayList<String> getPopulations() {

        return populations;
    }

    public static String getRegionAt(int index) {

        return regions.get(index);
    }

    public static ArrayList<String> getRegions() {

        return regions;
    }

}
