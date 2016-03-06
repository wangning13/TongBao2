package nju.tb.net;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nju.tb.Commen.MyAppContext;

@SuppressWarnings("deprecation")
public class HttpImage {
    private final String POST_URL = "http://120.27.112.9:8080/tongbao/user/uploadPicture";
    private Context context;
    private HttpClient httpClient;

    public HttpImage(Context context) {
        this.context = context;
        httpClient = ((MyAppContext) this.context.getApplicationContext()).getHttpClient();
    }

    //post请求 将手机本地图片上传，返回图片URL
    public String doUpload(File f) {
        if (!MyAppContext.getIsConnected()) {
            return "netwrong";
        }

        HttpPost httpPost = new HttpPost(POST_URL);

        FileBody pic = new FileBody(f);
        MultipartEntity multipartEntity = new MultipartEntity(); //与UrlEncodedFormEntity均继承HttpEnity，此类更适合文件上传
        multipartEntity.addPart("file", pic);
        try {
            httpPost.setEntity(multipartEntity);
            if (!MyAppContext.getIsConnected()) {
                return "netwrong";
            }
            HttpResponse response = httpClient.execute(httpPost);
            while (response == null) {
                if (!MyAppContext.getIsConnected()) {
                    return "netwrong";
                }
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            for (line = in.readLine(); line != null; line = in.readLine()) {
                stringBuffer.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            JSONObject data = jsonObject.getJSONObject("data");
            String linkurl = data.getString("url");
            if (linkurl.equals("")) {
                return "wrong";
            }
            return linkurl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "wrong";
    }


    //根据图片URL，获取图片的bitmap对象
    public Bitmap getHttpBitmap(String url) {
        if (!MyAppContext.getIsConnected()) {
            return null;
        }
        URL bitmapUrl = null;
        Bitmap bitmap = null;
        try {
            bitmapUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) bitmapUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            InputStream in = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            return bitmap;
        }
    }

}
