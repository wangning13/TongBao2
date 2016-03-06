package nju.tb.entity;


public class MyMessage {
    private int id;  //消息id
    private int type;  //消息类型  1:订单完成（通知司机）2: 正在进行的订单被取消(通知司机)3：其他消息,
    private String content; //消息内容
    private int hasRead;  //消息是否已读
    private String time;  //消息产生的时间
    private int objectId; //type为012时表示订单id

    public String covertTypeToString(int type) {
        switch (type) {
            case 1:
                return "订单已经完成";
            case 2:
                return "订单被取消";
            case 3:
                return "其他消息";
        }
        return null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getObjectId() {
        return objectId;
    }

}
