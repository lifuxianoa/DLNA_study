package com.example.mediaplayer;

import org.cybergarage.upnp.std.av.controller.MediaController;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	private final static String TAG = "MediaPlayer";
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		MediaController mController = new MediaController();
		mController.start();
		Log.e(TAG, "after start(), sleep...");
		try {
			Thread.sleep(1000 * 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e(TAG, "before print(), wakeup...");
		mController.printMediaServers();
		mController.stop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
