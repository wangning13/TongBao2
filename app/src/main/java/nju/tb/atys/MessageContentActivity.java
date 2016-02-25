package nju.tb.atys;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;



import nju.tb.R;

public class MessageContentActivity extends Activity{
    private TextView sourceTextView;
    private TextView textTextView;
    private TextView timeTextView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_message_content);

        sourceTextView=(TextView) findViewById(R.id.tv_messagecontent_source);
        textTextView=(TextView) findViewById(R.id.tv_messagecontent_text);
        timeTextView=(TextView) findViewById(R.id.tv_messagecontent_time);

        Bundle bundle=getIntent().getExtras();
        String contentSource=(String)bundle.get("ContentSource");
        String contentText=(String) bundle.get("ContentText");
        String contentTime=(String) bundle.get("ContentTime");

        sourceTextView.setText(contentSource);
        textTextView.setText(contentText);
        timeTextView.setText(contentTime);
    }
}
