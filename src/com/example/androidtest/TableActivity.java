package com.example.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_layout_test);
		TableLayout layout = (TableLayout) findViewById(R.id.table_layout);
		TableRow tableRow = (TableRow) findViewById(R.id.table_row);
		TextView textView = new TextView(this);
		textView.setText("123456");
		textView.setWidth(300);
		tableRow.addView(textView);

		for (int i = 0; i < 200; i++) {
			TableRow tableRow2 = new TableRow(this);
			for (int j = 0; j < 50; j++) {
				TextView textView2 = new TextView(this);
				textView2.setText("1234567897");
				textView2.setWidth(300);
				tableRow2.addView(textView2);
			}
			layout.addView(tableRow2);
		}
	}
}
