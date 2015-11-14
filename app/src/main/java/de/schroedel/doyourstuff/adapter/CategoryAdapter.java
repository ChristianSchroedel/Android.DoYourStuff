package de.schroedel.doyourstuff.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.content.Category;

/**+
 * Adapter containing available {@link Category} types.
 */
public class CategoryAdapter extends ArrayAdapter<Category>
{
	private LayoutInflater inflater;

	/**
	 * Creates {@link CategoryAdapter} containing {@link Category} types.
	 *
	 * @param context context
	 */
	public CategoryAdapter(Context context)
	{
		super(context, 0);

		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		return Category.values().length;
	}

	@Override
	public Category getItem(int i)
	{
		return Category.fromValue(i);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return getView(
			position,
			R.layout.spinner_category,
			convertView,
			parent);
	}

	@Override
	public View getDropDownView(
		int position,
		View convertView,
		ViewGroup parent)
	{
		return getView(
			position,
			R.layout.spinner_category_dropdown,
			convertView,
			parent);
	}

	/**
	 * Creates category view depending on given layout resource.
	 *
	 * @param position position
	 * @param layoutResource layout resource
	 * @param convertView view to inflate
	 * @param parent parent view group
	 * @return created category view
	 */
	private View getView(
		int position,
		int layoutResource,
		View convertView,
		ViewGroup parent)
	{
		Context context = getContext();

		Category category = getItem(position);

		if (convertView == null)
			convertView = inflater.inflate(layoutResource, parent, false);

		TextView tvCategoryValue =
			(TextView) convertView.findViewById(R.id.category_value);

		ImageView ivCategoryIcon =
			(ImageView) convertView.findViewById(R.id.category_icon);

		if (tvCategoryValue != null)
		{
			tvCategoryValue.setText(category.getString(context));
			tvCategoryValue.setCompoundDrawablesWithIntrinsicBounds(
				category.getDrawable(context),
				null,
				null,
				null);
		}
		else if (ivCategoryIcon != null)
			ivCategoryIcon.setImageDrawable(category.getDrawable(context));

		return convertView;
	}
}
