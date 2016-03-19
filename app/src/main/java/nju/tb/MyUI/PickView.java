package nju.tb.MyUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PickView extends View {
    //text间距与text文字较小大小的比值
    private static final float MARGIN_ALPHA = 2.8f;
    //view高度与字体最大值的比值
    private static final float VIEWHEIGHT_MAXTEXTSIZE = 4.0f;
    //字体最大值和最小值的比
    private static final float TEXTSIZE_MAX_MIN = 2.0f;
    //中间数据停留的地方与viewheight/2的差值可以忽略的极限
    private static final float DIS = 0.0001f;
    //回滚速度
    private static final float SPEED = 2;

    private List<Integer> dataList;
    //处于中间的index
    private int currentIndex;
    private Paint paint;

    private float maxTextSize = 80;
    private float minTextSize = 40;
    //透明度
    private float maxTextAlpha = 255;
    private float minTextAlpha = 120;

    private final int TEXT_COLOR = 0x333333;

    private int viewHeight;
    private int viewWidth;

    private float lastY;
    private float moveLength; //正数向下，负数向上
    private final int UP_DRAW = -1;  //向上绘制
    private final int DOWN_DRAW = 1; //向下绘制

    private boolean isMeasured = false;
    private Timer timer;
    private BackTimerTask backTimerTask;

    //用于touch up之后回滚到中间位置
    Handler backHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (Math.abs(moveLength) < DIS) {
                moveLength = 0;
                if (backTimerTask != null) {
                    backTimerTask.cancel();
                    backTimerTask = null;
                }
            } else {
                moveLength = moveLength - moveLength / Math.abs(moveLength) * SPEED;
            }
            invalidate();
        }
    };

    public PickView(Context context) {
        super(context);
        init();
    }

    public PickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 获取当前显示中间的index；
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * 初始化view
     */
    public void init() {
        currentIndex = 0;
        dataList = new ArrayList<>();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG); //消除锯齿
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(TEXT_COLOR);

        viewHeight = 0;
        viewWidth = 0;
        lastY = 0;
        moveLength = 0;
        timer = new Timer();
        backTimerTask = null;
    }

    /**
     * 设置datalist，滚动的数据
     *
     * @param list
     */
    public void setData(List<Integer> list) {
        this.dataList = list;
        if (list.size() <= 0) {
            return;
        }
        //设置默认显示的数据index
        currentIndex = list.size() / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        maxTextSize = viewHeight / VIEWHEIGHT_MAXTEXTSIZE;
        minTextSize = maxTextSize / TEXTSIZE_MAX_MIN;
        isMeasured = true;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isMeasured) {
            drawData(canvas);
        }
    }

    /**
     * 在view的画布上画图
     *
     * @param canvas
     */
    private void drawData(Canvas canvas) {
        //先绘制中间显示的text，再依次绘制上下部分
        float scale = parabola(viewHeight / 4.0f, moveLength);
        float size = (maxTextSize - minTextSize) * scale + minTextSize;
        paint.setTextSize(size);
        paint.setAlpha((int) ((maxTextAlpha - minTextAlpha) * scale + minTextAlpha));
        //垂直居中需要重新计算baseline的值，（FontMetricsInt.top+FontMetricsInt）/2的值就是字体大小的一半，用中心坐标减去这个值就可以计算baseline的值
        float x = (float) (viewWidth / 2.0);
        float y = (float) (viewHeight / 2.0 + moveLength);
        Paint.FontMetricsInt fmi = paint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.top + fmi.bottom) / 2);

        canvas.drawText(String.valueOf(dataList.get(currentIndex)), x, baseline, paint);
        //绘制上方data
        for (int i = 0; i < currentIndex; i++) {
            drawOtherText(canvas, i, UP_DRAW);
        }
        //绘制下方data
        for (int i = currentIndex + 1; i < dataList.size(); i++) {
            drawOtherText(canvas, i, DOWN_DRAW);
        }

    }

    /**
     * 绘制上下的data
     *
     * @param canvas
     * @param position
     * @param type     :-1表示向上，1表示向下
     */
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = (float) (MARGIN_ALPHA * minTextSize * (Math.abs(position - currentIndex) + moveLength));
        float scale = parabola(viewHeight / 4.0f, d);
        float size = (maxTextSize - minTextSize) * scale + minTextSize;
        paint.setTextSize(size);
        paint.setAlpha((int) ((maxTextAlpha - minTextAlpha) * scale + minTextAlpha));
        float y = (float) (viewHeight / 2.0 + type * d);
        float x = (float) (viewWidth / 2.0);
        Paint.FontMetricsInt fmi = paint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.top + fmi.bottom) / 2);
        canvas.drawText(String.valueOf(dataList.get(position)), x, baseline, paint);
    }

    /**
     * 函数，用来计算字体随着距离中心位置改变，应该改变的大小比例 ：抛物线scale=1-ax^2(x<=Height/4),scale = 0(x>Height/4)，a=(4/Height)^2
     *
     * @param zero 零点高度
     * @param x    偏移量
     * @return
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doTouchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doTouchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doTouchUp(event);
                break;
        }

        return true;
    }

    /**
     * 按下事件的处理
     *
     * @param event
     */
    private void doTouchDown(MotionEvent event) {
        if (backTimerTask != null) {
            backTimerTask.cancel();
            backTimerTask = null;
        }
        lastY = event.getY();
    }

    /**
     * 移动事件的处理
     *
     * @param event
     */
    private void doTouchMove(MotionEvent event) {
        moveLength += (event.getY() - lastY);
        //moveLength超过间距的一半就可以滑动到下一个position
        //向下
        if (moveLength > (MARGIN_ALPHA * minTextSize / 2)) {
            if (currentIndex != 0) {
                currentIndex--;
                moveLength = 0;
            }
        }
        //向上
        else if (moveLength < -(MARGIN_ALPHA * minTextSize / 2)) {
            if (currentIndex != dataList.size() - 1) {
                currentIndex++;
                moveLength = 0;
            }
        }
        lastY = event.getY();
        invalidate();
    }

    /**
     * 松开事件的处理
     *
     * @param event
     */
    private void doTouchUp(MotionEvent event) {
        if (moveLength < DIS) {
            moveLength = 0;
            return;
        }
        if (backTimerTask != null) {
            backTimerTask.cancel();
            backTimerTask = null;
        }
        backTimerTask = new BackTimerTask(backHandler);
        //每10ms刷新一次回滚
        timer.schedule(backTimerTask, 0, 10);
    }

    /**
     * 回滚定时任务
     */
    class BackTimerTask extends TimerTask {
        Handler handler;

        BackTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }
}
