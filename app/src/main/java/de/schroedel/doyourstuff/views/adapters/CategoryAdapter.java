package de.schroedel.doyourstuff.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.schroedel.doyourstuff.R;
import de.schroedel.doyourstuff.models.Category;

/**
 * Adapter containing available {@link Category} types.
 */
public class CategoryAdapter extends ArrayAdapter<Integer>
{
	private LayoutInflater inflater;
	private int[] categories;

	/**
	 * Creates {@link CategoryAdapter} containing {@link Category} types.
	 *
	 * @param context context
	 */
	public CategoryAdapter(Context context)
	{
		super(context, 0);

		this.inflater = LayoutInflater.from(context);
		this.categories = Category.getCategoryValues();
	}

	@Override
	public int getCount()
	{
		return categories.length;
	}

	@Override
	public Integer getItem(int i)
	{
		return categories[i];
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

		int category = getItem(position);

		if (convertView == null)
			convertView = inflater.inflate(layoutResource, parent, false);

		TextView tvCategoryValue =
			(TextView) convertView.findViewById(R.id.category_value);

		ImageView ivCategoryIcon =
			(ImageView) convertView.findViewById(R.id.category_icon);

		if (tvCategoryValue != null)
		{
			tvCategoryValue.setText(
				Category.getCategoryString(context, category));
			tvCategoryValue.setCompoundDrawablesWithIntrinsicBounds(
				Category.getCategoryDrawable(context, category),
				null,
				null,
				null);
		}
		else if (ivCategoryIcon != null)
			ivCategoryIcon.setImageDrawable(
				Category.getCategoryDrawable(context, category));

		return convertView;
	}
}
