package nju.tb.atys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import nju.tb.R;

import nju.tb.Commen.LocalImageHelper;
import nju.tb.atys.SelectAlbumActivity;
import nju.tb.net.HttpImage;

public class ChangeInfoActivity extends Activity {
    private LocalImageHelper localImageHelper;
    private LinearLayout changelayout;
    private ImageView iconImageView;
    private Bitmap iconBitmap;
    private String nickName;
    private EditText nickNameEditText;
    private EditText oldPassword;
    private EditText newPassword;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.view_driver_changeinfo);

        Bundle bundle = getIntent().getExtras();
        iconBitmap = (Bitmap) bundle.get("iconBitmap");
        nickName = (String) bundle.get("nickName");

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
        iconImageView = (ImageView) findViewById(R.id.iv_changeinfo_displaypic);
        nickNameEditText = (EditText) findViewById(R.id.et_change_name);
        oldPassword = (EditText) findViewById(R.id.et_oldpassword);
        newPassword = (EditText) findViewById(R.id.et_newpassword);

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
        if (iconBitmap == null) {

        } else {
            iconImageView.setImageBitmap(iconBitmap);
        }
        if (nickName == "") {

        } else {
            nickNameEditText.setText(nickName);
        }


    }


}
