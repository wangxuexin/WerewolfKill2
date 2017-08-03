package com.fizz.werewolfkill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fizz.werewolfkill.bean.UserInfo;
import com.fizz.werewolfkill.wxapi.WXEntryActivity;

public class TestActivity extends AppCompatActivity {

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

                case 10:

//                    UserInfo userInfo1 = new UserInfo();
//                    userInfo1.setNickname("1adm23");
//                    userInfo1.setHeadimgurl("http://www.baidu.com/image?id=123.jpg");
//                    userInfo1.setUnionid("123");
//                    Intent intent = new Intent();
////                    intent.putExtra(MainActivity.USERINFO,userInfo1);
//                    intent.putExtra("name","12wqew");
//                    intent.putExtra("img","http://www.baidu.com/image?id=123.jpg");
//                    intent.putExtra("id","123");
//
//                    setResult(1,intent);
//                    finish();

                    MainActivity activity = (MainActivity) App.getInstance().getActivity();
                    activity.wxLogin("123","http://www.baidu.com/image?id=123.jpg","123");

                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        putData();
    }

    private void putData() {
        Message msg = handler.obtainMessage();
        if (msg == null) {
            msg = new Message();
        }
        msg.what = 10;
        handler.sendMessageDelayed(msg,2000);
    }

}
