package com.crionuke.ane.spy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class Receiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		try {
			SharedPreferences sharedPref = context.getSharedPreferences(Constants.SHARED_FILE_NAME, Context.MODE_PRIVATE);
			int interval = sharedPref.getInt(Constants.SHARED_KEY_INTERVAL, Constants.SHARED_DEFAULT_INT_VALUE);
			String url = sharedPref.getString(Constants.SHARED_KEY_URL, Constants.SHARED_DEFAULT_STRING_VALUE);
			String token = sharedPref.getString(Constants.SHARED_KEY_TOKEN, Constants.SHARED_DEFAULT_STRING_VALUE);
			int icon = sharedPref.getInt(Constants.SHARED_KEY_ICON, Constants.SHARED_DEFAULT_INT_VALUE);
			String title = sharedPref.getString(Constants.SHARED_KEY_TITLE, Constants.SHARED_DEFAULT_STRING_VALUE);
			String text = sharedPref.getString(Constants.SHARED_KEY_TEXT, Constants.SHARED_DEFAULT_STRING_VALUE);
			
			Log.i(Constants.logTag, "onReceive : Start Back Service : " + interval + ", " + url + ", " + token + ", " + title + ", " + text);
			
			Intent backService = new Intent(context, BackService.class);
			backService.putExtra(Constants.SHARED_KEY_INTERVAL, interval);
			backService.putExtra(Constants.SHARED_KEY_URL, url);
			backService.putExtra(Constants.SHARED_KEY_TOKEN, token);
			backService.putExtra(Constants.SHARED_KEY_ICON, icon);
			backService.putExtra(Constants.SHARED_KEY_TITLE, title);
			backService.putExtra(Constants.SHARED_KEY_TEXT, text);
			context.startService(backService);
			
		} catch (Exception e) {
			Log.i(Constants.logTag, "onReceive error: " + e.getMessage());
		}
		
	}

}
