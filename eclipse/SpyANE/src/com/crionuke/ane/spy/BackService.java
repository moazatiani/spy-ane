package com.crionuke.ane.spy;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class BackService extends Service implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

	private int interval;
	private String url;
	private String token;
	private int icon;
	private String title;
	private String text;	
	
	private GoogleApiClient googleApiClient = null;
	
	@Override
    public void onCreate() {
        super.onCreate();
        
        Log.i(Constants.logTag, "BackService created");
    }
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		interval = intent.getIntExtra(Constants.SHARED_KEY_INTERVAL, Constants.SHARED_DEFAULT_INT_VALUE);
		url = intent.getStringExtra(Constants.SHARED_KEY_URL);
		token = intent.getStringExtra(Constants.SHARED_KEY_TOKEN);
		icon = intent.getIntExtra(Constants.SHARED_KEY_ICON, Constants.SHARED_DEFAULT_INT_VALUE);
		title = intent.getStringExtra(Constants.SHARED_KEY_TITLE);
		text = intent.getStringExtra(Constants.SHARED_KEY_TEXT);
		
		Log.i(Constants.logTag, "BackService start command with parameters: " + interval + ", " + url + ", " + token + ", " + icon + ", " + title + ", " + text);
		
		try {
			Notification.Builder builder = new Notification.Builder(this)
				.setSmallIcon(icon)
				//.setTicker("Ticker Text")
				//.setSubText("Test sub text")
				.setContentTitle(title)
				.setContentText(text);
				//.setContentInfo("Test content info");
			
			Notification notification;
			/*if (Build.VERSION.SDK_INT < 16)
				notification = builder.getNotification();
			else*/
				notification = builder.build();
					
			startForeground(Constants.BACKSERVICE_ID, notification);
			
			Log.i(Constants.logTag, "BackService startForeground");
			
		} catch (Exception e) {
			Log.i(Constants.logTag, "BackService create error: " + e.getMessage());
		}
	    
		
		googleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		Log.i(Constants.logTag, "GoogleApiClient" + googleApiClient);
		googleApiClient.connect();
		
	   return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        Log.i(Constants.logTag, "BackService destroy");
        
        if (googleApiClient != null) {
        	LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        	
        	googleApiClient.disconnect();
        }
        
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(Constants.logTag, "BackService bind");
        return  null;
    }

    
    @Override
	public void onConnectionSuspended(int arg0) {
		Log.i(Constants.logTag, "GoogleApiClient connection suspended: " + arg0);
		
	}
	
	@Override
	public void onConnected(Bundle arg0) {
		Log.i(Constants.logTag, "GoogleApiClient connected");
		Log.i(Constants.logTag, "GoogleApiClient in onConnected method: " + googleApiClient);
		
		/*
		Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
        	Log.i(Constants.logTag, "LastLocation:");
        	Log.i(Constants.logTag, String.valueOf(location.getLatitude()));
        	Log.i(Constants.logTag, String.valueOf(location.getLongitude()));
        } else {
        	Log.i(Constants.logTag, "LastLocation is null");
        }*/
        
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(interval * 1000);
        locationRequest.setFastestInterval(interval * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.i(Constants.logTag, "GoogleApiClient connection failed: " + arg0.getErrorMessage());
	}
    
	@Override
    public void onLocationChanged(Location location) {
		if (location != null) {
			Log.i(Constants.logTag, "New location:");
			Log.i(Constants.logTag, String.valueOf(location.getLatitude()));
			Log.i(Constants.logTag, String.valueOf(location.getLongitude()));
			
			String request = this.url + "/" + this.token + "/" + location.getLatitude() + "/" + location.getLongitude();
			
			Log.i(Constants.logTag, "Make SendGeo to " + request);
			
			SendGeo sendGeo = new SendGeo();
			sendGeo.execute(request);
			
			Log.i(Constants.logTag, "4");
			
		} else {
			Log.i(Constants.logTag, "Null location in LocationChanged method:");
		}
		
		//TODO: send location to server use url and token vars
    }
	
	private class SendGeo extends AsyncTask<String, Void, Void> {
		@Override
	    protected Void doInBackground(String... params) {
			
			HttpsURLConnection connector = null;
			
			try {
				
				URL server = new URL(params[0]);
				Log.i(Constants.logTag, "1");
				connector = (HttpsURLConnection) server.openConnection();
				if (connector != null) {
					Log.i(Constants.logTag, "2");
					if (connector.getResponseCode() == HttpsURLConnection.HTTP_OK) {
						Log.i(Constants.logTag, "HTTP OK");
					} else {
						Log.i(Constants.logTag, "FAILED");
					}
					Log.i(Constants.logTag, "2end");
				} else {
					Log.i(Constants.logTag, "Null connector");
				}
				
				Log.i(Constants.logTag, "3");
				
			} catch (IOException e) {
				Log.i(Constants.logTag, "IOException server request");
			}
			
			if (connector != null) {
				connector.disconnect();
			}			
			
			return null;
	    }
	}
}
