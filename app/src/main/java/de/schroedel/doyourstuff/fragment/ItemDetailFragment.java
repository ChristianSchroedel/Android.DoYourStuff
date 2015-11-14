package de.schroedel.doyourstuff.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.activity.ItemDetailActivity;
import de.schroedel.doyourstuff.activity.ItemListActivity;
import de.schroedel.doyourstuff.content.ToDoItem;
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


	private void initDetailViews(ToDoItem item)
	{
		tvTitle.setText(item.title);
		tvDescription.setText(item.description);

		String dateTime = null;

		if (item.timestamp > 0)
			dateTime = DateTimeHelper.getFormattedDate(
				item.timestamp,
				"EEEE - dd.MM.yyyy | HH:mm");

		tvDatetime.setText(dateTime);

		ivCategory.setImageDrawable(
			item.category.getDrawable(getContext()));
	}
}
