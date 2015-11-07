package de.schroedel.doitlater.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import de.schroedel.doitlater.R;
import de.schroedel.doitlater.content.ToDoItem;

/**
 * Created by Christian Schrödel on 07.11.15.
 *
 * Adapter handling available to do item categories.
 */
public class CategoryAdapter extends ArrayAdapter<ToDoItem.Category> implements SpinnerAdapter
{
	private LayoutInflater inflater;

	/**
	 * Creates to do item category adapter.
	 *
	 * @param context - context
	 */
	public CategoryAdapter(Context context)
	{
		super(context, 0);

		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		return ToDoItem.Category.values().length;
	}

	@Override
	public ToDoItem.Category getItem(int i)
	{
		return ToDoItem.Category.fromValue(i);
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
			R.layout.spinner_category,
			convertView,
			parent);
	}

	/**
	 * Creates category view depending on given layout resource.
	 *
	 * @param position - position
	 * @param layoutResource - layout resource
	 * @param convertView - view to inflate
	 * @param parent - parent view group
	 * @return - created category view
	 */
	private View getView(
		int position,
		int layoutResource,
		View convertView,
		ViewGroup parent)
	{
		Context context = getContext();

		ToDoItem.Category category = getItem(position);

		if (convertView == null)
			convertView = inflater.inflate(layoutResource, parent, false);

		TextView tvCategory =
			(TextView) convertView.findViewById(R.id.category_value);
		tvCategory.setText(getCategoryString(context, category));
		tvCategory.setCompoundDrawablesWithIntrinsicBounds(
			getCategoryDrawable(context, category),
			null,
			null,
			null);

		return convertView;
	}

	/**
	 * Returns string equivalent for given to do item category.
	 *
	 * @param context - context
	 * @param category - to do item category
	 * @return - category string
	 */
	public static String getCategoryString(
		Context context,
		ToDoItem.Category category)
	{
		Resources res = context.getResources();

		switch (category)
		{
			case ITEM_CAR:
				return res.getString(R.string.category_car);
			case ITEM_FOOD:
				return res.getString(R.string.category_food);
			case ITEM_GAMING:
				return res.getString(R.string.category_gaming);
			case ITEM_HOUSE:
				return res.getString(R.string.category_house);
			case ITEM_IMPORTANT:
				return res.getString(R.string.category_important);
			case ITEM_PARTY:
				return res.getString(R.string.category_party);
			case ITEM_PHONE:
				return res.getString(R.string.category_phone);
			case ITEM_SCHOOL:
				return res.getString(R.string.category_school);
			case ITEM_SHOPPING:
				return res.getString(R.string.category_shopping);
			case ITEM_SPORT:
				return res.getString(R.string.category_sport);
			case ITEM_WORK:
				return res.getString(R.string.category_work);

			case ITEM_DEFAULT:
			default:
				return res.getString(R.string.category_default);
		}
	}

	/**
	 * Returns drawable equivalent for given to do category item.
	 *
	 * @param context - context
	 * @param category - to do category item
	 * @return - category drawable
	 */
	public static Drawable getCategoryDrawable(
		Context context,
		ToDoItem.Category category)
	{
		switch (category)
		{
			case ITEM_CAR:
				return ContextCompat.getDrawable(
					context,
					R.drawable.ic_category_car);

			case ITEM_FOOD:
				return ContextCompat.getDrawable(
					context,
					R.drawable.ic_category_food);

			case ITEM_GAMING:
				return ContextCompat.getDrawable(
					context,
					R.drawable.ic_category_gaming);

			case ITEM_HOUSE:
				return ContextCompat.getDrawable(
					context,
					R.drawable.ic_category_house);

			case ITEM_IMPORTANT:
				return ContextCompat.getDrawable(
					context,
					R.drawable.ic_category_important);

			case ITEM_PARTY:
				return ContextCompat.getDrawable(
					context,
					R.drawable.ic_category_party);

			case ITEM_PHONE:
				return ContextCompat.getDrawable(
					context,
					R.drawable.ic_category_phone);

			case ITEM_SCHOOL:
				return ContextCompat.getDrawable(
					context,
					R.drawable.ic_category_school);

			case ITEM_SHOPPING:
				return ContextCompat.getDrawable(
					context,
					R.drawable.ic_category_shopping);

			case ITEM_SPORT:
				return ContextCompat.getDrawable(
					context,
					R.drawable.ic_category_sport);

			case ITEM_WORK:
				return ContextCompat.getDrawable(
					context,
					R.drawable.ic_category_work);

			case ITEM_DEFAULT:
			default:
				return ContextCompat.getDrawable(
					context,
					R.drawable.ic_category_default);
		}
	}
}
