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

import android.graphics.drawable;


public class MyService extends BackgroundService {
	
	private final static String TAG = MyService.class.getSimpleName();	
	private static String mParams = "Default";

	public String getParams() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);  
		// Should default to a minute
		return sharedPrefs.getString(this.getClass().getName() + ".Params", mParams);	
	}

	public void setParams(String params) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);  

		SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(this.getClass().getName() + ".Params", params);
        editor.commit();
	}
	
	public static String _Logindata = "{\"applicationname\":\"Hercules.Suite\",\"databasealias\":\"EFcv13nGGRUDyOkpqlIYJsh1yoNCslFZZ1nfZDFGCLPDCVyJo3mr37wvbqoZX+2UuLA/bVz/7UJEwofSaJedP/tbg/Bx77gLjI/5O6bFYRpWoKjtrKIMvOazZ0BuPUzChYbQLbQtQ9nbDXF6cTa8MLFRBbXdNcvoDlxYessVPl4gwYHxLvPzzIFwjrqEctOheKtjTKtVLByo5K743Aj+oB9rywEPaaeQw3cVBaBKRO6Sz5bePYkNJRzQpJjdbXnN9vVMAKxNKm9/fACqOB4e6tNMW3iDmQsJeFUR/68j4Ae+29rB8aqP9UPSRd8Zy/CCfBf+uyMg4Ccb15jtGG6r/qILxCGHOvp+f2e8LWKPJXatrsQ06V+U/30NFIHjuBOM8HRwgprHSw+PVIeaWlHbwwsyiPwupTi0wYmkxyQFWvKbkrQkYWC7HwDecGT04qUSKLen6q88eELO/RgzIFTSzdZFdnfASwSqHqkPnOPKbfoqnnAUuSsAPk976ow7FFMVFbRftUBIwbCQnPUuUkDRCdIr8fQYjnPMICgZ4HC+lFcF6nO9cC5oMGR466jxFEj8kcsa3ikb8RQv5+JG0iD1eSyGfhZGqB+hTlakfsf25FSrZF5aWNgYzDwOJkLHi9OV8CDxxxkATH3mU5L465mErtQeTvYxEE+szKP25Ut8L3s=\",\"username\":\"aUpUzsNW1xvO0mNmhm82tBJioGm9vgwpzQD1HGw/MA+9siFQLnvyEIDii2ASFpEkQNvRkGOMfzIKCIgSm3tEOErQcg1vDB8kcCKHnwSZhIWmNJg4oD+2Xvl9af3Sj/hPJZRb+eIbLuNNlaeLl8zeYP9KktoHpCFE2KQ5ui0Bfb4NbLTbD8vK3CYJUVpr17gDUD2NZYjawWJ0SWC+5sbutx7ac21wKXaYhD0nr2aHB5if6LtZPhcTV7BuCa6Xn0DtVe3LabCR//yDmAXjQVjnzKQ5lPo/csXcUlwHfx16n/ksaPXV00xy0FapCVUYsmJljrxPp/EV4D9inCCWQ8QIunY5G/6RBX6AIOzNsPjZ/0ERT6qNAi2mbiICkpA6zcjpSIB2q/Xu5wSADg5TBYsH+25eUZAzTuq9NjEsPE/CLFh3beRk34ZfoU5B2yDlWcSnO1gPvU9qMX2hpZjicVbiRF8FFO/4h6cbuCRP8tHJvDfIZev5tgYogFpSyQFSdqW8TYCEAbj/w2Ttl1KxcbtW37Nnlz89IarOQJtwCWGSsyhaCDM1Wxb24iWsEfDDjcTdgiPj5DZdY1yEe3N1oBzyVw0otiuwEoHpowaqty80gzcuiSpPBB2fY1yRsoojSkACZ82DoByIfU9fjvL14iHVh2FuEnLIaR6Yne8h8ca9rAs=\",\"password\":\"Z2A7edvE+jd53+e35+3VIrzzxRB1dcjhUCELPBhZ7Mvg/9q6WG7vod7VFXYSwhBZ1dChWhTxKILlTLcGkGlnotpKtdLhhQ4bhFBZa3jwYcqCdG9nh52zlXzbRydrjAprzV3CXun1bqSRVthgahA0fTvljkfDNB7HRE3cAWgQFw0Gib26kB3S2CYm/AskqNWxS/2u8o86CYGNxPKaFUUogzZXG/R6K/VF7j/DnUBowBWQf27w9l7WS9pQFcSWx1kFLjvVA7LD6bD/twNsJRPtv+jtBB41xszH4WzFh2WtOka1bmYsVNk7BLYpkmQbcL9eGUi05jtd5KQIn4H/PCfG1xSwAGIy6DA7Qw4UDgSjYciXnMWZH1cJn4130u1HkDAOIi/nU0fMydYsFYXZxf9uKmywNMf6qR3V6lR5o/+3OKS1lypYwYJCFoBzXUHWaK1vx7leuRjOWWJ2wtA/nRSxX9Gj1LFUrMhnjmcdQX99R2pRB6+7d7qhhM+p+FLzwoHneRRsy3l8CAycGPOwCwVC5FJNZrLmPtMLTCpokAx4A/VHrF22NBIgsVQaInoldDwoTC0rr1yM8Pm4yVDIEizCjQ9UVXJ2Fm8ghU8mspAPpl5kZUoSACDNEmsyA4PaYSkmFJWdOay2oe0gjuEylxMdcvKydFUTpx4+F84eTvCv7NM=\",\"extras\":\"dYmqMrQEEI+vR80s3iK+/FLjQm/k37/PnUTGgPLSs5UdWTAFlbLEej7C1eKY+25AGCKA+enaIOn4XdhoEtAaFZ9Rbb+ziLDnp8HczdEVdcEJEbA6j4UX6mMFHAj4vz+Ek4oE254uN4zvPEPEIYqB3HW59SNzT0tCNJ7i59Whu86AFjyJW+32T3aHmU7kkskreaMe6gCff7S1MNfsRnQGK/NWX0K15vbJYcbWiS8IQdKaM9RM1/1TBMUUHOiYwzNXTFxmmUmB6d0Ohzh7pfBYR9zG013Bitq1W6NkD6EoAqs21IWkymPDkmIPvx137BPTKnSz+wOvCvrpq1fC1ULTFNTUaAAAwngbMtmq4/siDitbqODK/w85OBKsYh32+VoDd8dMTfSGXtTqmTLUAGFUmEDaMndyo9XRfH2RmVXNjy0/103jWdIkoX+XYrYEhLZr8ZEA1GVJwIBGZxqbdkFGIQjdTAznA+ETth68rvu2mv0QpH4FGmWMy+xOz3o3dJn25Vuou5KiYpv8xziFYPGKdwEmP2tmM6goYz5iYLk0J2baeaKFutxv9eXFZYpFPmT9bUC4+SYblAAYuArZT0eSNjAciG55+5ttgoIIGHIQaBMGhm1B1QhHls76qI9gy5Lnb8o5v1FrYbK0DBwNevQ4lBSsk9SOfMhWnCjrEMLE4EA=\",\"threadculture\":\"en-US\",\"uiculture\":\"el-GR\",\"roleId\":\"68338af3-3079-e411-82e5-902b349c8e67\"}";
 
 
	private void LoginTest() {
		
		//String url = "http://10.1.1.67:7002/hercjson/check";
		String url = "http://10.1.1.225:7002/hercjson/secureloginculture2";
		URL obj = null;
		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection)obj.openConnection();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}

		// optional default is GET
		try {
	 
			 con.setDoOutput(true);
			 con.setDoInput(true);
			 con.setRequestProperty("Content-Type", "application/json");
			 con.setRequestProperty("Accept", "application/json");
			 con.setRequestMethod("POST");
			//con.setRequestMethod("GET");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		OutputStreamWriter wr;
		try {
			wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(_Logindata);
			wr.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int responseCode = 0;
		try {
			responseCode = con.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("\nSending 'POST' request to URL : " + url);
		// System.out.println("Response Code : " + responseCode);

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String inputLine;
		StringBuffer response = new StringBuffer();

		try {
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	protected JSONObject doWork() {
		
		JSONObject result = new JSONObject();
		
		try {
		
			if (!getParams().equals(mParams)) {
				setParams(mParams);
			}

			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			String now = df.format(new Date(System.currentTimeMillis())); 
			String msg = "Hello " + getParams() + " - its currently " + now;

			result.put("Message", getParams());
			
			
			LoginTest();
			
			
			
			
			Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
			Intent resultIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.epsilonnet.pylonmanagement");//new Intent(this, Result.class);
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

			PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		
		Drawable icon = getApplicationContext().getPackageManager().getApplicationIcon("com.epsilonnet.pylonmanagement");
		
					
			NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
				.setSound(uri)
				.setSmallIcon(icon)
				.setContentTitle("My notification")
				.setContentText(msg)
				.setContentIntent(resultPendingIntent);

	
			int mNotificationId = 001;
			// Gets an instance of the NotificationManager service
			NotificationManager mNotifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			// Builds the notification and issues it.
			mNotifyMgr.notify(mNotificationId, mBuilder.build());

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


