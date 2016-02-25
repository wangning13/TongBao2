package nju.tb.Adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nju.tb.Commen.LocalImageHelper;
import nju.tb.Commen.LocalImageHelper.LocalFile;

import nju.tb.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class AlbumListAdapter extends BaseAdapter {
    private Map<String, List<LocalFile>> folders;
    private Context context;
    private DisplayImageOptions options;
    private List<String> folderNames;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;

    public AlbumListAdapter(Context context, Map<String, List<LocalFile>> folders) {
        this.folders = folders;
        this.context = context;

        folderNames = new ArrayList<String>();
        layoutInflater = LayoutInflater.from(context);
//imageloader的基本设置
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.add_image_bg)
                .showImageForEmptyUri(R.drawable.add_image_bg)
                .showImageOnFail(R.drawable.add_image_bg)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .displayer(new SimpleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.RGB_565)   //设置图片解码方式，避免out of memory
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        //相册内相片数量降序显示
        Iterator iterator = folders.keySet().iterator();
        while (iterator.hasNext()) {
            folderNames.add((String) iterator.next());
        }
        Collections.sort(folderNames, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                int lhsSize = LocalImageHelper.getFolderByName(lhs).size();
                int rhsSize = LocalImageHelper.getFolderByName(rhs).size();
                if (lhsSize < rhsSize) {
                    return 1;
                } else if (lhsSize == rhsSize) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }

    @Override
    public int getCount() {
        return folders.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.view_albumlist_item, null);
            viewHolder.imageview = (ImageView) convertView.findViewById(R.id.album_thumbnail);
            viewHolder.textview = (TextView) convertView.findViewById(R.id.album_albumname);
            viewHolder.sizeview = (TextView) convertView.findViewById(R.id.album_size);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        String folderName = folderNames.get(position);
        List<LocalFile> files = folders.get(folderName);
        viewHolder.textview.setText(folderName);
        viewHolder.sizeview.setText("(" + files.size() + ")");
        if (files.size() > 0) {
            imageLoader.displayImage(files.get(0).getThumbnailUri(), viewHolder.imageview, options, null);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView imageview;
        TextView textview;
        TextView sizeview;
    }
}
