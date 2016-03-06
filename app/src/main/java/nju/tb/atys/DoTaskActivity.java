package nju.tb.atys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nju.tb.Adapters.TaskListAdapter;
import nju.tb.Commen.MyAppContext;
import nju.tb.entity.Order;
import nju.tb.R;
import nju.tb.net.ShowMyOrderList;

public class DoTaskActivity extends Activity {
    private ListView lv;
    private ArrayAdapter<Order> adapter;


    private TextView toolbar_text;




    private List<Map<String, Order>> mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_task);

        toolbar_text=(TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("任务");

        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DoTaskActivity.this.finish();
            }
        });



        lv=(ListView)findViewById(R.id.tasklistView);
        mData = getData();
        final  TaskListAdapter adapter = new TaskListAdapter(DoTaskActivity.this,mData);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(DoTaskActivity.this, TaskOrderContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("orderNumber", mData.get(position).get("info").getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public List<Map<String, Order>> getData() {

        final String USERTOKEN = ((MyAppContext) getApplicationContext()).getToken();
        ShowMyOrderList sm=new ShowMyOrderList(DoTaskActivity.this,USERTOKEN,"1");
        sm.start();
        ArrayList<Order> orderList=new ArrayList<Order>();
        while (sm.getResult() == -1) {
            if (!MyAppContext.getIsConnected()) {
                Log.i("断网了", "断网了");
                break;
            }
        }
        while(!sm.isRunover()){
        }

        if (sm.getResult() == 0) {
        }

        if (sm.getResult() == 1) {
            orderList = sm.getOrderList();
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
