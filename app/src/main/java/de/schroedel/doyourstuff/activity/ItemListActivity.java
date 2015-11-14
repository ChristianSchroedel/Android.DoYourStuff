package de.schroedel.doyourstuff.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.content.ToDoItem;
import de.schroedel.doyourstuff.database.ToDoDatabase;
import de.schroedel.doyourstuff.database.ToDoEntryTable;
import de.schroedel.doyourstuff.fragment.ItemDetailFragment;
import de.schroedel.doyourstuff.fragment.ItemListFragment;
import de.schroedel.doyourstuff.receiver.AlarmReceiver;


/**
 * An activity representing a list of items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a {@link ItemDetailActivity}
 * representing item details. On tablets, the activity presents the list of
 * items and item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * </p>
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 * </p>
 */
public class ItemListActivity extends AppCompatActivity
	implements ItemListFragment.Callbacks
{
	public static final int ADD_ITEM = 1;
	public static final int EDIT_ITEM = 2;

	private boolean twoPane;

	private MenuItem actionEdit;

	private ToDoItem selectedItem;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

        FloatingActionButton fabNew =
			(FloatingActionButton) findViewById(R.id.action_new);
		fabNew.setOnClickListener(
			new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					addItem();
				}
			}
		);

		if (findViewById(R.id.item_detail_container) != null)
		{
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and res/values-sw600dp).
			// If this view is present, then the activity should be in two-pane
			// mode.
			twoPane = true;

			// In two-pane mode, list items should be given the 'activated'
			// state when touched.
			((ItemListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.item_list))
				.setActivateOnItemClick(true);
		}

		Intent intent = getIntent();

		if (intent != null)
			this.selectedItem = intent.getParcelableExtra(ToDoItem.EXTRA_ITEM);

		if (savedInstanceState != null)
			this.selectedItem =
				savedInstanceState.getParcelable(ToDoItem.EXTRA_ITEM);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (selectedItem == null)
			return;

		if (twoPane)
			showToDoDetails(selectedItem);
	}


	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putParcelable(ToDoItem.EXTRA_ITEM, selectedItem);

		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(
			R.menu.menu_list,
			menu);

		this.actionEdit = menu.findItem(R.id.action_edit);

		actionEdit.setVisible(selectedItem != null);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{
		if (menuItem.getItemId() == R.id.action_edit)
		{
			editItem(selectedItem);
			return true;
		}

		return false;
	}

	@Override
	protected void onActivityResult(
		int requestCode,
		int resultCode,
		Intent data)
	{
		if (requestCode == ADD_ITEM ||
			requestCode == EDIT_ITEM)
		{
			if (resultCode == RESULT_OK)
			{
				ToDoItem item;

				if (data == null)
					return;

				item = data.getParcelableExtra(ToDoItem.EXTRA_ITEM);

				ToDoDatabase database = ToDoDatabase.getInstance(this);
				ToDoEntryTable entryTable = database.getToDoEntryTable();

				// If ID of to do item is not a real number it is not stored in
				// the database yet.
				if (item.id < 0)
					entryTable.insert(item);
				else
					entryTable.updateToDoItem(
						item.id,
						item.title,
						item.description,
						item.timestamp,
						item.category);

				if (item.timestamp != 0)
					setReminderAlarm(item);

				this.selectedItem = item;
			}
		}
	}

	@Override
	public void onItemSelected(int index)
	{
		ToDoItem item = null;
		ItemListFragment listFragment = getListFragment();

		if (listFragment != null)
			item = listFragment.getToDoItem(index);

		if (item == null)
			return;

		this.selectedItem = item;

		showToDoDetails(item);
	}

	@Override
	public void onItemDismissed(int index)
	{
		ToDoItem item = null;
		ItemListFragment listFragment = getListFragment();

		if (listFragment != null)
			item = listFragment.getToDoItem(index);

		if (item != null)
			removeItem(item);
	}

	/**
	 * Returns {@link ItemListFragment} containing {@link ToDoItem} objects.
	 *
	 * @return {@link ItemListFragment}
	 */
	private ItemListFragment getListFragment()
	{
		return (ItemListFragment) getSupportFragmentManager().
			findFragmentById(R.id.item_list);
	}

	/**
	 * Starts {@link ItemCreateActivity} to add a new {@link ToDoItem}.
	 */
	private void addItem()
	{
		Intent addIntent = new Intent(this, ItemCreateActivity.class);
		startActivityForResult(addIntent, ADD_ITEM);
	}

	/**
	 * Starts {@link ItemCreateActivity} to edit a {@link ToDoItem}.
	 *
	 * @param item {@link ToDoItem} to edit
	 */
	private void editItem(ToDoItem item)
	{
		Intent editIntent = new Intent(this, ItemCreateActivity.class);
		editIntent.putExtra(ToDoItem.EXTRA_ITEM, item);

		startActivityForResult(editIntent, EDIT_ITEM);
	}

	/**
	 * Removes {@link ToDoItem} from {@link ItemListFragment}.
	 *
	 * @param item {@link ToDoItem} to remove
	 */
	private void removeItem(ToDoItem item)
	{
		cancelReminderAlarm(item);

		ItemListFragment listFragment = getListFragment();

		if (listFragment != null)
			listFragment.removeItem(item);
	}

	/**
	 * Shows detailed information of {@link ToDoItem}.
	 * <p>
	 * On tablets details of {@link ToDoItem} are displayed in {@link ItemDetailFragment},
	 * while on handsets the {@link ItemDetailActivity} is started.
	 * </p>
	 *
	 * @param item {@link ToDoItem} to show detailed information of
	 */
	private void showToDoDetails(ToDoItem item)
	{
		if (twoPane)
		{
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putParcelable(ToDoItem.EXTRA_ITEM, item);

			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);

			getSupportFragmentManager()
				.beginTransaction()
				.replace(
					R.id.item_detail_container,
					fragment)
				.commit();

			if (actionEdit != null)
				actionEdit.setVisible(true);
		}
		else
		{
			// In single-pane mode, simply start the detail activity for the
			// selected to do item.
			Intent detailIntent = new Intent(this, ItemDetailActivity.class);
			detailIntent.putExtra(ToDoItem.EXTRA_ITEM, item);

			startActivity(detailIntent);
		}
	}

	/**
	 * Sets reminder alarm for given {@link ToDoItem}.
	 *
	 * @param item {@link ToDoItem} to set alarm for
	 */
	private void setReminderAlarm(ToDoItem item)
	{
		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra(ToDoItem.EXTRA_ITEM, item);

		PendingIntent alarm =
			PendingIntent.getBroadcast(this, (int) item.id, intent, 0);

		AlarmManager manager =
			(AlarmManager) getSystemService(Context.ALARM_SERVICE);
		manager.set(AlarmManager.RTC_WAKEUP, item.timestamp, alarm);
	}

	/**
	 * Cancels reminder alarm for given {@link ToDoItem}.
	 *
	 * @param item {@link ToDoItem} to cancel alarm for
	 */
	private void cancelReminderAlarm(ToDoItem item)
	{
		Intent intent = new Intent(this, AlarmReceiver.class);

		PendingIntent alarm =
			PendingIntent.getBroadcast(this, (int) item.id, intent, 0);

		AlarmManager manager =
			(AlarmManager) getSystemService(Context.ALARM_SERVICE);
		manager.cancel(alarm);
	}
}
