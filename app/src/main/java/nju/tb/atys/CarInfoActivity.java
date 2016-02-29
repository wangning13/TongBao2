package nju.tb.atys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nju.tb.R;

public class CarInfoActivity extends Activity{
    private TextView toolbar_text;
    private LinearLayout linearLayout;
    private String verify_State;
    private boolean verify_OK;
    private ImageView stateImageView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_personinfo);

        //toolbar的标题
        //回退按钮
        toolbar_text=(TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText("车主信息");


        //回退按钮
        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CarInfoActivity.this.finish();
            }
        });

        verify_State="审核未通过";
        if((!verify_State.equals("审核通过"))&&(!verify_State.equals("审核未通过"))){
            return ;
        }

        verify_OK=isVerify_OK(verify_State);
        linearLayout=(LinearLayout)findViewById(R.id.carinfo_shenhezhangtai);
        stateImageView=(ImageView)findViewById(R.id.verify_state);


        if(!verify_OK){
            stateImageView.setVisibility(View.VISIBLE);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CarInfoActivity.this, CertificationActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private boolean isVerify_OK(String state){
        return state.equals("审核通过")?true:false;
    }

}
