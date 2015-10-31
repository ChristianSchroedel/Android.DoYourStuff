package de.schroedel.doitlater.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import de.schroedel.doitlater.adapter.ToDoListAdapter;
import de.schroedel.doitlater.content.ListItem;
import de.schroedel.doitlater.content.ToDoDatabase;
import de.schroedel.doitlater.content.ToDoItem;
import de.schroedel.doitlater.listener.SwipeDismissListViewTouchListener;


/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 *
 * Created by Christian Schrödel on 10.04.15.
 *
 * Fragment containing to do list items.
 */
public class ItemListFragment extends ListFragment
{
	/**
	 * The serialization (saved instance state) Bundle EXTRA_ITEM representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks callback = sDummyCallbacks;
	private int mActivatedPosition = ListView.INVALID_POSITION;

	public interface Callbacks
	{
		void onItemSelected(ToDoItem item);
		void onItemDismissed(ToDoItem item);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks()
	{
		@Override
		public void onItemSelected(ToDoItem item)
		{
		}

		@Override
		public void onItemDismissed(ToDoItem item)
		{
		}
	};

	@Override
	public void onResume()
	{
		super.onResume();

		setListAdapter(new ToDoListAdapter(
			getActivity(),
			ToDoDatabase.getInstance(getActivity()).getItems()));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

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
									ToDoListAdapter.ItemType.LIST_ITEM.value);
                            }

                            @Override
                            public void onDismiss(
								ListView listView,
								int[] reverseSortedPositions)
                            {
                                if (reverseSortedPositions.length == 0)
                                    return;

                                callback.onItemDismissed(
									(ToDoItem) listView.getItemAtPosition(
                                        reverseSortedPositions[0]));
                            }
                        });

        getListView().setOnTouchListener(dismissListener);
        getListView().setOnScrollListener(dismissListener.makeScrollListener());

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
	public void onDetach()
	{
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		callback = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(
		ListView listView,
		View view,
		int position,
		long id)
	{
		super.onListItemClick(
			listView,
			view,
			position,
			id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		ListItem item = (ListItem) getListAdapter().getItem(position);

		if (item.getItemType() == ToDoListAdapter.ItemType.LIST_ITEM.value)
			callback.onItemSelected((ToDoItem) item);
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		if (mActivatedPosition != ListView.INVALID_POSITION)
		{
			// Serialize and persist the activated item position.
			outState.putInt(
				STATE_ACTIVATED_POSITION,
				mActivatedPosition);
		}
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
	 * Sets activated item in list view.
	 *
	 * @param position - position of item
	 */
	private void setActivatedPosition(int position)
	{
		if (position == ListView.INVALID_POSITION)
			getListView().setItemChecked(mActivatedPosition, false);
		else
			getListView().setItemChecked(position, true);

		mActivatedPosition = position;
	}
}
