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

import java.io.InterruptedIOException;

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
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
        }
        location=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //设置每6秒，每移动一米向LocationProvider获取一次GPS的定位信息
        //当LocationProvider可用，不可用或定位信息改变时，调用updateView,更新显示
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.01f, new LocationListener() {

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
        Log.i("888",location.getLatitude()+","+location.getLongitude());
        this.location=location;

    }

    public double[] getLatlon() {
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
