package com.crionuke.ane.spy;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class BackService extends Service implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

	private String log_tag = "SpyANE";
	
	private int interval;
	private String url;
	private String token;
	private String nTitle;
	private String nText;	
	
	private GoogleApiClient googleApiClient = null;
	
	@Override
    public void onCreate() {
        super.onCreate();
        
        Log.i(log_tag, "BackService created");
    }
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		interval = intent.getIntExtra("interval", 10);
		url = intent.getStringExtra("url");
		token = intent.getStringExtra("token");
		nTitle = intent.getStringExtra("nTitle");
		nText = intent.getStringExtra("nText");
		
		Log.i(log_tag, "BackService start command with parameters: " + interval + ", " + url + ", " + token + ", " + nTitle + ", " + nText);
				
		try {
			Notification.Builder builder = new Notification.Builder(this)
						.setContentTitle(nTitle.toString())
						.setContentInfo(nText.toString());
			Notification notification;
			if (Build.VERSION.SDK_INT < 16)
				notification = builder.getNotification();
			else
				notification = builder.build();
				
			startForeground(779, notification);
			
			Log.i(log_tag, "BackService startForeground 779");
			
		} catch (Exception e) {
			Log.i(log_tag, "BackService create error: " + e.getMessage());
		}
	    
		googleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		Log.i(log_tag, "GoogleApiClient" + googleApiClient);
		googleApiClient.connect();
		
	   return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        Log.i(log_tag, "BackService destroy");
        
        if (googleApiClient != null) {
        	LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        	
        	googleApiClient.disconnect();
        }
        
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(log_tag, "BackService bind");
        return  null;
    }

    
    @Override
	public void onConnectionSuspended(int arg0) {
		Log.i(log_tag, "GoogleApiClient connection suspended: " + arg0);
		
	}
	
	@Override
	public void onConnected(Bundle arg0) {
		Log.i(log_tag, "GoogleApiClient connected");
		Log.i(log_tag, "GoogleApiClient in onConnected method: " + googleApiClient);
		
		Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
        	Log.i(log_tag, "LastLocation:");
        	Log.i(log_tag, String.valueOf(location.getLatitude()));
        	Log.i(log_tag, String.valueOf(location.getLongitude()));
        } else {
        	Log.i(log_tag, "LastLocation is null");
        }
        
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(interval * 1000);
        locationRequest.setFastestInterval(interval * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.i(log_tag, "GoogleApiClient connection failed: " + arg0.getErrorMessage());
	}
    
	@Override
    public void onLocationChanged(Location location) {
		if (location != null) {
			Log.i(log_tag, "New location:");
			Log.i(log_tag, String.valueOf(location.getLatitude()));
			Log.i(log_tag, String.valueOf(location.getLongitude()));
		} else {
			Log.i(log_tag, "Null location in LocationChanged method:");
		}
		
		//TODO: send location to server use url and token vars
    }
}
