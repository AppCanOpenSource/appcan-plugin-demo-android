package com.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class MyClass {

	private Context mContext;
	private EUExTestObject mTest;

	public MyClass(Context ctx, EUExTestObject uexTestObject) {
		mContext = ctx;
		mTest = uexTestObject;
	}

	public void show(String defaultVlue) {
		AlertDialog.Builder dia = new AlertDialog.Builder(mContext);
		dia.setTitle(null);
		dia.setMessage("请输入:");
		final EditText input = new EditText(mContext);
		if (defaultVlue != null) {
			input.setText(defaultVlue);
		}
		dia.setView(input);
		dia.setCancelable(false);
		dia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String ret = input.getText().toString();
				mTest.myClassCallBack(ret);
			}
		});
		dia.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mTest.myClassCallBack(null);
			}
		});
		dia.create();
		dia.show();
	}

}
