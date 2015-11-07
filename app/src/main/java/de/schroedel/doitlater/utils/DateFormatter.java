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

	/**
	 * Checks if given date is today.
	 *
	 * @param timestamp - time stamp
	 * @return - true if today else false
	 */
	public static boolean dateIsToday(long timestamp)
	{
		return hasSameDay(System.currentTimeMillis(), timestamp);
	}

	/**
	 * Checks if two timestamps are on the same day.
	 *
	 * @param timestamp - first timestamp
	 * @param timestampOther - other timestamp
	 */
	public static boolean hasSameDay(long timestamp, long timestampOther)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

		calendar.setTimeInMillis(timestampOther);

		return (calendar.get(Calendar.YEAR) == year &&
			calendar.get(Calendar.MONTH) == month &&
			calendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth);
	}

	/**
	 * Checks if given timestamp is in the past.
	 *
	 * @param timestamp - timestamp
	 */
	public static boolean dateIsPast(long timestamp)
	{
		return timestamp < System.currentTimeMillis();
	}

	/**
	 * Returns day of week from calendar.
	 *
	 * @param timestamp - timestamp
	 * @return - day of week
	 */
	public static int getCalendarDayOfWeek(long timestamp)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);

		return calendar.get(Calendar.DAY_OF_WEEK);
	}
}
