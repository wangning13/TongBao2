package nju.tb.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import nju.tb.R;
import nju.tb.entity.Order;

/**
 * Created by Administrator on 2016/2/27.
 */
public class OrderListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<Map<String, Order>> list;

    public OrderListAdapter(Context context,List<Map<String, Order>> list) {
        this.context=context;
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.orderitem, null);
            holder.info = (TextView) convertView.findViewById(R.id.info);
            if(!list.isEmpty()){
                holder.info.setText(list.get(position).get("info").toString());
            }else{
                holder.info.setText("无");
            }
            holder.viewBtn = (Button) convertView.findViewById(R.id.view_btn);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                showInfo(position);
            }
        });
        return convertView;
    }


    public void showInfo(int position){
        View orderDialogView = mInflater.inflate(R.layout.activity_order_detail, null);
        Order order=list.get(position).get("info");
        String addressTo=order.getAddressTo();
        ImageView img=new ImageView(context);
        new AlertDialog.Builder(context).setView(img)
                .setMessage("终点："+addressTo)
                .setView(orderDialogView)
                .setNegativeButton("关闭", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public final class ViewHolder{
        public TextView info;
        public Button viewBtn;
    }

}