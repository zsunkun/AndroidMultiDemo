package com.example.androidtest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceViewTest extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MyView(this));
	}

	// ��ͼ�ڲ���
	class MyView extends SurfaceView implements SurfaceHolder.Callback {
		private SurfaceHolder holder;
		private MyThread myThread;

		public MyView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			holder = this.getHolder();
			 holder.addCallback(this);
			myThread = new MyThread(holder);// ����һ����ͼ�߳�
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			myThread.isRun = true;
			myThread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			myThread.isRun = false;
		}
	}

	// �߳��ڲ���
	class MyThread extends Thread {
		private SurfaceHolder holder;
		public boolean isRun;

		public MyThread(SurfaceHolder holder) {
			this.holder = holder;
			isRun = true;
		}

		@Override
		public void run() {

			int count = 0;
			while (isRun) {
				Canvas c = null;
				try {
					synchronized (holder) {
						c = holder.lockCanvas();// ����������һ����������Ϳ���ͨ���䷵�صĻ�������Canvas���������滭ͼ�Ȳ����ˡ�
						c.drawColor(Color.BLACK);// ���û���������ɫ
						Paint p = new Paint(); // ��������
						p.setColor(Color.WHITE);
						Rect r = new Rect(100, 50, 300, 250);
						c.drawRect(r, p);
						c.drawText("���ǵ�" + (count++) + "��", 100, 310, p);
						Thread.sleep(1000);// ˯��ʱ��Ϊ1��
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (c != null) {
						holder.unlockCanvasAndPost(c);// ����������ͼ�����ύ�ı䡣
					}
				}
			}
		}
	}
}
