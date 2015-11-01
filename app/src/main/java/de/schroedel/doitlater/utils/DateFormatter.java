package de.schroedel.doitlater.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Christian Schrödel on 01.11.15.
 *
 * Utility class to format date time strings.
 */
public final class DateFormatter
{
	/**
	 * Returns formatted string using given UTC timestamp + pattern.
	 *
	 * @param timestamp - UTC timestamp
	 * @param pattern - format pattern
	 * @return - formatted string
	 */
	public static String getFormattedDate(long timestamp, String pattern)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);

		SimpleDateFormat dateFormat =
			new SimpleDateFormat(pattern, Locale.getDefault());

		dateFormat.setCalendar(calendar);

		return dateFormat.format(calendar.getTime());
	}

}
