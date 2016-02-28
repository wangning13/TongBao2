package nju.tb.atys;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.w3c.dom.Text;

import nju.tb.MyUI.MyGridView;
import nju.tb.R;

import java.util.ArrayList;
import java.util.List;

import nju.tb.Adapters.AlbumDetailAdatper;
import nju.tb.Commen.LocalImageHelper.LocalFile;

import nju.tb.atys.SelectAlbumActivity.ListSerializable;

public class AlbumDetailActivity extends Activity {
    private MyGridView albumDetailGridView;
    private TextView albumNameTextView;
    private TextView albumDetailOKTextView;

    //缩小
    private ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f, 1f, 1.4f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    //放大
    private ScaleAnimation scaleAnimation1 = new ScaleAnimation(1f, 1.4f, 1f, 1.4f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_albumdetail);

        albumDetailGridView = (MyGridView) findViewById(R.id.albumdetail_gridview);
        albumNameTextView = (TextView) findViewById(R.id.albumdetail_name);
        albumDetailOKTextView=(TextView) findViewById(R.id.albumdetail_ok);

        Bundle bundle = this.getIntent().getExtras();
        //相册名
        String folderName = bundle.getString("foldername");
        albumNameTextView.setText(folderName);
        //相册图片
        ListSerializable myList = (ListSerializable) bundle.getSerializable("albumdetail");
        List<LocalFile> gridviewData = myList.getList();

        final AlbumDetailAdatper adapter = new AlbumDetailAdatper(this, gridviewData);

        albumDetailGridView.setAdapter(adapter);

        albumDetailGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                albumDetailGridView.setCurrentPosition(position);
                if (adapter.isFirstClick()) {
//                    startAnimation(view, true);
                    adapter.setIsClick(position);
                    adapter.setLastClick(position);
                    adapter.setFirstClick();
                    adapter.notifyDataSetChanged();

                    return;
                }
//                startAnimation(view, true);
//                startAnimation(adapter.getClickView(), false);
                adapter.setLastClick(adapter.getIsClick());
                adapter.setIsClick(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void startAnimation(View view, boolean toBigger) {
        AnimationSet set = new AnimationSet(true);
        if (toBigger) {
            scaleAnimation1.setDuration(500);
            set.addAnimation(scaleAnimation1);
            set.setFillAfter(true);
            view.startAnimation(set);
        } else {
            scaleAnimation.setDuration(500);
            set.addAnimation(scaleAnimation);
            set.setFillAfter(true);
            view.startAnimation(set);
        }
    }

}
