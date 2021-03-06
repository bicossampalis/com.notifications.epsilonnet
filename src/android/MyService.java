package com.red_folder.phonegap.plugin.backgroundservice.sample;


import org.json.JSONArray;
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
import android.util.Log;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

public class MyService extends BackgroundService {

private final static String TAG = MyService.class.getSimpleName();

private final static String _MissingParam = "##Frankenstein##";
private final static String _IPAddress = "IPAddress";
private final static String _Port = "Port";
private final static String _Cookie = "Cookie";
private final static String _RoleID = "RoleID";
private final static String _LoginData = "LoginData";
private final static String _Https = "Https";
private final static String _Uuid = "Uuid";
private final static String _LogData = "LogData";
private final static String _LogLimit = "LogLimit";
private final static String _Package = "Package";
private final static String _CallName = "CallName";
private final static String _AppName = "AppName";

	public int getIntParams(String key) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		return sharedPrefs.getInt(this.getClass().getName() + "." + key, 10);
	}
	
	public String getParams(String key) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		return sharedPrefs.getString(this.getClass().getName() + "." + key, _MissingParam);
	}

	public void setIntParams(String key, int value) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putInt(this.getClass().getName() + "." + key, value);
		editor.commit();
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

	private void setLogData(String timestamp, String contentMsg, String error) {
		try {
			String logStr = getParams(_LogData);
			JSONObject logJson = null;
			
			if(logStr.equals(_MissingParam)){
				logJson = new JSONObject();
				JSONArray logEmpty = new JSONArray();
				logJson.put("Data", logEmpty);
			} else
				logJson = new JSONObject(logStr);
			
			JSONArray log =  (JSONArray)logJson.get("Data");

			while (log.length() >= getIntParams(_LogLimit))
				log.remove(0);
			
			JSONObject newJson = new JSONObject();
			newJson.put("TimeStamp", timestamp);
			newJson.put("Message", contentMsg);
			newJson.put("Error", error);
			log.put(newJson);
			
			JSONObject newLogJson = new JSONObject();
			newLogJson.put("Data", log);
			setParams(_LogData, newLogJson.toString());
			
		} catch (JSONException e) {
			JSONObject errJSONObj = new JSONObject();
		}
	}
	
	private boolean Login() {
		
		String url = GetUrl("secureloginculture2");
		String loginData = getParams(_LoginData);
		if (url.equals(_MissingParam) || loginData.equals(_MissingParam))
			return false;

		URL obj = null;
		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 1","true");
			return false;
		}
		
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection)obj.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 2","true");
			return false;
		}

		try {
	 
			 con.setDoOutput(true);
			 con.setDoInput(true);
			 con.setRequestProperty("Content-Type", "application/json");
			 con.setRequestProperty("Accept", "application/json");
			 con.setRequestMethod("POST");
		} catch (ProtocolException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 3","true");
			return false;
		}

		OutputStreamWriter wr;
		try {
			wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(loginData);
			wr.flush();
		} catch (IOException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 4","true");
			return false;
		}

		int responseCode = 0;
		try {
			responseCode = con.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 5","true");
			return false;
		}

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 6","true");
			return false;
		}
		
		String inputLine;
		StringBuffer response = new StringBuffer();

		try {
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 7","true");
			return false;
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 8","true");
			return false;
		}

		try {
			JSONObject jsonResponse = new JSONObject(response.toString());

			if (jsonResponse.has("Status")){
				
				String status = jsonResponse.getString("Status");
				if (status.equals("ERROR")){
					setLogData(DateTimeNow(), "Error : " + jsonResponse.getString("Error")+" - 9","true");
					return false;
				} else {
					JSONObject jsonResult = new JSONObject(jsonResponse.getString("Result"));
	                setParams(_Cookie, jsonResult.getString("cookie"));
				}
			}	

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 10","true");
			return false;
		}
		
		return true;
	}
	
	private void GetNotification() {
		String callName = getParams(_CallName);
		String url = GetUrl(callName);
		String cookie = getParams(_Cookie);
		if (url.equals(_MissingParam) || cookie.equals(_MissingParam))
			return ;
		
		URL obj = null;
		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 11","true");
			return ;
		}
		
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection)obj.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 12","true");
			return ;
		}

		try {
	 
			 con.setDoOutput(true);
			 con.setDoInput(true);
			 con.setRequestProperty("Content-Type", "application/json");
			 con.setRequestProperty("Accept", "application/json");
			 con.setRequestMethod("POST");
		} catch (ProtocolException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 13","true");
			return ;
		}

		JSONObject inputParams = new JSONObject();
		try {
			inputParams.put("cookie", getParams(_Cookie));
			inputParams.put("uuid", getParams(_Uuid));
			inputParams.put("roleid", getParams(_RoleID));
		} catch (JSONException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 14","true");
			return;
		}

		OutputStreamWriter wr;
		try {
			wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(inputParams.toString());
			wr.flush();
		} catch (IOException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 15","true");
			return ;
		}

		int responseCode = 0;
		try {
			responseCode = con.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 16","true");
			return ;
		}

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 17","true");
			return ;
		}
		
		String inputLine;
		StringBuffer response = new StringBuffer();

		try {
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 18","true");
			return ;
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 19","true");
			return ;
		}

		try {
			JSONObject jsonResponse = new JSONObject(response.toString());

			if (jsonResponse.has("Status")){
				
				String status = jsonResponse.getString("Status");
				if (status.equals("ERROR")){
					setLogData(DateTimeNow(), "Error : " + jsonResponse.getString("Error")+" - 20","true");
				} else {
					
					JSONObject jsonResult = new JSONObject(jsonResponse.getString("Result"));
					String RetrievedDataStr = jsonResult.getString("RetrievedData");
					
					if (RetrievedDataStr != null && RetrievedDataStr.length() != 0) {
						CreateNotification(200, RetrievedDataStr);
						setLogData(DateTimeNow(), RetrievedDataStr,"false");
					} else
						setLogData(DateTimeNow(), "No Data Found","false");
				}
			}	

		} catch (JSONException e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 22","true");
			return ;
		}
	}

	private void CreateNotification(int notificationId, String contentMsg) {
		try {
			Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			String pkg = getParams(_Package);
			String appName = getParams(_AppName);
			Intent resultIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(pkg);//new Intent(this, Result.class);
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

			PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			StringBuilder sbMsg = new StringBuilder();
			sbMsg.append("(");
			sbMsg.append(notificationId);
			sbMsg.append(")");
			sbMsg.append(contentMsg);
			
			// int resourceID = getApplicationContext().getResources().getIdentifier( "icon" , "drawable", package );
			 Drawable icon = getApplicationContext().getPackageManager().getApplicationIcon(pkg);
			   Bitmap bitmap = ((BitmapDrawable)icon).getBitmap();
			NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
					.setSound(uri)
					.setSmallIcon(R.drawable.ic_dialog_info)
					.setLargeIcon(bitmap)
					.setContentTitle(appName)
					.setContentText(contentMsg)
					.setContentIntent(resultPendingIntent);

			NotificationManager mNotifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			mNotifyMgr.notify(notificationId, mBuilder.build());
		} catch (Exception e) {
			e.printStackTrace();
			setLogData(DateTimeNow(), e.getMessage()+" - 23","true");
			return ;
		}
	}
	
	private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
	
	private String DateTimeNow(){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		String now = df.format(new Date(System.currentTimeMillis())); 
		return now;
	}
	
	@Override
	protected JSONObject doWork() {
		JSONObject result = new JSONObject();
		try {
			String appName = getParams(_AppName);
			String msg = appName + DateTimeNow();
			result.put("Message", msg);
			
			if(isNetworkAvailable()){
				if (Login()) {
					GetNotification();
				}
			}
		} catch (JSONException e) {
			setLogData(DateTimeNow(), e.getMessage()+" - 24","true");
		}
		
		return result;	
	}

	@Override
	protected JSONObject getConfig() {
		JSONObject result = new JSONObject();
		
		try {
			result.put(_IPAddress, getParams(_IPAddress));
			result.put(_Port, getParams(_Port));
			result.put(_Cookie, getParams(_Cookie));
			result.put(_RoleID, getParams(_RoleID));
			result.put(_LoginData, getParams(_LoginData));
			result.put(_Https, getParams(_Https));
			result.put(_Uuid, getParams(_Uuid));
		    result.put(_LogData, getParams(_LogData));
			result.put(_LogLimit, getIntParams(_LogLimit));
			result.put(_CallName, getParams(_CallName));
			result.put(_Package, getParams(_Package));
			result.put(_AppName, getParams(_AppName));
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

			if (config.has(_Uuid))
				setParams(_Uuid, config.getString(_Uuid));
				
			if (config.has(_LogLimit))
				setIntParams(_LogLimit, config.getInt(_LogLimit));
				
			if (config.has(_LogData))
				setParams(_LogData, _MissingParam);
				
			if (config.has(_CallName))
				setParams(_CallName, config.getString(_CallName));
				
			if (config.has(_Package))
				setParams(_Package, config.getString(_Package));
				
			if (config.has(_AppName))
				setParams(_AppName, config.getString(_AppName));

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


}


