package nju.tb.Commen;


import android.app.Application;
import android.content.Intent;
import android.telephony.ServiceState;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nju.tb.net.HttpRequest;

@SuppressWarnings("deprecation")
public class MyAppContext extends Application {
    private static boolean isLogIn;
    private static HttpClient httpClient = new DefaultHttpClient();
    private final String DRIVER_DISPLAY_PICTURE_TOKEN =
            "81f5465b9f87dcdc21c19eb29d03b59f96b449da:ZXJERjJIZTR2dzFsOXA1akdqc3ZlUldGNDZ3PQ" +
                    "==:eyJkZWFkbGluZSI6MTQ1NjYzNjc2MSwiYWN0aW9uIjoiZ2V0IiwidWlkIjoiNTUwMTA1IiwiYWlkIjoiMTIwNTU1NiIsImZyb20iOiJmaWxlIn0=";
    private static boolean isConnected = false;

    private static MyAppContext myAppContext;

    public static void setIsConnected(boolean b) {
        isConnected = b;
    }

    public static boolean getIsConnected() {
        return isConnected;
    }

    public static MyAppContext getMyAppContext() {
        return myAppContext;
    }

    private String phone ;


    private String nickName = "";  //用户昵称
    private String iconUrl = "1";  //用户头像url
    private int point = 0;    //用户积分
    private int money = 0;    //用户余额
    private String token = "";    //识别用户的token

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    //昵称
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return this.nickName;
    }

    //头像
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    //积分
    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return this.point;
    }

    //余额
    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return this.money;
    }

    //token
    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    //登录状态
    public static void setLogIn(boolean logState) {
        isLogIn = logState;
    }

    public static boolean isLogIn() {
        return isLogIn;
    }

    public HttpClient getHttpClient() {
        httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 3000);
        httpClient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);

        return this.httpClient;
    }

    public String getDisplayToken() {
        return DRIVER_DISPLAY_PICTURE_TOKEN;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isLogIn = false;
        myAppContext = this;
        startService(new Intent(this, nju.tb.services.NetStateService.class));

    }


}
