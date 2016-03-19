package nju.tb.atys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nju.tb.Adapters.UseWalletAdapter;
import nju.tb.entity.Order;
import nju.tb.R;

public class UseWalletActivity extends Activity {

    private TextView toolbar_text;
    private ListView lv;
    private ArrayAdapter<Order> adapter;
    private  List<List<String>> mData;
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
                UseWalletActivity.this.finish();
            }
        });

        Button rechargebtn = (Button) findViewById(R.id.recharge_btn);
        rechargebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(UseWalletActivity.this, RechargeActivity.class);
                startActivity(i);
            }
        });
        Button withdrawbtn = (Button) findViewById(R.id.withdraw_btn);
        withdrawbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(UseWalletActivity.this, WithdrawActivity.class);
                startActivity(i);
            }
        });
        lv = (ListView) findViewById(R.id.consumelistView);
        mData = getData();
        UseWalletAdapter adapter = new UseWalletAdapter(this, mData);
        lv.setAdapter(adapter);

        findViewById(R.id.bill).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(UseWalletActivity.this, BillActivity.class);
                startActivity(intent);

            }
        });
    }




    public  List<List<String>> getData() {
        List<List<String>> list = new  ArrayList<List<String>>();
        List<String> item= new  ArrayList<String>();
        item.add("充值");
        item.add("+400");
        item.add("2015-06-09");
        item.add("2333");
        list.add(item);
        item= new  ArrayList<String>();
        item.add("消费");
        item.add("-400");
        item.add("2015-06-01");
        item.add("1933");
        list.add(item);
        return list;
    }


}
