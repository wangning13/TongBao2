package nju.tb.net;


import android.content.Context;
import android.content.SharedPreferences;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nju.tb.Commen.Common;
import nju.tb.Commen.MyAppContext;

public class Login extends Thread implements Parse.ParseHttp {
    private final String LOGIN = Net.URL_PREFIX + "/user/login";
    private static int result = -1;
    private Context context;
    private String phoneNumber;
    private String password;
    private MyAppContext myAppContext;
    SharedPreferences sharedPreferences;

    public Login(Context context, String phoneNumber, String password) {
        this.context = context;
        this.phoneNumber = phoneNumber;
        this.password = password;
        myAppContext = MyAppContext.getMyAppContext();
        result = -1;
    }

    public static int getResult() {
        return result;
    }

    @Override
    public void parseHttpResponse(HttpResponse httpResponse) {
        HttpEntity entity = httpResponse.getEntity();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Common.USER_INFO,Context.MODE_PRIVATE);

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuffer stringBuffer = new StringBuffer();
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                stringBuffer.append(line);
            }
            in.close();
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            result = jsonObject.getInt("result");
            if (result == 0) {
               entity.consumeContent();
                return;
            }

            MyAppContext.setLogIn(true);
            JSONObject data = jsonObject.getJSONObject("data");
            if (data.getString("nickName") == null) {
                myAppContext.setNickName("");
            } else {
                myAppContext.setNickName(data.getString("nickName"));
            }
            myAppContext.setIconUrl(data.getString("iconUrl"));
            myAppContext.setPoint(data.getInt("point"));
            myAppContext.setMoney(data.getInt("money"));
            myAppContext.setToken(data.getString("token"));
            myAppContext.setId(data.getString("id"));
            sharedPreferences.edit().putBoolean(Common.IS_LOGIN,true).apply();
            sharedPreferences.edit().putString(Common.PHONE_NUMBER,phoneNumber).apply();
            sharedPreferences.edit().putString(Common.PWD,password).apply();
            entity.consumeContent();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        HttpRequest request = new HttpRequest(context);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("phoneNumber", phoneNumber));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("type", "1"));
        HttpResponse httpResponse = request.sendHttpPostRequest(LOGIN, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
