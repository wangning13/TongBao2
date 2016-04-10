package nju.tb.atys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nju.tb.Adapters.OrderListAdapter;
import nju.tb.Commen.MyAppContext;
import nju.tb.MyUI.MyXListView;
import nju.tb.entity.MyMessage;
import nju.tb.entity.Order;
import nju.tb.R;
import nju.tb.net.ShowAllOrders;

public class ScrambleOrderFragment extends Fragment {
    private MyXListView lv;
    private String fromaddress="";
    private String toaddress="";
    private Spinner fromspinner;
    private Spinner tospinner;
    private List<Map<String, Order>> mData=new ArrayList<Map<String, Order>>();
    private OrderListAdapter orderadapter;
    private OrderListAdapter initadapter;
    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;
    private boolean noNews = false;
    private boolean noMore = false;
    private static final int VISIABLEITEMCOUNTS = 7;
    private List<Order> grabOrderList;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (!isRefreshing && !isLoadingMore) {
                    grabOrderList = (List<Order>) msg.obj;
                    if (grabOrderList.size() == 0) {
                        return;
                    }
                    if (grabOrderList.size() > VISIABLEITEMCOUNTS) {
                        mData = getDataList(grabOrderList.subList(0, VISIABLEITEMCOUNTS));
                    }else{
                        mData = getDataList(grabOrderList);
                    }
                    initadapter =  new OrderListAdapter(getActivity(),mData);
                    lv.setAdapter(initadapter);
                    return;
                }
                if (isRefreshing && !isLoadingMore) {
                    noNews = false;
                    List<Order> tempOrderList = (List<Order>) msg.obj;
                    if (tempOrderList.size() == 0) {
                        return;
                    }
                    if (isListEquals(grabOrderList, tempOrderList)) {
                        noNews = true;
                    }
                    grabOrderList = tempOrderList;
                    if (grabOrderList.size() > VISIABLEITEMCOUNTS) {
                        mData = getDataList(grabOrderList.subList(0, VISIABLEITEMCOUNTS));
                    }else{
                        mData = getDataList(grabOrderList);
                    }
                    initadapter =  new OrderListAdapter(getActivity(),mData);
                    lv.setAdapter(initadapter);
                    isRefreshing = false;
                    return;
                }
                if (!isRefreshing && isLoadingMore) {
                    noMore = false;
                    List<Order> tempOrderList = (List<Order>) msg.obj;
                    if (tempOrderList.size() == 0) {
                        return;
                    }
                    if (isListEquals(grabOrderList, tempOrderList)) {
                        noMore = true;
                    }
                    grabOrderList = tempOrderList;
                    mData = getDataList(grabOrderList);
                    initadapter =  new OrderListAdapter(getActivity(),mData);
                    lv.setAdapter(initadapter);
                    if (mData.size() > VISIABLEITEMCOUNTS) {
                        lv.setSelection(initadapter.getCount() - VISIABLEITEMCOUNTS);
                    }
                    isLoadingMore = false;
                    return;
                }
            } else if (msg.what == 1) {
                lv.setNews(false);
            } else if (msg.what == 2) {
                lv.setNews(true);
            } else if (msg.what == 3) {
                lv.setMore(false);
            } else if (msg.what == 4) {
                lv.setMore(true);
            }
        }
    };


    private boolean getIsRefreshing() {
        return this.isRefreshing;
    }

    private boolean getIsLoadingMore() {
        return this.isLoadingMore;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View scrambleOrderFragment = inflater.inflate(R.layout.scrambleorderfragment, container, false);

        fromspinner = (Spinner) scrambleOrderFragment.findViewById(R.id.fromAddress);
        String[] mItems = getResources().getStringArray(R.array.districts);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mItems);
        fromspinner.setAdapter(adapter);
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

        tospinner = (Spinner) scrambleOrderFragment.findViewById(R.id.toAddress);

        ArrayAdapter<String> toadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mItems);
        tospinner.setAdapter(toadapter);
        tospinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] districts = getResources().getStringArray(R.array.districts);
                toaddress = districts[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Button searchbtn = (Button) scrambleOrderFragment.findViewById(R.id.search_btn);
        lv = (MyXListView) scrambleOrderFragment.findViewById(R.id.listView);
        lv.setPullLoadMoreEnable(true);

        lv.setXListViewListener(new MyXListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                startRefresh();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (getIsRefreshing()) {
                        }
                        Message message;
                        if (noNews) {
                            message = handler.obtainMessage(1);
                        } else {
                            message = handler.obtainMessage(2);
                        }
                        handler.sendMessage(message);
                    }
                }).start();

            }

            @Override
            public void onLoadMore() {
                startLoadMore();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (getIsLoadingMore()) {
                        }
                        Message message;
                        if (noMore) {
                            message = handler.obtainMessage(3);
                        } else {
                            message = handler.obtainMessage(4);
                        }
                        handler.sendMessage(message);
                    }
                }).start();

            }
        });


        final String USERTOKEN = ((MyAppContext) getActivity().getApplicationContext()).getToken();
        ShowAllOrders sa=new ShowAllOrders(getActivity(),USERTOKEN,"","",handler);
        sa.start();




        searchbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mData.clear();
                mData = getData(fromaddress, toaddress);
                orderadapter = new OrderListAdapter(getActivity(), mData);
                lv.setAdapter(orderadapter);
            }
        });

        return scrambleOrderFragment;
    }


    public List<Map<String, Order>> getData(String faddress ,String taddress) {

        final String USERTOKEN = ((MyAppContext) getActivity().getApplicationContext()).getToken();
        ShowAllOrders sa=new ShowAllOrders(getActivity(),USERTOKEN,faddress,taddress,handler);
        sa.start();

        ArrayList<Order> orderList=new ArrayList<Order>();
        while (sa.getResult() == -1) {
            if (!MyAppContext.getIsConnected()) {
                break;
            }
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
    private  List<Map<String, Order>> getDataList(List<Order> orderList){
        List<Map<String, Order>> list = new ArrayList<Map<String, Order>>();

        for(int i=0;i<orderList.size();i++){
            Map<String, Order> map = new HashMap<String, Order>();
            Order data=orderList.get(i);
            map.put("info", data);
            list.add(map);
        }

        return list;
    }
    private void startRefresh() {
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;
        MyAppContext myAppContext = (MyAppContext) getActivity().getApplicationContext();
        ShowAllOrders sa=new ShowAllOrders(myAppContext,myAppContext.getToken(),"","",handler);
        sa.start();

    }

    private void startLoadMore() {
        if (isLoadingMore) {
            return;
        }
        isLoadingMore = true;
        MyAppContext myAppContext = (MyAppContext) getActivity().getApplicationContext();
        ShowAllOrders sa=new ShowAllOrders(myAppContext,myAppContext.getToken(),"","",handler);
        sa.start();

    }
    private boolean isListEquals(List<Order> list1, List<Order> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        int n = list1.size();
        for (int i = 0; i < n; i++) {
            if (list1.get(i).getId() != list2.get(i).getId()) {
                return false;
            }
        }
        return true;
    }

}
