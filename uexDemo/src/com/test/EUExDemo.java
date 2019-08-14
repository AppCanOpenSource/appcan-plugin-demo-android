package com.test;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BDebug;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;

public class EUExDemo extends EUExBase {

    private static final String TAG = "uexDemo";
    
    private int mFuncActivityCallback =-1;
    private static final int mMyActivityRequestCode = 10000;

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

    // ============= 分割线 ==============
    // 以下为生命周期事件监听方法，按需声明，不需要的话就不用声明。

    /**
     * 用于监听Application的onCreate事件，需要的写好这个方法用来接收引擎的通知。
     * 利用此方法可以解决绝大部分需要自定义Application的情况（AppCan插件中不允许自定义Application）
     *
     * @param context ApplicationContext上下文实例
     */
    public static void onApplicationCreate(Context context) {
        BDebug.i(TAG, TAG + " onApplicationCreate");
    }

    /**
     * 对应加载插件的引擎Activity的生命周期方法onCreate
     *
     * @param context 加载插件的引擎ActivityContext实例
     */
    public static void onActivityCreate(Context context) {
        BDebug.i(TAG, TAG + " onActivityCreate");
    }

    /**
     * 对应加载插件的引擎Activity的生命周期方法onStart
     *
     * @param context 加载插件的引擎ActivityContext实例
     */
    public static void onActivityStart(Context context) {
        BDebug.i(TAG, TAG + " onActivityStart");
    }

    /**
     * 对应加载插件的引擎Activity的生命周期方法onRestart
     *
     * @param context 加载插件的引擎ActivityContext实例
     */
    public static void onActivityReStart(Context context) {
        BDebug.i(TAG, TAG + " onActivityReStart");
    }

    /**
     * 对应加载插件的引擎Activity的生命周期方法onResume
     *
     * @param context 加载插件的引擎ActivityContext实例
     */
    public static void onActivityResume(Context context) {
        BDebug.i(TAG, TAG + " onActivityResume");
    }

    /**
     * 对应加载插件的引擎Activity的生命周期方法onPause
     *
     * @param context 加载插件的引擎ActivityContext实例
     */
    public static void onActivityPause(Context context) {
        BDebug.i(TAG, TAG + " onActivityPause");
    }

    /**
     * 对应加载插件的引擎Activity的生命周期方法onStop
     *
     * @param context 加载插件的引擎ActivityContext实例
     */
    public static void onActivityStop(Context context) {
        BDebug.i(TAG, TAG + " onActivityStop");
    }

    /**
     * 对应加载插件的引擎Activity的生命周期方法onDestroy
     *
     * @param context 加载插件的引擎ActivityContext实例
     */
    public static void onActivityDestroy(Context context) {
        BDebug.i(TAG, TAG + " onActivityDestroy");
    }

    // 以上为生命周期事件监听方法，按需声明，不需要的话就不用声明。
    // ============= 分割线 ==============

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
        // mContext变量是继承自父类的上下文，此Context实际上是加载本插件入口类对象的引擎Activity（4.0引擎中为EBrowserActivity的实例）
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
            // save the callbackId so you can callback to Javascript with it.
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
            if (m_v != null) {
                m_v.vibrate((int)inMilliseconds);
            } else {
                Toast.makeText(mContext, "Error: Vibrator is null", Toast.LENGTH_SHORT).show();
            }
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

    /**
     * 用于执行JS回调方法
     *
     * @param methodName 回调方法名
     * @param jsonData 回调json数据
     */
    private void callBackPluginJs(String methodName, String jsonData){
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "('" + jsonData + "');}";
        onCallback(js);
    }

    /**
     * 用于执行JS回调方法（数据为JSON object）
     * 推荐此方式，防止极个别情况下，回调数据中存在异常字符导致JS处理错误 by yipeng
     *
     * @param methodName 回调方法名
     * @param jsonData 回调json数据
     */
    private void callBackPluginJsWithJsonObject(String methodName, String jsonData){
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "(" + jsonData + ");}";
        // 是否看出与上面方法的区别？传参的括号内少了单引号，这样可以让JS收到回调时拿到的就是一个JS的object，而无需使用JSON.parse
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
        void onButtonClick();
    }

    private OnButtonClick onViewButtonClick = new OnButtonClick() {
        @Override
        public void onButtonClick() {
            callBackPluginJs(CALLBACK_ON_VIEW_BUTTON_CLICK, "");
        }
    };

    private OnButtonClick onFragmentButtonClick = new OnButtonClick() {
        @Override
        public void onButtonClick() {
            callBackPluginJs(CALLBACK_ON_FRAGMENT_BUTTON_CLICK, "");
        }
    };
}
