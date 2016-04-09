package nju.tb.atys;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import nju.tb.Commen.LocalImageHelper;
import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.net.SetRealNameAuthInfo;
import nju.tb.net.SetTruckAuthInfo;


@SuppressWarnings("deprecation")
public class CertificationActivity extends TabActivity {
    private TabHost mTabHost;
    private TabWidget mTabWidget;
    private TextView toolbar_text;
    private TextView submitCertification;

    private TextView truckAuthStateTextView, realNameAuthStateTextView, xingshizhengOk, chetouzhaoOk, jiashizhengOk,
            jiashirenOk;
    private TextView truckNumTextView;
    private EditText realNameEditText;
    private EditText driverLicenseNumEditText;//驾驶证号
    private LinearLayout xingshizhengLinearLayout, chetouzhaoLinearLayout, jiashizhengLinearLayout,
            jiashirenLinearLayout;
    private static String realNameEditTextSave;
    private static String driverLicenseNumEditTextSave;
    private static String truckNum;
    private static String state;
    private String token;
    private static String licensePicUrl; //驾驶证图片url          1
    private static String driverHeadPicUrl; //驾驶人头像url         2
    private static String truckHeadPicUrl;     // 车头图片url       3
    private static String driverLicensePicUrl;  //行驶证图片url        4
    private LocalImageHelper localImageHelper;
    private static boolean one, two, three, four;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_identification);

        //bundle为获取车牌号truckNum
        Bundle bundle = getIntent().getBundleExtra("CarInfoActivity");
        if (bundle != null) {
            truckNum = bundle.getString("truckNum");
            state = bundle.getString("state");
            realNameEditTextSave = "";
            driverLicenseNumEditTextSave = "";
            licensePicUrl = "";
            driverHeadPicUrl = "";
            truckHeadPicUrl = "";
            driverLicensePicUrl = "";
            one = two = three = four = false;
        }

        MyAppContext myAppContext = (MyAppContext) getApplicationContext();
        token = myAppContext.getToken();

        localImageHelper = new LocalImageHelper(this);
        if (!localImageHelper.isInited) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    localImageHelper.initImage();
                }
            }
            ).start();
        }
        //toolbar的标题
        //回退按钮
        toolbar_text = (TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("认证");
        //回退按钮
        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CertificationActivity.this.finish();
            }
        });

        submitCertification = (TextView) findViewById(R.id.askforcheck);

        mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("certification_name").setIndicator("实名认证").setContent(R.id
                .certification_shimingrenzheng));
        mTabHost.addTab(mTabHost.newTabSpec("certification_car").setIndicator("车辆认证").setContent(R.id
                .certification_cheliangrenzheng));

        mTabWidget = mTabHost.getTabWidget();
        for (int i = 0; i < mTabWidget.getChildCount(); i++) {
            TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(25);
        }
        mTabHost.setCurrentTab(0);

        truckAuthStateTextView = (TextView) findViewById(R.id.certification_cheliang_state);
        truckNumTextView = (TextView) findViewById(R.id.certification_chepaihao);
        xingshizhengLinearLayout = (LinearLayout) findViewById(R.id.xingshizhenglinearlayout);
        xingshizhengOk = (TextView) findViewById(R.id.xingshizheng);
        chetouzhaoLinearLayout = (LinearLayout) findViewById(R.id.chetouzhaolinearlayout);
        chetouzhaoOk = (TextView) findViewById(R.id.chetouzhao);
        realNameAuthStateTextView = (TextView) findViewById(R.id.certification_shiming_state);
        realNameEditText = (EditText) findViewById(R.id.certification_zhenshixingming);
        driverLicenseNumEditText = (EditText) findViewById(R.id.certification_jiashizhenghao);
        jiashizhengLinearLayout = (LinearLayout) findViewById(R.id.jiashizhenglinearlayout);
        jiashizhengOk = (TextView) findViewById(R.id.jiashizheng);
        jiashirenOk = (TextView) findViewById(R.id.jiashiren);
        jiashirenLinearLayout = (LinearLayout) findViewById(R.id.jiashirenlinearlayout);

        xingshizhengLinearLayout.setOnClickListener(new CertificationLayoutClickListener("CertificationActivity4"));
        chetouzhaoLinearLayout.setOnClickListener(new CertificationLayoutClickListener("CertificationActivity3"));
        jiashizhengLinearLayout.setOnClickListener(new CertificationLayoutClickListener("CertificationActivity1"));
        jiashirenLinearLayout.setOnClickListener(new CertificationLayoutClickListener("CertificationActivity2"));

        truckNumTextView.setText(truckNum);
        realNameEditText.setText(realNameEditTextSave);
        driverLicenseNumEditText.setText(driverLicenseNumEditTextSave);

        realNameAuthStateTextView.setText(state);
        truckAuthStateTextView.setText(state);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (one)
            jiashizhengOk.setVisibility(View.VISIBLE);
        if (two)
            jiashirenOk.setVisibility(View.VISIBLE);
        if (three)
            chetouzhaoOk.setVisibility(View.VISIBLE);
        if (four)
            xingshizhengOk.setVisibility(View.VISIBLE);


        //bundle1为获取AlbumDetailActivity执行的上传异步任务的返回值，判断上传的哪个图片
        Bundle bundle1 = getIntent().getBundleExtra("AlbumDetailActivityReturn");
        if (bundle1 != null) {
            int num = bundle1.getInt("num");
            switch (num) {
                case 1:
                    licensePicUrl = bundle1.getString("url");
                    one = true;
                    jiashizhengOk.setVisibility(View.VISIBLE);
                    mTabHost.setCurrentTab(bundle1.getInt("currentTab"));
                    break;
                case 2:
                    driverHeadPicUrl = bundle1.getString("url");
                    two = true;
                    jiashirenOk.setVisibility(View.VISIBLE);
                    mTabHost.setCurrentTab(bundle1.getInt("currentTab"));
                    break;
                case 3:
                    truckHeadPicUrl = bundle1.getString("url");
                    three = true;
                    chetouzhaoOk.setVisibility(View.VISIBLE);
                    mTabHost.setCurrentTab(bundle1.getInt("currentTab"));
                    break;
                case 4:
                    driverLicensePicUrl = bundle1.getString("url");
                    four = true;
                    xingshizhengOk.setVisibility(View.VISIBLE);
                    mTabHost.setCurrentTab(bundle1.getInt("currentTab"));
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        submitCertification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String realName = realNameEditText.getText().toString();
                String licenseNum = driverLicenseNumEditText.getText().toString();
                if (realName.equals("") || licenseNum.equals("") || licensePicUrl.equals("") || driverHeadPicUrl
                        .equals("") || truckHeadPicUrl.equals("") || driverLicensePicUrl.equals("")) {
                    Toast.makeText(CertificationActivity.this, "请务必填写所有条目", Toast.LENGTH_SHORT).show();
                    return;
                }
                new SetRealNameAuthInfo(CertificationActivity.this, token, truckNum, realName, licenseNum,
                        licensePicUrl, driverHeadPicUrl).start();
                new SetTruckAuthInfo(CertificationActivity.this, token, truckNum, truckHeadPicUrl,
                        driverLicensePicUrl).start();
                ProgressDialog p = new ProgressDialog(CertificationActivity.this);
                p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                p.setMessage("加载中...");
                p.setCancelable(false);
                p.show();
                while (SetRealNameAuthInfo.getResult() != 1 || SetTruckAuthInfo.getResult() != 1) {
                    if (SetRealNameAuthInfo.getResult() == 0 || SetTruckAuthInfo.getResult() == 0) {
                        Toast.makeText(CertificationActivity.this, "上传信息失败，请重新上传", Toast.LENGTH_SHORT).show();
                        p.dismiss();
                        return;
                    }
                }
                p.dismiss();
                Intent intent = new Intent(CertificationActivity.this, CarsManagementActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        realNameEditTextSave = realNameEditText.getText().toString();
        driverLicenseNumEditTextSave = driverLicenseNumEditText.getText().toString();
        this.finish();
    }


    class CertificationLayoutClickListener implements View.OnClickListener {
        private String sourceActivity;

        public CertificationLayoutClickListener(String sourceActivity) {
            this.sourceActivity = sourceActivity;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CertificationActivity.this, SelectAlbumActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("SourceActivity", sourceActivity);
            intent.putExtra("Activity", bundle);
            startActivity(intent);
        }
    }
}
