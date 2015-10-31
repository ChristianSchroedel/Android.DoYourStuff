package de.schroedel.doitlater.content;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.schroedel.doitlater.R;
import de.schroedel.doitlater.adapter.ToDoListAdapter;
import de.schroedel.doitlater.database.ToDoDatabase;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * To do list item holding information about planned task of user.
 */
public class ToDoItem implements Parcelable, de.schroedel.doitlater.content.ListItem
{
	public static final String EXTRA_ITEM = "todoItem";
	public static final String EXTRA_ID = "id";

	public static final String EXTRA_TITLE = "title";
	public static final String EXTRA_DESCRIPTION = "description";
	public static final String EXTRA_TIMESTAMP = "timestamp";
	public static final String EXTRA_CATEGORY = "category";
	public static final String EXTRA_DONE = "done";

	public static final int ITEM_PENDING = 0;
	public static final int ITEM_DONE = 1;

	public enum Category
	{
		ITEM_DEFAULT(0);

		private int value;

		Category(int value)
		{
			this.value = value;
		}

		public int toValue()
		{
			return value;
		}

		public static Category fromValue(int value)
		{
			for (Category cat : Category.values())
			{
				if (cat.toValue() == value)
					return cat;
			}

			return null;
		}
	}

	public long id;
	public String title;
	public String description;
/*
	public int year;
	public int month;
	public int dayOfMonth;
	public int hourOfDay;
	public int minute;
*/
	public long timestamp;

	public int itemDone;
	public Category category;

	static class ViewHolder
	{
		TextView tvTitle;
		TextView tvDesc;
		TextView tvTime;
	}

	/**
	 * Creates new to do item.
	 */
	public ToDoItem()
	{
		id = -1;

		title = null;
		description = null;

		itemDone = ITEM_PENDING;
		category = Category.ITEM_DEFAULT;
	}

	/**
	 * Creator to implement Parcelable.Creator interface.
	 */
	public static final Creator<ToDoItem> CREATOR = new Creator<ToDoItem>()
	{
		@Override
		public ToDoItem createFromParcel(Parcel in)
		{
			return new ToDoItem(in);
		}

		@Override
		public ToDoItem[] newArray(int size)
		{
			return new ToDoItem[size];
		}
	};

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags)
	{
		out.writeLong(id);
		out.writeString(title);
		out.writeString(description);
		out.writeLong(timestamp);
		out.writeInt(itemDone);
		out.writeInt(category.toValue());
	}

	/**
	 * Creates to do item from parcel.
	 *
	 * @param in - parcel containing data
	 */
	private ToDoItem(Parcel in)
	{
		id = in.readLong();
		title = in.readString();
		description = in.readString();
		timestamp = in.readLong();
		itemDone = in.readInt();
		category = Category.fromValue(in.readInt());
	}

	/**
	 * Returns date of to do item with 'dd.MM.yyyy' pattern.
	 *
	 * @return - date
	 */
	public String getDate()
	{
		return getFormattedDate(timestamp, "dd.MM.yyyy");
	}


	public String getDayOfMonth()
	{
		return getFormattedDate(timestamp, "dd");
	}


	public String getMonth()
	{
		return getFormattedDate(timestamp, "MM");
	}


	public String getMinute()
	{
		return getFormattedDate(timestamp, "m");
	}


	public String getHourOfDay()
	{
		return getFormattedDate(timestamp, "H");
	}

	/**
	 * Returns formatted string using given UTC timestamp + pattern.
	 *
	 * @param timestamp - UTC timestamp
	 * @param pattern - format pattern
	 * @return - formatted string
	 */
	private static String getFormattedDate(long timestamp, String pattern)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);

		SimpleDateFormat dateFormat =
			new SimpleDateFormat(pattern, Locale.getDefault());

		dateFormat.setCalendar(calendar);

		return dateFormat.format(calendar.getTime());
	}

	/**
	 * Puts to do list item data as extras into given intent.
	 *
	 * @param item - to do list item
	 * @param data - intent
	 */
	public static void putItemDataAsExtras(ToDoItem item, Intent data)
	{
		data.putExtra(EXTRA_TITLE, item.title);
		data.putExtra(EXTRA_DESCRIPTION, item.description);
		data.putExtra(EXTRA_TIMESTAMP, item.timestamp);
		data.putExtra(EXTRA_CATEGORY, item.category.toValue());
		data.putExtra(EXTRA_DONE, item.itemDone);
	}

	/**
	 * Checks if extras are valid. To be valid the data has to contain at least
	 * a non-empty title.
	 *
	 * @param data - extras
	 * @return - true if valid else false
	 */
	public static boolean itemExtrasAreValid(Intent data)
	{
		if (!data.hasExtra(ToDoItem.EXTRA_TITLE))
			return false;

		String title = data.getStringExtra(EXTRA_TITLE);

		return (title != null && !title.isEmpty());
	}

	/**
	 * Initializes to do list item with given extras.
	 *
	 * @param item - to do list item
	 * @param data - extras
	 */
	public static void initItemFromExtras(ToDoItem item, Intent data)
	{
		item.title = data.getStringExtra(EXTRA_TITLE);
		item.description = data.getStringExtra(EXTRA_DESCRIPTION);
		item.timestamp = data.getLongExtra(EXTRA_TIMESTAMP, 0);

		int cat = data.getIntExtra(ToDoItem.EXTRA_CATEGORY, 0);
		item.category = Category.fromValue(cat);

		item.itemDone = data.getIntExtra(
			ToDoItem.EXTRA_DONE,
			ITEM_PENDING);
	}

	@Override
	public int getItemType()
	{
		return ToDoListAdapter.ItemType.LIST_ITEM.value;
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView)
	{
		ViewHolder viewHolder;

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.todo_list_item, null);

			viewHolder = new ViewHolder();
			viewHolder.tvTitle =
				(TextView) convertView.findViewById(R.id.todo_list_title);
			viewHolder.tvDesc =
				(TextView) convertView.findViewById(R.id.todo_list_desc);
			viewHolder.tvTime =
				(TextView) convertView.findViewById(R.id.todo_list_time);

			convertView.setTag(viewHolder);
		}
		else
			viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.tvTitle.setText(title);
		viewHolder.tvDesc.setText(description);

		if (timestamp > 0)
		{
			String dateTime;

			if (ToDoDatabase.dateIsToday(timestamp))
				dateTime = String.format("%s:%s", getHourOfDay(), getMinute());
			else
				dateTime = String.format(
					"%s.%s. %s:%s",
					getMonth(),
					getDayOfMonth(),
					getHourOfDay(),
					getMinute());

			viewHolder.tvTime.setText(dateTime);
		}

		return convertView;
	}
}
