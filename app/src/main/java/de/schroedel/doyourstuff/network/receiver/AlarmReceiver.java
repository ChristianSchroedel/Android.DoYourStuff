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

		ToDoItem item = data.getParcelableExtra(ToDoItem.EXTRA_ITEM);

		if (item == null)
			return;

		AlarmNotification notification = AlarmNotification.getInstance(context);
		notification.showAlarm(item);
	}
}
