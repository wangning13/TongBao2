package nju.tb.atys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nju.tb.R;
import nju.tb.constants.ViewHolder;
import nju.tb.entity.Order;

public class HelpcenterActivity extends Activity {

    private ListView lv;
    private TextView toolbar_text;
    private List<String> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpcenter);

        toolbar_text = (TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("帮助中心");

        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HelpcenterActivity.this.finish();
            }
        });



        lv=(ListView)findViewById(R.id.qalistView);
        mData = getData();

        MyAdapter adapter = new MyAdapter(this);
        lv.setAdapter(adapter);
    }

    public List<String> getData() {
        List<String> list = new ArrayList<String>();
        String  data = "问题：什么是通宝？\n答案：通宝同城货运叫车平台";
        list.add(data);
        data = "问题：如果我有问题是否可以向你们反馈？\n答案：可以，请致电18602514017";
        list.add(data);
        data = "问题：登录通宝总是无法连接，怎么办？\n答案：请您先刷新一下；或者检查一下网络是否正常，能否登录其他网站；或者清除一下浏览器的缓存，如果以上三种方式都无效，还有一种情况是网页正在更新，可能影响您的浏览，还望能谅解。";
        list.add(data);
        data = "问题：订单取消后还能恢复吗？\n答案：订单一旦取消后将无法恢复，请您慎重操作。";
        list.add(data);
        data = "问题：订单提交成功后还可以修改订单信息吗？\n答案：很抱歉，订单一旦提交后将无法修改，请您取消订单重新购买。";
        list.add(data);
        return list;
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
                convertView = mInflater.inflate(R.layout.qaitem, null);
                holder.info = (TextView)convertView.findViewById(R.id.info);
                holder.info.setText(mData.get(position));
                convertView.setTag(holder);

            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            return convertView;
        }



    }
}
