
package com.ht.baselib.helper.download.android;

import android.content.Context;

import com.ht.baselib.helper.download.constants.NetType;
import com.ht.baselib.helper.download.entity.DefaultConfig;
import com.ht.baselib.utils.NetWorkUtil;

import java.util.Hashtable;

/**
 * Msg:下载配置
 * Update:  2015-11-06
 * Version: 1.0
 * Created by laijiacai on 2015-11-06 11:19.
 */
public class DefaultDownloadConfig extends DefaultConfig {

    private Context mContext;

    public DefaultDownloadConfig(Context context) {
        mContext = context;
    }

    @Override
    public NetType getNetType() {
        String networkType = NetWorkUtil.getNetworkType(mContext);
        if (NetWorkUtil.NetworkType.WIFI.equals(networkType)) {
            return NetType.WIFI;
        } else if (NetWorkUtil.NetworkType.NET_3G.equals(networkType)) {
            return NetType.G3;
        } else if (NetWorkUtil.NetworkType.NET_2G.equals(networkType)) {
            return NetType.G2;
        } else {
            return NetType.UNKNOWN;
        }
    }

    @Override
    public int getBlockSize() {
        if (is2GNet()) {
            // 2G下一次请求8K
            return 8 * 1024;
        }
        return 32 * 1024;
    }

    @Override
    public int getTaskNum() {
        return 2;
    }

    @Override
    public boolean isCmwap() {
        return NetWorkUtil.isCmwap(mContext);
    }

    @Override
    public long getRefreshInterval() {
        return 1000;
    }

    @Override
    public boolean isRange() {
        return true;
    }

    @Override
    public boolean isBlock() {
        return false;
    }

    @Override
    public int getBufferBlockNum() {
        return 800;
    }

    @Override
    public boolean isNetworkAvalid() {
        return NetWorkUtil.isNetworkAvailable(mContext);
    }

    private boolean is2GNet() {
        return getNetType() == NetType.G2;
    }

    @Override
    public Hashtable<String, String> getRequestHeaders() {
        Hashtable<String, String> headers = new Hashtable<String, String>();
        return headers;
    }

    @Override
    public boolean is2GNeedToForceBlock() {
        return false;
    }

}
