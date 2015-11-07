package de.schroedel.doyourstuff.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import de.schroedel.doyourstuff.content.ListItem;
import de.schroedel.doyourstuff.database.ToDoDatabase;
import de.schroedel.doyourstuff.content.ToDoItem;
import de.schroedel.doyourstuff.database.ToDoEntryTable;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * Adapter holding and managing to do list items.
 */
public class ToDoListAdapter extends ArrayAdapter<ListItem>
{
	private List<ListItem> items;
	private LayoutInflater inflater;

	/**
	 * Creates to do list adapter managing items.
	 *
	 * @param context - activity context
	 * @param items - to do items
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

		return item.getView(
			inflater,
			convertView,
			parent);
	}

	@Override
	public void add(ListItem item)
	{
		items.add(item);

		if (!(item instanceof ToDoItem))
			return;

		ToDoDatabase database = ToDoDatabase.getInstance(getContext());
		ToDoEntryTable entryTable = database.getToDoEntryTable();

		if (item.getItemType() == ListItem.ItemType.LIST_ITEM)
			entryTable.insert(item);

		notifyDataSetChanged();
	}

	@Override
	public void remove(ListItem item)
	{
		items.remove(item);

		if (!(item instanceof ToDoItem))
			return;

		ToDoDatabase database = ToDoDatabase.getInstance(getContext());
		ToDoEntryTable entryTable = database.getToDoEntryTable();

		if (item.getItemType() == ListItem.ItemType.LIST_ITEM)
			entryTable.remove(((ToDoItem) item).id);

		notifyDataSetChanged();
	}
}
