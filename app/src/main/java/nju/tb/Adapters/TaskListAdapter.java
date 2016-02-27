package nju.tb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import nju.tb.R;
import nju.tb.entity.Order;

/**
 * Created by Administrator on 2016/2/27.
 */
public class TaskListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Map<String, Order>> list;

    public TaskListAdapter(Context context,List<Map<String, Order>> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list=list;
    }

    public int getCount() {
        return list != null ? list.size() : 0;
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView info = null;
        Button viewBtn=null;
        convertView = mInflater.inflate(R.layout.taskorderitem, null);
        info = (TextView)convertView.findViewById(R.id.info);
        info.setText(list.get(position).get("info").toString());
        return convertView;
    }
}
