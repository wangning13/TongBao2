package nju.tb.atys;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nju.tb.R;
import nju.tb.atys.ChangeInfoActivity;
import nju.tb.atys.HelpcenterActivity;
import nju.tb.atys.UseWalletActivity;

/**
 * Created by Administrator on 2016/1/21.
 */
public class MeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mefragment, container, false);

        view.findViewById(R.id.wallet_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UseWalletActivity.class));
            }
        });
        view.findViewById(R.id.editinfo_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangeInfoActivity.class));
            }
        });
        view.findViewById(R.id.help_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HelpcenterActivity.class));
            }
        });
        return view;
    }
}



