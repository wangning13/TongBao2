package nju.tb.atys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import nju.tb.entity.Order;
import nju.tb.R;

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
        final  TaskListAdapter adapter = new TaskListAdapter(this,mData);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                showInfo(position);
            }
        });
    }

    public void showInfo(int position){

        Order order=mData.get(position).get("info");
        String detail=order.toStringdetail();
        ImageView img=new ImageView(DoTaskActivity.this);
        new AlertDialog.Builder(this).setView(img)
                .setMessage(detail)
                .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
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
        List<Map<String, Order>> list = new ArrayList<Map<String, Order>>();
        Map<String, Order> map = new HashMap<String, Order>();
        Order data=new Order("南京大学", "玄武湖",455, "2016-01-11","2016-01-12", "卡车", 30,"水车");
        map.put("info", data);
        list.add(map);
        map = new HashMap<String, Order>();
        data=new Order("东南大学", "玄武湖",655, "2016-01-11","2016-01-12", "卡车", 30,"水车");
        map.put("info", data);
        list.add(map);
        map = new HashMap<String, Order>();
        data=new Order("新街口", "玄武湖",456, "2016-01-11","2016-01-12", "卡车", 30,"水车");
        map.put("info", data);
        list.add(map);
        map = new HashMap<String, Order>();
        data=new Order("梅花山", "玄武湖",244, "2016-01-11","2016-01-12", "卡车", 30,"蔬菜");
        map.put("info",data);
        list.add(map);
        map = new HashMap<String, Order>();
        data=new Order("南审", "玄武湖",666, "2016-01-11","2016-01-12", "卡车", 30,"蔬菜");
        map.put("info",data);
        list.add(map);
        map = new HashMap<String, Order>();
        data=new Order("新模范马路", "玄武湖",575, "2016-01-11","2016-01-12", "卡车", 30,"蔬菜");
        map.put("info",data);
        list.add(map);
        map = new HashMap<String, Order>();
        data=new Order("软件大道", "玄武湖",888, "2016-01-11","2016-01-12", "卡车", 30,"蔬菜");
        map.put("info",data);
        list.add(map);
        map = new HashMap<String, Order>();
        data=new Order("梅花山", "南京南站",459, "2016-01-11","2016-01-12", "卡车", 30,"蔬菜");
        map.put("info",data);
        list.add(map);
        map = new HashMap<String, Order>();
        data=new Order("小红山", "玄武湖",999, "2016-01-11","2016-01-12", "卡车", 30,"蔬菜");
        map.put("info",data);
        list.add(map);
        return list;
    }


}
