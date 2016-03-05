package nju.tb.atys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.entity.TruckDetail;
import nju.tb.net.GetTruckDetail;

public class CarInfoActivity extends Activity {
    private TextView toolbar_text;
    private LinearLayout linearLayout;
    private ImageView stateImageView;
    private TextView nameTextView, typeTextView, truckNumTextView, capacityTextView, lengthTextView,
            phoneNumTextView, stateTextView;

    private boolean verify_OK;
    private int verify_state;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                TruckDetail truckDetail = (TruckDetail) msg.obj;
                nameTextView.setText(truckDetail.getRealName());
                typeTextView.setText(truckDetail.getTypeName());
                truckNumTextView.setText(truckDetail.getTruckNum());
                capacityTextView.setText(String.valueOf(truckDetail.getCapacity()));
                lengthTextView.setText(String.valueOf(truckDetail.getLength()));
                phoneNumTextView.setText(truckDetail.getPhoneNum());
                stateTextView.setText(CarsManagementActivity.convertState(truckDetail.getAuthState()));
                verify_state = truckDetail.getAuthState();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_personinfo);
        Bundle bundle = getIntent().getBundleExtra("truckInfo");

        MyAppContext myAppContext = (MyAppContext) getApplicationContext();
        new GetTruckDetail(this, myAppContext.getToken(), bundle.getInt("truckId"), handler).start();

        //toolbar的标题
        //回退按钮
        toolbar_text = (TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("车主信息");

        //回退按钮
        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CarInfoActivity.this.finish();
            }
        });

        nameTextView = (TextView) findViewById(R.id.tv_info_xingming);
        typeTextView = (TextView) findViewById(R.id.tv_info_chexing);
        truckNumTextView = (TextView) findViewById(R.id.tv_info_chepaihao);
        capacityTextView = (TextView) findViewById(R.id.tv_info_zaizhong);
        lengthTextView = (TextView) findViewById(R.id.tv_info_chechang);
        phoneNumTextView = (TextView) findViewById(R.id.tv_info_dianhua);
        stateTextView = (TextView) findViewById(R.id.tv_info_shenhe);

        verify_OK = isVerify_OK(verify_state);
        linearLayout = (LinearLayout) findViewById(R.id.carinfo_shenhezhangtai);
        stateImageView = (ImageView) findViewById(R.id.verify_state);

        if (!verify_OK) {
            stateImageView.setVisibility(View.VISIBLE);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CarInfoActivity.this, CertificationActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private boolean isVerify_OK(int state) {
        return state != 3 ? true : false;
    }

}
