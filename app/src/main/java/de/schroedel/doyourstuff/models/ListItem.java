package de.schroedel.doyourstuff.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Item contained in to do list.
 */
public abstract class ListItem
{
	public static final int TYPE_CNT = 2;

	public static final int TYPE_HEADER = 0;
	public static final int TYPE_TODO_ENTRY = 1;

	/**
	 * Returns type of item.
	 *
	 * @return type of item
	 */
	public abstract int getItemType();
}
