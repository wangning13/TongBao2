package nju.tb.atys;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import nju.tb.R;

import java.util.ArrayList;
import java.util.List;

import nju.tb.Adapters.AlbumDetailAdatper;
import nju.tb.Commen.LocalImageHelper.LocalFile;

import nju.tb.atys.SelectAlbumActivity.ListSerializable;

public class AlbumDetailActivity extends Activity {
   // private List<LocalFile> files = new ArrayList<LocalFile>();
    private GridView albumDetailGridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_albumdetail);

        albumDetailGridView = (GridView) findViewById(R.id.albumdetail_gridview);
//        albumDetailGridView.setAdapter(new AlbumDetailAdatper(this, files));

        Bundle bundle = this.getIntent().getExtras();
        ListSerializable myList = (ListSerializable) bundle.getSerializable("albumdetail");
//        Log.i("2222222222::::",myList.getList().size()+"");
        List<LocalFile> gridviewData = myList.getList();
        Log.i(" fdsfdsfsdfsdfsd方", gridviewData.size()+"");
//        Log.i("333333333:::",(gridviewData==null)+"");
        albumDetailGridView.setAdapter(new AlbumDetailAdatper(this, gridviewData));
    }

}
