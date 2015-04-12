package schroedel.de.doitlater.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import schroedel.de.doitlater.R;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * Database managing to do list items.
 */
public class ToDoDatabase
{
	private final static String INTEGER = " integer";
	private final static String TEXT = " text";
	private final static String COMMA = ", ";

	private static ToDoDatabase instance;
	private ToDoDatabaseHelper dbHelper;
	private Context context;

	private Header doneHeader;
	private Header missedHeader;
	private ToDoItem last;
	private boolean lastInPast = false;

	private final static String SQL_CREATE_ENTRIES =
		"CREATE TABLE " + ToDoEntry.TABLE_NAME +
		"(" +
		ToDoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA +
		ToDoEntry.COLUMN_TITLE + TEXT + COMMA +
		ToDoEntry.COLUMN_DESCRIPTION + TEXT + COMMA +
		ToDoEntry.COLUMN_DATETIME + TEXT + COMMA +
		ToDoEntry.COLUMN_CATEGORY + INTEGER + COMMA +
		ToDoEntry.COLUMN_DONE + INTEGER +
		")";

	private final static String SQL_ADD_DATETIME =
		"ALTER TABLE " + ToDoEntry.TABLE_NAME + " ADD COLUMN " +
			ToDoEntry.COLUMN_DATETIME + TEXT;

	private final static String SQL_ADD_CATEGORY =
		"ALTER TABLE " + ToDoEntry.TABLE_NAME + " ADD COLUMN " +
			ToDoEntry.COLUMN_CATEGORY + INTEGER;

	private final static String SQL_ADD_DONE =
		"ALTER TABLE " + ToDoEntry.TABLE_NAME + " ADD COLUMN " +
			ToDoEntry.COLUMN_DONE + INTEGER;

	/**
	 * To do list item columns.
	 */
	class ToDoEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "items";
		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_DESCRIPTION = "description";
		public static final String COLUMN_DATETIME = "datetime";
		public static final String COLUMN_CATEGORY = "category";
		public static final String COLUMN_DONE = "done";
	}

	/**
	 * Helper to access/create/update database content.
	 */
	class ToDoDatabaseHelper extends SQLiteOpenHelper
	{
		private static final String DB_NAME = "ToDoList.db";
		private static final int VERSION = 3;

		/**
		 * Creates database helper.
		 *
		 * @param context - context
		 */
		public ToDoDatabaseHelper(Context context)
		{
			super(context, DB_NAME, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(SQL_CREATE_ENTRIES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			if (oldVersion == 1 &&
				newVersion == 2)
				db.execSQL(SQL_ADD_DATETIME);
			else if (oldVersion == 2 &&
				newVersion == 3)
			{
				db.execSQL(SQL_ADD_CATEGORY);
				db.execSQL(SQL_ADD_DONE);
			}
		}
	}

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
			ToDoEntry.COLUMN_DATETIME,
			ToDoEntry.COLUMN_CATEGORY,
			ToDoEntry.COLUMN_DONE };

		String sortOrder = ToDoEntry.COLUMN_DATETIME + " ASC";

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
			String dateTime = cursor.getString(
				cursor.getColumnIndex(ToDoEntry.COLUMN_DATETIME));
			int category = cursor.getInt(
				cursor.getColumnIndex(ToDoEntry.COLUMN_CATEGORY));
			int done = cursor.getInt(
				cursor.getColumnIndex(ToDoEntry.COLUMN_DONE));

			ToDoItem item = new ToDoItem();
			item.id = id;
			item.title = title;
			item.description = desc;
			item.setDateTime(dateTime);
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

		if (!item.isDateValid())
			return null;

		if (dateIsPast(item))
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
				(item.year == last.year &&
				item.month == last.month &&
				item.dayOfMonth == last.dayOfMonth))
				return null;

			lastInPast = false;

			String header;

			if (dateIsToday(item.year, item.month, item.dayOfMonth))
				header = context.getResources().getString(R.string.today);
			else
				header = String.format(
					"%02d.%02d.%04d",
					item.dayOfMonth,
					item.month,
					item.year);

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
			ToDoEntry.COLUMN_DATETIME,
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
		String dateTime = cursor.getString(
			cursor.getColumnIndex(ToDoEntry.COLUMN_DATETIME));
		int category = cursor.getInt(
			cursor.getColumnIndex(ToDoEntry.COLUMN_CATEGORY));
		int done = cursor.getInt(
			cursor.getColumnIndex(ToDoEntry.COLUMN_DONE));

		ToDoItem item = new ToDoItem();
		item.id = id;
		item.title = title;
		item.description = desc;
		item.setDateTime(dateTime);
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
		values.put(ToDoEntry.COLUMN_DATETIME, item.getDateTimeString());
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
	 */
	public void updateItemText(long id, String title, String description)
	{
		SQLiteDatabase sql = dbHelper.getReadableDatabase();

		String selection = ToDoEntry._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };

		ContentValues values = new ContentValues();
		values.put(ToDoEntry.COLUMN_TITLE, title);
		values.put(ToDoEntry.COLUMN_DESCRIPTION, description);

		sql.update(
			ToDoEntry.TABLE_NAME,
			values,
			selection,
			selectionArgs);
	}

	/**
	 * Updates notification date and time of item in database.
	 *
	 * @param id - id of to do list item
	 * @param dateTime - new notification date time
	 */
	public void updateItemDateTime(long id, String dateTime)
	{
		SQLiteDatabase sql = dbHelper.getReadableDatabase();

		String selection = ToDoEntry._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };

		ContentValues values = new ContentValues();
		values.put(ToDoEntry.COLUMN_DATETIME, dateTime);

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

	/**
	 * Checks if given date is today.
	 *
	 * @param year - year of date
	 * @param month - month of date
	 * @param day - day of date
	 * @return - true if today else false
	 */
	public static boolean dateIsToday(int year, int month, int day)
	{
		Calendar calendar = Calendar.getInstance();

		return (calendar.get(Calendar.YEAR) == year &&
			calendar.get(Calendar.MONTH)+1 == month &&
			calendar.get(Calendar.DAY_OF_MONTH) == day);
	}

	/**
	 * Checks if given date is in the past.
	 *
	 * @param item - to do list item
	 * @return - true if past else false
	 */
	private boolean dateIsPast(ToDoItem item)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(
			item.year,
			item.month-1,
			item.dayOfMonth,
			item.hourOfDay,
			item.minute);

		long itemTime = calendar.getTimeInMillis();

		Calendar calNow = Calendar.getInstance();

		long now = calNow.getTimeInMillis();

		return (itemTime < now);
	}
}
