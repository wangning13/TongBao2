package nju.tb.atys;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import nju.tb.R;

public class AddCarActivity extends Activity {
    private Spinner selectTypeSpinner;
    private Button okButton;

    @Override
    public void onCreate(Bundle savedInsatancedState) {
        super.onCreate(savedInsatancedState);
        setContentView(R.layout.view_driver_addcar);
        selectTypeSpinner = (Spinner) findViewById(R.id.addcar_selecttype);
        final EditText carNumberEditText = (EditText) findViewById(R.id.addcar_chepaihao);
        final EditText carPhoneEditText = (EditText) findViewById(R.id.addcar_phone);
        okButton = (Button) findViewById(R.id.addcar_OK);

        final TextView weightTextView = (TextView) findViewById(R.id.addcar_zaizhong); //车辆载重
        final TextView lengthTextView = (TextView) findViewById(R.id.addcar_chechang); //车辆车长

        final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("type", "小面包车 50*50 30公斤");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("type", "中面包车 60*60 40公斤");
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("type", "大面包车 70*70 50公斤");
        data.add(map1);
        data.add(map2);
        data.add(map3);

        selectTypeSpinner.setAdapter(new SimpleAdapter(this, data, R.layout.view_selecttype_item, new
                String[]{"type"}, new int[]{R.id.item_type}));
        selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = data.get(position).get("type");
                String[] arr = type.split(" ");
                if (arr.length != 3) {
                    return;
                }
                weightTextView.setText(arr[1]);
                lengthTextView.setText(arr[2]);
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

        okButton.setOnClickListener(new View.OnClickListener() {
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
