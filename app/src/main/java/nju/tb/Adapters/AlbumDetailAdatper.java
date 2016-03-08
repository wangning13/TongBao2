package nju.tb.Adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import nju.tb.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nju.tb.Commen.LocalImageHelper;
import nju.tb.Commen.LocalImageHelper.LocalFile;
import nju.tb.atys.AlbumDetailActivity;

public class AlbumDetailAdatper extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<LocalFile> files;
    private DisplayImageOptions displayImageOptions;
    private ImageLoader imageLoader;
    private int isClick = -1;
    private int lastClick = isClick;
    private boolean firstClick = true;
    private View clickView = null;
    private LocalFile clickLocalFile = null;

    public AlbumDetailAdatper(Context context, List<LocalFile> files) {
        this.context = context;
        this.files = files;
        layoutInflater = LayoutInflater.from(context);

        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.add_image_bg)
                .showImageForEmptyUri(R.drawable.add_image_bg)
                .showImageOnFail(R.drawable.add_image_bg)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .displayer(new SimpleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

    }

    public void imageLoaderDestroy() {
        this.imageLoader.destroy();
    }

    public LocalFile getClickLocalFile() {
        return clickLocalFile;
    }

    public boolean isFirstClick() {
        return this.firstClick;
    }

    public void setFirstClick() {
        this.firstClick = false;
    }

    public int getIsClick() {
        return isClick;
    }

    public void setIsClick(int isClick) {
        this.isClick = isClick;
        clickLocalFile = files.get(isClick);
    }

    public int getLastClick() {
        return lastClick;
    }

    public void setLastClick(int lastClick) {
        this.lastClick = lastClick;
    }

    public View getClickView() {
        return clickView;
    }

    @Override
    public int getCount() {
        return files.size();
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
            convertView = layoutInflater.inflate(R.layout.view_albumdetail_item, null);
            viewHolder.albumDetailImageVIew = (ImageView) convertView.findViewById(R.id.albumdetail_item_imageview);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        imageLoader.displayImage(files.get(position).getThumbnailUri(), viewHolder.albumDetailImageVIew,
                displayImageOptions, null);
        if (position == isClick) {
            clickView = convertView;
            AlbumDetailActivity albumDetailActivity = new AlbumDetailActivity();
            albumDetailActivity.startAnimation(convertView, true);
        }
        if (position != isClick) {
            convertView.clearAnimation();
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView albumDetailImageVIew;
    }
}
