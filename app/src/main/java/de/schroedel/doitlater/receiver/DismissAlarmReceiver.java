package de.schroedel.doitlater.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.schroedel.doitlater.notification.AlarmNotification;

/**
 * Created by Christian Schrödel on 01.11.15.
 *
 * Receiver receiving callback when alarm notification was dismissed.
 */
public class DismissAlarmReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		AlarmNotification notification = AlarmNotification.getInstance();
		notification.cancel(context);
	}
}
