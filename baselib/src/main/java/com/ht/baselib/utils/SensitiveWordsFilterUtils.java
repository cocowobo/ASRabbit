package com.ht.baselib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 敏感词过滤&替换
 * <br/>初始化敏感词库，将敏感词加入到HashMap中，构建DFA
 * matchType 匹配规则1：最小匹配规则，2：最大匹配规则
 * </p>
 *
 * @author wenwei.chen
 * @version 1.0 (2016/1/7)
 */
public class SensitiveWordsFilterUtils {
    /**
     * 敏感词库
     */
    public Map sensitiveWordMap = null;
    /**
     * Hash敏感词库
     */
    public HashMap sensitiveWordHashMap;
    /**
     * 匹配类型
     */
    public int filterType = 2;
    /**
     * 过滤的词库
     */
    public String filterStr;
    /**
     * 替换的字符串
     */
    public String filterChar = "*";
    /**
     * 最小匹配规则
     */
    public static int minMatchType = 1;
    /**
     * 最大匹配规则
     */
    public static int maxMatchType = 2;
    /**
     * 字符编码
     */
    public String ENCODING = "UTF-8";

    /**
     * 构造函数，初始化敏感词库
     */
    public SensitiveWordsFilterUtils() {
        sensitiveWordMap = initKeyWord();
    }

    /**
     * 构造函数，初始化敏感词库
     * @param keyWordSet set集合
     */
    public SensitiveWordsFilterUtils(Set<String> keyWordSet) {
        sensitiveWordMap = initKeyWord(keyWordSet);
    }

    /**
     * 是否包含敏感字符
     *
     * @return 包含返回true
     */
    public boolean isContaintSensitiveWord() {
        return isContaintSensitiveWord(filterStr,filterType);
    }
    /**
     * 是否包含敏感字符
     *
     * @param txt       判断的文字
     * @param matchType 匹配类型
     * @return 包含返回true
     */
    public boolean isContaintSensitiveWord(String txt, int matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = this.checkSensitiveWord(txt, i, matchType); //判断是否包含敏感字符
            if (matchFlag > 0) {    //大于0存在，返回true
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt       文字
     * @param matchType 匹配类型
     * @return set集合
     */
    public Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<String>();
        int i;
        for (i = 0; i < txt.length(); i++) {
            int length = checkSensitiveWord(txt, i, matchType);    //判断是否包含敏感字符
            if (length > 0) {    //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }
        return sensitiveWordList;
    }

    /**
     * 替换敏感字符
     *
     * @return 返回替换的文字
     */
    public String replaceSensitiveWord() {
        return replaceSensitiveWord(filterStr,filterType,filterChar);
    }

    /**
     * 替换敏感字符
     *
     * @param txt         文字
     * @param matchType   匹配类型
     * @param replaceChar 替换的字符，如“*”
     * @return 返回替换的文字
     */
    public String replaceSensitiveWord(String txt, int matchType, String replaceChar) {
        String resultTxt = txt;
        Set<String> set = getSensitiveWord(txt, matchType);     //获取所有的敏感词
        Iterator<String> iterator = set.iterator();
        String word = null;
        String replaceString = null;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }
        return resultTxt;
    }

    /**
     * 获取替换字符串
     *
     * @param replaceChar 替换的字符
     * @param length      长度
     * @return 替换字符
     */
    private String getReplaceChars(String replaceChar, int length) {
        String resultReplace = replaceChar;
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }
        return resultReplace;
    }

    /**
     * 检查文字中是否包含敏感字符
     *
     * @param txt        文字
     * @param beginIndex 开始位置
     * @param matchType  匹配类型
     * @return 不存在返回0，存在返回敏感字符长度
     */
    @SuppressWarnings({"rawtypes"})
    public int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        boolean flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word = 0;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if (nowMap != null) {     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if ("1".equals(nowMap.get("isEnd"))) {       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true
                    if (SensitiveWordsFilterUtils.minMatchType == matchType) {    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            } else {     //不存在，直接返回
                break;
            }
        }
        if (matchFlag < 2 || !flag) {        //长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }

/*************************************初始化*******************************************************/

    /**
     * 读取敏感词库
     *
     * @return 敏感词库
     */
    public Map initKeyWord() {
        try {
            Set<String> keyWordSet = readSensitiveWordFile();
            addSensitiveWordToHashMap(keyWordSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordHashMap;
    }

    /**
     * 读取敏感词库
     * @param keyWordSet set 集合
     * @return 敏感词库
     */
    @SuppressWarnings("rawtypes")
    public Map initKeyWord(Set<String> keyWordSet) {
        try {
            addSensitiveWordToHashMap(keyWordSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordHashMap;
    }

    /**
     * 读取敏感词库中的内容
     *
     * @return set集合
     * @throws Exception 异常
     */
    @SuppressWarnings("resource")
    private Set<String> readSensitiveWordFile() throws Exception {
        Set<String> set = null;
        String saveDir = SDCardUtils.CACHE_FOLDER;
        FileUtils.createFolder(saveDir);
        File file = new File(saveDir + "SensitiveWord.txt");    //读取文件
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODING);
        try {
            if (file.isFile() && file.exists()) {      //文件流是否存在
                set = new HashSet<String>();
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                while ((txt = bufferedReader.readLine()) != null) {    //读取文件，将文件内容放入到set中
                    set.add(txt);
                }
            } else {         //不存在抛出异常信息
                throw new Exception("敏感词库文件不存在");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            read.close();     //关闭文件流
        }
        return set;
    }

    /**
     * 将敏感词库加入到HashMap中
     *
     * @param keyWordSet 敏感词库
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordHashMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();    //关键字
            nowMap = sensitiveWordHashMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取

                if (wordMap != null) {        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
    }
}
