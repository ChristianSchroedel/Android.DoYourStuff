package de.schroedel.doyourstuff.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.schroedel.doyourstuff.content.ToDoItem;
import de.schroedel.doyourstuff.notification.AlarmNotification;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
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

		AlarmNotification notification = AlarmNotification.getInstance();
		notification.showAlarm(context, item);
	}
}
