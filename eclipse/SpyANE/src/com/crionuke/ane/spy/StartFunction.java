package com.crionuke.ane.spy;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class StartFunction implements FREFunction {
	
	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		
		FREObject result = null;
		
		try {
			Activity activity = arg0.getActivity();
			
			int interval = arg1[0].getAsInt();
			String url = arg1[1].getAsString();
			String token = arg1[2].getAsString();
			String title = arg1[3].getAsString();
			String text = arg1[4].getAsString();
			
			Log.i(Constants.logTag, "Start function: " + interval + ", " + url + ", " + token + ", " + title + ", " + text);
			
			Intent intent = new Intent(activity, BackService.class);
			intent.putExtra("interval", interval);
			intent.putExtra("url", url);
			intent.putExtra("token", token);
			intent.putExtra("nTitle", title);
			intent.putExtra("nText", text);
			
			activity.startService(intent);
			
			result = FREObject.newObject(0);
		} catch (Exception e) {
			Log.i(Constants.logTag, "Start function error: " + e.getMessage());
		}
		
		return result;
	}

}
