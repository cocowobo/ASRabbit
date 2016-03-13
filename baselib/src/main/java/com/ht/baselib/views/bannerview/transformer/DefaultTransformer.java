package com.ht.baselib.views.bannerview.transformer;

import android.view.View;

/**
 * <p></p>
 *
 * @author 王多新
 * @version 1.0 (2016-1-27)
 */
public class DefaultTransformer extends ABaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
    }

    @Override
    public boolean isPagingEnabled() {
        return true;
    }

}

