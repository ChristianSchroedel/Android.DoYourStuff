package schroedel.de.doitlater.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import schroedel.de.doitlater.R;
import schroedel.de.doitlater.activity.ItemCreateActivity;
import schroedel.de.doitlater.content.ToDoItem;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * Dialog to pick date and time.
 */
public final class DateTimePickerFragment extends DialogFragment
{
	public interface OnDateTimePickedCallback
	{
		public void onDateTimePicked(
			int year,
			int month,
			int day,
			int hour,
			int minute);
	}

	private int year = -1;
	private int month = -1;
	private int day = -1;

	private int hour = -1;
	private int minute = -1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Bundle arguments = getArguments();

		if (arguments == null)
			return;

		if (arguments.containsKey(ToDoItem.EXTRA_YEAR))
			year = arguments.getInt(ToDoItem.EXTRA_YEAR);

		if (arguments.containsKey(ToDoItem.EXTRA_MONTH))
			month = arguments.getInt(ToDoItem.EXTRA_MONTH)-1;

		if (arguments.containsKey(ToDoItem.EXTRA_DAYOFMONTH))
			day = arguments.getInt(ToDoItem.EXTRA_DAYOFMONTH);

		if (arguments.containsKey(ToDoItem.EXTRA_HOUROFDAY))
			hour = arguments.getInt(ToDoItem.EXTRA_HOUROFDAY);

		if (arguments.containsKey(ToDoItem.EXTRA_MINUTE))
			minute = arguments.getInt(ToDoItem.EXTRA_MINUTE);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View view = LayoutInflater.from(getActivity()).inflate(
			R.layout.fragment_date_time_dialog,
			null);

		final Calendar calendar = Calendar.getInstance();

		if (year == -1 ||
			month == -1 ||
			day == -1 ||
			hour == -1 ||
			minute == -1)
		{
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH)+1;
			day = calendar.get(Calendar.DAY_OF_MONTH);

			hour = calendar.get(Calendar.HOUR_OF_DAY)+2;
			minute = calendar.get(Calendar.MINUTE);
		}

		final DatePicker datePicker =
			(DatePicker) view.findViewById(R.id.date_picker);
		datePicker.setMinDate(calendar.getTimeInMillis()-1000);
		datePicker.init(year, month - 1, day, null);

		final TimePicker timePicker =
			(TimePicker) view.findViewById(R.id.time_picker);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);

		builder.setView(view);
		builder.setPositiveButton(
			android.R.string.ok,
			new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialogInterface, int i)
				{
					year = datePicker.getYear();
					month = datePicker.getMonth()+1;
					day = datePicker.getDayOfMonth();

					hour = timePicker.getCurrentHour();
					minute = timePicker.getCurrentMinute();

					OnDateTimePickedCallback callback =
						(ItemCreateActivity) getActivity();
					callback.onDateTimePicked(year, month, day,	hour, minute);
				}
			});
		builder.setNegativeButton(android.R.string.cancel, null);

		return builder.create();
	}
}
