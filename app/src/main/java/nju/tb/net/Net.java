package nju.tb.net;

/**
 * 作者 ： motoon
 * 日期 ： 2017/4/10
 * 版本 ： v1.0
 */

public class Net {
    /**
     * url前缀
     */
    public static final String URL_PREFIX = "http://182.92.239.143:8080/tongbao";

    /**
     * 登陆
     */
    public static final String URL_USER_LOGIN = URL_PREFIX + "/user/login";
    /**
     * 网络处理失败
     */
    public static final int NET_OPERATION_FAILURE = 0;
    /**
     * 网络处理成功
     */
    public static final int NET_OPERATION_SUCCESS = 1;
}
