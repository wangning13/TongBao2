package nju.tb.net;


import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nju.tb.Commen.MyAppContext;

public class GetAllTruckTypes extends Thread implements Parse.ParseHttp {
    private final String GETALLTRUCKTYPES = "http://120.27.112.9:8080/tongbao/user/getAllTruckTypes";
    private static int result = -1;
    private Context context;
    private static String errormsg = "";
    private MyAppContext myAppContext;

    public GetAllTruckTypes(Context context) {
        this.context = context;
        myAppContext = MyAppContext.getMyAppContext();
        result = -1;
    }

    private static int getResult() {
        return result;
    }

    private static String getErrormsg() {
        return errormsg;
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
                errormsg = jsonObject.getString("errorMsg");
                return;
            }
            List<String> truckTypesList = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                String name = temp.getString("name");
                String capacity = temp.getString("capacity");
                String length = temp.getString("length");
                String width = temp.getString("width");
                String height = temp.getString("height");
                int type = temp.getInt("type");
                StringBuffer sb = new StringBuffer();
                sb.append(name + " " + capacity + " " + length + " " + width + " " + height + " " + type);
                truckTypesList.add(sb.toString());
            }
            myAppContext.setTruckList(truckTypesList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        HttpRequest request = new HttpRequest(context);
        HttpResponse httpResponse = request.sendHttpPostRequest(GETALLTRUCKTYPES, null);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
