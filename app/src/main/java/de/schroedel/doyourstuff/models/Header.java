package de.schroedel.doyourstuff.models;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
