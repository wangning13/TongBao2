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

public class ModifyNickName extends Thread implements Parse.ParseHttp {
    private final String MODIFYNICKNAME = Net.URL_PREFIX + "/user/auth/modifyNickName";
    private static int result = -1;
    private Context context;
    private String token;
    private String nickName;
    private static String errorMsg;

    public static int getResult() {
        return result;
    }

    public static String getErrorMsg() {
        return errorMsg;
    }

    public ModifyNickName(Context context, String token, String nickName) {
        this.context = context;
        this.nickName = nickName;
        this.token = token;
        result = -1;
        errorMsg = "";
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
            MyAppContext myAppContext=MyAppContext.getMyAppContext();
            myAppContext.setNickName(nickName);
            entity.consumeContent();
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
        params.add(new BasicNameValuePair("nickName", nickName));
        HttpResponse httpResponse = request.sendHttpPostRequest(MODIFYNICKNAME, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
