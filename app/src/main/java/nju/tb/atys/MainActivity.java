package nju.tb.atys;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import nju.tb.Commen.MyAppContext;
import nju.tb.R;

public class MainActivity extends Activity {


    private TextView toolbar_text;
    private RelativeLayout home_layout;
    private RelativeLayout order_layout;
    private RelativeLayout me_layout;
    private TextView home_text;
    private TextView order_text;
    private TextView me_text;
    private HomeFragment fragment1;
    private OrderFragment fragment2;
    private MeFragment fragment3;

    ImageView ivhome;
    ImageView ivorder;
    ImageView ivme;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(!MyAppContext.getIsConnected()){
//            Toast.makeText(this, "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show();
//        }

        toolbar_text = (TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("通宝");

        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.INVISIBLE);


        home_layout = (RelativeLayout) findViewById(R.id.home_layout);
        order_layout = (RelativeLayout) findViewById(R.id.order_layout);
        me_layout = (RelativeLayout) findViewById(R.id.me_layout);
        home_text = (TextView) findViewById(R.id.home_text);
        order_text = (TextView) findViewById(R.id.order_text);
        me_text = (TextView) findViewById(R.id.me_text);
        ivhome = (ImageView) findViewById(R.id.home_img);
        ivorder = (ImageView) findViewById(R.id.order_img);
        ivme = (ImageView) findViewById(R.id.me_img);
        fragmentManager = getFragmentManager();
        setTabSelection(0);
        ivhome.setImageDrawable(getResources().getDrawable(R.drawable.home2));


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);

        findViewById(R.id.home_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setTabSelection(0);
                ivhome.setImageDrawable(getResources().getDrawable(R.drawable.home2));
                home_text.setTextColor(Color.parseColor("#1F6EF2"));
            }
        });
        findViewById(R.id.order_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setTabSelection(1);
                ivorder.setImageDrawable(getResources().getDrawable(R.drawable.order2));
                order_text.setTextColor(Color.parseColor("#1F6EF2"));
            }
        });
        findViewById(R.id.me_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setTabSelection(2);
                ivme.setImageDrawable(getResources().getDrawable(R.drawable.me2));
                me_text.setTextColor(Color.parseColor("#1F6EF2"));
            }
        });
        int type = getIntent().getIntExtra("type", 0);
        if(type==2){
            setTabSelection(2);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getBundleExtra("ChangeInfoAcitivity");
        int targetFragment = -1;
        if (bundle == null) {
            return;
        }
        String updateBitmapPath = bundle.getString("changeInfoBitmap");
        targetFragment = bundle.getInt("TargetFragment");
        MeFragment meFragment = new MeFragment();
        Bundle updateBundle = new Bundle();
        updateBundle.putString("BitmapPathToUpdate", updateBitmapPath);
        meFragment.setArguments(updateBundle);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(meFragment, "update");
        transaction.commit();
        setTabSelection(targetFragment);
    }


    protected void setTabSelection(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (fragment1 == null) {
                    fragment1 = new HomeFragment();
                    transaction.add(R.id.content, fragment1);
                } else {
                    transaction.show(fragment1);
                }

                break;
            case 1:
                if (fragment2 == null) {
                    fragment2 = new OrderFragment();
                    transaction.add(R.id.content, fragment2);
                } else {
                    transaction.show(fragment2);
                }
                break;
            case 2:
                if (fragment3 == null) {
                    fragment3 = new MeFragment();
                    transaction.add(R.id.content, fragment3);
                } else {
                    transaction.show(fragment3);
                }
                break;
        }
        transaction.commit();
    }


    private void hideFragments(FragmentTransaction transaction) {
        if (fragment1 != null) {
            transaction.hide(fragment1);
            ivhome.setImageDrawable(getResources().getDrawable(R.drawable.home1));
            home_text.setTextColor(Color.GRAY);
        }
        if (fragment2 != null) {
            transaction.hide(fragment2);
            ivorder.setImageDrawable(getResources().getDrawable(R.drawable.order1));
            order_text.setTextColor(Color.GRAY);
        }
        if (fragment3 != null) {
            transaction.hide(fragment3);
            ivme.setImageDrawable(getResources().getDrawable(R.drawable.me1));
            me_text.setTextColor(Color.GRAY);
        }
    }
}