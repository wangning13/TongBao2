package nju.tb.net;

import android.content.Context;
import android.util.Log;

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
import nju.tb.entity.Order;

/**
 * Created by Administrator on 2016/3/4.
 */
public class ShowMyOrderList extends Thread implements Parse.ParseHttp {
    private final String SHOW_MYORDERLIST = "http://120.27.112.9:8080/tongbao/driver/auth/showMyOrderList";
    private static int result = -1;
    private Context context;
    private String token;
    private String type;
    private static ArrayList<Order> orderList;
    private boolean runover=false;

    public boolean isRunover(){
        return this.runover;
    }

    public ShowMyOrderList(Context context, String token, String type) {
        this.context = context;
        this.token = token;
        this.type = type;
        result = -1;
        orderList=new ArrayList<Order>();
    }

    public static int getResult() {
        return result;
    }

    public static ArrayList<Order> getOrderList(){
        return orderList;
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
            if(result==0){
                runover=true;
                return;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for(int i=0;i<jsonArray.length();i++) {
                JSONObject orderjsonobject = jsonArray.getJSONObject(i);
                int id = orderjsonobject.getInt("id");
                String time=orderjsonobject.getString("time");
                String addressFrom=orderjsonobject.getString("addressFrom");
                String addressTo=orderjsonobject.getString("addressTo");
                String money=orderjsonobject.getString("money");
                String truckTypes=orderjsonobject.getString("truckTypes");
                String fromContactName=orderjsonobject.getString("fromContactName");
                String fromContactPhone=orderjsonobject.getString("fromContactPhone");
                String toContactName=orderjsonobject.getString("toContactName");
                String toContactPhone=orderjsonobject.getString("toContactPhone");
                String loadTime=orderjsonobject.getString("loadTime");
                Order order=new Order();
                order.setId(id);
                order.setTime(time);
                order.setAddressFrom(addressFrom);
                order.setAddressTo(addressTo);
                order.setMoney(money);
                order.setTruckTypes(truckTypes);
                order.setFromContactName(fromContactName);
                order.setFromContactPhone(fromContactPhone);
                order.setToContactName(toContactName);
                order.setToContactPhone(toContactPhone);
                order.setLoadTime(loadTime);
                orderList.add(order);
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
        params.add(new BasicNameValuePair("type", type));
        HttpResponse httpResponse = request.sendHttpPostRequest(SHOW_MYORDERLIST, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }

}
