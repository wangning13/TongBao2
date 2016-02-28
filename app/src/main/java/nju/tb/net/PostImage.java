package nju.tb.net;


import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import nju.tb.Commen.MyAppContext;

@SuppressWarnings("deprecation")
public class PostImage {
    private final String POST_URL = "http://up.tietuku.com/";
    private Context context;
    private HttpClient httpClient;

    public PostImage(Context context) {
        this.context = context;
        httpClient = ((MyAppContext) this.context.getApplicationContext()).getHttpClient();
    }

    //post请求 将手机本地图片上传到贴图库，返回图片URL
    public String doUpload(File f, String token) {
        HttpPost httpPost = new HttpPost(POST_URL);

        FileBody pic = new FileBody(f);
        MultipartEntity multipartEntity = new MultipartEntity(); //与UrlEncodedFormEntity均继承HttpEnity，此类更适合文件上传
        try {
            multipartEntity.addPart("file", pic);
            multipartEntity.addPart("Token", new StringBody(((MyAppContext) this.context.getApplicationContext())
                    .getDisplayToken()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            httpPost.setEntity(multipartEntity);
            HttpResponse response = httpClient.execute(httpPost);
            //  int responseCode=response.getStatusLine().getStatusCode();  获取错误码
            HttpEntity responseEntity = response.getEntity();
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            for (line = in.readLine(); line != null; line = in.readLine()) {
                stringBuffer.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            String linkurl = jsonObject.getString("linkurl");
            return linkurl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "wrong";
    }
}
