package com.ht.baselib.utils;

import java.math.BigDecimal;

/**
 * 数学运算转换操作工具类
 * @author zmingchun
 * @version 1.0(2015/10/22)
 */
public class MathUtil {

    /**
     * 字符串转Integer
     * @param numStr 字符串数字
     * @return int.若为null表示字符串为空或者转换错误
     */
    public static Integer str2Integer(String numStr){
        if(StringUtils.isBlank(numStr)){
            return null;
        }else {
            try{
                return Integer.parseInt(numStr);
            }catch (Exception e){
                return null;
            }
        }
    }

    /**
     * 判断数值是否相等
     * @param num 数值
     * @param numStr 字符串数值
     * @return boolean:true相等，false否
     */
    public static boolean numEqualsNumstr(int num, String numStr) {
        Integer num2 = str2Integer(numStr);
        return null != num2 && num == num2;
    }


    /**
     * 获取四舍五入的小数
     * @param num 数字字符串
     * @param scale 保留小数位
     */
    public static String getScaleData(String num, int scale){
        if(StringUtils.isBlank(num)){
            num= "0";
        }
        BigDecimal mData = new BigDecimal(num)
                .setScale(scale, BigDecimal.ROUND_HALF_UP); //四舍五入，保留两位小数

        return ""+mData;
    }
}
