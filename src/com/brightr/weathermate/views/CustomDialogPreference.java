package com.brightr.weathermate.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.Toast;

public class CustomDialogPreference extends DialogPreference {
	
	private Context context;

	public CustomDialogPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		
		this.context = context;
		
	}

	public CustomDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    switch (which) {
	    case DialogInterface.BUTTON_POSITIVE:
	        // send feedback
	    	String[] email = {"jjbyfield@gmail.com"};
	    	Intent e = new Intent(android.content.Intent.ACTION_SEND);
	    	e.putExtra(android.content.Intent.EXTRA_EMAIL, email );
	    	e.setType("plain/text");
	    	context.startActivity(Intent.createChooser(e, "Send Feedback"));
	    
	        break;
	    case DialogInterface.BUTTON_NEGATIVE:
	    	//Toast.makeText(getContext(), "cancel", Toast.LENGTH_SHORT).show();
	        
	        break;
	    }
	}
	
	
	
	

}
