package de.schroedel.doyourstuff.database;

import java.util.List;

/**
 * Created by Christian Schrödel on 01.11.15.
 *
 * Database table interface.
 */
public interface DatabaseTable<T>
{
	/**
	 * Inserts element into database table.
	 *
	 * @param t - element
	 * @return - row ID
	 */
	long insert(T t);

	/**
	 * Removes item with ID from database table.
	 *
	 * @param id - ID of item
	 * @return - number of affected rows in table
	 */
	int remove(long id);

	/**
	 * Returns all items from database table.
	 *
	 * @return - all items
	 */
	List<T> getAll();

	/**
	 * Returns an item from database table.
	 *
	 * @param id - ID of item
	 * @return - item
	 */
	T get(long id);
}
