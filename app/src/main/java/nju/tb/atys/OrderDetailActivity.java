package nju.tb.atys;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import nju.tb.R;

public class OrderDetailActivity extends Activity {

    private TextView toolbar_text;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        toolbar_text=(TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("订单详情");

    }

}
