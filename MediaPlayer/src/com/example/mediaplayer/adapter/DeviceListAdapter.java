package com.example.mediaplayer.adapter;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mediaplayer.R;

public class DeviceListAdapter extends BaseAdapter {
	
	private Context mContext;
	private DeviceList mDevices;
	private LayoutInflater mInflater;
	
	public DeviceListAdapter(Context context, DeviceList devices) {
		mInflater = LayoutInflater.from(context);
		mDevices = devices;
		mContext = context;
	}

	public void refreshData(DeviceList devices) {
		mDevices = devices;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mDevices.size();
	}

	@Override
	public Object getItem(int position) {
		return mDevices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.device_list, null);
		}
		
		Device device = mDevices.getDevice(position);
		TextView textView = (TextView)convertView.findViewById(R.id.name);
		textView.setText(device.getFriendlyName());
		textView = (TextView)convertView.findViewById(R.id.location);
		textView.setText(device.getLocation());
		textView = (TextView)convertView.findViewById(R.id.devicetype);
		textView.setText(device.getDeviceType());
		
		return convertView;
	}

}
