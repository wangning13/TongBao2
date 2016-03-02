package nju.tb.atys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.conn.scheme.HostNameResolver;

import nju.tb.Commen.BitmapHelper;
import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.atys.ChangeInfoActivity;
import nju.tb.atys.HelpcenterActivity;
import nju.tb.atys.UseWalletActivity;
import nju.tb.net.HttpImage;

/**
 * Created by Administrator on 2016/1/21.
 */
public class MeFragment extends Fragment {
    private ImageView displayPicImageView;
    private Bitmap iconBitmap;
    private String nickName;
    private String url;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Toast.makeText(getActivity(), "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                return;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mefragment, container, false);

        displayPicImageView = (ImageView) view.findViewById(R.id.ImageView_add);

        view.findViewById(R.id.wallet_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UseWalletActivity.class));
            }
        });
        view.findViewById(R.id.editinfo_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ChangeInfoActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putParcelable("iconBitmap", iconBitmap);
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
            //以后要进行替换 LOGIN

            MyAppContext myAppContext = (MyAppContext) getActivity().getApplicationContext();
            nickName = myAppContext.getNickName();
            url = myAppContext.getIconUrl();
            if (url.equals("")) {
                iconBitmap = null;
            } else {
                GetHttpImageThread t = new GetHttpImageThread(url, getActivity(), handler);
                new Thread(t).start();
                while (!t.runover) {

                }
                iconBitmap = t.getBitmap();
                if (iconBitmap == null) {
                    return;
                }

            }
        } else {
            ////////////////////////////////////////////////////////////
            String updateBitmapPath = f.getArguments().getString("BitmapPathToUpdate");
            BitmapHelper bitmapHelper = new BitmapHelper(getActivity());
            iconBitmap = bitmapHelper.convertToBitmap(updateBitmapPath);
        }
        if (iconBitmap != null) {
            displayPicImageView.setImageBitmap(iconBitmap);
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
            HttpImage httpImage = new HttpImage(context);
            bitmap = httpImage.getHttpBitmap(threadurl);
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
        }

        public Bitmap getBitmap() {
            return bitmap;
        }
    }
}



