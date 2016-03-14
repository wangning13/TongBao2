package nju.tb.atys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.scheme.HostNameResolver;

import nju.tb.Commen.BitmapHelper;
import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.atys.ChangeInfoActivity;
import nju.tb.atys.HelpcenterActivity;
import nju.tb.atys.UseWalletActivity;
import nju.tb.net.GetBitmap;
import nju.tb.net.HttpImage;

public class MeFragment extends Fragment {
    private ImageView displayPicImageView;
    private TextView nickNameTextView, phoneTextView;

    private Bitmap iconBitmap;
    private String nickName;
    private String url;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Toast.makeText(getActivity(), "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                return;
            } else if (msg.what == 1) {
                iconBitmap = (Bitmap) msg.obj;
                displayPicImageView.setImageBitmap((Bitmap) msg.obj);
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mefragment, container, false);

        displayPicImageView = (ImageView) view.findViewById(R.id.ImageView_add);
        nickNameTextView = (TextView) view.findViewById(R.id.text_name);
        phoneTextView = (TextView) view.findViewById(R.id.text_tel);

        view.findViewById(R.id.wallet_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UseWalletActivity.class));
            }
        });
        view.findViewById(R.id.editinfo_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ChangeInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nickName", nickName);
                if (iconBitmap != null) {
                    BitmapHelper bitmapHelper = new BitmapHelper(getActivity());
                    MyAppContext myAppContext = (MyAppContext) getActivity().getApplicationContext();
                    String newPath = bitmapHelper.saveBitmapToSDcard(iconBitmap, myAppContext.getIconUrl());
                    bundle.putString("path", newPath);
                }

                intent.putExtra("MeFragment", bundle);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.help_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HelpcenterActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Fragment f = getFragmentManager().findFragmentByTag("update");
        if (f == null) {
            MyAppContext myAppContext = (MyAppContext) getActivity().getApplicationContext();
            nickName = myAppContext.getNickName();
            url = myAppContext.getIconUrl();
            if (url.equals("")) {
                iconBitmap = null;
            } else {
                GetHttpImageThread t = new GetHttpImageThread(url, getActivity(), handler);
                new Thread(t).start();
            }
            if (!nickName.equals("null")) {
                nickNameTextView.setText(nickName);
            }
            MyAppContext myAppContext1 = MyAppContext.getMyAppContext();
            phoneTextView.setText("电话:"+myAppContext1.getPhone());
        } else {
            String updateBitmapPath = f.getArguments().getString("BitmapPathToUpdate");
            BitmapHelper bitmapHelper = new BitmapHelper(getActivity());
            iconBitmap = bitmapHelper.convertToBitmap(updateBitmapPath);
            displayPicImageView.setImageBitmap(iconBitmap);

            MyAppContext myAppContext = (MyAppContext) getActivity().getApplicationContext();
            nickName = myAppContext.getNickName();
            if (!nickName.equals("null")) {
                nickNameTextView.setText(nickName);
            }
            MyAppContext myAppContext1 = MyAppContext.getMyAppContext();
            phoneTextView.setText("电话:"+myAppContext1.getPhone());
        }

    }

    class GetHttpImageThread implements Runnable {
        private Bitmap bitmap = null;
        private String threadurl;
        boolean runover = false;
        private Context context;
        private Handler handler;

        public GetHttpImageThread(String threadurl, Context context, Handler handler) {
            this.threadurl = threadurl;
            this.context = context;
            this.handler = handler;
        }

        @Override
        public void run() {
            bitmap = GetBitmap.getHttpBitmap(threadurl);
            while (bitmap == null) {
                if (!MyAppContext.getIsConnected()) {
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    runover = true;
                    return;
                }
            }
            runover = true;

            Message message1 = handler.obtainMessage(1);
            message1.obj = bitmap;
            handler.sendMessage(message1);
        }

        public Bitmap getBitmap() {
            return bitmap;
        }
    }
}



