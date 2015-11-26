package com.example.androidtest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressLint("NewApi")
public class BluetoothTest extends Activity {
	// ��UUID��ʾ���ڷ���
	static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	Button btnSearch, btnDis, btnExit;
	ToggleButton tbtnSwitch;
	ListView lvBTDevices;
	ArrayAdapter<String> adtDevices;
	List<String> lstDevices = new ArrayList<String>();
	BluetoothAdapter btAdapt;
	public static BluetoothSocket btSocket;
	private BluetoothDevice mDevice;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		// Button ����
		btnSearch = (Button) this.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new ClickEvent());
		btnExit = (Button) this.findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new ClickEvent());
		btnDis = (Button) this.findViewById(R.id.btnDis);
		btnDis.setOnClickListener(new ClickEvent());

		// ToogleButton����
		tbtnSwitch = (ToggleButton) this.findViewById(R.id.tbtnSwitch);
		tbtnSwitch.setOnClickListener(new ClickEvent());

		// ListView��������Դ ������
		lvBTDevices = (ListView) this.findViewById(R.id.lvDevices);
		adtDevices = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lstDevices);
		lvBTDevices.setAdapter(adtDevices);
		lvBTDevices.setOnItemClickListener(new ItemClickEvent());

		btAdapt = BluetoothAdapter.getDefaultAdapter();// ��ʼ��������������
		btAdapt.getProfileProxy(this, bs, BluetoothProfile.A2DP);

		// ========================================================
		// modified by jason0539 ����jason0539�����ҵĲ���
		/*
		 * if (btAdapt.getState() == BluetoothAdapter.STATE_OFF)// ��ȡ����״̬����ʾ
		 * tbtnSwitch.setChecked(false); else if (btAdapt.getState() ==
		 * BluetoothAdapter.STATE_ON) tbtnSwitch.setChecked(true);
		 */
		if (btAdapt.isEnabled()) {
			tbtnSwitch.setChecked(false);
		} else {
			tbtnSwitch.setChecked(true);
		}
		// ============================================================
		// ע��Receiver����ȡ�����豸��صĽ��
		IntentFilter intent = new IntentFilter();
		intent.addAction(BluetoothDevice.ACTION_FOUND);// ��BroadcastReceiver��ȡ���������
		intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(searchDevices, intent);
	}

	private BroadcastReceiver searchDevices = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Bundle b = intent.getExtras();
			Object[] lstName = b.keySet().toArray();

			// ��ʾ�����յ�����Ϣ����ϸ��
			for (int i = 0; i < lstName.length; i++) {
				String keyName = lstName[i].toString();
				Log.e(keyName, String.valueOf(b.get(keyName)));
			}
			BluetoothDevice device = null;
			// �����豸ʱ��ȡ���豸��MAC��ַ
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getBondState() == BluetoothDevice.BOND_NONE) {
					String str = "δ���|" + device.getName() + "|"
							+ device.getAddress();
					if (lstDevices.indexOf(str) == -1)// ��ֹ�ظ����
						lstDevices.add(str); // ��ȡ�豸���ƺ�mac��ַ
					adtDevices.notifyDataSetChanged();
				}
			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				switch (device.getBondState()) {
				case BluetoothDevice.BOND_BONDING:
					Log.d("BlueToothTestActivity", "�������......");
					break;
				case BluetoothDevice.BOND_BONDED:
					Log.d("BlueToothTestActivity", "������");
					final BluetoothDevice de = device;
					 Thread thread = new Thread(new Runnable() {
						
						@Override
						public void run() {
//							connect(de);// �����豸
							try {
								System.out.println("sunkun��ʼ����ssssssss");
								new BluetoothConnector(de,true,btAdapt,null).connect();
								System.out.println("sunkun--okkkkk");
							} catch (IOException e) {
								System.out.println("sunkun++"+e.getMessage());
								e.printStackTrace();
							}
							
						}
					});
					 thread.start();
					break;
				case BluetoothDevice.BOND_NONE:
					Log.d("BlueToothTestActivity", "ȡ�����");
				default:
					break;
				}
			}

		}
	};

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(searchDevices);
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	class ItemClickEvent implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (btAdapt.isDiscovering())
				btAdapt.cancelDiscovery();
			String str = lstDevices.get(arg2);
			String[] values = str.split("\\|");
			String address = values[2];
			Log.e("address", values[2]);
			BluetoothDevice btDev = btAdapt.getRemoteDevice(address);
