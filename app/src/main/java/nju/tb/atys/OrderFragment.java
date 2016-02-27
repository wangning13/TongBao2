package nju.tb.atys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nju.tb.Adapters.OldOrderListAdapter;
import nju.tb.entity.Order;
import nju.tb.R;

/**
 * Created by Administrator on 2016/1/21.
 */
public class OrderFragment extends Fragment {

    private ListView lv;
    private ArrayAdapter<Order> adapter;
    private List<Map<String, Order>> mData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orderfragment, container, false);
        lv=(ListView)view.findViewById(R.id.listView);
        mData = getData();
        final OldOrderListAdapter adapter = new OldOrderListAdapter(getActivity(),mData);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                showInfo(position);
            }
        });

        return view;
    }
    public void showInfo(int position){

        Order order=getData().get(position).get("info");
        String detail=order.toStringdetail();
        ImageView img=new ImageView(getActivity());
        new AlertDialog.Builder(getActivity()).setView(img)
                .setMessage(detail)
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

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



