package nju.tb.atys;

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


public class CardFragment extends Fragment {
    private EditText cardId, cardMoney;
    private Button cardOk;
    private MyAppContext myAppContext;
    private ProgressDialog p;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                p.dismiss();
                Toast.makeText(CardFragment.this.getActivity(), "提现失败，请重新操作", Toast.LENGTH_SHORT).show();
                return;
            } else if (msg.what == 1) {
                p.dismiss();
                Intent intent = new Intent(CardFragment.this.getActivity(), UserWalletActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cardfragment, container, false);
        cardId = (EditText) view.findViewById(R.id.cardID);
        cardMoney = (EditText) view.findViewById(R.id.cardmoney);
        cardOk = (Button) view.findViewById(R.id.cardconfirm);
        myAppContext = MyAppContext.getMyAppContext();
        p = new ProgressDialog(getActivity());
        RechargeActivity.setProgressDialog(p);

        cardOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(cardId.getText().toString()) || TextUtils.isEmpty(cardMoney.getText()
                        .toString())) {
                    Toast.makeText(CardFragment.this.getActivity(), "请填写支付信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                int money = Integer.parseInt(cardMoney.getText().toString());
                if (money > myAppContext.getMoney()) {
                    Toast.makeText(CardFragment.this.getActivity(), "余额不足", Toast.LENGTH_SHORT).show();
                    return;
                }
                p.show();
                new Withdraw(getActivity(), myAppContext.getToken(), money, handler).start();
            }
        });

        return view;
    }
}
