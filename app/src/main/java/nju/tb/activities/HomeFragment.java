package nju.tb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nju.tb.R;

/**
 * Created by Administrator on 2016/1/21.
 */
public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View m_vFindWorkFragment=inflater.inflate(R.layout.homefragment, container, false);
        m_vFindWorkFragment.findViewById(R.id.news_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MessageActivity.class);
                startActivity(i);
            }
        });
        m_vFindWorkFragment.findViewById(R.id.scrambleorder_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),ScrambleOrderActivity.class);
                startActivity(i);
            }
        });
        m_vFindWorkFragment.findViewById(R.id.truckmanage_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CarsManagementActivity.class);
                startActivity(i);
            }
        });
        m_vFindWorkFragment.findViewById(R.id.dotask_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),DoTaskActivity.class);
                startActivity(i);
            }
        });
        m_vFindWorkFragment.findViewById(R.id.wallet_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),UserWalletActivity.class);
                startActivity(i);
            }
        });
        m_vFindWorkFragment.findViewById(R.id.help_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),HelpcenterActivity.class);
                startActivity(i);
            }
        });
        return m_vFindWorkFragment;
}

}