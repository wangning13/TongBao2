package nju.tb.atys;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;



import nju.tb.R;

public class MessageContentActivity extends Activity{
    private TextView sourceTextView;
    private TextView textTextView;
    private TextView timeTextView;
    private TextView toolbar_text;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_message_content);

        textTextView=(TextView) findViewById(R.id.tv_messagecontent_text);
        timeTextView=(TextView) findViewById(R.id.tv_messagecontent_time);

        Bundle bundle=getIntent().getExtras();
        String contentSource=(String)bundle.get("ContentSource");
        String contentText=(String) bundle.get("ContentText");
        String contentTime=(String) bundle.get("ContentTime");


        //toolbar的标题
        toolbar_text=(TextView) findViewById(R.id.toolbar_title);
        toolbar_text.setText(contentSource);

        //回退按钮
        ImageButton titleBackBtn = (ImageButton) findViewById(R.id.head_TitleBackBtn);
        titleBackBtn.setVisibility(View.VISIBLE);
        titleBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MessageContentActivity.this.finish();
            }
        });

        textTextView.setText(contentText);
        timeTextView.setText(contentTime);
    }
}
