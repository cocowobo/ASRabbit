package com.ht.baselib.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * <p>字符串操作工具类，提供各种帮助方法</p>
 *
 * @author hxm
 * @version 1.0 (2015/10/19)
 */
public class StringUtils {
    /**
     * 字符串1是否含有字符串2
     * <br/>特别说明，都为空字符串或者null，则返回false
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return boolean:true包含，false不包含
     */
    public static boolean isContains(String str1, String str2) {
        if (StringUtils.isAllEmpty(str1, str2)) {
            return false;
        } else {
            if (null == str1) {
                str1 = "";
            }
            return str1.contains(str2);
        }
    }
    /**
     * 判断字符串任意多个是否全部为空
     *
     * @param strs 判断的字符串，一个或者多个
     * @return boolean:true都为空，false至少有一个非空
     *
     */
    public static boolean isAllEmpty(String... strs) {
        boolean flag = true;
        for (String str : strs) {
            if (isBlank(str)) {

            } else {//有一个非空则返回false并结束循环
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断字符串是否有值
     *
     * @param str 输入字符串
     * @return true 无值，false 有值
     */
    public static boolean isBlank(String str) {
        return (str == null || str.length() == 0 || str.trim().length() == 0);
    }

    /**
     * 判断字符串是否有值
     * @param string 输入字符串
     * @return true 无值，false 有值
     */
    public static boolean isBlankString(CharSequence string){
        if(string==null){
            return true;
        }
        return isBlank(string.toString());
    }
    /**
     * 比较两个字符串是否相等
     * <br/>特别说明，都为空字符串或者null，则返回false
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return boolean:true相等，false不相等
     */
    public static boolean isEquals(String str1, String str2) {
        if (StringUtils.isAllEmpty(str1, str2)) {
            return false;
        } else {
            if (null == str1) {
                str1 = "";
            }
            return str1.equals(str2);
        }
    }

    /**
     * 把空值转换成空字符串
     *
     * @param str 源字符串
     * @return 转换后的字符
     */
    public static String nullStrToEmpty(String str) {
        return (str == null ? "" : str);
    }

    /**
     * 把第一个字母转换成大写字母
     * <p>
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str 源字符串
     * @return String
     */
    public static String capitalizeFirstLetter(String str) {
        if (isBlank(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str
                : new StringBuilder(str.length())
                .append(Character.toUpperCase(c))
                .append(str.substring(1)).toString();
    }

    /**
     * 把字符串转换成utf-8编码
     * <p>
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * @param str 传入的字符
     * @return String
     */
    public static String utf8Encode(String str) {
        if (!isBlank(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(
                        "UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * 随机生成一个某长度的字符串
     *
     * @param length 字符长度
     * @return String
     */
    public static String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer(
                "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }

    /**
     * 字符串反向
     *
     * @param str 上下文
     * @return 反向后字符串
     */
    public static String getReverseStr(String str) {
        return new StringBuffer(str).reverse().toString();
    }

    /**
     * 补充固定字符，让字符固定长度
     *
     * @param resource   源字符串
     * @param repeatChar 补充字
     * @param length     固定长度
     * @return
     */
    public static String makeUpStr(String resource, String repeatChar, int length) {
        if (null == resource) {
            return null;
        }
        String str = "";
        for (int i = 0; i < length - resource.length(); i++) {
            str += repeatChar;
        }
        return str + resource;
    }


    /**
     * 截取str中splitTag之前或者之后的字符串
     *
     * @param str           待处理字符串
     * @param splitTag      截取字符串
     * @param beforeOrAfter true -before之前; false -after之后
     * @return
     */
    public static String getSubBeforeOrAfter(String str, String splitTag, boolean beforeOrAfter) {
        if (StringUtils.isAllEmpty(str) || StringUtils.isAllEmpty(splitTag)) {
            return null;
        }
        int index = str.lastIndexOf(splitTag);
        if (beforeOrAfter) {
            return str.substring(0, index);
        } else {
            return str.substring(index);
        }
    }

    /******************************Map Util ********************************/
    /**
     * 定义分割常量 （#在集合中的含义是每个元素的分割，|主要用于map类型的集合用于key与value中的分割）
     */
    private static final String SEP2 = ",";

    /**
     * List转换String
     *
     * @param list :需要转换的List
     * @return String转换后的字符串
     */
    public static String listToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i).equals("")) {
                    continue;
                }
                // 如果值是list类型则调用自己
                if (list.get(i) instanceof List) {
                    sb.append(listToString((List<?>) list.get(i)));
                    sb.append(SEP2);
                } else if (list.get(i) instanceof Map) {
                    sb.append(mapToString((Map<?, ?>) list.get(i)));
                    sb.append(SEP2);
                } else {
                    sb.append(list.get(i));
                    sb.append(SEP2);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Map转换String
     *
     * @param map :需要转换的Map
     * @return String转换后的字符串
     */
    public static String mapToString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        // 遍历map
        for (Object obj : map.keySet()) {
            if (obj == null) {
                continue;
            }
            Object key = obj;
            Object value = map.get(key);
            if (value instanceof List<?>) {
                //key.toString() + SEP1 +
                sb.append(listToString((List<?>) value));
                sb.append(SEP2);
            } else if (value instanceof Map<?, ?>) {
                //key.toString() + SEP1  +

                sb.append(mapToString((Map<?, ?>) value));
                sb.append(SEP2);
            } else {
                //key.toString() + SEP1 +
                sb.append(value.toString());
                sb.append(SEP2);
            }
        }
        return sb.toString();
    }

    /**
     * 去除首尾分隔符（主要求是去除字符串首尾的分隔符）(",1,2,3,4,5," ------> "1,2,3,4,5")
     *
     * @param str 被处理字符串
     * @param separator 分隔符
     */
    public static String separatorCutOut(String str, String separator) {
        String beginChar = "";
        String endChar = "";
        if (null != str && !"".equals(str)) {
            if (str.trim().length() > 1) {
                str = str.trim().replace(separator + separator, separator);
                beginChar = str.substring(0, 1);
                if (separator.equals(beginChar)) {
                    str = str.substring(1, str.length());
                }
                endChar = str.substring(str.length() - 1, str.length());
                if (separator.equals(endChar)) {
                    str = str.substring(0, str.length() - 1);
                }
                if (!separator.equals(beginChar) && !separator.equals(endChar) && !str.contains(separator + separator)) {
                    return str;
                } else {
                    return separatorCutOut(str, separator);
                }
            } else {
                if (separator.equals(str)) {
                    return "";
                } else {
                    return str;
                }
            }
        } else {
            return "";
        }
    }

    /**
     * 分隔符分隔的字符串转换为map(key和value一样)
     *
     * @param strs 逗号分隔的字符串
     * @param splitKey 分隔符
     * @return map
     */
    public static HashMap<String, String> strToMap(String strs, String splitKey) {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        if (!isAllEmpty(strs)) {
            String[] strArray = strs.split(splitKey);
            for (int i = 0, len = strArray.length; i < len; i++) {
                tempMap.put(strArray[i], strArray[i]);
            }
        }
        return tempMap;
    }

    /**
     * 判断某个字符串是否存在于数组中
     *
     * @param stringArray 原数组
     * @param source      查找的字符串
     * @return 是否找到
     */
    public static boolean contains(String[] stringArray, String source) {
        // 转换为list
        List<String> tempList = Arrays.asList(stringArray);
        // 利用list的包含方法,进行判断
        if (tempList.contains(source)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 为小于10的数字在前面添加“0” ，如 9  返回09
     *
     * @param numberStr 字符串数字
     * @return
     */
    public static String lenghtAddZero(String numberStr) {
        if (numberStr.length() == 1) {
            return 0 + numberStr;
        } else {
            return numberStr;
        }
    }

    /**
     * 获取Str的大小
     *
     * @param str 被处理字符串
     * @return
     */
    public static float getStrSize(String str) {
        float size = 0f;
        try {
            size = (float) str.getBytes("UTF-8").length / 1024;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 清除文本最后的换行符
     *
     * @param str 被处理字符串
     * @return String
     */
    public static String clearEndLineBreak(String str) {
        if (!isBlank(str)) {
            if (str.endsWith("\n")) {
                str = str.substring(0, str.lastIndexOf("\n") - 1);
                return clearEndLineBreak(str);
            } else {
                return str;
            }
        } else {
            return str;
        }
    }

}
