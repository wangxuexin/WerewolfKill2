package com.fizz.werewolfkill.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.fizz.werewolfkill.App;
import com.fizz.werewolfkill.MainActivity;
import com.fizz.werewolfkill.bean.AccessBean;
import com.fizz.werewolfkill.bean.TokenBean;
import com.fizz.werewolfkill.bean.UserInfo;
import com.fizz.werewolfkill.constant.HandlerWhat;
import com.fizz.werewolfkill.utils.HttpUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by xiexinwang on 2017/7/31.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private static final String TAG = "WXEntryActivity";

    private static final String APP_ID = "wx542bf2845d8b5aa6";
    private static final String SECRET = "5e0650f4a9274475d81d5b4377fd7312";

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case HandlerWhat.ACCESS_WHAT:

                    Log.i(TAG, "handleMessage1: ");
                    AccessBean accessBean = (AccessBean) msg.obj;
                    if (accessBean != null) {

                        mAccess_token = accessBean.getAccess_token();
                        mOpenid = accessBean.getOpenid();
                        mRefresh_token = accessBean.getRefresh_token();

                        String url = "https://api.weixin.qq.com/sns/auth?" +
                                "access_token=" + mAccess_token +
                                "&openid=" + mOpenid;

                        HttpUtils.HttpGetData(url, handler, HandlerWhat.TOKEN_WHAT);

                    }

                    break;

                case HandlerWhat.TOKEN_WHAT:

                    TokenBean tokenBean = (TokenBean) msg.obj;

                    if (tokenBean != null && tokenBean.getErrmsg().equals("ok")) {

                        String url = "https://api.weixin.qq.com/sns/userinfo?" +
                                "access_token=" + mAccess_token +
                                "&openid=" + mOpenid;

                        HttpUtils.HttpGetData(url, handler, HandlerWhat.USER_WHAT);
                        Log.i(TAG, "handleMessage2:o ");


                    } else {

                        if (mRefresh_token != null) {

                            String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
                                    "appid=" + APP_ID +
                                    "&grant_type=refresh_token" +
                                    "&refresh_token=" + mRefresh_token;
                            HttpUtils.HttpGetData(url, handler, HandlerWhat.TOKEN_WHAT);
                            Log.i(TAG, "handleMessage2: e");
                        }
                    }

                    break;

                case HandlerWhat.USER_WHAT:

                    Log.i(TAG, "handleMessage3: ");
                    UserInfo userInfo = (UserInfo) msg.obj;
                    if (userInfo != null) {

                        MainActivity activity = (MainActivity) App.getInstance().getActivity();
                        activity.wxLogin(userInfo.getNickname(), userInfo.getHeadimgurl(), userInfo.getUnionid());

                    }

                    finish();

                    break;

            }
        }
    };
    private String mAccess_token;
    private String mOpenid;
    private String mRefresh_token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        IWXAPI api = App.getInstance().getAPI();
        Log.i(TAG, "onCreate: ");
        api.handleIntent(getIntent(), this);

    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp baseResp) {

        switch (baseResp.errCode) {

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == baseResp.getType()) {
                } else {
                    Toast.makeText(this, "Logon failure", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (baseResp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) baseResp).code;

                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                                "appid=" + APP_ID +
                                "&secret=" + SECRET +
                                "&code=" + code +
                                "&grant_type=authorization_code";

                        HttpUtils.HttpGetData(url, handler, HandlerWhat.ACCESS_WHAT);

                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        break;

                    case RETURN_MSG_TYPE_SHARE:
                        finish();
                        break;
                }
                break;
        }
    }

}
