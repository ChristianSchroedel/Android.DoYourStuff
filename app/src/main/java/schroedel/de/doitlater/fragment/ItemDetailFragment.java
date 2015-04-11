package schroedel.de.doitlater.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import schroedel.de.doitlater.R;
import schroedel.de.doitlater.content.ToDoItem;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link schroedel.de.doitlater.activity.ItemListActivity}
 * in two-pane mode (on tablets) or a {@link schroedel.de.doitlater.activity.ItemDetailActivity}
 * on handsets.
 *
 * Created by Christian Schrödel on 10.04.15.
 *
 * Fragment containing to item details.
 */
public class ItemDetailFragment extends Fragment
{
	private TextView tvDesc;

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

		if (tvDesc == null ||
			item == null)
			return;

		tvDesc.setText(item.description);
	}

	@Override
	public View onCreateView(
		LayoutInflater inflater,
		ViewGroup container,
		Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(
			R.layout.todoitem_detail,
			container,
			false);

		// Show the dummy content as text in a TextView.
		if (item != null)
		{
			tvDesc = (TextView) rootView.findViewById(R.id.todo_detail_desc);
			tvDesc.setText(item.description);
		}

		return rootView;
	}
}
