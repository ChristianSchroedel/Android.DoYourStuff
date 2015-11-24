package de.schroedel.doyourstuff.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.schroedel.doyourstuff.activity.SettingsActivity;
import de.schroedel.doyourstuff.content.ToDoItem;
import de.schroedel.doyourstuff.receiver.AlarmReceiver;
import de.schroedel.doyourstuff.utils.DateTimeHelper;

/**
 * Static class managing alarms for {@link ToDoItem} objects.
 */
public final class ToDoAlarmManager
{
	/**
	 * Sets reminder alarm for given {@link ToDoItem}.
	 *
	 * @param context context
	 * @param item item to set alarm for
	 * @param leadTime lead time of an alarm
	 */
	public static void setReminderAlarm(
		Context context,
		ToDoItem item,
		long leadTime)
	{
		long dateTime = item.timestamp;

		// Only set alarms for valid timestamps + events in the future events.
		if (dateTime <= 0 ||
			DateTimeHelper.dateIsPast(dateTime))
			return;

		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra(ToDoItem.EXTRA_ITEM, item);

		PendingIntent alarm =
			PendingIntent.getBroadcast(context, (int) item.id, intent, 0);

		long alarmTime = item.timestamp - leadTime;

		AlarmManager manager = getAlarmManager(context);
		manager.set(AlarmManager.RTC_WAKEUP, alarmTime, alarm);
	}

	/**
	 * Cancels reminder alarm for given {@link ToDoItem}.
	 *
	 * @param context context
	 * @param item item to cancel alarm for
	 */
	public static void cancelReminderAlarm(Context context, ToDoItem item)
	{
		Intent intent = new Intent(context, AlarmReceiver.class);

		PendingIntent alarm =
			PendingIntent.getBroadcast(context, (int) item.id, intent, 0);

		AlarmManager manager = getAlarmManager(context);
		manager.cancel(alarm);
	}

	/**
	 * Returns lead time of an alarm for {@link ToDoItem}s from applications
	 * shared preferences.
	 *
	 * @return lead time
	 */
	public static long getAlarmLeadTime(Context context)
	{
		SharedPreferences sharedPreferences =
			PreferenceManager.getDefaultSharedPreferences(context);

		return Long.valueOf(
			sharedPreferences.getString(
				SettingsActivity.KEY_PREF_ALARM_LEAD_TIME,
				""));
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