//			try {
//				Boolean returnValue = false;
//				if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
//					// ���÷��䷽������BluetoothDevice.createBond(BluetoothDevice
//					// remoteDevice);
//					Method createBondMethod = BluetoothDevice.class
//							.getMethod("createBond");
//					Log.d("BlueToothTestActivity", "��ʼ���");
//					returnValue = (Boolean) createBondMethod.invoke(btDev);
//
//				} else if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
//					connect(btDev);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

			mDevice = btDev;
//			btAdapt.getProfileProxy(BluetoothTest.this, bs, BluetoothProfile.A2DP);
			
		}

	}

	private void connect(BluetoothDevice btDev) {
		UUID uuid = UUID.fromString(SPP_UUID);
		System.out.println("sunkun��ʼ����");
		try {
			btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
			
//			Class<?> clazz = btSocket.getRemoteDevice().getClass();
//			Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};
//
//			Method m = clazz.getMethod("createRfcommSocket", paramTypes);
//			Object[] params = new Object[] {Integer.valueOf(1)};

			BluetoothSocket fallbackSocket = (BluetoothSocket) btDev.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(btDev,1);
			fallbackSocket.connect();
			System.out.println("sunkun--lainjie");
//			btSocket.connect();
		} catch (IOException e) {
			System.out.println("sunkun++"+e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class ClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == btnSearch)// ���������豸����BroadcastReceiver��ʾ���
			{
				if (btAdapt.getState() == BluetoothAdapter.STATE_OFF) {// ���������û����
					Toast.makeText(BluetoothTest.this, "���ȴ�����", 1000).show();
					return;
				}
				if (btAdapt.isDiscovering())
					btAdapt.cancelDiscovery();
				lstDevices.clear();
				Object[] lstDevice = btAdapt.getBondedDevices().toArray();
				for (int i = 0; i < lstDevice.length; i++) {
					BluetoothDevice device = (BluetoothDevice) lstDevice[i];
					String str = "�����|" + device.getName() + "|"
							+ device.getAddress();
					lstDevices.add(str); // ��ȡ�豸���ƺ�mac��ַ
					adtDevices.notifyDataSetChanged();
				}
				setTitle("����������ַ��" + btAdapt.getAddress());
				btAdapt.startDiscovery();
			} else if (v == tbtnSwitch) {// ������������/�ر�
				if (tbtnSwitch.isChecked() == false)
					btAdapt.enable();

				else if (tbtnSwitch.isChecked() == true)
					btAdapt.disable();
			} else if (v == btnDis)// �������Ա�����
			{
				Intent discoverableIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(
						BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				startActivity(discoverableIntent);
			} else if (v == btnExit) {
				try {
					if (btSocket != null)
						btSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				BluetoothTest.this.finish();
			}
		}

	}
	
	BluetoothProfile.ServiceListener bs = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Log.i("log", "onServiceConnected");
            BluetoothHeadset bh =null;
            BluetoothA2dp a2dp =null;
            try {
                if (profile == BluetoothProfile.HEADSET) {
                	bh = (BluetoothHeadset) proxy;
                    if (bh.getConnectionState(mDevice) != BluetoothProfile.STATE_CONNECTED){
                        bh.getClass()
                                .getMethod("connect", BluetoothDevice.class)
                                .invoke(bh, mDevice);
                    }
                } else if (profile == BluetoothProfile.A2DP) {
                	a2dp = (BluetoothA2dp) proxy;
 
                    if (a2dp.getConnectionState(mDevice) != BluetoothProfile.STATE_CONNECTED){
                        a2dp.getClass()
                                .getMethod("connect", BluetoothDevice.class)
                                .invoke(a2dp, mDevice);
                    }
                }
//                if (bh != null&&a2dp != null) {
//                    A2dpConnectionThread.stop = false;
//                    new A2dpConnectionThread(context, device, a2dp, bh).start();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
 
        @Override
        public void onServiceDisconnected(int profile) {
        }
    };
}
