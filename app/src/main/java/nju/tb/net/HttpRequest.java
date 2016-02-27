package nju.tb.net;


import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
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
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            httpResponse = httpClient.execute(httpPost);
        } catch (IOException excetion) {

        }
        return httpResponse;
    }
}