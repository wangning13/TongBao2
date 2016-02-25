package nju.tb.atys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import nju.tb.R;

public class CarInfoActivity extends Activity{
    private LinearLayout linearLayout;
    private String verify_State;
    private boolean verify_OK;
    private ImageView stateImageView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_personinfo);
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
