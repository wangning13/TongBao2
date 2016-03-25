package nju.tb.MyUI;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import nju.tb.R;

public class MyXListView extends ListView {
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;
    //滑动时长
    private final static int SCROLLBACK_DURATION = 400;
    //footer加载更多的时候，判断的距离
    private final static int PULL_LOAD_MORE_DELTA = 100;
    //滑动比例 ，更好的用户体验
    private final static float OFFSET_RADIO = 2f;
    //记录按下点的Y坐标
    private float lastY;
    //Scroller 类用来计算坐标，用来回滚
    private Scroller scroller;
    //回调的接口，留作注册
    private IXListViewListener ixListViewListener;
    private MyXListViewHeader headerView;
    private RelativeLayout headerViewContent;
    private int headerViewHeight;
    //是否能刷新
    private boolean enableRefresh = true;
    //是否正在刷新
    private boolean isRefreshing = false;
    private MyXListViewFooter footerView;
    //是否可以加载更多
    private boolean enableLoadMore = true;
    //是否正在加载更多
    private boolean isLoadingMore = false;
    //判断footerview是否已经被添加，确保只添加一次footerview，在setAdapter之前
    private boolean isFooterViewAdd = false;
    //item总数
    private int totalItemCount;
    //记录是header还是footer需要回滚
    private int mSrollerBack;

    public MyXListView(Context context) {
        super(context);
        initView(context);
    }

    public MyXListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyXListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initView(Context context) {
        //Scroller(Context context,Interpolator interpolator)arg1代表动画插入器，DecelerateInterpolator表示刚开始快速之后减速的效果
        scroller = new Scroller(context, new DecelerateInterpolator());
        headerView = new MyXListViewHeader(context);
        footerView = new MyXListViewFooter(context);
        headerViewContent = (RelativeLayout) headerView.findViewById(R.id.xlistview_header_content);
        //布局绘制过程中getheight无法获得控件高度，使用监听来获取
        headerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                headerViewHeight = headerViewContent.getHeight();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        addHeaderView(headerView);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (!isFooterViewAdd) {
            isFooterViewAdd = true;
            addFooterView(footerView);
        }
        super.setAdapter(adapter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //0是headerview, totalItemCount-1是footerview
        if (getAdapter() == null) {
            totalItemCount = 2;
        } else {
            totalItemCount = getAdapter().getCount();

        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0) {
                    headerView.setState(MyXListViewHeader.STATE_NORMAL);
                } else if (getLastVisiblePosition() == totalItemCount - 1) {
                    footerView.setState(MyXListViewFooter.STATE_NORMAL);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float detlaY = ev.getRawY() - lastY;
                lastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && (detlaY > 0 || headerView.getVisiableHeight() > 0)) {
                    //headerview已经显示，并且可见的第一项是headerview（滑动到最上方）
                    updateHeaderHeight(detlaY / OFFSET_RADIO);
                } else if (getLastVisiblePosition() == totalItemCount - 1 && (footerView.getBottomMargin() > 0 ||
                        detlaY < 0)) {
                    //footerview显示
                    updateFooterHeight(-detlaY / OFFSET_RADIO);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (getFirstVisiblePosition() == 0) {
                    if (enableRefresh && headerView.getVisiableHeight() > headerViewHeight) {
                        isRefreshing = true;
                        headerView.setState(MyXListViewHeader.STATE_REFESHING);
                        if (ixListViewListener != null) {
                            ixListViewListener.onRefresh();
                        }
                    }
                    resetHeaderViewHeight();
                } else if (getLastVisiblePosition() == totalItemCount - 1) {
                    if (enableLoadMore && footerView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
                        startLoadMore();
                    }
                    resetFooterViewHeight();
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        //判断松手之后滑动scroll计算的滑动是否已经结束，true没结束，false结束
        if (scroller.computeScrollOffset()) {
            if (mSrollerBack == SCROLLBACK_HEADER) {
                headerView.setVisiableHeight(scroller.getCurrY());
            } else if (mSrollerBack == SCROLLBACK_FOOTER) {
                footerView.setBottomMargin(scroller.getCurrY());
            }
            invalidate();
        }
        super.computeScroll();
    }

    /**
     * 设置是否可以刷新
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        enableRefresh = enable;
        if (!enableRefresh) {
            headerView.hide();
        } else {
            headerView.show();
        }
    }

    /**
     * 设置是否可以上拉加载更多
     *
     * @param enable
     */
    public void setPullLoadMoreEnable(boolean enable) {
        enableLoadMore = enable;
        if (!enableLoadMore) {
            footerView.hide();
            footerView.setOnClickListener(null);
        } else {
            isLoadingMore = false;
            footerView.show();
            footerView.setState(MyXListViewFooter.STATE_NORMAL);
            footerView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //开始load
                    startLoadMore();
                }
            });
        }
    }

