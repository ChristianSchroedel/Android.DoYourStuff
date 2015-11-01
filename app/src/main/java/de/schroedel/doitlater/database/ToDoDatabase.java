package de.schroedel.doitlater.database;

import android.content.Context;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * Database managing to do list items.
 */
public class ToDoDatabase
{
	private static ToDoDatabase instance;

	private ToDoEntryTable toDoEntryTable;

	/**
	 * Creates database instance.
	 *
	 * @param context - activity context
	 */
	private ToDoDatabase(Context context)
	{
		this.toDoEntryTable = new ToDoEntryTable(context);
	}

	/**
	 * Gets only valid instance of database.
	 *
	 * @param context - activity context
	 * @return - database instance
	 */
	public static ToDoDatabase getInstance(Context context)
	{
		if (instance == null)
			instance = new ToDoDatabase(context);

		return instance;
	}

	/**
	 * Returns to do entry database table.
	 *
	 * @return - to do entry table
	 */
	public ToDoEntryTable getToDoEntryTable()
	{
		return toDoEntryTable;
	}
}
