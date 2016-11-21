package com.test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

public class DemoView extends FrameLayout{

    private Context mContext;
    private String mData;
    private String mButtonText;
    private EUExDemo.OnButtonClick mListener;

    public DemoView(Context context, String data, String buttonText, EUExDemo.OnButtonClick listener) {
        super(context);
        this.mContext = context;
        this.mData = data;
        this.mButtonText = buttonText;
        this.mListener =listener;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(EUExUtil
                        .getResLayoutID("plugin_uex_demo_test_view"),
                this, true);
        TextView textView = (TextView) findViewById(
                EUExUtil.getResIdID("plugin_uexdemo_textview_id"));
        textView.setText(mData);
        Button button = (Button) findViewById(
                EUExUtil.getResIdID("plugin_uexdemo_button_id"));
        button.setText(mButtonText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onButtonClick();
                }
            }
        });
    }


}
