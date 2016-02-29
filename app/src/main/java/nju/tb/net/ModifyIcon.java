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

public class ModifyIcon extends Thread implements Parse.ParseHttp {
    private final String MODIFY_ICON = "http://120.27.112.9:8080/tongbao/user/auth/modifyIcon";
    private int result = -1;
    private String errorMsg = "";
    private Context context;
    private String token;
    private String iconUrl;
    public  boolean runover=false;

    public ModifyIcon(Context context, String token, String iconUrl) {
        this.context = context;
        this.token = token;
        this.iconUrl = iconUrl;
    }

    public int getResult() {
        return result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void parseHttpResponse(HttpResponse httpResponse) {
        HttpEntity httpEntity = (HttpEntity) httpResponse.getEntity();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
            StringBuffer stringBuffer = new StringBuffer();
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                stringBuffer.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            result = jsonObject.getInt("result");
            errorMsg = jsonObject.getString("errorMsg");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        HttpRequest httpRequest = new HttpRequest(context);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("iconUrl", iconUrl));
        HttpResponse httpResponse = httpRequest.sendHttpPostRequest(MODIFY_ICON, params);
        while (httpResponse == null) {

        }

        parseHttpResponse(httpResponse);
        runover=true;
    }


}
