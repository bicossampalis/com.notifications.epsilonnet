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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class MyService extends BackgroundService {
	
	private final static String TAG = MyService.class.getSimpleName();
	
	private final static String _MissingParam = "##Frankenstein##";
	private final static String _IPAddress = "IPAddress";
	private final static String _Port = "Port";
	private final static String _Cookie = "Cookie";
	private final static String _RoleID = "RoleID";
	private final static String _LoginData = "LoginData";
	private final static String _Https = "Https";
	
	public String getParams(String key) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);  
		return sharedPrefs.getString(this.getClass().getName() + "." + key, _MissingParam);	
	}

	public void setParams(String key, String value) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);  
		SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(this.getClass().getName() + "." + key, value);
        editor.commit();
	}

	private String GetUrl(String operation) {
		
		String ipAddress = getParams(_IPAddress);
		String port = getParams(_Port);
		String https = getParams(_Https);

		if (ipAddress.equals(_MissingParam) || port.equals(_MissingParam) || https.equals(_MissingParam))
			return _MissingParam;

		String url = "http";
		if (https.equals("1"))
			url = "https";

		url += "://" + ipAddress + ":" + port + "/hercjson/" + operation;
		return url;
	}

 
	private void Login() {
		
		String url = GetUrl("secureloginculture2");
		URL obj = null;
		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CreateNotification(98, e.getMessage());
			return;
		}
		
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection)obj.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(97, e.getMessage());
			return;
		}

		try {
	 
			 con.setDoOutput(true);
			 con.setDoInput(true);
			 con.setRequestProperty("Content-Type", "application/json");
			 con.setRequestProperty("Accept", "application/json");
			 con.setRequestMethod("POST");
		} catch (ProtocolException e) {
			e.printStackTrace();
			CreateNotification(96, e.getMessage());
			return;
		}

		OutputStreamWriter wr;
		try {
			wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(_Logindata);
			wr.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
			CreateNotification(95, e.getMessage());
			return;
		}

		int responseCode = 0;
		try {
			responseCode = con.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(94, e.getMessage());
			return;
		}

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(93, e.getMessage());
			return;
		}
		
		String inputLine;
		StringBuffer response = new StringBuffer();

		try {
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(92, e.getMessage());
			return;
		}
		
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(91, e.getMessage());
			return;
		}

		try {
			JSONObject jsonResponse = new JSONObject(response.toString());
			
			if (jsonResponse.has("Status")){
				
				String status = jsonResponse.getString("Status");
				if (status.equals("ERROR")){
					System.out.println("Error : " + jsonResponse.getString("Error"));
				} else {
					JSONObject jsonResult = new JSONObject(jsonResponse.getString("Result"));
	                setParams(_Cookie, jsonResponse.getString("cookie"));
				}
			}	
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CreateNotification(90, e.getMessage());
		}
	}
	
	private void CreateNotification(int notificationId, String contentMsg) {
			
		Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
		Intent resultIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.epsilonnet.pylonmanagement");//new Intent(this, Result.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		StringBuilder sbMsg = new StringBuilder();
		sbMsg.append("(");
		sbMsg.append(notificationId);
		sbMsg.append(")");
		sbMsg.append(notificationId);
		 
		NotificationCompat.Builder mBuilder =
			new NotificationCompat.Builder(this)
				.setSound(uri)
				//.setSmallIcon(R.mipmap-hdpi-v4.icon)
				.setContentTitle("Pylon Management")
				.setContentText(sbMsg.toString())
				.setContentIntent(resultPendingIntent);

		NotificationManager mNotifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mNotifyMgr.notify(notificationId, mBuilder.build());
	}
	

	@Override
	protected JSONObject doWork() {
		
		JSONObject result = new JSONObject();
		
		try {

			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			String now = df.format(new Date(System.currentTimeMillis())); 
			String msg = "Pylon Management " + now;

			result.put("Message", msg);
			
			Login();
			
			CreateNotification(100, msg + getParams(_IPAddress));
		} catch (JSONException e) {
			CreateNotification(99, e.getMessage());
		}
		
		return result;	
	}

	@Override
	protected JSONObject getConfig() {
		JSONObject result = new JSONObject();
		
		try {
			//result.put("HelloTo", this.mParams);
		} catch (JSONException e) {
		}
		
		return result;
	}

	@Override
	protected void setConfig(JSONObject config) {
		try {

			if (config.has(_IPAddress))
				setParams(_IPAddress, config.getString(_IPAddress));

			if (config.has(_Port))
				setParams(_Port, config.getString(_Port));

			if (config.has(_Cookie))
				setParams(_Cookie, config.getString(_Cookie));

			if (config.has(_RoleID))
				setParams(_RoleID, config.getString(_RoleID));

			if (config.has(_LoginData))
				setParams(_LoginData, config.getString(_LoginData));

			if (config.has(_Https))
				setParams(_Https, config.getString(_Https));

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


