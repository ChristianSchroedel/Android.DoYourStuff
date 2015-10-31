package de.schroedel.doitlater.content;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Christian Schrödel on 10.04.15.
 *
 * Item contained in to do list.
 */
public interface ListItem
{
	int getItemType();
	View getView(LayoutInflater inflater, View convertView);
}
