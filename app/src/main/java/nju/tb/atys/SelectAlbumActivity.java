package nju.tb.atys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private ListView albumlist;
    private TextView toolbar_text;
    private String sourceActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //super.oncreate主要是为了重写绘制activity的时候能保存用户上次的状态，加强用户体验，父类已经写好，
        // （activity会因为内存等原因被销毁，再次显示的时候重建）
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_selectalbum);

        toolbar_text = (TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("照片");

        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelectAlbumActivity.this.finish();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        while (!LocalImageHelper.isInited) {

        }
        Bundle bundle = getIntent().getBundleExtra("Activity");
        sourceActivity = bundle.getString("SourceActivity");

        albumlist = (ListView) findViewById(R.id.lv_selectalbum_albumlist);
        final Map<String, List<LocalImageHelper.LocalFile>> data = LocalImageHelper.getFolders();
        if (data == null) {
            return;
        }
        albumlist.setAdapter(new AlbumListAdapter(this, data));

        albumlist.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String folderName = ((TextView) view.findViewById(R.id.album_albumname)).getText().toString();
                Intent intent = new Intent(SelectAlbumActivity.this, AlbumDetailActivity.class);
                Bundle bundle = new Bundle();
                ListSerializable myList = new ListSerializable();
                myList.setList(data.get(folderName));
                bundle.putSerializable("albumdetail", myList);
                bundle.putString("foldername", folderName);
                intent.putExtra("bundle", bundle);
                Bundle bundle1 = new Bundle();
                bundle1.putString("SourceActivity", sourceActivity);
                intent.putExtra("Activity", bundle1);
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
