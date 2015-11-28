package de.schroedel.doyourstuff.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.views.adapters.CategoryAdapter;
import de.schroedel.doyourstuff.models.Category;
import de.schroedel.doyourstuff.models.ToDoItem;

/**
 * Activity creating/editing {@link ToDoItem} values.
 */
public class ItemCreateActivity extends AppCompatActivity
{
	public static final int RESULT_CANCELED_NO_TITLE = 1;

	private EditText etTitle;
	private EditText etDesc;
	private Spinner spCategory;

	private ToDoItem item;

	private static Calendar calendar;
	private static long timestamp;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_create);

		initEditorViews();

		String title = null;
		ToDoItem item = getIntent().getParcelableExtra(ToDoItem.EXTRA_ITEM);

		if (item != null)
		{
			initEditorViewValues(item);
			title = item.title;
		}

		initActionBar(title);
		initCalendar(timestamp);
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

			resetTime();

			return true;
		}
		else if (id == R.id.action_edit)
		{
			String title = etTitle.getText().toString();

			if (title.isEmpty())
			{
				// A set title is mandatory for every to do item.
				setResultAndFinish(RESULT_CANCELED_NO_TITLE, null);
				return false;
			}

			if (item == null)
				item = new ToDoItem();

			item.title = title;
			item.description = etDesc.getText().toString();
			item.category = (int) spCategory.getSelectedItem();
			item.timestamp = timestamp;

			Intent resultIntent = new Intent();
			resultIntent.putExtra(ToDoItem.EXTRA_ITEM, item);

			setResultAndFinish(RESULT_OK, resultIntent);

			resetTime();

			return false;
		}

		return super.onOptionsItemSelected(menuItem);
	}

	/**
	 * Initializes {@link View} items of activity.
	 */
	private void initEditorViews()
	{
		this.etTitle = (EditText) findViewById(R.id.title);
		this.etDesc = (EditText) findViewById(R.id.description);
		this.spCategory = (Spinner) findViewById(R.id.category);

		spCategory.setAdapter(new CategoryAdapter(this));

		ImageButton btnDate = (ImageButton) findViewById(R.id.date);
		btnDate.setOnClickListener(
			new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					DatePickerFragment fragment = new DatePickerFragment();
					fragment.show(getSupportFragmentManager(), "datePicker");
				}
			});

		ImageButton btnTime = (ImageButton) findViewById(R.id.time);
		btnTime.setOnClickListener(
			new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					TimePickerFragment fragment = new TimePickerFragment();
					fragment.show(getSupportFragmentManager(), "timePicker");
				}
			});
	}

	/**
	 * Sets default values of activity {@link View} items with {@link ToDoItem}
	 * information.
	 *
	 * @param item item with information
	 */
	private void initEditorViewValues(ToDoItem item)
	{
		timestamp = item.timestamp;

		this.item = item;

		etTitle.setText(item.title);
		etDesc.setText(item.description);
		spCategory.setSelection(getCategorySelection(item.category));
	}

	/**
	 * Returns index of {@link Category} in {@link Spinner} from all available
	 * categories.
	 *
	 * @param category category item
	 * @return index of category in spinner
	 */
	private int getCategorySelection(int category)
	{
		int[] categories = Category.getCategoryValues();
		int selection = 0;

		for (int i = 0; i < categories.length; ++i)
		{
			if (category == categories[i])
			{
				selection = i;
				break;
			}
		}

		return selection;
	}

	/**
	 * Initializes {@link ActionBar} with a set title.
	 *
	 * @param title title of action bar
	 */
	private void initActionBar(String title)
	{
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();

		if (actionBar != null)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);

			if (title != null)
				actionBar.setTitle(title);
		}
	}

	/**
	 * Initializes {@link Calendar} with set timestamp. If timestamp is 0
	 * {@link Calendar} uses the current time.
	 *
	 * @param timestamp UTC timestamp
	 */
	private void initCalendar(long timestamp)
	{
		if (calendar == null)
			calendar = Calendar.getInstance();

		if (timestamp > 0)
			calendar.setTimeInMillis(timestamp);
	}

	/**
	 * Resets timestamp and {@link Calendar} instance.
	 */
	private void resetTime()
	{
		timestamp = 0;
		calendar = null;
	}

	/**
	 * Sets result for activity and finishes it.
	 *
	 * @param result result code
	 */
	private void setResultAndFinish(int result, Intent intent)
	{
		setResult(result, intent);
		finish();
	}

	/**
	 * Dialog fragment to pick hour of day and minute time values.
	 */
	public static class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener
	{
		@NonNull
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);

			Context context = getActivity();

			return new TimePickerDialog(
				context,
				this,
				hour,
				minute,
				DateFormat.is24HourFormat(context));
		}

		@Override
		public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute)
		{
			calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calendar.set(Calendar.MINUTE, minute);

			timestamp = calendar.getTimeInMillis();
		}
	}

	/**
	 * Dialog fragment to pick year, month and day of month date values.
	 */
	public static class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener
	{
		@NonNull
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);

			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(
			DatePicker datePicker,
			int year,
			int month,
			int day)
		{
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, day);

			timestamp = calendar.getTimeInMillis();
		}
	}
}
