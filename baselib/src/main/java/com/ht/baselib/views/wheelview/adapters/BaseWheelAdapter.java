/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.ht.baselib.views.wheelview.adapters;

import android.content.Context;

import java.util.ArrayList;

/**
 * The simple Array wheel adapter
 * @param <T> the element type
 */
public class BaseWheelAdapter<T> extends AbstractWheelTextAdapter {

    // items
    private ArrayList<T> datas;

    public BaseWheelAdapter(Context context, ArrayList<T> datas) {
        super(context);
        
        if (datas == null)
            datas = new ArrayList<>();

        this.datas = datas;
    }
    
    @Override
    public CharSequence getItemText(int index) {
        T item = datas.get(index);
        if (item instanceof CharSequence) {
            return (CharSequence) item;
        }

        return "";
    }

    @Override
    public int getItemsCount() {
        return datas.size();
    }
}
