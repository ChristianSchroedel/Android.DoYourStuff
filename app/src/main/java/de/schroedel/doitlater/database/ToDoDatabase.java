package de.schroedel.doitlater.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import de.schroedel.doitlater.R;
import de.schroedel.doitlater.content.Header;
import de.schroedel.doitlater.content.ListItem;
import de.schroedel.doitlater.content.ToDoItem;
import de.schroedel.doitlater.database.ToDoDatabaseHelper.ToDoEntry;
import de.schroedel.doitlater.utils.DateFormatter;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * Database managing to do list items.
 */
public class ToDoDatabase
{
	private static ToDoDatabase instance;
	private ToDoDatabaseHelper dbHelper;
	private Context context;

	private Header doneHeader;
	private Header missedHeader;
	private ToDoItem last;
	private boolean lastInPast = false;

	/**
	 * Creates database instance.
	 *
	 * @param context - activity context
	 */
	private ToDoDatabase(Context context)
	{
		this.dbHelper = new ToDoDatabaseHelper(context);
		this.context = context;
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
	 * Returns to do items from database.
	 *
	 * @return - items from database
	 */
	public List<ListItem> getItems()
	{
		List<ListItem> items = new ArrayList<>();

		SQLiteDatabase sql = dbHelper.getReadableDatabase();

		String[] projection = {
			ToDoEntry._ID,
			ToDoEntry.COLUMN_TITLE,
			ToDoEntry.COLUMN_DESCRIPTION,
			ToDoEntry.COLUMN_TIMESTAMP,
			ToDoEntry.COLUMN_CATEGORY,
			ToDoEntry.COLUMN_DONE };

		String sortOrder = ToDoEntry.COLUMN_TIMESTAMP + " ASC";

		Cursor cursor = sql.query(
			ToDoEntry.TABLE_NAME,
			projection,
			null,
			null,
			null,
			null,
			sortOrder);
		cursor.moveToFirst();

		// No entries -> return empty but initialized item list
		if (cursor.getCount() == 0)
        {
            cursor.close();
            return items;
        }

		doneHeader = null;
		missedHeader = null;
		last = null;

		do
		{
			long id = cursor.getLong(cursor.getColumnIndex(ToDoEntry._ID));
			String title = cursor.getString(
				cursor.getColumnIndex(ToDoEntry.COLUMN_TITLE));
			String desc = cursor.getString(
				cursor.getColumnIndex(ToDoEntry.COLUMN_DESCRIPTION));
			Long timeStamp = cursor.getLong(
				cursor.getColumnIndex(ToDoEntry.COLUMN_TIMESTAMP));
			int category = cursor.getInt(
				cursor.getColumnIndex(ToDoEntry.COLUMN_CATEGORY));
			int done = cursor.getInt(
				cursor.getColumnIndex(ToDoEntry.COLUMN_DONE));

			ToDoItem item = new ToDoItem();
			item.id = id;
			item.title = title;
			item.description = desc;
			item.timestamp = timeStamp;
			item.category = ToDoItem.Category.fromValue(category);
			item.itemDone = done;

			Header header = getHeader(item);

			if (header != null)
				items.add(header);

			items.add(item);

			last = item;
		}
		while (cursor.moveToNext());

        cursor.close();

		return items;
	}

	/**
	 * Creates header to put in front of to do item in item list.
	 *
	 * @param item - to do list item
	 * @return - created header or null if no header is necessary
	 */
	private Header getHeader(ToDoItem item)
	{
		if (item.itemDone == ToDoItem.ITEM_DONE)
		{
			if (doneHeader != null)
				return null;

			String done = "Done";

			doneHeader = new Header(done);
			return doneHeader;
		}

		if (item.timestamp == 0)
			return null;

		if (DateFormatter.dateIsPast(item.timestamp))
		{
			lastInPast = true;

			if (missedHeader != null)
				return null;

			String missed = context.getResources().getString(R.string.missed);

			missedHeader = new Header(missed);
			return missedHeader;
		}
		else
		{
			if (!lastInPast &&
				last != null &&
				DateFormatter.hasSameDay(item.timestamp, last.timestamp))
				return null;

			lastInPast = false;

			String header;

			if (DateFormatter.dateIsToday(item.timestamp))
				header = context.getResources().getString(R.string.today);
			else
				header = item.getDate();

			return new Header(header);
		}
	}

	/**
	 * Returns item with matching id from database.
	 *
	 * @param id - id of to do item
	 * @return - to do item
	 */
	public ToDoItem getItem(long id)
	{
		if (id < 0)
			return null;

		SQLiteDatabase sql = dbHelper.getReadableDatabase();

		String[] projection = {
			ToDoEntry._ID,
			ToDoEntry.COLUMN_TITLE,
			ToDoEntry.COLUMN_DESCRIPTION,
			ToDoEntry.COLUMN_TIMESTAMP,
			ToDoEntry.COLUMN_CATEGORY,
			ToDoEntry.COLUMN_DONE };

		String selection = ToDoEntry._ID + " LIKE ?";
		String[] selectionArgs = {String.valueOf(id)};

		Cursor cursor = sql.query(
			ToDoEntry.TABLE_NAME,
			projection,
			selection,
			selectionArgs,
			null,
			null,
			null);
		cursor.moveToFirst();

		if (cursor.getCount() == 0)
        {
            cursor.close();
            return null;
        }

		String title = cursor.getString(
			cursor.getColumnIndex(ToDoEntry.COLUMN_TITLE));
		String desc = cursor.getString(
			cursor.getColumnIndex(ToDoEntry.COLUMN_DESCRIPTION));
		long timeStamp = cursor.getLong(
			cursor.getColumnIndex(ToDoEntry.COLUMN_TIMESTAMP));
		int category = cursor.getInt(
			cursor.getColumnIndex(ToDoEntry.COLUMN_CATEGORY));
		int done = cursor.getInt(
			cursor.getColumnIndex(ToDoEntry.COLUMN_DONE));

		ToDoItem item = new ToDoItem();
		item.id = id;
		item.title = title;
		item.description = desc;
		item.timestamp = timeStamp;
		item.category = ToDoItem.Category.fromValue(category);
		item.itemDone = done;

        cursor.close();

		return item;
	}

	/**
	 * Inserts a new item into database.
	 *
	 * @param item - new item
	 * @return - new row number or -1 if error occurred
	 */
	public long insertItem(ToDoItem item)
	{
		SQLiteDatabase sql = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ToDoEntry.COLUMN_TITLE, item.title);
		values.put(ToDoEntry.COLUMN_DESCRIPTION, item.description);
		values.put(ToDoEntry.COLUMN_TIMESTAMP, item.timestamp);
		values.put(ToDoEntry.COLUMN_CATEGORY, item.category.toValue());
		values.put(ToDoEntry.COLUMN_DONE, item.itemDone);

		long newRowId = sql.insert(ToDoEntry.TABLE_NAME, "null", values);

		item.id = newRowId;

		return newRowId;
	}

