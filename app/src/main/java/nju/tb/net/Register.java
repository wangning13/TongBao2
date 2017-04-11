package nju.tb.net;


import android.content.Context;


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

public class Register extends Thread implements Parse.ParseHttp {
    private final String REGISTER = Net.URL_PREFIX + "/user/register";
    private static int result = -1;
    private Context context;
    private String phoneNumber;
    private String password;

    public Register(Context context, String phoneNumber, String password) {
        this.context = context;
        this.phoneNumber = phoneNumber;
        this.password = password;
        result = -1;
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
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            result = jsonObject.getInt("result");
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
        HttpResponse httpResponse = request.sendHttpPostRequest(REGISTER, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }

}
