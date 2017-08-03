package com.fizz.werewolfkill;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.just.library.AgentWeb;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;

/**
 * Created by xiexinwang on 2017/8/1.
 */

class AndroidInterface {

    private AgentWeb mAgentWeb;
    private Activity mActivity;

    public AndroidInterface(AgentWeb agentWeb, Activity activity) {

        mActivity = activity;
        mAgentWeb = agentWeb;
    }

    @JavascriptInterface
    public void WeiXin() {

        Log.i("flag", "WeiXin: ");
        WeixinLogin();

    }

    private void WeixinLogin() {

        IWXAPI api = App.getInstance().getAPI();

        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, "WeChat not installed", Toast.LENGTH_SHORT).show();
            return;
        }

        {// send oauth request
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "diandi_wx_login";

//        SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "wechat_sdk_demo_test";
            api.sendReq(req);
        }


    }
}
