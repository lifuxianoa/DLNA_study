package com.example.mediaplayer;

import java.util.ArrayList;
import java.util.HashMap;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.std.av.controller.MediaController;

import android.app.Activity;
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

public class MainActivity extends Activity implements OnClickListener,
	OnItemClickListener, DeviceChangeListener{

	private final static String TAG = "MyMediaPlayer";
	private final static int REFRESH_LIST = 1;
	
	private Button mBtnSearch;
	private Button mBtnReset;
	private Button mBtnExit;
	private ListView mListDev;
	private MediaController mMediaController;
	private ArrayList<HashMap<String, Object>> mListItem;
	private SimpleAdapter mListItemAdapter;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBtnSearch = (Button)findViewById(R.id.btn_search);
		mBtnReset = (Button)findViewById(R.id.btn_reset);
		mBtnExit = (Button)findViewById(R.id.btn_exit);
		mListDev = (ListView)findViewById(R.id.list_device);
		
		mBtnSearch.setOnClickListener(this);
		mBtnReset.setOnClickListener(this);
		mBtnExit.setOnClickListener(this);
		mListDev.setOnItemClickListener(this);
		
		mMediaController = new MediaController();
		mMediaController.addDeviceChangeListener(this);
		
		mListItem = new ArrayList<HashMap<String,Object>>();
		mListItemAdapter = new SimpleAdapter(this, mListItem, 
				R.layout.device_list, 
				new String[] {"name", "location", "devicetype"},
				new int[] {R.id.name, R.id.location, R.id.devicetype});
		mListDev.setAdapter(mListItemAdapter);
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case REFRESH_LIST:
					mListItemAdapter.notifyDataSetChanged();
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
					mMediaController.start();
				}
			}).start();
			break;
			
		case R.id.btn_reset:
			mMediaController.stop();

			mListItem.clear();
			Message msg = new Message();
			msg.what = REFRESH_LIST;
			mHandler.sendMessage(msg);
			break;
			
		case R.id.btn_exit:
			mMediaController.stop();
			finish();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
	}

	private void refreshDeviceList()
	{
		DeviceList devList = mMediaController.getServerDeviceList();
		mListItem.clear();
		for (int i = 0; i < devList.size(); i++) {
			Device device = devList.getDevice(i);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", device.getFriendlyName());
			map.put("location", device.getLocation());
			map.put("devicetype", device.getDeviceType());
			mListItem.add(map);
		}
		
		Message msg = new Message();
		msg.what = REFRESH_LIST;
		mHandler.sendMessage(msg);
	}
	
	@Override
	public void deviceAdded(Device dev) {
		if (null != dev) {
			Log.d(TAG, "deviceAdded: " + dev.getFriendlyName());
			mMediaController.printContentDirectory(dev);
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

}
