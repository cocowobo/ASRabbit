package com.ht.baselib.views.pulltorefresh.internal;

import com.ht.baselib.utils.LogUtils;

public class Utils {

    static final String LOG_TAG = "PullToRefresh";

    public static void warnDeprecation(String depreacted, String replacement) {
        LogUtils.e(LOG_TAG, "You're using the deprecated " + depreacted + " attr, please switch over to " + replacement);
    }

}
