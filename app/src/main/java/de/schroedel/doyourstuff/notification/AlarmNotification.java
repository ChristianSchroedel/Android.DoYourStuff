package de.schroedel.doyourstuff.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.ArrayList;
import java.util.List;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.activity.ItemListActivity;
import de.schroedel.doyourstuff.content.ToDoItem;
import de.schroedel.doyourstuff.receiver.DismissAlarmReceiver;
import de.schroedel.doyourstuff.receiver.UpdateItemReceiver;
import de.schroedel.doyourstuff.utils.DateFormatter;

/**
 * Created by Christian Schrödel on 01.11.15.
 *
 * To do item alarm notification.
 */
public class AlarmNotification
{
	private static final int ALARM_NOTIFICATION = 1;

	private static AlarmNotification instance;

	private List<ToDoItem> toDoItems;

	/**
	 * Creates new alarm notification.
	 */
	private AlarmNotification()
	{
		this.toDoItems = new ArrayList<>();
	}

	/**
	 * Returns instance of alarm notification.
	 *
	 * @return - instance
	 */
	public static AlarmNotification getInstance()
	{
		if (instance == null)
			instance = new AlarmNotification();

		return instance;
	}

	/**
	 * Shows alarm notification for to do items.
	 *
	 * @param context - context
	 * @param item - last to do item to show
	 */
	public void showAlarm(Context context, ToDoItem item)
	{
		toDoItems.add(item);

		Intent detailIntent = new Intent(context, ItemListActivity.class);
		detailIntent.putExtra(
			ToDoItem.EXTRA_ITEM,
			item);

		Intent laterIntent = new Intent(context, UpdateItemReceiver.class);
		laterIntent.putExtra(ToDoItem.EXTRA_ITEM, item);
		laterIntent.setAction(UpdateItemReceiver.ACTION_LATER);

		Intent doneIntent = new Intent(context, UpdateItemReceiver.class);
		doneIntent.putExtra(
			ToDoItem.EXTRA_ITEM,
			item);
		doneIntent.setAction(UpdateItemReceiver.ACTION_DONE);

		Intent deleteIntent = new Intent(context, DismissAlarmReceiver.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(ItemListActivity.class);
		stackBuilder.addNextIntent(detailIntent);

		PendingIntent content =
			stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		PendingIntent later =
			PendingIntent.getBroadcast(
				context,
				0,
				laterIntent,
				0);

		PendingIntent done =
			PendingIntent.getBroadcast(context, 0, doneIntent, 0);

		PendingIntent delete =
			PendingIntent.getBroadcast(context, 0, deleteIntent, 0);

		NotificationCompat.Builder builder =
			new NotificationCompat.Builder(context);
		builder.setSmallIcon(R.mipmap.ic_launcher);
		builder.setContentTitle(item.title);
		builder.setContentText(item.description);
		builder.setContentIntent(content);
		builder.setDeleteIntent(delete);

		if (toDoItems.size() == 1)
		{
			builder.addAction(R.mipmap.ic_launcher, "later", later);
			builder.addAction(R.mipmap.ic_launcher, "done", done);
		}
		else
			builder.setStyle(createInbox());

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			builder.setCategory(Notification.CATEGORY_ALARM);
			builder.setPriority(Notification.PRIORITY_HIGH);
		}

		builder.setVibrate(new long[]{1000, 500, 1000, 500});

		NotificationManager manager = (NotificationManager)
			context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(ALARM_NOTIFICATION,builder.build());
	}

	/**
	 * Cancels notification for to do list items.
	 */
	public void cancel(Context context)
	{
		toDoItems.clear();

		NotificationManager manager = (NotificationManager)
			context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(ALARM_NOTIFICATION);
	}

	/**
	 * Creates inbox style for notification.
	 *
	 * @return - inbox style
	 */
	private NotificationCompat.InboxStyle createInbox()
	{
		NotificationCompat.InboxStyle inboxStyle =
			new NotificationCompat.InboxStyle();
		inboxStyle.setBigContentTitle(toDoItems.size() + " items");

		for (ToDoItem item : toDoItems)
		{
			String line = String.format(
				"%s | %s - %s",
				DateFormatter.getFormattedDate(item.timestamp, "HH:mm"),
				item.title,
				item.description);

			inboxStyle.addLine(line);
		}

		return inboxStyle;
	}
}
