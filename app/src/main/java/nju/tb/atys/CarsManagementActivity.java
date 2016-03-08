package nju.tb.atys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import nju.tb.Commen.MyAppContext;
import nju.tb.MyUI.DeleteCarDialog;
import nju.tb.R;
import nju.tb.net.GetTruckList;
import nju.tb.net.RemoveTruck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarsManagementActivity extends Activity {
    private TextView toolbar_text;
    private ImageView addcar;
    private ListView carList;
    private List<String> truckList;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                MyAppContext myAppContext = (MyAppContext) getApplicationContext();
                GetTruckList getTruckList = new GetTruckList(CarsManagementActivity.this, myAppContext.getToken());
                getTruckList.start();
                while (getTruckList.getResult() == -1) {
                    if (!MyAppContext.getIsConnected()) {
                        Toast.makeText(CarsManagementActivity.this, "网络连接不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                while (!getTruckList.isRunover()) {

                }
                if (getTruckList.getResult() == 0) {
                    Toast.makeText(CarsManagementActivity.this, GetTruckList.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
                if (getTruckList.getResult() == 1) {
                    //解析数据
                    truckList = getTruckList.getTruckList();
                    List<Map<String, String>> adapterData = new ArrayList<Map<String, String>>();
                    for (String s : truckList) {
                        Map<String, String> map = new HashMap<>();
                        map.put("chepaihao", s.split(" ")[1]);
                        map.put("shenhejieguo", convertState(Integer.parseInt(s.split(" ")[2])));
                        adapterData.add(map);
                    }
                    SimpleAdapter adapter = new SimpleAdapter(CarsManagementActivity.this, adapterData, R.layout
                            .view_carlist_item, new
                            String[]{"chepaihao",
                            "shenhejieguo"}, new int[]{R.id.tv_carlist_item_chepaihao, R.id
                            .tv_carlist_item_shenhejieguo});
                    carList.setAdapter(adapter);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_cars);
        carList = (ListView) findViewById(R.id.lv_cars_carlist);
        addcar = (ImageView) findViewById(R.id.iv_cars_addcar);

        //toolbar的标题
        //回退按钮
        toolbar_text = (TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("车辆管理");

        //回退按钮
        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CarsManagementActivity.this.finish();
            }
        });

        addcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarsManagementActivity.this, AddCarActivity.class);
                startActivity(intent);
            }
        });

        carList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int truckId = Integer.parseInt(truckList.get(position).split(" ")[0]);
                String truckNum = truckList.get(position).split(" ")[1];
                Intent intent = new Intent(CarsManagementActivity.this, CarInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("truckId", truckId);
                bundle.putString("truckNum", truckNum);
                intent.putExtra("truckInfo", bundle);
                startActivity(intent);
            }
        });

        //删除车辆
        carList.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final DeleteCarDialog deleteCarDialog = new DeleteCarDialog(CarsManagementActivity.this);
                deleteCarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                deleteCarDialog.show();
                deleteCarDialog.setDeleteCar(new DeleteCarDialog.DeleteCar() {
                    @Override
                    public void deleteCar() {
                        deleteCarDialog.dismiss();
                        MyAppContext myAppContext = (MyAppContext) getApplicationContext();
                        new RemoveTruck(CarsManagementActivity.this, myAppContext.getToken(), truckList.get(position)
                                .split(" ")[1], handler).start();
                    }
                });

                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        MyAppContext myAppContext = (MyAppContext) getApplicationContext();
        GetTruckList getTruckList = new GetTruckList(this, myAppContext.getToken());
        getTruckList.start();
        while (getTruckList.getResult() == -1) {
            if (!MyAppContext.getIsConnected()) {
                Toast.makeText(this, "网络连接不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        while (!getTruckList.isRunover()) {

        }
        if (getTruckList.getResult() == 0) {
            Toast.makeText(this, GetTruckList.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
        if (getTruckList.getResult() == 1) {
            //解析数据
            truckList = getTruckList.getTruckList();
            List<Map<String, String>> adapterData = new ArrayList<Map<String, String>>();
            for (String s : truckList) {
                Map<String, String> map = new HashMap<>();
                map.put("chepaihao", s.split(" ")[1]);
                map.put("shenhejieguo", convertState(Integer.parseInt(s.split(" ")[2])));
                adapterData.add(map);
            }
            SimpleAdapter adapter = new SimpleAdapter(this, adapterData, R.layout.view_carlist_item, new
                    String[]{"chepaihao",
                    "shenhejieguo"}, new int[]{R.id.tv_carlist_item_chepaihao, R.id.tv_carlist_item_shenhejieguo});
            carList.setAdapter(adapter);
        }
    }

    public static String convertState(int state) {
        switch (state) {
            case 0:
                return "未验证";
            case 1:
                return "正在验证";
            case 2:
                return "验证成功";
            case 3:
                return "验证失败";
        }
        return null;
    }

}
