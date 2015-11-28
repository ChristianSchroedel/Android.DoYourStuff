package de.schroedel.doyourstuff.models.database;

import android.content.Context;

/**
 * Database managing to do list items.
 */
public class ToDoDatabase
{
	private static ToDoDatabase instance;

	private ToDoEntryTable toDoEntryTable;

	/**
	 * Creates {@link ToDoDatabase} instance.
	 *
	 * @param context activity context
	 */
	private ToDoDatabase(Context context)
	{
		this.toDoEntryTable = new ToDoEntryTable(context);
	}

	/**
	 * Gets only valid instance of {@link ToDoDatabase}.
	 *
	 * @param context activity context
	 * @return database instance
	 */
	public static ToDoDatabase getInstance(Context context)
	{
		if (instance == null)
			instance = new ToDoDatabase(context);

		return instance;
	}

	/**
	 * Returns {@link ToDoEntryTable} from {@link ToDoDatabase}.
	 *
	 * @return to do entry table
	 */
	public ToDoEntryTable getToDoEntryTable()
	{
		return toDoEntryTable;
	}
}
