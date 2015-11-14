package de.schroedel.doyourstuff.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.utils.DateTimeHelper;

/**
 * To do list item holding information about planned task of user.
 */
public class ToDoItem implements Parcelable, ListItem
{
	public static final String EXTRA_ITEM = "todoItem";
	public static final String EXTRA_TIMESTAMP = "timestamp";

	public static final int ITEM_PENDING = 0;
	public static final int ITEM_DONE = 1;

	public long id;
	public String title;
	public String description;
	public long timestamp;

	public int itemDone;
	public Category category;

	static class ViewHolder
	{
		ImageView ivCategory;
		TextView tvTitle;
		TextView tvDesc;
		TextView tvTime;
	}

	/**
	 * Creates new empty {@link ToDoItem}.
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
		out.writeInt(category.toValue());
	}

	/**
	 * Creates {@link ToDoItem} from {@link Parcel}.
	 *
	 * @param in parcel containing data
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
			viewHolder.ivCategory =
				(ImageView) convertView.findViewById(R.id.todo_list_category);
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

		viewHolder.ivCategory.setImageDrawable(
			category.getDrawable(inflater.getContext()));
		viewHolder.tvTitle.setText(title);
		viewHolder.tvDesc.setText(description);

		String dateTime = null;

		if (timestamp > 0)
			dateTime = DateTimeHelper.getFormattedDate(timestamp, "HH:mm");

		viewHolder.tvTime.setText(dateTime);

		return convertView;
	}
}
