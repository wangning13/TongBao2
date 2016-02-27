package nju.tb.Commen;


import android.app.Application;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class MyAppContext extends Application {
    private boolean isLogIn;
    private static HttpClient httpClient = new DefaultHttpClient();

    public void setLogIn(boolean logState) {
        this.isLogIn = logState;
    }

    public boolean isLogIn() {
        return this.isLogIn;
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isLogIn = false;
    }

}
