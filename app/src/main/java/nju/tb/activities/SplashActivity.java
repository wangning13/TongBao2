package nju.tb.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import nju.tb.Commen.Common;
import nju.tb.R;


/**
 * Created by motoon on 2017/3/28.
 */

public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGHT = 1000; // 延迟

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SplashActivity","init");
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPreferences = getSharedPreferences(Common.USER_INFO, Context.MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean(Common.IS_LOGIN,false);
        if (isLogin){
            Log.d("SplashActivity","已经登录");
            //默认是自动登录状态，直接跳转
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }

            }, SPLASH_DISPLAY_LENGHT);
        }else {
            Log.d("SplashActivity","未登录");
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Log.d("SplashActivity","转~");
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGHT);
        }
    }
}
