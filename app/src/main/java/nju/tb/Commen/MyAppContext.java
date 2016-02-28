package nju.tb.Commen;


import android.app.Application;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

@SuppressWarnings("deprecation")
public class MyAppContext extends Application {
    private boolean isLogIn;
    private static HttpClient httpClient = new DefaultHttpClient();
    private final String DRIVER_DISPLAY_PICTURE_TOKEN =
            "81f5465b9f87dcdc21c19eb29d03b59f96b449da:ZXJERjJIZTR2dzFsOXA1akdqc3ZlUldGNDZ3PQ" +
                    "==:eyJkZWFkbGluZSI6MTQ1NjYzNjc2MSwiYWN0aW9uIjoiZ2V0IiwidWlkIjoiNTUwMTA1IiwiYWlkIjoiMTIwNTU1NiIsImZyb20iOiJmaWxlIn0=";

    public void setLogIn(boolean logState) {
        this.isLogIn = logState;
    }

    public boolean isLogIn() {
        return this.isLogIn;
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public String getDisplayToken() {
        return DRIVER_DISPLAY_PICTURE_TOKEN;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isLogIn = false;
    }

}
