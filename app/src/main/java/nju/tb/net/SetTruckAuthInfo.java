package nju.tb.net;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

public class SetTruckAuthInfo extends Thread implements Parse.ParseHttp {
    private final String SETTRUCKAUTHINFO = "http://120.27.112.9:8080/tongbao/driver/auth/setTruckAuthInfo";
    private static int result = -1;
    private Context context;
    private String token;
    private String truckNum;
    private static String errorMsg;
    private String truckHeadPicUrl;
    private String driverLicensePicUrl;

    public SetTruckAuthInfo(Context context, String token, String truckNum, String truckHeadPicUrl, String
            driverLicensePicUrl) {
        this.context = context;
        this.token = token;
        this.truckNum = truckNum;
        this.truckHeadPicUrl = truckHeadPicUrl;
        this.driverLicensePicUrl = driverLicensePicUrl;
        result = -1;
        errorMsg = "";
    }

    public static int getResult() {
        return result;
    }

    public static String getErrorMsg() {
        return errorMsg;
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
            Log.i("result12", result + "");
            if (result == 0) {
                errorMsg = jsonObject.getString("errorMsg");
                Log.i("MEssge", errorMsg);
                httpEntity.consumeContent();
                return;
            }
            httpEntity.consumeContent();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (SetRealNameAuthInfo.getResult() == -1) {

        }
        HttpRequest request = new HttpRequest(context);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Log.i("token",token);
        Log.i("truckNum",truckNum);
        Log.i("truckHeadPicUrl", truckHeadPicUrl);
        Log.i("driveLicensePicUrl", driverLicensePicUrl);
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("truckNum", truckNum));
        params.add(new BasicNameValuePair("truckHeadPicUrl", truckHeadPicUrl));
        params.add(new BasicNameValuePair("driveLicensePicUrl", driverLicensePicUrl));
        HttpResponse httpResponse = request.sendHttpPostRequest(SETTRUCKAUTHINFO, params);
        while (httpResponse == null) {
            if (!MyAppContext.getIsConnected()) {
                return;
            }
        }
        parseHttpResponse(httpResponse);
    }
}
