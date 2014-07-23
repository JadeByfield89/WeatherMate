
package com.brightr.weathermate.providers;

import com.brightr.weathermate.R;
import com.brightr.weathermate.parsers.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Class that will provide the weather in an easy to access way

public class WeatherProvider {

    private static final String API_KEY = "xuqt8ny6jnquqwhasu6kn4ab";

    private String zipCode;

    String currentCond;

    String tempF;

    String tempC;

    String weatherDescription;

    String cloudCoverage;

    String humidity;

    String precipMM;

    String pressure;

    String visibility;

    String weatherIconUrl;

    String windir16Point;

    String winddirDegree;

    String windspeedKmph;

    String windspeedMiles;

    String date;

    String tempMaxC;

    String tempMaxF;

    String tempMinC;

    String tempMinF;

    String observationTime;

    String windDirection;

    private String cityName;

    private static final String WEATHER_PARTLYCLOUDY = "Partly Cloudy";

    private static final String WEATHER_OVERCAST = "Overcast";

    private static final String WEATHER_SUNNY = "Sunny";

    private static final String WEATHER_SUN = "Sun";

    private static final String WEATHER_RAINY = "Rainy";

    private static final String WEATHER_RAIN = "Rain";

    private static final String WEATHER_MIST = "Mist";

    private static final String WEATHER_FOG = "Fog";

    private static final String WEATHER_SNOW = "Snow";

    private static final String WEATHER_HAIL = "Hail";

    private static final String WEATHER_THUNDER = "Thunder";

    private static final String WEATHER_SHOWERS = "Showers";

    private static final String WEATHER_STORM = "Storm";

    private static final String WEATHER_NA = "N/A";

    private static final String WEATHER_CLEAR = "Clear";

    private static final String WEATHER_MOON = "Moon";

    private static final String WEATHER_LIGHTNING = "Lightning";

    private static final String WEATHER_WINDY = "Windy";

    private String[] phrases = {
            "Partly Cloudy", "Cloudy", "Overcast", "Sunny", "Sun", "Light Drizzle", "Rainy",
            "Rain", "Light Rain", "Mist", "Fog", "Snow", "Light Snow", "Hail", "Thunder",
            "Showers", "Storm", "N/A", "Clear", "Moon", "Lightning", "Windy"
    };

    private int[] drawableIds = {
            R.drawable.weather_icon_partlycloudy, R.drawable.weather_icon_partlycloudy,
            R.drawable.weather_icon_cloudy, R.drawable.weather_icon_sunny,
            R.drawable.weather_icon_sunny, R.drawable.weathericon_rainy,
            R.drawable.weathericon_rainy, R.drawable.weathericon_rainy,
            R.drawable.weathericon_rainy, R.drawable.weather_icon_foggy,
            R.drawable.weather_icon_foggy, R.drawable.weather_icon_snow,
            R.drawable.weather_icon_snow, R.drawable.weather_icon_thunder,
            R.drawable.weather_icon_thunder, R.drawable.weathericon_rainy,
            R.drawable.weather_icon_na, R.drawable.weather_icon_na, R.drawable.weather_icon_clear,
            R.drawable.weather_icon_clear, R.drawable.weather_icon_lightnight,
            R.drawable.weather_icon_windy
    };

    private static final String TAG_DATA = "data";

    private static final String TAG_CURRENT_CONDITION = "current_condition";

    private static final String TAG_CLOUD_COVER = "cloudcover";

    private static final String TAG_HUMIDITY = "humidity";

    private static final String TAG_OBSERVATION_TIME = "observation_time";

    private static final String TAG_PRECIPMM = "precipMM";

    private static final String TAG_PRESSURE = "pressure";

    private static final String TAG_TEMPC = "temp_C";

    private static final String TAG_TEMPF = "temp_F";

    private static final String TAG_VISIBILITY = "visibility";

    private static final String TAG_WEATHERCODE = "weatherCode";

    private static final String TAG_WEATHERDESC = "weatherDesc";

    private static final String TAG_WINDSPEEDMILES = "windspeedMiles";

    private static final String TAG_WINDSPEEDKMPH = "windspeedKmph";

    private static final String TAG_WINDIRDEGREE = "winddir16Point";

    private static final String TAG_DATE = "date";

    private static final String TAG_TEMPMAXC = "tempMaxC";

    private static final String TAG_TEMPMAXF = "tempMaxF";

    private static final String TAG_TEMPMINC = "tempMinC";

    private static final String TAG_TEMPMINF = "tempMinF";

    // private static final String TAG_WINDIRECTION = "winddirDegree";

    private static final String TAG = null;

    JSONArray weatherData = null;

    JSONArray currentCondition = null;

    JSONArray weatherDesc = null;

    ArrayList<String> dates = new ArrayList<String>();

    ArrayList<String> descriptions = new ArrayList<String>();

    ArrayList<String> cloudCoverages = new ArrayList<String>();

    ArrayList<String> humidities = new ArrayList<String>();

    ArrayList<String> tempsMaxF = new ArrayList<String>();

    ArrayList<String> tempsMinF = new ArrayList<String>();

