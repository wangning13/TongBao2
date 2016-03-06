package nju.tb.atys;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import nju.tb.Commen.MyAppContext;
import nju.tb.MyUI.MyGridView;
import nju.tb.R;

import java.util.List;

import nju.tb.Adapters.AlbumDetailAdatper;
import nju.tb.Commen.LocalImageHelper.LocalFile;

import nju.tb.atys.SelectAlbumActivity.ListSerializable;
import nju.tb.net.HttpImage;

public class AlbumDetailActivity extends Activity {
    private MyGridView albumDetailGridView;
    private TextView albumNameTextView;
    private TextView albumDetailOKTextView;
    private String iconUrl="";
    private ImageView returnImageView;

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

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
        albumDetailOKTextView = (TextView) findViewById(R.id.albumdetail_ok);
        returnImageView = (ImageView) findViewById(R.id.iv_albumdetail_leftarrow);

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

        class CommitPicThread implements Runnable {
            private HttpImage postImage;
            private LocalFile clickLocalFile;
            private String result;
            public boolean runover = false;

            public CommitPicThread(HttpImage postImage, LocalFile clickLocalFile) {
                this.postImage = postImage;
                this.clickLocalFile = clickLocalFile;
            }

            @Override
            public void run() {
                albumDetailOKTextView.setClickable(false);
                result = postImage.doUpload(clickLocalFile.getOriginalFile());
                runover = true;
            }

            public String getResult() {
                return result;
            }

        }

        //选择头像完成事件
        albumDetailOKTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adapter.getIsClick() < 0) {
                    Toast.makeText(AlbumDetailActivity.this, "未选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                LocalFile clickLocalFile = adapter.getClickLocalFile();
                HttpImage httpImage = new HttpImage(AlbumDetailActivity.this);
                CommitPicThread commitPicThread = new CommitPicThread(httpImage, clickLocalFile);
                new Thread(commitPicThread).start();
                while (!commitPicThread.runover) {

                }
                String result = commitPicThread.getResult();
                if (result.equals("wrong")) {
                    Toast.makeText(AlbumDetailActivity.this, "上传失败，请重新上传", Toast.LENGTH_SHORT).show();
                } else if (result.equals("netwrong")) {
                    Toast.makeText(AlbumDetailActivity.this, "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                } else {
//                    Log.i("URL", result);
                    setIconUrl(result);
                }
                albumDetailOKTextView.setClickable(true);
                Intent intent = new Intent(AlbumDetailActivity.this, ChangeInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("iconurl", getIconUrl());
                intent.putExtra("AlbumDetailActivityReturn", bundle);
                startActivity(intent);
            }
        });

        returnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlbumDetailActivity.this, SelectAlbumActivity.class);
                startActivity(intent);
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
