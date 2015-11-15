package de.schroedel.doyourstuff.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.adapter.ToDoListAdapter;
import de.schroedel.doyourstuff.content.ListItem;
import de.schroedel.doyourstuff.content.ToDoItem;
import de.schroedel.doyourstuff.database.ToDoDatabase;
import de.schroedel.doyourstuff.database.ToDoEntryTable;
import de.schroedel.doyourstuff.listener.SwipeDismissListViewTouchListener;
import de.schroedel.doyourstuff.receiver.BootReceiver;


/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which {@link ToDoItem} is currently being
 * viewed in a {@link ItemDetailFragment}.
 */
public class ItemListFragment extends ListFragment
{
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	private Callbacks callback;

	private int activatedPosition = ListView.INVALID_POSITION;

	public interface Callbacks
	{
		void onItemSelected(int position);
		void onItemDismissed(int position);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		ToDoDatabase database = ToDoDatabase.getInstance(getContext());
		ToDoEntryTable entryTable = database.getToDoEntryTable();

		setListAdapter(new ToDoListAdapter(getContext(), entryTable.getAll()));

		setActivatedPosition(activatedPosition);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		initListView();

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
			&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION))
		{
			setActivatedPosition(
				savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);

		// Activities containing this fragment must implement its callbacks.
		if (!(context instanceof Callbacks))
		{
			throw new IllegalStateException(
				"Activity must implement fragment's callbacks.");
		}

		callback = (Callbacks) context;
	}

	@Override
	public void onListItemClick(
		ListView listView,
		View view,
		int position,
		long id)
	{
		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		ListItem item = (ListItem) getListAdapter().getItem(position);

		if (item.getItemType() == ListItem.ItemType.LIST_ITEM)
			callback.onItemSelected(position);

		setActivatedPosition(position);
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		if (activatedPosition != ListView.INVALID_POSITION)
		{
			// Serialize and persist the activated item position.
			outState.putInt(
				STATE_ACTIVATED_POSITION,
				activatedPosition);
		}
	}

	/**
	 * Initializes underlying {@link ListView} with {@link
	 * SwipeDismissListViewTouchListener}.
	 */
	private void initListView()
	{
		SwipeDismissListViewTouchListener dismissListener =
			new SwipeDismissListViewTouchListener(
				getListView(),
				new SwipeDismissListViewTouchListener.DismissCallbacks()
				{
					@Override
					public boolean canDismiss(int position)
					{
						ListItem item =	(ListItem) getListAdapter().
							getItem(position);

						return (item.getItemType() ==
							ListItem.ItemType.LIST_ITEM);
					}

					@Override
					public void onDismiss(
						ListView listView,
						int[] reverseSortedPositions)
					{
						if (reverseSortedPositions.length == 0)
							return;

						callback.onItemDismissed(reverseSortedPositions[0]);
					}
				});

		ListView lv = getListView();
		lv.setOnTouchListener(dismissListener);
		lv.setOnScrollListener(dismissListener.makeScrollListener());
		lv.setSelector(R.drawable.item_list_selector);
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick)
	{
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
			activateOnItemClick
				? ListView.CHOICE_MODE_SINGLE
				: ListView.CHOICE_MODE_NONE);
	}

	/**
	 * Sets activated item in underlying {@link ListView}.
	 *
	 * @param position position of list item
	 */
	public void setActivatedPosition(int position)
	{
		ListView lv = getListView();

		if (position == ListView.INVALID_POSITION)
			lv.setItemChecked(activatedPosition, false);
		else
			lv.setItemChecked(position, true);

		this.activatedPosition = position;
	}

	/**
	 * Returns {@link ToDoItem} from underlying {@link ListView}.
	 *
	 * @param index index of item
	 * @return found item
	 */
	public ToDoItem getToDoItem(int index)
	{
		ToDoListAdapter adapter = (ToDoListAdapter) getListView().getAdapter();

		ListItem item = adapter.getItem(index);

		if (item instanceof ToDoItem)
			return (ToDoItem) item;

		return null;
	}

	/**
	 * Removes {@link ToDoItem} from underlying {@link ListView}.
	 *
	 * @param item item to remove
	 */
	public void removeItem(ToDoItem item)
	{
		ToDoDatabase database = ToDoDatabase.getInstance(getContext());

		ToDoEntryTable entryTable = database.getToDoEntryTable();
		entryTable.remove(item.id);

		// Disable boot command receiver if there are no more set alarms.
		if (entryTable.getCount() == 0)
			BootReceiver.setComponentEnabled(getContext(), false);

		ToDoListAdapter adapter = (ToDoListAdapter) getListAdapter();
		adapter.remove(item);
	}
}
