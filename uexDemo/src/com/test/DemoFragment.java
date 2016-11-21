package com.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.zywx.wbpalmstar.base.view.BaseFragment;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

public class DemoFragment extends BaseFragment{

    private String mText;
    private String mButtonText;
    private EUExDemo.OnButtonClick mListener;

    public void setFragmentText(String mText) {
        this.mText = mText;
    }

    public void setFragmentButtonText(String mButtonText) {
        this.mButtonText = mButtonText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(this.getActivity())
                .inflate(EUExUtil.getResLayoutID("plugin_uex_demo_test_view"),
                        container, false);
        TextView textView = (TextView) view.findViewById(
                EUExUtil.getResIdID("plugin_uexdemo_textview_id"));
        textView.setText(mText);
        Button button = (Button) view.findViewById(
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
        return view;
    }

    public void setOnButtonClick(EUExDemo.OnButtonClick onFragmentButtonClick) {
        this.mListener = onFragmentButtonClick;
    }
}
