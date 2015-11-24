package de.schroedel.doyourstuff.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.alarm.ToDoAlarmManager;
import de.schroedel.doyourstuff.content.ToDoItem;
import de.schroedel.doyourstuff.database.ToDoDatabase;
import de.schroedel.doyourstuff.database.ToDoEntryTable;
import de.schroedel.doyourstuff.fragment.ItemDetailFragment;


/**
 * An activity representing a single Item detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity
{
	public static final int EDIT_ITEM = 2;

	private ToDoItem item;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();

		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null)
		{
			item = getIntent().getParcelableExtra(ToDoItem.EXTRA_ITEM);

			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putParcelable(ToDoItem.EXTRA_ITEM, item);

			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);

			getSupportFragmentManager()
				.beginTransaction()
				.add(
					R.id.item_detail_container,
					fragment)
				.commit();
		}
		else
			item = savedInstanceState.getParcelable(ToDoItem.EXTRA_ITEM);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState)
	{
		savedInstanceState.putParcelable(ToDoItem.EXTRA_ITEM, item);

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_edit, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{
		int id = menuItem.getItemId();

		if (id == android.R.id.home)
		{
			NavUtils.navigateUpTo(
				this,
				new Intent(
					this,
					ItemListActivity.class));

			return true;
		}
		else if (menuItem.getItemId() == R.id.action_edit)
		{
			Intent editIntent = new Intent(this, ItemCreateActivity.class);
			editIntent.putExtra(
				ToDoItem.EXTRA_ITEM,
				item);

			startActivityForResult(editIntent, EDIT_ITEM);
		}

		return super.onOptionsItemSelected(menuItem);
	}

	@Override
	public void onActivityResult(
		int requestCode,
		int resultCode,
		Intent data)
	{
		if (requestCode == EDIT_ITEM)
		{
			if (resultCode == RESULT_OK)
			{
				ToDoItem item;

				if (data != null &&
					(item =
						data.getParcelableExtra(ToDoItem.EXTRA_ITEM)) != null)
				{
					ToDoDatabase database = ToDoDatabase.getInstance(this);
					ToDoEntryTable entryTable = database.getToDoEntryTable();

					entryTable.updateToDoItem(
						item.id,
						item.title,
						item.description,
						item.timestamp,
						item.category);

					ToDoAlarmManager.setReminderAlarm(
						this,
						item,
						ToDoAlarmManager.getAlarmLeadTime(this));
				}

				finish();
				NavUtils.navigateUpTo(
					this,
					new Intent(
						this,
						ItemListActivity.class));
			}
		}
	}
}
