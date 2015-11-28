package de.schroedel.doyourstuff.models.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * To do list database helper.
 */
public class ToDoDatabaseHelper extends SQLiteOpenHelper
{
	protected static final String DB_NAME = "ToDoList.db";
	protected static final int VERSION = 3;

	private final static String INTEGER = " integer";
	private final static String TEXT = " text";
	private final static String COMMA = ", ";

	private final static String SQL_CREATE_ENTRIES =
		"CREATE TABLE " + ToDoEntry.TABLE_NAME +
			"(" +
			ToDoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA +
			ToDoEntry.COLUMN_TITLE + TEXT + COMMA +
			ToDoEntry.COLUMN_DESCRIPTION + TEXT + COMMA +
			ToDoEntry.COLUMN_TIMESTAMP + INTEGER + COMMA +
			ToDoEntry.COLUMN_CATEGORY + INTEGER + COMMA +
			ToDoEntry.COLUMN_DONE + INTEGER +
			")";

	/**
	 * To do list item columns.
	 */
	class ToDoEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "items";
		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_DESCRIPTION = "description";
		public static final String COLUMN_TIMESTAMP = "datetime";
		public static final String COLUMN_CATEGORY = "category";
		public static final String COLUMN_DONE = "done";
	}

	/**
	 * Creates {@link ToDoDatabaseHelper}.
	 *
	 * @param context context
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

	}
}
