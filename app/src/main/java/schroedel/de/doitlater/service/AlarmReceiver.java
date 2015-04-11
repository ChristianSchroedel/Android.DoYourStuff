package schroedel.de.doitlater.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import schroedel.de.doitlater.R;
import schroedel.de.doitlater.content.ToDoDatabase;
import schroedel.de.doitlater.content.ToDoItem;

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
		ToDoItem item;
		long id = -1;

		if (data.hasExtra(ToDoItem.EXTRA_ID))
			id = data.getLongExtra(ToDoItem.EXTRA_ID, -1);

		item = ToDoDatabase.getInstance(context).getItem(id);

		if (item == null)
			return;

		NotificationCompat.Builder builder =
			new NotificationCompat.Builder(context);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(item.title);
		builder.setContentText(item.description);
		builder.setCategory(Notification.CATEGORY_ALARM);
		builder.setPriority(Notification.PRIORITY_HIGH);

		NotificationManager manager = (NotificationManager)
			context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(1, builder.build());
	}
}
