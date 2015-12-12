package de.schroedel.doyourstuff.models;

import android.content.Context;

import de.schroedel.doyourstuff.models.database.ToDoDatabase;
import de.schroedel.doyourstuff.models.database.ToDoEntryTable;

/**
 * To do list item holding information about planned task of user.
 */
public class ToDoItem extends ListItem
{
	public static final String EXTRA_ITEM_ID = "extra_item_id";

	public static final int ITEM_PENDING = 0;
	public static final int ITEM_DONE = 1;

	public long id;
	public String title;
	public String description;
	public long timestamp;

	public int itemDone;
	public int category;

	/**
	 * Creates new empty {@link ToDoItem}.
	 */
	public ToDoItem()
	{
		this.id = -1;

		this.title = null;
		this.description = null;

		this.itemDone = ITEM_PENDING;
		this.category = Category.CATEGORY_DEFAULT;
	}

	@Override
	public int getItemType()
	{
		return TYPE_TODO_ENTRY;
	}

	/**
	 * Returns {@link ToDoItem} from {@link ToDoEntryTable} for given item ID.
	 *
	 * @param context context
	 * @param itemId ID of item
	 * @return item matching ID
	 */
	public static ToDoItem fromDatabase(Context context, long itemId)
	{
		ToDoDatabase database = ToDoDatabase.getInstance(context);
		ToDoEntryTable entryTable = database.getToDoEntryTable();

		return entryTable.get(itemId);
	}
}
