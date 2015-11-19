package de.schroedel.doyourstuff.content;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import de.schroedel.doyourstuff.R;

/**
 * Category type of a to do item.
 */
public enum Category
{
	// Default
	ITEM_DEFAULT(0x01),
	ITEM_IMPORTANT(0x02),
	ITEM_HEALTH(0x03),

	// Goods
	ITEM_SHOPPING(0x11),
	ITEM_FOOD(0x12),

	// Social
	ITEM_GAMING(0x21),
	ITEM_PARTY(0x22),
	ITEM_PHONE(0x23),
	ITEM_PARTNER(0x24),

	// Household
	ITEM_HOUSE(0x31),

	// Duty
	ITEM_SCHOOL(0x41),
	ITEM_WORK(0x42),

	// Fitness
	ITEM_SPORT(0x51),
	ITEM_RUNNING(0x52),
	ITEM_BIKE(0x53),
	ITEM_SWIMMING(0x54),
	ITEM_FOOTBALL(0x55),
	ITEM_FITNESS(0x56),

	// Travel
	ITEM_CAR(0x61);

	private int value;

	Category(int value)
	{
		this.value = value;
	}

	/**
	 * Returns associated integer value of {@link Category}.
	 *
	 * @return integer value
	 */
	public int toValue()
	{
		return value;
	}

	/**
	 * Creates {@link Category} item matching given value.
	 *
	 * @param value value
	 * @return category item
	 */
	public static Category fromValue(int value)
	{
		for (Category cat : Category.values())
		{
			if (cat.toValue() == value)
				return cat;
		}

		return Category.ITEM_DEFAULT;
	}

	/**
	 * Returns string equivalent for given item {@link Category}.
	 *
	 * @param context context
	 * @return category string
	 */
	public String getString(Context context)
	{
		Resources res = context.getResources();

		int stringRes;

		switch (this)
		{
			case ITEM_BIKE: stringRes = R.string.category_bike;	break;
			case ITEM_CAR: stringRes = R.string.category_car; break;
			case ITEM_FITNESS: stringRes = R.string.category_fitness; break;
			case ITEM_FOOD: stringRes = R.string.category_food; break;
			case ITEM_FOOTBALL: stringRes = R.string.category_football; break;
			case ITEM_GAMING: stringRes = R.string.category_gaming; break;
			case ITEM_HEALTH: stringRes = R.string.category_health; break;
			case ITEM_HOUSE: stringRes = R.string.category_house; break;
			case ITEM_IMPORTANT: stringRes = R.string.category_important; break;
			case ITEM_PARTNER: stringRes = R.string.category_partner; break;
			case ITEM_PARTY: stringRes = R.string.category_party; break;
			case ITEM_PHONE: stringRes = R.string.category_phone; break;
			case ITEM_RUNNING: stringRes = R.string.category_running; break;
			case ITEM_SCHOOL: stringRes = R.string.category_school; break;
			case ITEM_SHOPPING: stringRes = R.string.category_shopping; break;
			case ITEM_SPORT: stringRes = R.string.category_sport; break;
			case ITEM_SWIMMING: stringRes = R.string.category_swimming; break;
			case ITEM_WORK: stringRes = R.string.category_work; break;

			case ITEM_DEFAULT:
			default: stringRes = R.string.category_default; break;
		}

		return res.getString(stringRes);
	}

	/**
	 * Returns drawable equivalent for given item {@link Category}.
	 *
	 * @param context context
	 * @return category drawable
	 */
	public Drawable getDrawable(Context context)
	{
		int drawableRes;

		switch (this)
		{
			case ITEM_BIKE:
				drawableRes = R.drawable.ic_category_bike;
				break;

			case ITEM_CAR:
				drawableRes = R.drawable.ic_category_car;
				break;

			case ITEM_FITNESS:
				drawableRes = R.drawable.ic_category_fitness;
				break;

			case ITEM_FOOD:
				drawableRes = R.drawable.ic_category_food;
				break;

			case ITEM_FOOTBALL:
				drawableRes = R.drawable.ic_category_football;
				break;

			case ITEM_GAMING:
				drawableRes = R.drawable.ic_category_gaming;
				break;

			case ITEM_HEALTH:
				drawableRes = R.drawable.ic_category_health;
				break;

			case ITEM_HOUSE:
				drawableRes = R.drawable.ic_category_house;
				break;

			case ITEM_IMPORTANT:
				drawableRes = R.drawable.ic_category_important;
				break;

			case ITEM_PARTNER:
				drawableRes = R.drawable.ic_category_partner;
				break;

			case ITEM_PARTY:
				drawableRes = R.drawable.ic_category_party;
				break;

			case ITEM_PHONE:
				drawableRes = R.drawable.ic_category_phone;
				break;

			case ITEM_RUNNING:
				drawableRes = R.drawable.ic_category_running;
				break;

			case ITEM_SCHOOL:
				drawableRes = R.drawable.ic_category_school;
				break;

			case ITEM_SHOPPING:
				drawableRes = R.drawable.ic_category_shopping;
				break;

			case ITEM_SPORT:
				drawableRes = R.drawable.ic_category_sport;
				break;

			case ITEM_SWIMMING:
				drawableRes = R.drawable.ic_category_swimming;
				break;

			case ITEM_WORK:
				drawableRes = R.drawable.ic_category_work;
				break;

			case ITEM_DEFAULT:
			default:
				drawableRes = R.drawable.ic_category_default;
		}

		return ContextCompat.getDrawable(context, drawableRes);
	}
}
