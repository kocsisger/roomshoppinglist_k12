package hu.unideb.inf.roomshoppinglist.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ShoppingListDAO {
    @Insert
    public void insertListItem(ShoppingListItem sli);

    @Query("SELECT * FROM ShoppingList")
    public List<ShoppingListItem> getAllItems();
}
