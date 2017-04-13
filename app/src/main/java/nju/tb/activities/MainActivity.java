package nju.tb.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nju.tb.R;

public class MainActivity extends FragmentActivity implements View.OnClickListener {


    private TextView toolbar_text;
    private RelativeLayout home_layout;
    private RelativeLayout order_layout;
    private RelativeLayout me_layout;
    private TextView home_text;
    private TextView order_text;
    private TextView me_text;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList;
    private HomeFragment fragment1;
    private OrderFragment fragment2;
    private MeFragment fragment3;

    int currenttab=-1;

    ImageView ivhome;
    ImageView ivorder;
    ImageView ivme;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MainActivity.class.getName(),"init");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(MainActivity.class.getName(),"initing");
//        if(!MyAppContext.getIsConnected()){
//            Toast.makeText(this, "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show();
//        }

        toolbar_text = (TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("通宝");

        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.INVISIBLE);


        mViewPager=(ViewPager) findViewById(R.id.viewpager);
        Log.d(MainActivity.class.getName(),"init3");
        fragmentList=new ArrayList<Fragment>();
        fragment1 = new HomeFragment();
        fragment2 = new OrderFragment();
        fragment3 = new MeFragment();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        Log.d(MainActivity.class.getName(),"init4");
        mViewPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
        Log.d(MainActivity.class.getName(),"init5");



        home_layout = (RelativeLayout) findViewById(R.id.home_layout);
        order_layout = (RelativeLayout) findViewById(R.id.order_layout);
        me_layout = (RelativeLayout) findViewById(R.id.me_layout);
        home_text = (TextView) findViewById(R.id.home_text);
        order_text = (TextView) findViewById(R.id.order_text);
        me_text = (TextView) findViewById(R.id.me_text);
        ivhome = (ImageView) findViewById(R.id.home_img);
        ivorder = (ImageView) findViewById(R.id.order_img);
        ivme = (ImageView) findViewById(R.id.me_img);
        Log.d(MainActivity.class.getName(),"init6");
        changeView(0);
        Log.d(MainActivity.class.getName(),"init7");
        ivhome.setImageDrawable(getResources().getDrawable(R.drawable.home2));

        home_layout.setOnClickListener(this);
        order_layout.setOnClickListener(this);
        me_layout.setOnClickListener(this);
        Log.d(MainActivity.class.getName(),"init8");




        int type = getIntent().getIntExtra("type", 0);
        Log.d(MainActivity.class.getName(),"init9");
        if(type==1){
            changeView(1);

        }
    }

    @Override
    public void onResume() {
        Log.d(MainActivity.class.getName(),"init10");
        super.onResume();
        Bundle bundle = getIntent().getBundleExtra("ChangeInfoAcitivity");
        Log.d(MainActivity.class.getName(),"init11");
        int targetFragment = -1;
        if (bundle == null) {
            Log.d(MainActivity.class.getName(),"init12");
            return;
        }
        Log.d(MainActivity.class.getName(),"init13");
        String updateBitmapPath = bundle.getString("changeInfoBitmap");
        targetFragment = bundle.getInt("TargetFragment");
        MeFragment meFragment = new MeFragment();
        Bundle updateBundle = new Bundle();
        updateBundle.putString("BitmapPathToUpdate", updateBitmapPath);
        meFragment.setArguments(updateBundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(meFragment, "update");
        transaction.commit();
        changeView(targetFragment);
    }




    class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter
    {

        public MyFrageStatePagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        /**
         * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
         */
        @Override
        public void finishUpdate(ViewGroup container)
        {
            super.finishUpdate(container);//这句话要放在最前面，否则会报错
            //获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
            int currentItem=mViewPager.getCurrentItem();
            if (currentItem==currenttab)
            {
                return ;
            }
            changeBtn(currentItem);
            currenttab=mViewPager.getCurrentItem();
        }

    }



    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.home_layout:
                changeView(0);
                break;
            case R.id.order_layout:
                changeView(1);
                break;
            case R.id.me_layout:
                changeView(2);
                break;
            default:
                break;
        }
    }


    private void changeBtn(int desTab) {
        if (desTab == 0) {
            ivhome.setImageDrawable(getResources().getDrawable(R.drawable.home2));
            home_text.setTextColor(Color.parseColor("#1F6EF2"));
        }
        if (desTab == 1) {
            ivorder.setImageDrawable(getResources().getDrawable(R.drawable.order2));
            order_text.setTextColor(Color.parseColor("#1F6EF2"));
        }
        if (desTab == 2) {
            ivme.setImageDrawable(getResources().getDrawable(R.drawable.me2));
            me_text.setTextColor(Color.parseColor("#1F6EF2"));
        }
        if (desTab != 0) {
            ivhome.setImageDrawable(getResources().getDrawable(R.drawable.home1));
            home_text.setTextColor(Color.GRAY);
        }
        if (desTab != 1) {
            ivorder.setImageDrawable(getResources().getDrawable(R.drawable.order1));
            order_text.setTextColor(Color.GRAY);
        }
        if (desTab != 2) {
            ivme.setImageDrawable(getResources().getDrawable(R.drawable.me1));
            me_text.setTextColor(Color.GRAY);
        }
    }


    //手动设置ViewPager要显示的视图
    private void changeView(int desTab)
    {
        changeBtn(desTab);
        mViewPager.setCurrentItem(desTab, true);
    }
}