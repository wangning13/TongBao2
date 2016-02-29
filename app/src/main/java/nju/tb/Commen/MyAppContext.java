package nju.tb.Commen;


import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
    private String nickName = "";  //用户昵称
    private String iconUrl = "1";  //用户头像url
    private int point = 0;    //用户积分
    private int money = 0;    //用户余额
    private String token = "";    //识别用户的token

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return this.point;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return this.money;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public static void setLogIn(boolean logState) {
        isLogIn = logState;
    }

    public static boolean isLogIn() {
        return isLogIn;
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public String getDisplayToken() {
        return DRIVER_DISPLAY_PICTURE_TOKEN;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isLogIn = false;

        //test 已登陆
        HttpResponse httpResponse = null;
        LoginThread loginThread = new LoginThread();
        new Thread(loginThread).start();
//        httpResponse = loginThread.getHttpResponse();

    }

    private void parseHttpResponse(HttpResponse httpResponse) {
        HttpEntity entity = httpResponse.getEntity();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuffer stringBuffer = new StringBuffer();
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                stringBuffer.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            int result = jsonObject.getInt("result");
            if (result == 0) {
                Log.i("result", "登陆失败");
                return;
            }
//            Log.i("result",""+result);
            JSONObject data = jsonObject.getJSONObject("data");
            setNickName(data.getString("nickName"));
            setIconUrl(data.getString("iconUrl"));
            setPoint(data.getInt("point"));
            setMoney(data.getInt("money"));
            setToken(data.getString("token"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class LoginThread implements Runnable {
        private HttpResponse httpResponse = null;
        private boolean runover = false;

        @Override
        public void run() {
            HttpRequest request = new HttpRequest(MyAppContext.this);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("phoneNumber", "15851813142"));
            params.add(new BasicNameValuePair("password", "a"));
            httpResponse = request.sendHttpPostRequest("http://120.27.112.9:8080/tongbao/user/login", params);
            // Log.i("state",httpResponse.getStatusLine().getStatusCode()+"");
            while (httpResponse == null) {
            }
            parseHttpResponse(httpResponse);
//            Log.i("nickname",getNickName());
//            Log.i("phone",getIconUrl());
            runover = true;
        }

        public HttpResponse getHttpResponse() {
            while (!runover) {
            }
            return this.httpResponse;
        }
    }

}