    public void stopRefresh() {
        if (isRefreshing) {
            isRefreshing = false;

        }
    }

    public void setNews(boolean hasNews) {
        if (!isRefreshing) {
            return;
        }
        isRefreshing = false;
        if (hasNews) {
            headerView.setState(MyXListViewHeader.STATE_NORMAL);
        } else {
            headerView.setState(MyXListViewHeader.STATE_ALLREADYNEW);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                headerView.hide();
            }
        }, 1500);
    }

    public void stopLoadMore() {
        if (isLoadingMore) {
            isLoadingMore = false;
            footerView.setState(MyXListViewFooter.STATE_NORMAL);
        }
    }

    public void setMore(boolean hasMore) {
        if (!isLoadingMore) {
            return;
        }
        isLoadingMore = false;
        if (hasMore) {
            footerView.setState(MyXListViewFooter.STATE_NORMAL);
        } else {
            footerView.setState(MyXListViewFooter.STATE_NOMORE);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                footerView.setState(MyXListViewFooter.STATE_NORMAL);
            }
        }, 1500);
    }

    /**
     * 更新headerview高度
     *
     * @param detla
     */
    private void updateHeaderHeight(float detla) {
        int height = headerView.getVisiableHeight() + (int) detla;
        if (enableRefresh && !isRefreshing) {
            if (height > headerViewHeight) {
                headerView.setState(MyXListViewHeader.STATE_READY);
            } else {
                headerView.setState(MyXListViewHeader.STATE_NORMAL);
            }
        }
        headerView.setVisiableHeight(height);

    }

    /**
     * 回滚headerview
     */
    private void resetHeaderViewHeight() {
        int height = headerView.getVisiableHeight();
        //刷新中并且可见高度没有达到headerview的原始高度，则不需要回滚， 可见高度为0的情况同样不需要回滚
        if (isRefreshing && height <= headerViewHeight || height == 0) {
            return;
        }
        //headerview回滚的位置
        int finalHeight = 0;
        if (isRefreshing && height > headerViewHeight) {
            finalHeight = headerViewHeight;
        }
        mSrollerBack = SCROLLBACK_HEADER;
        //scroller计算回滚的坐标
        scroller.startScroll(0, height, 0, finalHeight - height, SCROLLBACK_DURATION);
        //scroller->invalidate->computescroller 使用此方法可以调用computescroller
        invalidate();
    }

    /**
     * ontouch里面手指在屏幕上滑动，不断更新footerheight
     *
     * @param delta lasty的差
     */
    private void updateFooterHeight(float delta) {
        if (!enableLoadMore) {
            return;
        }
        int bottomMargin = footerView.getBottomMargin() + (int) delta;
        if (enableLoadMore && !isLoadingMore) {
            if (bottomMargin > PULL_LOAD_MORE_DELTA) {
                footerView.setState(MyXListViewFooter.STATE_READY);
            } else {
                footerView.setState(MyXListViewFooter.STATE_NORMAL);
            }
        }
        footerView.setBottomMargin(bottomMargin);
    }

    /**
     * 回滚footerview ，footerview的bottommargin需要回滚到0
     */
    private void resetFooterViewHeight() {
        int bottomMargin = footerView.getBottomMargin();
        if (bottomMargin > 0) {
            mSrollerBack = SCROLLBACK_FOOTER;
            scroller.startScroll(0, bottomMargin, 0, -bottomMargin);
            invalidate();
        }
    }

    private void startLoadMore() {
        isLoadingMore = true;
        footerView.setState(MyXListViewFooter.STATE_LOADING);
        if (ixListViewListener != null) {
            ixListViewListener.onLoadMore();
        }
    }

    /**
     * 注册回调接口
     *
     * @param ixListViewListener
     */
    public void setXListViewListener(IXListViewListener ixListViewListener) {
        this.ixListViewListener = ixListViewListener;
    }

    /**
     * 回调接口
     */
    public interface IXListViewListener {
        //刷新时执行的方法
        public void onRefresh();

        //加载更多数据时，执行的方法
        public void onLoadMore();
    }

}
