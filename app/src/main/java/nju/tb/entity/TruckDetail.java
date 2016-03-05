package nju.tb.entity;

public class TruckDetail {
    private String truckNum = "";  //车牌号
    private int authState = -1;   //审核状态
    private String typeName = "";  //车型
    private int length = -1;     //车长
    private int capacity = -1;   //载重
    private String phoneNum = "";  //随车电话
    private String realName = "";  //车主姓名

    public void setTruckNum(String truckNum) {
        this.truckNum = truckNum;
    }

    public String getTruckNum() {
        return this.truckNum;
    }

    public void setAuthState(int authState) {
        this.authState = authState;
    }

    public int getAuthState() {
        return this.authState;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return this.length;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return this.realName;
    }
}
