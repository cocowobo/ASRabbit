package com.ht.baselib.views.pickerview.subview;

import java.util.ArrayList;

/**
 * The simple Array wheel adapter
 * @param <T> the element type
 */
public class ArrayWheelAdapter<T> implements WheelAdapter {
	
	/** The default items length
     * change:4->10
     * update by zmingchun  on 2015/08/24
     * */
	public static final int DEFAULT_LENGTH = 10;
	
	// items
	private ArrayList<T> items;
	// length
	private int length;

	/**
	 * Constructor
	 * @param items the items
	 * @param length the max items length
	 */
	public ArrayWheelAdapter(ArrayList<T> items, int length) {
		this.items = items;
		this.length = length;
	}
	
	/**
	 * Contructor
	 * @param items the items
	 */
	public ArrayWheelAdapter(ArrayList<T> items) {
		this(items, DEFAULT_LENGTH);
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < items.size()) {
			return items.get(index).toString();
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return items.size();
	}

	@Override
	public int getMaximumLength() {
		return length;
	}

}
