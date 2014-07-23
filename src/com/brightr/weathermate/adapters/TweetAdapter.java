package com.brightr.weathermate.adapters;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.util.Linkify;
import android.text.util.Linkify.TransformFilter;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brightr.weathermate.R;

public class TweetAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<String> tweets;
	private ArrayList<String> times;
	private ArrayList<Integer> icons;
	private ArrayList<String> mUsernames ;
	
	LayoutInflater inflater;
	
	
	public TweetAdapter(Context context, ArrayList<String> tweets, ArrayList<String> times){
		
		this.mContext = context;
		this.tweets = tweets;
		this.times = times;
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mUsernames = new ArrayList<String>();
		mUsernames.add("cnn");
		mUsernames.add("yahoonews");
		mUsernames.add("abcnews");
		mUsernames.add("nytimes");
		mUsernames.add("cnetnews");
		mUsernames.add("engadget");
		mUsernames.add("reddit");
		mUsernames.add("rawstory");
		mUsernames.add("reuters");
		mUsernames.add("drudge_report");
		mUsernames.add("huffingtonpost");
		
		mUsernames.add("espn");
		mUsernames.add("yahoosports");
		mUsernames.add("cbssports");
		mUsernames.add("bleacherreport");
		mUsernames.add("sinow");
		
		mUsernames.add("youtube");
		mUsernames.add("collegehumor");
		mUsernames.add("forbes");
		mUsernames.add("9gag");
		mUsernames.add("funnyordie");
		mUsernames.add("ted_tweets");
		
		icons = new ArrayList<Integer>();
		icons.add(R.drawable.news_cnn);
		icons.add(R.drawable.news_yahoo);
		icons.add(R.drawable.news_abc);
		icons.add(R.drawable.news_nytimes);
		icons.add(R.drawable.news_cnet);
		icons.add(R.drawable.news_engadget);
		icons.add(R.drawable.news_reddit);
		icons.add(R.drawable.news_rawstory);
		icons.add(R.drawable.news_reuters);
		icons.add(R.drawable.news_drudge);
		icons.add(R.drawable.news_huffingtonpost);
		icons.add(R.drawable.news_espn);
		icons.add(R.drawable.news_yahoosports);
		icons.add(R.drawable.news_cbssports);
		icons.add(R.drawable.news_bleacherreport);
		icons.add(R.drawable.news_sportsillustrated);
		icons.add(R.drawable.news_logo);
		icons.add(R.drawable.news_collegehumor);
		icons.add(R.drawable.news_forbes);
		icons.add(R.drawable.news_ninegag);
		icons.add(R.drawable.news_funnyordie);
		icons.add(R.drawable.news_ted);
		
		
		
		
		
	}
	
	public void linkifyText(TextView text){
		
		TransformFilter filter = new TransformFilter() {
		    public final String transformUrl(final Matcher match, String url) {
		        return match.group();
		    }
		};

		Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9_-]+)");
		String mentionScheme = "http://www.twitter.com/";
		Linkify.addLinks(text, mentionPattern, mentionScheme, null, filter);

		Pattern hashtagPattern = Pattern.compile("#([A-Za-z0-9_-]+)");
		String hashtagScheme = "http://www.twitter.com/search/";
		Linkify.addLinks(text, hashtagPattern, hashtagScheme, null, filter);

		Pattern urlPattern = Patterns.WEB_URL;
		Linkify.addLinks(text, urlPattern, null, null, filter);
		
	}

	@Override
	public int getCount() {
		
		return tweets.size();
	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}
	
	private class ViewHolder{
		
		TextView tweet;
		TextView username;
		TextView time;
		ImageView icon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder v;
		
		if(convertView == null){
			
			convertView = inflater.inflate(R.layout.social_list_row, null);
			v = new ViewHolder();
			v.tweet = (TextView) convertView.findViewById(R.id.tvTweet);
			v.time = (TextView) convertView.findViewById(R.id.tvTweetTime);
			v.icon = (ImageView) convertView.findViewById(R.id.ivTwitterIcon);
			v.username = (TextView) convertView.findViewById(R.id.tvTwitterUsername);
			
			convertView.setTag(v);
		}
		
		else
			v = (ViewHolder) convertView.getTag();
		    v.tweet.setText(tweets.get(position));
		    linkifyText(v.tweet);
		    v.time.setText(times.get(position));
		    v.icon.setImageResource(icons.get(position));
		    v.username.setText(mUsernames.get(position));
		    
		
		
		
		return convertView;
	}

}
