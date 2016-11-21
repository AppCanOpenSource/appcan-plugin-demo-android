package com.test;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;

public class EUExDemo extends EUExBase {

    int mFuncActivityCallback =-1;

    static final int mMyActivityRequestCode = 10000;
    private static final String TAG = "uexDemo";
    private static final String CALLBACK_ON_VIEW_BUTTON_CLICK = "uexDemo.onViewButtonClick";
    private static final String CALLBACK_ON_FRAGMENT_BUTTON_CLICK = "uexDemo.onFragmentButtonClick";

    private Vibrator m_v;
    private View mAddView;
    private ViewDataVO mAddViewData;

    private DemoFragment mAddFragmentView;
    private ViewDataVO mAddFragmentData;



    public EUExDemo(Context context, EBrowserView view) {
        super(context, view);
    }

    public void test_addView(String[] parm) {
        if (parm.length < 1) {
            return;
        }
        mAddViewData = DataHelper.gson.fromJson(parm[0], ViewDataVO.class);
        if (mAddViewData == null){
            return;
        }
        if (mAddView != null){
            test_removeView(null);
        }
        mAddView = new DemoView(mContext, "我是一个添加的view的text",
                "view button", onViewButtonClick);
        if (mAddViewData.isScrollWithWebView()){
            android.widget.AbsoluteLayout.LayoutParams lp = new
                    android.widget.AbsoluteLayout.LayoutParams(
                    mAddViewData.getWidth(),
                    mAddViewData.getHeight(),
                    mAddViewData.getLeft(),
                    mAddViewData.getTop());
            addViewToWebView(mAddView, lp, TAG);
        }else{
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    mAddViewData.getWidth(), mAddViewData.getHeight());
            lp.leftMargin = mAddViewData.getLeft();
            lp.topMargin = mAddViewData.getTop();
            addViewToCurrentWindow(mAddView, lp);
        }
    }

    public void test_removeView(String[] params) {
        if (mAddViewData == null || mAddView == null){
            return;
        }
        if (mAddViewData.isScrollWithWebView()){
            removeViewFromWebView(TAG);
        }else{
            removeViewFromCurrentWindow(mAddView);
            mAddView = null;
        }
    }

    // this case start a Activity: HelloAppCanNativeActivity
    public void test_startActivityForResult(String[] parm) {
        if (parm.length>0) {
            mFuncActivityCallback = Integer.parseInt(parm[0]);
        }
        Intent intent = new Intent();
        intent.setClass(mContext, HelloAppCanNativeActivity.class);
        try {
            startActivityForResult(intent, mMyActivityRequestCode);
        } catch (Exception e) {
            Toast.makeText(mContext, "找不到此Activity!!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    // this case to use Vibrator
    public boolean test_vibrator(String[] parm) {
        if (parm.length < 1) {
            return false;
        }
        VibratorDataVO dataVO = DataHelper.gson.fromJson(parm[0], VibratorDataVO.class);
        double inMilliseconds = dataVO.getTime();
        try {
            if (null == m_v) {
                m_v = (Vibrator) mContext
                        .getSystemService(Service.VIBRATOR_SERVICE);
            }
            m_v.vibrate((int)inMilliseconds);
        } catch (SecurityException e) {
            Toast.makeText(mContext, "未配置震动权限或参数错误!!", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    // this case show a input dialog
    public void test_showInputDialog(String[] parm) {
        if (parm.length < 1) {
            return;
        }
        int funcDialogCallback = -1;
        if (parm.length>1){
            funcDialogCallback= Integer.parseInt(parm[1]);
        }
        DialogDataVO dataVO = DataHelper.gson.fromJson(parm[0], DialogDataVO.class);
        String defaultValue = dataVO.getDefaultValue();
        new DialogUtil(mContext, this).show(defaultValue,funcDialogCallback);
    }

    // this case show a custom view into window
    public void test_addFragment(String[] parm) {
        if (parm.length < 1) {
            return;
        }
        if (mAddFragmentView != null){
            test_removeFragment(null);
        }
        mAddFragmentData = DataHelper.gson.fromJson(parm[0], ViewDataVO.class);
        if (mAddFragmentData == null) return;
        mAddFragmentView = new DemoFragment();
        mAddFragmentView.setFragmentText("我是一个添加的fragment的text");
        mAddFragmentView.setFragmentButtonText("fragment button");
        mAddFragmentView.setOnButtonClick(onFragmentButtonClick);
        if (mAddFragmentData.isScrollWithWebView()){
            android.widget.AbsoluteLayout.LayoutParams lp = new
                    android.widget.AbsoluteLayout.LayoutParams(
                    mAddFragmentData.getWidth(),
                    mAddFragmentData.getHeight(),
                    mAddFragmentData.getLeft(),
                    mAddFragmentData.getTop());
            addFragmentToWebView(mAddFragmentView, lp, TAG);
        }else{
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    mAddFragmentData.getWidth(), mAddFragmentData.getHeight());
            lp.leftMargin = mAddFragmentData.getLeft();
            lp.topMargin = mAddFragmentData.getTop();
            addFragmentToCurrentWindow(mAddFragmentView, lp, TAG);
        }

    }

    // this case remove a custom view from window
    public void test_removeFragment(String[] parm) {
        if (mAddFragmentData == null || mAddFragmentView == null){
            return;
        }
        if (mAddFragmentData.isScrollWithWebView()){
            removeFragmentFromWebView(TAG);
        }else{
            removeFragmentFromWindow(mAddFragmentView);
            mAddFragmentView = null;
        }
    }

    private void callBackPluginJs(String methodName, String jsonData){
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "('" + jsonData + "');}";
        onCallback(js);
    }

    // clean something
    @Override
    protected boolean clean() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mMyActivityRequestCode) {
            JSONObject jsonObject = new JSONObject();
            try {
                if (resultCode == Activity.RESULT_OK) {
                    String ret = data.getStringExtra("result");
                    jsonObject.put("result", ret);
                } else {
                    jsonObject.put("result", "cancel");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            callbackToJs(mFuncActivityCallback,false,0, jsonObject);
        }
    }

    public interface OnButtonClick{
        public void onButtonClick();
    }

    OnButtonClick onViewButtonClick = new OnButtonClick() {
        @Override
        public void onButtonClick() {
            callBackPluginJs(CALLBACK_ON_VIEW_BUTTON_CLICK, "");
        }
    };

    OnButtonClick onFragmentButtonClick = new OnButtonClick() {
        @Override
        public void onButtonClick() {
            callBackPluginJs(CALLBACK_ON_FRAGMENT_BUTTON_CLICK, "");
        }
    };
}
