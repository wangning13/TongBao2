package nju.tb.atys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nju.tb.R;

/**
 * Created by Administrator on 2016/3/20.
 */
public class NearbyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View m_vFindWorkFragment=inflater.inflate(R.layout.content_nearby, container, false);
        return m_vFindWorkFragment;
    }

}
