package nju.tb.atys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.entity.Order;
import nju.tb.net.GetOrderDetail;
import nju.tb.net.RemoveOrder;

/**
 * Created by Administrator on 2016/3/5.
 */
public class OldOrderContentActivity extends Activity {
    private TextView fname1_text;
    private TextView number_text;
    private TextView time_text;
    private TextView faddress_text;
    private TextView fname_text;
    private TextView fphone_text;
    private TextView taddress_text;
    private TextView tname_text;
    private TextView tphone_text;
    private TextView loadtime_text;
    private TextView trucktype_text;
    private TextView money_text;
    private TextView toolbar_text;
    private TextView state_text;
    private Button deletebtn;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oldordercontent);


        //toolbar的标题
        toolbar_text=(TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("订单详情");

        //回退按钮
        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OldOrderContentActivity.this.finish();
            }
        });
        fname1_text=(TextView) findViewById(R.id.fname1);
        number_text=(TextView) findViewById(R.id.number);
        time_text=(TextView) findViewById(R.id.time);
        faddress_text=(TextView) findViewById(R.id.faddress);
        fname_text=(TextView) findViewById(R.id.fname);
        fphone_text=(TextView) findViewById(R.id.fphone);
        taddress_text=(TextView) findViewById(R.id.taddress);
        tname_text=(TextView) findViewById(R.id.tname);
        tphone_text=(TextView) findViewById(R.id.tphone);
        loadtime_text=(TextView) findViewById(R.id.loadtime);
        state_text=(TextView) findViewById(R.id.state);
        taddress_text=(TextView) findViewById(R.id.taddress);
        trucktype_text=(TextView) findViewById(R.id.trucktype);
        money_text=(TextView) findViewById(R.id.money);
        deletebtn=(Button) findViewById(R.id.delete_btn);

//
        Bundle bundle=getIntent().getExtras();
        final int orderNumber=(int)bundle.get("orderNumber");

        final String USERTOKEN = ((MyAppContext)getApplicationContext()).getToken();
        GetOrderDetail god=new GetOrderDetail( OldOrderContentActivity.this,USERTOKEN,orderNumber+"");
        god.start();
        while (god.getResult() == -1) {
            if (!MyAppContext.getIsConnected()) {
                Log.i("断网了", "断网了");
                break;

            }
        }
        while(!god.isRunover()){
        }

        if (god.getResult() == -2) {
            Toast.makeText(OldOrderContentActivity.this, "订单信息有误", Toast.LENGTH_SHORT).show();
        }

        if (god.getResult() == 1) {
            Order order = god.getOrder();
            fname1_text.setText(order.getFromContactName());
            number_text.setText(order.getId()+"");
            time_text.setText(order.getTime());
            faddress_text.setText(order.getAddressFrom());
            fname_text.setText(order.getFromContactName());
            fphone_text.setText(order.getFromContactPhone());
            taddress_text.setText(order.getAddressTo());
            tname_text.setText(order.getToContactName());
            tphone_text.setText(order.getToContactPhone());
            loadtime_text.setText(order.getLoadTime());
            taddress_text.setText(order.getAddressTo());
            trucktype_text.setText(order.getTruckTypes());
            money_text.setText(order.getMoney());
            state_text.setText(order.getState());
        }




        deletebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String USERTOKEN = ((MyAppContext) getApplicationContext()).getToken();
                RemoveOrder dorder = new RemoveOrder(OldOrderContentActivity.this, USERTOKEN, orderNumber);
                dorder.start();
                while (!dorder.runover) {

                }
                if (dorder.getResult() ==0) {
                    Toast.makeText(OldOrderContentActivity.this, dorder.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    OldOrderContentActivity.this.finish();
                }
                if (dorder.getResult() == -1 && MyAppContext.getIsConnected() == false) {
                    Toast.makeText(OldOrderContentActivity.this, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
                    OldOrderContentActivity.this.finish();
                }
                if (dorder.getResult() ==1) {
                    Intent intent = new Intent(OldOrderContentActivity.this, MainActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }
            }
        });
    }
}
