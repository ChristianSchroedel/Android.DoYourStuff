package de.schroedel.doyourstuff.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import de.schroedel.doyourstuff.content.ListItem;
import de.schroedel.doyourstuff.content.ToDoItem;

/**
 * Adapter containing available {@link ToDoItem} information.
 */
public class ToDoListAdapter extends ArrayAdapter<ListItem>
{
	private List<ListItem> items;
	private LayoutInflater inflater;

	/**
	 * Creates {@link ToDoListAdapter} containing {@link ToDoItem} items.
	 *
	 * @param context activity context
	 * @param items items
	 */
	public ToDoListAdapter(Context context, List<ListItem> items)
	{
		super(context, 0, items);

		this.items = items;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getViewTypeCount()
	{
		return ListItem.ItemType.values().length;
	}

	@Override
	public int getItemViewType(int position)
	{
		ListItem item = getItem(position);

		return item.getItemType().value;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ListItem item = getItem(position);

		return item.getView(inflater, convertView, parent);
	}

	@Override
	public void remove(ListItem item)
	{
		items.remove(item);
		notifyDataSetChanged();
	}

	@Override
	public void insert(ListItem item, int index)
	{
		items.add(index, item);
		notifyDataSetChanged();
	}
}
