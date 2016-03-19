package nju.tb.atys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nju.tb.R;

public class WithdrawActivity extends FragmentActivity implements View.OnClickListener {

    private TextView toolbar_text;
    private ViewPager wViewPager;
    private List<Fragment> fragmentList;
    int currenttab=-1;
    private  AlipayFragment fragment1;
    private  CardFragment fragment2;
    private Button left;
    private Button right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        toolbar_text=(TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("钱包");




        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WithdrawActivity.this.finish();
            }
        });


        left=(Button)findViewById(R.id.alipay);
        right=(Button)findViewById(R.id.card);

        left.setOnClickListener(this);
        right.setOnClickListener(this);

        wViewPager=(ViewPager) findViewById(R.id.wviewpager);
        fragmentList=new ArrayList<Fragment>();
        fragment1 = new AlipayFragment();
        fragment2 = new CardFragment();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        wViewPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));



        changeView(0);
    }


    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.alipay:
                changeView(0);
                break;
            case R.id.card:
                changeView(1);
                break;
            default:
                break;
        }
    }


    private void changeBtn(int desTab) {
        if (desTab == 0) {
            left.setBackgroundDrawable(getResources().getDrawable(R.drawable.leftshape2));
            right.setBackgroundDrawable(getResources().getDrawable(R.drawable.rightshape1));
        }
        if (desTab == 1) {
            left.setBackgroundDrawable(getResources().getDrawable(R.drawable.leftshape1));
            right.setBackgroundDrawable(getResources().getDrawable(R.drawable.rightshape2));
        }

    }


    //手动设置ViewPager要显示的视图
    private void changeView(int desTab)
    {
        changeBtn(desTab);
        wViewPager.setCurrentItem(desTab, true);
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
            int currentItem=wViewPager.getCurrentItem();
            if (currentItem==currenttab)
            {
                return ;
            }
            changeBtn(currentItem);
            currenttab=wViewPager.getCurrentItem();
        }

    }
}
