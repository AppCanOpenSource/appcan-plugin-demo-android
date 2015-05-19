package com.test;

import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExCallback;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class EUExTestObject extends EUExBase {

	static final String func_activity_callback = "uexDemo.cbStartActivityForResult";
	static final String func_dialog_callback = "uexDemo.cbTest_showInputDialog";
	static final String func_on_callback = "javascript:uexDemo.onCallBack";

	static final int mMyActivityRequestCode = 10000;

	private Vibrator m_v;
	private View m_myView;

	public EUExTestObject(Context context, EBrowserView view) {
		super(context, view);
	}

	@SuppressWarnings("deprecation")
	public void testOpenActivity(String[] parm) {
		// Demo方法，用于打开一个由一个Activity单独控制的窗口。
		if (parm.length < 2) {
			return;
		}
		final int width = Integer.parseInt(parm[0]);
		final int height = Integer.parseInt(parm[1]);
		final int x = Integer.parseInt(parm[2]);
		final int y = Integer.parseInt(parm[3]);
		((ActivityGroup) mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(mContext,
						HelloAppCanNativeActivity.class);
				LocalActivityManager mgr = ((ActivityGroup) mContext)
						.getLocalActivityManager();
				Window window = mgr.startActivity("test", intent);
				View mMapDecorView = window.getDecorView();
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						width, height);
				lp.leftMargin = x;
				lp.topMargin = y;
				addViewToCurrentWindow(mMapDecorView, lp);
			}
		});
	}

	// this case start a Activity: HelloAppCanNative
	public void test_startActivityForResult(String[] parm) {
		Intent intent = new Intent();
		intent.setClass(mContext, HelloAppCanNativeActivity.class);
		try {
			startActivityForResult(intent, mMyActivityRequestCode);
		} catch (Exception e) {
			Toast.makeText(mContext, "找不到此Activity!!", Toast.LENGTH_LONG)
					.show();
			;
		}
	}

	// this case to use Vibrator
	public void test_vibrator(String[] parm) {
		if (parm.length < 1) {
			return;
		}
		String inMilliseconds = parm[0];
		try {
			if (null == m_v) {
				m_v = (Vibrator) mContext
						.getSystemService(Service.VIBRATOR_SERVICE);
			}
			m_v.vibrate(Integer.parseInt(inMilliseconds));
		} catch (SecurityException e) {
			Toast.makeText(mContext, "未配置震动权限或参数错误!!", Toast.LENGTH_LONG)
					.show();
			;
			return;
		}
		String jsCallBack = func_on_callback + "('" + "成功震动了" + inMilliseconds
				+ "毫秒" + "');";
		onCallback(jsCallBack);
	}

	// this case show a input dialog
	public void test_showInputDialog(String[] parm) {
		if (parm.length < 1) {
			return;
		}
		String defaultValue = parm[0];
		new MyClass(mContext, this).show(defaultValue);
	}

	// this case show a custom view into window
	public void test_addMyViewToWindow(String[] parm) {
		if (parm.length < 4) {
			return;
		}
		String inX = parm[0];
		String inY = parm[1];
		String inW = parm[2];
		String inH = parm[3];

		int x = 0;
		int y = 0;
		int w = 0;
		int h = 0;
		try {
			x = Integer.parseInt(inX);
			y = Integer.parseInt(inY);
			w = Integer.parseInt(inW);
			h = Integer.parseInt(inH);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		if (null == m_myView) {
			// Dynamic get resources ID, does not allow use R
			int myViewID = EUExUtil.getResLayoutID("plugin_uex_demo_test_view");
			if (myViewID <= 0) {
				Toast.makeText(mContext, "找不到名为:my_uex_test_view的layout文件!",
						Toast.LENGTH_LONG).show();
				return;
			}
			m_myView = View.inflate(mContext, myViewID, null);

			// Dynamic get resources ID, does not allow use R
			int imageViewID = EUExUtil.getResIdID("my_icon");
			int drawableID = EUExUtil.getResDrawableID("plugin_uex_demo_image");
			if (imageViewID > 0 && drawableID > 0) {
				ImageView image = (ImageView) m_myView
						.findViewById(imageViewID);
				image.setImageResource(drawableID);
			}
			RelativeLayout.LayoutParams lparm = new RelativeLayout.LayoutParams(
					w, h);
			lparm.leftMargin = x;
			lparm.topMargin = y;
			addViewToCurrentWindow(m_myView, lparm);
		} else {
			RelativeLayout.LayoutParams lparm = new RelativeLayout.LayoutParams(
					w, h);
			lparm.leftMargin = x;
			lparm.topMargin = y;
			removeViewFromCurrentWindow(m_myView);
			addViewToCurrentWindow(m_myView, lparm);
		}

	}

	// this case remove a custom view from window
	public void test_removeMyViewFromWindow(String[] parm) {
		if (null != m_myView) {
			removeViewFromCurrentWindow(m_myView);
		}
	}

	// input dialog result
	protected void myClassCallBack(String result) {
		jsCallback(func_dialog_callback, 0, EUExCallback.F_C_TEXT, result);

	}

	// clean something
	@Override
	protected boolean clean() {
		if (null != m_v) {
			try {
				m_v.cancel();
			} catch (SecurityException e) {
				;
			}
		}
		m_v = null;
		if (null != m_myView) {
			removeViewFromCurrentWindow(m_myView);
			m_myView = null;
		}
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == mMyActivityRequestCode) {
			if (resultCode == Activity.RESULT_OK) {
				String ret = data.getStringExtra("result");
				jsCallback(func_activity_callback, 0, EUExCallback.F_C_TEXT,
						ret);
			} else {
				jsCallback(func_activity_callback, 0, EUExCallback.F_C_TEXT,
						"cancel");
			}
		}
	}
}
