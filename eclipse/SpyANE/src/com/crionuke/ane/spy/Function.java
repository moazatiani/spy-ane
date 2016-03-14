package com.crionuke.ane.spy;

import com.adobe.fre.FREContext;

import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

import android.util.Log;

public class Function implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		
		FREObject result = null;
		
		try {
			result = FREObject.newObject(1);
		} catch (Exception e) {
			Log.i(Constants.logTag, Function.class.getSimpleName() + " error: " + e.getMessage());
		}
		
		return result;
	}

}
