package de.schroedel.doyourstuff.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

import de.schroedel.doyourstuff.BuildConfig;
import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.managers.ToDoAlarmManager;
import de.schroedel.doyourstuff.models.ListItem;
import de.schroedel.doyourstuff.models.ToDoItem;
import de.schroedel.doyourstuff.models.database.ToDoDatabase;
import de.schroedel.doyourstuff.models.database.ToDoEntryTable;

/**
 * Activity containing available {@link Preference} items of application.
 */
public class SettingsActivity extends PreferenceActivity
{
	public static final String KEY_PREF_ALARM_LEAD_TIME =
		"pref_alarm_lead_time";

	public static final String KEY_PREF_VERSION = "pref_version";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getFragmentManager().
			beginTransaction().
			replace(android.R.id.content, new SettingsFragment()).
			commit();
	}

	/**
	 * Fragment showing {@link Preference} items and listening to changes of
	 * {@link SharedPreferences}.
	 */
	public static class SettingsFragment extends PreferenceFragment implements
		SharedPreferences.OnSharedPreferenceChangeListener
	{
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);

			addPreferencesFromResource(R.xml.preferences);

			Preference prefVersion = findPreference(KEY_PREF_VERSION);
			prefVersion.setSummary(
				getString(
					R.string.preference_summary_version,
					BuildConfig.VERSION_NAME));

			ListPreference prefAlarmLeadTime = (ListPreference)
				getPreferenceScreen().
					findPreference(KEY_PREF_ALARM_LEAD_TIME);
			prefAlarmLeadTime.setSummary(prefAlarmLeadTime.getEntry());
		}

		@Override
		public void onResume()
		{
			super.onResume();

			getPreferenceScreen().
				getSharedPreferences().
				registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onPause()
		{
			super.onPause();

			getPreferenceScreen().
				getSharedPreferences().
				unregisterOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onSharedPreferenceChanged(
			SharedPreferences sharedPreferences,
			String key)
		{
			if (key.equals(KEY_PREF_ALARM_LEAD_TIME))
			{
				ListPreference prefAlarmLeadTime =
					(ListPreference) getPreferenceScreen().findPreference(key);
				prefAlarmLeadTime.setSummary(prefAlarmLeadTime.getEntry());

				updateAlarmTimes(getActivity());
			}
		}
	}

	/**
	 * Updates already set alarm times of known {@link ToDoItem}s.
	 *
	 * @param context context
	 * @param leadTime lead time before an alarm will be triggered before
	 */
	private static void updateAlarmTimes(Context context)
	{
		ToDoDatabase doDatabase = ToDoDatabase.getInstance(context);
		ToDoEntryTable entryTable = doDatabase.getToDoEntryTable();

		List<ListItem> items = entryTable.getAllListItems();

		for (ListItem item : items)
		{
			if (item.getItemType() == ListItem.TYPE_TODO_ENTRY)
				ToDoAlarmManager.setReminderAlarm(context, (ToDoItem) item);
		}
	}
}
