package nju.tb.Commen;

public class StringUtils {
    /*
    isEmpty()
    判断字符串是否为空，制表符、换行符、空格、回车都属于空
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;

    }

}
