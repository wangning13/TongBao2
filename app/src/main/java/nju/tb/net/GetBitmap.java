package nju.tb.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import nju.tb.Commen.MyAppContext;

/**
 * Created by zhuchen on 2016/3/6.
 */
public class GetBitmap {
    //根据图片URL，获取图片的bitmap对象
    public static Bitmap getHttpBitmap(String url) {
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
