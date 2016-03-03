package nju.tb.atys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.net.Login;

public class LoginActivity extends Activity {
    private EditText phoneNumberEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView toRegister;

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
    }

    @Override
    public void onStart() {
        super.onStart();
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
    public void onResume() {
        super.onResume();

        loginButton.setOnClickListener(new LoginButtonClickListener());
        loginButton.setClickable(false);

        Bundle loadingReturn = getIntent().getBundleExtra("LoadingActivity");
        if (loadingReturn != null && loadingReturn.getString("state").equals("fail")) {
            passwordEditText.setText("");
            phoneNumberEditText.setText(loadingReturn.getString("phone"));
            Toast.makeText(this, "账号或密码错误，请重新登录", Toast.LENGTH_SHORT).show();
        }

        Bundle netReturn = getIntent().getBundleExtra("LoadingActivityNet");
        if (netReturn != null) {
            Toast.makeText(this, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
        }
    }

    class LoginButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
//            if (!MyAppContext.getIsConnected()) {
//                Toast.makeText(LoginActivity.this, "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show();
//                return;
//            }
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
