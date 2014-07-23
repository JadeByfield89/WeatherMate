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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.util.Log;

public class JSONTrafficParser {
	
	
	   static InputStream is = null;
	    static JSONObject jObj = null;
	    static String json = "";
	    
	    
	    // constructor
	    public JSONTrafficParser() {
	 
	    }
	    
	    
	    public JSONObject getJSONFromUrl(String url) {
	    	 
	        // Making HTTP request
	        try {
	            // defaultHttpClient
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpPost httpPost = new HttpPost(url);
	 
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            
	           
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
	            Log.w("JSONTrafficParser", json.toString());
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
	    
	    
	    public static JSONObject requestWebService(String serviceUrl) {
	    	disableConnectionReuseIfNecessary();

	    	HttpURLConnection urlConnection = null;
	    	try {
	    		// create connection
	    		URL urlToRequest = new URL(serviceUrl);
	    		urlConnection = (HttpURLConnection) 
	    			urlToRequest.openConnection();
	    		int CONNECTION_TIMEOUT = 0;
	    		int DATARETRIEVAL_TIMEOUT = 0;
	    		
				urlConnection.setConnectTimeout(CONNECTION_TIMEOUT );
	    		
				urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT );
	    		
	    		// handle issues
	    		int statusCode = urlConnection.getResponseCode();
	    		Log.w("JSONTrafficParser", "Response Code is " + statusCode);
	    		if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
	    			// handle unauthorized (if service requires user login)
	    		} else if (statusCode != HttpURLConnection.HTTP_OK) {
	    			// handle any other errors, like 404, 500,..
	    		}
	    		
	    		// create JSON object from content
	    		InputStream in = new BufferedInputStream(
	    			urlConnection.getInputStream());
	    		return new JSONObject(getResponseText(in));
	    		
	    	} catch (MalformedURLException e) {
	    		// URL is invalid
	    	} catch (SocketTimeoutException e) {
	    		// data retrieval or connection timed out
	    	} catch (IOException e) {
	    		// could not read response body 
	    		// (could not create input stream)
	    	} catch (JSONException e) {
	    		// response body is no valid JSON string
	    	} finally {
	    		if (urlConnection != null) {
	    			urlConnection.disconnect();
	    		}
	    	}		
	    	
	    	return null;
	    }

	    /**
	     * required in order to prevent issues in earlier Android version.
	     */
	    private static void disableConnectionReuseIfNecessary() {
	    	// see HttpURLConnection API doc
	    	if (Integer.parseInt(Build.VERSION.SDK) 
	    			< Build.VERSION_CODES.FROYO) {
	    		System.setProperty("http.keepAlive", "false");
	    	}
	    }

	    private static String getResponseText(InputStream inStream) {
	    	// very nice trick from 
	    	// http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
	    	return new Scanner(inStream).useDelimiter("\\A").next();
	    }

	    
	    
	    
	   

}
