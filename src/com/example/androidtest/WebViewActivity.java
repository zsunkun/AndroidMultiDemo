package com.example.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {

	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web_view);
		mWebView = (WebView) findViewById(R.id.my_webview);
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		String url = "http://www.baidu.com/";
		client.shouldOverrideUrlLoading(mWebView, url);
		mWebView.setWebViewClient(client);
	}

	private WebViewClient client = new WebViewClient() {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return false;
		}
	};

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	};
}
