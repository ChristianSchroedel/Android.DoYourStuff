package de.schroedel.doyourstuff.network.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.schroedel.doyourstuff.models.ToDoItem;
import de.schroedel.doyourstuff.views.notifications.AlarmNotification;

/**
 * Receiver receiving alarms to remind user.
 */
public class AlarmReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent data)
	{
		if (data == null)
			return;

		long itemId = data.getLongExtra(ToDoItem.EXTRA_ITEM_ID, 0);

		ToDoItem item = ToDoItem.fromDatabase(context, itemId);

		if (item == null)
			return;

		AlarmNotification notification = AlarmNotification.getInstance(context);
		notification.showAlarm(item);
	}
}
