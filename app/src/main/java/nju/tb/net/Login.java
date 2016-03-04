package nju.tb.net;


import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nju.tb.Commen.MyAppContext;

public class Login extends Thread implements Parse.ParseHttp {
    private final String LOGIN = "http://120.27.112.9:8080/tongbao/user/login";
    private static int result = -1;
    private Context context;
    private String phoneNumber;
    private String password;
    private MyAppContext myAppContext;

    public Login(Context context, String phoneNumber, String password) {
        this.context = context;
        this.phoneNumber = phoneNumber;
        this.password = password;
        myAppContext = MyAppContext.getMyAppContext();
        result=-1;
    }

    public static  int getResult() {
        return result;
    }

    @Override
    public void parseHttpResponse(HttpResponse httpResponse) {
        HttpEntity entity = httpResponse.getEntity();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuffer stringBuffer = new StringBuffer();
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                stringBuffer.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            result = jsonObject.getInt("result");
            if (result == 0) {
                return;
            }

            MyAppContext.setLogIn(true);
            JSONObject data = jsonObject.getJSONObject("data");
            myAppContext.setNickName(data.getString("nickName"));
            myAppContext.setIconUrl(data.getString("iconUrl"));
            myAppContext.setPoint(data.getInt("point"));
            myAppContext.setMoney(data.getInt("money"));
            myAppContext.setToken(data.getString("token"));
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
        params.add(new BasicNameValuePair("type","1"));
        HttpResponse httpResponse = request.sendHttpPostRequest(LOGIN, params);
        while (httpResponse == null) {
//            if (!MyAppContext.getIsConnected()) {
//                return;
//            }

        }
        parseHttpResponse(httpResponse);
    }
}
