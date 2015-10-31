package de.schroedel.doitlater.service;

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

import de.schroedel.doitlater.R;
import de.schroedel.doitlater.activity.ItemListActivity;
import de.schroedel.doitlater.content.ToDoDatabase;
import de.schroedel.doitlater.content.ToDoItem;

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

		Intent laterIntent = new Intent(context, UpdateItemReceiver.class);
		laterIntent.putExtra(ToDoItem.EXTRA_ID, id);
		laterIntent.setAction(UpdateItemReceiver.ACTION_LATER);

		Intent doneIntent = new Intent(context, UpdateItemReceiver.class);
		doneIntent.putExtra(ToDoItem.EXTRA_ID, id);
		doneIntent.setAction(UpdateItemReceiver.ACTION_DONE);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(ItemListActivity.class);
		stackBuilder.addNextIntent(detailIntent);

		PendingIntent content =
			stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		PendingIntent later =
			PendingIntent.getBroadcast(context, 0, laterIntent, 0);

		PendingIntent done =
			PendingIntent.getBroadcast(context, 0, doneIntent, 0);

		NotificationCompat.Builder builder =
			new NotificationCompat.Builder(context);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(item.title);
		builder.setContentText(item.description);
		builder.setCategory(Notification.CATEGORY_ALARM);
		builder.setPriority(Notification.PRIORITY_HIGH);
		builder.setContentIntent(content);
		builder.addAction(
			R.drawable.ic_launcher,
			"later",
			later);
		builder.addAction(R.drawable.ic_launcher, "done", done);

		Uri alarmSound = RingtoneManager.getDefaultUri(
			RingtoneManager.TYPE_ALARM);
		builder.setSound(alarmSound);
		builder.setVibrate(new long[] { 1000, 500, 1000, 500 });

		NotificationManager manager = (NotificationManager)
			context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify((int) id, builder.build());
	}
}
