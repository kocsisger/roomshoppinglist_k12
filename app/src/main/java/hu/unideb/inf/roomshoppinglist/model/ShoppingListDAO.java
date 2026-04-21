package hu.unideb.inf.roomshoppinglist.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ShoppingListDAO {
    @Insert
    public void insertListItem(ShoppingListItem sli);

    @Query("SELECT * FROM ShoppingList")
    public LiveData<List<ShoppingListItem>> getAllItems();

    @Query("DELETE FROM ShoppingList")
    public void deleteDB();

    @Delete
    public void deleteItem(ShoppingListItem sli);
}
