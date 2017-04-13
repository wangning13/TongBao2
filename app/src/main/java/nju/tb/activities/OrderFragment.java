package nju.tb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nju.tb.Adapters.OldOrderListAdapter;
import nju.tb.Commen.MyAppContext;
import nju.tb.entity.Order;
import nju.tb.R;
import nju.tb.net.ShowMyOrderList;

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
                Intent intent = new Intent(getActivity(), OldOrderContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("orderNumber", mData.get(position).get("info").getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }
    public List<Map<String, Order>> getData() {

        final String USERTOKEN = ((MyAppContext) getActivity().getApplicationContext()).getToken();
        ShowMyOrderList sm=new ShowMyOrderList(getActivity(),USERTOKEN,"2");
        sm.start();
        ArrayList<Order> orderList=new ArrayList<Order>();
        while (sm.getResult() == -1) {
            if (!MyAppContext.getIsConnected()) {
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



