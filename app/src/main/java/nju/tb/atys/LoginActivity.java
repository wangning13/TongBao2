package nju.tb.atys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import cn.jpush.android.api.JPushInterface;
import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.net.GetAllTruckTypes;
import nju.tb.net.Login;

public class LoginActivity extends Activity {
    private EditText phoneNumberEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView toRegister;
    private CheckBox keepPassword;

    private static boolean autoLogin = false;
    private boolean changeLogin = false;

    private boolean phoneNumberOk = false;
    private boolean passwordOk = false;

    Handler loginOkButtonHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                loginButton.setBackgroundResource(R.color.colorGreen);
                loginButton.setClickable(true);
            } else if (msg.what == 1) {
                loginButton.setBackgroundResource(R.color.colorGray);
                loginButton.setClickable(false);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_login);

        phoneNumberEditText = (EditText) findViewById(R.id.et_login_phonenumber);
        passwordEditText = (EditText) findViewById(R.id.et_login_password);
        loginButton = (Button) findViewById(R.id.login_button);
        toRegister = (TextView) findViewById(R.id.login_toregister);
        keepPassword = (CheckBox) findViewById(R.id.login_keeppassword);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.WAKE_LOCK
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.INTERNET
                , Manifest.permission.GET_ACCOUNTS
                , Manifest.permission.ACCESS_NETWORK_STATE
                , Manifest.permission.INTERNET
                , Manifest.permission.CHANGE_WIFI_STATE
                , Manifest.permission.ACCESS_WIFI_STATE
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.BROADCAST_STICKY
                , Manifest.permission.WRITE_SETTINGS
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
        }, 123);



    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences settings = getSharedPreferences("loginSettings", 0);
        String phone1 = settings.getString("phone", "");
        String password1 = settings.getString("password", "");

        if (settings != null && !phone1.equals("") && !password1.equals("")) {
            autoLogin = true;
            phoneNumberEditText.setText(phone1);
            passwordEditText.setText(password1);
            loginButton.setBackgroundResource(R.color.colorGreen);
            loginButton.setClickable(true);

        } else {
            autoLogin = false;
        }


        if (phoneNumberEditText.getText().length() == 11) {
            phoneNumberOk = true;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!MyAppContext.isLogIn()) {
                    if (passwordOk && phoneNumberOk && !loginButton.isClickable()) {
                        Message message = loginOkButtonHandler.obtainMessage(0);
                        loginOkButtonHandler.sendMessage(message);
                    }
                }
            }
        }).start();


        phoneNumberEditText.addTextChangedListener(new LoginEditTextViewWatcher(11, 11, phoneNumberEditText));
        passwordEditText.addTextChangedListener(new LoginEditTextViewWatcher(8, 255, passwordEditText));

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
        if (!autoLogin) {
            loginButton.setClickable(false);
        }

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

        if (changeLogin) {
            SharedPreferences loginSettings = getSharedPreferences("loginSettings", 0);
            loginSettings.edit().clear().commit();
            autoLogin=false;
        }
        if (keepPassword.isChecked()) {
            SharedPreferences loginSettings = getSharedPreferences("loginSettings", 0);
            if (MyAppContext.isLogIn() && autoLogin == false) {
                loginSettings.edit().putString("phone", phoneNumberEditText.getText().toString()).putString("password",
                        passwordEditText.getText().toString()).commit();
            }
        } else {
            SharedPreferences loginSettings = getSharedPreferences("loginSettings", 0);
            if (autoLogin == true) {
                loginSettings.edit().clear().commit();
            }
        }

        this.finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return  true;
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


        }
    }

    class LoginEditTextViewWatcher implements TextWatcher {
        private int minLength = -1;
        private int maxLength = -1;
        private EditText editText = null;

        public LoginEditTextViewWatcher(int minLength, int maxLength, EditText editText) {
            this.minLength = minLength;
            this.maxLength = maxLength;
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            changeLogin = true;
            Editable editable = editText.getText();
            int length = editable.length();

            if (length >= minLength && length <= maxLength) {
                if (editText == passwordEditText) {
                    passwordOk = true;
                } else {
                    phoneNumberOk = true;
                }
            } else {
                if (editText == passwordEditText) {
                    passwordOk = false;
                } else {
                    phoneNumberOk = false;
                }
            }


            if (length > maxLength) {
                int selEndIndex = Selection.getSelectionEnd(editable);
                String str = editable.toString();
                //截取新字符串
                String newStr = str.substring(0, maxLength);
                editText.setText(newStr);
                editable = editText.getText();

                //新字符串的长度
                int newLen = editable.length();
                //旧光标位置超过字符串长度
                if (selEndIndex > newLen) {
                    selEndIndex = editable.length();
                }
                //设置新光标所在的位置
                Selection.setSelection(editable, selEndIndex);

            }
            if (length < minLength && loginButton.isClickable()) {
                Message message = loginOkButtonHandler.obtainMessage(1);
                loginOkButtonHandler.sendMessage(message);

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }
}
