package com.example.androidtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * onNewIntent Test 1.LanchMode is singleTask result:you will get the old Intent
 * if you dont't call the {@link #setIntent(Intent)},even if in the
 * {@link #onNewIntent(Intent)}
 * 
 * @author sunkun
 *
 */
public class MainActivityB extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity_b);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivityB.this, MainActivity.class));
			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		System.out.println("---getIntent:"
				+ getIntent().getStringExtra("sunkun"));
	}
}
