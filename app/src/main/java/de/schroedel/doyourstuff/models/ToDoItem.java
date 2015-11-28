package de.schroedel.doyourstuff.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * To do list item holding information about planned task of user.
 */
public class ToDoItem extends ListItem implements Parcelable
{
	public static final String EXTRA_ITEM = "todoItem";

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
	 * Creator to implement {@link android.os.Parcelable.Creator} interface.
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
		out.writeInt(category);
	}

	/**
	 * Creates {@link ToDoItem} from {@link Parcel}.
	 *
	 * @param in parcel containing data
	 */
	private ToDoItem(Parcel in)
	{
		this.id = in.readLong();
		this.title = in.readString();
		this.description = in.readString();
		this.timestamp = in.readLong();
		this.itemDone = in.readInt();
		this.category = in.readInt();
	}
}
