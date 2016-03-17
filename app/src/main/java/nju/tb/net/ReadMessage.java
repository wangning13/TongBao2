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

public class ReadMessage extends Thread implements Parse.ParseHttp {
    private final String READMESSAGE = "http://120.27.112.9:8080/tongbao/user/auth/readMessage";
    private static int result = -1;
    private Context context;
    private String token;
    private int id;
    private static String errorMsg;

    public static int getResult() {
        return result;
    }

    public static String getErrorMsg() {
        return errorMsg;
    }

    public ReadMessage(Context context, String token, int id) {
        this.context = context;
        this.token = token;
        this.id = id;
        result = -1;
        errorMsg = "";
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
            if (0 == result) {
                errorMsg = jsonObject.getString("errorMsg");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
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
        params.add(new BasicNameValuePair("id", String.valueOf(id)));
        HttpResponse httpResponse = request.sendHttpPostRequest(READMESSAGE, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
