package de.schroedel.doitlater.content;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import de.schroedel.doitlater.R;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * Header item in to do list.
 */
public class Header implements ListItem
{
	private String text;
	private int dayOfWeek;

	static class ViewHolder
	{
		TextView tvHeader;
	}

	/**
	 * Creates new header with given text.
	 *
	 * @param text - header text
	 */
	public Header(String text, int dayOfWeek)
	{
		this.text = text;
		this.dayOfWeek = dayOfWeek;
	}

	@Override
	public ItemType getItemType()
	{
		return ItemType.HEADER_ITEM;
	}

	@Override
	public View getView(
		LayoutInflater inflater,
		View convertView,
		ViewGroup parent)
	{
		ViewHolder viewHolder;

		if (convertView == null)
		{
			convertView = inflater.inflate(
				R.layout.todo_list_header,
				parent,
				false);

			viewHolder = new ViewHolder();
			viewHolder.tvHeader =
				(TextView) convertView.findViewById(R.id.list_item_header);

			convertView.setTag(viewHolder);
		}
		else
			viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.tvHeader.setText(text);

		convertView.setBackgroundColor(
			ContextCompat.getColor(
				inflater.getContext(),
				getColorForDayOfWeek(dayOfWeek)));

		return convertView;
	}

	/**
	 * Returns color resource equivalent to day of week.
	 *
	 * @param dayOfWeek - day of week
	 * @return - color resource
	 */
	private static int getColorForDayOfWeek(int dayOfWeek)
	{
		switch (dayOfWeek)
		{
			case Calendar.MONDAY: return R.color.background_monday;
			case Calendar.TUESDAY: return R.color.background_tuesday;
			case Calendar.WEDNESDAY: return R.color.background_wednesday;
			case Calendar.THURSDAY: return R.color.background_thursday;
			case Calendar.FRIDAY: return R.color.background_friday;
			case Calendar.SATURDAY: return R.color.background_saturday;
			case Calendar.SUNDAY: return R.color.background_sunday;

			default: return R.color.background_default;
		}
	}
}
