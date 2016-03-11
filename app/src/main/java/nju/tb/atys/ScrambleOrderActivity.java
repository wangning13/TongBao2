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

    private Spinner fromAddressSpinner;
    private TextView toolbar_text;
    private List<Map<String, Order>> mData=new ArrayList<Map<String, Order>>();
    private OrderListAdapter adapter;


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


//        fromAddressSpinner = (Spinner) findViewById(R.id.addcar_selecttype);
//
//        final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
//        Map<String, String> map1 = new HashMap<String, String>();
//        map1.put("type", "玄武区");
//        Map<String, String> map2 = new HashMap<String, String>();
//        map2.put("type", "鼓楼区");
//        Map<String, String> map3 = new HashMap<String, String>();
//        map3.put("type", "建邺区");
//        Map<String, String> map4 = new HashMap<String, String>();
//        map4.put("type", "秦淮区");
//        Map<String, String> map5 = new HashMap<String, String>();
//        map5.put("type", "雨花台区");
//        Map<String, String> map6 = new HashMap<String, String>();
//        map6.put("type", "浦口区");
//        Map<String, String> map7 = new HashMap<String, String>();
//        map7.put("type", "栖霞区");
//        Map<String, String> map8 = new HashMap<String, String>();
//        map8.put("type", "江宁区");
//        Map<String, String> map9 = new HashMap<String, String>();
//        map9.put("type", "六合区");
//        Map<String, String> map10 = new HashMap<String, String>();
//        map10.put("type", "溧水区");
//        Map<String, String> map11 = new HashMap<String, String>();
//        map11.put("type", "高淳区");
//        data.add(map1);
//        data.add(map2);
//        data.add(map3);
//        data.add(map4);
//        data.add(map5);
//        data.add(map6);
//        data.add(map7);
//        data.add(map8);
//        data.add(map9);
//        data.add(map10);
//        data.add(map11);
//
//        fromAddressSpinner.setAdapter(new SimpleAdapter(this, data, R.layout.view_selecttype_item, new
//                String[]{"type"}, new int[]{R.id.item_type}));
//        fromAddressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String type = data.get(position).get("type");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                fromAddressSpinner.setSelection(0, true);
//                String type = data.get(0).get("type");
//                String[] arr = type.split(" ");
//                if (arr.length != 3) {
//                    return;
//                }
//            }
//        });
        Button searchbtn = (Button) findViewById(R.id.search_btn);
        lv=(ListView)findViewById(R.id.listView);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mData=getData();
                adapter = new OrderListAdapter(ScrambleOrderActivity.this,mData);
                lv.setAdapter(adapter);
            }
        });

    }


    public List<Map<String, Order>> getData() {

        final String USERTOKEN = ((MyAppContext) getApplicationContext()).getToken();
        ShowAllOrders sa=new ShowAllOrders(ScrambleOrderActivity.this,USERTOKEN,"鼓楼区","玄武区");
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
