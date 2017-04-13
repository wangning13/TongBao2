package nju.tb.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import nju.tb.Commen.Common;
import nju.tb.entity.User;
import nju.tb.net.Net;

/**
 * 作者 ： motoon
 * 日期 ： 2017/4/13
 * 版本 ： v1.0
 */

public class UserService {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
    /**
     * 获取错误信息
     *
     * @param jsonObject response
     * @return String
     * @throws JSONException
     */
    public static String getErrorMsg(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("errorMsg");
    }

    /**
     * 获取请求处理结果
     *
     * @param jsonObject response
     * @return boolean
     * @throws JSONException
     */
    public static boolean getResult(JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt("result") == Net.NET_OPERATION_SUCCESS;
    }
    /**
     * 用户登陆
     *
     * @param jsonObject response
     * @param phoneNum   phoneNum
     * @param password   password
     * @return boolean
     * @throws JSONException
     */
    public static boolean login(final Context context, JSONObject jsonObject, String phoneNum, String password, int type) throws JSONException {
        if (getResult(jsonObject)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Common.USER_INFO,Context.MODE_PRIVATE);
            JSONObject data = jsonObject.getJSONObject("data");
            User.getInstance().setId(data.getInt("id"));
            User.getInstance().setNiceName(data.getString("nickName"));
            User.getInstance().setIconUrl(data.getString("iconUrl"));
            User.getInstance().setPoint(data.getInt("point"));
            User.getInstance().setMoney(data.getDouble("money"));
            User.getInstance().setToken(data.getString("token"));
            User.getInstance().setPhoneNum(phoneNum);
            User.getInstance().setPassword(password);
            User.getInstance().setType(type);
            sharedPreferences.edit().putBoolean(Common.IS_LOGIN,true).apply();
            sharedPreferences.edit().putString(Common.PHONE_NUMBER,phoneNum).apply();
            sharedPreferences.edit().putString(Common.PWD,password).apply();
            try {
                User.getInstance().setRegisterTime(sdf.parse(data.getString("registerTime")));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // 为不同的用户设置不同的别名set jpush alias
            JPushInterface.setAlias(context, String.valueOf(User.getInstance().getId()), new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                    if (i == 0) {
                        Log.d(context.getClass().getName(), String.format("login alias: %s", s));
                    }
                }
            });
            return true;
        }
        return false;
    }
}
