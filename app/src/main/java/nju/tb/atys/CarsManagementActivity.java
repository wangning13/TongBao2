package nju.tb.atys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import nju.tb.MyUI.DeleteCarDialog;
import nju.tb.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarsManagementActivity extends Activity {
    private TextView toolbar_text;
    private ImageView addcar;
    private ListView carList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_cars);
        carList = (ListView) findViewById(R.id.lv_cars_carlist);
        addcar = (ImageView) findViewById(R.id.iv_cars_addcar);


        //toolbar的标题
        //回退按钮
        toolbar_text=(TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("车辆管理");


        //回退按钮
        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CarsManagementActivity.this.finish();
            }
        });

        // 测试数据
        final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("chepaihao", "苏A12345");
        map1.put("shenhejieguo", "审核通过");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("chepaihao", "苏B1234");
        map2.put("shenhejieguo", "审核未通过");
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("chepaihao", "苏C1234");
        map3.put("shenhejieguo", "审核未通过");

        data.add(map1);
        data.add(map2);
        data.add(map3);

        final SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.view_carlist_item, new
                String[]{"chepaihao",
                "shenhejieguo"}, new int[]{R.id.tv_carlist_item_chepaihao, R.id.tv_carlist_item_shenhejieguo});
        carList.setAdapter(adapter);

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
                Intent intent = new Intent(CarsManagementActivity.this, CarInfoActivity.class);
                startActivity(intent);
            }
        });
        carList.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final DeleteCarDialog deleteCarDialog = new DeleteCarDialog(CarsManagementActivity.this);
                deleteCarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                Window window = deleteCarDialog.getWindow();
//                window.setGravity(Gravity.CENTER);
//                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//                Display d = manager.getDefaultDisplay();
//                WindowManager.LayoutParams p = window.getAttributes();
//                p.height = (int) (d.getHeight() * 0.2);
//                p.width = (int) (d.getWidth() * 0.8);
//                window.setAttributes(p);
                deleteCarDialog.show();
                deleteCarDialog.setDeleteCar(new DeleteCarDialog.DeleteCar() {
                    @Override
                    public void deleteCar() {
                        deleteCarDialog.dismiss();
                        data.remove(position);
                        ;
                        adapter.notifyDataSetChanged();
                    }
                });

                return true;
            }
        });
    }

}
