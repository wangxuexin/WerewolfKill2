package com.fizz.werewolfkill.utils;


import android.os.Handler;
import android.os.Message;

import com.fizz.werewolfkill.bean.AccessBean;
import com.fizz.werewolfkill.bean.TokenBean;
import com.fizz.werewolfkill.bean.UserInfo;
import com.fizz.werewolfkill.constant.HandlerWhat;
import com.google.gson.Gson;

/**
 * Created by xiexinwang on 2017/8/1.
 */

public class DataParse {

    public static void getData(String result, Handler handler, int handlerWhat) {

        Message msg = handler.obtainMessage();
        if (msg == null) {

            msg = new Message();
        }

        switch (handlerWhat) {

            case HandlerWhat.ACCESS_WHAT:

                AccessBean accessBean = new Gson().fromJson(result, AccessBean.class);
                msg.what = handlerWhat;
                msg.obj = accessBean;
                handler.sendMessage(msg);
                break;

            case HandlerWhat.TOKEN_WHAT:

                TokenBean tokenBean = new Gson().fromJson(result, TokenBean.class);
                msg.what = handlerWhat;
                msg.obj = tokenBean;
                handler.sendMessage(msg);
                break;

            case HandlerWhat.USER_WHAT:

                UserInfo userInfo = new Gson().fromJson(result, UserInfo.class);
                msg.what = handlerWhat;
                msg.obj = userInfo;
                handler.sendMessage(msg);
                break;

            default:

                break;

        }


    }
}
