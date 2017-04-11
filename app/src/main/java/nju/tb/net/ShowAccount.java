package nju.tb.net;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nju.tb.Commen.MyAppContext;
import nju.tb.entity.Account;

public class ShowAccount extends Thread implements Parse.ParseHttp {
    private final String SHOWACCOUNT = Net.URL_PREFIX + "/user/auth/showAccount";
    private static int result = -1;
    private Context context;
    private String token;
    private Handler handler;
    private List<Account> accountList;

    public ShowAccount(Context context, String token, Handler handler) {
        this.context = context;
        this.token = token;
        this.handler = handler;
        result = -1;
        accountList = new ArrayList<>();
    }

    public static int getResult() {
        return result;
    }

    @Override
    public void parseHttpResponse(HttpResponse httpResponse) {
        HttpEntity httpEntity = httpResponse.getEntity();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
            StringBuffer stringBuffer = new StringBuffer();
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                stringBuffer.append(line);
            }
            Log.i("token" ,token);
            Log.i("token11" ,stringBuffer.toString());
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            result = jsonObject.getInt("result");
            if (result == 0) {
                return;
            }
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Account account = new Account();
                JSONObject temp = data.getJSONObject(i);

                int type = temp.getInt("type");
                account.setType(type);
                account.setTime(temp.getString("time"));
                account.setMoney(temp.getInt("money"));
                if (type == 0 || type == 1) {
                    accountList.add(account);
                    continue;
                }
                JSONObject order = temp.getJSONObject("order");
                account.setOrderId(order.getInt("id"));
                account.setAddressFrom(order.getString("addressFrom"));
                account.setAddressTo(order.getString("addressTo"));
                accountList.add(account);
            }
            MyAppContext myAppContext = MyAppContext.getMyAppContext();
            myAppContext.setAccountList(accountList);
            Message message = handler.obtainMessage();
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
        HttpResponse httpResponse = request.sendHttpPostRequest(SHOWACCOUNT, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
