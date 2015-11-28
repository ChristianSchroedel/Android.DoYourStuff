package de.schroedel.doyourstuff.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Utility class to set and cancel alarm timers in the {@link AlarmManager}
 * system service.
 */
public final class AlarmHelper
{
	/**
	 * Sets reusable alarm with ID at given time with and sends a broadcast with
	 * a optional {@link Intent} extra.
	 *
	 * @param context context
	 * @param id ID of alarm
	 * @param timestamp time alarm will be triggered
	 * @param intent intent containing extra data (optional)
	 */
	public static void setAlarm(
		Context context,
		int id,
		long timestamp,
		Intent intent)
	{
		PendingIntent alarm = PendingIntent.getBroadcast(
			context,
			id,
			intent,
			0);

		AlarmManager manager = getAlarmManager(context);
		manager.set(AlarmManager.RTC_WAKEUP, timestamp, alarm);
	}

	/**
	 * Cancels set reminder alarm with ID and sends an broadcast with a optional
	 * {@link Intent} extra.
	 * <p>
	 * If there is no alarm set with the provided ID, nothing happens.
	 * </p>
	 *
	 * @param context context
	 * @param id ID of alarm to cancel
	 * @param intent intent containing extra data (optional)
	 */
	public static void cancelAlarm(Context context, int id, Intent intent)
	{
		PendingIntent alarm = PendingIntent.getBroadcast(
			context,
			id,
			intent,
			0);

		AlarmManager manager = getAlarmManager(context);
		manager.cancel(alarm);
	}

	/**
	 * Returns {@link AlarmManager} system service.
	 *
	 * @param context context
	 * @return alarm manager
	 */
	private static AlarmManager getAlarmManager(Context context)
	{
		return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}
}
