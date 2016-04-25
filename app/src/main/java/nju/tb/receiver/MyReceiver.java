package nju.tb.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

import cn.jpush.android.api.JPushInterface;
import nju.tb.atys.CarInfoActivity;
import nju.tb.atys.MainActivity;
import nju.tb.atys.OldOrderContentActivity;
import nju.tb.atys.TaskOrderContentActivity;

public class MyReceiver extends BroadcastReceiver {
//    private static final String TAG = "JPush";
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Bundle bundle = intent.getExtras();
//        Log.i(TAG, "[PushReceiver] onReceive - " + intent.getAction() + ", extras: "+ printBundle(bundle));;
//
//        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//            Log.i(TAG, "[PushReceiver] 接收Registeration Id : " + regId);
//        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            Log.i(TAG, "[PushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//        }else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            Log.i(TAG, "[PushReceiver] 接收到推送下来的通知");
//            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//            Log.i(TAG, "[PushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//
//        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//            Log.i(TAG, "[PushReceiver] 用户点击打开了通知");
//
//            String message = bundle.getString(JPushInterface.EXTRA_ALERT);
//            Log.i(TAG, "message:" + message);
//            try {
//                JSONObject jsonObject = new JSONObject(message);
//                String str = jsonObject.getString("type");
//                if (str.equals("1")) {
//                    Intent i = new Intent(context, OldOrderContentActivity.class);
//                    int orderNumber=Integer.parseInt(jsonObject.getString("id"));
//                    bundle.putInt("orderNumber", orderNumber);
//                    i.putExtras(bundle);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                    context.startActivity(i);
//                }else  if (str.equals("2")) {
//                    Intent i = new Intent(context, TaskOrderContentActivity.class);
//                    int orderNumber=Integer.parseInt(jsonObject.getString("id"));
//                    bundle.putInt("orderNumber", orderNumber);
//                    i.putExtras(bundle);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                    context.startActivity(i);
//                }else  if (str.equals("4")) {
//                    Intent i = new Intent(context, CarInfoActivity.class);
//                    int truckId=Integer.parseInt(jsonObject.getString("id"));
//                    bundle.putString("truckNum", "12345");
//                    bundle.putInt("truckId", truckId);
//                    i.putExtra("truckInfo", bundle);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(i);
//                }else  if (str.equals("5")) {
//                    Intent i = new Intent(context, CarInfoActivity.class);
//                    String truckNum=jsonObject.getString("id");
//                    bundle.putString("truckNum", truckNum);
//                    i.putExtras(bundle);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                    context.startActivity(i);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//
//        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//            Log.i(TAG, "[PushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
//
//        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//            Log.i(TAG, "[PushReceiver]" + intent.getAction() + " connected state change to " + connected);
//        } else {
//            Log.i(TAG, "[PushReceiver] Unhandled intent - " + intent.getAction());
//        }
//    }
//    // 打印所有的 intent extra 数据
//    private static String printBundle(Bundle bundle) {
//        StringBuilder sb = new StringBuilder();
//        for (String key : bundle.keySet()) {
//            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
//                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
//            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
//                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
//            }
//            else {
//                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
//            }
//        }
//        return sb.toString();
//    }
private static final String TAG = "MyReceiver";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.i(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "接受到推送下来的通知");

            receivingNotification(context,bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.i(TAG, "用户点击打开了通知");

            openNotification(context,bundle);

        } else {
            Log.i(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle){
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.i(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.i(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.i(TAG, "extras : " + extras);
    }

    private void openNotification(Context context, Bundle bundle){
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.i("88888888888888888",extras);

        String myValue = "";
        String myId="";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("type");
            Log.i("type888888888", myValue);
            myId= extrasJson.optString("id");
            Log.i("id88888888888", myId);
        } catch (Exception e) {
            Log.i(TAG, " Unexpected: extras is not a valid json");
            return;
        }
        if ("1".equals(myValue)) {
            Intent i = new Intent(context, OldOrderContentActivity.class);
            int orderNumber=Integer.parseInt(myId);
            bundle.putInt("orderNumber", orderNumber);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity(i);
        } else if ("2".equals(myValue)){
            Intent i = new Intent(context, TaskOrderContentActivity.class);
            int orderNumber=Integer.parseInt(myId);
            bundle.putInt("orderNumber", orderNumber);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity(i);
        }else if ("4".equals(myValue)){
            Intent i = new Intent(context, CarInfoActivity.class);
            int truckId=Integer.parseInt(myId);
            bundle.putInt("truckId", truckId);
            i.putExtra("truckInfo", bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity(i);
        }else if ("5".equals(myValue)){
            Intent i = new Intent(context, CarInfoActivity.class);
            int truckId=Integer.parseInt(myId);
            bundle.putInt("truckId", truckId);
            i.putExtra("truckInfo", bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity(i);
        }
    }

}