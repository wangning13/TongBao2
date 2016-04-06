package nju.tb.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.InterruptedIOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import nju.tb.Commen.MyAppContext;
import nju.tb.atys.MainActivity;
import nju.tb.net.RemoveOrder;
import nju.tb.net.UpdateMyPostion;

public class GPSService extends Service {

    LocationManager manager;
    Location location;
    double[] latlon;


    // 实例化自定义的Binder类
    private final IBinder mBinder = new LocalBinder();

    public GPSService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        manager=(LocationManager)getSystemService(LOCATION_SERVICE);
        //从GPS_PROVIDER获取最近的定位信息\
        location=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //设置每6秒，每移动一米向LocationProvider获取一次GPS的定位信息
        //当LocationProvider可用，不可用或定位信息改变时，调用updateView,更新显示
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000,10, new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
                //
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                updateView(manager.getLastKnownLocation(provider));
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                updateView(null);
            }

            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                //location为变化完的新位置，更新显示
                updateView(location);
            }
        });


        return START_STICKY;
    }
    public void updateView(Location location)
    {
        this.location=location;

        final String USERTOKEN = ((MyAppContext) getApplicationContext()).getToken();
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        UpdateMyPostion ump = new UpdateMyPostion(getApplicationContext(), USERTOKEN,df.format(date) ,location.getLatitude()+"",location.getLongitude()+"");
        ump.start();
        while (!ump.runover) {

        }
        if (ump.getResult() ==0) {
            Toast.makeText(getApplicationContext(), ump.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
        if (ump.getResult() == -1 && MyAppContext.getIsConnected() == false) {
            Toast.makeText(getApplicationContext(), "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
        }

    }

    public double[] getLatlon() {

        if(location==null)
        {
            return null;
        }
        double[] latlonnow=new double[]{location.getLatitude(),location.getLongitude()};
        return latlonnow;
    }

    /**
     * 自定义的Binder类，这个是一个内部类，所以可以知道其外围类的对象，通过这个类，让Activity知道其Service的对象
     */
    public class LocalBinder extends Binder {
        public GPSService getService() {
            // 返回Activity所关联的Service对象，这样在Activity里，就可调用Service里的一些公用方法和公用属性
            return GPSService.this;
        }
    }


}