    ArrayList<String> tempsMaxC = new ArrayList<String>();

    ArrayList<String> tempsMinC = new ArrayList<String>();

    ArrayList<String> windDirections = new ArrayList<String>();

    ArrayList<String> daysOfWeek = new ArrayList<String>();

    int[] icons = new int[5];

    Date day = new Date();

    SimpleDateFormat simpleDate;

    private String dateObject;

    String oldChar = "-";

    String newChar = "/";

    String newFormat = new SimpleDateFormat("EEEE").format(new Date());;

    public WeatherProvider(String zip) {

        this.zipCode = zip;

    }

    public WeatherProvider(String zip, String city) {
        this.zipCode = zip;
        this.cityName = city;
    }

    public String getZipCode() {

        return zipCode;
    }

    @SuppressLint("SimpleDateFormat")
    public void getWeatherData() {

        String url = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=" + zipCode
                + "&format=json&num_of_days=5&key=" + API_KEY;

        // Create a JSON Parser instance
        JSONParser jParser = new JSONParser();

        // getting JSON String from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        try {
            // Get array of weather data
            weatherData = json.getJSONObject("data").getJSONArray("weather");
            Log.w(TAG, "Array consists of  " + weatherData.toString());
            currentCondition = json.getJSONObject("data").getJSONArray("current_condition");
            // weatherDesc =
            // json.getJSONObject("data").getJSONArray("weatherDesc");

            JSONObject cloudCov = currentCondition.getJSONObject(0);

            JSONArray description = cloudCov.getJSONArray("weatherDesc");
            JSONObject desc = description.getJSONObject(0);

            weatherDescription = desc.getString("value");
            descriptions.add(weatherDescription);

            if (weatherData != null) {
                // loop through the data array
                for (int i = 0; i < weatherData.length(); i++) {

                    // Grab specific arrays, and the objects that we need
                    // JSONObject cloudCov = currentCondition.getJSONObject(0);
                    JSONObject data = weatherData.getJSONObject(i);

                    // IMPORTANT: THESE CALLS RETURNS THE CURRENT CONDITIONS
                    // ONLY!
                    date = data.getString(TAG_DATE);

                    String newDate = date.replace(oldChar, newChar);
                    Log.w(TAG, "newDate is " + newDate);

                    Date dee = new Date(newDate);
                    DateFormat df = new SimpleDateFormat("EE");
                    String dayOfWeek = df.format(dee);

                    daysOfWeek.add(dayOfWeek);

                    dates.add(date);

                    tempF = cloudCov.getString(TAG_TEMPF);

                    cloudCoverage = cloudCov.getString(TAG_CLOUD_COVER);

                    // JSONObject current = currentCondition.getJSONObject(0);

                    observationTime = cloudCov.getString(TAG_OBSERVATION_TIME);

                    windspeedMiles = cloudCov.getString(TAG_WINDSPEEDMILES);

                    pressure = cloudCov.getString(TAG_PRESSURE);

                    precipMM = cloudCov.getString(TAG_PRECIPMM);

                    visibility = cloudCov.getString(TAG_VISIBILITY);

                    winddirDegree = cloudCov.getString(TAG_WINDIRDEGREE);

                    humidity = cloudCov.getString(TAG_HUMIDITY);

                    windspeedKmph = cloudCov.getString(TAG_WINDSPEEDKMPH);

                    tempC = cloudCov.getString(TAG_TEMPC);

                    // Return conditions for the rest of the days
                    // next maximum temp F for future days
                    String nextTempF = data.getString(TAG_TEMPMAXF);
                    tempsMaxF.add(nextTempF);

                    // next minimum temp F for future days
                    String nextTempMinF = data.getString(TAG_TEMPMINF);
                    tempsMinF.add(nextTempMinF);

                    if (i >= 1) {
                        // next description for future days
                        JSONObject shit = weatherData.getJSONObject(i);

                        JSONArray descr = shit.getJSONArray(TAG_WEATHERDESC);
                        JSONObject d = descr.getJSONObject(0);

                        String nextDescription = d.getString("value");
                        // descriptions.add(weatherDescription);
                        descriptions.add(nextDescription);
                    }

                    // next humidity for future days
                    // String nextHumidity = data.getString(TAG_HUMIDITY);
                    // humidities.add(nextHumidity);

                    // next temp max C for future days
                    String nextTempMaxC = data.getString(TAG_TEMPMAXC);
                    tempsMaxC.add(nextTempMaxC);

                    // next temp min C for future
                    String nextTempMinC = data.getString(TAG_TEMPMINC);
                    tempsMinC.add(nextTempMinC);

                }

                // Post results to log
                Log.w(TAG, "WeatherData length is " + weatherData.length());
                Log.w(TAG, "Date is " + date);
                Log.w(TAG, "Temperature F is " + tempF);
                Log.w(TAG, "Cloud Coverage is " + cloudCoverage);
                Log.w(TAG, "Weather Description is " + weatherDescription);
                Log.w(TAG, "Observation Time is " + observationTime);
                Log.w(TAG, "Windspeed Miles is " + windspeedMiles);
                Log.w(TAG, "Pressure is " + pressure);
                Log.w(TAG, "Precip is " + precipMM);
                Log.w(TAG, "Visibility is " + visibility);
                Log.w(TAG, "Wind Direction Degree is " + winddirDegree);
                Log.w(TAG, "Humidity is " + humidity);
                Log.w(TAG, "Windspeed KMPH is " + windspeedKmph);
                Log.w(TAG, "Temp C is " + tempC);
                // descriptions.remove(descriptions.size() );

                // Post list results to log
                Log.w(TAG, "TempsMaxF list contains  " + tempsMaxF.toString());
                Log.w(TAG, "TempsMinF list contains  " + tempsMinF.toString());
                Log.w(TAG, "Descriptions list contains  " + descriptions.toString());
                Log.w(TAG, "Description list length is " + descriptions.size());
                // Log.w(TAG, "Humidities list contains  " +
                // humidities.toString());
                Log.w(TAG, "TempsMaxC list contains  " + tempsMaxC.toString());
                Log.w(TAG, "TempsMinC list contains  " + tempsMinC.toString());
                Log.w(TAG, "Date list contains  " + dates.toString());
                Log.w(TAG, "Days of week list contains " + daysOfWeek.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // This method will compare the weather description to a list or predefined
    // "descriptions"
    // if there is a match or similar words it will set the icon at that item
    // index
    // according to that description
    public int[] getWeatherIcon() {

        try {

            String phrase1 = getDescriptions().get(0);
            String phrase2 = getDescriptions().get(1);
            String phrase3 = getDescriptions().get(2);
            String phrase4 = getDescriptions().get(3);
            String phrase5 = getDescriptions().get(4);

            icons[0] = R.drawable.weather_icon_na;
            icons[1] = R.drawable.weather_icon_na;
            icons[2] = R.drawable.weather_icon_na;
            icons[3] = R.drawable.weather_icon_na;
            icons[4] = R.drawable.weather_icon_na;

            for (int j = 0; j < phrases.length; j++) {

                if (phrase1.toLowerCase().contains(phrases[j].toLowerCase())) {

                    icons[0] = drawableIds[j];

                }

                if (phrase2.toLowerCase().contains(phrases[j].toLowerCase())) {

                    icons[1] = drawableIds[j];

                }

                if (phrase3.toLowerCase().contains(phrases[j].toLowerCase())) {

                    icons[2] = drawableIds[j];

                }

                if (phrase4.toLowerCase().contains(phrases[j].toLowerCase())) {

                    icons[3] = drawableIds[j];
                }

                if (phrase5.toLowerCase().contains(phrases[j].toLowerCase())) {

                    icons[4] = drawableIds[j];

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return icons;

    }

    // A bunch of neat getters so that I can just make simple calls to get the
    // data that I need
    public String getCurrentCond() {
        return currentCond;
    }

    public String getTempF() {
        return tempF;
    }

    public String getTempC() {
        return tempC;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public String getCloudCoverage() {
        return cloudCoverage;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPrecipMM() {
        return precipMM;
    }

    public String getPressure() {
        return pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getWeatherIconUrl() {
        return weatherIconUrl;
    }

    public String getWindir16Point() {
        return windir16Point;
    }

    public String getWinddirDegree() {
        return winddirDegree;
    }

    public String getWindspeedKmph() {
        return windspeedKmph;
    }

    public String getWindspeedMiles() {
        return windspeedMiles;
    }

    public String getDate() {
        return date;
    }

    public String getTempMaxC() {
        return tempMaxC;
    }

    public String getTempMaxF() {
        return tempMaxF;
    }

    public String getTempMinC() {
        return tempMinC;
    }

    public String getTempMinF() {
        return tempMinF;
    }

    public String getCityName() {
        return cityName;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getObservationTime() {
        return observationTime;
    }

    /**
     * @return the dates
     */
    public ArrayList<String> getDates() {
        return dates;
    }

    /**
     * @return the descriptions
     */
    public ArrayList<String> getDescriptions() {
        return descriptions;
    }

    /**
     * @return the cloudCoverages
     */
    public ArrayList<String> getCloudCoverages() {
        return cloudCoverages;
    }

    /**
     * @return the humidities
     */
    public ArrayList<String> getHumidities() {
        return humidities;
    }

    /**
     * @return the tempsMaxF
     */
    public ArrayList<String> getTempsMaxF() {
        return tempsMaxF;
    }

    /**
     * @return the tempsMinF
     */
    public ArrayList<String> getTempsMinF() {
        return tempsMinF;
    }

    /**
     * @return the tempsMaxC
     */
    public ArrayList<String> getTempsMaxC() {
        return tempsMaxC;
    }

    /**
     * @return the tempsMinC
     */
    public ArrayList<String> getTempsMinC() {
        return tempsMinC;
    }

    /**
     * @return the windDirections
     */
    public ArrayList<String> getWindDirections() {
        return windDirections;
    }

    public ArrayList<String> getDayOfWeek() {

        return daysOfWeek;
    }

}
