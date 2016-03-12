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
            if(!list.isEmpty()){
                Order order=list.get(position).get("info");
                holder.info = (TextView)convertView.findViewById(R.id.info);
                holder.info.setText(order.getFromContactName());
                holder.time = (TextView)convertView.findViewById(R.id.time);
                String pretime=order.getTime();
                String ordertime=pretime.substring(0,16);
                holder.time.setText(ordertime);
                holder.faddress = (TextView)convertView.findViewById(R.id.faddress);
                holder.faddress.setText(order.getAddressFrom());
                holder.taddress = (TextView)convertView.findViewById(R.id.taddress);
                holder.taddress.setText(order.getAddressTo());
                holder.viewBtn = (Button) convertView.findViewById(R.id.view_btn);
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showInfo(position);
            }
        });
        return convertView;
    }


    public void showInfo(int position){
        View orderDialogView = mInflater.inflate(R.layout.activity_order_detail, null);

        TextView number_text=(TextView) orderDialogView.findViewById(R.id.number);
        TextView faddress_text=(TextView) orderDialogView.findViewById(R.id.faddress);
        TextView taddress_text=(TextView) orderDialogView.findViewById(R.id.taddress);
        TextView loadtime_text=(TextView) orderDialogView.findViewById(R.id.loadtime);
        TextView money_text=(TextView) orderDialogView.findViewById(R.id.money);
        Order order=list.get(position).get("info");

        number_text.setText(order.getId()+"");
        faddress_text.setText(order.getAddressFrom());
        taddress_text.setText(order.getAddressTo());
        loadtime_text.setText(order.getLoadTime());
        taddress_text.setText(order.getAddressTo());
        money_text.setText(order.getMoney());

        new AlertDialog.Builder(context)
                .setView(orderDialogView)
                .show();
    }

    public final class ViewHolder{
        TextView info;
        TextView time;
        TextView faddress;
        TextView taddress;
        public Button viewBtn;
    }

}