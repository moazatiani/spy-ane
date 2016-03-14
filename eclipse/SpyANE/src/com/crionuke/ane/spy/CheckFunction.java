package com.crionuke.ane.spy;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

public class CheckFunction implements FREFunction {
	
	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		FREObject result = null;
		
		try {
			Log.i(Constants.logTag, "Check spy");
			
			Activity activity = arg0.getActivity();
			
			ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
	        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	            if (BackService.class.getName().equals(service.service.getClassName())) {
	            	result = FREObject.newObject(1);
	            	Log.i(Constants.logTag, "Check result: 1");
	            	return result;
	            }
	        }
	        
	        Log.i(Constants.logTag, "Check result: 0");
			result = FREObject.newObject(0);
		} catch (Exception e) {
			Log.i(Constants.logTag, "Check function error");
		}
		
		return result;
	}

}
