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

/**
 * Created by Administrator on 2016/4/6.
 */
public class UpdateMyPostion extends Thread implements Parse.ParseHttp{
    private final String UPDATE_MY_POSITION = Net.URL_PREFIX + "/driver/auth/updateMyPostion";
    private static int result = -1;
    private Context context;
    private String token;
    private String collectTime;
    private String lat;
    private String lng;
    public boolean runover=false;
    private String errorMsg = "";

    public UpdateMyPostion(Context context, String token, String collectTime,String lat,String lng) {
        this.context = context;
        this.token = token;
        this.collectTime = collectTime;
        this.lat = lat;
        this.lng = lng;
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
            Log.i("0",stringBuffer.toString());
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
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
        params.add(new BasicNameValuePair("collectTime", collectTime));
        params.add(new BasicNameValuePair("lat", lat));
        params.add(new BasicNameValuePair("lng", lng));
        HttpResponse httpResponse = request.sendHttpPostRequest(UPDATE_MY_POSITION, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
