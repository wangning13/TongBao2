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

public class RemoveTruck extends Thread implements Parse.ParseHttp {
    private final String REMOVETRUCK = "http://120.27.112.9:8080/tongbao/driver/auth/removeTruck";
    private static int result = -1;
    private Context context;
    private String token;
    private String truckNum;
    private Handler handler;
    private static String errorMsg;

    public RemoveTruck(Context context, String token, String truckNum, Handler handler) {
        this.context = context;
        this.token = token;
        this.truckNum = truckNum;
        this.handler = handler;
        result = -1;
        errorMsg = "";
    }

    public static int getResult() {
        return result;
    }

    public static String getErrorMsg() {
        return errorMsg;
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
            if (result == 0) {
                errorMsg = jsonObject.getString("errorMsg");
                return;
            }
            Message message = handler.obtainMessage(0);
            handler.sendMessage(message);
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
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("truckNum", truckNum));
        HttpResponse httpResponse = request.sendHttpPostRequest(REMOVETRUCK, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
