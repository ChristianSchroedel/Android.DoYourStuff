package de.schroedel.doyourstuff.network.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.schroedel.doyourstuff.views.notifications.AlarmNotification;

/**
 * Receiver receiving broadcasts when an alarm notification was dismissed.
 */
public class DismissAlarmReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		AlarmNotification notification = AlarmNotification.getInstance(context);
		notification.cancel();
	}
}
