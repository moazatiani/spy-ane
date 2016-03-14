package com.crionuke.ane.spy;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;

import android.util.Log;

public class Extension implements FREExtension {
	
	@Override
	public FREContext createContext(String arg0) {
		Log.i(Constants.logTag, "Create context");
		return new Context();
	}

	@Override
	public void dispose() {
		Log.i(Constants.logTag, "Dispose extension");			
	}

	@Override
	public void initialize() {
		Log.i(Constants.logTag, "Initialize extension");
	}

}
