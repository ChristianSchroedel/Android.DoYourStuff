package de.schroedel.doyourstuff.activities;

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
import de.schroedel.doyourstuff.fragments.ItemDetailFragment;
import de.schroedel.doyourstuff.models.ToDoItem;


/**
 * An activity representing a single Item detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity
{
	private long itemId;

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
			this.itemId = getIntent().getLongExtra(ToDoItem.EXTRA_ITEM_ID, 0);

			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putLong(ToDoItem.EXTRA_ITEM_ID, itemId);

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
			this.itemId = savedInstanceState.getLong(ToDoItem.EXTRA_ITEM_ID);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState)
	{
		savedInstanceState.putLong(ToDoItem.EXTRA_ITEM_ID, itemId);

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_item, menu);

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
			editIntent.putExtra(ToDoItem.EXTRA_ITEM_ID, itemId);

			startActivity(editIntent);
		}

		return super.onOptionsItemSelected(menuItem);
	}
}
