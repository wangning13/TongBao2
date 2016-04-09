package nju.tb.atys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.net.GetAllTruckTypes;
import nju.tb.net.Login;

public class LoadingActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_loading);

        Bundle newbundle = getIntent().getBundleExtra("setPhone");
        final String phone = newbundle.getString("phone");

        new Handler().postDelayed(new Runnable() {
            public void run() {
                while (Login.getResult() == -1) {
                    if (!MyAppContext.getIsConnected()) {
                        Intent netWrongIntent = new Intent(LoadingActivity.this, LoginActivity.class);
                        Bundle bundle = new Bundle();
                        netWrongIntent.putExtra("LoadingActivityNet", bundle);
                        startActivity(netWrongIntent);
                        LoadingActivity.this.finish();
                        break;
                    }

                }

                if (Login.getResult() == 0) {
                    Intent failureIntent = new Intent(LoadingActivity.this, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("state", "fail");
                    bundle.putString("phone", phone);
                    failureIntent.putExtra("LoadingActivity", bundle);
                    startActivity(failureIntent);
                }
                if (Login.getResult() == 1) {

                    new GetAllTruckTypes(LoadingActivity.this).start();//获取车辆类型
                    MyAppContext myAppContext = (MyAppContext) getApplicationContext();
                    myAppContext.setPhone(phone);

                    Set<String> tags=new HashSet<String>();
                    tags.add("driver");
                    String id=myAppContext.getId();
                    JPushInterface.setAliasAndTags(getApplicationContext(), id, tags, new TagAliasCallback() {
                        @Override
                        public void gotResult(int responseCode, String alias, Set<String> tags) {
                            // TODO
                            if (responseCode == 0) {
                                Log.i("tags", tags.toString());
                            }
                        }
                    });





                    Intent successIntent = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(successIntent);
                }
            }
        }, 2000);


    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}
