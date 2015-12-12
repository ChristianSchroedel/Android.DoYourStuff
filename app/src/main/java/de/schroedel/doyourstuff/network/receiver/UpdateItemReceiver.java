package de.schroedel.doyourstuff.network.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.schroedel.doyourstuff.models.ToDoItem;
import de.schroedel.doyourstuff.models.database.ToDoDatabase;
import de.schroedel.doyourstuff.models.database.ToDoEntryTable;
import de.schroedel.doyourstuff.views.notifications.AlarmNotification;

/**
 * Receiver receiving broadcasts to update to do list items.
 */
public class UpdateItemReceiver extends BroadcastReceiver
{
	public static final String ACTION_LATER = "later";
	public static final String ACTION_DONE = "done";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();

		if (action.equals(ACTION_DONE))
			itemIsDone(context, intent);
		else if (action.equals(ACTION_LATER))
			doItemLater(context, intent);
	}

	/**
	 * Marks to {@link ToDoItem} as done.
	 *
	 * @param context context
	 * @param doneIntent intent containing data of to do list item
	 */
	private void itemIsDone(Context context, Intent doneIntent)
	{
		long itemId = doneIntent.getLongExtra(ToDoItem.EXTRA_ITEM_ID, 0);

		ToDoDatabase database = ToDoDatabase.getInstance(context);
		ToDoEntryTable entryTable = database.getToDoEntryTable();

		ToDoItem item = entryTable.get(itemId);

		if (item == null)
			return;

		entryTable.updateItemIsDone(item.id, ToDoItem.ITEM_DONE);

		cancelNotification(context);
	}

	/**
	 * Sets alarm of {@link ToDoItem} to a later date.
	 *
	 * @param context context
	 * @param laterIntent intent containing data of to do list item
	 */
	private void doItemLater(Context context, Intent laterIntent)
	{
		long itemId = laterIntent.getLongExtra(ToDoItem.EXTRA_ITEM_ID, 0);

		ToDoDatabase database = ToDoDatabase.getInstance(context);
		ToDoEntryTable entryTable = database.getToDoEntryTable();

		ToDoItem item = entryTable.get(itemId);

		if (item == null)
			return;

		cancelNotification(context);
	}

	/**
	 * Cancels existing alarm notification.
	 *
	 * @param context context
	 */
	private void cancelNotification(Context context)
	{
		AlarmNotification notification = AlarmNotification.getInstance(context);
		notification.cancel();
	}
}
