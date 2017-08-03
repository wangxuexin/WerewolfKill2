package com.fizz.werewolfkill;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fizz.werewolfkill.bean.UserInfo;
import com.fizz.werewolfkill.utils.NetUtils;
import com.fizz.werewolfkill.wxapi.WXEntryActivity;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;

public class MainActivity extends AppCompatActivity {

    private AgentWeb mAgentWeb;
    public static String USERINFO = "user_info";
    private RelativeLayout mRelativeLayout;
    private ChromeClientCallbackManager.ReceivedTitleCallback mCallback;
    private Button click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.getInstance().setActivity(this);
//        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mRelativeLayout = findViewById(R.id.web_main);
        click = findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mAgentWeb.getJsEntraceAccess().quickCallJs("WXLogin",
//                        "abc123" , "http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg"
//                        , "123");
//                Log.i("flag", "onClick: ");

                startActivityForResult(new Intent(MainActivity.this, TestActivity.class),0);
            }
        });
        initData();


    }

    public void wxLogin(final String name, final String img, final String id){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mAgentWeb.getJsEntraceAccess().quickCallJs("WXLogin", name, img, id);
                mAgentWeb.getLoader().loadUrl("http://www.baidu.com");
                Log.i("flag", "run: +wxLogin");
                Log.i("flag", "run: +wxLogin");
            }
        });
    }
    private void initData() {
        boolean connected = NetUtils.isConnected(this);

        if (connected) {
            initView();

        } else {
            //没有网络
            Toast.makeText(this, "当前无网络", Toast.LENGTH_SHORT).show();
        }
    }


    private void initView() {

        mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
            @Override
            public void onReceivedTitle(WebView view, String title) {

            }
        };

        mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(mRelativeLayout, new RelativeLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()//
                .ready()
                .go("http://xyhaizao.bibizi.com/m/login");
        mAgentWeb.getJsInterfaceHolder().addJavaObject("Toyun", new AndroidInterface(mAgentWeb, this));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 1 && data != null) {
////            UserInfo userInfo = data.getParcelableExtra(USERINFO);
//            final String name = data.getStringExtra("name");
//            final String img = data.getStringExtra("img");
//            final String id = data.getStringExtra("id");
//
//            if (name != null) {
//
//                Log.i("flag", "onActivityResult: " + id);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        mAgentWeb.getJsEntraceAccess().quickCallJs("WXLogin", name, img, id);
//                    }
//                });
//
////                mAgentWeb.getJsEntraceAccess().quickCallJs("WXLogin", userInfo.getNickname(), userInfo.getHeadimgurl(), userInfo.getUnionid());
////                userInfo = null;
////                getIntent().putExtra(USERINFO, userInfo);
//            }
//
//        }
//    }
}
