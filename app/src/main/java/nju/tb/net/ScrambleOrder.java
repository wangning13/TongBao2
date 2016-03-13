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
import java.util.ArrayList;
import java.util.List;

import nju.tb.Commen.MyAppContext;
import nju.tb.entity.Order;

/**
 * Created by Administrator on 2016/3/12.
 */
public class ScrambleOrder extends Thread implements Parse.ParseHttp{
    private final String GRAB_ORDER = "http://120.27.112.9:8080/tongbao/driver/auth/grabOrder";
    private static int result = -1;
    private Context context;
    private String token;
    private int orderid;
    public boolean runover=false;
    private String errorMsg = "";

    public ScrambleOrder(Context context, String token, int orderid) {
        this.context = context;
        this.token = token;
        this.orderid = orderid;
        result = -1;
    }

    public boolean isRunover(){
        return this.runover;
    }
    public String getErrorMsg(){
        return this.errorMsg;
    }
    public static int getResult() {
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
            Log.i("抢单", stringBuffer.toString());
            result = jsonObject.getInt("result");
            if (result == 0) {
                errorMsg = jsonObject.getString("errorMsg");
                Log.i("errorMsg", errorMsg);
                runover=true;
                return;
            }
            runover=true;
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
        params.add(new BasicNameValuePair("id", orderid + ""));
        HttpResponse httpResponse = request.sendHttpPostRequest(GRAB_ORDER, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
