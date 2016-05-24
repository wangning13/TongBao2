package nju.tb.net;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

public class AddTruck extends Thread implements Parse.ParseHttp {
    private final String ADDTRUCK = "http://120.27.112.9:8080/tongbao/driver/auth/addTruck";
    private static int result = -1;
    private Context context;
    private String token;
    private String truckNum;
    private int type;
    private String phoneNum;
    private static String errorMsg;

    public AddTruck(Context context, String token, String truckNum, int type, String phoneNum) {
        this.context = context;
        this.token = token;
        this.truckNum = truckNum;
        this.type = type;
        this.phoneNum = phoneNum;
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
//            Log.i("addcar~~~",stringBuffer.toString());
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            result = jsonObject.getInt("result");
            if (result == 0) {
                errorMsg = jsonObject.getString("errorMsg");
                httpEntity.consumeContent();
                return;
            }
            httpEntity.consumeContent();
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
        params.add(new BasicNameValuePair("type", type + ""));
        params.add(new BasicNameValuePair("phoneNum", phoneNum));
        HttpResponse httpResponse = request.sendHttpPostRequest(ADDTRUCK, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
