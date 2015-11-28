package de.schroedel.doyourstuff.models.database;

import java.util.List;

/**
 * Interface to access elements of a table of {@link ToDoDatabase}.
 */
public interface DatabaseTable<T>
{
	/**
	 * Inserts element into database table.
	 *
	 * @param t element
	 * @return row ID
	 */
	long insert(T t);

	/**
	 * Removes item with ID from database table.
	 *
	 * @param id ID of item
	 * @return number of affected rows in table
	 */
	int remove(long id);

	/**
	 * Returns all items from database table.
	 *
	 * @return all items
	 */
	List<Integer> getAll();

	/**
	 * Returns an item from database table.
	 *
	 * @param id ID of item
	 * @return item
	 */
	T get(long id);

	/**
	 * Returns amount of items in database table.
	 *
	 * @return amount of items
	 */
	int getCount();
}
