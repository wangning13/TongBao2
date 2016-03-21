package nju.tb.MyUI;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import nju.tb.R;

public class TimeSelectorDialog extends Dialog {
    private List<Integer> yearList;
    private List<Integer> monthList;
    private TextView cancelTextView;
    private TextView okTextView;
    private PickView yearPickView;
    private PickView monthPickView;
    private Window window;
    private TimeSelector timeSelector;

    public TimeSelectorDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public TimeSelectorDialog(Context context) {
        super(context);
    }

    // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
    public TimeSelectorDialog(Context context,int style, List<Integer> yearList, List<Integer> monthList) {
        this(context,style);
        this.yearList = yearList;
        this.monthList = monthList;
    }

    private void init() {
        window = getWindow();
        cancelTextView = (TextView) findViewById(R.id.timeselector_dialog_cancel);
        okTextView = (TextView) findViewById(R.id.timeselector_dialog_ok);
        yearPickView = (PickView) findViewById(R.id.timeselector_dialog_year);
        monthPickView = (PickView) findViewById(R.id.timeselector_dialog_month);
        yearPickView.setData(yearList);
        monthPickView.setData(monthList);
    }

    @Override
    public void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_timeselector_dialog);

        init();
//        windowDeploy();
        setCanceledOnTouchOutside(true);

        cancelTextView.setOnClickListener(new DialogOnclickListener());
        okTextView.setOnClickListener(new DialogOnclickListener());
    }

//    public void windowDeploy() {
//        window.setGravity(Gravity.BOTTOM);
//        window.setWindowAnimations(R.style.dialogWindowAnim);
//    }


    class DialogOnclickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.timeselector_dialog_cancel:
                    TimeSelectorDialog.this.dismiss();
                    break;
                case R.id.timeselector_dialog_ok:
                    int yearPosition = yearPickView.getCurrentIndex();
                    int monthPosition = monthPickView.getCurrentIndex();
                    timeSelector.onSelector(yearPosition, monthPosition);
                    break;
            }
        }
    }

    public void setTimeSelector(TimeSelector timeSelector) {
        this.timeSelector = timeSelector;
    }

    public interface TimeSelector {
        void onSelector(int yearPosition, int monthPosition);
    }

}
