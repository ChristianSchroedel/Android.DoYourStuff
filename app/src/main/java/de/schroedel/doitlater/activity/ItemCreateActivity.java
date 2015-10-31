package de.schroedel.doitlater.activity;

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

import de.schroedel.doitlater.R;
import de.schroedel.doitlater.content.ToDoItem;
import de.schroedel.doitlater.fragment.DateTimePickerFragment;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * Activity creating/editing to do list items.
 */
public class ItemCreateActivity extends AppCompatActivity implements
	DateTimePickerFragment.OnDateTimePickedCallback
{
	private EditText etTitle;
	private EditText etDesc;

	private int year = -1;
	private int month = -1;
	private int day = -1;
	private int hour = -1;
	private int minute = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_create);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();

		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();

		if (intent == null)
			return;

		etTitle = (EditText) findViewById(R.id.et_title);
		etDesc = (EditText) findViewById(R.id.et_desc);

		if (intent.hasExtra(ToDoItem.EXTRA_TITLE))
		{
			etTitle.setText(intent.getStringExtra(ToDoItem.EXTRA_TITLE));
			getSupportActionBar().setTitle(R.string.activity_label_update);
		}

		if (intent.hasExtra(ToDoItem.EXTRA_DESCRIPTION))
			etDesc.setText(intent.getStringExtra(ToDoItem.EXTRA_DESCRIPTION));

		if (intent.hasExtra(ToDoItem.EXTRA_YEAR))
			year = intent.getIntExtra(ToDoItem.EXTRA_YEAR, -1);

		if (intent.hasExtra(ToDoItem.EXTRA_MONTH))
			month = intent.getIntExtra(ToDoItem.EXTRA_MONTH, -1);

		if (intent.hasExtra(ToDoItem.EXTRA_DAYOFMONTH))
			day = intent.getIntExtra(ToDoItem.EXTRA_DAYOFMONTH, -1);

		if (intent.hasExtra(ToDoItem.EXTRA_HOUROFDAY))
			hour = intent.getIntExtra(ToDoItem.EXTRA_HOUROFDAY, -1);

		if (intent.hasExtra(ToDoItem.EXTRA_MINUTE))
			minute = intent.getIntExtra(ToDoItem.EXTRA_MINUTE, -1);

		Button btnDate = (Button) findViewById(R.id.btn_dateTime);
		btnDate.setOnClickListener(
			new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					Bundle arguments = new Bundle();
					arguments.putInt(ToDoItem.EXTRA_YEAR, year);
					arguments.putInt(ToDoItem.EXTRA_MONTH, month);
					arguments.putInt(ToDoItem.EXTRA_DAYOFMONTH, day);
					arguments.putInt(ToDoItem.EXTRA_HOUROFDAY, hour);
					arguments.putInt(ToDoItem.EXTRA_MINUTE, minute);

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
			Intent data = new Intent();
			data.putExtra(ToDoItem.EXTRA_TITLE, etTitle.getText().toString());
			data.putExtra(
				ToDoItem.EXTRA_DESCRIPTION,
				etDesc.getText().toString());
			data.putExtra(ToDoItem.EXTRA_YEAR, year);
			data.putExtra(ToDoItem.EXTRA_MONTH, month);
			data.putExtra(ToDoItem.EXTRA_DAYOFMONTH, day);
			data.putExtra(ToDoItem.EXTRA_HOUROFDAY, hour);
			data.putExtra(ToDoItem.EXTRA_MINUTE, minute);

			setResult(RESULT_OK, data);
			finish();

			return false;
		}

		return super.onOptionsItemSelected(menuItem);
	}

	@Override
	public void onDateTimePicked(
		int year,
		int month,
		int day,
		int hour,
		int minute)
	{
		this.hour = hour;
		this.minute = minute;

		this.year = year;
		this.month = month;
		this.day = day;
	}
}
