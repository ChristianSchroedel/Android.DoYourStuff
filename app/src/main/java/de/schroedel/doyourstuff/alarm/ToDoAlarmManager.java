package de.schroedel.doyourstuff.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import de.schroedel.doyourstuff.content.ToDoItem;
import de.schroedel.doyourstuff.receiver.AlarmReceiver;

/**
 * Static class managing alarms for {@link ToDoItem} objects.
 */
public final class ToDoAlarmManager
{
	/**
	 * Sets reminder alarm for given {@link ToDoItem}.
	 *
	 * @param item item to set alarm for
	 */
	public static void setReminderAlarm(Context context, ToDoItem item)
	{
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra(ToDoItem.EXTRA_ITEM, item);

		PendingIntent alarm =
			PendingIntent.getBroadcast(context, (int) item.id, intent, 0);

		AlarmManager manager = getAlarmManager(context);
		manager.set(AlarmManager.RTC_WAKEUP, item.timestamp, alarm);
	}

	/**
	 * Cancels reminder alarm for given {@link ToDoItem}.
	 *
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
	 * Returns {@link AlarmManager} system service.
	 *
	 * @return alarm manager
	 */
	private static AlarmManager getAlarmManager(Context context)
	{
		return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}
}
