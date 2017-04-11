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
 * Created by Administrator on 2016/3/5.
 */
public class GetOrderDetail extends Thread implements Parse.ParseHttp {
    private final String GET_ORDER_DETAIL = Net.URL_PREFIX + "/driver/auth/getOrderDetail";
    private static int result = -1;
    private Context context;
    private String token;
    private String id;
    private static Order order;
    private boolean runover=false;

    public boolean isRunover(){
        return this.runover;
    }

    public GetOrderDetail(Context context, String token, String id) {
        this.context = context;
        this.token = token;
        this.id = id;
        result = -1;
        order=new Order();
    }

    public static int getResult() {
        return result;
    }

    public static Order getOrder(){
        return order;
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
            Log.i("string:",stringBuffer.toString());
            if(!stringBuffer.toString().contains("result")){
                result=-2;
                runover=true;
                return;
            }
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            result = jsonObject.getInt("result");
            if(result==0){
                runover=true;
                return;
            }
            JSONObject orderjsonobject = jsonObject.getJSONObject("data");
            JSONArray types = orderjsonobject.getJSONArray("truckTypes");
            String typesatring="";
            for(int i=0;i<types.length();i++){
                typesatring+=types.get(i)+" ";
            }
            int id = orderjsonobject.getInt("id");
            String time=orderjsonobject.getString("time");
            String addressFrom=orderjsonobject.getString("addressFrom");
            String addressTo=orderjsonobject.getString("addressTo");
            String money=orderjsonobject.getString("money");
            String fromContactName=orderjsonobject.getString("fromContactName");
            String fromContactPhone=orderjsonobject.getString("fromContactPhone");
            String toContactName=orderjsonobject.getString("toContactName");
            String toContactPhone=orderjsonobject.getString("toContactPhone");
            String loadTime=orderjsonobject.getString("loadTime");
            String state=orderjsonobject.getString("state");
            order.setId(id);
            order.setTime(time);
            order.setAddressFrom(addressFrom);
            order.setAddressTo(addressTo);
            order.setMoney(money);
            order.setTruckTypes(typesatring);
            order.setFromContactName(fromContactName);
            order.setFromContactPhone(fromContactPhone);
            order.setToContactName(toContactName);
            order.setToContactPhone(toContactPhone);
            order.setLoadTime(loadTime);
            order.setState(state);
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
        params.add(new BasicNameValuePair("id", id));
        Log.i("token",token);
        Log.i("id",id);
        HttpResponse httpResponse = request.sendHttpPostRequest(GET_ORDER_DETAIL, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }

}
