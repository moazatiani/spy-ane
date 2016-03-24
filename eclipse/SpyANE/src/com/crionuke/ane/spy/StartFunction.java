package com.crionuke.ane.spy;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
			int icon = arg0.getResourceId("drawable.ic_launcher");
			String title = arg1[3].getAsString();
			String text = arg1[4].getAsString();
			
			Log.i(Constants.logTag, "Start function: " + interval + ", " + url + ", " + token + ", " + title + ", " + text);
			
			// Saved parameters in shared data
			SharedPreferences sharedPref = activity.getApplicationContext().getSharedPreferences(Constants.SHARED_FILE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putInt(Constants.SHARED_KEY_INTERVAL, interval);
			editor.putString(Constants.SHARED_KEY_URL, url);
			editor.putString(Constants.SHARED_KEY_TOKEN, token);
			editor.putInt(Constants.SHARED_KEY_ICON, icon);
			editor.putString(Constants.SHARED_KEY_TITLE, title);
			editor.putString(Constants.SHARED_KEY_TEXT, text);
			editor.commit();
			
			Intent intent = new Intent(activity, BackService.class);
			intent.putExtra(Constants.SHARED_KEY_INTERVAL, interval);
			intent.putExtra(Constants.SHARED_KEY_URL, url);
			intent.putExtra(Constants.SHARED_KEY_TOKEN, token);
			intent.putExtra(Constants.SHARED_KEY_ICON, icon);
			intent.putExtra(Constants.SHARED_KEY_TITLE, title);
			intent.putExtra(Constants.SHARED_KEY_TEXT, text);
			
			activity.startService(intent);
			
			result = FREObject.newObject(Constants.RESULT_STARTED);
		} catch (Exception e) {
			Log.i(Constants.logTag, "Start function error: " + e.getMessage());
		}
		
		return result;
	}

}
