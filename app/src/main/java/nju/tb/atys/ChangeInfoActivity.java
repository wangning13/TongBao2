package nju.tb.atys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import nju.tb.net.ModifyNickName;
import nju.tb.net.ModifyPassword;

public class ChangeInfoActivity extends Activity {
    private LocalImageHelper localImageHelper;
    private LinearLayout changelayout;
    private ImageView iconImageView;
    private Bitmap iconBitmap;
    private static String nickName;
    private TextView okTextView;
    private EditText nickNameEditText;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private String urlToPush;
    private BitmapHelper bitmapHelper;
    private static String path;

    private static String nickNameEditTextSave;
    private static String oldPasswordEditTextSave;
    private static String newPasswordEditTextSave;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Toast.makeText(ChangeInfoActivity.this, "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                return;
            } else if (msg.what == 1) {
                iconBitmap = (Bitmap) msg.obj;
                iconImageView.setImageBitmap(iconBitmap);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.view_driver_changeinfo);

        ImageView titleBackBtn = (ImageView) findViewById(R.id.iv_changeinfo_leftarrow);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChangeInfoActivity.this.finish();
            }
        });


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
        oldPasswordEditText = (EditText) findViewById(R.id.et_oldpassword);
        newPasswordEditText = (EditText) findViewById(R.id.et_newpassword);
        okTextView = (TextView) findViewById(R.id.tv_changeinfo_ok);

        changelayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ChangeInfoActivity.this, SelectAlbumActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("SourceActivity", "ChangeInfoActivity");
                intent.putExtra("Activity", bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        bitmapHelper = new BitmapHelper(this);

        Bundle bundle = getIntent().getBundleExtra("MeFragment");
        if (bundle != null) {
            if (bundle.keySet().contains("path")) {
                path = bundle.getString("path");
                iconBitmap = bitmapHelper.convertToBitmap(path);
            }

            nickName = (String) bundle.get("nickName");
            if (!nickName.equals("null")) {
                nickNameEditText.setHint(nickName);
                nickNameEditTextSave = nickName;
            }

            nickNameEditTextSave = "";
            oldPasswordEditTextSave = "";
            newPasswordEditTextSave = "";
        }
        nickNameEditText.setText(nickNameEditTextSave);
        oldPasswordEditText.setText(oldPasswordEditTextSave);
        newPasswordEditText.setText(newPasswordEditTextSave);

        Bundle bundle2 = getIntent().getBundleExtra("SelectAlbumActivity");
        if (bundle2 != null) {
            iconBitmap = bitmapHelper.convertToBitmap(path);

        }
        Bundle bundle1 = getIntent().getBundleExtra("AlbumDetailActivityReturn");
        if (bundle1 != null) {
            urlToPush = bundle1.getString("iconurl");
            if (!urlToPush.equals("")) {
                MeFragment.GetHttpImageThread t = new MeFragment().new GetHttpImageThread(urlToPush, this, handler);
                new Thread(t).start();
            }
            if (iconBitmap == null) {
                return;
            }
        } else {
            MyAppContext myAppContext = (MyAppContext) getApplicationContext();
            urlToPush = myAppContext.getIconUrl();
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
                    Toast.makeText(ChangeInfoActivity.this, "数据库连接中断", Toast.LENGTH_SHORT).show();
                }
                if (modifyIcon.getResult() == -1 && MyAppContext.getIsConnected() == false) {
                    Toast.makeText(ChangeInfoActivity.this, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
                }
                if (iconBitmap == null) {
                    return;
                }
                MyAppContext myAppContext = (MyAppContext) getApplicationContext();
                myAppContext.setIconUrl(urlToPush);
                Intent intent = new Intent(ChangeInfoActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                String changeInfoBitmapPath = bitmapHelper.saveBitmapToSDcard(iconBitmap, urlToPush);
                bundle.putString("changeInfoBitmap", changeInfoBitmapPath);
                bundle.putInt("TargetFragment", 2);
                intent.putExtra("ChangeInfoAcitivity", bundle);
//                startActivity(intent);
                String editNickName = nickNameEditText.getText().toString();
                boolean nickNameRunning = false;
                if (!editNickName.equals(nickName) && !editNickName.equals("")) {
                    new ModifyNickName(ChangeInfoActivity.this, USERTOKEN, editNickName).start();
                    myAppContext.setNickName(editNickName);
                    nickNameRunning = true;
                }
                String op = oldPasswordEditText.getText().toString();
                int oplength = oldPasswordEditText.getText().length();
                String np = newPasswordEditText.getText().toString();
                int nplength = newPasswordEditText.getText().length();
                boolean isModifyPasswordRunning = false;
                if (oplength >= 8 && oplength <= 255 && nplength >= 8 && nplength <= 255) {
                    ModifyPassword m = new ModifyPassword(ChangeInfoActivity.this, USERTOKEN, op, np, nickNameRunning);
                    m.start();
                    while (m.getResult() == -1) {

                    }
                    if (m.getResult() == 0) {
                        Toast.makeText(ChangeInfoActivity.this, "密码修改失败", Toast.LENGTH_SHORT).show();
                    }
                    if (m.getResult() == 1) {
                        isModifyPasswordRunning = true;
                    }

                }
                if (isModifyPasswordRunning) {
                    MyAppContext.setLogIn(false);
                    SharedPreferences loginSettings = getSharedPreferences("loginSettings", 0);
                    loginSettings.edit().putString("password", "").commit();
                    Intent reLoginIntent = new Intent(ChangeInfoActivity.this, LoginActivity.class);
                    startActivity(reLoginIntent);
                } else {
                    startActivity(intent);
                }


               ChangeInfoActivity.this.finish();
            }


        });



    }

    @Override
    public void onPause() {
        super.onPause();
        nickNameEditTextSave = nickNameEditText.getText().toString();
        oldPasswordEditTextSave = oldPasswordEditText.getText().toString();
        newPasswordEditTextSave = newPasswordEditText.getText().toString();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
