package de.schroedel.doyourstuff.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Basic {@link Parcelable} to restore deleted {@link ToDoItem}s.
 */
public class UndoItem implements Parcelable
{
	public ToDoItem[] items;
	public int[] itemPositions;

	public UndoItem(ToDoItem[] items, int[] itemPositions)
	{
		this.items = items;
		this.itemPositions = itemPositions;
	}

	protected UndoItem(Parcel in)
	{
	}

	public static final Creator<UndoItem> CREATOR = new Creator<UndoItem>()
	{
		@Override
		public UndoItem createFromParcel(Parcel in)
		{
			return new UndoItem(in);
		}

		@Override
		public UndoItem[] newArray(int size)
		{
			return new UndoItem[size];
		}
	};

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i)
	{
	}
}
