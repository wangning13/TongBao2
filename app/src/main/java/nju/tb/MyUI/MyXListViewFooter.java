package nju.tb.MyUI;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nju.tb.R;

public class MyXListViewFooter extends LinearLayout {
    //显示在textview上的文字
    private final static String HINT_NORMAL = "上拉加载更多数据";
    private final static String HINT_READY = "松开加载数据";
    private final static String HINT_NOMORE = "没有更多数据";

    //正常状态
    public final static int STATE_NORMAL = 0;
    //准备加载状态
    public final static int STATE_READY = 1;
    //加载中状态
    public final static int STATE_LOADING = 2;
    //没有更多数据状态
    public final static int STATE_NOMORE = 3;
    //通过该relativeLayout的marginbottom来控制
    private View mContentView;
    private ProgressBar mProgressBar;
    private TextView mHintView;

    public MyXListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public MyXListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initView(Context context) {
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_xlistview_footer,
                null);
        addView(moreView);
        moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mContentView = findViewById(R.id.xlistview_footer_content);
        mProgressBar = (ProgressBar) findViewById(R.id.xlistview_footer_progressbar);
        mHintView = (TextView) findViewById(R.id.xlistview_footer_hint_textview);
    }

    /**
     * 设置footer的状态
     *
     * @param state
     */
    public void setState(int state) {
        mProgressBar.setVisibility(INVISIBLE);
        mHintView.setVisibility(INVISIBLE);

        switch (state) {
            case STATE_NORMAL:
                mHintView.setVisibility(VISIBLE);
                mHintView.setText(HINT_NORMAL);
                break;
            case STATE_READY:
                mHintView.setVisibility(VISIBLE);
                mHintView.setText(HINT_READY);
                break;
            case STATE_LOADING:
                mProgressBar.setVisibility(VISIBLE);
                break;
            case STATE_NOMORE:
                mHintView.setVisibility(VISIBLE);
                mHintView.setText(HINT_NOMORE);
                break;
        }
    }

    /**
     * 设置contentview的layout_marginBottom属性，来调整footer的高度
     *
     * @param height
     */
    public void setBottomMargin(int height) {
        if (height < 0) {
            return;
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    public int getBottomMargin() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * 隐藏footer
     */
    public void hide() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    public void show() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }
}
