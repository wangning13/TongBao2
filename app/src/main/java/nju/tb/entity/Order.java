package nju.tb.entity;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/20.
 */



public class Order implements Serializable {
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTruckTypes() {
        return truckTypes;
    }

    public void setTruckTypes(String truckTypes) {
        this.truckTypes = truckTypes;
    }

    public String getFromContactName() {
        return fromContactName;
    }

    public void setFromContactName(String fromContactName) {
        this.fromContactName = fromContactName;
    }

    public String getToContactName() {
        return toContactName;
    }

    public void setToContactName(String toContactName) {
        this.toContactName = toContactName;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }

    public String getFromContactPhone() {
        return fromContactPhone;
    }

    public void setFromContactPhone(String fromContactPhone) {
        this.fromContactPhone = fromContactPhone;
    }

    public String getToContactPhone() {
        return toContactPhone;
    }

    public void setToContactPhone(String toContactPhone) {
        this.toContactPhone = toContactPhone;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private int id;
    private String time;
    private String addressFrom;
    private String addressTo;
    private String money;
    private String truckTypes;
    private String fromContactName;
    private String fromContactPhone;
    private String toContactName;
    private String toContactPhone;
    private String loadTime;
    private String state;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private double weight;
    private double distance;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
