package nju.tb.net;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import java.util.ArrayList;
import java.util.List;

import nju.tb.Commen.MyAppContext;

public class Withdraw extends Thread implements Parse.ParseHttp {
    private final String WITHWRAW = Net.URL_PREFIX + "/user/auth/withdraw";
    private static int result = -1;
    private Context context;
    private String token;
    private Handler handler;
    private int money;

    public static int getResult() {
        return result;
    }

    public Withdraw(Context context, String token, int money, Handler handler) {
        this.context = context;
        this.token = token;
        this.handler = handler;
        this.money = money;
        result = -1;
    }

    @Override
    public void parseHttpResponse(HttpResponse httpResponse) {
        HttpEntity httpEntity = httpResponse.getEntity();
        Message message = handler.obtainMessage();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
            StringBuffer stringBuffer = new StringBuffer();
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                stringBuffer.append(line);
            }

            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            result = jsonObject.getInt("result");
            if (result == 0) {
                message.what = 0;    //提现失败
                handler.sendMessage(message);
                return;
            }
            MyAppContext myAppContext = MyAppContext.getMyAppContext();
            myAppContext.setMoney(myAppContext.getMoney() - money);
            message.what = 1;  //提现成功
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                httpEntity.consumeContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        HttpRequest request = new HttpRequest(context);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("money", String.valueOf(money)));
        HttpResponse httpResponse = request.sendHttpPostRequest(WITHWRAW, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                Message message = handler.obtainMessage();
                message.what = 0;    //提现失败
                handler.sendMessage(message);
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
