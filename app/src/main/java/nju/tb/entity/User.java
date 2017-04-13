package nju.tb.entity;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者 ： motoon
 * 日期 ： 2017/4/13
 * 版本 ： v1.0
 */

public class User implements Serializable {
    /**
     * 用户实例
     */
    private static User user = new User();
    /**
     * 用户id
     */
    private int id;
    /**
     * 手机号
     */
    private String phoneNum;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 昵称
     */
    private String niceName;
    /**
     * 头像地址
     */
    private String iconUrl;
    /**
     * 头像
     */
    private Bitmap icon;
    /**
     * 积分
     */
    private int point;
    /**
     * 余额
     */
    private double money;
    /**
     * 注册时间
     */
    private Date registerTime;
    /**
     * 类型（0：货主 1：司机）
     */
    private int type;
    /**
     * 用户标识
     */
    private String token;

    /**
     * 获取登陆用户实例
     *
     * @return User
     */
    public static User getInstance() {
        return user;
    }

    /**
     * 查看用户是否登陆
     *
     * @return boolean
     */
    public static boolean isLogin() {
        return (User.getInstance().getToken() != null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNiceName() {
        return niceName;
    }

    public void setNiceName(String niceName) {
        this.niceName = niceName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
