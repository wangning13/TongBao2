package nju.tb.atys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import nju.tb.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nju.tb.Adapters.MessageListAdapter;

public class MessageActivity extends Activity {
    private ListView messageList;
    private Button cancelButton;
    private Button okButton;
    private RelativeLayout deleteLayout;
    private boolean longClickState = false;
    public List<Integer> selectedId = new ArrayList<Integer>();
    private TextView toolbar_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_message);



        //toolbar的标题
        toolbar_text=(TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("消息");

        //回退按钮
        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MessageActivity.this.finish();
            }
        });

        messageList = (ListView) findViewById(R.id.lv_messagelist);
        cancelButton = (Button) findViewById(R.id.delete_cancel);
        okButton = (Button) findViewById(R.id.delete_ok);
        deleteLayout = (RelativeLayout) findViewById(R.id.message_delete);

        //测试数据
        final List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("Source", "通宝小秘书");
        map1.put("Text", "[货主取消订单啦]\n您在2016-03-22 11:02am接的订单12344321，货主已取消订单。");
        map1.put("Time", "2016—3-22 2:30pm");
        map1.put("IsRead", true);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("Source", "通宝账单");
        map2.put("Text", "[运费到账啦]\n您在2016-03-20 09:42am接的订单9876678，运费已经到账。");
        map2.put("Time", "2016—3-22 2:30pm");
        map2.put("IsRead", false);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("Source", "通宝小秘书");
        map3.put("Text", "[货主取消订单啦]\n您在2016-03-12 10:44am接的订单5678987，货主已取消订单。");
        map3.put("Time", "2016—3-12 2:30pm");
        map3.put("IsRead", false);

        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("Source", "通宝账单");
        map4.put("Text", "[充值成功]\n您在2016-02-22 8:32am成功充值100元。");
        map4.put("Time", "2016—2-22 1:30pm");
        map4.put("IsRead", false);



        Map<String, Object> map5 = new HashMap<String, Object>();
        map5.put("Source", "通宝账单");
        map5.put("Text", "[运费到账啦]\n您在2016-02-20 8:32am接的订单5656454，运费已经到账。");
        map5.put("Time", "2016—2-22 2:30pm");
        map5.put("IsRead", false);

        Map<String, Object> map6 = new HashMap<String, Object>();
        map6.put("Source", "通宝账单");
        map6.put("Text", "[提现成功]\n您在2016-02-15 8:32am成功提现1000元。");
        map6.put("Time", "2016—2-15 1:30pm");
        map6.put("IsRead", false);


        data.add(map1);
        data.add(map2);
        data.add(map3);
        data.add(map4);
        data.add(map5);
        data.add(map6);

        final MessageListAdapter myAdapter = new MessageListAdapter(this, data);
        messageList.setAdapter(myAdapter);
        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!longClickState) {
                    HashMap<String, Object> map = (HashMap<String, Object>) parent.getItemAtPosition(position);
                    boolean is_read = (boolean) map.get("IsRead");
                    if (is_read == false) {
                        data.get(position).put("IsRead", true);
                        myAdapter.notifyDataSetChanged();
                    }
                    String messageSource=(String)data.get(position).get("Source");
                    String text=(String)data.get(position).get("Text");
                    String time=(String) data.get(position).get("Time");
                    Intent intent=new Intent(MessageActivity.this,MessageContentActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("ContentSource",messageSource);
                    bundle.putString("ContentText",text);
                    bundle.putString("ContentTime",time);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        class OnLongClick implements AdapterView.OnItemLongClickListener {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!longClickState) {
                    myAdapter.setClickState(true);
                    selectedId.clear();
                    deleteLayout.setVisibility(View.VISIBLE);
                    messageList.setAdapter(myAdapter);
                    return true;
                } else {
                    return false;
                }

            }
        }
        messageList.setOnItemLongClickListener(new OnLongClick());
        //确认删除按钮的监听事件
        okButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                selectedId = myAdapter.getSelectedId();
                if (selectedId.size() == 0) {
                    return;
                }
                ArrayList<Boolean> arr = new ArrayList<Boolean>();
                for (int i = 0; i < data.size(); i++) {
                    arr.add(false);
                }
                for (int j : selectedId) {
                    arr.set(j, true);
                }
                Iterator dataIterator = data.iterator();
                Iterator arrIterator = arr.iterator();
                while (arrIterator.hasNext()) {
                    boolean b = (boolean) arrIterator.next();
                    dataIterator.next();
                    if (b) {
                        arrIterator.remove();
                        dataIterator.remove();
                    }
                }
                myAdapter.setClickState(false);
                myAdapter.notifyDataSetChanged();
                deleteLayout.setVisibility(View.GONE);
            }
        });
        //取消按钮的监听事件
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                myAdapter.setClickState(false);
                messageList.setAdapter(myAdapter);
                deleteLayout.setVisibility(View.GONE);
            }
        });
    }

}
