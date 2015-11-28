package de.schroedel.doyourstuff.network.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import de.schroedel.doyourstuff.managers.ToDoAlarmManager;
import de.schroedel.doyourstuff.models.ListItem;
import de.schroedel.doyourstuff.models.ToDoItem;
import de.schroedel.doyourstuff.models.database.ToDoDatabase;
import de.schroedel.doyourstuff.models.database.ToDoEntryTable;

/**
 * Receiver receiving broadcasts when device has finished booting.
 */
public class BootReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
			setAlarms(context);
	}

	/**
	 * Sets alarms for available {@link ToDoItem} objects.
	 *
	 * @param context context
	 */
	private void setAlarms(Context context)
	{
		ToDoDatabase database = ToDoDatabase.getInstance(context);

		ToDoEntryTable table = database.getToDoEntryTable();

		for (ListItem item : table.getAllListItems())
		{
			if (item.getItemType() == ListItem.TYPE_TODO_ENTRY)
				ToDoAlarmManager.setReminderAlarm(context, (ToDoItem) item);
		}
	}

	/**
	 * Enables {@link BootReceiver} component.
	 *
	 * @param context context
	 * @param enabled new state of component
	 */
	public static void setComponentEnabled(Context context, boolean enabled)
	{
		ComponentName receiver = new ComponentName(context, BootReceiver.class);
		PackageManager packageManager = context.getPackageManager();

		int enabledFlag =
			enabled ?
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED;

		packageManager.setComponentEnabledSetting(
			receiver,
			enabledFlag,
			PackageManager.DONT_KILL_APP);
	}

	/**
	 * Checks if {@link BootReceiver} component is enabled.
	 *
	 * @param context context
	 */
	public static boolean isComponentEnabled(Context context)
	{
		ComponentName receiver = new ComponentName(context, BootReceiver.class);
		PackageManager packageManager = context.getPackageManager();

		int enabledFlag = packageManager.getComponentEnabledSetting(receiver);

		return (enabledFlag == PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
	}
}
