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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import nju.tb.Commen.BitmapHelper;
import nju.tb.Commen.MyAppContext;
import nju.tb.R;

import nju.tb.Commen.LocalImageHelper;
import nju.tb.atys.SelectAlbumActivity;
import nju.tb.net.HttpImage;
import nju.tb.net.ModifyIcon;

public class ChangeInfoActivity extends Activity {
    private LocalImageHelper localImageHelper;
    private LinearLayout changelayout;
    private ImageView iconImageView;
    private Bitmap iconBitmap;
    private String nickName;
    private TextView okTextView;
    private EditText nickNameEditText;
    private EditText oldPassword;
    private EditText newPassword;
    private String urlToPush;
    private BitmapHelper bitmapHelper;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.view_driver_changeinfo);

        bitmapHelper = new BitmapHelper(this);

        Bundle bundle = getIntent().getBundleExtra("MeFragment");
        if (bundle != null) {
            ///////////////////////////////////////////////////////////////
            String path = bundle.getString("path");
            iconBitmap = bitmapHelper.convertToBitmap(path);
            nickName = (String) bundle.get("nickName");
        }


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
        okTextView = (TextView) findViewById(R.id.tv_changeinfo_ok);

        changelayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ChangeInfoActivity.this, SelectAlbumActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getIntent().getBundleExtra("AlbumDetailActivityReturn");
        if (bundle != null) {
            urlToPush = bundle.getString("iconurl");
            MeFragment.GetHttpImageThread t = new MeFragment().new GetHttpImageThread(urlToPush, this);
            new Thread(t).start();
            while (!t.runover) {

            }
            iconBitmap = t.getBitmap();

            if (iconBitmap == null) {
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        bitmapHelper = new BitmapHelper(this);


        if (iconBitmap == null) {

        } else {
            iconImageView.setImageBitmap(iconBitmap);
        }
        if (nickName == "") {

        } else {
            nickNameEditText.setText(nickName);
        }

        final String USERTOKEN = ((MyAppContext) getApplicationContext()).getToken();

        //提交到数据库
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyIcon modifyIcon = new ModifyIcon(ChangeInfoActivity.this, USERTOKEN, urlToPush);
                modifyIcon.start();
                while (!modifyIcon.runover) {

                }
                if (modifyIcon.getResult() == 0) {
                    Toast.makeText(ChangeInfoActivity.this, "数据库连接中断", Toast.LENGTH_SHORT);
                }

                if (iconBitmap == null) {
                    return;
                }

                Intent intent = new Intent(ChangeInfoActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                String changeInfoBitmapPath = bitmapHelper.saveBitmapToSDcard(iconBitmap, urlToPush);
//                bundle.putParcelable("ChangeInfoActivityBitmap", iconBitmap);
                bundle.putString("changeInfoBitmap", changeInfoBitmapPath);
                bundle.putInt("TargetFragment", 2);
                intent.putExtra("ChangeInfoAcitivity", bundle);
                startActivity(intent);
            }


        });

    }


}
