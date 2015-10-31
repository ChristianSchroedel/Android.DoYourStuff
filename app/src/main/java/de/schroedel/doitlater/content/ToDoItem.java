package de.schroedel.doitlater.content;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.StringTokenizer;

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
	public static final String EXTRA_YEAR = "year";
	public static final String EXTRA_MONTH = "month";
	public static final String EXTRA_DAYOFMONTH = "dayOfMonth";
	public static final String EXTRA_HOUROFDAY = "hourOfDay";
	public static final String EXTRA_MINUTE = "minute";
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
	public int year;
	public int month;
	public int dayOfMonth;
	public int hourOfDay;
	public int minute;

	public long timeStamp;

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
		year = -1;
		month = -1;
		dayOfMonth = -1;
		hourOfDay = -1;
		minute = -1;
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
		out.writeInt(year);
		out.writeInt(month);
		out.writeInt(dayOfMonth);
		out.writeInt(hourOfDay);
		out.writeInt(minute);
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
		year = in.readInt();
		month = in.readInt();
		dayOfMonth = in.readInt();
		hourOfDay = in.readInt();
		minute = in.readInt();
		itemDone = in.readInt();
		category = Category.fromValue(in.readInt());
	}

	/**
	 * Returns date time in milliseconds.
	 *
	 * @return - date time
	 */
	public long getDateTime()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month-1, dayOfMonth, hourOfDay, minute);

		return calendar.getTimeInMillis();
	}

	/**
	 * Returns date time of item as ISO8601 string ("YYYY-MM-DD HH:MM:SS.SSS").
	 *
	 * @return - date time as ISO8601 string
	 */
	public String getDateTimeString()
	{
		if (year == -1 ||
			month == -1 ||
			dayOfMonth == -1 ||
			hourOfDay == -1 ||
			minute == -1)
			return null;

		return String.format(
			"%04d-%02d-%02d %02d:%02d:00.000",
			year,
			month,
			dayOfMonth,
			hourOfDay,
			minute);
	}

	/**
	 * Sets date and time to item using ISO8061 string.
	 *
	 * @param dateTime - date time after ISO8601 ("YYYY-MM-DD HH:MM:SS.SSS")
	 */
	public void setDateTime(String dateTime)
	{
		if (dateTime == null)
			return;

		StringTokenizer tokenizer = new StringTokenizer(dateTime);
		String date = tokenizer.nextToken();
		String time = tokenizer.nextToken();

		tokenizer = new StringTokenizer(date);

		year = Integer.valueOf(tokenizer.nextToken("-"));
		month = Integer.valueOf(tokenizer.nextToken("-"));
		dayOfMonth = Integer.valueOf(tokenizer.nextToken());

		tokenizer = new StringTokenizer(time);

		hourOfDay = Integer.valueOf(tokenizer.nextToken(":"));
		minute = Integer.valueOf(tokenizer.nextToken(":"));
	}

	/**
	 * Checks if set date is valid.
	 *
	 * @return - true if valid else false
	 */
	public boolean isDateValid()
	{
		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		if (this.year < year)
			return false;
		else if (this.year == year &&
			this.month < month)
			return false;
		else if (this.year == year &&
			this.month == month &&
			this.dayOfMonth < day)
			return false;

		return true;
	}

	/**
	 * Puts to do list item data as extras into given intent.
	 *
	 * @param item - to do list item
	 * @param data - intent
	 */
	public static void putItemDataAsExtras(ToDoItem item, Intent data)
	{
		data.putExtra(ToDoItem.EXTRA_TITLE, item.title);
		data.putExtra(ToDoItem.EXTRA_DESCRIPTION, item.description);
		data.putExtra(ToDoItem.EXTRA_YEAR, item.year);
		data.putExtra(ToDoItem.EXTRA_MONTH, item.month);
		data.putExtra(ToDoItem.EXTRA_DAYOFMONTH, item.dayOfMonth);
		data.putExtra(ToDoItem.EXTRA_HOUROFDAY, item.hourOfDay);
		data.putExtra(ToDoItem.EXTRA_MINUTE, item.minute);
		data.putExtra(ToDoItem.EXTRA_CATEGORY, item.category.toValue());
		data.putExtra(ToDoItem.EXTRA_DONE, item.itemDone);
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

		String title = data.getStringExtra(ToDoItem.EXTRA_TITLE);

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
		if (data.hasExtra(ToDoItem.EXTRA_TITLE))
			item.title = data.getStringExtra(ToDoItem.EXTRA_TITLE);

		if (data.hasExtra(ToDoItem.EXTRA_DESCRIPTION))
			item.description = data.getStringExtra(ToDoItem.EXTRA_DESCRIPTION);

		if (data.hasExtra(ToDoItem.EXTRA_YEAR))
			item.year = data.getIntExtra(ToDoItem.EXTRA_YEAR, -1);

		if (data.hasExtra(ToDoItem.EXTRA_MONTH))
			item.month = data.getIntExtra(ToDoItem.EXTRA_MONTH, -1);

		if (data.hasExtra(ToDoItem.EXTRA_DAYOFMONTH))
			item.dayOfMonth = data.getIntExtra(ToDoItem.EXTRA_DAYOFMONTH, -1);

		if (data.hasExtra(ToDoItem.EXTRA_HOUROFDAY))
			item.hourOfDay = data.getIntExtra(ToDoItem.EXTRA_HOUROFDAY, -1);

		if (data.hasExtra(ToDoItem.EXTRA_MINUTE))
			item.minute = data.getIntExtra(ToDoItem.EXTRA_MINUTE, -1);

		if (data.hasExtra(ToDoItem.EXTRA_CATEGORY))
		{
			int cat = data.getIntExtra(ToDoItem.EXTRA_CATEGORY, 0);
			item.category = Category.fromValue(cat);
		}

		if (data.hasExtra(ToDoItem.EXTRA_DONE))
			item.itemDone = data.getIntExtra(ToDoItem.EXTRA_DONE, ITEM_PENDING);
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

		if (hourOfDay != -1 &&
			minute != -1)
		{
			String dateTime;

			if (ToDoDatabase.dateIsToday(
				year,
				month,
				dayOfMonth))
				dateTime = String.format("%02d:%02d", hourOfDay, minute);
			else
				dateTime = String.format(
					"%02d.%02d. %02d:%02d",
					month,
					dayOfMonth,
					hourOfDay,
					minute);

			viewHolder.tvTime.setText(dateTime);
		}

		return convertView;
	}
}
