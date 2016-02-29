package nju.tb.atys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
                BitmapHelper bitmapHelper = new BitmapHelper(getActivity());
                String newPath = bitmapHelper.saveBitmapToSDcard(iconBitmap, url);
                bundle.putString("path", newPath);
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
                GetHttpImageThread t = new GetHttpImageThread(url, getActivity());
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
            Log.i("path", updateBitmapPath);
            BitmapHelper bitmapHelper = new BitmapHelper(getActivity());
            Log.i("isnull", "" + (bitmapHelper == null));
            iconBitmap = bitmapHelper.convertToBitmap(updateBitmapPath);
        }

        displayPicImageView.setImageBitmap(iconBitmap);


    }

    class GetHttpImageThread implements Runnable {
        private Bitmap bitmap = null;
        private String threadurl;
        boolean runover = false;
        private Context context;

        public GetHttpImageThread(String threadurl, Context context) {
            this.threadurl = threadurl;
            this.context = context;
        }

        @Override
        public void run() {
            HttpImage httpImage = new HttpImage(context);
            bitmap = httpImage.getHttpBitmap(threadurl);
            while (bitmap == null) {

            }
            runover = true;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }
    }
}



