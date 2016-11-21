package com.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class HelloAppCanNativeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = new LinearLayout(this);
		layout.setBackgroundColor(0xfaff0000);
		layout.setOrientation(LinearLayout.VERTICAL);
		TextView text = new TextView(this);
		text.setTextSize(15);
		text.setTextColor(0xffffffff);
		text.setText("Hello AppCan Native Plugin");
		LinearLayout.LayoutParams parmt = new LayoutParams(-1, -2);
		text.setLayoutParams(parmt);
		layout.addView(text);

		Button btn = new Button(this);
		btn.setTextSize(15);
		btn.setTextColor(0xffffffff);
		btn.setText("Back");
		LinearLayout.LayoutParams parmb = new LayoutParams(-1, -2);
		parmb.topMargin = 30;
		btn.setLayoutParams(parmb);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent in = new Intent(getIntent().getAction());
				in.putExtra("result",
						"this is result from my native plugin activity");
				setResult(Activity.RESULT_OK, in);
				finish();
			}
		});
		layout.addView(btn);
		setContentView(layout);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent in = new Intent(getIntent().getAction());
			setResult(Activity.RESULT_CANCELED, in);
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

}
