package de.schroedel.doyourstuff.views.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.models.Category;
import de.schroedel.doyourstuff.models.Header;
import de.schroedel.doyourstuff.models.ListItem;
import de.schroedel.doyourstuff.models.ToDoItem;
import de.schroedel.doyourstuff.utils.DateTimeHelper;

/**
 * Adapter containing available {@link ToDoItem} information.
 */
public class ToDoListAdapter extends ArrayAdapter<ListItem>
{
	private List<ListItem> items;
	private LayoutInflater inflater;

	static class ToDoEntryViewHolder
	{
		ImageView ivCategory;
		TextView tvTitle;
		TextView tvDesc;
		TextView tvTime;
	}

	static class HeaderViewHolder
	{
		TextView tvHeader;
	}

	/**
	 * Creates {@link ToDoListAdapter} containing {@link ToDoItem} items.
	 *
	 * @param context activity context
	 * @param items items
	 */
	public ToDoListAdapter(Context context, List<ListItem> items)
	{
		super(context, 0);

		this.items = items;
		this.inflater = LayoutInflater.from(context);
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
	public int getViewTypeCount()
	{
		return ListItem.TYPE_CNT;
	}

	@Override
	public int getItemViewType(int position)
	{
		ListItem item = getItem(position);

		return item.getItemType();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ListItem item = getItem(position);
		int itemType = getItemViewType(position);

		if (itemType == ListItem.TYPE_TODO_ENTRY)
			return getToDoEntryView((ToDoItem) item, convertView, parent);
		else if (itemType == ListItem.TYPE_HEADER)
			return getHeaderView((Header) item, convertView, parent);

		return null;
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

	@Override
	public int getPosition(ListItem item)
	{
		return items.indexOf(item);
	}

	/**
	 * Returns {@link View} representing a to do list entry item.
	 *
	 * @param item to do list entry
	 * @param convertView view to inflate
	 * @param parent parent view group
	 * @return created view
	 */
	private View getToDoEntryView(
		ToDoItem item,
		View convertView,
		ViewGroup parent)
	{
		ToDoEntryViewHolder viewHolder;

		if (convertView == null)
		{
			convertView = inflater.inflate(
				R.layout.list_item_todo_entry,
				parent,
				false);

			viewHolder = new ToDoEntryViewHolder();
			viewHolder.ivCategory =
				(ImageView) convertView.findViewById(R.id.todo_list_category);
			viewHolder.tvTitle =
				(TextView) convertView.findViewById(R.id.todo_list_title);
			viewHolder.tvDesc =
				(TextView) convertView.findViewById(R.id.todo_list_desc);
			viewHolder.tvTime =
				(TextView) convertView.findViewById(R.id.todo_list_time);

			convertView.setTag(viewHolder);
		}
		else
			viewHolder = (ToDoEntryViewHolder) convertView.getTag();

		viewHolder.ivCategory.setImageDrawable(
			Category.getCategoryDrawable(inflater.getContext(), item.category));
		viewHolder.tvTitle.setText(item.title);
		viewHolder.tvDesc.setText(item.description);

		String dateTime = null;

		if (item.timestamp > 0)
			dateTime = DateTimeHelper.getFormattedDate(item.timestamp, "HH:mm");

		viewHolder.tvTime.setText(dateTime);

		return convertView;
	}

	/**
	 * Returns {@link View} representing a to do list header item.
	 *
	 * @param header to do list header
	 * @param convertView view to inflate
	 * @param parent parent view group
	 * @return created view
	 */
	private View getHeaderView(
		Header header,
		View convertView,
		ViewGroup parent)
	{
		HeaderViewHolder viewHolder;

		if (convertView == null)
		{
			convertView = inflater.inflate(
				R.layout.list_item_header,
				parent,
				false);

			viewHolder = new HeaderViewHolder();
			viewHolder.tvHeader =
				(TextView) convertView.findViewById(R.id.list_item_header);

			convertView.setTag(viewHolder);
		}
		else
			viewHolder = (HeaderViewHolder) convertView.getTag();

		viewHolder.tvHeader.setText(header.text);

		convertView.setBackgroundColor(
			ContextCompat.getColor(
				inflater.getContext(),
				header.getHeaderColor()));

		return convertView;
	}

	/**
	 * Sets collection of {@link ListItem} items.
	 *
	 * @param items items
	 */
	public void setItems(List<ListItem> items)
	{
		this.items = items;
		notifyDataSetChanged();
	}
}
