package com.red_folder.phonegap.plugin.backgroundservice.sample;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import android.app.Activity;
import android.content.Intent;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.red_folder.phonegap.plugin.backgroundservice.sample.ResultActivity;

import android.util.Log;

import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;

public class MyService extends BackgroundService {
	
	private final static String TAG = MyService.class.getSimpleName();
	
	private static String mParams = "Default";
	
	private static String mParamsDefault = "Default";

	
	public int getParams() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);  
		// Should default to a minute
		return sharedPrefs.getString(this.getClass().getName() + ".Params", mParamsDefault);	
	}

	public void setParams(String params) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);  

		SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(this.getClass().getName() + ".Params", params);
        editor.commit();
	}
	
	

	@Override
	protected JSONObject doWork() {
		JSONObject result = new JSONObject();
		
		
		
		try {
		
			if (getParams().equals(mParamsDefault)) {
				setParams(mParams);
			}
				

			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			String now = df.format(new Date(System.currentTimeMillis())); 
			String msg = "Hello " + getParams() + " - its currently " + now;

			result.put("Message", getParams());
			
			Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
			Intent resultIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.epsilonnet.pylonmanagement");//new Intent(this, Result.class);
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

			PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
			NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
				.setSound(uri)
				.setSmallIcon(R.mipmap.sym_def_app_icon)
				.setContentTitle("My notification")
				.setContentText(msg)
				.setContentIntent(resultPendingIntent);

	
			int mNotificationId = 001;
			// Gets an instance of the NotificationManager service
			NotificationManager mNotifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			// Builds the notification and issues it.
			mNotifyMgr.notify(mNotificationId, mBuilder.build());
	
		


			Log.d(TAG, "skata");
		} catch (JSONException e) {
		
		}
		
		return result;	
	}

	@Override
	protected JSONObject getConfig() {
		JSONObject result = new JSONObject();
		
		try {
			result.put("HelloTo", this.mParams);
		} catch (JSONException e) {
		}
		
		return result;
	}

	@Override
	protected void setConfig(JSONObject config) {
		try {
			if (config.has("HelloTo"))
				this.mParams = config.getString("HelloTo");
		} catch (JSONException e) {
		}
		
	}     

	@Override
	protected JSONObject initialiseLatestResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onTimerEnabled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onTimerDisabled() {
		// TODO Auto-generated method stub
		
	}
}


