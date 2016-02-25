package nju.tb.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nju.tb.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageListAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> list;
    private LayoutInflater layoutInflater;
    private boolean isLongClick;
    private List<Integer> selectedId;

    /*
    List-Map  keys:Source,Text,Time,IsRead
     */
    public MessageListAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        selectedId = new ArrayList<Integer>();
        isLongClick = false;
    }

    public void setClickState(boolean clickState) {
        this.isLongClick = clickState;
    }

    public List<Integer> getSelectedId() {
        return this.selectedId;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.view_messagelist_item, null);
        }
        TextView sourceTextView = (TextView) convertView.findViewById(R.id.message_source);
        TextView textTextView = (TextView) convertView.findViewById(R.id.message_text);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.message_time);
        ImageView isReadImageView = (ImageView) convertView.findViewById(R.id.message_read);
        LinearLayout unLongClick = (LinearLayout) convertView.findViewById(R.id.message_item_unlongclick);
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

        if (!isLongClick) {
            unLongClick.setVisibility(View.VISIBLE);
            longClick.setVisibility(View.GONE);
            sourceTextView.setText(list.get(position).get("Source").toString());
            textTextView.setText(list.get(position).get("Text").toString());
            timeTextView.setText(list.get(position).get("Time").toString());
            if (((boolean) list.get(position).get("IsRead")) == true) {
                isReadImageView.setImageBitmap(null);
            } else if (((boolean) list.get(position).get("IsRead")) == false) {
                isReadImageView.setImageResource(R.drawable.redpoint);
            }
        } else {
            unLongClick.setVisibility(View.GONE);
            longClick.setVisibility(View.VISIBLE);
            sourceTextView.setText(list.get(position).get("Source").toString());
            textTextView.setText(list.get(position).get("Text").toString());
            timeTextView.setText(list.get(position).get("Time").toString());
            checkBox.setClickable(true);
        }

        return convertView;
    }


}
