package com.brightr.weathermate.parsers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.util.Log;

//Class to retrieve the flight tracking data and parse the returned JSON

public class FlightTrackingParser {
	
	private static final String API_KEY = "50ba19799c05dfa32b320f1ed0320decf7d5383d";
	private static final String userName = "jjbyfield";
	private static final int QUERY_FLIGHTINFO = 0;
	private static final int QUERY_FLIGHTDETAILS = 1;
	
	
//Input stream that will read the data
	   static InputStream is = null;
	    static JSONObject jObj = null;
	    static String json = "";
	    
	    
	    // constructor
	    public FlightTrackingParser() {
	 
	    }
	    
	    
	    public static JSONObject getJSONFromUrl(String query, String maxResults) {
	    	
	    String url = "http://flightxml.flightaware.com/json/FlightXML2/FlightInfoEx?ident="+query+"&howMany="+maxResults+"&offset=0";
	    	 
	        // Making HTTP request
	        try {
	            // defaultHttpClient
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpGet httpGet = new HttpGet(url);
	           // HttpPost httpPost = new HttpPost(url);
	            
	            httpGet.addHeader(BasicScheme.authenticate(
	            		 new UsernamePasswordCredentials(userName, API_KEY),
	            		 "UTF-8", false));
	            
	            
	 
	            HttpResponse httpResponse = httpClient.execute(httpGet);
	            
	           
	            HttpEntity httpEntity = httpResponse.getEntity();
	            
	            is = httpEntity.getContent();          
	 
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            is.close();
	            json = sb.toString();
	            Log.w("FlightTrackingParser", json.toString());
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }
	 
	        // try parsing the string to a JSON object
	        try {
	        	if(json != null){
	            jObj = new JSONObject(json);
	        	}else{
	        		jObj = null;
	        	}
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
	 
	        // return JSON String
	        return jObj;
	       
	 
	    }
	    
	    
	    
	    
	    public static JSONObject getFlightDetails(String flightId) {
	    	
		    String url = "http://flightxml.flightaware.com/json/FlightXML2/AirlineFlightInfo?faFlightID=" + flightId;
		    	 
		        // Making HTTP request
		        try {
		            // defaultHttpClient
		            DefaultHttpClient httpClient = new DefaultHttpClient();
		            HttpGet httpGet = new HttpGet(url);
		           // HttpPost httpPost = new HttpPost(url);
		            
		            httpGet.addHeader(BasicScheme.authenticate(
		            		 new UsernamePasswordCredentials(userName, API_KEY),
		            		 "UTF-8", false));
		            
		            
		 
		            HttpResponse httpResponse = httpClient.execute(httpGet);
		            
		           
		            HttpEntity httpEntity = httpResponse.getEntity();
		            
		            is = httpEntity.getContent();          
		 
		        } catch (UnsupportedEncodingException e) {
		            e.printStackTrace();
		        } catch (ClientProtocolException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		 
		        try {
		            BufferedReader reader = new BufferedReader(new InputStreamReader(
		                    is, "iso-8859-1"), 8);
		            StringBuilder sb = new StringBuilder();
		            String line = null;
		            while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		            }
		            is.close();
		            json = sb.toString();
		            Log.w("FlightTrackingParser", json.toString());
		        } catch (Exception e) {
		            Log.e("Buffer Error", "Error converting result " + e.toString());
		        }
		 
		        // try parsing the string to a JSON object
		        try {
		        	if(json != null){
		            jObj = new JSONObject(json);
		        	}else{
		        		jObj = null;
		        	}
		        } catch (JSONException e) {
		            Log.e("JSON Parser", "Error parsing data " + e.toString());
		        }
		 
		        // return JSON String
		        return jObj;
		       
		 
		    }
		    
		    
		    
	    
	    
	    
	    

}





