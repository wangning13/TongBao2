package nju.tb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nju.tb.R;
import nju.tb.entity.Order;

/**
 * Created by Administrator on 2016/3/16.
 */
public class UseWalletAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<List<String>> list;
    private List<Integer> selectedId;

    public UseWalletAdapter(Context context, List<List<String>> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        selectedId = new ArrayList<Integer>();
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView state = null;
        TextView change = null;
        TextView time = null;
        TextView money = null;
        convertView = mInflater.inflate(R.layout.consumptionitem, null);

        state = (TextView) convertView.findViewById(R.id.state);
        state.setText(list.get(position).get(0));
        change = (TextView) convertView.findViewById(R.id.change);
        change.setText(list.get(position).get(1));
        time = (TextView) convertView.findViewById(R.id.time);
        time.setText(list.get(position).get(2));
//        money = (TextView) convertView.findViewById(R.id.money);
//        money.setText(list.get(position).get(3));

        return convertView;
    }
}

