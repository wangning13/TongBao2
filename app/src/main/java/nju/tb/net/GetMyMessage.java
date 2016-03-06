package nju.tb.net;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nju.tb.Commen.MyAppContext;
import nju.tb.entity.MyMessage;

public class GetMyMessage extends Thread implements Parse.ParseHttp {
    private final String GETMYMESSAGE = "http://120.27.112.9:8080/tongbao/user/auth/getMyMessages";
    private static int result = -1;
    private Context context;
    private List<MyMessage> myMessagesList;
    private String token;
    private static String errorMsg;
    private Handler handler;

    public static int getResult() {
        return result;
    }

    public static String getErrorMsg() {
        return errorMsg;
    }

    public GetMyMessage(Context context, String token, Handler handler) {
        this.context = context;
        this.token = token;
        myMessagesList = new ArrayList<>();
        errorMsg = "";
        result = -1;
        this.handler = handler;
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
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                MyMessage myMessage = new MyMessage();
                myMessage.setId(temp.getInt("id"));
                myMessage.setType(temp.getInt("type"));
                myMessage.setContent(temp.getString("content"));
                myMessage.setHasRead(temp.getInt("hasRead"));
                myMessage.setTime(temp.getString("time"));
                myMessage.setObjectId(temp.getInt("objectId"));
                myMessagesList.add(myMessage);
            }
            Message message = handler.obtainMessage(0);
            message.obj = myMessagesList;
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
        HttpResponse httpResponse = request.sendHttpPostRequest(GETMYMESSAGE, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
