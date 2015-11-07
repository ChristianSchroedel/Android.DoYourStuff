package de.schroedel.doitlater.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.schroedel.doitlater.R;
import de.schroedel.doitlater.adapter.CategoryAdapter;
import de.schroedel.doitlater.content.ToDoItem;
import de.schroedel.doitlater.utils.DateFormatter;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link de.schroedel.doitlater.activity.ItemListActivity}
 * in two-pane mode (on tablets) or a {@link de.schroedel.doitlater.activity.ItemDetailActivity}
 * on handsets.
 *
 * Created by Christian Schrödel on 10.04.15.
 *
 * Fragment containing to item details.
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

		if (!isInitialized())
			return;

		tvTitle.setText(item.title);
		tvDescription.setText(item.description);
		tvDatetime.setText(
			DateFormatter.getFormattedDate(
				item.timestamp,
				"EEEE - dd.MM.yyyy | HH:mm"));
		ivCategory.setImageDrawable(
			CategoryAdapter.getCategoryDrawable(getContext(), item.category));
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
			tvTitle = (TextView) rootView.findViewById(R.id.todo_detail_title);
			tvTitle.setText(item.title);

			tvDescription =
				(TextView) rootView.findViewById(R.id.todo_detail_desc);
			tvDescription.setText(item.description);

			tvDatetime =
				(TextView) rootView.findViewById(R.id.todo_detail_datetime);
			tvDatetime.setText(
				DateFormatter.getFormattedDate(
					item.timestamp,
					"EEEE - dd.MM.yyyy | HH:mm"));

			ivCategory =
				(ImageView) rootView.findViewById(R.id.todo_detail_category);
			ivCategory.setImageDrawable(
				CategoryAdapter.getCategoryDrawable(
					getContext(),
					item.category));
		}

		return rootView;
	}

	/**
	 * Checks if fragment view is already initialized.
	 */
	private boolean isInitialized()
	{
		return
			(tvTitle != null &&
			tvDescription != null &&
			tvDatetime != null &&
			ivCategory != null &&
			item != null);
	}
}
