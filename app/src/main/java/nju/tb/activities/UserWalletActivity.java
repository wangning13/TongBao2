package nju.tb.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nju.tb.Adapters.UseWalletAdapter;
import nju.tb.Commen.MyAppContext;
import nju.tb.entity.Account;
import nju.tb.entity.Order;
import nju.tb.R;
import nju.tb.net.ShowAccount;

public class UserWalletActivity extends Activity {

    private TextView toolbar_text;
    private ListView lv;
    private ArrayAdapter<Order> adapter;
    private List<List<String>> mData;
    private Button rechargebtn;
    private Button withdrawbtn;
    //余额
    private TextView balanceTextView;
    private MyAppContext myAppContext;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //刷新明细listview
            List<Account> accountList = myAppContext.getAccountList();
            mData = getData(accountList);
            UseWalletAdapter adapter = new UseWalletAdapter(UserWalletActivity.this, mData);
            lv.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_wallet);

        toolbar_text = (TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("钱包");

        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(UserWalletActivity.this, MainActivity.class);
                startActivity(intent);
                UserWalletActivity.this.finish();
            }
        });

        //余额
        balanceTextView = (TextView) findViewById(R.id.text_balance);
        //上下文
        myAppContext = (MyAppContext) getApplication();

        //充值
        rechargebtn = (Button) findViewById(R.id.recharge_btn);
        rechargebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(UserWalletActivity.this, RechargeActivity.class);
                startActivity(i);
            }
        });
        //提现
        withdrawbtn = (Button) findViewById(R.id.withdraw_btn);
        withdrawbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(UserWalletActivity.this, WithdrawActivity.class);
                startActivity(i);
            }
        });
        //明细
        lv = (ListView) findViewById(R.id.consumelistView);

        findViewById(R.id.bill).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(UserWalletActivity.this, BillActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        int balance = myAppContext.getMoney();
        balanceTextView.setText(String.valueOf(balance));

        //获取账单列表
        new ShowAccount(this, myAppContext.getToken(), handler).start();
    }


//    public List<List<String>> getData() {
//        List<List<String>> list = new ArrayList<List<String>>();
//        List<String> item = new ArrayList<String>();
//        item.add("充值");
//        item.add("+400");
//        item.add("2015-06-09");
//        item.add("2333");
//        list.add(item);
//        item = new ArrayList<String>();
//        item.add("消费");
//        item.add("-400");
//        item.add("2015-06-01");
//        item.add("1933");
//        list.add(item);
//        return list;
//    }

    /**
     * 解析accountlist，并获取listview的data
     *
     * @param list
     * @return
     */
    private List<List<String>> getData(List<Account> list) {
        if (list == null) {
            return null;
        }
        List<List<String>> returnList = new ArrayList<>();

        for (Account account : list) {
            List<String> temp = new ArrayList<>();
            int type = account.getType(); //type
            temp.add(convertType(type));
            int money = account.getMoney(); //money
            String moneyString = "";
            if (type == 0 || type == 4) {
                moneyString = "+" + money;
            } else {
                moneyString = "-" + money;
            }
            temp.add(moneyString);
            temp.add(account.getTime()); //time
            if (type == 0 || type == 1) {
                temp.add("");            //detail
                returnList.add(temp);
                continue;
            }
            temp.add(account.getAddressFrom() + "--" + account.getAddressTo());
            returnList.add(temp);
        }

        return returnList;
    }

    /**
     * 根据type转换成String表示
     *
     * @param type
     * @return
     */
    public static String convertType(int type) {
        String s = "";
        switch (type) {
            case 0:
                s = "充值";
                break;
            case 1:
                s = "提现";
                break;
            case 2:
                s = "支付";
                break;
            case 3:
                s = "退款";
                break;
            case 4:
                s = "到账";
                break;
        }

        return s;
    }

}
