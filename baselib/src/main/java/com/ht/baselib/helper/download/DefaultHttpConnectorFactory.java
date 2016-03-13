
package com.ht.baselib.helper.download;

import com.ht.baselib.helper.download.interfaces.IHttpConnector;

/**
 * Msg:默认连网类工厂
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class DefaultHttpConnectorFactory {

    /**
     *
     * @param isCmwap 是否是cmwap
     * @return 默认联网实体
     */
    public static IHttpConnector create(boolean isCmwap) {
        return new DefaultHttpConnector(isCmwap);
    }

}
