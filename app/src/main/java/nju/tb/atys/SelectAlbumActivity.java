package nju.tb.atys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import nju.tb.R;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nju.tb.Adapters.AlbumListAdapter;
import nju.tb.Commen.LocalImageHelper;

public class SelectAlbumActivity extends Activity {
    ListView albumlist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_selectalbum);
        while (!LocalImageHelper.isInited) {

        }
        albumlist = (ListView) findViewById(R.id.lv_selectalbum_albumlist);
        final Map<String, List<LocalImageHelper.LocalFile>> data = LocalImageHelper.getFolders();
        if (data == null) {
            return;
        }
        albumlist.setAdapter(new AlbumListAdapter(this, data));

        albumlist.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
  //              Log.i("fdsfdsfdsfdsfd发的说法的是非得失",""+position);
                String folderName = ((TextView) view.findViewById(R.id.album_albumname)).getText().toString();
//                Log.i("相册名",folderName);
//                Log.i("position:",position+"");
                Intent intent = new Intent(SelectAlbumActivity.this, AlbumDetailActivity.class);
                Bundle bundle = new Bundle();
                ListSerializable myList = new ListSerializable();
                myList.setList(data.get(folderName));
//                Log.i("000000000:::",(myList.getList()==null)+"");
//                Log.i("111111111111111:::",myList.getList().size()+"");
                bundle.putSerializable("albumdetail", myList);
                bundle.putString("foldername",folderName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    static class ListSerializable implements Serializable {
        private ArrayList<LocalImageHelper.LocalFile> list;

        public void setList(List<LocalImageHelper.LocalFile> list) {
            ArrayList<LocalImageHelper.LocalFile> newList = new ArrayList<LocalImageHelper.LocalFile>(list);
            this.list = newList;
        }

        public ArrayList<LocalImageHelper.LocalFile> getList() {
            return list;
        }
    }
}
