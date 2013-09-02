package com.example.mediaplayer;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class LibraryActivity extends Activity {
	
	Button mBtnBack;
	private ListView mListLib;
	private ArrayList<HashMap<String, Object>> mListItem;
	private SimpleAdapter mListItemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_activity);		

		mBtnBack = (Button)findViewById(R.id.btn_back);
		mListLib = (ListView)findViewById(R.id.list_library);
		
		mListItem = new ArrayList<HashMap<String,Object>>();
		mListItemAdapter = new SimpleAdapter(this, mListItem, 
				R.layout.device_list, 
				new String[] {"imageView", "content"},
				new int[] {R.id.imageView, R.id.content});
		mListLib.setAdapter(mListItemAdapter);
		
	}

}
