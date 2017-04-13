package nju.tb.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import nju.tb.MyUI.MyViewPager;
import java.util.ArrayList;
import java.util.List;

import nju.tb.R;

/**
 * Created by Administrator on 2016/3/20.
 */
public class ScrambleOrderActivity extends FragmentActivity implements View.OnClickListener {


    private TextView so_text;
    private TextView nb_text;
    private MyViewPager soViewPager;
    private List<Fragment> fragmentList;

    int currenttab=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrambleorderactivity);
//
        TextView toolbar_text = (TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("抢单");

        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScrambleOrderActivity.this.finish();
            }
        });

//
//
        soViewPager=(MyViewPager) findViewById(R.id.soviewpager);
        soViewPager.setScanScroll(false);
        fragmentList=new ArrayList<Fragment>();
        ScrambleOrderFragment fragment1 = new ScrambleOrderFragment();
        NearbyFragment fragment2 = new NearbyFragment();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        soViewPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
////
////
//
        RelativeLayout so_layout = (RelativeLayout) findViewById(R.id.so_layout);
        RelativeLayout nb_layout = (RelativeLayout) findViewById(R.id.nb_layout);
        so_text = (TextView) findViewById(R.id.so_text);
        nb_text = (TextView) findViewById(R.id.nb_text);
        changeView(0);

        so_layout.setOnClickListener(this);
        nb_layout.setOnClickListener(this);

    }


    class MyFrageStatePagerAdapter extends FragmentPagerAdapter
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
            int currentItem=soViewPager.getCurrentItem();
            if (currentItem==currenttab)
            {
                return ;
            }
            changeBtn(currentItem);
            currenttab=soViewPager.getCurrentItem();
        }

    }



    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.so_layout:
                changeView(0);
                break;
            case R.id.nb_layout:
                changeView(1);
            default:
                break;
        }
    }


    private void changeBtn(int desTab) {
        if (desTab == 0) {
            so_text.setTextColor(Color.parseColor("#1F6EF2"));
            nb_text.setTextColor(Color.GRAY);
        }
        if (desTab == 1) {
            nb_text.setTextColor(Color.parseColor("#1F6EF2"));
            so_text.setTextColor(Color.GRAY);
        }
    }


    //手动设置ViewPager要显示的视图
    private void changeView(int desTab)
    {
        changeBtn(desTab);
        soViewPager.setCurrentItem(desTab, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}