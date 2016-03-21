package nju.tb.entity;


public class Account {
    private int type; //(账单类型)0表示充值 1表示提现 2表示支付 3表示退款 4表示到帐
    private String time;  //year-month-day  hour:minute:second
    private int money;
    private int orderId;
    private String addressFrom;
    private String addressTo;

    public Account() {
        type = -1;
        time = null;
        money = -1;
        orderId = -1;
        addressFrom = null;
        addressTo = null;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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
}
