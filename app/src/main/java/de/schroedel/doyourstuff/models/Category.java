package de.schroedel.doyourstuff.models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import de.schroedel.doyourstuff.R;

/**
 * Category type of a to do item.
 */
public final class Category
{
	// Default
	public static final int CATEGORY_DEFAULT = 0x01;
	public static final int CATEGORY_IMPORTANT = 0x02;
	public static final int CATEGORY_HEALTH = 0x03;

	// Goods
	public static final int CATEGORY_SHOPPING = 0x11;
	public static final int CATEGORY_FOOD = 0x12;

	// Social
	public static final int CATEGORY_GAMING = 0x21;
	public static final int CATEGORY_PARTY = 0x22;
	public static final int CATEGORY_PHONE = 0x23;
	public static final int CATEGORY_PARTNER = 0x24;

	// Household
	public static final int CATEGORY_HOUSE = 0x31;

	// Duty
	public static final int CATEGORY_SCHOOL = 0x41;
	public static final int CATEGORY_WORK = 0x42;

	// Fitness
	public static final int CATEGORY_SPORT = 0x51;
	public static final int CATEGORY_RUNNING = 0x52;
	public static final int CATEGORY_BIKE = 0x53;
	public static final int CATEGORY_SWIMMING = 0x54;
	public static final int CATEGORY_FOOTBALL = 0x55;
	public static final int CATEGORY_FITNESS = 0x56;

	// Travel
	public static final int CATEGORY_CAR = 0x61;

	private static final int[] categories = new int[] {
		CATEGORY_DEFAULT,
		CATEGORY_IMPORTANT,
		CATEGORY_HEALTH,

		CATEGORY_SHOPPING,
		CATEGORY_FOOD,

		CATEGORY_GAMING,
		CATEGORY_PARTY,
		CATEGORY_PHONE,
		CATEGORY_PARTNER,

		CATEGORY_HOUSE,

		CATEGORY_SCHOOL,
		CATEGORY_WORK,

		CATEGORY_SPORT,
		CATEGORY_RUNNING,
		CATEGORY_BIKE,
		CATEGORY_SWIMMING,
		CATEGORY_FOOTBALL,
		CATEGORY_FITNESS,

		CATEGORY_CAR
	};

	/**
	 * Returns all available category values.
	 *
	 * @return category values
	 */
	public static int[] getCategoryValues()
	{
		return categories;
	}

	/**
	 * Returns string equivalent for given item {@link Category}.
	 *
	 * @param context context
	 * @return category string
	 */
	public static String getCategoryString(Context context, int category)
	{
		Resources res = context.getResources();

		int stringRes;

		switch (category)
		{
			case CATEGORY_BIKE: stringRes = R.string.content_title_bike;	break;
			case CATEGORY_CAR: stringRes = R.string.content_title_car; break;
			case CATEGORY_FITNESS: stringRes = R.string.content_title_fitness; break;
			case CATEGORY_FOOD: stringRes = R.string.content_title_food; break;
			case CATEGORY_FOOTBALL: stringRes = R.string.content_title_football; break;
			case CATEGORY_GAMING: stringRes = R.string.content_title_gaming; break;
			case CATEGORY_HEALTH: stringRes = R.string.content_title_health; break;
			case CATEGORY_HOUSE: stringRes = R.string.content_title_house; break;
			case CATEGORY_IMPORTANT: stringRes = R.string.content_title_important; break;
			case CATEGORY_PARTNER: stringRes = R.string.content_title_partner; break;
			case CATEGORY_PARTY: stringRes = R.string.content_title_party; break;
			case CATEGORY_PHONE: stringRes = R.string.content_title_phone; break;
			case CATEGORY_RUNNING: stringRes = R.string.content_title_running; break;
			case CATEGORY_SCHOOL: stringRes = R.string.content_title_school; break;
			case CATEGORY_SHOPPING: stringRes = R.string.content_title_shopping; break;
			case CATEGORY_SPORT: stringRes = R.string.content_title_sport; break;
			case CATEGORY_SWIMMING: stringRes = R.string.content_title_swimming; break;
			case CATEGORY_WORK: stringRes = R.string.content_title_work; break;

			case CATEGORY_DEFAULT:
			default: stringRes = R.string.content_title_default; break;
		}

		return res.getString(stringRes);
	}

	/**
	 * Returns drawable equivalent for given item {@link Category}.
	 *
	 * @param context context
	 * @return category drawable
	 */
	public static Drawable getCategoryDrawable(Context context, int category)
	{
		int drawableRes;

		switch (category)
		{
			case CATEGORY_BIKE:
				drawableRes = R.drawable.ic_category_bike;
				break;

			case CATEGORY_CAR:
				drawableRes = R.drawable.ic_category_car;
				break;

			case CATEGORY_FITNESS:
				drawableRes = R.drawable.ic_category_fitness;
				break;

			case CATEGORY_FOOD:
				drawableRes = R.drawable.ic_category_food;
				break;

			case CATEGORY_FOOTBALL:
				drawableRes = R.drawable.ic_category_football;
				break;

			case CATEGORY_GAMING:
				drawableRes = R.drawable.ic_category_gaming;
				break;

			case CATEGORY_HEALTH:
				drawableRes = R.drawable.ic_category_health;
				break;

			case CATEGORY_HOUSE:
				drawableRes = R.drawable.ic_category_house;
				break;

			case CATEGORY_IMPORTANT:
				drawableRes = R.drawable.ic_category_important;
				break;

			case CATEGORY_PARTNER:
				drawableRes = R.drawable.ic_category_partner;
				break;

			case CATEGORY_PARTY:
				drawableRes = R.drawable.ic_category_party;
				break;

			case CATEGORY_PHONE:
				drawableRes = R.drawable.ic_category_phone;
				break;

			case CATEGORY_RUNNING:
				drawableRes = R.drawable.ic_category_running;
				break;

			case CATEGORY_SCHOOL:
				drawableRes = R.drawable.ic_category_school;
				break;

			case CATEGORY_SHOPPING:
				drawableRes = R.drawable.ic_category_shopping;
				break;

			case CATEGORY_SPORT:
				drawableRes = R.drawable.ic_category_sport;
				break;

			case CATEGORY_SWIMMING:
				drawableRes = R.drawable.ic_category_swimming;
				break;

			case CATEGORY_WORK:
				drawableRes = R.drawable.ic_category_work;
				break;

			case CATEGORY_DEFAULT:
			default:
				drawableRes = R.drawable.ic_category_default;
		}

		return ContextCompat.getDrawable(context, drawableRes);
	}
}
