package com.fizz.werewolfkill;

import android.app.Activity;
import android.app.Application;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by xiexinwang on 2017/7/31.
 */

public class App extends Application {

    private String app_id = "wx542bf2845d8b5aa6";
    private Activity mActivity;

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    private IWXAPI api;
    public static App mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        regTowx();
    }

    public static App getInstance() {


        return mApp;
    }

    private void regTowx() {

        api = WXAPIFactory.createWXAPI(this, app_id, true);
        api.registerApp(app_id);
    }

    public IWXAPI getAPI(){

        return api;
    }
}
