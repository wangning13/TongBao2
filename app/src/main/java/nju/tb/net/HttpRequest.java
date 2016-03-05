package nju.tb.net;


import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import nju.tb.Commen.MyAppContext;

public class HttpRequest {
    private HttpClient httpClient;

    public HttpRequest(Context context) {
        this.httpClient = ((MyAppContext) context.getApplicationContext()).getHttpClient();
    }

    public HttpResponse sendHttpPostRequest(String url, List<NameValuePair> params) {
        HttpPost httpPost = new HttpPost(url);
        HttpResponse httpResponse = null;
        try {
            if (params != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            }
            if (MyAppContext.getIsConnected()) {
                httpResponse = httpClient.execute(httpPost);
            } else {
                return httpResponse;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (org.apache.http.conn.HttpHostConnectException e) {
            e.printStackTrace();
            return null;
        } catch (java.net.ConnectException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return httpResponse;
    }
}
