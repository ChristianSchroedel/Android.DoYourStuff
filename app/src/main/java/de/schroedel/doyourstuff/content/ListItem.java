package de.schroedel.doyourstuff.content;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * Item contained in to do list.
 */
public interface ListItem
{
	enum ItemType
	{
		LIST_ITEM(0),
		HEADER_ITEM(1);

		public int value;

		ItemType(int value)
		{
			this.value = value;
		}
	}

	/**
	 * Returns type of item.
	 *
	 * @return - type of item
	 */
	ItemType getItemType();

	/**
	 * Returns item view.
	 *
	 * @param inflater - inflater used to create item view
	 * @param convertView - cached item view (may be null)
	 * @param parent - parent view
	 * @return - item view
	 */
	View getView(LayoutInflater inflater, View convertView, ViewGroup parent);
}
