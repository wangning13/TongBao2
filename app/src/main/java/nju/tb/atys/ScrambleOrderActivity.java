package nju.tb.atys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import nju.tb.entity.Order;
import nju.tb.R;
import nju.tb.constants.ViewHolder;

public class ScrambleOrderActivity extends Activity {
    private ListView lv;
    private ArrayAdapter<Order> adapter;

    private TextView toolbar_text;
    private List<Map<String, Order>> mData;


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



        lv=(ListView)findViewById(R.id.listView);
        mData = getData();
        MyAdapter adapter = new MyAdapter(this);
        lv.setAdapter(adapter);

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
        map.put("info", data);
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
        map.put("info", data);
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

    public void showInfo(int position){
        LayoutInflater inflater = getLayoutInflater();
        View orderDialogView = inflater.inflate(R.layout.activity_order_detail, null);
        Order order=getData().get(position).get("info");
        String detail=order.toStringdetail();
        ImageView img=new ImageView(ScrambleOrderActivity.this);
        new AlertDialog.Builder(this).setView(img)
                .setMessage(detail)
                .setView(orderDialogView)
                .setNegativeButton("关闭", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return mData.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder=new ViewHolder();
                convertView = mInflater.inflate(R.layout.orderitem, null);
                holder.info = (TextView)convertView.findViewById(R.id.info);
                holder.info.setText(mData.get(position).get("info").toString());
                holder.viewBtn = (Button)convertView.findViewById(R.id.view_btn);
                convertView.setTag(holder);

            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.viewBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    showInfo(position);
                }
            });
            return convertView;
        }



    }
}
