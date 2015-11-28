package de.schroedel.doyourstuff.models.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.schroedel.doyourstuff.activities.SettingsActivity;
import de.schroedel.doyourstuff.models.ToDoItem;

/**
 * Created by christian on 28.11.15.
 */
public final class Preferences
{
	/**
	 * Returns lead time of an alarm for {@link ToDoItem}s from applications
	 * shared preferences.
	 *
	 * @return lead time
	 */
	public static long getAlarmLeadTime(Context context)
	{
		SharedPreferences sharedPreferences =
			PreferenceManager.getDefaultSharedPreferences(context);

		return Long.valueOf(
			sharedPreferences.getString(
				SettingsActivity.KEY_PREF_ALARM_LEAD_TIME,
				""));
	}
}
