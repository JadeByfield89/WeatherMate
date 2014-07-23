package com.brightr.weathermate.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.brightr.weathermate.R;

public class WebsiteViewActivity extends Activity {
	
	
	ProgressDialog pd;
	
	
	//Url passed in from news activity
	String url;
	
	
	//WebView
	WebView browser;
	
	//ProgressBar
	ProgressBar pBar;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		
		url = getIntent().getStringExtra("key");
		
		browser = (WebView) findViewById(R.id.wvNewsWebview);
		
		browser.getSettings().setJavaScriptEnabled(true);
		browser.getSettings().setLoadWithOverviewMode(true);
		browser.getSettings().setUseWideViewPort(true);
		browser.setWebViewClient(new BrowserClient());
		browser.getSettings().setBuiltInZoomControls(true);
		browser.getSettings().setRenderPriority(RenderPriority.HIGH);
		
		try{
		browser.loadUrl(url);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
	//new LoadWebpage().execute();
		
		
	
		
		
	
		
		
	}
	
	private class BrowserClient extends WebViewClient{

	
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			view.loadUrl(url);
			return true;
		}
		
		
		
		
	}
	
	private class LoadWebpage extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... arg0) {
			
			
			browser.getSettings().setJavaScriptEnabled(true);
			browser.getSettings().setLoadWithOverviewMode(true);
			browser.getSettings().setUseWideViewPort(true);
			browser.setWebViewClient(new BrowserClient());
			browser.getSettings().setBuiltInZoomControls(true);
			browser.getSettings().setRenderPriority(RenderPriority.HIGH);
			
			try{
			browser.loadUrl(url);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			
			return null;
		}

	
		@Override
		protected void onPostExecute(String result) {
			
			pd.dismiss();
		}

		
		@Override
		protected void onPreExecute() {
			
			pd = new ProgressDialog(WebsiteViewActivity.this);
			pd.setTitle("Loading Webpage");
			pd.setMessage("Loading " + url);
			
			pd.show();
		}
		
		
		
		
	}
	
	

}
