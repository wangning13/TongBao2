package nju.tb.Commen;


import android.content.Context;
import android.database.Cursor;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v4.util.Pools;
import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import nju.tb.Commen.StringUtils;

public class LocalImageHelper {
    private Context context;
    private final List<LocalFile> allLocalFile = new ArrayList<LocalFile>(); //所有图片
    private final static Map<String, List<LocalFile>> folders = new HashMap<String, List<LocalFile>>(); //
    // 相册列表，映射相册内所有图片
    public static boolean isInited = false;

    public LocalImageHelper(Context context) {
        this.context = context;
    }

    //获取相册
    public static Map<String, List<LocalFile>> getFolders() {
        if (!isInited) {
            return null;
        }
        return folders;
    }

    public static List<LocalFile> getFolderByName(String folderName) {
        return folders.get(folderName);
    }

    //大图数据表中要获取的字段
    private final String[] STROE_IMAGES = {
            MediaStore.Images.Media._ID,    //照片ID
            MediaStore.Images.Media.DATA,   //照片路径
            MediaStore.Images.Media.ORIENTATION  //照片的旋转角度
    };
    //缩略图数据表中要获取的字段
    private final String[] THUMBNAIL_STORE_IMAGE = {
            MediaStore.Images.Thumbnails._ID,
            MediaStore.Images.Thumbnails.DATA
    };

    //初始化，获取所有图片！！！
    public synchronized void initImage() {
        if (isInited) {
            return;
        }
        Cursor imageCursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STROE_IMAGES,
                null,
                null,
                MediaStore.Images.Media.DATE_TAKEN + " DESC"); //根据时间升序排列
        if (imageCursor == null) {
            return;
        }
        while (imageCursor.moveToNext()) {
            //      Log.i("图片ID", imageCursor.getInt(0) + "");
            //     Log.i("图片路径", imageCursor.getString(1));
            int imageId = imageCursor.getInt(0);
            String imagePath = imageCursor.getString(1);
            File file = new File(imagePath);
            if (file.exists()) {
                String thumbUri = getThumbnail(imageId);
                String imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(Integer
                        .toString(imageId)).build().toString();
                //  Log.i("图片URI", imageUri);
                if (StringUtils.isEmpty(imageUri)) {
                    continue;
                }
                if (StringUtils.isEmpty(thumbUri)) {
                    thumbUri = imageUri;
                }

                LocalFile localFile = new LocalFile();
                localFile.setOriginalUri(imageUri);
                localFile.setThumbnailUri(thumbUri);
                localFile.setOrientation(imageCursor.getInt(2));
                localFile.setOriginalFile(file);
                allLocalFile.add(localFile);

                String folder = file.getParentFile().getName();
                //           Log.i("名称", folder);
                if (folders.containsKey(folder)) {
                    folders.get(folder).add(localFile);
                } else {
                    List<LocalFile> list = new ArrayList<LocalFile>();
                    list.add(localFile);
                    folders.put(folder, list);
                }

            }

        }
        folders.put("所有图片", allLocalFile);
        imageCursor.close();
        isInited = true;
    }

    //获取缩略图的URI
    private String getThumbnail(int id) {
        Cursor thumbnail = context.getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,   //缩略图URI
                THUMBNAIL_STORE_IMAGE,
                MediaStore.Images.Thumbnails.IMAGE_ID + " = ?",  //缩略图对应的原始图片ID original
                new String[]{id + ""},  //arg2和arg3组合成筛选条件
                null
        );
        if (thumbnail.getCount() > 0) {
            thumbnail.moveToFirst();
            int thumbnailId = thumbnail.getInt(0);
            String thumbnailURI = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI.buildUpon().appendPath(Integer
                    .toString(thumbnailId)).build().toString();
            thumbnail.close();
//            Log.i("缩略图ID", thumbnailId+"");
//            Log.i("缩略图Uri",thumbnailURI);
            return thumbnailURI;
        }
        thumbnail.close();
        return null;
    }

    public static class LocalFile implements Serializable {
        private String originalUri; //存储原图的URI
        private String thumbnailUri; //存储缩略图的URI
        private int orientation; //存储原图的图片旋转角度
        private File originalFile; //原图的file文件

        public File getOriginalFile() {
            return originalFile;
        }

        public void setOriginalFile(File file) {
            this.originalFile = file;
        }

        public String getOriginalUri() {
            return originalUri;
        }

        public void setOriginalUri(String originalUri) {
            this.originalUri = originalUri;
        }

        public String getThumbnailUri() {
            return thumbnailUri;
        }

        public void setThumbnailUri(String thumbnailUri) {
            this.thumbnailUri = thumbnailUri;
        }

        public int getOrientation() {
            return orientation;
        }

        public void setOrientation(int orientation) {
            this.orientation = orientation;
        }
    }
}
