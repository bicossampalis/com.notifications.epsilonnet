package com.red_folder.phonegap.plugin.backgroundservice.sample;

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

	private boolean Login() {
		
		String url = GetUrl("secureloginculture2");
		String loginData = getParams(_LoginData);
		if (url.equals(_MissingParam) || loginData.equals(_MissingParam))
			return false;
		
		
		URL obj = null;
		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CreateNotification(98, e.getMessage());
			return false;
		}
		
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection)obj.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(97, e.getMessage());
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
			CreateNotification(96, e.getMessage());
			return false;
		}

		OutputStreamWriter wr;
		try {
			wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(loginData);
			wr.flush();
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(95, e.getMessage());
			return false;
		}

		int responseCode = 0;
		try {
			responseCode = con.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(94, e.getMessage());
			return false;
		}

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(93, e.getMessage());
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
			CreateNotification(92, e.getMessage());
			return false;
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(91, e.getMessage());
			return false;
		}

		try {
			JSONObject jsonResponse = new JSONObject(response.toString());

			if (jsonResponse.has("Status")){
				
				String status = jsonResponse.getString("Status");
				if (status.equals("ERROR")){
					CreateNotification(89, "Error : " + jsonResponse.getString("Error"));
				} else {
					JSONObject jsonResult = new JSONObject(jsonResponse.getString("Result"));
	                setParams(_Cookie, jsonResult.getString("cookie"));
				}
			}	

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CreateNotification(90, e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private void GetNotification() throws JSONException  {
		
		String url = GetUrl("pdaMGNotifyForPendingApprovals");
		String cookie = getParams(_Cookie);
		if (url.equals(_MissingParam) || cookie.equals(_MissingParam))
			return ;
		
		
		URL obj = null;
		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CreateNotification(98, e.getMessage());
			return ;
		}
		
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection)obj.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(97, e.getMessage());
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
			CreateNotification(96, e.getMessage());
			return ;
		}

		OutputStreamWriter wr;
		try {
		
			JSONObject inputParams = new JSONObject();
			inputParams.put("cookie", getParams(_Cookie));
			inputParams.put("uuid", getParams(_Uuid));
			inputParams.put("roleid", getParams(_RoleID));
		
			wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(inputParams.toString());
			wr.flush();
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(95, e.getMessage());
			return ;
		}

		int responseCode = 0;
		try {
			responseCode = con.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(94, e.getMessage());
			return ;
		}

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(93, e.getMessage());
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
			CreateNotification(92, e.getMessage());
			return ;
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			CreateNotification(91, e.getMessage());
			return ;
		}

		try {
			JSONObject jsonResponse = new JSONObject(response.toString());

			if (jsonResponse.has("Status")){
				
				String status = jsonResponse.getString("Status");
				if (status.equals("ERROR")){
					CreateNotification(89, "Error : " + jsonResponse.getString("Error"));
				} else {
					JSONObject jsonResult = new JSONObject(jsonResponse.getString("Result"));
	                int RetrievedData = jsonResult.getInt("RetrievedData");
					String RetrievedDataStr = jsonResult.getString("RetrievedData");
					if(RetrievedData > 0)
					 CreateNotification(200, RetrievedDataStr);
				}
			}	

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CreateNotification(90, e.getMessage());
			return ;
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
		sbMsg.append(contentMsg);
		 
		NotificationCompat.Builder mBuilder =
			new NotificationCompat.Builder(this)
				.setSound(uri)
				.setSmallIcon(R.mipmap.sym_def_app_icon)
				.setContentTitle("Pylon Management")
				.setContentText(sbMsg.toString())
				.setContentIntent(resultPendingIntent);

		NotificationManager mNotifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mNotifyMgr.notify(notificationId, mBuilder.build());
	}
	
	
	private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
	@Override
	protected JSONObject doWork() {
		
		JSONObject result = new JSONObject();

		try {

			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			String now = df.format(new Date(System.currentTimeMillis())); 
			String msg = "Pylon Management " + now;

			result.put("Message", msg);
			if(isNetworkAvailable()){
				if (Login()) {
					GetNotification();
				}
			}
		} catch (JSONException e) {
			CreateNotification(99, e.getMessage());
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


