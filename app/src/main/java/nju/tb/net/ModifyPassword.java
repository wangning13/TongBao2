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

public class ModifyPassword extends Thread implements Parse.ParseHttp {
    private final String MODIFYPASSWORD = "http://120.27.112.9:8080/tongbao/user/auth/modifyPassword";
    private static int result = -1;
    private Context context;
    private String token;
    private static String errorMsg;
    private String oldPassword;
    private String newPassword;
    private boolean isModifyNickNameRunning;

    public static int getResult() {
        return result;
    }

    public static String getErrorMsg() {
        return errorMsg;
    }

    public ModifyPassword(Context context, String token, String oldPassword, String newPassword, boolean
            isModifyNickNameRunning) {
        this.context = context;
        this.token = token;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        result = -1;
        errorMsg = "";
        this.isModifyNickNameRunning = isModifyNickNameRunning;
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
                entity.consumeContent();
                errorMsg = jsonObject.getString("errorMsg");
                return;
            }
            entity.consumeContent();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (isModifyNickNameRunning) {
            while (ModifyNickName.getResult() == -1) {
            }
        }
        HttpRequest request = new HttpRequest(context);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("oldPassword", oldPassword));
        params.add(new BasicNameValuePair("newPassword", newPassword));
        HttpResponse httpResponse = request.sendHttpPostRequest(MODIFYPASSWORD, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
