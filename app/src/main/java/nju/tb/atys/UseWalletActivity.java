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

import nju.tb.entity.Order;
import nju.tb.R;

public class UseWalletActivity extends Activity {

    private TextView toolbar_text;
    private ListView lv;
    private ArrayAdapter<Order> adapter;
    private List<String> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_wallet);

        toolbar_text=(TextView) findViewById(R.id.toolbar_title);
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
                Intent i = new Intent(UseWalletActivity.this,RechargeActivity.class);
                startActivity(i);
            }
        });
        Button withdrawbtn = (Button) findViewById(R.id.withdraw_btn);
        withdrawbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(UseWalletActivity.this,WithdrawActivity.class);
                startActivity(i);
            }
        });
        lv=(ListView)findViewById(R.id.consumelistView);
        mData = getData();
        MyAdapter adapter = new MyAdapter(this);
        lv.setAdapter(adapter);

    }




    public List<String> getData() {
        List<String> list = new ArrayList<String>();
        String data=    "充值   +400"+"\n" +
                        "2016-01-08"+"\n"+
                        "余额：2333.33"+"\n";
        list.add(data);

        data=    "消费   -200"+"\n" +
                "2016-01-06"+"\n"+
                "余额：1933.33"+"\n";
        list.add(data);

        data=    "充值   +200"+"\n" +
                "2016-01-03"+"\n"+
                "余额：2133.33"+"\n";
        list.add(data);

        data=    "充值   +800"+"\n" +
                "2015-12-30"+"\n"+
                "余额：1933.33"+"\n";
        list.add(data);


        data=    "消费   -200"+"\n" +
                "2015-12-28"+"\n"+
                "余额：1133.33"+"\n";
        list.add(data);

        data=    "充值   +500"+"\n" +
                "2015-12-26"+"\n"+
                "余额：1333.33"+"\n";
        list.add(data);
        return list;
    }


    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return mData.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView info = null;
            convertView = mInflater.inflate(R.layout.consumptionitem, null);
            info = (TextView)convertView.findViewById(R.id.info);
            info.setText(mData.get(position));
            return convertView;
        }

    }
}
