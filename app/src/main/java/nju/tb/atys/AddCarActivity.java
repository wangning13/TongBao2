package nju.tb.atys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.net.AddTruck;

public class AddCarActivity extends Activity {
    private TextView toolbar_text;
    private Spinner selectTypeSpinner;
    //底部的button完成改成右上角的textview
    private TextView okaddcar;

    private List<String> truckTypesList;
    private int selectedTruckType = 0;

    @Override
    public void onCreate(Bundle savedInsatancedState) {
        super.onCreate(savedInsatancedState);
        setContentView(R.layout.view_driver_addcar);
        MyAppContext myAppContext = (MyAppContext) getApplicationContext();
        truckTypesList = myAppContext.getTruckList();  //车辆类型列表初始化

        //toolbar的标题
        //回退按钮
        toolbar_text = (TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("添加车辆");


        //回退按钮
        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddCarActivity.this.finish();
            }
        });


        selectTypeSpinner = (Spinner) findViewById(R.id.addcar_selecttype);
        final EditText carNumberEditText = (EditText) findViewById(R.id.addcar_chepaihao);
        final EditText carPhoneEditText = (EditText) findViewById(R.id.addcar_phone);
        okaddcar = (TextView) findViewById(R.id.addcar_OK);

        final TextView weightTextView = (TextView) findViewById(R.id.addcar_zaizhong); //车辆载重
        final TextView lengthTextView = (TextView) findViewById(R.id.addcar_chechang); //车辆车长

        final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        //解析truckTypeslist
        for (int i = 0; i < truckTypesList.size(); i++) {
            String temp = truckTypesList.get(i);
            Map<String, String> map = new HashMap<>();
            map.put("type", temp.split(" ")[0]);
            data.add(map);
        }

        selectTypeSpinner.setAdapter(new SimpleAdapter(this, data, R.layout.view_selecttype_item, new
                String[]{"type"}, new int[]{R.id.item_type}));
        selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = truckTypesList.get(position);
                weightTextView.setText(type.split(" ")[1]);
                lengthTextView.setText("长:" + type.split(" ")[2] + " " + "宽:" + type.split(" ")[3] + " " + "高:" +
                        type.split(" ")[4]);
                selectedTruckType = Integer.parseInt(type.split(" ")[5]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectTypeSpinner.setSelection(0, true);
                String type = data.get(0).get("type");
                String[] arr = type.split(" ");
                if (arr.length != 3) {
                    return;
                }
                weightTextView.setText(arr[1]);
                lengthTextView.setText(arr[2]);
            }
        });

        class EditChangedListener implements TextWatcher {
            private EditText editText;

            EditChangedListener(EditText editText) {
                this.editText = editText;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setBackgroundResource(R.drawable.edittext_right_shape);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }

        carNumberEditText.addTextChangedListener(new EditChangedListener(carNumberEditText));
        carPhoneEditText.addTextChangedListener(new EditChangedListener(carPhoneEditText));

        okaddcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                if (carNumberEditText.getText().toString().equals("")) {
                    toast = Toast.makeText(AddCarActivity.this, "车牌号码未填写", Toast.LENGTH_SHORT);
                    toast.show();
                    carNumberEditText.setBackgroundResource(R.drawable.edittext_wrong_shape);
                    return;
                }
                if (!validateCarNum(carNumberEditText.getText().toString())) {
                    toast = Toast.makeText(AddCarActivity.this, "车牌号码格式错误", Toast.LENGTH_SHORT);
                    toast.show();
                    carNumberEditText.setBackgroundResource(R.drawable.edittext_wrong_shape);
                    return;
                }
                if (carPhoneEditText.getText().toString().equals("")) {
                    toast = Toast.makeText(AddCarActivity.this, "随车电话未填写", Toast.LENGTH_SHORT);
                    toast.show();
                    carPhoneEditText.setBackgroundResource(R.drawable.edittext_wrong_shape);
                    return;
                }
                if (!isMobileNum(carPhoneEditText.getText().toString())) {
                    toast = Toast.makeText(AddCarActivity.this, "随车电话格式错误", Toast.LENGTH_SHORT);
                    toast.show();
                    carPhoneEditText.setBackgroundResource(R.drawable.edittext_wrong_shape);
                    return;
                }
                //验证完成，继续操作
                MyAppContext myAppContext = (MyAppContext) getApplicationContext();
                new AddTruck(AddCarActivity.this, myAppContext.getToken(), carNumberEditText.getText().toString(),
                        selectedTruckType, carPhoneEditText.getText().toString()).start();
                Intent intent = new Intent(AddCarActivity.this, CarsManagementActivity.class);
                startActivity(intent);
            }
        });


    }

    //正则判断车牌号是否正确
    public static boolean validateCarNum(String carNum) {
        boolean result = false;

        String[] provence = new String[]{"京", "津", "冀", "晋", "辽", "吉", "黑", "沪", "苏", "浙", "皖", "闽", "赣", "鲁", "豫",
                "鄂", "湘", "粤", "桂", "琼", "渝",
                "川", "黔", "滇", "藏", "陕", "甘", "青", "宁", "新", "港", "澳", "蒙"};

        String reg = "[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";

        boolean firstChar = false;

        if (carNum.length() > 0) {
            firstChar = Arrays.asList(provence).contains(carNum.substring(0, 1));
        }

        try {
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(carNum);
            if (m.matches() && firstChar) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //正则判断手机号格式是否正确

    public static boolean isMobileNum(String mobileNum) {

        boolean result = false;

        try {
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Matcher m = p.matcher(mobileNum);
            result = m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
