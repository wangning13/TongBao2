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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.net.HasRegister;
import nju.tb.net.Register;

public class RegisterActivity extends Activity {
    private TextView textView_step1, textView_step2, textView_step3;
    private EditText phoneNumberEditText;
    private Button sendAuthcodeButton;
    private EditText enterAuthcodeEditText;
    private Button resendAuthcodeButton;
    private Button sumbitAuthcodeButton;
    private EditText enterPasswordEditText;
    private EditText verifyPasswordEditText;
    private Button submitPasswordButton;
    private RelativeLayout registerBack;

    private int time;
    private String phone; //edittext中填的手机号
    private boolean phoneNubmerJudge = false; //判断手机号是否合格
    private boolean authcodeJudge = false; //判断验证码填写是否合格
    private boolean enterPasswordJudge = false; //判断密码是否合格
    private boolean verifyPasswordJudge = false; //判断确认密码是否合格
    private String nowStep = "step1";  //目前处于哪一步 step1 step2 step3


    private Handler registerActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    if (!nowStep.equals("step2")) {
                        break;
                    }
                    resendAuthcodeButton.setClickable(false);
                    resendAuthcodeButton.setBackgroundResource(R.color.colorGray);
                    resendAuthcodeButton.setText(time + "s之后重新发送");
                    break;
                case -2:
                    if (!nowStep.equals("step2")) {
                        break;
                    }
                    resendAuthcodeButton.setBackgroundResource(R.color.colorGreen);
                    resendAuthcodeButton.setClickable(true);
                    resendAuthcodeButton.setText("点击重新发送");
                    break;
                case 0:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object object = msg.obj;
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //验证码已经发送
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        }
                        //验证码已经提交验证
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                            HashMap<String, Object> map = (HashMap) object;   //keyset()  key:phone,country
//                            SMSSDKReturnPhone = map.get("phone").toString();
//                            SMSSDKReturnCountry = (int) map.get("country");
                            //进入step3
                            enterAuthcodeEditText.setVisibility(View.GONE);
                            resendAuthcodeButton.setVisibility(View.GONE);
                            sumbitAuthcodeButton.setVisibility(View.GONE);
                            enterPasswordEditText.setVisibility(View.VISIBLE);
                            verifyPasswordEditText.setVisibility(View.VISIBLE);
                            submitPasswordButton.setVisibility(View.VISIBLE);
                            textView_step2.setTextColor(getResources().getColor(R.color.colorBlack));
                            textView_step3.setTextColor(getResources().getColor(R.color.stepSelectedGreen));
                            nowStep = "step3";

                        }
                    } else if (result == SMSSDK.RESULT_ERROR) {
                        Toast.makeText(RegisterActivity.this, "校验码错误，请稍后重新获取验证码", Toast.LENGTH_SHORT).show();

                    }
                    break;
                case 1: //sendAuthcodeButton不可用
                    sendAuthcodeButton.setBackgroundResource(R.color.colorGray);
                    sendAuthcodeButton.setClickable(false);
                    break;
                case 2: //sendAuthcodeButton可用
                    sendAuthcodeButton.setBackgroundResource(R.color.colorGreen);
                    sendAuthcodeButton.setClickable(true);
                    break;
                case 3: //submitAuthcodeButton不可用
                    sumbitAuthcodeButton.setBackgroundResource(R.color.colorGray);
                    sumbitAuthcodeButton.setClickable(false);
                    break;
                case 4: //submitAuthcodeButton可用
                    sumbitAuthcodeButton.setBackgroundResource(R.color.colorGreen);
                    sumbitAuthcodeButton.setClickable(true);
                    break;
                case 5: //submitPasswordButton不可用
                    submitPasswordButton.setBackgroundResource(R.color.colorGray);
                    submitPasswordButton.setClickable(false);
                    break;
                case 6: //submitPasswordButton可用
                    submitPasswordButton.setBackgroundResource(R.color.colorGreen);
                    submitPasswordButton.setClickable(true);
                    break;
            }
        }
    };

    //初始化短信验证SDK
    public void initSDK() {
        SMSSDK.initSDK(this, "feb52eed0635", "4bceea6c65aa23b202366a293e3d36f5");
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message = registerActivityHandler.obtainMessage(0);
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                registerActivityHandler.sendMessage(message);
            }
        };
        //注册回调接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_register);
        initSDK();

        textView_step1 = (TextView) findViewById(R.id.register_step1);
        textView_step2 = (TextView) findViewById(R.id.register_step2);
        textView_step3 = (TextView) findViewById(R.id.register_step3);
        phoneNumberEditText = (EditText) findViewById(R.id.et_register_phonenumber);
        sendAuthcodeButton = (Button) findViewById(R.id.register_sendyanzhengma_button);
        enterAuthcodeEditText = (EditText) findViewById(R.id.et_register_yanzhengma);
        resendAuthcodeButton = (Button) findViewById(R.id.register_resend_button);
        sumbitAuthcodeButton = (Button) findViewById(R.id.register_submityanzhengma_button);
        enterPasswordEditText = (EditText) findViewById(R.id.et_register_enterpassword);
        verifyPasswordEditText = (EditText) findViewById(R.id.et_register_verifypassword);
        submitPasswordButton = (Button) findViewById(R.id.register_submitpassword_button);
        registerBack = (RelativeLayout) findViewById(R.id.register_leftarrow);

        //step1的提交按钮的判断
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!MyAppContext.isLogIn() && nowStep.equals("step1")) {
                    if (phoneNubmerJudge && !sendAuthcodeButton.isClickable()) {
                        Message message = registerActivityHandler.obtainMessage(2);
                        registerActivityHandler.sendMessage(message);
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!MyAppContext.isLogIn() && !nowStep.equals("step3")) {
                    if (authcodeJudge && !sumbitAuthcodeButton.isClickable()) {
                        Message message = registerActivityHandler.obtainMessage(4);
                        registerActivityHandler.sendMessage(message);
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!MyAppContext.isLogIn() && !nowStep.equals("over")) {
                    if (enterPasswordJudge && verifyPasswordJudge && !submitPasswordButton.isClickable()) {
                        Message message = registerActivityHandler.obtainMessage(6);
                        registerActivityHandler.sendMessage(message);
                    }
                }
            }
        }).start();

    }

    @Override
    public void onStart() {
        super.onStart();

        phoneNumberEditText.addTextChangedListener(new RegisterEditTextWatcherStep1(11, 11, phoneNumberEditText));
        sendAuthcodeButton.setOnClickListener(new RegisterAcitivityButtonOnclickListener());
        sendAuthcodeButton.setClickable(false);

        enterAuthcodeEditText.addTextChangedListener(new RegisterEditTextWatcherStep2(4, 4, enterAuthcodeEditText));
        resendAuthcodeButton.setOnClickListener(new RegisterAcitivityButtonOnclickListener());
        resendAuthcodeButton.setClickable(false);
        sumbitAuthcodeButton.setOnClickListener(new RegisterAcitivityButtonOnclickListener());
        sumbitAuthcodeButton.setClickable(false);

        enterPasswordEditText.addTextChangedListener(new RegisterEditTextWatcherStep3(255, 8, enterPasswordEditText));
        verifyPasswordEditText.addTextChangedListener(new RegisterEditTextWatcherStep3(255, 8, verifyPasswordEditText));
        submitPasswordButton.setOnClickListener(new RegisterAcitivityButtonOnclickListener());
        submitPasswordButton.setClickable(false);

        registerBack.setOnClickListener(new RegisterAcitivityButtonOnclickListener());

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    class RegisterAcitivityButtonOnclickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!MyAppContext.getIsConnected()) {
                Toast.makeText(RegisterActivity.this, "网络连接不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (v.getId()) {
                case R.id.register_sendyanzhengma_button:   //step1发送验证码按钮
                    phone = phoneNumberEditText.getText().toString();
                    if (!AddCarActivity.isMobileNum(phone)) {
                        Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    HasRegister hasRegister = new HasRegister(RegisterActivity.this, phone);
                    hasRegister.start();
                    while (hasRegister.getResult() == -1) {
                        if (!MyAppContext.getIsConnected()) {
                            return;
                        }
                    }
                    if (hasRegister.getResult() == 1) {
                        Toast.makeText(RegisterActivity.this, "该手机号已被注册", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //发送短信
                    SMSSDK.getVerificationCode("86", phone);

                    phoneNumberEditText.setVisibility(View.GONE);
                    sendAuthcodeButton.setVisibility(View.GONE);
                    enterAuthcodeEditText.setVisibility(View.VISIBLE);
                    resendAuthcodeButton.setVisibility(View.VISIBLE);
                    sumbitAuthcodeButton.setVisibility(View.VISIBLE);
                    textView_step1.setTextColor(getResources().getColor(R.color.colorBlack));
                    textView_step2.setTextColor(getResources().getColor(R.color.stepSelectedGreen));
                    nowStep = "step2";
                    new Thread(new TimeThread()).start();

                    break;

                case R.id.register_resend_button:  //step2重新发送按钮
                    SMSSDK.getVerificationCode("86", phone);
                    new Thread(new TimeThread()).start();
                    break;

                case R.id.register_submityanzhengma_button:  //step2提交验证码按钮
                    String authcode = enterAuthcodeEditText.getText().toString();
                    SMSSDK.submitVerificationCode("86", phone, authcode);
                    break;

                case R.id.register_submitpassword_button: //step3提交密码按钮
                    String enterPassword = enterPasswordEditText.getText().toString();
                    String verifyPassword = verifyPasswordEditText.getText().toString();
                    if (!enterPassword.equals(verifyPassword)) {
                        Toast.makeText(RegisterActivity.this, "两次输入密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Register register = new Register(RegisterActivity.this, phone, verifyPassword);
                    register.start();
                    while (register.getResult() == -1) {
                        if (!MyAppContext.getIsConnected()) {
                            return;
                        }
                    }
                    if (register.getResult() == 0) {
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            nowStep = "over";
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            RegisterActivity.this.finish();
                        }
                    }, 1500);
                    break;
                case R.id.register_leftarrow: //返回
                    nowStep = "over";
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                    break;
            }

        }
    }

    //倒计时的线程
    class TimeThread implements Runnable {

        @Override
        public void run() {
            for (time = 70; time > 0; time--) {
                Message message = registerActivityHandler.obtainMessage(-1);
                registerActivityHandler.sendMessage(message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = registerActivityHandler.obtainMessage(-2);
            registerActivityHandler.sendMessage(message);
        }
    }

    class RegisterEditTextWatcherStep1 implements TextWatcher {
        private int maxLength = -1;
        private int minLength = -1;
        private EditText editText = null;

        public RegisterEditTextWatcherStep1(int maxLength, int minLength, EditText editText) {
            this.maxLength = maxLength;
            this.editText = editText;
            this.minLength = minLength;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Editable editable = editText.getText();
            int length = editable.length();
            if (length >= minLength && length <= maxLength) {
                phoneNubmerJudge = true;
            } else {
                phoneNubmerJudge = false;
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

            if (length < minLength && sendAuthcodeButton.isClickable()) {
                Message message = registerActivityHandler.obtainMessage(1);
                registerActivityHandler.sendMessage(message);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class RegisterEditTextWatcherStep2 implements TextWatcher {
        private int maxLength = -1;
        private int minLength = -1;
        private EditText editText = null;

        public RegisterEditTextWatcherStep2(int maxLength, int minLength, EditText editText) {
            this.maxLength = maxLength;
            this.editText = editText;
            this.minLength = minLength;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Editable editable = editText.getText();
            int length = editable.length();
            if (length >= minLength && length <= maxLength) {
                authcodeJudge = true;
            } else {
                authcodeJudge = false;
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
            if (length < minLength && sendAuthcodeButton.isClickable()) {
                Message message = registerActivityHandler.obtainMessage(3);
                registerActivityHandler.sendMessage(message);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    class RegisterEditTextWatcherStep3 implements TextWatcher {
        private int maxLength = -1;
        private int minLength = -1;
        private EditText editText = null;

        public RegisterEditTextWatcherStep3(int maxLength, int minLength, EditText editText) {
            this.maxLength = maxLength;
            this.editText = editText;
            this.minLength = minLength;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Editable editable = editText.getText();
            int length = editable.length();
            if (length >= minLength && length <= maxLength) {
                if (editText.getId() == R.id.et_register_enterpassword) {
                    enterPasswordJudge = true;
                } else if (editText.getId() == R.id.et_register_verifypassword) {
                    verifyPasswordJudge = true;
                }
            } else {
                if (editText.getId() == R.id.et_register_enterpassword) {
                    enterPasswordJudge = false;
                } else if (editText.getId() == R.id.et_register_verifypassword) {
                    verifyPasswordJudge = false;
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
            if (length < minLength && submitPasswordButton.isClickable()) {
                Message message = registerActivityHandler.obtainMessage(5);
                registerActivityHandler.sendMessage(message);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
