package com.crionuke.ane.spy;
import java.util.HashMap;

import java.util.Map;

import com.adobe.air.SpyANEActivityResultCallback;
import com.adobe.air.AndroidActivityWrapper;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;

import android.content.Intent;
import android.util.Log;

public class Context extends FREContext implements SpyANEActivityResultCallback {

	private AndroidActivityWrapper androidActivityWrapper;
	
	public Context() {
		androidActivityWrapper = AndroidActivityWrapper.GetAndroidActivityWrapper();
		androidActivityWrapper.addActivityResultListener(this);
	}
	
	@Override
	public void dispose() {
		Log.i(Constants.logTag, "Dispose");
		
		if (androidActivityWrapper != null) {
            androidActivityWrapper.removeActivityResultListener(this);
            androidActivityWrapper = null;
        }
	}

	@Override
	public Map<String, FREFunction> getFunctions() {
		Map<String, FREFunction> map = new HashMap<String, FREFunction>();
		map.put("start", new StartFunction());
		map.put("check", new CheckFunction());
		map.put("stop", new StopFunction());
		return map;
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.i(Constants.logTag, "onActivityResult with requestCode=" + requestCode + " and resultCode=" + resultCode);
    }
	
}
