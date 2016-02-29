package nju.tb.Commen;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class BitmapHelper {
    private Context context;
    private final String BITMAP_PATH = "/sdcard/TongBao";
    private String phoneNumber;

    public BitmapHelper(Context context) {
        this.context = context;
        phoneNumber = ((MyAppContext) context.getApplicationContext()).getPhone();
    }

    public String saveBitmapToSDcard(Bitmap bitmap, String url) {
        File file = new File(BITMAP_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        String bitmapType = url.substring(url.lastIndexOf("."));
        String newPath = BITMAP_PATH + "/" + phoneNumber + "." + bitmapType;
        file = new File(newPath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            switch (bitmapType) {
                case "png":
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
                    break;
                case "PNG":
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
                    break;
                default:
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newPath;
    }

    public Bitmap convertToBitmap(String path) {
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path));
        return Bitmap.createBitmap(weak.get());
    }
}
