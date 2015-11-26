package com.example.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;

public class FlingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		System.out.println("--OntouchEvent");
		return mGestureDetector.onTouchEvent(event);
	}

	@SuppressWarnings("deprecation")
	private GestureDetector mGestureDetector = new GestureDetector(
			new OnGestureListener() {

				@Override
				public boolean onDown(MotionEvent e) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void onShowPress(MotionEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public boolean onSingleTapUp(MotionEvent e) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void onLongPress(MotionEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {
					System.out.println("exe--" + (e1.getX() - e2.getX()));
					return false;
				}

				@Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2,
						float distanceX, float distanceY) {
					// TODO Auto-generated method stub
					return false;
				}
			});
}
