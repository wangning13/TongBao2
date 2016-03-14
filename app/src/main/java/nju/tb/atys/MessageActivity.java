package nju.tb.atys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

import nju.tb.Commen.MyAppContext;
import nju.tb.MyUI.MyXListView;
import nju.tb.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nju.tb.Adapters.MessageListAdapter;
import nju.tb.entity.MyMessage;
import nju.tb.net.GetMyMessage;

public class MessageActivity extends Activity {
    private MyXListView messageList;
    private Button cancelButton;
    private Button okButton;
    private RelativeLayout deleteLayout;
    private boolean longClickState = false;
    public List<Integer> selectedId = new ArrayList<Integer>();
    private TextView toolbar_text;
    private List<MyMessage> myMessageList;
    private List<Map<String, Object>> data;
    private MessageListAdapter adapter;
    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;
    private boolean noNews = false;
    private boolean noMore = false;
    private static final int VISIABLEITEMCOUNTS = 7;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (!isRefreshing && !isLoadingMore) {
                    myMessageList = (List<MyMessage>) msg.obj;
                    if (myMessageList.size() == 0) {
                        return;
                    }
                    if (myMessageList.size() > VISIABLEITEMCOUNTS) {
                       data = getData(myMessageList.subList(0, VISIABLEITEMCOUNTS));
                    }else{
                        data = getData(myMessageList);
                    }
                    adapter = getAdapter(data);
                    messageList.setAdapter(adapter);
                    return;
                }
                if (isRefreshing && !isLoadingMore) {
                    noNews = false;
                    List<MyMessage> tempMessageList = (List<MyMessage>) msg.obj;
                    if (tempMessageList.size() == 0) {
                        return;
                    }
                    if (isListEquals(myMessageList, tempMessageList)) {
                        noNews = true;
                    }
                    myMessageList = tempMessageList;
                    if (myMessageList.size() > VISIABLEITEMCOUNTS) {
                        data = getData(myMessageList.subList(0, VISIABLEITEMCOUNTS));
                    }else{
                        data = getData(myMessageList);
                    }
                    adapter = getAdapter(data);
                    messageList.setAdapter(adapter);
                    isRefreshing = false;
                    return;
                }
                if (!isRefreshing && isLoadingMore) {
                    noMore = false;
                    List<MyMessage> tempMessageList = (List<MyMessage>) msg.obj;
                    if (tempMessageList.size() == 0) {
                        return;
                    }
                    if (isListEquals(myMessageList, tempMessageList)) {
                        noMore = true;
                    }
                    myMessageList = tempMessageList;
                    data = getData(myMessageList);
                    adapter = getAdapter(data);
                    messageList.setAdapter(adapter);
                    if (data.size() > VISIABLEITEMCOUNTS) {
                        messageList.setSelection(adapter.getCount() - VISIABLEITEMCOUNTS);
                    }
                    isLoadingMore = false;
                    return;
                }
            } else if (msg.what == 1) {
                messageList.setNews(false);
            } else if (msg.what == 2) {
                messageList.setNews(true);
            } else if (msg.what == 3) {
                messageList.setMore(false);
            } else if (msg.what == 4) {
                messageList.setMore(true);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_message);

        //toolbar的标题
        toolbar_text = (TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("消息");

        //回退按钮
        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MessageActivity.this.finish();
            }
        });

        messageList = (MyXListView) findViewById(R.id.lv_messagelist);
        cancelButton = (Button) findViewById(R.id.delete_cancel);
        okButton = (Button) findViewById(R.id.delete_ok);
        deleteLayout = (RelativeLayout) findViewById(R.id.message_delete);

        messageList.setPullLoadMoreEnable(true);

        messageList.setXListViewListener(new MyXListView.IXListViewListener() {
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
        MyAppContext myAppContext = (MyAppContext) getApplicationContext();
        new GetMyMessage(this, myAppContext.getToken(), handler).start();
//        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (!longClickState) {
//                    HashMap<String, Object> map = (HashMap<String, Object>) parent.getItemAtPosition(position);
//                    boolean is_read = (boolean) map.get("IsRead");
//                    if (is_read == false) {
//                        //另起线程上传到数据库，本地直接变化
//                        data.get(position).put("IsRead", true);
//                        adapter.notifyDataSetChanged();
//                    }
//                    String messageSource = (String) data.get(position).get("Source");
//                    String text = (String) data.get(position).get("Text");
//                    String time = (String) data.get(position).get("Time");
//                    Intent intent = new Intent(MessageActivity.this, MessageContentActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("ContentSource", messageSource);
//                    bundle.putString("ContentText", text);
//                    bundle.putString("ContentTime", time);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//            }
//        });

//        class OnLongClick implements AdapterView.OnItemLongClickListener {
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                if (!longClickState) {
//                    adapter.setClickState(true);
//                    selectedId.clear();
//                    deleteLayout.setVisibility(View.VISIBLE);
//                    messageList.setAdapter(adapter);
//                    return true;
//                } else {
//                    return false;
//                }
//
//            }
//        }
//        messageList.setOnItemLongClickListener(new OnLongClick());
//        //确认删除按钮的监听事件
//        okButton.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View view) {
//                selectedId = adapter.getSelectedId();
//                if (selectedId.size() == 0) {
//                    return;
//                }
//                ArrayList<Boolean> arr = new ArrayList<Boolean>();
//                for (int i = 0; i < data.size(); i++) {
//                    arr.add(false);
//                }
//                for (int j : selectedId) {
//                    arr.set(j, true);
//                }
//                Iterator dataIterator = data.iterator();
//                Iterator arrIterator = arr.iterator();
//                while (arrIterator.hasNext()) {
//                    boolean b = (boolean) arrIterator.next();
//                    dataIterator.next();
//                    if (b) {
//                        arrIterator.remove();
//                        dataIterator.remove();
//                    }
//                }
//                adapter.setClickState(false);
//                adapter.notifyDataSetChanged();
//                deleteLayout.setVisibility(View.GONE);
//            }
//        });
//        //取消按钮的监听事件
//        cancelButton.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View view) {
//                adapter.setClickState(false);
//                messageList.setAdapter(adapter);
//                deleteLayout.setVisibility(View.GONE);
//            }
//        });
    }

    private List<Map<String, Object>> getData(List<MyMessage> list) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (MyMessage myMessage : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("Source", myMessage.covertTypeToString(myMessage.getType()));
            map.put("Text", myMessage.getContent());
            map.put("Time", myMessage.getTime());
            map.put("IsRead", myMessage.getHasRead());
            data.add(map);
        }
        return data;
    }

    private MessageListAdapter getAdapter(List<Map<String, Object>> data) {
        return new MessageListAdapter(MessageActivity.this, data);
    }

    private void startRefresh() {
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;
        MyAppContext myAppContext = (MyAppContext) getApplicationContext();
        GetMyMessage getMyMessage = new GetMyMessage(this, myAppContext.getToken(), handler);
        getMyMessage.start();
    }

    private void startLoadMore() {
        if (isLoadingMore) {
            return;
        }
        isLoadingMore = true;
        MyAppContext myAppContext = (MyAppContext) getApplicationContext();
        GetMyMessage getMyMessage = new GetMyMessage(this, myAppContext.getToken(), handler);
        getMyMessage.start();
    }

    private boolean isListEquals(List<MyMessage> list1, List<MyMessage> list2) {
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
