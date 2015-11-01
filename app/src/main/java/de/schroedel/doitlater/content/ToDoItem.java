package de.schroedel.doitlater.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.schroedel.doitlater.R;
import de.schroedel.doitlater.database.ToDoDatabase;
import de.schroedel.doitlater.utils.DateFormatter;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * To do list item holding information about planned task of user.
 */
public class ToDoItem implements Parcelable, ListItem
{
	public static final String EXTRA_ITEM = "todoItem";
	public static final String EXTRA_TIMESTAMP = "timestamp";

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
	 * Creates new empty to do item.
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
	 * Returns date of to do item as string with 'dd.MM.yyyy' pattern.
	 *
	 * @return - date
	 */
	public String getDate()
	{
		return DateFormatter.getFormattedDate(timestamp, "dd.MM.yyyy");
	}

	/**
	 * Returns day of month of to do item as string.
	 *
	 * @return - day of month
	 */
	public String getDayOfMonth()
	{
		return DateFormatter.getFormattedDate(timestamp, "dd");
	}

	/**
	 * Returns month of to do item as string.
	 *
	 * @return - month
	 */
	public String getMonth()
	{
		return DateFormatter.getFormattedDate(timestamp, "MM");
	}

	/**
	 * Returns minute of to do item as string.
	 *
	 * @return - minute
	 */
	public String getMinute()
	{
		return DateFormatter.getFormattedDate(timestamp, "mm");
	}

	/**
	 * Returns hour of to do item of day as string.
	 *
	 * @return - hour of day
	 */
	public String getHourOfDay()
	{
		return DateFormatter.getFormattedDate(timestamp, "HH");
	}

	@Override
	public ItemType getItemType()
	{
		return ItemType.LIST_ITEM;
	}

	@Override
	public View getView(
		LayoutInflater inflater,
		View convertView,
		ViewGroup parent)
	{
		ViewHolder viewHolder;

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.todo_list_item, parent, false);

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

		String dateTime = null;

		if (timestamp > 0)
		{
			if (ToDoDatabase.dateIsToday(timestamp))
				dateTime = String.format("%s:%s", getHourOfDay(), getMinute());
			else
				dateTime = String.format(
					"%s.%s. %s:%s",
					getMonth(),
					getDayOfMonth(),
					getHourOfDay(),
					getMinute());
		}

		viewHolder.tvTime.setText(dateTime);

		return convertView;
	}
}
