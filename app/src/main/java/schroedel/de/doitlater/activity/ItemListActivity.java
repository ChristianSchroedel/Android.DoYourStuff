package schroedel.de.doitlater.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;

import schroedel.de.doitlater.R;
import schroedel.de.doitlater.adapter.ToDoListAdapter;
import schroedel.de.doitlater.content.ToDoDatabase;
import schroedel.de.doitlater.content.ToDoItem;
import schroedel.de.doitlater.fragment.ItemDetailFragment;
import schroedel.de.doitlater.fragment.ItemListFragment;
import schroedel.de.doitlater.service.AlarmReceiver;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 *
 * The activity makes heavy use of fragments. The list of items is a
 * {@link schroedel.de.doitlater.fragment.ItemListFragment} and the item details
 * (if present) is a {@link schroedel.de.doitlater.fragment.ItemDetailFragment}.
 *
 * This activity also implements the required
 * {@link schroedel.de.doitlater.fragment.ItemListFragment.Callbacks} interface
 * to listen for item selections.
 *
 * Created by Christian Schrödel on 10.04.15.
 *
 * Activity showing list of existing to do items.
 */
public class ItemListActivity extends ActionBarActivity
	implements ItemListFragment.Callbacks
{
	public static final int ADD_ITEM = 1;
	public static final int EDIT_ITEM = 2;

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
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
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			twoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ItemListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.item_list))
				.setActivateOnItemClick(true);
		}

		Intent intent = getIntent();

		if (intent != null)
		{
			if (intent.hasExtra(ToDoItem.EXTRA_ID))
			{
				long id = intent.getLongExtra(ToDoItem.EXTRA_ID, -1);
				onItemSelected(ToDoDatabase.getInstance(this).getItem(id));
			}
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (selectedItem == null)
			return;

		if (twoPane)
			onItemSelected(selectedItem);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_list, menu);

		actionEdit = menu.findItem(R.id.action_edit);

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

	/**
	 * Returns list adapter of list fragment.
	 *
	 * @return - list adapter
	 */
	private ToDoListAdapter getListAdapter()
	{
		return (ToDoListAdapter) ((ItemListFragment) getSupportFragmentManager()
			.findFragmentById(R.id.item_list))
			.getListAdapter();
	}

	/**
	 * Adds a new item to the to do list.
	 */
	private void addItem()
	{
		Intent addIntent = new Intent(this, ItemCreateActivity.class);
		startActivityForResult(addIntent, ADD_ITEM);
	}

	/**
	 * Edits item of the to do list.
	 *
	 * @param item - to do list item
	 */
	private void editItem(ToDoItem item)
	{
		Intent editIntent = new Intent(this, ItemCreateActivity.class);
		ToDoItem.putItemDataAsExtras(item, editIntent);
		startActivityForResult(editIntent, EDIT_ITEM);
	}

	/**
	 * Removes item of the to do list.
	 *
	 * @param item - to do list item
	 */
	private void removeItem(ToDoItem item)
	{
		cancelReminderAlarm(item);
		getListAdapter().remove(item);
	}

	@Override
	protected void onActivityResult(
		int requestCode,
		int resultCode,
		Intent data)
	{
		if (requestCode == ADD_ITEM)
		{
			if (resultCode == RESULT_OK)
			{
				if (!ToDoItem.itemExtrasAreValid(data))
					return;

				ToDoItem item = new ToDoItem();

				ToDoItem.initItemFromExtras(item, data);

				ToDoListAdapter adapter = getListAdapter();
				adapter.add(item);

				setReminderAlarm(item);

				selectedItem = item;
			}
		}
		else if (requestCode == EDIT_ITEM)
		{
			if (resultCode == RESULT_OK)
			{
				if (selectedItem == null)
					return;

				if (!ToDoItem.itemExtrasAreValid(data))
					return;

				ToDoItem.initItemFromExtras(selectedItem, data);

				ToDoDatabase.getInstance(this).updateItemText(
					selectedItem.id,
					selectedItem.title,
					selectedItem.description);
				ToDoDatabase.getInstance(this).updateItemDateTime(
					selectedItem.id,
					selectedItem.getDateTimeString());

				setReminderAlarm(selectedItem);
			}
		}
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks}
	 * indicating that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(ToDoItem item)
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

			selectedItem = item;
			actionEdit.setVisible(true);
		}
		else
		{
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ItemDetailActivity.class);
			detailIntent.putExtra(ToDoItem.EXTRA_ITEM, item);

			startActivity(detailIntent);
		}
	}

	@Override
	public void onItemDismissed(ToDoItem item)
	{
		removeItem(item);
	}

	/**
	 * Sets reminder alarm for given to do list item.
	 *
	 * @param item - to do list item
	 */
	private void setReminderAlarm(ToDoItem item)
	{
		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra(ToDoItem.EXTRA_ID, item.id);

		PendingIntent alarm =
			PendingIntent.getBroadcast(this, (int) item.id, intent, 0);

		AlarmManager manager =
			(AlarmManager) getSystemService(Context.ALARM_SERVICE);
		manager.set(AlarmManager.RTC_WAKEUP, item.getDateTime(), alarm);
	}

	/**
	 * Cancels reminder alarm for given to do list item.
	 *
	 * @param item - to do list item
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
