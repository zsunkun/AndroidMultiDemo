package com.example.androidtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.internal.telephony.ISms;

import aidl.calculate.ICalculateService;
import android.R.anim;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class MainActivity extends Activity implements OnDateSetListener {

	private static boolean isFirst = true;
	String mUrl = "http://martin-frontend.b0.upaiyun.com//hulk/apps/0.6.2/062.apk";

	private ICalculateService mService;
	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mService = ICalculateService.Stub.asInterface(service);
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button = (Button) findViewById(R.id.button);
		button.setText("确定");
		TextView textView = (TextView) findViewById(R.id.textView1);
		String str = null;
		// textView.setText(str);
		System.out.println("---" + textView.getText().length());
		// printDeviceInf();
		Intent intent = new Intent("com.service.RemoteServiceTest");
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mediaPlayer();
				sendMessage();
				aidlService();
				adjustVolume();
				showDatePicker();
				openGPS();
				printDeviceInf();
				powerOff();
				Test();
				share();
				createNotification();
				surfaceViewTest();
				loadByUrlConnection();
				loadByLoadResThread();
			}
		});
	}

	private void loadByUrlConnection() {
		new Thread() {
			@Override
			public void run() {
				load();
			}
		}.start();
	}

	private void loadByLoadResThread() {
		new Thread(
				new LoadResThread(
						MainActivity.this,
						"http://martin-frontend.b0.upaiyun.com/hulk/apps/0.6.2/062.apk",
						new File(
								Environment.getExternalStorageDirectory()
										.getAbsolutePath()
										+ "/download/"
										+ getFileName("http://martin-frontend.b0.upaiyun.com/hulk/apps/0.6.2/062.apk"))))
				.start();
	}

	private void mediaPlayer() {
		startActivity(new Intent(MainActivity.this, VideoPlayerDemo.class));
	}

	private void sendMessage() {
		boolean result = SmsUtils.sendMessage("10010", "CXLL");
	}

	private void aidlService() {
		try {
			System.out.println("calculate:" + mService.calcul(10, 20));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void surfaceViewTest() {
		startActivity(new Intent(MainActivity.this, SurfaceViewTest.class));
	}

	@SuppressLint("NewApi")
	private void showDatePicker() {
		DatePickerDialog dlg = new DatePickerDialog(this, this, 2015, 10, 26);
		if (android.os.Build.VERSION.SDK_INT >= 11)
			dlg.getDatePicker().setCalendarViewShown(false);
		dlg.show();
	}

	private void adjustVolume() {
		AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
				AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
	}

	private void createNotification() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("My notification")
				.setContentText("Hello World!");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MainActivityB.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		// stackBuilder.addParentStack(MainActivityB.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(111, mBuilder.build());
	}

	private void powerOff() {
		System.out.println("-----powerOff");
		Intent intent = new Intent(Intent.ACTION_SHUTDOWN);
		// 其中false换成true,会弹出是否关机的确认窗口
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		try {

			// 获得ServiceManager类
			Class<?> ServiceManager = Class
					.forName("android.os.ServiceManager");

			// 获得ServiceManager的getService方法
			Method getService = ServiceManager.getMethod("getService",
					java.lang.String.class);

			// 调用getService获取RemoteService
			Object oRemoteService = getService.invoke(null,
					Context.POWER_SERVICE);

			// 获得IPowerManager.Stub类
			Class<?> cStub = Class.forName("android.os.IPowerManager$Stub");
			// 获得asInterface方法
			Method asInterface = cStub.getMethod("asInterface",
					android.os.IBinder.class);
			// 调用asInterface方法获取IPowerManager对象
			Object oIPowerManager = asInterface.invoke(null, oRemoteService);
			// 获得shutdown()方法
			Method shutdown = oIPowerManager.getClass().getMethod("shutdown",
					boolean.class, boolean.class);
			// 调用shutdown()方法
			shutdown.invoke(oIPowerManager, false, true);

		} catch (Exception e) {
			Log.e("shutdown", e.toString(), e);
		}
	}

	private void setScreenBrightness(int paramInt) {
		Window localWindow = this.getWindow();
		WindowManager.LayoutParams localLayoutParams = localWindow
				.getAttributes();
		float f = paramInt / 255.0F;
		localLayoutParams.screenBrightness = f;
		localWindow.setAttributes(localLayoutParams);
	}

	@SuppressLint("NewApi")
	private void Test() {
		Dialog dialog = new Dialog(this);
		dialog.getActionBar();
		dialog.show();
	}

	private void share() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
		intent.putExtra(Intent.EXTRA_TEXT,
				"I have successfully share my message for test");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, getTitle()));
	}

	private void printDeviceInf() {
		StringBuilder sb = new StringBuilder();
		sb.append("PRODUCT ").append(android.os.Build.PRODUCT).append("\n");
		sb.append("BOARD ").append(android.os.Build.BOARD).append("\n");
		sb.append("BOOTLOADER ").append(android.os.Build.BOOTLOADER)
				.append("\n");
		sb.append("BRAND ").append(android.os.Build.BRAND).append("\n");
		sb.append("CPU_ABI ").append(android.os.Build.CPU_ABI).append("\n");
		sb.append("CPU_ABI2 ").append(android.os.Build.CPU_ABI2).append("\n");
		sb.append("DEVICE ").append(android.os.Build.DEVICE).append("\n");
		sb.append("DISPLAY ").append(android.os.Build.DISPLAY).append("\n");
		sb.append("FINGERPRINT ").append(android.os.Build.FINGERPRINT)
				.append("\n");
		sb.append("HARDWARE ").append(android.os.Build.HARDWARE).append("\n");
		sb.append("HOST ").append(android.os.Build.HOST).append("\n");
		sb.append("ID ").append(android.os.Build.ID).append("\n");
		sb.append("MANUFACTURER ").append(android.os.Build.MANUFACTURER)
				.append("\n");
		sb.append("MODEL ").append(android.os.Build.MODEL).append("\n");
		sb.append("PRODUCT ").append(android.os.Build.PRODUCT).append("\n");
		sb.append("RADIO ").append(android.os.Build.RADIO).append("\n");
		sb.append("SERIAL ").append(android.os.Build.SERIAL).append("\n");
		sb.append("TAGS ").append(android.os.Build.TAGS).append("\n");
		sb.append("TIME ").append(android.os.Build.TIME).append("\n");
		sb.append("TYPE ").append(android.os.Build.TYPE).append("\n");
		sb.append("USER ").append(android.os.Build.USER).append("\n");
		Log.i("sunkun", sb.toString());
	}

	private String load() {
		String TAG = "sunkun";
		Log.v(TAG, "start load resource: " + mUrl);
		File file = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/download/" + getFileName(mUrl));
		try {
			File dir = file.getParentFile();
			if (!dir.exists())
				dir.mkdirs();
			if (!file.exists())
				file.createNewFile();
		} catch (Throwable e) {
		}

		HttpURLConnection conn = null;
		InputStream is = null;
		FileOutputStream fout = null;
		String errorMessage = null;

		try {
			URL url = new URL(mUrl);
			Log.v(TAG, "url1111=============" + url);
			conn = NetUtil.getHttpUrlConnection(this, url);
			conn.setFollowRedirects(true);
			conn.setReadTimeout(5 * 60 * 1000); // 5min
			conn.connect();
			is = conn.getInputStream();
			// URL url = new URL(mUrl);
			// URL urlRedirect = url;
			// do {
			// url = urlRedirect;
			// conn = NetUtil.getHttpUrlConnection(this, url);
			// conn.setReadTimeout(5 * 60 * 1000);
			// conn.connect();
			// is = conn.getInputStream();
			// urlRedirect = conn.getURL();
			// } while (!url.toString().equals(urlRedirect.toString()));
			fout = new FileOutputStream(file);

			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = is.read(buf)) > 0) {
				Log.v(TAG, "len=============" + len);
				fout.write(buf, 0, len);
			}
			fout.flush();
			conn.disconnect();
		} catch (SocketTimeoutException e) {
			Log.e(TAG, "socket time out. " + mUrl);
			errorMessage = e.getMessage();
		} catch (IOException e) {
			Log.e(TAG, "Loading res failed: " + mUrl, e);
			errorMessage = e.getMessage();
		} catch (Throwable e) {
			Log.e(TAG, "Loading res failed(throwable): " + mUrl, e);
			errorMessage = e.getMessage();
		} finally {
			if (conn != null)
				try {
					conn.disconnect();
				} catch (Throwable e) {
				}
			Log.v(TAG, "resource: " + mUrl + " load "
					+ (errorMessage == null ? "success." : "failed."));
		}
		return errorMessage;
	}

	private static String getFileName(String url) {
		try {
			URI uri = URI.create(url);
			String name = uri.getHost();
			String path = uri.getPath();
			String query = uri.getQuery();
			if (path != null)
				name += "/" + path;
			if (query != null)
				name += "/" + query;
			int idx = name.lastIndexOf('/');
			if (idx >= 0 && idx < name.length() - 1)
				name = name.substring(idx + 1);
			Matcher m = Pattern.compile("[^0-9a-zA-Z._]+").matcher(name);
			return m.replaceAll("_");
		} catch (Throwable e) {
		}
		return "temp" + System.currentTimeMillis();
	}

	private void openGPS() {
		System.out.println("------openGPS");
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		this.sendBroadcast(intent);

		String provider = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (!provider.contains("gps")) { // if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			this.sendBroadcast(poke);
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
	}

}
