package nju.tb.atys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.entity.Order;
import nju.tb.net.CancelOrder;
import nju.tb.net.GetOrderDetail;
import nju.tb.net.ScrambleOrder;

public class GrabOrderContentActivity extends AppCompatActivity {
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
    private Button grabbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grab_order_content);

        //toolbar的标题
        toolbar_text=(TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("订单详情");

        //回退按钮
        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(GrabOrderContentActivity.this,ScrambleOrderActivity.class);
                startActivity(intent);
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
        trucktype_text=(TextView) findViewById(R.id.trucktype);
        money_text=(TextView) findViewById(R.id.money);
        grabbtn=(Button) findViewById(R.id.grab_btn);

        Bundle bundle=getIntent().getExtras();
        final Order order=(Order)bundle.get("order");
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
//
        grabbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String USERTOKEN = ((MyAppContext) getApplicationContext()).getToken();
                ScrambleOrder go = new ScrambleOrder(GrabOrderContentActivity.this, USERTOKEN, order.getId());
                go.start();
                while (!go.runover) {
                }
                if (go.getResult() ==0) {
                    Toast.makeText(GrabOrderContentActivity.this, go.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
                if (go.getResult() == -1 && MyAppContext.getIsConnected() == false) {
                    Toast.makeText(GrabOrderContentActivity.this, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
                }
                if (go.getResult() ==1) {
                    new AlertDialog.Builder(GrabOrderContentActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("抢单成功！")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    grabbtn.setVisibility(View.GONE);
                                }
                            }).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}