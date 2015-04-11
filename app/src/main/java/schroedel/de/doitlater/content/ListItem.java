package schroedel.de.doitlater.content;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * Item contained in to do list.
 */
public interface ListItem
{
	public int getItemType();
	public View getView(LayoutInflater inflater, View convertView);
}
