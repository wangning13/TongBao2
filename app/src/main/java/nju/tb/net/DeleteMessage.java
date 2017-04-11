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

public class DeleteMessage implements Runnable,Parse.ParseHttp {
    private final String DELETEMESSAGE = Net.URL_PREFIX + "/user/auth/deleteMessage";
    private static int result = -1;
    private Context context;
    private String token;
    private static String errorMsg;
    private String id;

    public static int getResult() {
        return result;
    }

    public static String getErrorMsg() {
        return errorMsg;
    }

    public DeleteMessage(Context context,String token,String id){
        this.context = context;
        this.token = token;
        this.id=id;
        errorMsg = "";
        result = -1;
    }



    @Override
    public void parseHttpResponse(HttpResponse httpResponse) {
        HttpEntity httpEntity = httpResponse.getEntity();
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
            StringBuffer stringBuffer = new StringBuffer();
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                stringBuffer.append(line);
            }
            Log.i("TAG",stringBuffer.toString());
            Log.i("id",id);
            Log.i("token",token);
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            result = jsonObject.getInt("result");
            if (result == 0) {
                errorMsg = jsonObject.getString("errorMsg");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        HttpRequest request = new HttpRequest(context);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("id", id));
        HttpResponse httpResponse = request.sendHttpPostRequest(DELETEMESSAGE, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
