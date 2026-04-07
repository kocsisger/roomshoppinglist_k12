package hu.unideb.inf.roomshoppinglist.model;

import androidx.room.Database;

@Database(entities = ShoppingListItem.class, version = 1, exportSchema = false)
public abstract class ShoppingListDatabase {
    abstract ShoppingListDAO shoppingListDAO();
}
