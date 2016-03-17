package nju.tb.atys;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nju.tb.Adapters.UseWalletAdapter;
import nju.tb.R;

/**
 * Created by Administrator on 2016/3/16.
 */
public class BillActivity  extends Activity {

    private TextView toolbar_text;
    private ListView lv;
    private  List<List<String>> mData;
    private Calendar c = null;
    private Dialog mDialog = null;
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

        findViewById(R.id.datepicker).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });




        lv = (ListView) findViewById(R.id.consumelistView);
        mData = getData();
        UseWalletAdapter adapter = new UseWalletAdapter(this, mData);
        lv.setAdapter(adapter);


    }



    public  List<List<String>> getData() {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> item= new  ArrayList<String>();
        item.add("充值");
        item.add("+400");
        item.add("2015-06-09");
        item.add("2333");
        list.add(item);
        item= new  ArrayList<String>();
        item.add("消费");
        item.add("-400");
        item.add("2015-06-01");
        item.add("1933");
        list.add(item);
        return list;
    }



}
