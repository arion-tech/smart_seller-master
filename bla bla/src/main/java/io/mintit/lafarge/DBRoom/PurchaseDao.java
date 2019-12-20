package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.mintit.lafarge.model.Purchase;
import io.reactivex.Flowable;

@Dao
public interface PurchaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPurchase(Purchase purchase);

    @Query("SELECT * FROM Purchase")
    Flowable<List<Purchase>> getAllPurchases();

    @Update
    void updatePurchase(Purchase purchase);
}
