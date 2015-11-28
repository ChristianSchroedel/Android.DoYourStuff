package de.schroedel.doyourstuff.views.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.activities.ItemListActivity;
import de.schroedel.doyourstuff.models.ToDoItem;
import de.schroedel.doyourstuff.network.receiver.DismissAlarmReceiver;
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

		Intent contentIntent = new Intent(context, ItemListActivity.class);

		// Only trigger showing details if there is only one to do item, else
		// just show the list overview.
		if (toDoItems.size() == 1)
		{
			contentIntent.setAction(ItemListActivity.SHOW_DETAIL);
			contentIntent.putExtra(ToDoItem.EXTRA_ITEM, item);
		}
		else
			contentIntent.setAction(ItemListActivity.SHOW_LIST);

		Intent deleteIntent = new Intent(context, DismissAlarmReceiver.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(ItemListActivity.class);
		stackBuilder.addNextIntent(contentIntent);

		PendingIntent content =
			stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		PendingIntent delete =
			PendingIntent.getBroadcast(
				context,
				0,
				deleteIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		Resources res = context.getResources();

		String time = DateTimeHelper.getFormattedDate(item.timestamp, "HH:mm");

		String title = res.getString(
			R.string.notification_title_alarm,
			time,
			item.title);

		String ticker = res.getString(
			R.string.notification_ticker_alarm,
			item.title,
			time);

		NotificationCompat.Builder builder =
			new NotificationCompat.Builder(context);
		builder.setSmallIcon(R.drawable.ic_item_notification);
		builder.setColor(ContextCompat.getColor(context, R.color.primary));
		builder.setContentTitle(title);
		builder.setContentText(item.description);
		builder.setContentIntent(content);
		builder.setDeleteIntent(delete);
		builder.setAutoCancel(true);
		builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
		builder.setTicker(ticker);

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
		manager.notify(ALARM_NOTIFICATION, builder.build());
	}

	/**
	 * Cancels notification for {@link ToDoItem} objects.
	 */
	public void cancel(Context context)
	{
		clear();

		NotificationManager manager = (NotificationManager)
			context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(ALARM_NOTIFICATION);
	}

	/**
	 * Clears set {@link ToDoItem} objects.
	 */
	public void clear()
	{
		toDoItems.clear();
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
				"%s | %s",
				DateTimeHelper.getFormattedDate(item.timestamp, "HH:mm"),
				item.title);

			inboxStyle.addLine(line);
		}

		return inboxStyle;
	}
}
