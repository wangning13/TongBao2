package nju.tb.atys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nju.tb.Adapters.OrderListAdapter;
import nju.tb.Commen.MyAppContext;
import nju.tb.entity.Order;
import nju.tb.R;
import nju.tb.net.ShowAllOrders;
import nju.tb.net.ShowMyOrderList;

public class ScrambleOrderActivity extends Activity {
    private ListView lv;
    private String fromaddress="";
    private String toaddress="";
    private Spinner fromspinner;
    private Spinner tospinner;
    private TextView toolbar_text;
    private List<Map<String, Order>> mData=new ArrayList<Map<String, Order>>();
    private OrderListAdapter orderadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scramble_order);

        toolbar_text=(TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("抢单");

        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScrambleOrderActivity.this.finish();
            }
        });


        fromspinner = (Spinner) findViewById(R.id.fromAddress);
        String[] mItems = getResources().getStringArray(R.array.districts);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        fromspinner .setAdapter(adapter);
        fromspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] districts = getResources().getStringArray(R.array.districts);
                fromaddress = districts[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tospinner = (Spinner) findViewById(R.id.toAddress);
        ArrayAdapter<String> toadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        tospinner .setAdapter(toadapter);
        tospinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] districts = getResources().getStringArray(R.array.districts);
                toaddress=districts[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });





        Button searchbtn = (Button) findViewById(R.id.search_btn);
        lv=(ListView)findViewById(R.id.listView);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mData.clear();
                mData=getData(fromaddress,toaddress);
                orderadapter = new OrderListAdapter(ScrambleOrderActivity.this,mData);
                lv.setAdapter(orderadapter);
            }
        });

    }


    public List<Map<String, Order>> getData(String faddress ,String taddress) {

        final String USERTOKEN = ((MyAppContext) getApplicationContext()).getToken();
        ShowAllOrders sa=new ShowAllOrders(ScrambleOrderActivity.this,USERTOKEN,faddress,taddress);
        Log.i("出发地",faddress);
        Log.i("目的地",taddress);
        sa.start();

        ArrayList<Order> orderList=new ArrayList<Order>();
        while (sa.getResult() == -1) {
            if (!MyAppContext.getIsConnected()) {
                break;
            }
        }
        while(!sa.isRunover()){
        }

        if (sa.getResult() == 0) {
        }

        if (sa.getResult() == 1) {
            orderList = sa.getAllorders();
        }


        List<Map<String, Order>> list = new ArrayList<Map<String, Order>>();

        for(int i=0;i<orderList.size();i++){
            Map<String, Order> map = new HashMap<String, Order>();
            Order data=orderList.get(i);
            map.put("info", data);
            list.add(map);
        }

        return list;
    }


}
