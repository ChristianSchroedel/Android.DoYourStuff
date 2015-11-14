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
import de.schroedel.doyourstuff.utils.DateTimeHelper;

/**
 * Notification manager for {@link ToDoItem} alarms.
 */
public class AlarmNotification
{
	private static final int ALARM_NOTIFICATION = 1;

	private static AlarmNotification instance;

	private List<ToDoItem> toDoItems;

	/**
	 * Creates new {@link AlarmNotification} manager.
	 */
	private AlarmNotification()
	{
		this.toDoItems = new ArrayList<>();
	}

	/**
	 * Returns instance of alarm notification.
	 *
	 * @return instance
	 */
	public static AlarmNotification getInstance()
	{
		if (instance == null)
			instance = new AlarmNotification();

		return instance;
	}

	/**
	 * Shows notification containing {@link ToDoItem} information.
	 *
	 * @param context context
	 * @param item last to do item to show
	 */
	public void showAlarm(Context context, ToDoItem item)
	{
		toDoItems.add(item);

		Intent detailIntent = new Intent(context, ItemListActivity.class);
		detailIntent.putExtra(ToDoItem.EXTRA_ITEM, item);

		Intent deleteIntent = new Intent(context, DismissAlarmReceiver.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(ItemListActivity.class);
		stackBuilder.addNextIntent(detailIntent);

		PendingIntent content =
			stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		PendingIntent delete =
			PendingIntent.getBroadcast(context, 0, deleteIntent, 0);

		NotificationCompat.Builder builder =
			new NotificationCompat.Builder(context);
		builder.setSmallIcon(R.mipmap.ic_launcher);
		builder.setContentTitle(item.title);
		builder.setContentText(item.description);
		builder.setContentIntent(content);
		builder.setDeleteIntent(delete);

		if (toDoItems.size() > 1)
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
	 * Cancels notification for {@link ToDoItem} objects.
	 */
	public void cancel(Context context)
	{
		toDoItems.clear();

		NotificationManager manager = (NotificationManager)
			context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(ALARM_NOTIFICATION);
	}

	/**
	 * Creates {@link NotificationCompat.InboxStyle} for notification.
	 *
	 * @return inbox style
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
				DateTimeHelper.getFormattedDate(item.timestamp, "HH:mm"),
				item.title,
				item.description);

			inboxStyle.addLine(line);
		}

		return inboxStyle;
	}
}
