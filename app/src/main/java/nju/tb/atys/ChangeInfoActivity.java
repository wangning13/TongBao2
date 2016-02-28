package nju.tb.atys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import nju.tb.R;

import nju.tb.Commen.LocalImageHelper;
import nju.tb.atys.SelectAlbumActivity;
import nju.tb.net.HttpImage;

public class ChangeInfoActivity extends Activity {
    private LocalImageHelper localImageHelper;
    private LinearLayout changelayout;
    private ImageView displayPicImageView;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.view_driver_changeinfo);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        localImageHelper = new LocalImageHelper(this);
        if (!localImageHelper.isInited) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    localImageHelper.initImage();
                }
            }
            ).start();
        }
        changelayout = (LinearLayout) findViewById(R.id.change_layout);
        changelayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ChangeInfoActivity.this, SelectAlbumActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        displayPicImageView = (ImageView) findViewById(R.id.iv_changeinfo_displaypic);
        //做测试，url从数据库获取
        String url = "http://i11.tietuku.com/53bb63223f5ca8dc.jpg";
        if (url.equals("")) {
            return;
        }
        Bitmap bitmap = null;
        GetHttpImageThread t = new GetHttpImageThread(url);
        new Thread(t).start();
        while (!t.runover) {

        }
        bitmap = t.getBitmap();
        if (bitmap == null) {
            return;
        }
        displayPicImageView.setImageBitmap(bitmap);
    }

    class GetHttpImageThread implements Runnable {
        private Bitmap bitmap = null;
        private String url = "";
        boolean runover = false;

        public GetHttpImageThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            HttpImage httpImage = new HttpImage(ChangeInfoActivity.this);
            bitmap = httpImage.getHttpBitmap(url);
            runover = true;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }
    }
}
