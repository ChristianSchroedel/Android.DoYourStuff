package schroedel.de.doitlater.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import schroedel.de.doitlater.R;
import schroedel.de.doitlater.activity.ItemListActivity;
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

		Intent detailIntent = new Intent(context, ItemListActivity.class);
		detailIntent.putExtra(ToDoItem.EXTRA_ID, id);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(ItemListActivity.class);
		stackBuilder.addNextIntent(detailIntent);

		PendingIntent pendingIntent =
			stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder =
			new NotificationCompat.Builder(context);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(item.title);
		builder.setContentText(item.description);
		builder.setCategory(Notification.CATEGORY_ALARM);
		builder.setPriority(Notification.PRIORITY_HIGH);
		builder.setContentIntent(pendingIntent);

		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		builder.setSound(alarmSound);
		builder.setVibrate(new long[] { 1000, 500, 1000, 500 });

		NotificationManager manager = (NotificationManager)
			context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(1, builder.build());
	}
}
