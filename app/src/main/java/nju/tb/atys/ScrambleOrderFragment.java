package nju.tb.atys;

import android.os.Bundle;
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
import nju.tb.entity.Order;
import nju.tb.R;
import nju.tb.net.ShowAllOrders;

public class ScrambleOrderFragment extends Fragment {
    private ListView lv;
    private String fromaddress="";
    private String toaddress="";
    private Spinner fromspinner;
    private Spinner tospinner;
    private List<Map<String, Order>> mData=new ArrayList<Map<String, Order>>();
    private OrderListAdapter orderadapter;
    private OrderListAdapter initadapter;


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
        lv = (ListView) scrambleOrderFragment.findViewById(R.id.listView);
        initadapter = new OrderListAdapter(getActivity(), getData("", ""));
        lv.setAdapter(initadapter);


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
        ShowAllOrders sa=new ShowAllOrders(getActivity(),USERTOKEN,faddress,taddress);
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
