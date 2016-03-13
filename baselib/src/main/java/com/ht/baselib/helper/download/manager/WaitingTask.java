
package com.ht.baselib.helper.download.manager;

import com.ht.baselib.helper.download.entity.ParamsWrapper;
import com.ht.baselib.helper.download.interfaces.IProgressListener;

/**
 * Msg:等待下载任务
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class WaitingTask {
    ParamsWrapper pw;

    IProgressListener listener;

    @Override
    public int hashCode() {
        if (pw != null) {
            return pw.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        if (pw != null) {
            return pw.equals(((WaitingTask) obj).pw);
        }
        return super.equals(obj);
    }
}
