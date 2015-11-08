package de.schroedel.doyourstuff.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.content.Category;
import de.schroedel.doyourstuff.content.Header;
import de.schroedel.doyourstuff.content.ListItem;
import de.schroedel.doyourstuff.content.ToDoItem;
import de.schroedel.doyourstuff.database.ToDoDatabaseHelper.ToDoEntry;
import de.schroedel.doyourstuff.utils.DateFormatter;

/**
 * Created by Christian Schrödel on 01.11.15.
 *
 * To do items database table.
 */
public class ToDoEntryTable implements DatabaseTable<ListItem>
{
	private Context context;
	private ToDoDatabaseHelper dbHelper;

	private Header doneHeader;
	private Header missedHeader;
	private ToDoItem last;
	private boolean lastInPast = false;

	public ToDoEntryTable(Context context)
	{
		this.context = context;
		this.dbHelper = new ToDoDatabaseHelper(context);
	}

	@Override
	public long insert(ListItem listItem)
	{
		if (!(listItem instanceof ToDoItem))
			return -1;

		ToDoItem item = (ToDoItem) listItem;

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

	@Override
	public int remove(long id)
	{
		SQLiteDatabase sql = dbHelper.getWritableDatabase();

		String selection = ToDoEntry._ID + " LIKE ?";
		String[] selectionArgs = {String.valueOf(id)};

		return sql.delete(
			ToDoEntry.TABLE_NAME,
			selection,
			selectionArgs);
	}

	@Override
	public List<ListItem> getAll()
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
			item.category = Category.fromValue(category);
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

	@Override
	public ToDoItem get(long id)
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
		item.category = Category.fromValue(category);
		item.itemDone = done;

		cursor.close();

		return item;
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

			doneHeader = new Header(done, -1);
			return doneHeader;
		}

		if (item.timestamp == 0)
			return null;

		if (DateFormatter.dateIsPast(item.timestamp))
		{
			lastInPast = true;

			if (missedHeader != null)
				return null;

			String missed = context.getResources().getString(R.string.header_missed);

			missedHeader = new Header(missed, -1);
			return missedHeader;
		}
		else
		{
			if (!lastInPast &&
				last != null &&
				DateFormatter.hasSameDay(item.timestamp, last.timestamp))
				return null;

			lastInPast = false;

			String headerTitle;

			if (DateFormatter.dateIsToday(item.timestamp))
				headerTitle = context.getResources().getString(R.string.header_today);
			else
				headerTitle = DateFormatter.getFormattedDate(
					item.timestamp,
					"EEEE - dd.MM.yyyy");

			int dayOfWeek = DateFormatter.getCalendarDayOfWeek(item.timestamp);

			return new Header(headerTitle, dayOfWeek);
		}
	}
	/**
	 * Updates title and description of item in database.
	 *
	 * @param id - id of to do list item
	 * @param title - new item title
	 * @param description - new item description
	 * @param timestamp - timestamp
	 * @param category - category
	 */
	public void updateToDoItem(
		long id,
		String title,
		String description,
		long timestamp,
		Category category)
	{
		SQLiteDatabase sql = dbHelper.getReadableDatabase();

		String selection = ToDoEntry._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };

		ContentValues values = new ContentValues();
		values.put(ToDoEntry.COLUMN_TITLE, title);
		values.put(ToDoEntry.COLUMN_DESCRIPTION, description);
		values.put(ToDoEntry.COLUMN_TIMESTAMP, timestamp);
		values.put(ToDoEntry.COLUMN_CATEGORY, category.toValue());

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
