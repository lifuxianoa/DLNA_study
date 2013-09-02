package com.example.mediaplayer;

import java.util.ArrayList;
import java.util.HashMap;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.device.DeviceChangeListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mediaplayer.adapter.DeviceListAdapter;

public class DeviceActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private final static String TAG = "MyMediaPlayer";
	private final static int REFRESH_LIST = 1;

	private Button mBtnSearch;
	private Button mBtnReset;
	private Button mBtnExit;
	private ListView mListDev;
	private DLNAProxy mDLNAProxy;
	private DeviceListAdapter mDeviceListAdapter;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBtnSearch = (Button) findViewById(R.id.btn_search);
		mBtnReset = (Button) findViewById(R.id.btn_reset);
		mBtnExit = (Button) findViewById(R.id.btn_exit);
		mListDev = (ListView) findViewById(R.id.list_device);

		mBtnSearch.setOnClickListener(this);
		mBtnReset.setOnClickListener(this);
		mBtnExit.setOnClickListener(this);
		mListDev.setOnItemClickListener(this);

		mDLNAProxy = DLNAProxy.getInstance();
		mDLNAProxy.addDeviceChangeListener(mDeviceChangeListener);

		mDeviceListAdapter = new DeviceListAdapter(this, new DeviceList());
		mListDev.setAdapter(mDeviceListAdapter);

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case REFRESH_LIST:
					DeviceList devices = mDLNAProxy.getServerDeviceList();
					mDeviceListAdapter.refreshData(devices);
					break;

				default:
					break;
				}
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			new Thread(new Runnable() {

				@Override
				public void run() {
					mDLNAProxy.startSearchDevice();
				}
			}).start();
			break;

		case R.id.btn_reset:
			mDLNAProxy.stopSearchDevice();

			Message msg = new Message();
			msg.what = REFRESH_LIST;
			mHandler.sendMessage(msg);
			break;

		case R.id.btn_exit:
			mDLNAProxy.stopSearchDevice();
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Device device = (Device) parent.getItemAtPosition(position);
		Log.d(TAG, "Devname:" + device.getFriendlyName());
		mDLNAProxy.setSelectedDevice(device);
		Intent intent = new Intent(this, LibraryActivity.class);
		startActivity(intent);
	}

	private void refreshDeviceList() {
		Message msg = new Message();
		msg.what = REFRESH_LIST;
		mHandler.sendMessage(msg);
	}

	private DeviceChangeListener mDeviceChangeListener = new DeviceChangeListener() {

		@Override
		public void deviceAdded(Device dev) {
			if (null != dev) {
				Log.d(TAG, "deviceAdded: " + dev.getFriendlyName());
				refreshDeviceList();
			}
		}

		@Override
		public void deviceRemoved(Device dev) {
			if (null != dev) {
				Log.d(TAG, "deviceRemoved: " + dev.getFriendlyName());
				refreshDeviceList();
			}
		}
	};

}
