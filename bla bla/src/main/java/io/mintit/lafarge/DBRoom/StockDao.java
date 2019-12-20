package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.mintit.lafarge.model.Stock;
import io.reactivex.Flowable;

@Dao
public abstract class StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertStock(List<Stock> stocks);

    @Query("SELECT * FROM Stock")
    public abstract Flowable<List<Stock>> getAllStock();

    @Query("DELETE FROM Stock")
    abstract public void deleteStock();

    @Transaction
    public void deleteAndInsertStock(List<Stock> stocks) {
        deleteStock();
        insertStock(stocks);
    }
}
