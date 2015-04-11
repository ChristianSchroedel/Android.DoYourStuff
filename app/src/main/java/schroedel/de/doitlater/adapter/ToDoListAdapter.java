package schroedel.de.doitlater.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import schroedel.de.doitlater.content.ListItem;
import schroedel.de.doitlater.content.ToDoDatabase;
import schroedel.de.doitlater.content.ToDoItem;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * Adapter holding and managing to do list items.
 */
public class ToDoListAdapter extends ArrayAdapter<ListItem>
{
	private List<ListItem> items;
	private LayoutInflater inflater;

	public enum ItemType
	{
		LIST_ITEM(0),
		HEADER_ITEM(1);

		public int value;

		private ItemType(int value)
		{
			this.value = value;
		}
	}

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
		return ItemType.values().length;
	}

	@Override
	public int getItemViewType(int position)
	{
		return items.get(position).getItemType();
	}

	@Override
	public int getCount()
	{
		return items.size();
	}

	@Override
	public ListItem getItem(int position)
	{
		return items.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return items.get(position).getView(inflater, convertView);
	}

	@Override
	public void add(ListItem item)
	{
		items.add(item);

		if (item.getItemType() == ItemType.LIST_ITEM.value)
			ToDoDatabase.getInstance(getContext()).insertItem((ToDoItem) item);

		notifyDataSetChanged();
	}

	@Override
	public void remove(ListItem item)
	{
		items.remove(item);

		if (item.getItemType() == ItemType.LIST_ITEM.value)
			ToDoDatabase.getInstance(getContext()).removeItem((ToDoItem) item);

		notifyDataSetChanged();
	}
}
