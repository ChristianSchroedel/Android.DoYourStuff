package de.schroedel.doyourstuff.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.activities.ItemDetailActivity;
import de.schroedel.doyourstuff.activities.ItemListActivity;
import de.schroedel.doyourstuff.models.Category;
import de.schroedel.doyourstuff.models.ToDoItem;
import de.schroedel.doyourstuff.utils.DateTimeHelper;

/**
 * A fragment representing a single {@link ToDoItem} detail screen. This
 * fragment is either contained in a {@link ItemListActivity} in two-pane mode
 * (on tablets) or a {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment
{
	private TextView tvTitle;
	private TextView tvDescription;
	private TextView tvDatetime;
	private ImageView ivCategory;

	private ToDoItem item;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Bundle arguments = getArguments();

		if (arguments.containsKey(ToDoItem.EXTRA_ITEM))
			item = arguments.getParcelable(ToDoItem.EXTRA_ITEM);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		if (isInitialized() &&
			item != null)
			initDetailViews(item);
	}

	@Override
	public View onCreateView(
		LayoutInflater inflater,
		ViewGroup container,
		Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(
			R.layout.fragment_item_detail,
			container,
			false);

		if (item != null)
		{
			this.tvTitle = (TextView) rootView.findViewById(R.id.todo_detail_title);
			this.tvDescription =
				(TextView) rootView.findViewById(R.id.todo_detail_desc);
			this.tvDatetime =
				(TextView) rootView.findViewById(R.id.todo_detail_datetime);
			this.ivCategory =
				(ImageView) rootView.findViewById(R.id.todo_detail_category);

			initDetailViews(item);
		}

		return rootView;
	}

	/**
	 * Checks {@link View} items of {@link ItemDetailFragment} are already
	 * initialized.
	 */
	private boolean isInitialized()
	{
		return
			(tvTitle != null &&
			tvDescription != null &&
			tvDatetime != null &&
			ivCategory != null);
	}

	/**
	 * Initializes fragment {@link View} objects with {@link ToDoItem}
	 * information.
	 *
	 * @param item item containing information
	 */
	private void initDetailViews(ToDoItem item)
	{
		initDateTimeView(item.timestamp);

		tvTitle.setText(item.title);
		tvDescription.setText(item.description);
		ivCategory.setImageDrawable(
			Category.getCategoryDrawable(getContext(), item.category));
	}

	/**
	 * Initializes {@link TextView} showing datetime of {@link ToDoItem} with a
	 * timestamp value.
	 * <p>
	 * Hides view showing datetime when there is no valid datetime provided.
	 * </p>
	 *
	 * @param timestamp timestamp
	 */
	private void initDateTimeView(long timestamp)
	{
		if (timestamp > 0)
		{
			String dateTime = DateTimeHelper.getFormattedDate(
				timestamp,
				"EEEE - dd.MM.yyyy | HH:mm");

			tvDatetime.setText(dateTime);
		}
		else
			tvDatetime.setVisibility(View.GONE);
	}
}
