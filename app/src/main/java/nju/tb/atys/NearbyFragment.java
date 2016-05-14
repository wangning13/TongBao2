package nju.tb.atys;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
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
import java.util.Timer;
import java.util.TimerTask;

import nju.tb.Commen.MyAppContext;
import nju.tb.MyUI.OverlayManager;
import nju.tb.R;
import nju.tb.entity.Order;
import nju.tb.net.ShowAllOrders;
import nju.tb.services.GPSService;

/**
 * Created by Administrator on 2016/3/20.
 */
public class NearbyFragment extends Fragment {
    MapView mMapView = null;
    ServiceConnection mSc;
    double[] location;
    Handler handler;
    GPSService ss;
    OverlayManager olmanager;



    TimerTask task1 = new TimerTask() {

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };
    TimerTask task2 = new TimerTask() {

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what =2;
            handler.sendMessage(message);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        task1.cancel();
        task2.cancel();
        getActivity().unbindService(mSc);
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

        Timer timer = new Timer();
        timer.schedule(task1, 500, 300000); // 500秒后执行task,1然后进过300000秒再次执行task1，这个用于循环任务，执行无数次,直到timer.cancel()来取消计时器的执行
        timer.schedule(task2, 500, 300000); // 0.5s后执行task,经过5min再次执行
        mSc = new ServiceConnection(){
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ss = ((GPSService.LocalBinder) service).getService();
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };

        Intent service = new Intent(getActivity().getApplicationContext(),GPSService.class);
        getActivity().bindService(service, mSc, Context.BIND_AUTO_CREATE);
        View m_vFindWorkFragment = inflater.inflate(R.layout.nearbyfragment, container, false);
        mMapView = (MapView) m_vFindWorkFragment.findViewById(R.id.bmapView);
        final BaiduMap  mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);



        final List<OverlayOptions> overlayOptionslist = new ArrayList<OverlayOptions>();
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if(msg.what==2){
                    LatLng latLng = null;
                    OverlayOptions overlayOptions = null;
                    Marker marker = null;
                    for (Order order : getData("",""))
                    {
                        // 位置
                        latLng = new LatLng(Double.parseDouble(order.getLat()), Double.parseDouble(order.getLng()));
                        // 图标
                        BitmapDescriptor bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.icon_marka);
                        overlayOptions = new MarkerOptions().position(latLng)
                                .icon(bitmap).zIndex(5);
                        marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order", order);
                        marker.setExtraInfo(bundle);
                    }
                    mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(final Marker marker) {
                            Log.i("maker", marker.getPosition().latitude + "");
                            Order order = (Order) marker.getExtraInfo().get("order");
                            Intent intent = new Intent(getActivity(), GrabOrderContentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("order", order);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            return true;
                        }
                    });

                }
                if (msg.what == 1) {
                    location = ss.getLatlon();
                    if(location==null){
                        Toast.makeText(getActivity(), "GPS服务不可用", Toast.LENGTH_SHORT).show();
                    }else{
                        LatLng point = new LatLng(location[0],location[1]);
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
                        overlayOptionslist.add(centeroption);

                        OverlayManager manager = new OverlayManager(mBaiduMap) {
                            public List<OverlayOptions> getOverlayOptions() {
                                return overlayOptionslist;
                            }
                        };
                        manager.addToMap();



                        int size=overlayOptionslist.size();
                        Log.i("size", size + "");
                        if(size!=1){
                            for(int i=1;i<size;i++){
                                overlayOptionslist.remove(1);
                                manager.removeOverlay(1);
                            }
                        }
                    }
                    }
                super.handleMessage(msg);
            };
        };






        return m_vFindWorkFragment;
    }


    public List<Order> getData(String faddress ,String taddress) {

        final String USERTOKEN = ((MyAppContext) getActivity().getApplicationContext()).getToken();
        ShowAllOrders sa=new ShowAllOrders(getActivity(),USERTOKEN,faddress,taddress,handler);
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