	/**
	 * Removes an item from database.
	 *
	 * @param id - id of item to remove
	 */
	public void removeItem(long id)
	{
		SQLiteDatabase sql = dbHelper.getWritableDatabase();

		String selection = ToDoEntry._ID + " LIKE ?";
		String[] selectionArgs = {String.valueOf(id)};

		sql.delete(ToDoEntry.TABLE_NAME, selection, selectionArgs);
	}

	/**
	 * Updates title and description of item in database.
	 *
	 * @param id - id of to do list item
	 * @param title - new item title
	 * @param description - new item description
	 * @param timestamp - timestamp
	 */
	public void updateToDoItem(
		long id,
		String title,
		String description,
		long timestamp)
	{
		SQLiteDatabase sql = dbHelper.getReadableDatabase();

		String selection = ToDoEntry._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };

		ContentValues values = new ContentValues();
		values.put(ToDoEntry.COLUMN_TITLE, title);
		values.put(ToDoEntry.COLUMN_DESCRIPTION, description);
		values.put(ToDoEntry.COLUMN_TIMESTAMP, timestamp);

		sql.update(
			ToDoEntry.TABLE_NAME,
			values,
			selection,
			selectionArgs);
	}

	/**
	 * Updates state of item in database.
	 *
	 * @param id - id of to do item
	 * @param done - new state of item
	 */
	public void updateItemIsDone(long id, int done)
	{
		SQLiteDatabase sql = dbHelper.getReadableDatabase();

		String selection = ToDoEntry._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };

		ContentValues values = new ContentValues();
		values.put(ToDoEntry.COLUMN_DONE, done);

		sql.update(
			ToDoEntry.TABLE_NAME,
			values,
			selection,
			selectionArgs);
	}

	/**
	 * Updates category of item in database.
	 *
	 * @param id - id of to do item
	 * @param category - new category of item
	 */
	public void updateItemCategory(long id, int category)
	{
		SQLiteDatabase sql = dbHelper.getReadableDatabase();

		String selection = ToDoEntry._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };

		ContentValues values = new ContentValues();
		values.put(ToDoEntry.COLUMN_CATEGORY, category);

		sql.update(
			ToDoEntry.TABLE_NAME,
			values,
			selection,
			selectionArgs);
	}
}
