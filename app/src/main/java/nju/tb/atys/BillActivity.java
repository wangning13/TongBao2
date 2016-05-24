package nju.tb.atys;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import nju.tb.Adapters.UseWalletAdapter;
import nju.tb.Commen.MyAppContext;
import nju.tb.Commen.StringUtils;
import nju.tb.MyUI.TimeSelectorDialog;
import nju.tb.R;
import nju.tb.entity.Account;

/**
 * Created by Administrator on 2016/3/16.
 */
public class BillActivity extends Activity {

    private TextView toolbar_text;
    private ListView lv;
    private List<List<String>> mData;
    private Calendar c = null;
    private TimeSelectorDialog timeSelectorDialog;
    private TextView yearTextView, monthTextView, incomeTextView, payTextView;

    private int totalIncome = 0;
    private int totalPay = 0;
    private int currentYear = 0;
    private int currentMonth = 0;
    private MyAppContext myAppContext;
    //年
    private static final int[] YEAR_ARR = new int[]{2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019};
    //月
    private static final int[] MONTH_ARR = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        toolbar_text = (TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("账单");

        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BillActivity.this.finish();
            }
        });


        lv = (ListView) findViewById(R.id.consumelistDetailView);
        yearTextView = (TextView) findViewById(R.id.year);
        monthTextView = (TextView) findViewById(R.id.month);
        incomeTextView = (TextView) findViewById(R.id.income);
        payTextView = (TextView) findViewById(R.id.pay);

        currentYear = YEAR_ARR[YEAR_ARR.length / 2];  //初始年
        currentMonth = MONTH_ARR[MONTH_ARR.length / 2];  //初始月
        myAppContext = (MyAppContext) getApplicationContext();


        findViewById(R.id.datepicker).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timeSelectorDialog = new TimeSelectorDialog(BillActivity.this, R.style.CustomDatePickerDialog,
                        getArrayList(YEAR_ARR), getArrayList(MONTH_ARR));
                timeSelectorDialog.setTimeSelector(new TimeSelectorDialog.TimeSelector() {
                    @Override
                    public void onSelector(int yearPosition, int monthPosition) {
                        timeSelectorDialog.dismiss();

                        currentYear = YEAR_ARR[yearPosition];
                        currentMonth = MONTH_ARR[monthPosition];
                        yearTextView.setText(String.valueOf(currentYear));
                        monthTextView.setText(String.valueOf(currentMonth));
                        List
                        mData = getData(getListByYearMonth(myAppContext.getAccountList(), currentYear, currentMonth));
                        incomeTextView.setText(String.valueOf(totalIncome));
                        payTextView.setText(String.valueOf(totalPay));

//                        if (mData.size() == 0) {
//                            return;
//                        }
                        UseWalletAdapter adapter = new UseWalletAdapter(BillActivity.this, mData);
                        lv.setAdapter(adapter);

                    }
                });
                timeSelectorDialog.show();
                setDialogSize(timeSelectorDialog);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        yearTextView.setText(String.valueOf(currentYear));
        monthTextView.setText(String.valueOf(currentMonth));
        mData = getData(getListByYearMonth(myAppContext.getAccountList(), currentYear, currentMonth));
        incomeTextView.setText(String.valueOf(totalIncome));
        payTextView.setText(String.valueOf(totalPay));

        if (mData.size() == 0) {
            return;
        }

        UseWalletAdapter adapter = new UseWalletAdapter(this, mData);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
    /**
     * 设置dialog位置和大小
     *
     * @param dialog
     */
    private void setDialogSize(Dialog dialog) {
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        int totalHeight = d.getHeight();
        int totalWidth = d.getWidth();
        p.x = 0;
        p.y = (int) (totalHeight / 2);
        p.width = totalWidth;
        p.height = (int) (totalHeight / 2);
        dialogWindow.setAttributes(p);
    }

    /**
     * 数组转arraylist
     *
     * @param arr
     * @return
     */
    private ArrayList<Integer> getArrayList(int[] arr) {
        int n = arr.length;
        if (n <= 0) {
            return null;
        }
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(arr[i]);
        }
        return list;
    }


    private List<List<String>> getData(List<Account> list) {
        if (list == null) {
            return null;
        }
        List<List<String>> returnList = new ArrayList<>();

        for (Account account : list) {
            List<String> temp = new ArrayList<>();
            int type = account.getType(); //type
            temp.add(UserWalletActivity.convertType(type));
            int money = account.getMoney(); //money
            String moneyString = "";
            if (type == 0 || type == 4) {
                moneyString = "+" + money;
            } else {
                moneyString = "-" + money;
            }
            temp.add(moneyString);
            temp.add(account.getTime()); //time
            if (type == 0 || type == 1) {
                temp.add("");            //detail
                returnList.add(temp);
                continue;
            }
            temp.add(account.getAddressFrom() + "--" + account.getAddressTo());
            returnList.add(temp);
        }

        return returnList;
    }

    /**
     * 根据年月来对账单列表进行筛选
     *
     * @param list
     * @return
     */
    private List<Account> getListByYearMonth(List<Account> list, int year, int month) {
        List<Account> returnList = new ArrayList<>();
        int income = 0;
        int pay = 0;
        for (Account account : list) {
            if (!isMatch(account.getTime(), year, month)) {
                continue;
            }
            returnList.add(account);
            int type = account.getType();
            if (type == 0 || type == 4) {
                income += account.getMoney();
            } else {
                pay += account.getMoney();
            }
        }
        this.totalIncome = income;
        this.totalPay = pay;
        return returnList;
    }

    /**
     * 判断这个日期是否是在帅选条件的年月中
     *
     * @param time
     * @param year
     * @param month
     * @return
     */
    private boolean isMatch(String time, int year, int month) {
        if (StringUtils.isEmpty(time) || year == 0 || month == 0) {
            return false;
        }
        String[] arr = time.split(" ");
        if (arr.length != 2) {
            return false;
        }
        String[] arr1 = arr[0].split("-");
        if (arr1.length != 3) {
            return false;
        }
        if (Integer.parseInt(arr1[0]) != year || Integer.parseInt(arr1[1]) != month) {
            return false;
        }
        return true;
    }

}
