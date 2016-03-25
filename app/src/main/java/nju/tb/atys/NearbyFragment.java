package nju.tb.atys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import nju.tb.Commen.MyAppContext;
import nju.tb.R;
import nju.tb.entity.Order;
import nju.tb.net.ShowAllOrders;

/**
 * Created by Administrator on 2016/3/20.
 */
public class NearbyFragment extends Fragment {
    MapView mMapView = null;

    private void updateToNewLocation(Location location) {

        if (location != null) {
            double  latitude = location.getLatitude();
            double longitude= location.getLongitude();
        } else {
        }

    }



    private void openGPSSettings() {
        LocationManager alm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return;
        }

        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent, 0); //此为设置完成后返回到获取界面

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View m_vFindWorkFragment = inflater.inflate(R.layout.nearbyfragment, container, false);
        mMapView = (MapView) m_vFindWorkFragment.findViewById(R.id.bmapView);
        BaiduMap  mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        LatLng point = new LatLng(32.05000d, 118.78333d);
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(15)
                .build();

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        BitmapDescriptor centerbitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_center);
        OverlayOptions centeroption = new MarkerOptions()
                .position(point)
                .icon(centerbitmap);
        mBaiduMap.addOverlay(centeroption);


        LatLng point1 = new LatLng(32.03000d, 118.78333d);
        LatLng point2 = new LatLng(32.03100d, 118.76333d);
        LatLng point3 = new LatLng(32.03010d, 118.78333d);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
        OverlayOptions option1 = new MarkerOptions()
                .position(point1)
                .icon(bitmap);
        mBaiduMap.addOverlay(option1);
        OverlayOptions option2 = new MarkerOptions()
                .position(point2)
                .icon(bitmap);
        mBaiduMap.addOverlay(option2);
        OverlayOptions option3 = new MarkerOptions()
                .position(point3)
                .icon(bitmap);
        mBaiduMap.addOverlay(option3);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(final Marker marker)
            {
                Log.i("maker",marker.getPosition().latitude+"");
                return true;
            }
        });













        return m_vFindWorkFragment;
    }

    public List<Order> getData(String faddress ,String taddress) {

        final String USERTOKEN = ((MyAppContext) getActivity().getApplicationContext()).getToken();
        ShowAllOrders sa=new ShowAllOrders(getActivity(),USERTOKEN,faddress,taddress);
        sa.start();

        ArrayList<Order> orderList=new ArrayList<Order>();
        while (sa.getResult() == -1) {
            if (!MyAppContext.getIsConnected()) {
                break;
            }
        }
        while(!sa.isRunover()){
        }

        if (sa.getResult() == 0) {
        }

        if (sa.getResult() == 1) {
            orderList = sa.getAllorders();
        }
        return orderList;
    }


}
