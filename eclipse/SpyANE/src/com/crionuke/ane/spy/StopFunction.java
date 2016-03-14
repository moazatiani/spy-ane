package com.crionuke.ane.spy;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class StopFunction implements FREFunction {
	
	
	
	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		FREObject result = null;
		
		try {
			Log.i(Constants.logTag, "Stop spy");
			
			Activity activity = arg0.getActivity();
			
			activity.stopService(new Intent(activity, BackService.class));
			
			result = FREObject.newObject(Constants.RESULT_STOPPED);
			
		} catch (Exception e) {
			Log.i(Constants.logTag, "Stop function error: " + e.getMessage());
		}
		
		return result;
	}

}
