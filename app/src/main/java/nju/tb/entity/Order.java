package nju.tb.entity;

/**
 * Created by Administrator on 2016/1/20.
 */
public class Order {
    public Order(String begining, String endding, double distance, String placeorderdate, String departdate, String type, double weight, String remark) {
        this.begining = begining;
        this.endding = endding;
        this.distance = distance;
        this.placeorderdate = placeorderdate;
        this.departdate = departdate;
        this.type = type;
        this.weight = weight;
        this.remark = remark;
    }

    private long ordernum;
    private String placeorderdate;
    private String departdate;
    private String begining;
    private String endding;
    private String type;
    private double weight;
    private double distance;
    private String remark;

    public long getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(long ordernum) {
        this.ordernum = ordernum;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getPlaceorderdate() {
        return placeorderdate;
    }

    public void setPlaceorderdate(String placeorderdate) {
        this.placeorderdate = placeorderdate;
    }
    public String getDepartdate() {
        return departdate;
    }

    public String getBegining() {
        return begining;
    }

    public String getEndding() {
        return endding;
    }

    public String getType() {
        return type;
    }

    public double getWeight() {
        return weight;
    }

    public String getRemark() {
        return remark;
    }


    public void setDepartdate(String departdate) {
        this.departdate = departdate;
    }

    public void setBegining(String begining) {
        this.begining = begining;
    }

    public void setEndding(String endding) {
        this.endding = endding;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String toStringdetail(){
        return "订单号："+getOrdernum()+"\n" +
                "起点："+getBegining()+"\n" +
                "终点："+getEndding()+" \n" +
                "里程数："+getDistance()+"\n" +
                "发车日期："+getDepartdate()+"\n" +
                "下单日期："+getPlaceorderdate()+"\n" +
                "货车类型："+getType()+"\n" +
                "货物详情："+getRemark()+" "+getWeight()+"吨\n";
    }



    public String toString(){
        return
                "起点："+getBegining()+"\n" +
                "终点："+getEndding()+" \n" +
                "里程数："+getDistance()+"\n" +
                "发车日期："+getDepartdate()+"\n" +
                "货车类型："+getType()+"\n" +
                "货物详情："+getRemark()+" "+getWeight()+"吨\n";
    }

}
