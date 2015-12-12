package de.schroedel.doyourstuff.managers;

import android.content.Context;
import android.content.Intent;

import de.schroedel.doyourstuff.models.ToDoItem;
import de.schroedel.doyourstuff.models.preferences.Preferences;
import de.schroedel.doyourstuff.network.receiver.AlarmReceiver;
import de.schroedel.doyourstuff.utils.AlarmHelper;
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
	 */
	public static void setReminderAlarm(Context context, ToDoItem item)
	{
		long dateTime = item.timestamp;

		// Only set alarms for valid timestamps + events in the future events.
		if (dateTime <= 0 ||
			DateTimeHelper.dateIsPast(dateTime))
			return;

		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra(ToDoItem.EXTRA_ITEM_ID, item.id);

		long alarmTime = item.timestamp - Preferences.getAlarmLeadTime(context);

		AlarmHelper.setAlarm(context, (int) item.id, alarmTime, intent);
	}

	/**
	 * Cancels reminder alarm for given {@link ToDoItem}.
	 *
	 * @param context context
	 * @param item item to cancel alarm for
	 */
	public static void cancelReminderAlarm(Context context, ToDoItem item)
	{
		AlarmHelper.cancelAlarm(
			context,
			(int) item.id,
			new Intent(context, AlarmReceiver.class));
	}
}
