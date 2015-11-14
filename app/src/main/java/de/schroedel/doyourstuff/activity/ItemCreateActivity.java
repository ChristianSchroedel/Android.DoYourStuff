package de.schroedel.doyourstuff.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.adapter.CategoryAdapter;
import de.schroedel.doyourstuff.content.Category;
import de.schroedel.doyourstuff.content.ToDoItem;
import de.schroedel.doyourstuff.fragment.DateTimePickerFragment;

/**
 * Activity creating/editing {@link ToDoItem} values.
 */
public class ItemCreateActivity extends AppCompatActivity implements
	DateTimePickerFragment.OnDateTimePickedCallback
{
	private EditText etTitle;
	private EditText etDesc;
	private Spinner spCategory;

	private ToDoItem item;

	private long timestamp;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_create);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		final ActionBar actionBar = getSupportActionBar();

		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		this.etTitle = (EditText) findViewById(R.id.title);
		this.etDesc = (EditText) findViewById(R.id.description);
		this.spCategory = (Spinner) findViewById(R.id.category);

		spCategory.setAdapter(new CategoryAdapter(this));

		Intent intent = getIntent();

		ToDoItem item = intent.getParcelableExtra(ToDoItem.EXTRA_ITEM);

		if (item != null)
		{
			this.item = item;
			this.timestamp = item.timestamp;

			etTitle.setText(item.title);
			etDesc.setText(item.description);
			spCategory.setSelection(item.category.toValue());

			if (actionBar != null)
				actionBar.setTitle(item.title);
		}

		Button btnDate = (Button) findViewById(R.id.dateTime);
		btnDate.setOnClickListener(
			new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					Bundle arguments = new Bundle();
					arguments.putLong(ToDoItem.EXTRA_TIMESTAMP, timestamp);

					DialogFragment picker = new DateTimePickerFragment();
					picker.setArguments(arguments);
					picker.show(getFragmentManager(), "dateTimePicker");
				}
			});
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
		else if (id == R.id.action_edit)
		{
			if (item == null)
				item = new ToDoItem();

			item.title = etTitle.getText().toString();
			item.description = etDesc.getText().toString();
			item.category = (Category) spCategory.getSelectedItem();
			item.timestamp = timestamp;

			Intent resultIntent = new Intent();
			resultIntent.putExtra(ToDoItem.EXTRA_ITEM, item);

			setResult(RESULT_OK, resultIntent);
			finish();

			return false;
		}

		return super.onOptionsItemSelected(menuItem);
	}

	@Override
	public void onDateTimePicked(long timestamp)
	{
		this.timestamp = timestamp;
	}
}
