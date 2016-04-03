package nju.tb.Commen;


import android.app.Application;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import nju.tb.entity.Account;
import nju.tb.net.GetAllTruckTypes;
import nju.tb.net.HttpRequest;

@SuppressWarnings("deprecation")
public class MyAppContext extends Application {
    private static boolean isLogIn;
    public static boolean isNetServiceStarted = false;
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

    private String phone;

    private List<String> truckTypesList;

    private String nickName = "";  //用户昵称
    private String iconUrl = "1";  //用户头像url
    private int point = 0;    //用户积分
    private int money = 0;    //用户余额
    private String token = "";    //识别用户的token
    private String id = "";    //识别用户的token

    private List<Account> accountList; //账单列表

    public void setAccountList(List<Account> list) {
        this.accountList = list;
    }

    public List<Account> getAccountList() {
        return this.accountList;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //登录状态
    public static void setLogIn(boolean logState) {
        isLogIn = logState;
    }

    public static boolean isLogIn() {
        return isLogIn;
    }

    public synchronized HttpClient getHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, true);
        ConnManagerParams.setMaxTotalConnections(params, 100);
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 3000);
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
        return new DefaultHttpClient(conMgr, params);
    }

    public void setTruckList(List<String> list) {
        this.truckTypesList = list;
    }

    public List<String> getTruckList() {
        return truckTypesList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        isLogIn = false;
        myAppContext = this;
        startService(new Intent(this, nju.tb.services.NetStateService.class));  //监控网络
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER );
        if(gpsEnabled){
            startService(new Intent(this, nju.tb.services.GPSService.class));
        }


    }


}
