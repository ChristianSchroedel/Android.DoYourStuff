package de.schroedel.doitlater.content;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import de.schroedel.doitlater.R;
import de.schroedel.doitlater.adapter.ToDoListAdapter;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * Header item in to do list.
 */
public class Header implements ListItem
{
	private String text;

	static class ViewHolder
	{
		TextView tvHeader;
	}

	/**
	 * Creates new header with given text.
	 *
	 * @param text - header text
	 */
	public Header(String text)
	{
		this.text = text;
	}

	@Override
	public int getItemType()
	{
		return ToDoListAdapter.ItemType.HEADER_ITEM.value;
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView)
	{
		ViewHolder viewHolder;

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.todo_list_header, null);

			viewHolder = new ViewHolder();
			viewHolder.tvHeader =
				(TextView) convertView.findViewById(R.id.list_item_header);

			convertView.setTag(viewHolder);
		}
		else
			viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.tvHeader.setText(text);

		return convertView;
	}
}
