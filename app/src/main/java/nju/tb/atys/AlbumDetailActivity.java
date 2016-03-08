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

import java.util.ArrayList;
import java.util.List;

import nju.tb.Adapters.AlbumDetailAdatper;
import nju.tb.Commen.LocalImageHelper.LocalFile;

import nju.tb.atys.SelectAlbumActivity.ListSerializable;
import nju.tb.net.HttpImage;

public class AlbumDetailActivity extends Activity {
    private MyGridView albumDetailGridView;
    private TextView albumNameTextView;
    private TextView albumDetailOKTextView;
    private String iconUrl = "";
    private ImageView returnImageView;
    private String sourceActivity;
    private AlbumDetailAdatper adapter;

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


    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle1 = getIntent().getBundleExtra("Activity");
        sourceActivity = bundle1.getString("SourceActivity");

        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        //相册名
        String folderName = bundle.getString("foldername");
        albumNameTextView.setText(folderName);
        //相册图片
        ListSerializable myList = (ListSerializable) bundle.getSerializable("albumdetail");
        List<LocalFile> gridviewData = myList.getList();

        adapter = new AlbumDetailAdatper(this, gridviewData);

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


        //选择图片完成事件
        albumDetailOKTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adapter.getIsClick() < 0) {
                    Toast.makeText(AlbumDetailActivity.this, "未选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                LocalFile clickLocalFile = adapter.getClickLocalFile();

                final List<String> list = new ArrayList<String>();
                HttpImage httpImage = new HttpImage(AlbumDetailActivity.this, clickLocalFile.getOriginalFile());
                if (sourceActivity.equals("ChangeInfoActivity")) {   //修改头像上传图片
                    httpImage.setPostOver(new HttpImage.PostOver() {
                        //回调实现，异步任务完成后执行的动作
                        @Override
                        public void over(String result) {
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
                } else if (sourceActivity.equals("CertificationActivity1")) {  //驾驶证图片上传
                    httpImage.setPostOver(new HttpImage.PostOver() {
                        @Override
                        public void over(String result) {
                            if (result.equals("wrong")) {
                                Toast.makeText(AlbumDetailActivity.this, "上传失败，请重新上传", Toast.LENGTH_SHORT).show();
                            } else if (result.equals("netwrong")) {
                                Toast.makeText(AlbumDetailActivity.this, "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                            }
                            String url = result;
                            albumDetailOKTextView.setClickable(true);
                            Intent intent = new Intent(AlbumDetailActivity.this, CertificationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("url", url);
                            bundle.putInt("num", 1);
                            bundle.putInt("currentTab", 0);
                            intent.putExtra("AlbumDetailActivityReturn", bundle);
                            startActivity(intent);
                        }
                    });
                } else if (sourceActivity.equals("CertificationActivity2")) {   //驾驶人头像上传
                    httpImage.setPostOver(new HttpImage.PostOver() {
                        @Override
                        public void over(String result) {
                            if (result.equals("wrong")) {
                                Toast.makeText(AlbumDetailActivity.this, "上传失败，请重新上传", Toast.LENGTH_SHORT).show();
                            } else if (result.equals("netwrong")) {
                                Toast.makeText(AlbumDetailActivity.this, "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                            }
                            String url = result;
                            albumDetailOKTextView.setClickable(true);
                            Intent intent = new Intent(AlbumDetailActivity.this, CertificationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("url", url);
                            bundle.putInt("num", 2);
                            bundle.putInt("currentTab", 0);
                            intent.putExtra("AlbumDetailActivityReturn", bundle);
                            startActivity(intent);
                        }
                    });
                } else if (sourceActivity.equals("CertificationActivity3")) {   // 车头图片
                    httpImage.setPostOver(new HttpImage.PostOver() {
                        @Override
                        public void over(String result) {
                            if (result.equals("wrong")) {
                                Toast.makeText(AlbumDetailActivity.this, "上传失败，请重新上传", Toast.LENGTH_SHORT).show();
                            } else if (result.equals("netwrong")) {
                                Toast.makeText(AlbumDetailActivity.this, "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                            }
                            String url = result;
                            albumDetailOKTextView.setClickable(true);
                            Intent intent = new Intent(AlbumDetailActivity.this, CertificationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("url", url);
                            bundle.putInt("num", 3);
                            bundle.putInt("currentTab", 1);
                            intent.putExtra("AlbumDetailActivityReturn", bundle);
                            startActivity(intent);
                        }
                    });
                } else if (sourceActivity.equals("CertificationActivity4")) {   //行驶证上传
                    httpImage.setPostOver(new HttpImage.PostOver() {
                        @Override
                        public void over(String result) {
                            if (result.equals("wrong")) {
                                Toast.makeText(AlbumDetailActivity.this, "上传失败，请重新上传", Toast.LENGTH_SHORT).show();
                            } else if (result.equals("netwrong")) {
                                Toast.makeText(AlbumDetailActivity.this, "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                            }
                            String url = result;
                            albumDetailOKTextView.setClickable(true);
                            Intent intent = new Intent(AlbumDetailActivity.this, CertificationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("url", url);
                            bundle.putInt("num", 4);
                            bundle.putInt("currentTab", 1);
                            intent.putExtra("AlbumDetailActivityReturn", bundle);
                            startActivity(intent);
                        }
                    });
                }

                httpImage.execute();


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

    @Override
    public void onStop() {
        super.onStop();
        adapter.imageLoaderDestroy();
        this.finish();
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
