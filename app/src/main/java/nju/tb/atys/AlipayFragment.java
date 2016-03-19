package nju.tb.atys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import nju.tb.Adapters.OldOrderListAdapter;
import nju.tb.R;

/**
 * Created by Administrator on 2016/3/17.
 */
public class AlipayFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alipayfragment, container, false);

        return view;
    }
}
