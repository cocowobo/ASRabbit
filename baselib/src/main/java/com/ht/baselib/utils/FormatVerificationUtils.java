package com.ht.baselib.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * <p>格式验证工具类 1、提供各种输入格式的验证功能</p>
 *
 * @author hxm
 * @version <p>1.0 (2015/10/19) hxm <br/>1.5 (2015/11/21) zmingchun 完善正则表达式<p/>
 */
public class FormatVerificationUtils {

    // 待验证
    //==============================================================================================

    /**
     * 验证身份证号码-非严格
     *
     * @param idCard 15位和18位身份证号码的基本数字和位数验校
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "(^\\d{15}$)|(\\d{17}(?:\\d|x|X)$)";
        return !(idCard == null || "".equals(idCard)) && testRegex(regex, idCard);
    }


    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *               <p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *               <p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
        return testRegex(regex, mobile);
    }

    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *              <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *              数字之后是空格分隔的国家（地区）代码。</p>
     *              <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     *              <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return testRegex(regex, phone);
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return testRegex(regex, digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDecimals(String decimals) {
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return testRegex(regex, decimals);
    }

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return testRegex(regex, chinese);
    }

    /**
     * 判断是否纯数字
     *
     * @param value 数字字符串
     * @return true为正确，false为错误
     */
    public static boolean matcherNumber(String value) {
        String regex = "[0-9]{1,}";
        return testRegex(regex, value);
    }
    // 已应用
    //==============================================================================================

    /**
     * 判断真实姓名是否格式正确
     * <p>
     * 要求：真实姓名可以是汉字，也可以是字母，但是不能两者都有，也不能包含任何符号和数字
     * 注意：1.如果是英文名,可以允许英文名字中出现空格
     * 2.英文名的空格可以是多个，但是不能连续出现多个
     * 3.汉字不能出现空格
     * </p>
     *
     * @param value 真实姓名字符串
     * @return true为正确，false为错误
     */
    public static boolean matcherRealName(String value) {
        String regex = "^([\\u4e00-\\u9fa5]+|([a-zA-Z]+\\s?)+)$";
        return testRegex(regex, value);
    }

    /**
     * 判断手机号码是否格式正确
     *
     * @param value 手机号码字符串
     * @return true为正确，false为错误
     */
    public static boolean matcherPhoneNum(String value) {
        // 匹配11数字，并且13-19开头
        String regex = "^(\\+?\\d{2}-?)?(1[0-9])\\d{9}$";
        return testRegex(regex, value);
    }

    /**
     * 判断密码格式正确（必须输入6-12位非纯数字、英文的密码）
     *
     * @param value 密码字符串
     * @return true为正确，false为错误
     */
    public static boolean matcherPassword(String value) {
        String regex = "^[a-zA-Z0-9]{6,12}$";// (6-12位字母或数字)
//        String regex = "(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}";//(6-12位字母或数字,必须同时包含字母和数字)
        return testRegex(regex, value);
    }

    /**
     * 判断注册账号格式正确
     *
     * @param value 账号字符串
     * @return true为正确，false为错误
     */
    public static boolean matcherAccount(String value) {
        // （4-20位字符）
        String regex = "[\\u4e00-\\u9fa5a-zA-Z0-9\\-]{4,20}";
        return testRegex(regex, value);
    }

    /**
     * 判断邮箱格式正确
     *
     * @param value 邮箱字符串
     * @return true为正确，false为错误
     */
    public static boolean matcherEmail(String value) {
//        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)" +
//                "+[a-zA-Z]{2,}$";
        String regex = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
        return testRegex(regex, value);
    }

    /**
     * 判断IP地址格式正确
     *
     * @param value IP字符串
     * @return true为正确，false为错误
     */
    public static boolean matcherIP(String value) {
        String regex = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\" +
                "d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\" +
                "d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
        return testRegex(regex, value.toLowerCase());
    }

