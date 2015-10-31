package de.schroedel.doitlater.fragment;

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

import de.schroedel.doitlater.R;
import de.schroedel.doitlater.activity.ItemCreateActivity;
import de.schroedel.doitlater.content.ToDoItem;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * Dialog to pick date and time.
 */
public final class DateTimePickerFragment extends DialogFragment
{
	public interface OnDateTimePickedCallback
	{
		void onDateTimePicked(long timestamp);
	}

	private long timestamp = 0;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Bundle arguments = getArguments();

		if (arguments == null)
			return;

		this.timestamp = arguments.getLong(ToDoItem.EXTRA_TIMESTAMP, 0);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View view = LayoutInflater.from(getActivity()).inflate(
			R.layout.fragment_date_time_dialog,
			null);

		final Calendar calendar = Calendar.getInstance();

		if (timestamp > 0)
			calendar.setTimeInMillis(timestamp);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		int hour = calendar.get(Calendar.HOUR_OF_DAY)+2;
		int minute = calendar.get(Calendar.MINUTE);

		final DatePicker datePicker =
			(DatePicker) view.findViewById(R.id.date_picker);
		// We have to set a date at least 1 second before now.
		datePicker.setMinDate(System.currentTimeMillis()-1000L);
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
					int year = datePicker.getYear();
					int month = datePicker.getMonth();
					int day = datePicker.getDayOfMonth();

					int hour = timePicker.getCurrentHour();
					int minute = timePicker.getCurrentMinute();

					calendar.set(Calendar.YEAR, year);
					calendar.set(Calendar.MONTH, month);
					calendar.set(Calendar.DAY_OF_MONTH, day);
					calendar.set(Calendar.HOUR_OF_DAY, hour);
					calendar.set(Calendar.MINUTE, minute);

					OnDateTimePickedCallback callback =
						(ItemCreateActivity) getActivity();
					callback.onDateTimePicked(calendar.getTimeInMillis());
				}
			});
		builder.setNegativeButton(android.R.string.cancel, null);

		return builder.create();
	}
}
