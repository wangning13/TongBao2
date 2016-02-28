package nju.tb.net;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    private final String POST_URL = "http://up.tietuku.com/";
    private final String PIC_URL = "http://api.tietuku.com/v1/Pic";
    private Context context;
    private HttpClient httpClient;

    public HttpImage(Context context) {
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
            if (response == null) {
                return "wrong";
            }
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
        URL bitmapUrl = null;
        Bitmap bitmap = null;
        try {
            bitmapUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) bitmapUrl.openConnection();
            conn.setConnectTimeout(6000);
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
        }

        return bitmap;
    }

}
