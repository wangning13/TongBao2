package nju.tb.atys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.net.Recharge;

public class RechargeActivity extends Activity {
    private CheckBox zhifubaoCheckBox; //支付宝支付
    private CheckBox weixinCheckBox;  //微信支付
    private EditText withdrawMoney; //金额
    private Button withdrawOk; //确认

    private TextView toolbar_text;

    private ProgressDialog progressDialog;
    private MyAppContext myAppContext;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                progressDialog.dismiss();
                Toast.makeText(RechargeActivity.this, "充值失败，请重新操作", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                progressDialog.dismiss();
                Intent intent = new Intent(RechargeActivity.this, UserWalletActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        toolbar_text = (TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("钱包");

        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RechargeActivity.this.finish();
            }
        });
        //init
        zhifubaoCheckBox = (CheckBox) findViewById(R.id.zhifubao);
        weixinCheckBox = (CheckBox) findViewById(R.id.weixin);
        withdrawMoney = (EditText) findViewById(R.id.withdrawamoney);
        withdrawOk = (Button) findViewById(R.id.withdraw_ok);
        progressDialog = new ProgressDialog(this);
        setProgressDialog(progressDialog);
        myAppContext = (MyAppContext) getApplicationContext();

        zhifubaoCheckBox.setOnCheckedChangeListener(new CheckBoxOnSelectListener());
        weixinCheckBox.setOnCheckedChangeListener(new CheckBoxOnSelectListener());
    }

    @Override
    public void onStart() {
        super.onStart();

        withdrawOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(withdrawMoney.getText().toString())) {
                    Toast.makeText(RechargeActivity.this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                int money = Integer.parseInt(withdrawMoney.getText().toString());
                progressDialog.show();
                new Recharge(RechargeActivity.this, myAppContext.getToken(), money, handler).start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
    class CheckBoxOnSelectListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.zhifubao:
                    if (weixinCheckBox.isChecked()) {
                        weixinCheckBox.setChecked(false);
                    }
                    if (isChecked) {
                        zhifubaoCheckBox.setChecked(true);
                    } else {
                        zhifubaoCheckBox.setChecked(false);
                    }

                    break;
                case R.id.weixin:
                    if (zhifubaoCheckBox.isChecked()) {
                        zhifubaoCheckBox.setChecked(false);
                    }
                    if (isChecked) {
                        weixinCheckBox.setChecked(true);
                    } else {
                        weixinCheckBox.setChecked(false);
                    }

                    break;
            }
        }
    }

    public static void setProgressDialog(ProgressDialog progressDialog) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("请稍等。。。");
        progressDialog.setCancelable(false);
    }

}
