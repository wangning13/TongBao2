package nju.tb.net;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

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
import nju.tb.entity.TruckDetail;

public class GetTruckDetail extends Thread implements Parse.ParseHttp {
    private final String GETTRUCKDETAIL = Net.URL_PREFIX + "/driver/auth/getTruckDetail";
    private static int result = -1;
    private Context context;
    private String token;
    private int id;
    private Handler handler;
    private TruckDetail truckDetail;
    private static String errorMsg;

    public TruckDetail getTruckDetail() {
        return this.truckDetail;
    }

    public static int getResult() {
        return result;
    }

    public static String getErrorMsg() {
        return errorMsg;
    }

    public GetTruckDetail(Context context, String token, int id, Handler handler) {
        this.context = context;
        this.token = token;
        this.id = id;
        this.handler = handler;
        result = -1;
        errorMsg = "";
        truckDetail = new TruckDetail();
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
                httpEntity.consumeContent();
                return;
            }
            JSONObject data = jsonObject.getJSONObject("data");
            truckDetail.setTruckNum(data.getString("truckNum"));
            truckDetail.setAuthState(data.getInt("authState"));
            truckDetail.setTypeName(data.getString("typeName"));
            truckDetail.setLength(data.getInt("length"));
            truckDetail.setCapacity(data.getInt("capacity"));
            truckDetail.setPhoneNum(data.getString("phoneNum"));
            if (data.getString("realName").equals("null")) {
                truckDetail.setRealName("暂无");
            } else {
                truckDetail.setRealName(data.getString("realName"));
            }
            httpEntity.consumeContent();

            Message message = handler.obtainMessage(0);
            message.obj = truckDetail;
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
        params.add(new BasicNameValuePair("id", String.valueOf(id)));
        HttpResponse httpResponse = request.sendHttpPostRequest(GETTRUCKDETAIL, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }

}