    /**
     * 判断身份证格式正确性
     *
     * @param value 身份证字符串
     * @return true为正确，false为错误
     */

    public static boolean matcherIdentityCard(String value) {
//        String regex = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|" +
//                "(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|" +
//                "\\d{3}[Xx])$)$";
//        return testRegex(regex, value);
        IDCardTester idCardTester = new IDCardTester();
        return idCardTester.test(value);
    }

    /**
     * 判断网址格式正确性 支持http，https，ftp
     *
     * @param value 网址字符串
     * @return true为正确，false为错误
     */
    public static boolean matcherUrl(String value) {
        String regex = "^(([hH][tT]{2}[pP][sS]?)|([fF][tT][pP]))\\:\\/\\/[wW]{3}\\.[\\w-]+\\.\\w{2,4}(\\/.*)?$";
        return testRegex(regex, value.toLowerCase());
    }

    /**
     * 中国民用车辆号牌
     *
     * @param value 车牌号码
     * @return true为正确，false为错误
     */
    public static boolean matcherVehicleNumber(String value) {
        String regex = "^[京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼川贵云藏陕甘青宁新渝]?[A-Z][A-HJ-NP-Z0-9学挂港澳练]{5}$";
        return testRegex(regex, value.toLowerCase());
    }

    // 公用方法
    //============================================================

    /**
     * 身份证校验
     * <p/>
     * 根据〖中华人民共和国国家标准 GB 11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * 地址码表示编码对象常住户口所在县(市、旗、区)的行政区划代码。
     * 出生日期码表示编码对象出生的年、月、日，其中年份用四位数字表示，年、月、日之间不用分隔符。
     * 顺序码表示同一地址码所标识的区域范围内，对同年、月、日出生的人员编定的顺序号。顺序码的奇数分给男性，偶数分给女性。
     * 校验码是根据前面十七位数字码，按照ISO 7064:1983.MOD 11-2校验码计算出来的检验码。
     * <p/>
     * 出生日期计算方法。
     * 15位的身份证编码首先把出生年扩展为4位，简单的就是增加一个19或18,这样就包含了所有1800-1999年出生的人;
     * 2000年后出生的肯定都是18位的了没有这个烦恼，至于1800年前出生的,那啥那时应该还没身份证号这个东东，⊙﹏⊙b汗...
     * 下面是正则表达式:
     * 出生日期1800-2099  /(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])/
     * 身份证正则表达式 /^[1-9]\d{5}((1[89]|20)\d{2})(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dx]$/i
     * 15位校验规则 6位地址编码+6位出生日期+3位顺序号
     * 18位校验规则 6位地址编码+8位出生日期+3位顺序号+1位校验位
     * <p/>
     * 校验位规则     公式:∑(ai×Wi)(mod 11)……………………………………(1)
     * 公式(1)中：
     * i----表示号码字符从由至左包括校验码在内的位置序号；
     * ai----表示第i位置上的号码字符值；
     * Wi----示第i位置上的加权因子，其数值依据公式Wi=2^(n-1）(mod 11)计算得出。
     * i 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1
     * Wi 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 1
     * </P>
     *
     * @author Yoojia.Chen (yoojia.chen@gmail.com)
     * @version version 2015-05-21
     * @since 2.0
     */
    private static class IDCardTester {
        public boolean test(String content) {
            if (TextUtils.isEmpty(content)) {
                return false;
            }
            final int length = content.length();
            if (15 == length) {
                try {
                    return isOldCNIDCard(content);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                }
            } else if (18 == length) {
                return isNewCNIDCard(content);
            } else {
                return false;
            }
        }

        final int[] WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

        final char[] VALID = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        public boolean isNewCNIDCard(String numbers) {
            //转换字符串中的字母为大写字母
            numbers = numbers.toUpperCase();
            int sum = 0;
            for (int i = 0; i < WEIGHT.length; i++) {
                final int cell = Character.getNumericValue(numbers.charAt(i));
                sum += WEIGHT[i] * cell;
            }
            int index = sum % 11;
            return VALID[index] == numbers.charAt(17);
        }

