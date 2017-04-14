package nju.tb.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import nju.tb.Commen.Common;
import nju.tb.R;
import nju.tb.net.Login;

public class LoginActivity extends Activity {
    private EditText phoneNumberEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView toRegister;
    private CheckBox keepPassword;

    private static boolean autoLogin = false;
    private boolean changeLogin = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LoginActivity.class.getName(),"init");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_login);
        SharedPreferences sharedPreferences = getSharedPreferences(Common.USER_INFO, Context.MODE_PRIVATE);
        phoneNumberEditText = (EditText) findViewById(R.id.et_login_phonenumber);
        phoneNumberEditText.setText(sharedPreferences.getString(Common.PHONE_NUMBER,""));
        passwordEditText = (EditText) findViewById(R.id.et_login_password);
        passwordEditText.setText(sharedPreferences.getString(Common.PWD,""));
        loginButton = (Button) findViewById(R.id.login_button);
        toRegister = (TextView) findViewById(R.id.login_toregister);




    }

    @Override
    public void onStart() {
        super.onStart();

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginToRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(loginToRegister);
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
//        LoginActivity.this.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        JPushInterface.onResume(this);


        loginButton.setOnClickListener(new LoginButtonClickListener());
//        if (!autoLogin) {
//            loginButton.setClickable(false);
//        }

        Bundle loadingReturn = getIntent().getBundleExtra("LoadingActivity");
        if (loadingReturn != null && loadingReturn.getString("state").equals("fail")) {
            passwordEditText.setText("");
            phoneNumberEditText.setText(loadingReturn.getString("phone"));
            autoLogin = false;
            Toast.makeText(this, "账号或密码错误，请重新登录", Toast.LENGTH_SHORT).show();
        }

        Bundle netReturn = getIntent().getBundleExtra("LoadingActivityNet");
        if (netReturn != null) {
            Toast.makeText(this, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

//        if (changeLogin) {
//            SharedPreferences loginSettings = getSharedPreferences("loginSettings", 0);
//            loginSettings.edit().clear().commit();
//            autoLogin=false;
//        }
//        if (keepPassword.isChecked()) {
//            SharedPreferences loginSettings = getSharedPreferences("loginSettings", 0);
//            if (MyAppContext.isLogIn() && autoLogin == false) {
//                loginSettings.edit().putString("phone", phoneNumberEditText.getText().toString()).putString("password",
//                        passwordEditText.getText().toString()).commit();
//            }
//        } else {
//            SharedPreferences loginSettings = getSharedPreferences("loginSettings", 0);
//            if (autoLogin == true) {
//                loginSettings.edit().clear().commit();
//            }
//        }
//
//        this.finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
        }
        return  super.onKeyDown(keyCode, event);

    }
    class LoginButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String phoneNumber = phoneNumberEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            Login login = new Login(LoginActivity.this, phoneNumber, password);
            login.start();

            Intent loadingIntent = new Intent(LoginActivity.this, LoadingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("phone", phoneNumber);
            loadingIntent.putExtra("setPhone", bundle);
            startActivity(loadingIntent);
            finish();

        }
    }

}
