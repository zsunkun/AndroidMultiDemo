package com.example.androidtest;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
/**
 * notifyDataSetChanged后立即clear数据，listview也将被清空
 * @author sunkun
 *
 */
public class ListActivityTest extends Activity implements OnClickListener {

	private ArrayList<String> mListItems;
	private ArrayAdapter<String> mListViewAdapter;
	private ListView mListView;
	private Button mButton;
	private Thread mThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_activity_test);
		setListItems();
		mListView = (ListView) findViewById(R.id.list_view);
		mButton = (Button) findViewById(R.id.button);
		mListViewAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mListItems);
		mListView.setAdapter(mListViewAdapter);
		mButton.setOnClickListener(this);
		mThread = new MyThread();
	}

	private void setListItems() {
		mListItems = new ArrayList<String>();
		for (int i = 0; i < 30; i++) {
			mListItems.add(String.valueOf(i));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button:
			mListViewAdapter.notifyDataSetChanged();
			// mListItems.clear();//Item clear的话，滑动时会报错
			mThread.start();
			System.out.println("OnClick");
			break;
		}

	}

	class MyThread extends Thread {
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			mListItems.clear();// Item clear的话，滑动时会报错
			System.out.println("Run");
		}
	}
}
