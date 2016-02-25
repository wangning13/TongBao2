package nju.tb.atys;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import nju.tb.R;


@SuppressWarnings("deprecation")
public class CertificationActivity extends TabActivity {
    private TabHost mTabHost;
    private TabWidget mTabWidget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_identification);

        mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("certification_name").setIndicator("实名认证").setContent(R.id.certification_shimingrenzheng));
        mTabHost.addTab(mTabHost.newTabSpec("certification_car").setIndicator("车辆认证").setContent(R.id.certification_cheliangrenzheng));

        mTabWidget=mTabHost.getTabWidget();
        for(int i=0;i<mTabWidget.getChildCount();i++){
            TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(25);
        }
        mTabHost.setCurrentTab(0);

    }
}
