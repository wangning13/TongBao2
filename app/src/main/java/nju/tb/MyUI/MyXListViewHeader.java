package nju.tb.MyUI;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import nju.tb.R;

public class MyXListViewHeader extends LinearLayout {
    //显示在textview上的文字
    private static final String HINT_NORMAL = "下拉刷新";
    private static final String HINT_READY = "松开刷新数据";
    private static final String HINT_LOADING = "正在加载。。。";
    private static final String HINT_ALLREADYNEW = "已经是最新消息";

    //正常状态
    public final static int STATE_NORMAL = 0;
    //主播刷新状态，尚未刷新，此时箭头反转向上
    public final static int STATE_READY = 1;
    //松开状态，箭头消失，progressbar出现，执行刷新动作
    public final static int STATE_REFESHING = 2;
    //刷新之后没有新数据，显示已经是最新消息
    public final static int STATE_ALLREADYNEW = 3;
    //容器布局 LinearLayout
    private LinearLayout container;
    //箭头图片
    private ImageView mArrowImageView;
    //刷新的时候出现的progressbar
    private ProgressBar mProgressBar;
    //文本
    private TextView mHintTextView;
    //纪录当前的状态
    private int mState;
    //改变动画 箭头向上旋转
    private Animation mRotateUpAnim;
    //改编动画，箭头向下旋转
    private Animation mRotateDownAnim;
    //动画持续时间
    private final int ROTATE_ANIM_DURATION = 180;

    public MyXListViewHeader(Context context) {
        super(context);
        initView(context);
    }

    public MyXListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 用于初始化XListViewHeader内的控件
     *
     * @param context 上下文环境
     */
    private void initView(Context context) {
        mState = STATE_NORMAL;
        //开始的时候，设置布局高度为0，隐藏header
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        container = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_xlistview_header, null);
        addView(container, lp);

        mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
        mProgressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation
                .RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation
                .RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    /**
     * 设置header的状态
     *
     * @param state
     */
    public void setState(int state) {
        if (state == mState) {
            return;
        }

        if (state == STATE_REFESHING) {
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mArrowImageView.setVisibility(View.VISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                } else if (mState == STATE_REFESHING) {
                    mArrowImageView.clearAnimation();
                }
                mHintTextView.setText(HINT_NORMAL);
                break;
            case STATE_READY:
                if (mState == STATE_NORMAL) {
                    mArrowImageView.startAnimation(mRotateUpAnim);
                } else if (mState == STATE_REFESHING) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                }
                mHintTextView.setText(HINT_READY);
                break;
            case STATE_REFESHING:
                mHintTextView.setText(HINT_LOADING);
                break;
            case STATE_ALLREADYNEW:
                if (mState != STATE_REFESHING) {
                    return;
                }
                mProgressBar.setVisibility(INVISIBLE);
                mHintTextView.setText(HINT_ALLREADYNEW);
                break;
        }

        mState = state;
    }

    /**
     * 设置header的高度，在xlistview中滑动时更新header的可见高度
     *
     * @param height
     */
    public void setVisiableHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) container.getLayoutParams();
        lp.height = height;
        container.setLayoutParams(lp);
    }

    /**
     * 获取header高度，留作滑动的判断
     *
     * @return
     */
    public int getVisiableHeight() {
        return container.getLayoutParams().height;
    }

    /**
     * header显示
     */
    public void show() {
        container.setVisibility(View.VISIBLE);
    }

    /**
     * header隐藏
     */
    public void hide() {
        container.setVisibility(View.INVISIBLE);
    }

}
