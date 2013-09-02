package com.example.mediaplayer;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.std.av.controller.MediaController;

public class DLNAProxy {

	private static DLNAProxy mInstance = null;
	
	private MediaController mMediaController;
	private Device mSelectedDevice;
	
	public Device getSelectedDevice() {
		return mSelectedDevice;
	}

	public void setSelectedDevice(Device device) {
		this.mSelectedDevice = device;
	}

	private DLNAProxy() {
		mMediaController = new MediaController();
	}

	public static DLNAProxy getInstance() {
		if (mInstance == null) {
			mInstance = new DLNAProxy();
		}
		
		return mInstance;
	}
	
	public void addDeviceChangeListener(DeviceChangeListener listener) {
		mMediaController.addDeviceChangeListener(listener);
	}
	
	public DeviceList getServerDeviceList() {
		return mMediaController.getServerDeviceList();
	}
	
	public void startSearchDevice()
	{
		mMediaController.start();
	}
	
	public void stopSearchDevice()
	{
		mMediaController.stop();
	}
	
}
