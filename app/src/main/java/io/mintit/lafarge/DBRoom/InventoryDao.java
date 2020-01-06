package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.mintit.lafarge.model.Inventory;
import io.mintit.lafarge.model.InventoryArticle;
import io.reactivex.Flowable;

@Dao
public interface InventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllInventories(List<Inventory> inventories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllInventoryArticles(List<InventoryArticle> inventoryArticles);

    @Query("SELECT * FROM Inventory ")
    Flowable<List<Inventory>> getAllInventory();

    @Query("SELECT * FROM InventoryArticle INNER JOIN Product ON InventoryArticle.article = Product.productCode " +
            "WHERE InventoryArticle.codeliste = :codeList ")
    Flowable<List<InventoryArticle>> getInventoryArticle(String codeList);

    @Insert
    void insertAllInventoriesByArticle(List<InventoryArticle> inventories);

    @Update
    void updateInventory(Inventory inventory);


}

