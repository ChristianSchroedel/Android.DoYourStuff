package de.schroedel.doyourstuff.models;

import java.util.Calendar;

import de.schroedel.doyourstuff.R;

/**
 * Header item in a to do list.
 */
public class Header extends ListItem
{
	public String text;
	public int dayOfWeek;

	/**
	 * Creates new {@link Header} with given text.
	 *
	 * @param text header text
	 */
	public Header(String text, int dayOfWeek)
	{
		this.text = text;
		this.dayOfWeek = dayOfWeek;
	}

	@Override
	public int getItemType()
	{
		return ListItem.TYPE_HEADER;
	}

	/**
	 * Returns color resource equivalent to day of week.
	 *
	 * @return color resource
	 */
	public int getHeaderColor()
	{
		switch (dayOfWeek)
		{
			case Calendar.MONDAY: return R.color.brown;
			case Calendar.TUESDAY: return R.color.green_dark;
			case Calendar.WEDNESDAY: return R.color.green;
			case Calendar.THURSDAY: return R.color.indigo;
			case Calendar.FRIDAY: return R.color.blue_dark;
			case Calendar.SATURDAY: return R.color.purple;
			case Calendar.SUNDAY: return R.color.red_dark;

			default: return R.color.blue;
		}
	}
}