        public boolean isOldCNIDCard(String numbers) {
            //ABCDEFYYMMDDXXX
            String yymmdd = numbers.substring(6, 11);
            boolean aPass = numbers.equals(String.valueOf(Long.parseLong(numbers)));
            boolean yPass = true;
            try {
                new SimpleDateFormat("yyMMdd").parse(yymmdd);
            } catch (Exception e) {
                LogUtils.wv("----IDCard 校验失败 : " + numbers);
                e.printStackTrace();
                yPass = false;
            }
            LogUtils.e("----IDCard aPass : " + aPass);
            LogUtils.e("----IDCard yPass : " + yPass);
            return aPass && yPass;
        }
    }

    /**
     * 是否数值型
     * <br/>implements commit apache common utils
     *
     * @param input 校验内容
     * @return true为正确，false为错误
     */
    public static boolean isNumeric(String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        char[] chars = input.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal loader any possible sign up front
        int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
        if (sz > start + 1) {
            if (chars[start] == '0' && chars[start + 1] == 'x') {
                int i = start + 2;
                if (i == sz) {
                    return false; // str == "0x"
                }
                // checking hex (it can't be anything else)
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9')
                            && (chars[i] < 'a' || chars[i] > 'f')
                            && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--; // don't want to loop to the last char, check it afterwords
        // for build qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid numeric (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                // no build qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (!allowSigns
                    && (chars[i] == 'd'
                    || chars[i] == 'D'
                    || chars[i] == 'f'
                    || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l'
                    || chars[i] == 'L') {
                // not allowing L loader an exponent
                return foundDigit && !hasExp;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return !allowSigns && foundDigit;
    }

    /**
     * 校验内容是否匹配指定正则表达式
     *
     * @param regex      正则表达式
     * @param inputValue 内容
     * @return 是否匹配 true为匹配，false为不匹配
     */
    public static boolean testRegex(String regex, String inputValue) {
        return Pattern.compile(regex).matcher(inputValue).matches();
    }


    //------------------------------------- 检验多个EditText 是否都填写内容  fxj---------------------------------

    /**
     * 检验多个EditText：文本变更监听
     */
    private static TextWatcher twMultiET = null;

    /**
     * 检验多个EditText 是否都填写内容
     *
     * @param editTexts                    需要检查的EditText
     * @param checkMultiETIsFilledListener 检查结果回调
     */
    public static void checkMultiETIsFilled(final ArrayList<EditText> editTexts, final CheckMultiETIsFilledListener checkMultiETIsFilledListener) {
        twMultiET = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                for (int i = 0; i < editTexts.size(); i++) {
                    if (editTexts.get(i).getText() == null || editTexts.get(i).getText().toString().trim().equals("")) {
                        checkMultiETIsFilledListener.onCheckMultiETIsFilled(false);
                        return;
                    }
                }
                checkMultiETIsFilledListener.onCheckMultiETIsFilled(true);
            }
        };
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(twMultiET);
        }
    }

    /**
     * 检验多个EditText是否全部填写的回调
     */
    public interface CheckMultiETIsFilledListener {
        /**
         * 检验结果回来
         *
         * @param isAllFilled 是否全部填写
         */
        void onCheckMultiETIsFilled(boolean isAllFilled);
    }

    /**
     * 检测字符串长度
     *
     * @param input    输入串
     * @param minRange 最少长度
     * @param maxRange 最大长度
     * @return true 符合，false 不符合
     */
    public static boolean checkInputRangeIsConform(String input, int minRange, int maxRange) {
        if (input == null) {
            return false;
        }

        int length = input.length();

        if (length >= minRange && length <= maxRange) {
            return true;
        }

        return false;
    }

    //------------------------------------- 检验多个EditText 是否都填写内容  end---------------------------------
}
