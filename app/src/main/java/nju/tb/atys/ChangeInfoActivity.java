package nju.tb.atys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nju.tb.R;

import nju.tb.Commen.LocalImageHelper;
import nju.tb.atys.SelectAlbumActivity;

public class ChangeInfoActivity extends Activity {
    private LocalImageHelper localImageHelper;
    private LinearLayout changelayout;
    private TextView toolbar_text;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.view_driver_changeinfo);
        //toolbar的标题
        //回退按钮
        toolbar_text=(TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("修改信息");


        //回退按钮
        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChangeInfoActivity.this.finish();
            }
        });


        Log.i("的师傅的说法是第三方的师傅的所发生的", "11111");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
        localImageHelper = new LocalImageHelper(this);
        if(!localImageHelper.isInited){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    localImageHelper.initImage();
                }
            }
            ).start();
        }

        Log.i("的师傅的说法是第三方的师傅的所发生的", "22222");
//test
        changelayout=(LinearLayout) findViewById(R.id.change_layout);
        Log.i("的师傅的说法是第三方的师傅的所发生的",(changelayout==null)+"");
        changelayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.i("的师傅的说法是第三方的师傅的所发生的", "33333");
                Intent intent = new Intent(ChangeInfoActivity.this, SelectAlbumActivity.class);
                startActivity(intent);
            }
        });
    }
}
