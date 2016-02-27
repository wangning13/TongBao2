package nju.tb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nju.tb.R;
import nju.tb.entity.Order;

/**
 * Created by Administrator on 2016/2/27.
 */
public class OldOrderListAdapter  extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Map<String, Order>> list;
    private boolean isLongClick;
    private List<Integer> selectedId;

    public OldOrderListAdapter(Context context,List<Map<String, Order>> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list=list;
        selectedId = new ArrayList<Integer>();
        isLongClick = false;
    }

    public void setClickState(boolean clickState) {
        this.isLongClick = clickState;
    }

    public List<Integer> getSelectedId() {
        return this.selectedId;
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
        TextView info = null;
        Button viewBtn=null;
        convertView = mInflater.inflate(R.layout.oldorderitem, null);
        info = (TextView)convertView.findViewById(R.id.info);
        info.setText(list.get(position).get("info").toString());
        RelativeLayout longClick = (RelativeLayout) convertView.findViewById(R.id.message_item_longclick);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.message_check);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedId.add(position);
                } else {
                    selectedId.remove(position);
                }
            }
        });

        if(isLongClick){
            longClick.setVisibility(View.VISIBLE);
            checkBox.setClickable(true);
        }
        return convertView;
    }



}
