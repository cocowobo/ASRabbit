package com.adolsai.asrabbit.app;

import com.orhanobut.hawk.Hawk;

/**
 * <p>GlobalUrl类 1、提供URL地址</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-5-27 14:47)<br/>
 */
public class GlobalUrl {

    /**
     * 以com为结尾的dns
     */
    public static final String DNS_COM = "bbs.jjwxc.com";
    /**
     * 以net结尾的dns
     */
    public static final String DNS_NET = "bbs.jjwxc.net";

    /**
     * 关于
     */
    public static final String ABOUT_URL = "https://www.baidu.com";

    /**
     * 用户许可协议
     */
    public static final String AGREEMENT_URL = "https://www.baidu.com";


    /**
     * 获取分区下的详细数据
     *
     * @param id   分区id
     * @param page 页数
     * @return url
     */
    public static String getBoardUrlByIdAndPage(String id, int page) {
//        http://bbs.jjwxc.net/board.php?board=2&page=1&r=2341
        String dns = Hawk.get(SharePreferenceKey.SETTING_NET_DNS, DNS_COM);
        return "http://" + dns + "/board.php?board=" + id + "&page=" + page;
    }


}
