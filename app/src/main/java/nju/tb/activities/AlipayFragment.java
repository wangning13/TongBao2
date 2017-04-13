package nju.tb.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.net.Withdraw;

/**
 * Created by Administrator on 2016/3/17.
 */
public class AlipayFragment extends Fragment {
    private EditText alipayId, alipayMoney;
    private Button alipayOk;
    private MyAppContext myAppContext;
    private ProgressDialog p;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                p.dismiss();
                Toast.makeText(AlipayFragment.this.getActivity(), "提现失败，请重新操作", Toast.LENGTH_SHORT).show();
                return;
            } else if (msg.what == 1) {
                p.dismiss();
                Intent intent = new Intent(AlipayFragment.this.getActivity(), UserWalletActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alipayfragment, container, false);
        alipayId = (EditText) view.findViewById(R.id.alipayID);
        alipayMoney = (EditText) view.findViewById(R.id.alipaymoney);
        alipayOk = (Button) view.findViewById(R.id.alipayconfirm);
        myAppContext = MyAppContext.getMyAppContext();
        p = new ProgressDialog(getActivity());
        RechargeActivity.setProgressDialog(p);

        alipayOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(alipayId.getText().toString()) || TextUtils.isEmpty(alipayMoney.getText()
                        .toString())) {
                    Toast.makeText(AlipayFragment.this.getActivity(), "请填写支付信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                int money = Integer.parseInt(alipayMoney.getText().toString());
                if (money > myAppContext.getMoney()) {
                    Toast.makeText(AlipayFragment.this.getActivity(), "余额不足", Toast.LENGTH_SHORT).show();
                    return;
                }
                p.show();
                new Withdraw(getActivity(), myAppContext.getToken(), money, handler).start();
            }
        });
        return view;
    }


}
