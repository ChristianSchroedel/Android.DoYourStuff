package de.schroedel.doitlater.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.schroedel.doitlater.database.ToDoDatabase;
import de.schroedel.doitlater.content.ToDoItem;

/**
 * Created by Christian Schrödel on 11.04.15.
 *
 * Receiver receiving intents to update to do list items.
 */
public class UpdateItemReceiver extends BroadcastReceiver
{
	public static final String ACTION_LATER = "later";
	public static final String ACTION_DONE = "done";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();

		if (action.equalsIgnoreCase(ACTION_DONE))
			itemIsDone(context, intent);
		else if (action.equalsIgnoreCase(ACTION_LATER))
			doItemLater(context, intent);
	}

	/**
	 * Marks to do list item as done.
	 *
	 * @param context - context
	 * @param doneIntent - intent containing data of to do list item
	 */
	private void itemIsDone(Context context, Intent doneIntent)
	{
		if (doneIntent == null)
			return;

		ToDoItem item = doneIntent.getParcelableExtra(ToDoItem.EXTRA_ITEM);

		if (item == null)
			return;

		ToDoDatabase.getInstance(context).updateItemIsDone(
			item.id,
			ToDoItem.ITEM_DONE);

		cancelNotification(context, item.id);
	}

	/**
	 * Sets alarm of to do list items to a later date.
	 *
	 * @param context - context
	 * @param laterIntent - intent containing data of to do list item
	 */
	private void doItemLater(Context context, Intent laterIntent)
	{
		if (laterIntent == null)
			return;

		ToDoItem item = laterIntent.getParcelableExtra(ToDoItem.EXTRA_ITEM);

		if (item == null)
			return;

		cancelNotification(context, item.id);
	}

	/**
	 * Cancels existing notification matching giving id.
	 *
	 * @param context - context
	 * @param id - id of notification
	 */
	private void cancelNotification(Context context, long id)
	{
		NotificationManager manager = (NotificationManager)
			context.getSystemService(Context.NOTIFICATION_SERVICE);

		manager.cancel((int) id);
	}
}
