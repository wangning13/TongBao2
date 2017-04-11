package nju.tb.net;

import android.content.Context;

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

//根据用户token获取一个司机用户名下的所有车辆的列表
public class GetTruckList extends Thread implements Parse.ParseHttp {
    private final String GETTRUCKLIST = Net.URL_PREFIX + "/driver/auth/getTruckList";
    private static int result = -1;
    private Context context;
    private String token;
    private static String errorMsg;
    private List<String> truckList;
    private boolean runover = false;

    public boolean isRunover() {
        return runover;
    }

    public List<String> getTruckList() {
        return this.truckList;
    }

    public static int getResult() {
        return result;
    }

    public static String getErrorMsg() {
        return errorMsg;
    }

    public GetTruckList(Context context, String token) {
        this.context = context;
        this.token = token;
        result = -1;
        errorMsg = "";
        truckList = new ArrayList<>();
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
                runover = true;
                httpEntity.consumeContent();
                return;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                int id = temp.getInt("id");
                String truckNum = temp.getString("truckNum");
                int authState = temp.getInt("authState");
                truckList.add(id + " " + truckNum + " " + authState);
            }
            runover = true;
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
        HttpResponse httpResponse = request.sendHttpPostRequest(GETTRUCKLIST, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                runover = true;
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
